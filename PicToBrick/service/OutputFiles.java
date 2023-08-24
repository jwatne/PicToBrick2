package PicToBrick.service;

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.SwingUtilities;

import PicToBrick.model.ColorObject;
import PicToBrick.model.ElementObject;

/**
 * class:            OutputFiles
 * layer:            DataProcessing (three tier architecture)
 * description:      build all output files
 * @author           Tobias Reichling
 */
public class OutputFiles {

	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");

	//############################## all
	private final static String head = new String("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n<head>\r\n<title>PicToBrick</title>\r\n<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\r\n<style type=\"text/css\">\r\n#menu {\r\nleft:0;\r\ntop:10;\r\nwidth:200px;\r\nposition:absolute;\r\npadding-left:10px;\r\npadding-right:10px;\r\npadding-top:0px;\r\nmargin-left:0;\r\nmargin-top:0;\r\nmargin-right:0;\r\nfont-family:Verdana, Arial;\r\nfont-size:small;\r\n}\r\n#content {\r\nmargin-top:0px;\r\nmargin-left:200px;\r\nmargin-right:0px;\r\npadding-left:10px;\r\npadding-right:10px;\r\npadding-top:0px;\r\nfont-family:Verdana, Arial;\r\nfont-size:small;\r\n}\r\n#headline {\r\nfont-size:x-large;\r\n}\r\na:link {\r\ntext-decoration:none;\r\ncolor:blue;\r\n}\r\na:visited {\r\ntext-decoration:none;\r\ncolor:blue;\r\n}\r\na:hover {\r\ntext-decoration:underline;\r\ncolor:blue;\r\n}\r\na:active {\r\ntext-decoration:underline;\r\ncolor:blue;\r\n}\r\ntd {\r\npadding-right:10px;\r\npadding-left:10px;\r\n}\r\n</style>\r\n</head>\r\n<body>\r\n");
	private final static String end = new String("<br />\r\n</div>\r\n</body>\r\n</html>");
	private final static String projectname = new String("</ul>\r\n</div>\r\n<div id=\"content\">\r\n<p>\r\n<strong>PicToBrick - " + textbundle.getString("output_outputFiles_1") + "</strong>\r\n</p>\r\n<p id=\"headline\">\r\n<strong>" + textbundle.getString("output_outputFiles_2") + ": ");

	//############################## all without index.html
	private final static String menu_start = new String("<div id=\"menu\">\r\n<ul>\r\n<li>\r\n<a href=\"../index.html\">\r\n" + textbundle.getString("output_outputFiles_3") + "\r\n</a>\r\n</li>\r\n");
	private final static String menu_grafic = new String("<li>\r\n<a href=\"grafic.html\">\r\n" + textbundle.getString("output_outputFiles_4") + "\r\n</a>\r\n</li>\r\n");
	private final static String menu_configuration = new String("<li>\r\n<a href=\"configuration.html\">\r\n" + textbundle.getString("output_outputFiles_5") + "\r\n</a>\r\n<ul>\r\n<li>\r\n<a href=\"colors.html\">\r\n" + textbundle.getString("output_outputFiles_6") + "\r\n</a>\r\n</li>\r\n<li>\r\n<a href=\"elements.html\">\r\n" + textbundle.getString("output_outputFiles_7") + "\r\n</a>\r\n</li>\r\n</ul>\r\n</li>\r\n");
	private final static String menu_material = new String("<li>\r\n<a href=\"billofmaterial.html\">\r\n" + textbundle.getString("output_outputFiles_8") + "\r\n</a>\r\n</li>\r\n");
	private final static String menu_instruction = new String("<li>\r\n<a href=\"buildinginstruction.html\">\r\n" + textbundle.getString("output_outputFiles_9") + "\r\n</a>\r\n</li>\r\n");
	private final static String menu_xml = new String("<li>\r\n<a href=\"xml.html\">\r\n" + textbundle.getString("output_outputFiles_10") + "\r\n</a>\r\n</li>\r\n");
	private final static String menu_additional = new String("<li>\r\n<a href=\"additional.html\">\r\n" + textbundle.getString("output_outputFiles_11") + "\r\n</a>\r\n</li>\r\n");
	//---------->all:projectname
	private final static String project_end = new String("</strong>\r\n</p>\r\n");

