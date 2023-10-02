package pictobrick.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.service.ElementPainter;
import pictobrick.service.SwingWorker;

/**
 * Management for mosaic data.
 *
 * @author Tobias Reichling
 */
public class Mosaic {
    /** Minimum width or height. */
    public static final int MIN_WIDTH_OR_HEIGHT = 10;
    /** Painting portion of generating mosaic image. */
    private static final int PAINTING = 3;
    /** One hundred. */
    private static final int ONE_HUNDRED = 100;
    /** Mosaic width. */
    private int mosaicWidth = 0;
    /** Mosaic height. */
    private int mosaicHeight = 0;
    /** Mosaic matrix. */
    private List<List<Vector<String>>> mosaicMatrix;
    /** Mosaic image. */
    private BufferedImage mosaicImage;
    /** Configuration. */
    private Configuration configuration;
    /** Data manager. */
    private final DataManagement dataManagement;
    /** Percent (0-100). */
    private int percent;
    /** Indicates whether to apply 3D effect. */
    private boolean threeDEffect;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param width
     * @param height
     * @param dataManager
     */
    public Mosaic(final int width, final int height,
            final DataManagement dataManager) {
        this.dataManagement = dataManager;
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
     * (Re-)initializes a vector at a given position in the mosaic matrix.
     *
     * @author Tobias Reichling
     * @param row
     * @param column
     */
    public void initVector(final int row, final int column) {
        this.mosaicMatrix.get(row).set(column, new Vector<>());
    }

    /**
     * Adds a string to a vector at a given position in the mosaic matrix.
     *
     * @author Tobias Reichling
     * @param row
     * @param column
     * @param element        (String)
     * @param atTheBeginning (true = position 0, false = position end)
     */
    public void setElement(final int row, final int column,
            final String element, final boolean atTheBeginning) {
        if (atTheBeginning) {
            this.mosaicMatrix.get(row).get(column).insertElementAt(element, 0);
        } else {
            this.mosaicMatrix.get(row).get(column).addElement(element);
        }
    }

    /**
     * Returns the mosaic matrix.
     *
     * @author Tobias Reichling
     * @return mosaic matrix (List<List<Vector<String>>>)
     */
    public List<List<Vector<String>>> getMosaic() {
        return this.mosaicMatrix;
    }

    /**
     * Sets the mosaic matrix.
     *
     * @author Tobias Reichling
     * @param mosaic matrix (List<List<Vector<String>>>)
     */
    public void setMosaic(final List<List<Vector<String>>> mosaic) {
        this.mosaicMatrix = mosaic;
    }

    /**
     * Returns a copy of the mosaic matrix.
     *
     * @author Tobias Reichling
     * @return mosaic matrix (List<List<Vector<String>>>)
     */
    public List<List<Vector<String>>> mosaicCopy() {
        // final List<List<Vector<String>>> copy = new
        // Vector[mosaicHeight][mosaicWidth];
        final Mosaic copyMosaic = new Mosaic(mosaicWidth, mosaicHeight,
                dataManagement);
        final List<List<Vector<String>>> copy = copyMosaic.getMosaic();

        for (int row = 0; row < mosaicHeight; row++) {
            for (int column = 0; column < mosaicWidth; column++) {
                final Vector<String> vector = copy.get(row).get(column);

                for (final Enumeration<String> pixel = mosaicMatrix.get(row)
                        .get(column).elements(); pixel.hasMoreElements();) {
                    vector.add(pixel.nextElement());
                }
            }
        }

        return copy;
    }

    /**
     * Returns the width of the mosaic.
     *
     * @author Tobias Reichling
     * @return mosaic width
     */
    public int getMosaicWidth() {
        return this.mosaicWidth;
    }

    /**
     * Returns the height of the mosaic.
     *
     * @author Tobias Reichling
     * @return mosaic height
     */
    public int getMosaicHeight() {
        return this.mosaicHeight;
    }

    /**
     * Returns a buffered image of the mosaic.
     *
     * @author Tobias Reichling
     * @return mosaicImage
     */
    public BufferedImage getMosaicImage() {
        return this.mosaicImage;
    }

    /**
     * Returns a buffered image of the mosaic.
     *
     * @author Tobias Reichling
     * @param config (current configuration)
     * @param threeD (threeDEffect on/off)
     */
    public void generateMosaicImage(final Configuration config,
            final boolean threeD) {
        configuration = config;
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

                while (width < MIN_WIDTH_OR_HEIGHT
                        || height < MIN_WIDTH_OR_HEIGHT) {
                    width = width * 2;
                    height = height * 2;
                }

                mosaicImage = new BufferedImage(mosaicWidth * width,
                        mosaicHeight * height, BufferedImage.TYPE_INT_RGB);
                final Graphics2D g2d = mosaicImage.createGraphics();
                int counter = 0;
                percent = 0;
                int referenceValue = (mosaicWidth * mosaicHeight) / ONE_HUNDRED;

                if (referenceValue == 0) {
                    referenceValue = 1;
                }

                final ElementPainter painter = new ElementPainter(g2d, width,
                        height, threeDEffect);

                for (int row = 0; row < mosaicHeight; row++) {
                    for (int column = 0; column < mosaicWidth; column++) {
                        if (!mosaicMatrix.get(row).get(column).isEmpty()) {
                            for (final var matrixElement = mosaicMatrix.get(row)
                                    .get(column).elements(); matrixElement
                                            .hasMoreElements();) {
                                element = configuration.getElement(
                                        (String) matrixElement.nextElement());
                                color = configuration.getColor(
                                        (String) matrixElement.nextElement());
                                startX = column * width;
                                startY = row * height;
                                painter.paintElement(startX, startY, element,
                                        color.getRGB());
                            }
                        }

                        if (counter % referenceValue == 0) {
                            // try to assign the progressBar-refresh to the
                            // gui-thread
                            try {
                                SwingUtilities.invokeAndWait(new Runnable() {
                                    public void run() {
                                        percent++;
                                        dataManagement
                                                .refreshProgressBarAlgorithm(
                                                        percent, PAINTING);
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

}
