package com.shelter.majesco.stgbilling.test.automationScripts;

import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.AutomationInfo;
import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.Data;
import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.Transaction;
import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.WrongData;
import com.shelter.majesco.stgbilling.test.POM.MajescoUIHomePage;
import com.shelter.majesco.stgbilling.test.POM.MajescoUILoginPage;
import com.shelter.majesco.stgbilling.test.input.modifier.InputModifier;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UIAutomation {
    static List<WrongData> wrongDataList = new ArrayList<>(); //list of wrong date that need to be write to excel
    static AutomationInfo automationInfo = new AutomationInfo();//includes drivers,policy number etc.
    static DateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy"); //date format used across the Majesco
    static DateFormat oldDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    public static void main(String[] args) throws InterruptedException, IOException, ParseException {

        //Input section, where use enter dates and policy number.
        File excel = InputModifier.fileChooser(JFileChooser.FILES_ONLY);
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter the result from the base line, for  example:81015");
        String baseline = reader.next();

        reader = new Scanner(System.in);
        System.out.println("Please enter the date test it performed, for  example:81017");
        String policyPrefix = reader.next();

        System.out.println("Please enter the first policy number in the expected result, for exmaple: 1100");
        String policyNumber = reader.next();

        automationInfo.setPolicy(policyPrefix + policyNumber);


        //set the address of chrome driver
        String dir = System.getProperty("user.dir");
        dir += "\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", dir);

        String policy = "";
        String term = "";
        boolean gotAllData = false;
        int rowCount = 0;
        int colCount = 0;
        FileInputStream fis = new FileInputStream(excel);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIt = sheet.iterator();
        List<Transaction> transactionList = new ArrayList<>();//list to contain all transactions
        Transaction transaction = new Transaction();
        int transactionCount = -1; //to count the number of transactions
        Data data = new Data();
        Queue<String> threeTermPolicyList = new LinkedList<>();
        String threeTermPolicy = "";
        while (rowIt.hasNext()) {
            Row row = rowIt.next();
            Iterator<Cell> cellIt = row.cellIterator();
            while (cellIt.hasNext()) {
                Cell cell = cellIt.next();
                if (colCount == 1) {
                    threeTermPolicy = policyPrefix + cell.toString().replace(".0", "");
                }
                //if second - a found, that means the policy have more than two terms.
                if (cell.toString().equals("Second - a")) {
                    if (threeTermPolicyList.isEmpty()) {
                        threeTermPolicyList.add(threeTermPolicy);
                        System.out.println(threeTermPolicy);
                        System.out.print(cell.toString() + "(" + colCount + "," + rowCount + ")    ");
                    } else if (!threeTermPolicyList.contains(threeTermPolicy)) {
                        threeTermPolicyList.add(threeTermPolicy);
                        System.out.println(threeTermPolicy);
                        System.out.print(cell.toString() + "(" + colCount + "," + rowCount + ")    ");
                    }
                }
                colCount++;
            }
            rowCount++;
            colCount = 0;
        }

        //output all the policy that contain two terms.
        System.out.println("Elements of queue - " + threeTermPolicyList);
        Boolean notRun = false; //used to determined if certain set of automation should be run
        rowCount = 0;
        colCount = 0;
        rowIt = sheet.iterator();
        System.out.println("------------------------------------------------------------------------");
        //iterate tnrough excel
        while (rowIt.hasNext() && rowCount < 4505) {
            Row row = rowIt.next();
            Iterator<Cell> cellIt = row.cellIterator();
            while (cellIt.hasNext()) {
                Cell cell = cellIt.next();
                if (colCount == 1 && rowCount == 1) {
                    policy = cell.toString();
                }
                if (colCount == 2 && rowCount != 0) {
                    term = cell.toString();
                }
                if (colCount == 1 && rowCount != 0) {
                    policy = policyPrefix + cell.toString().replace(".0", "");
                }
            }
        }


    }


    //performing automation
    public static void automation(WebDriver driver, Data data, AutomationInfo automationInfo, int term) throws InterruptedException, IOException {
        driver.get(automationInfo.getURL());
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        MajescoUILoginPage majescoUILoginPage = new MajescoUILoginPage();
        majescoUILoginPage.login(automationInfo.getUserName(), automationInfo.getPassWord(), driver, wait);

        MajescoUIHomePage majescoUIHomePage = new MajescoUIHomePage();
        majescoUIHomePage.wait = wait;
        Thread.sleep(3000);
        System.out.println(automationInfo.getPolicy());
        majescoUIHomePage.searchAndClickPolicy(driver, automationInfo.getPolicy(), wait);

        String element = "dgSearch_" + term + "_1";

        WrongData wrongData = null;

        //verify excel aganist UI
        if (driver.findElements(By.id(element)).size() > 0) {
            driver.findElement(By.id(element)).click();
            majescoUIHomePage.openPolcyInfoTab(driver);
            if (!data.effectiveDate.isEmpty()) {
                wrongData = majescoUIHomePage.verifyPairs(driver, "Effective Date", data.effectiveDate);
                if (wrongData != null) {
                    wrongData.policyNumber = data.policyNumber + data.term;
                    wrongDataList.add(wrongData);
                }
            }
            if (!data.expiryDate.isEmpty()) {
                wrongData = majescoUIHomePage.verifyPairs(driver, "Expiry Date", data.expiryDate);
                if (wrongData != null) {
                    wrongData.policyNumber = data.policyNumber + data.term;
                    wrongDataList.add(wrongData);
                }
            }
            if (!data.paymentPlan.isEmpty()) {
                wrongData = majescoUIHomePage.verifyPairs(driver, "Payment Plan", data.paymentPlan);
                if (wrongData != null) {
                    wrongData.policyNumber = data.policyNumber + data.term;
                    wrongDataList.add(wrongData);
                }
            }
            if (!data.paymentMethod.isEmpty()) {
                wrongData = majescoUIHomePage.verifyPairs(driver, "Payment Method", data.paymentMethod);
                if (wrongData != null) {
                    wrongData.policyNumber = data.policyNumber + data.term;
                    wrongDataList.add(wrongData);
                }
            }
            if (!data.status.isEmpty()) {
                wrongData = majescoUIHomePage.verifyPairs(driver, "Status", data.status);
                if (wrongData != null) {
                    wrongData.policyNumber = data.policyNumber + data.term;
                    wrongDataList.add(wrongData);
                }
            }
            majescoUIHomePage.openCancellationTab(driver);

            if (!data.equityDate.isEmpty()) {
                wrongData = majescoUIHomePage.verifyPairs(driver, "Equity Date", data.equityDate);
                if (wrongData != null) {
                    wrongData.policyNumber = data.policyNumber + data.term;
                    wrongDataList.add(wrongData);
                }
            }
            majescoUIHomePage.openCancellationTab(driver);
            List<WrongData> transactionDataList = majescoUIHomePage.getTable(driver, data);
            if (transactionDataList.size() != 0) {
                wrongDataList.addAll(transactionDataList);
            }
            majescoUIHomePage.clickOnSearchBtn(driver, wait);
        }
        driver.close();
        driver.quit();
    }

}
