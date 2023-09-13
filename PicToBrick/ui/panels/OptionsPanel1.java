package pictobrick.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pictobrick.ui.MainWindow;

/**
 * Panel for the first set of options for the application. Code moved from
 * {@link pictobrick.ui.MainWindow} by John Watne 09/2023.
 */
public class OptionsPanel1 extends JPanel implements OptionsPanel {
    private final JPanel guiPanelOptions1Empty;
    private final JPanel guiPanelOptions1Top;
    private final JButton buttonImageLoad;
    private final JButton buttonConfigurationLoad;
    private final JButton buttonMosaicDimension;
    private final JButton buttonCutout;
    private final JLabel guiLabelImage;
    private final JLabel guiLabelConfiguration;
    private final JLabel guiLabelWidth;
    private final JLabel guiLabelHeight;

    /**
     * Constructs the panel for the main window with the given layout manager.
     *
     * @param layout     the LayoutManager to use.
     * @param mainWindow the main window of the application that is the action
     *                   listener for items on this panel.
     */
    public OptionsPanel1(final LayoutManager layout, final MainWindow mainWindow) {
        super(layout);
        setBorder(getOptionAreaBorder());
        guiPanelOptions1Empty = new JPanel();
        guiPanelOptions1Top = new JPanel(new GridLayout(8, 1));
        buttonImageLoad = new JButton(MainWindow.textbundle.getString("dialog_mainWindow_button_2"));
        buttonImageLoad.addActionListener(mainWindow);
        buttonImageLoad.setActionCommand("imageload");
        buttonConfigurationLoad = new JButton(MainWindow.textbundle.getString("dialog_mainWindow_button_3"));
        buttonConfigurationLoad.addActionListener(mainWindow);
        buttonConfigurationLoad.setActionCommand("configurationload");
        buttonMosaicDimension = new JButton(MainWindow.textbundle.getString("dialog_mainWindow_button_4"));
        buttonMosaicDimension.addActionListener(mainWindow);
        buttonMosaicDimension.setActionCommand("mosaicdimension");
        buttonCutout = new JButton(MainWindow.textbundle.getString("dialog_mainWindow_button_5"));
        buttonCutout.addActionListener(mainWindow);
        buttonCutout.setActionCommand("cutout");
        guiLabelImage = new JLabel(MainWindow.textbundle.getString("dialog_mainWindow_label_1") + ": ");
        guiLabelConfiguration = new JLabel(MainWindow.textbundle.getString("dialog_mainWindow_label_9") + ": ");
        guiLabelWidth = new JLabel(MainWindow.textbundle.getString("dialog_mainWindow_label_2") + ": ");
        guiLabelHeight = new JLabel(MainWindow.textbundle.getString("dialog_mainWindow_label_3") + ": ");
        add(guiPanelOptions1Top, BorderLayout.NORTH);
        add(guiPanelOptions1Empty, BorderLayout.CENTER);
        add(buttonCutout, BorderLayout.SOUTH);
        guiPanelOptions1Top.add(buttonImageLoad);
        guiPanelOptions1Top.add(guiLabelImage);
        guiPanelOptions1Top.add(buttonConfigurationLoad);
        guiPanelOptions1Top.add(guiLabelConfiguration);
        guiPanelOptions1Top.add(buttonMosaicDimension);
        guiPanelOptions1Top.add(guiLabelWidth);
        guiPanelOptions1Top.add(guiLabelHeight);
    }

    public JButton getButtonImageLoad() {
        return buttonImageLoad;
    }

    public JButton getButtonConfigurationLoad() {
        return buttonConfigurationLoad;
    }

    public JButton getButtonMosaicDimension() {
        return buttonMosaicDimension;
    }

    public JButton getButtonCutout() {
        return buttonCutout;
    }

    /**
     * method: showImageInfo
     * description: shows the name of the current image
     *
     * @author Tobias Reichling
     * @param fileName
     */
    public void showImageInfo(final String imageFileName) {
        String fileName = imageFileName;

        if (fileName.length() > 18) {
            fileName = fileName.substring(0, 15) + "..." + fileName.substring(fileName.length() - 3, fileName.length());
        }

        guiLabelImage.setText(MainWindow.textbundle.getString("output_mainWindow_20") + ": " + fileName);
    }

    /**
     * method: showConfigurationInfo
     * description: shows the name of the current configuration
     *
     * @author Tobias Reichling
     * @param fileName
     */
    public void showConfigurationInfo(final String configFilename) {
        String fileName = configFilename;

        if (fileName.length() > 18) {
            fileName = fileName.substring(0, 15) + "..." + fileName.substring(fileName.length() - 3, fileName.length());
        }

        guiLabelConfiguration.setText("CFG: " + fileName);
    }

    /**
     * method: showDimensionInfo
     * description: shows the current dimensions
     *
     * @author Tobias Reichling
     * @param widthValue
     * @param heightValue
     * @param reset       (true/false)
     */
    public void showDimensionInfo(final int widthValue, final int heightValue, final boolean reset) {
        if (reset) {
            guiLabelWidth.setText(MainWindow.textbundle.getString("output_mainWindow_29") + ": ");
            guiLabelHeight.setText(MainWindow.textbundle.getString("output_mainWindow_30") + ": ");
        } else {
            guiLabelWidth.setText(MainWindow.textbundle.getString("output_mainWindow_29") + ": " + widthValue + " "
                    + MainWindow.textbundle.getString("output_mainWindow_31"));
            guiLabelHeight.setText(MainWindow.textbundle.getString("output_mainWindow_30") + ": " + heightValue + " "
                    + MainWindow.textbundle.getString("output_mainWindow_31"));
        }
    }

    /**
     * Process options 1 panel portion of cutout state.
     */
    public void processCutoutState() {
        getButtonCutout().setEnabled(false);
        getButtonImageLoad().setEnabled(false);
        getButtonConfigurationLoad().setEnabled(false);
    }

    /**
     * Process options 1 panel portion of GUI start state.
     */
    public void processGuiStart() {
        // init information labels
        showImageInfo("");
        showConfigurationInfo("");
        showDimensionInfo(0, 0, true);
        // enable/disable buttons, menu items, etc.
        getButtonMosaicDimension().setEnabled(false);
        getButtonCutout().setEnabled(false);
        getButtonImageLoad().setEnabled(true);
        getButtonConfigurationLoad().setEnabled(true);
    }
}
