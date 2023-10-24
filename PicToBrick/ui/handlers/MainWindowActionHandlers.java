package pictobrick.ui.handlers;

import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;

import pictobrick.service.ConfigurationLoader;
import pictobrick.service.DataProcessor;
import pictobrick.service.SwingWorker;
import pictobrick.ui.MainWindow;
import pictobrick.ui.MosaicSizeDialog;
import pictobrick.ui.PictureElement;
import pictobrick.ui.WorkingDirectoryDialog;
import pictobrick.ui.menus.OutputMenu;
import pictobrick.ui.panels.OptionsPanel3;

/**
 * Handles ActionHandler events for the MainWindow. Code moved from MainWindow
 * by John Watne 09/2023.
 */
public class MainWindowActionHandlers {
    /** Number for miscellaneous progress bar. */
    private static final int MISCELLANEOUS_PROGRESS_BAR = 6;
    /** Number for XML progress bar. */
    private static final int XML_PROGRESS_BAR = 5;
    /** Number for instruction progress bar. */
    private static final int INSTRUCTION_PROGRESS_BAR = 4;
    /** Number for material progress bar. */
    private static final int MATERIAL_PROGRESS_BAR = 3;
    /** Tiling value 3. */
    private static final int TILING_3 = 3;
    /** Tiling value 4. */
    private static final int TILING_4 = 4;
    /** Molding optimization. */
    private static final int MOLDING = 3;
    /** Button number 7. */
    private static final int BUTTON_7 = 7;
    /** Button number 6. */
    private static final int BUTTON_6 = 6;
    /** Button number 5. */
    private static final int BUTTON_5 = 5;
    /** Button number 4. */
    private static final int BUTTON_4 = 4;
    /** Button number 3. */
    private static final int BUTTON_3 = 3;
    /** Index for configuration menu checkbox. */
    private static final int MENU_CONFIGURATION_CHECKBOX = 26;
    /** Index for material menu checkbox. */
    private static final int MENU_MATERIAL_CHECKBOX = 25;
    /** Index for building instruction menu checkbox. */
    private static final int MENU_BUILDING_INSTRUCTION_CHECKBOX = 24;
    /** Index for XML menu checkbox. */
    private static final int MENU_XML_CHECKBOX = 23;
    /** Index for graphic menu checkbox. */
    private static final int MENU_GRAPHIC_CHECKBOX = 21;
    /** Index for configuration checkbox. */
    private static final int CONFIGURATION_CHECKBOX = 16;
    /** Index for material checkbox. */
    private static final int MATERIAL_CHECKBOX = 15;
    /** Index for building instruction checkbox. */
    private static final int BUILDING_INSTRUCTION_CHECKBOX = 14;
    /** Index for XML checkbox. */
    private static final int XML_CHECKBOX = 13;
    /** Index for grafic checkbox. */
    private static final int GRAPHIC_CHECKBOX = 11;
    /** The main window of the application. */
    private final MainWindow mainWindow;
    /** Handler for updating the GUI status of the main window. */
    private GuiStatusHandler guiStatusHandler;

    /**
     * Constructor.
     *
     * @param owner the main window whose actions are handled by this instance.
     */
    public MainWindowActionHandlers(final MainWindow owner) {
        this.mainWindow = owner;
        this.guiStatusHandler = owner.getGuiStatusHandler();
    }

    /**
     * Returns the handler for updating the GUI status of the main window.
     *
     * @return the handler for updating the GUI status of the main window.
     */
    public final GuiStatusHandler getGuiStatusHandler() {
        if (guiStatusHandler == null) {
            guiStatusHandler = mainWindow.getGuiStatusHandler();
        }

        return guiStatusHandler;
    }

    /**
     * Synchronize the status of the check boxes and the corresponding menu
     * items.
     *
     * @param actionCommand The action triggering the need to synchronize the
     *                      two.
     */
    public void setCheckboxGuiToMenuValue(final String actionCommand) {
        if (actionCommand.contains("grafic")) {
            checkBoxStatus(GRAPHIC_CHECKBOX, false);
        } else if (actionCommand.contains("xml")) {
            checkBoxStatus(XML_CHECKBOX, false);
        } else if (actionCommand.contains("buildinginstruction")) {
            checkBoxStatus(BUILDING_INSTRUCTION_CHECKBOX, false);
        } else if (actionCommand.contains("material")) {
            checkBoxStatus(MATERIAL_CHECKBOX, false);
        } else if (actionCommand.contains("configuration")) {
            checkBoxStatus(CONFIGURATION_CHECKBOX, false);
        }
    }

