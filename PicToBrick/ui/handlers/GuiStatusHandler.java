package pictobrick.ui.handlers;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;

import pictobrick.service.DataProcessor;
import pictobrick.ui.MainWindow;
import pictobrick.ui.PictureElement;

/**
 * Handler for updating the GUI status for the MainWindow. Code moved from
 * MainWindow by John Watne 09/2023.
 */
public class GuiStatusHandler {
    /**
     * GUI start status.
     */
    public static final int GUI_START = 10;
    /**
     * Image and configuration loaded status.
     */
    public static final int IMAGE_AND_CONFIG_LOADED = 11;
    /**
     * Cutout status.
     */
    public static final int CUTOUT = 12;
    /**
     * Cutout with no rectangle available state.
     */
    public static final int CUTOUT_NO_RECTANGLE_AVAILABLE = 13;
    /**
     * Cutout with rectangle available state.
     */
    public static final int CUTOUT_WITH_RECTANGLE_AVAILABLE = 14;
    /**
     * Generate mosaic status.
     */
    public static final int GENERATE_MOSAIC = 20;
    /**
     * Disable GUI while generating mosaic state.
     */
    public static final int DISABLE_GUI_WHILE_GENERATE_MOSAIC = 21;
    /**
     * Enable GUI after generating mosaic state.
     */
    public static final int ENABLE_GUI_AFTER_GENERATE_MOSAIC = 22;
    /**
     * Output status.
     */
    public static final int OUTPUT = 30;
    /**
     * Disable GUI while generating output files state.
     */
    public static final int DISABLE_GUI_WHILE_GENERATING_OUTPUT = 31;
    /**
     * Enable GUI after generating output files state.
     */
    public static final int ENABLE_GUI_AFTER_GENERATING_OUTPUT = 32;
    private final MainWindow mainWindow;

