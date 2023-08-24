
package PicToBrick.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * class:          MoldingOptimisationDialog
 * layer:          Gui (three tier architecture)
 * description:    dialog for choosing parameter for molding optimisation
 * @author         Tobias Reichling
 */
public class MoldingOptimisationDialog
extends JDialog
implements ActionListener
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private ButtonGroup optimisation = new ButtonGroup();

	/**
	 * method:          MoldingOptimisationDialog
	 * description:     constructor
	 * @author          Tobias Reichling
	 * @param           owner
	 * @param           quantisation
	 */
	public MoldingOptimisationDialog(Frame owner, int quantisationNumber, String quantisation)
	{
		super(owner,textbundle.getString("dialog_moldingOptimisation_frame"),true);
		this.setLocation(100,100);
		this.setResizable(false);
		JPanel radio;
		JPanel content = new JPanel(new BorderLayout());
		//if quantisation = error diffusino (2color) the radio panel
		//must be greater because it must show more information
		if (quantisationNumber == 2){
			radio = new JPanel(new GridLayout(12,1,0,0));
		}else{
			radio = new JPanel(new GridLayout(6,1,0,0));
		}
		JPanel buttons = new JPanel();
		//Label
		JLabel text1, text2, text3, text4, text5, text6, text7, text8, text9, text10;
		text1 = new JLabel(textbundle.getString("dialog_moldingOptimisation_label_1") + " " + quantisation + ".");
		text2 = new JLabel(textbundle.getString("dialog_moldingOptimisation_label_2"));
		text3 = new JLabel(textbundle.getString("dialog_moldingOptimisation_label_3"));
		text4 = new JLabel("");
		//BUTTON
		JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		//Radio Buttons
		JRadioButton a = new JRadioButton();
		a.setText(textbundle.getString("dialog_moldingOptimisation_radio_1"));
		a.setActionCommand("no");
		a.setSelected(true);
		JRadioButton b = new JRadioButton();
		b.setText(textbundle.getString("dialog_moldingOptimisation_radio_2"));
		b.setActionCommand("yes");
		optimisation.add(a);
		optimisation.add(b);
		radio.add(text1);
		radio.add(text2);
		radio.add(text3);
		radio.add(text4);
		radio.add(a);
		radio.add(b);
		//if quantisation = error diffusion (2color) the radio panel
		//must be greater because it must show more information
		if (quantisationNumber == 2){
			text5 = new JLabel("");
			text6 = new JLabel(textbundle.getString("dialog_moldingOptimisation_label_6a") + " " + quantisation + " " + textbundle.getString("dialog_moldingOptimisation_label_6b"));
			text7 = new JLabel(textbundle.getString("dialog_moldingOptimisation_label_7"));
			text8 = new JLabel(textbundle.getString("dialog_moldingOptimisation_label_8"));
			text9 = new JLabel(textbundle.getString("dialog_moldingOptimisation_label_9"));
			text10 = new JLabel(textbundle.getString("dialog_moldingOptimisation_label_10"));
			radio.add(text5);
			radio.add(text6);
			radio.add(text7);
			radio.add(text8);
			radio.add(text9);
			radio.add(text10);
		}
		buttons.add(ok);
		TitledBorder optimierungBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_moldingOptimisation_border"));
		radio.setBorder(optimierungBorder);
		optimierungBorder.setTitleColor(new Color(100,100,100));
		content.add(radio, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * method:          getMethod
	 * description:     returns the method
	 * @author          Tobias Reichling
	 * @return          action command (vector)
	 */
	public Vector getMethod(){
		Vector vector = new Vector();
		vector.add(optimisation.getSelection().getActionCommand());
		return vector;
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
