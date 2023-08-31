package PicToBrick.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.SwingUtilities;

import PicToBrick.model.ColorObject;
import PicToBrick.model.Configuration;
import PicToBrick.model.Lab;
import PicToBrick.model.Mosaic;

/**
 * class: PatternDitheringQuantizer
 * layer: DataProcessing (three tier architecture)
 * description: pattern dithering with 2x2 color pattern
 *
 * @author Adrian Schuetz
 */
public class PatternDitheringQuantizer
		implements Quantizer {

	private final DataProcessor dataProcessing;
	private final Calculator calculation;
	private int percent = 0;
	private int rows = 0;
	private int mosaicRow;
	private final int luminanceLimit;

	/**
	 * method: PatternDitheringQuantizer
	 * description: constructor
	 *
	 * @author Adrian Schuetz
	 * @param data             processing class
	 * @param calculation      class
	 * @param quantisationInfo
	 */
	public PatternDitheringQuantizer(final DataProcessor dataProcessing, final Calculator calculation,
			final Vector<Object> quantisationInfo) {
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
		this.luminanceLimit = (int) quantisationInfo.get(0);
	}

	/**
	 * method: quantisation
	 * description: pattern dithering with 2x2 color pattern
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
		// 4 colors of the dither pattern (2x2)
		String colorName1 = new String("");
		String colorName2 = new String("");
		String colorName3 = new String("");
		String colorName4 = new String("");
		// compute pixelMatrix
		final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
		// lab color vector
		final Vector<ColorObject> labColors = new Vector<>();

		for (final Enumeration<ColorObject> allColorsEnumeration = configuration.getAllColors(); allColorsEnumeration
				.hasMoreElements();) {
			final ColorObject color = (ColorObject) (allColorsEnumeration.nextElement());
			labColors.add(color);
		}

		// compute a vector with color-combinations (each with 2 colors)
		final Vector<Object> ditherColors = new Vector<>();
		final int labColorsNumber = labColors.size();

		for (int i = 0; i < labColorsNumber; i++) {
			// delete first element from vector
			final ColorObject color = (ColorObject) labColors.remove(0);
			// compute lab color
			// pattern with only one color
			ditherColors.addElement(calculation.rgbToLab(color.getRGB()));
			ditherColors.addElement(color.getName());
			ditherColors.addElement(color.getName());
			ditherColors.addElement(color.getName());
			ditherColors.addElement(color.getName());

			// compute mixed color values
			for (final Enumeration<ColorObject> additionalColor = labColors.elements(); additionalColor
					.hasMoreElements();) {
				final ColorObject secondColor = additionalColor.nextElement();

				// computes color values if the luminance values of the 2 colors
				// do not differentiate more than the given limit
				if (isLuminanceLimit(color, secondColor, luminanceLimit)) {
					// 3x fist color, 1x second color
					ditherColors.addElement(computeMixedColorValue(color, secondColor, 3, 1));
					ditherColors.addElement(color.getName());
					ditherColors.addElement(color.getName());
					ditherColors.addElement(color.getName());
					ditherColors.addElement(secondColor.getName());
					// 2x fist color, 2x second color
					ditherColors.addElement(computeMixedColorValue(color, secondColor, 2, 2));
					ditherColors.addElement(color.getName());
					ditherColors.addElement(secondColor.getName());
					ditherColors.addElement(secondColor.getName());
					ditherColors.addElement(color.getName());
					// 1x fist color, 3x second color
					ditherColors.addElement(computeMixedColorValue(color, secondColor, 1, 3));
					ditherColors.addElement(secondColor.getName());
					ditherColors.addElement(secondColor.getName());
					ditherColors.addElement(secondColor.getName());
					ditherColors.addElement(color.getName());
				}
			}
		}
		// working mosaic with lab colors
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

		// Number of dither colors (5 colors pro original color)
		final int ditherColorNumber = (ditherColors.size() / 5);
		// Arrays fuer Farbwerte und colorNamen der ditherColors
		final Lab[] colorValues = new Lab[ditherColorNumber];
		final String[] ColorNames1 = new String[ditherColorNumber];
		final String[] ColorNames2 = new String[ditherColorNumber];
		final String[] ColorNames3 = new String[ditherColorNumber];
		final String[] ColorNames4 = new String[ditherColorNumber];
		// Arrays mit Werten aus dem Vektor der ditherColors befuellen
		int i = 0;

		for (final Enumeration<Object> dithercolorEnumeration = ditherColors.elements(); dithercolorEnumeration
				.hasMoreElements();) {
			colorValues[i] = (Lab) (dithercolorEnumeration.nextElement());
			ColorNames1[i] = (String) (dithercolorEnumeration.nextElement());
			ColorNames2[i] = (String) (dithercolorEnumeration.nextElement());
			ColorNames3[i] = (String) (dithercolorEnumeration.nextElement());
			ColorNames4[i] = (String) (dithercolorEnumeration.nextElement());
			i++;
		}

		// color matching
		Lab lab;
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
				// color values
				final double l = workingMosaic[mosaicRow][mosaicColumn].getL();
				final double a = workingMosaic[mosaicRow][mosaicColumn].getA();
				final double b = workingMosaic[mosaicRow][mosaicColumn].getB();
				for (int x = 0; x < ditherColorNumber; x++) {
					lab = colorValues[x];
					// compute difference
					difference = Math.sqrt(Math.pow(l - lab.getL(), 2.0) +
							Math.pow(a - lab.getA(), 2.0) +
							Math.pow(b - lab.getB(), 2.0));
					if (difference < minimumDifference) {
						minimumDifference = difference;
						colorName1 = ColorNames1[x];
						colorName2 = ColorNames2[x];
						colorName3 = ColorNames3[x];
						colorName4 = ColorNames4[x];
					}
				}
				// set color
				// choose the color from the color pattern using the position
				// information
				// if the mosaic row is even (odd) take the color from the pattern position
				// where the row is even (odd)
				// column: see above
				if ((mosaicRow % 2) == 0) {
					// mosaicRow even
					if ((mosaicColumn % 2) == 0) {
						// mosaicColumn even
						mosaic.setElement(mosaicRow, mosaicColumn, colorName1, false);
					} else {
						// mosaicColumn odd
						mosaic.setElement(mosaicRow, mosaicColumn, colorName2, false);
					}
				} else {
					// mosaicRow odd
					if ((mosaicColumn % 2) == 0) {
						// mosaicColumn even
						mosaic.setElement(mosaicRow, mosaicColumn, colorName3, false);
					} else {
						// mosaicColumn odd
						mosaic.setElement(mosaicRow, mosaicColumn, colorName4, false);
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
	 * method: computeMixedColorValue
	 * description: computes the average color value from a color pattern
	 * ( color 1 + color 2 + color 3 + color 4 ) / 4
	 *
	 * @author Adrian Schuetz
	 * @param color1
	 * @param color2
	 * @param due1
	 * @param due2
	 * @return result
	 */
	private Lab computeMixedColorValue(final ColorObject color1, final ColorObject color2, final int due1,
			final int due2) {
		final Lab lab1 = calculation.rgbToLab(color1.getRGB());
		final Lab lab2 = calculation.rgbToLab(color2.getRGB());
		return new Lab((lab1.getL() * due1 + lab2.getL() * due2) / 4,
				(lab1.getA() * due1 + lab2.getA() * due2) / 4,
				(lab1.getB() * due1 + lab2.getB() * due2) / 4);
	}

	/**
	 * method: isLuminanceLimit
	 * description: checks if a luminance difference is allowed
	 *
	 * @author Adrian Schuetz
	 * @param color1
	 * @param color2
	 * @return true or false
	 */
	private boolean isLuminanceLimit(final ColorObject color1, final ColorObject color2, final int luminanceLimit) {
		final Lab lab1 = calculation.rgbToLab(color1.getRGB());
		final Lab lab2 = calculation.rgbToLab(color2.getRGB());
		if (Math.abs(lab1.getL() - lab2.getL()) > luminanceLimit) {
			return false;
		}
		return true;
	}
}
