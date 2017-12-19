package migrate;

import java.util.List;

import migrate.dto.Config;
import migrate.handler.ReplaceContext;
import migrate.util.Constants;

public class App {

	
	public static void main(String[] args) {
		List<Config> configs = ConfigFactory.getConfig(Constants.STUDENT_INFO_XLS_PATH);
		if (configs == null || configs.isEmpty()) {
			System.err.println("没有任何配置!!");
		}
		ReplaceContext replaceContext = new ReplaceContext();
		for (Config config : configs) {
			replaceContext.handler(config);
		}
	}
}
