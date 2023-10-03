package pictobrick.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.Configuration;
import pictobrick.model.Lab;
import pictobrick.model.Mosaic;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Based on the naive quantisation cielab lonely pixels are deleted with a
 * special filter result: big monochrome regions (solid regions).
 *
 * @author Tobias Reichling
 */
public class SolidRegionsQuantizer implements Quantizer {
    /** Starting minimum difference value for determining best fit region. */
    private static final double STARTING_MIN_DIFFERENCE = 500.0;
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private final Calculator calculation;
    /** Percent of rows calculated (x 100%). */
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
     * @param calculator
     */
    public SolidRegionsQuantizer(final DataProcessor processor,
            final Calculator calculator) {
        this.dataProcessing = processor;
        this.calculation = calculator;
    }

    /**
     * Based on the naive quantisation cielab lonely pixels are deleted with a
     * special filter result: big monochrome regions (solid regions).
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
        String colorName = "";
        // compute pixelMatrix
        final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
        // compute vector of lab colors
        final Vector<Object> labColors = new Vector<>();

        for (final Enumeration<ColorObject> colorEnumeration = configuration
                .getAllColors(); colorEnumeration.hasMoreElements();) {
            final ColorObject color = (ColorObject) (colorEnumeration
                    .nextElement());
            labColors.add(calculation.rgbToLab(color.getRGB()));
            labColors.add(color.getName());
        }

        // working mosaic gets an one pixel wide border
        // so it is not necassary to computes the mosaic border areas in a
        // special way
        final String[][] workingMosaic = new String[mosaicHeight
                + 2][mosaicWidth + 2];
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

            for (int mColumn = 0; mColumn < mosaicWidth; mColumn++) {
                double minimumDifference = STARTING_MIN_DIFFERENCE;
                red = pixelMatrix[mosaicRow][mColumn][0];
                green = pixelMatrix[mosaicRow][mColumn][1];
                blue = pixelMatrix[mosaicRow][mColumn][2];
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

                mosaic.setElement(mosaicRow, mColumn, colorName, false);
                workingMosaic[mosaicRow + 1][mColumn + 1] = colorName;
            }
        }

        // the one pixel wide border is filled with the original border pixel
        // values
        // special cases: border, corner, etc.
        for (int row = 0; row < (mosaicHeight + 2); row++) {
            for (int column = 0; column < (mosaicWidth + 2); column++) {
                // TOP
                if (row == 0) {
                    // TOP-left
                    if (column == 0) {
                        workingMosaic[row][column] = workingMosaic[row
                                + 1][column + 1];
                    }
                    // TOP-Center
                    if (column > 0 && column < (mosaicWidth + 2)) {
                        workingMosaic[row][column] = workingMosaic[row
                                + 1][column];
                    }
                    // TOP-right
                    if (column == (mosaicWidth + 2)) {
                        workingMosaic[row][column] = workingMosaic[row
                                + 1][column - 1];
                    }
                }
                // CENTER
                if (row > 0 && row < (mosaicHeight + 2)) {
                    // CENTER-left
                    if (column == 0) {
                        workingMosaic[row][column] = workingMosaic[row][column
                                + 1];
                    }
                    // CENTER-right
                    if (column == (mosaicWidth + 2)) {
                        workingMosaic[row][column] = workingMosaic[row][column
                                - 1];
                    }
                }
                // BOTTOM
                if (row == (mosaicHeight + 2)) {
                    // BOTTOM-left
                    if (column == 0) {
                        workingMosaic[row][column] = workingMosaic[row
                                - 1][column + 1];
                    }
                    // BOTTOM-Center
                    if (column > 0 && column < (mosaicWidth + 2)) {
                        workingMosaic[row][column] = workingMosaic[row
                                - 1][column];
                    }
                    // BOTTOM-right
                    if (column == (mosaicWidth + 2)) {
                        workingMosaic[row][column] = workingMosaic[row
                                - 1][column - 1];
                    }
                }

            }
        }
        //

        generateBigMonochromeRegionsUsing3X3Filter(mosaicWidth, mosaicHeight,
                workingMosaic);
        transferColorInfo(mosaicWidth, mosaicHeight, mosaic, workingMosaic);
        maxProgressBar();
        return mosaic;
    }

    /**
     * Filter for generating big monochrome regions: Count how often the current
     * color exists in the near 8 pixels, using 3x3 filter.
     *
     * @param mosaicWidth
     * @param mosaicHeight
     * @param workingMosaic
     */
    private void generateBigMonochromeRegionsUsing3X3Filter(
            final int mosaicWidth, final int mosaicHeight,
            final String[][] workingMosaic) {
        int colorCounter;
        String currentColorName;

        for (int row = 1; row < mosaicHeight; row++) {
            for (int column = 1; column < mosaicWidth; column++) {
                colorCounter = 0;
                currentColorName = workingMosaic[row][column];

                if (currentColorName.equals(workingMosaic[row + 1][column])) {
                    colorCounter++;
                }

                if (currentColorName.equals(workingMosaic[row - 1][column])) {
                    colorCounter++;
                }

                if (currentColorName.equals(workingMosaic[row][column + 1])) {
                    colorCounter++;
                }

                if (currentColorName.equals(workingMosaic[row][column - 1])) {
                    colorCounter++;
                }

                if (currentColorName
                        .equals(workingMosaic[row + 1][column + 1])) {
                    colorCounter++;
                }

                if (currentColorName
                        .equals(workingMosaic[row - 1][column - 1])) {
                    colorCounter++;
                }
                if (currentColorName
                        .equals(workingMosaic[row + 1][column - 1])) {
                    colorCounter++;
                }

                if (currentColorName
                        .equals(workingMosaic[row - 1][column + 1])) {
                    colorCounter++;
                }

                computeColorChange(workingMosaic, colorCounter, row, column);
            }
        }
    }

