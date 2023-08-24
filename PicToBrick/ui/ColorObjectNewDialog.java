package PicToBrick.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;

/**
 * class:          ColorObjectNewDialog
 * layer:          gui (three tier architecture)
 * description:    dialog with color mixer
 * @author         Adrian Schuetz
 */
public class ColorObjectNewDialog
extends JDialog
implements ActionListener, ChangeListener
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private SpinnerNumberModel valueRed;
	private SpinnerNumberModel valueGreen;
	private SpinnerNumberModel valueBlue;
	private JPanel colorPreview;
	private Color selectedColor;
	private JTextField colorName;
	private String error = "";
	private boolean cancel = true;

	/**
	 * method:          ColorObjectNewDialog
	 * description:     constructor
	 * @author          Adrian Schuetz
	 * @param           owner
	 */
	public ColorObjectNewDialog(JDialog owner){
		super(owner,textbundle.getString("dialog_colorObjectNew_frame"),true);
		this.setLocation(600,100);
		this.setResizable(false);
		JPanel input = new JPanel();
		TitledBorder inputBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_colorObjectNew_border"));
		input.setBorder(inputBorder);
		inputBorder.setTitleColor(new Color(100,100,100));
		JPanel content = new JPanel(new BorderLayout());
		JPanel left = new JPanel(new GridLayout(4,2,5,5));
		JPanel right = new JPanel();
		colorPreview = new JPanel();
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		colorPreview.setBorder(border);
		right.add(colorPreview);
		JPanel bottom = new JPanel();
		JLabel name = new JLabel(textbundle.getString("dialog_colorObjectNew_label_name") + ":");
		colorName = new JTextField();
		JLabel red = new JLabel(textbundle.getString("dialog_colorObjectNew_label_red") + ":  ");
		JLabel green = new JLabel(textbundle.getString("dialog_colorObjectNew_label_green") + ":  ");
		JLabel blue = new JLabel(textbundle.getString("dialog_colorObjectNew_label_blue") + ":  ");
		// SpinnerNumberModel mit Wert, Minimum, Maximum und Schrittweite
		valueRed = new SpinnerNumberModel(0, 0, 255, 1);
		valueGreen = new SpinnerNumberModel(0, 0, 255, 1);
		valueBlue = new SpinnerNumberModel(0, 0, 255, 1);
		JSpinner spinnerRed = new JSpinner(valueRed);
		JSpinner spinnerGreen = new JSpinner(valueGreen);
		JSpinner spinnerBlue = new JSpinner(valueBlue);
		spinnerRed.addChangeListener(this);
		spinnerGreen.addChangeListener(this);
		spinnerBlue.addChangeListener(this);
		JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		JButton cancelButton = new JButton(textbundle.getString("button_cancel"));
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		left.add(name);
		left.add(colorName);
		left.add(red);
		left.add(spinnerRed);
		left.add(green);
		left.add(spinnerGreen);
		left.add(blue);
		left.add(spinnerBlue);
		//initialize color preview
		selectedColor = new Color(0,0,0);
		colorPreview.setBackground(selectedColor);
		bottom.add(ok);
		bottom.add(cancelButton);
		input.add(left);
		input.add(right);
		colorPreview.setMinimumSize(new Dimension(70, left.getMinimumSize().height));
		colorPreview.setPreferredSize(new Dimension(70, left.getMinimumSize().height));
		content.add(input, BorderLayout.CENTER);
		content.add(bottom, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * method:          isCanceled
	 * description:     returns if the dialog is canceled or not
	 * @author          Adrian Schuetz
	 * @return          true or false
	 */
	public boolean isCanceled(){
		return this.cancel;
	}

	/**
	 * method:          getColorObject
	 * description:     returns the mixed color
	 * @author          Adrian Schuetz
	 * @return          color object
	 */
	public Color getColorObject(){
		return this.selectedColor;
	}

	/**
	 * method:          getColorName
	 * description:     Returns the name of the color
	 * @author          Adrian Schuetz
	 * @return          color name
	 */
	public String getColorName(){
		return this.colorName.getText();
	}

	/**
	 * method:          validInput
	 * description:     returns if the input is valid or not
	 * @author          Adrian Schuetz
	 * @return          true or false
	 */
	private boolean validInput(){
		error = new String("");
		if (colorName.getText().length()==0){
			error = error.concat(textbundle.getString("dialog_colorObjectNew_error") + "\n");
		}
		if (!(error.equals(""))){
			return false;
		}
		return true;
	}

	/**
	 * method:          setVisible
	 * description:     shows the dialog
	 * @author          Adrian Schuetz
	 */
	public void setVisible(){
		this.setVisible(true);
	}

	/**
	 * method:          actionPerformed
	 * description:     ActionListener
	 * @author          Adrian Schuetz
	 * @param           event
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().contains("cancel")){
			this.cancel = true;
			this.setVisible(false);
		}
		if (event.getActionCommand().contains("ok")){
			if (validInput()){
				this.cancel = false;
				this.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this,error,textbundle.getString("dialog_error_frame"),JOptionPane.ERROR_MESSAGE);
				error = "";
			}
		}
	}

	/**
	 * method:         stateChanged
	 * description:    ChangeListener
	 *                 controls the color preview
	 * @author         Adrian Schuetz
	 * @param          event
	 */
	public void stateChanged(ChangeEvent event)
	{
		int red = ((Integer)valueRed.getValue()).intValue();
		int green = ((Integer)valueGreen.getValue()).intValue();
		int blue = ((Integer)valueBlue.getValue()).intValue();
		this.selectedColor = new Color(red, green, blue);
		colorPreview.setBackground(this.selectedColor);
	}
}
