package com.shelter.majesco.stgbilling.test.POM;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MajescoUILoginPage {

    private static By elementUserID = By.id("userID");
    private static By elementPassword = By.id("userPass");
    private static By elementLoginBtn = By.id("btnLogin");

    public void login(String userName, String passWord, WebDriver driver, WebDriverWait wait){
        //Step1 log in
        wait.until(ExpectedConditions.elementToBeClickable(elementLoginBtn));
        driver.findElement(elementUserID).sendKeys(userName);
        driver.findElement(elementPassword).sendKeys(passWord);
        driver.findElement(elementLoginBtn).click();
    }
}
