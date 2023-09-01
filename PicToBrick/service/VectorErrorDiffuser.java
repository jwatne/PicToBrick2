package pictobrick.service;

import java.awt.image.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.*;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.Configuration;
import pictobrick.model.Lab;
import pictobrick.model.Mosaic;

/**
 * class: VectorErrorDiffuser
 * layer: DataProcessing (three tier architecture)
 * description: Vector error diffusion (floyd steinberg) - cielab
 *
 * @author Tobias Reichling
 */
public class VectorErrorDiffuser
		implements Quantizer {

	private final DataProcessor dataProcessing;
	private final Calculator calculation;
	private int percent = 0;
	private int rows = 0;
	private int mosaicRow;

	/**
	 * method: VectorErrorDiffusion
	 * description: constructor
	 *
	 * @author Tobias Reichling
	 * @param data        processing class
	 * @param calculation class
	 */
	public VectorErrorDiffuser(final DataProcessor dataProcessing, final Calculator calculation) {
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
	}

	/**
	 * method: quantisation
	 * description: Vector error diffusion (floyd steinberg) - cielab
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
		// scale image to mosaic dimensions
		final BufferedImage cutout = calculation.scale(image, mosaicWidth, mosaicHeight,
				dataProcessing.getInterpolation());
		// compute pixelMatrix
		final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
		// compute lab color vector
		final Vector<Object> labColors = new Vector<>();

		for (final Enumeration<ColorObject> colorEnumeration = configuration.getAllColors(); colorEnumeration
				.hasMoreElements();) {
			final ColorObject color = colorEnumeration.nextElement();
			labColors.add(calculation.rgbToLab(color.getRGB()));
			labColors.add(color.getName());
		}

		// compute working mosaic with lab color information (from srgb colors)
		final Lab[][] workingMosaic = new Lab[mosaicHeight][mosaicWidth];
		int red, green, blue;

		for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				red = pixelMatrix[mosaicRow][mosaicColumn][0];
				green = pixelMatrix[mosaicRow][mosaicColumn][1];
				blue = pixelMatrix[mosaicRow][mosaicColumn][2];
				workingMosaic[mosaicRow][mosaicColumn] = calculation.rgbToLab(new Color(red, green, blue));
			}
		}

		// computing the colors (minimum euklidean distance)
		// then floyd steinberg error diffusion (error vector)
		Lab lab1 = new Lab(), lab2, newColor = new Lab(), errorVector;
		String colorName = "";
		String name = "";
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
			} catch (final Exception e) {
				System.out.println(e.toString());
			}

			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				double minimumDifference = 500.0;

				for (final Enumeration<Object> colorEnumeration2 = labColors.elements(); colorEnumeration2
						.hasMoreElements();) {
					lab1 = workingMosaic[mosaicRow][mosaicColumn];
					lab2 = (Lab) (colorEnumeration2.nextElement());
					name = (String) (colorEnumeration2.nextElement());
					difference = java.lang.Math.sqrt(java.lang.Math.pow(lab1.getL() - lab2.getL(), 2.0) +
							java.lang.Math.pow(lab1.getA() - lab2.getA(), 2.0) +
							java.lang.Math.pow(lab1.getB() - lab2.getB(), 2.0));

					if (difference < minimumDifference) {
						minimumDifference = difference;
						colorName = name;
						newColor = lab2;
					}
				}

				// set color information to mosaic
				mosaic.setElement(mosaicRow, mosaicColumn, colorName, false);
				// compute error vector
				errorVector = new Lab(newColor.getL() - lab1.getL(),
						newColor.getA() - lab1.getA(),
						newColor.getB() - lab1.getB());
				// error diffusion
				// (caution: mosaic borders!)
				// pixel right: 7/16
				if (!(mosaicColumn == mosaicWidth - 1)) {
					workingMosaic[mosaicRow][mosaicColumn + 1] = errorDistribution(
							workingMosaic[mosaicRow][mosaicColumn + 1], (7.0 / 16.0), errorVector);
				}
				if (!(mosaicRow == mosaicHeight - 1)) {
					// pixel bottom: 5/16
					workingMosaic[mosaicRow + 1][mosaicColumn] = errorDistribution(
							workingMosaic[mosaicRow + 1][mosaicColumn], (5.0 / 16.0), errorVector);
					if (!(mosaicColumn == mosaicWidth - 1)) {
						// pixel bottom right: 1/16
						workingMosaic[mosaicRow + 1][mosaicColumn + 1] = errorDistribution(
								workingMosaic[mosaicRow + 1][mosaicColumn + 1], (1.0 / 16.0), errorVector);
					}
					if (!(mosaicColumn == 0)) {
						// pixel bottom left: 3/16
						workingMosaic[mosaicRow + 1][mosaicColumn - 1] = errorDistribution(
								workingMosaic[mosaicRow + 1][mosaicColumn - 1], (3.0 / 16.0), errorVector);
					}
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

	/**
	 * method: errorDistribution
	 * description: computes a color vector with the error vector and a special
	 * factor
	 * than cuts the results to the cielab color space
	 *
	 * @author Tobias Reichling
	 * @param color
	 * @param faktor
	 * @param error
	 * @return result (result = color - (factor*error))
	 */
	private Lab errorDistribution(final Lab color, final double factor, final Lab error) {
		final Lab result = new Lab();
		result.setL(color.getL() - (error.getL() * factor));
		result.setA(color.getA() - (error.getA() * factor));
		result.setB(color.getB() - (error.getB() * factor));
		// cut to color space: cielab
		if (result.getL() > 100.0) {
			result.setL(100.0);
		}
		if (result.getL() < 0.0) {
			result.setL(0.0);
		}
		if (result.getA() > 128.0) {
			result.setA(128.0);
		}
		if (result.getA() < -128.0) {
			result.setA(-128.0);
		}
		if (result.getB() > 128.0) {
			result.setB(128.0);
		}
		if (result.getB() < -128.0) {
			result.setB(-128.0);
		}
		return result;
	}

}
