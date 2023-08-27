package PicToBrick.service;

import java.awt.image.*;
import java.awt.*;
import java.util.*;

import javax.swing.SwingUtilities;

import PicToBrick.model.Configuration;
import PicToBrick.model.Mosaic;

/**
 * class: FloydSteinbergQuantizer
 * layer: DataProcessing (three tier architecture)
 * description: quantisation with error diffusion (floyd steinberg) with 2
 * colors
 *
 * @author Adrian Schuetz
 */
public class FloydSteinbergQuantizer
		implements Quantizer {
	private final DataProcessor dataProcessing;
	private final Calculator calculation;
	private final Vector selection;
	private int percent = 0;
	private int rows = 0;
	private int mosaicRow;

	/**
	 * method: FloydSteinberg
	 * description: constructor
	 *
	 * @author Adrian Schuetz
	 * @param data      processing class
	 * @param selection
	 */
	public FloydSteinbergQuantizer(final DataProcessor dataProcessing, final Calculator calculation,
			final Vector selection) {
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
		this.selection = selection;
	}

	/**
	 * method: quantisation
	 * description: quantisation with error diffusion (floyd steinberg) with 2
	 * colors
	 *
	 * @author Adrian Schuetz
	 * @param image
	 * @param mosaicWidth
	 * @param mosaicHeight
	 * @param configuration
	 * @param mosaic        (empty)
	 * @return mosaic (with color information)
	 */
	public Mosaic quantisation(final BufferedImage image,
			final int mosaicWidth,
			final int mosaicHeight,
			final Configuration configuration,
			final Mosaic mosaic) {
		this.rows = mosaicHeight;
		final String dark = (String) selection.get(0);
		final String light = (String) selection.get(1);
		final int method = (Integer) selection.get(2);
		// scale image to mosaic dimensions
		final BufferedImage cutout = calculation.scale(image, mosaicWidth, mosaicHeight,
				dataProcessing.getInterpolation());
		// compute pixelMatrix
		final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
		// compute source image only with luminance values
		final double[][] workingMosaic = new double[mosaicHeight][mosaicWidth];
		int red, green, blue;
		for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				red = pixelMatrix[mosaicRow][mosaicColumn][0];
				green = pixelMatrix[mosaicRow][mosaicColumn][1];
				blue = pixelMatrix[mosaicRow][mosaicColumn][2];
				workingMosaic[mosaicRow][mosaicColumn] = (calculation.rgbToLab(new Color(red, green, blue)).getL());
			}
		}
		double error;
		if (method == 1) {
			// **********************************************************************************************
			// *********************************** FloydSteinberg Scanline
			// **********************************
			// **********************************************************************************************
			for (mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
				// submit progress bar refresh-function to the gui-thread
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							percent = (int) ((100.0 / rows) * mosaicRow);
							dataProcessing.refreshProgressBarAlgorithm(percent, 1);
						}
					});
				} catch (final Exception e) {
					System.out.println(e.toString());
				}
				for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
					// compute color and error
					if (workingMosaic[mosaicRow][mosaicColumn] < 50) {
						mosaic.setElement(mosaicRow, mosaicColumn, dark, false);
						error = 0 - workingMosaic[mosaicRow][mosaicColumn];
					} else {
						mosaic.setElement(mosaicRow, mosaicColumn, light, false);
						error = 100 - workingMosaic[mosaicRow][mosaicColumn];
					}
					// distribute error (caution: mosaic borders!)
					// pixel right: 7/16
					if (!(mosaicColumn == mosaicWidth - 1)) {
						workingMosaic[mosaicRow][mosaicColumn + 1] = errorDistribution(
								workingMosaic[mosaicRow][mosaicColumn + 1], (7.0 / 16.0), error);
					}
					if (!(mosaicRow == mosaicHeight - 1)) {
						// pixel bottom: 5/16
						workingMosaic[mosaicRow + 1][mosaicColumn] = errorDistribution(
								workingMosaic[mosaicRow + 1][mosaicColumn], (5.0 / 16.0), error);
						if (!(mosaicColumn == mosaicWidth - 1)) {
							// pixel bottom right: 1/16
							workingMosaic[mosaicRow + 1][mosaicColumn + 1] = errorDistribution(
									workingMosaic[mosaicRow + 1][mosaicColumn + 1], (1.0 / 16.0), error);
						}
						if (!(mosaicColumn == 0)) {
							// pixel bottom left: 3/16
							workingMosaic[mosaicRow + 1][mosaicColumn - 1] = errorDistribution(
									workingMosaic[mosaicRow + 1][mosaicColumn - 1], (3.0 / 16.0), error);
						}
					}
				}
			}
		} else if (method == 2) {
			// **********************************************************************************************
			// *********************************** FloydSteinberg Serpentines
			// *******************************
			// **********************************************************************************************
			for (mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
				// submit progress bar refresh-function to the gui-thread
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							percent = (int) ((100.0 / rows) * mosaicRow);
							dataProcessing.refreshProgressBarAlgorithm(percent, 1);
						}
					});
				} catch (final Exception e) {
					System.out.println(e.toString());
				}
				for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
					if ((mosaicRow % 2) == 0) {
						// row even -> to the right
						// ---------------------------
						// compute color and error
						if (workingMosaic[mosaicRow][mosaicColumn] < 50) {
							mosaic.setElement(mosaicRow, mosaicColumn, dark, false);
							error = 0 - workingMosaic[mosaicRow][mosaicColumn];
						} else {
							mosaic.setElement(mosaicRow, mosaicColumn, light, false);
							error = 100 - workingMosaic[mosaicRow][mosaicColumn];
						}
						// distribute error (caution: mosaic borders!)
						// pixel right: 7/16
						if (!(mosaicColumn == mosaicWidth - 1)) {
							workingMosaic[mosaicRow][mosaicColumn + 1] = errorDistribution(
									workingMosaic[mosaicRow][mosaicColumn + 1], (7.0 / 16.0), error);
						}
						if (!(mosaicRow == mosaicHeight - 1)) {
							// pixel bottom: 5/16
							workingMosaic[mosaicRow + 1][mosaicColumn] = errorDistribution(
									workingMosaic[mosaicRow + 1][mosaicColumn], (5.0 / 16.0), error);
							if (!(mosaicColumn == mosaicWidth - 1)) {
								// pixel bottom right: 1/16
								workingMosaic[mosaicRow + 1][mosaicColumn + 1] = errorDistribution(
										workingMosaic[mosaicRow + 1][mosaicColumn + 1], (1.0 / 16.0), error);
							}
							if (!(mosaicColumn == 0)) {
								// pixel bottom left: 3/16
								workingMosaic[mosaicRow + 1][mosaicColumn - 1] = errorDistribution(
										workingMosaic[mosaicRow + 1][mosaicColumn - 1], (3.0 / 16.0), error);
							}
						}
					} else {
						// row odd -> to the left
						// ----------------------------
						// compute color and error
						final int column = (mosaicWidth - mosaicColumn) - 1;
						if (workingMosaic[mosaicRow][column] < 50) {
							mosaic.setElement(mosaicRow, column, dark, false);
							error = 0 - workingMosaic[mosaicRow][column];
						} else {
							mosaic.setElement(mosaicRow, column, light, false);
							error = 100 - workingMosaic[mosaicRow][column];
						}
						// distribute error (caution: mosaic borders!)
						// pixel left: 7/16
						if (!((column) == 0)) {
							workingMosaic[mosaicRow][(column) - 1] = errorDistribution(
									workingMosaic[mosaicRow][(column) - 1], (7.0 / 16.0), error);
						}
						if (!(mosaicRow == mosaicHeight - 1)) {
							// pixel bottom: 5/16
							workingMosaic[mosaicRow + 1][(column)] = errorDistribution(
									workingMosaic[mosaicRow + 1][(column)], (5.0 / 16.0), error);
							if (!((column) == mosaicWidth - 1)) {
								// pixel bottom right: 3/16
								workingMosaic[mosaicRow + 1][(column) + 1] = errorDistribution(
										workingMosaic[mosaicRow + 1][(column) + 1], (3.0 / 16.0), error);
							}
							if (!((column) == 0)) {
								// pixel bottom left: 1/16
								workingMosaic[mosaicRow + 1][(column) - 1] = errorDistribution(
										workingMosaic[mosaicRow + 1][(column) - 1], (1.0 / 16.0), error);
							}
						}
					}
				}
			}
		} else if (method == 3) {
			// **********************************************************************************************
			// *********************************** Hilbert curce
			// ********************************************
			// **********************************************************************************************
			final Vector<Integer> coordinates = new Vector<>(calculation.hilbertCoordinates(mosaicWidth, mosaicHeight));
			int x0, x1, x2, x3, x4;
			int y0, y1, y2, y3, y4;
			final int coordinatesNumber = (coordinates.size() / 2);
			final Enumeration<Integer> coordinatesEnum = coordinates.elements();
			// x0 and y0 are the current coordinates
			// x1,y1 ... x4,y4 are the following coordinates
			// ----------------------------------
			// init:
			x0 = (Integer) coordinatesEnum.nextElement();
			y0 = (Integer) coordinatesEnum.nextElement();
			x1 = (Integer) coordinatesEnum.nextElement();
			y1 = (Integer) coordinatesEnum.nextElement();
			x2 = (Integer) coordinatesEnum.nextElement();
			y2 = (Integer) coordinatesEnum.nextElement();
			x3 = (Integer) coordinatesEnum.nextElement();
			y3 = (Integer) coordinatesEnum.nextElement();
			x4 = (Integer) coordinatesEnum.nextElement();
			y4 = (Integer) coordinatesEnum.nextElement();
			int counter = 0;
			percent = 0;
			int referenceValue = (mosaicHeight * mosaicWidth) / 100;
			if (referenceValue == 0) {
				referenceValue = 1;
			}
			for (int i = 0; i < (coordinatesNumber); i++) {
				// refresh progress bar
				if (counter % referenceValue == 0) {
					// submit progress bar refresh-function to the gui-thread
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								percent++;
								dataProcessing.refreshProgressBarAlgorithm(percent, 1);
							}
						});
					} catch (final Exception e) {
						System.out.println(e.toString());
					}
				}
				counter++;
				// compute color and error
				if (workingMosaic[x0][y0] < 50) {
					mosaic.setElement(x0, y0, dark, false);
					error = 0 - workingMosaic[x0][y0];
				} else {
					mosaic.setElement(x0, y0, light, false);
					error = 100 - workingMosaic[x0][y0];
				}
				x0 = x1;
				y0 = y1;
				// distribute error
				// ... 7/16 ... 5/16 ... 3/16 ... 1/16
				// (caution: at the last mosaic pixel, the error can only be distribute
				// to the following last pixels!)
				if (i < (coordinatesNumber - 2)) {
					workingMosaic[x1][y1] = errorDistribution(workingMosaic[x1][y1], (7.0 / 16.0), error);
					x1 = x2;
					y1 = y2;
				}
				if (i < (coordinatesNumber - 3)) {
					workingMosaic[x2][y2] = errorDistribution(workingMosaic[x2][y2], (5.0 / 16.0), error);
					x2 = x3;
					y2 = y3;
				}
				if (i < (coordinatesNumber - 4)) {
					workingMosaic[x3][y3] = errorDistribution(workingMosaic[x3][y3], (3.0 / 16.0), error);
					x3 = x4;
					y3 = y4;
				}
				if (i < (coordinatesNumber - 5)) {
					workingMosaic[x4][y4] = errorDistribution(workingMosaic[x4][y4], (1.0 / 16.0), error);
					x4 = (Integer) coordinatesEnum.nextElement();
					y4 = (Integer) coordinatesEnum.nextElement();
				}
			}
		}
		// submit progress bar refresh-function to the gui-thread
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					dataProcessing.refreshProgressBarAlgorithm(100, 1);
				}
			});
		} catch (final Exception e) {
			System.out.println(e.toString());
		}
		return mosaic;
	}

	/**
	 * method: errorDistribution
	 * description: errorDistribution Floyd-Steinberg
	 *
	 * @author Adrian Schuetz
	 * @param luminance (double)
	 * @param factor    (double)
	 * @param error     (double)
	 * @return result
	 */
	private double errorDistribution(final double luminance, final double factor, final double error) {
		double result;
		result = luminance - (error * factor);
		// Farben auf Farbraum begrenzen
		if (result > 100.0) {
			result = 100.0;
		}
		if (result < 0.0) {
			result = 0.0;
		}
		return result;
	}
}