	//############################## index.html
	//---------->all:head
	private final static String index_menu_start = new String("<div id=\"menu\">\r\n<ul>\r\n<li>\r\n<a href=\"index.html\">\r\n" + textbundle.getString("output_outputFiles_3") + "\r\n</a>\r\n</li>\r\n");
	private final static String index_menu_grafic = new String("<li>\r\n<a href=\"data/grafic.html\">\r\n" + textbundle.getString("output_outputFiles_4") + "\r\n</a>\r\n</li>\r\n");
	private final static String index_menu_configuration = new String("<li>\r\n<a href=\"data/configuration.html\">\r\n" + textbundle.getString("output_outputFiles_5") + "\r\n</a>\r\n<ul>\r\n<li>\r\n<a href=\"data/colors.html\">\r\n" + textbundle.getString("output_outputFiles_6") + "\r\n</a>\r\n</li>\r\n<li>\r\n<a href=\"data/elements.html\">\r\n" + textbundle.getString("output_outputFiles_7") + "\r\n</a>\r\n</li>\r\n</ul>\r\n</li>\r\n");
	private final static String index_menu_material = new String("<li>\r\n<a href=\"data/billofmaterial.html\">\r\n" + textbundle.getString("output_outputFiles_8") + "\r\n</a>\r\n</li>\r\n");
	private final static String index_menu_instruction = new String("<li>\r\n<a href=\"data/buildinginstruction.html\">\r\n" + textbundle.getString("output_outputFiles_9") + "\r\n</a>\r\n</li>\r\n");
	private final static String index_menu_xml = new String("<li>\r\n<a href=\"data/xml.html\">\r\n" + textbundle.getString("output_outputFiles_10") + "\r\n</a>\r\n</li>\r\n");
	private final static String index_menu_additional = new String("<li>\r\n<a href=\"data/additional.html\">\r\n" + textbundle.getString("output_outputFiles_11") + "\r\n</a>\r\n</li>\r\n");
	//---------->projectname
	private final static String index_content_project_2 = new String("</strong>\r\n</p>\r\n<p>\r\n<strong>" + textbundle.getString("output_outputFiles_12") + ":</strong>\r\n<br />\r\n");
	//---------->width x height
	private final static String index_content_project_3 = new String(" " + textbundle.getString("output_outputFiles_13") + "\r\n<br />\r\n");
	//---------->width x height
	private final static String index_content_project_4 = new String(" mm\r\n<br />\r\n</p>\r\n");
	private final static String index_content_grafic = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_4") + "</strong><br />\r\n" + textbundle.getString("output_outputFiles_14") + "\r\n</p>\r\n");
	private final static String index_content_configuration = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_5") + "</strong><br />\r\n" + textbundle.getString("output_outputFiles_15") + "\r\n</p>\r\n");
	private final static String index_content_material = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_8") + "</strong><br />\r\n" + textbundle.getString("output_outputFiles_16") + "\r\n</p>\r\n");
	private final static String index_content_instruction = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_9") + "</strong><br />\r\n" + textbundle.getString("output_outputFiles_17") + "\r\n</p>\r\n");
	private final static String index_content_xml = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_10") + "</strong><br />\r\n" + textbundle.getString("output_outputFiles_18") + "\r\n</p>\r\n");
	//---------->all:end

