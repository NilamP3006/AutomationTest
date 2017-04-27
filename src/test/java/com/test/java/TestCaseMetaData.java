package com.test.java;

/**
 * @author khirade_n
 *
 */
public class TestCaseMetaData {
	private String testCaseName;
	private String runMode;
	private Object[][] testCaseData;

	public TestCaseMetaData(String testCaseName, String runMode) {
		this.testCaseName = testCaseName;
		this.runMode = runMode;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public String getRunMode() {
		return runMode;
	}

	public void setRunMode(String runMode) {
		this.runMode = runMode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (testCaseData != null && testCaseData.length > 0) {
			for (int i = 0; i < testCaseData.length; i++) {
				sb.append((testCaseData[i][0]).toString() + " | ");
			}
		}
		return "TestCaseMetaData [testCaseName=" + testCaseName + ", runMode=" + runMode + ", testCaseData="
				+ sb.toString() + "]";

	}

	public Object[][] getTestCaseData() {
		return testCaseData;
	}

	public void setTestCaseData(Object[][] testCaseData) {
		this.testCaseData = testCaseData;
	}

}
