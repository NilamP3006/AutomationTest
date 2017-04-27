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


public class ExecuteBuildOffer1 extends BaseTest {

	@BeforeSuite
	public void beforeTest() {
		test = extentReports.startTest("******Build Offer 1******");
		openBrowserInLoginTest(prop.getProperty(ADSConstants.BROWSER_TYPE),ADSConstants.BUILDORFFER_URL);

	}

	@BeforeMethod
	public void resettingValues() {
		actualResult = ADSConstants.DEFAULT_FAILURE_MESSAGE;
		testFailed = false;
		waitInSecond(1);
		if (extentReports != null) {
			// extentReports.endTest(test);
			extentReports.flush();
		}
	}

	/**
	 * To Validate the test case of Login feature.
	 * 
	 * @param data
	 * @throws TestLinkAPIException
	 */
	@Test(dataProvider = "getBuildOffer1Data", dataProviderClass = TestDataProvider.class)
	public void getBuildOffer1Test(Hashtable<String, String> data) throws Exception {
		expResult = data.get(ADSConstants.EXPECTED_RESULT);
		testCaseName = data.get(ADSConstants.DESCRIPTION);
		System.out.println("***************" + expResult + "------------>" + testCaseName);

		s_assert = new SoftAssert();
		test.log(LogStatus.INFO, data.toString());
		test = extentReports.startTest(data.get(ADSConstants.DESCRIPTION));
		String testCaseRunMode = data.get(ADSConstants.RUNMODE);
		checkTestRunMode("BuildOffer1", testCaseRunMode);

		test.log(LogStatus.INFO, ADSConstants.EXECUTING_TEST_CASE_MESSAGE + testCaseName);

		/*
		 * Login Details
		 */

		type("loginid_xpath", "admin", "admin");
		type("password_xpath", "admin", "admin");
		click("signinButton_xpath");
		waitInSecond(2);
		// waitInSecond(2);

		/*
		 * Dashboard details
		 */

		click("BuildOffer_xpath");

		/*
		 * Create New Offer
		 */
		waitInSecond(2);
		click("CreateNewOfferButton_xpath");

		/*
		 * Build Offer 1 Details --->Select Goal
		 */

		waitInSecond(2);

		selectElement("", "customerName_xpath", data.get(ADSConstants.CUSTOMER_NAME), ADSConstants.CUSTOMER_NAME);
		waitInSecond(2);
		selectElement("", "origincountry_xpath", data.get(ADSConstants.ORIGIN_COUNTRY), ADSConstants.ORIGIN_COUNTRY);
		waitInSecond(2);
		type("projectednumberofmsg_xpath", data.get(ADSConstants.PROJECTED_NUMBER_OF_MESSAGES),
				ADSConstants.PROJECTED_NUMBER_OF_MESSAGES);
		waitInSecond(2);
		selectElement("", "customertype_xpath", data.get(ADSConstants.CUSTOMER_TYPE), ADSConstants.CUSTOMER_TYPE);
		waitInSecond(2);

		// Search Qualifiers Selectors
		selectElement("", "directclassic_xpath", data.get(ADSConstants.DIRECTCLASSIC), ADSConstants.DIRECTCLASSIC);
		waitInSecond(2);
		selectElement("", "customertype_xpath", data.get(ADSConstants.CUSTOMER_TYPE), ADSConstants.CUSTOMER_TYPE);
		selectElement("", "region_xpath", data.get(ADSConstants.REGION), ADSConstants.REGION);
		waitInSecond(2);
		selectElement("", "destinationcountry_xpath", data.get(ADSConstants.DESTINATION_COUNTRY),
				ADSConstants.DESTINATION_COUNTRY);
		waitInSecond(2);
		selectElement("", "destionationoperator_xpath", data.get(ADSConstants.DESTINATION_OPERATOR),
				ADSConstants.DESTINATION_OPERATOR);
		waitInSecond(2);
		click("buildoffer1addbutton_xpath");
		click("savebutton_xpath");
		waitInSecond(2);
		click("findRoutes_xpath");
		waitInSecond(2);

		Boolean isSuccess = checkLoginSuccessful(getElements("buildoffer2selectroute_xpath"));
		expectedResult = data.get(ADSConstants.EXPECTTED_RESULT);

		// checkTestRunMode("Buildoffer1", testCaseRunMode);

		if (isSuccess) {
			actualResult = ADSConstants.SELECT_ROUTE_SUCCESS;

		} else {
			actualResult = ADSConstants.SELECT_ROUTE_FAILURE;

		}
		try {
			if (actualResult.equalsIgnoreCase(expectedResult)) {
				reportPass(testCaseName + ADSConstants.DEFAULT_TEST_PASSED_MESSAGE);
				notes = actualResult;
			} else {
				s_assert.assertEquals(actualResult, expResult, actualResult + " And " + expResult + " Not Match");
				testFailed = true;
				notes = actualResult;
				takeScreenShot();

			}
		} catch (Exception e) {
			notes = actualResult;

		} finally {

			if (testFailed) {
				reportPass(testCaseName + ADSConstants.DEFAULT_TEST_PASSED_MESSAGE);
				notes = actualResult;
				takeScreenShot();
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
		}
	}

	@AfterSuite
	public void aftersuite() throws IOException {

		if (driver != null) {
			driver.quit();
		//	Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
		//	deleteFolder(ADSConstants.LOCAL_TEMP_DIR_PATH);

		}
	}

}
