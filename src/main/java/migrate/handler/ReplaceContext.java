package migrate.handler;

import java.util.ResourceBundle;

import migrate.dto.Config;
import migrate.util.Constants;

public class ReplaceContext {
	
	private static final ResourceBundle properties = ResourceBundle.getBundle("handler");

	/**
	 * 本质上这里都是创建一个工厂方法，可以用各种方式进行，这里尝试使用配置+反射的方式
	 * @param config
	 */
	@SuppressWarnings("unchecked")
	public void handler(Config config) {
		String handlerClass = properties.getString(config.getType());
		if (handlerClass == null) {
			System.err.println("找不到处理的方法");
		}
		try {
			Class<ReplaceHandler> class1 = (Class<ReplaceHandler>) Class.forName(handlerClass);
			ReplaceHandler replaceHandler = class1.newInstance();
			replaceHandler.handler(config ,Constants.BASE_DIR);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
}
