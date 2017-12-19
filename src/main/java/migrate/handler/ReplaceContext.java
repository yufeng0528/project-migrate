package migrate.handler;

import java.util.ResourceBundle;

import migrate.MigrateException;
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
		if (!properties.containsKey(config.getType())) {
			throw new MigrateException("找不到处理的方法 配置类型不正确或者没有加入到handler.properties");
		}
		String handlerClass = properties.getString(config.getType());
		if (handlerClass == null) {
			throw new MigrateException("找不到处理的方法 配置类型不正确或者没有加入到handler.properties或者类名写错了");
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
