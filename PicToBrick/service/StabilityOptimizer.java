package pictobrick.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.Configuration;
import pictobrick.model.ElementObject;
import pictobrick.model.Lab;
import pictobrick.model.Mosaic;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Tiling with stability optimisation.
 *
 * @author Adrian Schuetz
 */
public class StabilityOptimizer implements Tiler {
    /** Cutoff height. */
    public static final int CUTOFF_HEIGHT = 3;
    /** Height of a LEGO brick (vs. 1 for a tile). */
    private static final int BRICK_HEIGHT = 3;
    /** Int value 4. */
    private static final int INT4 = 4;
    /** First threshold value. */
    private static final int THRESHOLD1 = 50;
    /** Second threshold value. */
    private static final int THRESHOLD2 = 100;
    /** Bottom border height. */
    private static final int BOTTOM_BORDER = 5;

    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private final Calculator calculation;
    /** Percent of rows calculated (x 100%). */
    private int percent = 0;
    /** Number of rows in image. */
    private int rows = 0;
    /** number of row currently being colored. */
    private int colorRow;
    /** Borders. */
    private boolean[][] borders;
    /** <code>true</code> if output statistics. */
    private boolean statisticOutput;
    /** Tiling information. */
    private final Vector<Object> tilingInfo;
    /** Quantization algorithm number. */
    private int quantisationAlgo;
    /** <code>true</code> if optimizing. */
    private boolean optimisation = false;
    /** <code>true</code> if only border handling. */
    private boolean onlyBorderHandling = true;
    /** Maximum gap height. */
    private int maximumGapHeight = 0;
    /** Number of colors. */
    private int colorCount = 0;
    /** LAB colors. */
    private final Vector<Object> labColors = new Vector<>();
    /** Doubled elements? */
    private ElementObject doubleElement;
    /** Number of recolored elements. */
    private int recoloredElements = 0;

    /**
     * Contructor.
     *
     * @author Adrian Schuetz
     * @param processor         dataProcessing
     * @param calculator        calculation
     * @param tilingInformation
     */
    public StabilityOptimizer(final DataProcessor processor,
            final Calculator calculator,
            final Vector<Object> tilingInformation) {
        this.dataProcessing = processor;
        this.calculation = calculator;
        this.tilingInfo = tilingInformation;
    }

