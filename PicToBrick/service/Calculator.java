package pictobrick.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.Random;
import java.util.Vector;

import pictobrick.model.Lab;
import pictobrick.model.Picture;

/**
 * Utility class with mathematical methods.
 *
 * @author Tobias Reichling / Adrian Schuetz
 */
public class Calculator {
    /** Number of RGB elements / cells per combination of row and column. */
    private static final int NUM_RGB_ELEMENTS = 3;
    /** Bicubic interpolation. */
    private static final int BICUBIC = 1;
    /** Bilinear interpolation. */
    private static final int BILINEAR = 2;
    /** Nearest neighbor interpolation. */
    private static final int NEAREST_NEIGHBOR = 3;
    /** Power of 2.4. */
    private static final double POWER_2_4 = 2.4;
    /** Divisor value 1.055. */
    private static final double DIVISOR_1_055 = 1.055;
    /** Offset added value of 0.055. */
    private static final double OFFSET_0_055 = 0.055;
    /** Multiplier value 0.950444. */
    private static final double MULT_0_950444 = 0.950444;
    /** Multiplier value 0.119193. */
    private static final double MULT_0_119193 = 0.119193;
    /** Multiplier value 0.019332. */
    private static final double MULT_0_019332 = 0.019332;
    /** Multiplier value 0.072186. */
    private static final double MULT_0_072186 = 0.072186;
    /** Multiplier value 0.715158. */
    private static final double MULT_0_715158 = 0.715158;
    /** Multiplier value 0.212656. */
    private static final double MULT_0_212656 = 0.212656;
    /** Multiplier value 0.180464. */
    private static final double MULT_0_180464 = 0.180464;
    /** Multiplier value 0.357579. */
    private static final double MULT_0_357579 = 0.357579;
    /** Multiplier value 0.412424. */
    private static final double MULT_0_412424 = 0.412424;
    /** 95.047% (multiplied by 100.0). */
    private static final double PERCENT_95_047 = 95.047;
    /**
     * Amount added/subtracted from various LAB values to determine RGB values.
     */
    private static final double LAB_TO_RGB_OFFSET = 16.0;
    /** Amount by which various LAB values divided to determine RGB values. */
    private static final double LAB_TO_RGB_DIVISOR = 116.0;
    /** Divisor of LAB A value to determine RGB values. */
    private static final double LAB_A_DIVISOR = 500.0;
    /** Divisor of LAB B value to determine RGB values. */
    private static final double LAB_B_DIVISOR = 200.0;
    /** Power of 3 = cubed. */
    private static final double CUBED = 3.0;
    /** Max cubed value allowed in LAB to RGB calculation. */
    private static final double MAX_CUBED_VALUE = 0.008856;
    /** Divisor used in LAB to RGB calculation. */
    private static final double DIVISOR_7_787 = 7.787;
    /** 100% (multiplied by 100.0). */
    private static final double ONE_HUNDRED_PERCENT = 100.0;
    /** 108.883% (multiplied by 100.0). */
    private static final double PCT_108_883 = 108.883;
    /** Mulitplier value 3.2406. */
    private static final double MULT_3_2406 = 3.2406;
    /** Multiplier value -1.5372. */
    private static final double MULT_NEG_1_5372 = -1.5372;
    /** Multiplier value -0.4986. */
    private static final double MULT_NEG_0_4986 = -0.4986;
    /** Multiplier value -0.9689. */
    private static final double MULT_NEG_0_9689 = -0.9689;
    /** Multiplier value 1.8758. */
    private static final double MULT_1_8758 = 1.8758;
    /** Multiplier value 0.0415. */
    private static final double MULT_0_0415 = 0.0415;
    /** Multiplier value 0.0557. */
    private static final double MULT_0_0557 = 0.0557;
    /** Multiplier value -0.2040. */
    private static final double MULT_NEG_0_2040 = -0.2040;
    /** Multiplier value 1.0570. */
    private static final double MULT_1_0570 = 1.0570;
    /**
     * Maximum positive value represented by 2 bytes, as double for division
     * calculations.
     */
    private static final double MAX_2_BYTE_VALUE = 255.0;
    /**
     * Cutoff value for initial RGB values to detmerine which formula to use for
     * final conversion from LAB.
     */
    private static final double RGB_CALC_CUTOFF = 0.0031308;
    /** Final multiplier for RGB values below {@link #RGB_CALC_CUTOFF}. */
    private static final double SIMPLE_FINAL_RGB_MULTIPLIER = 12.92;
    /** Cutoff value used to determine formula for calculating LAB from RGB. */
    private static final double ADJUSTED_RGB_CUTOFF = 0.04045;
    /** Hilbert type 3. */
    private static final int HILBERT_TYPE_3 = 3;
    /** Width of mosaic being processed, in pixels. */
    private static int mosaicWidth;
    /** Height of mosaic being processed, in pixels. */
    private static int mosaicHeight;

