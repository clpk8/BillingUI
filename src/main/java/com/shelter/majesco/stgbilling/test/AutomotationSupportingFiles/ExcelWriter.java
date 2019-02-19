package com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExcelWriter {

    private static List<Data> listData = new ArrayList<>();
    public static List<WrongData> wrongDataList = new ArrayList<>();
    private static String[] columns = {"Policy Number", "Expected Value", "Actual Value", "Situation"};

    //Write the results to a excel file
    public static void writeExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("UI Output Data");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        // Create Other rows and cells with employees data
        int rowNum = 1;

        for (WrongData wrongData : wrongDataList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(wrongData.policyNumber);

            row.createCell(1)
                    .setCellValue(wrongData.oldValue);

            row.createCell(2)
                    .setCellValue(wrongData.newValue);
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        String fileName = new SimpleDateFormat("'UI_Output_Data_'yyyMMddHHmm'.xlsx'").format(new Date());
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }

}
