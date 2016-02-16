package src.com.ScratchHome;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import com.eteks.sweethome3d.model.Home;



public class ScratchListener implements Runnable{

	private static final int PORT = 12345; // set to your extension's port number

	private static InputStream sockIn;
	private static OutputStream sockOut;


	private boolean running = true;
	private ServerSocket serverSock = null;

	private Home home;





	public ScratchListener (Home home) {
		this.home = home;
	}

	public boolean isRunning() {
		return running;
	}


	private boolean suspended = false;
	private void waitWhileSuspended() throws InterruptedException  {
		while (true) {
			if(suspended) {
				System.out.println("Server Suspended");
				wait();
				System.out.println("Server resumed");
			}
		}
	}
	public void suspend() {
		suspended = true;
	}
	public void resume() {
		suspended = false;
		this.notify();
	}






	public void terminate()  {
		running = false;
		try {
			serverSock.close();
		} catch (IOException e) {}
		System.out.println("tryin' to crash it");
	}

	public void run() {
		try {
			waitWhileSuspended();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			System.out.println("Server launched");
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
			System.out.println("The server has been shut down");

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
				System.out.println("Socket closed; no HTTP header.");
				return;
			}
			httpBuf += new String(Arrays.copyOf(buf, bytes_read));
		}

		String header = httpBuf.substring(0, i);
		if (header.indexOf("GET ") != 0) {
			System.out.println("This server only handles HTTP GET requests.");
			return;
		}
		i = header.indexOf("HTTP/1");
		if (i < 0) {
			System.out.println("Bad HTTP GET header.");
			return;
		}
		header = header.substring(5, i - 1);
		if (header.equals("favicon.ico")) return; // igore browser favicon.ico requests
		else if (header.equals("crossdomain.xml")) sendPolicyFile();
		else if (header.equals("poll")) return;
		else if (header.equals("reset_all")) return;
		else doCommand(header);
	}

	private void sendPolicyFile() {
		// Send a Flash null-teriminated cross-domain policy file.
		String policyFile =
				"<cross-domain-policy>\n" +
						"  <allow-access-from domain=\"*\" to-ports=\"" + PORT + "\"/>\n" +
						"</cross-domain-policy>\n\0";
		sendResponse(policyFile);
	}

	private void sendResponse(String s) {
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
		String[] cmd = cmdAndArgs.split("/"); 


		HomeModifier.changeColor(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), this.home);

	}

}