    /**
     * Computes a s-rgb color object from a given cie-lab color object.
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param lab
     * @return rgb
     */
    public Color labToRgb(final Lab lab) {
        double x;
        double y;
        double z;
        double r;
        double g;
        double b;
        // ----------------------------------------------------
        y = (lab.getL() + LAB_TO_RGB_OFFSET) / LAB_TO_RGB_DIVISOR;
        x = lab.getA() / LAB_A_DIVISOR + y;
        z = y - lab.getB() / LAB_B_DIVISOR;
        // ----------------------------------------------------

        if (java.lang.Math.pow(y, CUBED) > MAX_CUBED_VALUE) {
            y = java.lang.Math.pow(y, CUBED);
        } else {
            y = (y - LAB_TO_RGB_OFFSET / LAB_TO_RGB_DIVISOR) / DIVISOR_7_787;
        }

        if (java.lang.Math.pow(x, CUBED) > MAX_CUBED_VALUE) {
            x = java.lang.Math.pow(x, CUBED);
        } else {
            x = (x - LAB_TO_RGB_OFFSET / LAB_TO_RGB_DIVISOR) / DIVISOR_7_787;
        }

        if (java.lang.Math.pow(z, CUBED) > MAX_CUBED_VALUE) {
            z = java.lang.Math.pow(z, CUBED);
        } else {
            z = (z - LAB_TO_RGB_OFFSET / LAB_TO_RGB_DIVISOR) / DIVISOR_7_787;
        }

        // ----------------------------------------------------
        x = x * PERCENT_95_047 / ONE_HUNDRED_PERCENT; // >
        y = y * ONE_HUNDRED_PERCENT / ONE_HUNDRED_PERCENT; // > reference
                                                           // values: CIE
        // Observer= 2°,
        // Illuminant= D65
        z = z * PCT_108_883 / ONE_HUNDRED_PERCENT; // >
        // ----------------------------------------------------
        r = x * MULT_3_2406 + y * MULT_NEG_1_5372 + z * MULT_NEG_0_4986;
        g = x * MULT_NEG_0_9689 + y * MULT_1_8758 + z * MULT_0_0415;
        b = x * MULT_0_0557 + y * MULT_NEG_0_2040 + z * MULT_1_0570;
        // ----------------------------------------------------

        if (r > RGB_CALC_CUTOFF) {
            r = getFinalRGB(r);
        } else {
            r = SIMPLE_FINAL_RGB_MULTIPLIER * r;
        }

        if (g > RGB_CALC_CUTOFF) {
            g = getFinalRGB(g);
        } else {
            g = SIMPLE_FINAL_RGB_MULTIPLIER * g;
        }

        if (b > RGB_CALC_CUTOFF) {
            b = getFinalRGB(b);
        } else {
            b = SIMPLE_FINAL_RGB_MULTIPLIER * b;
        }

        // ----------------------------------------------------
        final Color rgb = new Color(java.lang.Math.round((float) (r * 255.0)),
                java.lang.Math.round((float) (g * 255.0)),
                java.lang.Math.round((float) (b * 255.0)));
        return rgb;
    }

