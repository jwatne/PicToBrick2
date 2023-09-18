package pictobrick.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import pictobrick.model.ColorObject;
import pictobrick.service.DataProcessor;
import pictobrick.ui.handlers.GuiStatusHandler;
import pictobrick.ui.handlers.MainWindowActionHandlers;
import pictobrick.ui.menus.FileMenu;
import pictobrick.ui.menus.MosaicMenu;
import pictobrick.ui.menus.OutputMenu;
import pictobrick.ui.menus.PreprocessingMenu;
import pictobrick.ui.panels.OptionsPanel1;
import pictobrick.ui.panels.OptionsPanel2;
import pictobrick.ui.panels.OptionsPanel3;
import pictobrick.ui.panels.ZoomPanel;

/**
 * MainWindow class in GUI layer (three tier architecture). layer: Gui (three
 * tier architecture) description: MainWindow
 *
 * @author Tobias Reichling
 */
public class MainWindow extends JFrame
        implements ActionListener, ChangeListener {
    /** Minimum available memory for program to run, in MB. */
    private static final int MIN_AVAILABLE_MEMORY_MB = 250;
    /** Bytes per KB. */
    private static final int ONE_K = 1024;
    /** Minimum slider value. */
    private static final int SLIDER1_MIN_VALUE = 3;
    /**
     * Resource bundle.
     */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");

    /**
     * Returns the text resource bundle.
     *
     * @return the text resource bundle.
     */
    public static ResourceBundle getTextBundle() {
        return textbundle;
    }

    // ----------------------------------------------------------------
    /**
     * Data processor.
     */
    private final DataProcessor dataProcessing;
    // ----------------------------------------------------------------
    // dialogs
    /**
     * Progress bars for algorithms.
     */
    private final ProgressBarsAlgorithms progressBarsAlgorithm;
    /**
     * Progress bars for output files.
     */
    private final ProgressBarsOutputFiles progressBarsOutputFiles;
    /**
     * Mosaic width in pixels.
     */
    private int mosaicWidth;
    /**
     * Mosaic height in pixels.
     */
    private int mosaicHeight;
    // ---------------------------------------------------------------
    // menu
    /**
     * Menu bar.
     */
    private JMenuBar menuBar;
    /**
     * File menu.
     */
    private FileMenu menuFile;

    /**
     * Returns file menu.
     *
     * @return file menu.
     */
    public final FileMenu getMenuFile() {
        return menuFile;
    }

    /**
     * Preprocessing menu.
     */
    private PreprocessingMenu menuPreprocessing;

    /**
     * Returns preprocessing menu.
     *
     * @return preprocessing menu.
     */
    public final PreprocessingMenu getMenuPreprocessing() {
        return menuPreprocessing;
    }

    /**
     * Mosaic menu.
     */
    private MosaicMenu menuMosaic;

    /**
     * Returns mosaic menu.
     *
     * @return mosaic menu.
     */
    public final MosaicMenu getMenuMosaic() {
        return menuMosaic;
    }

    /**
     * Output menu.
     */
    private OutputMenu menuOutput;

    /**
     * Return output menu.
     *
     * @return output menu.
     */
    public final OutputMenu getMenuOutput() {
        return menuOutput;
    }

    /**
     * Help menu.
     */
    private JMenu menuHelp;

    /**
     * About menu item.
     */
    private JMenuItem menuAbout;

    /**
     * Returns about menu item.
     *
     * @return about menu item.
     */
    public final JMenuItem getMenuAbout() {
        return menuAbout;
    }

    /**
     * Top area panel.
     */
    private JPanel guiPanelTopArea;
    /**
     * Bottom area panel.
     */
    private JPanel guiPanelBottomArea;

    /**
     * Information panel.
     */
    private JPanel guiPanelInformation;

    /** Zoom panel. */
    private ZoomPanel guiPanelZoom;

    /**
     * Returns room panel.
     *
     * @return room panel.
     */
    public final ZoomPanel getGuiPanelZoom() {
        return guiPanelZoom;
    }

    /** Panel for first set of options. */
    private OptionsPanel1 guiPanelOptions1;
    /** Panel for second set of options. */
    private OptionsPanel2 guiPanelOptions2;
    /** Panel for third set of options. */
    private OptionsPanel3 guiPanelOptions3;

    /**
     * Returns panel for first set of options.
     *
     * @return panel for first set of options.
     */
    public final OptionsPanel1 getGuiPanelOptions1() {
        return guiPanelOptions1;
    }

    /**
     * Returns panel for second set of options.
     *
     * @return panel for second set of options.
     */
    public final OptionsPanel2 getGuiPanelOptions2() {
        return guiPanelOptions2;
    }

    /**
     * Returns panel for third set of options.
     *
     * @return panel for third set of options.
     */
    public final OptionsPanel3 getGuiPanelOptions3() {
        return guiPanelOptions3;
    }

    /** Right area panel. */
    private JPanel guiPanelRightArea;

    /**
     * Returns right area panel.
     *
     * @return right area panel.
     */
    public final JPanel getGuiPanelRightArea() {
        return guiPanelRightArea;
    }

    /** Second top area panel. */
    private JPanel guiPanelTopArea2;
    /** Second bottom area panel. */
    private JPanel guiPanelBottomArea2;

    /**
     * Returns second top area panel.
     *
     * @return second top area panel.
     */
    public final JPanel getGuiPanelTopArea2() {
        return guiPanelTopArea2;
    }

    /**
     * Returns second bottom area panel.
     *
     * @return second bottom area panel.
     */
    public final JPanel getGuiPanelBottomArea2() {
        return guiPanelBottomArea2;
    }

    /** Top picture element. */
    private PictureElement guiPictureElementTop;
    /** Bottom picture element. */
    private PictureElement guiPictureElementBottom;

    /**
     * Returns bottom picture element.
     *
     * @return bottom picture element.
     */
    public final PictureElement getGuiPictureElementBottom() {
        return guiPictureElementBottom;
    }

    /** Split pane. */
    private JSplitPane guiSplitPane;
    /** Top scroll pane. */
    private JScrollPane guiScrollPaneTop;
    /** Bottom scroll pane. */
    private JScrollPane guiScrollPaneBottom;

    /**
     * Returns top scroll pane.
     *
     * @return top scroll pane.
     */
    public final JScrollPane getGuiScrollPaneTop() {
        return guiScrollPaneTop;
    }

    /**
     * Returns bottom scroll pane.
     *
     * @return bottom scroll pane.
     */
    public final JScrollPane getGuiScrollPaneBottom() {
        return guiScrollPaneBottom;
    }

    /** Information text field. */
    private JTextField guiTextFieldInformation;
    /** Action handlers for main window. */
    private final MainWindowActionHandlers mainWindowActionHandlers;

    /**
     * Returns action handlers for main window.
     *
     * @return action handlers for main window.
     */
    public final MainWindowActionHandlers getMainWindowActionHandlers() {
        return mainWindowActionHandlers;
    }

    /** GUI status handler. */
    private GuiStatusHandler guiStatusHandler;

    /**
     * Returns GUI status handler.
     *
     * @return GUI status handler.
     */
    public final GuiStatusHandler getGuiStatusHandler() {
        return guiStatusHandler;
    }

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     */
    public MainWindow() {
        super("pictobrick");
        addWindowListener(new WindowClosingAdapter(true));
        dataProcessing = new DataProcessor(this);
        progressBarsAlgorithm = new ProgressBarsAlgorithms(this);
        progressBarsOutputFiles = new ProgressBarsOutputFiles(this);
        mainWindowActionHandlers = new MainWindowActionHandlers(this);
        guiStatusHandler = new GuiStatusHandler(this);
        buildMenu();
        buildGui();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Check if the minimum of 256MB memory are available
        if (((Runtime.getRuntime().maxMemory() / ONE_K)
                / ONE_K) < MIN_AVAILABLE_MEMORY_MB) {
            errorDialog(textbundle.getString("dialog_mainWindow_error_1")
                    + "\r\n" + textbundle.getString("dialog_mainWindow_error_2")
                    + "\r\n"
                    + textbundle.getString("dialog_mainWindow_error_3"));
        }

        workingDirectory(false);
    }

    /**
     * Returns data processor.
     *
     * @return data processor.
     */
    public final DataProcessor getDataProcessing() {
        return dataProcessing;
    }

    /**
     * Returns mosaic width in pixels.
     *
     * @return mosaic width in pixels.
     */
    public final int getMosaicWidth() {
        return mosaicWidth;
    }

    /**
     * Sets mosaic width in pixels.
     *
     * @param width mosaic width in pixels.
     */
    public final void setMosaicWidth(final int width) {
        this.mosaicWidth = width;
    }

    /**
     * Returns mosaic height in pixels.
     *
     * @return mosaic height in pixels.
     */
    public final int getMosaicHeight() {
        return mosaicHeight;
    }

    /**
     * Sets mosaic height in pixels.
     *
     * @param height mosaic height in pixels.
     */
    public final void setMosaicHeight(final int height) {
        this.mosaicHeight = height;
    }

    /**
     * Returns top picture element.
     *
     * @return top picture element.
     */
    public final PictureElement getGuiPictureElementTop() {
        return guiPictureElementTop;
    }

    /**
     * Sets top picture element.
     *
     * @param element top picture element.
     */
    public final void setGuiPictureElementTop(final PictureElement element) {
        this.guiPictureElementTop = element;
    }

    /**
     * Dialog for choosing workingDirectory.
     *
     * @param newDirectory <code>true</code> if the directory is newly
     *                     specified.
     *
     * @author Tobias Reichling
     */
    public void workingDirectory(final boolean newDirectory) {
        File wdFile = null;

        if (!newDirectory) {
            // Load working directory information from filesystem (if available)
            wdFile = dataProcessing.loadWorkingDirectory();
        }

        if (wdFile == null || !(wdFile.isDirectory())) {
            // Working Directory dialog
            final JFileChooser d = new JFileChooser();
            d.setDialogTitle(textbundle.getString("output_mainWindow_1"));
            d.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            d.setFileFilter(new FileFilter() {
                public boolean accept(final File f) {
                    return f.isDirectory();
                }

                public String getDescription() {
                    return textbundle.getString("output_mainWindow_2");
                }
            });

            d.showOpenDialog(this);
            wdFile = d.getSelectedFile();
        }

        if (wdFile != null) {
            dataProcessing.setWorkingDirectory(wdFile);
            showInfo(textbundle.getString("output_mainWindow_3") + ": " + wdFile
                    + ".    " + textbundle.getString("output_mainWindow_4")
                    + "!");
            // Save information about current working directory
            dataProcessing.saveWorkingDirectory(wdFile);
        } else {
            errorDialog(textbundle.getString("output_mainWindow_5") + "!");
        }
    }

    /**
     * Refreshes the Progress Bars.
     *
     * @author Tobias Reichling
     * @param value
     * @param number of progressBar
     */
    public void refreshProgressBarAlgorithm(final int value, final int number) {
        progressBarsAlgorithm.showValue(value, number);
    }

    /**
     * Hide dialog.
     *
     * @author Tobias Reichling
     */
    public void hideProgressBarAlgorithm() {
        progressBarsAlgorithm.hideDialog();
    }

    /**
     * Show dialog.
     *
     * @author Tobias Reichling
     */
    public void showProgressBarAlgorithm() {
        progressBarsAlgorithm.showDialog();
    }

    /**
     * Sets status.
     *
     * @author Tobias Reichling
     * @param graphic <code>true</code> if progress bar is shown.
     */
    public void setStatusProgressBarOutputFiles(final boolean graphic,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final boolean miscellaneous) {
        progressBarsOutputFiles.setStatus(graphic, configuration, material,
                instruction, xml, miscellaneous);
    }

    /**
     * method: setStatusProgressBarAlgorithm description: sets status to
     * progressBar statistic
     *
     * @author Tobias Reichling
     * @param true/false
     */
    public void setStatusProgressBarAlgorithm(final boolean active) {
        progressBarsAlgorithm.setStatus(active);
    }

    /**
     * method: refreshProgressBarOutputFiles description: refreshes progressBars
     *
     * @author Tobias Reichling
     * @param value
     * @param number of progressBar
     */
    public void refreshProgressBarOutputFiles(final int value,
            final int number) {
        progressBarsOutputFiles.showValue(value, number);
    }

    /**
     * method: hideProgressBarOutputFiles description: hide dialog
     *
     * @author Tobias Reichling
     */
    public void hideProgressBarOutputFiles() {
        progressBarsOutputFiles.hideDialog();
    }

    /**
     * method: showProgressBarOutputFiles description: show dialog
     *
     * @author Tobias Reichling
     */
    public void showProgressBarOutputFiles() {
        progressBarsOutputFiles.showDialog();
    }

    /**
     * method: animateGraficProgressBarOutputFiles description: animate
     * progressBar grafic while saving
     *
     * @author Tobias Reichling
     * @param true/false
     */
    public void animateGraficProgressBarOutputFiles(final boolean active) {
        progressBarsOutputFiles.animateGraphic(active);
    }

    /**
     * method: stateChanged description: ChangeListener (zoom slider)
     *
     * @author Tobias Reichling
     * @param event
     */
    public void stateChanged(final ChangeEvent event) {
        final JSlider guiZoomSlider1 = guiPanelZoom.getGuiZoomSlider1();
        final int guiZoomSlider1Value = guiPanelZoom.getGuiZoomSlider1Value();
        final int guiZoomSlider2Value = guiPanelZoom.getGuiZoomSlider2Value();
        final JSlider guiZoomSlider2 = guiPanelZoom.getGuiZoomSlider2();

        if ((JSlider) event.getSource() == guiZoomSlider1) {
            if (guiZoomSlider1Value != guiZoomSlider1.getValue()) {
                guiPanelZoom.setGuiZoomSlider1Value(guiZoomSlider1.getValue());
                guiPictureElementTop.setImage(dataProcessing
                        .getScaledImage(false, guiZoomSlider1.getValue()));
                guiPictureElementTop.updateUI();
            }
        } else {
            if (guiZoomSlider2Value != guiZoomSlider2.getValue()) {
                guiPanelZoom.setGuiZoomSlider2Value(guiZoomSlider2.getValue());
                guiPictureElementBottom.setImage(dataProcessing
                        .getScaledImage(true, guiZoomSlider2.getValue()));
                guiPictureElementBottom.updateUI();
            }
        }
    }

    /**
     * Method: actionPerformed.
     * <p>
     * description: ActionListener
     *
     * @author Tobias Reichling
     * @param event the ActionEvent to process.
     */
    public void actionPerformed(final ActionEvent event) {
        final String actionCommand = event.getActionCommand();

        if (actionCommand.contains("menu")) {
            // Synchronize check boxes (gui) and menu items (menu)
            mainWindowActionHandlers.setCheckboxGuiToMenuValue(actionCommand);
            // Synchronize check boxes (gui) and menu items (menu)
        } else if (actionCommand.contains("gui")) {
            mainWindowActionHandlers.setCheckboxMenuToGuiValue(actionCommand);
            // Synchronize radio buttons (gui) and menu items (menu)
        } else if (actionCommand.contains("algorithm")) {
            mainWindowActionHandlers
                    .synchronizeRadioButtonsAndMenuItems(actionCommand);
            // buttons
        } else if (actionCommand.contains("cutout")) {
            cutout();
        } else if (actionCommand.contains("output")) {
            mainWindowActionHandlers.outputAction();
        } else if (actionCommand.contains("mosaicnew")) {
            mainWindowActionHandlers.startNewMosaic();
        } else if (actionCommand.contains("mosaicgenerate")) {
            mainWindowActionHandlers.generateMosaic();
        } else if (actionCommand.contains("imageload")) {
            mainWindowActionHandlers.loadImage();
        } else if (actionCommand.contains("configurationload")) {
            mainWindowActionHandlers.loadConfiguration();
        } else if (actionCommand.contains("settings")) {
            mainWindowActionHandlers.setWorkingDirectory();
        } else if (actionCommand.contains("exit")) {
            this.setVisible(false);
            this.dispose();
            System.exit(0);
        } else if (actionCommand.contains("mosaicdimension")) {
            mainWindowActionHandlers.setMosaicDimensions();
        } else if (actionCommand.contains("documentgenerate")) {
            mainWindowActionHandlers.generateDocument();
        } else if (actionCommand.contains("about")) {
            final AboutDialog aboutDialog = new AboutDialog(this);
            aboutDialog.setVisible(true);
        }
    }

    /**
     * method: cutout description: cutout the user defined rectangle button:
     * cutout or double click within the rectangle
     *
     * @author Tobias Reichling
     */
    public void cutout() {
        if (guiPictureElementTop.isCutout()) {
            guiPictureElementTop.removeMouseListener(guiPictureElementTop);
            guiPictureElementTop
                    .removeMouseMotionListener(guiPictureElementTop);
            guiStatusHandler.guiStatus(GuiStatusHandler.GENERATE_MOSAIC);
            dataProcessing.replaceImageByCutout(
                    guiPictureElementTop.getCutoutRectangle());
            dataProcessing.computeScaleFactor(false,
                    (double) (guiScrollPaneTop.getWidth() - 40),
                    (double) ((guiSplitPane.getHeight()
                            - guiSplitPane.getDividerLocation() - 60)));
            final JSlider guiZoomSlider1 = guiPanelZoom.getGuiZoomSlider1();
            guiPictureElementTop.setImage(dataProcessing.getScaledImage(false,
                    guiZoomSlider1.getValue()));
            guiZoomSlider1.setValue(SLIDER1_MIN_VALUE);
            guiPanelZoom.setGuiZoomSlider1Value(3);
            guiPictureElementTop.updateUI();
        } else {
            errorDialog(textbundle.getString("output_mainWindow_11"));
        }
    }

    /**
     * method: showMosaic description: shows the mosaic (started in tiling
     * thread)
     *
     * @author Tobias Reichling
     */
    public void showMosaic() {
        dataProcessing.computeScaleFactor(false,
                (double) (guiScrollPaneTop.getWidth() - 40),
                (double) ((guiSplitPane.getHeight()
                        - guiSplitPane.getDividerLocation() - 60)));
        guiPictureElementTop.setImage(dataProcessing.getScaledImage(false,
                guiPanelZoom.getGuiZoomSlider1().getValue()));
        dataProcessing.computeScaleFactor(true,
                (double) (guiScrollPaneBottom.getWidth() - 40),
                (double) ((guiSplitPane.getHeight()
                        - guiSplitPane.getDividerLocation() - 60)));
        guiPictureElementBottom.setImage(dataProcessing.getScaledImage(true,
                guiPanelZoom.getGuiZoomSlider2().getValue()));
        guiStatusHandler
                .guiStatus(GuiStatusHandler.ENABLE_GUI_AFTER_GENERATE_MOSAIC);
        guiPictureElementBottom.updateUI();

        if (guiPanelOptions2.getGuiStatistic().isSelected()) {
            final Enumeration<String> statisticInformation = dataProcessing
                    .getInfo(1);
            String statisticInformationString = "";

            while (statisticInformation.hasMoreElements()) {
                statisticInformationString = statisticInformationString
                        + (String) statisticInformation.nextElement() + "\n\r";
            }

            JOptionPane.showMessageDialog(this, statisticInformationString,
                    textbundle.getString("output_mainWindow_12"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * method: dialogFloydSteinberg description: dialog for choosing colors and
     * threshold
     *
     * @author Tobias Reichling
     * @param possible colors
     * @return vector (colors and threshold)
     */
    public Vector<Object> dialogFloydSteinberg(
            final Enumeration<ColorObject> colors) {
        final Vector<Object> result = new Vector<>();
        FloydSteinbergColorDialog floydSteinbergColorDialog = new FloydSteinbergColorDialog(
                this, colors);
        result.add(floydSteinbergColorDialog.getDark());
        result.add(floydSteinbergColorDialog.getLight());
        result.add(floydSteinbergColorDialog.getMethod());
        floydSteinbergColorDialog = null;
        return result;
    }

    /**
     * method: dialogPatternDithering description: dialog for setting maximum
     * luminance value distance
     *
     * @author Tobias Reichling
     * @return vector result
     */
    public Vector<Object> dialogPatternDithering() {
        final Vector<Object> result = new Vector<>();
        PatternDitheringDialog patternDitheringDialog = new PatternDitheringDialog(
                this);
        result.add(patternDitheringDialog.getDistance());
        patternDitheringDialog = null;
        return result;
    }

    /**
     * method: dialogSlicingColor description: dialog for choosing color
     * quantity for slicing
     *
     * @author Tobias Reichling
     * @return color quantity
     */
    public int dialogSlicingColor() {
        final int min = 1;
        final int max = 8;
        final SlicingColorNumberDialog slicingColorNumberDialog = new SlicingColorNumberDialog(
                this, min, max);
        return slicingColorNumberDialog.getQuantity();
    }

    /**
     * method: dialogSlicingThreshold description: dialog for choosing
     * thresholds for slicing
     *
     * @author Tobias Reichling
     * @param colorQuantity
     * @param colors        (Enumeration)
     * @return thresholds (vector)
     */
    public Vector<Object> dialogSlicingThreshold(final int colorQuantity,
            final Enumeration<ColorObject> colors) {
        final SlicingThresholdDialog slicingThresholdDialog = new SlicingThresholdDialog(
                this, colorQuantity, colors);
        return slicingThresholdDialog.getSelection();
    }

    /**
     * method: dialogMoldingOptimisation description: dialog for choosing
     * additinal optimisation methods
     *
     * @author Tobias Reichling
     * @param quantisation (string)
     * @return method (vector)
     */
    public Vector<Object> dialogMoldingOptimisation(final int method,
            final String quantisation) {
        final MoldingOptimisationDialog moldingOptimisationDialog = new MoldingOptimisationDialog(
                this, method, quantisation);
        return moldingOptimisationDialog.getMethod();
    }

    /**
     * method: dialogStabilityOptimisation description: dialog for choosing
     * additinal optimisation methods
     *
     * @author Tobias Reichling
     * @return Vector: optimisation y/n; border/complete; maximum gap height
     */
    public Vector<Object> dialogStabilityOptimisation() {
        final StabilityOptimisationDialog stabilityOptimisationDialog = new StabilityOptimisationDialog(
                this);
        return stabilityOptimisationDialog.getMethod();
    }

    /**
     * method: imageLoad description: system dialog for image loading
     *
     * @author Tobias Reichling
     * @exception IOExcepion
     */
    public void imageLoad() {
        // system dialog
        final JFileChooser d = new JFileChooser();
        d.setFileFilter(new FileFilter() {
            public boolean accept(final File f) {
                return f.isDirectory()
                        || f.getName().toLowerCase().endsWith(".jpg")
                        || f.getName().toLowerCase().endsWith(".jpeg")
                        || f.getName().toLowerCase().endsWith(".gif")
                        || f.getName().toLowerCase().endsWith(".png");
            }

            public String getDescription() {
                return "*.jpg;*.gif;*.png";
            }
        });

        d.showOpenDialog(this);
        final File menuFile = d.getSelectedFile();

        if (dataProcessing.imageLoad(menuFile)) {
            final JSlider guiZoomSlider1 = guiPanelZoom.getGuiZoomSlider1();
            guiZoomSlider1.setEnabled(true);
            dataProcessing.computeScaleFactor(false,
                    (double) (guiScrollPaneTop.getWidth() - 40),
                    (double) ((guiSplitPane.getHeight()
                            - guiSplitPane.getDividerLocation() - 60)));
            guiZoomSlider1.setValue(3);
            guiPictureElementTop.setImage(dataProcessing.getScaledImage(false,
                    guiZoomSlider1.getValue()));
            guiPictureElementTop.updateUI();
            getGuiPanelOptions1().showImageInfo(menuFile.getName());
            showInfo(textbundle.getString("output_mainWindow_20") + " "
                    + menuFile.getName() + " "
                    + textbundle.getString("output_mainWindow_18") + ".");
        } else {
            errorDialog(textbundle.getString("output_mainWindow_21"));
        }
    }

    /**
     * method: errorDialog description: shows an error dialog
     *
     * @author Tobias Reichling
     * @param errorMessage
     */
    public void errorDialog(final String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage,
                textbundle.getString("dialog_error_frame"),
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * method: radioButtonStatus description: Synchronize radio buttons gui and
     * menu
     *
     * @author Tobias Reichling
     * @param groupNumber
     * @param buttonNumber
     */
    public void radioButtonStatus(final int groupNumber,
            final int buttonNumber) {
        menuMosaic.radioButtonStatus(groupNumber, buttonNumber);
        guiPanelOptions2.radioButtonStatus(groupNumber, buttonNumber);

    }

    /**
     * method: adjustDividerLocation description: adjust divider location (split
     * pane)
     *
     * @author Tobias Reichling
     */
    public void adjustDividerLocation() {
        guiSplitPane.setDividerLocation(guiSplitPane.getHeight() / 2);
    }

    /**
     * method: showInfo description: shows an information text
     *
     * @author Tobias Reichling
     * @param text
     */
    public void showInfo(final String text) {
        guiTextFieldInformation.setText(text);
    }

    /**
     * method: buildGui description: builds gui
     *
     * @author Tobias Reichling
     */
    private void buildGui() {
        final Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        guiPanelTopArea = new JPanel(new BorderLayout());
        guiPanelBottomArea = new JPanel(new BorderLayout());
        contentPane.add(guiPanelTopArea, BorderLayout.CENTER);
        contentPane.add(guiPanelBottomArea, BorderLayout.SOUTH);
        guiPanelRightArea = new JPanel(new BorderLayout());
        guiPanelTopArea.add(guiPanelRightArea, BorderLayout.EAST);
        // image area top
        guiPictureElementTop = new PictureElement(this);
        final GridBagLayout top2gbl = new GridBagLayout();
        final GridBagConstraints top2gbc = new GridBagConstraints();
        top2gbc.anchor = GridBagConstraints.CENTER;
        top2gbl.setConstraints(guiPictureElementTop, top2gbc);
        guiPanelTopArea2 = new JPanel(top2gbl);
        guiPanelTopArea2.add(guiPictureElementTop);
        guiScrollPaneTop = new JScrollPane(guiPanelTopArea2,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        guiScrollPaneTop.setMinimumSize(new Dimension(0, 0));
        guiScrollPaneTop.setPreferredSize(new Dimension(100, 100));
        // image area bottom
        guiPictureElementBottom = new PictureElement(this);
        final GridBagLayout bottom2gbl = new GridBagLayout();
        final GridBagConstraints bottom2gbc = new GridBagConstraints();
        bottom2gbc.anchor = GridBagConstraints.CENTER;
        bottom2gbl.setConstraints(guiPictureElementBottom, bottom2gbc);
        guiPanelBottomArea2 = new JPanel(bottom2gbl);
        guiPanelBottomArea2.add(guiPictureElementBottom);
        guiScrollPaneBottom = new JScrollPane(guiPanelBottomArea2,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        guiScrollPaneBottom.setMinimumSize(new Dimension(0, 0));
        guiScrollPaneBottom.setPreferredSize(new Dimension(100, 100));
        // split pane
        guiSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        guiSplitPane.setDividerLocation(0);
        guiSplitPane.setContinuousLayout(true);
        final TitledBorder imageAreaBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_mainWindow_border_1"));
        guiSplitPane.setBorder(imageAreaBorder);
        imageAreaBorder.setTitleColor(new Color(100, 100, 100));
        guiSplitPane.setTopComponent(guiScrollPaneTop);
        guiSplitPane.setBottomComponent(guiScrollPaneBottom);
        guiPanelTopArea.add(guiSplitPane, BorderLayout.CENTER);
        // option panels all
        final TitledBorder optionAreaBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_mainWindow_border_2"));
        optionAreaBorder.setTitleColor(new Color(100, 100, 100));
        // option panel 1
        guiPanelOptions1 = new OptionsPanel1(new BorderLayout(), this);
        // option panel 2
        guiPanelOptions2 = new OptionsPanel2(new BorderLayout(), this);
        // option panel 3
        guiPanelOptions3 = new OptionsPanel3(new BorderLayout(), this);

        // zoom area
        guiPanelZoom = new ZoomPanel(new GridLayout(4, 1), this);

        // information area
        guiPanelInformation = new JPanel(new GridLayout(1, 1));
        guiTextFieldInformation = new JTextField();
        guiTextFieldInformation.setEditable(false);
        guiTextFieldInformation.setBackground(new Color(255, 255, 255));
        guiTextFieldInformation.setForeground(new Color(0, 0, 150));
        guiPanelBottomArea.add(guiPanelInformation, BorderLayout.CENTER);
        final TitledBorder informationAreaBorder = BorderFactory
                .createTitledBorder(
                        textbundle.getString("dialog_mainWindow_border_4"));
        informationAreaBorder.setTitleColor(new Color(100, 100, 100));
        guiPanelInformation.setBorder(informationAreaBorder);
        guiPanelInformation.add(guiTextFieldInformation);
        guiStatusHandler.guiStatus(GuiStatusHandler.GUI_START);
        pack();
    }

    /**
     * method: buildMenu description: generate Menu
     *
     * @author Tobias Reichling
     */
    private void buildMenu() {
        menuBar = new JMenuBar();
        // menuFile
        menuFile = new FileMenu(
                textbundle.getString("dialog_mainWindow_menu_10"), this);
        menuBar.add(menuFile);
        // menuPreprocessing
        menuPreprocessing = new PreprocessingMenu(
                textbundle.getString("dialog_mainWindow_menu_20"), this);
        menuBar.add(menuPreprocessing);
        // menuMosaic
        menuMosaic = new MosaicMenu(
                textbundle.getString("dialog_mainWindow_menu_30"), this);
        menuBar.add(menuMosaic);
        // menuOutput
        menuOutput = new OutputMenu(
                textbundle.getString("dialog_mainWindow_menu_40"), this);
        menuBar.add(menuOutput);
        // menuHelp
        menuHelp = new JMenu(textbundle.getString("dialog_mainWindow_menu_50"));
        menuAbout = new JMenuItem(
                textbundle.getString("dialog_mainWindow_menu_51"));
        menuAbout.addActionListener(this);
        menuAbout.setActionCommand("about");
        menuHelp.add(menuAbout);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }

    /**
     * method: main description: main method of pictobrick
     *
     * @author Tobias Reichling
     */
    public static void main(final String[] args) {
        final MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }
}
