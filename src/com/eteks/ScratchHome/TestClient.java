package com.eteks.ScratchHome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
public class TestClient {

	public static void main(String[] args) {

		Socket smtpSocket = null;  
		PrintStream os = null;
		BufferedReader d = null;

		try {
			smtpSocket = new Socket("localhost", 2016);

			os = new PrintStream(smtpSocket.getOutputStream());
			d = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: hostname");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: hostname");
		}



		if (smtpSocket != null && os != null & d!= null) {
			try {


				String responseLine;
				while ((responseLine = d.readLine()) != null) {
					if (responseLine.equals("QUIT")) {
						break;
					}
					System.out.println("Server: " + responseLine);
				}



				Scanner sc = new Scanner(System.in);
				boolean cont = true;
				String text = "";
				while (cont) {
					System.out.println("Donnez le prochain envois :");
					text = sc.nextLine();
					if(text.equals("end")) {
						cont = false;
					}
					os.println(text);
				}



				sc.close();
				os.close();
				smtpSocket.close();   
			} catch (UnknownHostException e) {
				System.err.println("Trying to connect to unknown host: " + e);
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
	}           
}