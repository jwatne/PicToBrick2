package pictobrick.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import pictobrick.service.ConfigurationLoader;

/**
 * Loading a configuration.
 *
 * @author Tobias Reichling
 */
public class ConfigurationLoadingDialog extends JDialog
        implements ActionListener, PicToBrickDialog {
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** List of available configurations. */
    private final JComboBox<String> list;
    /** ButtonGroup containing type of configuration chosen. */
    private final ButtonGroup group;
    /** <code>true</code> if the dialog is canceled by the user. */
    private boolean cancel = true;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param owner
     * @param selection
     * @param lastConfigurationSource
     * @param lastConfigurationSelectionIndex
     */
    public ConfigurationLoadingDialog(final Frame owner,
            final Vector<String> selection,
            final Integer lastConfigurationSource,
            final Integer lastConfigurationSelectionIndex) {
        super(owner, textbundle.getString("dialog_configurationLoading_frame"),
                true);
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setResizable(false);
        final JPanel content = new JPanel(new BorderLayout());
        final JPanel radios = new JPanel(new GridLayout(4, 1));
        final JPanel buttons = new JPanel();
        group = new ButtonGroup();
        final JRadioButton newConfiguration = new JRadioButton(
                textbundle.getString("dialog_configurationLoading_radio_1"));
        newConfiguration.setActionCommand("new");
        newConfiguration.addActionListener(this);
        final JRadioButton derivateConfiguration = new JRadioButton(
                textbundle.getString("dialog_configurationLoading_radio_2"));
        derivateConfiguration.setActionCommand("derivate");
        derivateConfiguration.addActionListener(this);
        final JRadioButton loadConfiguration = new JRadioButton(
                textbundle.getString("dialog_configurationLoading_radio_3"));
        loadConfiguration.setActionCommand("load");
        loadConfiguration.addActionListener(this);
        group.add(newConfiguration);
        group.add(derivateConfiguration);
        group.add(loadConfiguration);
        radios.add(newConfiguration);
        radios.add(derivateConfiguration);
        radios.add(loadConfiguration);
        list = new JComboBox<>(selection);
        list.setEditable(false);
        list.setEnabled(false);

        // Change from default of new to load existing -- probably more likely.
        // Also use previous selection, if available from the same run of the
        // application. - JAW 11/2023.
        if (lastConfigurationSource == null) {
            loadConfiguration.setSelected(true);
            list.setEnabled(true);
        } else {
            switch (lastConfigurationSource.intValue()) {
            case ConfigurationLoader.NEW_CONFIGURATION:
                newConfiguration.setSelected(true);
                break;
            case ConfigurationLoader.DERIVITIVE_CONFIGURATION:
                derivateConfiguration.setSelected(true);
                list.setEnabled(true);
                break;
            case ConfigurationLoader.LOAD_EXISTING_CONFIGURATION:
                loadConfiguration.setSelected(true);
                list.setEnabled(true);
                break;
            default:
                loadConfiguration.setSelected(true);
            }
        }

        if (lastConfigurationSelectionIndex != null) {
            list.setSelectedIndex(lastConfigurationSelectionIndex.intValue());
        }

        final JButton ok = new JButton(textbundle.getString("button_ok"));
        ok.addActionListener(this);
        ok.setActionCommand("ok");
        final JButton cancelButton = new JButton(
                textbundle.getString("button_cancel"));
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        radios.add(list);
        buttons.add(ok);
        buttons.add(cancelButton);
        content.add(radios, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        final TitledBorder radioBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_configurationLoading_border"));
        radios.setBorder(radioBorder);
        radioBorder.setTitleColor(PicToBrickDialog.GRANITE_GRAY);
        this.getContentPane().add(content);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Returns the selected item (radio button) as integer.
     *
     * @author Tobias Reichling
     * @return number of the selected radio button
     */
    public int getSelection() {
        final String actionCommand = group.getSelection().getActionCommand();

        if (actionCommand.contains("new")) {
            return ConfigurationLoader.NEW_CONFIGURATION;
        } else if (actionCommand.contains("derivate")) {
            return ConfigurationLoader.DERIVITIVE_CONFIGURATION;
        } else if (actionCommand.contains("load")) {
            return ConfigurationLoader.LOAD_EXISTING_CONFIGURATION;
        }

        return 0;
    }

    /**
     * Returns the selected item (combo box) as integer.
     *
     * @author Tobias Reichling
     * @return number of the selected combo box item
     */
    public int getFile() {
        return list.getSelectedIndex();
    }

    /**
     * Returns if the dialog is canceled by user or not.
     *
     * @author Tobias Reichling
     * @return true or false
     */
    public boolean isCanceled() {
        return cancel;
    }

    /**
     * ActionListener.
     *
     * @author Tobias Reichling
     * @param event
     */
    public void actionPerformed(final ActionEvent event) {
        final String actionCommand = event.getActionCommand();

        if (actionCommand.contains("new")) {
            list.setEnabled(false);
        }

        if (actionCommand.contains("derivate")) {
            list.setEnabled(true);
        }

        if (actionCommand.contains("load")) {
            list.setEnabled(true);
        }

        if (actionCommand.contains("cancel")) {
            this.setVisible(false);
        }

        if (actionCommand.contains("ok")) {
            cancel = false;
            this.setVisible(false);
        }
    }
}
