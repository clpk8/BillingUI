package com.shelter.majesco.stgbilling.test.automationScripts;

import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.*;
import com.shelter.majesco.stgbilling.test.input.modifier.InputModifier;
import com.shelter.majesco.stgbilling.test.POM.MajescoUIHomePage;
import com.shelter.majesco.stgbilling.test.POM.MajescoUILoginPage;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//test Majesco UI with all policies
public class SearchPolicy {

    static List<WrongData> wrongDataList = new ArrayList<>(); //list of wrong date that need to be write to excel
    static AutomationInfo automationInfo = new AutomationInfo();//includes drivers,policy number etc.
    static DateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy"); //date format used across the Majesco
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

        automationInfo.setPolicy(policyPrefix+policyNumber);

        //calculate day difference
        Date date = new Date();
        Date beginning = new SimpleDateFormat("yyMMdd").parse(1 + baseline);
        Date end = new SimpleDateFormat("yyMMdd").parse(1 + policyPrefix);
        long days = (end.getTime() - beginning.getTime()) / 86400000;
        System.out.println("Difference between two date is " + days);
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginning);
        days = 0;

        //set the address of chrome driver
        String dir = System.getProperty("user.dir");
        dir += "\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", dir);

        //First loop through the excel and find the policies with more than two terms.
        //create inputstream for excel
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
        int daysToBump = 0; // used to determined if we need to bump days due to EFT Draft being 29,30,31
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

                if (colCount == 2 && rowCount != 0) {
                    data.transactionList = transactionList;
                    //new term start
                    if (data.term != null && !term.equals(data.term) || !policy.equals(automationInfo.getPolicy())) {
                        System.out.println("Here we run a automation srcips on term " + data.term);
                        System.out.println(automationInfo.getPolicy());
                        System.out.println("Effective date: " + data.effectiveDate);
                        System.out.println("Expiry Date: " + data.expiryDate);
                        System.out.println("Payment plant: " + data.paymentPlan);
                        System.out.println("Payment method is: " + data.paymentMethod);
                        System.out.println("Status is: " + data.status);
                        System.out.println("Equity Date is " + data.equityDate);
                        int i = 0;
                        if (automationInfo.getPolicy().equals(threeTermPolicyList.peek())) {
                            System.out.println("The first element in the queue is " + threeTermPolicyList.peek());
                            if (data.term.equals("First")) {
                                i = 2;
                            } else if (data.term.equals("Second - a")) {
                                i = 0;
                                notRun = false;
                            } else if (data.term.equals("Second - b")) {
                                i = 1;
                                notRun = false;
                                threeTermPolicyList.remove();
                            } else if (data.term.equals("Second")) {
                                notRun = true;
                            }
                        } else {
                            if (!policy.equals(automationInfo.getPolicy())) {
                                if (data.term.equals("First")) {
                                    i = 0;
                                }
                            } else {
                                if (data.term.equals("First")) {
                                    i = 1;
                                } else if (data.term.equals("Second")) {
                                    i = 0;
                                }
                            }
                        }

                        //if stopped, check for month-end processing and start new term.
                        if (!notRun) {
                            WebDriver driver = new ChromeDriver();
                            automation(driver, data, automationInfo, i);
                            //daysToBump = 0;
                            data.cleanDate();
                            transactionList = new ArrayList<>();
                            transaction = new Transaction();
                            term = data.term;
                            automationInfo.setPolicy(policy);
                        }
                    }
                    data.term = cell.toString();
                    data.policyNumber = policy;
                }
                String dateString = "";
                //populate data from excel
                if (rowCount != 0) {
                    switch (cell.toString()) {
                        case "Effective Date":
                            dateString = cellIt.next().toString();
                            date = newDateFormat.parse(dateString);
                            cal.setTime(date);
                            dateString = newDateFormat.format(date);
                            data.effectiveDate = dateString;
                            System.out.println("Effective date: " + dateString);
                            break;
                        case "Expiry Date":
                            dateString = cellIt.next().toString();
                            date = newDateFormat.parse(dateString);
                            cal.setTime(date);
                            dateString = newDateFormat.format(date);
                            data.expiryDate = dateString;
                            break;
                        case "Payment Plan":
                            data.paymentPlan = cellIt.next().toString();
                            break;
                        case "Payment Method":
                            data.paymentMethod = cellIt.next().toString();
                            break;
                        case "Status":
                            data.status = cellIt.next().toString();
                            break;
                        case "Equity Date":
                            dateString = cellIt.next().toString();
                            date = newDateFormat.parse(dateString);
                            cal.setTime(date);
                            dateString = newDateFormat.format(date);
                            data.equityDate = dateString;
                            break;
                    }
                }
                //when eft found,
                if (colCount == 0) {
                    transactionCount = -1;
                    if (transaction.transactionType != null) {
                        if (transaction.transactionType.equals("EFT Draft Notice")) {

                            Date effectiveDate = newDateFormat.parse(transaction.effectiveDate);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(effectiveDate);
                            int dayOfTheMonth = calendar.get(Calendar.DAY_OF_MONTH);
                            if (dayOfTheMonth == 29 || dayOfTheMonth == 30 || dayOfTheMonth == 31) {
                                Calendar bumpCalendar = Calendar.getInstance();
                                bumpCalendar.setTime(effectiveDate);
                                bumpCalendar.add(Calendar.MONTH, 1);
                                bumpCalendar.set(Calendar.DATE, bumpCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                                Date nextMonthFirstDay = bumpCalendar.getTime();
                                daysToBump = (int) (nextMonthFirstDay.getTime() - effectiveDate.getTime()) / 86400000;
                                System.out.println("Difference between two date is " + daysToBump);
                            }
                        }
                        transactionList.add(transaction);
                    }
                    System.out.println();
                } else {
                    if (cell.toString().equals("Transactions")) {
                        transaction = new Transaction();
                        transactionCount = 0;
                    }
                }
                if (transactionCount != -1) {
                    System.out.print(cell.toString() + "(" + transactionCount + ")" + "|");
                    String cellData = cell.toString();
                    if (cell.toString().startsWith("-")) {
                        cellData = cell.toString().substring(1);
                        cellData = "(" + cellData + ")";
                    }

                    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {

                            Date tempDate = newDateFormat.parse(cell.toString());
                            cal.setTime(tempDate);
                            cal.add(Calendar.DAY_OF_YEAR, (int) days);
                            tempDate = cal.getTime();
                            cellData = newDateFormat.format(tempDate);
                        }
                    }

                    //populate transcation
                    switch (transactionCount) {
                        case 3:
                            transaction.transactionType = cellData;
                            break;
                        case 4:
                            transaction.entryDate = cellData;
                            break;
                        case 5:
                            transaction.effectiveDate = cellData;
                            break;
                        case 6:
                            transaction.expiryDate = cellData;
                            break;
                        case 7:
                            transaction.amount = cellData;
                            break;
                        case 8:
                            transaction.grossAmount = cellData;
                            break;
                        case 9:
                            transaction.commission = cellData;
                            break;
                        case 10:
                            transaction.commissionAmount = cellData;
                            break;
                        case 11:
                            transaction.netAmount = cellData;
                            break;
                        case 12:
                            transaction.receivableAmount = cellData;
                            break;
                        case 13:
                            transaction.balance = cellData;
                            break;
                        case 14:
                            transaction.userID = cellData;
                            break;
                        default:
                            break;
                    }
                    transactionCount++;
                }
                colCount++;
            }
            rowCount++;
            colCount = 0;
        }
        //write to excel
        ExcelWriter excelWriter = new ExcelWriter();
        excelWriter.wrongDataList = wrongDataList;
        excelWriter.writeExcel();

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

            //Code added just to taking care of data was dated
            //should be deleted later
            //--------------------

            Thread.sleep(2000);
            majescoUIHomePage.changeTheDisplayDateAndClickOnView(driver);
            //--------------------


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