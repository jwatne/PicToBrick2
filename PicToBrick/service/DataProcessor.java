package PicToBrick.service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.*;
import java.awt.image.*;

import javax.swing.SwingUtilities;

import PicToBrick.model.Configuration;
import PicToBrick.model.DataManagement;
import PicToBrick.model.Mosaic;
import PicToBrick.ui.MainWindow;

/**
 * class: DataProcessor
 * layer: Data processing (three tier architecture)
 * description: Provides appropriate methods
 *
 * @author Adrian Schuetz
 */
public class DataProcessor {
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private final DataManagement dataManagement;
	private final MainWindow mainWindow;
	private final OutputFileGenerator outputFiles;
	private final Calculator calculation;
	private final DataProcessor dataProcessing;
	private Vector<String> info1, info2;
	private Vector quantisationInfo;
	private Vector tilingInfo;
	private boolean threeDEffect, statisticOutput;
	private int quantisationAlgo, tilingAlgo;
	private NaiveRgbQuantizer naiveQuantisation;
	private FloydSteinbergQuantizer floydSteinberg;
	private NaiveLabQuantizer naiveQuantisationLab;
	private Slicer slicing;
	private SolidRegionsQuantizer solidRegions;
	private PatternDitheringQuantizer patternDithering;
	private VectorErrorDiffuser vectorErrorDiffusion;
	private BasicElementsOnlyTiler basicElementsOnly;
	private StabilityOptimizer stabilityOptimisation;
	private CostsOptimizer costsOptimisation;
	private MoldingOptimizer moldingOptimisation;
	private ElementSizeOptimizer elementSizeOptimisation;
	private Mosaic statisticMosaic;
	private int interpolation;

	/**
	 * method: DataProcessing
	 * description: constructor
	 *
	 * @author Adrian Schuetz
	 * @param mainWindow
	 */
	public DataProcessor(final MainWindow mainWindow) {
		dataManagement = new DataManagement(this);
		this.mainWindow = mainWindow;
		outputFiles = new OutputFileGenerator(this);
		calculation = new Calculator();
		dataProcessing = this;
		initInfo();
	}

