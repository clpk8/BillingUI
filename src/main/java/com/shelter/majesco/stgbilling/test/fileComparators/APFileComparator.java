package com.shelter.majesco.stgbilling.test.fileComparators;

import com.shelter.majesco.stgbilling.test.output.modifier.ExcelPicker;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xerces.xs.XSFacet;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class APFileComparator {


    public static void main(String[] args) {
        compareTwoExcel();
    }
    public static void compareTwoExcel(){

        ExcelPicker excelPicker = new ExcelPicker();
        File file1 = excelPicker.fileChooser();
        File file2 = excelPicker.fileChooser();
        List<AccountsPayable> expectedAPList= new ArrayList<AccountsPayable>();
        List<AccountsPayable> resultAPList= new ArrayList<AccountsPayable>();

        try {
            FileInputStream fis1 =  new FileInputStream(file1);
            FileInputStream fis2 =  new FileInputStream(file2);


            XSSFWorkbook workbook1 = new XSSFWorkbook(fis1);
            XSSFWorkbook workbook2 = new XSSFWorkbook(fis2);



            // Create a CellStyle with the font
            Font missCompairFont = workbook2.createFont();
            missCompairFont.setColor(IndexedColors.RED.getIndex());
            CellStyle missCompairCellStyle = workbook2.createCellStyle();
            missCompairCellStyle.setFont(missCompairFont);


            XSSFSheet sheet1 = workbook1.getSheetAt(0);
            XSSFSheet sheet2 = workbook2.getSheetAt(0);

            Iterator<Row> rowIterator1 = sheet1.rowIterator();
            Iterator<Row> rowIterator2 = sheet2.rowIterator();

            int colCount = 0;
            int rowCount = 0;
            while(rowIterator1.hasNext() && rowIterator2.hasNext()){
                Row row1 = rowIterator1.next();
                Row row2 = rowIterator2.next();

                AccountsPayable expectedAccountPayable = new AccountsPayable();
                AccountsPayable resultAccountPayable = new AccountsPayable();


                Iterator<Cell> cellIterator1 = row1.cellIterator();
                Iterator<Cell> cellIterator2 = row2.cellIterator();

                while (cellIterator1.hasNext() && cellIterator2.hasNext()){
                    Cell cell1 = cellIterator1.next();
                    Cell cell2 = cellIterator2.next();
                    if (rowCount != 0){
                        switch (colCount){
                            case 0:
                                expectedAccountPayable.AP_DISB_TYPE = cell1.toString();
                                resultAccountPayable.AP_DISB_TYPE = cell2.toString();
                            case 1:
                                expectedAccountPayable.AP_ACCT_YYYYMM = cell1.toString();
                                resultAccountPayable.AP_ACCT_YYYYMM = cell2.toString();
                            case 2:
                                expectedAccountPayable.AP_TAX_ID = cell1.toString();
                                resultAccountPayable.AP_TAX_ID = cell2.toString();
                            case 3:
                                expectedAccountPayable.AP_INVOICE_NO = cell1.toString();
                                resultAccountPayable.AP_INVOICE_NO = cell2.toString();
                            case 4:
                                expectedAccountPayable.AP_BROKER_NO = cell1.toString();
                                resultAccountPayable.AP_BROKER_NO = cell2.toString();
                            case 5:
                                expectedAccountPayable.AP_VENDER_NO = cell1.toString();
                                resultAccountPayable.AP_VENDER_NO = cell2.toString();
                            case 6:
                                expectedAccountPayable.AP_ACCOUNT_NO = cell1.toString();
                                resultAccountPayable.AP_ACCOUNT_NO = cell2.toString();
                            case 7:
                                expectedAccountPayable.AP_POLICY_NO = cell1.toString();
                                resultAccountPayable.AP_POLICY_NO = cell2.toString();
                            case 8:
                                expectedAccountPayable.AP_POLICY_EFF_DT = cell1.toString();
                                resultAccountPayable.AP_POLICY_EFF_DT = cell2.toString();
                            case 9:
                                expectedAccountPayable.AP_INSURED_NAME = cell1.toString();
                                resultAccountPayable.AP_INSURED_NAME = cell2.toString();
                            case 10:
                                expectedAccountPayable.AP_PAYEE_TYPE = cell1.toString();
                                resultAccountPayable.AP_PAYEE_TYPE = cell2.toString();
                            case 11:
                                expectedAccountPayable.AP_PAYEE_NAME = cell1.toString();
                                resultAccountPayable.AP_PAYEE_NAME = cell2.toString();
                            case 12:
                                expectedAccountPayable.AP_PAYEE_ADDR_1 = cell1.toString();
                                resultAccountPayable.AP_PAYEE_ADDR_1 = cell2.toString();
                            case 13:
                                expectedAccountPayable.AP_PAYEE_ADDR_2 = cell1.toString();
                                resultAccountPayable.AP_PAYEE_ADDR_2= cell2.toString();
                            case 14:
                                expectedAccountPayable.AP_PAYEE_CITY = cell1.toString();
                                resultAccountPayable.AP_PAYEE_CITY = cell2.toString();
                            case 15:
                                expectedAccountPayable.AP_PAYEE_STATE = cell1.toString();
                                resultAccountPayable.AP_PAYEE_STATE = cell2.toString();
                            case 16:
                                expectedAccountPayable.AP_PAYEE_ZIP_CODE = cell1.toString();
                                resultAccountPayable.AP_PAYEE_ZIP_CODE = cell2.toString();
                            case 17:
                                expectedAccountPayable.AP_PAYEE_COUNTRY_CD = cell1.toString();
                                resultAccountPayable.AP_PAYEE_COUNTRY_CD = cell2.toString();
                            case 18:
                                expectedAccountPayable.AP_AMOUNT = cell1.toString();
                                resultAccountPayable.AP_AMOUNT = cell2.toString();
                            case 19:
                                expectedAccountPayable.AP_PAYMENT_DATE = cell1.toString();
                                resultAccountPayable.AP_PAYMENT_DATE = cell2.toString();
                            case 20:
                                expectedAccountPayable.AP_BANK_CODE = cell1.toString();
                                resultAccountPayable.AP_BANK_CODE = cell2.toString();
                            case 21:
                                expectedAccountPayable.AP_CHECK_NO = cell1.toString();
                                resultAccountPayable.AP_CHECK_NO = cell2.toString();
                            case 22:
                                expectedAccountPayable.AP_ORIG_CHECK_NO = cell1.toString();
                                resultAccountPayable.AP_ORIG_CHECK_NO = cell2.toString();
                            case 23:
                                expectedAccountPayable.AP_REFUND_METHOD = cell1.toString();
                                resultAccountPayable.AP_REFUND_METHOD = cell2.toString();
                            case 24:
                                expectedAccountPayable.AP_REFUND_REASON = cell1.toString();
                                resultAccountPayable.AP_REFUND_REASON = cell2.toString();
                            case 25:
                                expectedAccountPayable.AP_REFUND_DESC = cell1.toString();
                                resultAccountPayable.AP_REFUND_DESC = cell2.toString();
                            case 26:
                                expectedAccountPayable.AP_CLIENT_ID = cell1.toString();
                                resultAccountPayable.AP_CLIENT_ID= cell2.toString();
                            case 27:
                                expectedAccountPayable.AP_USER_ID = cell1.toString();
                                resultAccountPayable.AP_USER_ID = cell2.toString();
                            case 28:
                                expectedAccountPayable.AP_SOURCE_POLICY_ID = cell1.toString();
                                resultAccountPayable.AP_SOURCE_POLICY_ID = cell2.toString();
                            case 29:
                                expectedAccountPayable.AP_MAILING_NAME = cell1.toString();
                                resultAccountPayable.AP_MAILING_NAME = cell2.toString();
                        }
                    }
                    colCount++;
                }
                expectedAPList.add(expectedAccountPayable);
                resultAPList.add(resultAccountPayable);
                colCount = 0;
                rowCount++;
            }

            System.out.println("Done");
            writeDifferences(expectedAPList, resultAPList);
            workbook1.close();
            workbook2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void writeDifferences(List<AccountsPayable> expectedAPList, List<AccountsPayable> resultAPList) throws IOException {
       int sizeOfExpected = expectedAPList.size();
       int sizeOfResult = resultAPList.size();
       List<AccountsPayableMissCompair> accountsPayableMissCompairList = new ArrayList<AccountsPayableMissCompair>();

       System.out.println("The size of expected is " + sizeOfExpected + " The size of result is " + sizeOfResult);
       if (sizeOfExpected == sizeOfResult){
           AccountsPayableMissCompair accountsPayableMissCompair = new AccountsPayableMissCompair();
           for (int i = 1; i < sizeOfExpected; i++){
               if (!expectedAPList.get(i).AP_INVOICE_NO.equals(resultAPList.get(i).AP_INVOICE_NO)){
                    accountsPayableMissCompair.Expected = expectedAPList.get(i).AP_INVOICE_NO;
                    accountsPayableMissCompair.Actual = resultAPList.get(i).AP_INVOICE_NO;
                    accountsPayableMissCompair.policyNumber = expectedAPList.get(i).getMaskedPolicyNumber();
                    accountsPayableMissCompair.Column = "AP_INVOCIE_NO";
                    accountsPayableMissCompairList.add(accountsPayableMissCompair);

               }
               else if (!expectedAPList.get(i).AP_DISB_TYPE.equals(resultAPList.get(i).AP_DISB_TYPE)){
                   accountsPayableMissCompair.Expected = expectedAPList.get(i).AP_DISB_TYPE;
                   accountsPayableMissCompair.Actual = resultAPList.get(i).AP_DISB_TYPE;
                   accountsPayableMissCompair.policyNumber = expectedAPList.get(i).getMaskedPolicyNumber();
                   accountsPayableMissCompair.Column = "AP_DISB_TYPE";
                   accountsPayableMissCompairList.add(accountsPayableMissCompair);

               }
               else if (!expectedAPList.get(i).AP_ACCT_YYYYMM.equals(resultAPList.get(i).AP_ACCT_YYYYMM)){
                   accountsPayableMissCompair.Expected = expectedAPList.get(i).AP_ACCT_YYYYMM;
                   accountsPayableMissCompair.Actual = resultAPList.get(i).AP_ACCT_YYYYMM;
                   accountsPayableMissCompair.policyNumber = expectedAPList.get(i).getMaskedPolicyNumber();
                   accountsPayableMissCompair.Column = "AP_ACCT_YYYYMM";
                   accountsPayableMissCompairList.add(accountsPayableMissCompair);

               }
               else if (!expectedAPList.get(i).AP_TAX_ID.equals(resultAPList.get(i).AP_TAX_ID)){
                   accountsPayableMissCompair.Expected = expectedAPList.get(i).AP_TAX_ID;
                   accountsPayableMissCompair.Actual = resultAPList.get(i).AP_TAX_ID;
                   accountsPayableMissCompair.policyNumber = expectedAPList.get(i).getMaskedPolicyNumber();
                   accountsPayableMissCompair.Column = "AP_TAX_ID";
                   accountsPayableMissCompairList.add(accountsPayableMissCompair);

               }
           }

       }

        String[] columns = {"Policy Number", "Column", "Expected Value", "Actual Value", "Situation"};
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

        int rowNum = 1;

        for (AccountsPayableMissCompair accountsPayableMissCompair : accountsPayableMissCompairList){
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(accountsPayableMissCompair.policyNumber);
            row.createCell(1).setCellValue(accountsPayableMissCompair.Column);
            row.createCell(2).setCellValue(accountsPayableMissCompair.Expected);
            row.createCell(3).setCellValue(accountsPayableMissCompair.Actual);
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        String fileName = new SimpleDateFormat("'AP_COMP_Output_Data_'yyyMMddHHmm'.xlsx'").format(new Date());
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }



}
