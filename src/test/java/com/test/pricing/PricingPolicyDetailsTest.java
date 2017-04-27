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
 * Pricing Policy Details class is used to implement the test case for log-in function
 * 
 * @author khirade_n
 */

public class PricingPolicyDetailsTest extends BaseTest {

	@BeforeSuite
	public void beforeTest() {
		test = extentReports.startTest("****Pricing Policy Details Test****");
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
	 * To Validate the test case of Pricing Policy Details feature.
	 * 
	 * @param data
	 */
	@Test(dataProvider = "getPricingPolicyDetails", dataProviderClass = TestDataProvider.class)
	public void getPricingTest(Hashtable<String, String> data) throws Exception {
		expResult = data.get(ADSConstants.EXPECTED_RESULT);
		testCaseName = data.get(ADSConstants.DESCRIPTION);
		System.out.println("***************" + expResult + "------------>" + testCaseName);

		s_assert = new SoftAssert();
		test.log(LogStatus.INFO, data.toString());
		test = extentReports.startTest(data.get(ADSConstants.DESCRIPTION));
		String testCaseRunMode = data.get(ADSConstants.RUNMODE);
		checkTestRunMode("PricingPolicyDetails", testCaseRunMode);

		test.log(LogStatus.INFO, ADSConstants.EXECUTING_TEST_CASE_MESSAGE + testCaseName);
		waitInSecond(2);
		type("loginid_xpath", "opadmin", "opadmin");
		waitInSecond(2);
		type("password_xpath", "opadmin", "opadmin");
		click("signinButton_xpath");
		waitInSecond(2);
		
		click("pricelist_xpath");
		waitInSecond(2);
		
		click("pricepolicydetail_button_xpath");
		String pricingParameter=data.get(ADSConstants.PRICING_PARAMETER);
		
		if(pricingParameter.equals("Group Costs"))
		{
			click("groupcost_tab_xpath");
			waitInSecond(2);
			click("groupcosttabeditbutton_xpath");
			click("groupcosttabeditokbutton_xpath");
			waitInSecond(5);
			click("groupcosttabcheckboxbutton_xpath");
			waitInSecond(5);
			
			click("groupcostdeletebutton_xpath");
			waitInSecond(5);
			click("groupcosttabeditokbutton_xpath");
			waitInSecond(5);
			
			click("groupcosttabsavebutton_xpath");
			
			//click("groupcost_tab_xpath");
			//click("groupcost_tab_xpath");
		}
		else if(pricingParameter.equals("Aggragator Pricing"))
		{
			click("aggregatorpricingtab_xpath");
		}
		else if(pricingParameter.equals("General Settings"))
		{
			click("generalsettingstab_xpath");
			waitInSecond(5);
			click("generalsettingeditbutton_xpath");
			waitInSecond(5);
			click("shortcodebutton_xpath");
			waitInSecond(5);
			click("generalsettingsavebutton_xpath");
			waitInSecond(2);
		}
		else if(pricingParameter.equals("Enterprise Pricing"))
		{
			click("enterprisetab_xpath");
		}
				
		
		waitInSecond(2);
		
		Boolean isSuccess=true;
		checkTestRunMode("PricingPolicyDetails", testCaseRunMode);

		if (isSuccess) {
			actualResult="Pricing Parameter Page will be displayed";
			expResult=data.get(ADSConstants.EXPECTED_RESULT);
				
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
			driver.navigate().back();
			driver.navigate().back();
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
