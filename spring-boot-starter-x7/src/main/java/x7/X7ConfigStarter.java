package x7;


import org.springframework.core.env.Environment;
import x7.config.ConfigBuilder;
import x7.core.config.Configs;


public class X7ConfigStarter {

	public X7ConfigStarter (Environment environment){

		Configs.setEnvironment(environment);

		String[] ativeProfiles = environment.getActiveProfiles();

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
