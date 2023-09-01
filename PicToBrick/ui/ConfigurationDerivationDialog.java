package PicToBrick.ui;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.*;

import PicToBrick.model.ColorObject;
import PicToBrick.model.Configuration;
import PicToBrick.model.ElementObject;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * class: ConfigurationDerivationDialog
 * layer: Gui (three tier architecture)
 * Beschreibung: configuration dialog
 *
 * @author Tobias Reichling
 */
public class ConfigurationDerivationDialog
		extends JDialog
		implements ActionListener {
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private boolean cancel = true;
	private final JList<String> colorList;
	private DefaultListModel<String> colorListModel = null;
	private final JList<String> elementList;
	private DefaultListModel<String> elementListModel = null;
	private Configuration configuration;
	private boolean derivate;
	private final JTextField configurationNameInput;

	/**
	 * method: ConfigurationDerivationDialog
	 * description: constructor
	 *
	 * @author Tobias Reichling
	 * @param owner
	 * @param configurationNew the new configuration
	 * @param configurationOld configuration from which the new configuration is
	 *                         derived
	 */
	public ConfigurationDerivationDialog(final Frame owner, final Configuration configurationNew,
			final Configuration configurationOld) {
		super(owner, textbundle.getString("dialog_configurationDerivation_frame"), true);
		this.setLocation(100, 100);
		this.setResizable(false);
		final JPanel content = new JPanel(new BorderLayout());
		final JPanel input = new JPanel(new BorderLayout());
		final JPanel data = new JPanel(new BorderLayout());
		final JPanel data1 = new JPanel(new GridLayout(1, 2));
		final JPanel data2 = new JPanel(new GridLayout(5, 2));
		final JPanel colors = new JPanel(new BorderLayout(5, 0));
		final JPanel scroll = new JPanel(new GridLayout(2, 1));
		final JPanel colorButtons1 = new JPanel(new BorderLayout());
		final JPanel colorButtons2 = new JPanel(new BorderLayout());
		final JPanel elements = new JPanel(new BorderLayout(5, 0));
		final JPanel elementsButtons1 = new JPanel(new BorderLayout());
		final JPanel elementsButtons2 = new JPanel(new BorderLayout());
		final JPanel buttons = new JPanel();
		final JLabel configurationName = new JLabel(
				textbundle.getString("dialog_configurationDerivation_label_1") + ":");
		configurationNameInput = new JTextField();
		final JLabel basisName1 = new JLabel(textbundle.getString("dialog_configurationDerivation_label_2") + ":");
		final JLabel basisRatio1 = new JLabel(textbundle.getString("dialog_configurationDerivation_label_3") + ":");
		final JLabel basisWidthMM1 = new JLabel(textbundle.getString("dialog_configurationDerivation_label_4") + ":");
		final JLabel basisStability1 = new JLabel(textbundle.getString("dialog_configurationDerivation_label_5") + ":");
		final JLabel basisCosts1 = new JLabel(textbundle.getString("dialog_configurationDerivation_label_6") + ":");
		final JLabel basisName2 = new JLabel();
		final JLabel basisRatio2 = new JLabel();
		final JLabel basisWidthMM2 = new JLabel();
		final JLabel basisStability2 = new JLabel();
		final JLabel basisCosts2 = new JLabel();
		final JButton colorNew = new JButton(textbundle.getString("dialog_configurationDerivation_button_1"));
		colorNew.setActionCommand("colorNew");
		colorNew.addActionListener(this);
		final JButton colorDelete = new JButton(textbundle.getString("dialog_configurationDerivation_button_2"));
		colorDelete.setActionCommand("colorDelete");
		colorDelete.addActionListener(this);
		final JButton elementNew = new JButton(textbundle.getString("dialog_configurationDerivation_button_3"));
		elementNew.setActionCommand("elementNew");
		elementNew.addActionListener(this);
		final JButton elementDelete = new JButton(textbundle.getString("dialog_configurationDerivation_button_4"));
		elementDelete.setActionCommand("elementDelete");
		elementDelete.addActionListener(this);
		final JButton ok = new JButton(textbundle.getString("dialog_configurationDerivation_button_5"));
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		final JButton cancelButton = new JButton(textbundle.getString("dialog_configurationDerivation_button_6"));
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		data1.add(configurationName);
		data1.add(configurationNameInput);
		data2.add(basisName1);
		data2.add(basisName2);
		data2.add(basisRatio1);
		data2.add(basisRatio2);
		data2.add(basisWidthMM1);
		data2.add(basisWidthMM2);
		data2.add(basisStability1);
		data2.add(basisStability2);
		data2.add(basisCosts1);
		data2.add(basisCosts2);
		final TitledBorder data1Border = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_configurationDerivation_border_1"));
		data1.setBorder(data1Border);
		data1Border.setTitleColor(new Color(100, 100, 100));
		final TitledBorder data2Border = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_configurationDerivation_border_2"));
		data2.setBorder(data2Border);
		data2Border.setTitleColor(new Color(100, 100, 100));
		data.add(data1, BorderLayout.NORTH);
		data.add(data2, BorderLayout.SOUTH);
		colorButtons1.add(colorNew, BorderLayout.NORTH);
		colorButtons1.add(colorButtons2, BorderLayout.CENTER);
		colorButtons2.add(colorDelete, BorderLayout.NORTH);
		colorListModel = new DefaultListModel<>();
		colorList = new JList<>(colorListModel);
		colorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JScrollPane colorScroll = new JScrollPane(colorList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		colors.add(colorScroll, BorderLayout.CENTER);
		colors.add(colorButtons1, BorderLayout.EAST);
		final TitledBorder colorBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_configurationDerivation_border_3"));
		colors.setBorder(colorBorder);
		colorBorder.setTitleColor(new Color(100, 100, 100));
		colors.setPreferredSize(new Dimension(colors.getPreferredSize().width, 150));
		elementsButtons1.add(elementNew, BorderLayout.NORTH);
		elementsButtons1.add(elementsButtons2, BorderLayout.CENTER);
		elementsButtons2.add(elementDelete, BorderLayout.NORTH);
		elementListModel = new DefaultListModel<>();
		elementList = new JList<>(elementListModel);
		elementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JScrollPane elementsScroll = new JScrollPane(elementList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		elements.add(elementsScroll, BorderLayout.CENTER);
		elements.add(elementsButtons1, BorderLayout.EAST);
		final TitledBorder elementsBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_configurationDerivation_border_4"));
		elements.setBorder(elementsBorder);
		elementsBorder.setTitleColor(new Color(100, 100, 100));
		elements.setPreferredSize(new Dimension(elements.getPreferredSize().width, 150));
		scroll.add(colors);
		scroll.add(elements);
		input.add(data, BorderLayout.NORTH);
		input.add(scroll, BorderLayout.CENTER);
		content.add(input, BorderLayout.CENTER);
		buttons.add(ok);
		buttons.add(cancelButton);
		content.add(buttons, BorderLayout.SOUTH);

		if (!(configurationNew == null)) {
			configuration = configurationNew;
			derivate = false;
		} else {
			configuration = configurationOld;
			derivate = true;
		}

		configurationNameInput.setText(configuration.getName());
		basisName2.setText(configuration.getBasisName());
		basisRatio2.setText(configuration.getBasisWidth()
				+ " x "
				+ configuration.getBasisHeight());
		basisWidthMM2.setText(configuration.getBasisWidthMM() + " mm");
		basisStability2.setText(configuration.getBasisStability() + "");
		basisCosts2.setText(configuration.getBasisCosts() + "");

		if (derivate) {
			for (final Enumeration<ColorObject> colorEnum = configurationOld.getAllColors(); colorEnum
					.hasMoreElements();) {
				colorListModel.addElement((colorEnum.nextElement()).getName());
			}

			for (final Enumeration<ElementObject> elementsEnum = configurationOld.getAllElements(); elementsEnum
					.hasMoreElements();) {
				elementListModel.addElement((elementsEnum.nextElement()).getName());
			}
		} else {
			elementListModel.addElement(configuration.getBasisName());
		}

		this.getContentPane().add(content);
		this.pack();
		this.setSize(400, content.getPreferredSize().height);
		this.setVisible(true);
	}

	/**
	 * method: isCanceled
	 * description: checks if the dialog is canceled by user
	 *
	 * @author Tobias Reichling
	 * @return true or false
	 */
	public boolean isCanceled() {
		return this.cancel;
	}

	/**
	 * method: isInputValid
	 * description: checks input
	 *
	 * @author Tobias Reichling
	 * @return true or false
	 */
	private boolean isInputValid() {
		String error = "";
		if (configurationNameInput.getText().length() == 0) {
			error = (textbundle.getString("dialog_configurationDerivation_error_1"));
		} else if (configurationNameInput.getText().contains("<")
				|| configurationNameInput.getText().contains(">")
				|| configurationNameInput.getText().contains("?")
				|| configurationNameInput.getText().contains("\"")
				|| configurationNameInput.getText().contains(":")
				|| configurationNameInput.getText().contains("|")
				|| configurationNameInput.getText().contains("\\")
				|| configurationNameInput.getText().contains("/")
				|| configurationNameInput.getText().contains("*")) {
			error = error.concat(textbundle.getString("dialog_configurationDerivation_error_2") + "\n");
		}
		if (colorListModel.size() == 0) {
			error = error.concat(textbundle.getString("dialog_configurationDerivation_error_3"));
		}
		if (!(error.length() == 0)) {
			JOptionPane.showMessageDialog(this, error, textbundle.getString("dialog_error_frame"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * method: getConfiguration
	 * description: returns a configuration
	 *
	 * @author Tobias Reichling
	 * @return configuration
	 */
	public Configuration getConfiguration() {
		return this.configuration;
	}

	/**
	 * method: show
	 * description: shows dialog
	 *
	 * @author Tobias Reichling
	 */
	public void showDialog() {
		this.setVisible(true);
	}

	/**
	 * method: actionPerformed
	 * description: actionListener
	 *
	 * @author Tobias Reichling
	 * @param event
	 */
	public void actionPerformed(final ActionEvent event) {
		if (event.getActionCommand().contains("colorNew")) {
			ColorObjectNewDialog newColor = new ColorObjectNewDialog(this);
			boolean cancel = false;
			while (!(cancel)) {
				if (!newColor.isCanceled()) {
					if (!(configuration.setColor(newColor.getColorName(), newColor.getColorObject()))) {
						JOptionPane.showMessageDialog(this,
								textbundle.getString("dialog_configurationDerivation_error_4"),
								textbundle.getString("dialog_error_frame"), JOptionPane.ERROR_MESSAGE);
						newColor.setVisible();
					} else {
						colorListModel.addElement(newColor.getColorName());
						if (colorListModel.getSize() > 0) {
							colorList.setSelectedIndex(colorListModel.getSize() - 1);
						}
						cancel = true;
						newColor = null;
					}
				} else {
					cancel = true;
					newColor = null;
				}
			}
		}
		if (event.getActionCommand().contains("colorDelete")) {
			if (colorListModel.getSize() > 0) {
				if (!(configuration
						.deleteColor((String) (colorListModel.getElementAt(colorList.getMinSelectionIndex()))))) {
					JOptionPane.showMessageDialog(this, textbundle.getString("dialog_configurationDerivation_error_5"),
							textbundle.getString("dialog_error_frame"), JOptionPane.ERROR_MESSAGE);
				} else {
					colorListModel.removeElementAt(colorList.getMinSelectionIndex());
					colorList.setSelectedIndex(0);
				}
			}
		}
		if (event.getActionCommand().contains("elementNew")) {
			ElementObjectNewDialog newElement = new ElementObjectNewDialog(this, configuration.getBasisWidth(),
					configuration.getBasisHeight());
			boolean cancel = false;
			while (!(cancel)) {
				if (!newElement.isCanceled()) {
					if (!(configuration.setElement(newElement.getElementName(), newElement.getElementWidth(),
							newElement.getElementHeight(), newElement.getElement(), newElement.getStability(),
							newElement.getCosts()))) {
						JOptionPane.showMessageDialog(this,
								textbundle.getString("dialog_configurationDerivation_error_6"),
								textbundle.getString("dialog_error_frame"), JOptionPane.ERROR_MESSAGE);
						newElement.showDialog();
					} else {
						elementListModel.addElement(newElement.getElementName());
						if (elementListModel.getSize() > 0) {
							elementList.setSelectedIndex(elementListModel.getSize() - 1);
						}
						cancel = true;
						newElement = null;
					}
				} else {
					cancel = true;
					newElement = null;
				}
			}
		}
		if (event.getActionCommand().contains("elementDelete")) {
			if (elementListModel.getSize() > 0) {
				if (((String) (elementListModel.getElementAt(elementList.getMinSelectionIndex())))
						.equals(configuration.getBasisName())) {
					JOptionPane.showMessageDialog(this, textbundle.getString("dialog_configurationDerivation_error_7"),
							textbundle.getString("dialog_error_frame"), JOptionPane.ERROR_MESSAGE);
				} else {
					if (!(configuration.deleteElement(
							(String) (elementListModel.getElementAt(elementList.getMinSelectionIndex()))))) {
						JOptionPane.showMessageDialog(this,
								textbundle.getString("dialog_configurationDerivation_error_8"),
								textbundle.getString("dialog_error_frame"), JOptionPane.ERROR_MESSAGE);
					} else {
						elementListModel.removeElementAt(elementList.getMinSelectionIndex());
						elementList.setSelectedIndex(0);
					}
				}
			}
		}
		if (event.getActionCommand().contains("cancel")) {
			this.cancel = true;
			this.setVisible(false);
		}
		if (event.getActionCommand().contains("ok")) {
			if (isInputValid()) {
				this.configuration.setName(configurationNameInput.getText());
				this.cancel = false;
				this.setVisible(false);
			}
		}
	}
}
