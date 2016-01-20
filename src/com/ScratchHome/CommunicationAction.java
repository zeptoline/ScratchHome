package com.ScratchHome;



import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.plugin.PluginAction;


public class CommunicationAction extends PluginAction {


	private Home home;
	
	private Thread thread = null;
	private CommunicationClient te = null;

	private boolean running = false;


	public void execute() {
		if(running) 
		{
			System.out.println("-1");
			te.terminate();
			putPropertyValue(Property.NAME, "Status en cours: OFF");
			running = false;

		} 
		else 
		{
			
			System.out.println("0");
			if (te == null)
				te = new CommunicationClient(home);
			thread = new Thread(te);
			thread.start();
			putPropertyValue(Property.NAME, "Status en cours : ON");

			running = true;
		}
	}

	public CommunicationAction(Home home) {
		this.home = home;
		putPropertyValue(Property.NAME, "Test");
		putPropertyValue(Property.MENU, "ScratchHome");
		// Enables the action by default
		setEnabled(true);
	}




}
