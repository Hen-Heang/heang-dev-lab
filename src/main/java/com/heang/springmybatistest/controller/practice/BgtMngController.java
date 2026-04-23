package com.heang.springmybatistest.controller.practice;

import com.heang.springmybatistest.common.utils.ExcelUtils;
import com.heang.springmybatistest.service.BgtMngService;
import com.heang.springmybatistest.vo.BgtMngInVO;
import com.heang.springmybatistest.vo.BgtMngOutVO;
import com.heang.springmybatistest.vo.BgtMngVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * BgtMngController — HTTP request handler for Budget Management (예산 관리 컨트롤러)
 *
 * Korean enterprise pattern: @Controller + @ResponseBody (NOT @RestController)
 *   @Controller  → used for MVC pages that return a view (HTML template name)
 *   @ResponseBody → added per method when that method returns JSON data instead of a view
 *   This mixed approach is common in Korean government projects (일부는 화면, 일부는 API)
 *
 * URL convention: .do suffix (Korean enterprise standard for action URLs)
 *   /budget/bgtMng             → page URL (returns HTML view)
 *   /budget/selectBgtMngList.do → API URL (returns JSON)
 *
 * @RequiredArgsConstructor (Lombok) → injects BgtMngService via constructor
 */
@Controller
@RequiredArgsConstructor
public class BgtMngController {

    private final BgtMngService bgtMngService;

    /**
     * Page URL — renders the budget management HTML page (화면 렌더링)
     * Returns the Thymeleaf template path: src/templates/budget/bgtMng.html
     * No data is passed to the model — all data is loaded via AJAX after page load.
     */
    @RequestMapping("/budget/bgtMng")
    public String bgtMng(ModelMap model) throws Exception {
        return "budget/bgtMng";
    }

    /**
     * AJAX — fetch paginated budget list + total count (목록 조회)
     *
     * @RequestBody BgtMngInVO inVO
     *   → Spring reads the JSON body from the POST request and binds it to BgtMngInVO
     *   → Requires contentType: 'application/json' on the JS side
     *
     * Returns Map<String, Object> with:
     *   { "list": [...], "totalCount": 25 }
     * JS accesses it as: data.list, data.totalCount
     */
    @ResponseBody
    @RequestMapping("/budget/selectBgtMngList.do")
    public Map<String, Object> selectBgtMngList(
            @RequestBody BgtMngInVO inVO) throws Exception {
        return bgtMngService.selectBgtMngList(inVO);
    }

    /**
     * AJAX — fetch one record's full detail for the edit popup (상세 조회)
     * Only needs inVO.id — other fields in inVO are ignored here.
     * Returns BgtMngVO as JSON → JS fills popup fields with the values.
     */
    @ResponseBody
    @RequestMapping("/budget/selectBgtMngDetail.do")
    public BgtMngVO selectBgtMngDetail(
            @RequestBody BgtMngInVO inVO) throws Exception {
        return bgtMngService.selectBgtMngDetail(inVO);
    }

    /**
     * AJAX — insert new budget record (등록)
     * Returns plain string "success" → JS checks for this in the success callback.
     */
    @ResponseBody
    @RequestMapping("/budget/insertBgtMng.do")
    public String insertBgtMng(
            @RequestBody BgtMngInVO inVO) throws Exception {
        bgtMngService.insertBgtMng(inVO);
        return "success";
    }

    /**
     * AJAX — update existing budget record (수정)
     * inVO.id identifies which record to update.
     */
    @ResponseBody
    @RequestMapping("/budget/updateBgtMng.do")
    public String updateBgtMng(
            @RequestBody BgtMngInVO inVO) throws Exception {
        bgtMngService.updateBgtMng(inVO);
        return "success";
    }

    /**
     * AJAX — soft delete budget record (논리삭제)
     * Sets del_yn = 'Y' in DB — does NOT physically remove the row.
     * Only inVO.id is used.
     */
    @ResponseBody
    @RequestMapping("/budget/deleteBgtMng.do")
    public String deleteBgtMng(
            @RequestBody BgtMngInVO inVO) throws Exception {
        bgtMngService.deleteBgtMng(inVO);
        return "success";
    }

