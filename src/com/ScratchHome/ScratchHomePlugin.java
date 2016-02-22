package src.com.ScratchHome;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.plugin.Plugin;
import com.eteks.sweethome3d.plugin.PluginAction;


public class ScratchHomePlugin extends Plugin {

	public PluginAction[] getActions() {
		

		Properties prop = new Properties();
		Properties prop2 = new Properties();
		InputStream input = null;
		InputStream input_lang = null;
		HashMap<String, String> language = new HashMap<String, String>(); 

		String lang = "_en";
		
		try {

			input = new FileInputStream("language.properties");
			// load a properties file
			prop.load(input);
			lang = prop.getProperty("language");

			input_lang = new FileInputStream("language"+lang+".properties");
			prop2.load(input_lang);
			
			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				language.put(key, value);
			}
			

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (input_lang != null) {
				try {
					input_lang.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		Home home = getHome();
		
		return new PluginAction [] {new ScratchAction(home, language), new JSONAction(home, language)};

	}

}
