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

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;

public class FurnitureList {

    private int hash = 0;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getHashFromFurniture(Home home) {
	
	    JLabel label = new JLabel("Chose a furniture");
	    final ArrayList<String> hashcode = new ArrayList<String>();
	    
	    for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			hashcode.add(String.valueOf(fourniture.hashCode()));
		}
		JList list = new JList(hashcode.toArray());
	    

	    list.addListSelectionListener(new ListSelectionListener() {
	    	 public void valueChanged(ListSelectionEvent evt) {

		            hash = Integer.valueOf(hashcode.get(evt.getFirstIndex()));
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
	
	    return hash;
	}
}