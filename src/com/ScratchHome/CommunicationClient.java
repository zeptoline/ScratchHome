package com.ScratchHome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;

public class CommunicationClient implements Runnable {

	private volatile boolean running = true;


	private volatile Socket smtpSocket = null;

	private Home home;
	public CommunicationClient(Home home) {
		this.home = home;
	}




	public void terminate() {
		running = false;
		if(smtpSocket != null) {
			try {
				smtpSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}




	public void run() {


		String line;

		BufferedReader d = null;
		PrintStream os = null;  

		try 
		{
			smtpSocket = new Socket("localhost", 2016);	

			d = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
			os = new PrintStream(smtpSocket.getOutputStream());

		} 
		catch (UnknownHostException e) {
			System.err.println("Don't know about host: hostname");
		} 
		catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: hostname");
		} 


		if (smtpSocket != null && os != null & d!= null) {
			try {

				/*
				 * Lignes pour envoyer les informations sur les meubles

				for (HomePieceOfFurniture fourniture : home.getFurniture()) {
					os.println(fourniture.getName()+"   "+fourniture.hashCode());
				}
				os.print("QUIT");
				///// */


				boolean choseFurniture = true;
				int hash = 0;
				int color = 0;

				//if(d.readLine().equals("BEGIN")) {
				running = true;
				while(running) {

					line = d.readLine();
					System.out.println(line);

					if(choseFurniture) {
						try {
							hash = Integer.valueOf(line);
							choseFurniture = false;
						}
						catch (NumberFormatException e) {
							System.out.println(e);
							choseFurniture = true;
						}
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
				//}

				smtpSocket.close();
				d.close();
				os.close();

			}   
			catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	public void changeColor(int hash, int color, Home home) {
		for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			if(fourniture.hashCode() == hash)
				fourniture.setColor(color);
		}
	}
}
