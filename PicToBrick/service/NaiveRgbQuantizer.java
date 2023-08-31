package PicToBrick.service;

import java.awt.image.*;
import java.util.Enumeration;
import javax.swing.*;

import PicToBrick.model.ColorObject;
import PicToBrick.model.Configuration;
import PicToBrick.model.Mosaic;

/**
 * class: NaiveRgbQuantizer
 * layer: DataProcessing (three tier architecture)
 * description: simple quantisation - sRGB - euclidean distance
 *
 * @author Adrian Schuetz
 */
public class NaiveRgbQuantizer
		implements Quantizer {

	private final DataProcessor dataProcessing;
	private final Calculator calculation;
	private int percent = 0;
	private int rows = 0;
	private int mosaicRow;

	/**
	 * method: NaiveQuantisationRgb
	 * description: constructor
	 *
	 * @author Adrian Schuetz
	 * @param data        processing class
	 * @param calculation class
	 */
	public NaiveRgbQuantizer(final DataProcessor dataProcessing, final Calculator calculation) {
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
	}

	/**
	 * method: quantisation
	 * description: simple quantisation - sRGB - euclidean distance
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
		// scale image to mosaic dimensions
		final BufferedImage cutout = calculation.scale(image, mosaicWidth, mosaicHeight,
				dataProcessing.getInterpolation());
		String colorName = new String("");
		// compute pixelMatrix
		final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);

		// color matching
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
				int minimumDifference = 500;
				final int red = pixelMatrix[mosaicRow][mosaicColumn][0];
				final int green = pixelMatrix[mosaicRow][mosaicColumn][1];
				final int blue = pixelMatrix[mosaicRow][mosaicColumn][2];

				// find the nearest color in the color vector
				for (final Enumeration<ColorObject> colorEnumeration = configuration.getAllColors(); colorEnumeration
						.hasMoreElements();) {
					final ColorObject color = colorEnumeration.nextElement();

					// compute difference an save color with minimum difference
					final int difference = (int) java.lang.Math
							.sqrt(java.lang.Math.pow(red - color.getRGB().getRed(), 2.0) +
									java.lang.Math.pow(green - color.getRGB().getGreen(), 2.0) +
									java.lang.Math.pow(blue - color.getRGB().getBlue(), 2.0));

					if (difference < minimumDifference) {
						minimumDifference = difference;
						colorName = color.getName();
					}
				}

				// set color to the mosaic
				mosaic.setElement(mosaicRow, mosaicColumn, colorName, false);
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
}
