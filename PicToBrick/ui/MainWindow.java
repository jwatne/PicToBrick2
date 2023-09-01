package PicToBrick.ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.event.*;
import javax.swing.BorderFactory;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;

import PicToBrick.model.ColorObject;
import PicToBrick.model.Configuration;
import PicToBrick.service.DataProcessor;
import PicToBrick.service.SwingWorker;

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
	// resource bundle
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	// --------------------------------------------------------------------------------------------------------
	// data processing
	private final DataProcessor dataProcessing;
	// --------------------------------------------------------------------------------------------------------
	// dialogs
	private final ProgressBarsAlgorithms progressBarsAlgorithm;
	private final ProgressBarsOutputFiles progressBarsOutputFiles;
	// --------------------------------------------------------------------------------------------------------
	// other
	private int mosaicWidth, mosaicHeight;
	// --------------------------------------------------------------------------------------------------------
	// menu
	private JMenuBar menuBar;
	private JMenu menuFile, menuPreprocessing, menuMosaic, menuOutput, menuHelp;
	private JMenuItem menuNewMosaik, menuImageLoad, menuConfigurationLoad, menuSettings, menuEnd;
	private JMenuItem menuMosaicDimension, menuMosaicGenerate, menuDocumentGenerate, menuAbout;
	private ButtonGroup menuGroupQuantisation, menuGroupTiling;
	private JRadioButtonMenuItem menuAlgorithm11, menuAlgorithm12, menuAlgorithm13, menuAlgorithm14;
	private JRadioButtonMenuItem menuAlgorithm15, menuAlgorithm16, menuAlgorithm17, menuAlgorithm25;
	private JRadioButtonMenuItem menuAlgorithm21, menuAlgorithm22, menuAlgorithm23, menuAlgorithm24;
	private JCheckBoxMenuItem menuGrafic, menuXml, menuBuildingInstruction, menuMaterial, menuConfiguration;
	// --------------------------------------------------------------------------------------------------------
	// gui
	private JPanel guiPanelTopArea, guiPanelBottomArea, guiPanelRightArea, guiPanelInformation, guiPanelZoom;
	private JPanel guiPanelOptions1, guiPanelOptions2, guiPanelOptions3, guiPanelOptions2Bottom;
	private JPanel guiPanelOptions1Top, guiPanelOptions2Top, guiPanelOptions3Top, guiPanelOptions1Empty;
	private JPanel guiPanelThreeDEffectStatistic, guiPanelOptions3Empty;
	private JPanel guiPanelTopArea2, guiPanelBottomArea2;
	private JButton buttonImageLoad, buttonConfigurationLoad, buttonMosaicDimension;
	private JButton buttonCutout, buttonOutput, buttonMosaicNew, buttonMosaicGenerate, buttonDocumentsGenerate;
	private ButtonGroup guiGroupQuantisation, guiGroupTiling;
	private JRadioButton guiRadioAlgorithm11, guiRadioAlgorithm12, guiRadioAlgorithm13, guiRadioAlgorithm14;
	private JRadioButton guiRadioAlgorithm15, guiRadioAlgorithm16, guiRadioAlgorithm17, guiRadioAlgorithm21;
	private JRadioButton guiRadioAlgorithm22, guiRadioAlgorithm23, guiRadioAlgorithm24, guiRadioAlgorithm25;
	private JCheckBox guiOutputGrafic, guiOutputXml, guiThreeDEffect, guiStatistic;
	private JCheckBox guiOutputConfiguration, guiOutputMaterial, guiOutputBuildingInstruction;
	private PictureElement guiPictureElementTop, guiPictureElementBottom;
	private JLabel guiLabelImage, guiLabelConfiguration, guiLabelWidth, guiLabelHeight, guiLabelZoom1, guiLabelZoom2;
	private JLabel guiLabelQuantisation, guiLabelTiling, guiLabelOutput, guiLabelSeparator;
	private JComboBox<Vector<String>> guiComboBoxInterpolation;
	private JSplitPane guiSplitPane;
	private JScrollPane guiScrollPaneTop, guiScrollPaneBottom;
	private JTextField guiTextFieldInformation;
	private JSlider guiZoomSlider1, guiZoomSlider2;
	private int guiZoomSlider1Value, guiZoomSlider2Value;

	/**
	 * method: MainWindow
	 * description: constructor
	 *
	 * @author Tobias Reichling
	 */
	public MainWindow() {
		super("PicToBrick");
		addWindowListener(new WindowClosingAdapter(true));
		osStyle();
		dataProcessing = new DataProcessor(this);
		progressBarsAlgorithm = new ProgressBarsAlgorithms(this);
		progressBarsOutputFiles = new ProgressBarsOutputFiles(this);
		buildMenu();
		buildGui();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		// Check if the minimum of 256MB memory are available
		if (((Runtime.getRuntime().maxMemory() / 1024) / 1024) < 250) {
			errorDialog(textbundle.getString("dialog_mainWindow_error_1") + "\r\n" +
					textbundle.getString("dialog_mainWindow_error_2") + "\r\n" +
					textbundle.getString("dialog_mainWindow_error_3"));
		}
		workingDirectory(false);
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
		if ((JSlider) event.getSource() == guiZoomSlider1) {
			if (guiZoomSlider1Value != guiZoomSlider1.getValue()) {
				guiZoomSlider1Value = guiZoomSlider1.getValue();
				guiPictureElementTop.setImage(dataProcessing.getScaledImage(false, guiZoomSlider1.getValue()));
				guiPictureElementTop.updateUI();
			}
		} else {
			if (guiZoomSlider2Value != guiZoomSlider2.getValue()) {
				guiZoomSlider2Value = guiZoomSlider2.getValue();
				guiPictureElementBottom.setImage(dataProcessing.getScaledImage(true, guiZoomSlider2.getValue()));
				guiPictureElementBottom.updateUI();
			}
		}
	}

	/**
	 * method: actionPerformed
	 * description: ActionListener
	 *
	 * @author Tobias Reichling
	 * @param event
	 */
	public void actionPerformed(final ActionEvent event) {
		// synchronisize check boxes (gui) and menu items (menu)
		if (event.getActionCommand().contains("menu")) {
			if (event.getActionCommand().contains("grafic")) {
				checkBoxStatus(11, false);
			} else if (event.getActionCommand().contains("xml")) {
				checkBoxStatus(13, false);
			} else if (event.getActionCommand().contains("buildinginstruction")) {
				checkBoxStatus(14, false);
			} else if (event.getActionCommand().contains("material")) {
				checkBoxStatus(15, false);
			} else if (event.getActionCommand().contains("configuration")) {
				checkBoxStatus(16, false);
			}
			// synchronisize check boxes (gui) and menu items (menu)
		} else if (event.getActionCommand().contains("gui")) {
			if (event.getActionCommand().contains("grafic")) {
				checkBoxStatus(21, false);
			} else if (event.getActionCommand().contains("xml")) {
				checkBoxStatus(23, false);
			} else if (event.getActionCommand().contains("buildinginstruction")) {
				checkBoxStatus(24, false);
			} else if (event.getActionCommand().contains("material")) {
				checkBoxStatus(25, false);
			} else if (event.getActionCommand().contains("configuration")) {
				checkBoxStatus(26, false);
			}
			// synchronisize radio buttons (gui) and menu items (menu)
		} else if (event.getActionCommand().contains("algorithm")) {
			if (event.getActionCommand().contains("11")) {
				radioButtonStatus(1, 1);
			} else if (event.getActionCommand().contains("12")) {
				radioButtonStatus(1, 2);
			} else if (event.getActionCommand().contains("13")) {
				radioButtonStatus(1, 3);
			} else if (event.getActionCommand().contains("14")) {
				radioButtonStatus(1, 4);
			} else if (event.getActionCommand().contains("15")) {
				radioButtonStatus(1, 5);
			} else if (event.getActionCommand().contains("16")) {
				radioButtonStatus(1, 6);
			} else if (event.getActionCommand().contains("17")) {
				radioButtonStatus(1, 7);
			} else if (event.getActionCommand().contains("21")) {
				radioButtonStatus(2, 1);
			} else if (event.getActionCommand().contains("22")) {
				radioButtonStatus(2, 2);
			} else if (event.getActionCommand().contains("23")) {
				radioButtonStatus(2, 3);
			} else if (event.getActionCommand().contains("24")) {
				radioButtonStatus(2, 4);
			} else if (event.getActionCommand().contains("25")) {
				radioButtonStatus(2, 5);
			}
			// buttons
		} else if (event.getActionCommand().contains("cutout")) {
			cutout();
		} else if (event.getActionCommand().contains("output")) {
			guiStatus(30);
			adjustDividerLocation();
		} else if (event.getActionCommand().contains("mosaicnew")) {
			guiStatus(10);
			adjustDividerLocation();
			showInfo(textbundle.getString("output_mainWindow_4") + "!");
		} else if (event.getActionCommand().contains("mosaicgenerate")) {
			dataProcessing.initInfo();
			dataProcessing.setInterpolation(guiComboBoxInterpolation.getSelectedIndex() + 1);
			final int quantisation = (new Integer(
					guiGroupQuantisation.getSelection().getActionCommand().substring(9, 11)).intValue() - 10);
			final int tiling = (new Integer(guiGroupTiling.getSelection().getActionCommand().substring(9, 11))
					.intValue() - 20);
			if (tiling == 2 && !(dataProcessing.getCurrentConfiguration().getMaterial() == 3)) {
				errorDialog(textbundle.getString("output_mainWindow_6"));
			} else if (tiling == 4 && ((dataProcessing.getCurrentConfiguration().getMaterial() == 3)
					|| (dataProcessing.getCurrentConfiguration().getMaterial() == 1))) {
				errorDialog(textbundle.getString("output_mainWindow_7"));
			} else if (tiling == 3 && (dataProcessing.getCurrentConfiguration().getMaterial() == 3)) {
				errorDialog(textbundle.getString("output_mainWindow_8"));
			} else {
				adjustDividerLocation();
				guiStatus(21);
				dataProcessing.generateMosaic(mosaicWidth, mosaicHeight, quantisation, tiling,
						guiThreeDEffect.isSelected(), guiStatistic.isSelected());
			}
		} else if (event.getActionCommand().contains("imageload")) {
			adjustDividerLocation();
			imageLoad();
			if (dataProcessing.isImage() && dataProcessing.isConfiguration()) {
				guiStatus(11);
			}
		} else if (event.getActionCommand().contains("configurationload")) {

			if (dataProcessing.getWorkingDirectory() == null) {
				errorDialog(textbundle.getString("output_mainWindow_9"));
			} else {
				configurationLoad();
			}
			if (dataProcessing.isImage() && dataProcessing.isConfiguration()) {
				guiStatus(11);
			}
		} else if (event.getActionCommand().contains("settings")) {
			WorkingDirectoryDialog workingDirectoryDialog = new WorkingDirectoryDialog(this,
					dataProcessing.getWorkingDirectory());
			if (workingDirectoryDialog.getButton() == 1) {
				workingDirectory(true);
			}
			workingDirectoryDialog = null;
		} else if (event.getActionCommand().contains("exit")) {
			this.setVisible(false);
			this.dispose();
			System.exit(0);
		} else if (event.getActionCommand().contains("mosaicdimension")) {
			MosaicSizeDialog mosaicDimensionDialog = new MosaicSizeDialog(this,
					dataProcessing.getCurrentConfiguration());
			if (!mosaicDimensionDialog.isCanceled()) {
				guiPictureElementTop.removeMouseListener(guiPictureElementTop);
				guiPictureElementTop.removeMouseMotionListener(guiPictureElementTop);
				guiPictureElementTop.setCutoutRatio(new Dimension(
						mosaicDimensionDialog.getArea().width * dataProcessing.getConfigurationDimension().width,
						mosaicDimensionDialog.getArea().height * dataProcessing.getConfigurationDimension().height));
				guiPictureElementTop.addMouseListener(guiPictureElementTop);
				guiPictureElementTop.addMouseMotionListener(guiPictureElementTop);
				guiPictureElementTop.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				mosaicWidth = mosaicDimensionDialog.getArea().width;
				mosaicHeight = mosaicDimensionDialog.getArea().height;
				showDimensionInfo(mosaicWidth, mosaicHeight, false);
				guiStatus(12);
			}
			mosaicDimensionDialog = null;
		} else if (event.getActionCommand().contains("documentgenerate")) {
			if (guiOutputGrafic.isSelected() ||
					guiOutputConfiguration.isSelected() ||
					guiOutputMaterial.isSelected() ||
					guiOutputBuildingInstruction.isSelected() ||
					guiOutputXml.isSelected()) {
				guiStatus(31);
				refreshProgressBarOutputFiles(0, 1);
				refreshProgressBarOutputFiles(0, 2);
				refreshProgressBarOutputFiles(0, 3);
				refreshProgressBarOutputFiles(0, 4);
				refreshProgressBarOutputFiles(0, 5);
				refreshProgressBarOutputFiles(0, 6);
				setStatusProgressBarOutputFiles(guiOutputGrafic.isSelected(),
						guiOutputConfiguration.isSelected(),
						guiOutputMaterial.isSelected(),
						guiOutputBuildingInstruction.isSelected(),
						guiOutputXml.isSelected(),
						true);
				showProgressBarOutputFiles();
				// SwingWorker
				// "construct": all commands are startet in a new thread
				// "finished": all commands are queued to the gui thread
				// after finshing aforesaid (construct-)thread
				final SwingWorker worker = new SwingWorker() {
					public Object construct() {
						final String message = new String(dataProcessing.generateDocuments(
								guiOutputGrafic.isSelected(),
								guiOutputConfiguration.isSelected(),
								guiOutputMaterial.isSelected(),
								guiOutputBuildingInstruction.isSelected(),
								guiOutputXml.isSelected(),
								dataProcessing.getInfo(2)));
						if (!message.equals("")) {
							errorDialog(message);
						} else {
							showInfo(textbundle.getString("output_mainWindow_10"));
						}
						return true;
					}

					public void finished() {
						guiStatus(32);
						hideProgressBarOutputFiles();
					}
				};
				worker.start();
			}
		} else if (event.getActionCommand().contains("about")) {
			AboutDialog aboutDialog = new AboutDialog(this);
			aboutDialog = null;
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
			guiStatus(20);
			dataProcessing.replaceImageByCutout(guiPictureElementTop.getCutoutRectangle());
			dataProcessing.computeScaleFactor(false, (double) (guiScrollPaneTop.getWidth() - 40),
					(double) ((guiSplitPane.getHeight() - guiSplitPane.getDividerLocation() - 60)));
			guiPictureElementTop.setImage(dataProcessing.getScaledImage(false, guiZoomSlider1.getValue()));
			guiZoomSlider1.setValue(3);
			guiZoomSlider1Value = 3;
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
		guiPictureElementTop.setImage(dataProcessing.getScaledImage(false, guiZoomSlider1.getValue()));
		dataProcessing.computeScaleFactor(true, (double) (guiScrollPaneBottom.getWidth() - 40),
				(double) ((guiSplitPane.getHeight() - guiSplitPane.getDividerLocation() - 60)));
		guiPictureElementBottom.setImage(dataProcessing.getScaledImage(true, guiZoomSlider2.getValue()));
		guiStatus(22);
		guiPictureElementBottom.updateUI();
		if (guiStatistic.isSelected()) {
			final Enumeration statisticInformation = dataProcessing.getInfo(1);
			String statisticInformationString = new String("");
			while (statisticInformation.hasMoreElements()) {
				statisticInformationString = statisticInformationString + (String) statisticInformation.nextElement()
						+ "\n\r";
			}
			JOptionPane.showMessageDialog(this, statisticInformationString,
					textbundle.getString("output_mainWindow_12"), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * method: disableStatisticButton
	 * description: disables statistic if not possible
	 *
	 * @author Tobias Reichling
	 */
	public void disableStatisticButton() {
		guiStatistic.setSelected(false);
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
	public Vector<Object> dialogSlicingThreshold(final int colorQuantity, final Enumeration colors) {
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
	private void configurationLoad() {
		final Vector<String> configurationVector = dataProcessing.getConfiguration();
		ConfigurationLoadingDialog configurationLoadingDialog = new ConfigurationLoadingDialog(this,
				configurationVector);

		if (!configurationLoadingDialog.isCanceled()) {
			switch (configurationLoadingDialog.getSelection()) {
				// new configuration
				case 1: {
					ConfigurationNewDialog configurationNewDialog = new ConfigurationNewDialog(this);

					if (!configurationNewDialog.isCanceled()) {
						final Enumeration results = configurationNewDialog.getValues().elements();
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
									showConfigurationInfo(
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
				}
				// derivate configuration
				case 2: {
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
							String name = new String("");
							for (final Enumeration results2 = dataProcessing.getConfiguration().elements(); results2
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
									showConfigurationInfo(
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
				}
				// load existing configuration
				case 3: {
					final String file = (String) configurationVector.elementAt(configurationLoadingDialog.getFile());
					if (configurationLoadingDialog.getFile() < 3) {
						dataProcessing.setCurrentConfiguration(
								dataProcessing.getSystemConfiguration(configurationLoadingDialog.getFile()));
						showConfigurationInfo(file);
						showInfo(textbundle.getString("output_mainWindow_13") + " " + file + " "
								+ textbundle.getString("output_mainWindow_18") + ".");
					} else {
						try {
							dataProcessing.setCurrentConfiguration(dataProcessing.configurationLoad(file));
							showConfigurationInfo(file);
							showInfo(textbundle.getString("output_mainWindow_13") + " " + file + " "
									+ textbundle.getString("output_mainWindow_18") + ".");
						} catch (final IOException configurationLoadIO) {
							errorDialog(textbundle.getString("output_mainWindow_19") + ": " + configurationLoadIO);
						}
					}
				}
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
	private void imageLoad() {
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
			guiZoomSlider1.setEnabled(true);
			dataProcessing.computeScaleFactor(false, (double) (guiScrollPaneTop.getWidth() - 40),
					(double) ((guiSplitPane.getHeight() - guiSplitPane.getDividerLocation() - 60)));
			guiZoomSlider1.setValue(3);
			guiPictureElementTop.setImage(dataProcessing.getScaledImage(false, guiZoomSlider1.getValue()));
			guiPictureElementTop.updateUI();
			showImageInfo(menuFile.getName());
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
	 * description: synchronisize radio buttons gui and menu
	 *
	 * @author Tobias Reichling
	 * @param groupNumber
	 * @param buttonNumber
	 */
	private void radioButtonStatus(final int groupNumber, final int buttonNumber) {
		switch (groupNumber) {
			case 1: {
				switch (buttonNumber) {
					case 1: {
						menuAlgorithm11.setSelected(true);
						guiRadioAlgorithm11.setSelected(true);
						menuAlgorithm12.setSelected(false);
						guiRadioAlgorithm12.setSelected(false);
						menuAlgorithm13.setSelected(false);
						guiRadioAlgorithm13.setSelected(false);
						menuAlgorithm14.setSelected(false);
						guiRadioAlgorithm14.setSelected(false);
						menuAlgorithm15.setSelected(false);
						guiRadioAlgorithm15.setSelected(false);
						menuAlgorithm16.setSelected(false);
						guiRadioAlgorithm16.setSelected(false);
						menuAlgorithm17.setSelected(false);
						guiRadioAlgorithm17.setSelected(false);
						break;
					}
					case 2: {
						menuAlgorithm11.setSelected(false);
						guiRadioAlgorithm11.setSelected(false);
						menuAlgorithm12.setSelected(true);
						guiRadioAlgorithm12.setSelected(true);
						menuAlgorithm13.setSelected(false);
						guiRadioAlgorithm13.setSelected(false);
						menuAlgorithm14.setSelected(false);
						guiRadioAlgorithm14.setSelected(false);
						menuAlgorithm15.setSelected(false);
						guiRadioAlgorithm15.setSelected(false);
						menuAlgorithm16.setSelected(false);
						guiRadioAlgorithm16.setSelected(false);
						menuAlgorithm17.setSelected(false);
						guiRadioAlgorithm17.setSelected(false);
						break;
					}
					case 3: {
						menuAlgorithm11.setSelected(false);
						guiRadioAlgorithm11.setSelected(false);
						menuAlgorithm12.setSelected(false);
						guiRadioAlgorithm12.setSelected(false);
						menuAlgorithm13.setSelected(true);
						guiRadioAlgorithm13.setSelected(true);
						menuAlgorithm14.setSelected(false);
						guiRadioAlgorithm14.setSelected(false);
						menuAlgorithm15.setSelected(false);
						guiRadioAlgorithm15.setSelected(false);
						menuAlgorithm16.setSelected(false);
						guiRadioAlgorithm16.setSelected(false);
						menuAlgorithm17.setSelected(false);
						guiRadioAlgorithm17.setSelected(false);
						break;
					}
					case 4: {
						menuAlgorithm11.setSelected(false);
						guiRadioAlgorithm11.setSelected(false);
						menuAlgorithm12.setSelected(false);
						guiRadioAlgorithm12.setSelected(false);
						menuAlgorithm13.setSelected(false);
						guiRadioAlgorithm13.setSelected(false);
						menuAlgorithm14.setSelected(true);
						guiRadioAlgorithm14.setSelected(true);
						menuAlgorithm15.setSelected(false);
						guiRadioAlgorithm15.setSelected(false);
						menuAlgorithm16.setSelected(false);
						guiRadioAlgorithm16.setSelected(false);
						menuAlgorithm17.setSelected(false);
						guiRadioAlgorithm17.setSelected(false);
						break;
					}
					case 5: {
						menuAlgorithm11.setSelected(false);
						guiRadioAlgorithm11.setSelected(false);
						menuAlgorithm12.setSelected(false);
						guiRadioAlgorithm12.setSelected(false);
						menuAlgorithm13.setSelected(false);
						guiRadioAlgorithm13.setSelected(false);
						menuAlgorithm14.setSelected(false);
						guiRadioAlgorithm14.setSelected(false);
						menuAlgorithm15.setSelected(true);
						guiRadioAlgorithm15.setSelected(true);
						menuAlgorithm16.setSelected(false);
						guiRadioAlgorithm16.setSelected(false);
						menuAlgorithm17.setSelected(false);
						guiRadioAlgorithm17.setSelected(false);
						break;
					}
					case 6: {
						menuAlgorithm11.setSelected(false);
						guiRadioAlgorithm11.setSelected(false);
						menuAlgorithm12.setSelected(false);
						guiRadioAlgorithm12.setSelected(false);
						menuAlgorithm13.setSelected(false);
						guiRadioAlgorithm13.setSelected(false);
						menuAlgorithm14.setSelected(false);
						guiRadioAlgorithm14.setSelected(false);
						menuAlgorithm15.setSelected(false);
						guiRadioAlgorithm15.setSelected(false);
						menuAlgorithm16.setSelected(true);
						guiRadioAlgorithm16.setSelected(true);
						menuAlgorithm17.setSelected(false);
						guiRadioAlgorithm17.setSelected(false);
						break;
					}
					case 7: {
						menuAlgorithm11.setSelected(false);
						guiRadioAlgorithm11.setSelected(false);
						menuAlgorithm12.setSelected(false);
						guiRadioAlgorithm12.setSelected(false);
						menuAlgorithm13.setSelected(false);
						guiRadioAlgorithm13.setSelected(false);
						menuAlgorithm14.setSelected(false);
						guiRadioAlgorithm14.setSelected(false);
						menuAlgorithm15.setSelected(false);
						guiRadioAlgorithm15.setSelected(false);
						menuAlgorithm16.setSelected(false);
						guiRadioAlgorithm16.setSelected(false);
						menuAlgorithm17.setSelected(true);
						guiRadioAlgorithm17.setSelected(true);
						break;
					}
				}
				break;
			}
			case 2: {
				switch (buttonNumber) {
					case 1: {
						menuAlgorithm21.setSelected(true);
						guiRadioAlgorithm21.setSelected(true);
						menuAlgorithm22.setSelected(false);
						guiRadioAlgorithm22.setSelected(false);
						menuAlgorithm23.setSelected(false);
						guiRadioAlgorithm23.setSelected(false);
						menuAlgorithm24.setSelected(false);
						guiRadioAlgorithm24.setSelected(false);
						menuAlgorithm25.setSelected(false);
						guiRadioAlgorithm25.setSelected(false);
						guiStatistic.setEnabled(true);

						break;
					}
					case 2: {
						menuAlgorithm21.setSelected(false);
						guiRadioAlgorithm21.setSelected(false);
						menuAlgorithm22.setSelected(true);
						guiRadioAlgorithm22.setSelected(true);
						menuAlgorithm23.setSelected(false);
						guiRadioAlgorithm23.setSelected(false);
						menuAlgorithm24.setSelected(false);
						guiRadioAlgorithm24.setSelected(false);
						menuAlgorithm25.setSelected(false);
						guiRadioAlgorithm25.setSelected(false);
						guiStatistic.setEnabled(true);
						break;
					}
					case 3: {
						menuAlgorithm21.setSelected(false);
						guiRadioAlgorithm21.setSelected(false);
						menuAlgorithm22.setSelected(false);
						guiRadioAlgorithm22.setSelected(false);
						menuAlgorithm23.setSelected(true);
						guiRadioAlgorithm23.setSelected(true);
						menuAlgorithm24.setSelected(false);
						guiRadioAlgorithm24.setSelected(false);
						menuAlgorithm25.setSelected(false);
						guiRadioAlgorithm25.setSelected(false);
						guiStatistic.setEnabled(true);
						break;
					}
					case 4: {
						menuAlgorithm21.setSelected(false);
						guiRadioAlgorithm21.setSelected(false);
						menuAlgorithm22.setSelected(false);
						guiRadioAlgorithm22.setSelected(false);
						menuAlgorithm23.setSelected(false);
						guiRadioAlgorithm23.setSelected(false);
						menuAlgorithm24.setSelected(true);
						guiRadioAlgorithm24.setSelected(true);
						menuAlgorithm25.setSelected(false);
						guiRadioAlgorithm25.setSelected(false);
						guiStatistic.setEnabled(true);
						break;
					}
					case 5: {
						menuAlgorithm21.setSelected(false);
						guiRadioAlgorithm21.setSelected(false);
						menuAlgorithm22.setSelected(false);
						guiRadioAlgorithm22.setSelected(false);
						menuAlgorithm23.setSelected(false);
						guiRadioAlgorithm23.setSelected(false);
						menuAlgorithm24.setSelected(false);
						guiRadioAlgorithm24.setSelected(false);
						menuAlgorithm25.setSelected(true);
						guiRadioAlgorithm25.setSelected(true);
						guiStatistic.setEnabled(false);
						guiStatistic.setSelected(false);
						break;
					}
				}
				break;
			}
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
	private void checkBoxStatus(final int checkBoxNumber, final boolean reset) {
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
				case 11: {
					if (menuGrafic.isSelected()) {
						guiOutputGrafic.setSelected(true);
					} else {
						guiOutputGrafic.setSelected(false);
					}
					break;
				}
				case 13: {
					if (menuXml.isSelected()) {
						guiOutputXml.setSelected(true);
					} else {
						guiOutputXml.setSelected(false);
					}
					break;
				}
				case 14: {
					if (menuBuildingInstruction.isSelected()) {
						guiOutputBuildingInstruction.setSelected(true);
					} else {
						guiOutputBuildingInstruction.setSelected(false);
					}
					break;
				}
				case 15: {
					if (menuMaterial.isSelected()) {
						guiOutputMaterial.setSelected(true);
					} else {
						guiOutputMaterial.setSelected(false);
					}
					break;
				}
				case 16: {
					if (menuConfiguration.isSelected()) {
						guiOutputConfiguration.setSelected(true);
					} else {
						guiOutputConfiguration.setSelected(false);
					}
					break;
				}
				case 21: {
					if (guiOutputGrafic.isSelected()) {
						menuGrafic.setSelected(true);
					} else {
						menuGrafic.setSelected(false);
					}
					break;
				}
				case 23: {
					if (guiOutputXml.isSelected()) {
						menuXml.setSelected(true);
					} else {
						menuXml.setSelected(false);
					}
					break;
				}
				case 24: {
					if (guiOutputBuildingInstruction.isSelected()) {
						menuBuildingInstruction.setSelected(true);
					} else {
						menuBuildingInstruction.setSelected(false);
					}
					break;
				}
				case 25: {
					if (guiOutputMaterial.isSelected()) {
						menuMaterial.setSelected(true);
					} else {
						menuMaterial.setSelected(false);
					}
					break;
				}
				case 26: {
					if (guiOutputConfiguration.isSelected()) {
						menuConfiguration.setSelected(true);
					} else {
						menuConfiguration.setSelected(false);
					}
					break;
				}
			}
		}
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
			case 10: {
				dataProcessing.setCurrentConfiguration(null);
				// sets option panel 1
				guiPanelRightArea.removeAll();
				guiPanelRightArea.add(guiPanelOptions1, BorderLayout.CENTER);
				guiPanelRightArea.add(guiPanelZoom, BorderLayout.SOUTH);
				// init check boxes
				checkBoxStatus(0, true);
				// init zoom sliders
				guiZoomSlider1.setEnabled(false);
				guiZoomSlider2.setEnabled(false);
				guiZoomSlider1.setValue(3);
				guiZoomSlider2.setValue(3);
				guiZoomSlider1Value = 3;
				guiZoomSlider2Value = 3;
				// init information labels
				showImageInfo("");
				showConfigurationInfo("");
				showDimensionInfo(0, 0, true);
				// reset images
				dataProcessing.imageReset();
				guiPanelTopArea2.removeAll();
				guiPictureElementTop = new PictureElement(this);
				guiPanelTopArea2.add(guiPictureElementTop);
				guiPictureElementTop.setImage(null);
				guiScrollPaneTop.updateUI();
				guiPictureElementBottom.setImage(null);
				guiScrollPaneBottom.updateUI();
				// enable/disable buttons, menu items, etc.
				guiPanelOptions1.updateUI();
				menuImageLoad.setEnabled(true);
				menuConfigurationLoad.setEnabled(true);
				menuMosaicDimension.setEnabled(false);
				buttonMosaicDimension.setEnabled(false);
				menuSettings.setEnabled(true);
				menuAlgorithm11.setEnabled(false);
				menuAlgorithm12.setEnabled(false);
				menuAlgorithm13.setEnabled(false);
				menuAlgorithm14.setEnabled(false);
				menuAlgorithm15.setEnabled(false);
				menuAlgorithm16.setEnabled(false);
				menuAlgorithm17.setEnabled(false);
				menuAlgorithm21.setEnabled(false);
				menuAlgorithm22.setEnabled(false);
				menuAlgorithm23.setEnabled(false);
				menuAlgorithm24.setEnabled(false);
				menuAlgorithm25.setEnabled(false);
				guiComboBoxInterpolation.setEnabled(false);
				guiComboBoxInterpolation.setSelectedIndex(0);
				menuMosaicGenerate.setEnabled(false);
				menuGrafic.setEnabled(false);
				menuXml.setEnabled(false);
				menuBuildingInstruction.setEnabled(false);
				menuMaterial.setEnabled(false);
				menuConfiguration.setEnabled(false);
				menuDocumentGenerate.setEnabled(false);
				buttonCutout.setEnabled(false);
				buttonImageLoad.setEnabled(true);
				buttonConfigurationLoad.setEnabled(true);
				menuConfigurationLoad.setEnabled(true);
				menuImageLoad.setEnabled(true);
				radioButtonStatus(1, 1);
				radioButtonStatus(2, 5);
				break;
			}
			// image and configuration are loaded
			case 11: {
				// enable/disable buttons, menu items, etc.
				menuMosaicDimension.setEnabled(true);
				buttonMosaicDimension.setEnabled(true);
				// set information text
				showInfo(textbundle.getString("output_mainWindow_22"));
				break;
			}
			// cutout situation
			case 12: {
				// set information text
				showInfo(textbundle.getString("output_mainWindow_23") + ": " + mosaicWidth + " x " + mosaicHeight + ". "
						+ textbundle.getString("output_mainWindow_24") + ".");
				// enable/disable buttons, menu items, etc.
				buttonCutout.setEnabled(false);
				buttonImageLoad.setEnabled(false);
				buttonConfigurationLoad.setEnabled(false);
				menuConfigurationLoad.setEnabled(false);
				menuImageLoad.setEnabled(false);
				menuSettings.setEnabled(false);
				guiStatus(13);
				break;
			}
			// cutout situation - no rectangle available
			case 13: {
				// enable/disable buttons, menu items, etc.
				guiZoomSlider1.setEnabled(true);
				buttonCutout.setEnabled(false);
				break;
			}
			// cutout situation - rectangle available
			case 14: {
				// enable/disable buttons, menu items, etc.
				guiZoomSlider1.setEnabled(false);
				buttonCutout.setEnabled(true);
				// sets information text
				showInfo(textbundle.getString("output_mainWindow_25"));
				break;
			}
			// generate mosaic
			case 20: {
				// sets information text
				showInfo(textbundle.getString("output_mainWindow_26"));
				// sets option panel 2
				guiPanelRightArea.remove(guiPanelOptions1);
				guiPanelRightArea.add(guiPanelOptions2, BorderLayout.CENTER);
				guiPanelRightArea.add(guiPanelZoom, BorderLayout.SOUTH);
				guiPanelOptions2.updateUI();
				// enable/disable buttons, menu items, etc.
				guiZoomSlider1.setEnabled(true);
				menuImageLoad.setEnabled(false);
				menuConfigurationLoad.setEnabled(false);
				menuMosaicDimension.setEnabled(false);
				menuAlgorithm11.setEnabled(true);
				menuAlgorithm12.setEnabled(true);
				menuAlgorithm13.setEnabled(true);
				menuAlgorithm14.setEnabled(true);
				menuAlgorithm15.setEnabled(true);
				menuAlgorithm16.setEnabled(true);
				menuAlgorithm17.setEnabled(true);
				menuAlgorithm21.setEnabled(true);
				menuAlgorithm22.setEnabled(true);
				menuAlgorithm23.setEnabled(true);
				menuAlgorithm24.setEnabled(true);
				menuAlgorithm25.setEnabled(true);
				guiComboBoxInterpolation.setEnabled(true);
				menuMosaicGenerate.setEnabled(true);
				menuGrafic.setEnabled(false);
				menuXml.setEnabled(false);
				menuBuildingInstruction.setEnabled(false);
				menuMaterial.setEnabled(false);
				menuConfiguration.setEnabled(false);
				menuDocumentGenerate.setEnabled(false);
				buttonOutput.setEnabled(false);
				guiThreeDEffect.setEnabled(true);
				guiThreeDEffect.setSelected(true);
				guiStatistic.setSelected(false);
				guiStatistic.setEnabled(false);
				break;
			}
			// disable gui while generating mosaic
			case 21: {
				// enable/disable buttons, menu items, etc.
				menuAlgorithm11.setEnabled(false);
				menuAlgorithm12.setEnabled(false);
				menuAlgorithm13.setEnabled(false);
				menuAlgorithm14.setEnabled(false);
				menuAlgorithm15.setEnabled(false);
				menuAlgorithm16.setEnabled(false);
				menuAlgorithm17.setEnabled(false);
				menuAlgorithm21.setEnabled(false);
				menuAlgorithm22.setEnabled(false);
				menuAlgorithm23.setEnabled(false);
				menuAlgorithm24.setEnabled(false);
				menuAlgorithm25.setEnabled(false);
				guiRadioAlgorithm11.setEnabled(false);
				guiRadioAlgorithm12.setEnabled(false);
				guiRadioAlgorithm13.setEnabled(false);
				guiRadioAlgorithm14.setEnabled(false);
				guiRadioAlgorithm15.setEnabled(false);
				guiRadioAlgorithm16.setEnabled(false);
				guiRadioAlgorithm17.setEnabled(false);
				guiRadioAlgorithm21.setEnabled(false);
				guiRadioAlgorithm22.setEnabled(false);
				guiRadioAlgorithm23.setEnabled(false);
				guiRadioAlgorithm24.setEnabled(false);
				guiRadioAlgorithm25.setEnabled(false);
				guiComboBoxInterpolation.setEnabled(false);
				buttonOutput.setEnabled(false);
				guiZoomSlider1.setEnabled(false);
				guiZoomSlider2.setEnabled(false);
				menuMosaicGenerate.setEnabled(false);
				buttonMosaicGenerate.setEnabled(false);
				guiThreeDEffect.setEnabled(false);
				guiStatistic.setEnabled(false);
				break;
			}
			// enable gui after generating mosaic
			case 22: {
				// sets information text
				showInfo(textbundle.getString("output_mainWindow_27"));
				// enable/disable buttons, menu items, etc.
				buttonOutput.setEnabled(true);
				guiZoomSlider1.setValue(3);
				guiZoomSlider1Value = 3;
				guiZoomSlider2.setValue(3);
				guiZoomSlider2Value = 3;
				guiZoomSlider1.setEnabled(true);
				guiZoomSlider2.setEnabled(true);
				menuAlgorithm11.setEnabled(true);
				menuAlgorithm12.setEnabled(true);
				menuAlgorithm13.setEnabled(true);
				menuAlgorithm14.setEnabled(true);
				menuAlgorithm15.setEnabled(true);
				menuAlgorithm16.setEnabled(true);
				menuAlgorithm17.setEnabled(true);
				menuAlgorithm21.setEnabled(true);
				menuAlgorithm22.setEnabled(true);
				menuAlgorithm23.setEnabled(true);
				menuAlgorithm24.setEnabled(true);
				menuAlgorithm25.setEnabled(true);
				guiRadioAlgorithm11.setEnabled(true);
				guiRadioAlgorithm12.setEnabled(true);
				guiRadioAlgorithm13.setEnabled(true);
				guiRadioAlgorithm14.setEnabled(true);
				guiRadioAlgorithm15.setEnabled(true);
				guiRadioAlgorithm16.setEnabled(true);
				guiRadioAlgorithm17.setEnabled(true);
				guiRadioAlgorithm21.setEnabled(true);
				guiRadioAlgorithm22.setEnabled(true);
				guiRadioAlgorithm23.setEnabled(true);
				guiRadioAlgorithm24.setEnabled(true);
				guiRadioAlgorithm25.setEnabled(true);
				guiComboBoxInterpolation.setEnabled(true);
				menuMosaicGenerate.setEnabled(true);
				buttonMosaicGenerate.setEnabled(true);
				guiThreeDEffect.setEnabled(true);
				if (!guiRadioAlgorithm25.isSelected()) {
					guiStatistic.setEnabled(true);
				}
				break;
			}
			// output
			case 30: {
				// sets information text
				showInfo(textbundle.getString("output_mainWindow_28"));
				// sets option panel 3
				guiPanelRightArea.remove(guiPanelOptions2);
				guiPanelRightArea.add(guiPanelOptions3, BorderLayout.CENTER);
				guiPanelRightArea.add(guiPanelZoom, BorderLayout.SOUTH);
				// enable/disable buttons, menu items, etc.
				guiZoomSlider1.setEnabled(true);
				guiZoomSlider2.setEnabled(true);
				radioButtonStatus(1, 1);
				radioButtonStatus(2, 5);
				guiPanelOptions3.updateUI();
				menuImageLoad.setEnabled(false);
				menuConfigurationLoad.setEnabled(false);
				menuMosaicDimension.setEnabled(false);
				menuAlgorithm11.setEnabled(false);
				menuAlgorithm12.setEnabled(false);
				menuAlgorithm13.setEnabled(false);
				menuAlgorithm14.setEnabled(false);
				menuAlgorithm15.setEnabled(false);
				menuAlgorithm16.setEnabled(false);
				menuAlgorithm17.setEnabled(false);
				menuAlgorithm21.setEnabled(false);
				menuAlgorithm22.setEnabled(false);
				menuAlgorithm23.setEnabled(false);
				menuAlgorithm24.setEnabled(false);
				menuAlgorithm25.setEnabled(false);
				guiComboBoxInterpolation.setEnabled(false);
				menuMosaicGenerate.setEnabled(false);
				menuGrafic.setEnabled(true);
				menuXml.setEnabled(true);
				menuBuildingInstruction.setEnabled(true);
				menuMaterial.setEnabled(true);
				menuConfiguration.setEnabled(true);
				menuDocumentGenerate.setEnabled(true);
				guiThreeDEffect.setEnabled(false);
				guiStatistic.setEnabled(false);
				break;
			}
			// disable gui while generating output documents
			case 31: {
				// enable/disable buttons, menu items, etc.
				guiZoomSlider1.setEnabled(false);
				guiZoomSlider2.setEnabled(false);
				menuGrafic.setEnabled(false);
				menuXml.setEnabled(false);
				menuBuildingInstruction.setEnabled(false);
				menuMaterial.setEnabled(false);
				menuConfiguration.setEnabled(false);
				guiOutputGrafic.setEnabled(false);
				guiOutputXml.setEnabled(false);
				guiOutputBuildingInstruction.setEnabled(false);
				guiOutputMaterial.setEnabled(false);
				guiOutputConfiguration.setEnabled(false);
				menuDocumentGenerate.setEnabled(false);
				buttonDocumentsGenerate.setEnabled(false);
				break;
			}
			// enable gui after generating output documents
			case 32: {
				// enable/disable buttons, menu items, etc.
				guiZoomSlider1.setEnabled(true);
				guiZoomSlider2.setEnabled(true);
				menuGrafic.setEnabled(true);
				menuXml.setEnabled(true);
				menuBuildingInstruction.setEnabled(true);
				menuMaterial.setEnabled(true);
				menuConfiguration.setEnabled(true);
				guiOutputGrafic.setEnabled(true);
				guiOutputXml.setEnabled(true);
				guiOutputBuildingInstruction.setEnabled(true);
				guiOutputMaterial.setEnabled(true);
				guiOutputConfiguration.setEnabled(true);
				menuDocumentGenerate.setEnabled(true);
				buttonDocumentsGenerate.setEnabled(true);
				break;
			}
		}
	}

	/**
	 * method: adjustDividerLocation
	 * description: adjust divider location (split pane)
	 *
	 * @author Tobias Reichling
	 */
	private void adjustDividerLocation() {
		guiSplitPane.setDividerLocation(guiSplitPane.getHeight() / 2);
	}

	/**
	 * method: showImageInfo
	 * description: shows the name of the current image
	 *
	 * @author Tobias Reichling
	 * @param fileName
	 */
	private void showImageInfo(String fileName) {
		if (fileName.length() > 18) {
			fileName = fileName.substring(0, 15) + "..." + fileName.substring(fileName.length() - 3, fileName.length());
		}
		guiLabelImage.setText(textbundle.getString("output_mainWindow_20") + ": " + fileName);
	}

	/**
	 * method: showConfigurationInfo
	 * description: shows the name of the current configuration
	 *
	 * @author Tobias Reichling
	 * @param fileName
	 */
	private void showConfigurationInfo(String fileName) {
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
	private void showDimensionInfo(final int widthValue, final int heightValue, final boolean reset) {
		if (reset) {
			guiLabelWidth.setText(textbundle.getString("output_mainWindow_29") + ": ");
			guiLabelHeight.setText(textbundle.getString("output_mainWindow_30") + ": ");
		} else {
			guiLabelWidth.setText(textbundle.getString("output_mainWindow_29") + ": " + widthValue + " "
					+ textbundle.getString("output_mainWindow_31"));
			guiLabelHeight.setText(textbundle.getString("output_mainWindow_30") + ": " + heightValue + " "
					+ textbundle.getString("output_mainWindow_31"));
		}
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
		buttonMosaicNew = new JButton(textbundle.getString("dialog_mainWindow_button_1"));
		// option panel 1
		guiPanelOptions1 = new JPanel(new BorderLayout());
		guiPanelOptions1.setBorder(optionAreaBorder);
		guiPanelOptions1Empty = new JPanel();
		guiPanelOptions1Top = new JPanel(new GridLayout(8, 1));
		buttonImageLoad = new JButton(textbundle.getString("dialog_mainWindow_button_2"));
		buttonImageLoad.addActionListener(this);
		buttonImageLoad.setActionCommand("imageload");
		buttonConfigurationLoad = new JButton(textbundle.getString("dialog_mainWindow_button_3"));
		buttonConfigurationLoad.addActionListener(this);
		buttonConfigurationLoad.setActionCommand("configurationload");
		buttonMosaicDimension = new JButton(textbundle.getString("dialog_mainWindow_button_4"));
		buttonMosaicDimension.addActionListener(this);
		buttonMosaicDimension.setActionCommand("mosaicdimension");
		buttonCutout = new JButton(textbundle.getString("dialog_mainWindow_button_5"));
		buttonCutout.addActionListener(this);
		buttonCutout.setActionCommand("cutout");
		guiLabelImage = new JLabel(textbundle.getString("dialog_mainWindow_label_1") + ": ");
		guiLabelConfiguration = new JLabel(textbundle.getString("dialog_mainWindow_label_9") + ": ");
		guiLabelWidth = new JLabel(textbundle.getString("dialog_mainWindow_label_2") + ": ");
		guiLabelHeight = new JLabel(textbundle.getString("dialog_mainWindow_label_3") + ": ");
		guiPanelOptions1.add(guiPanelOptions1Top, BorderLayout.NORTH);
		guiPanelOptions1.add(guiPanelOptions1Empty, BorderLayout.CENTER);
		guiPanelOptions1.add(buttonCutout, BorderLayout.SOUTH);
		guiPanelOptions1Top.add(buttonImageLoad);
		guiPanelOptions1Top.add(guiLabelImage);
		guiPanelOptions1Top.add(buttonConfigurationLoad);
		guiPanelOptions1Top.add(guiLabelConfiguration);
		guiPanelOptions1Top.add(buttonMosaicDimension);
		guiPanelOptions1Top.add(guiLabelWidth);
		guiPanelOptions1Top.add(guiLabelHeight);
		// option panel 2
		guiPanelOptions2 = new JPanel(new BorderLayout());
		guiPanelOptions2.setBorder(optionAreaBorder);
		if ((System.getProperty("os.name").startsWith("Mac"))) {
			guiPanelOptions2Top = new JPanel(new GridLayout(14, 1, 0, 4));
		} else {
			guiPanelOptions2Top = new JPanel(new GridLayout(14, 1, 0, 0));
		}
		guiPanelOptions2Bottom = new JPanel(new GridLayout(5, 1, 0, 0));
		buttonMosaicGenerate = new JButton(textbundle.getString("dialog_mainWindow_button_6"));
		buttonMosaicGenerate.setActionCommand("mosaicgenerate");
		buttonMosaicGenerate.addActionListener(this);
		guiLabelQuantisation = new JLabel(textbundle.getString("dialog_mainWindow_label_4") + ":");
		guiLabelQuantisation.setFont(new Font(guiLabelQuantisation.getFont().getFontName(), Font.BOLD,
				guiLabelQuantisation.getFont().getSize()));
		guiLabelTiling = new JLabel(textbundle.getString("dialog_mainWindow_label_5") + ":");
		guiLabelTiling.setFont(
				new Font(guiLabelTiling.getFont().getFontName(), Font.BOLD, guiLabelTiling.getFont().getSize()));
		guiLabelSeparator = new JLabel("");
		guiGroupQuantisation = new ButtonGroup();
		guiGroupTiling = new ButtonGroup();
		buttonOutput = new JButton(textbundle.getString("dialog_mainWindow_button_7"));
		buttonOutput.setActionCommand("output");
		buttonOutput.addActionListener(this);
		guiRadioAlgorithm11 = new JRadioButton(textbundle.getString("algorithm_naiveQuantisationRgb"));
		guiRadioAlgorithm12 = new JRadioButton(textbundle.getString("algorithm_errorDiffusion"));
		guiRadioAlgorithm13 = new JRadioButton(textbundle.getString("algorithm_vectorErrorDiffusion"));
		guiRadioAlgorithm14 = new JRadioButton(textbundle.getString("algorithm_patternDithering"));
		guiRadioAlgorithm15 = new JRadioButton(textbundle.getString("algorithm_solidRegions"));
		guiRadioAlgorithm16 = new JRadioButton(textbundle.getString("algorithm_slicing"));
		guiRadioAlgorithm17 = new JRadioButton(textbundle.getString("algorithm_naiveQuantisationLab"));
		guiRadioAlgorithm21 = new JRadioButton(textbundle.getString("algorithm_elementSizeOptimisation"));
		guiRadioAlgorithm22 = new JRadioButton(textbundle.getString("algorithm_moldingOptimisation"));
		guiRadioAlgorithm23 = new JRadioButton(textbundle.getString("algorithm_costsOptimisation"));
		guiRadioAlgorithm24 = new JRadioButton(textbundle.getString("algorithm_stabilityOptimisation"));
		guiRadioAlgorithm25 = new JRadioButton(textbundle.getString("algorithm_basicElementsOnly"));
		guiRadioAlgorithm11.addActionListener(this);
		guiRadioAlgorithm12.addActionListener(this);
		guiRadioAlgorithm13.addActionListener(this);
		guiRadioAlgorithm14.addActionListener(this);
		guiRadioAlgorithm15.addActionListener(this);
		guiRadioAlgorithm16.addActionListener(this);
		guiRadioAlgorithm17.addActionListener(this);
		guiRadioAlgorithm21.addActionListener(this);
		guiRadioAlgorithm22.addActionListener(this);
		guiRadioAlgorithm23.addActionListener(this);
		guiRadioAlgorithm24.addActionListener(this);
		guiRadioAlgorithm25.addActionListener(this);
		guiRadioAlgorithm11.setActionCommand("algorithm11");
		guiRadioAlgorithm11.setSelected(true);
		guiRadioAlgorithm12.setActionCommand("algorithm12");
		guiRadioAlgorithm13.setActionCommand("algorithm13");
		guiRadioAlgorithm14.setActionCommand("algorithm14");
		guiRadioAlgorithm15.setActionCommand("algorithm15");
		guiRadioAlgorithm16.setActionCommand("algorithm16");
		guiRadioAlgorithm17.setActionCommand("algorithm17");
		guiRadioAlgorithm21.setActionCommand("algorithm21");
		guiRadioAlgorithm22.setActionCommand("algorithm22");
		guiRadioAlgorithm23.setActionCommand("algorithm23");
		guiRadioAlgorithm24.setActionCommand("algorithm24");
		guiRadioAlgorithm25.setActionCommand("algorithm25");
		guiRadioAlgorithm25.setSelected(true);
		final Vector<String> guiComboBoxInterpolationsVerfahren = new Vector<>();
		guiComboBoxInterpolationsVerfahren.add(textbundle.getString("dialog_mainWindow_combo_1"));
		guiComboBoxInterpolationsVerfahren.add(textbundle.getString("dialog_mainWindow_combo_2"));
		guiComboBoxInterpolationsVerfahren.add(textbundle.getString("dialog_mainWindow_combo_3"));
		guiComboBoxInterpolation = new JComboBox(guiComboBoxInterpolationsVerfahren);
		guiComboBoxInterpolation.setEditable(false);
		guiComboBoxInterpolation.setEnabled(true);
		guiThreeDEffect = new JCheckBox(textbundle.getString("dialog_mainWindow_check_1"));
		guiThreeDEffect.setEnabled(true);
		guiThreeDEffect.setSelected(true);
		guiStatistic = new JCheckBox(textbundle.getString("dialog_mainWindow_check_2"));
		guiStatistic.setEnabled(true);
		guiStatistic.setSelected(false);
		guiPanelThreeDEffectStatistic = new JPanel(new BorderLayout());
		guiPanelThreeDEffectStatistic.add(guiThreeDEffect, BorderLayout.WEST);
		guiPanelThreeDEffectStatistic.add(guiStatistic, BorderLayout.CENTER);
		guiGroupQuantisation.add(guiRadioAlgorithm11);
		guiGroupQuantisation.add(guiRadioAlgorithm17);
		guiGroupQuantisation.add(guiRadioAlgorithm12);
		guiGroupQuantisation.add(guiRadioAlgorithm13);
		guiGroupQuantisation.add(guiRadioAlgorithm14);
		guiGroupQuantisation.add(guiRadioAlgorithm15);
		guiGroupQuantisation.add(guiRadioAlgorithm16);
		guiGroupTiling.add(guiRadioAlgorithm21);
		guiGroupTiling.add(guiRadioAlgorithm22);
		guiGroupTiling.add(guiRadioAlgorithm23);
		guiGroupTiling.add(guiRadioAlgorithm24);
		guiGroupTiling.add(guiRadioAlgorithm25);
		guiPanelOptions2Top.add(guiLabelQuantisation);
		guiPanelOptions2Top.add(guiRadioAlgorithm11);
		guiPanelOptions2Top.add(guiRadioAlgorithm17);
		guiPanelOptions2Top.add(guiRadioAlgorithm12);
		guiPanelOptions2Top.add(guiRadioAlgorithm13);
		guiPanelOptions2Top.add(guiRadioAlgorithm14);
		guiPanelOptions2Top.add(guiRadioAlgorithm15);
		guiPanelOptions2Top.add(guiRadioAlgorithm16);
		guiPanelOptions2Top.add(guiLabelTiling);
		guiPanelOptions2Top.add(guiRadioAlgorithm25);
		guiPanelOptions2Top.add(guiRadioAlgorithm21);
		guiPanelOptions2Top.add(guiRadioAlgorithm22);
		guiPanelOptions2Top.add(guiRadioAlgorithm23);
		guiPanelOptions2Top.add(guiRadioAlgorithm24);
		guiPanelOptions2Bottom.add(guiPanelThreeDEffectStatistic);
		guiPanelOptions2Bottom.add(guiComboBoxInterpolation);
		guiPanelOptions2Bottom.add(buttonMosaicGenerate);
		guiPanelOptions2Bottom.add(guiLabelSeparator);
		guiPanelOptions2Bottom.add(buttonOutput);
		guiPanelOptions2.add(guiPanelOptions2Top, BorderLayout.NORTH);
		guiPanelOptions2.add(guiPanelOptions2Bottom, BorderLayout.SOUTH);
		// option panel 3
		guiPanelOptions3 = new JPanel(new BorderLayout());
		guiPanelOptions3.setBorder(optionAreaBorder);
		guiPanelOptions3Empty = new JPanel();
		guiPanelOptions3Top = new JPanel(new GridLayout(8, 1));
		buttonDocumentsGenerate = new JButton(textbundle.getString("dialog_mainWindow_button_8"));
		buttonDocumentsGenerate.addActionListener(this);
		buttonDocumentsGenerate.setActionCommand("documentgenerate");
		guiLabelOutput = new JLabel(textbundle.getString("dialog_mainWindow_label_6") + ":");
		guiOutputGrafic = new JCheckBox(textbundle.getString("dialog_mainWindow_check_3"));
		guiOutputXml = new JCheckBox(textbundle.getString("dialog_mainWindow_check_4"));
		guiOutputConfiguration = new JCheckBox(textbundle.getString("dialog_mainWindow_check_5"));
		guiOutputMaterial = new JCheckBox(textbundle.getString("dialog_mainWindow_check_6"));
		guiOutputBuildingInstruction = new JCheckBox(textbundle.getString("dialog_mainWindow_check_7"));
		guiOutputGrafic.addActionListener(this);
		guiOutputGrafic.setActionCommand("guigrafic");
		guiOutputXml.addActionListener(this);
		guiOutputXml.setActionCommand("guixml");
		guiOutputConfiguration.addActionListener(this);
		guiOutputConfiguration.setActionCommand("guiconfiguration");
		guiOutputMaterial.addActionListener(this);
		guiOutputMaterial.setActionCommand("guimaterialliste");
		guiOutputBuildingInstruction.addActionListener(this);
		guiOutputBuildingInstruction.setActionCommand("guibuildinginstruction");
		buttonMosaicNew.addActionListener(this);
		buttonMosaicNew.setActionCommand("mosaicnew");
		guiPanelOptions3.add(guiPanelOptions3Top, BorderLayout.NORTH);
		guiPanelOptions3.add(guiPanelOptions3Empty, BorderLayout.CENTER);
		guiPanelOptions3.add(buttonMosaicNew, BorderLayout.SOUTH);
		guiPanelOptions3Top.add(guiLabelOutput);
		guiPanelOptions3Top.add(guiOutputGrafic);
		guiPanelOptions3Top.add(guiOutputConfiguration);
		guiPanelOptions3Top.add(guiOutputMaterial);
		guiPanelOptions3Top.add(guiOutputBuildingInstruction);
		guiPanelOptions3Top.add(guiOutputXml);
		guiPanelOptions3Top.add(buttonDocumentsGenerate);
		// zoom area
		guiPanelZoom = new JPanel(new GridLayout(4, 1));
		final TitledBorder zoomAreaBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_mainWindow_border_3"));
		zoomAreaBorder.setTitleColor(new Color(100, 100, 100));
		guiPanelZoom.setBorder(zoomAreaBorder);
		guiPanelRightArea.add(guiPanelZoom, BorderLayout.SOUTH);
		guiLabelZoom1 = new JLabel(textbundle.getString("dialog_mainWindow_label_7"));
		guiLabelZoom2 = new JLabel(textbundle.getString("dialog_mainWindow_label_8"));
		guiZoomSlider1 = new JSlider(1, 7, 3);
		guiZoomSlider1Value = 3;
		guiZoomSlider1.setMinorTickSpacing(1);
		guiZoomSlider1.setPaintTicks(true);
		guiZoomSlider1.setSnapToTicks(true);
		guiZoomSlider1.addChangeListener(this);
		guiZoomSlider1.setFocusable(false);
		guiZoomSlider2 = new JSlider(1, 7, 3);
		guiZoomSlider2Value = 3;
		guiZoomSlider2.setMinorTickSpacing(1);
		guiZoomSlider2.setPaintTicks(true);
		guiZoomSlider2.setSnapToTicks(true);
		guiZoomSlider2.addChangeListener(this);
		guiZoomSlider2.setFocusable(false);
		final Dimension d = new Dimension(210, guiLabelZoom1.getHeight());
		guiLabelZoom1.setPreferredSize(d);
		guiLabelZoom2.setPreferredSize(d);
		guiPanelZoom.add(guiLabelZoom1);
		guiPanelZoom.add(guiZoomSlider1);
		guiPanelZoom.add(guiLabelZoom2);
		guiPanelZoom.add(guiZoomSlider2);
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
		guiStatus(10);
		pack();
	}

	/**
	 * method: osStyle
	 * description: look and feel
	 *
	 * @author Tobias Reichling
	 */
	private void osStyle() {
		final String osName = System.getProperty("os.name");
		if (osName.contains("Windows")) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				SwingUtilities.updateComponentTreeUI(this);
			} catch (final UnsupportedLookAndFeelException e) {
				System.err.println(e.toString());
			} catch (final ClassNotFoundException e) {
				System.err.println(e.toString());
			} catch (final InstantiationException e) {
				System.err.println(e.toString());
			} catch (final IllegalAccessException e) {
				System.err.println(e.toString());
			}
		}
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
		menuFile = new JMenu(textbundle.getString("dialog_mainWindow_menu_10"));
		menuNewMosaik = new JMenuItem(textbundle.getString("dialog_mainWindow_menu_11"));
		menuNewMosaik.addActionListener(this);
		menuNewMosaik.setActionCommand("mosaicnew");
		menuFile.add(menuNewMosaik);
		menuImageLoad = new JMenuItem(textbundle.getString("dialog_mainWindow_menu_12"));
		menuImageLoad.addActionListener(this);
		menuImageLoad.setActionCommand("imageload");
		menuFile.add(menuImageLoad);
		menuConfigurationLoad = new JMenuItem(textbundle.getString("dialog_mainWindow_menu_13"));
		menuConfigurationLoad.addActionListener(this);
		menuConfigurationLoad.setActionCommand("configurationload");
		menuFile.add(menuConfigurationLoad);
		menuSettings = new JMenuItem(textbundle.getString("dialog_mainWindow_menu_14"));
		menuSettings.addActionListener(this);
		menuSettings.setActionCommand("settings");
		menuFile.add(menuSettings);
		menuEnd = new JMenuItem(textbundle.getString("dialog_mainWindow_menu_15"));
		menuEnd.addActionListener(this);
		menuEnd.setActionCommand("exit");
		menuFile.add(menuEnd);
		menuBar.add(menuFile);
		// menuPreprocessing
		menuPreprocessing = new JMenu(textbundle.getString("dialog_mainWindow_menu_20"));
		menuMosaicDimension = new JMenuItem(textbundle.getString("dialog_mainWindow_menu_21"));
		menuMosaicDimension.addActionListener(this);
		menuMosaicDimension.setActionCommand("mosaicdimension");
		menuPreprocessing.add(menuMosaicDimension);
		menuBar.add(menuPreprocessing);
		// menuMosaik
		menuMosaic = new JMenu(textbundle.getString("dialog_mainWindow_menu_30"));
		menuGroupQuantisation = new ButtonGroup();
		menuAlgorithm11 = new JRadioButtonMenuItem(textbundle.getString("algorithm_naiveQuantisationRgb"), true);
		menuAlgorithm11.addActionListener(this);
		menuAlgorithm11.setActionCommand("algorithm11");
		menuGroupQuantisation.add(menuAlgorithm11);
		menuMosaic.add(menuAlgorithm11);
		menuAlgorithm17 = new JRadioButtonMenuItem(textbundle.getString("algorithm_naiveQuantisationLab"));
		menuAlgorithm17.addActionListener(this);
		menuAlgorithm17.setActionCommand("algorithm17");
		menuGroupQuantisation.add(menuAlgorithm17);
		menuMosaic.add(menuAlgorithm17);
		menuAlgorithm12 = new JRadioButtonMenuItem(textbundle.getString("algorithm_errorDiffusion"));
		menuAlgorithm12.addActionListener(this);
		menuAlgorithm12.setActionCommand("algorithm12");
		menuGroupQuantisation.add(menuAlgorithm12);
		menuMosaic.add(menuAlgorithm12);
		menuAlgorithm13 = new JRadioButtonMenuItem(textbundle.getString("algorithm_vectorErrorDiffusion"));
		menuAlgorithm13.addActionListener(this);
		menuAlgorithm13.setActionCommand("algorithm13");
		menuGroupQuantisation.add(menuAlgorithm13);
		menuMosaic.add(menuAlgorithm13);
		menuAlgorithm14 = new JRadioButtonMenuItem(textbundle.getString("algorithm_patternDithering"));
		menuAlgorithm14.addActionListener(this);
		menuAlgorithm14.setActionCommand("algorithm14");
		menuGroupQuantisation.add(menuAlgorithm14);
		menuMosaic.add(menuAlgorithm14);
		menuAlgorithm15 = new JRadioButtonMenuItem(textbundle.getString("algorithm_solidRegions"));
		menuAlgorithm15.addActionListener(this);
		menuAlgorithm15.setActionCommand("algorithm15");
		menuGroupQuantisation.add(menuAlgorithm15);
		menuMosaic.add(menuAlgorithm15);
		menuAlgorithm16 = new JRadioButtonMenuItem(textbundle.getString("algorithm_slicing"));
		menuAlgorithm16.addActionListener(this);
		menuAlgorithm16.setActionCommand("algorithm16");
		menuGroupQuantisation.add(menuAlgorithm16);
		menuMosaic.add(menuAlgorithm16);
		menuMosaic.addSeparator();
		menuGroupTiling = new ButtonGroup();
		menuAlgorithm25 = new JRadioButtonMenuItem(textbundle.getString("algorithm_basicElementsOnly"), true);
		menuAlgorithm25.addActionListener(this);
		menuAlgorithm25.setActionCommand("algorithm25");
		menuGroupTiling.add(menuAlgorithm25);
		menuMosaic.add(menuAlgorithm25);
		menuAlgorithm21 = new JRadioButtonMenuItem(textbundle.getString("algorithm_elementSizeOptimisation"));
		menuAlgorithm21.addActionListener(this);
		menuAlgorithm21.setActionCommand("algorithm21");
		menuGroupTiling.add(menuAlgorithm21);
		menuMosaic.add(menuAlgorithm21);
		menuAlgorithm22 = new JRadioButtonMenuItem(textbundle.getString("algorithm_moldingOptimisation"));
		menuAlgorithm22.addActionListener(this);
		menuAlgorithm22.setActionCommand("algorithm22");
		menuGroupTiling.add(menuAlgorithm22);
		menuMosaic.add(menuAlgorithm22);
		menuAlgorithm23 = new JRadioButtonMenuItem(textbundle.getString("algorithm_costsOptimisation"));
		menuAlgorithm23.addActionListener(this);
		menuAlgorithm23.setActionCommand("algorithm23");
		menuGroupTiling.add(menuAlgorithm23);
		menuMosaic.add(menuAlgorithm23);
		menuAlgorithm24 = new JRadioButtonMenuItem(textbundle.getString("algorithm_stabilityOptimisation"));
		menuAlgorithm24.addActionListener(this);
		menuAlgorithm24.setActionCommand("algorithm24");
		menuGroupTiling.add(menuAlgorithm24);
		menuMosaic.add(menuAlgorithm24);
		menuMosaic.addSeparator();
		menuMosaicGenerate = new JMenuItem(textbundle.getString("dialog_mainWindow_menu_31"));
		menuMosaicGenerate.addActionListener(this);
		menuMosaicGenerate.setActionCommand("mosaicgenerate");
		menuMosaic.add(menuMosaicGenerate);
		menuBar.add(menuMosaic);
		// menuOutput
		menuOutput = new JMenu(textbundle.getString("dialog_mainWindow_menu_40"));
		menuGrafic = new JCheckBoxMenuItem(textbundle.getString("dialog_mainWindow_menu_41"));
		menuGrafic.addActionListener(this);
		menuGrafic.setActionCommand("menugrafic");
		menuXml = new JCheckBoxMenuItem(textbundle.getString("dialog_mainWindow_menu_42"));
		menuXml.addActionListener(this);
		menuXml.setActionCommand("menuxml");
		menuBuildingInstruction = new JCheckBoxMenuItem(textbundle.getString("dialog_mainWindow_menu_43"));
		menuBuildingInstruction.addActionListener(this);
		menuBuildingInstruction.setActionCommand("menubuildinginstruction");
		menuMaterial = new JCheckBoxMenuItem(textbundle.getString("dialog_mainWindow_menu_44"));
		menuMaterial.addActionListener(this);
		menuMaterial.setActionCommand("menumaterial");
		menuConfiguration = new JCheckBoxMenuItem(textbundle.getString("dialog_mainWindow_menu_45"));
		menuConfiguration.addActionListener(this);
		menuConfiguration.setActionCommand("menuconfiguration");
		menuOutput.add(menuGrafic);
		menuOutput.add(menuConfiguration);
		menuOutput.add(menuMaterial);
		menuOutput.add(menuBuildingInstruction);
		menuOutput.add(menuXml);
		menuOutput.addSeparator();
		menuDocumentGenerate = new JMenuItem(textbundle.getString("dialog_mainWindow_menu_46"));
		menuDocumentGenerate.addActionListener(this);
		menuDocumentGenerate.setActionCommand("documentgenerate");
		menuOutput.add(menuDocumentGenerate);
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
	}
}
