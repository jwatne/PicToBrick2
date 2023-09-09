package pictobrick.ui.handlers;

import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;

import pictobrick.service.DataProcessor;
import pictobrick.service.SwingWorker;
import pictobrick.ui.MainWindow;
import pictobrick.ui.MosaicSizeDialog;
import pictobrick.ui.PictureElement;
import pictobrick.ui.WorkingDirectoryDialog;

/**
 * Handles ActionHandler events for the MainWindow. Code moved from MainWindow
 * by John Watne 09/2023.
 */
public class MainWindowActionHandlers {
    private final MainWindow mainWindow;
    private GuiStatusHandler guiStatusHandler;

    public MainWindowActionHandlers(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.guiStatusHandler = mainWindow.getGuiStatusHandler();
    }

    public GuiStatusHandler getGuiStatusHandler() {
        if (guiStatusHandler == null) {
            guiStatusHandler = mainWindow.getGuiStatusHandler();
        }

        return guiStatusHandler;
    }

    /**
     * Synchronize the status of the check boxes and the corresponding menu items.
     *
     * @param actionCommand The action triggering the need to synchronize the two.
     */
    public void setCheckboxGuiToMenuValue(final String actionCommand) {
        if (actionCommand.contains("grafic")) {
            checkBoxStatus(11, false);
        } else if (actionCommand.contains("xml")) {
            checkBoxStatus(13, false);
        } else if (actionCommand.contains("buildinginstruction")) {
            checkBoxStatus(14, false);
        } else if (actionCommand.contains("material")) {
            checkBoxStatus(15, false);
        } else if (actionCommand.contains("configuration")) {
            checkBoxStatus(16, false);
        }
    }

    /**
     * Sets the menu value to the GUI (checkbox) value.
     *
     * @param actionCommand the actionCommand sent by toggling a checkbox.
     */
    public void setCheckboxMenuToGuiValue(final String actionCommand) {
        if (actionCommand.contains("grafic")) {
            checkBoxStatus(21, false);
        } else if (actionCommand.contains("xml")) {
            checkBoxStatus(23, false);
        } else if (actionCommand.contains("buildinginstruction")) {
            checkBoxStatus(24, false);
        } else if (actionCommand.contains("material")) {
            checkBoxStatus(25, false);
        } else if (actionCommand.contains("configuration")) {
            checkBoxStatus(26, false);
        }
    }

