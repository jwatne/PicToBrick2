package pictobrick.service;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import pictobrick.model.ElementObject;
import pictobrick.model.Mosaic;

/**
 * Container of attributes related to pixels accessed during stability
 * optimization. Code moved from StabilityOptimizer by John Watne 9/2023.
 */
public class PixelStatus {
    /** Pixels being processed in the current row. */
    private Vector<String> pixel;
    /** The current color name. */
    private String currentColor;
    /** Sorted Enumeration of ElementObjects. */
    private Enumeration<ElementObject> sorted;
    /** Indicates whether element is set for the current pixel. */
    private boolean elementSet;
    /** ElementObject triggering elementSet. */
    private ElementObject elFlag;
    /** Points flag. */
    private int pointsFlag;
    /** Distance flag. */
    private int distanceFlag;
    /** Points for criteria 3 and 4. */
    private int points = -1;
    /** Current ElementObject. */
    private ElementObject currentElement;
    /** End position of current element. */
    private int elementsEnd = 0;
    /** Left position of current element. */
    private int left = 0;
    /** Right position of current element. */
    private int right = 0;

    /**
     * Constructor.
     *
     * @param pixels            pixels being processed in the current row.
     * @param color             the current color name.
     * @param sortedObjects     sorted Enumeration of ElementObjects.
     * @param setElement        <code>true</code> if element is set for the
     *                          current pixel.
     * @param elementObjectFlag ElementObject triggering elementSet.
     * @param flagPoints        points flag.
     * @param flagDistance      distance flag.
     */
    public PixelStatus(final Vector<String> pixels, final String color,
            final Enumeration<ElementObject> sortedObjects,
            final boolean setElement, final ElementObject elementObjectFlag,
            final int flagPoints, final int flagDistance) {
        this.pixel = pixels;
        // Determine the pixelcolor
        this.currentColor = color;
        // Enumeration (sorted elements)
        this.sorted = sortedObjects;
        // Break condition for while
        this.elementSet = setElement;
        // Flag variables
        this.elFlag = elementObjectFlag;
        this.pointsFlag = flagPoints;
        this.distanceFlag = flagDistance;
    }

    /**
     * Returns pixels being processed in the current row.
     *
     * @return pixels being processed in the current row.
     */
    public Vector<String> getPixel() {
        return pixel;
    }

    /**
     * Sets pixels being processed in the current row.
     *
     * @param pixels pixels being processed in the current row.
     */
    public void setPixel(final Vector<String> pixels) {
        this.pixel = pixels;
    }

    /**
     * Returns the current color name.
     *
     * @return the current color name.
     */
    public String getCurrentColor() {
        return currentColor;
    }

    /**
     * Sets the current color name.
     *
     * @param color the current color name.
     */
    public void setCurrentColor(final String color) {
        this.currentColor = color;
    }

    /**
     * Returns sorted Enumeration of ElementObjects.
     *
     * @return sorted Enumeration of ElementObjects.
     */
    public Enumeration<ElementObject> getSorted() {
        return sorted;
    }

    /**
     * Sets sorted Enumeration of ElementObjects.
     *
     * @param sortedObjects sorted Enumeration of ElementObjects.
     */
    public void setSorted(final Enumeration<ElementObject> sortedObjects) {
        this.sorted = sortedObjects;
    }

    /**
     * Indicates whether element is set for the current pixel.
     *
     * @return <code>true</code> if element is set for the current pixel.
     */
    public boolean isElementSet() {
        return elementSet;
    }

    /**
     * Sets whether element is set for the current pixel.
     *
     * @param setElement <code>true</code> if element is set for the current
     *                   pixel.
     */
    public void setElementSet(final boolean setElement) {
        this.elementSet = setElement;
    }

    /**
     * Returns ElementObject triggering elementSet.
     *
     * @return ElementObject triggering elementSet.
     */
    public ElementObject getElFlag() {
        return elFlag;
    }

    /**
     * Sets ElementObject triggering elementSet.
     *
     * @param elementObjectFlag ElementObject triggering elementSet.
     */
    public void setElFlag(final ElementObject elementObjectFlag) {
        this.elFlag = elementObjectFlag;
    }

    /**
     * Returns points flag.
     *
     * @return points flag.
     */
    public int getPointsFlag() {
        return pointsFlag;
    }

    /**
     * Sets points flag.
     *
     * @param flagPoints points flag.
     */
    public void setPointsFlag(final int flagPoints) {
        this.pointsFlag = flagPoints;
    }

