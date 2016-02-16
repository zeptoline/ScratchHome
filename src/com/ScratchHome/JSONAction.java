package com.ScratchHome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.plugin.PluginAction;

public class JSONAction extends PluginAction{

	private Home home;
	JFileChooser chooser = new JFileChooser();
	
	public void execute() {
		createJSON(this.home);
	}
	
	public JSONAction(Home home) {
		this.home = home;
		putPropertyValue(Property.NAME, "Create SB2(JSON) file");
		putPropertyValue(Property.MENU, "ScratchHome");
		// Enables the action by default
		setEnabled(true);
	} 
 
    public void createJSON(Home home) {
        StringBuffer vocStringBuffer = new StringBuffer();
        
        vocStringBuffer.append("{  \"extensionName\": \"ScratchHome (Scratch with SweetHome3D)\",\n   \"extensionPort\": 2016,\n   \"blockSpecs\": [\n\n        [\" \", \"mettre %m.objectList en %m.colorList\", \"setColor\"],\n],\n   \"menus\": { \n       \"colorList\": [\"bleu\", \"blanc\", \"rouge\", \"jaune\", \"jaune_pale\", \"jaune_clair\"],\n       \"objectList\": [ ");
        
        ArrayList<String> listElem = new ArrayList<String>();
        
        for (HomePieceOfFurniture fourniture : home.getFurniture()) {
            //vocStringBuffer.append(fourniture.getName()+"   "+fourniture.hashCode());
            //vocStringBuffer.append("\n");
            listElem.add(fourniture.getName()+" ("+fourniture.hashCode()+")");
        }

        for( int i = 0; i < listElem.size(); i++){
            if(i!=0){
               vocStringBuffer.append(", \""+listElem.get(i)+"\"");
            }else{
               vocStringBuffer.append("\""+listElem.get(i)+"\""); 
            }
        }

        vocStringBuffer.append("],\n},\n}");
	
		
	    this.chooser.setFileFilter(new FileFilter()
	    {
	        @Override
	        public boolean accept(File f)
	        {
	            return f.isDirectory() || f.getName().toLowerCase().endsWith(".sb2");
	        }
	        
	        @Override
	        public String getDescription() 
	        {
	            return "Fichiers d'extension Scratch (*.sb2)";
	        }
	    });
	    
	    int n = chooser.showSaveDialog(null);
	            
	    if (n==JFileChooser.APPROVE_OPTION) {
	        System.out.printf("Le fichier choisi est\t%s\n",chooser.getSelectedFile());
	        String chemin = this.chooser.getSelectedFile().toString();
	    	this.writeFile(vocStringBuffer.toString(), chemin+".sb2", false);
	    } else {
	        System.err.printf("Aucun fichier choisi (code %d)\n",n);
	    }
	}
			
	
	/**
	 * Method writing a text in a file (by overwriting it or concatenating it).
	 *
	 * @param text the text to write.
	 * @param filename the name of the file.
	 * @param append true if the text is to append at the end of the file, false otherwise.
	 */
	private void writeFile (String text, String filename, boolean append) {
		PrintWriter out = null;

		int dirPathEnd = filename.lastIndexOf(File.separator);
		String dirPath = "";
		if (dirPathEnd != -1) {
			dirPath = filename.substring(0, dirPathEnd);
			createDir(dirPath); 
		}
		
		try {
			out = new PrintWriter(new FileOutputStream(new File(filename), append));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		out.print(text);
		out.close();
	}
	
	/**
	 * Method creating a directory.
	 * 
	 * @param dirPath the path of the directory to create.
	 */
	private void createDir(String dirPath) {
		File theDir = new File(dirPath);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.err.println("Info: creating directory: " + dirPath);
			boolean result = theDir.mkdir();  

			if(!result) {    
				System.err.println("Error: problem when creating directory: " + dirPath);  
			}
		}
	}

}
