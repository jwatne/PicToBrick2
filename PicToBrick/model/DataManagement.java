package PicToBrick.model;

import java.io.*;
import javax.imageio.*;
import javax.imageio.stream.*;

import PicToBrick.service.DataProcessing;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.*;
import java.awt.*;
import java.text.*;

/**
 * class:            DataManagement
 * layer:            Data management (three tier architecture)
 * description:      Contains all program data and provides appropriate methods
 * @author           Adrian Schuetz
 */
public class DataManagement {

	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private Picture originalImage = null;
	private Picture mosaicImage = null;
	private Configuration current, legoTop, legoSide, ministeck;
	private File workingDirectory = null;
	private File projectFolder = null;
	private String projectName = "";
	private Vector configurations;
	private Mosaic mosaic;
	private DataProcessing dataProcessing;

	/**
	 * method:           DataManagement
	 * description:      constructor
	 * @author           Adrian Schuetz
	 */
	public DataManagement(){}

	/**
	 * method:           DataManagement
	 * description:      constructor
	 * @author           Adrian Schuetz
	 * @param            dataProcessing
	 */
	public DataManagement(DataProcessing dataProcessing)
	{
		this.dataProcessing = dataProcessing;
		configurations = new Vector();
		configurations.add("PicToBrick_Lego_" + textbundle.getString("output_dataManagement_1") +".cfg");
		configurations.add("PicToBrick_Lego_" + textbundle.getString("output_dataManagement_2") + ".cfg");
		configurations.add("PicToBrick_Ministeck.cfg");
		this.legoTop  = new Configuration("PicToBrick_Lego_"+textbundle.getString("output_dataManagement_1"),"Plate_1x1_(3024)",1,1,8.0,1,4,1);
		this.legoSide = new Configuration("PicToBrick_Lego_"+textbundle.getString("output_dataManagement_2"),"Plate_1x1_(3024)",5,2,8.0,7,4,2);
		this.ministeck = new Configuration("PicToBrick_Ministeck","Ministeck_1x1",1,1,4.16666,1,1,3);
		initConfigurationLegoTop();
		initConfigurationLegoSide();
		initConfigurationMinisteck();
	}

	/**
	 * method:           configurationCopy
	 * description:      returns a copy of the configuration
	 * @author           Adrian Schuetz
	 * @param            configuration
	 * @return           configuration (copy)
	 */
	public Configuration configurationCopy(Configuration configuration){
		Configuration kopie = new Configuration(
			configuration.getName(),
			configuration.getBasisName(),
			configuration.getBasisWidth(),
			configuration.getBasisHeight(),
			configuration.getBasisWidthMM(),
			configuration.getBasisStability(),
			configuration.getBasisCosts(),
			configuration.getMaterial());
		Enumeration colorEnum = configuration.getAllColors();
		while (colorEnum.hasMoreElements()) {
			kopie.setColor((ColorObject)colorEnum.nextElement());
		}
		Enumeration elementEnum = configuration.getAllElements();
		//Basis element is already added by the constructor
		elementEnum.nextElement();
		while (elementEnum.hasMoreElements()) {
			kopie.setElement((ElementObject)elementEnum.nextElement());
		}
		return kopie;
	}

	/**
	 * method:           getSystemconfiguration
	 * description:      returns the configuration (specified by number)
	 * @author           Adrian Schuetz
	 * @param            number
	 * @return           configuration
	 */
	public Configuration getSystemConfiguration(int number){
		Configuration configuration = new Configuration();;
		switch (number){
		case 0:{
			configuration = configurationCopy(legoTop);
			break;
		}
		case 1:{
			configuration = configurationCopy(legoSide);
			break;
		}
		case 2:{
			configuration = configurationCopy(ministeck);
			break;
		}
		}
		return configuration;
	}

	/**
	 * method:           getConfigurations
	 * description:      returns configurations
	 * @author           Adrian Schuetz
	 * @return           vector containing configurations
	 */
	public Vector getConfiguration(){
		Vector allConfigurations = new Vector();
		for (Enumeration configurationsEnum=configurations.elements(); configurationsEnum.hasMoreElements(); ) {
			allConfigurations.add(configurationsEnum.nextElement());
		}
		String[] list = workingDirectory.list();
		for (int i=0; i<list.length; i++){
			if(list[i].contains(".cfg")){
				allConfigurations.add(list[i]);
			}
		}
		return allConfigurations;
	}

