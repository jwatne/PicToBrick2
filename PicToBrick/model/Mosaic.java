package PicToBrick.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.SwingUtilities;

import PicToBrick.service.SwingWorker;

/**
 * class: Mosaic
 * layer: DataManagement (three tier architecture)
 * description: management for mosaic data
 *
 * @author Tobias Reichling
 */
public class Mosaic {

	private int mosaicWidth = 0;
	private int mosaicHeight = 0;
	// private Vector[][] mosaicMatrix;
	private List<List<Vector<String>>> mosaicMatrix;
	private BufferedImage mosaicImage;
	private Configuration configuration;
	private final DataManagement dataManagement;
	private int percent;
	private boolean threeDEffect;

	/**
	 * method: Mosaic
	 * description: constructor
	 *
	 * @author Tobias Reichling
	 * @param width
	 * @param height
	 * @param dataManagement
	 */
	public Mosaic(final int width, final int height, final DataManagement dataManagement) {
		this.dataManagement = dataManagement;
		this.mosaicWidth = width;
		this.mosaicHeight = height;
		// this.mosaicMatrix = new Vector[height][width];
		this.mosaicMatrix = new ArrayList<>(height);

		for (int row = 0; row < height; row++) {
			this.mosaicMatrix.add(row, new ArrayList<>(height));

			for (int column = 0; column < width; column++) {
				this.mosaicMatrix.get(row).add(column, new Vector<>());
			}
		}
	}

	/**
	 * method: initVector
	 * description: (re-)initializes a vector at a given position in the mosaic
	 * matrix
	 *
	 * @author Tobias Reichling
	 * @param row
	 * @param column
	 */
	public void initVector(final int row, final int column) {
		this.mosaicMatrix.get(row).set(column, new Vector<>());
	}

	/**
	 * method: setElement
	 * description: adds a string to a vector at a given position in the mosaic
	 * matrix
	 *
	 * @author Tobias Reichling
	 * @param row
	 * @param column
	 * @param element        (String)
	 * @param atTheBeginning (true = position 0, false = position end)
	 */
	public void setElement(final int row, final int column, final String element, final boolean atTheBeginning) {
		if (atTheBeginning) {
			this.mosaicMatrix.get(row).get(column).insertElementAt(element, 0);
		} else {
			this.mosaicMatrix.get(row).get(column).addElement(element);
		}
	}

	/**
	 * method: getMosaic
	 * description: returns the mosaic matrix
	 *
	 * @author Tobias Reichling
	 * @return mosaic matrix (List<List<Vector<String>>>)
	 */
	public List<List<Vector<String>>> getMosaic() {
		return this.mosaicMatrix;
	}

	/**
	 * method: setMosaic
	 * description: sets the mosaic matrix
	 *
	 * @author Tobias Reichling
	 * @param mosaic matrix (List<List<Vector<String>>>)
	 */
	public void setMosaic(final List<List<Vector<String>>> mosaic) {
		this.mosaicMatrix = mosaic;
	}

	/**
	 * method: mosaicCopy
	 * description: returns a copy of the mosaic matrix
	 *
	 * @author Tobias Reichling
	 * @return mosaic matrix (List<List<Vector<String>>>)
	 */
	public List<List<Vector<String>>> mosaicCopy() {
		// final List<List<Vector<String>>> copy = new
		// Vector[mosaicHeight][mosaicWidth];
		final List<List<Vector<String>>> copy = new ArrayList<>(mosaicHeight);

		for (int row = 0; row < mosaicHeight; row++) {
			copy.add(row, new ArrayList<>());

			for (int column = 0; column < mosaicWidth; column++) {
				copy.get(row).set(column, new Vector<>());

				for (final Enumeration<String> pixel = mosaicMatrix.get(row).get(column).elements(); pixel
						.hasMoreElements();) {
					copy.get(row).get(column).add(pixel.nextElement());
				}
			}
		}

		return copy;
	}

