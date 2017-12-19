package migrate.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import migrate.dto.Config;

public class ReadExcel {

	/**
	 * read the Excel file
	 * 
	 * @param path
	 *            the path of the Excel file
	 * @return
	 * @throws IOException
	 */
	public static List<Config> readExcel(String path) throws IOException {
		if (path == null || FileConstants.EMPTY.equals(path)) {
			return null;
		} else {
			String postfix = getPostfix(path);
			if (!FileConstants.EMPTY.equals(postfix)) {
				if (FileConstants.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				} else if (FileConstants.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsx(path);
				}
			} else {
				System.out.println(path + Constants.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

	/**
	 * Read the Excel 2010
	 * 
	 * @param path
	 *            the path of the excel file
	 * @return
	 * @throws IOException
	 */
	private static List<Config> readXlsx(String path) throws IOException {
		System.out.println(Constants.PROCESSING + path);
		InputStream is = new FileInputStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		Config student = null;
		List<Config> list = new ArrayList<Config>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);

				if (xssfRow != null) {
					student = new Config();
					XSSFCell project = xssfRow.getCell(0);
					XSSFCell dir = xssfRow.getCell(1);
					XSSFCell key = xssfRow.getCell(2);
					XSSFCell value = xssfRow.getCell(3);
					XSSFCell type = xssfRow.getCell(4);

					student.setProject(getValue(project));
					student.setDir(getValue(dir));
					student.setKey(getValue(key));
					student.setValue(getValue(value));
					student.setType(getValue(type));
					list.add(student);
				}
			}
		}
		xssfWorkbook.close();
		return list;
	}

	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param path
	 *            the path of the Excel
	 * @return
	 * @throws IOException
	 */
	private static List<Config> readXls(String path) throws IOException {
		System.out.println(Constants.PROCESSING + path);
		InputStream is = new FileInputStream(path);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Config student = null;
		List<Config> list = new ArrayList<Config>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			Config lastConfig = null;
			
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					student = new Config();
					HSSFCell project = hssfRow.getCell(0);
					HSSFCell dir = hssfRow.getCell(1);
					HSSFCell key = hssfRow.getCell(2);
					HSSFCell value = hssfRow.getCell(3);
					HSSFCell type = hssfRow.getCell(4);

					if (StringUtils.isEmpty(getValue(project)) && lastConfig != null) {
						student.setProject(lastConfig.getProject());
					} else {
						student.setProject(getValue(project));
					}
					if (StringUtils.isEmpty(getValue(dir)) && lastConfig != null) {
						student.setDir(lastConfig.getDir());
					} else {
						student.setDir(getValue(dir));
					}
					if (StringUtils.isEmpty(getValue(key)) && lastConfig != null) {
						student.setKey(lastConfig.getKey());
					} else {
						student.setKey(getValue(key));
					}
					if (StringUtils.isEmpty(getValue(value)) && lastConfig != null) {
						student.setValue(lastConfig.getValue());
					} else {
						student.setValue(getValue(value));
					}
					if (StringUtils.isEmpty(getValue(type)) && lastConfig != null) {
						student.setType(lastConfig.getType());
					} else {
						student.setType(getValue(type));
					}
					
					lastConfig = student;
					
					list.add(student);
				}
			}
		}
		hssfWorkbook.close();
		return list;
	}

	@SuppressWarnings("static-access")
	private static String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	@SuppressWarnings("static-access")
	private static String getValue(HSSFCell hssfCell) {
		if (hssfCell == null) {
			return "";
		}
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	public static String getPostfix(String path) {
		if (path == null || FileConstants.EMPTY.equals(path.trim())) {
			return FileConstants.EMPTY;
		}
		if (path.contains(FileConstants.POINT)) {
			return path.substring(path.lastIndexOf(FileConstants.POINT) + 1, path.length());
		}
		return FileConstants.EMPTY;
	}

	public static void main(String[] args) throws IOException {
		String excel2003_2007 = Constants.STUDENT_INFO_XLS_PATH;
//		String excel2010 = Constants.STUDENT_INFO_XLSX_PATH;
		// read the 2003-2007 excel
		List<Config> list = ReadExcel.readExcel(excel2003_2007);
		if (list != null) {
			for (Config student : list) {
				System.out.println(student.getProject() + "-" + student.getDir() + "-" + student.getKey() + "-" + student.getValue());
			}
		}
//		System.out.println("======================================");
//		// read the 2010 excel
//		List<Config> list1 = new ReadExcel().readExcel(excel2010);
//		if (list1 != null) {
//			for (Config student : list1) {
//				System.out.println(student.getKey());
//			}
//		}
	}
}
