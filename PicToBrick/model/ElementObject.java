package pictobrick.model;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Contains all information about an element.
 *
 * @author Tobias Reichling
 */
public class ElementObject implements Serializable {
    /** Height of a LEGO brick (vs. 1 for a tile). */
    private static final int BRICK_HEIGHT = 3;
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
     * Init a hash with same elements of different height (element with height 1
     * | same element with height 3).
     *
     * @author Adrian Schuetz
     * @param configuration
     * @return Hash
     */
    public static Hashtable<String, String> hashInit(
            final Configuration configuration) {
        final Hashtable<String, String> hash = new Hashtable<>();
        ElementObject testElement1;
        ElementObject testElement2;
        final Vector<ElementObject> heightOne = new Vector<>();
        final Vector<ElementObject> heightThree = new Vector<>();
        // Enumeration of all elements (split by height)
        // vector1: elements with height = 1
        // vector2: elements with height = 3
        final Enumeration<ElementObject> elements = configuration
                .getAllElements();

        while (elements.hasMoreElements()) {
            testElement1 = (ElementObject) elements.nextElement();

            if (testElement1.getHeight() == 1) {
                heightOne.add(testElement1);
            } else {
                heightThree.add(testElement1);
            }
        }

        // Enumeration of all elements with height = 1
        // check if there is a matching element with height = 3
        // -> put in the hash
        final Enumeration<ElementObject> heightOneEnum = heightOne.elements();
        Enumeration<ElementObject> heightThreeEnum;

        while (heightOneEnum.hasMoreElements()) {
            testElement1 = (ElementObject) heightOneEnum.nextElement();
            heightThreeEnum = heightThree.elements();

            while (heightThreeEnum.hasMoreElements()) {
                testElement2 = (ElementObject) heightThreeEnum.nextElement();

                if (testElement1.getWidth() == testElement2.getWidth()) {
                    hash.put(testElement1.getName(), testElement2.getName());
                }
            }
        }

        return hash;
    }

    /**
     * Sorts the element Vector by stability.
     *
     * @author Adrian Schuetz
     * @param elementsUnsorted
     * @return elementsSorted
     */
    public static Vector<ElementObject> sortElementsByStability(
            final Enumeration<ElementObject> elementsUnsorted) {
        int stabi = 0;
        boolean included = false;
        int position;
        ElementObject supportElement;
        final Vector<ElementObject> elementsSorted = new Vector<>();

        // Elements sorted by stability
        while (elementsUnsorted.hasMoreElements()) {
            supportElement = ((ElementObject) elementsUnsorted.nextElement());

            if (elementsSorted.size() == 0) {
                elementsSorted.add(supportElement);
            } else {
                stabi = supportElement.getStability();
                position = 0;
                included = false;
                final Enumeration<ElementObject> supportEnum = elementsSorted
                        .elements();

                while (supportEnum.hasMoreElements() && !included) {
                    final var anotherElement = (ElementObject) supportEnum
                            .nextElement();

                    if (stabi >= anotherElement.getStability()) {
                        elementsSorted.add(position, supportElement);
                        included = true;
                    } else {
                        position++;
                    }
                }

                if (!included) {
                    elementsSorted.add(supportElement);
                }
            }
        }

        return elementsSorted;
    }

    /**
     * Checks if there is a valid configuration elements must be rectangles,
     * without holes and the height must be 1 or 3 (Standard-Lego).
     *
     * @author Adrian Schuetz
     * @param configuration
     * @return true, false
     */
    public static boolean consistencyCheck(final Configuration configuration) {
        ElementObject testElement;

        // check all elements
        for (final Enumeration<ElementObject> currentEnum = configuration
                .getAllElements(); currentEnum.hasMoreElements();) {
            testElement = currentEnum.nextElement();

            if (!(testElement.getHeight() == 1
                    || testElement.getHeight() == BRICK_HEIGHT)) {
                return false;
            } else {
                // if element is not holohedral, false
                for (int i = 0; i < testElement.getHeight(); i++) {
                    for (int j = 0; j < testElement.getWidth(); j++) {
                        if (!testElement.getMatrix()[i][j]) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Checks if there is a valid configuration for the optimisation (element
     * width = 2 and height = 1).
     *
     * @author Adrian Schuetz
     * @param configuration
     * @return true, false
     */
    public static boolean consistencyCheckOptimisation(
            final Configuration configuration) {
        ElementObject testElement;

        // check all elements
        for (final Enumeration<ElementObject> currentEnum = configuration
                .getAllElements(); currentEnum.hasMoreElements();) {
            testElement = currentEnum.nextElement();

            if ((testElement.getHeight() == 1 && testElement.getWidth() == 2)) {
                return true;
            }
        }

        return false;
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