	/**
	 * method:           getMosaicWidth
	 * description:      returns mosaic width
	 * @author           Adrian Schuetz
	 * @return           width
	 */
	public int getMosaicWidth(){
		return mosaic.getMosaicWidth();
	}

	/**
	 * method:           getMosaicHeight
	 * description:      returns mosaic height
	 * @author           Adrian Schuetz
	 * @return           height
	 */
	public int getMosaicHeight(){
		return mosaic.getMosaicHeight();
	}

	/**
	 * method:           getMosaic
	 * description:      calls the appropriate method in mosaic
	 * @author           Adrian Schuetz
	 * @return           Vector[][]
	 */
	public Vector[][] getMosaic(){
		return mosaic.getMosaic();
	}

	/**
	 * method:           mosaicCopy
	 * description:      calls the appropriate method in mosaic
	 * @author           Adrian Schuetz
	 * @return           Vector[][]
	 */
	public Vector[][] mosaicCopy(){
		return mosaic.mosaicCopy();
	}


	/**
	 * method:           getMosaicInstance
	 * description:      returns the instance of the mosaic object
	 * @author           Adrian Schuetz
	 * @return           mosaic
	 */
	public Mosaic getMosaicInstance(){
		return this.mosaic;
	}

	/**
	 * method:           generateFolderOutput
	 * description:      Searches the working directory for folders with same name.
	 *                   If a folder is found a new folder with higher serial-number
	 *                   is generated. starting with serial-number 001.
	 * @author           Adrian Schuetz
	 */
	public String generateFolderOutput(){
		String imageName = "";
		int counter = 0;
		//List of file objects from the working directory
		File[] list = workingDirectory.listFiles();

		//Split image names and ending
		String[] imageData = originalImage.getImageName().split("\\.");
		imageName = imageData[0];

		//Test if object is a folder
		for (int i=0; i<list.length; i++){
			if(list[i].isDirectory()){
				//Check serial-number
				if(list[i].getName().toLowerCase().contains(imageName.toLowerCase()+"_")){
					try{
						//Get serial-number (last 3 characters)
						String sub = list[i].getName().substring(list[i].getName().length()-3);
						//Get name of folder
						String subrest = list[i].getName().substring(0,list[i].getName().length()-3);
						//Check imageName
						if(subrest.toLowerCase().equals(imageName.toLowerCase()+"_")){
							int number = new Integer(sub).intValue();
							//Count if folder with higher serial-number is found
							if(number > counter){
								counter = number;
							}
						}
						//counter = (new Integer(sub).intValue());
					}catch(Exception e){System.out.println(e.toString());}
				}
			}
		}

		//Format output
		DecimalFormat format = new DecimalFormat("000");
		this.projectName = workingDirectory.getAbsolutePath()+"/"+imageName+"_"+format.format(counter+1);
		this.projectFolder = new File(this.projectName);

		//Create project folder
		this.projectFolder.mkdir();

		//Create subfolder
		File subFolder = new File(projectFolder.getAbsolutePath()+"/data");
		subFolder.mkdir();

		String folderName = imageName+"_"+format.format(counter+1);
		return folderName;
	}

