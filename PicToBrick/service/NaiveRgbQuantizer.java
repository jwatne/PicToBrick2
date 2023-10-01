package pictobrick.service;

import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.Configuration;
import pictobrick.model.Mosaic;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Simple quantisation - sRGB - euclidean distance.
 *
 * @author Adrian Schuetz
 */
public class NaiveRgbQuantizer implements Quantizer {
    /** Starting minimum difference in calculating best-fit color. */
    private static final int STARTING_MIN_DIFFERENCE = 500;
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private final Calculator calculation;
    /** Percentage of rows calculated. */
    private int percent = 0;
    /** Number of rows in image. */
    private int rows = 0;
    /** Row currently being processed. */
    private int mosaicRow;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param processor
     * @param calculator
     */
    public NaiveRgbQuantizer(final DataProcessor processor,
            final Calculator calculator) {
        this.dataProcessing = processor;
        this.calculation = calculator;
    }

    /**
     * Simple quantisation - sRGB - euclidean distance.
     *
     * @author Adrian Schuetz
     * @param image
     * @param mosaicWidth
     * @param mosaicHeight
     * @param configuration
     * @param mosaic        (empty)
     * @return mosaic (with color information)
     */
    public Mosaic quantisation(final BufferedImage image, final int mosaicWidth,
            final int mosaicHeight, final Configuration configuration,
            final Mosaic mosaic) {
        this.rows = mosaicHeight;
        // scale image to mosaic dimensions
        final BufferedImage cutout = calculation.scale(image, mosaicWidth,
                mosaicHeight, dataProcessing.getInterpolation());
        String colorName = "";
        // compute pixelMatrix
        final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);

        // color matching
        for (mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
            // submit progress bar refresh-function to the gui-thread
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        percent = (int) ((Calculator.ONE_HUNDRED_PERCENT / rows)
                                * mosaicRow);
                        dataProcessing.refreshProgressBarAlgorithm(percent, 1);
                    }
                });
            } catch (final Exception e) {
                System.out.println(e.toString());
            }

            for (int mosaicCol = 0; mosaicCol < mosaicWidth; mosaicCol++) {
                int minimumDifference = STARTING_MIN_DIFFERENCE;
                final int red = pixelMatrix[mosaicRow][mosaicCol][0];
                final int green = pixelMatrix[mosaicRow][mosaicCol][1];
                final int blue = pixelMatrix[mosaicRow][mosaicCol][2];

                // find the nearest color in the color vector
                for (final var colorEnumeration = configuration
                        .getAllColors(); colorEnumeration.hasMoreElements();) {
                    final ColorObject color = colorEnumeration.nextElement();

                    // compute difference an save color with minimum difference
                    final int difference = (int) java.lang.Math
                            .sqrt(java.lang.Math
                                    .pow(red - color.getRGB().getRed(), 2.0)
                                    + java.lang.Math.pow(
                                            green - color.getRGB().getGreen(),
                                            2.0)
                                    + java.lang.Math.pow(
                                            blue - color.getRGB().getBlue(),
                                            2.0));

                    if (difference < minimumDifference) {
                        minimumDifference = difference;
                        colorName = color.getName();
                    }
                }

                // set color to the mosaic
                mosaic.setElement(mosaicRow, mosaicCol, colorName, false);
            }
        }

        // submit progress bar refresh-function to the gui-thread
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    dataProcessing.refreshProgressBarAlgorithm(
                            ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT, 1);
                }
            });
        } catch (final Exception e) {
            System.out.println(e.toString());
        }

        return mosaic;
    }
}
