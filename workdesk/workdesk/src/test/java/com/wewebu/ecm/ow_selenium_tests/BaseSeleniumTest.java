package com.wewebu.ecm.ow_selenium_tests;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import ch.vorburger.webdriver.reporting.LoggingTestWatchman;
import ch.vorburger.webdriver.reporting.LoggingWebDriverEventListener;
import ch.vorburger.webdriver.reporting.TestCaseReportWriter;

public abstract class BaseSeleniumTest {

    /** driver api */
    private WebDriver driver;

    private static ChromeDriverService service;

    EventFiringWebDriver driverWithReporting;

    protected String baseUrl = "http://localhost:8888/";

    
    static private final TestCaseReportWriter LOG_FILE_WRITER = new TestCaseReportWriter();

    public static final Environment.DriverToUse USED_DRIVER = Environment.DriverToUse.IE;

    public enum DriverToUse
    {
        IE, FIREFOX, CHROME
    };

    @Rule
    public MethodRule logRule = new LoggingTestWatchman(LOG_FILE_WRITER);
    
    public WebDriver getDriver() {
       
        return driver;
    }

    public void SelectBrowser(Environment.DriverToUse drv) throws IOException
    {
        switch (drv)
        {
            case IE:
            {
                DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
                ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                File file = new File("C:/Selenium/IEDriverServer.exe");
                System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
               
                driver = new InternetExplorerDriver(ieCapabilities);
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            }
            case CHROME:
            {
//                //TODO: set path and test with Chrome
//                service = ChromeDriverService.createDefaultService();
//                ChromeOptions options = new ChromeOptions();
//                options.addArguments("--start-maximized");
//                service.start();
//                File file = new File("C:/Selenium/chromedriver.exe");
//                System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
//                
//                driver = new ChromeDriver(service,options);
//                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                break;
            }
            case FIREFOX:
            {
                FirefoxProfile ffProfile = new FirefoxProfile();
                ffProfile.setPreference("browser.safebrowsing.malware.enabled", false);
                driver = new FirefoxDriver(ffProfile);
                driver.manage().window().setPosition(new Point(0, 0));
                java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension dim = new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
                driver.manage().window().setSize(dim);
                break;

            }
        }
    }

    @Before
    public void setUp() throws Exception
    {
        SelectBrowser(USED_DRIVER);
        WebDriverEventListener loggingListener = new LoggingWebDriverEventListener(LOG_FILE_WRITER);
        driverWithReporting = new EventFiringWebDriver(driver);
        driverWithReporting.register(loggingListener);
    }
    
    @After
    public void tearDown() throws Exception
    {
        driver.quit();

     //   if (USED_DRIVER == Environment.DriverToUse.CHROME)
     //       service.stop();
    }


}
