package migrate.util;

import java.util.ResourceBundle;

public class Constants {

	private static final ResourceBundle properties = ResourceBundle.getBundle("config");
	
	public static final String BASE_DIR = properties.getString("base.dir");
	
	public static final String PROJECT_NAME = properties.getString("project");
	

    public static final String STUDENT_INFO_XLS_PATH = properties.getString("xls_path");
    public static final String STUDENT_INFO_XLSX_PATH = properties.getString("xlsx_path");
    public static final String NOT_EXCEL_FILE = " : Not the Excel file!";
    public static final String PROCESSING = "Processing...";
}
