package pictobrick.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Contains all information about a single configuration and offers related
 * methods.
 *
 * @author Tobias Reichling
 */
public class Configuration implements Serializable {
    /** Colors. */
    private Vector<ColorObject> colors;
    /** Elements. */
    private Vector<ElementObject> elements;
    /** Name of the configuration. */
    private String name;
    /** The name of the basis element. */
    private String basisName;
    /** The width of the basis element (ratio). */
    private int basisWidth;
    /** The height of the basis element (ratio). */
    private int basisHeight;
    /** Costs. */
    private int basisCosts;
    /** Stability. */
    private int basisStability;
    /** The material index. */
    private int material;
    /** The width of the basis element in millimeters. */
    private double basisWidthMM;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     */
    public Configuration() {
    }

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param configurationName name configuration
     * @param basis             basis element, consisting of name, width,
     *                          height, and width in mm.
     * @param costs             costs basisElement
     * @param stability         stability basisElement
     * @param materialIndex     material index
     */
    public Configuration(final String configurationName,
            final BasisElement basis, final int stability, final int costs,
            final int materialIndex) {
        this.name = configurationName;
        this.basisName = basis.basisName();
        this.basisWidth = basis.basisWidth();
        this.basisHeight = basis.basisHeight();
        this.basisWidthMM = basis.basisWidthMM();
        this.basisCosts = costs;
        this.basisStability = stability;
        this.material = materialIndex;
        colors = new Vector<>();
        elements = new Vector<>();
        final boolean[][] basisElement = {{true}};
        setElement(basis.basisName(), 1, 1, basisElement, stability, costs);
    }

    /**
     * Returns the stability.
     *
     * @author Tobias Reichling
     * @return stability
     */
    public int getBasisStability() {
        return this.basisStability;
    }

    /**
     * Returns the costs.
     *
     * @author Tobias Reichling
     * @return costs
     */
    public int getBasisCosts() {
        return this.basisCosts;
    }

    /**
     * Returns the quantity of colors added to the quantity of elements.
     *
     * @author Tobias Reichling
     * @return quantity of colors added to the quantity of elements
     */
    public int getQuantityColorsAndElements() {
        return (colors.size() + elements.size());
    }

    /**
     * Returns the quantity of colors.
     *
     * @author Tobias Reichling
     * @return quantity of colors
     */
    public int getQuantityColors() {
        return colors.size();
    }

    /**
     * Returns the material index.
     *
     * @author Tobias Reichling
     * @return material
     */
    public int getMaterial() {
        return this.material;
    }

    /**
     * Sets the material index.
     *
     * @author Tobias Reichling
     * @param materialIndex
     */
    public void setMaterial(final int materialIndex) {
        this.material = materialIndex;
    }

    /**
     * Returns the name of the basis element.
     *
     * @author Tobias Reichling
     * @return name
     */
    public String getBasisName() {
        return this.basisName;
    }

    /**
     * Returns the name of the configuration.
     *
     * @author Tobias Reichling
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the configuration.
     *
     * @author Tobias Reichling
     * @param configurationName
     */
    public void setName(final String configurationName) {
        this.name = configurationName;
    }

    /**
     * Returns the width of the basis element (ratio).
     *
     * @author Tobias Reichling
     * @return width
     */
    public int getBasisWidth() {
        return this.basisWidth;
    }

    /**
     * Returns the height of the basis element (ratio).
     *
     * @author Tobias Reichling
     * @return height
     */
    public int getBasisHeight() {
        return this.basisHeight;
    }

    /**
     * Returns the width of the basis element (millimeter).
     *
     * @author Tobias Reichling
     * @return width millimeter
     */
    public double getBasisWidthMM() {
        return this.basisWidthMM;
    }

    /**
     * Adds a new color object (consisting of single values r,g,b).
     *
     * @author Tobias Reichling
     * @param colorName name of the color
     * @param r         color value red
     * @param g         color value green
     * @param b         color value blue
     * @return true or false
     */
    public boolean setColor(final String colorName, final int r, final int g,
            final int b) {
        final Color rgb = new Color(r, g, b);
        return setColor(colorName, rgb);
    }

    /**
     * Adds a new color object.
     *
     * @author Tobias Reichling
     * @param color object
     */
    public void setColor(final ColorObject color) {
        colors.addElement(color);
    }

    /**
     * Adds a new color object.
     *
     * @author Tobias Reichling
     * @param colorName name of the color
     * @param rgb       color object
     * @return true or false
     */
    public boolean setColor(final String colorName, final Color rgb) {
        ColorObject color;
        final ColorObject colorNew = new ColorObject(colorName, rgb);

        for (final Enumeration<ColorObject> enumColors = colors
                .elements(); enumColors.hasMoreElements();) {
            color = (ColorObject) enumColors.nextElement();

            if (color.getName().equals(colorName)) {
                return false;
            }

            if (color.getRGB().equals(rgb)) {
                return false;
            }
        }

        colors.addElement(colorNew);
        return true;
    }

