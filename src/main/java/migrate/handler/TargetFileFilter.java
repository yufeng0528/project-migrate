package migrate.handler;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

import migrate.util.FileConstants;

public class TargetFileFilter implements IOFileFilter {

	@Override
	public boolean accept(File file) {
		if (file.getName().startsWith(FileConstants.POINT)) {
			return false;
		}
		
		if (file.getName().equals(FileConstants.CLASS_DIR_NAME)) {
			return false;
		}
		if (file.getName().equals(FileConstants.TAGRGET_DIR_NAME)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean accept(File dir, String name) {
		return true;
	}

}
