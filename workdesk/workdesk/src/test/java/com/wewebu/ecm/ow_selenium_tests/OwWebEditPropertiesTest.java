package com.wewebu.ecm.ow_selenium_tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class OwWebEditPropertiesTest extends BaseSeleniumTest
{
    private boolean acceptNextAlert = true;
    
    private StringBuffer verificationErrors = new StringBuffer();

    
    @Test
    public void testOwWebEditProperties() throws Exception
    {
        driverWithReporting.get(baseUrl + "/workdesk/");
        driverWithReporting.findElement(By.id("loginNameId")).clear();
        driverWithReporting.findElement(By.id("loginNameId")).sendKeys("admin");
        driverWithReporting.findElement(By.id("loginPasswordId")).clear();
        driverWithReporting.findElement(By.id("loginPasswordId")).sendKeys("admin");

        driverWithReporting.findElement(By.cssSelector("input[type=\"button\"]")).click();

//        driverWithReporting.findElement(By.name("Search")).click();
//
//        driverWithReporting.findElement(By.linkText("Doru Sular1")).click();
//
//        driverWithReporting.findElement(By.cssSelector("img.OwFunctionIcon")).click();
//
//        driverWithReporting.findElement(By.cssSelector("input[name=\"Save and Close\"]")).click();
//        driverWithReporting.findElement(By.cssSelector("img.OwFunctionIcon")).click();

        try
        {
            assertTrue(driverWithReporting.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*comment[\\s\\S]*$"));
        }
        catch (Error e)
        {
            verificationErrors.append(e.toString());
        }
      //  driverWithReporting.findElement(By.cssSelector("input[name=\"Save and Close\"]")).click();
     //   driverWithReporting.findElement(By.linkText("Logout")).click();

    }

   

    private boolean isElementPresent(By by)
    {
        try
        {
            driverWithReporting.findElement(by);
            return true;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
    }

    private String closeAlertAndGetItsText()
    {
        try
        {
            Alert alert = super.getDriver().switchTo().alert();
            if (acceptNextAlert)
            {
                alert.accept();
            }
            else
            {
                alert.dismiss();
            }
            return alert.getText();
        }
        finally
        {
            acceptNextAlert = true;
        }
    }

    private String captureScreen()
    {

        String path;
        try
        {

            File source = new File("s");
            source = ((TakesScreenshot) super.getDriver()).getScreenshotAs(OutputType.FILE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            path = "./target/screenshots/" + "capture_" + timeStamp + "" + ".png";
            FileUtils.copyFile(source, new File(path));
            System.out.println("the screenshot printed at:- " + path);
        }
        catch (IOException e)
        {
            path = "Failed to capture screenshot: " + e.getMessage();
        }

        return path;
    }

}
