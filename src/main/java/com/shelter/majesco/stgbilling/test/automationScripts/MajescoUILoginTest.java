package com.shelter.majesco.stgbilling.test.automationScripts;
import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.AutomationInfo;
import com.shelter.majesco.stgbilling.test.POM.MajescoUIHomePage;
import com.shelter.majesco.stgbilling.test.POM.MajescoUILoginPage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

//test login
public class MajescoUILoginTest {
    public static void main(String[] args) throws InterruptedException {

        AutomationInfo automationInfo = new AutomationInfo();
        String dir = System.getProperty("user.dir");
        dir += "\\src\\main\\java\\com\\shelter\\majesco\\stgbilling\\test\\AutomotationSupportingFiles\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", dir);
        WebDriver driver = new ChromeDriver();
        driver.get(automationInfo.getURL());
        driver.manage().window().maximize();

        WebDriverWait wait= new WebDriverWait(driver,10);

        MajescoUILoginPage majescoUILoginPage = new MajescoUILoginPage();
        majescoUILoginPage.login(automationInfo.getUserName(), automationInfo.getPassWord(), driver,wait);
        //Thread.sleep(3000);
        MajescoUIHomePage majescoUIHomePage = new MajescoUIHomePage();
        majescoUIHomePage.logout(driver);
        driver.close();
        driver.quit();
    }
}
