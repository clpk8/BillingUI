package com.shelter.majesco.stgbilling.test.automationScripts;

import com.shelter.majesco.stgbilling.test.POM.MajescoUIHomePage;
import com.shelter.majesco.stgbilling.test.POM.MajescoUILoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.shelter.majesco.stgbilling.test.automationScripts.SearchPolicy.automationInfo;

//potential way to replace current automation and speed up the test.
public class TableTest {
    public static void main(String[] args) throws InterruptedException {
        String dir = System.getProperty("user.dir");
        dir += "\\src\\main\\java\\com\\shelter\\majesco\\stgbilling\\test\\AutomotationSupportingFiles\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", dir);
        WebDriver driver = new ChromeDriver();
        driver.get(automationInfo.getURL());
        driver.manage().window().maximize();
        WebDriverWait wait= new WebDriverWait(driver,10);

        for (int i = 0 ; i < 90; i++){
        MajescoUILoginPage majescoUILoginPage = new MajescoUILoginPage();
        majescoUILoginPage.login(automationInfo.getUserName(), automationInfo.getPassWord(), driver, wait);
        MajescoUIHomePage majescoUIHomePage = new MajescoUIHomePage();
        majescoUIHomePage.wait = wait;
        Thread.sleep(1000);
        System.out.println(automationInfo.getPolicy());

        //int i = 3;
            automationInfo.setPolicy(Integer.toString(810171100 + i));
            System.out.println("The modifed PN " + automationInfo.getPolicy());
            majescoUIHomePage.searchAndClickPolicy(driver, automationInfo.getPolicy(),wait);

            int numberOfTerm = majescoUIHomePage.getNumberOfPolicy(driver);
            String element = "dgSearch_" + (numberOfTerm-1) + "_1";

            driver.findElement(By.id(element)).click();
            Thread.sleep(1000);

            System.out.println("The number is term is " + numberOfTerm);
            switch (numberOfTerm){
                case 1:
                    majescoUIHomePage.tableTest(driver);
                    break;
                case 2:
                    majescoUIHomePage.tableTest(driver);
                    Thread.sleep(1000);
                    majescoUIHomePage.clickOnDifferentTerm(driver, numberOfTerm-1);
                    Thread.sleep(1000);
                    majescoUIHomePage.tableTest(driver);
                    break;
                case 3:
                    majescoUIHomePage.tableTest(driver);
                    majescoUIHomePage.clickOnDifferentTerm(driver, numberOfTerm-1);
                    Thread.sleep(1000);
                    majescoUIHomePage.tableTest(driver);
                    majescoUIHomePage.clickOnDifferentTerm(driver, numberOfTerm-2);
                    Thread.sleep(1000);
                    majescoUIHomePage.tableTest(driver);
            }
            driver.navigate().refresh();
        }
        driver.close();
        driver.quit();
    }
}