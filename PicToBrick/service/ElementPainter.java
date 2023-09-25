package pictobrick.service;

import java.awt.Color;
import java.awt.Graphics2D;

import pictobrick.model.ElementObject;

/**
 * Utility class that paints elements of a mosaic. Code moved from Mosaic by
 * John Watne 09/2023.
 */
public class ElementPainter {
    /** Minimum dark color value. */
    private static final int MIN_DARK_COLOR_VALUE = 20;
    /** Amount subtracted from initial color. */
    private static final int DARK_COLOR_DECREMENT = 50;
    /** Maximum R, G, or B color value for light color. */
    private static final int MAX_LIGHT_COLOR_VALUE = 235;
    /** Maximum R, G, or B color value. */
    private static final int MAX_COLOR_VALUE = 255;
    /** Amount added to initial color. */
    private static final int LIGHT_COLOR_INCREMENT = 50;
    /** Border thickness in pixels. */
    private static final int BORDER_THICKNESS = 4;
    /** A Graphics2D used to draw into BufferedImage for Mosaic. */
    private final Graphics2D g2d;
    /** The width of the basis element (ratio). */
    private final int width;
    /** The height of the basis element (ratio). */
    private final int height;
    /** Indicates whether to apply 3D effect. */
    private final boolean threeDEffect;

    /**
     * Constructs an ElementPainter.
     *
     * @param graphics     A Graphics2D used to draw into BufferedImage for
     *                     Mosaic.
     * @param mosaicWidth  The width of the basis element (ratio).
     * @param mosaicHeight The height of the basis element (ratio).
     * @param threeD       <code>true</code> if applying 3D effect.
     */
    public ElementPainter(final Graphics2D graphics, final int mosaicWidth,
            final int mosaicHeight, final boolean threeD) {
        this.g2d = graphics;
        this.width = mosaicWidth;
        this.height = mosaicHeight;
        this.threeDEffect = threeD;
    }

    /**
     * Paints an element in a specified color.
     *
     * @author Tobias Reichling
     * @param initialStartX
     * @param startY
     * @param element
     * @param rgb           (color)
     */
    public void paintElement(final int initialStartX, final int startY,
            final ElementObject element, final Color rgb) {
        int startX = initialStartX;
        final Color colorNormal = rgb;
        Color colorDark;
        Color colorLight;
        int leftIndicator = -1;
        int red = getDarkColor(colorNormal.getRed());
        int green = getDarkColor(colorNormal.getGreen());
        int blue = getDarkColor(colorNormal.getBlue());
        colorDark = new Color(red, green, blue);
        red = getLightColor(colorNormal.getRed());
        green = getLightColor(colorNormal.getGreen());
        blue = getLightColor(colorNormal.getBlue());
        colorLight = new Color(red, green, blue);
        // surround the element matrix with zero values
        final boolean[][] matrix = element.getMatrix();
        final boolean[][] matrixNew = new boolean[element.getHeight()
                + 2][element.getWidth() + 2];
        leftIndicator = surroundElementMatrixWithZeroValues(element,
                leftIndicator, matrix, matrixNew);
        startX = startX - (leftIndicator * width);

        for (int row = 0; row < (element.getHeight() + 2); row++) {
            for (int column = 0; column < (element.getWidth() + 2); column++) {
                if (matrixNew[row][column]) {
                    g2d.setColor(colorNormal);
                    g2d.fillRect(startX + width * (column - 1),
                            startY + height * (row - 1), width, height);
                    if (threeDEffect) {
                        process3D(startX, startY, colorDark, colorLight,
                                matrixNew, row, column);
                    }
                }
            }
        }
    }

    private void process3D(final int startX, final int startY,
            final Color colorDark, final Color colorLight,
            final boolean[][] matrixNew, final int row, final int column) {
        g2d.setColor(colorLight);

        // border left
        if (!matrixNew[row][column - 1]) {
            g2d.fillRect(startX + width * (column - 1),
                    startY + height * (row - 1) + 2, 2,
                    height - BORDER_THICKNESS);
        }

        // border top
        if (!matrixNew[row - 1][column]) {
            g2d.fillRect(startX + width * (column - 1) + 2,
                    startY + height * (row - 1), width - BORDER_THICKNESS, 2);
        }

        // corner top left
        if (!(matrixNew[row][column - 1] && matrixNew[row - 1][column]
                && matrixNew[row][column - 1])) {
            g2d.fillRect(startX + width * (column - 1),
                    startY + height * (row - 1), 2, 2);
        }

        // corner bottom left
        if (!(matrixNew[row][column - 1] && matrixNew[row + 1][column]
                && matrixNew[row + 1][column - 1])) {
            if (matrixNew[row][column - 1]) {
                g2d.setColor(colorDark);
            }

            g2d.fillRect(startX + width * (column - 1),
                    startY + height * (row - 1) + height - 2, 2, 2);
            g2d.setColor(colorLight);
        }

        g2d.setColor(colorDark);

        // border right
        if (!matrixNew[row][column + 1]) {
            g2d.fillRect(startX + width * (column - 1) + width - 2,
                    startY + height * (row - 1) + 2, 2,
                    height - BORDER_THICKNESS);
        }

        // border bottom
        if (!matrixNew[row + 1][column]) {
            g2d.fillRect(startX + width * (column - 1) + 2,
                    startY + height * (row - 1) + height - 2,
                    width - BORDER_THICKNESS, 2);
        }

        // corner top right
        if (!(matrixNew[row][column + 1] && matrixNew[row - 1][column]
                && matrixNew[row - 1][column + 1])) {
            if (matrixNew[row][column + 1]) {
                g2d.setColor(colorLight);
            }
            g2d.fillRect(startX + width * (column - 1) + width - 2,
                    startY + height * (row - 1), 2, 2);
            g2d.setColor(colorDark);
        }

        // corner bottom right
        if (!(matrixNew[row][column + 1] && matrixNew[row + 1][column]
                && matrixNew[row + 1][column + 1])) {
            g2d.fillRect(startX + width * (column - 1) + width - 2,
                    startY + height * (row - 1) + height - 2, 2, 2);
        }
    }

    private int surroundElementMatrixWithZeroValues(final ElementObject element,
            final int initialLeftIndicator, final boolean[][] matrix,
            final boolean[][] matrixNew) {
        int leftIndicator = initialLeftIndicator;

        for (int row = 0; row < (element.getHeight() + 2); row++) {
            for (int column = 0; column < (element.getWidth() + 2); column++) {
                if (row == 0 || column == 0) {
                    matrixNew[row][column] = false;
                } else if (row == element.getHeight() + 1
                        || column == element.getWidth() + 1) {
                    matrixNew[row][column] = false;
                } else {
                    if (matrix[row - 1][column - 1] && leftIndicator < 0) {
                        leftIndicator = column - 1;
                    }

                    matrixNew[row][column] = matrix[row - 1][column - 1];
                }
            }
        }

        return leftIndicator;
    }

    private int getLightColor(final int initialColor) {
        int lightColor = initialColor;

        if ((lightColor + LIGHT_COLOR_INCREMENT) > MAX_COLOR_VALUE) {
            lightColor = MAX_LIGHT_COLOR_VALUE;
        } else {
            lightColor += LIGHT_COLOR_INCREMENT;
        }

        return lightColor;
    }

    private int getDarkColor(final int initialColor) {
        int darkColor = initialColor;

        if ((darkColor - DARK_COLOR_DECREMENT) < 0) {
            darkColor = MIN_DARK_COLOR_VALUE;
        } else {
            darkColor = darkColor - DARK_COLOR_DECREMENT;
        }

        return darkColor;
    }
}
