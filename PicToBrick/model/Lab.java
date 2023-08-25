package PicToBrick.model;

/**
 * class:            Lab
 * layer:            DataProcessing (three tier architecture)
 * description:      Encapsulation of CIE-LAB color values
 * @author           Tobias Reichling
 */
public class Lab {

	private double l,a,b;

	/**
	 * method:           Lab
	 * description:      constructor
	 * @author           Tobias Reichling
	 */
	public Lab(){
		this.l = 0.0;
		this.a = 0.0;
		this.b = 0.0;
	}

	/**
	 * method:           Lab
	 * description:      constructor
	 * @author           Tobias Reichling
	 * @param            l = l-value LAB
	 * @param            a = a-value LAB
	 * @param            b = b-value LAB
	 */
	public Lab(double l, double a, double b){
		this.l = l;
		this.a = a;
		this.b = b;
	}

	/**
	 * method:         setL
	 * description:    sets the l value
	 * @author         Tobias Reichling
	 * @param          l = l value
	 */
	public void setL(double l){
		this.l = l;
	}

	/**
	 * method:         getL
	 * description:    returns the l value
	 * @author         Tobias Reichling
	 * @return         l = l value
	 */
	public double getL(){
		return this.l;
	}

	/**
	 * method:         setA
	 * description:    sets the a value
	 * @author         Tobias Reichling
	 * @param          a = a value
	 */
	public void setA(double a){
		this.a = a;
	}

	/**
	 * method:         getA
	 * description:    returns the a value
	 * @author         Tobias Reichling
	 * @return         a = a value
	 */
	public double getA(){
		return this.a;
	}

	/**
	 * method:         setB
	 * description:    sets the b value
	 * @author         Tobias Reichling
	 * @param          b = b value
	 */
	public void setB(double b){
		this.b = b;
	}

	/**
	 * method:         getB
	 * description:    returns the b value
	 * @author         Tobias Reichling
	 * @return         b = b value
	 */
	public double getB(){
		return this.b;
	}

}
