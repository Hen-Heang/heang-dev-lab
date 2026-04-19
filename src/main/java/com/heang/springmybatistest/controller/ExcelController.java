package com.heang.springmybatistest.controller;

import com.heang.springmybatistest.common.utils.ExcelUtils;
import com.heang.springmybatistest.dto.ProductSearchRequest;
import com.heang.springmybatistest.mapper.UserMapper;
import com.heang.springmybatistest.model.Product;
import com.heang.springmybatistest.model.Users;
import com.heang.springmybatistest.service.BoardService;
import com.heang.springmybatistest.service.ProductService;
import com.heang.springmybatistest.vo.BoardVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * ExcelController — Excel download endpoints (엑셀 다운로드 컨트롤러)
 *
 * Korean enterprise pattern:
 *   Every list page has an "Excel Download" button.
 *   This controller handles all Excel exports in one place.
 *
 * URLs:
 *   GET /excel/products  → products list .xlsx
 *   GET /excel/board     → board posts list .xlsx
 *   GET /excel/users     → users list .xlsx
 *
 * Pattern:
 *   1. Load data from service (서비스에서 데이터 조회)
 *   2. Create Workbook + Sheet (워크북 + 시트 생성)
 *   3. Write header row (헤더 행 작성)
 *   4. Write data rows (데이터 행 작성)
 *   5. Auto-size columns (컬럼 너비 자동 조정)
 *   6. Stream to response (응답으로 전송)
 */
