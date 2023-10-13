package pictobrick.service;

import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

import pictobrick.model.Configuration;
import pictobrick.model.ElementObject;
import pictobrick.model.Mosaic;

/**
 * Tiling with additional molding optimization. Code moved from MoldingOptimizer
 * by John Watne 10/2023.
 */
public class MoldingAdditionalOptimizer {
    /** Critical quantity for additional optimization, 2x2. */
    private static final int CRITICAL_2X2 = 8;
    /** Critical quantity for additional optimization, corner 2x2. */
    private static final int CRITICAL_CORNER_COVERING_2X2 = 4;
    /** Critical quantity for additional optimization, 1x3. */
    private static final int CRITICAL_1X3 = 12;
    /** Critical quantity for additional optimization, 1x2. */
    private static final int CRITICAL_1X2 = 16;
    /** Parent molding optimizer. */
    private MoldingOptimizer moldingOptimizer;
    /** Array of tiling elements for additional optimization. */
    private int[][] optElements;
    /** Configuration used. */
    private Configuration configuration;

    /**
     * Constructs the optimizer for the given parent base molding optimizer.
     *
     * @param parent the parent base optimizer.
     */
    public MoldingAdditionalOptimizer(final MoldingOptimizer parent) {
        this.moldingOptimizer = parent;
    }

    /** Sets configuration used. */
    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Performs additional optimization, making random usage of critical
     * elements to avoid visual artefacts, and strict usage of very critical
     * elements. Critical elements have a quantity bigger than the quantity of
     * the element in a normal molding. Very critical elements have a quantity
     * twice as much as the quantity of the element in a normal molding.
     */
    public void performAdditionalOptimization() {
        final Random random = new Random();

        // 1x2 element
        calculateOptElements(random, MoldingOptimizer.COVERING_1X2,
                CRITICAL_1X2);
        // 1x3 element
        calculateOptElements(random, MoldingOptimizer.COVERING_1X3,
                CRITICAL_1X3);
        // corner element
        calculateOptElements(random, MoldingOptimizer.CORNER_COVERING_2X2,
                CRITICAL_CORNER_COVERING_2X2);
        // 2x2 element
        calculateOptElements(random, MoldingOptimizer.COVERING_2X2,
                CRITICAL_2X2);
    }

    public boolean getAdditionalOptimizationModeElementSet(
            final int mosaicWidth, final int mosaicHeight, final Mosaic mosaic,
            final boolean elementInitiallySet, final int colorCol) {
        boolean elementSet = elementInitiallySet;
        final var critElems = new Vector<ElementObject>();

        // 2x2 element
        if (covering2x2()) {
            final var criticalElements4 = computeElements(4, 2, 2);

            while (criticalElements4.hasMoreElements()) {
                critElems.add(criticalElements4.nextElement());
            }
        }

        // corner element
        if (cornerElement()) {
            final var criticalElements3 = computeElements(3, 2, 2);

            while (criticalElements3.hasMoreElements()) {
                critElems.add(criticalElements3.nextElement());
            }
        }

        final int currColorNum = moldingOptimizer.getCurrColorNum();

        // 1x3 element
        if (optElements[currColorNum][MoldingOptimizer.COVERING_1X3] == 1) {
            final var criticalElements2 = computeElements(3, 1, 3);

            while (criticalElements2.hasMoreElements()) {
                critElems.add(criticalElements2.nextElement());
            }
        }

        // 1x2 element
        if (optElements[currColorNum][MoldingOptimizer.COVERING_1X2] == 1) {
            final var criticalElements1 = computeElements(2, 1, 2);

            while (criticalElements1.hasMoreElements()) {
                critElems.add(criticalElements1.nextElement());
            }
        }

        // if critical elements are found, the critical
        // elment vector is scanned
        // for each element we check, if we can used it in
        // the
        // mosaic with a maximal re-coloring of 1 pixel
        if (critElems.size() > 0) {
            elementSet = processCriticalElements(mosaicWidth, mosaicHeight,
                    mosaic, elementSet, colorCol, critElems);
        } // end if (criticalElements.size()>0)

        return elementSet;
    }

    private void calculateOptElements(final Random random, final int type,
            final int criticalValue) {
        final int[][] elementArray = moldingOptimizer.getElementArray();
        final int currColorNum = moldingOptimizer.getCurrColorNum();

        // Very critical.
        if (elementArray[currColorNum][type] > 2 * criticalValue) {
            optElements[currColorNum][type] = 1;
            // critical: random
        } else if (elementArray[currColorNum][type] > criticalValue) {
            if (random.nextInt(2) % 2 == 0) {
                optElements[currColorNum][type] = 1;
            } else {
                optElements[currColorNum][type] = 0;
            }
        } else {
            // not critical
            optElements[currColorNum][type] = 0;
        }
    }