    /**
     * Returns distance flag.
     *
     * @return distance flag.
     */
    public int getDistanceFlag() {
        return distanceFlag;
    }

    /**
     * Sets distance flag.
     *
     * @param flagDistance distance flag.
     */
    public void setDistanceFlag(final int flagDistance) {
        this.distanceFlag = flagDistance;
    }

    /**
     * Returns current ElementObject.
     *
     * @return current ElementObject.
     */
    public ElementObject getCurrentElement() {
        return currentElement;
    }

    /**
     * Sets current ElementObject.
     *
     * @param element current ElementObject.
     */
    public void setCurrentElement(final ElementObject element) {
        this.currentElement = element;
    }

    /**
     * Sets elementSet to <code>true</code> and elFlag to the currentElement.
     */
    public void setElementSetAndElFlag() {
        this.setElementSet(true);
        this.setElFlag(this.getCurrentElement());
    }

    /**
     * Returns Points for criteria 3 and 4.
     *
     * @return Points for criteria 3 and 4.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets Points for criteria 3 and 4.
     *
     * @param criteriaPoints Points for criteria 3 and 4.
     */
    public void setPoints(final int criteriaPoints) {
        this.points = criteriaPoints;
    }

    /**
     * Returns End position of current element.
     *
     * @return End position of current element.
     */
    public int getElementsEnd() {
        return elementsEnd;
    }

    /**
     * Sets End position of current element.
     *
     * @param end End position of current element.
     */
    public void setElementsEnd(final int end) {
        this.elementsEnd = end;
    }

    /**
     * Returns Left position of current element.
     *
     * @return Left position of current element.
     */
    public int getLeft() {
        return left;
    }

    /**
     * Sets Left position of current element.
     *
     * @param position Left position of current element.
     */
    public void setLeft(final int position) {
        this.left = position;
    }

    /**
     * Returns Right position of current element.
     *
     * @return Right position of current element.
     */
    public int getRight() {
        return right;
    }

    /**
     * Sets Right position of current element.
     *
     * @param position Right position of current element.
     */
    public void setRight(final int position) {
        this.right = position;
    }

    /**
     * Perform StabilityOptimizer check 5: Element end is as centered as
     * possible between 2 gaps of the row above (only check if the element end
     * is not below a gap).
     *
     * @param mosaicWidth the width of the mosaic in elements.
     * @param colorCol    the current colomn being processed.
     * @param borders     borders.
     * @param colorRow    the row currently being processed.
     */
    public void doCheck5(final int mosaicWidth, final int colorCol,
            final boolean[][] borders, final int colorRow) {
        setPoints(points + 2);
        // doesn't end on a gap
        // -------------------------------
        // CHECK 5) element end is as
        // centered as posible between 2
        // gaps of the row above (only check
        // if the element end is not below a
        // gap)
        setElementsEnd(colorCol + currentElement.getWidth());
        // find next gap (left above)
        setLeft(findNextGapLeftAbove(colorCol, borders, colorRow));
        // find next gap (right above)
        setRight(findNextGapRightAbove(mosaicWidth, colorCol, borders,
                colorRow));
    }

    /**
     * Process for element with height 1.
     *
     * @param mosaic   the Mosaic being created.
     * @param hash     Hashtable for randomization of tile selection.
     * @param colorCol the column currently being processed.
     * @param colorRow the row currently being processed.
     * @param borders  borders for the elements in the Mosaic.
     */
    public void processForHeight1(final Mosaic mosaic,
            final Hashtable<String, String> hash, final int colorCol,
            final int colorRow, final boolean[][] borders) {
        if (mustSetElementAndSetCoveredPixelsNull(mosaic, hash, colorCol,
                colorRow)) {
            // set element and set all covered pixels to
            // "null"
            setElementandSetCoveredPixelsNull(mosaic, hash, colorCol, colorRow,
                    borders);
        } else {
            // set element and set all covered pixels to
            // "null"
            setElementAndNullAllCoveredPixels(mosaic, colorCol, colorRow,
                    borders);
        }
    }

