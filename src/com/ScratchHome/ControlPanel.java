package src.com.ScratchHome;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ControlPanel implements Runnable{

	private ScratchAction sa;
	private JLabel message;
	private JLabel status;
	private JButton terminate;
	private JButton reup;
	
	
	public ControlPanel (ScratchAction sa) {
		this.sa = sa;
		message = new JLabel("Lancer le serveur");
		status = new JLabel("EN COURS");
		status.setForeground(Color.green);
		terminate = new JButton("Arrêter le serveur");
		reup = new JButton("Relancer le serveur");
	}

	public void run() {
		final JFrame frame = new JFrame("Panneau de contrôle du serveur");
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
		JButton close = new JButton("Fermer");
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
		subpanelCen.add(new JLabel("Dernier message reçu :   \n"));
		subpanelCen.add(message);
		panel.add(subpanelCen, BorderLayout.CENTER);
		
		
		JPanel subpanelNor = new JPanel();
		subpanelNor.add(new JLabel("Statut du serveur :   "));
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