@Controller
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ProductService productService;
    private final BoardService   boardService;
    private final UserMapper     userMapper;  // direct mapper for full user fields

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    // ── Products ──────────────────────────────────────────────────────────

    /**
     * GET /excel/products
     * Download all products as Excel (상품 목록 엑셀 다운로드)
     */
    @GetMapping("/products")
    public void downloadProducts(HttpServletResponse response) throws IOException {

        // 1. Load data (데이터 조회)
        List<Product> products = productService.search(new ProductSearchRequest());

        // 2. Create workbook and sheet (워크북 + 시트 생성)
        XSSFWorkbook wb = ExcelUtils.createWorkbook();
        Sheet sheet = wb.createSheet("Products");

        // 3. Prepare styles (스타일 준비)
        CellStyle headerStyle = ExcelUtils.createHeaderStyle(wb);
        CellStyle dataStyle   = ExcelUtils.createDataStyle(wb);
        CellStyle altStyle    = ExcelUtils.createAltStyle(wb);
        CellStyle numStyle    = ExcelUtils.createNumberStyle(wb);

        // Alternating number styles (홀짝 행 숫자 스타일)
        CellStyle numAltStyle = ExcelUtils.createNumberStyle(wb);
        numAltStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        numAltStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 4. Write header row (헤더 행 작성)
        // Korean column names — real Korean projects use Korean headers
        ExcelUtils.writeHeader(sheet, headerStyle,
                "No", "ID", "상품명", "카테고리", "가격(₩)", "재고", "등록일");

        // 5. Write data rows (데이터 행 작성)
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            Row row = sheet.createRow(i + 1); // row 0 = header
            row.setHeight((short) 400);

            // Alternating row style (홀짝 행 색상 교체)
            boolean isAlt = (i % 2 == 1);
            CellStyle cs    = isAlt ? altStyle    : dataStyle;
            CellStyle numCs = isAlt ? numAltStyle : numStyle;

            ExcelUtils.setCell(row, 0, i + 1,               cs);     // No
            ExcelUtils.setCell(row, 1, p.getId(),            cs);     // ID
            ExcelUtils.setCell(row, 2, p.getName(),          cs);     // 상품명
            ExcelUtils.setCell(row, 3, p.getCategoryName(),  cs);     // 카테고리
            ExcelUtils.setCell(row, 4, p.getPrice(),         numCs);  // 가격 (right-aligned, comma)
            ExcelUtils.setCell(row, 5, p.getStock(),         numCs);  // 재고
            ExcelUtils.setCell(row, 6, p.getCreatedAt(),     cs);     // 등록일
        }

        // 6. Auto-size columns (컬럼 너비 자동 조정)
        ExcelUtils.autoSizeColumns(sheet, 7);

        // 7. Stream to browser (브라우저로 전송)
        String filename = "products_" + LocalDateTime.now().format(DATE_FMT) + ".xlsx";
        ExcelUtils.write(wb, response, filename);
    }

    // ── Board ─────────────────────────────────────────────────────────────

    /**
     * GET /excel/board
     * Download board post list as Excel (게시판 목록 엑셀 다운로드)
     */
    @GetMapping("/board")
    public void downloadBoard(HttpServletResponse response) throws IOException {

        List<BoardVO> boards = boardService.findAll();

        XSSFWorkbook wb = ExcelUtils.createWorkbook();
        Sheet sheet = wb.createSheet("Board");

        CellStyle headerStyle = ExcelUtils.createHeaderStyle(wb);
        CellStyle dataStyle   = ExcelUtils.createDataStyle(wb);
        CellStyle altStyle    = ExcelUtils.createAltStyle(wb);

        ExcelUtils.writeHeader(sheet, headerStyle,
                "No", "번호(SN)", "제목", "상태", "등록일시");

        for (int i = 0; i < boards.size(); i++) {
            BoardVO b = boards.get(i);
            Row row = sheet.createRow(i + 1);
            row.setHeight((short) 400);

            CellStyle cs = (i % 2 == 1) ? altStyle : dataStyle;

            ExcelUtils.setCell(row, 0, i + 1,                                cs);
            ExcelUtils.setCell(row, 1, b.getBoardSn(),                        cs);
            ExcelUtils.setCell(row, 2, b.getBoardTitle(),                     cs);
            ExcelUtils.setCell(row, 3, "Y".equals(b.getUseYn()) ? "활성" : "비활성", cs);
            ExcelUtils.setCell(row, 4, b.getDataRegDt(),                      cs);
        }

        ExcelUtils.autoSizeColumns(sheet, 5);

        String filename = "board_" + LocalDateTime.now().format(DATE_FMT) + ".xlsx";
        ExcelUtils.write(wb, response, filename);
    }

    // ── Users ─────────────────────────────────────────────────────────────

    /**
     * GET /excel/users
     * Download user list as Excel (사용자 목록 엑셀 다운로드)
     */
    @GetMapping("/users")
    public void downloadUsers(HttpServletResponse response) throws IOException {

        // Use mapper directly to get all fields (name, phone, role, status)
        // UserService.searchUsers() returns UserResponse which lacks name/phone/role
        List<Users> users = userMapper.selectUserList();

        XSSFWorkbook wb = ExcelUtils.createWorkbook();
        Sheet sheet = wb.createSheet("Users");

        CellStyle headerStyle = ExcelUtils.createHeaderStyle(wb);
        CellStyle dataStyle   = ExcelUtils.createDataStyle(wb);
        CellStyle altStyle    = ExcelUtils.createAltStyle(wb);

        ExcelUtils.writeHeader(sheet, headerStyle,
                "No", "ID", "아이디", "이메일", "이름", "전화번호", "권한", "상태", "가입일");

        for (int i = 0; i < users.size(); i++) {
            Users u = users.get(i);
            Row row = sheet.createRow(i + 1);
            row.setHeight((short) 400);

            CellStyle cs = (i % 2 == 1) ? altStyle : dataStyle;

            ExcelUtils.setCell(row, 0, i + 1,         cs);
            ExcelUtils.setCell(row, 1, u.getId(),      cs);
            ExcelUtils.setCell(row, 2, u.getUsername(), cs);
            ExcelUtils.setCell(row, 3, u.getEmail(),   cs);
            ExcelUtils.setCell(row, 4, u.getName(),    cs);
            ExcelUtils.setCell(row, 5, u.getPhone(),   cs);
            ExcelUtils.setCell(row, 6, u.getRole(),    cs);
            ExcelUtils.setCell(row, 7, u.getStatus(),  cs);
            ExcelUtils.setCell(row, 8, u.getCreatedAt(), cs);
        }

        ExcelUtils.autoSizeColumns(sheet, 9);

        String filename = "users_" + LocalDateTime.now().format(DATE_FMT) + ".xlsx";
        ExcelUtils.write(wb, response, filename);
    }
}
