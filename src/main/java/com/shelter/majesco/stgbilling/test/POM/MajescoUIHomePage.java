package com.shelter.majesco.stgbilling.test.POM;

import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.Data;
import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.Transaction;
import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.WrongData;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//compare transaction
class TransactionTypeComparator implements Comparator<Transaction>{

    static DateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public int compare(Transaction t1, Transaction t2){
        int isEqual = t1.transactionType.compareTo(t2.transactionType);
        if (isEqual == 0){
            try {
                return newDateFormat.parse(t1.entryDate).compareTo(newDateFormat.parse(t2.entryDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return isEqual;
    }
}
public class MajescoUIHomePage {

    public WebDriverWait wait;

    //web elements
    private static By elementPolicyField = By.name("quickSearchValue");
    private static By elementSearchBtn = By.id("quickSearchValue_lnk");
    private static By elementPolicyLink = By.id("dgSearch_0_1");
    private static By elementLogoutBtn = By.id("logout");
    private static By elementPolicyInfoTab = By.cssSelector("a[data-cid='tabPolicyInfo']");
    private static By elementEffectDate = By.cssSelector("div[id='txtEffDate']");
    private static By elementExpiryDate = By.id("txtExpDate");
    private static By elementPaymentPlan = By.id("linkPayPlan");
    private static By elementPaymentMethod = By.id("txtPayMethod");
    private static By elementStatus = By.id("txtStatus");
    private static By elementCanCelTab = By.cssSelector("a[data-cid='tabCancellation']");
    private static By elementEquityDate = By.id("txtPolEquityDate");
    private static By elementPolicyDDL = By.name("listPolicyTermIds");
    private static By elementFromDateField = By.cssSelector("input[name='frmdate']");
    private static By elementViewBtn = By.cssSelector("button[name='btnViews']");
    private static By elementOkButton = By.cssSelector("button[name='Ok']");

    public void waitForElementToBeClickable(WebDriver driver, By element) {
        WebElement myDynamicElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(elementLogoutBtn));
    }

    public void changeTheDisplayDateAndClickOnView(WebDriver driver) throws InterruptedException {
        if (driver.findElements(elementOkButton).size() > 0){
            driver.findElement(elementOkButton).click();;
        }
        wait.until(ExpectedConditions.elementToBeClickable(elementFromDateField));
        driver.findElement(elementFromDateField).clear();
        driver.findElement(elementFromDateField).sendKeys("01/01/2018");
        driver.findElement(elementViewBtn).click();
        Thread.sleep(500);

    }
    public void clickOnDifferentTerm(WebDriver driver, int index){
        WebElement ddl = driver.findElement(elementPolicyDDL);
        Select dropDown = new Select(ddl);

        dropDown.selectByIndex(index);
    }
    public void searchAndClickPolicy(WebDriver driver, String policyNumber, WebDriverWait wait) throws InterruptedException {
        //Step2 search and click on policy
        Thread.sleep(500);
        wait.until(ExpectedConditions.elementToBeClickable(elementSearchBtn));
        driver.findElement(elementPolicyField).clear();
        driver.findElement(elementPolicyField).sendKeys(policyNumber);
        Thread.sleep(1000);
        driver.findElement(elementSearchBtn).click();
        wait.until(ExpectedConditions.elementToBeClickable(elementPolicyLink));
    }

    public void logout(WebDriver driver) {
        waitForElementToBeClickable(driver, elementLogoutBtn);
        driver.findElement(elementLogoutBtn).click();
    }

    public void searchPolicy(String policyNumber, WebDriver driver, WebDriverWait wait) {

        //Step2 search and click on policy
        wait.until(ExpectedConditions.elementToBeClickable(elementSearchBtn));
        driver.findElement(elementPolicyField).sendKeys(policyNumber);
        driver.findElement(elementSearchBtn).click();
        wait.until(ExpectedConditions.elementToBeClickable(elementPolicyLink));
    }

    public void clickOnSearchBtn(WebDriver driver, WebDriverWait wait) {
        driver.findElement(elementSearchBtn).click();
        wait.until(ExpectedConditions.elementToBeClickable(elementPolicyLink));
    }

    public void openPolcyInfoTab(WebDriver driver) throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(elementPolicyInfoTab));
        wait.until(ExpectedConditions.elementToBeClickable(elementPolicyInfoTab));
        Thread.sleep(1000);
        driver.findElement(elementPolicyInfoTab).click();
    }

    public void openCancellationTab(WebDriver driver) throws InterruptedException {
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(elementCanCelTab));
        driver.findElement(elementCanCelTab).click();
    }

