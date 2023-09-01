package pictobrick.model;

import java.awt.Color;
import java.io.*;

/**
 * class: ColorObject.
 * layer: DataManagement (three tier architecture).
 * description: contains all information about a color-object.
 *
 * @author Tobias Reichling
 */
public class ColorObject
		implements Serializable {

	String name;
	Color rgb;

	/**
	 * method: ColorObject
	 * description: constructor
	 *
	 * @author Tobias Reichling
	 */
	public ColorObject() {
	}

	/**
	 * method: ColorObject
	 * description: constructor
	 *
	 * @author Tobias Reichling
	 * @param name
	 * @param rgb
	 */
	public ColorObject(final String name, final Color rgb) {
		this.name = name;
		this.rgb = rgb;
	}

	/**
	 * method: setName
	 * description: sets the name of the color-object
	 *
	 * @author Tobias Reichling
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * method: setRGB
	 * description: sets the color values of the rgb-color-object
	 *
	 * @author Tobias Reichling
	 * @param rgb
	 */
	public void setRGB(final Color rgb) {
		this.rgb = rgb;
	}

	/**
	 * method: getName
	 * description: returns the name of the color-object
	 *
	 * @author Tobias Reichling
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * method: getRGB
	 * description: returns the rgb-object of the color-object
	 *
	 * @author Tobias Reichling
	 * @return rgb
	 */
	public Color getRGB() {
		return rgb;
	}
}
