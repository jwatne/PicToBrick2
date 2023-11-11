package pictobrick.ui.panels;

import java.awt.BorderLayout;
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
    /**
     * Number of last characters in filename displayed if filename length
     * exceeds max number displayed.
     */
    private static final int FILENAME_ENDING_CHARS = 3;
    /**
     * Number of starting characters in filename displayed if filename length
     * exceeds max number displayed.
     */
    private static final int FILENAME_STARTING_CHARS = 15;
    /** Maximum filename length. */
    private static final int MAX_FILENAME_LENGTH = 18;
    /** Empty options panel 1. */
    private final JPanel guiPanelOptions1Empty;
    /** Top panel options 1. */
    private final JPanel guiPanelOptions1Top;
    /** Load image button. */
    private final JButton buttonImageLoad;
    /** Load configuration button. */
    private final JButton buttonConfigurationLoad;
    /** Mosaic dimensions button. */
    private final JButton buttonMosaicDimension;
    /** Cutout button. */
    private final JButton buttonCutout;
    /** Image label. */
    private final JLabel guiLabelImage;
    /** Configuration label. */
    private final JLabel guiLabelConfiguration;
    /** Width label. */
    private final JLabel guiLabelWidth;
    /** Height label. */
    private final JLabel guiLabelHeight;

    /**
     * Constructs the panel for the main window with the given layout manager.
     *
     * @param layout     the LayoutManager to use.
     * @param mainWindow the main window of the application that is the action
     *                   listener for items on this panel.
     */
    public OptionsPanel1(final LayoutManager layout,
            final MainWindow mainWindow) {
        super(layout);
        setBorder(getOptionAreaBorder());
        guiPanelOptions1Empty = new JPanel();
        guiPanelOptions1Top = new JPanel(GRIDLAYOUT_8_1);
        buttonImageLoad = new JButton(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_button_2"));
        buttonImageLoad.addActionListener(mainWindow);
        buttonImageLoad.setActionCommand("imageload");
        buttonConfigurationLoad = new JButton(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_button_3"));
        buttonConfigurationLoad.addActionListener(mainWindow);
        buttonConfigurationLoad.setActionCommand("configurationload");
        buttonMosaicDimension = new JButton(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_button_4"));
        buttonMosaicDimension.addActionListener(mainWindow);
        buttonMosaicDimension.setActionCommand("mosaicdimension");
        buttonCutout = new JButton(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_button_5"));
        buttonCutout.addActionListener(mainWindow);
        buttonCutout.setActionCommand("cutout");
        guiLabelImage = new JLabel(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_label_1") + ": ");
        guiLabelConfiguration = new JLabel(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_label_9") + ": ");
        guiLabelWidth = new JLabel(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_label_2") + ": ");
        guiLabelHeight = new JLabel(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_label_3") + ": ");
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

    /**
     * Returns load image button.
     *
     * @return load image button.
     */
    public final JButton getButtonImageLoad() {
        return buttonImageLoad;
    }

    /**
     * Returns load configuration button.
     *
     * @return load configuration button.
     */
    public final JButton getButtonConfigurationLoad() {
        return buttonConfigurationLoad;
    }

    /**
     * Returns mosaic dimensions button.
     *
     * @return mosaic dimensions button.
     */
    public final JButton getButtonMosaicDimension() {
        return buttonMosaicDimension;
    }

    /**
     * Returns cutout button.
     *
     * @return cutout button.
     */
    public final JButton getButtonCutout() {
        return buttonCutout;
    }

    /**
     * Shows the name of the current image.
     *
     * @author Tobias Reichling
     * @param imageFileName
     */
    public void showImageInfo(final String imageFileName) {
        String fileName = imageFileName;

        if (fileName.length() > MAX_FILENAME_LENGTH) {
            fileName = fileName.substring(0, FILENAME_STARTING_CHARS) + "..."
                    + fileName.substring(
                            fileName.length() - FILENAME_ENDING_CHARS,
                            fileName.length());
        }

        guiLabelImage.setText(
                MainWindow.getTextBundle().getString("output_mainWindow_20")
                        + ": " + fileName);
    }

    /**
     * Shows the name of the current configuration.
     *
     * @author Tobias Reichling
     * @param configFilename
     */
    public void showConfigurationInfo(final String configFilename) {
        String fileName = configFilename;

        if (fileName.length() > MAX_FILENAME_LENGTH) {
            fileName = fileName.substring(0, FILENAME_STARTING_CHARS) + "..."
                    + fileName.substring(
                            fileName.length() - FILENAME_ENDING_CHARS,
                            fileName.length());
        }

        guiLabelConfiguration.setText("CFG: " + fileName);
    }

    /**
     * Shows the current dimensions.
     *
     * @author Tobias Reichling
     * @param widthValue
     * @param heightValue
     * @param reset       (true/false)
     */
    public void showDimensionInfo(final int widthValue, final int heightValue,
            final boolean reset) {
        if (reset) {
            guiLabelWidth.setText(
                    MainWindow.getTextBundle().getString("output_mainWindow_29")
                            + ": ");
            guiLabelHeight.setText(
                    MainWindow.getTextBundle().getString("output_mainWindow_30")
                            + ": ");
        } else {
            guiLabelWidth.setText(
                    MainWindow.getTextBundle().getString("output_mainWindow_29")
                            + ": " + widthValue + " "
                            + MainWindow.getTextBundle()
                                    .getString("output_mainWindow_31"));
            guiLabelHeight.setText(
                    MainWindow.getTextBundle().getString("output_mainWindow_30")
                            + ": " + heightValue + " "
                            + MainWindow.getTextBundle()
                                    .getString("output_mainWindow_31"));
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
        // showConfigurationInfo("");
        showDimensionInfo(0, 0, true);
        // enable/disable buttons, menu items, etc.
        getButtonMosaicDimension().setEnabled(false);
        getButtonCutout().setEnabled(false);
        getButtonImageLoad().setEnabled(true);
        getButtonConfigurationLoad().setEnabled(true);
    }
}
