package PicToBrick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.BorderFactory; 
import javax.swing.border.*;

/**
 * class:          ConfigurationLoadingDialog
 * layer:          Gui (three tier architecture)
 * description:    loading a configuration
 * @author         Tobias Reichling
 */
public class ConfigurationLoadingDialog
extends JDialog
implements ActionListener
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	JComboBox list;
	ButtonGroup group;
	boolean cancel = true;
	
	/**
	 * method:          ConfigurationLoadingDialog
	 * description:     constructor
	 * @author          Tobias Reichling
	 * @param           owner
	 * @param           selection
	 */
	public ConfigurationLoadingDialog(Frame owner, Vector selection)
	{
		super(owner,textbundle.getString("dialog_configurationLoading_frame"),true);
		this.setLocation(100,100);
		this.setResizable(false);
		JPanel content = new JPanel(new BorderLayout());
		JPanel radios = new JPanel(new GridLayout(4,1));
		JPanel buttons = new JPanel();
		group = new ButtonGroup();
		JRadioButton newConfiguration = new JRadioButton(textbundle.getString("dialog_configurationLoading_radio_1"));
		newConfiguration.setActionCommand("new");
		newConfiguration.addActionListener(this);
		JRadioButton derivateConfiguration = new JRadioButton(textbundle.getString("dialog_configurationLoading_radio_2"));
		derivateConfiguration.setActionCommand("derivate");
		derivateConfiguration.addActionListener(this);
		JRadioButton loadConfiguration = new JRadioButton(textbundle.getString("dialog_configurationLoading_radio_3"));
		loadConfiguration.setActionCommand("load");
		loadConfiguration.addActionListener(this);
		newConfiguration.setSelected(true);
		group.add(newConfiguration);
		group.add(derivateConfiguration);
		group.add(loadConfiguration);
		radios.add(newConfiguration);
		radios.add(derivateConfiguration);
		radios.add(loadConfiguration);
		JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.addActionListener(this);
		ok.setActionCommand("ok");
		JButton cancelButton = new JButton(textbundle.getString("button_cancel"));
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		list = new JComboBox(selection);
		list.setEditable(false);
		list.setEnabled(false);
		radios.add(list);
		buttons.add(ok);
		buttons.add(cancelButton);
		content.add(radios, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.SOUTH);
		TitledBorder radioBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_configurationLoading_border"));
		radios.setBorder(radioBorder);
		radioBorder.setTitleColor(new Color(100,100,100));
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * method:          getSelection
	 * description:     returns the selected item (radio button) as integer
	 * @author          Tobias Reichling
	 * @return          number of the selected radio button
	 */
	public int getSelection(){
		if (group.getSelection().getActionCommand().contains("new")){
			return 1;
		}else if (group.getSelection().getActionCommand().contains("derivate")){
			return 2;
		}else if (group.getSelection().getActionCommand().contains("load")){
			return 3;
		}
		return 0;
	}
	
	/**
	 * method:          getFile
	 * description:     returns the selected item (combo box) as integer
	 * @author          Tobias Reichling
	 * @return          number of the selected combo box item
	 */
	public int getFile(){
		return list.getSelectedIndex();
	}
	
	/**
	 * method:          isCanceled
	 * description:     returns if the dialog is canceled by user or not
	 * @author          Tobias Reichling
	 * @return          true or false
	 */
	public boolean isCanceled(){
		return cancel;
	}
	
	/**
	 * method:          actionPerformed
	 * description:     ActionListener
	 * @author          Tobias Reichling
	 * @param			event
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().contains("new")){
			list.setEnabled(false);
		}
		if (event.getActionCommand().contains("derivate")){
			list.setEnabled(true);
		}
		if (event.getActionCommand().contains("load")){
			list.setEnabled(true);
		}
		if (event.getActionCommand().contains("cancel")){
			this.setVisible(false);
		}
		if (event.getActionCommand().contains("ok")){
			cancel = false;
			this.setVisible(false);
		}
	}
}