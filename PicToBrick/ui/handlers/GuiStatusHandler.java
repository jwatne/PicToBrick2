package pictobrick.ui.handlers;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
                mainWindow.getMenuGrafic().setEnabled(true);
                mainWindow.getMenuXml().setEnabled(true);
                mainWindow.getMenuBuildingInstruction().setEnabled(true);
                mainWindow.getMenuMaterial().setEnabled(true);
                mainWindow.getMenuConfiguration().setEnabled(true);
                mainWindow.getGuiOutputGrafic().setEnabled(true);
                mainWindow.getGuiOutputXml().setEnabled(true);
                mainWindow.getGuiOutputBuildingInstruction().setEnabled(true);
                mainWindow.getGuiOutputMaterial().setEnabled(true);
                mainWindow.getGuiOutputConfiguration().setEnabled(true);
                mainWindow.getMenuDocumentGenerate().setEnabled(true);
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
        mainWindow.getMenuGrafic().setEnabled(false);
        mainWindow.getMenuXml().setEnabled(false);
        mainWindow.getMenuBuildingInstruction().setEnabled(false);
        mainWindow.getMenuMaterial().setEnabled(false);
        mainWindow.getMenuConfiguration().setEnabled(false);
        mainWindow.getGuiOutputGrafic().setEnabled(false);
        mainWindow.getGuiOutputXml().setEnabled(false);
        mainWindow.getGuiOutputBuildingInstruction().setEnabled(false);
        mainWindow.getGuiOutputMaterial().setEnabled(false);
        mainWindow.getGuiOutputConfiguration().setEnabled(false);
        mainWindow.getMenuDocumentGenerate().setEnabled(false);
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
        mainWindow.getMenuAlgorithm11().setEnabled(false);
        mainWindow.getMenuAlgorithm12().setEnabled(false);
        mainWindow.getMenuAlgorithm13().setEnabled(false);
        mainWindow.getMenuAlgorithm14().setEnabled(false);
        mainWindow.getMenuAlgorithm15().setEnabled(false);
        mainWindow.getMenuAlgorithm16().setEnabled(false);
        mainWindow.getMenuAlgorithm17().setEnabled(false);
        mainWindow.getMenuAlgorithm21().setEnabled(false);
        mainWindow.getMenuAlgorithm22().setEnabled(false);
        mainWindow.getMenuAlgorithm23().setEnabled(false);
        mainWindow.getMenuAlgorithm24().setEnabled(false);
        mainWindow.getMenuAlgorithm25().setEnabled(false);
        mainWindow.getGuiComboBoxInterpolation().setEnabled(false);
        mainWindow.getMenuMosaicGenerate().setEnabled(false);
        mainWindow.getMenuGrafic().setEnabled(true);
        mainWindow.getMenuXml().setEnabled(true);
        mainWindow.getMenuBuildingInstruction().setEnabled(true);
        mainWindow.getMenuMaterial().setEnabled(true);
        mainWindow.getMenuConfiguration().setEnabled(true);
        mainWindow.getMenuDocumentGenerate().setEnabled(true);
        mainWindow.getGuiThreeDEffect().setEnabled(false);
        mainWindow.getGuiStatistic().setEnabled(false);
    }

    /**
     * Process enable GUI after generating mosaic state.
     */
    private void processEnableGuiAfterGeneratingMosaic() {
        // sets information text
        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_27"));
        // enable/disable buttons, menu items, etc.
        mainWindow.getButtonOutput().setEnabled(true);
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
        mainWindow.getMenuAlgorithm11().setEnabled(true);
        mainWindow.getMenuAlgorithm12().setEnabled(true);
        mainWindow.getMenuAlgorithm13().setEnabled(true);
        mainWindow.getMenuAlgorithm14().setEnabled(true);
        mainWindow.getMenuAlgorithm15().setEnabled(true);
        mainWindow.getMenuAlgorithm16().setEnabled(true);
        mainWindow.getMenuAlgorithm17().setEnabled(true);
        mainWindow.getMenuAlgorithm21().setEnabled(true);
        mainWindow.getMenuAlgorithm22().setEnabled(true);
        mainWindow.getMenuAlgorithm23().setEnabled(true);
        mainWindow.getMenuAlgorithm24().setEnabled(true);
        mainWindow.getMenuAlgorithm25().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm11().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm12().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm13().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm14().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm15().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm16().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm17().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm21().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm22().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm23().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm24().setEnabled(true);
        mainWindow.getGuiRadioAlgorithm25().setEnabled(true);
        mainWindow.getGuiComboBoxInterpolation().setEnabled(true);
        mainWindow.getMenuMosaicGenerate().setEnabled(true);
        mainWindow.getButtonMosaicGenerate().setEnabled(true);
        mainWindow.getGuiThreeDEffect().setEnabled(true);
        final JRadioButton guiRadioAlgorithm25 = mainWindow.getGuiRadioAlgorithm25();

        if (!guiRadioAlgorithm25.isSelected()) {
            mainWindow.getGuiStatistic().setEnabled(true);
        }
    }

    /**
     * Process disable GUI while generating mosaic state.
     */
    private void processDisableGuiWhileGeneratingMosaic() {
        // enable/disable buttons, menu items, etc.
        mainWindow.getMenuAlgorithm11().setEnabled(false);
        mainWindow.getMenuAlgorithm12().setEnabled(false);
        mainWindow.getMenuAlgorithm13().setEnabled(false);
        mainWindow.getMenuAlgorithm14().setEnabled(false);
        mainWindow.getMenuAlgorithm15().setEnabled(false);
        mainWindow.getMenuAlgorithm16().setEnabled(false);
        mainWindow.getMenuAlgorithm17().setEnabled(false);
        mainWindow.getMenuAlgorithm21().setEnabled(false);
        mainWindow.getMenuAlgorithm22().setEnabled(false);
        mainWindow.getMenuAlgorithm23().setEnabled(false);
        mainWindow.getMenuAlgorithm24().setEnabled(false);
        mainWindow.getMenuAlgorithm25().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm11().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm12().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm13().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm14().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm15().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm16().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm17().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm21().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm22().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm23().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm24().setEnabled(false);
        mainWindow.getGuiRadioAlgorithm25().setEnabled(false);
        mainWindow.getGuiComboBoxInterpolation().setEnabled(false);
        mainWindow.getButtonOutput().setEnabled(false);
        mainWindow.getGuiZoomSlider1().setEnabled(false);
        mainWindow.getGuiZoomSlider2().setEnabled(false);
        mainWindow.getMenuMosaicGenerate().setEnabled(false);
        mainWindow.getButtonMosaicGenerate().setEnabled(false);
        mainWindow.getGuiThreeDEffect().setEnabled(false);
        mainWindow.getGuiStatistic().setEnabled(false);
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
        mainWindow.getMenuAlgorithm11().setEnabled(true);
        mainWindow.getMenuAlgorithm12().setEnabled(true);
        mainWindow.getMenuAlgorithm13().setEnabled(true);
        mainWindow.getMenuAlgorithm14().setEnabled(true);
        mainWindow.getMenuAlgorithm15().setEnabled(true);
        mainWindow.getMenuAlgorithm16().setEnabled(true);
        mainWindow.getMenuAlgorithm17().setEnabled(true);
        mainWindow.getMenuAlgorithm21().setEnabled(true);
        mainWindow.getMenuAlgorithm22().setEnabled(true);
        mainWindow.getMenuAlgorithm23().setEnabled(true);
        mainWindow.getMenuAlgorithm24().setEnabled(true);
        mainWindow.getMenuAlgorithm25().setEnabled(true);
        mainWindow.getGuiComboBoxInterpolation().setEnabled(true);
        mainWindow.getMenuMosaicGenerate().setEnabled(true);
        mainWindow.getMenuGrafic().setEnabled(false);
        mainWindow.getMenuXml().setEnabled(false);
        mainWindow.getMenuBuildingInstruction().setEnabled(false);
        mainWindow.getMenuMaterial().setEnabled(false);
        mainWindow.getMenuConfiguration().setEnabled(false);
        mainWindow.getMenuDocumentGenerate().setEnabled(false);
        mainWindow.getButtonOutput().setEnabled(false);
        final JCheckBox guiThreeDEffect = mainWindow.getGuiThreeDEffect();
        guiThreeDEffect.setEnabled(true);
        guiThreeDEffect.setSelected(true);
        final JCheckBox guiStatistic = mainWindow.getGuiStatistic();
        guiStatistic.setSelected(false);
        guiStatistic.setEnabled(false);
    }

    /**
     * Process cutout with rectangle available state.
     */
    private void processCutoutWithRectangleAvailable() {
        // enable/disable buttons, menu items, etc.
        mainWindow.getGuiZoomSlider1().setEnabled(false);
        mainWindow.getButtonCutout().setEnabled(true);
        // sets information text
        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_25"));
    }

    /**
     * Process cutout without rectangle available state.
     */
    private void processCutoutNoRectangleAvailable() {
        // enable/disable buttons, menu items, etc.
        mainWindow.getGuiZoomSlider1().setEnabled(true);
        mainWindow.getButtonCutout().setEnabled(false);
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
        mainWindow.getButtonCutout().setEnabled(false);
        mainWindow.getButtonImageLoad().setEnabled(false);
        mainWindow.getButtonConfigurationLoad().setEnabled(false);
        mainWindow.getMenuFile().processCutoutState();
        processCutoutNoRectangleAvailable();
    }

    /**
     * Process image and configuration loaded state.
     */
    private void processImageAndConfigurationLoaded() {
        // enable/disable buttons, menu items, etc.
        mainWindow.getMenuPreprocessing().getMenuMosaicDimension().setEnabled(true);
        mainWindow.getButtonMosaicDimension().setEnabled(true);
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
        // init information labels
        mainWindow.showImageInfo("");
        mainWindow.showConfigurationInfo("");
        mainWindow.showDimensionInfo(0, 0, true);
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
        mainWindow.getButtonMosaicDimension().setEnabled(false);
        mainWindow.getMenuAlgorithm11().setEnabled(false);
        mainWindow.getMenuAlgorithm12().setEnabled(false);
        mainWindow.getMenuAlgorithm13().setEnabled(false);
        mainWindow.getMenuAlgorithm14().setEnabled(false);
        mainWindow.getMenuAlgorithm15().setEnabled(false);
        mainWindow.getMenuAlgorithm16().setEnabled(false);
        mainWindow.getMenuAlgorithm17().setEnabled(false);
        mainWindow.getMenuAlgorithm21().setEnabled(false);
        mainWindow.getMenuAlgorithm22().setEnabled(false);
        mainWindow.getMenuAlgorithm23().setEnabled(false);
        mainWindow.getMenuAlgorithm24().setEnabled(false);
        mainWindow.getMenuAlgorithm25().setEnabled(false);
        final JComboBox<String> guiComboBoxInterpolation = mainWindow.getGuiComboBoxInterpolation();
        guiComboBoxInterpolation.setEnabled(false);
        guiComboBoxInterpolation.setSelectedIndex(0);
        mainWindow.getMenuMosaicGenerate().setEnabled(false);
        mainWindow.getMenuGrafic().setEnabled(false);
        mainWindow.getMenuXml().setEnabled(false);
        mainWindow.getMenuBuildingInstruction().setEnabled(false);
        mainWindow.getMenuMaterial().setEnabled(false);
        mainWindow.getMenuConfiguration().setEnabled(false);
        mainWindow.getMenuDocumentGenerate().setEnabled(false);
        mainWindow.getButtonCutout().setEnabled(false);
        mainWindow.getButtonImageLoad().setEnabled(true);
        mainWindow.getButtonConfigurationLoad().setEnabled(true);
        mainWindow.radioButtonStatus(1, 1);
        mainWindow.radioButtonStatus(2, 5);
    }

}
