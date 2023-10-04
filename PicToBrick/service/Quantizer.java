package pictobrick.service;

import java.awt.image.BufferedImage;

import pictobrick.model.Configuration;
import pictobrick.model.Mosaic;

/**
 * Interface for all quantization classes.
 *
 * @author Adrian Schuetz
 */
public interface Quantizer {
    /** Starting minimum difference value for determining best fit region. */
    double STARTING_MIN_DIFFERENCE = 500.0;

    /**
     * Color matching.
     *
     * @author Adrian Schuetz
     * @param image
     * @param mosaicWidth
     * @param mosaicHeight
     * @param configuration
     * @param mosaic
     * @return mosaic with color information
     */
    Mosaic quantisation(BufferedImage image, int mosaicWidth, int mosaicHeight,
            Configuration configuration, Mosaic mosaic);
}
