package migrate.handler;

import migrate.dto.Config;

public interface ReplaceHandler {

	void handler(Config config, String basePath);
}