    /**
     * Sets the menu value to the GUI (checkbox) value.
     *
     * @param actionCommand the actionCommand sent by toggling a checkbox.
     */
    public void setCheckboxMenuToGuiValue(final String actionCommand) {
        if (actionCommand.contains("grafic")) {
            checkBoxStatus(MENU_GRAPHIC_CHECKBOX, false);
        } else if (actionCommand.contains("xml")) {
            checkBoxStatus(MENU_XML_CHECKBOX, false);
        } else if (actionCommand.contains("buildinginstruction")) {
            checkBoxStatus(MENU_BUILDING_INSTRUCTION_CHECKBOX, false);
        } else if (actionCommand.contains("material")) {
            checkBoxStatus(MENU_MATERIAL_CHECKBOX, false);
        } else if (actionCommand.contains("configuration")) {
            checkBoxStatus(MENU_CONFIGURATION_CHECKBOX, false);
        }
    }

    /**
     * Synchronisize check boxes gui and menu.
     *
     * @author Tobias Reichling
     * @param checkBoxNumber
     * @param reset          (true/false)
     */
    public void checkBoxStatus(final int checkBoxNumber, final boolean reset) {
        final OutputMenu menuOutput = mainWindow.getMenuOutput();
        final OptionsPanel3 guiPanelOptions3 = mainWindow.getGuiPanelOptions3();
        final JCheckBoxMenuItem menuGraphic = menuOutput.getMenuGraphic();
        final JCheckBox guiOutputGraphic = guiPanelOptions3
                .getGuiOutputGraphic();
        final JCheckBoxMenuItem menuXml = menuOutput.getMenuXml();
        final JCheckBox guiOutputXml = guiPanelOptions3.getGuiOutputXml();
        final JCheckBoxMenuItem menuConfiguration = menuOutput
                .getMenuConfiguration();
        final JCheckBox guiOutputConfiguration = guiPanelOptions3
                .getGuiOutputConfiguration();
        final JCheckBoxMenuItem menuBuildingInstruction = menuOutput
                .getMenuBuildingInstruction();
        final JCheckBox guiOutputBuildingInstruction = guiPanelOptions3
                .getGuiOutputBuildingInstruction();
        final JCheckBoxMenuItem menuMaterial = menuOutput.getMenuMaterial();
        final JCheckBox guiOutputMaterial = guiPanelOptions3
                .getGuiOutputMaterial();

        if (reset) {
            menuGraphic.setSelected(false);
            guiOutputGraphic.setSelected(false);
            menuXml.setSelected(false);
            guiOutputXml.setSelected(false);
            menuConfiguration.setSelected(false);
            guiOutputConfiguration.setSelected(false);
            menuBuildingInstruction.setSelected(false);
            guiOutputBuildingInstruction.setSelected(false);
            menuMaterial.setSelected(false);
            guiOutputMaterial.setSelected(false);
        } else {
            // 1x = menu
            // 2x = gui
            // ------------------
            // x1 = grafic
            // x3 = xml
            // x4 = buildingInstruction
            // x5 = material
            // x6 = configuration
            switch (checkBoxNumber) {
            case GRAPHIC_CHECKBOX:
                if (menuGraphic.isSelected()) {
                    guiOutputGraphic.setSelected(true);
                } else {
                    guiOutputGraphic.setSelected(false);
                }

                break;
            case XML_CHECKBOX:
                if (menuXml.isSelected()) {
                    guiOutputXml.setSelected(true);
                } else {
                    guiOutputXml.setSelected(false);
                }

                break;
            case BUILDING_INSTRUCTION_CHECKBOX:
                if (menuBuildingInstruction.isSelected()) {
                    guiOutputBuildingInstruction.setSelected(true);
                } else {
                    guiOutputBuildingInstruction.setSelected(false);
                }

                break;
            case MATERIAL_CHECKBOX:
                if (menuMaterial.isSelected()) {
                    guiOutputMaterial.setSelected(true);
                } else {
                    guiOutputMaterial.setSelected(false);
                }

                break;
            case CONFIGURATION_CHECKBOX:
                if (menuConfiguration.isSelected()) {
                    guiOutputConfiguration.setSelected(true);
                } else {
                    guiOutputConfiguration.setSelected(false);
                }

                break;
            case MENU_GRAPHIC_CHECKBOX:
                if (guiOutputGraphic.isSelected()) {
                    menuGraphic.setSelected(true);
                } else {
                    menuGraphic.setSelected(false);
                }

                break;
            case MENU_XML_CHECKBOX:
                if (guiOutputXml.isSelected()) {
                    menuXml.setSelected(true);
                } else {
                    menuXml.setSelected(false);
                }

                break;
            case MENU_BUILDING_INSTRUCTION_CHECKBOX:
                if (guiOutputBuildingInstruction.isSelected()) {
                    menuBuildingInstruction.setSelected(true);
                } else {
                    menuBuildingInstruction.setSelected(false);
                }

                break;
            case MENU_MATERIAL_CHECKBOX:
                if (guiOutputMaterial.isSelected()) {
                    menuMaterial.setSelected(true);
                } else {
                    menuMaterial.setSelected(false);
                }

                break;
            case MENU_CONFIGURATION_CHECKBOX:
                if (guiOutputConfiguration.isSelected()) {
                    menuConfiguration.setSelected(true);
                } else {
                    menuConfiguration.setSelected(false);
                }

                break;
            default:
                break;
            }
        }
    }

