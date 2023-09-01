package pictobrick.service;

import java.awt.image.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.*;

import javax.swing.SwingUtilities;

import PicToBrick.model.ColorObject;
import PicToBrick.model.Configuration;
import PicToBrick.model.Lab;
import PicToBrick.model.Mosaic;

/**
 * class: SolidRegions
 * layer: DataProcessing (three tier architecture)
 * description: based on the naive quantisation cielab
 * lonely pixels are deleted with a special filter
 * result: big monochrome regions (solid regions)
 *
 * @author Tobias Reichling
 */
public class SolidRegionsQuantizer
		implements Quantizer {

	private DataProcessor dataProcessing;
	private Calculator calculation;
	private int percent = 0;
	private int rows = 0;
	private int mosaicRow;

	/**
	 * method: SolidRegions
	 * description: constructor
	 *
	 * @author Tobias Reichling
	 * @param data        processing class
	 * @param calculation class
	 */
	public SolidRegionsQuantizer(DataProcessor dataProcessing, Calculator calculation) {
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
	}

	/**
	 * method: quantisation
	 * description: based on the naive quantisation cielab
	 * lonely pixels are deleted with a special filter
	 * result: big monochrome regions (solid regions)
	 *
	 * @author Tobias Reichling
	 * @param image
	 * @param mosaicWidth
	 * @param mosaicHeight
	 * @param configuration
	 * @param mosaic        (empty)
	 * @return mosaic (with color information)
	 */
	public Mosaic quantisation(BufferedImage image,
			int mosaicWidth,
			int mosaicHeight,
			Configuration configuration,
			Mosaic mosaic) {
		this.rows = mosaicHeight;
		// scale image to mosaic dimensions
		BufferedImage cutout = calculation.scale(image, mosaicWidth, mosaicHeight, dataProcessing.getInterpolation());
		String colorName = "";
		// compute pixelMatrix
		int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
		// compute vector of lab colors
		Vector<Object> labColors = new Vector<>();

		for (Enumeration<ColorObject> colorEnumeration = configuration.getAllColors(); colorEnumeration
				.hasMoreElements();) {
			ColorObject color = (ColorObject) (colorEnumeration.nextElement());
			labColors.add(calculation.rgbToLab(color.getRGB()));
			labColors.add(color.getName());
		}

		// working mosaic gets an one pixel wide border
		// so it is not necassary to computes the mosaic border areas in a special way
		String[][] workingMosaic = new String[mosaicHeight + 2][mosaicWidth + 2];
		int red, green, blue;
		Lab lab, lab2;
		String name;
		double difference;

		for (mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
			// submit progress bar refresh-function to the gui-thread
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						percent = (int) ((100.0 / rows) * mosaicRow);
						dataProcessing.refreshProgressBarAlgorithm(percent, 1);
					}
				});
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				double minimumDifference = 500.0;
				red = pixelMatrix[mosaicRow][mosaicColumn][0];
				green = pixelMatrix[mosaicRow][mosaicColumn][1];
				blue = pixelMatrix[mosaicRow][mosaicColumn][2];
				lab = calculation.rgbToLab(new Color(red, green, blue));

				for (Enumeration<Object> colorEnumeration2 = labColors.elements(); colorEnumeration2
						.hasMoreElements();) {
					lab2 = (Lab) (colorEnumeration2.nextElement());
					name = (String) (colorEnumeration2.nextElement());
					difference = java.lang.Math.sqrt(java.lang.Math.pow(lab.getL() - lab2.getL(), 2.0) +
							java.lang.Math.pow(lab.getA() - lab2.getA(), 2.0) +
							java.lang.Math.pow(lab.getB() - lab2.getB(), 2.0));

					if (difference < minimumDifference) {
						minimumDifference = difference;
						colorName = name;
					}
				}

				mosaic.setElement(mosaicRow, mosaicColumn, colorName, false);
				workingMosaic[mosaicRow + 1][mosaicColumn + 1] = colorName;
			}
		}

		// the one pixel wide border is filled with the original border pixel values
		// special cases: border, corner, etc.
		for (int rows = 0; rows < (mosaicHeight + 2); rows++) {
			for (int column = 0; column < (mosaicWidth + 2); column++) {
				// TOP
				if (rows == 0) {
					// TOP-left
					if (column == 0) {
						workingMosaic[rows][column] = workingMosaic[rows + 1][column + 1];
					}
					// TOP-Center
					if (column > 0 && column < (mosaicWidth + 2)) {
						workingMosaic[rows][column] = workingMosaic[rows + 1][column];
					}
					// TOP-right
					if (column == (mosaicWidth + 2)) {
						workingMosaic[rows][column] = workingMosaic[rows + 1][column - 1];
					}
				}
				// CENTER
				if (rows > 0 && rows < (mosaicHeight + 2)) {
					// CENTER-left
					if (column == 0) {
						workingMosaic[rows][column] = workingMosaic[rows][column + 1];
					}
					// CENTER-right
					if (column == (mosaicWidth + 2)) {
						workingMosaic[rows][column] = workingMosaic[rows][column - 1];
					}
				}
				// BOTTOM
				if (rows == (mosaicHeight + 2)) {
					// BOTTOM-left
					if (column == 0) {
						workingMosaic[rows][column] = workingMosaic[rows - 1][column + 1];
					}
					// BOTTOM-Center
					if (column > 0 && column < (mosaicWidth + 2)) {
						workingMosaic[rows][column] = workingMosaic[rows - 1][column];
					}
					// BOTTOM-right
					if (column == (mosaicWidth + 2)) {
						workingMosaic[rows][column] = workingMosaic[rows - 1][column - 1];
					}
				}

			}
		}
		// filter for gerneration big monochromo regions:
		// count how often the current color exists in the near 8 pixels
		// 3x3 filter
		int colorCounter;
		String currentColorName;
		for (int rows = 1; rows < mosaicHeight; rows++) {
			for (int column = 1; column < mosaicWidth; column++) {
				colorCounter = 0;
				currentColorName = workingMosaic[rows][column];
				if (currentColorName.equals(workingMosaic[rows + 1][column])) {
					colorCounter++;
				}
				if (currentColorName.equals(workingMosaic[rows - 1][column])) {
					colorCounter++;
				}
				if (currentColorName.equals(workingMosaic[rows][column + 1])) {
					colorCounter++;
				}
				if (currentColorName.equals(workingMosaic[rows][column - 1])) {
					colorCounter++;
				}
				if (currentColorName.equals(workingMosaic[rows + 1][column + 1])) {
					colorCounter++;
				}
				if (currentColorName.equals(workingMosaic[rows - 1][column - 1])) {
					colorCounter++;
				}
				if (currentColorName.equals(workingMosaic[rows + 1][column - 1])) {
					colorCounter++;
				}
				if (currentColorName.equals(workingMosaic[rows - 1][column + 1])) {
					colorCounter++;
				}
				// a color is changed if it exists less than 2 times
				// the threshold is 2 because a lower or a higher
				// threshold will affect the image quality
				//
				// computes the color which exits more often than other
				// colors in this 3x3 window
				// change the current color to this new color
				if (colorCounter < 2) {
					int colorCounter2;
					String newColor = "";
					int flag = 0;
					String testColor;
					for (int windowRow = (rows - 1); windowRow < (rows + 2); windowRow++) {
						for (int windowColumn = (column - 1); windowColumn < (column + 2); windowColumn++) {
							testColor = workingMosaic[windowRow][windowColumn];
							colorCounter2 = 0;
							for (int windowRow2 = (rows - 1); windowRow2 < (rows + 2); windowRow2++) {
								for (int windowColumn2 = (column - 1); windowColumn2 < (column + 2); windowColumn2++) {
									if (testColor.equals(workingMosaic[windowRow2][windowColumn2])) {
										colorCounter2++;
									}
								}
							}
							if (colorCounter2 > flag) {
								newColor = testColor;
								flag = colorCounter2;
							}
							;
						}
					}
					// change color
					workingMosaic[rows][column] = newColor;
				}
			}
		}
		// transfer color information from working mosaic to original mosaic
		// (caution: dont transfer the color information from the one pixel wide border)
		for (int rows = 1; rows < mosaicHeight; rows++) {
			for (int column = 1; column < mosaicWidth; column++) {
				mosaic.initVector(rows - 1, column - 1);
				mosaic.setElement(rows - 1, column - 1, workingMosaic[rows][column], false);
			}
		}
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					dataProcessing.refreshProgressBarAlgorithm(100, 1);
				}
			});
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return mosaic;
	}
}
