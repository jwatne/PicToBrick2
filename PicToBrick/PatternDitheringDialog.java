package PicToBrick;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory; 
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * class:          PatternDitheringDialog
 * layer:          Gui (three tier architecture)
 * description:    dialog for luminance value distance
 * @author         Adrian Schuetz
 */
public class PatternDitheringDialog
extends JDialog
implements ActionListener
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private JSlider distance;

	/**
	 * method:          PatternDitheringDialog
	 * description:     constructor
	 * @author          Adrian Schuetz
	 * @param           owner
	 */
	public PatternDitheringDialog(Frame owner)
	{
		super(owner,textbundle.getString("dialog_patternDithering_frame"),true);
		this.setLocation(100,100);
		this.setResizable(false);
		JPanel content = new JPanel(new BorderLayout());
		JPanel textSlider = new JPanel(new BorderLayout());
		JPanel text = new JPanel(new GridLayout(2,1,5,5));
		JPanel slider = new JPanel(new GridLayout(1,1));
		JPanel buttons = new JPanel();
		//BUTTON
		JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		//LABEL
		JLabel text1 = new JLabel(textbundle.getString("dialog_patternDithering_label_1"));
		JLabel text2 = new JLabel(textbundle.getString("dialog_patternDithering_label_2"));
		//Slider
		distance = new JSlider(0,100,75);
		distance.setMinorTickSpacing(1);
		distance.setMajorTickSpacing(10);
		distance.setPaintLabels(true);
		distance.setPaintTicks(true);
		distance.setSnapToTicks(true);
		distance.setFocusable(false);
		slider.add(distance);
		text.add(text1);
		text.add(text2);
		textSlider.add(text, BorderLayout.NORTH);
		textSlider.add(slider, BorderLayout.SOUTH);
		TitledBorder textSliderBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_patternDithering_border"));
		textSlider.setBorder(textSliderBorder);
		textSliderBorder.setTitleColor(new Color(100,100,100));
		buttons.add(ok);
		content.add(textSlider, BorderLayout.NORTH);
		content.add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * method:          getDistance
	 * description:     returns the maximum distance of the luminance values
	 * @author          Adrian Schuetz
	 * @return          distance
	 */
	public int getDistance(){
		return distance.getValue();
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
	}
}