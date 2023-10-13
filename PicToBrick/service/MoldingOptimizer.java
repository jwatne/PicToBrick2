package pictobrick.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.Configuration;
import pictobrick.model.ElementObject;
import pictobrick.model.Mosaic;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Tiling with molding optimisation.
 *
 * @author Tobias Reichling
 */
public class MoldingOptimizer implements Tiler {
    /** Element covers 4 studs. */
    private static final int COVERS_4 = 4;
    /** Element covers 3 studs. */
    private static final int COVERS_3 = 3;
    /**
     * Value of 10 - if cents less than this value, prepend 0 when creating
     * price string.
     */
    private static final int TEN = 10;
    /** Number of cents per Euro. */
    private static final int CENT_TO_EURO_MULTIPLIER = 100;
    /** Single appearance average multiplier for 1x1 moldings. */
    private static final double SINGLE_APPEARANCE_AVG_1X1_MULTIPLIER = 20.0;
    /** Single price average multiplier for 1x1 moldings. */
    private static final double SINGLE_PRICE_AVG_1X1_MULTIPLIER = 10.0 / 20.0;
    /** Divisor of elementArray COVERING_1X1 values. */
    private static final double COVERING_1X2_DIVISOR = 16.0;
    /** Divisor of elementArray COVERING_1X3 values. */
    private static final double COVERING_1X3_DIVISOR = 12.0;
    /** Divisor of elementArray covering corner 2x2 values. */
    private static final double CORNER_COVERING_2X2_DIVISOR = 4.0;
    /** Divisor of elementArray COVERING_2X2 values. */
    private static final double COVERING_2X2_DIVISOR = 8.0;
    /** Divisor for all coverings larger than 1x1 in determining refValues. */
    private static final double COVERING_DIVISOR = 27.0 / 114.0;
    /** 100% as integer. */
    private static final int ONE_HUNDRED_PERCENT = 100;
    /** Number of tiling optimizations for standard optimization. */
    private static final int NUMBER_OF_TILING_OPTIONS = 7;
    /** Number of tiling options for additional optimization. */
    static final int NUMBER_OF_OPT_TILING_OPTIONS = 5;
    /** Index for covering 1x1 indicator. */
    static final int COVERING_1X1 = 0;
    /** Index for covering 1x2 indicator. */
    public static final int COVERING_1X2 = 1;
    /** Index for covering 1x3 indicator. */
    public static final int COVERING_1X3 = 2;
    /** Index for corner covering with 2x2 indicator. */
    public static final int CORNER_COVERING_2X2 = 3;
    /** Index for 2x2 covering indicator. */
    public static final int COVERING_2X2 = 4;
    /** Index for normal molding indicator. */
    private static final int NORMAL_MOLDING = 5;
    /** Index for number of different 1x1 molding colors needed. */
    private static final int NUM_1X1_MOLDINGS = 6;
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Percent calculatd. */
    private int percent = 0;
    /** Number of rows in image. */
    private int rows = 0;
    /** Row being processed. */
    private int colorRow;

    /** <code>true</code> if calculating statistics. */
    private boolean statisticOutput;
    /** Names of available colors. */
    private String[] colorArray;
    /** Array of tiling elements. */
    private int[][] elementArray;

    /** Name of color for pixel being processed. */
    private String currentColorName;

    /**
     * Index of element in {@link #colorArray} with value matching
     * {@link #currentColorName}.
     */
    private int currColorNum;

    /** Number of colors available for tiling. */
    private int colorCount;

    /** Number of pixels in mosaic image. */
    private int pixelCount;
    /** Information about the algorithm mode. */
    private final Vector<Object> dialogSelection;
    /**
     * Indicates whether to perform additional optimization.
     */
    private boolean doAdditionalOptimization;
    /**
     * Number of times pixel colors change.
     */
    private int pixelColorChanges;

    /** Service class performing additional optimization. */
    private MoldingAdditionalOptimizer additionalOptimizer;

