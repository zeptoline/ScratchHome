package com.ScratchHome;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.plugin.PluginAction;

public class ShowListe extends PluginAction{
	private Home home;

	public void execute() {

		for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			System.out.println(fourniture.getName()+"   "+fourniture.hashCode());
		}
	}

	

	public ShowListe(Home home) {
		this.home = home;
		putPropertyValue(Property.NAME, "Show Liste of furnitures");
		putPropertyValue(Property.MENU, "ScratchHome");
		// Enables the action by default
		setEnabled(true);
	} 
}
