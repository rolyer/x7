package x7;

import x7.config.ConfigBuilder;
import x7.core.util.StringUtil;

import java.io.File;

public class ConfigStarter {

	public ConfigStarter(String[] ativeProfiles){

		String path = ConfigStarter.class.getClassLoader().getResource("").getPath();
		ConfigBuilder.build(path, ativeProfiles);
	}
}