    /**
     * Contructor.
     *
     * @author Tobias Reichling
     * @param processor
     * @param calculation
     * @param selection
     */
    public MoldingOptimizer(final DataProcessor processor,
            final Calculator calculation, final Vector<Object> selection) {
        this.dataProcessing = processor;
        this.dialogSelection = selection;
        this.additionalOptimizer = new MoldingAdditionalOptimizer(this);
    }

    /**
     * Returns row being processed.
     *
     * @return row being processed.
     */
    public int getColorRow() {
        return colorRow;
    }

    /**
     * Returns Array of tiling elements.
     *
     * @return Array of tiling elements.
     */
    public int[][] getElementArray() {
        return elementArray;
    }

    /**
     * Returns current color number.
     *
     * @return current color number.
     */
    public int getCurrColorNum() {
        return currColorNum;
    }

    /**
     * Returns name of color for pixel being processed.
     *
     * @return name of color for pixel being processed.
     */
    public String getCurrentColorName() {
        return currentColorName;
    }

    /**
     * Returns number of times pixel colors change.
     *
     * @return number of times pixel colors change.
     */
    public int getPixelColorChanges() {
        return pixelColorChanges;
    }

    /**
     * Sets number of times pixel colors change.
     *
     * @param changes number of times pixel colors change.
     */
    public void setPixelColorChanges(final int changes) {
        this.pixelColorChanges = changes;
    }

    /**
     * Returns number of colors available for tiling.
     *
     * @return number of colors available for tiling.
     */
    public int getColorCount() {
        return colorCount;
    }