    public GuiStatusHandler(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * method: guiStatus
     * description: changes gui status
     *
     * @author Tobias Reichling
     * @param status
     */
    public void guiStatus(final int status) {
        switch (status) {
            // gui start
            case GUI_START:
                processGuiStart();
                break;
            // image and configuration are loaded
            case IMAGE_AND_CONFIG_LOADED:
                processImageAndConfigurationLoaded();
                break;
            // cutout situation
            case CUTOUT:
                processCutoutState();
                break;
            // cutout situation - no rectangle available
            case CUTOUT_NO_RECTANGLE_AVAILABLE:
                processCutoutNoRectangleAvailable();
                break;
            // cutout situation - rectangle available
            case CUTOUT_WITH_RECTANGLE_AVAILABLE:
                processCutoutWithRectangleAvailable();
                break;
            // generate mosaic
            case GENERATE_MOSAIC:
                processGenerateMosaic();
                break;
            // disable gui while generating mosaic
            case DISABLE_GUI_WHILE_GENERATE_MOSAIC:
                processDisableGuiWhileGeneratingMosaic();
                break;
            // enable gui after generating mosaic
            case ENABLE_GUI_AFTER_GENERATE_MOSAIC:
                processEnableGuiAfterGeneratingMosaic();
                break;
            // output
            case OUTPUT:
                processOutput();
                break;
            // disable gui while generating output documents
            case DISABLE_GUI_WHILE_GENERATING_OUTPUT:
                processDisableGuiWhileGeneratingOutput();
                break;
            // enable gui after generating output documents
            case ENABLE_GUI_AFTER_GENERATING_OUTPUT: // enable/disable buttons, menu items, etc.
                mainWindow.getGuiZoomSlider1().setEnabled(true);
                mainWindow.getGuiZoomSlider2().setEnabled(true);
                mainWindow.getMenuOutput().enableGuiAfterGeneratingOutput();
                mainWindow.getGuiOutputGrafic().setEnabled(true);
                mainWindow.getGuiOutputXml().setEnabled(true);
                mainWindow.getGuiOutputBuildingInstruction().setEnabled(true);
                mainWindow.getGuiOutputMaterial().setEnabled(true);
                mainWindow.getGuiOutputConfiguration().setEnabled(true);
                mainWindow.getButtonDocumentsGenerate().setEnabled(true);
                break;
            default:
                break;
        }
    }

    /**
     * Process disable GUI while geneating output files state.
     */
    private void processDisableGuiWhileGeneratingOutput() {
        // enable/disable buttons, menu items, etc.
        mainWindow.getGuiZoomSlider1().setEnabled(false);
        mainWindow.getGuiZoomSlider2().setEnabled(false);
        mainWindow.getMenuOutput().processDisableGuiWhileGeneratingOutput();
        mainWindow.getGuiOutputGrafic().setEnabled(false);
        mainWindow.getGuiOutputXml().setEnabled(false);
        mainWindow.getGuiOutputBuildingInstruction().setEnabled(false);
        mainWindow.getGuiOutputMaterial().setEnabled(false);
        mainWindow.getGuiOutputConfiguration().setEnabled(false);
        mainWindow.getButtonDocumentsGenerate().setEnabled(false);
    }

    /**
     * Process output (GUI) state.
     */
    private void processOutput() {
        // sets information text
        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_28"));
        // sets option panel 3
        final JPanel guiPanelRightArea = mainWindow.getGuiPanelRightArea();
        final JPanel guiPanelOptions2 = mainWindow.getGuiPanelOptions2();
        guiPanelRightArea.remove(guiPanelOptions2);
        final JPanel guiPanelOptions3 = mainWindow.getGuiPanelOptions3();
        guiPanelRightArea.add(guiPanelOptions3, BorderLayout.CENTER);
        final JPanel guiPanelZoom = mainWindow.getGuiPanelZoom();
        guiPanelRightArea.add(guiPanelZoom, BorderLayout.SOUTH);
        // enable/disable buttons, menu items, etc.
        mainWindow.getGuiZoomSlider1().setEnabled(true);
        mainWindow.getGuiZoomSlider2().setEnabled(true);
        mainWindow.radioButtonStatus(1, 1);
        mainWindow.radioButtonStatus(2, 5);
        guiPanelOptions3.updateUI();
        mainWindow.getMenuFile().processOutput();
        mainWindow.getMenuPreprocessing().getMenuMosaicDimension().setEnabled(false);
        mainWindow.getMenuMosaic().processOutput();
        mainWindow.getGuiPanelOptions2().processOutput();
        mainWindow.getMenuOutput().processOutput();
    }

    /**
     * Process enable GUI after generating mosaic state.
     */
    private void processEnableGuiAfterGeneratingMosaic() {
        // sets information text
        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_27"));
        // enable/disable buttons, menu items, etc.
        mainWindow.getGuiPanelOptions2().processEnableGuiAfterGeneratingMosaic();
        final JSlider guiZoomSlider1 = mainWindow.getGuiZoomSlider1();
        final int guiZoomSlider1Value = 3;
        guiZoomSlider1.setValue(guiZoomSlider1Value);
        guiZoomSlider1.setEnabled(true);
        mainWindow.setGuiZoomSlider1Value(guiZoomSlider1Value);
        final int guiZoomSlider2Value = 3;
        final JSlider guiZoomSlider2 = mainWindow.getGuiZoomSlider2();
        guiZoomSlider2.setValue(guiZoomSlider2Value);
        mainWindow.setGuiZoomSlider2Value(guiZoomSlider2Value);
        guiZoomSlider2.setEnabled(true);
        mainWindow.getMenuMosaic().processEnableGuiAfterGeneratingMosaic();
    }

    /**
     * Process disable GUI while generating mosaic state.
     */
    private void processDisableGuiWhileGeneratingMosaic() {
        // enable/disable buttons, menu items, etc.
        mainWindow.getMenuMosaic().processDisableGuiWhileGeneratingMosaic();
        mainWindow.getGuiPanelOptions2().processDisableGuiWhileGeneratingMosaic();
        mainWindow.getGuiZoomSlider1().setEnabled(false);
        mainWindow.getGuiZoomSlider2().setEnabled(false);
    }

    /**
     * Process generate mosaic state.
     */
    private void processGenerateMosaic() {
        // sets information text
        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_26"));
        // sets option panel 2
        final JPanel guiPanelRightArea = mainWindow.getGuiPanelRightArea();
        final JPanel guiPanelOptions1 = mainWindow.getGuiPanelOptions1();
        guiPanelRightArea.remove(guiPanelOptions1);
        final JPanel guiPanelOptions2 = mainWindow.getGuiPanelOptions2();
        guiPanelRightArea.add(guiPanelOptions2, BorderLayout.CENTER);
        final JPanel guiPanelZoom = mainWindow.getGuiPanelZoom();
        guiPanelRightArea.add(guiPanelZoom, BorderLayout.SOUTH);
        guiPanelOptions2.updateUI();
        // enable/disable buttons, menu items, etc.
        mainWindow.getGuiZoomSlider1().setEnabled(true);
        mainWindow.getMenuFile().processGenerateMosaic();
        mainWindow.getMenuPreprocessing().getMenuMosaicDimension().setEnabled(false);
        mainWindow.getMenuMosaic().processGenerateMosaic();
        mainWindow.getGuiPanelOptions2().processGenerateMosaic();
        mainWindow.getMenuOutput().processGenerateMosaic();
    }

    /**
     * Process cutout with rectangle available state.
     */
    private void processCutoutWithRectangleAvailable() {
        // enable/disable buttons, menu items, etc.
        mainWindow.getGuiZoomSlider1().setEnabled(false);
        mainWindow.getGuiPanelOptions1().getButtonCutout().setEnabled(true);
        // sets information text
        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_25"));
    }

    /**
     * Process cutout without rectangle available state.
     */
    private void processCutoutNoRectangleAvailable() {
        // enable/disable buttons, menu items, etc.
        mainWindow.getGuiZoomSlider1().setEnabled(true);
        mainWindow.getGuiPanelOptions1().getButtonCutout().setEnabled(false);
    }

    /**
     * Process cutout state.
     */
    private void processCutoutState() {
        // set information text
        final int mosaicWidth = mainWindow.getMosaicWidth();
        final int mosaicHeight = mainWindow.getMosaicHeight();
        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_23") + ": " + mosaicWidth + " x "
                + mosaicHeight + ". "
                + MainWindow.textbundle.getString("output_mainWindow_24") + ".");
        // enable/disable buttons, menu items, etc.
        mainWindow.getGuiPanelOptions1().processCutoutState();
        mainWindow.getMenuFile().processCutoutState();
        processCutoutNoRectangleAvailable();
    }