	/**
	 * method: generateMosaic
	 * description: generate mosaic
	 *
	 * @author Adrian Schuetz
	 * @param mosaicWidth
	 * @param mosaicHeight
	 * @param quantisation
	 * @param tiling
	 * @param threeD
	 * @param statistic
	 */
	public void generateMosaic(final int mosaicWidth, final int mosaicHeight,
			final int quantisation, final int tiling,
			final boolean threeD, final boolean statistic) {
		initInfo();
		this.threeDEffect = threeD;
		this.statisticOutput = statistic;
		this.tilingAlgo = tiling;
		this.quantisationAlgo = quantisation;
		dataManagement.generateMosaic(mosaicWidth, mosaicHeight);
		// Vectors for dialog imput
		quantisationInfo = new Vector();
		tilingInfo = new Vector();
		// Call dialogs -----------------------------------------------------------
		switch (quantisationAlgo) {
			case 1: {
				break;
			}
			case 2: {
				// FloydSteinberg
				quantisationInfo = mainWindow
						.dialogFloydSteinberg(dataManagement.getCurrentConfiguration().getAllColors());
				break;
			}
			case 3: {
				break;
			}
			case 4: {
				// Pattern dithering
				quantisationInfo = mainWindow.dialogPatternDithering();
				break;
			}
			case 5: {
				break;
			}
			case 6: {
				// Slicing
				quantisationInfo = mainWindow.dialogSlicingThreshold(mainWindow.dialogSlicingColor(),
						dataManagement.getCurrentConfiguration().getAllColors());
				break;
			}
			case 7: {
				break;
			}
			default: {
				break;
			}
		}
		switch (tilingAlgo) {
			case 1: {
				break;
			}
			case 2: {
				// moldingOptimisation
				// Optimisation dialog only in combination with the 3 methods below
				if (quantisationAlgo == 2) {
					tilingInfo = mainWindow.dialogMoldingOptimisation(2,
							textbundle.getString("algorithm_errorDiffusion"));
				} else if (quantisationAlgo == 3) {
					tilingInfo = mainWindow.dialogMoldingOptimisation(3,
							textbundle.getString("algorithm_vectorErrorDiffusion"));
				} else if (quantisationAlgo == 4) {
					tilingInfo = mainWindow.dialogMoldingOptimisation(4,
							textbundle.getString("algorithm_patternDithering"));
				} else
					break;
			}
			case 3: {
				break;
			}
			case 4: {
				// Stability
				tilingInfo = mainWindow.dialogStabilityOptimisation();
				tilingInfo.insertElementAt(quantisationAlgo, 0);
				if (quantisationAlgo == 2) {
					tilingInfo.add(quantisationInfo.elementAt(0));
					tilingInfo.add(quantisationInfo.elementAt(1));
				} else if (quantisationAlgo == 6) {
					final Enumeration quantisationInfoEnum = quantisationInfo.elements();
					while (quantisationInfoEnum.hasMoreElements()) {
						tilingInfo.add(quantisationInfoEnum.nextElement());
					}
				}
				break;
			}
			default: {
				break;
			}
		}
		// Algorithms
		// ----------------------------------------------------------------------------
		mainWindow.refreshProgressBarAlgorithm(0, 1);
		mainWindow.refreshProgressBarAlgorithm(0, 2);
		mainWindow.refreshProgressBarAlgorithm(0, 4);
		mainWindow.refreshProgressBarAlgorithm(0, 3);
		mainWindow.setStatusProgressBarAlgorithm(statisticOutput);
		mainWindow.showProgressBarAlgorithm();
		// SwingWorker
		// "construct": all commands are startet in a new thread
		// "finished": all commands are queued to the gui thread
		// after finshing aforesaid (construct-)thread
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				switch (quantisationAlgo) {
					case 1: {
						naiveQuantisation = new NaiveRgbQuantizer(dataProcessing, calculation);
						naiveQuantisation.quantisation(dataManagement.getImage(false),
								dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance());
						naiveQuantisation = null;
						break;
					}
					case 2: {
						floydSteinberg = new FloydSteinbergQuantizer(dataProcessing, calculation, quantisationInfo);
						floydSteinberg.quantisation(dataManagement.getImage(false),
								dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance());
						floydSteinberg = null;
						break;
					}
					case 3: {
						vectorErrorDiffusion = new VectorErrorDiffuser(dataProcessing, calculation);
						vectorErrorDiffusion.quantisation(dataManagement.getImage(false),
								dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance());
						vectorErrorDiffusion = null;
						break;
					}
					case 4: {
						patternDithering = new PatternDitheringQuantizer(dataProcessing, calculation, quantisationInfo);
						patternDithering.quantisation(dataManagement.getImage(false),
								dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance());
						patternDithering = null;
						break;
					}
					case 5: {
						solidRegions = new SolidRegionsQuantizer(dataProcessing, calculation);
						solidRegions.quantisation(dataManagement.getImage(false),
								dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance());
						solidRegions = null;
						break;
					}
					case 6: {
						slicing = new Slicer(dataProcessing, calculation, quantisationInfo);
						slicing.quantisation(dataManagement.getImage(false),
								dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance());
						slicing = null;
						break;
					}
					case 7: {
						naiveQuantisationLab = new NaiveLabQuantizer(dataProcessing, calculation);
						naiveQuantisationLab.quantisation(dataManagement.getImage(false),
								dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance());
						naiveQuantisationLab = null;
						break;
					}
					default: {
						break;
					}
				}
				return true;
			}