    /**
     * Synchronizes radio button and menu item values.
     *
     * @param actionCommand the action command set by toggling the radio button
     *                      or corresponding menu item.
     */
    public void synchronizeRadioButtonsAndMenuItems(
            final String actionCommand) {
        if (actionCommand.contains("11")) {
            mainWindow.radioButtonStatus(1, 1);
        } else if (actionCommand.contains("12")) {
            mainWindow.radioButtonStatus(1, 2);
        } else if (actionCommand.contains("13")) {
            mainWindow.radioButtonStatus(1, BUTTON_3);
        } else if (actionCommand.contains("14")) {
            mainWindow.radioButtonStatus(1, BUTTON_4);
        } else if (actionCommand.contains("15")) {
            mainWindow.radioButtonStatus(1, BUTTON_5);
        } else if (actionCommand.contains("16")) {
            mainWindow.radioButtonStatus(1, BUTTON_6);
        } else if (actionCommand.contains("17")) {
            mainWindow.radioButtonStatus(1, BUTTON_7);
        } else if (actionCommand.contains("21")) {
            mainWindow.radioButtonStatus(2, 1);
        } else if (actionCommand.contains("22")) {
            mainWindow.radioButtonStatus(2, 2);
        } else if (actionCommand.contains("23")) {
            mainWindow.radioButtonStatus(2, BUTTON_3);
        } else if (actionCommand.contains("24")) {
            mainWindow.radioButtonStatus(2, BUTTON_4);
        } else if (actionCommand.contains("25")) {
            mainWindow.radioButtonStatus(2, BUTTON_5);
        }
    }

    /**
     * Handle &quot;output&quot; ActionEvent.
     */
    public void outputAction() {
        getGuiStatusHandler().guiStatus(GuiStatusHandler.OUTPUT);
        mainWindow.adjustDividerLocation();
    }

    /**
     * Starts a new mosaic.
     */
    public void startNewMosaic() {
        getGuiStatusHandler().guiStatus(GuiStatusHandler.GUI_START);
        mainWindow.adjustDividerLocation();
        mainWindow.showInfo(
                MainWindow.getTextBundle().getString("output_mainWindow_4")
                        + "!");
    }

