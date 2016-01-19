package com.eteks.ScratchHome;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.plugin.PluginAction;
import com.eteks.sweethome3d.swing.HomePane;
import com.eteks.sweethome3d.viewcontroller.ContentManager;
import com.eteks.sweethome3d.viewcontroller.HomeController;


public class ScratchHomeAction2 extends PluginAction {

	private Home home;
	private HomeController homeCont;
	private UserPreferences preference;

	public void execute() {
		int hashCode = selectFurniture(home);

		for (HomePieceOfFurniture fourniture : home.getFurniture()) 
			System.out.println(fourniture.hashCode());
		changeColor(this.home, hashCode );
	}

	public ScratchHomeAction2(Home home, HomeController homeCont, UserPreferences preference) {
		this.home = home;
		this.homeCont = homeCont;
		this.preference = preference;
		putPropertyValue(Property.NAME, "change to blue with selection");
		putPropertyValue(Property.MENU, "ScratchHome");
		// Enables the action by default
		setEnabled(true);
	} 

	public int selectFurniture(Home home) {
		FurnitureList li = new FurnitureList();

		return li.getHashFromFurniture(home);

	}


	public void changeColor(Home home, int hash) {
		for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			if(fourniture.hashCode() == hash)
				fourniture.setColor( -16776961);
		}

	}


}