	/**
	 * method: getMosaicWidth
	 * description: returns the width of the mosaic
	 *
	 * @author Tobias Reichling
	 * @return mosaic width
	 */
	public int getMosaicWidth() {
		return this.mosaicWidth;
	}

	/**
	 * method: getMosaicHeight
	 * description: returns the height of the mosaic
	 *
	 * @author Tobias Reichling
	 * @return mosaic height
	 */
	public int getMosaicHeight() {
		return this.mosaicHeight;
	}

	/**
	 * method: getMosaicImage
	 * description: returns a buffered image of the mosaic
	 *
	 * @author Tobias Reichling
	 * @return mosaicImage
	 */
	public BufferedImage getMosaicImage() {
		return this.mosaicImage;
	}

	/**
	 * method: generateMosaicImage
	 * description: returns a buffered image of the mosaic
	 *
	 * @author Tobias Reichling
	 * @param configurat (current configuration)
	 * @param threeD     (threeDEffect on/off)
	 */
	public void generateMosaicImage(final Configuration configurat, final boolean threeD) {
		configuration = configurat;
		this.threeDEffect = threeD;
		// SwingWorker
		// "construct": all commands are startet in a new thread
		// "finished": all commands are queued to the gui thread
		// after finshing aforesaid (construct-)thread
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				ElementObject element;
				ColorObject color;
				int startX = 0;
				int startY = 0;
				int width = configuration.getBasisWidth();
				int height = configuration.getBasisHeight();

				while (width < 10 || height < 10) {
					width = width * 2;
					height = height * 2;
				}

				mosaicImage = new BufferedImage(mosaicWidth * width, mosaicHeight * height, BufferedImage.TYPE_INT_RGB);
				final Graphics2D g2d = mosaicImage.createGraphics();
				int counter = 0;
				percent = 0;
				int referenceValue = (mosaicWidth * mosaicHeight) / 100;

				if (referenceValue == 0) {
					referenceValue = 1;
				}

				for (int row = 0; row < mosaicHeight; row++) {
					for (int column = 0; column < mosaicWidth; column++) {
						if (!mosaicMatrix.get(row).get(column).isEmpty()) {
							for (final Enumeration<String> matrixElement = mosaicMatrix.get(row).get(column)
									.elements(); matrixElement
											.hasMoreElements();) {
								element = configuration.getElement((String) matrixElement.nextElement());
								color = configuration.getColor((String) matrixElement.nextElement());
								startX = column * width;
								startY = row * height;
								paintElement(g2d, startX, startY, element, color.getRGB(), width, height, threeDEffect);
							}
						}

						if (counter % referenceValue == 0) {
							// try to assign the progressBar-refresh to the gui-thread
							try {
								SwingUtilities.invokeAndWait(new Runnable() {
									public void run() {
										percent++;
										dataManagement.refreshProgressBarAlgorithm(percent, 3);
									}
								});
							} catch (final Exception e) {
							}
						}
						counter++;
					}
				}
				return true;
			}

