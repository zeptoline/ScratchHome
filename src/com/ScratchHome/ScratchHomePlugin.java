package src.com.ScratchHome;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.eteks.sweethome3d.io.FileUserPreferences;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.plugin.Plugin;
import com.eteks.sweethome3d.plugin.PluginAction;


public class ScratchHomePlugin extends Plugin{
	private static final String     APPLICATION_PLUGINS_SUB_FOLDER = "plugins";


	public PluginAction[] getActions() {



		final HashMap<String, String> language = new HashMap<String, String>(); 

		getLanguage(language);
		if (language.isEmpty())
			return new PluginAction [] {};
		
		Home home = getHome();
		

		Properties properties = new Properties();
		try {
			File [] applicationPluginsFolders = ((FileUserPreferences) getUserPreferences())
					.getApplicationSubfolders(APPLICATION_PLUGINS_SUB_FOLDER);
			properties.load(new FileInputStream(applicationPluginsFolders[0].getPath()+"/general.properties"));
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final ScratchAction sa = new ScratchAction(home, language);
		final JSONAction jsa = new JSONAction(home, language, properties);
		
		getUserPreferences().addPropertyChangeListener(UserPreferences.Property.LANGUAGE, new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent arg0) {
				getLanguage(language);
				
				sa.recharger(language);
				jsa.recharger(language);
			}
		});


		return new PluginAction [] {sa, jsa};

	}


	private void getLanguage(HashMap<String, String> language) {
		String lang = "en";

		Properties langprop = new Properties();
		Properties properties = new Properties();
		BufferedReader input_lang = null;
		UserPreferences userPreferences = getUserPreferences();
		
		//language.clear();


		try {

			if (userPreferences instanceof FileUserPreferences) {
				File [] applicationPluginsFolders = ((FileUserPreferences) userPreferences)
						.getApplicationSubfolders(APPLICATION_PLUGINS_SUB_FOLDER);


				// load a properties file
				//prop.load(input);
				//lang = prop.getProperty("language");
				
				lang = userPreferences.getLanguage();

				File folder = new File(applicationPluginsFolders[0].getPath());
				File[] listOfFiles = folder.listFiles();
				ArrayList<String> arr = new ArrayList<String>();
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if(listOfFiles[i].getName().startsWith("language_")) {
							arr.add(listOfFiles[i].getName().substring(9, listOfFiles[i].getName().indexOf(".")));
						}
					}
				}	
				if(!arr.contains(lang)) {
					JOptionPane.showMessageDialog(null, "Your language does not have a properties file in your plugin folder.\n Please add a file named language_"+lang+".properties in the folder : "+applicationPluginsFolders[0].getPath()+", and complete it.", "Language not supported", JOptionPane.INFORMATION_MESSAGE);
					
					properties.load(new FileInputStream(applicationPluginsFolders[0].getPath()+"/general.properties"));
					lang = properties.getProperty("language");
				}

				//input_lang = new FileInputStream();
				input_lang = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(applicationPluginsFolders[0].getPath()+"/language_"+lang+".properties"), "UTF8"));
				langprop.load(input_lang);

				Enumeration<?> e = langprop.propertyNames();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					String value = langprop.getProperty(key);
					language.put(key, value);
				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input_lang != null) {
				try {
					input_lang.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}




}
