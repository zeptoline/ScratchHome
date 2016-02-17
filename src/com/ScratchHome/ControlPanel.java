package src.com.ScratchHome;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlPanel implements Runnable{

	private ScratchAction sa;
	private JLabel message;
	private JLabel status;
	JButton terminate;
	JButton reup;
	
	public ControlPanel (ScratchAction sa) {
		this.sa = sa;
		message = new JLabel("Server launched");
		status = new JLabel("EN COURS");
		status.setForeground(Color.green);
		terminate = new JButton("Terminate server");
		reup = new JButton("Relaunch server");
	}

	public void run() {
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
		
		
		
		panel.add(terminate, BorderLayout.SOUTH);
		panel.add(reup, BorderLayout.WEST);
		reup.setVisible(false);
		
		panel.add(new JLabel("Dernier message reçu :   \n"), BorderLayout.CENTER);
		panel.add(message, BorderLayout.CENTER);
		panel.add(new JLabel("Status du server :   "), BorderLayout.NORTH);
		panel.add(status, BorderLayout.NORTH);
		
		JFrame frame = new JFrame("control panel");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        frame.add(panel);
        frame.setVisible(true);
	}
	
	
	public void changeMessage(String newMessage) {
		message.setText(newMessage);
	}
	public void changeStatus(boolean enCours) {
		if(enCours) {
			status.setText("EN COURS");
			status.setForeground(Color.green);
			reup.setVisible(false);
			terminate.setVisible(true);
		} else {
			status.setText("OFF");
			status.setForeground(Color.red);
			reup.setVisible(true);
			terminate.setVisible(false);
		}
	}
	
	
}
