package com.ScratchHome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;



public class TestServer {

	public static void main(String[] args) {

		BufferedReader is;
		PrintStream os;
		ServerSocket server = null;
		Scanner sc = new Scanner(System.in);

		try 
		{
			server = new ServerSocket(2016);
		}
		catch (IOException e) 
		{
			System.out.println(e);
		}   


		Socket clientSocket = null;
		try {
			while(true) {
				
				System.out.println("Waiting for connection");
				clientSocket = server.accept();
				System.out.println("Got a connection from : "+clientSocket.getLocalAddress());
			
				is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				os = new PrintStream(clientSocket.getOutputStream());

				String responseLine;
				while ((responseLine = is.readLine()) != null) {
					if (responseLine.equals("QUIT")) 
						break;
					System.out.println("Client: " + responseLine);
				}

				String text = "";
				boolean conn = true;
				os.println("BEGIN");
				
				
				while (conn = !os.checkError()) {
					System.out.println("Donnez le prochain envois :");
					text = sc.nextLine();
					if(text.equals("end")) {
						break;
					}
					os.println(text);
					
				}
				if (!conn) {
					System.out.println("Connection probablement perdu");
				}

				clientSocket.close();
				is.close();
				os.close();
				
				
				boolean stupid = false;
				if(stupid)
					break;
				
			}
			server.close();
			server = null;
			sc.close();

		}   
		catch (IOException e) {
			System.out.println(e);
		}




	}

}
