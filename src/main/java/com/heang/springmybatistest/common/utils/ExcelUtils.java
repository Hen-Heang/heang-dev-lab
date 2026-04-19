package com.heang.springmybatistest.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExcelUtils — Apache POI helper (엑셀 유틸리티)
 *
 * Provides:
 *  - createWorkbook()      → XSSFWorkbook (.xlsx)
 *  - createHeaderStyle()   → blue header row style
 *  - createDataStyle()     → normal data row style
 *  - createAltStyle()      → alternating gray row style
 *  - setCell()             → write any value to a cell
 *  - autoSizeColumns()     → fit column width to content
 *  - write()               → stream workbook to HTTP response as download
 *
 * Usage pattern in every Korean admin system:
 *   Workbook wb = ExcelUtils.createWorkbook();
 *   Sheet sheet = wb.createSheet("Products");
 *   // write header + data rows
 *   ExcelUtils.write(wb, response, "products.xlsx");
 */
public class ExcelUtils {

    private ExcelUtils() {} // utility class — no instances

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ── Factory ──────────────────────────────────────────────────────────

    public static XSSFWorkbook createWorkbook() {
        return new XSSFWorkbook();
    }

    // ── Cell Styles ──────────────────────────────────────────────────────

    /**
     * Header row style (헤더 스타일)
     * Blue background, white bold text, centered, thin border
     */
    public static CellStyle createHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();

        // Background color (배경색)
        style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Font (폰트)
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);

        // Alignment (정렬)
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // Border (테두리)
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    /**
     * Normal data row style (데이터 행 스타일)
     */
    public static CellStyle createDataStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return style;
    }

    /**
     * Alternating row style — light gray background (홀수 행 스타일)
     */
    public static CellStyle createAltStyle(Workbook wb) {
        CellStyle style = createDataStyle(wb);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * Number cell style — right-aligned with comma (숫자 스타일: 우측정렬, 천단위 콤마)
     */
    public static CellStyle createNumberStyle(Workbook wb) {
        CellStyle style = createDataStyle(wb);
        style.setAlignment(HorizontalAlignment.RIGHT);
        DataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));
        return style;
    }

    // ── Cell Writers ─────────────────────────────────────────────────────

    /**
     * Write any value to a cell (셀에 값 쓰기)
     * Handles String, Number, LocalDateTime, Boolean automatically
     */
    public static Cell setCell(Row row, int col, Object value, CellStyle style) {
        Cell cell = row.createCell(col);
        if (style != null) cell.setCellStyle(style);

        if (value == null) {
            cell.setBlank();
        } else if (value instanceof String s) {
            cell.setCellValue(s);
        } else if (value instanceof Number n) {
            cell.setCellValue(n.doubleValue());
        } else if (value instanceof LocalDateTime dt) {
            cell.setCellValue(dt.format(FMT));
        } else if (value instanceof Boolean b) {
            cell.setCellValue(b);
        } else {
            cell.setCellValue(value.toString());
        }
        return cell;
    }

    /** Shorthand — set cell without explicit style */
    public static Cell setCell(Row row, int col, Object value) {
        return setCell(row, col, value, null);
    }

    // ── Sheet Helpers ─────────────────────────────────────────────────────

    /**
     * Write header row with column names (헤더 행 작성)
     * Returns the CellStyle so caller can reuse it for sub-headers.
     */
    public static void writeHeader(Sheet sheet, CellStyle headerStyle, String... columns) {
        Row header = sheet.createRow(0);
        header.setHeight((short) 500); // taller header row
        for (int i = 0; i < columns.length; i++) {
            setCell(header, i, columns[i], headerStyle);
        }
        // Freeze top row so it stays visible when scrolling (틱 행 고정)
        sheet.createFreezePane(0, 1);
    }

    /**
     * Auto-size all columns (컬럼 너비 자동 조정)
     * Call AFTER all rows are written.
     */
    public static void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            // Add a small padding (약간의 여백 추가)
            int width = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, Math.min(width + 512, 15000));
        }
    }

    // ── Output ────────────────────────────────────────────────────────────

    /**
     * Stream workbook to browser as file download (브라우저로 파일 다운로드 전송)
     *
     * Content-Disposition: attachment → browser downloads instead of opening
     * filename is URL-encoded to support Korean characters (한글 파일명 지원)
     */
    public static void write(Workbook wb, HttpServletResponse response, String filename)
            throws IOException {
        // URL-encode filename for Korean support (한글 파일명 처리)
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replace("+", "%20");

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment; filename*=UTF-8''" + encodedFilename);

        wb.write(response.getOutputStream());
        wb.close();
    }
}
