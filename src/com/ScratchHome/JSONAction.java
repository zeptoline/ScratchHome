package src.com.ScratchHome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Light;
import com.eteks.sweethome3d.model.RecorderException;
import com.eteks.sweethome3d.plugin.PluginAction;
import com.eteks.sweethome3d.viewcontroller.HomeController;

public class JSONAction extends PluginAction{

	private Home home;
	private HashMap<String, String> language;
	JFileChooser chooser = new JFileChooser();
	private HomeController controller;

	Properties properties;
	
	
	public void execute() {
		if(!(home.getFurniture().isEmpty())) {
			createJSON(this.home);
		} else {
			JOptionPane.showMessageDialog(null,language.get("NoObject"));
		}
	}

	public JSONAction(Home home, HashMap<String, String> language, Properties general_properties, HomeController controller) {
		this.home = home;
		this.language = language;
		this.properties = general_properties;
		this.controller= controller; 
		putPropertyValue(Property.NAME, language.get("ExportMenu"));
		putPropertyValue(Property.MENU, language.get("ScratchHome"));
		// Enables the action by default
		setEnabled(true);
	} 

	public void createJSON(Home home) {

		// Demande si ajout de tous les objets ou simplement les lampes
		boolean allObject = false;
		String propertiesAllObject = properties.getProperty("allObject");
		if(propertiesAllObject.equals("OFF")){
			Object[] options = { language.get("AllObjects"), language.get("OnlyLights") };
			int reply = JOptionPane.showOptionDialog(null, language.get("ChoiceOfExport"), language.get("ObjectSelection"),  JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (reply == JOptionPane.YES_OPTION)
			{
				allObject = true;
			}
			if (reply == JOptionPane.CLOSED_OPTION){
				return;
			}
		}else {
			allObject = propertiesAllObject.equals("ALL");
		}
		// Demande si un bloc avec menu déroulant ou un bloc par objet
		boolean menuDeroulant = false;
		String propertiesMenu = properties.getProperty("menu");
		if (propertiesMenu.equals("OFF")) {
			Object[] options2 = { language.get("PulldownMenuBlock"), language.get("SingleBlock")};
			int reply2 = JOptionPane.showOptionDialog(null, language.get("TypeOfBlockChoice"), language.get("TypeOfBlock"),  JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
			if (reply2 == JOptionPane.YES_OPTION)
			{
				menuDeroulant = true;
			}
			if (reply2 == JOptionPane.CLOSED_OPTION){
				return;
			}
		}else {
			menuDeroulant = propertiesMenu.equals("MENU");
		}

		ArrayList<String> listElem = new ArrayList<String>();

		for (HomePieceOfFurniture fourniture : home.getFurniture()) {
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
				vocStringBuffer.append(
						"{  \"extensionName\": \"ScratchHome\",\n"
								+ "   \"extensionPort\": 2016,\n"
								+ "   \"blockSpecs\": [\n\n  "
								+ "      [\" \", \""+language.get("ScratchMessageObjects")+"\", \"setColor\"],\n"
								+ "],\n"
								+ "   \"menus\": { \n       "
								+ "\"colorList\": [\""+language.get("black")+"\", \""+language.get("blue")+"\", \""+language.get("cyan")+"\", \""+language.get("grey")+"\", \""+language.get("green")+"\", \""+language.get("magenta")+"\", \""+language.get("red")+"\", \""+language.get("white")+"\", \""+language.get("yellow")+"\"],\n       "
								+ "\"objectList\": [ ");
			}else{
				vocStringBuffer.append(
						"{  \"extensionName\": \"ScratchHome\",\n"
								+ "   \"extensionPort\": 2016,\n"
								+ "   \"blockSpecs\": [\n\n"
								+ "        [\" \", \""+language.get("ScratchMessageLights")+"\", \"switchOnOff\"],\n"
								+ "],\n"
								+ "   \"menus\": { \n       "
								+ "\"colorList\": [\""+language.get("SwitchOn")+"\", \""+language.get("SwitchOff")+"\"],\n       "
								+ "\"objectList\": [ ");
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
			vocStringBuffer.append(
					"{  \"extensionName\": \"ScratchHome\",\n"
							+ "   \"extensionPort\": 2016,\n"
							+ "   \"blockSpecs\": [\n\n");


			if (allObject==true){	
				for( int i = 0; i < ((listElem.size())); i++){
					vocStringBuffer.append(String.format("        [\" \", \""+language.get("ScratchMessageObjectsEachBlock")+"\", \"setColor/%s\"],\n", listElem.get(i), listElem.get(i)));
				}
				vocStringBuffer.append("]");
				vocStringBuffer.append(
						",\n   \"menus\": { \n       "
								+ "\"colorList\": [\""+language.get("black")+"\", \""+language.get("blue")+"\", \""+language.get("cyan")+"\", \""+language.get("grey")+"\", \""+language.get("green")+"\", \""+language.get("magenta")+"\", \""+language.get("red")+"\", \""+language.get("white")+"\", \""+language.get("yellow")+"\"],\n},\n}");
			}else{
				for( int i = 0; i < ((listElem.size())); i++){
					vocStringBuffer.append(String.format("        [\" \", \""+language.get("ScratchMessageObjectsEachLight")+"\", \"switchOnOff/%s\"],\n", listElem.get(i), listElem.get(i)));
				}
				vocStringBuffer.append("]");
				vocStringBuffer.append(
						",\n   \"menus\": { \n       "
								+ "\"colorList\": [\""+language.get("SwitchOn")+"\", \""+language.get("SwitchOff")+"\"],\n   },\n}");
			}
		}


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
				return language.get("SB2Extension");
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
		if(!filename.substring(filename.length()-4, filename.length()).equals(".sb2")) {
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

		} else {
			try {
				ZipOutputStream zos = new ZipOutputStream(
						new FileOutputStream(filename));

				ZipEntry json = new ZipEntry("project.json");
				zos.putNextEntry(json);

				
				text = "{\"objName\":\"Stage\","
						+ "\"costumes\":[{"
							+ "\"costumeName\": \"backdrop1\","
							+ "\"baseLayerID\": 1,"
							+ "\"baseLayerMD5\": \"510da64cf172d53750dffd23fbf73563.png\","
							+ "\"bitmapResolution\": 1,"
							+ "\"rotationCenterX\": 240,"
							+ "\"rotationCenterY\": 180"
							+ "}],"
						+ "\"currentCostumeIndex\": 0,"
						+ "\"penLayerMD5\": \"279467d0d49e152706ed66539b577c00.png\","
						+ "\"tempoBPM\": 60,"
						+ "\"videoAlpha\": 0.5,"
						+ "\"children\" : [],"
						+ "\"info\" : {"
							+ "\"videoOn\":false,"
							+ "\"savedExtensions\": ["+text+""
							+ "],"
							+ "\"scriptCount\" : 0,"
							+ "\"spriteCount\" : 1,"
							+ "\"sfwVersion\" : \"v341\","
							+ "\"projectID\":\"11175527\","
							+ "\"flashVersion\" : \"LNX 10,2,159,1\","
							+ "\"hasCloudData\" : false"
						+ "} }";
				
				byte[] data = text.getBytes();
				zos.write(data, 0, data.length);

				zos.closeEntry();
				
				ZipEntry svg = new ZipEntry("1.svg");
				zos.putNextEntry(svg);
				
				
				String tempdir = System.getProperty("java.io.tmpdir");
				try {
					controller.getView().exportToSVG(tempdir+"/project.svg");
				} catch (RecorderException e) {
					e.printStackTrace();
				}
//				
//				
//				Scanner sc = new Scanner(tempdir+"/project.svg");
//			    String svgfile = "";
//			    while(sc.hasNextLine()) {
//			    	svgfile += sc.nextLine();
//			    }
//			    sc.close();
//				
//
//				byte[] data2 = svgfile.getBytes();
//				zos.write(data2, 0, data2.length);
//				zos.closeEntry();
				 FileInputStream fis = null;
			      try {
			        fis = new FileInputStream(tempdir+"/project.svg");
			        byte[] byteBuffer = new byte[1024];
			        int bytesRead = -1;
			        while ((bytesRead = fis.read(byteBuffer)) != -1) {
			          zos.write(byteBuffer, 0, bytesRead);
			        }
			        zos.flush();
			      } finally {
			        try {
			          fis.close();
			        } catch (Exception e) {
			        }
			      }
			      zos.closeEntry();
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}


		}	
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


	public void recharger(HashMap<String, String> language) {
		this.language = language;
		putPropertyValue(Property.NAME, language.get("ExportMenu"));
		putPropertyValue(Property.MENU, language.get("ScratchHome"));
	}

}