    /**
     * Deletes a color object if available.
     *
     * @author Tobias Reichling
     * @param colorName name of the color
     * @return true or false
     */
    public boolean deleteColor(final String colorName) {
        ColorObject color;
        int counter = 0;

        for (final Enumeration<ColorObject> enumColors = colors
                .elements(); enumColors.hasMoreElements();) {
            color = (ColorObject) enumColors.nextElement();

            if (color.getName().equals(colorName)) {
                colors.remove(counter);
                return true;
            }

            counter++;
        }

        return false;
    }

    /**
     * Returns a color object if available.
     *
     * @author Tobias Reichling
     * @param colorName name of the color object
     * @return color object or null
     */
    public ColorObject getColor(final String colorName) {
        ColorObject color;

        for (final Enumeration<ColorObject> enumColors = colors
                .elements(); enumColors.hasMoreElements();) {
            color = (ColorObject) enumColors.nextElement();

            if (color.getName().equals(colorName)) {
                return color;
            }
        }

        return null;
    }

    /**
     * Returns an enumeration of all colors.
     *
     * @author Tobias Reichling
     * @return enumeration
     */
    public Enumeration<ColorObject> getAllColors() {
        return colors.elements();
    }

    /**
     * Sets a new element.
     *
     * @author Tobias Reichling
     * @param elementName
     * @param width
     * @param height
     * @param matrix      (matrix: true if element covers the matrix field,
     *                    false otherwise)
     * @param stability
     * @param costs
     * @return true or false
     */
    public boolean setElement(final String elementName, final int width,
            final int height, final boolean[][] matrix, final int stability,
            final int costs) {
        ElementObject element;
        final ElementObject elementNew = new ElementObject(elementName, width,
                height, matrix, stability, costs);

        for (final Enumeration<ElementObject> enumElements = elements
                .elements(); enumElements.hasMoreElements();) {
            element = (ElementObject) enumElements.nextElement();

            if (element.getName().equals(elementName)) {
                return false;
            }

            if ((element.getWidth() == width)
                    && (element.getHeight() == height)) {
                final boolean[][] matrixNew = element.getMatrix();
                boolean difference = false;

                for (int row = 0; row < height; row++) {
                    for (int column = 0; column < width; column++) {
                        if (!(matrixNew[row][column] == matrix[row][column])) {
                            difference = true;
                        }
                    }
                }

                if (!difference) {
                    return false;
                }
            }
        }

        elements.addElement(elementNew);
        return true;
    }

    /**
     * Sets a new element object.
     *
     * @author Tobias Reichling
     * @param elementName
     * @param width
     * @param height
     * @param matrix      (matrix: true if element covers the matrix field,
     *                    false otherwise)
     * @return true or false
     */
    public boolean setElement(final String elementName, final int width,
            final int height, final boolean[][] matrix) {
        ElementObject element;
        final ElementObject elementNew = new ElementObject(elementName, width,
                height, matrix);

        for (final Enumeration<ElementObject> enumElements = elements
                .elements(); enumElements.hasMoreElements();) {
            element = (ElementObject) enumElements.nextElement();

            if (element.getName().equals(elementName)) {
                return false;
            }

            if ((element.getWidth() == width) && (element.getHeight() == height)
                    && (element.getMatrix() == matrix)) {
                return false;
            }
        }

        elements.addElement(elementNew);
        return true;
    }

    /**
     * Sets a new Element.
     *
     * @author Tobias Reichling
     * @param element
     */
    public void setElement(final ElementObject element) {
        elements.addElement(element);
    }

    /**
     * Deletes an element if available.
     *
     * @author Tobias Reichling
     * @param elementName
     * @return true or false
     */
    public boolean deleteElement(final String elementName) {
        ElementObject element;
        int counter = 0;

        for (final Enumeration<ElementObject> enumElements = elements
                .elements(); enumElements.hasMoreElements();) {
            element = (ElementObject) enumElements.nextElement();

            if (element.getName().equals(elementName)) {
                elements.remove(counter);
                return true;
            }

            counter++;
        }

        return false;
    }

    /**
     * Returns an element object.
     *
     * @author Tobias Reichling
     * @param elementName
     * @return element object
     */
    public ElementObject getElement(final String elementName) {
        ElementObject element;

        for (final Enumeration<ElementObject> enumElements = elements
                .elements(); enumElements.hasMoreElements();) {
            element = (ElementObject) enumElements.nextElement();

            if (element.getName().equals(elementName)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Returns an enumeration of all elements.
     *
     * @author Tobias Reichling
     * @return enumeration
     */
    public Enumeration<ElementObject> getAllElements() {
        return elements.elements();
    }

}
