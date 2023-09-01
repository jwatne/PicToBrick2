package pictobrick.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import pictobrick.model.ColorObject;

import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * class: FloydSteinbergColorDialog
 * layer: Gui (three tier architecture)
 * description: dialog for choosing 2 colors
 *
 * @author Adrian Schuetz
 */
public class FloydSteinbergColorDialog
		extends JDialog
		implements ActionListener {
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private final JComboBox<String> dark;
	private final JComboBox<String> light;
	private final JComboBox<String> method;

	/**
	 * method: FloydSteinbergColorDialog
	 * description: constructor
	 *
	 * @author Adrian Schuetz
	 * @param owner
	 * @param colors
	 */
	public FloydSteinbergColorDialog(final Frame owner, final Enumeration<ColorObject> colors) {
		super(owner, textbundle.getString("dialog_floydSteinbergColor_frame"), true);
		this.setLocation(100, 100);
		this.setResizable(false);
		final JPanel content = new JPanel(new BorderLayout());
		final JPanel methods = new JPanel(new GridLayout(1, 1));
		final JPanel dropdown = new JPanel(new GridLayout(1, 2, 10, 10));
		final JPanel dropRight = new JPanel(new GridLayout(2, 1));
		final JPanel dropLeft = new JPanel(new GridLayout(2, 1));
		final JPanel buttons = new JPanel();
		// BUTTON
		final JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		// DROPDOWN LISTS
		final Vector<String> colorChooser = new Vector<>();

		while (colors.hasMoreElements()) {
			final ColorObject color = colors.nextElement();
			colorChooser.add(color.getName());
		}

		dark = new JComboBox<>(colorChooser);
		dark.setEditable(false);
		dark.setEnabled(true);
		light = new JComboBox<>(colorChooser);
		light.setEditable(false);
		light.setEnabled(true);
		// LABELS
		final JLabel darkText = new JLabel(textbundle.getString("dialog_floydSteinbergColor_label_1") + ":       ");
		final JLabel lightText = new JLabel(textbundle.getString("dialog_floydSteinbergColor_label_2") + ":");
		final Vector<String> methodList = new Vector<>();
		methodList.add(textbundle.getString("dialog_floydSteinbergColor_combo_1"));
		methodList.add(textbundle.getString("dialog_floydSteinbergColor_combo_2"));
		methodList.add(textbundle.getString("dialog_floydSteinbergColor_combo_3"));
		method = new JComboBox<>(methodList);
		method.setEditable(false);
		method.setEnabled(true);
		methods.add(method);
		dropRight.add(darkText);
		dropRight.add(dark);
		dropLeft.add(lightText);
		dropLeft.add(light);
		dropdown.add(dropRight);
		dropdown.add(dropLeft);
		final TitledBorder dropdownBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_floydSteinbergColor_border_1"));
		dropdown.setBorder(dropdownBorder);
		dropdownBorder.setTitleColor(new Color(100, 100, 100));
		final TitledBorder methodsBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_floydSteinbergColor_border_2"));
		methods.setBorder(methodsBorder);
		methodsBorder.setTitleColor(new Color(100, 100, 100));
		buttons.add(ok);
		content.add(dropdown, BorderLayout.NORTH);
		content.add(methods, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * method: getDark
	 * description: returns the color for dark areas
	 *
	 * @author Adrian Schuetz
	 * @return dark color
	 */
	public String getDark() {
		return (String) dark.getSelectedItem();
	}

	/**
	 * method: getLight
	 * description: return the color for light areas
	 *
	 * @author Adrian Schuetz
	 * @return light color
	 */
	public String getLight() {
		return (String) light.getSelectedItem();
	}

	/**
	 * method: getMethod
	 * description: returns choosen method
	 *
	 * @author Adrian Schuetz
	 * @return method (integer)
	 */
	public int getMethod() {
		return method.getSelectedIndex() + 1;
	}

	/**
	 * method: actionPerformed
	 * description: ActionListener
	 *
	 * @author Adrian Schuetz
	 * @param event
	 */
	public void actionPerformed(final ActionEvent event) {
		if (event.getActionCommand().contains("ok")) {
			this.setVisible(false);
		}
	}
}