    //function used to verify pairs of informations
    public WrongData verifyPairs(WebDriver driver, String key, String value) throws InterruptedException {
        String newValue = "";
        switch (key) {
            case "Effective Date":
                wait.until(ExpectedConditions.elementToBeClickable(elementEffectDate));
                System.out.println("-----------------------");
                System.out.print(value + "==");
                newValue = driver.findElement(elementEffectDate).getText();
                System.out.println("effective date is " + newValue);
                System.out.println(newValue.equals(value));
                if (!newValue.equals(value)) {
                    return new WrongData("", key + ":" + value, key + ":" + newValue);
                }
                break;
            //return(driver.findElement(elementEffectDate).getText().equals(value));
            case "Expiry Date":
                wait.until(ExpectedConditions.elementToBeClickable(elementExpiryDate));
                System.out.println("-----------------------");
                System.out.print(value + "==");
                newValue = driver.findElement(elementExpiryDate).getText();
                System.out.println("Expiry date is " + newValue);
                System.out.println(newValue.equals(value));
                if (!newValue.equals(value)) {
                    return new WrongData("", key + ":" + value, key + ":" + newValue);
                }
                break;
            case "Payment Plan":
                wait.until(ExpectedConditions.elementToBeClickable(elementPaymentPlan));
                System.out.println("-----------------------");
                System.out.print(value + "==");
                newValue = driver.findElement(elementPaymentPlan).getText();
                System.out.println("Payment Plan is " + newValue);
                System.out.println(newValue.equals(value));
                if (!newValue.equals(value)) {
                    return new WrongData("", key + ":" + value, key + ":" + newValue);
                }
                break;
            case "Payment Method":
                wait.until(ExpectedConditions.elementToBeClickable(elementPaymentMethod));
                System.out.println("-----------------------");
                System.out.print(value + "==");
                newValue = driver.findElement(elementPaymentMethod).getText();
                System.out.println("Payment Method is " + newValue);
                System.out.println(newValue.equals(value));
                if (!newValue.equals(value)) {
                    return new WrongData("", key + ":" + value, key + ":" + newValue);
                }
                break;
            case "Status":
                wait.until(ExpectedConditions.elementToBeClickable(elementStatus));
                System.out.println("-----------------------");
                System.out.print(value + "==");
                newValue = driver.findElement(elementStatus).getText();
                System.out.println("Status is " + newValue);
                System.out.println(newValue.equals(value));
                if (!newValue.equals(value)) {
                    return new WrongData("", key + ":" + value, key + ":" + newValue);
                }
                break;
            case "Equity Date":
                wait.until(ExpectedConditions.elementToBeClickable(elementEquityDate));
                System.out.println("-----------------------");
                System.out.print(value + "==");
                newValue = driver.findElement(elementEquityDate).getText();
                System.out.println("Equitt Date date is " + newValue);
                System.out.println(newValue.equals(value));
                if (!newValue.equals(value)) {
                    return new WrongData("", key + ":" + value, key + ":" + newValue);
                }
                break;
        }
        return null;
    }

    //find the total number of terms in a policy
    public int getNumberOfPolicy(WebDriver driver) throws InterruptedException {
        for (int i = 2; i >= 0 ; i--){
            Thread.sleep(1000);
            if (driver.findElements(By.id("dgSearch_" + i + "_1")).size() > 0){
                return i+1;
            }
        }
        return 0;
    }

