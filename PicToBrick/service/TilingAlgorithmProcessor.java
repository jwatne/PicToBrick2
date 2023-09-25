package pictobrick.service;

import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.DataManagement;
import pictobrick.model.Mosaic;
import pictobrick.ui.MainWindow;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Utility class for processing tiling algorithms. Code moved from DataProcessor
 * by John Watne 09/2023.
 */
public class TilingAlgorithmProcessor {
    /** Index = 3 for max gap height??? */
    private static final int MAX_GAP_HEIGHT_INDEX = 3;
    /** 100% value for progress bars. */
    private static final int ONE_HUNDRED_PERECENT = 100;
    /** 50% value for progress bars. */
    private static final int FIFTY_PERCENT = 50;
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Calculator. */
    private Calculator calculation;
    /** Contains all program data and provides appropriate methods. */
    private final DataManagement dataManagement;
    /** Indicates wheter to output statistics. */
    private boolean statisticOutput;
    /** Main application window. */
    private final MainWindow mainWindow;
    /** Mosaic. */
    private Mosaic statisticMosaic;
    /** Molding optimizer. */
    private MoldingOptimizer moldingOptimisation;
    /** Tiling info. */
    private Vector<Object> tilingInfo;
    /** Costs optimizer. */
    private CostsOptimizer costsOptimisation;
    /** Stability optimizer. */
    private StabilityOptimizer stabilityOptimisation;
    /** Basic elements only tiler. */
    private BasicElementsOnlyTiler basicElementsOnly;

    /**
     * Consructor.
     *
     * @param dataProcessor the calling DataProcessor.
     */
    public TilingAlgorithmProcessor(final DataProcessor dataProcessor) {
        this.dataProcessing = dataProcessor;
        this.calculation = dataProcessor.getCalculation();
        this.dataManagement = dataProcessor.getDataManagement();
        this.statisticOutput = dataProcessor.isStatisticOutput();
        this.mainWindow = dataProcessor.getMainWindow();
        this.tilingInfo = dataProcessor.getTilingInfo();
    }