    /**
     * Process for element with height 3.
     *
     * @param mosaic   the Mosaic being created.
     * @param colorCol the column currently being processed.
     * @param colorRow the row currently being processed.
     * @param borders  borders for the elements in the Mosaic.
     */
    public void processForHeight3(final Mosaic mosaic, final int colorCol,
            final int colorRow, final boolean[][] borders) {
        for (int elRow = 0; elRow < StabilityOptimizer.CUTOFF_HEIGHT; elRow++) {
            for (int elementColumn = 0; elementColumn < elFlag
                    .getWidth(); elementColumn++) {
                mosaic.initVector(colorRow + elRow, colorCol + elementColumn);
            }
        }

        mosaic.setElement(colorRow, colorCol, currentColor, false);
        mosaic.setElement(colorRow, colorCol, elFlag.getName(), true);
        borders[colorRow + 2][colorCol] = true;
        borders[colorRow + 1][colorCol] = true;
        borders[colorRow][colorCol] = true;
    }

    /**
     * Sets element if it ends at the end of the color.
     *
     * @param mosaic   the Mosaic being generated.
     * @param colorCol the column currently being processed.
     * @param colorRow the row currently being processed.
     */
    public void setElementIfEndsAtColorEnd(final Mosaic mosaic,
            final int colorCol, final int colorRow) {
        // set element if it ends at the end of
        // the color
        final Vector<String> pixel2 = getPixel2(mosaic, colorCol, colorRow);

        if (!pixel2.isEmpty()) {
            if (atEndOfColor(pixel2)) {
                setElementSetAndElFlag();
            }
        } else {
            checkFor3HeightElementInRowAbove(mosaic, colorCol, colorRow);
        }
    }

    /**
     * Check StabilityOptimizer criteria 4: element covers a Gap - points 1; and
     * 5: elementend is as centered as posible between 2 gaps of the row above.
     *
     * @param mosaicWidth width of the Mosaic.
     * @param colorCol    the column being processed.
     * @param colorRow    the row being processed.
     * @param borders     borders.
     */
    public void checkCriteria4And5(final int mosaicWidth, final int colorCol,
            final int colorRow, final boolean[][] borders) {
        addPointIfCoversGapCheck4(mosaicWidth, colorCol, borders, colorRow);
        // All points for criterias 3) and 4)
        // are set
        // elements with the same points are
        // ordered by criteria 5)
        orderByCriteria5();
    }

    private int findNextGapRightAbove(final int mosaicWidth, final int colorCol,
            final boolean[][] borders, final int colorRow) {
        boolean gap;
        int counter;
        int updatedRightValue;
        gap = false;
        counter = 1;
        updatedRightValue = mosaicWidth;

        while (!gap) {
            if (colorCol + currentElement.getWidth() + counter == mosaicWidth) {
                updatedRightValue = mosaicWidth;
                gap = true;
            } else if (borders[colorRow - 1][colorCol
                    + currentElement.getWidth() + counter]) {
                updatedRightValue = colorCol + currentElement.getWidth()
                        + counter;
                gap = true;
            }

            counter++;
        }

        return updatedRightValue;
    }

    private int findNextGapLeftAbove(final int colorCol,
            final boolean[][] borders, final int colorRow) {
        boolean gap;
        int counter;
        int updatedLeftValue;
        gap = false;
        counter = 1;
        updatedLeftValue = 0;

        while (!gap) {
            if (colorCol + currentElement.getWidth() - counter == 0) {
                updatedLeftValue = 0;
                gap = true;
            } else if (borders[colorRow - 1][colorCol
                    + currentElement.getWidth() - counter]) {
                updatedLeftValue = colorCol + currentElement.getWidth()
                        - counter;
                gap = true;
            }

            counter++;
        }

        return updatedLeftValue;
    }

    /**
     * Order tiles by StabilityOptimizer criteria 5: elementend is as centered
     * as posible between 2 gaps of the row above.
     */
    private void orderByCriteria5() {
        final int diffDistance = diffDistance();

        if (points == pointsFlag) {
            if (diffDistance < distanceFlag) {
                setDistanceFlag(diffDistance);
                setPointsFlag(points);
                setElFlag(currentElement);
            }
        } else if (points > pointsFlag) {
            setPointsFlag(points);
            setElFlag(currentElement);
            setDistanceFlag(diffDistance);
        }
    }

    /**
     * Add a point for check 4 if element covers a gap.
     *
     * @param mosaicWidth the width of the Mosaic.
     * @param colorCol    the column currently being processed.
     * @param borders     borders.
     * @param colorRow    the row currently being processed.
     */
    private void addPointIfCoversGapCheck4(final int mosaicWidth,
            final int colorCol, final boolean[][] borders, final int colorRow) {
        boolean covered = false;

        for (int x = 0; x < (currentElement.getWidth() - 1); x++) {
            if (colorCol + x + 1 < mosaicWidth) {
                if (borders[colorRow - 1][colorCol + 1 + x]) {
                    covered = true;
                }
            }
        }

        if (covered) {
            points += 1; // element covers gap(s)
        }
    }