    /**
     * Process image and configuration loaded state.
     */
    private void processImageAndConfigurationLoaded() {
        // enable/disable buttons, menu items, etc.
        mainWindow.getMenuPreprocessing().getMenuMosaicDimension().setEnabled(true);
        mainWindow.getGuiPanelOptions1().getButtonMosaicDimension().setEnabled(true);
        // set information text
        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_22"));
    }

    /**
     * Process GUI start state.
     */
    private void processGuiStart() {
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();
        dataProcessing.setCurrentConfiguration(null);
        // sets option panel 1
        final JPanel guiPanelRightArea = mainWindow.getGuiPanelRightArea();
        guiPanelRightArea.removeAll();
        final JPanel guiPanelOptions1 = mainWindow.getGuiPanelOptions1();
        guiPanelRightArea.add(guiPanelOptions1, BorderLayout.CENTER);
        final JPanel guiPanelZoom = mainWindow.getGuiPanelZoom();
        guiPanelRightArea.add(guiPanelZoom, BorderLayout.SOUTH);
        // init check boxes
        mainWindow.getMainWindowActionHandlers().checkBoxStatus(0, true);
        // init zoom sliders
        final JSlider guiZoomSlider1 = mainWindow.getGuiZoomSlider1();
        guiZoomSlider1.setEnabled(false);
        final int guiZoomSlider1Value = 3;
        guiZoomSlider1.setValue(guiZoomSlider1Value);
        mainWindow.setGuiZoomSlider1Value(guiZoomSlider1Value);
        final JSlider guiZoomSlider2 = mainWindow.getGuiZoomSlider2();
        guiZoomSlider2.setEnabled(false);
        final int guiZoomSlider2Value = 3;
        guiZoomSlider2.setValue(guiZoomSlider2Value);
        mainWindow.setGuiZoomSlider2Value(guiZoomSlider2Value);
        mainWindow.getGuiPanelOptions1().processGuiStart();
        // reset images
        dataProcessing.imageReset();
        final JPanel guiPanelTopArea2 = mainWindow.getGuiPanelTopArea2();
        guiPanelTopArea2.removeAll();
        final PictureElement guiPictureElementTop = new PictureElement(mainWindow);
        mainWindow.setGuiPictureElementTop(guiPictureElementTop);
        guiPanelTopArea2.add(guiPictureElementTop);
        guiPictureElementTop.setImage(null);
        mainWindow.getGuiScrollPaneTop().updateUI();
        mainWindow.getGuiPictureElementBottom().setImage(null);
        mainWindow.getGuiScrollPaneBottom().updateUI();
        // enable/disable buttons, menu items, etc.
        guiPanelOptions1.updateUI();
        mainWindow.getMenuFile().processGuiStart();
        mainWindow.getMenuPreprocessing().getMenuMosaicDimension().setEnabled(false);
        mainWindow.getMenuMosaic().processGuiStart();
        mainWindow.getGuiPanelOptions2().processGuiStart();
        mainWindow.getMenuOutput().processGuiStart();
        mainWindow.radioButtonStatus(1, 1);
        mainWindow.radioButtonStatus(2, 5);
    }
}
