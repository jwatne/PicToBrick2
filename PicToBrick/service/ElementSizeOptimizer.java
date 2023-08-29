package PicToBrick.service;

import java.util.Enumeration;
import javax.swing.*;

import PicToBrick.model.ColorObject;
import PicToBrick.model.Configuration;
import PicToBrick.model.ElementObject;
import PicToBrick.model.Mosaic;

import java.util.*;

/**
 * class: ElementSizeOptimizer
 * layer: Data processing (three tier architecture)
 * description: tiling with element size optimisation
 *
 * @author Adrian Schuetz
 */
public class ElementSizeOptimizer
		implements Tiler {

	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private final DataProcessor dataProcessing;
	private final Calculator calculation;
	private int percent = 0;
	private int referenceValue;
	private int elements = 0;
	private int elementCounter = 0;
	private String[] colorArray;
	private int[][] elementArray;
	private boolean statisticOutput;
	private int totalCosts;

	/**
	 * method: ElementSizeOptimizer
	 * description: contructor
	 *
	 * @author Adrian Schuetz
	 * @param dataProcessing dataProcessing
	 * @param calculation    calculation
	 */
	public ElementSizeOptimizer(final DataProcessor dataProcessing, final Calculator calculation) {
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
	}

	/**
	 * method: tiling
	 * description: tiling with element size optimisation
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
		referenceValue = (mosaicWidth * mosaicHeight) / 100;
		if (referenceValue == 0) {
			referenceValue = 1;
		}
		// if the algorithm runs in the statistic mode
		// some arrays must be initialisized
		if (statistic && dataProcessing.getTilingAlgorithm() == 2) {
			moldingInit(configuration);
		}
		// build vector of all elements
		final Vector elementsSorted = sortElementsBySize(configuration.getAllElements());
		// scan the mosaic per random coordinates
		// compute fitting element for each pixel
		String currentColor = "";
		Enumeration sorted;
		Vector<String> pixel;
		Vector<String> pixel2;
		ElementObject currentElement;
		boolean elementSet = false;
		Vector elementCoords;
		// coordinates / position where the element will be set in the mosaic
		int left = 0;
		int top = 0;
		// flags for the left most "true" in the top row of the element matrix
		int elementLeft = -1;
		int elementTop = -1;
		// current coordinates in the mosaic
		int colorRow, colorColumn;
		// compute random coordinates
		final Vector coords = calculation.randomCoordinates(mosaicWidth, mosaicHeight);
		final Enumeration coordsEnum = coords.elements();

		// scan the mosaic per random coordinates
		while (coordsEnum.hasMoreElements()) {
			colorRow = (Integer) coordsEnum.nextElement();
			colorColumn = (Integer) coordsEnum.nextElement();
			// current pixel flag
			pixel = mosaic.getMosaic().get(colorRow).get(colorColumn);

			// if the current pixel is not reserved by an other element yet
			if (pixel.size() == 1) {
				// check the pixel color
				currentColor = (String) (pixel.get(0));

				// checks if the current pixel must be an 1x1 element
				// (if the 4 neighbor pixel have other colors or if
				// theses pixel are reserved by other elements
				// [this is only performance tuning!]
				if ((colorRow > 0)
						&& (colorRow < mosaicHeight - 1)
						&& (colorColumn > 0)
						&& (colorColumn < mosaicWidth - 1)
						// top:
						// pixel: vector contains 1 object and this ist noch the current color
						&& (((mosaic.getMosaic().get(colorRow - 1).get(colorColumn).size() == 1)
								&& !(((String) (mosaic.getMosaic().get(colorRow - 1).get(colorColumn).get(0)))
										.equals(currentColor)))
								// or:
								// vector contains 2 objects = pixel is reserved
								|| (mosaic.getMosaic().get(colorRow - 1).get(colorColumn).size() != 1))
						// bottom:
						&& (((mosaic.getMosaic().get(colorRow + 1).get(colorColumn).size() == 1)
								&& !(((String) (mosaic.getMosaic().get(colorRow + 1).get(colorColumn).get(0)))
										.equals(currentColor)))
								|| (mosaic.getMosaic().get(colorRow + 1).get(colorColumn).size() != 1))
						// right:
						&& (((mosaic.getMosaic().get(colorRow).get(colorColumn + 1).size() == 1)
								&& !(((String) (mosaic.getMosaic().get(colorRow).get(colorColumn + 1).get(0)))
										.equals(currentColor)))
								|| (mosaic.getMosaic().get(colorRow).get(colorColumn + 1).size() != 1))
						// left:
						&& (((mosaic.getMosaic().get(colorRow).get(colorColumn - 1).size() == 1)
								&& !(((String) (mosaic.getMosaic().get(colorRow).get(colorColumn - 1).get(0)))
										.equals(currentColor)))
								|| (mosaic.getMosaic().get(colorRow).get(colorColumn - 1).size() != 1))) {
					// change current element flag to the basis element
					currentElement = configuration.getElement(configuration.getBasisName());
					// set element to the mosaic
					mosaic.setElement(colorRow, colorColumn, currentElement.getName(), true);
					elements++;
					elementCounter++;
					// if the algorithm runs in the statistic mode (with molding optimisation)
					// we have to count the used elements or the costs
					if (statistic && dataProcessing.getTilingAlgorithm() == 2) {
						countMolding(currentColor, currentElement);
					} else if (statistic && dataProcessing.getTilingAlgorithm() == 3) {
						totalCosts = totalCosts + currentElement.getCosts();
					}
				} else {
					// if the current pixel is no 1x1 element
					// build a size-sorted enumeration of all alements
					sorted = elementsSorted.elements();
					elementSet = false;
					while (sorted.hasMoreElements() && !elementSet) {
						currentElement = (ElementObject) sorted.nextElement();
						// compute random coordinates for the element matrix
						elementCoords = calculation.randomCoordinates(currentElement.getWidth(),
								currentElement.getHeight());
						final Enumeration elementCoordsEnum = elementCoords.elements();
						while (elementCoordsEnum.hasMoreElements() && !elementSet) {
							// compute the position of the element matrix
							// for testing the element with the current mosaic position
							top = (Integer) elementCoordsEnum.nextElement();
							left = (Integer) elementCoordsEnum.nextElement();
							// test only if the the computet position in the element matrix
							// is "true"
							if (currentElement.getMatrix()[top][left]) {
								// check mosaic borders:
								// bottom
								if (((colorRow + currentElement.getHeight() - (top + 1)) < mosaicHeight)
										// top
										&& (colorRow - top >= 0)
										// left
										&& ((colorColumn - left) >= 0)
										// right
										&& ((colorColumn + (currentElement.getWidth() - (left + 1))) < mosaicWidth)) {
									// color matching with all "true" positions of the element matrix
									boolean elementFits = true;
									elementLeft = -1;
									elementTop = -1;

									for (int elementRow = 0; elementRow < currentElement.getHeight(); elementRow++) {
										for (int elementColumn = 0; elementColumn < currentElement
												.getWidth(); elementColumn++) {
											if (currentElement.getMatrix()[elementRow][elementColumn]) {
												// flags: left most "true" position in the top row of the element matrix
												if (elementLeft == -1) {
													elementLeft = elementColumn;
												}

												if (elementTop == -1) {
													elementTop = elementRow;
												}

												pixel2 = mosaic.getMosaic().get((colorRow + elementRow)
														- top).get((colorColumn + elementColumn) - left);

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

									if (elementFits) {
										// set element to mosaic
										elementSet = true;

										for (int elementRow = 0; elementRow < currentElement
												.getHeight(); elementRow++) {
											for (int elementColumn = 0; elementColumn < currentElement
													.getWidth(); elementColumn++) {
												if (currentElement.getMatrix()[elementRow][elementColumn]) {
													mosaic.initVector(((colorRow + elementRow) - top),
															((colorColumn + elementColumn) - left));
												}
											}
										}
										mosaic.setElement(colorRow - top + elementTop, colorColumn - left + elementLeft,
												currentColor, false);
										mosaic.setElement(colorRow - top + elementTop, colorColumn - left + elementLeft,
												currentElement.getName(), true);
										elements = elements + computeElementSize(currentElement);
										elementCounter++;
										// if the algorithm runs in the statistic mode (with molding optimisation)
										// we have to count the used elements or the costs
										if (statistic && dataProcessing.getTilingAlgorithm() == 2) {
											countMolding(currentColor, currentElement);
										} else if (statistic && dataProcessing.getTilingAlgorithm() == 3) {
											totalCosts = totalCosts + currentElement.getCosts();
										}
									}
								}
							}
						}
					}
				}
			}
			// refresh progress bar
			// 2 different bars: normal mode and statistic mode
			if (elements % referenceValue == 0) {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							percent++;
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
			}
		}
		dataProcessing.setInfo(textbundle.getString("output_elementSizeOptimisation_1"), 3);
		if (!statistic) {
			dataProcessing.setInfo(textbundle.getString("output_elementSizeOptimisation_2") + ": " + elementCounter
					+ " " + textbundle.getString("output_elementSizeOptimisation_3") + ".", 3);
		} else {
			// if the algorithm runs in the statistic mode (with molding optimisation)
			// we have to generate special outputs
			if (dataProcessing.getTilingAlgorithm() == 2) {
				ouputMoldingUsage(configuration);
			} else if (dataProcessing.getTilingAlgorithm() == 3) {
				dataProcessing.setInfo(textbundle.getString("output_elementSizeOptimisation_4") + ": "
						+ totalCosts / 100 + textbundle.getString("output_decimalPoint") + totalCosts % 100 + " "
						+ textbundle.getString("output_elementSizeOptimisation_5"), 3);
			}
		}
		// refresh progress bar
		// 2 different bars: normal mode and statistic mode
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
		return mosaic;
	}

	/**
	 * method: countMolding
	 * description: counts the used elements
	 *
	 * @author Adrian Schuetz
	 * @param color
	 * @param element
	 */
	private void countMolding(final String color, final ElementObject element) {
		elementArray[computeColorNumber(color)][computeElementNumber(element)]++;
	}

	/**
	 * method: ouputMoldingUsage
	 * description: returns information about the molding usage
	 *
	 * @author Adrian Schuetz
	 * @param Configuration
	 */
	private void ouputMoldingUsage(final Configuration configuration) {
		final int colorCount = configuration.getQuantityColors();
		int normalUsageAllColors = 0;
		int singleUsageAllColors = 0;
		int normalFracturedAllColors = 0;
		final int singleFracturedAllColors = 0;
		int normalFracturedOneColor = 0;
		// for every color ...
		for (int j = 0; j < colorCount; j++) {
			// decrement the countet elements so often till all element counters
			// are <= 0 (normal moldings and 1er moldings)
			// if one element counter is <= 0, a new counter for partially fractured
			// moldings is incremented
			normalFracturedOneColor = 0;
			while (elementArray[j][1] > 0
					|| elementArray[j][2] > 0
					|| elementArray[j][3] > 0
					|| elementArray[j][4] > 0) {
				normalUsageAllColors++;
				if (!(elementArray[j][1] > 0
						&& elementArray[j][2] > 0
						&& elementArray[j][3] > 0
						&& elementArray[j][4] > 0)) {
					normalFracturedOneColor++;
				}
				elementArray[j][0] = elementArray[j][0] - 2;
				elementArray[j][1] = elementArray[j][1] - 16;
				elementArray[j][2] = elementArray[j][2] - 12;
				elementArray[j][3] = elementArray[j][3] - 4;
				elementArray[j][4] = elementArray[j][4] - 8;
			}
			if (normalFracturedOneColor > 0) {
				normalFracturedOneColor--;
			}
			normalFracturedAllColors = normalFracturedAllColors + normalFracturedOneColor;
			while (elementArray[j][0] > 0) {
				singleUsageAllColors++;
				elementArray[j][0] = elementArray[j][0] - 20;
			}
		}
		// calculation costs, etc.
		final int costs = normalUsageAllColors * 27 + singleUsageAllColors * 10;
		final int euro = costs / 100;
		final int cent = costs % 100;
		String centText;
		if (cent < 10) {
			centText = "0" + cent;
		} else {
			centText = "" + cent;
		}
		final int percentUsage = (int) (100 * ((normalFracturedAllColors + singleFracturedAllColors)
				/ ((normalUsageAllColors + singleUsageAllColors) / 100.0)));
		// output costs, etc.
		dataProcessing.setInfo(textbundle.getString("output_elementSizeOptimisation_6") + ": "
				+ (normalUsageAllColors + singleUsageAllColors) + " "
				+ textbundle.getString("output_elementSizeOptimisation_7") + ". "
				+ (normalFracturedAllColors + singleFracturedAllColors)
				+ " (" + textbundle.getString("output_elementSizeOptimisation_8") + ". " + (percentUsage / 100)
				+ textbundle.getString("output_decimalPoint") + (percentUsage % 100) + " %) "
				+ textbundle.getString("output_elementSizeOptimisation_9") + ".", 3);
		dataProcessing.setInfo(textbundle.getString("output_elementSizeOptimisation_10") + " " + normalUsageAllColors
				+ " " + textbundle.getString("output_elementSizeOptimisation_11") + " " + singleUsageAllColors
				+ " " + textbundle.getString("output_elementSizeOptimisation_12") + ": " + euro
				+ textbundle.getString("output_decimalPoint") + centText + " "
				+ textbundle.getString("output_elementSizeOptimisation_13"), 3);
	}

	/**
	 * method: computeElementNumber
	 * description: returns a element number
	 *
	 * @author Adrian Schuetz
	 * @return Nummer
	 */
	private int computeElementNumber(final ElementObject supportElement) {
		int counter = 0;
		for (int row = 0; row < supportElement.getHeight(); row++) {
			for (int column = 0; column < supportElement.getWidth(); column++) {
				if (supportElement.getMatrix()[row][column]) {
					counter++;
				}
			}
		}
		switch (counter) {
			case 1: {
				return 0;
			}
			case 2: {
				return 1;
			}
			case 3: {
				if (supportElement.getWidth() == 2) {
					return 3;
				} else {
					return 2;
				}
			}
			case 4: {
				return 4;
			}
			default: {
				return -2;
			}
		}
	}

	/**
	 * method: computeColorNumber
	 * description: returns a color number
	 *
	 * @author Adrian Schuetz
	 * @param name
	 * @return number of color
	 */
	private int computeColorNumber(final String name) {
		for (int x = 0; x < colorArray.length; x++) {
			if (colorArray[x].equals(name)) {
				return x;
			}
		}
		return -1;
	}

	/**
	 * method: moldingInit
	 * description: init arrays for statistic usage with molding optimisation
	 *
	 * @author Adrian Schuetz
	 * @param configuration
	 */
	private void moldingInit(final Configuration configuration) {
		final Enumeration colorEnum = configuration.getAllColors();
		final int colorCount = configuration.getQuantityColors();
		// init array color
		colorArray = new String[colorCount];
		for (int i = 0; i < colorCount; i++) {
			colorArray[i] = ((ColorObject) colorEnum.nextElement()).getName();
		}
		// init array element counter
		elementArray = new int[colorCount][5];
		for (int j = 0; j < colorCount; j++) {
			elementArray[j][0] = 0; // 0 => 1x1
			elementArray[j][1] = 0; // 1 => 1x2
			elementArray[j][2] = 0; // 2 => 1x3
			elementArray[j][3] = 0; // 3 => corner
			elementArray[j][4] = 0; // 4 => 2x2
		}
	}

	/**
	 * method: sortElementsBySize
	 * description: sorts the element vektor (size)
	 *
	 * @author Adrian Schuetz
	 * @param elementsUnsorted
	 * @return elementsSorted
	 */
	private Vector sortElementsBySize(final Enumeration elementsUnsorted) {
		int size = 0;
		boolean included = false;
		int position;
		ElementObject supportElement;
		final Vector elementsSorted = new Vector();
		// sort element (by size)
		while (elementsUnsorted.hasMoreElements()) {
			supportElement = ((ElementObject) elementsUnsorted.nextElement());
			if (elementsSorted.size() == 0) {
				elementsSorted.add(supportElement);
			} else {
				size = computeElementSize(supportElement);
				position = 0;
				included = false;
				final Enumeration supportEnum = elementsSorted.elements();
				while (supportEnum.hasMoreElements() && !included) {
					final ElementObject anotherElement = (ElementObject) supportEnum.nextElement();
					if (size >= computeElementSize(anotherElement)) {
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
	 * method: computeElementSize
	 * description: computes the size of an element
	 *
	 * @author Adrian Schuetz
	 * @param element
	 * @return size
	 */
	private int computeElementSize(final ElementObject element) {
		int result = 0;
		for (int row = 0; row < element.getHeight(); row++) {
			for (int column = 0; column < element.getWidth(); column++) {
				if (element.getMatrix()[row][column]) {
					result++;
				}
			}
		}
		return result;
	}
}