    private boolean mustSetElementAndSetCoveredPixelsNull(final Mosaic mosaic,
            final Hashtable<String, String> hash, final int colorCol,
            final int colorRow) {
        return (hash.get(elFlag.getName()) != null) && (colorRow > 1)
                && ((mosaic.getMosaic().get(colorRow - 1).get(colorCol)
                        .size() != 0)
                        && (((String) (mosaic.getMosaic().get(colorRow - 1)
                                .get(colorCol).get(1))).equals(currentColor))
                        && (((String) (mosaic.getMosaic().get(colorRow - 1)
                                .get(colorCol).get(0)))
                                        .equals(elFlag.getName())))
                && ((mosaic.getMosaic().get(colorRow - 2).get(colorCol)
                        .size() != 0)
                        && (((String) (mosaic.getMosaic().get(colorRow - 2)
                                .get(colorCol).get(1))).equals(currentColor))
                        && (((String) (mosaic.getMosaic().get(colorRow - 2)
                                .get(colorCol).get(0)))
                                        .equals(elFlag.getName())));
    }

    private void setElementAndNullAllCoveredPixels(final Mosaic mosaic,
            final int colorCol, final int colorRow, final boolean[][] borders) {
        for (int elementRow = 0; elementRow < elFlag
                .getHeight(); elementRow++) {
            for (int elCol = 0; elCol < elFlag.getWidth(); elCol++) {
                mosaic.initVector(colorRow + elementRow, colorCol + elCol);
            }
        }

        mosaic.setElement(colorRow, colorCol, currentColor, false);
        mosaic.setElement(colorRow, colorCol, elFlag.getName(), true);
        borders[colorRow][colorCol] = true;
    }

    private void setElementandSetCoveredPixelsNull(final Mosaic mosaic,
            final Hashtable<String, String> hash, final int colorCol,
            final int colorRow, final boolean[][] borders) {
        for (int elRow = 0; elRow < StabilityOptimizer.CUTOFF_HEIGHT; elRow++) {
            for (int elementColumn = 0; elementColumn < elFlag
                    .getWidth(); elementColumn++) {
                mosaic.initVector(colorRow - 2 + elRow,
                        colorCol + elementColumn);
            }
        }

        mosaic.setElement(colorRow - 2, colorCol, currentColor, false);
        mosaic.setElement(colorRow - 2, colorCol,
                (String) hash.get(elFlag.getName()), true);
        borders[colorRow - 2][colorCol] = true;
        borders[colorRow - 1][colorCol] = true;
        borders[colorRow][colorCol] = true;
    }

    /**
     * Returns the difference (absolute value) between
     * <ol>
     * <li>the distance betwen elementEnd and left, and</li>
     * <li>the distance between elementEnd and right.</li>
     * </ol>
     *
     * @return the difference between the two distances.
     */
    private int diffDistance() {
        return Math.abs(
                Math.abs(elementsEnd - right) - Math.abs(elementsEnd - left));
    }

    private void checkFor3HeightElementInRowAbove(final Mosaic mosaic,
            final int colorCol, final int colorRow) {
        Vector<String> pixel2;
        // check if there is a element with
        // height=3 in the row above
        pixel2 = getPixel2(mosaic, colorCol, colorRow - 1);

        if (!pixel2.isEmpty()) {
            if (atEndOfColor(pixel2)) {
                setElementSetAndElFlag();
            }
        } else if (colorRow > 1) {
            // check if there is an element
            // with height=3 (2 rows above)
            pixel2 = getPixel2(mosaic, colorCol, colorRow - 2);

            if (!pixel2.isEmpty()) {
                if (atEndOfColor(pixel2)) {
                    setElementSetAndElFlag();
                }
            }
        }
    }

    private boolean atEndOfColor(final Vector<String> pixel2) {
        return !(currentColor.equals((String) (pixel2.get(0))));
    }

    private Vector<String> getPixel2(final Mosaic mosaic, final int colorCol,
            final int rowIndex) {
        return mosaic.getMosaic().get(rowIndex)
                .get(colorCol + currentElement.getWidth());
    }

}
