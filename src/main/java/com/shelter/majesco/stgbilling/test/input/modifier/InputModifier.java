package com.shelter.majesco.stgbilling.test.input.modifier;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;

public class InputModifier {

    //dates used across the class.
    static class Info {
        static String newPolicyNumber;
        static String oldPolicyNumBer;
        static String newPolicyEffectiveDateString;
        static String oldPolicyEffectiveDateString;
        static String newExpirationDateString;
        static String oldExpirationDateString;
        static String newExpirationDateNextYearString;
        static String oldExpirationDateNextYearString;
        static String newAdvancedPolicyEffectiveDateString;
        static String oldAdvancedPolicyEffectiveDateString;
        static String newAdvancedPolicyExpirationDateString;
        static String oldAdvancedPolicyExpirationDateString;
        static String newSubmitDateString;
        static String oldSubmitDateString;
    }

    static DateFormat oldDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    static DateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static void main(String[] args) throws ParseException {

        File root = fileChooser(JFileChooser.DIRECTORIES_ONLY);
        Info info = setDates();

        //change policy effective date and expiration date
        readAndModifyExcel(root, info);

        System.out.println(info.oldExpirationDateString);
        System.out.println(info.newExpirationDateString);
    }

    //folder chooser
    public static File fileChooser(int fileType) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select the root folder that contains all excels");
        chooser.setFileSelectionMode(fileType);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            System.out.println("You;ve choosed " + file.getName());
            return file;
        } else {
            System.out.println("There is an error openning the file");
            return null;
        }
    }

    //read the new excel and modify it to new dates.
    private static void readAndModifyExcel(File root, Info info) throws ParseException {
        File[] files = root.listFiles();
        if (files.length == 0) {
            System.out.println("The directory is empty");
        } else {
            for (File aFile : files) {

                if (aFile.getName().endsWith(".xlsx") && aFile.getName().startsWith("Regression")) {
                    String newLocation = getNewFileLocation(aFile.getPath());
                    File newFile = new File(newLocation);
                    try {
                        int day = getDateFromFileName(aFile.getName());
                        calculateSubmitDate(day, info);
                        copyExcel(aFile, newFile);
                        generateExcel(info, newFile);

                    } catch (IOException ie) {

                    }
                }
            }
        }
    }

    //user input on dates and calculate different dates
    private static Info setDates() throws ParseException {
        Calendar cal = Calendar.getInstance();
        Info info = new Info();

        System.out.println("This program is used to modify data in excel for Says billing regression test.");
        System.out.println("Please enter the policy effective date for old date, format : yymmdd, example: 180926");
        Scanner userInput = new Scanner(System.in);
        Info.oldPolicyNumBer = userInput.next();

        System.out.println("Please enter the policy effective date for new date, format : yymmdd, example: 181015");
        Info.newPolicyNumber = userInput.next();
        System.out.println("You want " + Info.oldPolicyNumBer + " to be changed to " + Info.newPolicyNumber);
        userInput.close();

        Date oldPolicyEffectiveDate = stringToDate(Info.oldPolicyNumBer);
        Date newPolicyEffectiveDate = stringToDate(Info.newPolicyNumber);
        Info.oldPolicyEffectiveDateString = oldDateFormat.format(oldPolicyEffectiveDate);
        Info.newPolicyEffectiveDateString = newDateFormat.format(newPolicyEffectiveDate);
        System.out.println(Info.oldPolicyEffectiveDateString);
        System.out.println(Info.newPolicyEffectiveDateString);

        //calculate advanced policy dates
        cal.setTime(newPolicyEffectiveDate);
        cal.add(Calendar.MONTH, +1);
        Info.newAdvancedPolicyEffectiveDateString = newDateFormat.format(cal.getTime());
        cal.add(Calendar.MONTH, +6);
        Info.newAdvancedPolicyExpirationDateString = newDateFormat.format(cal.getTime());

        //get policy dates
        cal.setTime(newPolicyEffectiveDate);
        cal.add(Calendar.MONTH, +6);
        Date newExpirationDate = cal.getTime();
        Info.newExpirationDateString = newDateFormat.format(newExpirationDate);
        cal.add(Calendar.MONTH, +6);
        newExpirationDate = cal.getTime();
        info.newExpirationDateNextYearString = newDateFormat.format(newExpirationDate);
        System.out.println("New Expiration date is " + Info.newExpirationDateString);
        System.out.println("New second Expiration date is " + Info.newExpirationDateNextYearString);

        //calculate advanced policy old dates
        cal.setTime(oldPolicyEffectiveDate);
        cal.add(Calendar.MONTH, +1);
        Info.oldAdvancedPolicyEffectiveDateString = oldDateFormat.format(cal.getTime());
        cal.add(Calendar.MONTH, +6);
        Info.oldAdvancedPolicyExpirationDateString = oldDateFormat.format(cal.getTime());

        //get policy dates
        cal.setTime(oldPolicyEffectiveDate);
        cal.add(Calendar.MONTH, +6);
        Date oldExpirationDate = cal.getTime();
        Info.oldExpirationDateString = oldDateFormat.format(oldExpirationDate);
        cal.add(Calendar.MONTH, +6);
        oldExpirationDate = cal.getTime();
        info.oldExpirationDateNextYearString = oldDateFormat.format(oldExpirationDate);
        System.out.println("Old Expiration date is " + Info.oldExpirationDateString);
        System.out.println("Old second Expiration date is " + Info.oldExpirationDateNextYearString);
        //day = getDateFromFileName("Regression - Say - Day 20 - Step 1 - PC Can Rein - fix exp date.xlsx");
        return info;

    }



    //calculate submit date based on the business rule
    private static void calculateSubmitDate(int day, Info info) throws ParseException {
        DateFormat oldDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date oldSubmitDate = oldDateFormat.parse(info.oldPolicyEffectiveDateString);

        Calendar cal = Calendar.getInstance();
        cal.setTime(oldSubmitDate);
        cal.add(Calendar.DAY_OF_YEAR, +day - 1);
        oldSubmitDate = cal.getTime();
        info.oldSubmitDateString = oldDateFormat.format(oldSubmitDate);


        Date newSubmitDate = newDateFormat.parse(info.newPolicyEffectiveDateString);
        cal.setTime(newSubmitDate);
        cal.add(Calendar.DAY_OF_YEAR, +day - 1);
        newSubmitDate = cal.getTime();
        info.newSubmitDateString = newDateFormat.format(newSubmitDate);


        System.out.println("The new Submit time is " + info.newSubmitDateString);
        System.out.println("The old Submit time is " + info.oldSubmitDateString);

    }

    //calculate change effective date based on the business rule
    private static String calculateChangeEffectiveDate(Date oldChangeEffectiveDate, Info info) throws ParseException {
        Date oldSubmitDate = oldDateFormat.parse(info.oldSubmitDateString);
        int daysBetween = daysBetween(oldChangeEffectiveDate, oldSubmitDate);
        Date newSubmitDate = newDateFormat.parse(info.newSubmitDateString);

        Calendar cal = Calendar.getInstance();
        cal.setTime(newSubmitDate);
        if (daysBetween > 0) {
            cal.add(Calendar.DAY_OF_YEAR, daysBetween);
        } else {
            System.out.println("The day in between is " + daysBetween);
            cal.add(Calendar.DAY_OF_YEAR, daysBetween);
        }
        cal.setTime(newSubmitDate);
        cal.add(Calendar.DAY_OF_YEAR, daysBetween);
        Date newEffectiveDate = cal.getTime();
        return newDateFormat.format(newEffectiveDate);

    }

    //modify the excel content
    private static void generateExcel(Info info, File excelFile) throws IOException, ParseException {
        int colCount = 0;
        int changeEffectiveDateCol = 0;
        int paymentEffectiveDateCol = 0;
        FileInputStream fis = new FileInputStream(excelFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIt = sheet.iterator();

        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();

        while (rowIt.hasNext()) {
            Row row = rowIt.next();
            Iterator<Cell> cellIt = row.cellIterator();

            while (cellIt.hasNext()) {
                cellStyle.setDataFormat(
                        createHelper.createDataFormat().getFormat("mm/dd/yyyy"));
                Cell cell = cellIt.next();
                if (cell.toString().equals("changeEffectiveDate")) {
                    changeEffectiveDateCol = colCount;
                }
                if (cell.toString().equals("paymentEffectiveDate")) {
                    paymentEffectiveDateCol = colCount;
                }
                if (isNumeric(cell.toString())) {
                    String s = new BigDecimal(cell.toString()).toPlainString();
                    String replace = s.replace(".0", "");
                    replace = replace.replace(info.oldPolicyNumBer.substring(1), info.newPolicyNumber.substring(1));
                    cell.setCellValue(replace);
                } else if (cell.toString().equals(info.oldPolicyEffectiveDateString)) {
                    cell.setCellValue(info.newPolicyEffectiveDateString);
                } else if (cell.toString().equals(info.oldExpirationDateString)) {
                    cell.setCellValue(info.newExpirationDateString);
                } else if (cell.toString().equals(info.oldExpirationDateNextYearString)) {
                    cell.setCellValue(info.newExpirationDateNextYearString);
                } else if (cell.toString().equals(info.oldSubmitDateString)) {
                    cell.setCellValue(info.newSubmitDateString);
                } else if (cell.toString().equals(info.oldAdvancedPolicyEffectiveDateString)) {
                    cell.setCellValue(info.newAdvancedPolicyEffectiveDateString);
                } else if (cell.toString().equals(info.oldAdvancedPolicyExpirationDateString)) {
                    cell.setCellValue(info.newAdvancedPolicyExpirationDateString);
                }
                if (colCount == changeEffectiveDateCol) {
                    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            System.out.println(" ---" + newDateFormat.format(cell.getDateCellValue()));
                            cell.setCellValue(calculateChangeEffectiveDate(cell.getDateCellValue(), info));
                        }
                    }

                } else if (colCount == paymentEffectiveDateCol) {
                    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            System.out.println(" ---" + newDateFormat.format(cell.getDateCellValue()));
                            cell.setCellValue(calculateChangeEffectiveDate(cell.getDateCellValue(), info));
                        }
                    }
                }
                colCount++;
            }
            colCount = 0;
        }

        //close workbook and output stream.
        FileOutputStream fos = new FileOutputStream(excelFile);
        workbook.write(fos);
        workbook.close();
        fis.close();
        fos.close();
    }

    //days between dates.
    private static int daysBetween(Date oldDate, Date newDate) {
        int days = 0;
        days = Days.daysBetween(
                new LocalDate(oldDate.getTime()),
                new LocalDate(newDate.getTime())).getDays();
        if (oldDate.getTime() - newDate.getTime() > 0) {
            return days;
        } else {
            days = -days;
            return days;
        }
    }

    //supporting function that convert string to date
    private static Date stringToDate(String dateString) throws ParseException {
        //format = 90926
        System.out.println(dateString);
        Date date = new SimpleDateFormat("yyMMdd").parse(dateString);
        return date;

    }

    //extract date from regression file.
    private static int getDateFromFileName(String filename) {
        String[] names = filename.split(" ");
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals("Day")) {
                return Integer.parseInt(names[i + 1]);
            }
        }
        return 0;
    }

    //set new location for the folder.
    private static String getNewFileLocation(String oldLocation) {
        Path path = Paths.get(oldLocation);
        String location = path.getParent().toString();
        String[] filePaths = location.split("\\\\");
        String newLocation = "";
        System.out.println(filePaths.length);
        for (int i = 0; i < filePaths.length; i++) {
            if (i == filePaths.length - 2) {
                newLocation += filePaths[i] + "_New" + "\\";
            } else {
                newLocation += filePaths[i] + "\\";
            }
        }
        newLocation += path.getFileName();
        System.out.println(newLocation);
        return newLocation;
    }

    //copy over the excel file
    private static void copyExcel(File from, File to) throws IOException {
        FileUtils.copyFile(from, to);
    }

    //check whether a string is a number
    private static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}