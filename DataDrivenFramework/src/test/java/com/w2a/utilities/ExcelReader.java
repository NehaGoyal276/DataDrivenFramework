package com.w2a.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    public String path;
    public FileInputStream fis = null;
    public FileOutputStream fos = null;

    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row = null;
    private XSSFCell cell = null;

    DataFormatter formatter = new DataFormatter();

    // Constructor
    public ExcelReader(String path) {

        this.path = path;

        try {

            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            fis.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // Get row count
    public int getRowCount(String sheetName) {

        sheet = workbook.getSheet(sheetName);

        if (sheet == null)
            return 0;

        return sheet.getLastRowNum() + 1;
    }

    // Get column count
    public int getColumnCount(String sheetName) {

        sheet = workbook.getSheet(sheetName);

        if (sheet == null)
            return 0;

        row = sheet.getRow(0);

        if (row == null)
            return 0;

        return row.getLastCellNum();
    }

    // Get cell data (MODERN POI 5.x way)
    public String getCellData(String sheetName, int colNum, int rowNum) {

        try {

            sheet = workbook.getSheet(sheetName);

            if (sheet == null)
                return "";

            row = sheet.getRow(rowNum - 1);

            if (row == null)
                return "";

            cell = row.getCell(colNum);

            if (cell == null)
                return "";

            CellType cellType = cell.getCellType();

            switch (cellType) {

                case STRING:
                    return cell.getStringCellValue();

                case NUMERIC:

                    if (DateUtil.isCellDateFormatted(cell)) {

                        LocalDate date = cell.getLocalDateTimeCellValue().toLocalDate();
                        return date.toString();

                    } else {

                        return formatter.formatCellValue(cell);
                    }

                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());

                case FORMULA:
                    return formatter.formatCellValue(cell);

                case BLANK:
                    return "";

                default:
                    return formatter.formatCellValue(cell);
            }

        } catch (Exception e) {

            e.printStackTrace();
            return "Invalid Data";
        }
    }

    // Set cell data
    public boolean setCellData(String sheetName, String colName, int rowNum, String data) {

        try {

            sheet = workbook.getSheet(sheetName);

            if (sheet == null)
                return false;

            row = sheet.getRow(0);

            int colNum = -1;

            for (int i = 0; i < row.getLastCellNum(); i++) {

                if (row.getCell(i).getStringCellValue().trim().equals(colName)) {

                    colNum = i;
                    break;
                }
            }

            row = sheet.getRow(rowNum - 1);

            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);

            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);

            fos = new FileOutputStream(path);

            workbook.write(fos);

            fos.close();

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Check sheet exists
    public boolean isSheetExist(String sheetName) {

        return workbook.getSheet(sheetName) != null;
    }
}