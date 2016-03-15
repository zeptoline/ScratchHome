package src.com.ScratchHome;



import java.util.HashMap;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.plugin.PluginAction;


public class ScratchAction extends PluginAction {

	private boolean instanciate = true;
	
	private HashMap<String, String> language;
	
	private Home home;
	private Thread thread = null;
	private Thread control = null;
	private ControlPanel cp = null;
	private ScratchListener sl = null;

	public void execute() {
		if(instanciate) {
			cp = new ControlPanel(this, language);
			sl = new ScratchListener(home, cp, language);
			thread = new Thread(sl);
			thread.start();
			control = new Thread(cp);
			control.start();
			
			instanciate = false;
		}
		setEnabled(false);

	}
	public void closeListener() {
		sl.terminate();
	}

	public void reInstanciate() {
		instanciate = true;
		setEnabled(true);
	}
	
	public void reupListener() {
		if (!sl.isRunning()) {
			sl = new ScratchListener(home, cp, language);
			thread = new Thread(sl);
			thread.start();
		}
	}
	
	public ScratchAction(Home home, HashMap<String, String> language) {
		this.home = home;
		this.language = language;
		putPropertyValue(Property.NAME, language.get("ScratchActionMenu"));
		putPropertyValue(Property.MENU, language.get("ScratchHome"));

		setEnabled(true);
	}

	public void recharger(HashMap<String, String> language) {
		this.language = language;
		putPropertyValue(Property.NAME, language.get("ScratchActionMenu"));
		putPropertyValue(Property.MENU, language.get("ScratchHome"));
	}


}
