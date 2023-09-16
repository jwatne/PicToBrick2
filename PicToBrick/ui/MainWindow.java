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
import java.io.IOException;
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
import pictobrick.model.Configuration;
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
 * class: MainWindow
 * layer: Gui (three tier architecture)
 * description: MainWindow
 *
 * @author Tobias Reichling
 */
public class MainWindow
		extends JFrame
		implements ActionListener, ChangeListener {
	private static final int SLIDER1_MIN_VALUE = 3;
	// resource bundle
	public static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	// --------------------------------------------------------------------------------------------------------
	// data processing
	private final DataProcessor dataProcessing;
	// --------------------------------------------------------------------------------------------------------
	// dialogs
	private final ProgressBarsAlgorithms progressBarsAlgorithm;
	private final ProgressBarsOutputFiles progressBarsOutputFiles;
	private int mosaicWidth;
	private int mosaicHeight;
	// --------------------------------------------------------------------------------------------------------
	// menu
	private JMenuBar menuBar;
	private FileMenu menuFile;

	public FileMenu getMenuFile() {
		return menuFile;
	}

	private PreprocessingMenu menuPreprocessing;

	public PreprocessingMenu getMenuPreprocessing() {
		return menuPreprocessing;
	}

	private MosaicMenu menuMosaic;

	public MosaicMenu getMenuMosaic() {
		return menuMosaic;
	}

	private OutputMenu menuOutput;

	public OutputMenu getMenuOutput() {
		return menuOutput;
	}

	private JMenu menuHelp;

	private JMenuItem menuAbout;

	public JMenuItem getMenuAbout() {
		return menuAbout;
	}

	private JPanel guiPanelTopArea;
	private JPanel guiPanelBottomArea;

	private JPanel guiPanelInformation;
	private ZoomPanel guiPanelZoom;

	public ZoomPanel getGuiPanelZoom() {
		return guiPanelZoom;
	}

	private OptionsPanel1 guiPanelOptions1;
	private OptionsPanel2 guiPanelOptions2;
	private OptionsPanel3 guiPanelOptions3;

	public OptionsPanel1 getGuiPanelOptions1() {
		return guiPanelOptions1;
	}

	public OptionsPanel2 getGuiPanelOptions2() {
		return guiPanelOptions2;
	}

	public OptionsPanel3 getGuiPanelOptions3() {
		return guiPanelOptions3;
	}

	private JPanel guiPanelRightArea;

	public JPanel getGuiPanelRightArea() {
		return guiPanelRightArea;
	}

	private JPanel guiPanelTopArea2;
	private JPanel guiPanelBottomArea2;

	public JPanel getGuiPanelTopArea2() {
		return guiPanelTopArea2;
	}

	public JPanel getGuiPanelBottomArea2() {
		return guiPanelBottomArea2;
	}

	private PictureElement guiPictureElementTop;
	private PictureElement guiPictureElementBottom;

	public PictureElement getGuiPictureElementBottom() {
		return guiPictureElementBottom;
	}

	private JSplitPane guiSplitPane;
	private JScrollPane guiScrollPaneTop;
	private JScrollPane guiScrollPaneBottom;

	public JScrollPane getGuiScrollPaneTop() {
		return guiScrollPaneTop;
	}

	public JScrollPane getGuiScrollPaneBottom() {
		return guiScrollPaneBottom;
	}

	private JTextField guiTextFieldInformation;

	private final MainWindowActionHandlers mainWindowActionHandlers;

	public MainWindowActionHandlers getMainWindowActionHandlers() {
		return mainWindowActionHandlers;
	}

	GuiStatusHandler guiStatusHandler;

	public GuiStatusHandler getGuiStatusHandler() {
		return guiStatusHandler;
	}

	/**
	 * method: MainWindow
	 * description: constructor
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
		if (((Runtime.getRuntime().maxMemory() / 1024) / 1024) < 250) {
			errorDialog(textbundle.getString("dialog_mainWindow_error_1") + "\r\n" +
					textbundle.getString("dialog_mainWindow_error_2") + "\r\n" +
					textbundle.getString("dialog_mainWindow_error_3"));
		}

		workingDirectory(false);
	}

	public DataProcessor getDataProcessing() {
		return dataProcessing;
	}

	public int getMosaicWidth() {
		return mosaicWidth;
	}

	public void setMosaicWidth(final int mosaicWidth) {
		this.mosaicWidth = mosaicWidth;
	}

	public int getMosaicHeight() {
		return mosaicHeight;
	}

	public void setMosaicHeight(final int mosaicHeight) {
		this.mosaicHeight = mosaicHeight;
	}

	public PictureElement getGuiPictureElementTop() {
		return guiPictureElementTop;
	}

	public void setGuiPictureElementTop(final PictureElement guiPictureElementTop) {
		this.guiPictureElementTop = guiPictureElementTop;
	}

	/**
	 * method: workingDirectory
	 * description: dialog for choosing workingDirectory
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
			showInfo(textbundle.getString("output_mainWindow_3") + ": " + wdFile + ".    "
					+ textbundle.getString("output_mainWindow_4") + "!");
			// Save information about current working directory
			dataProcessing.saveWorkingDirectory(wdFile);
		} else {
			errorDialog(textbundle.getString("output_mainWindow_5") + "!");
		}
	}

	/**
	 * method: refreshProgressBarAlgorithm
	 * description: refreshes the Progress Bars
	 *
	 * @author Tobias Reichling
	 * @param value
	 * @param number of progressBar
	 */
	public void refreshProgressBarAlgorithm(final int value, final int number) {
		progressBarsAlgorithm.showValue(value, number);
	}

	/**
	 * method: hideProgressBarAlgorithm
	 * description: hide dialog
	 *
	 * @author Tobias Reichling
	 */
	public void hideProgressBarAlgorithm() {
		progressBarsAlgorithm.hideDialog();
	}

	/**
	 * method: showProgressBarAlgorithm
	 * description: show dialog
	 *
	 * @author Tobias Reichling
	 */
	public void showProgressBarAlgorithm() {
		progressBarsAlgorithm.showDialog();
	}

	/**
	 * method: setStatusProgressBarOutputFiles
	 * description: sets status
	 *
	 * @author Tobias Reichling
	 * @param true/false for every progress bar
	 */
	public void setStatusProgressBarOutputFiles(final boolean grafic,
			final boolean konfi,
			final boolean material,
			final boolean anleit,
			final boolean xml,
			final boolean sonstiges) {
		progressBarsOutputFiles.setStatus(grafic,
				konfi,
				material,
				anleit,
				xml,
				sonstiges);
	}

	/**
	 * method: setStatusProgressBarAlgorithm
	 * description: sets status to progressBar statistic
	 *
	 * @author Tobias Reichling
	 * @param true/false
	 */
	public void setStatusProgressBarAlgorithm(final boolean active) {
		progressBarsAlgorithm.setStatus(active);
	}

	/**
	 * method: refreshProgressBarOutputFiles
	 * description: refreshes progressBars
	 *
	 * @author Tobias Reichling
	 * @param value
	 * @param number of progressBar
	 */
	public void refreshProgressBarOutputFiles(final int value, final int number) {
		progressBarsOutputFiles.showValue(value, number);
	}

	/**
	 * method: hideProgressBarOutputFiles
	 * description: hide dialog
	 *
	 * @author Tobias Reichling
	 */
	public void hideProgressBarOutputFiles() {
		progressBarsOutputFiles.hideDialog();
	}

	/**
	 * method: showProgressBarOutputFiles
	 * description: show dialog
	 *
	 * @author Tobias Reichling
	 */
	public void showProgressBarOutputFiles() {
		progressBarsOutputFiles.showDialog();
	}

	/**
	 * method: animateGraficProgressBarOutputFiles
	 * description: animate progressBar grafic while saving
	 *
	 * @author Tobias Reichling
	 * @param true/false
	 */
	public void animateGraficProgressBarOutputFiles(final boolean active) {
		progressBarsOutputFiles.animateGraphic(active);
	}

	/**
	 * method: stateChanged
	 * description: ChangeListener (zoom slider)
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
				guiPictureElementTop.setImage(dataProcessing.getScaledImage(false, guiZoomSlider1.getValue()));
				guiPictureElementTop.updateUI();
			}
		} else {
			if (guiZoomSlider2Value != guiZoomSlider2.getValue()) {
				guiPanelZoom.setGuiZoomSlider2Value(guiZoomSlider2.getValue());
				guiPictureElementBottom.setImage(dataProcessing.getScaledImage(true, guiZoomSlider2.getValue()));
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
			mainWindowActionHandlers.synchronizeRadioButtonsAndMenuItems(actionCommand);
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
	 * method: cutout
	 * description: cutout the user defined rectangle
	 * button: cutout or double click within the rectangle
	 *
	 * @author Tobias Reichling
	 */
	public void cutout() {
		if (guiPictureElementTop.isCutout()) {
			guiPictureElementTop.removeMouseListener(guiPictureElementTop);
			guiPictureElementTop.removeMouseMotionListener(guiPictureElementTop);
			guiStatusHandler.guiStatus(GuiStatusHandler.GENERATE_MOSAIC);
			dataProcessing.replaceImageByCutout(guiPictureElementTop.getCutoutRectangle());
			dataProcessing.computeScaleFactor(false, (double) (guiScrollPaneTop.getWidth() - 40),
					(double) ((guiSplitPane.getHeight() - guiSplitPane.getDividerLocation() - 60)));
			final JSlider guiZoomSlider1 = guiPanelZoom.getGuiZoomSlider1();
			guiPictureElementTop.setImage(dataProcessing.getScaledImage(false, guiZoomSlider1.getValue()));
			guiZoomSlider1.setValue(SLIDER1_MIN_VALUE);
			guiPanelZoom.setGuiZoomSlider1Value(3);
			guiPictureElementTop.updateUI();
		} else {
			errorDialog(textbundle.getString("output_mainWindow_11"));
		}
	}

	/**
	 * method: showMosaic
	 * description: shows the mosaic (started in tiling thread)
	 *
	 * @author Tobias Reichling
	 */
	public void showMosaic() {
		dataProcessing.computeScaleFactor(false, (double) (guiScrollPaneTop.getWidth() - 40),
				(double) ((guiSplitPane.getHeight() - guiSplitPane.getDividerLocation() - 60)));
		guiPictureElementTop
				.setImage(dataProcessing.getScaledImage(false, guiPanelZoom.getGuiZoomSlider1().getValue()));
		dataProcessing.computeScaleFactor(true, (double) (guiScrollPaneBottom.getWidth() - 40),
				(double) ((guiSplitPane.getHeight() - guiSplitPane.getDividerLocation() - 60)));
		guiPictureElementBottom
				.setImage(dataProcessing.getScaledImage(true, guiPanelZoom.getGuiZoomSlider2().getValue()));
		guiStatusHandler.guiStatus(GuiStatusHandler.ENABLE_GUI_AFTER_GENERATE_MOSAIC);
		guiPictureElementBottom.updateUI();

		if (guiPanelOptions2.getGuiStatistic().isSelected()) {
			final Enumeration<String> statisticInformation = dataProcessing.getInfo(1);
			String statisticInformationString = "";

			while (statisticInformation.hasMoreElements()) {
				statisticInformationString = statisticInformationString + (String) statisticInformation.nextElement()
						+ "\n\r";
			}

			JOptionPane.showMessageDialog(this, statisticInformationString,
					textbundle.getString("output_mainWindow_12"), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * method: dialogFloydSteinberg
	 * description: dialog for choosing colors and threshold
	 *
	 * @author Tobias Reichling
	 * @param possible colors
	 * @return vector (colors and threshold)
	 */
	public Vector<Object> dialogFloydSteinberg(final Enumeration<ColorObject> colors) {
		final Vector<Object> result = new Vector<>();
		FloydSteinbergColorDialog floydSteinbergColorDialog = new FloydSteinbergColorDialog(this, colors);
		result.add(floydSteinbergColorDialog.getDark());
		result.add(floydSteinbergColorDialog.getLight());
		result.add(floydSteinbergColorDialog.getMethod());
		floydSteinbergColorDialog = null;
		return result;
	}

	/**
	 * method: dialogPatternDithering
	 * description: dialog for setting maximum luminance value distance
	 *
	 * @author Tobias Reichling
	 * @return vector result
	 */
	public Vector<Object> dialogPatternDithering() {
		final Vector<Object> result = new Vector<>();
		PatternDitheringDialog patternDitheringDialog = new PatternDitheringDialog(this);
		result.add(patternDitheringDialog.getDistance());
		patternDitheringDialog = null;
		return result;
	}

	/**
	 * method: dialogSlicingColor
	 * description: dialog for choosing color quantity for slicing
	 *
	 * @author Tobias Reichling
	 * @return color quantity
	 */
	public int dialogSlicingColor() {
		final int min = 1;
		final int max = 8;
		final SlicingColorNumberDialog slicingColorNumberDialog = new SlicingColorNumberDialog(this, min, max);
		return slicingColorNumberDialog.getQuantity();
	}

	/**
	 * method: dialogSlicingThreshold
	 * description: dialog for choosing thresholds for slicing
	 *
	 * @author Tobias Reichling
	 * @param colorQuantity
	 * @param colors        (Enumeration)
	 * @return thresholds (vector)
	 */
	public Vector<Object> dialogSlicingThreshold(final int colorQuantity, final Enumeration<ColorObject> colors) {
		final SlicingThresholdDialog slicingThresholdDialog = new SlicingThresholdDialog(this, colorQuantity, colors);
		return slicingThresholdDialog.getSelection();
	}

	/**
	 * method: dialogMoldingOptimisation
	 * description: dialog for choosing additinal optimisation methods
	 *
	 * @author Tobias Reichling
	 * @param quantisation (string)
	 * @return method (vector)
	 */
	public Vector<Object> dialogMoldingOptimisation(final int method, final String quantisation) {
		final MoldingOptimisationDialog moldingOptimisationDialog = new MoldingOptimisationDialog(this, method,
				quantisation);
		return moldingOptimisationDialog.getMethod();
	}

	/**
	 * method: dialogStabilityOptimisation
	 * description: dialog for choosing additinal optimisation methods
	 *
	 * @author Tobias Reichling
	 * @return Vector: optimisation y/n; border/complete; maximum gap height
	 */
	public Vector<Object> dialogStabilityOptimisation() {
		final StabilityOptimisationDialog stabilityOptimisationDialog = new StabilityOptimisationDialog(this);
		return stabilityOptimisationDialog.getMethod();
	}

	/**
	 * method: configurationLoad
	 * description: loads a configuration
	 *
	 * @author Tobias Reichling
	 */
	public void configurationLoad() {
		final Vector<String> configurationVector = dataProcessing.getConfiguration();
		ConfigurationLoadingDialog configurationLoadingDialog = new ConfigurationLoadingDialog(this,
				configurationVector);

		if (!configurationLoadingDialog.isCanceled()) {
			switch (configurationLoadingDialog.getSelection()) {
				// new configuration
				case 1:
					ConfigurationNewDialog configurationNewDialog = new ConfigurationNewDialog(this);

					if (!configurationNewDialog.isCanceled()) {
						final Enumeration<Number> results = configurationNewDialog.getValues().elements();
						final Configuration configurationNew = new Configuration(
								configurationNewDialog.getName(),
								configurationNewDialog.getBasisName(),
								(Integer) results.nextElement(),
								(Integer) results.nextElement(),
								(Double) results.nextElement(),
								(Integer) results.nextElement(),
								(Integer) results.nextElement(),
								configurationNewDialog.getMaterial());
						ConfigurationDerivationDialog configurationDerivationDialog = new ConfigurationDerivationDialog(
								this, configurationNew, null);
						boolean cancel = false;

						while (!(cancel)) {
							if (!configurationDerivationDialog.isCanceled()) {
								try {
									dataProcessing.configurationSave(configurationDerivationDialog.getConfiguration());
									dataProcessing
											.setCurrentConfiguration(configurationDerivationDialog.getConfiguration());
									getGuiPanelOptions1().showConfigurationInfo(
											configurationDerivationDialog.getConfiguration().getName() + ".cfg");
									showInfo(textbundle.getString("output_mainWindow_13") + " "
											+ configurationDerivationDialog.getConfiguration().getName() + ".cfg "
											+ textbundle.getString("output_mainWindow_14") + ".");
									cancel = true;
									configurationDerivationDialog = null;
								} catch (final IOException saveIO) {
									errorDialog(textbundle.getString("output_mainWindow_15") + ": " + saveIO);
									configurationDerivationDialog.showDialog();
								}
							}

							cancel = true;
							configurationDerivationDialog = null;
						}
					}
					configurationNewDialog = null;
					break;
				// derivate configuration
				case 2:
					final String file = (String) configurationVector.elementAt(configurationLoadingDialog.getFile());
					Configuration configurationOld = new Configuration();
					if (configurationLoadingDialog.getFile() < 3) {
						configurationOld = dataProcessing.getSystemConfiguration(configurationLoadingDialog.getFile());
					} else {
						try {
							configurationOld = dataProcessing.configurationLoad(file);
						} catch (final IOException loadIO) {
							errorDialog(textbundle.getString("output_mainWindow_16") + ": " + loadIO);
						}
					}
					ConfigurationDerivationDialog configurationDerivationDialog = new ConfigurationDerivationDialog(
							this, null, configurationOld);
					boolean cancel = false;
					while (!(cancel)) {
						if (!configurationDerivationDialog.isCanceled()) {
							boolean nameAvailable = false;
							String name = "";

							for (final Enumeration<String> results2 = dataProcessing.getConfiguration()
									.elements(); results2
											.hasMoreElements();) {
								name = ((String) results2.nextElement());

								if ((name.substring(0, name.length() - 4))
										.equals(configurationDerivationDialog.getConfiguration().getName())) {
									nameAvailable = true;
								}
							}

							if (nameAvailable) {
								errorDialog(textbundle.getString("output_mainWindow_17"));
								configurationDerivationDialog.showDialog();
							} else {
								try {
									dataProcessing.configurationSave(configurationDerivationDialog.getConfiguration());
									dataProcessing
											.setCurrentConfiguration(configurationDerivationDialog.getConfiguration());
									getGuiPanelOptions1().showConfigurationInfo(
											configurationDerivationDialog.getConfiguration().getName() + ".cfg");
									showInfo(textbundle.getString("output_mainWindow_13") + " "
											+ configurationDerivationDialog.getConfiguration().getName() + ".cfg  "
											+ textbundle.getString("output_mainWindow_14") + ".");
									cancel = true;
									configurationDerivationDialog = null;
								} catch (final IOException speicherIO) {
									errorDialog(textbundle.getString("output_mainWindow_15") + ": " + speicherIO);
									configurationDerivationDialog.showDialog();
								}
							}
						} else {
							cancel = true;
							configurationDerivationDialog = null;
						}
					}

					break;
				// load existing configuration
				case 3:
					final String existingFile = (String) configurationVector
							.elementAt(configurationLoadingDialog.getFile());

					if (configurationLoadingDialog.getFile() < 3) {
						dataProcessing.setCurrentConfiguration(
								dataProcessing.getSystemConfiguration(configurationLoadingDialog.getFile()));
						getGuiPanelOptions1().showConfigurationInfo(existingFile);
						showInfo(textbundle.getString("output_mainWindow_13") + " " + existingFile + " "
								+ textbundle.getString("output_mainWindow_18") + ".");
					} else {
						try {
							dataProcessing.setCurrentConfiguration(dataProcessing.configurationLoad(existingFile));
							getGuiPanelOptions1().showConfigurationInfo(existingFile);
							showInfo(textbundle.getString("output_mainWindow_13") + " " + existingFile + " "
									+ textbundle.getString("output_mainWindow_18") + ".");
						} catch (final IOException configurationLoadIO) {
							errorDialog(textbundle.getString("output_mainWindow_19") + ": " + configurationLoadIO);
						}
					}

				default:
					break;
			}
		}

		configurationLoadingDialog = null;
	}

	/**
	 * method: imageLoad
	 * description: system dialog for image loading
	 *
	 * @author Tobias Reichling
	 * @exception IOExcepion
	 */
	public void imageLoad() {
		// system dialog
		final JFileChooser d = new JFileChooser();
		d.setFileFilter(new FileFilter() {
			public boolean accept(final File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") ||
						f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".gif") ||
						f.getName().toLowerCase().endsWith(".png");
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
			dataProcessing.computeScaleFactor(false, (double) (guiScrollPaneTop.getWidth() - 40),
					(double) ((guiSplitPane.getHeight() - guiSplitPane.getDividerLocation() - 60)));
			guiZoomSlider1.setValue(3);
			guiPictureElementTop.setImage(dataProcessing.getScaledImage(false, guiZoomSlider1.getValue()));
			guiPictureElementTop.updateUI();
			getGuiPanelOptions1().showImageInfo(menuFile.getName());
			showInfo(textbundle.getString("output_mainWindow_20") + " " + menuFile.getName() + " "
					+ textbundle.getString("output_mainWindow_18") + ".");
		} else {
			errorDialog(textbundle.getString("output_mainWindow_21"));
		}
	}

	/**
	 * method: errorDialog
	 * description: shows an error dialog
	 *
	 * @author Tobias Reichling
	 * @param errorMessage
	 */
	public void errorDialog(final String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, textbundle.getString("dialog_error_frame"),
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * method: radioButtonStatus
	 * description: Synchronize radio buttons gui and menu
	 *
	 * @author Tobias Reichling
	 * @param groupNumber
	 * @param buttonNumber
	 */
	public void radioButtonStatus(final int groupNumber, final int buttonNumber) {
		menuMosaic.radioButtonStatus(groupNumber, buttonNumber);
		guiPanelOptions2.radioButtonStatus(groupNumber, buttonNumber);

	}

	/**
	 * method: adjustDividerLocation
	 * description: adjust divider location (split pane)
	 *
	 * @author Tobias Reichling
	 */
	public void adjustDividerLocation() {
		guiSplitPane.setDividerLocation(guiSplitPane.getHeight() / 2);
	}

	/**
	 * method: showInfo
	 * description: shows an information text
	 *
	 * @author Tobias Reichling
	 * @param text
	 */
	public void showInfo(final String text) {
		guiTextFieldInformation.setText(text);
	}

	/**
	 * method: buildGui
	 * description: builds gui
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
		guiScrollPaneTop = new JScrollPane(guiPanelTopArea2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
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
		guiScrollPaneBottom = new JScrollPane(guiPanelBottomArea2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		guiScrollPaneBottom.setMinimumSize(new Dimension(0, 0));
		guiScrollPaneBottom.setPreferredSize(new Dimension(100, 100));
		// split pane
		guiSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		guiSplitPane.setDividerLocation(0);
		guiSplitPane.setContinuousLayout(true);
		final TitledBorder imageAreaBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_mainWindow_border_1"));
		guiSplitPane.setBorder(imageAreaBorder);
		imageAreaBorder.setTitleColor(new Color(100, 100, 100));
		guiSplitPane.setTopComponent(guiScrollPaneTop);
		guiSplitPane.setBottomComponent(guiScrollPaneBottom);
		guiPanelTopArea.add(guiSplitPane, BorderLayout.CENTER);
		// option panels all
		final TitledBorder optionAreaBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_mainWindow_border_2"));
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
				.createTitledBorder(textbundle.getString("dialog_mainWindow_border_4"));
		informationAreaBorder.setTitleColor(new Color(100, 100, 100));
		guiPanelInformation.setBorder(informationAreaBorder);
		guiPanelInformation.add(guiTextFieldInformation);
		guiStatusHandler.guiStatus(GuiStatusHandler.GUI_START);
		pack();
	}

	/**
	 * method: buildMenu
	 * description: generate Menu
	 *
	 * @author Tobias Reichling
	 */
	private void buildMenu() {
		menuBar = new JMenuBar();
		// menuFile
		menuFile = new FileMenu(textbundle.getString("dialog_mainWindow_menu_10"), this);
		menuBar.add(menuFile);
		// menuPreprocessing
		menuPreprocessing = new PreprocessingMenu(textbundle.getString("dialog_mainWindow_menu_20"), this);
		menuBar.add(menuPreprocessing);
		// menuMosaic
		menuMosaic = new MosaicMenu(textbundle.getString("dialog_mainWindow_menu_30"), this);
		menuBar.add(menuMosaic);
		// menuOutput
		menuOutput = new OutputMenu(textbundle.getString("dialog_mainWindow_menu_40"), this);
		menuBar.add(menuOutput);
		// menuHelp
		menuHelp = new JMenu(textbundle.getString("dialog_mainWindow_menu_50"));
		menuAbout = new JMenuItem(textbundle.getString("dialog_mainWindow_menu_51"));
		menuAbout.addActionListener(this);
		menuAbout.setActionCommand("about");
		menuHelp.add(menuAbout);
		menuBar.add(menuHelp);
		setJMenuBar(menuBar);
	}

	/**
	 * method: main
	 * description: main method of pictobrick
	 *
	 * @author Tobias Reichling
	 */
	public static void main(final String[] args) {
		final MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}
}
