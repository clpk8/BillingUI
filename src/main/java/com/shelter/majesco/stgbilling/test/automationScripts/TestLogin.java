package com.shelter.majesco.stgbilling.test.automationScripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class TestLogin {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "E:/downloads/javaLib/chromedriver_win32/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("https://mbsu.shelterinsurance.com:8643/MajescoBilling/");

        // login
        WebElement userName = driver.findElement(By.id("userID"));
        userName.sendKeys("user1");

        WebElement password = driver.findElement(By.id("userPass"));
        password.sendKeys("icd123");

        WebElement login = driver.findElement(By.id("btnLogin"));
        login.click();

        //search for policy
        try {
            WebElement policyNo1 = driver.findElement(By.name("SearchValue"));
            policyNo1.sendKeys("712011205");


            WebElement policyNo2 = driver.findElement(By.name("quickSearchValue"));
            policyNo2.sendKeys("712011205");

            WebElement policyNo3 = driver.findElement(By.name("fw.icd.quick.search.policy.no"));
            policyNo3.sendKeys("712011205");
        }
        catch (Exception e){

        }
        //"712011205"
    }
}
