package com.test.java;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.test.java.BaseTest;
import com.test.java.ADSConstants;

public class DataUtil {
	private static ExtentTest test;
	private static XSSFWorkbook workbook = null;
	static Map<String, TestCaseMetaData> testCaseMetaDataMap = null;

	static {
		//System.out.println("data util init");
		init();
	}

	static void init() {
		setupTestCaseSuite();
		setupTestCaseData();
	}

	static void setupTestCaseSuite() {
		//System.out.println("1111111");
		String testDateFile = BaseTest.prop.getProperty(ADSConstants.TESTDATA_WORKSHEET_LOCATION);
		 System.out.println(ADSConstants.TESTDATA_WORKSHEET_LOCATION);
		try {
			FileInputStream fis = new FileInputStream(testDateFile);
			workbook = new XSSFWorkbook(fis);
			fis.close();
			int testCasesSheetIndex = workbook.getSheetIndex(ADSConstants.TESTCASES_SHEET_NAME);
			if (testCasesSheetIndex != -1) {
				XSSFSheet sheet = workbook.getSheetAt(testCasesSheetIndex);
				int rowCount = sheet.getPhysicalNumberOfRows();
				if (rowCount > 1) {
					testCaseMetaDataMap = new HashMap<String, TestCaseMetaData>();
					for (int r = 1; r < rowCount; r++) {
						XSSFRow row = sheet.getRow(r);
						String testCaseName = getCellDataAsString(row.getCell(0));
						String runMode = getCellDataAsString(row.getCell(1));
						// System.out.println(testCaseName);
						testCaseMetaDataMap.put(testCaseName, new TestCaseMetaData(testCaseName, runMode));
						System.out.println("  "+testCaseMetaDataMap.toString());
					}
				}
			}
		} catch (Exception e) {
			test.log(LogStatus.ERROR, "Unable to read testdata worksheet");
		}
		// System.out.println("Suite end");
	}

	/*
	 * public static void main(String[] args) { setupTestCaseSuite();
	 * setupTestCaseData(); System.out.println(testCaseMetaDataMap); }
	 */

	private static String getCellDataAsString(XSSFCell cell) {
		String cellValue = "";
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				cellValue = cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				cellValue = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellValue = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellValue = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellValue;

					// System.out.println(cellText);

				}
			}
		}
		return cellValue.trim();
	}

	public static void setupTestCaseData() {

		if (testCaseMetaDataMap != null && testCaseMetaDataMap.size() > 0) {
			int testCasesSheetIndex = workbook.getSheetIndex(ADSConstants.DATA_SHEET_NAME);
			if (testCasesSheetIndex != -1) {
				XSSFSheet sheet = workbook.getSheetAt(testCasesSheetIndex);
				int rowCount = sheet.getPhysicalNumberOfRows();
				if (rowCount > 1) {
					for (int r = 0; r < rowCount;) {
						XSSFRow row = sheet.getRow(r);
						if (row != null) {
							TestCaseMetaData testCaseMetaData = testCaseMetaDataMap
									.get(getCellDataAsString(row.getCell(0)));
							if (testCaseMetaData != null) {
								r++;
								XSSFRow headerRow = sheet.getRow(r);
								r++;
								int rowStart = r;
								int colEnd = 0;
								while (!getCellDataAsString(headerRow.getCell(colEnd)).equals("")) {
									colEnd++;
								}
								while (sheet.getRow(r) != null
										&& !getCellDataAsString(sheet.getRow(r).getCell(0)).equals("")) {
									r++;
								}
								int rowEnd = r;
								Object[][] data = new Object[rowEnd - rowStart][1];
								Hashtable<String, String> table = null;
								for (int rNum = rowStart, dataRow = 0; rNum < rowEnd; rNum++, dataRow++) {
									row = sheet.getRow(rNum);
									table = new Hashtable<String, String>();
									for (int cNum = 0; cNum < colEnd; cNum++) {
										String key = getCellDataAsString(headerRow.getCell(cNum));
										String value = getCellDataAsString(row.getCell(cNum));
										table.put(key, value);
									}
									data[dataRow][0] = table;
								}
								testCaseMetaData.setTestCaseData(data);
							} else {
								r++;
							}
						} else {
							r++;
						}
					}
				}
			}
		}

		// System.out.println("test end");
	}

	public static Object[][] getTestData(String testCaseName) {
		Object[][] data = null;
		//System.out.println(testCaseName);
		TestCaseMetaData testCaseMetaData = testCaseMetaDataMap.get(testCaseName);
		if (testCaseMetaData != null) {
			data = testCaseMetaData.getTestCaseData();
		}
		// System.out.println(data);
		return data;
	}

	public static boolean isRunnable(String testCaseName) {
		boolean runMode = false;
		TestCaseMetaData testCaseMetaData = testCaseMetaDataMap.get(testCaseName);
		if ("Y".equalsIgnoreCase(testCaseMetaData.getRunMode())) {
			runMode = true;
		}
		return runMode;
	}
}
