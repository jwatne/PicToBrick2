package pictobrick.model;

import javax.imageio.ImageIO;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

/**
 * Contains all information and methods for an image.
 *
 * @author Adrian Schuetz
 */

public class Picture extends Canvas {
    /** Hex value ff = 255 decimal. */
    public static final int HEX_FF = 0xff;
    /** 16 bit shift. */
    public static final int SHIFT16 = 16;
    /** 8 bit shift. */
    public static final int SHIFT8 = 8;
    /** Number of components (3) in RGB color value. */
    private static final int COLOR_VALUES = 3;
    /**
     * Number subtracted from slider value for exponent used to calculate
     * scaling factor.
     */
    private static final int SCALING_FACTOR_POWER_ADJUSTMENT = 3;
    /**
     * Value to which rectangle's width or height is set if original value <= 0.
     */
    private static final int MIN_WIDTH_OR_HEIGHT = 10;
    /** Image. */
    private BufferedImage image;
    /** Scaled image. */
    private BufferedImage scaledImage;
    /** Magnification factor. */
    private double factor = 1.0;
    /** Name of image. */
    private String imageName = "";
    /** Scaling factor. */
    private double scalingFactor;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param file (image file)
     * @exception exception
     */
    public Picture(final File file) throws IOException {
        loadImage(file);
        this.imageName = file.getName();
    }

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param bufferedImage
     */
    public Picture(final BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    /**
     * Loads an image file.
     *
     * @author Adrian Schuetz
     * @param file (image file)
     * @exception exception
     */
    private void loadImage(final File file) throws IOException {
        this.image = ImageIO.read(file);

        if (image.getType() == 0) {
            // if type is custom do not load image
            this.image = null;
        }
    }

    /**
     * Replace the image with a cutout of the original image.
     *
     * @author Adrian Schuetz
     * @param rectangle rectangle
     */
    public void cutout(final Rectangle rectangle) {
        // map rectangle coordinates from scaled image to original image
        final int startX = java.lang.Math
                .round((float) (rectangle.x / scalingFactor));
        final int startY = java.lang.Math
                .round((float) (rectangle.y / scalingFactor));
        final int endX = java.lang.Math.round(
                (float) ((rectangle.x + rectangle.width) / scalingFactor));
        final int endY = java.lang.Math.round(
                (float) ((rectangle.y + rectangle.height) / scalingFactor));

        // the cutout is processed from the scaled image
        try {
            this.image = this.image.getSubimage(startX, startY, (endX - startX),
                    (endY - startY));
        } catch (final Exception e) {
            System.out.println("startX: " + startX);
            System.out.println("startY: " + startY);
            System.out.println("endX - startX: " + (endX - startX));
            System.out.println("endy - startY: " + (endY - startY));
        }
    }

    /**
     * Scales the image.
     *
     * @author Adrian Schuetz
     * @param scaleFactor
     */
    private void scaleImage(final double scaleFactor) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final int scaleWidth = (int) (width * scaleFactor);
        final int scaleHeight = (int) (height * scaleFactor);
        this.scaledImage = new BufferedImage(scaleWidth, scaleHeight,
                image.getType());
        // creates an graphics2d object, which is used to draw the scaled image
        final Graphics2D g2 = scaledImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        final AffineTransform at = AffineTransform.getScaleInstance(scaleFactor,
                scaleFactor);
        g2.drawRenderedImage(image, at);
    }

    /**
     * Returns the image.
     *
     * @author Adrian Schuetz
     * @return image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Returns the image name.
     *
     * @author Adrian Schuetz
     * @return image name
     */
    public String getImageName() {
        return this.imageName;
    }

    /**
     * Computes the scale factor from the user-defined rectangle.
     *
     * @author Adrian Schuetz
     * @param width
     * @param height
     */
    public void computeScaleFactor(final double width, final double height) {
        final double imageWidth = (double) image.getWidth();
        final double imageHeight = (double) image.getHeight();
        double rectangleWidth;
        double rectangleHeight;

        if (width <= 0) {
            rectangleWidth = MIN_WIDTH_OR_HEIGHT;
        } else {
            rectangleWidth = width;
        }

        if (height <= 0) {
            rectangleHeight = MIN_WIDTH_OR_HEIGHT;
        } else {
            rectangleHeight = height;
        }

        // compute factor
        if ((rectangleHeight / rectangleWidth) >= (imageHeight / imageWidth)) {
            this.factor = rectangleWidth / imageWidth;
        } else {
            this.factor = rectangleHeight / imageHeight;
        }
    }

    /**
     * Returns a scaled image to a given slider value.
     *
     * @author Adrian Schuetz
     * @param sliderValue value of the scaling slider.
     * @return scaled image
     */
    public BufferedImage getScaledImage(final int sliderValue) {
        this.scalingFactor = factor * Math.pow((Math.sqrt(2.0)),
                (sliderValue - SCALING_FACTOR_POWER_ADJUSTMENT));
        scaleImage(scalingFactor);
        return this.scaledImage;
    }

    /**
     * Overwrites the paint method (Canvas).
     *
     * @author Adrian Schuetz
     * @param g2d
     */
    public void paint(final Graphics g2d) {
        g2d.drawImage(image, 1, 1, this);
    }

    /**
     * Returns the image height.
     *
     * @author Adrian Schuetz
     * @return image height
     */
    public int getHeight() {
        return image.getHeight(this);
    }

    /**
     * Returns the image width.
     *
     * @author Adrian Schuetz
     * @return image width
     */
    public int getWidth() {
        return image.getWidth(this);
    }

    /**
     * Returns a 3D pixel matrix (width, height, rgb).
     *
     * @author Adrian Schuetz
     * @return pixel matrix
     */
    public int[][][] getPixelMatrix() {
        final int width = image.getWidth(this);
        final int height = image.getHeight(this);
        final int[] pixels = new int[height * width];
        final int[][][] pixelMatrix = new int[height][width][COLOR_VALUES];
        final PixelGrabber grabber = new PixelGrabber(image, 0, 0, width,
                height, pixels, 0, width);
        try {
            grabber.grabPixels();

            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    final int pixel = pixels[row * width + column];
                    pixelMatrix[row][column][0] = getRedComponent(pixel);
                    pixelMatrix[row][column][1] = getGreenComponent(pixel);
                    pixelMatrix[row][column][2] = getBlueComponent(pixel);
                }
            }
        } catch (final InterruptedException e) {
        }

        return pixelMatrix;
    }

    private int getBlueComponent(final int pixel) {
        return (pixel) & HEX_FF;
    }

    private int getGreenComponent(final int pixel) {
        return (pixel >> SHIFT8) & HEX_FF;
    }

    private int getRedComponent(final int pixel) {
        return (pixel >> SHIFT16) & HEX_FF;
    }
}
