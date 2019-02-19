package com.shelter.majesco.stgbilling.test.automationScripts;

import com.shelter.majesco.stgbilling.test.POM.MajescoUILoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumLogOut {
    private static String userName = "user1";
    private static String passWord = "icd123";
    private static String policyNumber = "809051100";
    private static By elementUserID = By.id("userID");
    private static By elementPassword = By.id("userPass");
    private static By elementLoginBtn = By.id("btnLogin");
    private static By elementPolicyField = By.name("quickSearchValue");
    private static By elementSearchBtn = By.id("quickSearchValue_lnk");
    private static By elementPolicyLink = By.id("dgSearch_0_1");

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "//columbia/DFSR1/QDoc/Payment Services/Code/chromedriver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        MajescoUILoginPage majescoUILoginPage = new MajescoUILoginPage();

        //find how many links
        int i = 0;
        while(true)
        {

            driver.get("https://mbst.shelterinsurance.com:8643/MajescoBilling/");
            driver.manage().window().maximize();
            //initialize WebDriverWait
            WebDriverWait wait= new WebDriverWait(driver,10);
            wait.until(ExpectedConditions.elementToBeClickable(elementLoginBtn));

            //Step1 log in
            driver.findElement(elementUserID).sendKeys(userName);
            driver.findElement(elementPassword).sendKeys(passWord);
            driver.findElement(elementLoginBtn).click();

            //Step2 search and click on policy
            wait.until(ExpectedConditions.elementToBeClickable(elementSearchBtn));
            driver.findElement(elementPolicyField).sendKeys(policyNumber);
            driver.findElement(elementSearchBtn).click();

            wait.until(ExpectedConditions.elementToBeClickable(elementPolicyLink));

            String element = "dgSearch_" + i + "_1";
            //System.out.println(driver.findElement(By.id(element)).);

            if(driver.findElements(By.id(element)).size() > 0)
            {
                driver.findElement(By.id(element)).click();
                Thread.sleep(1000);
                //getTable(driver);
                driver.findElement(elementSearchBtn).click();
                wait.until(ExpectedConditions.elementToBeClickable(elementPolicyLink));
                i++;
            } else{
                break;
            }
        }
    }
}