
package PicToBrick;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.BorderFactory; 
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * class:          StabilityOptimisationDialog
 * layer:          Gui (three tier architecture)
 * description:    dialog for choosing optimisation method
 * @author         Adrian Schuetz
 */
public class StabilityOptimisationDialog
extends JDialog
implements ActionListener, ChangeListener
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private ButtonGroup optimisation = new ButtonGroup();
	private SpinnerNumberModel valueGapHeight;
	private int maxGabHeight = 10;
	private JSpinner spinnerGab;
	
	/**
	 * method:          StabilityOptimisationDialog
	 * description:     constructor
	 * @author          Adrian Schuetz
	 * @param           owner
	 */
	public StabilityOptimisationDialog(Frame owner)
	{
		super(owner,textbundle.getString("dialog_stabilityOptimisation_frame"),true);
		this.setLocation(100,100);
		this.setResizable(false);
		JPanel radio = new JPanel(new GridLayout(6,1,0,0));
		JPanel gab = new JPanel(new BorderLayout());
		JPanel selection = new JPanel(new BorderLayout());
		JPanel content = new JPanel(new BorderLayout());
		JPanel buttons = new JPanel();
		//Label
		JLabel text1, text2, text3, text4, text5;
		text1 = new JLabel(textbundle.getString("dialog_stabilityOptimisation_label_1"));
		text2 = new JLabel(textbundle.getString("dialog_stabilityOptimisation_label_2"));
		text3 = new JLabel(textbundle.getString("dialog_stabilityOptimisation_label_3"));
		text4 = new JLabel(textbundle.getString("dialog_stabilityOptimisation_label_4"));
		text5 = new JLabel(textbundle.getString("dialog_stabilityOptimisation_label_5"));
		valueGapHeight = new SpinnerNumberModel(10, 6, 30, 1);
		spinnerGab = new JSpinner(valueGapHeight);
		spinnerGab.addChangeListener(this);
		spinnerGab.setEnabled(false);
		//BUTTON
		JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		//Radio Buttons
		JRadioButton a = new JRadioButton();
		a.setText(textbundle.getString("dialog_stabilityOptimisation_radio_1"));
		a.setActionCommand("normal");
		a.setSelected(true);
		a.addActionListener(this);
		JRadioButton b = new JRadioButton();
		b.setText(textbundle.getString("dialog_stabilityOptimisation_radio_2"));
		b.setActionCommand("border");
		b.addActionListener(this);
		JRadioButton c = new JRadioButton();
		c.setText(textbundle.getString("dialog_stabilityOptimisation_radio_3"));
		c.setActionCommand("complete");
		c.addActionListener(this);
		optimisation.add(a);
		optimisation.add(b);
		optimisation.add(c);
		radio.add(text1);
		radio.add(text2);
		radio.add(text3);
		radio.add(a);
		radio.add(b);
		radio.add(c);
		gab.add(text4, BorderLayout.WEST);
		gab.add(spinnerGab, BorderLayout.CENTER);
		gab.add(text5, BorderLayout.EAST);
		buttons.add(ok);
		selection.add(radio, BorderLayout.CENTER);
		selection.add(gab, BorderLayout.SOUTH);
		TitledBorder optimierungBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_stabilityOptimisation_border"));
		selection.setBorder(optimierungBorder);
		optimierungBorder.setTitleColor(new Color(100,100,100));
		content.add(selection, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * method:          getMethod
	 * description:     return optimisation method
	 * @author          Adrian Schuetz
	 * @return          optimisation method (vector)
	 */
	public Vector getMethod(){
		Vector vector = new Vector();
		//otimisation yes/no
		//only border or complete
		//max. gap height
		if(optimisation.getSelection().getActionCommand() == "normal"){
			vector.add(false);
			vector.add(true);
			vector.add(maxGabHeight);
		}else if(optimisation.getSelection().getActionCommand() == "border"){
			vector.add(true);
			vector.add(true);
			vector.add(maxGabHeight);
		}else{//"complete"
			vector.add(true);
			vector.add(false);
			vector.add(maxGabHeight);
		}
		return vector;
	}
	
	/**
	 * method:          actionPerformed
	 * description:     ActionListener
	 * @author          Adrian Schuetz
	 * @param			event
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().contains("ok")){
			this.setVisible(false);
		}
		if (event.getActionCommand().contains("complete")){
			spinnerGab.setEnabled(true);
		}else{
			spinnerGab.setEnabled(false);
		}
	}
	
	/**
	 * method:         stateChanged
	 * description:    ChangeListener
	 * @author         Adrian Schuetz
	 * @param          event
	 */
	public void stateChanged(ChangeEvent event)
	{
		this.maxGabHeight = ((Integer)valueGapHeight.getValue()).intValue();
	}
}