package pictobrick.service;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.image.BufferedImage;

import pictobrick.model.ColorObject;
import pictobrick.model.Configuration;
import pictobrick.model.Lab;
import pictobrick.model.Mosaic;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Simple quantisation - cielab - euclidean distance.
 *
 * @author Adrian Schuetz
 */
public class NaiveLabQuantizer implements Quantizer {
    /** Starting minimum difference in determining best-fit color. */
    private static final double STARTING_MIN_DIFFERENCE = 500.0;
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private final Calculator calculation;
    /** Percentage of rows calculated. */
    private int percent = 0;
    /** Number of rows in image. */
    private int rows = 0;
    /** The row currently being processed. */
    private int mosaicRow;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param processor  dataProcessing
     * @param calculator calculation
     */
    public NaiveLabQuantizer(final DataProcessor processor,
            final Calculator calculator) {
        this.dataProcessing = processor;
        this.calculation = calculator;
    }

    /**
     * Farbmatching.
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
        // create a vector of all lab colors
        final Vector<Object> labColors = new Vector<>();

        for (final Enumeration<ColorObject> colorEnumeration = configuration
                .getAllColors(); colorEnumeration.hasMoreElements();) {
            final ColorObject color = (ColorObject) (colorEnumeration
                    .nextElement());
            labColors.add(calculation.rgbToLab(color.getRGB()));
            labColors.add(color.getName());
        }

        // farb matching - cielab - euclidean distance
        // compute every sRGB color to CieLAB color
        int red;
        int green;
        int blue;
        Lab lab;
        Lab lab2;
        String name;
        double difference;

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
                double minimumDifference = STARTING_MIN_DIFFERENCE;
                red = pixelMatrix[mosaicRow][mosaicCol][0];
                green = pixelMatrix[mosaicRow][mosaicCol][1];
                blue = pixelMatrix[mosaicRow][mosaicCol][2];
                lab = calculation.rgbToLab(new Color(red, green, blue));

                for (final Enumeration<Object> colorEnumeration2 = labColors
                        .elements(); colorEnumeration2.hasMoreElements();) {
                    lab2 = (Lab) (colorEnumeration2.nextElement());
                    name = (String) (colorEnumeration2.nextElement());
                    difference = java.lang.Math.sqrt(
                            java.lang.Math.pow(lab.getL() - lab2.getL(), 2.0)
                                    + java.lang.Math
                                            .pow(lab.getA() - lab2.getA(), 2.0)
                                    + java.lang.Math.pow(
                                            lab.getB() - lab2.getB(), 2.0));

                    if (difference < minimumDifference) {
                        minimumDifference = difference;
                        colorName = name;
                    }
                }

                // sets found color to the mosaic
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
