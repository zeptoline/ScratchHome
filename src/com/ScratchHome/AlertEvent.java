package com.ScratchHome;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AlertEvent {

	public AlertEvent(String message) {
	
	    JLabel labelmess = new JLabel(message);
	    
        JPanel panel= new JPanel();
        panel.add(labelmess);

	    JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        frame.add(panel);
        frame.setVisible(true);
	}
}