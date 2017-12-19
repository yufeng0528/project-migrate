package migrate;

import java.io.IOException;
import java.util.List;

import migrate.dto.Config;
import migrate.util.ReadExcel;

public class ConfigFactory {

	public  static List<Config> getConfig(String path) {
		List<Config> list = null;
		try {
			list = ReadExcel.readExcel(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
