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

@Controller
@RequiredArgsConstructor
public class BgtMngController {

    private final BgtMngService bgtMngService;

    @RequestMapping("/budget/bgtMng")
    public String bgtMng(ModelMap model) throws Exception {
        return "budget/bgtMng";
    }

    @ResponseBody
    @RequestMapping("/budget/selectBgtMngList.do")
    public List<BgtMngOutVO> selectBgtMngList(
            @RequestBody BgtMngInVO inVO) throws Exception {
        return bgtMngService.selectBgtMngList(inVO);
    }

    @ResponseBody
    @RequestMapping("/budget/selectBgtMngDetail.do")
    public BgtMngVO selectBgtMngDetail(
            @RequestBody BgtMngInVO inVO) throws Exception {
        return bgtMngService.selectBgtMngDetail(inVO);
    }

    @ResponseBody
    @RequestMapping("/budget/insertBgtMng.do")
    public String insertBgtMng(
            @RequestBody BgtMngInVO inVO) throws Exception {
        bgtMngService.insertBgtMng(inVO);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/budget/updateBgtMng.do")
    public String updateBgtMng(
            @RequestBody BgtMngInVO inVO) throws Exception {
        bgtMngService.updateBgtMng(inVO);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/budget/deleteBgtMng.do")
    public String deleteBgtMng(
            @RequestBody BgtMngInVO inVO) throws Exception {
        bgtMngService.deleteBgtMng(inVO);
        return "success";
    }

    // ── Excel download (엑셀 다운로드) ──────────────────────────────
    @RequestMapping("/budget/downloadBgtMngExcel.do")
    public void downloadBgtMngExcel(BgtMngInVO inVO,
                                    HttpServletResponse response) throws Exception {

        List<BgtMngOutVO> list = bgtMngService.selectBgtMngList(inVO);

        XSSFWorkbook wb  = ExcelUtils.createWorkbook();
        Sheet        sheet = wb.createSheet("예산관리");

        CellStyle headerStyle = ExcelUtils.createHeaderStyle(wb);
        CellStyle dataStyle   = ExcelUtils.createDataStyle(wb);
        CellStyle altStyle    = ExcelUtils.createAltStyle(wb);
        CellStyle numStyle    = ExcelUtils.createNumberStyle(wb);

        CellStyle numAltStyle = ExcelUtils.createNumberStyle(wb);
        numAltStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        numAltStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        ExcelUtils.writeHeader(sheet, headerStyle,
                "순번", "지자체", "시작월", "종료월", "국비(A)", "지방비(B)", "예산합계(A+B)");

        for (int i = 0; i < list.size(); i++) {
            BgtMngOutVO row = list.get(i);
            Row dataRow = sheet.createRow(i + 1);
            dataRow.setHeight((short) 400);

            boolean isAlt = (i % 2 == 1);
            CellStyle cs    = isAlt ? altStyle    : dataStyle;
            CellStyle numCs = isAlt ? numAltStyle : numStyle;

            ExcelUtils.setCell(dataRow, 0, row.getRn(),         cs);
            ExcelUtils.setCell(dataRow, 1, row.getJijacheNm(),  cs);
            ExcelUtils.setCell(dataRow, 2, row.getStartMm(),    cs);
            ExcelUtils.setCell(dataRow, 3, row.getEndMm(),      cs);
            ExcelUtils.setCell(dataRow, 4, row.getNtnlBgt(),    numCs);
            ExcelUtils.setCell(dataRow, 5, row.getLclBgt(),     numCs);
            ExcelUtils.setCell(dataRow, 6, row.getBgtSum(),     numCs);
        }

        ExcelUtils.autoSizeColumns(sheet, 7);
        ExcelUtils.write(wb, response, "예산관리.xlsx");
    }
}
