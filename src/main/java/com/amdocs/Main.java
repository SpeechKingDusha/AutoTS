package com.amdocs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static java.lang.System.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        File file = new File("ConfigurationTS.json");
        //File file = new File(".\\src\\main\\java\\com\\Amdocs\\configurationTS.json");

        ConfigTS configTS = new ConfigTS(ConfigTS.readFileConfig(file));

        //WebDriverManager.chromedriver().setup();
        //WebDriver driver = new ChromeDriver();
        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver();
        driver.manage().window().setSize(new Dimension(1920,1080));

        try {

            driver.get(configTS.getUrlBaseAuth());
            out.println(configTS.getUrlBaseAuth());
            driver.navigate().refresh();
        }
        catch (Exception e)
        {
                out.println(e.toString());
                driver.findElement(By.xpath("//body")).sendKeys(Keys.F5);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        var linesTable = driver.findElement(By.id("ctl00_ctl00_ctl33_g_d0c93618_554e_48e4_959d_1efdae192259_TimesheetPartJSGridControl_rightpane_mainTable"))
                .findElement(By.cssSelector("tbody"))
                .findElements(By.cssSelector("tr"));

        populateData (linesTable, driver );

       if (validate(linesTable)) {
           SaveTS (driver);
           if (!configTS.isTestedMode()) {
               approveTS(driver);
           }
       };
    }
    static void SaveTS (WebDriver driver){
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"s");
    }
    static void approveTS(WebDriver driver) throws InterruptedException {
        new Actions(driver).keyDown(Keys.CONTROL)
                .keyDown(Keys.SHIFT)
                .sendKeys("s")
                .keyUp(Keys.SHIFT)
                .keyUp(Keys.CONTROL)
                .build().perform();
        Thread.sleep(1000);
        driver.switchTo().frame(driver.findElement(By.className("ms-dlgFrame")))
                .findElement(By.xpath("//div[@id='divButtons'] "))
                .findElement(By.id("ctl00_ctl00_PlaceHolderMain_idOkButton")).click();
        driver.close();
    }
    static boolean validate (List<WebElement> linesTable ){
        byte countErrors = 0;
        for (int k=1; k<7 && countErrors==0;++k){
            if (!linesTable.get(linesTable.size()-1).findElements(By.cssSelector("td")).get(k).getText().equals(
                    linesTable.get(linesTable.size()-2).findElements(By.cssSelector("td")).get(k).getText())) {
                countErrors++;
            }
        }
        return  countErrors==0;
    }
    static void populateData(List<WebElement> linesTable, WebDriver driver){
        Actions action = new Actions(driver);
        for (int i=2; i < linesTable.size()-2; i+=2){
            for (int k=1;k<7;++k) {
                String value = linesTable.get(i).findElements(By.cssSelector("td")).get(k).getText();
                if (value != null) {
                    action.moveToElement(linesTable.get(i - 1).findElements(By.cssSelector("td")).get(k)).click().pause(400)
                            .sendKeys(value).build().perform();
                }
            }
        }
    }
}
