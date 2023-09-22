package pictobrick.model;

import java.awt.Color;
import java.io.Serializable;

/**
 * class: ColorObject. layer: DataManagement (three tier architecture).
 * description: contains all information about a color-object.
 *
 * @author Tobias Reichling
 */
public class ColorObject implements Serializable {
    /** Name of color? */
    private String name;
    /** Color value. */
    private Color rgb;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     */
    public ColorObject() {
    }

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param colorName
     * @param rgbValue
     */
    public ColorObject(final String colorName, final Color rgbValue) {
        this.name = colorName;
        this.rgb = rgbValue;
    }

    /**
     * Sets the name of the color-object.
     *
     * @author Tobias Reichling
     * @param colorName
     */
    public void setName(final String colorName) {
        this.name = colorName;
    }

    /**
     * Sets the color values of the rgb-color-object.
     *
     * @author Tobias Reichling
     * @param rgbValue
     */
    public void setRGB(final Color rgbValue) {
        this.rgb = rgbValue;
    }

    /**
     * Returns the name of the color-object.
     *
     * @author Tobias Reichling
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the rgb-object of the color-object.
     *
     * @author Tobias Reichling
     * @return rgb
     */
    public Color getRGB() {
        return rgb;
    }
}
