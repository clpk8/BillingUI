package com.shelter.majesco.stgbilling.test.fileComparators;


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xpath.operations.Bool;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class APFileConvertor {

    public static List<AccountsPayable> accountsPayableList;
    public static String policyPrefix;
    public static Boolean m81Installed = false;
    public static void main(String[] args) throws IOException{
        accountsPayableList = new ArrayList<AccountsPayable>();

        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter the prefix of the policy, for example: 90109");
        policyPrefix = reader.nextLine();
        System.out.println("Here is all the AP records found");
        reader.close();

        folderChooser();
        for (AccountsPayable accountsPayable : accountsPayableList){
            accountsPayable.print();
        }
        Collections.sort(accountsPayableList, new AccountsPayableComparator());
        generateExcelFromTxt();
    }

    public static void folderChooser(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            for (File file : files) {
                System.out.println("You've chosen " + file.getName());
                parseAPFile(file);
            }
        }
    }
    private static void parseAPFile(File file){

        StringBuilder contentBuilder = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            System.out.println("Reading file using Buffered Reader");
            while ((line = br.readLine()) != null){
                if (line.length() == 51){
                    //System.out.println(line);
                }
                else{
                    AccountsPayable accountsPayable = populateAP(line);
                    if (accountsPayable.checkAPByPolicyNumber(policyPrefix)){
                        accountsPayableList.add(accountsPayable);
                    }
                }

            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private static AccountsPayable populateAP(String line){
        AccountsPayable accountsPayable = new AccountsPayable();
        if (m81Installed){
            accountsPayable.AP_INVOICE_NO = line.substring(         2,	24).trim();
            accountsPayable.AP_DISB_TYPE = line.substring(          24,	34).trim();
            accountsPayable.AP_ACCT_YYYYMM = line.substring(        34,	40).trim();
            accountsPayable.AP_TAX_ID = line.substring(             40,	65).trim();
            accountsPayable.AP_BROKER_NO = line.substring(          65,	85).trim();
            accountsPayable.AP_VENDER_NO = line.substring(          85,	105).trim();
            accountsPayable.AP_ACCOUNT_NO = line.substring(         105,	125).trim();
            accountsPayable.AP_POLICY_NO = line.substring(          125,	175).trim();
            accountsPayable.AP_POLICY_EFF_DT = line.substring(      175,	185).trim();
            accountsPayable.AP_INSURED_NAME = line.substring(       185,	365).trim();
            accountsPayable.AP_PAYEE_TYPE = line.substring(         365,	395).trim();
            accountsPayable.AP_PAYEE_NAME = line.substring(         395,	575).trim();
            accountsPayable.AP_PAYEE_ADDR_1 = line.substring(       575,	615).trim();
            accountsPayable.AP_PAYEE_ADDR_2 = line.substring(       615,	655).trim();
            accountsPayable.AP_PAYEE_CITY = line.substring(         655,	705).trim();
            accountsPayable.AP_PAYEE_STATE = line.substring(        705,	710).trim();
            accountsPayable.AP_PAYEE_ZIP_CODE = line.substring(     710,	725).trim();
            accountsPayable.AP_PAYEE_COUNTRY_CD = line.substring(   725,	785).trim();

            accountsPayable.AP_AMOUNT = line.substring(             785,	807).trim();
            //accountsPayable.AP_AMOUNT = StringUtils.stripStart(accountsPayable.AP_AMOUNT, "0")

            accountsPayable.AP_PAYMENT_DATE = line.substring(       807,	817).trim();
            accountsPayable.AP_BANK_CODE = line.substring(          817,	847).trim();
            accountsPayable.AP_CHECK_NO = line.substring(           847,	867).trim();
            accountsPayable.AP_ORIG_CHECK_NO = line.substring(      867,	889).trim();
            accountsPayable.AP_REFUND_METHOD = line.substring(      889,	899).trim();
            accountsPayable.AP_REFUND_REASON = line.substring(      899,	999).trim();
            accountsPayable.AP_REFUND_DESC = line.substring(        999,	1999).trim();
            accountsPayable.AP_SUPRESS_CHECK_YN = line.substring(   1999,	2000).trim();
            accountsPayable.AP_CLIENT_ID = line.substring(          2000,   2020).trim();
            accountsPayable.AP_USER_ID = line.substring(            2020,   2040).trim();
            accountsPayable.AP_SOURCE_POLICY_ID = line.substring(   2040,   2070).trim();
            accountsPayable.AP_MAILING_NAME = line.substring(       2070,   2250);

        }
        else{
            accountsPayable.AP_INVOICE_NO = line.substring(         2,	24).trim();
            accountsPayable.AP_DISB_TYPE = line.substring(          24,	34).trim();
            accountsPayable.AP_ACCT_YYYYMM = line.substring(        34,	40).trim();
            accountsPayable.AP_TAX_ID = line.substring(             40,	65).trim();
            accountsPayable.AP_BROKER_NO = line.substring(          65,	85).trim();
            accountsPayable.AP_VENDER_NO = line.substring(          85,	105).trim();
            accountsPayable.AP_ACCOUNT_NO = line.substring(         105,	125).trim();
            accountsPayable.AP_POLICY_NO = line.substring(          125,	175).trim();
            accountsPayable.AP_POLICY_EFF_DT = line.substring(      175,	185).trim();
            accountsPayable.AP_INSURED_NAME = line.substring(       185,	365).trim();
            accountsPayable.AP_PAYEE_TYPE = line.substring(         365,	395).trim();
            accountsPayable.AP_PAYEE_NAME = line.substring(         395,	575).trim();
            accountsPayable.AP_PAYEE_ADDR_1 = line.substring(       575,	615).trim();
            accountsPayable.AP_PAYEE_ADDR_2 = line.substring(       615,	655).trim();
            accountsPayable.AP_PAYEE_CITY = line.substring(         655,	705).trim();
            accountsPayable.AP_PAYEE_STATE = line.substring(        705,	710).trim();
            accountsPayable.AP_PAYEE_ZIP_CODE = line.substring(     710,	725).trim();
            accountsPayable.AP_PAYEE_COUNTRY_CD = line.substring(   725,	785).trim();
            accountsPayable.AP_AMOUNT = line.substring(             785,	807).trim();
            accountsPayable.AP_PAYMENT_DATE = line.substring(       807,	817).trim();
            accountsPayable.AP_BANK_CODE = line.substring(          817,	847).trim();
            accountsPayable.AP_CHECK_NO = line.substring(           847,	867).trim();
            accountsPayable.AP_ORIG_CHECK_NO = line.substring(      867,	889).trim();
            accountsPayable.AP_REFUND_METHOD = line.substring(      889,	899).trim();
            accountsPayable.AP_REFUND_REASON = line.substring(      899,	999).trim();
            accountsPayable.AP_REFUND_DESC = line.substring(        999,	1999).trim();
            accountsPayable.AP_SUPRESS_CHECK_YN = line.substring(   1999,	2000).trim();
        }


        return accountsPayable;
    }

    private static void readExcel (File file) throws IOException {
        String key = "";
        if (file.length() == 0){
            System.out.println("The directory is empty");
        }else{
            FileInputStream fis = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIt = sheet.iterator();

            while(rowIt.hasNext()){
                Row row = rowIt.next();

                Iterator<Cell> cellIt = row.cellIterator();
                int count = 0;

                while(cellIt.hasNext()){
                    if (count == 1){
                        //Account number found here
                        //get key
                    }
                    Cell cell = cellIt.next();
                    System.out.print(cell.toString() + "     ");
                    count++;
                }
                System.out.println();
            }
        }
    }

    public static void generateExcelFromTxt(){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("AP Results");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < accountsPayableList.get(0).columns.length; i++){
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(accountsPayableList.get(0).columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;

        for (AccountsPayable accountsPayable : accountsPayableList){
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(accountsPayable.AP_DISB_TYPE);
            row.createCell(1).setCellValue(accountsPayable.AP_ACCT_YYYYMM);
            row.createCell(2).setCellValue(accountsPayable.AP_TAX_ID);
            row.createCell(3).setCellValue(accountsPayable.AP_INVOICE_NO);
            row.createCell(4).setCellValue(accountsPayable.AP_BROKER_NO);
            row.createCell(5).setCellValue(accountsPayable.AP_VENDER_NO);
            row.createCell(6).setCellValue(accountsPayable.AP_ACCOUNT_NO);
            row.createCell(7).setCellValue(accountsPayable.AP_POLICY_NO);
            row.createCell(8).setCellValue(accountsPayable.AP_POLICY_EFF_DT);
            row.createCell(9).setCellValue(accountsPayable.AP_INSURED_NAME);
            row.createCell(10).setCellValue(accountsPayable.AP_PAYEE_TYPE);
            row.createCell(11).setCellValue(accountsPayable.AP_PAYEE_NAME);
            row.createCell(12).setCellValue(accountsPayable.AP_PAYEE_ADDR_1);
            row.createCell(13).setCellValue(accountsPayable.AP_PAYEE_ADDR_2);
            row.createCell(14).setCellValue(accountsPayable.AP_PAYEE_CITY);
            row.createCell(15).setCellValue(accountsPayable.AP_PAYEE_STATE);
            row.createCell(16).setCellValue(accountsPayable.AP_PAYEE_ZIP_CODE);
            row.createCell(17).setCellValue(accountsPayable.AP_PAYEE_COUNTRY_CD);
            row.createCell(18).setCellValue(accountsPayable.AP_AMOUNT);
            row.createCell(19).setCellValue(accountsPayable.AP_PAYMENT_DATE);
            row.createCell(20).setCellValue(accountsPayable.AP_BANK_CODE);
            row.createCell(21).setCellValue(accountsPayable.AP_CHECK_NO);
            row.createCell(22).setCellValue(accountsPayable.AP_ORIG_CHECK_NO);
            row.createCell(23).setCellValue(accountsPayable.AP_REFUND_METHOD);
            row.createCell(24).setCellValue(accountsPayable.AP_REFUND_REASON);
            row.createCell(25).setCellValue(accountsPayable.AP_REFUND_DESC);
            row.createCell(26).setCellValue(accountsPayable.AP_CLIENT_ID);
            row.createCell(27).setCellValue(accountsPayable.AP_USER_ID);
            row.createCell(28).setCellValue(accountsPayable.AP_SOURCE_POLICY_ID);
            row.createCell(29).setCellValue(accountsPayable.AP_MAILING_NAME);
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < accountsPayableList.get(0).columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        String fileName = new SimpleDateFormat("'AP_DATA_Data_'yyyMMddHHmm'.xlsx'").format(new Date());
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
