package pictobrick.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.Configuration;
import pictobrick.model.ElementObject;
import pictobrick.model.Lab;
import pictobrick.model.Mosaic;

/**
 * class: StabilityOptimizer
 * layer: Data processing (three tier architecture)
 * description: tiling with stability optimisation
 *
 * @author Adrian Schuetz
 */
public class StabilityOptimizer implements Tiler {
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private final DataProcessor dataProcessing;
	private final Calculator calculation;
	private int percent = 0;
	private int rows = 0;
	private int colorRow;
	private boolean[][] borders;
	private boolean statisticOutput;
	private final Vector<Object> tilingInfo;
	private int quantisationAlgo;
	private boolean optimisation = false;
	private boolean onlyBorderHandling = true;
	private int maximumGapHeight = 0;
	private int colorCount = 0;
	private final Vector<Object> labColors = new Vector<>();
	private ElementObject doubleElement;
	private int recoloredElements = 0;

	/**
	 * method: StabilityOptimisation
	 * description: contructor
	 *
	 * @author Adrian Schuetz
	 * @param dataProcessing dataProcessing
	 * @param calculation    calculation
	 * @param Vector         tilingInfo
	 */
	public StabilityOptimizer(final DataProcessor dataProcessing, final Calculator calculation,
			final Vector<Object> tilingInfo) {
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
		this.tilingInfo = tilingInfo;
	}

