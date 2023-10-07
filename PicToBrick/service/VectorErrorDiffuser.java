package pictobrick.service;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.Configuration;
import pictobrick.model.Lab;
import pictobrick.model.Mosaic;
import pictobrick.ui.ProgressBarsAlgorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Vector error diffusion (floyd steinberg) - cielab.
 *
 * @author Tobias Reichling
 */
public class VectorErrorDiffuser implements Quantizer {
    /** Maximum value = 128.0 for cielab color component. */
    private static final double MAX_COMPONENT_128 = 128.0;
    /** Maximum value = 100.0 for cielab color component. */
    private static final double MAX_COMPONENT_100 = 100.0;
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private final Calculator calculation;
    /** Percent of rows calculated. */
    private int percent = 0;
    /** Number of rows in image. */
    private int rows = 0;
    /** Number of row being processed. */
    private int mosaicRow;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param processor
     * @param calculator class
     */
    public VectorErrorDiffuser(final DataProcessor processor,
            final Calculator calculator) {
        this.dataProcessing = processor;
        this.calculation = calculator;
    }

    /**
     * Vector error diffusion (floyd steinberg) - cielab.
     *
     * @author Tobias Reichling
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
        // compute pixelMatrix
        final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
        // compute lab color vector
        final Vector<Object> labColors = new Vector<>();

        for (final Enumeration<ColorObject> colorEnumeration = configuration
                .getAllColors(); colorEnumeration.hasMoreElements();) {
            final ColorObject color = colorEnumeration.nextElement();
            labColors.add(calculation.rgbToLab(color.getRGB()));
            labColors.add(color.getName());
        }

        // compute working mosaic with lab color information (from srgb colors)
        final Lab[][] workingMosaic = new Lab[mosaicHeight][mosaicWidth];
        int red;
        int green;
        int blue;

        for (int mRow = 0; mRow < mosaicHeight; mRow++) {
            for (int mCol = 0; mCol < mosaicWidth; mCol++) {
                red = pixelMatrix[mRow][mCol][0];
                green = pixelMatrix[mRow][mCol][1];
                blue = pixelMatrix[mRow][mCol][2];
                workingMosaic[mRow][mCol] = calculation
                        .rgbToLab(new Color(red, green, blue));
            }
        }

        // computing the colors (minimum euklidean distance)
        // then floyd steinberg error diffusion (error vector)
        Lab lab1 = new Lab();
        Lab lab2;
        Lab newColor = new Lab();
        Lab errorVector;
        String colorName = "";
        String name = "";
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

            for (int mCol = 0; mCol < mosaicWidth; mCol++) {
                double minimumDifference = STARTING_MIN_DIFFERENCE;

                for (final Enumeration<Object> colorEnumeration2 = labColors
                        .elements(); colorEnumeration2.hasMoreElements();) {
                    lab1 = workingMosaic[mosaicRow][mCol];
                    lab2 = (Lab) (colorEnumeration2.nextElement());
                    name = (String) (colorEnumeration2.nextElement());
                    difference = java.lang.Math.sqrt(
                            java.lang.Math.pow(lab1.getL() - lab2.getL(), 2.0)
                                    + java.lang.Math
                                            .pow(lab1.getA() - lab2.getA(), 2.0)
                                    + java.lang.Math.pow(
                                            lab1.getB() - lab2.getB(), 2.0));

                    if (difference < minimumDifference) {
                        minimumDifference = difference;
                        colorName = name;
                        newColor = lab2;
                    }
                }

                // set color information to mosaic
                mosaic.setElement(mosaicRow, mCol, colorName, false);
                // compute error vector
                errorVector = new Lab(newColor.getL() - lab1.getL(),
                        newColor.getA() - lab1.getA(),
                        newColor.getB() - lab1.getB());
                // error diffusion
                // (caution: mosaic borders!)
                // pixel right: 7/16
                if (!(mCol == mosaicWidth - 1)) {
                    workingMosaic[mosaicRow][mCol + 1] = errorDistribution(
                            workingMosaic[mosaicRow][mCol + 1], FACTOR_7_16,
                            errorVector);
                }
                if (!(mosaicRow == mosaicHeight - 1)) {
                    // pixel bottom: 5/16
                    workingMosaic[mosaicRow + 1][mCol] = errorDistribution(
                            workingMosaic[mosaicRow + 1][mCol], FACTOR_5_16,
                            errorVector);
                    if (!(mCol == mosaicWidth - 1)) {
                        // pixel bottom right: 1/16
                        workingMosaic[mosaicRow + 1][mCol
                                + 1] = errorDistribution(
                                        workingMosaic[mosaicRow + 1][mCol + 1],
                                        FACTOR_1_16, errorVector);
                    }
                    if (!(mCol == 0)) {
                        // pixel bottom left: 3/16
                        workingMosaic[mosaicRow + 1][mCol
                                - 1] = errorDistribution(
                                        workingMosaic[mosaicRow + 1][mCol - 1],
                                        FACTOR_3_16, errorVector);
                    }
                }
            }
        }
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

    /**
     * Computes a color vector with the error vector and a special factor than
     * cuts the results to the cielab color space.
     *
     * @author Tobias Reichling
     * @param color
     * @param factor
     * @param error
     * @return result (result = color - (factor*error))
     */
    private Lab errorDistribution(final Lab color, final double factor,
            final Lab error) {
        final Lab result = new Lab();
        result.setL(color.getL() - (error.getL() * factor));
        result.setA(color.getA() - (error.getA() * factor));
        result.setB(color.getB() - (error.getB() * factor));

        // cut to color space: cielab
        if (result.getL() > MAX_COMPONENT_100) {
            result.setL(MAX_COMPONENT_100);
        }

        if (result.getL() < 0.0) {
            result.setL(0.0);
        }

        if (result.getA() > MAX_COMPONENT_128) {
            result.setA(MAX_COMPONENT_128);
        }

        if (result.getA() < -MAX_COMPONENT_128) {
            result.setA(-MAX_COMPONENT_128);
        }

        if (result.getB() > MAX_COMPONENT_128) {
            result.setB(MAX_COMPONENT_128);
        }

        if (result.getB() < -MAX_COMPONENT_128) {
            result.setB(-MAX_COMPONENT_128);
        }

        return result;
    }

}
