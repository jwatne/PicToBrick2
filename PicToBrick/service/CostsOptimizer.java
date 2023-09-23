package pictobrick.service;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.Configuration;
import pictobrick.model.ElementObject;
import pictobrick.model.Mosaic;

/**
 * Tiling with cost optimisation.
 *
 * @author Tobias Reichling
 */
public class CostsOptimizer implements Tiler {
    /** Show in both dialog and output documents. */
    private static final int SHOW_IN_BOTH = 3;
    /** Int divisor for percentages. */
    private static final int ONE_HUNDRED_PERCENT = 100;
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private final Calculator calculation;
    /** Progress bar percentage. */
    private int procent = 0;
    /** Reference value. */
    private int referenceValue;
    /** Number of elements. */
    private int elements = 0;
    /** Total costs in hundreths of Euros. */
    private int totalCosts = 0;

    /**
     * Contructor.
     *
     * @author Tobias Reichling
     * @param processor  dataProcessing
     * @param calculator calculation
     */
    public CostsOptimizer(final DataProcessor processor,
            final Calculator calculator) {
        this.dataProcessing = processor;
        this.calculation = calculator;
    }

    /**
     * Tiling with cost optimisation.
     *
     * @author Tobias Reichling
     * @param mosaicWidth
     * @param mosaicHeight
     * @param configuration
     * @param mosaic        the mosaic.
     * @param statistic
     * @return mosaic
     */
    public Mosaic tiling(final int mosaicWidth, final int mosaicHeight,
            final Configuration configuration, final Mosaic mosaic,
            final boolean statistic) {
        referenceValue = (mosaicWidth * mosaicHeight) / ONE_HUNDRED_PERCENT;

        if (referenceValue == 0) {
            referenceValue = 1;
        }

        // Vector with all elements (sorted by costs)
        final Vector<ElementObject> elementsSorted = sortElementsByCosts(
                configuration.getAllElements());
        // run through the image by random - determine element
        // compute random coordinates
        final Vector<Integer> coords = calculation
                .randomCoordinates(mosaicWidth, mosaicHeight);
        final Enumeration<Integer> coordsEnum = coords.elements();

        // run through the image (random coordinates)
        while (coordsEnum.hasMoreElements()) {
            handleNextPixel(mosaicWidth, mosaicHeight, configuration, mosaic,
                    elementsSorted, coordsEnum);
        }

        dataProcessing.setInfo(
                textbundle.getString("output_costsOptimisation_1"),
                SHOW_IN_BOTH);
        dataProcessing
                .setInfo(
                        textbundle.getString("output_costsOptimisation_2")
                                + ": " + totalCosts / ONE_HUNDRED_PERCENT
                                + textbundle.getString("output_decimalPoint")
                                + totalCosts % ONE_HUNDRED_PERCENT + " "
                                + textbundle.getString(
                                        "output_costsOptimisation_3"),
                        SHOW_IN_BOTH);

        // Update progressbar
        finishProgressBar();
        return mosaic;
    }

