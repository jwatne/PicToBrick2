package PicToBrick.service;

import java.awt.image.*;

import PicToBrick.model.Configuration;
import PicToBrick.model.Mosaic;

/**
 * interface:        Quantisation
 * layer:            DataProcessing (three tier architecture)
 * description:      interface for all quantisation classes
 * @author           Adrian Schuetz
 */
public interface Quantisation {

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
