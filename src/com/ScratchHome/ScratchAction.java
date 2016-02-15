package com.ScratchHome;



import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.plugin.PluginAction;


public class ScratchAction extends PluginAction {
	
	@SuppressWarnings("unused")
	private Home home;
	private Thread thread = null;


	public void execute() {
		ScratchListener sl = new ScratchListener();
		thread = new Thread(sl);
		thread.start();
		
		
		setEnabled(true);
	}

	public ScratchAction(Home home) {
		this.home = home;
		putPropertyValue(Property.NAME, "Start listening Scratch");
		putPropertyValue(Property.MENU, "ScratchHome");

		
		setEnabled(true);
	}




}
