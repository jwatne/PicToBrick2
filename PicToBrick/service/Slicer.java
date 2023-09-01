package pictobrick.service;

import java.awt.image.*;
import java.awt.*;
import java.util.*;

import javax.swing.SwingUtilities;

import pictobrick.model.Configuration;
import pictobrick.model.Mosaic;

/**
 * class: Slicer
 * layer: DataProcessing (three tier architecture)
 * description: slicing (n-level-quantisation; pseudocolor-quantisation;
 * gray-level-to-color-transformation)
 *
 * @author Tobias Reichling
 */
public class Slicer implements Quantizer {
	private final DataProcessor dataProcessing;
	private final Calculator calculation;
	private final Vector<Object> selection;
	private int percent = 0;
	private int rows = 0;
	private int mosaicRow;

	/**
	 * method: Slicer
	 * description: constructor
	 *
	 * @author Tobias Reichling
	 * @param data        processing class
	 * @param calculation class
	 * @param selection
	 */
	public Slicer(final DataProcessor dataProcessing, final Calculator calculation, final Vector<Object> selection) {
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
		this.selection = selection;
	}

	/**
	 * method: quantisation
	 * description: slicing (n-level-quantisation; pseudocolor-quantisation;
	 * gray-level-to-color-transformation)
	 *
	 * @author Tobias Reichling
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
		// put colors and thresholds to an arry
		final int colorNumber = (selection.size() / 2);
		final int[] thresholds = new int[colorNumber + 1];
		final String[] colors = new String[colorNumber];
		final Enumeration<Object> selectionEnum = selection.elements();

		for (int i = 0; i < colorNumber; i++) {
			colors[i] = (String) selectionEnum.nextElement();
			thresholds[i] = (Integer) selectionEnum.nextElement();
		}

		// add a last threshold value (100) to this array
		thresholds[colorNumber] = 100;
		// scale image to mosaic dimensions
		final BufferedImage cutout = calculation.scale(image, mosaicWidth, mosaicHeight,
				dataProcessing.getInterpolation());
		// compute pixelMatrix
		final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
		// compute a workingMosaic only with luminance information
		// (compute srgb colors to cielab colors (luminace value))
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

		// color matching
		// the source pixel luminance value must be equal or greater than
		// the lower threshold AND less than the higher threshold of
		// a color
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
				int indicator = 0;
				for (int x = 0; x < colorNumber; x++) {
					if ((thresholds[x] <= workingMosaic[mosaicRow][mosaicColumn])
							&& (thresholds[x + 1] > workingMosaic[mosaicRow][mosaicColumn])) {
						mosaic.setElement(mosaicRow, mosaicColumn, colors[x], false);
						indicator = 1;
					}
				}
				// if a luminance value is 100.0 it cant be fount
				// in the threshold array. so this value must be
				// processed here:
				if (indicator == 0) {
					mosaic.setElement(mosaicRow, mosaicColumn, colors[colorNumber - 1], false);
				}
			}
		}
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
}