    /**
     * Perform element size optimization.
     *
     * @param outputDataProcessing1 Output data processing 1 text.
     * @param outputDataProcessing2 Output data processing 2 text.
     * @param outputDataProcessing3 Output data processing 3 text.
     * @param outputDataProcessing4 Output data processing 4 text.
     */
    public final void doElementSizeOptimization(
            final String outputDataProcessing1,
            final String outputDataProcessing2,
            final String outputDataProcessing3,
            final String outputDataProcessing4) {
        var elementSizeOptimisation = new ElementSizeOptimizer(dataProcessing,
                calculation);
        dataProcessing.setElementSizeOptimisation(elementSizeOptimisation);
        elementSizeOptimisation.tiling(dataManagement.getMosaicWidth(),
                dataManagement.getMosaicHeight(),
                dataManagement.getCurrentConfiguration(),
                dataManagement.getMosaicInstance(), false);
        elementSizeOptimisation = null;

        // Compare the algorithm with method Basic elements only
        // for statistic evaluation
        if (statisticOutput) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        mainWindow.refreshProgressBarAlgorithm(FIFTY_PERCENT,
                                ProgressBarsAlgorithms.STATISTICS);
                    }
                });
            } catch (final Exception e) {
                System.out.println(e.toString());
            }

            dataProcessing.setInfo(outputDataProcessing1 + ":",
                    DataProcessor.SHOW_IN_BOTH);
            dataProcessing.setInfo(outputDataProcessing2,
                    DataProcessor.SHOW_IN_BOTH);
            dataProcessing.setInfo(
                    outputDataProcessing3 + ": "
                            + (dataManagement.getMosaicWidth()
                                    * dataManagement.getMosaicHeight())
                            + " " + outputDataProcessing4 + ".",
                    DataProcessor.SHOW_IN_BOTH);
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        mainWindow.refreshProgressBarAlgorithm(
                                ONE_HUNDRED_PERECENT,
                                ProgressBarsAlgorithms.STATISTICS);
                    }
                });
            } catch (final Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * Perform molding optimization.
     *
     * @param outputDataProcessing1 Output data processing 1 text.
     */
    public void doMoldingOptimization(final String outputDataProcessing1) {
        // Copy the mosaic for statistic evaluation
        if (statisticOutput) {
            statisticMosaic = new Mosaic(dataManagement.getMosaicWidth(),
                    dataManagement.getMosaicHeight(), dataManagement);
            statisticMosaic.setMosaic(dataManagement.mosaicCopy());
        }

        // now the real algorithm
        moldingOptimisation = new MoldingOptimizer(dataProcessing, calculation,
                tilingInfo);
        moldingOptimisation.tiling(dataManagement.getMosaicWidth(),
                dataManagement.getMosaicHeight(),
                dataManagement.getCurrentConfiguration(),
                dataManagement.getMosaicInstance(), false);
        moldingOptimisation = null;

        // If the user chooses the improved molding optimisation:
        // Statistic evaluation with molding optimisation (without
        // improvements)
        // else statistic evaluation with element size optimisation
        if (statisticOutput) {
            dataProcessing.setInfo(outputDataProcessing1 + ":",
                    DataProcessor.SHOW_IN_BOTH);

            if (tilingInfo.isEmpty()
                    || ((String) tilingInfo.elementAt(0)).equals("no")) {
                // Element size optimisation
                var elementSizeOptimisation = new ElementSizeOptimizer(
                        dataProcessing, calculation);
                dataProcessing
                        .setElementSizeOptimisation(elementSizeOptimisation);
                elementSizeOptimisation.tiling(dataManagement.getMosaicWidth(),
                        dataManagement.getMosaicHeight(),
                        dataManagement.getCurrentConfiguration(),
                        statisticMosaic, true);
                elementSizeOptimisation = null;
            } else {
                // Molding optimisation (without improvements)
                final Vector<Object> no = new Vector<>();
                no.add("no");
                moldingOptimisation = new MoldingOptimizer(dataProcessing,
                        calculation, no);
                moldingOptimisation.tiling(dataManagement.getMosaicWidth(),
                        dataManagement.getMosaicHeight(),
                        dataManagement.getCurrentConfiguration(),
                        statisticMosaic, true);
                moldingOptimisation = null;
            }
        }
    }

    /**
     * Perform costs optimization.
     *
     * @param outputDataProcessing1 Output data processing 1 text.
     */
    public void doCostsOptimization(final String outputDataProcessing1) {
        if (statisticOutput) {
            statisticMosaic = new Mosaic(dataManagement.getMosaicWidth(),
                    dataManagement.getMosaicHeight(), dataManagement);
            statisticMosaic.setMosaic(dataManagement.mosaicCopy());
        }

        costsOptimisation = new CostsOptimizer(dataProcessing, calculation);
        costsOptimisation.tiling(dataManagement.getMosaicWidth(),
                dataManagement.getMosaicHeight(),
                dataManagement.getCurrentConfiguration(),
                dataManagement.getMosaicInstance(), false);
        costsOptimisation = null;

        // Statistic evaluation with element size optimisation
        if (statisticOutput) {
            dataProcessing.setInfo(outputDataProcessing1 + ":",
                    DataProcessor.SHOW_IN_BOTH);
            final var elementSizeOptimisation = new ElementSizeOptimizer(
                    dataProcessing, calculation);
            dataProcessing.setElementSizeOptimisation(elementSizeOptimisation);
            elementSizeOptimisation.tiling(dataManagement.getMosaicWidth(),
                    dataManagement.getMosaicHeight(),
                    dataManagement.getCurrentConfiguration(), statisticMosaic,
                    true);
            dataProcessing.setElementSizeOptimisation(null);
        }
    }

    /**
     * Perform stability optimization.
     *
     * @param outputDataProcessing5 Output data processing 5 text.
     * @param outputDataProcessing6 Output data processing 6 text.
     */
    public void doStabilityOptimization(final String outputDataProcessing5,
            final String outputDataProcessing6) {
        // Only print statistic if improvements are selected
        if (statisticOutput) {
            // Copy the mosaic for statistic evaluation
            if ((Boolean) tilingInfo.elementAt(1)
                    && !((Boolean) tilingInfo.elementAt(2))) {
                statisticMosaic = new Mosaic(dataManagement.getMosaicWidth(),
                        dataManagement.getMosaicHeight(), dataManagement);
                statisticMosaic.setMosaic(dataManagement.mosaicCopy());
            } else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            // deactivate statistic of normal
                            // algorithm (without improvements) is
                            // selected
                            mainWindow.errorDialog(outputDataProcessing5
                                    + "\n\r" + outputDataProcessing6 + ".");
                            mainWindow.getGuiPanelOptions2()
                                    .disableStatisticButton();
                        }
                    });
                } catch (final Exception e) {
                    System.out.println(e.toString());
                }
            }
        }

        stabilityOptimisation = new StabilityOptimizer(dataProcessing,
                calculation, tilingInfo);
        stabilityOptimisation.tiling(dataManagement.getMosaicWidth(),
                dataManagement.getMosaicHeight(),
                dataManagement.getCurrentConfiguration(),
                dataManagement.getMosaicInstance(), false);
        stabilityOptimisation = null;

        // statistic only for whole image improvements
        if (statisticOutput && !((Boolean) tilingInfo.elementAt(2))) {
            final Vector<Object> noOptimisation = new Vector<>();
            noOptimisation.add(0);
            noOptimisation.add(false);
            noOptimisation.add(false);
            noOptimisation
                    .add((Integer) tilingInfo.elementAt(MAX_GAP_HEIGHT_INDEX));
            stabilityOptimisation = new StabilityOptimizer(dataProcessing,
                    calculation, noOptimisation);
            stabilityOptimisation.tiling(dataManagement.getMosaicWidth(),
                    dataManagement.getMosaicHeight(),
                    dataManagement.getCurrentConfiguration(), statisticMosaic,
                    true);
            stabilityOptimisation = null;
        }
    }

    /**
     * Perform basic elements only processing.
     */
    public void doBasicElementsOnly() {
        basicElementsOnly = new BasicElementsOnlyTiler(dataProcessing,
                calculation);
        basicElementsOnly.tiling(dataManagement.getMosaicWidth(),
                dataManagement.getMosaicHeight(),
                dataManagement.getCurrentConfiguration(),
                dataManagement.getMosaicInstance(), false);
        basicElementsOnly = null;
        // no statistic evaluation
    }

}
