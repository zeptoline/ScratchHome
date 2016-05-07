package src.com.ScratchHome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Light;
import com.eteks.sweethome3d.model.RecorderException;
import com.eteks.sweethome3d.plugin.PluginAction;
import com.eteks.sweethome3d.viewcontroller.HomeController;

/**
 *Create a SB2 file (actually a ZIP), that is the Scratch project format, by creating a JSON and SVG to represent SH3D scene's objects
 *
 */
public class JSONAction extends PluginAction{

	private Home home; //representing the 3D scene
	private HashMap<String, String> language; //list of plugin languages 
	JFileChooser chooser = new JFileChooser();
	private HomeController controller; //get view methods

	Properties properties; //SH3D properties (ie. user language)
		
    /**
     * Method called by launching JSONAction in the plugin menu. If the 3D scene is not empty, calls the function to create the JSON.
     *
     */
	public void execute() {
		if(!(home.getFurniture().isEmpty())) {
			createJSON(this.home);
		} else {
			JOptionPane.showMessageDialog(null,language.get("NoObject"));
		}
	}

	/**
	 * JSONAction Constructor.
	 * @param home representing the 3D scene.
	 * @param language the list of plugin languages.
	 * @param general_properties SH3D properties (ie. user language).
	 * @param controller to get view methods.
	 */
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

	/**
	 * Method to create JSON file representing SH3D scene objects and putting it in a SB2 file which is the Scratch project format.
	 * Creating also a SVG picture, added to the SB2 file, representing the SH3D scene for watching it in Scratch project.
	 *
	 * @param home representing the 3D scene.
	 */
	public void createJSON(Home home) {
		//A JSwing window to ask what export : all objects or only lights
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
		
		//A JSwing window to ask how export : all objects in only one Scratch block or one block per object
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

		//A list to add current 3D scene objects
		ArrayList<String> listElem = new ArrayList<String>();
		//Adding objects to the previous list
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
			//Below is the JSON to create an only Scratch block containing all objects/all lights
			//if all objects were chosen
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
			//if only lights were chosen
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
			//Below is the JSON to create a Scratch block for each objects/lights
			vocStringBuffer.append(
					"{  \"extensionName\": \"ScratchHome\",\n"
							+ "   \"extensionPort\": 2016,\n"
							+ "   \"blockSpecs\": [\n\n");
			//if all objects were chosen
			if (allObject==true){	
				for( int i = 0; i < ((listElem.size())); i++){
					vocStringBuffer.append(String.format("        [\" \", \""+language.get("ScratchMessageObjectsEachBlock")+"\", \"setColor/%s\"],\n", listElem.get(i), listElem.get(i)));
				}
				vocStringBuffer.append("]");
				vocStringBuffer.append(
						",\n   \"menus\": { \n       "
								+ "\"colorList\": [\""+language.get("black")+"\", \""+language.get("blue")+"\", \""+language.get("cyan")+"\", \""+language.get("grey")+"\", \""+language.get("green")+"\", \""+language.get("magenta")+"\", \""+language.get("red")+"\", \""+language.get("white")+"\", \""+language.get("yellow")+"\"],\n},\n}");
			//if only lights were chosen
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

		//Below the code to deal with the File filter window in order to create the Sb2 file (with a call to writeFile function)
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
			//check if user has written ".sb2" at the end of its file and let it or remove it 
			if(chemin.substring(chemin.length()-4, chemin.length()).equals(".sb2")) {
				//writeFile function is called to create a true SB2 file (that is a .zip actually)
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
		//we create a zip file, that contains the JSON and a SVG picture
		try {
			ZipOutputStream zos = new ZipOutputStream(
					new FileOutputStream(filename));

			ZipEntry json = new ZipEntry("project.json");
			zos.putNextEntry(json);

			//Below the text to write in the JSON in order to be considered as correct for a Scratch project
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

			zos.closeEntry(); //don't forget to close the entry

			ZipEntry svg = new ZipEntry("1.svg");
			zos.putNextEntry(svg);

			
			String tempdir = System.getProperty("java.io.tmpdir");
			try {
				//get a SVG picture of the SH3D 2D scene
				controller.getView().exportToSVG(tempdir+"/project.svg");
			} catch (RecorderException e) {
				e.printStackTrace();
			}
			
			//resizing the SVG because Scratch needs a 480x360 SVG
			try {
				//get the SVG file (which is a XML file)
				String filepath = tempdir+"/project.svg";
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(filepath);
		
				//get the svg element by tag name directly
				Node svgNode = doc.getElementsByTagName("svg").item(0);

				//get width attribute value (and remove non numeric characters because the whole value finishes with "px")
				NamedNodeMap attr = svgNode.getAttributes();
				Node nodeAttr = attr.getNamedItem("width");
				double width = Double.parseDouble(nodeAttr.getTextContent().replaceAll("\\D", ""));
				//get height attribute value
				nodeAttr = attr.getNamedItem("height");
				double height = Double.parseDouble(nodeAttr.getTextContent().replaceAll("\\D", ""));
				
				//calculate best scale factor both for width and height
				double x1 = 480/width;
				double x2 = 360/height;
				//keep the lower value to be sure the SVG won't be trimmed
				double x = Math.min(x1, x2);
				
				//get the first g node in the xml file
				Node gNode1 = doc.getElementsByTagName("g").item(0);
				//add transform attribute to this node with the right scale factor
				((Element)gNode1).setAttribute("transform","scale("+x+")");
				
				//write the content into svg file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(filepath));
				transformer.transform(source, result);

			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (SAXException sae) {
				sae.printStackTrace();
			}				
			
			//adding the SVG file to the SB2
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

	/**
	 * Method to reload plugin language 
	 * 
	 * @param language 
	 */
	public void recharger(HashMap<String, String> language) {
		this.language = language;
		putPropertyValue(Property.NAME, language.get("ExportMenu"));
		putPropertyValue(Property.MENU, language.get("ScratchHome"));
	}
}
