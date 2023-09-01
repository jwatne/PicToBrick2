package pictobrick.service;

import pictobrick.model.Configuration;
import pictobrick.model.Mosaic;

/**
 * interface:        Tiling
 * layer:            DataProcessing (three tier architecture)
 * description:      interface for all tiling clases
 * @author           Tobias Reichling
 */
public interface Tiler {

	/**
	 * method:           tiling
	 * description:      tiling the mosaic
	 * @author           Tobias Reichling
	 * @param            mosaic width
	 * @param            mosaic height
	 * @param            configuration
	 * @param            mosaic with color information
	 * @param            statistic (boolean)
	 * @return           mosaic with color information
	 */
	public Mosaic tiling(int mosaicWidth,
            int mosaicHeight,
            Configuration configuration,
            Mosaic mosaic,
            boolean statistic);
}
