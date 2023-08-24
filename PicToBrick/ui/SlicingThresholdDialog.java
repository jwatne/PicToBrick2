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

import PicToBrick.model.ColorObject;

import java.util.*;

/**
 * class:          SlicingThresholdDialog
 * layer:          Gui (three tier architecture)
 * description:    dialog for choosing colors und thresholds
 * @author         Tobias Reichling
 */
public class SlicingThresholdDialog
extends JDialog
implements ActionListener, ChangeListener
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private JComboBox[] colors;
	private JSpinner[]  thresholdFrom;
	private JSpinner[]  thresholdTo;
	private int         colorCount;

	/**
	 * method:          SlicingThresholdDialog
	 * description:     constructor
	 * @author          Tobias Reichling
	 * @param           owner
	 * @param           colorCount
	 * @param           colorEnum
	 */
	public SlicingThresholdDialog(Frame owner, int colorCount, Enumeration colorEnum)
	{
		super(owner,textbundle.getString("dialog_slicingThreshold_frame"),true);
		this.setLocation(100,100);
		this.setResizable(false);
		this.colorCount = colorCount;
		JPanel content = new JPanel(new BorderLayout());
		JPanel selection = new JPanel(new GridLayout(colorCount,1,0,0));
		JPanel buttons = new JPanel();
		//BUTTON
		JButton ok = new JButton("ok");
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		//COLOR FOR DROPDOWNBOX
		Vector colorSelection = new Vector();
		while (colorEnum.hasMoreElements()) {
			ColorObject farbe = (ColorObject)(colorEnum.nextElement());
			colorSelection.add(farbe.getName());
		}
		//Array init
		colors = new JComboBox[colorCount];
		thresholdFrom = new JSpinner[colorCount];
		thresholdTo = new JSpinner[colorCount];
		//Gui Block for every color
		int suggestionValueTo = 0;
		int suggestionValueFrom = 0;
		for (int i = 0; i < colorCount; i++){
			JSpinner fromValue;
			JSpinner toValue;
			suggestionValueTo = (int)((100.0/colorCount)*(i+1));
			suggestionValueFrom = (int)((100.0/colorCount)*(i));
			//GUI Elements
			JComboBox colorBox = new JComboBox(colorSelection);
			colorBox.setEditable(false);
			colorBox.setEnabled(true);
			SpinnerNumberModel fromModel = new SpinnerNumberModel(suggestionValueFrom, i, 100-(colorCount-i), 1);
			fromValue = new JSpinner(fromModel);
			fromValue.setEnabled(true);
			fromValue.addChangeListener(this);
			if (i == 0){fromValue.setEnabled(false);}
			SpinnerNumberModel toModel = new SpinnerNumberModel(suggestionValueTo, i+1, 100-(colorCount-i-1), 1);
			toValue = new JSpinner(toModel);
			toValue.setEnabled(true);
			toValue.addChangeListener(this);
			if (i == (colorCount-1)){toValue.setEnabled(false);}
			JLabel from = new JLabel(textbundle.getString("dialog_slicingThreshold_label_1") + ":");
			JLabel to = new JLabel(textbundle.getString("dialog_slicingThreshold_label_2") + ":");
			JPanel selectionColor = new JPanel();
			JPanel selectionThreshold = new JPanel(new GridLayout(1,4,10,10));
			JPanel selectionElement = new JPanel(new BorderLayout());
			//Elements
			selectionColor.add(colorBox);
			selectionThreshold.add(from);
			selectionThreshold.add(fromValue);
			selectionThreshold.add(to);
			selectionThreshold.add(toValue);
			JPanel selectionThresholdum = new JPanel();
			selectionThresholdum.add(selectionThreshold);
			selectionElement.add(selectionColor, BorderLayout.WEST);
			selectionElement.add(selectionThresholdum, BorderLayout.EAST);
			//Border
			TitledBorder Border = BorderFactory.createTitledBorder(textbundle.getString("dialog_slicingThreshold_border") + " "+(i+1)+":");
			selectionElement.setBorder(Border);
			Border.setTitleColor(new Color(100,100,100));
			colors[i] = colorBox;
			thresholdFrom[i] = fromValue;
			thresholdTo[i] = toValue;
			selection.add(selectionElement);
		}
		//Build dialog
		buttons.add(ok);
		content.add(selection, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * method:          getSelection
	 * description:     returns a vector (color/threshold)
	 * @author          Tobias Reichling
	 * @return          vector
	 */
	public Vector getSelection(){
		Vector result = new Vector();
		for (int i = 0; i < colorCount; i++){
			result.add(colors[i].getSelectedItem());
			result.add(thresholdFrom[i].getValue());
		}
		return result;
	}

	/**
	 * method:          actionPerformed
	 * description:     ActionListener
	 * @author          Tobias Reichling
	 * @param		   event
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().contains("ok")){
			this.setVisible(false);
		}
	}

	/**
	 * method:         stateChanged
	 * description:    ChangeListener for JSpinner
	 * @author         Tobias Reichling
	 * @param          event
	 */
	public void stateChanged(ChangeEvent event)
	{
		int row = -1;
		boolean from = false;
		boolean increment = false;
		//bestimmen welcher JSpinner das Event gesendet hat
		for (int i = 0; i < colorCount; i++){
			if ((JSpinner)event.getSource()==thresholdFrom[i]){
				//FROM-JSpinner
				row = i;
				from = true;
				if (((Integer)(thresholdFrom[i].getValue()) < (Integer)(thresholdTo[i-1].getValue()))){
					increment = false;
				}else{
					increment = true;
				}
				if (!(thresholdTo[i-1].getValue() == thresholdFrom[i].getValue())){
					//change associated JSpinner
					thresholdTo[i-1].setValue(thresholdFrom[i].getValue());
					//check that from an to values are not the same!
					if (increment){
						if (!from){row = row+1;}
						if (thresholdFrom[row].getValue().equals(thresholdTo[row].getValue())){
							thresholdTo[row].setValue(((Integer)thresholdTo[row].getValue())+1);
						}
					}else{
						if (from){row = row-1;}
						if (thresholdFrom[row].getValue().equals(thresholdTo[row].getValue())){
							thresholdFrom[row].setValue(((Integer)thresholdFrom[row].getValue())-1);
						}
					}
				}
			}
			if ((JSpinner)event.getSource()==thresholdTo[i]){
				//TO-JSpinner
				row = i;
				from = false;
				if (((Integer)(thresholdFrom[i+1].getValue()) < (Integer)(thresholdTo[i].getValue()))){
					increment = true;
				}else{
					increment = false;
				}
				//change associated JSpinner
				if (!(thresholdFrom[i+1].getValue() == thresholdTo[i].getValue())){
					//change associated JSpinner
					thresholdFrom[i+1].setValue(thresholdTo[i].getValue());
					//check that from an to values are not the same!
					if (increment){
						if (!from){row = row+1;}
						if (thresholdFrom[row].getValue().equals(thresholdTo[row].getValue())){
							thresholdTo[row].setValue(((Integer)thresholdTo[row].getValue())+1);
						}
					}else{
						if (from){row = row-1;}
						if (thresholdFrom[row].getValue().equals(thresholdTo[row].getValue())){
							thresholdFrom[row].setValue(((Integer)thresholdFrom[row].getValue())-1);
						}
					}
				}
			}
		}
	}
}
