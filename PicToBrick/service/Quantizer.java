package pictobrick.service;

import java.awt.image.*;

import pictobrick.model.Configuration;
import pictobrick.model.Mosaic;

/**
 * interface:        Quantizer
 * layer:            DataProcessing (three tier architecture)
 * description:      interface for all quantisation classes
 * @author           Adrian Schuetz
 */
public interface Quantizer {

	/**
	 * method:           quantisation
	 * description:      color matching
	 * @author           Adrian Schuetz
	 * @param            image
	 * @param            mosaic width
	 * @param            mosaic height
	 * @param            configuration
	 * @param            mosaic empty
	 * @return           mosaic with color information
	 */
	public Mosaic quantisation(BufferedImage image,
			                   int mosaicWidth,
			                   int mosaicHeight,
			                   Configuration configuration,
			                   Mosaic mosaic);
}