    /**
     * Tiling with stability optimisation.
     *
     * @author Adrian Schuetz
     * @param mosaicWidth
     * @param mosaicHeight
     * @param configuration
     * @param statistic
     * @param mosaic
     * @return mosaic
     */
    public Mosaic tiling(final int mosaicWidth, final int mosaicHeight,
            final Configuration configuration, final Mosaic mosaic,
            final boolean statistic) {
        this.statisticOutput = statistic;
        this.rows = mosaicHeight;
        final Enumeration<Object> tilingInfoEnum = tilingInfo.elements();
        quantisationAlgo = (Integer) tilingInfoEnum.nextElement();
        setColorCount();
        // Values from the stability optimisation dialog
        this.optimisation = (Boolean) tilingInfoEnum.nextElement();
        this.onlyBorderHandling = (Boolean) tilingInfoEnum.nextElement();
        this.maximumGapHeight = (Integer) tilingInfoEnum.nextElement();
        final String[] colors = new String[colorCount]; // Array for the colors
        final int[] threshold = new int[colorCount + 1]; // Array for the
                                                         // threshold of the
                                                         // colors

        for (int i = 0; i < colorCount; i++) {
            colors[i] = "";
            threshold[i] = -1;
        }

        initializeColorVector(configuration);
        initializeThresholdAndColorArrays(tilingInfoEnum, colors, threshold);
        final int[][][] originalMatrix = getOriginalMatrix(mosaicWidth,
                mosaicHeight);
        initializeBordersArray(mosaicWidth, mosaicHeight);
        // Create vector with elements (sorted by stability)
        final Vector<ElementObject> elementsSorted = sortElementsByStability(
                configuration.getAllElements());

        // Consistency check
        if (!consistencyCheck(configuration) || (optimisation
                && !consistencyCheckOptimisation(configuration))) {
            // ------------ALTERNATIVE-ALGORITHM------------
            return getResultForFailedConsistencyChecks(mosaicWidth,
                    mosaicHeight, configuration, mosaic);
        } else {
            // ------------MAIN-ALGORITHM------------
            // run linear through the image - determine the fitting element for
            // every pixel
            final Hashtable<String, String> hash = hashInit(configuration);
            // int points = -1;
            boolean elementFits = false;
            // ElementObject currentElement;
            // int elementsEnd = 0;
            // int left = 0;
            // int right = 0;

            // run linear through the image
            // decide which element is placed by 5 criteria
            // 1) element fits
            // 2) element ends at the end of the row or color
            // 3) elementend is not below a gap - points 2
            // 4) element covers a Gap - points 1
            // 5) elementend is as centered as posible between 2 gaps of the row
            // above
            for (colorRow = 0; colorRow < mosaicHeight; colorRow++) {
                // assign progress bar controls to the GUI thread
                percent = (int) ((Calculator.ONE_HUNDRED_PERCENT / rows)
                        * colorRow);
                updateProgressBarValues(percent);

                for (int colorCol = 0; colorCol < mosaicWidth; colorCol++) {
                    // Color information of the current pixel
                    final Vector<String> pixel = mosaic.getMosaic()
                            .get(colorRow).get(colorCol);

                    // if the pixel is not covered
                    if (!pixel.isEmpty()) {
                        final var pStatus = new PixelStatus(pixel,
                                (String) (pixel.get(0)),
                                elementsSorted.elements(), false, null, -1,
                                THRESHOLD2);

                        // run through elementvector with all elements (sorted
                        // by stability)
                        final Enumeration<ElementObject> sorted = pStatus
                                .getSorted();

                        while (sorted.hasMoreElements()
                                && !pStatus.isElementSet()) {
                            // get next element
                            pStatus.setCurrentElement(
                                    (ElementObject) sorted.nextElement());
                            // Counter for the criterias 3) and 4)
                            pStatus.setPoints(0);

                            // CHECK 1) element fits
                            elementFits = checkIfElementFits(mosaicWidth,
                                    mosaicHeight, mosaic, pStatus, colorCol);
                            // END of optimisation

                            // Only continue if the element fits
                            continueIfElementFits(mosaicWidth, mosaic,
                                    elementFits, colorCol, pStatus);
                        } // END: while (sorted.hasMoreElements() &&
                          // !elementSet)

                        // ------------------------------------
                        // optimisation:
                        // if a basis element should be placed but creates a
                        // high gap:
                        // a double element (width=2, height=1) is placed
                        // in addition a mixed color is determined
                        if (basisElementShouldBePlacedButCreatesHighGap(
                                mosaicWidth, mosaicHeight, pStatus.getElFlag(),
                                colorCol)) {
                            placeDoubleElementWithMixedColor(colors, threshold,
                                    originalMatrix, colorCol, pStatus);
                        }
                        // END optimisation

                        // -----------------------------------------------------
                        // Check if the same element is already in the 2 rows
                        // above
                        // -> replace the three height=1 elements by one
                        // height=3 element
                        checkIfSameElementAlreadyIn2RowsAbove(mosaic, hash,
                                pStatus, colorCol);
                    } // END: if (!pixel.isEmpty())
                      // ---------------------------------------------
                }
            } // END: Double For

            // assign progress bar controls to the GUI thread
            updateProgressBarValues(THRESHOLD2);
            // -----------------------------------------------------
            // optimisation: statistic output for gaps
            doStatisticOutputForGaps(mosaicWidth, mosaicHeight, statistic);
            return mosaic;
        }
    }

    private void continueIfElementFits(final int mosaicWidth,
            final Mosaic mosaic, final boolean elementFits, final int colorCol,
            final PixelStatus pStatus) {
        if (elementFits) {
            // The criterias 3) and 4) can not be checked in
            // the first row
            // -> set element with best stability
            if (colorRow == 0) {
                // set element
                pStatus.setElementSetAndElFlag();
            } else {
                // All other rows in the mosaic
                // --------------------------------------
                // CHECK 2) element ends at the end of the
                // row or color
                if (colorCol + pStatus.getCurrentElement()
                        .getWidth() == mosaicWidth) {
                    pStatus.setElementSetAndElFlag();
                } else {
                    setElementIfEndsAtColorEnd(mosaic, colorCol, pStatus);
                } // END: if
                  // (colorColumn+currentElement.getWidth()
                  // == mosaicWidth)

                // ----------------------------------
                // CHECK 3) elementend is not below a gap
                // - points 2
                final ElementObject currentElement = pStatus
                        .getCurrentElement();

                if ((colorCol + currentElement.getWidth() < mosaicWidth)
                        && !pStatus.isElementSet()) {
                    if (!borders[colorRow - 1][colorCol
                            + currentElement.getWidth()]) {
                        pStatus.doCheck5(mosaicWidth, colorCol, borders,
                                colorRow);
                    }
                } // END: if
                  // (mosaic.getMosaic()[colorRow-1]
                  // [colorColumn+currentElement.getWidth()]
                  // .size()==0)

                if (!pStatus.isElementSet()) {
                    checkCriteria4And5(mosaicWidth, colorCol, pStatus);
                }
                // ---------------------------------
            } // END: if (colorRow==0)
        } // END: if (elementFits)
    }

    private void checkCriteria4And5(final int mosaicWidth, final int colorCol,
            final PixelStatus pStatus) {
        final int points = addPointIfCoversGapCheck4(mosaicWidth, pStatus,
                colorCol);
        pStatus.setPoints(points);

        // All points for criterias 3) and 4)
        // are set
        // elements with the same points are
        // ordered by criteria 5)
        pStatus.orderByCriteria5();
    }