	//############################## grafic.html
	//---------->all:head
	//---------->all without index: from menu_start till project_end
	private final static String grafic_start = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_4") + "</strong><br />\r\n" + textbundle.getString("output_outputFiles_19") + "\r\n</p>\r\n<p>\r\n<img src=\"mosaic.jpg\" width=\"");
	//---------->image width
	private final static String grafic_height = new String("\" height=\"");
	//---------->image height
	private final static String grafic_print_width = new String("\" alt=\"mosaic\" />\r\n</p>\r\n<p>\r\n<a href=\"mosaic.jpg\">\r\n" + textbundle.getString("output_outputFiles_20") + "\r\n</a>\r\n</p>\r\n<p>\r\n" + textbundle.getString("output_outputFiles_21") + " ");
	//---------->image print_width
	private final static String grafic_end = new String(" " + textbundle.getString("output_outputFiles_22") + "\r\n</p>\r\n");
	//---------->all:end

	//############################## configuration.html
	//---------->all:head
	//---------->all without index: from menu_start till project_end
	private final static String configuration_start = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_5") + "</strong><br />\r\n</p>\r\n");
	private final static String configuration_name = new String("<p>\r\n" + textbundle.getString("output_outputFiles_23") + ": ");
	//---------->configuration name
	private final static String configuration_basis_name = new String("\r\n<br />\r\n" + textbundle.getString("output_outputFiles_24") + ": ");
	//---------->basis name
	private final static String configuration_basis_width = new String("\r\n<br />\r\n" + textbundle.getString("output_outputFiles_25") + ": ");
	//---------->basis width
	private final static String configuration_basis_height = new String("\r\n<br />\r\n" + textbundle.getString("output_outputFiles_26") + ": ");
	//---------->basis height
	private final static String configuration_basis_widthmm = new String("\r\n<br />\r\n" + textbundle.getString("output_outputFiles_27") + ": ");
	//---------->basis width mm
	private final static String configuration_end = new String("\r\n</p>\r\n<p>\r\n" + textbundle.getString("output_outputFiles_28") + " <a href=\"colors.html\">" + textbundle.getString("output_outputFiles_6") + "</a> " + textbundle.getString("output_outputFiles_29") + " <a href=\"elements.html\">" + textbundle.getString("output_outputFiles_7") + "</a> " + textbundle.getString("output_outputFiles_30") + ".\r\n</p>\r\n");
	//---------->all:end

	//############################## elements.html
	//---------->all:head
	//---------->all without index: from menu_start till project_end
	private final static String element_start = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_7") + "</strong>\r\n<br />\r\n</p>\r\n<table>\r\n");
	private final static String element_cell_start = new String("<tr>\r\n<td>\r\n");
	//<img src="XXX.jpg" width="432" height="144" alt="mosaic" />
	private final static String element_name = new String("\r\n</td>\r\n<td>\r\n<strong>" + textbundle.getString("output_outputFiles_31") + ":</strong> ");
	//---------->name
	private final static String element_width = new String("\r\n<br />\r\n" + textbundle.getString("output_outputFiles_32") + ": ");
	//---------->width
	private final static String element_height = new String("\r\n<br />\r\n" + textbundle.getString("output_outputFiles_33") + ": ");
	//---------->height
	private final static String element_stability = new String("\r\n<br />\r\n" + textbundle.getString("output_outputFiles_34") + ": ");
	//---------->stability
	private final static String element_costs = new String("\r\n<br />\r\n" + textbundle.getString("output_outputFiles_35") + ": ");
	//---------->costs
	private final static String element_cell_end = new String("\r\n</td>\r\n</tr>\r\n");
	private final static String element_end = new String("</table>\r\n");
	//---------->all:end

	//############################## colors.html
	//---------->all:head
	//---------->all without index: from menu_start till project_end
	private final static String color_start = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_6") + "</strong><br />\r\n</p>\r\n<table>\r\n");
	private final static String color_cell_start = new String("<tr>\r\n<td>\r\n<img src=\"");
	//---------->file name image
	private final static String color_image = new String(".jpg\" width=\"300\" height=\"50\" alt=\"color\" />\r\n</td>\r\n<td>\r\n");
	//---------->color name
	private final static String color_info = new String("\r\n<br />\r\n" + textbundle.getString("output_outputFiles_36") + ": ");
	//---------->rgb
	private final static String color_cell_end = new String("\r\n</td>\r\n</tr>\r\n");
	private final static String color_end = new String("</table>\r\n");
	//---------->all:end

	//############################## billofmaterial.html
	//---------->all:head
	//---------->all without index: from menu_start till project_end
	private final static String material_start = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_8") + "</strong><br />\r\n</p>\r\n<table>\r\n");
	private final static String material_row_start = new String("<tr>\r\n<td>\r\n");
	//---------->color
	private final static String material_between = new String("\r\n</td>\r\n<td>\r\n");
	//---------->element
	//-----------material_between
	//---------->quantity + " piece"
	private final static String material_row_end = new String("\r\n</td>\r\n</tr>\r\n");
	private final static String material_end = new String("</table>\r\n");
	//---------->all:end

	//############################## buildinginstruction.html
	//---------->all:head
	//---------->all without index: from menu_start till project_end
	private final static String instruction_start = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_9") + "</strong><br />\r\n</p>\r\n<ul>\r\n");
	private final static String instruction_cell_start = new String("<li>\r\n");
	//---------->row x, column y: name, color
	private final static String instruction_cell_end = new String("\r\n</li>\r\n");
	private final static String instruction_end = new String("</ul>\r\n");
	//---------->all:end

	//############################## xml.html
	//---------->all:head
	//---------->all without index: from menu_start till project_end
	private final static String xml_all = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_10") + "</strong><br />\r\n</p>\r\n<p>\r\n" + textbundle.getString("output_outputFiles_18") + "\r\n</p>\r\n<p>\r\n<a href=\"mosaic.xml\">\r\n" + textbundle.getString("output_outputFiles_37") + "\r\n</a>\r\n</p>");
	//---------->all:end

	//############################## mosaic.xml
	private final static String xml_document_start1 = new String("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n<project name=\"");
	//---------->projectname
	private final static String xml_document_start2 = new String("\">");
	private final static String xml_document_row_start = new String("<row>\r\n");
	private final static String xml_document_element_start = new String("<unit>\r\n<element>\r\n");
	//---------->element
	private final static String xml_document_element_center = new String("\r\n</element>\r\n<color>\r\n");
	//---------->color
	private final static String xml_document_element_end = new String("\r\n</color>\r\n</unit>\r\n");
	private final static String xml_document_element_empty = new String("<unit />\r\n");
	private final static String xml_document_row_end = new String("</row>\r\n");
	private final static String xml_document_end = new String("</project>\r\n");

	//############################## additional.html
	//---------->all:head
	//---------->all without index: from menu_start till project_end
	private final static String additional_start = new String("<p>\r\n<strong>" + textbundle.getString("output_outputFiles_11") + "</strong><br />\r\n</p>\r\n");
	//---------->inelementation
	private final static String additional_cell_end = new String("\r\n<br />\r\n");
	//---------->all:end


	private String project;
	private DataProcessing dataProcessing;
	private Color colorNormal = new Color(59,45,167);
	private Color colorLight = new Color(159,145,255);
	private Color colorDark = new Color(0,0,67);
	private Color colorWhite = new Color(255,255,255);
	private int percent = 0;
	private int referenceValue = 0;

	/**
	 * method:           OutputFiles
	 * description:      constructor
	 * @author           Tobias Reichling
	 */
	public OutputFiles(DataProcessing dataProcessing){
		this.dataProcessing = dataProcessing;
	}

	/**
	 * method:           setProject
	 * description:      sets the projects name
	 * @author           Tobias Reichling
	 * @param            projects name
	 */
	public void setProject(String name){
		this.project = name;
	}

	/**
	 * method:           generateDocuments
	 * description:      generates documents and returns a message
	 * @author           Tobias Reichling
	 * @param            grafic    			true, if document will be generated
	 * @param            configuration     	true, if document will be generated
	 * @param            material  			true, if document will be generated
	 * @param            instruction   		true, if document will be generated
	 * @param            xml       			true, if document will be generated
	 * @return           message   			error
	 */
	public String generateDocuments(boolean grafic, boolean configuration, boolean material, boolean instruction, boolean xml, Enumeration infos){
		String error = new String("");
		//index.html
		StringBuffer index = new StringBuffer();
		index.append(head);
		index.append(index_menu_start);
		if (grafic){index.append(index_menu_grafic);};
		if (configuration){index.append(index_menu_configuration);};
		if (material){index.append(index_menu_material);};
		if (instruction){index.append(index_menu_instruction);};
		if (xml){index.append(index_menu_xml);};
		index.append(index_menu_additional);
		index.append(projectname);
		index.append(project);
		index.append(index_content_project_2);
		index.append(dataProcessing.getMosaicWidth()+" x "+dataProcessing.getMosaicHeight());
		index.append(index_content_project_3);
		int width2 = (int)(100*(dataProcessing.getCurrentConfiguration().getBasisWidthMM()*dataProcessing.getMosaicWidth()));
		int height2 =  (int)(100*(dataProcessing.getCurrentConfiguration().getBasisWidthMM()/dataProcessing.getCurrentConfiguration().getBasisWidth()*dataProcessing.getCurrentConfiguration().getBasisHeight()*dataProcessing.getMosaicHeight()));
		index.append((width2/100) + textbundle.getString("output_decimalPoint") + (width2%100) + " x " + (height2/100) + textbundle.getString("output_decimalPoint") + (height2%100));
		index.append(index_content_project_4);
		if (grafic){index.append(index_content_grafic);};
		if (configuration){index.append(index_content_configuration);};
		if (material){index.append(index_content_material);};
		if (instruction){index.append(index_content_instruction);};
		if (xml){index.append(index_content_xml);};
		index.append(end);
		if (!(dataProcessing.generateUTFOutput(index.toString(), "index.html", false))){
			error =  error + "index.html " + textbundle.getString("output_outputFiles_38") + ".\n\r";
		}


		//grafic.html
		if (grafic){
			try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
				dataProcessing.animateGraficProgressBarOutputFiles(true);
				dataProcessing.refreshProgressBarOutputFiles(0,1);
			}});}catch(Exception e){System.out.println(e);}
			StringBuffer graficString = new StringBuffer();
			graficString.append(head);
			graficString.append(menu_start);
			if (grafic){graficString.append(menu_grafic);};
			if (configuration){graficString.append(menu_configuration);};
			if (material){graficString.append(menu_material);};
			if (instruction){graficString.append(menu_instruction);};
			if (xml){graficString.append(menu_xml);};
			graficString.append(menu_additional);
			graficString.append(projectname);
			graficString.append(project);
			graficString.append(project_end);
			graficString.append(grafic_start);
			BufferedImage mosaikImage = dataProcessing.getImage(true);
			dataProcessing.generateImageOutput(mosaikImage, "mosaic");
			int height = (int)(mosaikImage.getHeight() * (600.0/mosaikImage.getWidth()));
			int width = 600;
			int print_width = (int)(100*(dataProcessing.getMosaicWidth() * dataProcessing.getCurrentConfiguration().getBasisWidthMM()));
			graficString.append(width);
			graficString.append(grafic_height);
			graficString.append(height);
			graficString.append(grafic_print_width);
			graficString.append((print_width/100) + textbundle.getString("output_decimalPoint") + (print_width%100));
			graficString.append(grafic_end);
			graficString.append(end);
			if (!(dataProcessing.generateUTFOutput(graficString.toString(), "grafic.html", true))){
				error =  error + "grafic.html " + textbundle.getString("output_outputFiles_38") + ".\n\r";
			}
			try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
				dataProcessing.animateGraficProgressBarOutputFiles(false);
				dataProcessing.refreshProgressBarOutputFiles(100,1);
			}});}catch(Exception e){}
		}


		//configuration.html
		if (configuration){
			StringBuffer configurationString = new StringBuffer();
			configurationString.append(head);
			configurationString.append(menu_start);
			if (grafic){configurationString.append(menu_grafic);};
			if (configuration){configurationString.append(menu_configuration);};
			if (material){configurationString.append(menu_material);};
			if (instruction){configurationString.append(menu_instruction);};
			if (xml){configurationString.append(menu_xml);};
			configurationString.append(menu_additional);
			configurationString.append(projectname);
			configurationString.append(project);
			configurationString.append(project_end);
			configurationString.append(configuration_start);
			configurationString.append(configuration_name);
			configurationString.append(dataProcessing.getCurrentConfiguration().getName());
			configurationString.append(configuration_basis_name);
			configurationString.append(dataProcessing.getCurrentConfiguration().getBasisName());
			configurationString.append(configuration_basis_width);
			configurationString.append(dataProcessing.getCurrentConfiguration().getBasisWidth());
			configurationString.append(configuration_basis_height);
			configurationString.append(dataProcessing.getCurrentConfiguration().getBasisHeight());
			configurationString.append(configuration_basis_widthmm);
			int widthMM = (int)(100 * (dataProcessing.getCurrentConfiguration().getBasisWidthMM()));
			configurationString.append((widthMM/100) + textbundle.getString("output_decimalPoint") + (widthMM%100));
			configurationString.append(configuration_end);
			configurationString.append(end);
			if (!(dataProcessing.generateUTFOutput(configurationString.toString(), "configuration.html", true))){
				error =  error + "configuration.html " + textbundle.getString("output_outputFiles_38") + ".\n\r";
			}

			//elements.html
			int width = dataProcessing.getCurrentConfiguration().getBasisWidth();
			int height = dataProcessing.getCurrentConfiguration().getBasisHeight();
			while (width < 10 || height < 10){
				width = width*2;
				height = height*2;
			}
			StringBuffer elementsString = new StringBuffer();
			elementsString.append(head);
			elementsString.append(menu_start);
			if (grafic){elementsString.append(menu_grafic);};
			if (configuration){elementsString.append(menu_configuration);};
			if (material){elementsString.append(menu_material);};
			if (instruction){elementsString.append(menu_instruction);};
			if (xml){elementsString.append(menu_xml);};
			elementsString.append(menu_additional);
			elementsString.append(projectname);
			elementsString.append(project);
			elementsString.append(project_end);
			elementsString.append(element_start);
			ElementObject element = new ElementObject();
			boolean[][] matrix;
			boolean[][] matrixNew;
			BufferedImage elementImage;
			percent = 0;
			referenceValue = (dataProcessing.getCurrentConfiguration().getQuantityColorsAndElements())/10;
			if (referenceValue == 0){referenceValue = 1;}
			int counter = 0;
			int flag1 = 0;
			for (Enumeration elementsEnumeration = dataProcessing.getCurrentConfiguration().getAllElements(); elementsEnumeration.hasMoreElements(); ) {
				counter++;
				element = (ElementObject)elementsEnumeration.nextElement();
				elementsString.append(element_cell_start);
				matrix = element.getMatrix();
				matrixNew = new boolean[element.getHeight()+2][element.getWidth()+2];
				//add a NULL-border to the element matrix
				for(int row = 0; row < (element.getHeight()+2); row++){
					for(int column = 0; column < (element.getWidth()+2); column++){
						if(row == 0 || column == 0){
							matrixNew[row][column] = false;
						}else if(row == element.getHeight()+1 || column == element.getWidth()+1){
							matrixNew[row][column] = false;
						}else{
							matrixNew[row][column] = matrix[row-1][column-1];
						}
					}
				}
				elementImage = new BufferedImage( width*8, height*element.getHeight(), BufferedImage.TYPE_INT_RGB );
				Graphics2D g = elementImage.createGraphics();
				g.setColor(colorWhite);
				g.fillRect(0,0,width*8,height*element.getHeight());
				for(int row = 0; row < (element.getHeight()+2); row++){
					for(int column = 0; column < (element.getWidth()+2); column++){
						if (matrixNew[row][column]){
							g.setColor(colorNormal);
							g.fillRect(width*(column-1),height*(row-1),width,height);
							g.setColor(colorLight);
							//border left
							if(!matrixNew[row][column-1]){
								g.fillRect(width*(column-1),height*(row-1)+2,2,height-4);
							}
							//border top
							if(!matrixNew[row-1][column]){
								g.fillRect(width*(column-1)+2,height*(row-1),width-4,2);
							}
							//corner top left
							if (!(matrixNew[row][column-1] && matrixNew[row-1][column] && matrixNew[row-1][column-1])){
								g.fillRect(width*(column-1),height*(row-1),2,2);
							}
							//corner bottom left
							if (!(matrixNew[row][column-1] && matrixNew[row+1][column] && matrixNew[row+1][column-1])){
								if(matrixNew[row][column-1]){
									g.setColor(colorDark);
								}
								g.fillRect(width*(column-1),height*(row-1)+height-2,2,2);
								g.setColor(colorLight);
							}
							g.setColor(colorDark);
							//border right
							if(!matrixNew[row][column+1]){
								g.fillRect(width*(column-1)+width-2,height*(row-1)+2,2,height-4);
							}
							//border bottom
							if(!matrixNew[row+1][column]){
								g.fillRect(width*(column-1)+2,height*(row-1)+height-2,width-4,2);
							}
							//corner top right
							if (!(matrixNew[row][column+1] && matrixNew[row-1][column] && matrixNew[row-1][column+1])){
								if(matrixNew[row][column+1]){
									g.setColor(colorLight);
								}
								g.fillRect(width*(column-1)+width-2,height*(row-1),2,2);
								g.setColor(colorDark);
							}
							//corner bottom right
							if (!(matrixNew[row][column+1] && matrixNew[row+1][column] && matrixNew[row+1][column+1])){
								g.fillRect(width*(column-1)+width-2,height*(row-1)+height-2,2,2);
							}
						}
					}
				}
				dataProcessing.generateImageOutput(elementImage,"element_"+counter);
				if (flag1%referenceValue == 0){
					//refresh progress bars -> gui thread
					try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
						percent=percent+10;
						dataProcessing.refreshProgressBarOutputFiles(percent,2);
					}});}catch(Exception e){}
				}
				flag1++;
				elementsString.append("<img src=\"" + "element_" + counter + ".jpg\" width=\"" + width*8 + "\" height=\"" + height*element.getHeight() + "\" alt=\"mosaic\" />");
				elementsString.append(element_name);
				elementsString.append(element.getName());
				elementsString.append(element_width);
				elementsString.append(element.getWidth());
				elementsString.append(element_height);
				elementsString.append(element.getHeight());
				elementsString.append(element_stability);
				elementsString.append(element.getStability());
				elementsString.append(element_costs);
				elementsString.append(element.getCosts());
				elementsString.append(element_cell_end);
			}
			elementsString.append(element_end);
			elementsString.append(end);
			if (!(dataProcessing.generateUTFOutput(elementsString.toString(), "elements.html", true))){
				error =  error + "elements.html " + textbundle.getString("output_outputFiles_38") + ".\n\r";
			}

			//colors.html
			StringBuffer colorsString = new StringBuffer();
			colorsString.append(head);
			colorsString.append(menu_start);
			if (grafic){colorsString.append(menu_grafic);};
			if (configuration){colorsString.append(menu_configuration);};
			if (material){colorsString.append(menu_material);};
			if (instruction){colorsString.append(menu_instruction);};
			if (xml){colorsString.append(menu_xml);};
			colorsString.append(menu_additional);
			colorsString.append(projectname);
			colorsString.append(project);
			colorsString.append(project_end);
			colorsString.append(color_start);
			ColorObject color = new ColorObject();
			int counter2 = 0;
			for (Enumeration colorsEnumeration = dataProcessing.getCurrentConfiguration().getAllColors(); colorsEnumeration.hasMoreElements(); ) {
				counter2++;
				color = (ColorObject)colorsEnumeration.nextElement();
				dataProcessing.generateColorOutput(color.getRGB(), "color_"+counter2);
				if (flag1%referenceValue == 0){
					//refresh progress bars -> gui thread
					try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
						percent=percent+10;
						dataProcessing.refreshProgressBarOutputFiles(percent,2);
					}});}catch(Exception e){}
				}
				flag1++;
				colorsString.append(color_cell_start);
				colorsString.append("color_" + counter2);
				colorsString.append(color_image);
				colorsString.append(color.getName());
				colorsString.append(color_info);
				colorsString.append(color.getRGB().getRed() + ", " + color.getRGB().getGreen() + ", " + color.getRGB().getBlue());
				colorsString.append(color_cell_end);
			}
			colorsString.append(color_end);
			colorsString.append(end);
			if (!(dataProcessing.generateUTFOutput(colorsString.toString(), "colors.html", true))){
				error =  error + "colors.html " + textbundle.getString("output_outputFiles_38") + ".\n\r";
			}
		}

		//billofmaterial.html
		if (material){
			StringBuffer billofmaterialString = new StringBuffer();
			billofmaterialString.append(head);
			billofmaterialString.append(menu_start);
			if (grafic){billofmaterialString.append(menu_grafic);};
			if (configuration){billofmaterialString.append(menu_configuration);};
			if (material){billofmaterialString.append(menu_material);};
			if (instruction){billofmaterialString.append(menu_instruction);};
			if (xml){billofmaterialString.append(menu_xml);};
			billofmaterialString.append(menu_additional);
			billofmaterialString.append(projectname);
			billofmaterialString.append(project);
			billofmaterialString.append(project_end);
			billofmaterialString.append(material_start);
			Hashtable materialHash = new Hashtable();
			Enumeration colorsEnumeration = dataProcessing.getCurrentConfiguration().getAllColors();
			Enumeration elementsEnumeration;
			while (colorsEnumeration.hasMoreElements()){
				elementsEnumeration = dataProcessing.getCurrentConfiguration().getAllElements();
				Hashtable elementenHash = new Hashtable();
				while (elementsEnumeration.hasMoreElements()){
					elementenHash.put(((ElementObject)elementsEnumeration.nextElement()).getName(),0);
				}
				materialHash.put(((ColorObject)colorsEnumeration.nextElement()).getName(), elementenHash);
			}
			//count elements
			Vector[][] mosaicMatrix = dataProcessing.getMosaic();
			String elementName, colorName;
			int quantity;
			percent = 0;
			referenceValue = (dataProcessing.getMosaicHeight()*dataProcessing.getMosaicWidth())/100;
			for(int row = 0; row < dataProcessing.getMosaicHeight(); row++){
				for(int column = 0; column < dataProcessing.getMosaicWidth(); column++){
					if (!mosaicMatrix[row][column].isEmpty()){
						elementName = new String((String)mosaicMatrix[row][column].elementAt(0));
						colorName = new String((String)mosaicMatrix[row][column].elementAt(1));
						quantity = (Integer)(((Hashtable)materialHash.get(colorName)).get(elementName));
						((Hashtable)materialHash.get(colorName)).put(elementName, (quantity+1));
					}
					try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
						percent++;
						dataProcessing.refreshProgressBarOutputFiles(percent,3);
					}});}catch(Exception e){}
				}
			}
			//output
			colorsEnumeration = dataProcessing.getCurrentConfiguration().getAllColors();
			while (colorsEnumeration.hasMoreElements()){
				colorName = ((ColorObject)colorsEnumeration.nextElement()).getName();
				elementsEnumeration = dataProcessing.getCurrentConfiguration().getAllElements();
				while (elementsEnumeration.hasMoreElements()){
					elementName = ((ElementObject)elementsEnumeration.nextElement()).getName();
					quantity = (Integer)(((Hashtable)materialHash.get(colorName)).get(elementName));
					if (quantity > 0){
						billofmaterialString.append(material_row_start);
						billofmaterialString.append(colorName);
						billofmaterialString.append(material_between);
						billofmaterialString.append(elementName);
						billofmaterialString.append(material_between);
						billofmaterialString.append(quantity + " " + textbundle.getString("output_outputFiles_39") + "");
						billofmaterialString.append(material_row_end);
					}
				}
			}
			billofmaterialString.append(material_end);
			billofmaterialString.append(end);
			if (!(dataProcessing.generateUTFOutput(billofmaterialString.toString(), "billofmaterial.html", true))){
				error =  error + "billofmaterial.html " + textbundle.getString("output_outputFiles_38") + ".\n\r";
			}
		}

		//instruction.html
		if(instruction){
			Vector[][] mosaicMatrix = dataProcessing.getMosaic();
			StringBuffer instructionString = new StringBuffer();
			instructionString.append(head);
			instructionString.append(menu_start);
			if (grafic){instructionString.append(menu_grafic);};
			if (configuration){instructionString.append(menu_configuration);};
			if (material){instructionString.append(menu_material);};
			if (instruction){instructionString.append(menu_instruction);};
			if (xml){instructionString.append(menu_xml);};
			instructionString.append(menu_additional);
			instructionString.append(projectname);
			instructionString.append(project);
			instructionString.append(project_end);
			instructionString.append(instruction_start);
			mosaicMatrix = dataProcessing.getMosaic();
			percent = 0;
			referenceValue = (dataProcessing.getMosaicHeight()*dataProcessing.getMosaicWidth())/100;
			for(int row = 0; row < dataProcessing.getMosaicHeight(); row++){
				for(int column = 0; column < dataProcessing.getMosaicWidth(); column++){
					instructionString.append(instruction_cell_start);
					instructionString.append(textbundle.getString("output_outputFiles_40") + " " + (row+1) + ", " + textbundle.getString("output_outputFiles_41") + " " + (column+1) + ": ");
					if (mosaicMatrix[row][column].isEmpty()){
						instructionString.append("(" + textbundle.getString("output_outputFiles_42") + ")");
					}else{
						instructionString.append((String)mosaicMatrix[row][column].elementAt(0));
						instructionString.append(", ");
						instructionString.append((String)mosaicMatrix[row][column].elementAt(1));
					}
					instructionString.append(instruction_cell_end);
					try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
						percent++;
						dataProcessing.refreshProgressBarOutputFiles(percent,4);
					}});}catch(Exception e){}
				}
			}
			instructionString.append(instruction_end);
			instructionString.append(end);
			if (!(dataProcessing.generateUTFOutput(instructionString.toString(), "buildinginstruction.html", true))){
				error =  error + "buildinginstruction.html " + textbundle.getString("output_outputFiles_38") + ".\n\r";
			}
		}

		//xml.html
		if(xml){
			Vector[][] mosaicMatrix = dataProcessing.getMosaic();
			StringBuffer xmlAusgabe = new StringBuffer();
			xmlAusgabe.append(head);
			xmlAusgabe.append(menu_start);
			if (grafic){xmlAusgabe.append(menu_grafic);};
			if (configuration){xmlAusgabe.append(menu_configuration);};
			if (material){xmlAusgabe.append(menu_material);};
			if (instruction){xmlAusgabe.append(menu_instruction);};
			if (xml){xmlAusgabe.append(menu_xml);};
			xmlAusgabe.append(menu_additional);
			xmlAusgabe.append(projectname);
			xmlAusgabe.append(project);
			xmlAusgabe.append(project_end);
			xmlAusgabe.append(xml_all);
			xmlAusgabe.append(end);
			if (!(dataProcessing.generateUTFOutput(xmlAusgabe.toString(), "xml.html", true))){
				error =  error + "xml.html " + textbundle.getString("output_outputFiles_38") + ".\n\r";
			}

			//mosaic.xml
			StringBuffer xmldocumentString = new StringBuffer();
			xmldocumentString.append(xml_document_start1);
			xmldocumentString.append(project);
			xmldocumentString.append(xml_document_start2);
			mosaicMatrix = dataProcessing.getMosaic();
			percent = 0;
			referenceValue = (dataProcessing.getMosaicHeight()*dataProcessing.getMosaicWidth())/100;
			for(int row = 0; row < dataProcessing.getMosaicHeight(); row++){
				xmldocumentString.append(xml_document_row_start);
				for(int column = 0; column < dataProcessing.getMosaicWidth(); column++){
					if (mosaicMatrix[row][column].isEmpty()){
						xmldocumentString.append(xml_document_element_empty);
					}else{
						xmldocumentString.append(xml_document_element_start);
						xmldocumentString.append(mosaicMatrix[row][column].elementAt(0));
						xmldocumentString.append(xml_document_element_center);
						xmldocumentString.append(mosaicMatrix[row][column].elementAt(1));
						xmldocumentString.append(xml_document_element_end);
					}
					try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
						percent++;
						dataProcessing.refreshProgressBarOutputFiles(percent,5);
					}});}catch(Exception e){}
				}
				xmldocumentString.append(xml_document_row_end);
			}
			xmldocumentString.append(xml_document_end);
			if (!(dataProcessing.generateUTFOutput(xmldocumentString.toString(), "mosaic.xml", true))){
				error =  error + "mosaic.xml " + textbundle.getString("output_outputFiles_38") + ".\n\r";
			}
		}

		//additional.html
		StringBuffer additionalString = new StringBuffer();
		try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
			dataProcessing.refreshProgressBarOutputFiles(100,6);
		}});}catch(Exception e){}
		additionalString.append(head);
		additionalString.append(menu_start);
		if (grafic){additionalString.append(menu_grafic);};
		if (configuration){additionalString.append(menu_configuration);};
		if (material){additionalString.append(menu_material);};
		if (instruction){additionalString.append(menu_instruction);};
		if (xml){additionalString.append(menu_xml);};
		additionalString.append(menu_additional);
		additionalString.append(projectname);
		additionalString.append(project);
		additionalString.append(project_end);
		additionalString.append(additional_start);
		if (!infos.hasMoreElements()){
			additionalString.append(textbundle.getString("output_outputFiles_43"));
			additionalString.append(additional_cell_end);
		}else{
			while (infos.hasMoreElements()) {
				additionalString.append((String)infos.nextElement());
				additionalString.append(additional_cell_end);
			}
		}
		additionalString.append(end);
		if (!(dataProcessing.generateUTFOutput(additionalString.toString(), "additional.html", true))){
			error =  error + "additional.html " + textbundle.getString("output_outputFiles_38") + ".\n\r";
		}
		return error;
	}
}
