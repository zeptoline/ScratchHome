package com.eteks.ScratchHome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

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

		ServerSocket server = null;
		String line;
		BufferedReader d;
		PrintStream os;

		Socket clientSocket = null;

		try 
		{
			server = new ServerSocket(2016);
		}
		catch (IOException e) 
		{
			System.out.println(e);
		}   


		try {
			clientSocket = server.accept();
			d = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			os = new PrintStream(clientSocket.getOutputStream());

			for (HomePieceOfFurniture fourniture : home.getFurniture()) {
				os.println(String.valueOf(fourniture.hashCode()));
			}
			os.println("QUIT");

			boolean choseFurniture = true;
			int hash = 0;
			int color = 0;
			while(running) {
				
				line = d.readLine();
				
				
				if(choseFurniture) {
					hash = Integer.valueOf(line);
					choseFurniture = false;
				} else {
					if (line.equals("red"))
						color =  -65536;
					else if (line.equals("blue"))
						color = -16776961;
					else if (line.equals("green"))
						color =  -16711936;
					else if (line.equals("black"))
						color = -16777216;
					else if (line.equals("white"))
						color = -1;
					else if (line.equals("gray"))
						color =  -7829368;
					else if (line.equals("yellow"))
						color =-256 ;
					else
						color = 0;

					if(hash != 0 && color != 0) {
						changeColor(hash, color, home);
					}
					choseFurniture = true;

				}
				System.out.println(line); 

			}
		}   
		catch (IOException e) {
			System.out.println(e);
		}
	}

	public void changeColor(int hash, int color, Home home) {
		for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			if(fourniture.hashCode() == hash)
				fourniture.setColor(color);
		}
	}

}
