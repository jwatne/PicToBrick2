package PicToBrick;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory; 
import javax.swing.border.*;

/**
 * class:          MosaicSizeDialog
 * layer:          Gui (three tier architecture)
 * description:    Mosaic size
 * @author         Tobias Reichling
 */

public class MosaicSizeDialog
extends JDialog
implements ActionListener{
	
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private boolean cancel = true;
	private String error = "";
	JTextField mosaicWidthMM;
	JTextField mosaicHeightMM;
	JTextField mosaicWidthBE;
	JTextField mosaicHeightBE;
	private int widthBE;
	private int heightBE;
	private double widthMM;
	private double heightMM;
	private double configurationBasisWidthMM;
	private double configurationBasisHeightMM;
	
	/**
	 * method:          MosaicSizeDialog
	 * description:     constructor
	 * @author          Tobias Reichling
	 * @param           owner
	 * @param           configuration
	 */
	public MosaicSizeDialog(Frame owner, Configuration configuration){
		super(owner,textbundle.getString("dialog_mosaicSize_frame"),true);
		this.setLocation(100,100);
		this.setResizable(false);
		//CONFIGURATION INFOS
		this.configurationBasisWidthMM = configuration.getBasisWidthMM();
		this.configurationBasisHeightMM = configuration.getBasisWidthMM()
								/ configuration.getBasisWidth()
								* configuration.getBasisHeight();
		//PANEL
		JPanel content = new JPanel(new BorderLayout());
		JPanel metric = new JPanel(new GridLayout(2,3,7,7));
		JPanel update = new JPanel(new GridLayout(2,1,7,7));
		JPanel basis = new JPanel(new GridLayout(2,3,7,7));
		JPanel buttons = new JPanel();
		//BORDER
		TitledBorder metricBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_mosaicSize_border_1"));
		metric.setBorder(metricBorder);
		metricBorder.setTitleColor(new Color(100,100,100));
		TitledBorder basisBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_mosaicSize_border_2"));
		basis.setBorder(basisBorder);
		basisBorder.setTitleColor(new Color(100,100,100));
		TitledBorder updateBorder = BorderFactory.createTitledBorder(" ");
		update.setBorder(updateBorder);
		updateBorder.setBorder(BorderFactory.createEmptyBorder());
		updateBorder.setTitleColor(update.getBackground());
		//LABEL		
		JLabel widthMMText = new JLabel(textbundle.getString("dialog_mosaicSize_label_1") + ": ");
		JLabel heightMMText = new JLabel(textbundle.getString("dialog_mosaicSize_label_2") + ": ");
		JLabel widthBEText = new JLabel(textbundle.getString("dialog_mosaicSize_label_1") + ": ");
		JLabel heightBEText = new JLabel(textbundle.getString("dialog_mosaicSize_label_2") + ": ");
		JLabel widthMMTextUnit = new JLabel(" " + textbundle.getString("dialog_mosaicSize_label_3"));
		JLabel heightMMTextUnit = new JLabel(" " + textbundle.getString("dialog_mosaicSize_label_3"));
		JLabel widthBETextUnit = new JLabel(" " + textbundle.getString("dialog_mosaicSize_label_4"));
		JLabel heightBETextUnit = new JLabel(" " + textbundle.getString("dialog_mosaicSize_label_4"));
		//EINGABEFELDER		
		mosaicWidthMM = new JTextField();
		mosaicWidthMM.setHorizontalAlignment(JTextField.RIGHT);
		mosaicHeightMM = new JTextField();
		mosaicHeightMM.setHorizontalAlignment(JTextField.RIGHT);
		mosaicWidthBE = new JTextField();
		mosaicWidthBE.setHorizontalAlignment(JTextField.RIGHT);
		mosaicHeightBE = new JTextField();
		mosaicHeightBE.setHorizontalAlignment(JTextField.RIGHT);
		//BUTTON
		JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.addActionListener(this);
		ok.setActionCommand("ok");
		JButton cancelButton = new JButton(textbundle.getString("button_cancel"));
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		JButton updateMMtoBE = new JButton(textbundle.getString("dialog_mosaicSize_button_1"));
		updateMMtoBE.setActionCommand("mmtobe");
		updateMMtoBE.addActionListener(this);
		JButton updateBEtoMM = new JButton(textbundle.getString("dialog_mosaicSize_button_2"));
		updateBEtoMM.addActionListener(this);
		updateBEtoMM.setActionCommand("betomm");
		//BUILD
		metric.add(widthMMText);
		metric.add(mosaicWidthMM);
		metric.add(widthMMTextUnit);
		metric.add(heightMMText);
		metric.add(mosaicHeightMM);
		metric.add(heightMMTextUnit);
		update.add(updateMMtoBE);
		update.add(updateBEtoMM);
		basis.add(widthBEText);
		basis.add(mosaicWidthBE);
		basis.add(widthBETextUnit);
		basis.add(heightBEText);
		basis.add(mosaicHeightBE);
		basis.add(heightBETextUnit);
		buttons.add(ok);
		buttons.add(cancelButton);
		content.add(metric, BorderLayout.WEST);
		content.add(update, BorderLayout.CENTER);
		content.add(basis, BorderLayout.EAST);
		content.add(buttons, BorderLayout.SOUTH);
		//SHOW
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * method:          isCanceled
	 * description:     checks if dialog is canceled
	 * @author          Tobias Reichling
	 * @return          true or false
	 */
	public boolean isCanceled(){
		return cancel;
	}
	
	/**
	 * method:          getArea
	 * description:     returns the area od the mosaic in basis elements
	 * @author          Tobias Reichling
	 * @return          dimension
	 */
	public Dimension getArea(){
		return new Dimension(widthBE, heightBE);
	}
	
	/**
	 * method:          isValidInputBE
	 * description:     checks if the input (in basis elements) is valid
	 * @author          Tobias Reichling
	 * @return          true or false
	 */
	private boolean isValidInputBE(){
		error = new String("");
		//check width
		try{
			if (mosaicWidthBE.getText().length()==0){
				error = error.concat(textbundle.getString("dialog_mosaicSize_error_1") + "\n");
			}
			else
			{
				this.widthBE = (new Integer(mosaicWidthBE.getText()).intValue());
				if (widthBE <= 0)
					error = error.concat(textbundle.getString("dialog_mosaicSize_error_2") + "\n");
			}
		}
		catch(NumberFormatException e){
			error = error.concat(textbundle.getString("dialog_mosaicSize_error_3") + "\n");
		}
		//check height
		try{
			if (mosaicHeightBE.getText().length()==0){
				error = error.concat(textbundle.getString("dialog_mosaicSize_error_4") + "\n");
			}
			else
			{
				this.heightBE = (new Integer(mosaicHeightBE.getText()).intValue());
				if (heightBE <= 0)
					error = error.concat(textbundle.getString("dialog_mosaicSize_error_5") + "\n");
			}
		}
		catch(NumberFormatException e){
			error = error.concat(textbundle.getString("dialog_mosaicSize_error_6") + "\n");
		}
		if (!(error.equals(""))){
			return false;
		}
		return true;
	}
	
	/**
	 * method:          isValidInputMM
	 * description:     checks if the input (in millimeter) is valid
	 * @author          Tobias Reichling
	 * @return          true or false
	 */
	private boolean isValidInputMM(){
		error = new String("");
		//check width
		try{
			if (mosaicWidthMM.getText().length()==0){
				error = error.concat(textbundle.getString("dialog_mosaicSize_error_7") + "\n");
			}
			else
			{
				this.widthMM = (new Double(mosaicWidthMM.getText()).doubleValue());
				if (widthMM <= 0)
					error = error.concat(textbundle.getString("dialog_mosaicSize_error_8") + "\n");
			}
		}
		catch(NumberFormatException e){
			error = error.concat(textbundle.getString("dialog_mosaicSize_error_9") + "\n");
		}
		//check height
		try{
			if (mosaicHeightMM.getText().length()==0){
				error = error.concat(textbundle.getString("dialog_mosaicSize_error_10") + "\n");
			}
			else
			{
				this.heightMM = (new Double(mosaicHeightMM.getText()).doubleValue());
				if (heightMM <= 0)
					error = error.concat(textbundle.getString("dialog_mosaicSize_error_11") + "\n");
			}
		}
		catch(NumberFormatException e){
			error = error.concat(textbundle.getString("dialog_mosaicSize_error_12") + "\n");
		}
		if (!(error.equals(""))){
			return false;
		}
		return true;
	}
	
	/**
	 * method:          actionPerformed
	 * description:     ActionListener
	 * @author          Tobias Reichling
	 * @param           event
	 */
	public void actionPerformed(ActionEvent event){
		//Conversion from millimeter to basis element (with automatic rounding)
		//if the result ist 0 basis elements it is set to 1 basis element!
		if (event.getActionCommand().contains("mmtobe")){
			if (isValidInputMM()){
				if (((int)java.lang.Math.round(this.widthMM/this.configurationBasisWidthMM)) < 1){
					mosaicWidthBE.setText("1");
				}else{
					mosaicWidthBE.setText(""+((int)java.lang.Math.round(this.widthMM/this.configurationBasisWidthMM)));
				}
				if (((int)java.lang.Math.round(this.heightMM/this.configurationBasisHeightMM)) < 1){
					mosaicHeightBE.setText("1");
				}else{
					mosaicHeightBE.setText(""+((int)java.lang.Math.round(this.heightMM/this.configurationBasisHeightMM)));
				}
			}else{
				JOptionPane.showMessageDialog(this,error,textbundle.getString("dialog_error_frame"),JOptionPane.ERROR_MESSAGE); 
			}
		//Conversion from basis elements to millimeter (simple computation)
		//truncate after second decimal point
		}else if (event.getActionCommand().contains("betomm")){
			if (isValidInputBE()){
				mosaicWidthMM.setText(""+((int)((this.widthBE * this.configurationBasisWidthMM)*100))/100.0);
				mosaicHeightMM.setText(""+((int)((this.heightBE * this.configurationBasisHeightMM)*100))/100.0);		
			} else {
				JOptionPane.showMessageDialog(this,error,textbundle.getString("dialog_error_frame"),JOptionPane.ERROR_MESSAGE); 
			}
		}else if (event.getActionCommand().contains("cancel")){
			this.setVisible(false);
		}else if (event.getActionCommand().contains("ok")){
			if (isValidInputBE()){
				cancel = false;
				this.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this,error,textbundle.getString("dialog_error_frame"),JOptionPane.ERROR_MESSAGE); 
			}
		}
	}
}