    /**
     * Places a double element (width = 2, height = 1) and determines a mixed
     * color.
     *
     * @param colors         Array for the colors.
     * @param threshold      Array for the threshold of the colors.
     * @param originalMatrix the original image (to determine the original
     *                       colors later).
     * @param colorCol       Column currently being processed.
     * @param pStatus        Container of attributes related to pixels accessed
     *                       during stability optimization.
     */
    private void placeDoubleElementWithMixedColor(final String[] colors,
            final int[] threshold, final int[][][] originalMatrix,
            final int colorCol, final PixelStatus pStatus) {
        pStatus.setElFlag(this.doubleElement);

        // Algorithm without error distribution.
        pStatus.setCurrentColor(getCurrColorNoErrorDistribution(colors,
                threshold, originalMatrix, colorCol));
        // Count recolored pixel for statistic output
        this.recoloredElements++;
    }

    private void setElementIfEndsAtColorEnd(final Mosaic mosaic,
            final int colorCol, final PixelStatus pStatus) {
        final ElementObject currentElement = pStatus.getCurrentElement();
        Vector<String> pixel2;
        // set element if it ends at the end of
        // the color
        pixel2 = getPixel2(mosaic, currentElement, colorCol, colorRow);

        if (!pixel2.isEmpty()) {
            if (atEndOfColor(pStatus.getCurrentColor(), pixel2)) {
                pStatus.setElementSetAndElFlag();
            }
        } else {
            checkFor3HeightElementInRowAbove(mosaic, colorCol, pStatus);
        }
    }

    private void checkFor3HeightElementInRowAbove(final Mosaic mosaic,
            final int colorCol, final PixelStatus pStatus) {
        final ElementObject currentElement = pStatus.getCurrentElement();
        Vector<String> pixel2;
        // check if there is a element with
        // height=3 in the row above
        pixel2 = getPixel2(mosaic, currentElement, colorCol, colorRow - 1);

        if (!pixel2.isEmpty()) {
            if (atEndOfColor(pStatus.getCurrentColor(), pixel2)) {
                pStatus.setElementSetAndElFlag();
            }
        } else if (colorRow > 1) {
            // check if there is an element
            // with height=3 (2 rows above)
            pixel2 = getPixel2(mosaic, currentElement, colorCol, colorRow - 2);

            if (!pixel2.isEmpty()) {
                if (atEndOfColor(pStatus.getCurrentColor(), pixel2)) {
                    pStatus.setElementSetAndElFlag();
                }
            }
        }
    }

    private void checkIfSameElementAlreadyIn2RowsAbove(final Mosaic mosaic,
            final Hashtable<String, String> hash, final PixelStatus pStatus,
            final int colorCol) {
        final ElementObject elFlag = pStatus.getElFlag();

        if (elFlag.getHeight() == 1) {
            pStatus.processForHeight1(mosaic, hash, colorCol, colorRow,
                    borders);
        } else { // height =3
            pStatus.processForHeight3(mosaic, colorCol, colorRow, borders);
        }
    }

    private int addPointIfCoversGapCheck4(final int mosaicWidth,
            final PixelStatus pStatus,
            // final int initialPoints, final ElementObject currentElement,
            final int colorCol) {
        int points = pStatus.getPoints();
        final ElementObject currentElement = pStatus.getCurrentElement();
        boolean covered = false;
        // int points = initialPoints;

        for (int x = 0; x < (currentElement.getWidth() - 1); x++) {
            if (colorCol + x + 1 < mosaicWidth) {
                if (borders[colorRow - 1][colorCol + 1 + x]) {
                    covered = true;
                }
            }
        }

        if (covered) {
            points = points + 1; // element
                                 // covers
                                 // gap(s)
        }

        return points;
    }

    private boolean checkIfElementFits(final int mosaicWidth,
            final int mosaicHeight, final Mosaic mosaic,
            final PixelStatus pStatus, final int colorCol) {
        final String currentColor = pStatus.getCurrentColor();
        final ElementObject currentElement = pStatus.getCurrentElement();
        boolean elementFits;
        elementFits = testBottomAndRightBorders(mosaicWidth, mosaicHeight,
                mosaic, currentColor, currentElement, colorCol);

        // optimisation: only continue if the extended
        // requirements are fulfilled
        // check elements with height = 3
        // if element ends at the end of the row:
        // test high gaps at the left side of the element
        elementFits = meetsExtendedRequirements(mosaicWidth, elementFits,
                currentElement, colorCol);

        // check elements in the mosaic
        if (elementFits && optimisation
                && ((colorCol + currentElement.getWidth()) < mosaicWidth)) {
            elementFits = noElementsHigherThan1ByBorder(mosaicHeight,
                    elementFits, currentElement);

            if (colorRow == 1 || colorRow == 2 || colorRow == mosaicHeight - 1
                    || colorRow == mosaicHeight - 2) {
                // if the element creates a high gap (left
                // or right side) the element is not
                // fitting
                if ((colorCol > 0 && bigGap(colorRow, colorCol, 1)) || bigGap(
                        colorRow, colorCol + currentElement.getWidth(), 1)) {
                    elementFits = false;
                }
            } else if (!onlyBorderHandling) {
                // Rest of the image. If the element creates
                // a high gap (left or right side) the
                // element is not fitting.
                elementFits = doesNotCreateHightGap(elementFits, currentElement,
                        colorCol);
            }

            // bais elements must be possible
            if (currentElement.getHeight() == 1
                    && currentElement.getWidth() == 1) {
                elementFits = true;
            }
        }

        // remember element with height=1 and width=2
        if (currentElement.getWidth() == 2 && currentElement.getHeight() == 1) {
            this.doubleElement = currentElement;
        }
        return elementFits;
    }

