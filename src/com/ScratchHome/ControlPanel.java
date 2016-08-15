package src.com.ScratchHome;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The view for the server. Allow to restart it or close it.
 *
 */
public class ControlPanel implements Runnable{

	private ScratchAction sa;
	private JLabel message;
	private JLabel status;
	private JButton terminate;
	private JButton reup;
	
	private HashMap<String, String> language;
	
	/**
	 * ControlPanel constructor
	 * @param sa The class for the button in the menu (used to disable or re-enabling it if the server is running/closed)
	 * @param language The HashMap containing all the String loaded from the langage file
	 */
	public ControlPanel (ScratchAction sa, HashMap<String, String> language) {
		this.language=language;
		this.sa = sa;
		message = new JLabel(language.get("ServerLaunch"));
		message.setFont(new java.awt.Font("MS Song", 0, 12));
		status = new JLabel(language.get("StatusOn"));
		status.setFont(new java.awt.Font("MS Song", 0, 12));
		status.setForeground(Color.green);
		terminate = new JButton(language.get("ServerTerminate"));
		terminate.setFont(new java.awt.Font("MS Song", 0, 12));
		reup = new JButton(language.get("ServerRelaunch"));
		reup.setFont(new java.awt.Font("MS Song", 0, 12));
	}
	/**
	 * Launch the control panel
	 */
	public void run() {
		final JFrame frame = new JFrame(language.get("ControlPanel") + " - " + language.get("ScratchHomeVersion"));
		JPanel panel = new JPanel(new BorderLayout());
		
		terminate.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				sa.closeListener();
			}
		});
		reup.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				sa.reupListener();
			}
		});
		JButton close = new JButton(language.get("Close"));
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sa.closeListener();
				sa.reInstanciate();
				frame.dispose();
			}
		});
		

		JPanel subpanelSou = new JPanel();
		subpanelSou.add(terminate);
		subpanelSou.add(reup);
		reup.setVisible(false);
		subpanelSou.add(close);
		panel.add(subpanelSou, BorderLayout.SOUTH);
		
		JPanel subpanelCen = new JPanel();
		subpanelCen.add(new JLabel(language.get("LastMessage")));
		subpanelCen.add(message);
		panel.add(subpanelCen, BorderLayout.CENTER);
		
		
		JPanel subpanelNor = new JPanel();
		subpanelNor.add(new JLabel(language.get("ServerStatus")));
		subpanelNor.add(status);
		panel.add(subpanelNor, BorderLayout.NORTH);
		
		
		frame.add(panel);
      
        frame.setSize(450,200);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
				sa.closeListener();
				sa.reInstanciate();
				frame.dispose();
            }
        }); 

		frame.setVisible(true);
	}
	
	/**
	 * Change the displayed message
	 * @param newMessage The new message to display
	 */
	public void changeMessage(String newMessage) {
		message.setText(newMessage);
	}
	/**
	 * Change the status of the server (ON if running, OFF otherwise)
	 * @param running new value of the server (true if running, false otherwise)  
	 */
	public void changeStatus(boolean running) {
		if(running) {
			status.setText(language.get("StatusOn"));
			status.setForeground(Color.green);
			reup.setVisible(false);
			terminate.setVisible(true);
		} else {
			status.setText(language.get("StatusOff"));
			status.setForeground(Color.red);
			reup.setVisible(true);
			terminate.setVisible(false);
		}
	}
	
	
}