    /**
     * Generate a mosaic.
     */
    public void generateMosaic() {
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();
        final JComboBox<String> guiComboBoxInterpolation = mainWindow
                .getGuiPanelOptions2().getGuiComboBoxInterpolation();
        final ButtonGroup guiGroupQuantisation = mainWindow
                .getGuiPanelOptions2().getGuiGroupQuantisation();
        final ButtonGroup guiGroupTiling = mainWindow.getGuiPanelOptions2()
                .getGuiGroupTiling();
        dataProcessing.initInfo();
        dataProcessing.setInterpolation(
                guiComboBoxInterpolation.getSelectedIndex() + 1);
        final int quantisation = Integer.parseInt(guiGroupQuantisation
                .getSelection().getActionCommand().substring(9, 11)) - 10;
        final int tiling = Integer.parseInt(guiGroupTiling.getSelection()
                .getActionCommand().substring(9, 11)) - 20;

        if (tiling == 2 && !(dataProcessing.getCurrentConfiguration()
                .getMaterial() == MOLDING)) {
            mainWindow.errorDialog(MainWindow.getTextBundle()
                    .getString("output_mainWindow_6"));
        } else if (tiling == TILING_4 && ((dataProcessing
                .getCurrentConfiguration().getMaterial() == MOLDING)
                || (dataProcessing.getCurrentConfiguration()
                        .getMaterial() == 1))) {
            mainWindow.errorDialog(MainWindow.getTextBundle()
                    .getString("output_mainWindow_7"));
        } else if (tiling == TILING_3 && (dataProcessing
                .getCurrentConfiguration().getMaterial() == MOLDING)) {
            mainWindow.errorDialog(MainWindow.getTextBundle()
                    .getString("output_mainWindow_8"));
        } else {
            final int mosaicWidth = mainWindow.getMosaicWidth();
            final int mosaicHeight = mainWindow.getMosaicHeight();
            final JCheckBox guiThreeDEffect = mainWindow.getGuiPanelOptions2()
                    .getGuiThreeDEffect();
            final JCheckBox guiStatistic = mainWindow.getGuiPanelOptions2()
                    .getGuiStatistic();
            mainWindow.adjustDividerLocation();
            getGuiStatusHandler().guiStatus(
                    GuiStatusHandler.DISABLE_GUI_WHILE_GENERATE_MOSAIC);
            dataProcessing.generateMosaic(mosaicWidth, mosaicHeight,
                    quantisation, tiling, guiThreeDEffect.isSelected(),
                    guiStatistic.isSelected());
        }
    }

    /**
     * Process load image request.
     */
    public void loadImage() {
        mainWindow.adjustDividerLocation();
        ImageLoader.imageLoad(mainWindow);
        checkIfImageAndConfigLoaded();
    }

    /**
     * Process load configuration request.
     */
    public void loadConfiguration() {
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();

        if (dataProcessing.getWorkingDirectory() == null) {
            mainWindow.errorDialog(MainWindow.getTextBundle()
                    .getString("output_mainWindow_9"));
        } else {
            new ConfigurationLoader(mainWindow).configurationLoad();
        }

        checkIfImageAndConfigLoaded();
    }

    /**
     * Display the dialog to set the working directory.
     */
    public void setWorkingDirectory() {
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();
        var workingDirectoryDialog = new WorkingDirectoryDialog(mainWindow,
                dataProcessing.getWorkingDirectory());

        if (workingDirectoryDialog.getButton() == 1) {
            mainWindow.workingDirectory(true);
        }

        workingDirectoryDialog = null;
    }

    /**
     * Display dialog for setting the dimensions of the mosaic to be created.
     */
    public void setMosaicDimensions() {
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();
        MosaicSizeDialog mosaicDimensionDialog = new MosaicSizeDialog(
                mainWindow, dataProcessing.getCurrentConfiguration());
        final PictureElement guiPictureElementTop = mainWindow
                .getGuiPictureElementTop();

        if (!mosaicDimensionDialog.isCanceled()) {
            guiPictureElementTop.removeMouseListener(guiPictureElementTop);
            guiPictureElementTop
                    .removeMouseMotionListener(guiPictureElementTop);
            guiPictureElementTop.setCutoutRatio(new Dimension(
                    mosaicDimensionDialog.getArea().width
                            * dataProcessing.getConfigurationDimension().width,
                    mosaicDimensionDialog.getArea().height * dataProcessing
                            .getConfigurationDimension().height));
            guiPictureElementTop.addMouseListener(guiPictureElementTop);
            guiPictureElementTop.addMouseMotionListener(guiPictureElementTop);
            guiPictureElementTop.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            final int mosaicWidth = mosaicDimensionDialog.getArea().width;
            mainWindow.setMosaicWidth(mosaicWidth);
            final int mosaicHeight = mosaicDimensionDialog.getArea().height;
            mainWindow.setMosaicHeight(mosaicHeight);
            mainWindow.getGuiPanelOptions1().showDimensionInfo(mosaicWidth,
                    mosaicHeight, false);
            getGuiStatusHandler().guiStatus(GuiStatusHandler.CUTOUT);
        }

        mosaicDimensionDialog = null;
    }

