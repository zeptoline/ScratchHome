package src.com.ScratchHome;



import java.util.HashMap;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.plugin.PluginAction;

/**
 * Allow the listening of Scratch' actions and modifications on SH3D scene
 * 
 */
public class ScratchAction extends PluginAction {

	private boolean instanciate = true;
	
	private HashMap<String, String> language;
	
	private Home home;
	private Thread thread = null;
	private Thread control = null;
	private ControlPanel controlPanel = null;
	private ScratchListener scratchListener = null;

	/**
	 * Method called by launching ScratchAction in the plugin menu. If not launched already, launch the server listening Scratc actions.
	 * 
	 */
	public void execute() {
		//instanciate is true when the server is already launched
		if(instanciate) {
			controlPanel = new ControlPanel(this, language);
			scratchListener = new ScratchListener(home, controlPanel, language);
			thread = new Thread(scratchListener);
			thread.start();
			control = new Thread(controlPanel);
			control.start();
			
			instanciate = false;
		}
		setEnabled(false);

	}
	
	/**
	 * Method to close the server
	 * 
	 */
	public void closeListener() {
		scratchListener.terminate();
	}

	/**
	 * Method that allow to instanciate again the server
	 * 
	 */
	public void reInstanciate() {
		instanciate = true;
		setEnabled(true);
	}
	
	/**
	 * Metho that launch again the server
	 * 
	 */
	public void reupListener() {
		if (!scratchListener.isRunning()) {
			scratchListener = new ScratchListener(home, controlPanel, language);
			thread = new Thread(scratchListener);
			thread.start();
		}
	}
	
	/**
	 * ScratchAction constructor
	 * 
	 * @param home representing the 3D scene. 
	 * @param language the list of plugin languages.
	 */
	public ScratchAction(Home home, HashMap<String, String> language) {
		this.home = home;
		this.language = language;
		putPropertyValue(Property.NAME, language.get("ScratchActionMenu"));
		putPropertyValue(Property.MENU, language.get("ScratchHome"));

		setEnabled(true);
	}

	/**
	 * Method to reload plugin language 
	 * 
	 * @param language 
	 */
	public void recharger(HashMap<String, String> language) {
		this.language = language;
		putPropertyValue(Property.NAME, language.get("ScratchActionMenu"));
		putPropertyValue(Property.MENU, language.get("ScratchHome"));
	}


}