			public void finished() {
				dataManagement.setMosaicImage(mosaicImage);
			}
		};
		worker.start();
	}

	/**
	 * method: paintElement
	 * description: paints an element in a specified color
	 *
	 * @author Tobias Reichling
	 * @param g2d     (drawing area)
	 * @param startX
	 * @param startY
	 * @param element
	 * @param rgb     (color)
	 * @param width
	 * @param height
	 * @param threeD  (on/off)
	 * @return mosaic image
	 */
	private void paintElement(final Graphics2D g2d, int startX, final int startY, final ElementObject element,
			final Color rgb, final int width,
			final int height, final boolean threeD) {
		final Color colorNormal = rgb;
		Color colorDark;
		Color colorLight;
		int red = colorNormal.getRed();
		int green = colorNormal.getGreen();
		int blue = colorNormal.getBlue();
		int leftIndicator = -1;
		if ((red - 50) < 0) {
			red = 20;
		} else {
			red = red - 50;
		}
		if ((green - 50) < 0) {
			green = 20;
		} else {
			green = green - 50;
		}
		if ((blue - 50) < 0) {
			blue = 20;
		} else {
			blue = blue - 50;
		}
		colorDark = new Color(red, green, blue);
		red = colorNormal.getRed();
		green = colorNormal.getGreen();
		blue = colorNormal.getBlue();

		if ((red + 50) > 255) {
			red = 235;
		} else {
			red = red + 50;
		}

		if ((green + 50) > 255) {
			green = 235;
		} else {
			green = green + 50;
		}

		if ((blue + 50) > 255) {
			blue = 235;
		} else {
			blue = blue + 50;
		}

		colorLight = new Color(red, green, blue);
		// surround the element matrix with zero values
		final boolean[][] matrix = element.getMatrix();
		final boolean[][] matrixNew = new boolean[element.getHeight() + 2][element.getWidth() + 2];

		for (int row = 0; row < (element.getHeight() + 2); row++) {
			for (int column = 0; column < (element.getWidth() + 2); column++) {
				if (row == 0 || column == 0) {
					matrixNew[row][column] = false;
				} else if (row == element.getHeight() + 1 || column == element.getWidth() + 1) {
					matrixNew[row][column] = false;
				} else {
					if (matrix[row - 1][column - 1] && leftIndicator < 0) {
						leftIndicator = column - 1;
					}
					matrixNew[row][column] = matrix[row - 1][column - 1];
				}
			}
		}

		startX = startX - (leftIndicator * width);

		for (int row = 0; row < (element.getHeight() + 2); row++) {
			for (int column = 0; column < (element.getWidth() + 2); column++) {
				if (matrixNew[row][column]) {
					g2d.setColor(colorNormal);
					g2d.fillRect(startX + width * (column - 1), startY + height * (row - 1), width, height);
					if (threeD) {
						g2d.setColor(colorLight);
						// border left
						if (!matrixNew[row][column - 1]) {
							g2d.fillRect(startX + width * (column - 1), startY + height * (row - 1) + 2, 2, height - 4);
						}
						// border top
						if (!matrixNew[row - 1][column]) {
							g2d.fillRect(startX + width * (column - 1) + 2, startY + height * (row - 1), width - 4, 2);
						}
						// corner top left
						if (!(matrixNew[row][column - 1] && matrixNew[row - 1][column] && matrixNew[row][column - 1])) {
							g2d.fillRect(startX + width * (column - 1), startY + height * (row - 1), 2, 2);
						}
						// corner bottom left
						if (!(matrixNew[row][column - 1] && matrixNew[row + 1][column]
								&& matrixNew[row + 1][column - 1])) {
							if (matrixNew[row][column - 1]) {
								g2d.setColor(colorDark);
							}
							g2d.fillRect(startX + width * (column - 1), startY + height * (row - 1) + height - 2, 2, 2);
							g2d.setColor(colorLight);
						}
						g2d.setColor(colorDark);
						// border right
						if (!matrixNew[row][column + 1]) {
							g2d.fillRect(startX + width * (column - 1) + width - 2, startY + height * (row - 1) + 2, 2,
									height - 4);
						}
						// border bottom
						if (!matrixNew[row + 1][column]) {
							g2d.fillRect(startX + width * (column - 1) + 2, startY + height * (row - 1) + height - 2,
									width - 4, 2);
						}
						// corner top right
						if (!(matrixNew[row][column + 1] && matrixNew[row - 1][column]
								&& matrixNew[row - 1][column + 1])) {
							if (matrixNew[row][column + 1]) {
								g2d.setColor(colorLight);
							}
							g2d.fillRect(startX + width * (column - 1) + width - 2, startY + height * (row - 1), 2, 2);
							g2d.setColor(colorDark);
						}
						// corner bottom right
						if (!(matrixNew[row][column + 1] && matrixNew[row + 1][column]
								&& matrixNew[row + 1][column + 1])) {
							g2d.fillRect(startX + width * (column - 1) + width - 2,
									startY + height * (row - 1) + height - 2, 2, 2);
						}
					}
				}
			}
		}
	}
}
