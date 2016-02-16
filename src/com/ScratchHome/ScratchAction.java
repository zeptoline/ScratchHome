package src.com.ScratchHome;



import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.plugin.PluginAction;


public class ScratchAction extends PluginAction {
	
	private Home home;
	private Thread thread = null;


	public void execute() {
		ScratchListener sl = new ScratchListener(home);
		thread = new Thread(sl);
		thread.start();

		putPropertyValue(Property.NAME, "Listening Scratch");
		
	}

	public ScratchAction(Home home) {
		this.home = home;
		putPropertyValue(Property.NAME, "Start listening Scratch");
		putPropertyValue(Property.MENU, "ScratchHome");

		
		setEnabled(true);
	}




}
