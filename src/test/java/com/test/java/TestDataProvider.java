package com.test.java;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class TestDataProvider {
	@DataProvider()
	public static Object[][] getLoginTestData() throws IOException {
		return DataUtil.getTestData("LoginTest");
	}

	@DataProvider()
	public static Object[][] getBuildOffer1Data() throws IOException {
		return DataUtil.getTestData("BuildOffer1");
	}

	@DataProvider()
	public static Object[][] getBuildOffer2Data() throws IOException {
		return DataUtil.getTestData("BuildOffer2");
	}
	
	@DataProvider()
	public static Object[][] getBuildOffer3Data() throws IOException {
		return DataUtil.getTestData("BuildOffer3");
	}
	
	
	@DataProvider()
	public static Object[][] getPricingLoginTestData1() throws IOException {
		return DataUtil.getTestData("PricingLoginTest");
	}
	
	@DataProvider()
	public static Object[][] getPricingSheet() throws IOException {
		return DataUtil.getTestData("CreatingPricingSheet");
	}
	
	@DataProvider()
	public static Object[][] getPricingPolicyDetails() throws IOException {
		return DataUtil.getTestData("PricingPolicyDetails");
	}
}
