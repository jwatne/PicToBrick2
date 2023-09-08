package pictobrick.model;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

/**
 * class: Picture
 * layer: DataManagement (three tier architecture)
 * description: contains all information and methods for an image
 *
 * @author Adrian Schuetz
 */

public class Picture
		extends Canvas {
	private BufferedImage image;
	private BufferedImage scaledImage;
	private double factor = 1.0;
	private String imageName = "";
	private double scalingFactor;

	/**
	 * method: Picture
	 * description: constructor
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
	 * method: Picture
	 * description: constructor
	 *
	 * @author Adrian Schuetz
	 * @param image
	 */
	public Picture(final BufferedImage image) {
		this.image = image;
	}

	/**
	 * method: loadImage
	 * description: loads an image file
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
	 * method: cutout
	 * description: replace the image with a cutout of the original image
	 *
	 * @author Adrian Schuetz
	 * @param cutout rectangle
	 */
	public void cutout(final Rectangle rectangle) {
		// map rectangle coordinates from scaled image to original image
		final int startX = java.lang.Math.round((float) (rectangle.x / scalingFactor));
		final int startY = java.lang.Math.round((float) (rectangle.y / scalingFactor));
		final int endX = java.lang.Math.round((float) ((rectangle.x + rectangle.width) / scalingFactor));
		final int endY = java.lang.Math.round((float) ((rectangle.y + rectangle.height) / scalingFactor));

		// the cutout is processed from the scaled image
		try {
			this.image = this.image.getSubimage(startX, startY, (endX - startX), (endY - startY));
		} catch (final Exception e) {
			System.out.println("startX: " + startX);
			System.out.println("startY: " + startY);
			System.out.println("endX - startX: " + (endX - startX));
			System.out.println("endy - startY: " + (endY - startY));
		}
	}

	/**
	 * method: scaleImage
	 * description: scales the image
	 *
	 * @author Adrian Schuetz
	 * @param scaleFactor
	 */
	private void scaleImage(final double scaleFactor) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		final int scaleWidth = (int) (width * scaleFactor);
		final int scaleHeight = (int) (height * scaleFactor);
		this.scaledImage = new BufferedImage(scaleWidth, scaleHeight, image.getType());
		// creates an graphics2d object, which is used to draw the scaled image
		final Graphics2D g2 = scaledImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		final AffineTransform at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
		g2.drawRenderedImage(image, at);
	}

	/**
	 * method: getImage
	 * description: returns the image
	 *
	 * @author Adrian Schuetz
	 * @return image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * method: getImageName
	 * description: returns the image name
	 *
	 * @author Adrian Schuetz
	 * @return image name
	 */
	public String getImageName() {
		return this.imageName;
	}

	/**
	 * method: computeScaleFactor
	 * description: computes the scale factor from the user-defined rectangle
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
			rectangleWidth = 10;
		} else {
			rectangleWidth = width;
		}
		if (height <= 0) {
			rectangleHeight = 10;
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
	 * method: getScaledImage
	 * description: returns a scaled image to a given slider value
	 *
	 * @author Adrian Schuetz
	 * @return scaled image
	 */
	public BufferedImage getScaledImage(final int sliderValue) {
		this.scalingFactor = factor * Math.pow((Math.sqrt(2.0)), (sliderValue - 3));
		scaleImage(scalingFactor);
		return this.scaledImage;
	}

	/**
	 * method: paint
	 * description: overwrites the paint method (Canvas)
	 *
	 * @author Adrian Schuetz
	 * @param g2d
	 */
	public void paint(final Graphics g2d) {
		g2d.drawImage(image, 1, 1, this);
	}

	/**
	 * method: getHeight
	 * description: returns the image height
	 *
	 * @author Adrian Schuetz
	 * @return image height
	 */
	public int getHeight() {
		return image.getHeight(this);
	}

	/**
	 * method: getWidth
	 * description: returns the image width
	 *
	 * @author Adrian Schuetz
	 * @return image width
	 */
	public int getWidth() {
		return image.getWidth(this);
	}

	/**
	 * method: getPixelMatrix
	 * description: returns a 3D pixel matrix (width, height, rgb)
	 *
	 * @author Adrian Schuetz
	 * @return pixel matrix
	 */
	public int[][][] getPixelMatrix() {
		final int width = image.getWidth(this), height = image.getHeight(this);
		final int pixels[] = new int[height * width];
		final int pixelMatrix[][][] = new int[height][width][3];
		final PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
		try {
			grabber.grabPixels();
			for (int row = 0; row < height; row++) {
				for (int column = 0; column < width; column++) {
					final int pixel = pixels[row * width + column];
					pixelMatrix[row][column][0] = (pixel >> 16) & 0xff; // red
					pixelMatrix[row][column][1] = (pixel >> 8) & 0xff; // green
					pixelMatrix[row][column][2] = (pixel) & 0xff; // blue
				}
			}
		} catch (final InterruptedException e) {
		}
		return pixelMatrix;
	}
}
