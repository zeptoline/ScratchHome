package src.com.ScratchHome;



import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.plugin.PluginAction;


public class ScratchAction extends PluginAction {

	private boolean instanciate = true;

	private Home home;
	private Thread thread = null;
	private Thread control = null;
	private ControlPanel cp = null;
	private ScratchListener sl = null;

	public void execute() {
		if(instanciate) {
			sl = new ScratchListener(home);
			cp = new ControlPanel(sl);
			thread = new Thread(sl);
			thread.start();
			control = new Thread(cp);
			control.start();
		}
		putPropertyValue(Property.NAME, "Listening Scratch");

	}

	public ScratchAction(Home home) {
		this.home = home;
		putPropertyValue(Property.NAME, "Start listening Scratch");
		putPropertyValue(Property.MENU, "ScratchHome");


		setEnabled(true);
	}




}