    /**
     * A color is changed if it exists less than 2 times. The threshold is 2
     * because a lower or a higher threshold will affect the image quality.
     *
     * Computes the color which exists more often than other colors in this 3x3
     * window. Change the current color to this new color.
     *
     * @param workingMosaic
     * @param colorCounter
     * @param row
     * @param column
     */
    private void computeColorChange(final String[][] workingMosaic,
            final int colorCounter, final int row, final int column) {
        if (colorCounter < 2) {
            int colorCounter2;
            String newColor = "";
            int flag = 0;
            String testColor;

            for (int windowRow = (row - 1); windowRow < (row
                    + 2); windowRow++) {
                for (int windowColumn = (column - 1); windowColumn < (column
                        + 2); windowColumn++) {
                    testColor = workingMosaic[windowRow][windowColumn];
                    colorCounter2 = 0;

                    for (int winRow2 = (row - 1); winRow2 < (row
                            + 2); winRow2++) {
                        for (int winCol2 = (column - 1); winCol2 < (column
                                + 2); winCol2++) {
                            if (testColor
                                    .equals(workingMosaic[winRow2][winCol2])) {
                                colorCounter2++;
                            }
                        }
                    }

                    if (colorCounter2 > flag) {
                        newColor = testColor;
                        flag = colorCounter2;
                    }
                }
            }

            // change color
            workingMosaic[row][column] = newColor;
        }
    }

    /**
     * Transfer color information from working mosaic to original mosaic.
     * (Caution: don't transfer the color information from the one pixel wide
     * border.)
     *
     * @param mosaicWidth   width of mosaic.
     * @param mosaicHeight  height of mosaic.
     * @param mosaic        mosaic being processed.
     * @param workingMosaic source mosaic with data to be copied to mosaic.
     */
    private void transferColorInfo(final int mosaicWidth,
            final int mosaicHeight, final Mosaic mosaic,
            final String[][] workingMosaic) {
        for (int row = 1; row < mosaicHeight; row++) {
            for (int column = 1; column < mosaicWidth; column++) {
                mosaic.initVector(row - 1, column - 1);
                mosaic.setElement(row - 1, column - 1,
                        workingMosaic[row][column], false);
            }
        }
    }

    private void maxProgressBar() {
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
    }
}