    private boolean processCriticalElements(final int mosaicWidth,
            final int mosaicHeight, final Mosaic mosaic,
            final boolean initialElementSet, final int colorCol,
            final Vector<ElementObject> critElems) {
        boolean elementSet = initialElementSet;
        Vector<String> pixel2;
        int left;
        ElementObject currentElement;
        final var criticalElementsEnum = critElems.elements();

        while (criticalElementsEnum.hasMoreElements() && !elementSet) {
            currentElement = criticalElementsEnum.nextElement();
            // find the left element in the top row
            left = -1;

            for (int i = 0; i < currentElement.getWidth(); i++) {
                if (currentElement.getMatrix()[0][i] && left == -1) {
                    left = i;
                }
            }

            // check mosaic borders
            // bottom
            final int colorRow = moldingOptimizer.getColorRow();

            if (((colorRow + currentElement.getHeight() - 1) < mosaicHeight)
                    // left
                    && ((colorCol - left) >= 0)
                    // right
                    && ((colorCol + (currentElement.getWidth()
                            - (left + 1))) < mosaicWidth)) {
                // color matching
                // -------------------------
                // count all pixel who fits in color
                int fits = 0;
                boolean occupied = false;

                for (int elementRow = 0; elementRow < currentElement
                        .getHeight(); elementRow++) {
                    for (int elementColumn = 0; elementColumn < currentElement
                            .getWidth(); elementColumn++) {
                        // test only "true" positions in
                        // elment matrix
                        if (currentElement
                                .getMatrix()[elementRow][elementColumn]) {
                            pixel2 = mosaic.getMosaic()
                                    .get(colorRow + elementRow)
                                    .get((colorCol + elementColumn) - left);
                            final String currentColorName = moldingOptimizer
                                    .getCurrentColorName();

                            if (!pixel2.isEmpty()) {
                                if (((String) (pixel2.get(0)))
                                        .equals(currentColorName)) {
                                    fits++;
                                }
                            } else {
                                occupied = true;
                            }
                        }
                    }
                }

                // if the number of fitting colors is
                // equal or only 1 less than the element
                // size
                // the element is set to the mosaic
                // (increment the
                // pixelColorChanges-counter)
                if (fits >= (computeElementSize(currentElement) - 1)
                        && !occupied) {
                    // set element
                    int pixelColorChanges = moldingOptimizer
                            .getPixelColorChanges();
                    moldingOptimizer.setPixelColorChanges(pixelColorChanges++);
                    elementSet = true;

                    for (int elementRow = 0; elementRow < currentElement
                            .getHeight(); elementRow++) {
                        for (int elementCol = 0; elementCol < currentElement
                                .getWidth(); elementCol++) {
                            if (currentElement
                                    .getMatrix()[elementRow][elementCol]) {
                                mosaic.initVector(colorRow + elementRow,
                                        (colorCol + elementCol) - left);
                            }
                        }
                    }

                    final String currentColorName = moldingOptimizer
                            .getCurrentColorName();
                    mosaic.setElement(colorRow, colorCol, currentColorName,
                            false);
                    mosaic.setElement(colorRow, colorCol,
                            currentElement.getName(), true);
                    // count element and if necessary
                    // use new molding
                    moldingOptimizer.updateElementAppearance(currentElement);
                }
            }
        } // end while

        return elementSet;
    }

    /**
     * Computes the size of an element.
     *
     * @author Tobias Reichling
     * @param element
     * @return size
     */
    private int computeElementSize(final ElementObject element) {
        int result = 0;

        for (int row = 0; row < element.getHeight(); row++) {
            for (int column = 0; column < element.getWidth(); column++) {
                if (element.getMatrix()[row][column]) {
                    result++;
                }
            }
        }

        return result;
    }

    /**
     * Computes elements.
     *
     * @param covering
     * @param width
     * @param height
     * @return Enumeration of ElementObjects.
     * @author Tobias Reichling
     */
    private Enumeration<ElementObject> computeElements(final int covering,
            final int width, final int height) {
        ElementObject element;
        int cover = 0;
        final Vector<ElementObject> elements = new Vector<>();
        final Enumeration<ElementObject> allElements = configuration
                .getAllElements();

        while (allElements.hasMoreElements()) {
            element = allElements.nextElement();

            // check width and height ...
            if (element.getWidth() == width && element.getHeight() == height) {
                cover = 0;

                for (int i = 0; i < element.getHeight(); i++) {
                    for (int j = 0; j < element.getWidth(); j++) {
                        if (element.getMatrix()[i][j]) {
                            cover++;
                        }
                    }
                }

                // ... and the covering of the element
                if (cover == covering) {
                    elements.add(element);
                }
            }

            // also check covering if width = height and height = width because
            // there
            // are different directions of elements use (2x3 = 3x2)
            if (element.getWidth() == height && element.getHeight() == width) {
                cover = 0;

                for (int i = 0; i < element.getHeight(); i++) {
                    for (int j = 0; j < element.getWidth(); j++) {
                        if (element.getMatrix()[i][j]) {
                            cover++;
                        }
                    }
                }

                if (cover == covering) {
                    elements.add(element);
                }
            }
        }

        final Enumeration<ElementObject> elementsEnum = elements.elements();
        return elementsEnum;
    }

    private boolean cornerElement() {
        final int currColorNum = moldingOptimizer.getCurrColorNum();
        return optElements[currColorNum][MoldingOptimizer.CORNER_COVERING_2X2] == 1;
    }

    private boolean covering2x2() {
        final int currColorNum = moldingOptimizer.getCurrColorNum();
        return optElements[currColorNum][MoldingOptimizer.COVERING_2X2] == 1;
    }

    public void initializeElementArrayOptimization() {
        final int colorCount = moldingOptimizer.getColorCount();
        // init array
        optElements = new int[colorCount][MoldingOptimizer.NUMBER_OF_OPT_TILING_OPTIONS];

        for (int j = 0; j < colorCount; j++) {
            optElements[j][MoldingOptimizer.COVERING_1X1] = 0; // 0 => 1x1
                                                               // covering:
            // 1
            optElements[j][MoldingOptimizer.COVERING_1X2] = 0; // 1 => 1x2
                                                               // covering:
            // 2
            optElements[j][MoldingOptimizer.COVERING_1X3] = 0; // 2 => 1x3
                                                               // covering:
            // 3
            // and matrix 1x3
            optElements[j][MoldingOptimizer.CORNER_COVERING_2X2] = 0; // 3 =>
                                                                      // corner
            // covering:
            // 3 and matrix 2x2
            optElements[j][MoldingOptimizer.COVERING_2X2] = 0; // 4 => 2x2
                                                               // covering:
            // 4
        }
    }

}
