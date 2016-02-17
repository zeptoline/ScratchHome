package src.com.ScratchHome;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlPanel implements Runnable{

	private ScratchAction sa;
	private JLabel message;
	private JLabel message2;
	
	public ControlPanel (ScratchAction sa) {
		this.sa = sa;
		message = new JLabel("Server launched");
		message2 = new JLabel("autre message");
	}

	public void run() {
		JPanel panel = new JPanel(new BorderLayout());
		
		JButton terminate = new JButton("Terminate server");
		terminate.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				sa.closeListener();
			}
		});
		panel.add(terminate, BorderLayout.SOUTH);
		
		panel.add(new JLabel("Dernier message reçu :"), BorderLayout.NORTH);
		panel.add(message, BorderLayout.CENTER);
		panel.add(message2, BorderLayout.WEST);
		
		
		
		JFrame frame = new JFrame("control panel");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        frame.add(panel);
        frame.setVisible(true);
	}
	
	
	public void changeMessage(String newMessage) {
		message.setText(newMessage);
	}
	public void changeMessage2(String newMessage) {
		message2.setText(newMessage);
	}
	
}
