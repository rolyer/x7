package x7;

import x7.config.ConfigBuilder;
import x7.core.util.StringUtil;

import java.io.File;

public class ConfigStarter {

	public ConfigStarter(String[] ativeProfiles){

		if (ativeProfiles == null || ativeProfiles.length==0){
			System.out.println("______Load configs of activeProfile: default");
		}else {
			for (String str : ativeProfiles){
				System.out.println("______Load configs of activeProfile: "+str);
			}
		}

		String path = ConfigStarter.class.getClassLoader().getResource("").getPath();
		ConfigBuilder.build(path, ativeProfiles);
	}
}
