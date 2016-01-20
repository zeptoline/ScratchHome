package com.eteks.ScratchHome;



import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.plugin.PluginAction;


public class TestAction extends PluginAction {


	private Home home;
	
	private Thread thread = null;
	private Test te = null;

	private boolean running = false;


	public void execute() {
		if(running) {
			System.out.println("-1");
			te.terminate();
			putPropertyValue(Property.NAME, "Status en cours: OFF");
			running = false;

		} else {
			System.out.println("0");
			te = new Test(home);
			thread = new Thread(te);
			thread.start();
			putPropertyValue(Property.NAME, "Status en cours : ON");

			running = true;
		}
	}

	public TestAction(Home home) {
		this.home = home;
		putPropertyValue(Property.NAME, "Test");
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

		String clColor = cl.getColor();

		if (clColor.equals("red"))
			color =  -65536;
		else if (clColor.equals("blue"))
			color = -16776961;
		else if (clColor.equals("green"))
			color =  -16711936;
		else if (clColor.equals("black"))
			color = -16777216;
		else if (clColor.equals("white"))
			color = -1;
		else if (clColor.equals("gray"))
			color =  -7829368;
		else if (clColor.equals("yellow"))
			color =-256 ;

		return color;
	}


	public void changeColor(Home home, int hash, int color) {
		for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			if(fourniture.hashCode() == hash)
				fourniture.setColor(color);
		}

	}


}