    /**
     * method: checkBoxStatus
     * description: synchronisize check boxes gui and menu
     *
     * @author Tobias Reichling
     * @param checkBoxNumber
     * @param reset          (true/false)
     */
    public void checkBoxStatus(final int checkBoxNumber, final boolean reset) {
        final JCheckBoxMenuItem menuGrafic = mainWindow.getMenuGrafic();
        final JCheckBox guiOutputGrafic = mainWindow.getGuiOutputGrafic();
        final JCheckBoxMenuItem menuXml = mainWindow.getMenuXml();
        final JCheckBox guiOutputXml = mainWindow.getGuiOutputXml();
        final JCheckBoxMenuItem menuConfiguration = mainWindow.getMenuConfiguration();
        final JCheckBox guiOutputConfiguration = mainWindow.getGuiOutputConfiguration();
        final JCheckBoxMenuItem menuBuildingInstruction = mainWindow.getMenuBuildingInstruction();
        final JCheckBox guiOutputBuildingInstruction = mainWindow.getGuiOutputBuildingInstruction();
        final JCheckBoxMenuItem menuMaterial = mainWindow.getMenuMaterial();
        final JCheckBox guiOutputMaterial = mainWindow.getGuiOutputMaterial();

        if (reset) {
            menuGrafic.setSelected(false);
            guiOutputGrafic.setSelected(false);
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
                case 11:
                    if (menuGrafic.isSelected()) {
                        guiOutputGrafic.setSelected(true);
                    } else {
                        guiOutputGrafic.setSelected(false);
                    }
                    break;
                case 13:
                    if (menuXml.isSelected()) {
                        guiOutputXml.setSelected(true);
                    } else {
                        guiOutputXml.setSelected(false);
                    }
                    break;
                case 14:
                    if (menuBuildingInstruction.isSelected()) {
                        guiOutputBuildingInstruction.setSelected(true);
                    } else {
                        guiOutputBuildingInstruction.setSelected(false);
                    }
                    break;
                case 15:
                    if (menuMaterial.isSelected()) {
                        guiOutputMaterial.setSelected(true);
                    } else {
                        guiOutputMaterial.setSelected(false);
                    }
                    break;
                case 16:
                    if (menuConfiguration.isSelected()) {
                        guiOutputConfiguration.setSelected(true);
                    } else {
                        guiOutputConfiguration.setSelected(false);
                    }
                    break;
                case 21:
                    if (guiOutputGrafic.isSelected()) {
                        menuGrafic.setSelected(true);
                    } else {
                        menuGrafic.setSelected(false);
                    }
                    break;
                case 23:
                    if (guiOutputXml.isSelected()) {
                        menuXml.setSelected(true);
                    } else {
                        menuXml.setSelected(false);
                    }
                    break;
                case 24:
                    if (guiOutputBuildingInstruction.isSelected()) {
                        menuBuildingInstruction.setSelected(true);
                    } else {
                        menuBuildingInstruction.setSelected(false);
                    }
                    break;
                case 25:
                    if (guiOutputMaterial.isSelected()) {
                        menuMaterial.setSelected(true);
                    } else {
                        menuMaterial.setSelected(false);
                    }
                    break;
                case 26:
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
     * @param actionCommand the action command set by toggling the radio button or
     *                      corresponding menu item.
     */
    public void synchronizeRadioButtonsAndMenuItems(final String actionCommand) {
        if (actionCommand.contains("11")) {
            mainWindow.radioButtonStatus(1, 1);
        } else if (actionCommand.contains("12")) {
            mainWindow.radioButtonStatus(1, 2);
        } else if (actionCommand.contains("13")) {
            mainWindow.radioButtonStatus(1, 3);
        } else if (actionCommand.contains("14")) {
            mainWindow.radioButtonStatus(1, 4);
        } else if (actionCommand.contains("15")) {
            mainWindow.radioButtonStatus(1, 5);
        } else if (actionCommand.contains("16")) {
            mainWindow.radioButtonStatus(1, 6);
        } else if (actionCommand.contains("17")) {
            mainWindow.radioButtonStatus(1, 7);
        } else if (actionCommand.contains("21")) {
            mainWindow.radioButtonStatus(2, 1);
        } else if (actionCommand.contains("22")) {
            mainWindow.radioButtonStatus(2, 2);
        } else if (actionCommand.contains("23")) {
            mainWindow.radioButtonStatus(2, 3);
        } else if (actionCommand.contains("24")) {
            mainWindow.radioButtonStatus(2, 4);
        } else if (actionCommand.contains("25")) {
            mainWindow.radioButtonStatus(2, 5);
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
        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_4") + "!");
    }

    /**
     * Generate a mosaic.
     */
    public void generateMosaic() {
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();
        final JComboBox<String> guiComboBoxInterpolation = mainWindow.getGuiComboBoxInterpolation();
        final ButtonGroup guiGroupQuantisation = mainWindow.getGuiGroupQuantisation();
        final ButtonGroup guiGroupTiling = mainWindow.getGuiGroupTiling();
        dataProcessing.initInfo();
        dataProcessing.setInterpolation(guiComboBoxInterpolation.getSelectedIndex() + 1);
        final int quantisation = Integer.parseInt(
                guiGroupQuantisation.getSelection().getActionCommand().substring(9, 11)) - 10;
        final int tiling = Integer.parseInt(guiGroupTiling.getSelection().getActionCommand().substring(9, 11))
                - 20;

        if (tiling == 2 && !(dataProcessing.getCurrentConfiguration().getMaterial() == 3)) {
            mainWindow.errorDialog(MainWindow.textbundle.getString("output_mainWindow_6"));
        } else if (tiling == 4 && ((dataProcessing.getCurrentConfiguration().getMaterial() == 3)
                || (dataProcessing.getCurrentConfiguration().getMaterial() == 1))) {
            mainWindow.errorDialog(MainWindow.textbundle.getString("output_mainWindow_7"));
        } else if (tiling == 3 && (dataProcessing.getCurrentConfiguration().getMaterial() == 3)) {
            mainWindow.errorDialog(MainWindow.textbundle.getString("output_mainWindow_8"));
        } else {
            final int mosaicWidth = mainWindow.getMosaicWidth();
            final int mosaicHeight = mainWindow.getMosaicHeight();
            final JCheckBox guiThreeDEffect = mainWindow.getGuiThreeDEffect();
            final JCheckBox guiStatistic = mainWindow.getGuiStatistic();
            mainWindow.adjustDividerLocation();
            getGuiStatusHandler().guiStatus(GuiStatusHandler.DISABLE_GUI_WHILE_GENERATE_MOSAIC);
            dataProcessing.generateMosaic(mosaicWidth, mosaicHeight, quantisation, tiling,
                    guiThreeDEffect.isSelected(), guiStatistic.isSelected());
        }
    }

    /**
     * Process load image request.
     */
    public void loadImage() {
        mainWindow.adjustDividerLocation();
        mainWindow.imageLoad();
        checkIfImageAndConfigLoaded();
    }

    /**
     * Process load configuration request.
     */
    public void loadConfiguration() {
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();

        if (dataProcessing.getWorkingDirectory() == null) {
            mainWindow.errorDialog(MainWindow.textbundle.getString("output_mainWindow_9"));
        } else {
            mainWindow.configurationLoad();
        }

        checkIfImageAndConfigLoaded();
    }

    /**
     * Display the dialog to set the working directory.
     */
    public void setWorkingDirectory() {
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();
        WorkingDirectoryDialog workingDirectoryDialog = new WorkingDirectoryDialog(mainWindow,
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
        MosaicSizeDialog mosaicDimensionDialog = new MosaicSizeDialog(mainWindow,
                dataProcessing.getCurrentConfiguration());
        final PictureElement guiPictureElementTop = mainWindow.getGuiPictureElementTop();

        if (!mosaicDimensionDialog.isCanceled()) {
            guiPictureElementTop.removeMouseListener(guiPictureElementTop);
            guiPictureElementTop.removeMouseMotionListener(guiPictureElementTop);
            guiPictureElementTop.setCutoutRatio(new Dimension(
                    mosaicDimensionDialog.getArea().width * dataProcessing.getConfigurationDimension().width,
                    mosaicDimensionDialog.getArea().height * dataProcessing.getConfigurationDimension().height));
            guiPictureElementTop.addMouseListener(guiPictureElementTop);
            guiPictureElementTop.addMouseMotionListener(guiPictureElementTop);
            guiPictureElementTop.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            final int mosaicWidth = mosaicDimensionDialog.getArea().width;
            mainWindow.setMosaicWidth(mosaicWidth);
            final int mosaicHeight = mosaicDimensionDialog.getArea().height;
            mainWindow.setMosaicHeight(mosaicHeight);
            mainWindow.showDimensionInfo(mosaicWidth, mosaicHeight, false);
            getGuiStatusHandler().guiStatus(GuiStatusHandler.CUTOUT);
        }

        mosaicDimensionDialog = null;
    }

    /**
     * Process generate documents request.
     */
    public void generateDocument() {
        final JCheckBox guiOutputGrafic = mainWindow.getGuiOutputGrafic();
        final JCheckBox guiOutputConfiguration = mainWindow.getGuiOutputConfiguration();
        final JCheckBox guiOutputMaterial = mainWindow.getGuiOutputMaterial();
        final JCheckBox guiOutputBuildingInstruction = mainWindow.getGuiOutputBuildingInstruction();
        final JCheckBox guiOutputXml = mainWindow.getGuiOutputXml();

        if (guiOutputGrafic.isSelected() ||
                guiOutputConfiguration.isSelected() ||
                guiOutputMaterial.isSelected() ||
                guiOutputBuildingInstruction.isSelected() ||
                guiOutputXml.isSelected()) {
            getGuiStatusHandler().guiStatus(GuiStatusHandler.DISABLE_GUI_WHILE_GENERATING_OUTPUT);
            mainWindow.refreshProgressBarOutputFiles(0, 1);
            mainWindow.refreshProgressBarOutputFiles(0, 2);
            mainWindow.refreshProgressBarOutputFiles(0, 3);
            mainWindow.refreshProgressBarOutputFiles(0, 4);
            mainWindow.refreshProgressBarOutputFiles(0, 5);
            mainWindow.refreshProgressBarOutputFiles(0, 6);
            mainWindow.setStatusProgressBarOutputFiles(guiOutputGrafic.isSelected(),
                    guiOutputConfiguration.isSelected(),
                    guiOutputMaterial.isSelected(),
                    guiOutputBuildingInstruction.isSelected(),
                    guiOutputXml.isSelected(),
                    true);
            mainWindow.showProgressBarOutputFiles();

            // SwingWorker
            // "construct": all commands are startet in a new thread
            // "finished": all commands are queued to the gui thread
            // after finshing aforesaid (construct-)thread
            final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    final DataProcessor dataProcessing = mainWindow.getDataProcessing();
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
                        mainWindow.showInfo(MainWindow.textbundle.getString("output_mainWindow_10"));
                    }

                    return true;
                }

                public void finished() {
                    getGuiStatusHandler().guiStatus(GuiStatusHandler.ENABLE_GUI_AFTER_GENERATING_OUTPUT);
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
            getGuiStatusHandler().guiStatus(GuiStatusHandler.IMAGE_AND_CONFIG_LOADED);
        }
    }
}
