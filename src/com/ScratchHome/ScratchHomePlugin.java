package com.ScratchHome;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.plugin.Plugin;
import com.eteks.sweethome3d.plugin.PluginAction;


public class ScratchHomePlugin extends Plugin {

	public PluginAction[] getActions() {
		Home home = getHome();
		
		return new PluginAction [] {new CommunicationAction(home), new ShowListe(home), new ScratchAction(home)};

	}

}
