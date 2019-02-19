package com.shelter.majesco.stgbilling.test.output.modifier;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ExpectedResultModifier {
    public static void main(String[] args) {
        ExcelPicker excelPicker = new ExcelPicker();
        File excel = excelPicker.fileChooser();
        String path = excel.getParent() + "/new_" + excel.getName();
        File newFile = new File(path);
        System.out.println(path);
        try {
            excelPicker.copyFile(excel, newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DateModifier dateModifier = new DateModifier();
        Date beginning = new Date();
        Date end = new Date();
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter the result from the base line, for  example:81015");
        String baseline = reader.next();
        reader = new Scanner(System.in);
        System.out.println("Please enter the date test it performed, for  example:81017");
        String policyPrefix = reader.next();
        try {
            beginning = new SimpleDateFormat("yyMMdd").parse(1 + baseline);
            end = new SimpleDateFormat("yyMMdd").parse(1 + policyPrefix);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int days = dateModifier.findDifferenceBetweenDate(beginning, end);
        PolicyDates policyDates = new PolicyDates();
        policyDates.calculateDates(beginning, end);
        ExcelModifier excelModifier = new ExcelModifier(excel, beginning, end, policyDates);
        excelModifier.saveExcelFile(newFile);
        excelModifier.checkForMonthEnd(newFile.getAbsolutePath());
    }
}
