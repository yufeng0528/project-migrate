package migrate.handler;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.StringUtils;

import migrate.dto.Config;
import migrate.util.Constants;
import migrate.util.FileConstants;

public class JavaReplaceHandler implements ReplaceHandler{

	private String basePath;
	
	private Config config;
	
	public void handler(Config config, String basePath) {
		this.config = config;
		this.basePath = basePath;
		find();
	}
	
	private void find() {
		File baseDirectory = new File(basePath + "/" + config.getProject());
		Iterator<File> tagetFiles = FileUtils.iterateFiles(baseDirectory, new IOFileFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return true;
			}
			
			@Override
			public boolean accept(File file) {
				if (file.getName().startsWith(".")) {
					return false;
				}
				String suffix = FilenameUtils.getExtension(file.getName());
				if (suffix.equals(FileConstants.JAVA_SUFFIX)) {
					if (file.getName().equals(config.getDir()+ FileConstants.POINT + FileConstants.JAVA_SUFFIX)) {
						return true;
					}
					return false;
				}
				return false;
			}
		}, new TargetFileFilter());
		
		while (tagetFiles.hasNext()) {
			File file = (File) tagetFiles.next();
			// 内存流, 作为临时流  
			CharArrayWriter  tempStream = new CharArrayWriter();  
			
			System.out.println("--" + file.getAbsolutePath());
			try {
				List<String> lines = FileUtils.readLines(file, "UTF-8");
				for (String line : lines) {
					if (line.contains(config.getKey()) && StringUtils.isNotBlank(config.getValue())) {
						String[] values = line.split("=");
						tempStream.write(values[0] + "= " + config.getValue() + ";");  
					} else {
						tempStream.write(line);  
					}
					tempStream.write(FileConstants.LINE_SEPERATOR);
				}
				// 将内存中的流 写入 文件  
				FileWriter out = new FileWriter(file);
				tempStream.writeTo(out);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	public static void main(String[] args) {
		JavaReplaceHandler replaceHandler = new JavaReplaceHandler();
		Config config = new Config();
		config.setDir("Constants");
		config.setKey("QRCODE_TAG");
		config.setValue("\"maiti\"");
		config.setProject("mtfix-account-center");
		replaceHandler.handler(config, Constants.BASE_DIR);
	}
}
