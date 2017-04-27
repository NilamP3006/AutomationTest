package com.test.pricing;

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
 * Pricing Test class is used to implement the test case for log-in function
 * 
 * @author khirade_n
 */

public class PricingTest extends BaseTest {

	@BeforeSuite
	public void beforeTest() {
		test = extentReports.startTest("******Create Pricing Sheet Test******");
		openBrowserInLoginTest(prop.getProperty(ADSConstants.BROWSER_TYPE),ADSConstants.PRICINGAPPURL);

	}

	@BeforeMethod
	public void resettingValues() {
		actualResult = ADSConstants.DEFAULT_FAILURE_MESSAGE;
		testFailed = false;
		waitInSecond(1);
		if (extentReports != null) {
			extentReports.endTest(test);
			extentReports.flush();
		}
	}

	/**
	 * To Validate the test case of creating Pricing Sheet feature.
	 * 
	 * @param data
	 * @throws TestLinkAPIException
	 */
	@Test(dataProvider = "getPricingSheet", dataProviderClass = TestDataProvider.class)
	public void getPricingTest(Hashtable<String, String> data) throws Exception {
		expResult = data.get(ADSConstants.EXPECTED_RESULT);
		testCaseName = data.get(ADSConstants.DESCRIPTION);
		System.out.println("***************" +testCaseName + "------------>" + expResult);

		s_assert = new SoftAssert();
		test.log(LogStatus.INFO, data.toString());
		test = extentReports.startTest(data.get(ADSConstants.DESCRIPTION));
		String testCaseRunMode = data.get(ADSConstants.RUNMODE);
		checkTestRunMode("CreatingPricingSheet", testCaseRunMode);

		test.log(LogStatus.INFO, ADSConstants.EXECUTING_TEST_CASE_MESSAGE + testCaseName);
		waitInSecond(2);
		type("loginid_xpath", "opadmin", "opadmin");
		waitInSecond(2);
		type("password_xpath", "opadmin", "opadmin");
		click("signinButton_xpath");
		waitInSecond(2);

		click("pricelist_xpath");
		waitInSecond(2);
		if (testCaseName.equalsIgnoreCase("Verify the title")) {
			actualResult = driver.getTitle();
		} else {
			click("createnewpricesheetbutton_xpath");
			waitInSecond(2);
			type("pricesheetname_xpath", data.get(ADSConstants.PRICE_SHEET_NAME), ADSConstants.PRICE_SHEET_NAME);
			waitInSecond(2);
			selectElement("", "pricinglist_template_xpath", data.get(ADSConstants.PRICING_LIST_TEMPLATE),
					ADSConstants.PRICING_LIST_TEMPLATE);
			waitInSecond(2);
			selectElement("", "pricinglist_template_xpath", data.get(ADSConstants.PRICING_LIST_TEMPLATE),
					ADSConstants.PRICING_LIST_TEMPLATE);
			waitInSecond(2);
			selectElement("", "pricingpolicy_xpath", data.get(ADSConstants.PRICING_POLICY),
					ADSConstants.PRICING_POLICY);

			waitInSecond(2);

			click("savepricesheetbutton_xpath");
			waitInSecond(2);

			Boolean isSuccess = checkLoginSuccessful(getElements("datadisplaysuccess_xpath"));
			Boolean isErrors = checkErrors(getElements("datadisplayfailure_xpath"));

			checkTestRunMode("CreatingPricingSheet", testCaseRunMode);

			if (isSuccess) {
				click("selectallcheckbx_xpath");
				waitInSecond(2);
				actualResult = "Data should be saved successfully in grid";
				click("activatebutton_xpath");
				click("activateokbutton_xpath");

			}
			if (isErrors) {
				actualResult = "Mandatory fields errors should be displayed";

			}
		}
		try {
			test.log(LogStatus.INFO, "Actual Result ------- " + actualResult);
			test.log(LogStatus.INFO, "Expected Result ------- " + expResult);
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
			driver.navigate().back();
			driver.navigate().back();
			driver.navigate().back();
		}
	}

	@AfterSuite
	public void aftersuite() throws IOException {

		if (driver != null) {
			driver.quit();
			// deleteFolder(ADSConstants.LOCAL_TEMP_DIR_PATH);

		}
	}

}