    /**
     * Only border handling. Testing rows 2+3 and the last two rows: rows 1, 2,
     * third and fourth, last row: no elements higher than 1 are allowed.
     *
     * @param mosaicHeight
     * @param initialElementFits
     * @param currentElement
     * @return <code>true</code> if there are no elements higher than 1 near the
     *         borders.
     */
    private boolean noElementsHigherThan1ByBorder(final int mosaicHeight,
            final boolean initialElementFits,
            final ElementObject currentElement) {
        boolean elementFits = initialElementFits;

        if (currentElement.getHeight() == CUTOFF_HEIGHT
                && (colorRow < 2 || colorRow > mosaicHeight - BOTTOM_BORDER)) {
            elementFits = false;
        }

        return elementFits;
    }

    private boolean meetsExtendedRequirements(final int mosaicWidth,
            final boolean initialElementFits,
            final ElementObject currentElement, final int colorCol) {
        boolean elementFits = initialElementFits;

        if ((colorCol + currentElement.getWidth()) == mosaicWidth
                && currentElement.getHeight() == CUTOFF_HEIGHT && colorCol > 0
                && colorRow >= (maximumGapHeight - 1)
                        - (currentElement.getHeight() - 1)
                && bigGap(colorRow, colorCol, (maximumGapHeight - 1)
                        - (currentElement.getHeight() - 1))) {
            elementFits = false;
        }

        return elementFits;
    }

    /**
     * Test mosaic-borders (bottom and right).
     *
     * @param mosaicWidth
     * @param mosaicHeight
     * @param mosaic
     * @param currentColor
     * @param currentElement
     * @param colorCol
     * @return <code>true</code> if elementFits.
     */
    private boolean testBottomAndRightBorders(final int mosaicWidth,
            final int mosaicHeight, final Mosaic mosaic,
            final String currentColor, final ElementObject currentElement,
            final int colorCol) {
        boolean elementFits;

        if (((colorRow + currentElement.getHeight() - 1) < mosaicHeight)
                && ((colorCol + currentElement.getWidth()) <= mosaicWidth)) {
            // colormatching with true-values of the element
            // elementFits is set to true
            // if a pixel with another color is found it is
            // set to false
            elementFits = true;

            for (int elRow = 0; elRow < currentElement.getHeight(); elRow++) {
                for (int elCol = 0; elCol < currentElement
                        .getWidth(); elCol++) {
                    // Only check if the current
                    // elementcoordinate contains "true"
                    elementFits = elementFits(mosaic, currentColor, elementFits,
                            currentElement, colorCol, elRow, elCol);
                }
            }
        } else {
            elementFits = false;
        }
        return elementFits;
    }

    private boolean elementFits(final Mosaic mosaic, final String currentColor,
            final boolean initialValueElementFits,
            final ElementObject currentElement, final int colorCol,
            final int elRow, final int elCol) {
        boolean elementFits = initialValueElementFits;
        Vector<String> pixel2;

        if (currentElement.getMatrix()[elRow][elCol]) {
            pixel2 = mosaic.getMosaic().get(colorRow + elRow)
                    .get(colorCol + elCol);

            if (!pixel2.isEmpty()) {
                if (!((String) (pixel2.get(0))).equals(currentColor)) {
                    elementFits = false;
                }
            } else {
                elementFits = false;
            }
        }

        return elementFits;
    }

    private boolean basisElementShouldBePlacedButCreatesHighGap(
            final int mosaicWidth, final int mosaicHeight,
            final ElementObject elFlag, final int colorCol) {
        return optimisation && elFlag.getWidth() == 1
                && colorCol + elFlag.getWidth() < mosaicWidth
                // whole image
                && (!onlyBorderHandling
                        && (colorRow >= (maximumGapHeight - 1) && bigGap(
                                colorRow, colorCol + elFlag.getWidth(),
                                (maximumGapHeight - 1)))
                        // border handling
                        || ((colorRow == 1 || colorRow == 2
                                || colorRow == mosaicHeight - 1
                                || colorRow == mosaicHeight - 2))
                                && bigGap(colorRow,
                                        colorCol + elFlag.getWidth(), 1));
    }