    /**
     * Computes a cie-lab color object from a given s-rgb color object.
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param rgb
     * @return lab
     */
    public Lab rgbToLab(final Color rgb) {
        double r;
        double g;
        double b;
        double x;
        double y;
        double z;
        // ----------------------------------------------------
        r = (double) rgb.getRed() / MAX_2_BYTE_VALUE;
        g = (double) rgb.getGreen() / MAX_2_BYTE_VALUE;
        b = (double) rgb.getBlue() / MAX_2_BYTE_VALUE;
        // ----------------------------------------------------
        r = getAdjustedRGBValue(r);
        g = getAdjustedRGBValue(g);
        b = getAdjustedRGBValue(b);
        // ----------------------------------------------------
        r = r * ONE_HUNDRED_PERCENT;
        g = g * ONE_HUNDRED_PERCENT;
        b = b * ONE_HUNDRED_PERCENT;
        // ----------------------------------------------------
        x = r * MULT_0_412424 + g * MULT_0_357579 + b * MULT_0_180464;
        y = r * MULT_0_212656 + g * MULT_0_715158 + b * MULT_0_072186;
        z = r * MULT_0_019332 + g * MULT_0_119193 + b * MULT_0_950444;
        // ----------------------------------------------------
        x = x / PERCENT_95_047; // >
        y = y / ONE_HUNDRED_PERCENT; // > reference values: CIE Observer= 2°,
                                     // Illuminant=D65
        z = z / PCT_108_883; // >
        // ----------------------------------------------------
        x = getAdjustedXYZValue(x);
        y = getAdjustedXYZValue(y);
        z = getAdjustedXYZValue(z);
        // ----------------------------------------------------
        final Lab lab = new Lab((116.0 * y) - 16.0, 500.0 * (x - y),
                200.0 * (y - z));
        return lab;
    }

    /**
     * Scaling a picture.
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param image             source image
     * @param destinationWidth  destination image width
     * @param destinationHeight destination image height
     * @param interpolation     1=bicubic, 2=bilinear, 3=nearestneighbor
     * @return destination image
     */
    public BufferedImage scale(final BufferedImage image,
            final int destinationWidth, final int destinationHeight,
            final int interpolation) {
        final double factorX = ((double) destinationWidth
                / (double) image.getWidth());
        final double factorY = ((double) destinationHeight
                / (double) image.getHeight());
        final BufferedImage imageScaled = new BufferedImage(destinationWidth,
                destinationHeight, image.getType());
        final Graphics2D g2 = imageScaled.createGraphics();

        switch (interpolation) {
        case BICUBIC:
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            break;
        case BILINEAR:
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            break;
        case NEAREST_NEIGHBOR:
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            break;
        default:
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            break;
        }

        final AffineTransform at = AffineTransform.getScaleInstance(factorX,
                factorY);
        g2.drawRenderedImage(image, at);
        return imageScaled;
    }

    /**
     * Computes a matrix of rgb-values from the source image.
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param image (including information: width and height)
     * @return int[][][] pixelMatrix
     */
    public int[][][] pixelMatrix(final BufferedImage image) {
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
        final int[] pixels = new int[imageHeight * imageWidth];
        final var pMatrix = new int[imageHeight][imageWidth][NUM_RGB_ELEMENTS];
        final PixelGrabber grabber = new PixelGrabber(image, 0, 0, imageWidth,
                imageHeight, pixels, 0, imageWidth);

        try {
            grabber.grabPixels();

            for (int row = 0; row < imageHeight; row++) {
                for (int column = 0; column < imageWidth; column++) {
                    pixelMatrixLoopProcessRowAndColumn(imageWidth, pixels,
                            pMatrix, row, column);
                }
            }
        } catch (final InterruptedException e) {
        }

        return pMatrix;
    }