    public void tableTest(WebDriver driver){
        WebElement table = driver.findElement(By.cssSelector("table[data-cid='dgPolicyTransactions_grid']"));
        List<WebElement> listOfRows = table.findElements(By.tagName("tr"));
        System.out.println("Rows: " + listOfRows.size());

        List<WebElement> listOfCols = listOfRows.get(0).findElements(By.tagName("th")); //If first row is header row

        System.out.println("Columns: " + listOfCols.size());

    }
//  getTable and compare the table
    public List<WrongData> getTable(WebDriver driver, Data data) {

        WebElement table = driver.findElement(By.cssSelector("table[data-cid='dgPolicyTransactions_grid']"));
        List<WebElement> listOfRows = table.findElements(By.tagName("tr"));
        System.out.println("Rows: " + listOfRows.size());
        List<WebElement> listOfCols = listOfRows.get(0).findElements(By.tagName("th")); //If first row is header row
        System.out.println("Columns: " + listOfCols.size());

        int col = listOfCols.size();
        int row = listOfRows.size();

        List<Transaction> transactionList = new ArrayList<>();
        List<WrongData> wrongData = new ArrayList<>();

        String tableIDPrefix = "dgPolicyTransactions";
        String cellData;
        for (int j = 0; j < row; j++) {
            Transaction transaction = new Transaction();
            for (int i = 0; i < col; i++) {
                //System.out.println(tableIDPrefix + "_" + j + "_" + i);
                if (driver.findElements(By.id(tableIDPrefix + "_" + j + "_" + i)).size() > 0) {
                    //System.out.println("The number of table found is " + driver.findElements(By.id(tableIDPrefix + "_" + j + "_" + i)).size());
                    cellData = driver.findElement(By.id(tableIDPrefix + "_" + j + "_" + i)).getText();
                    if (cellData.isEmpty()) {
                        cellData = "n/a";
                    }
                    cellData = cellData.replace(",","");
                    if (cellData.endsWith(".00")) {
                        cellData = cellData.substring(0, cellData.length() - 1);
                    }
                    if (cellData.endsWith(".00)")) {
                        cellData = cellData.substring(0, cellData.length() - 2);
                        cellData = cellData + ")";
                    }
                    System.out.print(cellData + "(" + i + "," + j + ")      ");
                    switch (i) {
                        case 1:
                            transaction.transactionType = cellData;
                            break;
                        case 3:
                            transaction.entryDate = cellData;
                            break;
                        case 4:
                            transaction.effectiveDate = cellData;
                            break;
                        case 5:
                            transaction.expiryDate = cellData;
                            break;
                        case 6:
                            transaction.amount = cellData;
                            break;
                        case 7:
                            transaction.grossAmount = cellData;
                            break;
                        case 9:
                            transaction.commissionAmount = cellData;
                            break;
                        case 10:
                            transaction.netAmount = cellData;
                            break;
                        case 11:
                            transaction.receivableAmount = cellData;
                            break;
                        case 12:
                            transaction.balance = cellData;
                            break;
                        case 13:
                            transaction.userID = cellData;
                            break;
                    }


                } else {
                    //System.out.println(" ");
                }
            }
            transaction.commission = "n/a";
            if (transaction.transactionType != null && transaction.userID != null && transaction.entryDate != null && !transaction.userID.startsWith("Billing_")) {
                transactionList.add(transaction);
            }
            System.out.println();
        }

        for (Transaction t : data.transactionList) {
            System.out.println(t.transactionType + t.entryDate + t.effectiveDate + t.expiryDate + t.amount +
                    t.grossAmount + t.commission + t.commissionAmount + t.netAmount + t.receivableAmount +
                    t.balance + t.userID);
        }

        //sort transactions
        Collections.sort(data.transactionList, new TransactionTypeComparator());
        System.out.println("---------------------Compare line ----------------------------");
        Collections.reverse(transactionList);
        for (Transaction t : transactionList) {
            System.out.println(t.transactionType + t.entryDate + t.effectiveDate + t.expiryDate + t.amount +
                    t.grossAmount + t.commission + t.commissionAmount + t.netAmount + t.receivableAmount +
                    t.balance + t.userID);
        }
        Collections.sort(transactionList, new TransactionTypeComparator());

        if (transactionList.size() != data.transactionList.size()) {
            System.out.println(data.policyNumber + " number of transactions does not agree");
            wrongData.add(new WrongData(data.policyNumber + data.term, "Expected " + data.transactionList.size() + " of Transactions",
                    "Actual " + transactionList.size() + " of Transactions"));
        } else {
            System.out.println("---------------------Result line ----------------------------");
            for (int i = 0; i < transactionList.size(); i++) {
                if (!transactionList.get(i).transactionType.equals(data.transactionList.get(i).transactionType)) {
                    System.out.print("Actual: " + transactionList.get(i).transactionType);
                    System.out.println("     Expected: " + data.transactionList.get(i).transactionType);
                    wrongData.add(new WrongData(data.policyNumber + data.term, "Transaction:" + data.transactionList.get(i).transactionType,
                            "Transaction: " + transactionList.get(i).transactionType));
                }
                if (!transactionList.get(i).entryDate.equals(data.transactionList.get(i).entryDate) &&!data.transactionList.get(i).entryDate.equals("n/a")) {
                    System.out.print("Actual: " + transactionList.get(i).entryDate);
                    System.out.println("     Expected: " + data.transactionList.get(i).entryDate);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, data.transactionList.get(i).transactionType + "-Entry Date:"
                            + data.transactionList.get(i).entryDate, transactionList.get(i).transactionType
                            + "-Entry Date:" + transactionList.get(i).entryDate));
                }
                if (!transactionList.get(i).effectiveDate.equals(data.transactionList.get(i).effectiveDate) && !data.transactionList.get(i).effectiveDate.equals("n/a")) {
                    System.out.print("Actual: " + transactionList.get(i).effectiveDate);
                    System.out.println("     Expected: " + data.transactionList.get(i).effectiveDate);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, data.transactionList.get(i).transactionType + "-Effective Date:"
                            + data.transactionList.get(i).effectiveDate, transactionList.get(i).transactionType +
                            "-Effective Date:" + transactionList.get(i).effectiveDate));
                }
                if (!transactionList.get(i).expiryDate.equals(data.transactionList.get(i).expiryDate) && !data.transactionList.get(i).expiryDate.equals("n/a") ) {
                    System.out.print("Actual: " + transactionList.get(i).expiryDate);
                    System.out.println("     Expected: " + data.transactionList.get(i).expiryDate);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, data.transactionList.get(i).transactionType + "-Expiry Date:"
                            + data.transactionList.get(i).expiryDate, transactionList.get(i).transactionType +
                            "-Expiry Date:" + transactionList.get(i).expiryDate));
                }
                if (!transactionList.get(i).amount.equals(data.transactionList.get(i).amount) && !data.transactionList.get(i).amount.equals("n/a") ) {
                    System.out.print("Actual: " + transactionList.get(i).amount);
                    System.out.println("     Expected: " + data.transactionList.get(i).amount);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, data.transactionList.get(i).transactionType + "-Amount:"
                            + data.transactionList.get(i).amount, transactionList.get(i).transactionType +
                            "-Amount:" + transactionList.get(i).amount));
                }
                if (!transactionList.get(i).grossAmount.equals(data.transactionList.get(i).grossAmount)  && !data.transactionList.get(i).grossAmount.equals("n/a")) {
                    System.out.print("Actual: " + transactionList.get(i).grossAmount);
                    System.out.println("     Expected: " + data.transactionList.get(i).grossAmount);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, data.transactionList.get(i).transactionType + "-Gross Amount:"
                            + data.transactionList.get(i).grossAmount, transactionList.get(i).transactionType +
                            "-Gross Amount:" + transactionList.get(i).grossAmount));
                }
                if (!transactionList.get(i).commission.equals(data.transactionList.get(i).commission) && !data.transactionList.get(i).commission.equals("n/a")) {
                    System.out.print("Actual: " + transactionList.get(i).commission);
                    System.out.println("     Expected: " + data.transactionList.get(i).commission);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, data.transactionList.get(i).transactionType + "-Commission:"
                            + data.transactionList.get(i).commission, transactionList.get(i).transactionType +
                            "-Commission:" + transactionList.get(i).commission));
                }
                if (!transactionList.get(i).commissionAmount.equals(data.transactionList.get(i).commissionAmount) && !data.transactionList.get(i).commissionAmount.equals("n/a")) {
                    System.out.print("Actual: " + transactionList.get(i).commissionAmount);
                    System.out.println("     Expected: " + data.transactionList.get(i).commissionAmount);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, data.transactionList.get(i).transactionType + "-Commission Amount:"
                            + data.transactionList.get(i).commissionAmount, transactionList.get(i).transactionType +
                            "-Commission Amount:" + transactionList.get(i).commissionAmount));
                }
                if (!transactionList.get(i).netAmount.equals(data.transactionList.get(i).netAmount) && !data.transactionList.get(i).netAmount.equals("n/a")) {
                    System.out.print("Actual: " + transactionList.get(i).netAmount);
                    System.out.println("     Expected: " + data.transactionList.get(i).netAmount);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, data.transactionList.get(i).transactionType + "-Net Amount:"
                            + data.transactionList.get(i).netAmount, transactionList.get(i).transactionType +
                            "-Net Amount:" + transactionList.get(i).netAmount));
                }
                if (!transactionList.get(i).receivableAmount.equals(data.transactionList.get(i).receivableAmount) && !data.transactionList.get(i).receivableAmount.equals("n/a")) {
                    System.out.print("Actual: " + transactionList.get(i).receivableAmount);
                    System.out.println("     Expected: " + data.transactionList.get(i).receivableAmount);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, data.transactionList.get(i).transactionType + "-Receivable Amount:"
                            + data.transactionList.get(i).receivableAmount, transactionList.get(i).transactionType +
                            "-Receivable Amount:" + transactionList.get(i).receivableAmount));
                }
                if (!transactionList.get(i).balance.equals(data.transactionList.get(i).balance) && !data.transactionList.get(i).balance.equals("n/a")) {
                    System.out.print("Actual: " + transactionList.get(i).balance);
                    System.out.println("     Expected: " + data.transactionList.get(i).balance);
                    wrongData.add(new WrongData(data.policyNumber+ data.term, transactionList.get(i).transactionType + "-Balance:"
                            + data.transactionList.get(i).balance, transactionList.get(i).transactionType +
                            "-Balance:" + transactionList.get(i).balance));
                }
                if (!transactionList.get(i).userID.equals(data.transactionList.get(i).userID)) {
                    System.out.print("Actual: " + transactionList.get(i).userID);
                    System.out.println("     Expected: " + data.transactionList.get(i).userID);
                    wrongData.add(new WrongData(data.policyNumber + data.term, data.transactionList.get(i).transactionType + "-UserId:"
                            + data.transactionList.get(i).userID, transactionList.get(i).transactionType +
                            "-UserId:" + transactionList.get(i).userID));
                }
            }
        }
        System.out.println("---------------------Result End -----------------------------");

        return wrongData;
    }
}