    private String getCurrColorNoErrorDistribution(final String[] colors,
            final int[] threshold, final int[][][] originalMatrix,
            final int colorCol) {
        // colors
        final Lab color1 = new Lab();
        final Lab color2 = new Lab();
        String currentColor;
        // color 1: value from the original image
        getColor1FromOriginalImage(originalMatrix, colorCol, color1);
        // color 2: value from the original image
        getColor2FromOriginalImage(originalMatrix, colorCol, color2);
        // set current color to mixed color
        currentColor = getMixedColor(colors, threshold, color1, color2);
        return currentColor;
    }

    private void getColor1FromOriginalImage(final int[][][] originalMatrix,
            final int colorCol, final Lab color1) {
        int red;
        int green;
        int blue;
        red = originalMatrix[colorRow][colorCol][0];
        green = originalMatrix[colorRow][colorCol][1];
        blue = originalMatrix[colorRow][colorCol][2];
        color1.setL(calculation.rgbToLab(new Color(red, green, blue)).getL());
        color1.setA(calculation.rgbToLab(new Color(red, green, blue)).getA());
        color1.setB(calculation.rgbToLab(new Color(red, green, blue)).getB());
    }

    private void getColor2FromOriginalImage(final int[][][] originalMatrix,
            final int colorCol, final Lab color2) {
        int red;
        int green;
        int blue;
        red = originalMatrix[colorRow][colorCol + 1][0];
        green = originalMatrix[colorRow][colorCol + 1][1];
        blue = originalMatrix[colorRow][colorCol + 1][2];
        color2.setL(calculation.rgbToLab(new Color(red, green, blue)).getL());
        color2.setA(calculation.rgbToLab(new Color(red, green, blue)).getA());
        color2.setB(calculation.rgbToLab(new Color(red, green, blue)).getB());
    }

    private Mosaic getResultForFailedConsistencyChecks(final int mosaicWidth,
            final int mosaicHeight, final Configuration configuration,
            final Mosaic mosaic) {
        showErrorDialogForFailedConsistencyCheck();

        for (colorRow = 0; colorRow < mosaicHeight; colorRow++) {
            // assign progress bar controls to the GUI thread
            percent = (int) ((Calculator.ONE_HUNDRED_PERCENT / rows)
                    * colorRow);
            updateProgressBarValues(percent);

            for (int colorCol = 0; colorCol < mosaicWidth; colorCol++) {
                mosaic.setElement(colorRow, colorCol,
                        configuration.getBasisName(), true);
            }
        }

        return mosaic;
    }

