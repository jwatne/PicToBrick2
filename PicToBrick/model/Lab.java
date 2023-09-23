package pictobrick.model;

/**
 * Encapsulation of CIE-LAB color values.
 *
 * @author Tobias Reichling
 */
public class Lab {
    /** l-value LAB. */
    private double l;
    /** a-value LAB. */
    private double a;
    /** b-value LAB. */
    private double b;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     */
    public Lab() {
        this.l = 0.0;
        this.a = 0.0;
        this.b = 0.0;
    }

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param labL = l-value LAB
     * @param labA = a-value LAB
     * @param labB = b-value LAB
     */
    public Lab(final double labL, final double labA, final double labB) {
        this.l = labL;
        this.a = labA;
        this.b = labB;
    }

    /**
     * Sets the l value.
     *
     * @author Tobias Reichling
     * @param labL = l value
     */
    public void setL(final double labL) {
        this.l = labL;
    }

    /**
     * Returns the l value.
     *
     * @author Tobias Reichling
     * @return l = l value
     */
    public double getL() {
        return this.l;
    }

    /**
     * Sets the a value.
     *
     * @author Tobias Reichling
     * @param labA = a value
     */
    public void setA(final double labA) {
        this.a = labA;
    }

    /**
     * Returns the a value.
     *
     * @author Tobias Reichling
     * @return a = a value
     */
    public double getA() {
        return this.a;
    }

    /**
     * Sets the b value.
     *
     * @author Tobias Reichling
     * @param labB = b value
     */
    public void setB(final double labB) {
        this.b = labB;
    }

    /**
     * Returns the b value.
     *
     * @author Tobias Reichling
     * @return b = b value
     */
    public double getB() {
        return this.b;
    }

}