    /**
     * Process generate documents request.
     */
    public void generateDocument() {
        final OptionsPanel3 guiPanelOptions3 = mainWindow.getGuiPanelOptions3();
        final JCheckBox guiOutputGrafic = guiPanelOptions3
                .getGuiOutputGraphic();
        final JCheckBox guiOutputConfiguration = guiPanelOptions3
                .getGuiOutputConfiguration();
        final JCheckBox guiOutputMaterial = guiPanelOptions3
                .getGuiOutputMaterial();
        final JCheckBox guiOutputBuildingInstruction = guiPanelOptions3
                .getGuiOutputBuildingInstruction();
        final JCheckBox guiOutputXml = guiPanelOptions3.getGuiOutputXml();

        if (guiOutputGrafic.isSelected() || guiOutputConfiguration.isSelected()
                || guiOutputMaterial.isSelected()
                || guiOutputBuildingInstruction.isSelected()
                || guiOutputXml.isSelected()) {
            getGuiStatusHandler().guiStatus(
                    GuiStatusHandler.DISABLE_GUI_WHILE_GENERATING_OUTPUT);
            mainWindow.refreshProgressBarOutputFiles(0, 1);
            mainWindow.refreshProgressBarOutputFiles(0, 2);
            mainWindow.refreshProgressBarOutputFiles(0, MATERIAL_PROGRESS_BAR);
            mainWindow.refreshProgressBarOutputFiles(0,
                    INSTRUCTION_PROGRESS_BAR);
            mainWindow.refreshProgressBarOutputFiles(0, XML_PROGRESS_BAR);
            mainWindow.refreshProgressBarOutputFiles(0,
                    MISCELLANEOUS_PROGRESS_BAR);
            mainWindow.setStatusProgressBarOutputFiles(
                    guiOutputGrafic.isSelected(),
                    guiOutputConfiguration.isSelected(),
                    guiOutputMaterial.isSelected(),
                    guiOutputBuildingInstruction.isSelected(),
                    guiOutputXml.isSelected());
            mainWindow.showProgressBarOutputFiles();

            // SwingWorker
            // "construct": all commands are startet in a new thread
            // "finished": all commands are queued to the gui thread
            // after finshing aforesaid (construct-)thread
            final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    final DataProcessor dataProcessing = mainWindow
                            .getDataProcessing();
                    final String message = dataProcessing.generateDocuments(
                            guiOutputGrafic.isSelected(),
                            guiOutputConfiguration.isSelected(),
                            guiOutputMaterial.isSelected(),
                            guiOutputBuildingInstruction.isSelected(),
                            guiOutputXml.isSelected(),
                            dataProcessing.getInfo(2));

                    if (!message.equals("")) {
                        mainWindow.errorDialog(message);
                    } else {
                        mainWindow.showInfo(MainWindow.getTextBundle()
                                .getString("output_mainWindow_10"));
                    }

                    return true;
                }

                public void finished() {
                    getGuiStatusHandler().guiStatus(
                            GuiStatusHandler.ENABLE_GUI_AFTER_GEN_OUTPUT);
                    mainWindow.hideProgressBarOutputFiles();
                }
            };

            worker.start();
        }
    }

    /**
     * Check if both image and configuration are loaded and, if so, set the GUI
     * state accordingly.
     */
    private void checkIfImageAndConfigLoaded() {
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();

        if (dataProcessing.isImage() && dataProcessing.isConfiguration()) {
            getGuiStatusHandler()
                    .guiStatus(GuiStatusHandler.IMAGE_AND_CONFIG_LOADED);
        }
    }
}
