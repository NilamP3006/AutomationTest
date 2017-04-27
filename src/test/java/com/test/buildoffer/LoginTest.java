package com.test.buildoffer;

import java.io.IOException;
import java.util.Hashtable;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import com.test.java.ADSConstants;
import com.test.java.BaseTest;
import com.test.java.TestDataProvider;

/**
 * LoginTest class is used to implement the test case for log-in function
 * 
 * @author khirade_n
 */

public class LoginTest extends BaseTest {

	@BeforeSuite
	public void beforeTest() {
		test = extentReports.startTest("******Login Test******");
		openBrowserInLoginTest(prop.getProperty(ADSConstants.BROWSER_TYPE),ADSConstants.BUILDORFFER_URL);

	}

	@BeforeMethod
	public void resettingValues() {
		actualResult = ADSConstants.DEFAULT_FAILURE_MESSAGE;
		testFailed = false;
		waitInSecond(1);
		if (extentReports != null) {
			//extentReports.endTest(test);
			extentReports.flush();
		}
	}

	/**
	 * To Validate the test case of Login feature.
	 * 
	 * @param data
	 * @throws TestLinkAPIException
	 */
	@Test(dataProvider = "getLoginTestData", dataProviderClass = TestDataProvider.class)
	public void getLoginTest(Hashtable<String, String> data) throws Exception {
		expResult = data.get(ADSConstants.EXPECTED_RESULT);
		testCaseName = data.get(ADSConstants.DESCRIPTION);
		System.out.println("***************" + expResult + "------------>" + testCaseName);

		s_assert = new SoftAssert();
		test.log(LogStatus.INFO, data.toString());
		test = extentReports.startTest(data.get(ADSConstants.DESCRIPTION));
		String testCaseRunMode = data.get(ADSConstants.RUNMODE);
		checkTestRunMode("LoginTest", testCaseRunMode);

		test.log(LogStatus.INFO, ADSConstants.EXECUTING_TEST_CASE_MESSAGE + testCaseName);

		type("loginid_xpath", data.get(ADSConstants.USERNAME), ADSConstants.USERNAME);
		type("password_xpath", data.get(ADSConstants.PASSWORD), ADSConstants.PASSWORD);
		click("signinButton_xpath");
		waitInSecond(2);

		Boolean isSuccess = checkLoginSuccessful(getElements("loginPageInSuccessPage_xpath"));
		Boolean isErrors = checkErrors(getElements("loginPageInLineError_xpath"));

		checkTestRunMode("LoginTest", testCaseRunMode);

		if (isSuccess) {
			actualResult = getElement("loginPageInSuccessPage_xpath").getText();

		}
		if (isErrors) {
			actualResult = getElement("loginPageInLineError_xpath").getText();

		}

		try {
			if (actualResult.equalsIgnoreCase(expResult)) {
				reportPass(testCaseName + ADSConstants.DEFAULT_TEST_PASSED_MESSAGE);
				notes = actualResult;
			} else {
				s_assert.assertEquals(actualResult, expResult, actualResult + " And " + expResult + " Not Match");
				testFailed = true;
				notes = actualResult;

			}
		} catch (Exception e) {
			notes = actualResult;
			test.log(LogStatus.ERROR, notes);
		} finally {

			if (testFailed) {
				reportFailure(testCaseName + ADSConstants.DEFAULT_TEST_FAILED_MESSAGE);
			}
		}
		s_assert.assertAll();

	}

	@AfterMethod
	public void afterMethod() throws Exception {

		if (isLoggedIn) {
			extentReports.flush();
			driver.navigate().back();
		}
	}

	@AfterSuite
	public void aftersuite() throws IOException {

		if (driver != null) {
			driver.quit();
			//deleteFolder(ADSConstants.LOCAL_TEMP_DIR_PATH);

		}
	}

}
