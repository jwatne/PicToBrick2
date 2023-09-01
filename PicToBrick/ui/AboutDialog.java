package pictobrick.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.border.*;
import java.util.*;

/**
 * class:          AboutDialog
 * layer:          gui (three tier architecture)
 * description:    dialog with information about pictobrick
 * @author         Adrian Schuetz
 */
public class AboutDialog
extends JDialog
implements ActionListener
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");

	/**
	 * method:          AboutDialog
	 * description:     constructor
	 * @author          Adrian Schuetz
	 * @param           owner - window which owns this dialog
	 */
	public AboutDialog(Frame owner)
	{
		super(owner,textbundle.getString("dialog_about_frame"),true);
		this.setLocation(100,100);
		this.setResizable(false);
		JPanel content = new JPanel();
		JPanel information = new JPanel(new GridLayout(4,1,10,10));
		JPanel information2 = new JPanel(new GridLayout(4,1,3,3));
		JLabel name = new JLabel("pictobrick 2.0 - 2023-09-01");
		name.setFont(new Font(name.getFont().getFontName(), Font.BOLD, name.getFont().getSize()));
		JLabel homepage = new JLabel("http://www.pictobrick.de (" + textbundle.getString("dialog_about_label_5") + ")");
		JLabel author1 = new JLabel("Tobias Reichling - pictobrick@t-reichling.de");
		JLabel author2 = new JLabel("Adrian Schütz - pictobrick@basezero.net");
		JLabel author3 = new JLabel("John Watne (v2.0) - john.watne@gmail.com");
		JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.setActionCommand("ok");
		JPanel okPanel = new JPanel();
		okPanel.add(ok);
		ok.addActionListener(this);
		TitledBorder informationBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_about_border_1"));
		information.setBorder(informationBorder);
		informationBorder.setTitleColor(new Color(100,100,100));
		content.setLayout(new BorderLayout());
		information.add(name);
		information.add(homepage);
		information.add(author1);
		information.add(author2);
		information.add(author3);
		JLabel lego1 = new JLabel(textbundle.getString("dialog_about_label_1"));
		JLabel lego2 = new JLabel(textbundle.getString("dialog_about_label_2"));
		JLabel ministeck1 = new JLabel(textbundle.getString("dialog_about_label_3"));
		JLabel ministeck2 = new JLabel(textbundle.getString("dialog_about_label_4"));
		information2.add(lego1);
		information2.add(lego2);
		information2.add(ministeck1);
		information2.add(ministeck2);
		TitledBorder information2Border = BorderFactory.createTitledBorder(textbundle.getString("dialog_about_border_2"));
		information2.setBorder(information2Border);
		information2Border.setTitleColor(new Color(100,100,100));
		content.add(information, BorderLayout.NORTH);
		content.add(information2, BorderLayout.CENTER);
		content.add(okPanel, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
	}

	/**
	 * method:          actionPerformed
	 * description:     ActionListener
	 * @author          Adrian Schuetz
	 * @param	       event
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().contains("ok")){
			this.setVisible(false);
		}
	}
}
