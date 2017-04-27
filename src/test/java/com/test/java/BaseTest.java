package com.test.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Objects;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.test.java.DataUtil;
import com.test.java.ADSConstants;
import com.test.java.ExtentManager;

/**
 * This class used for initializing the browser and have the utility functions
 * inherited by its child classes
 * 
 * @author khirade_n
 */

public class BaseTest {
	public WebDriver driver;
	public static Properties prop;
	public ExtentReports extentReports = ExtentManager.getInstance();
	public ExtentTest test;
	boolean gridRun = true;
	public String notes = null;
	public String result = null;
	public String testCaseName = null;
	public Boolean isErrors = false;
	public String actualResult;
	public String expectedResult;

	public String actualResultSuccess;

	public Boolean testFailed = false;
	public String expResult;
	public SoftAssert s_assert = null;
	public static boolean isLoggedIn = true;
	public static WebDriver existingChromeBrowser = null;
	public static WebDriver existingMozillaBrowser = null;
	public static WebDriver existingIEBrowser = null;
	public static WebDriverWait wait;

	public BaseTest() {
		init();

	}

	/**
	 * Used to initialize the properties files.
	 */
	public void init() {

		if (prop == null) {
			prop = new Properties();
			try {
				FileInputStream fs = new FileInputStream(
						System.getProperty("user.dir") + "//src//test//resources//projectconfiguration.properties");

				prop.load(fs);

			} catch (Exception e) {
				test.log(LogStatus.ERROR, "Error while initializing the properties file");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initializing the Browser
	 * 
	 * @param btype
	 */
	public void openBrowser(String btype,String URL) {
		test.log(LogStatus.INFO, "Initializing " + btype + " browser.");

		if (btype.equalsIgnoreCase(ADSConstants.BROWSER_MOZILLA) && existingMozillaBrowser != null) {
			driver = existingMozillaBrowser;
			return;
		} else if (btype.equalsIgnoreCase(ADSConstants.BROWSER_CHROME) && existingChromeBrowser != null) {
			driver = existingChromeBrowser;
			return;
		} else if (btype.equalsIgnoreCase(ADSConstants.BROWSER_IE) && existingIEBrowser != null) {
			driver = existingIEBrowser;
			return;
		}
/*
		if (btype.equalsIgnoreCase(ADSConstants.BROWSER_MOZILLA)) {
		System.setProperty("webdriver.gecko.driver","./src/test/resources/geckodriver.exe"); 
		ProfilesIni profiles = new ProfilesIni(); 
			FirefoxProfile profile =  profiles.getProfile("default"); 
			driver = new  FirefoxDriver(profile);
			 
			File pathToBinary = new File("C:\\Users\\khirade_n\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
			FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			driver = new FirefoxDriver(ffBinary, firefoxProfile);
		} */
		//else
		if (btype.equalsIgnoreCase(ADSConstants.BROWSER_CHROME)) {
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromeDriver_exe"));
			driver = new ChromeDriver();

		} else if (btype.equalsIgnoreCase(ADSConstants.BROWSER_IE)) {
			System.setProperty("webdriver.ie.driver", prop.getProperty("ieDriver_exe"));
			driver = new InternetExplorerDriver();
		}
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 10);
		test.log(LogStatus.INFO, "Maximizing " + btype + " browser.");
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		navigate(URL);
		//navigate("appUrl");
		//navigate("pricingappUrl");
		doDefaulLogin();
	}

	/**
	 * Initializing the Browser
	 * 
	 * @param btype
	 */
	public void openBrowserInLoginTest(String btype,String URL) {
		test.log(LogStatus.INFO, "Initializing " + btype + " browser.");
		if (btype.equalsIgnoreCase(ADSConstants.BROWSER_MOZILLA)) {
			/*
			 * System.setProperty("webdriver.gecko.driver",
			 * "./src/test/resources/geckodriver.exe"); ProfilesIni profile =
			 * new ProfilesIni(); FirefoxProfile myprofile =
			 * profile.getProfile("default");
			 * //myprofile.setPreference("browser.private.browsing.autostart",
			 * true); driver = new FirefoxDriver(myprofile); driver = new
			 * FirefoxDriver();
			 */
			File pathToBinary = new File("C:\\Users\\khirade_n\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
			FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			driver = new FirefoxDriver(ffBinary, firefoxProfile);
		} else if (btype.equalsIgnoreCase(ADSConstants.BROWSER_CHROME)) {
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromeDriver_exe"));
			driver = new ChromeDriver();
		} else if (btype.equalsIgnoreCase(ADSConstants.BROWSER_IE)) {
			System.setProperty("webdriver.ie.driver", prop.getProperty("ieDriver_exe"));
			driver = new InternetExplorerDriver();
		}
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		test.log(LogStatus.INFO, "Maximizing " + btype + " browser.");
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		navigate(URL);
		//navigate("appUrl");
		//navigate("pricingappUrl");
	}

	/**
	 * Navigating to URL
	 * 
	 * @param urlKey
	 */
	public void navigate(String urlKey) {
		test.log(LogStatus.INFO, "Navigating to " + prop.getProperty(urlKey));
		driver.get(prop.getProperty(urlKey));
	}

	/**
	 * Click on a Web Element
	 * 
	 * @param locatorKey
	 */
	public void click(String locatorKey) {
		test.log(LogStatus.INFO, "Clicking on " + locatorKey);
		getElement(locatorKey).click();
		test.log(LogStatus.INFO, "Clicked successfully on " + locatorKey);
	}

	public void clear(String locatorKey, String data) {
		test.log(LogStatus.INFO, "Clearing " + locatorKey + ". Data - " + data);
		getElement(locatorKey).clear();
		test.log(LogStatus.INFO, "Cleared data successfully " + locatorKey);

	}

	/**
	 * Click on a Web Element
	 * 
	 * @param locatorKey
	 */
	public void click(String locatorKey, String description) {
		test.log(LogStatus.INFO, "Clicking on " + description + ".");
		getElement(locatorKey).click();
		test.log(LogStatus.INFO, "Clicked successfully on " + description);
		waitInSecond(2);

	}

	/**
	 * Writes data in the web element
	 * 
	 * @param locatorKey
	 * @param data
	 */
	public void type(String locatorKey, String data) {
		test.log(LogStatus.INFO, "Typing in " + locatorKey + ". Data - " + data);
		getElement(locatorKey).clear();
		getElement(locatorKey).sendKeys(data);
		test.log(LogStatus.INFO, "Typed successfully in " + locatorKey);

	}

	/**
	 * Writes data in the web element
	 * 
	 * @param locatorKey
	 * @param data
	 */
	public void type(String locatorKey, String data, String desciption) {
		if (data != null && !data.isEmpty()) {
			test.log(LogStatus.INFO, "Typing value in " + desciption + ". Data - " + data);
			getElement(locatorKey).clear();
			getElement(locatorKey).sendKeys(data);
			test.log(LogStatus.INFO, "Typed successfully in " + desciption);
		}
	}

	/**
	 * Finding element and returning it
	 * 
	 * @param locatorKey
	 * @return webElement
	 */
	public WebElement getElement(String locatorKey) {
		WebElement e = null;
		try {
			if (locatorKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_name"))
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else {
				reportFailure("Locator not correct - " + locatorKey);
				Assert.fail("Locator not correct - " + locatorKey);
			}

		} catch (Exception ex) {
			// fail the test and report the error
			reportFailure(locatorKey + " is not found");
			Assert.fail(locatorKey + " is not found");
		}
		return e;
	}

	/**
	 * To get multiple lists
	 * 
	 * @param locatorKey
	 * @return WebElementList
	 */
	public List<WebElement> getElements(String locatorKey) {
		List<WebElement> elementList = null;
		if (locatorKey.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_name"))
			elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else {
			reportFailure("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}

		return elementList;
	}

	/**
	 * Verifying the texts in drop down list
	 * 
	 * @return boolean
	 */
	public boolean verifyTextInDropDownList(String val, String locatorKey) {
		boolean isPresent = false;
		if (val != null && !val.isEmpty()) {
			WebElement element = getElement(locatorKey);
			Select select = new Select(element);

			List<WebElement> allOptions = select.getOptions();
			System.out.println(allOptions.toString());

			for (WebElement el : allOptions)

				if (el.getText().equalsIgnoreCase(val)) {
					System.out.println(el.getText());
					isPresent = true;
					break;
				}

		}
		return isPresent;
	}

	/*********************** Validations ***********************/
	/**
	 * Verifying the title.
	 * 
	 * @return boolean
	 */
	public boolean verifyTitle() {
		return false;
	}

	/**
	 * Checks if the web element present in the web page.
	 * 
	 * @param locatorKey
	 * @return boolean
	 */
	public boolean isElementPresent(String locatorKey) {
		List<WebElement> elementList = null;
		if (locatorKey.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_name"))
			elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else {
			reportFailure("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}

		if (elementList.size() == 0)
			return false;
		else
			return true;
	}

	/**
	 * Compares expected text with Actual text.
	 * 
	 * @param locatorKey
	 * @param expectedTextKey
	 * @return boolean
	 */
	public boolean verifyText(String locatorKey, String expectedTextKey) {
		String actualText = getElement(locatorKey).getText().trim();
		String expectedText = prop.getProperty(expectedTextKey);
		if (actualText.equals(expectedText))
			return true;
		else
			return false;

	}

	/**
	 * Clicks on a web element & waits for 2secs
	 * 
	 * @param locator_clicked
	 * @param locator_pres
	 */
	public void clickAndWait(String locator_clicked, String locator_pres) {
		test.log(LogStatus.INFO, "Clicking and waiting on - " + locator_clicked);
		int count = 5;
		for (int i = 0; i < count; i++) {
			getElement(locator_clicked).click();
			waitInSecond(1);
			if (isElementPresent(locator_pres))
				break;
		}
	}

	/***************************** Reporting ********************************/
	/**
	 * Reporting Pass
	 * 
	 * @param msg
	 */
	public void reportPass(String msg) {
		test.log(LogStatus.PASS, msg);
		takeScreenShot();
	}

	/**
	 * Reporting Failure & Captures screenshot if test fails.
	 * 
	 * @param msg
	 */
	public void reportFailure(String msg) {
		test.log(LogStatus.FAIL, msg);
		takeScreenShot();
		// Assert.fail(msg);
	}

	/**
	 * Captures screenshot.
	 */
	public void takeScreenShot() {
		// fileName of the screenshot
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "//screenshots//" + screenshotFile));
		} catch (IOException e) {
			e.getMessage();
		}
		// put screenshot file in reports
		test.log(LogStatus.INFO, "Screenshot-> "
				+ test.addScreenCapture(System.getProperty("user.dir") + "//screenshots//" + screenshotFile));

	}

	/**
	 * Accepting Alerts
	 */
	public void acceptAlert() {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.alertIsPresent());
		test.log(LogStatus.INFO, "Accepting alert");
		driver.switchTo().alert().accept();
		driver.switchTo().defaultContent();
	}

	/**
	 * Waits for page loading.
	 */
	public void waitForPageToLoad() {
		waitInSecond(1);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String state = (String) js.executeScript("return document.readyState");

		while (!state.equals("complete")) {
			waitInSecond(1);
			state = (String) js.executeScript("return document.readyState");
		}
	}

	/**
	 * Used in case of Thread.Sleep()
	 * 
	 * @param timeToWaitInSec
	 */
	public void waitInSecond(int timeToWaitInSec) {
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get text from a web element.
	 * 
	 * @param locatorKey
	 * @return text
	 */
	public String getText(String locatorKey) {
		test.log(LogStatus.INFO, "Getting text from " + locatorKey);
		return getElement(locatorKey).getText();

	}

	/************************ App functions *****************************/
	/**
	 * Login function which will test the data from excel sheet.
	 * 
	 * @param username
	 * @param password
	 * @return boolean
	 */
	public boolean doLogin(String username, String password) {
		test.log(LogStatus.INFO, "Trying to login with " + username + "," + password);
		type("loginid_xpath", username);
		type("password_xpath", password);
		click("signinButton_xpath");
		waitInSecond(1);
		if (getElement("loggedInUserNameText_xpath").getText().trim().equalsIgnoreCase(username)) {
			test.log(LogStatus.INFO, "Login Success");
			return true;
		} else {
			test.log(LogStatus.INFO, "Login Failed");
			return false;
		}

	}

	/**
	 * To login with default user name & password. The default user name &
	 * password are fetched from the properties file.
	 * 
	 * @return boolean
	 */
	public void doDefaulLogin() {
		test.log(LogStatus.INFO, "Trying to login with " + prop.getProperty("defaultUserName") + ","
				+ prop.getProperty("defaultPassword"));
		waitInSecond(1);
		type("loginid_xpath", prop.getProperty("defaultUserName"));
		type("password_xpath", prop.getProperty("defaultPassword"));
		click("signinButton_xpath");
		waitInSecond(1);
		if (getElement("loggedInUserNameText_xpath").getText().trim()
				.equalsIgnoreCase(prop.getProperty("defaultUserName"))) {
			test.log(LogStatus.INFO, "Login Success");
		} else {
			test.log(LogStatus.FAIL, "Login Failed");
		}
	}

	/**
	 * Selecting web element
	 * 
	 * @param by
	 * @param locatorKey
	 * @param val
	 */
	public void selectElement(String by, String locatorKey, String val) {
		try {
			if (!val.isEmpty()) {
				if (by.equalsIgnoreCase("index")) {

					test.log(LogStatus.INFO, "Selecting the Value at index: " + new Double(val).intValue() + " ");
					new Select(getElement(locatorKey)).selectByIndex(new Double(val).intValue());
					String selectedValue = new Select(getElement(locatorKey)).getFirstSelectedOption().getText();
					test.log(LogStatus.INFO, selectedValue + " selected.");
				} else if (by.equalsIgnoreCase("value"))
					new Select(getElement(locatorKey)).selectByValue(val);
				else
					new Select(getElement(locatorKey)).selectByVisibleText(val);

			}
		} catch (Exception e) {
			reportFailure("Selected value: " + val + " not present in " + locatorKey + "drop down list.");
		}
	}

	/**
	 * Selecting web element
	 * 
	 * @param by
	 * @param locatorKey
	 * @param val
	 */
	public boolean selectElement(String by, String locatorKey, String val, String description) {
		Boolean isSelected = true;
		try {
			if (verifyTextInDropDownList(val, locatorKey)) {
				if (by.equalsIgnoreCase("index")) {
					test.log(LogStatus.INFO, "Selecting by index: " + new Double(val).intValue() + " ");
					Select selected = new Select(getElement(locatorKey));
					selected.selectByIndex(new Double(val).intValue());
					String selectedValue = selected.getFirstSelectedOption().getText();
					test.log(LogStatus.INFO, selectedValue + " selected.");
				} else if (by.equalsIgnoreCase("value")) {
					new Select(getElement(locatorKey)).selectByValue(val);
					test.log(LogStatus.INFO, "Selecting by value: " + new Double(val).intValue() + " ");
				} else {
					test.log(LogStatus.INFO, "Selecting " + description + " by Visible text:  " + val);
					new Select(getElement(locatorKey)).selectByVisibleText(val);
				}

			} else {
				isSelected = false;
			}
		} catch (Exception e) {
			isSelected = false;
			reportFailure("Selected value: " + val + " not present in " + locatorKey + "drop down list.");
		}

		return isSelected;
	}

	/**
	 * Checks the row number in the data grid.
	 * 
	 * @param dataInGrid
	 * @return Integer
	 */
	public Integer getRowNum(String dataInGrid, String locatorKey) {
		List<WebElement> dataColLists = getElements(locatorKey);
		for (int i = 0; i < dataColLists.size(); i++) {
			System.out.println(dataColLists.get(i).getText());
			if (dataColLists.get(i).getText().trim().equals(dataInGrid)) {
				test.log(LogStatus.INFO, "Data found in row num " + (i + 1));
				return (i + 1);
			}
		}
		test.log(LogStatus.INFO, "Data not found ");
		return -1;
	}

	/**
	 * Clicks on a row in the data grid.
	 * 
	 * @param nameInGrid
	 */
	public void clickOnRow(String nameInGrid, String locterString) {
		int rNum = getRowNum(nameInGrid, locterString);
		if (rNum == -1)
			reportFailure("Lead not found " + nameInGrid);
		driver.findElement(By.xpath(prop.getProperty("part1_xpath") + rNum + prop.getProperty("part2_xpath"))).click();

	}

	/**
	 * Selects Date.
	 * 
	 * @param d
	 */
	public boolean selectDate(String locatorKey, String d) {
		Boolean isValidDate = true;
		test.log(LogStatus.INFO, "Selecting the date " + d);
		// convert the string date(input) in date object
		if (!d.isEmpty()) {
			try {
				waitInSecond(2);
				click(locatorKey);
				waitInSecond(2);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				Date dateTobeSelected = sdf.parse(d);
				// Date currentDate = new Date();
				sdf = new SimpleDateFormat("MMM");
				String monthToBeSelected = sdf.format(dateTobeSelected);
				sdf = new SimpleDateFormat("yyyy");
				String yearToBeSelected = sdf.format(dateTobeSelected);
				sdf = new SimpleDateFormat("d");
				String dayToBeSelected = sdf.format(dateTobeSelected);

				while (isValidDate) {
					WebElement yearDropdown = driver
							.findElement(By.xpath("//*[@id='ui-datepicker-div']/div/div/select[2]"));
					Select selectYear = new Select(yearDropdown);
					WebElement selectedYear = selectYear.getFirstSelectedOption();
					String currentSelectedYear = selectedYear.getText();

					WebElement monthDropdown = driver
							.findElement(By.xpath("//*[@id='ui-datepicker-div']/div/div/select[1]"));
					Select selectMonth = new Select(monthDropdown);
					WebElement selectedMonth = selectMonth.getFirstSelectedOption();
					String currentSelectedMonth = selectedMonth.getText();

					String currentSelection = "01-" + currentSelectedMonth + "-" + currentSelectedYear;
					sdf = new SimpleDateFormat("dd-MMM-yyyy");
					Date curentSelectedDate = sdf.parse(currentSelection);
					if (yearToBeSelected.equals(currentSelectedYear)) {
						if (monthToBeSelected.equals(currentSelectedMonth)) {
							/*
							 * waitInSecond(2);
							 * driver.findElement(By.linkText(dayToBeSelected
							 * )).click(); break;
							 */
							// find the calendar
							WebElement dateWidget = driver.findElement(By.id("ui-datepicker-div"));
							List<WebElement> columns = dateWidget.findElements(By.tagName("td"));

							// comparing the text of cell with today's date and
							// clicking it.
							for (WebElement cell : columns) {
								if (cell.getText().equals(dayToBeSelected)) {
									cell.click();
									break;
								}
							}
							break;
						}
					}

					if (curentSelectedDate.compareTo(dateTobeSelected) == 1) {
						// back
						if (driver.findElements(By.xpath("//a[contains(@class, 'ui-state-disabled')]")).size() == 0) {
							click("back_xpath");
							waitInSecond(2);
						}

						else {
							// System.out.println("Cann't GO Back");
							isValidDate = false;
							waitInSecond(2);
							driver.findElement(By.xpath("//*[@id='cont']/div[2]/div[1]/ul/li")).click();
						}

					} else if (curentSelectedDate.compareTo(dateTobeSelected) == -1) {
						// front
						click("forward_xpath");
						waitInSecond(2);
					}
				}
				if (!isValidDate)
					test.log(LogStatus.INFO, "Date Selection Unsuccessful --- " + d);
				else
					test.log(LogStatus.INFO, "Date Selection Successful " + d);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return isValidDate;
	}

	public boolean selectMonthYear(String locatorKey, String d) {
		test.log(LogStatus.INFO, "Selecting Month & Year: " + d);
		// test.log(LogStatus.INFO, "Selecting the date " + d);
		// convert the string date(input) in date object
		Boolean isValidDate = true;
		waitInSecond(2);
		click(locatorKey);
		waitInSecond(2);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy");
		if (!d.isEmpty()) {
			try {
				Date dateTobeSelected = sdf.parse(d);
				Date currentDate = new Date();
				sdf = new SimpleDateFormat("MMM");
				String monthToBeSelected = sdf.format(dateTobeSelected);
				sdf = new SimpleDateFormat("yyyy");
				String yearToBeSelected = sdf.format(dateTobeSelected);

				while (isValidDate) {
					WebElement yearDropdown = driver
							.findElement(By.xpath("//*[@id='ui-datepicker-div']/div/div/select[2]"));
					Select selectYear = new Select(yearDropdown);
					WebElement selectedYear = selectYear.getFirstSelectedOption();
					String currentSelectedYear = selectedYear.getText();

					WebElement monthDropdown = driver
							.findElement(By.xpath("//*[@id='ui-datepicker-div']/div/div/select[1]"));
					Select selectMonth = new Select(monthDropdown);
					WebElement selectedMonth = selectMonth.getFirstSelectedOption();
					String currentSelectedMonth = selectedMonth.getText();

					if (yearToBeSelected.equals(currentSelectedYear)) {
						if (monthToBeSelected.equals(currentSelectedMonth)) {
							// break;
							return isValidDate;
						}

					}

					if (currentDate.after(dateTobeSelected)) {
						// back
						if (driver.findElements(By.xpath("//a[contains(@class, 'ui-state-disabled')]")).size() == 0)
							click("back_xpath");
						else {
							isValidDate = false;
							// return isValidDate;
						}

					} else if (currentDate.before(dateTobeSelected)) {
						// front
						click("forward_xpath");
					}
				}
				if (!isValidDate)
					test.log(LogStatus.INFO, "Date Selection Unsuccessful " + d);
				else
					test.log(LogStatus.INFO, "Date Selection Successful " + d);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return isValidDate;
	}

	/*
	 * public static void reportResultinTestLink(String TestProject, String
	 * TestPlan, String Testcase, String Build, String Notes, String Result)
	 * throws TestLinkAPIException { TestLinkAPIClient api = new
	 * TestLinkAPIClient(ADSConstants.DEVKEY, ADSConstants.URL);
	 * api.reportTestCaseResult(TestProject, TestPlan, Testcase, Build, Notes,
	 * Result); }
	 */
	/**********************************************
	 * 
	 * Application Specific
	 * 
	 **********************************************/
	public void selectCustomer(String locatorKey, String customerName) {
		try {
			// checkErrors(getElelement(id));
			test.log(LogStatus.INFO, "Selecting Customer :" + customerName);
			selectElement(" ", locatorKey, customerName);
		} catch (Exception e) {
			reportFailure("Customer Name Not Present.");
		}
	}

	public void selectInvoiceAccount(String locatorKey, String invoiceAccount) {
		try {
			// checkErrors(getElelement(id));
			test.log(LogStatus.INFO, "Selecting Invoice Account :" + invoiceAccount);
			selectElement(" ", locatorKey, invoiceAccount);
		} catch (Exception e) {
			reportFailure("Invoice Account Not Present.");
		}
	}

	public boolean checkLoginSuccessful(List<WebElement> elements) {
		// List<WebElement> elements = getElements(locatorKey);
		// List<String> errorsList = new ArrayList<String>();
		for (WebElement element : elements) {
			String checkContent = element.getText();
			if (!checkContent.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public boolean validateURL(String expectedURL, String actualURL) {

		if (Objects.equal(actualURL, expectedURL)) {
			return true;
		}

		return false;
	}

	public boolean checkErrors(List<WebElement> elements) {
		// List<WebElement> elements = getElements(locatorKey);
		// List<String> errorsList = new ArrayList<String>();
		for (WebElement element : elements) {
			String errorContent = element.getText();
			if (!errorContent.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public void getErrorsContent(List<WebElement> elements) {
		takeScreenShot();
		List<String> errorsList = new ArrayList<String>();
		for (WebElement element : elements) {
			String errorContent = element.getText();
			if (!errorContent.isEmpty()) {
				errorsList.add(errorContent);
				test.log(LogStatus.INFO, "InLine Errors ------ " + errorContent);
			}
		}
	}

	public void reporting(String tcName, String expectedResult) throws Exception {
		try {
			waitInSecond(1);
			testFailed = false;
			isErrors = checkErrors(getElements("inlineErrors_xpath"));
			if (isErrors) {// check for errors in Step-1
				actualResult = "Please check the inline error messages.";
				getErrorsContent(getElements("inlineErrors_xpath"));
			} else if (getElement("confirmationPopUpText_xpath").isDisplayed()) {
				actualResult = getText("confirmationPopUpText_xpath");
			}
			test.log(LogStatus.INFO, "Actual Result     :" + actualResult);
			test.log(LogStatus.INFO, "Expected Result     :" + expectedResult);
			// TODO : check if below condition is working for null actualResult
			// also
			// if (actualResult!=null &&
			// actualResult.equalsIgnoreCase(expectedResult)) {
			if (actualResult.equalsIgnoreCase(expectedResult)) {
				reportPass(tcName + " test Pass.");
				notes = actualResult;
			} else {
				s_assert.assertEquals(actualResult, expectedResult,
						actualResult + " And " + expectedResult + " Not Match");
				testFailed = true;
				notes = actualResult;
				// reportFailure(data.get(EmdbConstants.DESCRIPTION) +
				// " test Failed.");
			}
		} catch (Exception e) {
			notes = actualResult;

		} finally {

			if (testFailed) {
				reportFailure(tcName + " test Failed.");
			}
			if (getElement("confirmationPopUpText_xpath").isDisplayed())
				click("confirmationPopUpOKBtn_xpath");
		}
	}

	/**
	 * Method to check the Test Run Mode status
	 * 
	 * @param testCaseName
	 * @param testCaseRunMode
	 */
	public void checkTestRunMode(String testCaseName, String testCaseRunMode) throws SkipException {

		if (!DataUtil.isRunnable(testCaseName) || "N".equalsIgnoreCase(testCaseRunMode)) {

			test.log(LogStatus.SKIP, ADSConstants.DEFAULT_SKIP_TEST_MESSAGE);
			actualResult = ADSConstants.DEFAULT_SKIP_TEST_MESSAGE;
			throw new SkipException(actualResult);
		}
	}

	/**
	 * Method to delete the folder if exists
	 * 
	 * @param folderName
	 */
	public void deleteFolder(String folderPath) {
		if (folderPath != null && !folderPath.isEmpty()) {
			File folder = new File(folderPath);
			try {
				if (folder.exists()) {
					FileUtils.cleanDirectory(folder);
					FileUtils.forceDelete(folder);
				}

			} catch (IOException e) {
				System.err.println("Folder is unable to delete");
			}
		}
	}

}