	/**
	 * method: tiling
	 * description: tiling with stability optimisation
	 *
	 * @author Adrian Schuetz
	 * @param mosaicWidth
	 * @param mosaicHeight
	 * @param configuration
	 * @param statistic
	 * @return mosaic
	 */
	public Mosaic tiling(final int mosaicWidth,
			final int mosaicHeight,
			final Configuration configuration,
			final Mosaic mosaic,
			final boolean statistic) {
		this.statisticOutput = statistic;
		this.rows = mosaicHeight;
		final Enumeration<Object> tilingInfoEnum = tilingInfo.elements();
		quantisationAlgo = (Integer) tilingInfoEnum.nextElement();

		// compute colorCount for Floyd-Steinberg 2 colors
		// for all other algorithms: colorVectorlength (colorValue+Name) / 2
		if (quantisationAlgo == 2) {
			this.colorCount = 2;
		} else {
			this.colorCount = (tilingInfo.size() - 4) / 2;
		}

		// Values from the stybility optimisation dialog
		this.optimisation = (Boolean) tilingInfoEnum.nextElement();
		this.onlyBorderHandling = (Boolean) tilingInfoEnum.nextElement();
		this.maximumGapHeight = (Integer) tilingInfoEnum.nextElement();

		final String[] colors = new String[colorCount]; // Array for the colors
		final int[] threshold = new int[colorCount + 1]; // Array for the threshold of the colors

		for (int i = 0; i < colorCount; i++) {
			colors[i] = "";
			threshold[i] = -1;
		}

		// color vector with all colors
		for (final Enumeration<ColorObject> colorsEnum = configuration.getAllColors(); colorsEnum.hasMoreElements();) {
			final ColorObject color = (ColorObject) (colorsEnum.nextElement());
			this.labColors.add(calculation.rgbToLab(color.getRGB()));
			this.labColors.add(color.getName());
		}

		// thresholdarray and colorarray
		if (quantisationAlgo == 2) {// Floyd-Steinberg
			colors[0] = (String) tilingInfoEnum.nextElement();
			colors[1] = (String) tilingInfoEnum.nextElement();
			threshold[0] = 0;
			threshold[1] = 50;
			threshold[2] = 100;
		} else if (quantisationAlgo == 6) {// N-Level
			for (int i = 0; i < colorCount; i++) {
				colors[i] = (String) tilingInfoEnum.nextElement();
				threshold[i] = (Integer) tilingInfoEnum.nextElement();
			}

			// the threshold array is adjusted by the luminance value 100
			// the colors are placed between 2 thresholds
			threshold[colorCount] = 100;
		}

		// the original image (to determine the original colors later)
		final BufferedImage original = calculation.scale(dataProcessing.getImage(false), mosaicWidth, mosaicHeight,
				dataProcessing.getInterpolation());
		final int[][][] originalMatrix = calculation.pixelMatrix(original);
		// Array th remeber the gaps
		borders = new boolean[mosaicHeight][mosaicWidth];

		for (int x = 0; x < mosaicHeight; x++) {
			for (int y = 0; y < mosaicWidth; y++) {
				borders[x][y] = false;
			}
		}

		// Create vector with elements (sorted by stability)
		final Vector<ElementObject> elementsSorted = sortElementsByStability(configuration.getAllElements());

		// Consistency check
		if (!consistencyCheck(configuration) || (optimisation && !consistencyCheckOptimisation(configuration))) {
			// ------------ALTERNATIVE-ALGORITHM------------
			if (optimisation) {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							dataProcessing.errorDialog(textbundle.getString("output_stabilityOptimisation_1")
									+ "\n\r" + textbundle.getString("output_stabilityOptimisation_2")
									+ "\n\r" + textbundle.getString("output_stabilityOptimisation_3")
									+ "\n\r" + textbundle.getString("output_stabilityOptimisation_4"));
						}
					});
				} catch (final Exception e) {
					System.out.println(e.toString());
				}
			} else {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							dataProcessing.errorDialog(textbundle.getString("output_stabilityOptimisation_1")
									+ "\n\r" + textbundle.getString("output_stabilityOptimisation_2")
									+ "\n\r" + textbundle.getString("output_stabilityOptimisation_4"));
						}
					});
				} catch (final Exception e) {
					System.out.println(e.toString());
				}
			}
			for (colorRow = 0; colorRow < mosaicHeight; colorRow++) {
				// assign progress bar controls to the GUI thread
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							percent = (int) ((100.0 / rows) * colorRow);

							if (statisticOutput) {
								dataProcessing.refreshProgressBarAlgorithm(percent, 4);
							} else {
								dataProcessing.refreshProgressBarAlgorithm(percent, 2);
							}
						}
					});
				} catch (final Exception e) {
					System.out.println(e.toString());
				}

				for (int colorColumn = 0; colorColumn < mosaicWidth; colorColumn++) {
					mosaic.setElement(colorRow, colorColumn, configuration.getBasisName(), true);
				}
			}

			return mosaic;
		} else {
			// ------------MAIN-ALGORITHM------------
			// run linear through the image - determine the fitting element for every pixel
			// ------------------------------------------------------------------------
			final Hashtable<String, String> hash = hashInit(configuration);
			String currentColor;
			Vector<String> pixel; // current Pixel
			Vector<String> pixel2;
			Enumeration<ElementObject> sorted;
			// Flag
			int points = -1;
			int pointsFlag = -1;
			int distanceFlag = 100;
			ElementObject elementFlag = new ElementObject();
			boolean elementSet = false;
			boolean elementFits = false;
			ElementObject currentElement;
			int elementsEnd = 0;
			boolean gap = false;
			int counter = 0;
			int left = 0;
			int right = 0;
			// run linear through the image
			// decide which element is placed by 5 criterias
			// 1) element fits
			// 2) element ends at the end of the row or color
			// 3) elementend is not below a gap - points 2
			// 4) element covers a Gap - points 1
			// 5) elementend is as centered as posible between 2 gaps of the row above
			// ------------------------------------------------------------------------------------------
			for (colorRow = 0; colorRow < mosaicHeight; colorRow++) {
				// assign progress bar controls to the GUI thread
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							percent = (int) ((100.0 / rows) * colorRow);

							if (statisticOutput) {
								dataProcessing.refreshProgressBarAlgorithm(percent, 4);
							} else {
								dataProcessing.refreshProgressBarAlgorithm(percent, 2);
							}
						}
					});
				} catch (final Exception e) {
					System.out.println(e.toString());
				}

				for (int colorColumn = 0; colorColumn < mosaicWidth; colorColumn++) {
					// Color information of the current pixel
					pixel = mosaic.getMosaic().get(colorRow).get(colorColumn);

					// ------------------------------------------------------------------------------------------
					// if the pixel is not covered
					if (!pixel.isEmpty()) {
						// Determine the pixelcolor
						currentColor = (String) (pixel.get(0));
						// Enumeration (sorted elements)
						sorted = elementsSorted.elements();
						// Break condition for while
						elementSet = false;
						// Flag variables
						elementFlag = null;
						pointsFlag = -1;
						distanceFlag = 100;

						// ------------------------------------------------------------------------------------------
						// run through elementvector with all elements (sorted by stability)
						while (sorted.hasMoreElements() && !elementSet) {
							// get next element
							currentElement = (ElementObject) sorted.nextElement();
							// Counter for the criterias 3) and 4)
							points = 0;

							// ------------------------------------------------------------------------------------------
							// CHECK 1) element fits
							// test mosaic-borders (bottom and right)
							if (((colorRow + currentElement.getHeight() - 1) < mosaicHeight)
									&& ((colorColumn + currentElement.getWidth()) <= mosaicWidth)) {
								// colormatching with true-values of the element
								// elementFits is set to true
								// if a pixel with another color is found it is set to false
								elementFits = true;

								for (int elementRow = 0; elementRow < currentElement.getHeight(); elementRow++) {
									for (int elementColumn = 0; elementColumn < currentElement
											.getWidth(); elementColumn++) {
										// Only check if the current elementcoordinate contains "true"
										if (currentElement.getMatrix()[elementRow][elementColumn]) {
											pixel2 = mosaic.getMosaic().get(colorRow + elementRow).get(colorColumn
													+ elementColumn);

											if (!pixel2.isEmpty()) {
												if (!((String) (pixel2.get(0))).equals(currentColor)) {
													elementFits = false;
												}
											} else {
												elementFits = false;
											}
										}
									}
								}
							} else {
								elementFits = false;
							}

							// ------------------------------------------------------------------------------------------
							// optimisation: only continue if the extended requirements are fulfilled
							// check elements with height = 3
							// if element ends at the end of the row:
							// test high gaps at the left side of the element
							if ((colorColumn + currentElement.getWidth()) == mosaicWidth
									&& currentElement.getHeight() == 3
									&& colorColumn > 0
									&& colorRow >= (maximumGapHeight - 1) - (currentElement.getHeight() - 1)
									&& bigGap(colorRow, colorColumn,
											(maximumGapHeight - 1) - (currentElement.getHeight() - 1))) {
								elementFits = false;
							}
							// check elements in the mosaic
							if (elementFits && optimisation
									&& ((colorColumn + currentElement.getWidth()) < mosaicWidth)) {
								// only border handling
								// testing rows 2+3 and the last two rows
								// rows 1, 2, third and fourth last row: no elements higher than 1 are allowed
								if (currentElement.getHeight() == 3
										&& (colorRow < 2 || colorRow > mosaicHeight - 5)) {
									elementFits = false;
								}
								if (colorRow == 1 || colorRow == 2 || colorRow == mosaicHeight - 1
										|| colorRow == mosaicHeight - 2) {
									// if the element creates a high gap (left or right side) the element is not
									// fitting
									if ((colorColumn > 0 && bigGap(colorRow, colorColumn, 1))
											|| bigGap(colorRow, colorColumn + currentElement.getWidth(), 1)) {
										elementFits = false;
									}
								} else if (!onlyBorderHandling) {// Rest of the image
									// if the element creates a high gap (left or right side) the element is not
									// fitting
									if (colorRow >= (maximumGapHeight - 1) - (currentElement.getHeight() - 1)) {
										if ((currentElement.getHeight() == 3 && colorColumn > 0
												&& bigGap(colorRow, colorColumn,
														(maximumGapHeight - 1) - (currentElement.getHeight() - 1)))
												|| bigGap(colorRow, colorColumn + currentElement.getWidth(),
														(maximumGapHeight - 1) - (currentElement.getHeight() - 1))) {
											elementFits = false;
										}
									}
								}
								// bais elements must be possible
								if (currentElement.getHeight() == 1 && currentElement.getWidth() == 1) {
									elementFits = true;
								}
							}
							// remember element with height=1 and width=2
							if (currentElement.getWidth() == 2 && currentElement.getHeight() == 1) {
								this.doubleElement = currentElement;
							}
							// END of optimisation
							// ------------------------------------------------------------------------------------------
							// Only continue if the element fits
							if (elementFits) {
								// The criterias 3) and 4) can not be checked in the first row
								// -> set element with best stability
								if (colorRow == 0) {
									// set element
									elementSet = true;
									elementFlag = currentElement;
								} else {
									// All other rows in the mosaic
									// ------------------------------------------------------------------------------------------
									// CHECK 2) element ends at the end of the row or color
									if (colorColumn + currentElement.getWidth() == mosaicWidth) {
										// set element if it ends at the end of the row
										elementSet = true;
										elementFlag = currentElement;
									} else {
										// set element if it ends at the end of the color
										pixel2 = mosaic.getMosaic().get(colorRow)
												.get(colorColumn + currentElement.getWidth());

										if (!pixel2.isEmpty()) {
											if (!(currentColor.equals((String) (pixel2.get(0))))) {
												elementSet = true;
												elementFlag = currentElement;
											}
										} else {// check if there is a element with height=3 in the row above
											pixel2 = mosaic.getMosaic().get(colorRow - 1).get(colorColumn
													+ currentElement.getWidth());

											if (!pixel2.isEmpty()) {
												if (!(currentColor.equals((String) (pixel2.get(0))))) {
													elementSet = true;
													elementFlag = currentElement;
												}
											} else if (colorRow > 1) {// check if there is a element with height=3 (2
																		// rows above)
												pixel2 = mosaic.getMosaic().get(colorRow - 2).get(colorColumn
														+ currentElement.getWidth());

												if (!pixel2.isEmpty()) {
													if (!(currentColor.equals((String) (pixel2.get(0))))) {
														elementSet = true;
														elementFlag = currentElement;
													}
												}
											}
										}
									} // END: if (colorColumn+currentElement.getWidth() == mosaicWidth)
										// ------------------------------------------------------------------------------------------
										// CHECK 3) elementend is not below a gap - points 2
									if ((colorColumn + currentElement.getWidth() < mosaicWidth) && !elementSet) {
										if (!borders[colorRow - 1][colorColumn + currentElement.getWidth()]) {
											points = points + 2; // Endet nicht auf gap
											// ------------------------------------------------------------------------------------------
											// CHECK 5) elementend is as centered as posible between 2 gaps of the row
											// above
											// (only check if the elementend is not below a gap)
											elementsEnd = colorColumn + currentElement.getWidth();
											// find next gap (left above)
											gap = false;
											counter = 1;
											left = 0;
											while (!gap) {
												if (colorColumn + currentElement.getWidth() - counter == 0) {
													left = 0;
													gap = true;
												} else if (borders[colorRow - 1][colorColumn + currentElement.getWidth()
														- counter]) {
													left = colorColumn + currentElement.getWidth() - counter;
													gap = true;
												}
												counter++;
											}
											// find next gap (right above)
											gap = false;
											counter = 1;
											right = mosaicWidth;
											while (!gap) {
												if (colorColumn + currentElement.getWidth() + counter == mosaicWidth) {
													right = mosaicWidth;
													gap = true;
												} else if (borders[colorRow - 1][colorColumn + currentElement.getWidth()
														+ counter]) {
													right = colorColumn + currentElement.getWidth() + counter;
													gap = true;
												}
												counter++;
											}
										}
									} // END: if
										// (mosaic.getMosaic()[colorRow-1][colorColumn+currentElement.getWidth()].size()==0)
										// ------------------------------------------------------------------------------------------
										// CHECK 4) element covers a Gap - points 1
									if (!elementSet) {
										boolean covered = false;
										for (int x = 0; x < (currentElement.getWidth() - 1); x++) {
											if (colorColumn + x + 1 < mosaicWidth) {
												if (borders[colorRow - 1][colorColumn + 1 + x]) {
													covered = true;
												}
											}
										}
										if (covered) {
											points = points + 1; // element covers gap(s)
										}
										// All points for criterias 3) and 4) are set
										// elements with the same points are ordered by criteria 5)
										if (points == pointsFlag) {
											if (Math.abs(Math.abs(elementsEnd - right)
													- Math.abs(elementsEnd - left)) < distanceFlag) {
												distanceFlag = Math.abs(
														Math.abs(elementsEnd - right) - Math.abs(elementsEnd - left));
												pointsFlag = points;
												elementFlag = currentElement;
											}
										} else if (points > pointsFlag) {
											pointsFlag = points;
											elementFlag = currentElement;
											distanceFlag = Math
													.abs(Math.abs(elementsEnd - right) - Math.abs(elementsEnd - left));
										}
									}
									// ------------------------------------------------------------------------------------------
								} // END: if (colorRow==0)
							} // END: if (elementFits)
						} // END: while (sorted.hasMoreElements() && !elementSet)
							// ------------------------------------------------------------------------------------------
							// optimisation:
							// if a basis element should be placed but creates a high gap:
							// a double element (width=2, height=1) ist placed
							// in addition a mixed color is determined
						if (optimisation && elementFlag.getWidth() == 1
								&& colorColumn + elementFlag.getWidth() < mosaicWidth
								// whole image
								&& (!onlyBorderHandling
										&& (colorRow >= (maximumGapHeight - 1)
												&& bigGap(colorRow, colorColumn + elementFlag.getWidth(),
														(maximumGapHeight - 1)))
										// border handling
										|| ((colorRow == 1 || colorRow == 2 || colorRow == mosaicHeight - 1
												|| colorRow == mosaicHeight - 2))
												&& bigGap(colorRow, colorColumn + elementFlag.getWidth(), 1))) {
							elementFlag = this.doubleElement;
							// colors
							Lab color1 = new Lab();
							Lab color2 = new Lab();

							if (quantisationAlgo == 2 && quantisationAlgo == 3) { // Algo mit Fehlerverteilung
								// color 1 und 2: value from the mosaic
								color1 = calculation.rgbToLab(configuration
										.getColor(mosaic.getMosaic().get(colorRow).get(colorColumn).get(0))
										.getRGB());
								color2 = calculation.rgbToLab(configuration.getColor(
										mosaic.getMosaic().get(colorRow).get(colorColumn + elementFlag.getWidth())
												.get(0))
										.getRGB());
							} else { // Algo ohne Fehlerverteilung
								int red = 0, green = 0, blue = 0;
								// color 1: value from the original image
								red = originalMatrix[colorRow][colorColumn][0];
								green = originalMatrix[colorRow][colorColumn][1];
								blue = originalMatrix[colorRow][colorColumn][2];
								color1.setL(calculation.rgbToLab(new Color(red, green, blue)).getL());
								color1.setA(calculation.rgbToLab(new Color(red, green, blue)).getA());
								color1.setB(calculation.rgbToLab(new Color(red, green, blue)).getB());
								// color 2: value from the original image
								red = originalMatrix[colorRow][colorColumn + 1][0];
								green = originalMatrix[colorRow][colorColumn + 1][1];
								blue = originalMatrix[colorRow][colorColumn + 1][2];
								color2.setL(calculation.rgbToLab(new Color(red, green, blue)).getL());
								color2.setA(calculation.rgbToLab(new Color(red, green, blue)).getA());
								color2.setB(calculation.rgbToLab(new Color(red, green, blue)).getB());
							}

							// set current color to mixed color
							if (quantisationAlgo == 2) {// Floyd-Steinberg
								currentColor = computeMixedColor(color1, color2, threshold, colors);
							} else if (quantisationAlgo == 6) {// Slicing
								currentColor = computeMixedColor(color1, color2, threshold, colors);
							} else {// all other
								currentColor = computeMixedColor(color1, color2, labColors);
							}
							// Count recolored pixel for statistic output
							this.recoloredElements++;
						}
						// END optimisation
						// ------------------------------------------------------------------------------------------
						// Check if the same element ist allready in the 2 rows above
						// -> replace the three height=1 elements by one height=3 element
						if (elementFlag.getHeight() == 1) {
							if ((hash.get(elementFlag.getName()) != null)
									&& (colorRow > 1)
									&& ((mosaic.getMosaic().get(colorRow - 1).get(colorColumn).size() != 0)
											&& (((String) (mosaic.getMosaic().get(colorRow - 1).get(colorColumn)
													.get(1)))
													.equals(currentColor))
											&& (((String) (mosaic.getMosaic().get(colorRow - 1).get(colorColumn)
													.get(0)))
													.equals(elementFlag.getName())))
									&& ((mosaic.getMosaic().get(colorRow - 2).get(colorColumn).size() != 0)
											&& (((String) (mosaic.getMosaic().get(colorRow - 2).get(colorColumn)
													.get(1)))
													.equals(currentColor))
											&& (((String) (mosaic.getMosaic().get(colorRow - 2).get(colorColumn)
													.get(0)))
													.equals(elementFlag.getName())))) {
								// set element and set all covered pixels to "null"
								for (int elementRow = 0; elementRow < 3; elementRow++) {
									for (int elementColumn = 0; elementColumn < elementFlag
											.getWidth(); elementColumn++) {
										mosaic.initVector(colorRow - 2 + elementRow, colorColumn + elementColumn);
									}
								}
								mosaic.setElement(colorRow - 2, colorColumn, currentColor, false);
								mosaic.setElement(colorRow - 2, colorColumn, (String) hash.get(elementFlag.getName()),
										true);
								borders[colorRow - 2][colorColumn] = true;
								borders[colorRow - 1][colorColumn] = true;
								borders[colorRow][colorColumn] = true;
							} else {
								// set element and set all covered pixels to "null"
								for (int elementRow = 0; elementRow < elementFlag.getHeight(); elementRow++) {
									for (int elementColumn = 0; elementColumn < elementFlag
											.getWidth(); elementColumn++) {
										mosaic.initVector(colorRow + elementRow, colorColumn + elementColumn);
									}
								}
								mosaic.setElement(colorRow, colorColumn, currentColor, false);
								mosaic.setElement(colorRow, colorColumn, elementFlag.getName(), true);
								borders[colorRow][colorColumn] = true;
							}
						} else { // height =3
							for (int elementRow = 0; elementRow < 3; elementRow++) {
								for (int elementColumn = 0; elementColumn < elementFlag.getWidth(); elementColumn++) {
									mosaic.initVector(colorRow + elementRow, colorColumn + elementColumn);
								}
							}
							mosaic.setElement(colorRow, colorColumn, currentColor, false);
							mosaic.setElement(colorRow, colorColumn, elementFlag.getName(), true);
							borders[colorRow + 2][colorColumn] = true;
							borders[colorRow + 1][colorColumn] = true;
							borders[colorRow][colorColumn] = true;
						}
					} // END: if (!pixel.isEmpty())
						// ------------------------------------------------------------------------------------------
				}
			} // END: Double For
				// assign progress bar controls to the GUI thread
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						if (statisticOutput) {
							dataProcessing.refreshProgressBarAlgorithm(100, 4);
						} else {
							dataProcessing.refreshProgressBarAlgorithm(100, 2);
						}
					}
				});
			} catch (final Exception e) {
				System.out.println(e.toString());
			}

			// ------------------------------------------------------------------------------------------
			// optimisation: statistic output for gaps
			int highGaps = 0; // quantity of high gaps
			int gapCounter = 0; // temp gap counter
			int highestGap = 0; // highest gap
			final int[] gapFlag = new int[mosaicHeight + 1]; // Flag to count the different gap highs
			// Run through mosaic by column
			// Ignore first column (gap in column 0 is the left border)
			for (int i = 1; i < mosaicWidth; i++) {
				for (int j = 0; j < mosaicHeight; j++) {
					if (borders[j][i]) {
						// Remember gap
						gapCounter++;
					} else if (gapCounter > 0) {
						if (gapCounter > maximumGapHeight) {
							highGaps++;
						}
						// Remember highest gap
						if (gapCounter > highestGap) {
							highestGap = gapCounter;
						}
						// gapFlag
						gapFlag[gapCounter] = gapFlag[gapCounter] + 1;
						gapCounter = 0;
					}
				}
				// do not forget gap at the end!
				if (gapCounter > 0) {
					if (gapCounter > maximumGapHeight) {
						highGaps++;
					}
					if (gapCounter > highestGap) {
						highestGap = gapCounter;
					}
					gapFlag[gapCounter] = gapFlag[gapCounter] + 1;
					gapCounter = 0;
				}
			}

			// Recolored pixels in percent
			final int recolored = (int) ((((double) recoloredElements) / ((double) (mosaicWidth * mosaicHeight)))
					* 1000);

			// Output for optimisation and statistic
			if (optimisation) {
				dataProcessing.setInfo(textbundle.getString("output_stabilityOptimisation_5"), 3);
				dataProcessing.setInfo("(" + textbundle.getString("output_stabilityOptimisation_8") + ". "
						+ recolored / 10 + textbundle.getString("output_decimalPoint") + recolored % 10 + " % "
						+ textbundle.getString("output_stabilityOptimisation_9") + ".)", 3);
				dataProcessing.setInfo(textbundle.getString("output_stabilityOptimisation_10") + " " + maximumGapHeight
						+ ": " + highGaps, 3);
				dataProcessing.setInfo("(" + textbundle.getString("output_stabilityOptimisation_11") + " " + highestGap
						+ " " + textbundle.getString("output_stabilityOptimisation_12") + ")", 3);
			} else if (statistic) {
				dataProcessing.setInfo(textbundle.getString("output_stabilityOptimisation_7") + ":", 3);
				dataProcessing.setInfo(textbundle.getString("output_stabilityOptimisation_6"), 3);
				dataProcessing.setInfo(textbundle.getString("output_stabilityOptimisation_10") + " " + maximumGapHeight
						+ ": " + highGaps, 3);
				dataProcessing.setInfo("(" + textbundle.getString("output_stabilityOptimisation_11") + " " + highestGap
						+ " " + textbundle.getString("output_stabilityOptimisation_12") + ")", 3);
			}
			return mosaic;
		}
	}

	/**
	 * method: computeMixedColor
	 * description: computes the mixed color from 2 lab colors
	 * (using color vector)
	 *
	 * @author Adrian Schuetz
	 * @param color1
	 * @param color2
	 * @param colorVector
	 * @return Mischcolor
	 */
	private String computeMixedColor(final Lab color1, final Lab color2, final Vector<Object> colorVector) {
		// remember best fitting color
		String newColor = "";
		// Determine mixed color
		final Lab mixedColor = new Lab();
		mixedColor.setL((color1.getL() + color2.getL()) / 2);
		mixedColor.setA((color1.getA() + color2.getA()) / 2);
		mixedColor.setB((color1.getB() + color2.getB()) / 2);
		// Run through color vector to find the best fitting color
		Lab testColor;
		String testColorName;
		double deviation;
		double smalestDeviation = 500.0;

		for (final Enumeration<Object> colorsEnum = colorVector.elements(); colorsEnum.hasMoreElements();) {
			testColor = (Lab) (colorsEnum.nextElement());
			testColorName = (String) (colorsEnum.nextElement());
			deviation = java.lang.Math.sqrt(java.lang.Math.pow(mixedColor.getL() - testColor.getL(), 2.0) +
					java.lang.Math.pow(mixedColor.getA() - testColor.getA(), 2.0) +
					java.lang.Math.pow(mixedColor.getB() - testColor.getB(), 2.0));

			if (deviation < smalestDeviation) {
				smalestDeviation = deviation;
				newColor = testColorName;
			}
		}

		return newColor;
	}

	/**
	 * method: computeMixedColor
	 * description: computes the mixed color from 2 lab colors
	 * (using threshold arrays).
	 *
	 * @author Adrian Schuetz
	 * @param LAB         color
	 * @param LAB         color
	 * @param colorVector
	 * @return Mischcolor
	 */
	private String computeMixedColor(final Lab color1, final Lab color2, final int[] threshold, final String[] colors) {
		// remember best fitting color
		String newColor = "";
		int indicator = 0;
		// Determine average luminance of the two colors
		final double luminance = ((color1.getL() + color2.getL()) / 2);
		for (int x = 0; x < this.colorCount; x++) {
			if ((threshold[x] <= luminance) && (threshold[x + 1] > luminance)) {
				newColor = colors[x];
				indicator = 1;
			}
		}
		// set color if the luminance value is exact 100.0
		if (indicator == 0) {
			newColor = colors[colorCount - 1];
		}

		return newColor;
	}

	/**
	 * method: bigGap
	 * description: checks if there is a gap higher than the maximum value
	 * if the gap is to high true is returned
	 *
	 * @author Adrian Schuetz
	 * @param colorRow
	 * @param colorColumn
	 * @param maximumGapHeight
	 * @return true or false
	 */
	private boolean bigGap(final int colorRow, final int colorColumn, final int maximumGapHeight) {
		boolean bigGap = true;
		for (int x = 1; x <= maximumGapHeight; x++) {
			if (!borders[colorRow - x][colorColumn]) {
				bigGap = false;
			}
		}
		return bigGap;
	}

	/**
	 * method: hashInit
	 * description: init a hash with same elements of different height
	 * (element with height 1 | same element with height 3)
	 *
	 * @author Adrian Schuetz
	 * @param Configuration
	 * @return Hash
	 */
	private Hashtable<String, String> hashInit(final Configuration configuration) {
		final Hashtable<String, String> hash = new Hashtable<>();
		ElementObject testElement1;
		ElementObject testElement2;
		final Vector<ElementObject> heightOne = new Vector<>();
		final Vector<ElementObject> heightThree = new Vector<>();
		// Enumeration of all elements (split by height)
		// vektor1: elements with height = 1
		// vektor2: elements with height = 3
		final Enumeration<ElementObject> elements = configuration.getAllElements();

		while (elements.hasMoreElements()) {
			testElement1 = (ElementObject) elements.nextElement();

			if (testElement1.getHeight() == 1) {
				heightOne.add(testElement1);
			} else {
				heightThree.add(testElement1);
			}
		}

		// Enumeration of all elements with height = 1
		// check if there is a matching element with height = 3
		// -> put in the hash
		final Enumeration<ElementObject> heightOneEnum = heightOne.elements();
		Enumeration<ElementObject> heightThreeEnum;

		while (heightOneEnum.hasMoreElements()) {
			testElement1 = (ElementObject) heightOneEnum.nextElement();
			heightThreeEnum = heightThree.elements();

			while (heightThreeEnum.hasMoreElements()) {
				testElement2 = (ElementObject) heightThreeEnum.nextElement();

				if (testElement1.getWidth() == testElement2.getWidth()) {
					hash.put(testElement1.getName(), testElement2.getName());
				}
			}
		}

		return hash;
	}

	/**
	 * method: sortElementsByStability
	 * description: sorts the element Vector by stability
	 *
	 * @author Adrian Schuetz
	 * @param elementsUnsorted
	 * @return elementsSorted
	 */
	private Vector<ElementObject> sortElementsByStability(final Enumeration<ElementObject> elementsUnsorted) {
		int stabi = 0;
		boolean included = false;
		int position;
		ElementObject supportElement;
		final Vector<ElementObject> elementsSorted = new Vector<>();

		// Elements sorted by stability
		while (elementsUnsorted.hasMoreElements()) {
			supportElement = ((ElementObject) elementsUnsorted.nextElement());

			if (elementsSorted.size() == 0) {
				elementsSorted.add(supportElement);
			} else {
				stabi = supportElement.getStability();
				position = 0;
				included = false;
				final Enumeration<ElementObject> supportEnum = elementsSorted.elements();

				while (supportEnum.hasMoreElements() && !included) {
					final ElementObject anotherElement = (ElementObject) supportEnum.nextElement();

					if (stabi >= anotherElement.getStability()) {
						elementsSorted.add(position, supportElement);
						included = true;
					} else {
						position++;
					}
				}

				if (!included) {
					elementsSorted.add(supportElement);
				}
			}
		}

		return elementsSorted;
	}

	/**
	 * method: consistencyCheck
	 * description: checks if there is a valid configuration
	 * elements must be rectangles, without holes and the height must be 1 or 3
	 * (Standard-Lego)
	 *
	 * @author Adrian Schuetz
	 * @param configuration
	 * @return true, false
	 */
	private boolean consistencyCheck(final Configuration configuration) {
		ElementObject testElement;

		// check all elements
		for (final Enumeration<ElementObject> currentEnum = configuration.getAllElements(); currentEnum
				.hasMoreElements();) {
			testElement = currentEnum.nextElement();

			if (!(testElement.getHeight() == 1 || testElement.getHeight() == 3)) {
				return false;
			} else {
				// if element is not holohedral, false
				for (int i = 0; i < testElement.getHeight(); i++) {
					for (int j = 0; j < testElement.getWidth(); j++) {
						if (!testElement.getMatrix()[i][j]) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * method: consistencyCheckOptimisation
	 * description: checks if there is a valid configuration for the optimisation
	 * (element width = 2 and height = 1)
	 *
	 * @author Adrian Schuetz
	 * @param configuration
	 * @return true, false
	 */
	private boolean consistencyCheckOptimisation(final Configuration configuration) {
		ElementObject testElement;

		// check all elements
		for (final Enumeration<ElementObject> currentEnum = configuration.getAllElements(); currentEnum
				.hasMoreElements();) {
			testElement = currentEnum.nextElement();

			if ((testElement.getHeight() == 1 && testElement.getWidth() == 2)) {
				return true;
			}
		}

		return false;
	}
}