    /**
     * Tiling with molding optimisation.
     *
     * @author Tobias Reichling
     * @param mosaicWidth
     * @param mosaicHeight
     * @param config
     * @param mosaic
     * @param statistic
     * @return mosaic
     */
    public Mosaic tiling(final int mosaicWidth, final int mosaicHeight,
            final Configuration config, final Mosaic mosaic,
            final boolean statistic) {
        setOptimizationMethod();
        this.rows = mosaicHeight;
        this.pixelCount = mosaicWidth * mosaicHeight;
        this.statisticOutput = statistic;
        additionalOptimizer.setConfiguration(config);
        // consistency check: all elements in the configuration must be
        // the same elements as in the system ministeck configuration!
        // if no, we must switch to the algorithm "basic elements only"
        // if yes, we can process the original algorithm for molding
        // optimisation
        final Configuration ministeckSystem = dataProcessing
                .getSystemConfiguration(2);

        if (!consistencyCheck(ministeckSystem, config)) {
            return basicElementsOnly(mosaicWidth, mosaicHeight, config, mosaic);
        } else {
            // ------------ molding optimisation ------------
            // flags
            pixelColorChanges = 0;
            currentColorName = "";
            currColorNum = -1;
            Vector<String> pixel;
            Enumeration<ElementObject> sorted;
            int left = 0;
            boolean elementSet = false;
            ElementObject currentElement;
            // init arrays
            colorCount = config.getQuantityColors();
            final Enumeration<ColorObject> colorEnum = config.getAllColors();
            initializeColorArray(colorEnum);
            initializeElementArray();

            // if: additional optimisation mode ...
            if (doAdditionalOptimization) {
                additionalOptimizer.initializeElementArrayOptimization();
            }

            // scan mosaic linear
            // for each pixel the elementVector is computed
            // (greatest quantity to the vector head)
            // if an element is not available any more, a new molding
            // is added to all elment counters in the current color
            for (colorRow = 0; colorRow < mosaicHeight; colorRow++) {
                // refresh progress bar
                try {
                    refreshProgressBarAlgorithm();
                } catch (final Exception e) {
                    System.out.println(e.toString());
                }

                for (int colorCol = 0; colorCol < mosaicWidth; colorCol++) {
                    pixel = mosaic.getMosaic().get(colorRow).get(colorCol);

                    // if the pixel is not reserved by an other element ...
                    if (!pixel.isEmpty()) {
                        // compute color
                        currentColorName = (String) (pixel.get(0));
                        currColorNum = computeColorNumber();
                        // compute element vector
                        final var elementsSorted = sortElementsByAvailability(
                                config.getAllElements());
                        sorted = elementsSorted.elements();
                        // check each element in this sorted vector
                        elementSet = false;

                        // if: additional optimisation mode:
                        // a vector is computed with critical elements
                        // an element is critical if the counter is more than 2x
                        // the quantity of this element on a normal molding
                        // -----------------------------------------------
                        // (1x1 elements cause no problems)
                        if (doAdditionalOptimization) {
                            elementSet = additionalOptimizer
                                    .getAdditionalOptimizationModeElementSet(
                                            mosaicWidth, mosaicHeight, mosaic,
                                            elementSet, colorCol);
                        }

                        // if: normal optimisation mode
                        // ... or ...
                        // if: additional optimisation mode, but no critical
                        // element is set to the
                        // mosaic:
                        // ...
                        // the normal algorithm is used here:
                        // scan sorted element vector and check for each
                        // element
                        while (sorted.hasMoreElements() && !elementSet) {
                            currentElement = sorted.nextElement();
                            // find the left position in the top row of the
                            // element matrix
                            left = -1;

                            for (int i = 0; i < currentElement
                                    .getWidth(); i++) {
                                if (currentElement.getMatrix()[0][i]
                                        && left == -1) {
                                    left = i;
                                }
                            }

                            // check mosaic border: bottom
                            if (((colorRow + currentElement.getHeight()
                                    - 1) < mosaicHeight)
                                    // left
                                    && ((colorCol - left) >= 0)
                                    // right
                                    && ((colorCol + (currentElement.getWidth()
                                            - (left + 1))) < mosaicWidth)) {
                                elementSet = checkBottomBorder(mosaic, left,
                                        elementSet, currentElement, colorCol);
                            } // end border check
                        } // end while
                    } // end if (!pixel.isEmpty()){
                }
            } // end for(for())

            // output
            output();

            // refresh progress bar
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        if (statisticOutput) {
                            dataProcessing.refreshProgressBarAlgorithm(
                                    ONE_HUNDRED_PERCENT,
                                    ProgressBarsAlgorithms.STATISTICS);
                        } else {
                            dataProcessing.refreshProgressBarAlgorithm(
                                    ONE_HUNDRED_PERCENT,
                                    ProgressBarsAlgorithms.TILING);
                        }
                    }
                });
            } catch (final Exception e) {
                System.out.println(e.toString());
            }

            return mosaic;
        }
    }

    private boolean checkBottomBorder(final Mosaic mosaic, final int left,
            final boolean initialElementSet, final ElementObject currentElement,
            final int colorCol) {
        boolean elementSet = initialElementSet;
        Vector<String> pixel2;
        // farb matching
        boolean elementFits = true;

        for (int elementRow = 0; elementRow < currentElement
                .getHeight(); elementRow++) {
            for (int elementColumn = 0; elementColumn < currentElement
                    .getWidth(); elementColumn++) {
                // check only "true" positions in the
                // element matrix
                if (currentElement.getMatrix()[elementRow][elementColumn]) {
                    pixel2 = mosaic.getMosaic().get(colorRow + elementRow)
                            .get((colorCol + elementColumn) - left);

                    if (!pixel2.isEmpty()) {
                        if (!((String) (pixel2.get(0)))
                                .equals(currentColorName)) {
                            elementFits = false;
                        }
                    } else {
                        elementFits = false;
                    }
                }
            }
        }

        if (elementFits) {
            // set element
            elementSet = true;

            for (int elementRow = 0; elementRow < currentElement
                    .getHeight(); elementRow++) {
                for (int elementColumn = 0; elementColumn < currentElement
                        .getWidth(); elementColumn++) {
                    if (currentElement.getMatrix()[elementRow][elementColumn]) {
                        mosaic.initVector(colorRow + elementRow,
                                (colorCol + elementColumn) - left);
                    }
                }
            }
            mosaic.setElement(colorRow, colorCol, currentColorName, false);
            mosaic.setElement(colorRow, colorCol, currentElement.getName(),
                    true);
            // count element and if necessary use new
            // molding
            updateElementAppearance(currentElement);
        }
        return elementSet;
    }

    private void refreshProgressBarAlgorithm()
            throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                percent = (int) ((Calculator.ONE_HUNDRED_PERCENT / rows)
                        * colorRow);

                if (statisticOutput) {
                    dataProcessing.refreshProgressBarAlgorithm(percent,
                            ProgressBarsAlgorithms.STATISTICS);
                } else {
                    dataProcessing.refreshProgressBarAlgorithm(percent,
                            ProgressBarsAlgorithms.TILING);
                }
            }
        });
    }

    private void initializeElementArray() {
        elementArray = new int[colorCount][NUMBER_OF_TILING_OPTIONS];

        for (int j = 0; j < colorCount; j++) {
            elementArray[j][COVERING_1X1] = 0; // 0 => 1x1 covering: 1
            elementArray[j][COVERING_2X2] = 0; // 1 => 1x2 covering: 2
            elementArray[j][COVERING_1X3] = 0; // 2 => 1x3 covering: 3 and
                                               // matrix 1x3
            elementArray[j][CORNER_COVERING_2X2] = 0; // 3 => corner covering: 3
                                                      // and matrix
            // 2x2
            elementArray[j][COVERING_2X2] = 0; // 4 => 2x2 covering: 4
            elementArray[j][NORMAL_MOLDING] = 0; // 5 => normal molding
            elementArray[j][NUM_1X1_MOLDINGS] = 0; // 6 => 1er molding
        }
    }

    private void initializeColorArray(
            final Enumeration<ColorObject> colorEnum) {
        colorArray = new String[colorCount];

        for (int i = 0; i < colorCount; i++) {
            colorArray[i] = ((ColorObject) colorEnum.nextElement()).getName();
        }
    }

    private Mosaic basicElementsOnly(final int mosaicWidth,
            final int mosaicHeight, final Configuration config,
            final Mosaic mosaic) {
        // ------------"basic elements only"------------
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    dataProcessing.errorDialog(
                            textbundle.getString("output_moldingOptimisation_1")
                                    + "\n\r" + textbundle.getString(
                                            "output_moldingOptimisation_2"));
                }
            });
        } catch (final Exception e) {
            System.out.println(e.toString());
        }

        for (colorRow = 0; colorRow < mosaicHeight; colorRow++) {
            // refresh progress bar
            // 2 different bars: normal mode and statistic mode
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        percent = (int) ((Calculator.ONE_HUNDRED_PERCENT / rows)
                                * colorRow);
                        if (statisticOutput) {
                            dataProcessing.refreshProgressBarAlgorithm(percent,
                                    ProgressBarsAlgorithms.STATISTICS);
                        } else {
                            dataProcessing.refreshProgressBarAlgorithm(percent,
                                    ProgressBarsAlgorithms.TILING);
                        }
                    }
                });
            } catch (

            final Exception e) {
                System.out.println(e.toString());
            }

            for (int col = 0; col < mosaicWidth; col++) {
                mosaic.setElement(colorRow, col, config.getBasisName(), true);
            }
        }

        return mosaic;
    }

    /**
     * Vector dialogSelection contains information about the algorithm mode.
     * <ul>
     * <li>vector is empty: no additional optimisation</li>
     * <li>vector contains action command "no": no additional optimisation</li>
     * <li>vector contains action command "yes": additional optimisation
     * mode</li>
     * </ul>
     */
    private void setOptimizationMethod() {
        this.doAdditionalOptimization = ((!dialogSelection.isEmpty())
                && ("yes".equals((String) dialogSelection.elementAt(0))));
    }

    /**
     * Sorts the element vector.
     *
     * @author Tobias Reichling
     * @param elementsUnsorted
     * @return elementsSorted
     */
    private Vector<ElementObject> sortElementsByAvailability(
            final Enumeration<ElementObject> elementsUnsorted) {
        // vector init
        final Vector<ElementObject> elementsSorted = new Vector<>();
        // array init
        final var refValues = new double[NUMBER_OF_OPT_TILING_OPTIONS];
        // -------------------
        // 1er molding: quantity of 1x1 elements = 20
        // normal molding: quantity of 1x1 elements = 2
        // -------------------
        // during algorithm:
        // 1x1 element: computes average values for quantity and costs
        // -------------------
        final var singleAppearAvg = getSingleAppearanceAverage();
        final var singlePriceAvg = getSinglePriceAverage();
        //
        // ( current availaible quantity costs per molding )
        // referenceValue = ( --------------------------------- /
        // ----------------------------- )
        // ( quantity in a normal molding covering per molding )
        //
        // first fraction
        // bigger, the more quantity of the element is available in ratio to the
        // quantity of this
        // element in a normal molding
        //
        // second fraction
        // compute the covering costs per pixel of an element
        // 1x1 elements have other costs per coverd pixel ... so ...
        // the more 1er moldings we use the dearer are the covering costs of an
        // 1x1
        // element
        //
        // complete fraction
        // uses current available quantity and costs per covering one pixel to
        // sort
        // the element vector
        //
        refValues[COVERING_1X1] = (elementArray[currColorNum][COVERING_1X1]
                / singleAppearAvg) / singlePriceAvg;
        refValues[COVERING_1X2] = getRefValueForTileType(COVERING_1X2,
                COVERING_1X2_DIVISOR);
        refValues[COVERING_1X3] = getRefValueForTileType(COVERING_1X3,
                COVERING_1X3_DIVISOR);
        refValues[CORNER_COVERING_2X2] = getRefValueForTileType(
                CORNER_COVERING_2X2, CORNER_COVERING_2X2_DIVISOR);
        refValues[COVERING_2X2] = getRefValueForTileType(COVERING_2X2,
                COVERING_2X2_DIVISOR);
        // flags
        boolean included = false;
        int position;
        ElementObject supportElement;

        while (elementsUnsorted.hasMoreElements()) {
            supportElement = elementsUnsorted.nextElement();

            // if the sorted vector is empty, add the next element ...
            if (elementsSorted.size() == 0) {
                elementsSorted.add(supportElement);
            } else {
                // ... then check the reference value of the next element and
                // sort
                // it accordingly into the vector
                position = 0;
                included = false;
                final Enumeration<ElementObject> supportEnum = elementsSorted
                        .elements();

                while (supportEnum.hasMoreElements() && !included) {
                    final ElementObject anotherElement = supportEnum
                            .nextElement();

                    if (refValues[computeElementNumber(
                            supportElement)] >= refValues[computeElementNumber(
                                    anotherElement)]) {
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

    private double getRefValueForTileType(final int typeIndex,
            final double typeDivisor) {
        return (elementArray[currColorNum][typeIndex] / typeDivisor)
                / COVERING_DIVISOR;
    }

    private double getSinglePriceAverage() {
        return (elementArray[currColorNum][NORMAL_MOLDING] * COVERING_DIVISOR
                + elementArray[currColorNum][NUM_1X1_MOLDINGS]
                        * SINGLE_PRICE_AVG_1X1_MULTIPLIER)
                / (elementArray[currColorNum][NORMAL_MOLDING]
                        + elementArray[currColorNum][NUM_1X1_MOLDINGS]);
    }

    private double getSingleAppearanceAverage() {
        return (elementArray[currColorNum][NORMAL_MOLDING] * 2.0
                + elementArray[currColorNum][NUM_1X1_MOLDINGS]
                        * SINGLE_APPEARANCE_AVG_1X1_MULTIPLIER)
                / (elementArray[currColorNum][NORMAL_MOLDING]
                        + elementArray[currColorNum][NUM_1X1_MOLDINGS]);
    }

    /**
     * Updates the array with the element quantity and broach a new molding if
     * necessary.
     *
     * @param element
     * @author Tobias Reichling
     */
    void updateElementAppearance(final ElementObject element) {
        final int elementNumber = computeElementNumber(element);
        final int quantity = elementArray[currColorNum][elementNumber];
        // decrement
        elementArray[currColorNum][elementNumber] = (quantity - 1);

        if (quantity == 0) {
            if (!(elementNumber == 0)) {
                // if quantity is 0, use a new normal molding ...
                elementArray[currColorNum][COVERING_1X1] = getElement(
                        COVERING_1X1, 2);
                elementArray[currColorNum][COVERING_1X2] = getElement(
                        COVERING_1X2, (int) COVERING_1X2_DIVISOR);
                elementArray[currColorNum][COVERING_1X3] = getElement(
                        COVERING_1X3, (int) COVERING_1X3_DIVISOR);
                elementArray[currColorNum][CORNER_COVERING_2X2] = getElement(
                        CORNER_COVERING_2X2, (int) CORNER_COVERING_2X2_DIVISOR);
                elementArray[currColorNum][COVERING_2X2] = getElement(
                        COVERING_2X2, (int) COVERING_2X2_DIVISOR);
                elementArray[currColorNum][NORMAL_MOLDING] = getElement(
                        NORMAL_MOLDING, 1);
            } else {
                // if quantity of 1x1 element is 0, use a new 1er molding ...
                elementArray[currColorNum][COVERING_1X1] = getElement(
                        COVERING_1X1,
                        (int) SINGLE_APPEARANCE_AVG_1X1_MULTIPLIER);
                elementArray[currColorNum][NUM_1X1_MOLDINGS] = getElement(
                        NUM_1X1_MOLDINGS, 1);
            }
        }

        if (doAdditionalOptimization) {
            additionalOptimizer.performAdditionalOptimization();
        }
    }

    private int getElement(final int type, final int addition) {
        return elementArray[currColorNum][type] + addition;
    }

    /**
     * Computes output.
     *
     * @author Tobias Reichling
     */
    private void output() {
        int normalUsageAllColors = 0;
        int singleUsageAllColors = 0;
        int normalFracturedAllColors = 0;
        int singleFracturedAllColors = 0;
        int normalFracturedOneColor = 0;
        int singleFracturedOneColor = 0;
        dataProcessing.setInfo(
                textbundle.getString("output_moldingOptimisation_3"),
                DataProcessor.SHOW_IN_BOTH);

        if (!doAdditionalOptimization) {
            dataProcessing.setInfo(
                    "(" + textbundle.getString("output_moldingOptimisation_4")
                            + ")",
                    DataProcessor.SHOW_IN_BOTH);
        } else {
            final int recolored = (int) ((((double) pixelColorChanges)
                    / ((double) (pixelCount))) * 1000);
            dataProcessing.setInfo("("
                    + textbundle.getString("output_moldingOptimisation_5")
                    + ". " + recolored / RECOLORED_DIVISOR
                    + textbundle.getString("output_decimalPoint")
                    + recolored % RECOLORED_DIVISOR + " % "
                    + textbundle.getString("output_moldingOptimisation_6")
                    + ".)", DataProcessor.SHOW_IN_BOTH);
        }

        dataProcessing.setInfo(
                textbundle.getString("output_moldingOptimisation_7") + ",", 2);
        dataProcessing.setInfo(
                textbundle.getString("output_moldingOptimisation_8") + ":", 2);
        dataProcessing.setInfo("[ "
                + textbundle.getString("output_moldingOptimisation_9") + " / "
                + textbundle.getString("output_moldingOptimisation_10") + " ]",
                2);

        // for each color ...
        for (int j = 0; j < colorCount; j++) {
            // ... and only if we have used the color within the mosaic
            if (elementArray[j][NORMAL_MOLDING] > 0
                    || elementArray[j][NUM_1X1_MOLDINGS] > 0) {
                normalUsageAllColors = normalUsageAllColors
                        + elementArray[j][NORMAL_MOLDING];
                singleUsageAllColors = singleUsageAllColors
                        + elementArray[j][NUM_1X1_MOLDINGS];
                normalFracturedOneColor = 0;
                singleFracturedOneColor = 0;

                // decrement the remaining elements so often (with the quantity
                // of a normal
                // molding)
                // till all element counters are <= 0 (normal moldings and 1er
                // moldings)
                normalFracturedOneColor = decrementElementArrayValues(
                        normalFracturedOneColor, j);

                // for each molding variation one will set to "not complete
                // usable"
                if (normalFracturedOneColor > 0) {
                    normalFracturedOneColor--;
                }

                normalFracturedAllColors = normalFracturedAllColors
                        + normalFracturedOneColor;

                singleFracturedOneColor = updateCounterForUsedUniqueMoldings(
                        singleFracturedOneColor, j);

                // for each molding variation one will set to "not complete
                // usable"
                if (singleFracturedOneColor > 0) {
                    singleFracturedOneColor--;
                }

                singleFracturedAllColors = singleFracturedAllColors
                        + singleFracturedOneColor;
                // output per color
                dataProcessing.setInfo("[" + elementArray[j][NORMAL_MOLDING]
                        + "/" + normalFracturedOneColor + "]  ["
                        + +elementArray[j][NUM_1X1_MOLDINGS] + "/"
                        + singleFracturedOneColor + "]  -  " + colorArray[j],
                        2);
            }
        }

        // calculation costs, etc.
        final int costs = normalUsageAllColors * 27 + singleUsageAllColors * 10;
        final int euro = costs / CENT_TO_EURO_MULTIPLIER;
        final int cent = costs % CENT_TO_EURO_MULTIPLIER;
        String centText;

        if (cent < TEN) {
            centText = "0" + cent;
        } else {
            centText = "" + cent;
        }
        final int percentUsage = (int) (((double) (normalFracturedAllColors
                + singleFracturedAllColors))
                / ((double) (normalUsageAllColors + singleUsageAllColors))
                * 10000.0);
        // output costs, etc.
        dataProcessing.setInfo(textbundle
                .getString("output_moldingOptimisation_11") + ": "
                + (normalUsageAllColors + singleUsageAllColors) + " "
                + textbundle.getString("output_moldingOptimisation_12") + ". "
                + (normalFracturedAllColors + singleFracturedAllColors) + " ("
                + textbundle.getString("output_moldingOptimisation_13") + ". "
                + (percentUsage / CENT_TO_EURO_MULTIPLIER)
                + textbundle.getString("output_decimalPoint")
                + (percentUsage % CENT_TO_EURO_MULTIPLIER) + " %) "
                + textbundle.getString("output_moldingOptimisation_14") + ".",
                DataProcessor.SHOW_IN_BOTH);
        dataProcessing
                .setInfo(
                        textbundle.getString("output_moldingOptimisation_15")
                                + " " + normalUsageAllColors + " "
                                + textbundle.getString(
                                        "output_moldingOptimisation_16")
                                + " " + singleUsageAllColors + " "
                                + textbundle.getString(
                                        "output_moldingOptimisation_17")
                                + ": " + euro
                                + textbundle.getString("output_decimalPoint")
                                + centText + " "
                                + textbundle.getString(
                                        "output_moldingOptimisation_18"),
                        DataProcessor.SHOW_IN_BOTH);
    }

    private int updateCounterForUsedUniqueMoldings(
            final int initialSingleFracturedOneColor, final int j) {
        int singleFracturedOneColor = initialSingleFracturedOneColor;

        while (elementArray[j][COVERING_1X1] > 0) {
            // counter for particular used 1er moldings
            singleFracturedOneColor++;
            elementArray[j][COVERING_1X1] = elementArray[j][COVERING_1X1]
                    - (int) SINGLE_APPEARANCE_AVG_1X1_MULTIPLIER;
        }

        return singleFracturedOneColor;
    }

    private int decrementElementArrayValues(
            final int initialNormalFracturedOneColor, final int j) {
        int normalFracturedOneColor = initialNormalFracturedOneColor;

        while (elementArray[j][COVERING_1X2] > 0
                || elementArray[j][COVERING_1X3] > 0
                || elementArray[j][CORNER_COVERING_2X2] > 0
                || elementArray[j][COVERING_2X2] > 0) {
            // counter for particular used normal moldings
            normalFracturedOneColor++;
            elementArray[j][COVERING_1X1] -= 2;
            elementArray[j][COVERING_1X2] -= (int) COVERING_1X2_DIVISOR;
            elementArray[j][COVERING_1X3] -= (int) COVERING_1X3_DIVISOR;
            final int decrement = (int) CORNER_COVERING_2X2_DIVISOR;
            elementArray[j][CORNER_COVERING_2X2] -= decrement;
            elementArray[j][COVERING_2X2] -= (int) COVERING_2X2_DIVISOR;
        }

        return normalFracturedOneColor;
    }

    /**
     * Returns the color number.
     *
     * @author Tobias Reichling
     * @return Nummer
     */
    private int computeColorNumber() {
        for (int x = 0; x < colorArray.length; x++) {
            if (colorArray[x].equals(currentColorName)) {
                return x;
            }
        }

        return -1;
    }

    /**
     * Returns the element number.
     *
     * @author Tobias Reichling
     * @param element Element whose number is returned.
     * @return element number.
     */
    private int computeElementNumber(final ElementObject element) {
        int counter = 0;

        for (int row = 0; row < element.getHeight(); row++) {
            for (int column = 0; column < element.getWidth(); column++) {
                if (element.getMatrix()[row][column]) {
                    counter++;
                }
            }
        }

        switch (counter) {
        case 1:
            return COVERING_1X1;
        case 2:
            return COVERING_1X2;
        case COVERS_3:
            if (element.getWidth() == 2) {
                return CORNER_COVERING_2X2;
            } else {
                return COVERING_1X3;
            }
        case COVERS_4:
            return COVERING_2X2;
        default:
            return -1;
        }
    }

    /**
     * Checks if configuration is valid.
     *
     * @author Tobias Reichling
     * @param ministeckSystem
     * @param config
     * @return true, false
     */
    private boolean consistencyCheck(final Configuration ministeckSystem,
            final Configuration config) {
        // elementsvektoren erstellen
        final Vector<ElementObject> systemConf = new Vector<>();
        final Vector<ElementObject> currentConf = new Vector<>();

        for (final Enumeration<ElementObject> systemEnum = ministeckSystem
                .getAllElements(); systemEnum.hasMoreElements();) {
            systemConf.add(systemEnum.nextElement());
        }

        for (final Enumeration<ElementObject> currentEnum = config
                .getAllElements(); currentEnum.hasMoreElements();) {
            currentConf.add(currentEnum.nextElement());
        }

        // not valid if the two vectors have different size!
        if (!(systemConf.size() == currentConf.size())) {
            return false;
        } else {
            int counter = -1;
            boolean[][] systemMatrix;
            int systemWidth;
            int systemHeight;
            ElementObject supportElement;

            for (final Enumeration<ElementObject> systemMatrixEnum = systemConf
                    .elements(); systemMatrixEnum.hasMoreElements();) {
                supportElement = (systemMatrixEnum.nextElement());
                systemMatrix = supportElement.getMatrix();
                systemWidth = supportElement.getWidth();
                systemHeight = supportElement.getHeight();
                counter = 0;
                // check each element from vector 1 to each element from vector
                // 2
                boolean found = false;

                while ((counter < currentConf.size()) && (!found)) {
                    if ((systemWidth == (currentConf.get(counter)).getWidth())
                            && (systemHeight == (currentConf.get(counter))
                                    .getHeight())) {
                        if (elementEqual((currentConf.get(counter)).getMatrix(),
                                systemMatrix, systemWidth, systemHeight)) {
                            found = true;
                            currentConf.remove(counter);
                        }
                    }

                    counter++;
                }

                if (!found) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Checks 2 boolean arrays and returns true if the arrays are equal.
     *
     * @author Tobias Reichling
     * @param matrix1
     * @param matrix2
     * @param width
     * @param height
     * @return result: true bzw. false
     */
    private boolean elementEqual(final boolean[][] matrix1,
            final boolean[][] matrix2, final int width, final int height) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!(matrix1[i][j] == matrix2[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
}
