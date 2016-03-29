package src.com.ScratchHome;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;



public class ScratchListener implements Runnable{

	private static final int PORT = 2016; // set to your extension's port number

	private static InputStream sockIn;
	private static OutputStream sockOut;


	private boolean running = true;
	private ServerSocket serverSock = null;

	private Home home;
	private ControlPanel cp;
	private HashMap<String, String> language;



	public ScratchListener (Home home, ControlPanel cp, HashMap<String, String> language) {
		this.home = home;
		this.cp = cp;
		this.language = language;
	}

	public boolean isRunning() {
		return running;
	}


	public void terminate()  {
		running = false;
		try {
			serverSock.close();
		} catch (IOException e) {}
		cp.changeStatus(false);
	}

	public void run() {
		cp.changeStatus(true);
		try {
			cp.changeMessage(language.get("ServerLaunched"));
			serverSock = new ServerSocket(PORT);
			running = true;
			while (running) {
				try {
					Socket sock = serverSock.accept();
					sockIn = sock.getInputStream();
					sockOut = sock.getOutputStream();
					try {
						handleRequest();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
					sock.close();
					sockIn.close();
					sockOut.close();
				}catch(SocketException e){}

				if (!running)
					break;
			}
			cp.changeMessage(language.get("ServerTerminated"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void handleRequest() throws IOException {
		String httpBuf = "";
		int i;

		// read data until the first HTTP header line is complete (i.e. a '\n' is seen)
		while ((i = httpBuf.indexOf('\n')) < 0) {
			byte[] buf = new byte[5000];
			int bytes_read = sockIn.read(buf, 0, buf.length);
			if (bytes_read < 0) {
				System.err.println("Socket closed; no HTTP header.");
				return;
			}
			httpBuf += new String(Arrays.copyOf(buf, bytes_read));
		}

		String header = httpBuf.substring(0, i);
		if (header.indexOf("GET ") != 0) {
			System.err.println("This server only handles HTTP GET requests.");
			return;
		}
		i = header.indexOf("HTTP/1");
		if (i < 0) {
			System.err.println("Bad HTTP GET header.");
			return;
		}
		header = header.substring(5, i - 1);
		if (header.equals("favicon.ico")) return;
		else if (header.equals("crossdomain.xml")) sendPolicyFile();
		else if (header.equals("poll")) sendResponse("");
		else if (header.equals("reset_all")) return;
		else doCommand(header);
	}
	private static void sendPolicyFile() {
		// Send a Flash null-teriminated cross-domain policy file.
		String policyFile =
				"<cross-domain-policy>\n" +
						"  <allow-access-from domain=\"*\" to-ports=\"" + PORT + "\"/>\n" +
						"</cross-domain-policy>\n\0";
		sendResponse(policyFile);
	}


	private static void sendResponse(String s) {
		String crlf = "\r\n";
		String httpResponse = "HTTP/1.1 200 OK" + crlf;
		httpResponse += "Content-Type: text/html; charset=ISO-8859-1" + crlf;
		httpResponse += "Access-Control-Allow-Origin: *" + crlf;
		httpResponse += crlf;
		httpResponse += s + crlf;
		try {
			byte[] outBuf = httpResponse.getBytes();
			sockOut.write(outBuf, 0, outBuf.length);
		} catch (Exception ignored) { }
	}

	private void doCommand(String cmdAndArgs) {

		cmdAndArgs= cmdAndArgs.replaceAll("%2F", "/");
		String[] cmd = cmdAndArgs.split("/"); 
	
		if(cmd[0].startsWith("setColor")) {
			
			cmd[1] = cmd[1].replaceAll("%..", "");
			cmd[1] = cmd[1].replaceAll("[\\D]", "");



			int color = 0;
			cmd[2] = cmd[2].toLowerCase();
			if (cmd[2].equals(language.get("black").toLowerCase())) {
				color = -15000000;
			}
			if (cmd[2].equals(language.get("blue").toLowerCase())) {
				color = -16776961;
			}
			if (cmd[2].equals(language.get("cyan").toLowerCase())) {
				color = -16711681;
			}
			if (cmd[2].equals(language.get("grey").toLowerCase())) {
				color = -7829368;
			}
			if (cmd[2].equals(language.get("green").toLowerCase())) {
				color = -16711936;
			}
			if (cmd[2].equals(language.get("magenta").toLowerCase())) {
				color = -65281;
			}
			if (cmd[2].equals(language.get("red").toLowerCase())) {
				color = -65536;
			}
			if (cmd[2].equals(language.get("white").toLowerCase())) {
				color = -1;
			}
			if (cmd[2].equals(language.get("yellow").toLowerCase())) {
				color = -256;
			}
			String listofFour= " - ";
			for (HomePieceOfFurniture four : home.getFurniture()) {
				listofFour += four.hashCode()+" - ";
			}
			
			cp.changeMessage(cmd[0]+" "+cmd[1]+" "+cmd[2]+" "+color+listofFour);

			HomeModifier.changeColor(Integer.valueOf(cmd[1]), color, this.home);
		}else if (cmd[0].startsWith("switchOnOff")) {
			cmd[2] = cmd[2].replaceAll("%20", "");
			cmd[2] = cmd[2].replaceAll("[\\D]", "");


			int color = 0;
			cmd[1] = cmd[1].toLowerCase();
			if (cmd[1].equals(language.get("On").toLowerCase())) {
				color = -256;
			}
			if (cmd[1].equals(language.get("Off").toLowerCase())) {
				color = -15000000;
			}
			HomeModifier.changeColor(Integer.valueOf(cmd[2]), color, this.home);
		}
	}

}
