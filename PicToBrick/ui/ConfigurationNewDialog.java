package pictobrick.ui;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * class: ConfigurationNewDialog
 * layer: Gui (three tier architecture)
 * description: new configuration dialog
 *
 * @author Adrian Schuetz
 */
public class ConfigurationNewDialog
		extends JDialog
		implements ActionListener, ItemListener {
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private boolean cancel = true;
	private final JTextField nameInput;
	private final JTextField basisNameInput;
	private final JTextField basisWidthInput;
	private final JTextField basisHeightInput;
	private final JTextField basisWidthMMInput;
	private final JTextField stabilityInput;
	private final JTextField costsInput;
	private final JComboBox<String> materialBox;
	private String error = "";
	private int basisWidth = -1, basisHeight = -1;
	private double basisWidthMM = -1.0;
	private int stability = -1, costs = -1;

	/**
	 * method: ConfigurationNewDialog
	 * description: constructor
	 *
	 * @author Adrian Schuetz
	 * @param owner
	 */
	public ConfigurationNewDialog(final Frame owner) {
		super(owner, textbundle.getString("dialog_configurationNew_frame"), true);
		this.setLocation(100, 100);
		this.setResizable(false);
		final JPanel input1 = new JPanel(new GridLayout(1, 2, 5, 5));
		final JPanel input2 = new JPanel(new GridLayout(6, 2, 5, 5));
		final JPanel input3 = new JPanel(new GridLayout(1, 2, 5, 5));
		final JPanel input4 = new JPanel(new BorderLayout());
		final JPanel buttons = new JPanel();
		final JPanel content = new JPanel(new BorderLayout());
		final JLabel nameLabel = new JLabel(textbundle.getString("dialog_configurationNew_label_1") + ": ");
		final JLabel materialLabel = new JLabel(textbundle.getString("dialog_configurationNew_label_2") + ": ");
		final JLabel basisNameLabel = new JLabel(textbundle.getString("dialog_configurationNew_label_3") + ": ");
		final JLabel basisWidthLabel = new JLabel(textbundle.getString("dialog_configurationNew_label_4") + ": ");
		final JLabel basisHeightLabel = new JLabel(textbundle.getString("dialog_configurationNew_label_5") + ": ");
		final JLabel basisWidthMMLabel = new JLabel(textbundle.getString("dialog_configurationNew_label_6") + ": ");
		final JLabel stabilityLabel = new JLabel(textbundle.getString("dialog_configurationNew_label_7") + ": ");
		final JLabel costsLabel = new JLabel(textbundle.getString("dialog_configurationNew_label_8") + ": ");
		nameInput = new JTextField();
		basisNameInput = new JTextField();
		basisWidthInput = new JTextField();
		basisHeightInput = new JTextField();
		basisWidthMMInput = new JTextField();
		stabilityInput = new JTextField();
		costsInput = new JTextField();
		materialBox = new JComboBox<>();
		materialBox.addItem(textbundle.getString("dialog_configurationNew_combo_1"));
		materialBox.addItem(textbundle.getString("dialog_configurationNew_combo_2"));
		materialBox.addItem(textbundle.getString("dialog_configurationNew_combo_3"));
		materialBox.addItem(textbundle.getString("dialog_configurationNew_combo_4"));
		materialBox.setEditable(false);
		materialBox.addItemListener(this);
		input1.add(nameLabel);
		input1.add(nameInput);
		input2.add(basisNameLabel);
		input2.add(basisNameInput);
		input2.add(basisWidthLabel);
		input2.add(basisWidthInput);
		input2.add(basisHeightLabel);
		input2.add(basisHeightInput);
		input2.add(basisWidthMMLabel);
		input2.add(basisWidthMMInput);
		input2.add(stabilityLabel);
		input2.add(stabilityInput);
		input2.add(costsLabel);
		input2.add(costsInput);
		input3.add(materialLabel);
		input3.add(materialBox);
		final TitledBorder input1Border = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_configurationNew_border_1"));
		input1.setBorder(input1Border);
		input1Border.setTitleColor(new Color(100, 100, 100));
		final TitledBorder input2Border = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_configurationNew_border_2"));
		input2.setBorder(input2Border);
		input2Border.setTitleColor(new Color(100, 100, 100));
		final TitledBorder input3Border = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_configurationNew_border_3"));
		input3.setBorder(input3Border);
		input3Border.setTitleColor(new Color(100, 100, 100));
		input4.add(input1, BorderLayout.NORTH);
		input4.add(input3, BorderLayout.CENTER);
		input4.add(input2, BorderLayout.SOUTH);
		final JButton ok = new JButton(textbundle.getString("button_ok"));
		ok.addActionListener(this);
		ok.setActionCommand("ok");
		final JButton cancelButton = new JButton(textbundle.getString("button_cancel"));
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		buttons.add(ok);
		buttons.add(cancelButton);
		content.add(input4, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(content);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * method: isCanceled
	 * description: returns if the dialog is canceled by user or not
	 *
	 * @author Adrian Schuetz
	 * @return true or false
	 */
	public boolean isCanceled() {
		return cancel;
	}

	/**
	 * method: getValues
	 * description: returns the user inputs
	 *
	 * @author Adrian Schuetz
	 * @return user inputs in a vector
	 */
	public Vector<Number> getValues() {
		final Vector<Number> values = new Vector<>();
		values.add(basisWidth);
		values.add(basisHeight);
		values.add(basisWidthMM);
		values.add(stability);
		values.add(costs);
		return values;
	}

	/**
	 * method: getName
	 * description: returns the user input
	 *
	 * @author Adrian Schuetz
	 * @return name configuration
	 */
	public String getName() {
		if (nameInput.getText().endsWith(".cfg")) {
			return nameInput.getText().substring(0, nameInput.getText().length() - 4);
		} else {
			return nameInput.getText();
		}
	}

	/**
	 * method: getBasisName
	 * description: returns the user input
	 *
	 * @author Adrian Schuetz
	 * @return name basis element
	 */
	public String getBasisName() {
		return basisNameInput.getText();
	}

	/**
	 * method: getMaterial
	 * description: returns the user input
	 * 0 = user-defined, 1 = Lego top view, 2 = Lego side view, 3 = Ministeck
	 *
	 * @author Adrian Schuetz
	 * @return material
	 */
	public int getMaterial() {
		return materialBox.getSelectedIndex();
	}

	/**
	 * method: validInputs
	 * description: checks the user inputs
	 *
	 * @author Adrian Schuetz
	 * @return true or false
	 */
	private boolean validInputs() {
		error = "";
		if (nameInput.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_configurationNew_error_1") + "\n");
		} else if (nameInput.getText().contains("<")
				|| nameInput.getText().contains(">")
				|| nameInput.getText().contains("?")
				|| nameInput.getText().contains("\"")
				|| nameInput.getText().contains(":")
				|| nameInput.getText().contains("|")
				|| nameInput.getText().contains("\\")
				|| nameInput.getText().contains("/")
				|| nameInput.getText().contains("*")) {
			error = error.concat(textbundle.getString("dialog_configurationNew_error_2") + "\n");
		}

		if (basisNameInput.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_configurationNew_error_3") + "\n");
		}

		if (basisWidthInput.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_configurationNew_error_4") + "\n");
		} else {
			try {
				basisWidth = Integer.parseInt(basisWidthInput.getText());

				if (basisWidth <= 0 || basisWidth > 10) {
					error = error.concat(textbundle.getString("dialog_configurationNew_error_5") + "\n");
				}
			} catch (final NumberFormatException e) {
				error = error.concat(textbundle.getString("dialog_configurationNew_error_6") + "\n");
			}
		}

		if (basisHeightInput.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_configurationNew_error_7") + "\n");
		} else {
			try {
				basisHeight = Integer.parseInt(basisHeightInput.getText());

				if (basisHeight <= 0 || basisHeight > 10) {
					error = error.concat(textbundle.getString("dialog_configurationNew_error_8") + "\n");
				}
			} catch (final NumberFormatException e) {
				error = error.concat(textbundle.getString("dialog_configurationNew_error_9") + "\n");
			}
		}

		if (basisWidthMMInput.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_configurationNew_error_10") + "\n");
		} else {
			try {
				basisWidthMM = Double.parseDouble(basisWidthMMInput.getText());

				if (basisWidthMM <= 0) {
					error = error.concat(textbundle.getString("dialog_configurationNew_error_11") + "\n");
				}
			} catch (final NumberFormatException e) {
				error = error.concat(textbundle.getString("dialog_configurationNew_error_12") + "\n");
			}
		}

		if (stabilityInput.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_configurationNew_error_13") + "\n");
		} else {

			try {
				stability = Integer.parseInt(stabilityInput.getText());

				if (stability <= 0) {
					error = error.concat(textbundle.getString("dialog_configurationNew_error_14") + "\n");
				}
			} catch (final NumberFormatException e) {
				error = error.concat(textbundle.getString("dialog_configurationNew_error_15") + "\n");
			}
		}

		if (costsInput.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_configurationNew_error_16") + "\n");
		} else {
			try {
				costs = Integer.parseInt(costsInput.getText());

				if (costs <= 0) {
					error = error.concat(textbundle.getString("dialog_configurationNew_error_17") + "\n");
				}
			} catch (final NumberFormatException e) {
				error = error.concat(textbundle.getString("dialog_configurationNew_error_18") + "\n");
			}
		}

		if ((basisWidth > 0) && (basisHeight > 0)) {
			int ratio;

			if (basisWidth > basisHeight) {
				ratio = basisWidth / basisHeight;
			} else {
				ratio = basisHeight / basisWidth;
			}

			if (ratio > 3) {
				error = error.concat(textbundle.getString("dialog_configurationNew_error_19") + "\n");
			}
		}

		if (!(error.equals(""))) {
			return false;
		}
		return true;
	}

	/**
	 * method: actionPerformed
	 * description: ActionListener
	 *
	 * @author Adrian Schuetz
	 * @param event
	 */
	public void actionPerformed(final ActionEvent event) {
		if (event.getActionCommand().contains("cancel")) {
			this.setVisible(false);
		}
		if (event.getActionCommand().contains("ok")) {
			if (validInputs()) {
				cancel = false;
				this.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this, error, textbundle.getString("dialog_error_frame"),
						JOptionPane.ERROR_MESSAGE);
				error = "";
			}
		}
	}

	/**
	 * method: itemStateChanged
	 * description: ItemChangedListener
	 *
	 * @author Adrian Schuetz
	 * @param event
	 */
	public void itemStateChanged(final ItemEvent event) {
		if (event.getSource() == materialBox) {
			if (materialBox.getSelectedIndex() == 0) {
				basisWidthInput.setText("");
				basisHeightInput.setText("");
				basisWidthMMInput.setText("");
			}
			if (materialBox.getSelectedIndex() == 1) {
				basisWidthInput.setText("1");
				basisHeightInput.setText("1");
				basisWidthMMInput.setText("8.0");
			}
			if (materialBox.getSelectedIndex() == 2) {
				basisWidthInput.setText("5");
				basisHeightInput.setText("2");
				basisWidthMMInput.setText("8.0");
			}
			if (materialBox.getSelectedIndex() == 3) {
				basisWidthInput.setText("1");
				basisHeightInput.setText("1");
				basisWidthMMInput.setText("4.16666");
			}
		}
	}
}