    private void updateProgressBarValues(final int barValue) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {

                    if (statisticOutput) {
                        dataProcessing.refreshProgressBarAlgorithm(barValue,
                                ProgressBarsAlgorithms.STATISTICS);
                    } else {
                        dataProcessing.refreshProgressBarAlgorithm(barValue,
                                ProgressBarsAlgorithms.TILING);
                    }
                }
            });
        } catch (final Exception e) {
            System.out.println(e.toString());
        }
    }

    private void doStatisticOutputForGaps(final int mosaicWidth,
            final int mosaicHeight, final boolean statistic) {
        int highGaps = 0; // quantity of high gaps
        int gapCounter = 0; // temp gap counter
        int highestGap = 0; // highest gap
        final int[] gapFlag = new int[mosaicHeight + 1]; // Flag to count
                                                         // the different
                                                         // gap highs

        // Run through mosaic by column
        // Ignore first column (gap in column 0 is the left border)
        for (int i = 1; i < mosaicWidth; i++) {
            for (int j = 0; j < mosaicHeight; j++) {
                if (borders[j][i]) {
                    // Remember gap
                    gapCounter++;
                } else if (gapCounter > 0) {
                    if (gapCounter > maximumGapHeight) {
                        highGaps++;
                    }

                    // Remember highest gap
                    if (gapCounter > highestGap) {
                        highestGap = gapCounter;
                    }
                    // gapFlag
                    gapFlag[gapCounter] = gapFlag[gapCounter] + 1;
                    gapCounter = 0;
                }
            }

            // do not forget gap at the end!
            if (gapCounter > 0) {
                if (gapCounter > maximumGapHeight) {
                    highGaps++;
                }

                if (gapCounter > highestGap) {
                    highestGap = gapCounter;
                }

                gapFlag[gapCounter] = gapFlag[gapCounter] + 1;
                gapCounter = 0;
            }
        }

        // Recolored pixels in percent
        final int recolored = (int) ((((double) recoloredElements)
                / ((double) (mosaicWidth * mosaicHeight))) * 1000);

        // Output for optimisation and statistic
        if (optimisation) {
            setOptimizationInfo(highGaps, highestGap, recolored);
        } else if (statistic) {
            setStatisticInfo(highGaps, highestGap);
        }
    }

    /**
     * Returns the original image (to determine the original colors later).
     *
     * @param mosaicWidth
     * @param mosaicHeight
     * @return the original image (to determine the original colors later).
     */
    private int[][][] getOriginalMatrix(final int mosaicWidth,
            final int mosaicHeight) {
        final BufferedImage original = calculation.scale(
                dataProcessing.getImage(false), mosaicWidth, mosaicHeight,
                dataProcessing.getInterpolation());
        final int[][][] originalMatrix = calculation.pixelMatrix(original);
        return originalMatrix;
    }

    private void setOptimizationInfo(final int highGaps, final int highestGap,
            final int recolored) {
        dataProcessing.setInfo(
                textbundle.getString("output_stabilityOptimisation_5"),
                DataProcessor.SHOW_IN_BOTH);
        dataProcessing.setInfo("("
                + textbundle.getString("output_stabilityOptimisation_8") + ". "
                + recolored / RECOLORED_DIVISOR
                + textbundle.getString("output_decimalPoint")
                + recolored % RECOLORED_DIVISOR + " % "
                + textbundle.getString("output_stabilityOptimisation_9") + ".)",
                DataProcessor.SHOW_IN_BOTH);
        dataProcessing.setInfo(
                textbundle.getString("output_stabilityOptimisation_10") + " "
                        + maximumGapHeight + ": " + highGaps,
                DataProcessor.SHOW_IN_BOTH);
        dataProcessing.setInfo(
                "(" + textbundle.getString("output_stabilityOptimisation_11")
                        + " " + highestGap + " "
                        + textbundle.getString(
                                "output_stabilityOptimisation_12")
                        + ")",
                DataProcessor.SHOW_IN_BOTH);
    }

    private void setStatisticInfo(final int highGaps, final int highestGap) {
        dataProcessing.setInfo(
                textbundle.getString("output_stabilityOptimisation_7") + ":",
                DataProcessor.SHOW_IN_BOTH);
        dataProcessing.setInfo(
                textbundle.getString("output_stabilityOptimisation_6"),
                DataProcessor.SHOW_IN_BOTH);
        dataProcessing.setInfo(
                textbundle.getString("output_stabilityOptimisation_10") + " "
                        + maximumGapHeight + ": " + highGaps,
                DataProcessor.SHOW_IN_BOTH);
        dataProcessing.setInfo(
                "(" + textbundle.getString("output_stabilityOptimisation_11")
                        + " " + highestGap + " "
                        + textbundle.getString(
                                "output_stabilityOptimisation_12")
                        + ")",
                DataProcessor.SHOW_IN_BOTH);
    }

    private void showErrorDialogForFailedConsistencyCheck() {
        if (optimisation) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        dataProcessing.errorDialog(textbundle
                                .getString("output_stabilityOptimisation_1")
                                + "\n\r"
                                + textbundle.getString(
                                        "output_stabilityOptimisation_2")
                                + "\n\r"
                                + textbundle.getString(
                                        "output_stabilityOptimisation_3")
                                + "\n\r" + textbundle.getString(
                                        "output_stabilityOptimisation_4"));
                    }
                });
            } catch (final Exception e) {
                System.out.println(e.toString());
            }
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        dataProcessing.errorDialog(textbundle
                                .getString("output_stabilityOptimisation_1")
                                + "\n\r"
                                + textbundle.getString(
                                        "output_stabilityOptimisation_2")
                                + "\n\r" + textbundle.getString(
                                        "output_stabilityOptimisation_4"));
                    }
                });
            } catch (final Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * Initialize array to remember the gaps.
     *
     * @param mosaicWidth
     * @param mosaicHeight
     */
    private void initializeBordersArray(final int mosaicWidth,
            final int mosaicHeight) {
        borders = new boolean[mosaicHeight][mosaicWidth];

        for (int x = 0; x < mosaicHeight; x++) {
            for (int y = 0; y < mosaicWidth; y++) {
                borders[x][y] = false;
            }
        }
    }

    /**
     * Thresholdarray and colorarray.
     *
     * @param tilingInfoEnum
     * @param colors
     * @param threshold
     */
    private void initializeThresholdAndColorArrays(
            final Enumeration<Object> tilingInfoEnum, final String[] colors,
            final int[] threshold) {
        if (quantisationAlgo == DataProcessor.FLOYD_STEINBERG) {
            // Floyd-Steinberg
            colors[0] = (String) tilingInfoEnum.nextElement();
            colors[1] = (String) tilingInfoEnum.nextElement();
            threshold[0] = 0;
            threshold[1] = THRESHOLD1;
            threshold[2] = THRESHOLD2;
        } else if (quantisationAlgo == DataProcessor.SLICING) {
            // N-Level
            for (int i = 0; i < colorCount; i++) {
                colors[i] = (String) tilingInfoEnum.nextElement();
                threshold[i] = (Integer) tilingInfoEnum.nextElement();
            }

            // the threshold array is adjusted by the luminance value 100
            // the colors are placed between 2 thresholds
            threshold[colorCount] = THRESHOLD2;
        }
    }

    /**
     * Color vector with all colors.
     *
     * @param configuration
     */
    private void initializeColorVector(final Configuration configuration) {
        for (final Enumeration<ColorObject> colorsEnum = configuration
                .getAllColors(); colorsEnum.hasMoreElements();) {
            final ColorObject color = (ColorObject) (colorsEnum.nextElement());
            this.labColors.add(calculation.rgbToLab(color.getRGB()));
            this.labColors.add(color.getName());
        }
    }

    /**
     * Compute colorCount for Floyd-Steinberg 2 colors. For all other
     * algorithms: colorVectorlength (colorValue+Name) / 2
     */
    private void setColorCount() {
        if (quantisationAlgo == DataProcessor.FLOYD_STEINBERG) {
            this.colorCount = 2;
        } else {
            this.colorCount = (tilingInfo.size() - INT4) / 2;
        }
    }

    private String getMixedColor(final String[] colors, final int[] threshold,
            final Lab color1, final Lab color2) {
        String currentColor;
        if (quantisationAlgo == DataProcessor.FLOYD_STEINBERG) {
            // Floyd-Steinberg
            currentColor = computeMixedColor(color1, color2, threshold, colors);
        } else if (quantisationAlgo == DataProcessor.SLICING) {
            // Slicing
            currentColor = computeMixedColor(color1, color2, threshold, colors);
        } else {
            // all other
            currentColor = computeMixedColor(color1, color2, labColors);
        }
        return currentColor;
    }

    private boolean atEndOfColor(final String currentColor,
            final Vector<String> pixel2) {
        return !(currentColor.equals((String) (pixel2.get(0))));
    }

    private Vector<String> getPixel2(final Mosaic mosaic,
            final ElementObject currentElement, final int colorCol,
            final int rowIndex) {
        return mosaic.getMosaic().get(rowIndex)
                .get(colorCol + currentElement.getWidth());
    }

    private boolean doesNotCreateHightGap(final boolean initialElementFits,
            final ElementObject currentElement, final int colorCol) {
        boolean elementFits = initialElementFits;

        if (colorRow >= (maximumGapHeight - 1)
                - (currentElement.getHeight() - 1)) {
            if ((currentElement.getHeight() == CUTOFF_HEIGHT && colorCol > 0
                    && bigGap(colorRow, colorCol,
                            (maximumGapHeight - 1)
                                    - (currentElement.getHeight() - 1)))
                    || bigGap(colorRow, colorCol + currentElement.getWidth(),
                            (maximumGapHeight - 1)
                                    - (currentElement.getHeight() - 1))) {
                elementFits = false;
            }
        }

        return elementFits;
    }

    /**
     * Computes the mixed color from 2 lab colors (using color vector).
     *
     * @author Adrian Schuetz
     * @param color1
     * @param color2
     * @param colorVector
     * @return Mischcolor
     */
    private String computeMixedColor(final Lab color1, final Lab color2,
            final Vector<Object> colorVector) {
        // remember best fitting color
        String newColor = "";
        // Determine mixed color
        final Lab mixedColor = new Lab();
        mixedColor.setL((color1.getL() + color2.getL()) / 2);
        mixedColor.setA((color1.getA() + color2.getA()) / 2);
        mixedColor.setB((color1.getB() + color2.getB()) / 2);
        // Run through color vector to find the best fitting color
        Lab testColor;
        String testColorName;
        double deviation;
        double smalestDeviation = Quantizer.STARTING_MIN_DIFFERENCE;

        for (final Enumeration<Object> colorsEnum = colorVector
                .elements(); colorsEnum.hasMoreElements();) {
            testColor = (Lab) (colorsEnum.nextElement());
            testColorName = (String) (colorsEnum.nextElement());
            deviation = java.lang.Math.sqrt(java.lang.Math
                    .pow(mixedColor.getL() - testColor.getL(), 2.0)
                    + java.lang.Math.pow(mixedColor.getA() - testColor.getA(),
                            2.0)
                    + java.lang.Math.pow(mixedColor.getB() - testColor.getB(),
                            2.0));

            if (deviation < smalestDeviation) {
                smalestDeviation = deviation;
                newColor = testColorName;
            }
        }

        return newColor;
    }

    /**
     * method: computeMixedColor description: computes the mixed color from 2
     * lab colors (using threshold arrays).
     *
     * @author Adrian Schuetz
     * @param color1
     * @param color2
     * @param threshold
     * @param colors
     * @return mixed color.
     */
    private String computeMixedColor(final Lab color1, final Lab color2,
            final int[] threshold, final String[] colors) {
        // remember best fitting color
        String newColor = "";
        int indicator = 0;
        // Determine average luminance of the two colors
        final double luminance = ((color1.getL() + color2.getL()) / 2);

        for (int x = 0; x < this.colorCount; x++) {
            if ((threshold[x] <= luminance) && (threshold[x + 1] > luminance)) {
                newColor = colors[x];
                indicator = 1;
            }
        }

        // set color if the luminance value is exact
        // Calculator.ONE_HUNDRED_PERCENT
        if (indicator == 0) {
            newColor = colors[colorCount - 1];
        }

        return newColor;
    }

    /**
     * Checks if there is a gap higher than the maximum value if the gap is to
     * high true is returned.
     *
     * @author Adrian Schuetz
     * @param row
     * @param colorColumn
     * @param maxGapHeight
     * @return true or false
     */
    private boolean bigGap(final int row, final int colorColumn,
            final int maxGapHeight) {
        boolean bigGap = true;

        for (int x = 1; x <= maxGapHeight; x++) {
            if (!borders[row - x][colorColumn]) {
                bigGap = false;
            }
        }

        return bigGap;
    }

    /**
     * Init a hash with same elements of different height (element with height 1
     * | same element with height 3).
     *
     * @author Adrian Schuetz
     * @param configuration
     * @return Hash
     */
    private Hashtable<String, String> hashInit(
            final Configuration configuration) {
        final Hashtable<String, String> hash = new Hashtable<>();
        ElementObject testElement1;
        ElementObject testElement2;
        final Vector<ElementObject> heightOne = new Vector<>();
        final Vector<ElementObject> heightThree = new Vector<>();
        // Enumeration of all elements (split by height)
        // vector1: elements with height = 1
        // vector2: elements with height = 3
        final Enumeration<ElementObject> elements = configuration
                .getAllElements();

        while (elements.hasMoreElements()) {
            testElement1 = (ElementObject) elements.nextElement();

            if (testElement1.getHeight() == 1) {
                heightOne.add(testElement1);
            } else {
                heightThree.add(testElement1);
            }
        }

        // Enumeration of all elements with height = 1
        // check if there is a matching element with height = 3
        // -> put in the hash
        final Enumeration<ElementObject> heightOneEnum = heightOne.elements();
        Enumeration<ElementObject> heightThreeEnum;

        while (heightOneEnum.hasMoreElements()) {
            testElement1 = (ElementObject) heightOneEnum.nextElement();
            heightThreeEnum = heightThree.elements();

            while (heightThreeEnum.hasMoreElements()) {
                testElement2 = (ElementObject) heightThreeEnum.nextElement();

                if (testElement1.getWidth() == testElement2.getWidth()) {
                    hash.put(testElement1.getName(), testElement2.getName());
                }
            }
        }

        return hash;
    }

    /**
     * Sorts the element Vector by stability.
     *
     * @author Adrian Schuetz
     * @param elementsUnsorted
     * @return elementsSorted
     */
    private Vector<ElementObject> sortElementsByStability(
            final Enumeration<ElementObject> elementsUnsorted) {
        int stabi = 0;
        boolean included = false;
        int position;
        ElementObject supportElement;
        final Vector<ElementObject> elementsSorted = new Vector<>();

        // Elements sorted by stability
        while (elementsUnsorted.hasMoreElements()) {
            supportElement = ((ElementObject) elementsUnsorted.nextElement());

            if (elementsSorted.size() == 0) {
                elementsSorted.add(supportElement);
            } else {
                stabi = supportElement.getStability();
                position = 0;
                included = false;
                final Enumeration<ElementObject> supportEnum = elementsSorted
                        .elements();

                while (supportEnum.hasMoreElements() && !included) {
                    final var anotherElement = (ElementObject) supportEnum
                            .nextElement();

                    if (stabi >= anotherElement.getStability()) {
                        elementsSorted.add(position, supportElement);
                        included = true;
                    } else {
                        position++;
                    }
                }

                if (!included) {
                    elementsSorted.add(supportElement);
                }
            }
        }

        return elementsSorted;
    }

    /**
     * Checks if there is a valid configuration elements must be rectangles,
     * without holes and the height must be 1 or 3 (Standard-Lego).
     *
     * @author Adrian Schuetz
     * @param configuration
     * @return true, false
     */
    private boolean consistencyCheck(final Configuration configuration) {
        ElementObject testElement;

        // check all elements
        for (final Enumeration<ElementObject> currentEnum = configuration
                .getAllElements(); currentEnum.hasMoreElements();) {
            testElement = currentEnum.nextElement();

            if (!(testElement.getHeight() == 1
                    || testElement.getHeight() == BRICK_HEIGHT)) {
                return false;
            } else {
                // if element is not holohedral, false
                for (int i = 0; i < testElement.getHeight(); i++) {
                    for (int j = 0; j < testElement.getWidth(); j++) {
                        if (!testElement.getMatrix()[i][j]) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Checks if there is a valid configuration for the optimisation (element
     * width = 2 and height = 1).
     *
     * @author Adrian Schuetz
     * @param configuration
     * @return true, false
     */
    private boolean consistencyCheckOptimisation(
            final Configuration configuration) {
        ElementObject testElement;

        // check all elements
        for (final Enumeration<ElementObject> currentEnum = configuration
                .getAllElements(); currentEnum.hasMoreElements();) {
            testElement = currentEnum.nextElement();

            if ((testElement.getHeight() == 1 && testElement.getWidth() == 2)) {
                return true;
            }
        }

        return false;
    }
}
