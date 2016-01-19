package com.eteks.ScratchHome;



import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.plugin.PluginAction;
import com.eteks.sweethome3d.viewcontroller.HomeController;


public class ScratchHomeAction2 extends PluginAction {

	private Home home;
	@SuppressWarnings("unused")
	private HomeController homeCont;
	@SuppressWarnings("unused")
	private UserPreferences preference;

	public void execute() {
		int hashCode = selectFurniture(home);

		int color = getColor();

		changeColor(this.home, hashCode , color);
	}

	public ScratchHomeAction2(Home home, HomeController homeCont, UserPreferences preference) {
		this.home = home;
		this.homeCont = homeCont;
		this.preference = preference;
		putPropertyValue(Property.NAME, "change with selection");
		putPropertyValue(Property.MENU, "ScratchHome");
		// Enables the action by default
		setEnabled(true);
	}

	public int selectFurniture(Home home) {
		FurnitureList li = new FurnitureList();

		return li.getHashFromFurniture(home);

	}
	public int getColor() {
		ColorList cl = new ColorList();
		int color = 0;
		switch (cl.getColor()) {
			case "red" :
				color =  -65536;
				break;
	    case "blue" :
				color = -16776961;
				break;
	    case "green" :
				color =  -16711936;
				break;
	    case "black" :
				color = -16777216;
				break;
	    case "white" :
				color = -1;
				break;
	    case "gray" :
				color =  -7829368;
				break;
	    case "yellow" :
				color =-256 ;
				break;
		default:
			break;
		}
		return color;
	}


	public void changeColor(Home home, int hash, int color) {
		for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			if(fourniture.hashCode() == hash)
				fourniture.setColor(color);
		}

	}


}
