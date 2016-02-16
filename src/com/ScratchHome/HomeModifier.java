package com.ScratchHome;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;

public class HomeModifier {
	public static void changeColor(int hash, int color, Home home) {
		for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			if(fourniture.hashCode() == hash)
				fourniture.setColor(color);
		}
	}
}
