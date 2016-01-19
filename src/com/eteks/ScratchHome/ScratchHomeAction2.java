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
		changeColor(this.home,137880095 );
	}

	public ScratchHomeAction2(Home home, HomeController homeCont, UserPreferences preference) {
		this.home = home;
		this.homeCont = homeCont;
		this.preference = preference;
		putPropertyValue(Property.NAME, "Change Color to blue");
		putPropertyValue(Property.MENU, "ScratchHome");
		// Enables the action by default
		setEnabled(true);
	} 

	public int selectFurniture(Home home) {
		
		
		return 0;
		
	}
	
	  /**
	   * Displays a content chooser save dialog to choose the name of a home.
	   */
	  public String showSaveDialog(String homeName) {
	    return homeCont.getContentManager().showSaveDialog(homeCont.getView(),
	        this.preference.getLocalizedString(HomePane.class, "exportToOBJDialog.title"), 
	        ContentManager.ContentType.OBJ, homeName);
	  }
	  
	  public void changeColor(Home home, int hash) {
		  for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			  if(fourniture.hashCode() == hash)
				  fourniture.setColor( -16776961);
		}
		  
	  }
	  

}
