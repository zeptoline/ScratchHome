package com.eteks.ScratchHome;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;

public class Test implements Runnable{

	private volatile boolean running = true;

	private Home home;
	public Test(Home home) {
		this.home = home;
	}
	
	
	public void terminate() {
		running = false;
	}

	public void run() {
		
		running = true;
		
		while(running) {
			try {
				
				Thread.sleep(5000);
				changeColor(home);
				
			} catch (InterruptedException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
	}
	
	  public void changeColor(Home home) {
		  for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			fourniture.setColor(-65536);
		}
		  
	  }
	
}
