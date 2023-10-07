package pictobrick.service;

import pictobrick.model.Configuration;
import pictobrick.model.Mosaic;

/**
 * Interface for all tiling clases.
 *
 * @author Tobias Reichling
 */
public interface Tiler {

    /** Divisor (and mod) of recolored values. */
    int RECOLORED_DIVISOR = 10;

    /**
     * Tiling the mosaic.
     *
     * @author Tobias Reichling
     * @param mosaicWidth
     * @param mosaicHeight
     * @param configuration
     * @param mosaic        with color information
     * @param statistic     (boolean)
     * @return mosaic with color information
     */
    Mosaic tiling(int mosaicWidth, int mosaicHeight,
            Configuration configuration, Mosaic mosaic, boolean statistic);
}
