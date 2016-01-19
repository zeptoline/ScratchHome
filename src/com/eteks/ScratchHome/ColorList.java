package com.eteks.ScratchHome;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ColorList {

    private String color = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getColor() {
	
	    JLabel label = new JLabel("Chose a furniture");
	    ArrayList<String> s = new ArrayList<String>();
	    s.add("red");
	    s.add("blue");
	    s.add("green");
	    s.add("black");
	    s.add("white");
	    s.add("gray");
	    s.add("yellow");
	    
	    
	    JList list = new JList(s.toArray());
	    

	    list.addListSelectionListener(new ListSelectionListener() {
	    	 public void valueChanged(ListSelectionEvent evt) {

		            color = s.get(evt.getFirstIndex());
		            JList button = (JList)evt.getSource();
		            SwingUtilities.getWindowAncestor(button).dispose();
	    	 }
	    });
	
	    
	
	    JPanel buttons = new JPanel();
	    buttons.add(list);
	
	    JPanel content = new JPanel(new BorderLayout(8, 8));
	    content.add(label, BorderLayout.CENTER);
	    content.add(buttons, BorderLayout.SOUTH);
	
	    JDialog dialog = new JDialog();
	    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    dialog.setModal(true);
	    dialog.setTitle("Chose a furniture");
	    dialog.getContentPane().add(content);
	    dialog.pack();
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true);
	
	    return color;
	}
}