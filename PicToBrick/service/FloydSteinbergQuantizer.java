package pictobrick.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.Configuration;
import pictobrick.model.Mosaic;

/**
 * Quantisation with error diffusion (floyd steinberg) with 2 colors.
 *
 * @author Adrian Schuetz
 */
public class FloydSteinbergQuantizer implements Quantizer {
    /** Int value 5. */
    private static final int INT5 = 5;
    /** Int value 4. */
    private static final int INT4 = 4;
    /** Int value 3. */
    private static final int INT3 = 3;
    /** Scanline method code. */
    private static final int SCANLINE = 1;
    /** Serpintines method code. */
    private static final int SERPENTINES = 2;
    /** Hilbert method code. */
    private static final int HILBERT = 3;
    /** 3/16 factor value. */
    private static final double FACTOR_3_16 = 3.0 / 16.0;
    /** 1/16 factor value. */
    private static final double FACTOR_1_16 = 1.0 / 16.0;
    /** 5/16 factor value. */
    private static final double FACTOR_5_16 = 5.0 / 16.0;
    /** 7/16 factor value. */
    private static final double FACTOR_7_16 = 7.0 / 16.0;
    /** Cutoff brightness value. */
    private static final int DARK_LIGHT_CUTOFF = 50;
    /** Maximum brightness value. */
    private static final int MAX_BRIGHTNESS = 100;

    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private final Calculator calculation;
    /** Dark / light / method selections. */
    private final Vector<Object> selection;
    /** Percent of rows calculated. */
    private int percent = 0;
    /** Number of rows in image. */
    private int rows = 0;
    /** Row currently being processed. */
    private int mosaicRow;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param dataProcessor
     * @param calculator
     * @param selections
     */
    public FloydSteinbergQuantizer(final DataProcessor dataProcessor,
            final Calculator calculator, final Vector<Object> selections) {
        this.dataProcessing = dataProcessor;
        this.calculation = calculator;
        this.selection = selections;
    }