    private void handleNextPixel(final int mosaicWidth, final int mosaicHeight,
            final Configuration configuration, final Mosaic mosaic,
            final Vector<ElementObject> elementsSorted,
            final Enumeration<Integer> coordsEnum) {
        // pixel of the current position

        // if the pixel is not already covered by an element
        handleUncoveredPixel(mosaicWidth, mosaicHeight, configuration, mosaic,
                elementsSorted, coordsEnum);

        // Update progressbar
        if (elements % referenceValue == 0) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        procent++;
                        dataProcessing.refreshProgressBarAlgorithm(procent, 2);
                    }
                });
            } catch (final Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    private void handleUncoveredPixel(final int mosaicWidth,
            final int mosaicHeight, final Configuration configuration,
            final Mosaic mosaic, final Vector<ElementObject> elementsSorted,
            final Enumeration<Integer> coordsEnum) {
        // position in the image (random coordinates from the vector)
        final int colorRow = (Integer) coordsEnum.nextElement();
        final int colorColumn = (Integer) coordsEnum.nextElement();
        final Vector<String> pixel = mosaic.getMosaic().get(colorRow)
                .get(colorColumn);

        if (pixel.size() == 1) {
            String currentColor;
            Enumeration<ElementObject> sorted;
            ElementObject currentElement;
            boolean elementSet;
            Vector<Integer> elementCoords;
            int left;
            int top;
            // compute pixel color
            currentColor = (String) (pixel.get(0));

            // the current pixel is a 1x1 element if the 4 surrounding pixels
            // are not of the same color or covered by another element
            // No border handling
            if (currentPixelIs1X1Element(mosaicWidth, mosaicHeight, mosaic,
                    colorRow, colorColumn, currentColor)) {
                // set current element to basis element
                currentElement = configuration
                        .getElement(configuration.getBasisName());
                // set mosaic element (color and element)
                mosaic.setElement(colorRow, colorColumn,
                        currentElement.getName(), true);
                totalCosts = totalCosts + currentElement.getCosts();
                elements++;
            } else {
                // Enumeration (sorted elements)
                sorted = elementsSorted.elements();
                // run through sorted elements
                elementSet = false;

                while (sorted.hasMoreElements() && !elementSet) {
                    currentElement = (ElementObject) sorted.nextElement();
                    // compute random coordinates for the element
                    elementCoords = calculation.randomCoordinates(
                            currentElement.getWidth(),
                            currentElement.getHeight());
                    final Enumeration<Integer> elementCoordsEnum = elementCoords
                            .elements();

                    while (elementCoordsEnum.hasMoreElements() && !elementSet) {
                        // the coordinates of the element (to place the
                        // element) are also random
                        // testing if the element fits!
                        top = (Integer) elementCoordsEnum.nextElement();
                        left = (Integer) elementCoordsEnum.nextElement();

                        // only place the element if it covers the current
                        // pixel!
                        if (currentElement.getMatrix()[top][left]) {
                            // test mosaic borders
                            // bottom
                            if (((colorRow + currentElement.getHeight()
                                    - (top + 1)) < mosaicHeight)
                                    // top
                                    && (colorRow - top >= 0)
                                    // left
                                    && ((colorColumn - left) >= 0)
                                    // right
                                    && ((colorColumn + (currentElement
                                            .getWidth()
                                            - (left + 1))) < mosaicWidth)) {
                                // colormatching with true values of the
                                // element
                                elementSet = matchColorsWithTrueValuesOfElement(
                                        mosaic, colorRow, colorColumn,
                                        currentColor, currentElement, left,
                                        top);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean matchColorsWithTrueValuesOfElement(final Mosaic mosaic,
            final int colorRow, final int colorColumn,
            final String currentColor, final ElementObject currentElement,
            final int left, final int top) {
        boolean elementSet = false;
        Vector<String> pixel2;
        int elementLeft;
        int elementTop;
        boolean elementFits = true;
        elementLeft = -1;
        elementTop = -1;

        for (int elementRow = 0; elementRow < currentElement
                .getHeight(); elementRow++) {
            for (int elementColumn = 0; elementColumn < currentElement
                    .getWidth(); elementColumn++) {
                // only test if "true" in the
                // element
                if (currentElement.getMatrix()[elementRow][elementColumn]) {
                    // remember leftmost element in
                    // top row
                    if (elementLeft == -1) {
                        elementLeft = elementColumn;
                    }

                    if (elementTop == -1) {
                        elementTop = elementRow;
                    }

                    pixel2 = mosaic.getMosaic()
                            .get((colorRow + elementRow) - top)
                            .get((colorColumn + elementColumn) - left);

                    if (!pixel2.isEmpty()) {
                        if (!((String) (pixel2.get(0))).equals(currentColor)) {
                            elementFits = false;
                        }
                    } else {
                        elementFits = false;
                    }
                }
            }
        }

        if (elementFits) {
            // Set element
            elementSet = true;

            for (int elementRow = 0; elementRow < currentElement
                    .getHeight(); elementRow++) {
                for (int elementColumn = 0; elementColumn < currentElement
                        .getWidth(); elementColumn++) {
                    // only set if "true" in the
                    // element
                    if (currentElement.getMatrix()[elementRow][elementColumn]) {
                        mosaic.initVector(((colorRow + elementRow) - top),
                                ((colorColumn + elementColumn) - left));
                    }
                }
            }

            mosaic.setElement(colorRow - top + elementTop,
                    colorColumn - left + elementLeft, currentColor, false);
            mosaic.setElement(colorRow - top + elementTop,
                    colorColumn - left + elementLeft, currentElement.getName(),
                    true);
            totalCosts = totalCosts + currentElement.getCosts();
            elements = elements + computeElementSize(currentElement);
        }
        return elementSet;
    }

    private boolean currentPixelIs1X1Element(final int mosaicWidth,
            final int mosaicHeight, final Mosaic mosaic, final int colorRow,
            final int colorColumn, final String currentColor) {
        return (colorRow > 0) && (colorRow < mosaicHeight - 1)
                && (colorColumn > 0) && (colorColumn < mosaicWidth - 1)
                // top: vector contains a object which is not the
                // current color
                && (((mosaic.getMosaic().get(colorRow - 1).get(colorColumn)
                        .size() == 1)
                        && !(((String) (mosaic.getMosaic().get(colorRow - 1)
                                .get(colorColumn).get(0)))
                                        .equals(currentColor)))
                        // or vector is empty or 2 objects (pixel is
                        // covered)
                        || (mosaic.getMosaic().get(colorRow - 1)
                                .get(colorColumn).size() != 1))
                // bottom:
                && (((mosaic.getMosaic().get(colorRow + 1).get(colorColumn)
                        .size() == 1)
                        && !(((String) (mosaic.getMosaic().get(colorRow + 1)
                                .get(colorColumn).get(0)))
                                        .equals(currentColor)))
                        || (mosaic.getMosaic().get(colorRow + 1)
                                .get(colorColumn).size() != 1))
                // right:
                && (((mosaic.getMosaic().get(colorRow).get(colorColumn + 1)
                        .size() == 1)
                        && !(((String) (mosaic.getMosaic().get(colorRow)
                                .get(colorColumn + 1).get(0)))
                                        .equals(currentColor)))
                        || (mosaic.getMosaic().get(colorRow)
                                .get(colorColumn + 1).size() != 1))
                // left:
                && (((mosaic.getMosaic().get(colorRow).get(colorColumn - 1)
                        .size() == 1)
                        && !(((String) (mosaic.getMosaic().get(colorRow)
                                .get(colorColumn - 1).get(0)))
                                        .equals(currentColor)))
                        || (mosaic.getMosaic().get(colorRow)
                                .get(colorColumn - 1).size() != 1));
    }

    private void finishProgressBar() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    dataProcessing.refreshProgressBarAlgorithm(
                            ONE_HUNDRED_PERCENT, 2);
                }
            });
        } catch (final Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Sort the element vector.
     *
     * @author Tobias Reichling
     * @param elementsUnsorted
     * @return elementsSorted
     */
    private Vector<ElementObject> sortElementsByCosts(
            final Enumeration<ElementObject> elementsUnsorted) {
        ElementObject supportElement;
        final Vector<ElementObject> elementsSorted = new Vector<>();

        // Sort elements by cost
        while (elementsUnsorted.hasMoreElements()) {
            supportElement = ((ElementObject) elementsUnsorted.nextElement());

            if (elementsSorted.size() == 0) {
                elementsSorted.add(supportElement);
            } else {
                final boolean included = loopThroughUnincludedSortedElements(
                        supportElement, elementsSorted);

                if (!included) {
                    elementsSorted.add(supportElement);
                }
            }
        }

        return elementsSorted;
    }

    /**
     * Loop through unincluded sorted elements.
     *
     * @param supportElement the current unsorted (by cost) element.
     * @param elementsSorted the sorted elements.
     * @return <code>true</code> if supportElement is added to elementsSorted.
     */
    private boolean loopThroughUnincludedSortedElements(
            final ElementObject supportElement,
            final Vector<ElementObject> elementsSorted) {
        final double costs = (double) supportElement.getCosts()
                / (double) computeElementSize(supportElement);
        boolean included = false;
        int position = 0;
        final Enumeration<ElementObject> supportEnum = elementsSorted
                .elements();

        while (supportEnum.hasMoreElements() && !included) {
            final ElementObject anotherElement = (ElementObject) supportEnum
                    .nextElement();
            final double costs2 = (double) anotherElement.getCosts()
                    / (double) computeElementSize(anotherElement);

            if (costs < costs2) {
                elementsSorted.add(position, supportElement);
                included = true;
            } else {
                position++;
            }
        }

        return included;
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
}
