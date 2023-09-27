package pictobrick.service;

import javax.swing.SwingUtilities;

import pictobrick.model.Configuration;
import pictobrick.model.Mosaic;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Tiling with basis elements.
 *
 * @author Adrian Schuetz
 */
public class BasicElementsOnlyTiler implements Tiler {
    /** 100% int value for progress bar values. */
    private static final int ONE_HUNDRED_PERCENT = 100;
    /** Multiplier applied to ratio to get percentage value. */
    private static final double PERCENTAGE_MULTIPLIER = 100.0;
    /** Data processing. */
    private final DataProcessor dataProcessing;
    /** Rows in image. */
    private int rows = 0;
    /** Percentage tiled (0-100). */
    private int percent = 0;
    /** <code>true</code> if calculating statistics. */
    private boolean statisticOutput;
    /** The row being processed. */
    private int colorRow;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param dataProcessor
     * @param calculation
     */
    public BasicElementsOnlyTiler(final DataProcessor dataProcessor,
            final Calculator calculation) {
        this.dataProcessing = dataProcessor;
    }

    /**
     * Tile every pixel with a basis element.
     *
     * @author Adrian Schuetz
     * @param mosaicWidth
     * @param mosaicHeight
     * @param configuration
     * @param statistic
     * @param mosaic        the Mosaic being processed.
     * @return mosaic
     */
    public Mosaic tiling(final int mosaicWidth, final int mosaicHeight,
            final Configuration configuration, final Mosaic mosaic,
            final boolean statistic) {
        this.statisticOutput = statistic;
        rows = mosaicHeight;
        percent = 0;

        for (colorRow = 0; colorRow < mosaicHeight; colorRow++) {
            // assign progress bar controls to the GUI thread
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        percent = (int) ((PERCENTAGE_MULTIPLIER / rows)
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
            } catch (final Exception e) {
                System.out.println(e.toString());
            }

            for (int colorCol = 0; colorCol < mosaicWidth; colorCol++) {
                mosaic.setElement(colorRow, colorCol,
                        configuration.getBasisName(), true);
            }
        }

        // assign progress bar controls to the GUI thread
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
