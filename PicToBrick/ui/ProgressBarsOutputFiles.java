package PicToBrick.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.*;
import java.util.*;

/**
 * class:          ProgressBarsOutputFiles
 * layer:          Gui (three tier architecture)
 * description:    shows 6 progress bars
 * @author         Adrian Schuetz
 */
public class ProgressBarsOutputFiles
extends JDialog
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	JProgressBar graphic;
	JProgressBar configuration;
	JProgressBar material;
	JProgressBar instruction;
	JProgressBar xml;
	JProgressBar miscellaneous;

	/**
	 * method:          ProgressBarsOutputFiles
	 * description:     constructor
	 * @author          Adrian Schuetz
	 * @param           owner
	 */
	public ProgressBarsOutputFiles(Frame owner)
	{
		super(owner,textbundle.getString("dialog_progressBarsOutputFiles_frame"),false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setLocation(100,100);
		this.setResizable(false);
		JPanel content = new JPanel(new GridLayout(6,1));
		JPanel graphicPanel = new JPanel(new GridLayout(1,1));
		graphic = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		graphic.setStringPainted(true);
		graphic.setValue(0);
		graphic.setForeground(new Color(0,0,150));
		Dimension d = new Dimension(300,25);
		graphic.setMaximumSize(d);
		graphic.setPreferredSize(d);
		graphic.setMinimumSize(d);
		TitledBorder graphicBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsOutputFiles_border_1"));
		graphicPanel.setBorder(graphicBorder);
		graphicBorder.setTitleColor(new Color(100,100,100));
		graphicPanel.add(graphic);
		JPanel konfiPanel = new JPanel(new GridLayout(1,1));
		configuration = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		configuration.setStringPainted(true);
		configuration.setValue(0);
		configuration.setForeground(new Color(0,0,150));
		configuration.setMaximumSize(d);
		configuration.setPreferredSize(d);
		configuration.setMinimumSize(d);
		TitledBorder konfiBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsOutputFiles_border_2"));
		konfiPanel.setBorder(konfiBorder);
		konfiBorder.setTitleColor(new Color(100,100,100));
		konfiPanel.add(configuration);
		JPanel materialPanel = new JPanel(new GridLayout(1,1));
		material = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		material.setStringPainted(true);
		material.setValue(0);
		material.setForeground(new Color(0,0,150));
		material.setMaximumSize(d);
		material.setPreferredSize(d);
		material.setMinimumSize(d);
		TitledBorder materialBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsOutputFiles_border_3"));
		materialPanel.setBorder(materialBorder);
		materialBorder.setTitleColor(new Color(100,100,100));
		materialPanel.add(material);
		JPanel instructionPanel = new JPanel(new GridLayout(1,1));
		instruction = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		instruction.setStringPainted(true);
		instruction.setValue(0);
		instruction.setForeground(new Color(0,0,150));
		instruction.setMaximumSize(d);
		instruction.setPreferredSize(d);
		instruction.setMinimumSize(d);
		TitledBorder instructionBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsOutputFiles_border_4"));
		instructionPanel.setBorder(instructionBorder);
		instructionBorder.setTitleColor(new Color(100,100,100));
		instructionPanel.add(instruction);
		JPanel xmlPanel = new JPanel(new GridLayout(1,1));
		xml = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		xml.setStringPainted(true);
		xml.setValue(0);
		xml.setForeground(new Color(0,0,150));
		xml.setMaximumSize(d);
		xml.setPreferredSize(d);
		xml.setMinimumSize(d);
		TitledBorder xmlBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsOutputFiles_border_5"));
		xmlPanel.setBorder(xmlBorder);
		xmlBorder.setTitleColor(new Color(100,100,100));
		xmlPanel.add(xml);
		JPanel miscellaneousPanel = new JPanel(new GridLayout(1,1));
		miscellaneous = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		miscellaneous.setStringPainted(true);
		miscellaneous.setValue(0);
		miscellaneous.setForeground(new Color(0,0,150));
		miscellaneous.setMaximumSize(d);
		miscellaneous.setPreferredSize(d);
		miscellaneous.setMinimumSize(d);
		TitledBorder miscellaneousBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsOutputFiles_border_6"));
		miscellaneousPanel.setBorder(miscellaneousBorder);
		miscellaneousBorder.setTitleColor(new Color(100,100,100));
		miscellaneousPanel.add(miscellaneous);
		content.add(graphicPanel);
		content.add(konfiPanel);
		content.add(materialPanel);
		content.add(instructionPanel);
		content.add(xmlPanel);
		content.add(miscellaneousPanel);
		this.getContentPane().add(content);
		this.pack();
		this.setModal(false);
	}

	/**
	 * method:          setStatus
	 * description:     sets the progress bar status (enabled/disabled)
	 * @author          Adrian Schuetz
	 * @param           graphic, configuration, material, instruction, xml, miscellaneous (true/false)
	 */
	public void setStatus(boolean graphic, boolean configuration, boolean material, boolean instruction, boolean xml, boolean miscellaneous){
		if (!graphic){
			this.graphic.setString(textbundle.getString("dialog_progressBarsOutputFiles_progressBar_1"));
		}else{
			this.graphic.setString(null);
		}
		if (!configuration){
			this.configuration.setString(textbundle.getString("dialog_progressBarsOutputFiles_progressBar_1"));
		}else{
			this.configuration.setString(null);
		}
		if (!material){
			this.material.setString(textbundle.getString("dialog_progressBarsOutputFiles_progressBar_1"));
		}else{
			this.material.setString(null);
		}
		if (!instruction){
			this.instruction.setString(textbundle.getString("dialog_progressBarsOutputFiles_progressBar_1"));
		}else{
			this.instruction.setString(null);
		}
		if (!xml){
			this.xml.setString(textbundle.getString("dialog_progressBarsOutputFiles_progressBar_1"));
		}else{
			this.xml.setString(null);
		}
		if (!miscellaneous){
			this.miscellaneous.setString(textbundle.getString("dialog_progressBarsOutputFiles_progressBar_1"));
		}else{
			this.miscellaneous.setString(null);
		}
	}

	/**
	 * method:          showValue
	 * description:     changes progress bar value
	 * @author          Adrian Schuetz
	 * @param           value
	 * @param           number
	 */
	public void showValue(int value, int number){
		switch(number){
		case 1:{
			if (value==0){
				graphic.setString(textbundle.getString("dialog_progressBarsOutputFiles_progressBar_2"));
			}else if (value == 100){
				graphic.setString(null);
			}
			graphic.setValue(value);
			break;
		}
		case 2:{
			configuration.setValue(value);
			break;
		}
		case 3:{
			material.setValue(value);
			break;
		}
		case 4:{
			instruction.setValue(value);
			break;
		}
		case 5:{
			xml.setValue(value);
			break;
		}
		case 6:{
			miscellaneous.setValue(value);
			break;
		}
		default:{break;}}
	}

	/**
	 * method:          hideDialog
	 * description:     hides the dialog
	 * @author          Adrian Schuetz
	 */
	public void hideDialog(){
		this.setVisible(false);
	}

	/**
	 * method:          showDialog
	 * description:     shows the dialog
	 * @author          Adrian Schuetz
	 */
	public void showDialog(){
		this.setLocation(100,100);
		this.setVisible(true);
	}

	/**
	 * method:          animateGraphic
	 * description:     animates the graphic progress bar
	 *                  while saving the image file
	 * @author          Adrian Schuetz
	 * @param           active (true/false)
	 */
	public void animateGraphic(boolean active){
		this.graphic.setIndeterminate(active);
	}
}
