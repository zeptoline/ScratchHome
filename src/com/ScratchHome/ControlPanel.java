package src.com.ScratchHome;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ControlPanel implements Runnable{

	private ScratchListener sl;
	
	
	public ControlPanel (ScratchListener sl) {
		this.sl = sl;
	}

	public void run() {
		JPanel panel = new JPanel(new BorderLayout());
		
		JButton terminate = new JButton("Terminate server");
		terminate.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				sl.terminate();
			}
		});
		panel.add(terminate, BorderLayout.SOUTH);
		
		JButton suspend = new JButton("suspend server");
		suspend.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				sl.suspend();
			}
		});
		panel.add(suspend, BorderLayout.NORTH);
		
		JButton resume = new JButton("resume server");
		resume.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				sl.resume();
			}
		});
		panel.add(resume, BorderLayout.CENTER);
		
		
		
		
		JFrame frame = new JFrame("control panel");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        frame.add(panel);
        frame.setVisible(true);
	}
	
	
	
}