    /**
     * Set R, G, and B values for the given row and column in the pixel matrix.
     *
     * @param imageWidth the width of the generated image.
     * @param pixels     the array of RGB values (hex #000000 - #FFFFFF) by row
     *                   and column.
     * @param pMatrix    the matrix populated with the individual red, green,
     *                   and blue values for each combination of row and column.
     * @param row        the row for which the values are being set.
     * @param column     the column for which the values are being set.
     */
    private void pixelMatrixLoopProcessRowAndColumn(final int imageWidth,
            final int[] pixels, final int[][][] pMatrix, final int row,
            final int column) {
        final int pixel = pixels[row * imageWidth + column];
        // Red
        pMatrix[row][column][0] = (pixel >> Picture.SHIFT16) & Picture.HEX_FF;
        // Green
        pMatrix[row][column][1] = (pixel >> Picture.SHIFT8) & Picture.HEX_FF;
        // Blue
        pMatrix[row][column][2] = (pixel) & Picture.HEX_FF;
    }

    /**
     * Computes random coordinates.
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param width
     * @param height
     * @return random coordinates as vector
     */
    public Vector<Integer> randomCoordinates(final int width,
            final int height) {
        final Vector<Integer> randomCoordinates = new Vector<>();
        final Random random = new Random();
        final int[][] coordinates = new int[2][(height * width)];
        int position;
        int tempX;
        int tempY;

        // initialize coordinates
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                coordinates[0][((i * width) + j)] = i;
                coordinates[1][((i * width) + j)] = j;
            }
        }

        // randomize coordinates
        for (int i = 0; i < (width * height); i++) {
            position = random.nextInt(width * height);
            tempX = coordinates[0][i];
            tempY = coordinates[1][i];
            coordinates[0][i] = coordinates[0][position];
            coordinates[1][i] = coordinates[1][position];
            coordinates[0][position] = tempX;
            coordinates[1][position] = tempY;
        }

        // add coordinates to the vector
        for (int x = 0; x < (height * width); x++) {
            randomCoordinates.add(coordinates[0][x]);
            randomCoordinates.add(coordinates[1][x]);
        }

        return randomCoordinates;
    }

    /**
     * Computes coordinates along a hilbert curve.
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param width
     * @param height
     * @return hilbert coordinates as vector
     */
    public Vector<Integer> hilbertCoordinates(final int width,
            final int height) {
        Calculator.mosaicHeight = height;
        Calculator.mosaicWidth = width;
        final Vector<Integer> hilbertCoordinates = new Vector<>();
        int length = 0;
        // length = largest mosaic dimension

        if (width > height) {
            length = width;
        } else {
            length = height;
        }

        // computes the recursion depth from the length
        int n = 1;

        while (Math.pow(2, n) < length) {
            n++;
        }

        // raise the length to the power of 2 because
        // we need a square (2^int x 2^int) to compute the hilbert coordinates
        length = (int) Math.pow(2, n);
        //
        // type 0 type 1 type 2 type 3
        // +------+ /\ + +------+ <------+
        // | | | | | |
        // | | | | | |
        // +------> +------+ + \/ +------+
        //
        // start recursion with type 0
        hilbert(0, 0, 0, length, n, hilbertCoordinates);
        return hilbertCoordinates;
    }

    /**
     * Computes coordinates along a hilbert curve (recursion).
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param type
     * @param x
     * @param y
     * @param length
     * @param initialN
     * @param coordinates
     */
    private void hilbert(final int type, final int x, final int y,
            final int length, final int initialN,
            final Vector<Integer> coordinates) {
        int n = initialN;
        // l2 = half length
        // for computing the four subsquares
        final int l2 = length / 2;

        // add the coordinates to the vector when the lowest recursions depth is
        // reached
        if (n == 0) {
            // coordinates are interchanged because we need them
            // to index values in an array
            // add only coordinates which are positioned in the original mosaic
            // dimensions
            if (y < mosaicHeight && x < mosaicWidth) {
                coordinates.add(y);
                coordinates.add(x);
            }
        } else {
            // if not the lowest recursions depth is reached, call deeper
            // recursions
            // decrements the recursions depth
            n = n - 1;

            //
            // type 0 type 1 type 2 type 3
            // +------+ /\ + +------+ <------+
            // | | | | | |
            // | | | | | |
            // +------> +------+ + \/ +------+
            //
            if (type == 0) {
                hilbert(1, x + l2, y, l2, n, coordinates);
                hilbert(0, x, y, l2, n, coordinates);
                hilbert(0, x, y + l2, l2, n, coordinates);
                hilbert(2, x + l2, y + l2, l2, n, coordinates);
            }

            if (type == 1) {
                hilbert(0, x + l2, y, l2, n, coordinates);
                hilbert(1, x + l2, y + l2, l2, n, coordinates);
                hilbert(1, x, y + l2, l2, n, coordinates);
                hilbert(HILBERT_TYPE_3, x, y, l2, n, coordinates);
            }

            if (type == 2) {
                hilbert(HILBERT_TYPE_3, x, y + l2, l2, n, coordinates);
                hilbert(2, x, y, l2, n, coordinates);
                hilbert(2, x + l2, y, l2, n, coordinates);
                hilbert(0, x + l2, y + l2, l2, n, coordinates);
            }

            if (type == HILBERT_TYPE_3) {
                hilbert(2, x, y + l2, l2, n, coordinates);
                hilbert(HILBERT_TYPE_3, x + l2, y + l2, l2, n, coordinates);
                hilbert(HILBERT_TYPE_3, x + l2, y, l2, n, coordinates);
                hilbert(1, x, y, l2, n, coordinates);
            }
        }
    }

    /**
     * Returns final RGB component values from initial calculated values in LAB
     * to RGB calculation, assuming initial value is greater than
     * {@link #RGB_CALC_CUTOFF}.
     *
     * @param initialRGBComponentValue Initial R, G, or B value.
     * @return final RGB component values from initial calculated values in LAB
     *         to RGB calculation.
     */
    private double getFinalRGB(final double initialRGBComponentValue) {
        return DIVISOR_1_055 * java.lang.Math.pow(initialRGBComponentValue,
                (1.0 / POWER_2_4)) - OFFSET_0_055;
    }

    /**
     * Get adjusted X, Y, or Z value in RGB to LAB calculation.
     *
     * @param initialXyzValue the initial X, Y, or Z value.
     * @return adjusted X, Y, or Z value in RGB to LAB calculation.
     */
    private double getAdjustedXYZValue(final double initialXyzValue) {
        double adjustedValue = initialXyzValue;

        if (adjustedValue > MAX_CUBED_VALUE) {
            adjustedValue = java.lang.Math.pow(adjustedValue, (1.0 / CUBED));
        } else {
            adjustedValue = (DIVISOR_7_787 * adjustedValue)
                    + (LAB_TO_RGB_OFFSET / LAB_TO_RGB_DIVISOR);
        }

        return adjustedValue;
    }

    /**
     * Get adjusted RGB component value in RGB to LAB calculation.
     *
     * @param initialRGBComponentValue the initial R, G, or B value.
     * @return adjusted RGB component value in RGB to LAB calculation.
     */
    private double getAdjustedRGBValue(final double initialRGBComponentValue) {
        double rgbValue = initialRGBComponentValue;

        if (rgbValue > ADJUSTED_RGB_CUTOFF) {
            rgbValue = java.lang.Math.pow(
                    ((rgbValue + OFFSET_0_055) / DIVISOR_1_055), POWER_2_4);
        } else {
            rgbValue = rgbValue / SIMPLE_FINAL_RGB_MULTIPLIER;
        }

        return rgbValue;
    }

}