			public void finished() {
				dataProcessing.tileMosaic(tilingAlgo, threeDEffect);
			}
		};
		worker.start();
	}

	/**
	 * method: tileMosaic
	 * description: Tiles the mosaic
	 * (own methode because of "threads")
	 *
	 * @author Adrian Schuetz
	 * @param tiling (algorithm)
	 */
	public void tileMosaic(final int tiling, final boolean threeD) {
		this.threeDEffect = threeD;
		this.tilingAlgo = tiling;
		// SwingWorker
		// "construct": all commands are startet in a new thread
		// "finished": all commands are queued to the gui thread
		// after finshing aforesaid (construct-)thread
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				switch (tilingAlgo) {
					case 1: {
						elementSizeOptimisation = new ElementSizeOptimizer(dataProcessing, calculation);
						elementSizeOptimisation.tiling(dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance(),
								false);
						elementSizeOptimisation = null;
						// Compare the algorithm with method Basic elements only
						// for statistic evaluation
						if (statisticOutput) {
							try {
								SwingUtilities.invokeAndWait(new Runnable() {
									public void run() {
										mainWindow.refreshProgressBarAlgorithm(50, 4);
									}
								});
							} catch (final Exception e) {
								System.out.println(e.toString());
							}
							dataProcessing.setInfo(textbundle.getString("output_dataProcessing_1") + ":", 3);
							dataProcessing.setInfo(textbundle.getString("output_dataProcessing_2"), 3);
							dataProcessing.setInfo(textbundle.getString("output_dataProcessing_3") + ": "
									+ (dataManagement.getMosaicWidth() * dataManagement.getMosaicHeight()) + " "
									+ textbundle.getString("output_dataProcessing_4") + ".", 3);
							try {
								SwingUtilities.invokeAndWait(new Runnable() {
									public void run() {
										mainWindow.refreshProgressBarAlgorithm(100, 4);
									}
								});
							} catch (final Exception e) {
								System.out.println(e.toString());
							}
						}
						break;
					}
					case 2: {
						// Copy the mosaic for statistic evaluation
						if (statisticOutput) {
							statisticMosaic = new Mosaic(dataManagement.getMosaicWidth(),
									dataManagement.getMosaicHeight(), dataManagement);
							statisticMosaic.setMosaic(dataManagement.mosaicCopy());
						}
						// now the real algorithm
						moldingOptimisation = new MoldingOptimizer(dataProcessing, calculation, tilingInfo);
						moldingOptimisation.tiling(dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance(),
								false);
						moldingOptimisation = null;
						// If the user chooses the improved molding optimisation:
						// Statistic evaluation with molding optimisation (without improvements)
						// else statistic evaluation with element size optimisation
						if (statisticOutput) {
							dataProcessing.setInfo(textbundle.getString("output_dataProcessing_1") + ":", 3);
							if (tilingInfo.isEmpty() || ((String) tilingInfo.elementAt(0)).equals("no")) {
								// Element size optimisation
								elementSizeOptimisation = new ElementSizeOptimizer(dataProcessing, calculation);
								elementSizeOptimisation.tiling(dataManagement.getMosaicWidth(),
										dataManagement.getMosaicHeight(),
										dataManagement.getCurrentConfiguration(),
										statisticMosaic,
										true);
								elementSizeOptimisation = null;
							} else {
								// Molding optimisation (without improvements)
								final Vector no = new Vector();
								no.add("no");
								moldingOptimisation = new MoldingOptimizer(dataProcessing, calculation, no);
								moldingOptimisation.tiling(dataManagement.getMosaicWidth(),
										dataManagement.getMosaicHeight(),
										dataManagement.getCurrentConfiguration(),
										statisticMosaic,
										true);
								moldingOptimisation = null;
							}
						}
						break;
					}
					case 3: {
						// Copy the mosaic for statistic evaluation
						if (statisticOutput) {
							statisticMosaic = new Mosaic(dataManagement.getMosaicWidth(),
									dataManagement.getMosaicHeight(), dataManagement);
							statisticMosaic.setMosaic(dataManagement.mosaicCopy());
						}
						costsOptimisation = new CostsOptimizer(dataProcessing, calculation);
						costsOptimisation.tiling(dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance(),
								false);
						costsOptimisation = null;
						// Statistic evaluation with element size optimisation
						if (statisticOutput) {
							dataProcessing.setInfo(textbundle.getString("output_dataProcessing_1") + ":", 3);
							elementSizeOptimisation = new ElementSizeOptimizer(dataProcessing, calculation);
							elementSizeOptimisation.tiling(dataManagement.getMosaicWidth(),
									dataManagement.getMosaicHeight(),
									dataManagement.getCurrentConfiguration(),
									statisticMosaic,
									true);
							elementSizeOptimisation = null;
						}
						break;
					}
					case 4: {
						// Only print statistic if improvements are selected
						if (statisticOutput) {
							// Copy the mosaic for statistic evaluation
							if ((Boolean) tilingInfo.elementAt(1) && !((Boolean) tilingInfo.elementAt(2))) {
								statisticMosaic = new Mosaic(dataManagement.getMosaicWidth(),
										dataManagement.getMosaicHeight(), dataManagement);
								statisticMosaic.setMosaic(dataManagement.mosaicCopy());
							} else {
								try {
									SwingUtilities.invokeAndWait(new Runnable() {
										public void run() {
											// deactivate statistic of normal algorithm (without improvements) is
											// selected
											mainWindow.errorDialog(textbundle.getString("output_dataProcessing_5")
													+ "\n\r" + textbundle.getString("output_dataProcessing_6") + ".");
											mainWindow.disableStatisticButton();
										}
									});
								} catch (final Exception e) {
									System.out.println(e.toString());
								}
							}
						}
						stabilityOptimisation = new StabilityOptimizer(dataProcessing, calculation, tilingInfo);
						stabilityOptimisation.tiling(dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance(),
								false);
						stabilityOptimisation = null;
						// statistic only for whole image improvements
						if (statisticOutput && !((Boolean) tilingInfo.elementAt(2))) {
							final Vector noOptimisation = new Vector();
							noOptimisation.add(0);
							noOptimisation.add(false);
							noOptimisation.add(false);
							noOptimisation.add((Integer) tilingInfo.elementAt(3));
							stabilityOptimisation = new StabilityOptimizer(dataProcessing, calculation, noOptimisation);
							stabilityOptimisation.tiling(dataManagement.getMosaicWidth(),
									dataManagement.getMosaicHeight(),
									dataManagement.getCurrentConfiguration(),
									statisticMosaic,
									true);
							stabilityOptimisation = null;
						}
						break;
					}
					case 5: {
						basicElementsOnly = new BasicElementsOnlyTiler(dataProcessing, calculation);
						basicElementsOnly.tiling(dataManagement.getMosaicWidth(),
								dataManagement.getMosaicHeight(),
								dataManagement.getCurrentConfiguration(),
								dataManagement.getMosaicInstance(),
								false);
						basicElementsOnly = null;
						// no statistic evaluation
						break;
					}
					default: {
						break;
					}
				}
				return true;
			}

			public void finished() {
				dataManagement.generateMosaicImage(threeDEffect);
			}
		};
		worker.start();
	}

	/**
	 * method: showMosaic
	 * description: shows mosaic
	 *
	 * @author Adrian Schuetz
	 */
	public void showMosaic() {
		mainWindow.hideProgressBarAlgorithm();
		mainWindow.showMosaic();
	}

	/**
	 * method: getTilingAlgorithm
	 * description: retruns the tiling algorithm (important for statistic
	 * evaluation)
	 *
	 * @author Adrian Schuetz
	 * @return Number of the algorithm
	 */
	public int getTilingAlgorithm() {
		return this.tilingAlgo;
	}

	/**
	 * method: getInterpolation
	 * description: returns the selected interpolation method
	 *
	 * @author Adrian Schuetz
	 * @return 1=bicubic, 2=bilinear, 3=nearestneighbor
	 */
	public int getInterpolation() {
		return this.interpolation;
	}

	/**
	 * method: setInterpolation
	 * description: sets the interpolation method
	 *
	 * @author Adrian Schuetz
	 * @param 1=bicubic, 2=bilinear, 3=nearestneighbor
	 */
	public void setInterpolation(final int interpolation) {
		this.interpolation = interpolation;
	}

	/**
	 * method: refreshProgressBarAlgorithm
	 * description: changes the display value
	 *
	 * @author Adrian Schuetz
	 * @param value
	 * @param 1:quantisation und 2:tiling
	 */
	public void refreshProgressBarAlgorithm(final int value, final int number) {
		mainWindow.refreshProgressBarAlgorithm(value, number);
	}

	/**
	 * method: refreshProgressBarOutputFiles
	 * description: changes the display value
	 *
	 * @author Adrian Schuetz
	 * @param value
	 * @param number (ID for progressBar)
	 */
	public void refreshProgressBarOutputFiles(final int value, final int number) {
		mainWindow.refreshProgressBarOutputFiles(value, number);
	}

	/**
	 * method: animateGraficProgressBarOutputFiles
	 * description: animates the progressBar
	 *
	 * @author Adrian Schuetz
	 * @param on (/off)
	 */
	public void animateGraficProgressBarOutputFiles(final boolean on) {
		mainWindow.animateGraficProgressBarOutputFiles(on);
	}

	/**
	 * method: getInfo
	 * description: returns an enumation with Information
	 *
	 * @author Adrian Schuetz
	 * @return Enumeration
	 */
	public Enumeration<String> getInfo(final int number) {
		Enumeration<String> infoEnum = null;

		if (number == 1) {
			infoEnum = info1.elements();
		} else if (number == 2) {
			infoEnum = info2.elements();
		}

		return infoEnum;
	}

	/**
	 * method: getMosaicHeight
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return mosaicHeight
	 */
	public int getMosaicHeight() {
		return dataManagement.getMosaicHeight();
	}

	/**
	 * method: getMosaicWidth
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return mosaicWidth
	 */
	public int getMosaicWidth() {
		return dataManagement.getMosaicWidth();
	}

	/**
	 * method: setInfo
	 * description: adds the information to the informationvector
	 *
	 * @author Adrian Schuetz
	 * @param text
	 * @param number = 1: show in dialog
	 *               2: show in output documents
	 *               3: show in both (1 and 2)
	 */
	public void setInfo(final String text, final int number) {
		if (number == 1) {
			info1.addElement(text);
		} else if (number == 2) {
			info2.addElement(text);
		} else {
			info1.addElement(text);
			info2.addElement(text);
		}
	}

	/**
	 * method: initInfo
	 * description: initialise the informationvector
	 *
	 * @author Adrian Schuetz
	 */
	public void initInfo() {
		info1 = new Vector<>();
		info2 = new Vector<>();
	}

	/**
	 * method: getMosaic
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return Vector[][]
	 */
	public Vector[][] getMosaic() {
		return dataManagement.getMosaic();
	}

	/**
	 * method: generateImageOutput
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param image
	 * @param fileName
	 * @return true or false
	 */
	public boolean generateImageOutput(final BufferedImage image, final String fileName) {
		return dataManagement.generateImageOutput(image, fileName);
	}

	/**
	 * method: generateColorOutput
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param color
	 * @param colorName
	 * @return true or false
	 */
	public boolean generateColorOutput(final Color color, final String colorName) {
		return dataManagement.generateColorOutput(color, colorName);
	}

	/**
	 * method: generateUTFOutput
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param content
	 * @param fileName
	 * @param subFolder
	 * @return true or false
	 */
	public boolean generateUTFOutput(final String content, final String fileName, final boolean subFolder) {
		return dataManagement.generateUTFOutput(content, fileName, subFolder);
	}

	/**
	 * method: generateDocuments
	 * description: generates the output documents
	 *
	 * @author Adrian Schuetz
	 * @param image         true, if document should be generated
	 * @param configuration true, if document should be generated
	 * @param material      true, if document should be generated
	 * @param instruction   true, if document should be generated
	 * @param xml           true, if document should be generated
	 * @param infos
	 * @return message (error/sucess)
	 */
	public String generateDocuments(final boolean image, final boolean configuration, final boolean material,
			final boolean instruction, final boolean xml, final Enumeration infos) {
		outputFiles.setProject(dataManagement.generateFolderOutput());
		return outputFiles.generateDocuments(image, configuration, material, instruction, xml, infos);
	}

	/**
	 * method: imageReset
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 */
	public void imageReset() {
		dataManagement.imageReset();
	}

	/**
	 * method: imageLoad
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param file
	 * @return true or false
	 */
	public boolean imageLoad(final File file) {
		return dataManagement.imageLoad(file);
	}

	/**
	 * method: replaceImageByCutout
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param cutout
	 */
	public void replaceImageByCutout(final Rectangle cutout) {
		dataManagement.replaceImageByCutout(cutout);
	}

	/**
	 * method: getScaledImage
	 * description: calls the appropriate method in dataManagement (scaling with
	 * sliderValue)
	 *
	 * @author Adrian Schuetz
	 * @param mosaic
	 * @param sliderValue
	 * @return BufferedImage
	 */
	public BufferedImage getScaledImage(final boolean mosaic, final int sliderValue) {
		return dataManagement.getScaledImage(mosaic, sliderValue);
	}

	/**
	 * method: getImage
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param mosaic
	 * @return BufferedImage
	 */
	public BufferedImage getImage(final boolean mosaic) {
		return dataManagement.getImage(mosaic);
	}

	/**
	 * method: isImage
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return true or false
	 */
	public boolean isImage() {
		return dataManagement.isImage();
	}

	/**
	 * method: isConfiguration
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return true or false
	 */
	public boolean isConfiguration() {
		return dataManagement.isConfiguration();
	}

	/**
	 * method: getSystemConfiguration
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param file
	 * @return configuration
	 */
	public Configuration getSystemConfiguration(final int file) {
		return dataManagement.getSystemConfiguration(file);
	}

	/**
	 * method: setCurrentConfiguration
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param configuration
	 */
	public void setCurrentConfiguration(final Configuration configuration) {
		dataManagement.setCurrentConfiguration(configuration);
	}

	/**
	 * method: getCurrentConfiguration
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return configuration
	 */
	public Configuration getCurrentConfiguration() {
		return dataManagement.getCurrentConfiguration();
	}

	/**
	 * method: configurationSave
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param configuration
	 */
	public void configurationSave(final Configuration configuration) throws IOException {
		dataManagement.configurationSave(configuration);
	}

	/**
	 * method: configurationLoad
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param configuration
	 * @exception IOException
	 */
	public Configuration configurationLoad(final String configuration) throws IOException {
		return dataManagement.configurationLoad(configuration);
	}

	/**
	 * method: computeScaleFactor
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param mosaic
	 * @param width
	 * @param height
	 */
	public void computeScaleFactor(final boolean mosaic, final double width, final double height) {
		dataManagement.computeScaleFactor(mosaic, width, height);
	}

	/**
	 * method: setWorkingDirectory
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param file
	 */
	public void setWorkingDirectory(final File file) {
		dataManagement.setWorkingDirectory(file);
	}

	/**
	 * method: getArbeitsverzeichnis
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return file
	 */
	public File getWorkingDirectory() {
		return dataManagement.getWorkingDirectory();
	}

	/**
	 * method: getConfiguration
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return vector with possible configurations
	 */
	public Vector getConfiguration() {
		return dataManagement.getConfiguration();
	}

	/**
	 * method: getConfigurationDimension
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return dimension
	 */
	public Dimension getConfigurationDimension() {
		return dataManagement.getConfigurationDimension();
	}

	/**
	 * method: errorDialog
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param error
	 */
	public void errorDialog(final String error) {
		mainWindow.errorDialog(error);
	}

	/**
	 * method: saveWorkingDirectory
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @param working directory
	 * @return true or false
	 */
	public boolean saveWorkingDirectory(final File workingDirectory) {
		return dataManagement.saveWorkingDirectory(workingDirectory);
	}

	/**
	 * method: loadWorkingDirectory
	 * description: calls the appropriate method in dataManagement
	 *
	 * @author Adrian Schuetz
	 * @return working directory
	 */
	public File loadWorkingDirectory() {
		return dataManagement.loadWorkingDirectory();
	}
}
