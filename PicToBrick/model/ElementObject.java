package pictobrick.model;

import java.io.Serializable;

/**
 * Contains all information about an element.
 *
 * @author Tobias Reichling
 */
public class ElementObject implements Serializable {
    /** Element width. */
    private int width;
    /** Element height. */
    private int height;
    /** Element matrix. */
    private boolean[][] matrix;
    /** Element name. */
    private String name;
    /** Element stability. */
    private int stability;
    /** Elements costs in 100ths of Euros. */
    private int costs;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     */
    public ElementObject() {
    }

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param elementName
     * @param elementWidth
     * @param elementHeight
     * @param elementMatrix
     */
    public ElementObject(final String elementName, final int elementWidth,
            final int elementHeight, final boolean[][] elementMatrix) {
        this.width = elementWidth;
        this.height = elementHeight;
        this.matrix = elementMatrix;
        this.name = elementName;
        this.stability = 1;
        this.costs = 1;
    }

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param elementName
     * @param elementWidth
     * @param elementHeight
     * @param elementMatrix
     * @param elementStability
     * @param elementCosts
     */
    public ElementObject(final String elementName, final int elementWidth,
            final int elementHeight, final boolean[][] elementMatrix,
            final int elementStability, final int elementCosts) {
        this.width = elementWidth;
        this.height = elementHeight;
        this.matrix = elementMatrix;
        this.name = elementName;
        this.stability = elementStability;
        this.costs = elementCosts;
    }

    /**
     * Returns the width.
     *
     * @author Tobias Reichling
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the name.
     *
     * @author Tobias Reichling
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the height.
     *
     * @author Tobias Reichling
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the matrix.
     *
     * @author Tobias Reichling
     * @return msatrix
     */
    public boolean[][] getMatrix() {
        return matrix;
    }

    /**
     * Returns the stability.
     *
     * @author Tobias Reichling
     * @return stability
     */
    public int getStability() {
        return stability;
    }

    /**
     * Returns the costs.
     *
     * @author Tobias Reichling
     * @return costs
     */
    public int getCosts() {
        return costs;
    }

    /**
     * Sets the name.
     *
     * @author Tobias Reichling
     * @param elementName
     */
    public void setName(final String elementName) {
        this.name = elementName;
    }

    /**
     * Sets the width.
     *
     * @author Tobias Reichling
     * @param elementWidth
     */
    public void setWidth(final int elementWidth) {
        this.width = elementWidth;
    }

    /**
     * Sets the height.
     *
     * @author Tobias Reichling
     * @param elementHeight
     */
    public void setHeight(final int elementHeight) {
        this.height = elementHeight;
    }

    /**
     * Sets the matrix.
     *
     * @author Tobias Reichling
     * @param elementMatrix
     */
    public void setMatrix(final boolean[][] elementMatrix) {
        this.matrix = elementMatrix;
    }

    /**
     * Sets the stability.
     *
     * @author Tobias Reichling
     * @param elementStability
     */
    public void setStability(final int elementStability) {
        this.stability = elementStability;
    }

    /**
     * Sets the costs.
     *
     * @author Tobias Reichling
     * @param elementCosts
     */
    public void setCosts(final int elementCosts) {
        this.costs = elementCosts;
    }
}
