package x7;

import x7.config.ConfigBuilder;
import x7.core.util.StringUtil;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ConfigStarter {

	public ConfigStarter(String[] ativeProfiles){

		if (ativeProfiles == null || ativeProfiles.length==0){
			System.out.println("______Load configs of activeProfile: default");
		}else {
			for (String str : ativeProfiles){
				System.out.println("______Load configs of activeProfile: "+str);
			}
		}

		ConfigBuilder.build(ativeProfiles);
	}
}
