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
    /** 3/16 factor value. */
    double FACTOR_3_16 = 3.0 / 16.0;
    /** 1/16 factor value. */
    double FACTOR_1_16 = 1.0 / 16.0;
    /** 5/16 factor value. */
    double FACTOR_5_16 = 5.0 / 16.0;
    /** 7/16 factor value. */
    double FACTOR_7_16 = 7.0 / 16.0;

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
