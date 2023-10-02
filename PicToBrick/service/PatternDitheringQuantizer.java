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
 * Pattern dithering with 2x2 color pattern.
 *
 * @author Adrian Schuetz
 */
public class PatternDitheringQuantizer implements Quantizer {
    /** Divisor for LAB values to compute mixed color values. */
    private static final int LAB_DIVISOR = 4;
    /** Starting minimum difference in determining best-fit color. */
    private static final double STARTING_MIN_DIFFERENCE = 500.0;
    /** Int constant 3. */
    private static final int INT3 = 3;
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private final Calculator calculation;
    /** Percent of rows calculated (x 100%). */
    private int percent = 0;
    /** Number of rows in image. */
    private int rows = 0;
    /** Number of row currently being processed. */
    private int mosaicRow;
    /** Luminance limit. */
    private final int luminanceLimit;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param processor
     * @param calculator
     * @param quantisationInfo
     */
    public PatternDitheringQuantizer(final DataProcessor processor,
            final Calculator calculator,
            final Vector<Object> quantisationInfo) {
        this.dataProcessing = processor;
        this.calculation = calculator;
        this.luminanceLimit = (int) quantisationInfo.get(0);
    }

    /**
     * Pattern dithering with 2x2 color pattern.
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
        // 4 colors of the dither pattern (2x2)
        String colorName1 = "";
        String colorName2 = "";
        String colorName3 = "";
        String colorName4 = "";
        // compute pixelMatrix
        final int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
        // lab color vector
        final Vector<ColorObject> labColors = new Vector<>();

        for (final Enumeration<ColorObject> allColorsEnumeration = configuration
                .getAllColors(); allColorsEnumeration.hasMoreElements();) {
            final ColorObject color = (ColorObject) (allColorsEnumeration
                    .nextElement());
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
            for (final Enumeration<ColorObject> additionalColor = labColors
                    .elements(); additionalColor.hasMoreElements();) {
                final ColorObject secondColor = additionalColor.nextElement();

                // computes color values if the luminance values of the 2 colors
                // do not differentiate more than the given limit
                if (isLuminanceLimit(color, secondColor, luminanceLimit)) {
                    // 3x fist color, 1x second color
                    ditherColors.addElement(computeMixedColorValue(color,
                            secondColor, INT3, 1));
                    ditherColors.addElement(color.getName());
                    ditherColors.addElement(color.getName());
                    ditherColors.addElement(color.getName());
                    ditherColors.addElement(secondColor.getName());
                    // 2x fist color, 2x second color
                    ditherColors.addElement(
                            computeMixedColorValue(color, secondColor, 2, 2));
                    ditherColors.addElement(color.getName());
                    ditherColors.addElement(secondColor.getName());
                    ditherColors.addElement(secondColor.getName());
                    ditherColors.addElement(color.getName());
                    // 1x fist color, 3x second color
                    ditherColors.addElement(computeMixedColorValue(color,
                            secondColor, 1, INT3));
                    ditherColors.addElement(secondColor.getName());
                    ditherColors.addElement(secondColor.getName());
                    ditherColors.addElement(secondColor.getName());
                    ditherColors.addElement(color.getName());
                }
            }
        }

        // working mosaic with lab colors
        final Lab[][] workingMosaic = new Lab[mosaicHeight][mosaicWidth];
        int red;
        int green;
        int blue;

        for (int mRow = 0; mRow < mosaicHeight; mRow++) {
            for (int mColumn = 0; mColumn < mosaicWidth; mColumn++) {
                red = pixelMatrix[mRow][mColumn][0];
                green = pixelMatrix[mRow][mColumn][1];
                blue = pixelMatrix[mRow][mColumn][2];
                workingMosaic[mRow][mColumn] = calculation
                        .rgbToLab(new Color(red, green, blue));
            }
        }

        // Number of dither colors (5 colors pro original color)
        final int ditherColorNumber = (ditherColors.size() / 5);
        // Arrays fuer Farbwerte und colorNamen der ditherColors
        final Lab[] colorValues = new Lab[ditherColorNumber];
        final String[] colorNames1 = new String[ditherColorNumber];
        final String[] colorNames2 = new String[ditherColorNumber];
        final String[] colorNames3 = new String[ditherColorNumber];
        final String[] colorNames4 = new String[ditherColorNumber];
        // Arrays mit Werten aus dem Vektor der ditherColors befuellen
        int i = 0;

        for (final Enumeration<Object> dithercolorEnumeration = ditherColors
                .elements(); dithercolorEnumeration.hasMoreElements();) {
            colorValues[i] = (Lab) (dithercolorEnumeration.nextElement());
            colorNames1[i] = (String) (dithercolorEnumeration.nextElement());
            colorNames2[i] = (String) (dithercolorEnumeration.nextElement());
            colorNames3[i] = (String) (dithercolorEnumeration.nextElement());
            colorNames4[i] = (String) (dithercolorEnumeration.nextElement());
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
                // color values
                final double l = workingMosaic[mosaicRow][mColumn].getL();
                final double a = workingMosaic[mosaicRow][mColumn].getA();
                final double b = workingMosaic[mosaicRow][mColumn].getB();

                for (int x = 0; x < ditherColorNumber; x++) {
                    lab = colorValues[x];
                    // compute difference
                    difference = Math.sqrt(Math.pow(l - lab.getL(), 2.0)
                            + Math.pow(a - lab.getA(), 2.0)
                            + Math.pow(b - lab.getB(), 2.0));

                    if (difference < minimumDifference) {
                        minimumDifference = difference;
                        colorName1 = colorNames1[x];
                        colorName2 = colorNames2[x];
                        colorName3 = colorNames3[x];
                        colorName4 = colorNames4[x];
                    }
                }
                // set color
                // choose the color from the color pattern using the position
                // information
                // if the mosaic row is even (odd) take the color from the
                // pattern position
                // where the row is even (odd)
                // column: see above
                if ((mosaicRow % 2) == 0) {
                    // mosaicRow even
                    if ((mColumn % 2) == 0) {
                        // mosaicColumn even
                        mosaic.setElement(mosaicRow, mColumn, colorName1,
                                false);
                    } else {
                        // mosaicColumn odd
                        mosaic.setElement(mosaicRow, mColumn, colorName2,
                                false);
                    }
                } else {
                    // mosaicRow odd
                    if ((mColumn % 2) == 0) {
                        // mosaicColumn even
                        mosaic.setElement(mosaicRow, mColumn, colorName3,
                                false);
                    } else {
                        // mosaicColumn odd
                        mosaic.setElement(mosaicRow, mColumn, colorName4,
                                false);
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
     * Computes the average color value from a color pattern ( color 1 + color 2
     * + color 3 + color 4 ) / 4.
     *
     * @author Adrian Schuetz
     * @param color1
     * @param color2
     * @param due1
     * @param due2
     * @return result
     */
    private Lab computeMixedColorValue(final ColorObject color1,
            final ColorObject color2, final int due1, final int due2) {
        final Lab lab1 = calculation.rgbToLab(color1.getRGB());
        final Lab lab2 = calculation.rgbToLab(color2.getRGB());
        return new Lab((lab1.getL() * due1 + lab2.getL() * due2) / LAB_DIVISOR,
                (lab1.getA() * due1 + lab2.getA() * due2) / LAB_DIVISOR,
                (lab1.getB() * due1 + lab2.getB() * due2) / LAB_DIVISOR);
    }

    /**
     * Checks if a luminance difference is allowed.
     *
     * @author Adrian Schuetz
     * @param color1
     * @param color2
     * @param limit
     * @return true or false
     */
    private boolean isLuminanceLimit(final ColorObject color1,
            final ColorObject color2, final int limit) {
        final Lab lab1 = calculation.rgbToLab(color1.getRGB());
        final Lab lab2 = calculation.rgbToLab(color2.getRGB());
        if (Math.abs(lab1.getL() - lab2.getL()) > limit) {
            return false;
        }
        return true;
    }
}