    /**
     * Quantisation with error diffusion (floyd steinberg) with 2 colors.
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
        final String dark = (String) selection.get(0);
        final String light = (String) selection.get(1);
        final int method = (Integer) selection.get(2);
        // scale image to mosaic dimensions
        final BufferedImage cutout = calculation.scale(image, mosaicWidth,
                mosaicHeight, dataProcessing.getInterpolation());
        // compute pixelMatrix
        final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
        // compute source image only with luminance values
        final double[][] workingMosaic = getSourceLuminanceValues(mosaicWidth,
                mosaicHeight, pixelMatrix);

        if (method == SCANLINE) {
            doFloydSteinbergScanline(mosaicWidth, mosaicHeight, mosaic, dark,
                    light, workingMosaic);
        } else if (method == SERPENTINES) {
            doFloydSteinbergSerpentines(mosaicWidth, mosaicHeight, mosaic, dark,
                    light, workingMosaic);
        } else if (method == HILBERT) {
            doHilbert(mosaicWidth, mosaicHeight, mosaic, dark, light,
                    workingMosaic);
        }

        // submit progress bar refresh-function to the gui-thread
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    dataProcessing.refreshProgressBarAlgorithm(MAX_BRIGHTNESS,
                            1);
                }
            });
        } catch (final Exception e) {
            System.out.println(e.toString());
        }

        return mosaic;
    }

    private void doHilbert(final int mosaicWidth, final int mosaicHeight,
            final Mosaic mosaic, final String dark, final String light,
            final double[][] workingMosaic) {
        double error;
        // *********************************** Hilbert curce
        final Vector<Integer> coordinates = new Vector<>(
                calculation.hilbertCoordinates(mosaicWidth, mosaicHeight));
        int x0;
        int x1;
        int x2;
        int x3;
        int x4;
        int y0;
        int y1;
        int y2;
        int y3;
        int y4;
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
        int referenceValue = (mosaicHeight * mosaicWidth) / MAX_BRIGHTNESS;

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
                            dataProcessing.refreshProgressBarAlgorithm(percent,
                                    1);
                        }
                    });
                } catch (final Exception e) {
                    System.out.println(e.toString());
                }
            }

            counter++;

            // compute color and error
            if (workingMosaic[x0][y0] < i) {
                mosaic.setElement(x0, y0, dark, false);
                error = 0 - workingMosaic[x0][y0];
            } else {
                mosaic.setElement(x0, y0, light, false);
                error = i - workingMosaic[x0][y0];
            }

            x0 = x1;
            y0 = y1;

            // distribute error
            // ... 7/16 ... 5/16 ... 3/16 ... 1/16
            // (caution: at the last mosaic pixel, the error can only be
            // distribute
            // to the following last pixels!)
            if (i < (coordinatesNumber - 2)) {
                workingMosaic[x1][y1] = errorDistribution(workingMosaic[x1][y1],
                        FACTOR_7_16, error);
                x1 = x2;
                y1 = y2;
            }

            if (i < (coordinatesNumber - INT3)) {
                workingMosaic[x2][y2] = errorDistribution(workingMosaic[x2][y2],
                        FACTOR_5_16, error);
                x2 = x3;
                y2 = y3;
            }

            if (i < (coordinatesNumber - INT4)) {
                workingMosaic[x3][y3] = errorDistribution(workingMosaic[x3][y3],
                        FACTOR_3_16, error);
                x3 = x4;
                y3 = y4;
            }

            if (i < (coordinatesNumber - INT5)) {
                workingMosaic[x4][y4] = errorDistribution(workingMosaic[x4][y4],
                        FACTOR_1_16, error);
                x4 = (Integer) coordinatesEnum.nextElement();
                y4 = (Integer) coordinatesEnum.nextElement();
            }
        }
    }

    private void doFloydSteinbergSerpentines(final int mosaicWidth,
            final int mosaicHeight, final Mosaic mosaic, final String dark,
            final String light, final double[][] workingMosaic) {
        double error;
        // *********************************** FloydSteinberg Serpentines
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
                if ((mosaicRow % 2) == 0) {
                    // row even -> to the right
                    // ---------------------------
                    // compute color and error
                    final double cell = workingMosaic[mosaicRow][mColumn];

                    if (cell < DARK_LIGHT_CUTOFF) {
                        mosaic.setElement(mosaicRow, mColumn, dark, false);
                        error = 0 - cell;
                    } else {
                        mosaic.setElement(mosaicRow, mColumn, light, false);
                        error = MAX_BRIGHTNESS - cell;
                    }

                    // distribute error (caution: mosaic borders!)
                    // pixel right: 7/16
                    if (!(mColumn == mosaicWidth - 1)) {
                        workingMosaic[mosaicRow][mColumn
                                + 1] = errorDistribution(
                                        workingMosaic[mosaicRow][mColumn + 1],
                                        FACTOR_7_16, error);
                    }
                    if (!(mosaicRow == mosaicHeight - 1)) {
                        // pixel bottom: 5/16
                        workingMosaic[mosaicRow
                                + 1][mColumn] = errorDistribution(
                                        workingMosaic[mosaicRow + 1][mColumn],
                                        FACTOR_5_16, error);
                        if (!(mColumn == mosaicWidth - 1)) {
                            // pixel bottom right: 1/16
                            workingMosaic[mosaicRow + 1][mColumn
                                    + 1] = errorDistribution(
                                            workingMosaic[mosaicRow + 1][mColumn
                                                    + 1],
                                            FACTOR_1_16, error);
                        }

                        if (!(mColumn == 0)) {
                            // pixel bottom left: 3/16
                            workingMosaic[mosaicRow + 1][mColumn
                                    - 1] = errorDistribution(
                                            workingMosaic[mosaicRow + 1][mColumn
                                                    - 1],
                                            FACTOR_3_16, error);
                        }
                    }
                } else {
                    // row odd -> to the left
                    // ----------------------------
                    // compute color and error
                    final int column = (mosaicWidth - mColumn) - 1;
                    final double cell = workingMosaic[mosaicRow][column];

                    if (cell < DARK_LIGHT_CUTOFF) {
                        mosaic.setElement(mosaicRow, column, dark, false);
                        error = 0 - cell;
                    } else {
                        mosaic.setElement(mosaicRow, column, light, false);
                        error = MAX_BRIGHTNESS - cell;
                    }

                    // distribute error (caution: mosaic borders!)
                    // pixel left: 7/16
                    if (!((column) == 0)) {
                        workingMosaic[mosaicRow][(column)
                                - 1] = errorDistribution(
                                        workingMosaic[mosaicRow][(column) - 1],
                                        FACTOR_7_16, error);
                    }

                    if (!(mosaicRow == mosaicHeight - 1)) {
                        // pixel bottom: 5/16
                        workingMosaic[mosaicRow
                                + 1][(column)] = errorDistribution(
                                        workingMosaic[mosaicRow + 1][(column)],
                                        FACTOR_5_16, error);
                        if (!((column) == mosaicWidth - 1)) {
                            // pixel bottom right: 3/16
                            workingMosaic[mosaicRow + 1][(column)
                                    + 1] = errorDistribution(
                                            workingMosaic[mosaicRow
                                                    + 1][(column) + 1],
                                            FACTOR_3_16, error);
                        }
                        if (!((column) == 0)) {
                            // pixel bottom left: 1/16
                            workingMosaic[mosaicRow + 1][(column)
                                    - 1] = errorDistribution(
                                            workingMosaic[mosaicRow
                                                    + 1][(column) - 1],
                                            FACTOR_1_16, error);
                        }
                    }
                }
            }
        }
    }

    private void doFloydSteinbergScanline(final int mosaicWidth,
            final int mosaicHeight, final Mosaic mosaic, final String dark,
            final String light, final double[][] workingMosaic) {
        double error;
        // *********************************** FloydSteinberg Scanline
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
                // compute color and error
                if (workingMosaic[mosaicRow][mColumn] < DARK_LIGHT_CUTOFF) {
                    mosaic.setElement(mosaicRow, mColumn, dark, false);
                    error = 0 - workingMosaic[mosaicRow][mColumn];
                } else {
                    mosaic.setElement(mosaicRow, mColumn, light, false);
                    error = MAX_BRIGHTNESS - workingMosaic[mosaicRow][mColumn];
                }

                // distribute error (caution: mosaic borders!)
                // pixel right: 7/16
                if (!(mColumn == mosaicWidth - 1)) {
                    workingMosaic[mosaicRow][mColumn + 1] = errorDistribution(
                            workingMosaic[mosaicRow][mColumn + 1], FACTOR_7_16,
                            error);
                }

                if (!(mosaicRow == mosaicHeight - 1)) {
                    // pixel bottom: 5/16
                    workingMosaic[mosaicRow + 1][mColumn] = errorDistribution(
                            workingMosaic[mosaicRow + 1][mColumn], FACTOR_5_16,
                            error);

                    if (!(mColumn == mosaicWidth - 1)) {
                        // pixel bottom right: 1/16
                        workingMosaic[mosaicRow + 1][mColumn
                                + 1] = errorDistribution(
                                        workingMosaic[mosaicRow + 1][mColumn
                                                + 1],
                                        FACTOR_1_16, error);
                    }

                    if (!(mColumn == 0)) {
                        // pixel bottom left: 3/16
                        workingMosaic[mosaicRow + 1][mColumn
                                - 1] = errorDistribution(
                                        workingMosaic[mosaicRow + 1][mColumn
                                                - 1],
                                        FACTOR_3_16, error);
                    }
                }
            }
        }
    }

    private double[][] getSourceLuminanceValues(final int mosaicWidth,
            final int mosaicHeight, final int[][][] pixelMatrix) {
        final double[][] workingMosaic = new double[mosaicHeight][mosaicWidth];
        int red;
        int green;
        int blue;

        for (int mRow = 0; mRow < mosaicHeight; mRow++) {
            for (int mColumn = 0; mColumn < mosaicWidth; mColumn++) {
                red = pixelMatrix[mRow][mColumn][0];
                green = pixelMatrix[mRow][mColumn][1];
                blue = pixelMatrix[mRow][mColumn][2];
                workingMosaic[mRow][mColumn] = (calculation
                        .rgbToLab(new Color(red, green, blue)).getL());
            }
        }
        return workingMosaic;
    }

    /**
     * ErrorDistribution Floyd-Steinberg.
     *
     * @author Adrian Schuetz
     * @param luminance (double)
     * @param factor    (double)
     * @param error     (double)
     * @return result
     */
    private double errorDistribution(final double luminance,
            final double factor, final double error) {
        double result;
        result = luminance - (error * factor);
        // Farben auf Farbraum begrenzen
        if (result > Calculator.ONE_HUNDRED_PERCENT) {
            result = Calculator.ONE_HUNDRED_PERCENT;
        }
        if (result < 0.0) {
            result = 0.0;
        }
        return result;
    }
}
