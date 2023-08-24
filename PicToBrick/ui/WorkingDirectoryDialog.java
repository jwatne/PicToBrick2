package PicToBrick.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


/**
 * class:          WorkingDirectoryDialog
 * layer:          Gui (three tier architecture)
 * description:    dialog for choosing the working directory
 * @author         Adrian Schuetz
 */
public class WorkingDirectoryDialog
extends JDialog
implements ActionListener
{
	private int buttonNumber = 0;
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");

	/**
	 * method:          WorkingDirectoryDialog
	 * description:     constructor
	 * @author          Adrian Schuetz
	 * @param           owner
	 * @param           working directory (old)
	 */
	public WorkingDirectoryDialog(Frame owner, File workingDirectoryOld)
	{
		super(owner, textbundle.getString("dialog_workingDirectory_frame") ,true);
		this.setLocation(100,100);
		this.setResizable(false);
		JPanel content = new JPanel(new BorderLayout());
		JPanel text = new JPanel();
		JPanel buttons = new JPanel();
		JLabel label = new JLabel();
		if (workingDirectoryOld == null){
			label.setText(textbundle.getString("dialog_workingDirectory_label"));
		}else {
			label.setText(workingDirectoryOld.getPath());
		}
		JButton newDirectory = new JButton(textbundle.getString("dialog_workingDirectory_button"));
		newDirectory.setActionCommand("ok");
		newDirectory.addActionListener(this);
		JButton cancel = new JButton(textbundle.getString("button_cancel"));
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		buttons.add(newDirectory);
		buttons.add(cancel);
		text.add(label);
		TitledBorder textBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_workingDirectory_border"));
		text.setBorder(textBorder);
		textBorder.setTitleColor(new Color(100,100,100));
		content.add(text, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}


	/**
	 * method:          getButton
	 * description:     returns the button number: 1 for choosing a new directory
	 * @author          Adrian Schuetz
	 * @return          1 for choosing a new directory, 0 else
	 */
	public int getButton(){
		return this.buttonNumber;
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
			buttonNumber = 1;
			this.setVisible(false);
		}
		if (event.getActionCommand().contains("cancel")){
			this.setVisible(false);
		}
	}
}
