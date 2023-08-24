package PicToBrick;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory; 
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * class:          SlicingColorNumberDialog
 * layer:          Gui (three tier architecture)
 * description:    dialog for choosing color quantity for slicing
 * @author         Tobias Reichling
 */
public class SlicingColorNumberDialog
extends JDialog
implements ActionListener
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private JComboBox quantity;
	
	/**
	 * method:          SlicingColorNumberDialog
	 * description:     constructor
	 * @author          Tobias Reichling
	 * @param           owner
	 * @param           start
	 * @param           end
	 */
	public SlicingColorNumberDialog(Frame besitzer, int start, int end)
	{
		super(besitzer,textbundle.getString("dialog_slicingColorNumber_frame"),true);
		this.setLocation(100,100);
		this.setResizable(false);
		JPanel content = new JPanel(new BorderLayout());
		JPanel dropdown = new JPanel(new GridLayout(1,2,10,10));
		JPanel buttons = new JPanel();
		//BUTTON
		JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		//DROPDOWN LISTS
		Vector colorQuantity = new Vector();
		for (int i = start; i <= end; i++){
			colorQuantity.add(i);
		}
		quantity = new JComboBox(colorQuantity);
		quantity.setEditable(false);
		quantity.setEnabled(true);
		//LABEL
		JLabel quantityText = new JLabel(textbundle.getString("dialog_slicingColorNumber_label") + ":");
		dropdown.add(quantityText);
		dropdown.add(quantity);
		buttons.add(ok);
		TitledBorder dropdownBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_slicingColorNumber_border"));
		dropdown.setBorder(dropdownBorder);
		dropdownBorder.setTitleColor(new Color(100,100,100));
		content.add(dropdown, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * method:          getQuantity
	 * description:     returns color quantity
	 * @author          Tobias Reichling
	 * @return          quantity
	 */
	public int getQuantity(){
		return (Integer)quantity.getSelectedItem();
	}
	
	/**
	 * method:          actionPerformed
	 * description:     ActionListener
	 * @author          Tobias Reichling
	 * @param			event
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().contains("ok")){
			this.setVisible(false);
		}
	}
}