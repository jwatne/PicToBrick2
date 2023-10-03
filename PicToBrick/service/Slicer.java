package pictobrick.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.Configuration;
import pictobrick.model.Mosaic;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Slicing (n-level-quantisation; pseudocolor-quantisation;
 * gray-level-to-color-transformation).
 *
 * @author Tobias Reichling
 */
public class Slicer implements Quantizer {
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private final Calculator calculation;
    /** Slicer selections. */
    private final Vector<Object> selection;
    /** Percent of rows processed (x 100%). */
    private int percent = 0;
    /** Number of rows in image. */
    private int rows = 0;
    /** Number of row bwing processed. */
    private int mosaicRow;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param processor
     * @param calculator
     * @param selections
     */
    public Slicer(final DataProcessor processor, final Calculator calculator,
            final Vector<Object> selections) {
        this.dataProcessing = processor;
        this.calculation = calculator;
        this.selection = selections;
    }

    /**
     * Slicing (n-level-quantisation; pseudocolor-quantisation;
     * gray-level-to-color-transformation).
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
        thresholds[colorNumber] = ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT;
        // scale image to mosaic dimensions
        final BufferedImage cutout = calculation.scale(image, mosaicWidth,
                mosaicHeight, dataProcessing.getInterpolation());
        // compute pixelMatrix
        final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
        // compute a workingMosaic only with luminance information
        // (compute srgb colors to cielab colors (luminace value))
        final double[][] workingMosaic = new double[mosaicHeight][mosaicWidth];
        int red;
        int green;
        int blue;

        for (int mRow = 0; mRow < mosaicHeight; mRow++) {
            for (int mCol = 0; mCol < mosaicWidth; mCol++) {
                red = pixelMatrix[mRow][mCol][0];
                green = pixelMatrix[mRow][mCol][1];
                blue = pixelMatrix[mRow][mCol][2];
                workingMosaic[mRow][mCol] = (calculation
                        .rgbToLab(new Color(red, green, blue)).getL());
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
                        percent = (int) ((Calculator.ONE_HUNDRED_PERCENT / rows)
                                * mosaicRow);
                        dataProcessing.refreshProgressBarAlgorithm(percent, 1);
                    }
                });
            } catch (final Exception e) {
                System.out.println(e.toString());
            }

            for (int mColumn = 0; mColumn < mosaicWidth; mColumn++) {
                int indicator = 0;

                for (int x = 0; x < colorNumber; x++) {
                    if ((thresholds[x] <= workingMosaic[mosaicRow][mColumn])
                            && (thresholds[x
                                    + 1] > workingMosaic[mosaicRow][mColumn])) {
                        mosaic.setElement(mosaicRow, mColumn, colors[x], false);
                        indicator = 1;
                    }
                }

                // if a luminance value is 100.0 it cant be fount
                // in the threshold array. so this value must be
                // processed here:
                if (indicator == 0) {
                    mosaic.setElement(mosaicRow, mColumn,
                            colors[colorNumber - 1], false);
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
}