    /**
     * Excel download — streams .xlsx file to the browser (엑셀 다운로드)
     *
     * Why NOT @ResponseBody and NOT AJAX?
     *   This method writes directly to HttpServletResponse (the raw HTTP response stream).
     *   ExcelUtils.write() sets the Content-Disposition: attachment header
     *   → browser interprets this as a file download and opens the Save dialog.
     *   AJAX cannot trigger a file Save dialog, so JS uses location.href instead.
     *
     * Why BgtMngInVO inVO (no @RequestBody)?
     *   location.href sends a GET request with query string parameters (?sidoCd=41&...)
     *   Spring binds query string params directly to the VO fields (no JSON body needed).
     *
     * Excel style follows the project's ExcelUtils pattern:
     *   Blue header row, alternating grey rows, comma number format, auto-sized columns
     */
    @RequestMapping("/budget/downloadBgtMngExcel.do")
    public void downloadBgtMngExcel(BgtMngInVO inVO,
                                    HttpServletResponse response) throws Exception {

        // Fetch all matching records (no pagination — export full result)
        List<BgtMngOutVO> list = bgtMngService.selectBgtMngList(inVO)
                .entrySet().stream()
                .filter(e -> e.getKey().equals("list"))
                .map(e -> (List<BgtMngOutVO>) e.getValue())
                .findFirst().orElse(List.of());

        XSSFWorkbook wb    = ExcelUtils.createWorkbook();
        Sheet        sheet = wb.createSheet("예산관리");

        // Header style: blue background (프로젝트 공통 스타일)
        CellStyle headerStyle = ExcelUtils.createHeaderStyle(wb);

        // Data styles: normal rows and alternating grey rows
        CellStyle dataStyle   = ExcelUtils.createDataStyle(wb);
        CellStyle altStyle    = ExcelUtils.createAltStyle(wb);

        // Number style: comma format (1,000,000) for budget amount columns
        CellStyle numStyle    = ExcelUtils.createNumberStyle(wb);

        // Alternating number style: same comma format but with grey background
        CellStyle numAltStyle = ExcelUtils.createNumberStyle(wb);
        numAltStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        numAltStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Write header row (행 0)
        ExcelUtils.writeHeader(sheet, headerStyle,
                "순번", "지자체", "시작월", "종료월", "국비(A)", "지방비(B)", "예산합계(A+B)");

        // Write data rows (행 1~N)
        for (int i = 0; i < list.size(); i++) {
            BgtMngOutVO row     = list.get(i);
            Row         dataRow = sheet.createRow(i + 1);
            dataRow.setHeight((short) 400);

            // Alternating row color: even rows = white, odd rows = light grey
            boolean    isAlt  = (i % 2 == 1);
            CellStyle  cs     = isAlt ? altStyle    : dataStyle;
            CellStyle  numCs  = isAlt ? numAltStyle : numStyle;

            ExcelUtils.setCell(dataRow, 0, row.getRn(),        cs);
            ExcelUtils.setCell(dataRow, 1, row.getJijacheNm(), cs);
            ExcelUtils.setCell(dataRow, 2, row.getStartMm(),   cs);
            ExcelUtils.setCell(dataRow, 3, row.getEndMm(),     cs);
            ExcelUtils.setCell(dataRow, 4, row.getNtnlBgt(),   numCs); // number format
            ExcelUtils.setCell(dataRow, 5, row.getLclBgt(),    numCs); // number format
            ExcelUtils.setCell(dataRow, 6, row.getBgtSum(),    numCs); // number format
        }

        ExcelUtils.autoSizeColumns(sheet, 7); // auto-fit all 7 columns
        ExcelUtils.write(wb, response, "예산관리.xlsx"); // stream file to browser
    }
}
