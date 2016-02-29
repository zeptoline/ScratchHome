package src.com.ScratchHome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Light;
import com.eteks.sweethome3d.plugin.PluginAction;

public class JSONAction extends PluginAction{

	private Home home;
	@SuppressWarnings("unused")
	private HashMap<String, String> language;
	JFileChooser chooser = new JFileChooser();

	public void execute() {
		if(!(home.getFurniture().isEmpty())) {
			createJSON(this.home);
		} else {
			JOptionPane.showMessageDialog(null,"La scène ne contient aucun meuble !");
		}
	}

	public JSONAction(Home home, HashMap<String, String> language) {
		this.home = home;
		this.language = language;
		putPropertyValue(Property.NAME, "Créer un fichier SB2(JSON)");
		putPropertyValue(Property.MENU, "ScratchHome");
		// Enables the action by default
		setEnabled(true);
	} 

	public void createJSON(Home home) {
		// Demande si ajout de tout les objet
		boolean allObject = false;

		Object[] options = { "Prendre tous les objets", "Ne prendre que les lampes" };
		int reply = JOptionPane.showOptionDialog(null, "Choisissez les objets à exporter :", "Selection des objets",  JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (reply == JOptionPane.YES_OPTION)
		{
			allObject = true;
		}
		if (reply == JOptionPane.CLOSED_OPTION){
			return;
		}


		/*
		 * 
		 * FAIRE LES DEUX CHOIX : AVEC MENU DEROULANT ET AVEC UN BLOC PAR OBJET
		 * 
		 */
		boolean menuDeroulant = false;
		Object[] options2 = { "Créer un bloc avec menu déroulant", "Créer un bloc pour chaque objet" };
		int reply2 = JOptionPane.showOptionDialog(null, "Choisissez le type de bloc sur Scratch :", "Type de bloc",  JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
		if (reply2 == JOptionPane.YES_OPTION)
		{
			menuDeroulant = true;
		}
		if (reply2 == JOptionPane.CLOSED_OPTION){
			return;
		}


		//////////////////////////////////////////

		ArrayList<String> listElem = new ArrayList<String>();

		for (HomePieceOfFurniture fourniture : home.getFurniture()) {
			//vocStringBuffer.append(fourniture.getName()+"   "+fourniture.hashCode());
			//vocStringBuffer.append("\n");
			if(allObject==true){
				listElem.add(fourniture.getName()+"("+fourniture.hashCode()+")");
			}else{
				if(fourniture instanceof Light){
					listElem.add(fourniture.getName()+"("+fourniture.hashCode()+")");
				}
			}

		}
				
		StringBuffer vocStringBuffer = new StringBuffer();
		if(menuDeroulant){
			// Construction d'un bloc avec un menu déroulant
			if (allObject==true){
				vocStringBuffer.append("{  \"extensionName\": \"ScratchHome\",\n   \"extensionPort\": 2016,\n   \"blockSpecs\": [\n\n        [\" \", \"mettre %m.objectList en %m.colorList\", \"setColor\"],\n],\n   \"menus\": { \n       \"colorList\": [\"Noir\", \"Bleu\", \"Cyan\", \"Gris\", \"Vert\", \"Magenta\", \"Rouge\", \"Blanc\", \"Jaune\"],\n       \"objectList\": [ ");
			}else{
				vocStringBuffer.append("{  \"extensionName\": \"ScratchHome\",\n   \"extensionPort\": 2016,\n   \"blockSpecs\": [\n\n        [\" \", \"%m.colorList %m.objectList\", \"switchOnOff\"],\n],\n   \"menus\": { \n       \"colorList\": [\"Allumer\", \"Eteindre\"],\n       \"objectList\": [ ");
			}

			for( int i = 0; i < listElem.size(); i++){
				if(i!=0){
					vocStringBuffer.append(", \""+listElem.get(i)+"\"");
				}else{
					vocStringBuffer.append("\""+listElem.get(i)+"\""); 
				}
			}

			vocStringBuffer.append("],\n},\n}");

		} 
		else {
			// construction d'un bloc par element
			vocStringBuffer.append("{  \"extensionName\": \"ScratchHome\",\n   \"extensionPort\": 2016,\n   \"blockSpecs\": [\n\n");
			
			for( int i = 0; i < listElem.size(); i++){
				vocStringBuffer.append("        [\" \", \"mettre " + listElem.get(i) + " en %m.colorList\", \"setColor\"],\n]");
			}
			
			if (allObject==true){
				vocStringBuffer.append(",\n   \"menus\": { \n       \"colorList\": [\"Noir\", \"Bleu\", \"Cyan\", \"Gris\", \"Vert\", \"Magenta\", \"Rouge\", \"Blanc\", \"Jaune\"],\n   },\n}");
			}else{
				vocStringBuffer.append(",\n   \"menus\": { \n       \"colorList\": [\"Allumer\", \"Eteindre\"],\n   },\n}");
			}
			
		}
		//////////////////////////////////////////


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
			String chemin = this.chooser.getSelectedFile().toString();
			if(chemin.substring(chemin.length()-4, chemin.length()).equals(".sb2")) {
				this.writeFile(vocStringBuffer.toString(), chemin, false);
			} else {
				this.writeFile(vocStringBuffer.toString(), chemin+".sb2", false);
			}
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