	/**
	 * method:           generateUTFOutput
	 * description:      writes the string in a UTF-8 encoded file
	 * @author           Adrian Schuetz
	 * @param            content
	 * @param            fileName
	 * @param            subFolder
	 * @return           true or false
	 */
	public boolean generateUTFOutput(String content, String fileName, boolean subFolder){
		String path;
		try{
			if(subFolder){
				path = projectFolder+"/data/"+fileName;
			}else{
				path = projectFolder+"/"+fileName;
			}
			OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(path),"UTF-8");
			output.write(content);
			output.close();
		}catch (IOException e){
			System.out.println(e);
			return false;
		}
		return true;
	}

	/**
	 * method:           generateImageOutput
	 * description:      Saves the image in the subfolder data
	 *                   (JPEG compression with maximum quality)
	 * @author           Adrian Schuetz
	 * @param            image
	 * @param            filename
	 * @return           true or false
	 */
	public boolean generateImageOutput(BufferedImage image, String filename){
		//The image is written to the file by the writer
		File file = new File( projectFolder+"/data/"+filename+".jpg");
		//Iterator containing all ImageWriter (JPEG)
		Iterator encoder = ImageIO.getImageWritersByFormatName("JPEG");
		ImageWriter writer = (ImageWriter) encoder.next();
		//Compression parameter (best quality)
		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(1.0f);
		//Try to write the image
		try{
			ImageOutputStream outputStream = ImageIO.createImageOutputStream(file);
			writer.setOutput(outputStream);
			writer.write(null, new IIOImage(image, null, null), param);
			outputStream.flush();
			writer.dispose();
			outputStream.close();
		}catch(IOException e){
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * method:           generateColorOutput
	 * description:      Saves an image of the color in the subfolder data
	 *                   (JPEG compression with maximum quality)
	 * @author           Adrian Schuetz
	 * @param            color
	 * @param            colorName
	 * @return           true or false
	 */
	public boolean generateColorOutput(Color color, String colorName){
		BufferedImage colorImage = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_RGB );
		Graphics2D g = colorImage.createGraphics();
		g.setColor(color);
		g.fillRect( 0, 0, 1, 1 );
		if(generateImageOutput(colorImage, colorName)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * method:           setCurrentConfiguration
	 * description:      set current configuration
	 * @author           Adrian Schuetz
	 * @param            configuration
	 */
	public void setCurrentConfiguration(Configuration configuration){
		this.current = configuration;
	}

	/**
	 * method:           getCurrentConfiguration
	 * description:      returns current configuration
	 * @author           Adrian Schuetz
	 * @return           configuration
	 */
	public Configuration getCurrentConfiguration(){
		return this.current;
	}

	/**
	 * method:           setWorkingDirectory
	 * description:      set current working directory
	 * @author           Adrian Schuetz
	 * @param            file
	 */
	public void setWorkingDirectory(File file){
		this.workingDirectory = file;
	}

	/**
	 * method:           isImage
	 * description:      true if an image is selected
	 * @author           Adrian Schuetz
	 * @return           true or false
	 */
	public boolean isImage()
	{
		if (originalImage == null){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * method:           isConfiguration
	 * description:      true if a configuration is selected
	 * @author           Adrian Schuetz
	 * @return           true or false
	 */
	public boolean isConfiguration()
	{
		if (this.current == null){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * method:           getWorkingDirectory
	 * description:      returns the current working directory
	 * @author           Adrian Schuetz
	 * @return           working directory
	 */
	public File getWorkingDirectory(){
		return this.workingDirectory;
	}

	/**
	 * method:           configurationSave
	 * description:      saves the configuration
	 * @author           Adrian Schuetz
	 * @exception        IOException
	 */
	public void configurationSave(Configuration configuration) throws IOException {
		try {
			FileOutputStream file = new FileOutputStream(workingDirectory+"/"+configuration.getName()+".cfg");
			ObjectOutputStream object = new ObjectOutputStream(file);
			object.writeObject(configuration);
			object.close();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * method:           configurationLoad
	 * description:      loads the configuration
	 * @author           Adrian Schuetz
	 * @exception        IOException
	 */
	public Configuration configurationLoad(String configuration) throws IOException {
		Configuration confi = new Configuration();
		try {
			FileInputStream file = new FileInputStream(workingDirectory+"/"+configuration);
			ObjectInputStream object = new ObjectInputStream(file);
			confi = (Configuration)object.readObject();
			object.close();
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return confi;
	}

	/**
	 * method:           initConfigurationLegoTop
	 * description:      creates the system configuration Lego top view
	 * @author           Adrian Schuetz
	 */
	private void initConfigurationLegoTop(){
		boolean[][] element_2x1			= 	{{true,true}};
		boolean[][] element_1x2			= 	{{true},{true}};
		boolean[][] element_3x1			= 	{{true,true,true}};
		boolean[][] element_1x3			= 	{{true},{true},{true}};
		boolean[][] element_4x1			= 	{{true,true,true,true}};
		boolean[][] element_1x4			= 	{{true},{true},{true},{true}};
		boolean[][] element_6x1			= 	{{true,true,true,true,true,true}};
		boolean[][] element_1x6			= 	{{true},{true},{true},{true},{true},{true}};
		boolean[][] element_8x1			= 	{{true,true,true,true,true,true,true,true}};
		boolean[][] element_1x8			= 	{{true},{true},{true},{true},{true},{true},{true},{true}};
		boolean[][] element_2x2			= 	{{true,true},{true,true}};
		boolean[][] element_2x2_Corner		= 	{{true,true},{true,false}};
		boolean[][] element_2x2_Corner_90	= 	{{true,true},{false,true}};
		boolean[][] element_2x2_Corner_180	= 	{{false,true},{true,true}};
		boolean[][] element_2x2_Corner_270	= 	{{true,false},{true,true}};
		boolean[][] element_3x2			= 	{{true,true,true},{true,true,true}};
		boolean[][] element_2x3			= 	{{true,true},{true,true},{true,true}};
		boolean[][] element_4x2			= 	{{true,true,true,true},{true,true,true,true}};
		boolean[][] element_2x4			= 	{{true,true},{true,true},{true,true},{true,true}};
		boolean[][] element_6x2			= 	{{true,true,true,true,true,true},{true,true,true,true,true,true}};
		boolean[][] element_2x6			= 	{{true,true},{true,true},{true,true},{true,true},{true,true},{true,true}};
		boolean[][] element_8x2			= 	{{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true}};
		boolean[][] element_2x8			= 	{{true,true},{true,true},{true,true},{true,true},{true,true},{true,true},{true,true},{true,true}};
		boolean[][] element_4x4			= 	{{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true}};
		boolean[][] element_4x4_Corner		= 	{{true,true,true,true},{true,true,true,true},{true,true,false,false},{true,true,false,false}};
		boolean[][] element_4x4_Corner_90	= 	{{true,true,true,true},{true,true,true,true},{false,false,true,true},{false,false,true,true}};
		boolean[][] element_4x4_Corner_180	= 	{{false,false,true,true},{false,false,true,true},{true,true,true,true},{true,true,true,true}};
		boolean[][] element_4x4_Corner_270	= 	{{true,true,false,false},{true,true,false,false},{true,true,true,true},{true,true,true,true}};
		boolean[][] element_6x4			= 	{{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true}};
		boolean[][] element_4x6			= 	{{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true}};
		boolean[][] element_8x4			= 	{{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true}};
		boolean[][] element_4x8			= 	{{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true}};
		boolean[][] element_6x6			= 	{{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true}};
		boolean[][] element_8x6			= 	{{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true}};
		boolean[][] element_6x8			= 	{{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true}};
		boolean[][] element_8x8			= 	{{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true}};
		legoTop.setElement("Plate_2x1_(3023)",2,1,element_2x1,1,4);
		legoTop.setElement("Plate_1x2_(3023)",1,2,element_1x2,1,4);
		legoTop.setElement("Plate_3x1_(3623)",3,1,element_3x1,1,7);
		legoTop.setElement("Plate_1x3_(3623)",1,3,element_1x3,1,7);
		legoTop.setElement("Plate_4x1_(3710)",4,1,element_4x1,1,6);
		legoTop.setElement("Plate_1x4_(3710)",1,4,element_1x4,1,6);
		legoTop.setElement("Plate_6x1_(3666)",6,1,element_6x1,1,7);
		legoTop.setElement("Plate_1x6_(3666)",1,6,element_1x6,1,7);
		legoTop.setElement("Plate_8x1_(3460)",8,1,element_8x1,1,10);
		legoTop.setElement("Plate_1x8_(3460)",1,8,element_1x8,1,10);
		legoTop.setElement("Plate_2x2_Corner_(2420)"    ,2,2,element_2x2_Corner    ,1,7);
		legoTop.setElement("Plate_2x2_Corner_90_(2420)" ,2,2,element_2x2_Corner_90 ,1,7);
		legoTop.setElement("Plate_2x2_Corner_180_(2420)",2,2,element_2x2_Corner_180,1,7);
		legoTop.setElement("Plate_2x2_Corner_270_(2420)",2,2,element_2x2_Corner_270,1,7);
		legoTop.setElement("Plate_2x2_(3022)",2,2,element_2x2,1,3);
		legoTop.setElement("Plate_3x2_(3021)",3,2,element_3x2,1,4);
		legoTop.setElement("Plate_2x3_(3021)",2,3,element_2x3,1,4);
		legoTop.setElement("Plate_4x2_(3020)",4,2,element_4x2,1,5);
		legoTop.setElement("Plate_2x4_(3020)",2,4,element_2x4,1,5);
		legoTop.setElement("Plate_6x2_(3795)",6,2,element_6x2,1,7);
		legoTop.setElement("Plate_2x6_(3795)",2,6,element_2x6,1,7);
		legoTop.setElement("Plate_8x2_(3024)",8,2,element_8x2,1,9);
		legoTop.setElement("Plate_2x8_(3024)",2,8,element_2x8,1,9);
		legoTop.setElement("Plate_4x4_(3031)",4,4,element_4x4,1,10);
		legoTop.setElement("Plate_4x4_Corner_(2639)"    ,4,4,element_4x4_Corner    ,1,32);
		legoTop.setElement("Plate_4x4_Corner_90_(2639)" ,4,4,element_4x4_Corner_90 ,1,32);
		legoTop.setElement("Plate_4x4_Corner_180_(2639)",4,4,element_4x4_Corner_180,1,32);
		legoTop.setElement("Plate_4x4_Corner_270_(2639)",4,4,element_4x4_Corner_270,1,32);
		legoTop.setElement("Plate_6x4_(3032)",6,4,element_6x4,1,17);
		legoTop.setElement("Plate_4x6_(3032)",4,6,element_4x6,1,17);
		legoTop.setElement("Plate_8x4_(3035)",8,4,element_8x4,1,23);
		legoTop.setElement("Plate_4x8_(3035)",4,8,element_4x8,1,23);
		legoTop.setElement("Plate_6x6_(3958)",6,6,element_6x6,1,28);
		legoTop.setElement("Plate_8x6_(3036)",8,6,element_8x6,1,44);
		legoTop.setElement("Plate_6x8_(3036)",6,8,element_6x8,1,44);
		legoTop.setElement("Plate_8x8_(41539)",8,8,element_8x8,1,74);
		legoTop.setColor("11_Black",0,0,0);
		legoTop.setColor("85_Dark_Bluish_Gray",73,88,101);
		legoTop.setColor("86_Light_Bluish_Gray",161,171,178);
		legoTop.setColor("99_Very_Light_Bluish_Gray",198,205,209);
		legoTop.setColor("01_White",255,255,255);
		legoTop.setColor("03_Yellow",255,214,0);
		legoTop.setColor("33_Light_Yellow",255,233,143);
		legoTop.setColor("69_Dark_Tan",144,144,105);
		legoTop.setColor("02_Tan",214,191,145);
		legoTop.setColor("88_Reddish_Brown",104,46,47);
		legoTop.setColor("04_Orange",244,123,32);
		legoTop.setColor("31_Medium_Orange",248,155,28);
		legoTop.setColor("59_Dark_Red",247,4,55);
		legoTop.setColor("05_Red",238,46,36);
		legoTop.setColor("25_Salmon",246,150,124);
		legoTop.setColor("26_Light_Salmon",250,188,174);
		legoTop.setColor("47_Dark_Pink",205,127,181);
		legoTop.setColor("23_Pink",247,179,204);
		legoTop.setColor("89_Dark_Purple",70,42,135);
		legoTop.setColor("24_Purple",127,63,152);
		legoTop.setColor("93_Light_Purple",142,49,146);
		legoTop.setColor("71_Magenta",171,29,137);
		legoTop.setColor("63_Dark_Blue",0,54,96);
		legoTop.setColor("07_Blue",0,87,164);
		legoTop.setColor("42_Medium_Blue",94,174,224);
		legoTop.setColor("62_Light_Blue",193,224,244);
		legoTop.setColor("39_Dark_Turquoise",0,146,121);
		legoTop.setColor("40_Light_Turquoise",0,179,176);
		legoTop.setColor("41_Aqua",162,218,222);
		legoTop.setColor("80_Dark_Green",0,65,37);
		legoTop.setColor("06_Green",0,148,74);
		legoTop.setColor("37_Medium_Green",151,211,184);
		legoTop.setColor("38_Light_Green",186,225,209);
		legoTop.setColor("34_Lime",181,206,47);
		legoTop.setColor("76_Medium_Lime",193,216,55);
		legoTop.setColor("35_Light_Lime",197,225,170);
	}

	/**
	 * method:           initConfigurationLegoSide
	 * description:      creates the system configuration Lego side view
	 * @author           Adrian Schuetz
	 */
	private void initConfigurationLegoSide(){
		boolean[][] element_2x1			= 	{{true,true}};
		boolean[][] element_3x1			= 	{{true,true,true}};
		boolean[][] element_4x1			= 	{{true,true,true,true}};
		boolean[][] element_6x1			= 	{{true,true,true,true,true,true}};
		boolean[][] element_8x1			= 	{{true,true,true,true,true,true,true,true}};
		boolean[][] element_1x3			= 	{{true},{true},{true}};
		boolean[][] element_2x3			= 	{{true,true},{true,true},{true,true}};
		boolean[][] element_3x3			= 	{{true,true,true},{true,true,true},{true,true,true}};
		boolean[][] element_4x3			= 	{{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true}};
		boolean[][] element_6x3			= 	{{true,true,true,true,true,true},{true,true,true,true,true,true},{true,true,true,true,true,true}};
		boolean[][] element_8x3			= 	{{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true},{true,true,true,true,true,true,true,true}};
		legoSide.setElement("Plate_1x2_(3023)",2,1,element_2x1,8,4);
		legoSide.setElement("Plate_1x3_(3623)",3,1,element_3x1,9,7);
		legoSide.setElement("Plate_1x4_(3710)",4,1,element_4x1,10,6);
		legoSide.setElement("Plate_1x6_(3666)",6,1,element_6x1,11,7);
		legoSide.setElement("Plate_1x8_(3460)",8,1,element_8x1,12,10);
		legoSide.setElement("Brick_1x1_(3005)",1,3,element_1x3,1,2);
		legoSide.setElement("Brick_1x2_(3004)",2,3,element_2x3,2,2);
		legoSide.setElement("Brick_1x3_(3622)",3,3,element_3x3,3,4);
		legoSide.setElement("Brick_1x4_(3010)",4,3,element_4x3,4,4);
		legoSide.setElement("Brick_1x6_(3009)",6,3,element_6x3,5,7);
		legoSide.setElement("Brick_1x8_(3008)",8,3,element_8x3,6,14);
		legoSide.setColor("11_Black",0,0,0);
		legoSide.setColor("85_Dark_Bluish_Gray",73,88,101);
		legoSide.setColor("86_Light_Bluish_Gray",161,171,178);
		legoSide.setColor("99_Very_Light_Bluish_Gray",198,205,209);
		legoSide.setColor("01_White",255,255,255);
		legoSide.setColor("03_Yellow",255,214,0);
		legoSide.setColor("33_Light_Yellow",255,233,143);
		legoSide.setColor("69_Dark_Tan",144,144,105);
		legoSide.setColor("02_Tan",214,191,145);
		legoSide.setColor("88_Reddish_Brown",104,46,47);
		legoSide.setColor("04_Orange",244,123,32);
		legoSide.setColor("31_Medium_Orange",248,155,28);
		legoSide.setColor("59_Dark_Red",247,4,55);
		legoSide.setColor("05_Red",238,46,36);
		legoSide.setColor("25_Salmon",246,150,124);
		legoSide.setColor("26_Light_Salmon",250,188,174);
		legoSide.setColor("47_Dark_Pink",205,127,181);
		legoSide.setColor("23_Pink",247,179,204);
		legoSide.setColor("89_Dark_Purple",70,42,135);
		legoSide.setColor("24_Purple",127,63,152);
		legoSide.setColor("93_Light_Purple",142,49,146);
		legoSide.setColor("71_Magenta",171,29,137);
		legoSide.setColor("63_Dark_Blue",0,54,96);
		legoSide.setColor("07_Blue",0,87,164);
		legoSide.setColor("42_Medium_Blue",94,174,224);
		legoSide.setColor("62_Light_Blue",193,224,244);
		legoSide.setColor("39_Dark_Turquoise",0,146,121);
		legoSide.setColor("40_Light_Turquoise",0,179,176);
		legoSide.setColor("41_Aqua",162,218,222);
		legoSide.setColor("80_Dark_Green",0,65,37);
		legoSide.setColor("06_Green",0,148,74);
		legoSide.setColor("37_Medium_Green",151,211,184);
		legoSide.setColor("38_Light_Green",186,225,209);
		legoSide.setColor("34_Lime",181,206,47);
		legoSide.setColor("76_Medium_Lime",193,216,55);
		legoSide.setColor("35_Light_Lime",197,225,170);
	}

	/**
	 * method:           initConfigurationMinisteck
	 * description:      creates the system configuration Ministeck
	 * @author           Adrian Schuetz
	 */
	private void initConfigurationMinisteck(){
		boolean[][] element_2x1			= 	{{true,true}};
		boolean[][] element_1x2			= 	{{true},{true}};
		boolean[][] element_3x1			= 	{{true,true,true}};
		boolean[][] element_1x3			= 	{{true},{true},{true}};
		boolean[][] element_2x2			= 	{{true,true},{true,true}};
		boolean[][] element_2x2_Corner		= 	{{true,true},{true,false}};
		boolean[][] element_2x2_Corner_90	= 	{{true,true},{false,true}};
		boolean[][] element_2x2_Corner_180	= 	{{false,true},{true,true}};
		boolean[][] element_2x2_Corner_270	= 	{{true,false},{true,true}};
		ministeck.setElement("Ministeck_2x1",2,1,element_2x1,1,1);
		ministeck.setElement("Ministeck_1x2",1,2,element_1x2,1,1);
		ministeck.setElement("Ministeck_3x1",3,1,element_3x1,1,1);
		ministeck.setElement("Ministeck_1x3",1,3,element_1x3,1,1);
		ministeck.setElement("Ministeck_2x2_Corner"    ,2,2,element_2x2_Corner    ,1,1);
		ministeck.setElement("Ministeck_2x2_Corner_90" ,2,2,element_2x2_Corner_90 ,1,1);
		ministeck.setElement("Ministeck_2x2_Corner_180",2,2,element_2x2_Corner_180,1,1);
		ministeck.setElement("Ministeck_2x2_Corner_270",2,2,element_2x2_Corner_270,1,1);
		ministeck.setElement("Ministeck_2x2",2,2,element_2x2,1,1);
		ministeck.setColor("601_Black",0,0,0);
		ministeck.setColor("602_Dark_Blue",36,60,150);
		ministeck.setColor("624_Medium_Blue",134,187,229);
		ministeck.setColor("621_Medium_Green",67,182,73);
		ministeck.setColor("605_Dark_Green",0,100,62);
		ministeck.setColor("606_Red",169,17,44);
		ministeck.setColor("607_Orange",242,103,36);
		ministeck.setColor("608_Yellow",255,242,3);
		ministeck.setColor("609_Beige",255,233,143);
		ministeck.setColor("610_Light_Brown",252,181,21);
		ministeck.setColor("611_Medium_Brown",160,94,18);
		ministeck.setColor("612_Dark_Brown",106,33,0);
		ministeck.setColor("613_White",255,255,255);
		ministeck.setColor("614_Light_Grey",165,169,172);
		ministeck.setColor("615_Light_Pink",245,154,169);
		ministeck.setColor("616_Dark_Grey",121,124,130);
		ministeck.setColor("617_Olive",76,55,4);
		ministeck.setColor("619_Flesh",251,193,159);
		ministeck.setColor("620_Purple",91,24,106);
		ministeck.setColor("604_Light_Green",0,169,79);
		ministeck.setColor("622_Corn_Gold",248,155,28);
		ministeck.setColor("623_Pink",234,54,146);
		ministeck.setColor("603_Light_Blue",0,111,186);
		ministeck.setColor("635_Grey_1",28,43,57);
		ministeck.setColor("636_Grey_2",73,88,101);
		ministeck.setColor("637_Grey_3",114,128,138);
		ministeck.setColor("638_Grey_4",161,171,178);
		ministeck.setColor("639_Grey_5",198,205,209);
		ministeck.setColor("641_Grey_6",216,220,219);
	}

	/**
	 * method:           imageReset
	 * description:      resets the image object
	 * @author           Adrian Schuetz
	 */
	public void imageReset(){
		this.originalImage = null;
	}

	/**
	 * method:           imageLoad
	 * description:      Checks if the image file type is valid and creates an image
	 * @author           Adrian Schuetz
	 * @param            file
	 * @return           true or false
	 */
	public boolean imageLoad(File file)
	{
		if (file != null){
			try{
				originalImage = new Picture(file);
				if(originalImage.getImage() == null){
					//invalid image file
					return false;
				}
			}catch(IOException e){
				//error loading the image
				System.out.println(e.toString());
				return false;
			}
			return true;
		}else{
			//image loading dialog is canceled
			return false;
		}
	}

	/**
	 * method:           replaceImageByCutout
	 * description:      Calls the method cutout from originalImage
	 * @author           Adrian Schuetz
	 * @param            cutout
	 */
	public void replaceImageByCutout(Rectangle cutout){
		originalImage.cutout(cutout);
	}

	/**
	 * method:           getScaledImage
	 * description:      Calls the method getScaledImage (scaling by sliderValue)
	 * @author           Adrian Schuetz
	 * @param            mosaic (true/false)
	 * @param            sliderValue
	 * @return           BufferedImage (scaled image)
	 */
	public BufferedImage getScaledImage(boolean mosaic, int sliderValue)
	{
		if(mosaic){
			return mosaicImage.getScaledImage(sliderValue);
		}else{
			return originalImage.getScaledImage(sliderValue);
		}
	}

	/**
	 * method:           getImage
	 * description:      Calls the method getImage
	 * @author           Adrian Schuetz
	 * @param            mosaic (true/false)
	 * @return           BufferedImage
	 */
	public BufferedImage getImage(boolean mosaic)
	{
		if(mosaic){
			return mosaicImage.getImage();
		}else{
			return originalImage.getImage();
		}
	}

	/**
	 * method:           generateMosaic
	 * description:      Generates a new mosaic
	 * @author           Adrian Schuetz
	 * @param            width
	 * @param            height
	 */
	public void generateMosaic(int width, int height){
		this.mosaic = new Mosaic(width, height, this);
	}

	/**
	 * method:           generateMosaicImage
	 * description:      Generates an image from the mosaic
	 * @author           Adrian Schuetz
	 */
	public void generateMosaicImage(boolean threeDInfo){
		this.mosaicImage = null;
		this.mosaic.generateMosaicImage(this.getCurrentConfiguration(), threeDInfo);
	}

	/**
	 * method:           setMosaicImage
	 * description:      Sets the variable mosaicImage
	 * @author           Adrian Schuetz
	 */
	public void setMosaicImage(BufferedImage image){
		this.mosaicImage = new Picture(image);
		dataProcessing.showMosaic();
	}

	/**
	 * method:          refreshProgressBarAlgorithm
	 * description:     Changes the value of the display
	 * @author          Adrian Schuetz
	 * @param           value
	 * @param           number = 1:quantisation, 2:tiling und 3:painting
	 */
	public void refreshProgressBarAlgorithm(int value, int number){
		dataProcessing.refreshProgressBarAlgorithm(value,number);
	}

	/**
	 * method:           getConfigurationDimension
	 * description:      Returns the aspect ratio of the basis element
	 * @author           Adrian Schuetz
	 * @return           aspect ratio basis element
	 */
	public Dimension getConfigurationDimension()
	{
		Dimension d = new Dimension(current.getBasisWidth(), current.getBasisHeight());
		return d;
	}

	/**
	 * method:           computeScaleFactor
	 * description:      Calls the method computeScaleFactor
	 * @author           Adrian Schuetz
	 * @param            mosaic (true/false)
	 * @param            width
	 * @param            height
	 */
	public void computeScaleFactor(boolean mosaic, double width, double height)
	{
		if(mosaic){
			mosaicImage.computeScaleFactor(width, height);
		}else{
			originalImage.computeScaleFactor(width, height);
		}
	}

	/**
	 * method:           saveWorkingDirectory
	 * description:      Saves information about the current working directory
	 * @author           Adrian Schuetz
	 * @param            working directory
	 * @return           true or false
	 */
	public boolean saveWorkingDirectory(File workingDirectory){
		try{
			FileOutputStream file = new FileOutputStream("workingDirectory.ser");
			ObjectOutputStream outputstream = new ObjectOutputStream(file);
			outputstream.writeObject (workingDirectory);
			outputstream.close();
			return true;
		}catch(IOException e){
			//Error writing the file
			System.out.println(e.toString());
			return false;
		}
	}

	/**
	 * method:           loadWorkingDirectory
	 * description:      Loads information about the current working directory (if available)
	 * @author           Adrian Schuetz
	 * @return           working directory
	 */
	public File loadWorkingDirectory(){
		try{
			File workingDirectory;
			FileInputStream file = new FileInputStream("workingDirectory.ser");
			ObjectInputStream inputstream = new ObjectInputStream(file);
			workingDirectory = (File)inputstream.readObject();
			inputstream.close();
			return workingDirectory;
		}catch(IOException io){
			System.out.println(io.toString());
			return null;
		}catch(ClassNotFoundException classNotFound){
			return null;
		}
	}
}
