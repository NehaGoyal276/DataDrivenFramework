package com.w2a.base;

import java.io.FileInputStream;
import com.relevantcodes.extentreports.ExtentReports;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.w2a.utilities.ExcelReader;
import com.w2a.utilities.ExtentManager;
import com.w2a.utilities.TestUtil;

public class TestBase {
	/*
	 * web driver- done Properties - done Logs - log4j .jar, .log, log4j.properties,
	 * logger class - done Extent reports DB Excel Mail ReportNG Jenkins
	 */
	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static Logger log = Logger.getLogger("devpinoyLogger");
	public static ExcelReader excel = new ExcelReader(
			System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;
	public ExtentReports rep = ExtentManager.getInstance();
	public static ExtentTest test;

	@BeforeSuite
	public void setUp() {

		if (driver == null) {

			try {
				fis = new FileInputStream(
						(System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\Config.properties"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				config.load(fis);
				log.debug("Config file loaded!!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				fis = new FileInputStream(
						(System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\OR.properties"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				OR.load(fis);
				log.debug("OR file loaded!!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (config.getProperty("browser").equals("Chrome")) {
				driver = new ChromeDriver();
				log.debug("Chrome launched!!!");
			}

			if (config.getProperty("browser").equals("firefox")) {
				driver = new FirefoxDriver();
				log.debug("Firefox launched!!!");
			}

			if (config.getProperty("browser").equals("ie")) {
				driver = new InternetExplorerDriver();
			}

			driver.get(config.getProperty("testsiteurl"));
			log.debug("Navigated to : " + config.getProperty("testsiteurl"));
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")),
					TimeUnit.SECONDS);
			wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		}

	}

	public void click(String locator) {

		if (locator.endsWith("_CSS")) {

			driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
		}

		else if (locator.endsWith("_XPATH")) {

			driver.findElement(By.xpath(OR.getProperty(locator))).click();
		}

		else if (locator.endsWith("_ID")) {

			driver.findElement(By.id(OR.getProperty(locator))).click();
		}

		test.log(LogStatus.INFO, "Clicking on : " + locator);

	}

	public void type(String locator, String value) {
		
		if (locator.endsWith("_CSS")) {
		driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		}
		
		if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
			}
		
		if (locator.endsWith("_ID")) {
			driver.findElement(By.id(OR.getProperty(locator))).sendKeys(value);
			}
		test.log(LogStatus.INFO, "Typing in : " + locator + "entered value: " + value);
	}
	public void select(String locator, String value) {

		if (locator.endsWith("_CSS")) {

			new Select(driver.findElement(
					By.cssSelector(OR.getProperty(locator))))
					.selectByVisibleText(value);
		}

		else if (locator.endsWith("_XPATH")) {

			new Select(driver.findElement(
					By.xpath(OR.getProperty(locator))))
					.selectByVisibleText(value);
		}

		else if (locator.endsWith("_ID")) {

			new Select(driver.findElement(
					By.id(OR.getProperty(locator))))
					.selectByVisibleText(value);
		}

		test.log(LogStatus.INFO,
				"Selecting from dropdown : "
				+ locator
				+ " value as : "
				+ value);
	}
	public boolean isElementPresent(By by) {

		try {

			driver.findElement(by);
			return true;
		}

		catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public static void verifyEquals(String expected, String actual) throws IOException {
		
		
		try {
			Assert.assertEquals(actual, expected);
			
			}
		catch(Throwable t) {
			TestUtil.captureScreenshot();
				  
			  Reporter.log("<br>"+ "Verification failure : " + t.getMessage() + "<br>");			  
			  Reporter.log("<a target=\"_blank\" href=\""+TestUtil.
			  screenshotName+"\">View Screenshot</a>");
			  Reporter.log("<br>");
			  Reporter.log("<br>");
		}
		
	}
	

	@AfterSuite
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}
		log.debug("Test execution Completed!!!");
	}
}
