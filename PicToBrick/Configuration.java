package PicToBrick;

import java.awt.Color;
import java.util.*;
import java.io.*;

/**
 * class:          Configuration
 * Schicht:        DataManagement (three tier architecture)
 * description:    contains all information about a single configuration
 *                 and offers related methods
 * @author         Tobias Reichling
 */
public class Configuration 
implements Serializable{

	private Vector colors, elements;
	private String name;
	private String basisName;
	private int basisWidth, basisHeight, basisCosts, basisStability;
	private int material;
	private double basisWidthMM;
	
	/**
	 * method:         Configuration
	 * description:    constructor
	 * @author         Tobias Reichling
	 */
	public Configuration(){}
	
	/**
	 * method:         Configuration
	 * description:    constructor
	 * @author         Tobias Reichling
	 * @param          name               name configuration
	 * @param          basisName          name basisElement
	 * @param          basisWidth         width basisElement (ratio)
	 * @param          basisHeight        height basisElement (ratio)
	 * @param          basisWidthMM       width basisElement (millimeter)
	 * @param          basisCosts         costs basisElement
	 * @param          basisStability     stability basisElement
	 * @param          material           material index
	 */
	public Configuration(String name, String basisName, int basisWidth, int basisHeight, double basisWidthMM, int basisStability, int basisCosts, int material){
		this.name = name;
		this.basisName = basisName;
		this.basisWidth = basisWidth;
		this.basisHeight = basisHeight;
		this.basisWidthMM = basisWidthMM;
		this.basisCosts = basisCosts;
		this.basisStability = basisStability;
		this.material = material;
		colors = new Vector();
		elements = new Vector();
		boolean[][] basisElement = {{true}};
		setElement(basisName,1,1,basisElement,basisStability,basisCosts);
	}
	
	/**
	 * method:         getBasisStability
	 * description:    returns the stability
	 * @author         Tobias Reichling
	 * @return         stability
	 */
	public int getBasisStability(){
		return this.basisStability;
	}
	
	/**
	 * method:         getBasisCosts
	 * description:    returns the costs
	 * @author         Tobias Reichling
	 * @return         costs
	 */
	public int getBasisCosts(){
		return this.basisCosts;
	}
	
	/**
	 * method:         getQuantityColorsAndElements
	 * description:    returns the quantity of colors added to the quantity of elements
	 * @author         Tobias Reichling
	 * @return         quantity of colors added to the quantity of elements
	 */
	public int getQuantityColorsAndElements(){
		return (colors.size()+elements.size());
	}
	
	/**
	 * method:         getQuantityColors
	 * description:    returns the quantity of colors
	 * @author         Tobias Reichling
	 * @return         quantity of colors
	 */
	public int getQuantityColors(){
		return colors.size();
	}
	
	/**
	 * method:         getMaterial
	 * description:    returns the material index
	 * @author         Tobias Reichling
	 * @return         material
	 */
	public int getMaterial(){
		return this.material;
	}
	
	/**
	 * method:         setMaterial
	 * description:    Sets the material index
	 * @author         Tobias Reichling
	 * @param          material
	 */
	public void setMaterial(int material){
		this.material = material;
	}
	
	/**
	 * method:         getBasisName
	 * description:    returns the name of the basis element
	 * @author         Tobias Reichling
	 * @return         name
	 */
	public String getBasisName(){
		return this.basisName;
	}
	
	/**
	 * method:         getName
	 * description:    returns the name of the configuration
	 * @author         Tobias Reichling
	 * @return         name
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * method:         setName
	 * description:    sets the name of the configuration
	 * @author         Tobias Reichling
	 * @param          name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * method:         getBasisWidth
	 * description:    returns the width of the basis element (ratio)
	 * @author         Tobias Reichling
	 * @return         width
	 */
	public int getBasisWidth(){
		return this.basisWidth;
	}
	
	/**
	 * method:         getBasisHeight
	 * description:    returns the height of the basis element (ratio)
	 * @author         Tobias Reichling
	 * @return         height
	 */
	public int getBasisHeight(){
		return this.basisHeight;
	}
	
	/**
	 * method:         getBasisWidhtMM
	 * description:    returns the width of the basis element (millimeter)
	 * @author         Tobias Reichling
	 * @return         width millimeter
	 */
	public double getBasisWidthMM(){
		return this.basisWidthMM;
	}
	
	/**
	 * method:         setColor
	 * description:    adds a new color object (consisting of single values r,g,b)
	 * @author         Tobias Reichling
	 * @param          name  name of the color
	 * @param          r     color value red
	 * @param          g     color value green
	 * @param          b     color value blue
	 * @return         true or false
	 */
	public boolean setColor(String name, int r, int g, int b){
		Color rgb = new Color(r,g,b);
		return setColor(name, rgb);
	}
	
	/**
	 * method:         setColor
	 * description:    adds a new color object
	 * @author         Tobias Reichling
	 * @param          color object
	 */
	public void setColor(ColorObject color){
		colors.addElement(color);
	}
	
	/**
	 * method:         setColor
	 * description:    adds a new color object
	 * @author         Tobias Reichling
	 * @param          name  name of the color
	 * @param          rgb   color object
	 * @return         true or false
	 */
	public boolean setColor(String name, Color rgb){
		ColorObject color;
		ColorObject colorNew = new ColorObject(name, rgb);
		for (Enumeration enumColors = colors.elements(); enumColors.hasMoreElements();){
			color = (ColorObject)enumColors.nextElement();
			if (color.getName().equals(name)){
				return false;
			}
			if (color.getRGB().equals(rgb)){
				return false;
			}
		}
		colors.addElement(colorNew);
		return true;
	}
	
	/**
	 * method:         deleteColor
	 * description:    delets a color object if available
	 * @author         Tobias Reichling
	 * @param          name  name of the color
	 * @return         true or false
	 */
	public boolean deleteColor(String name){
		ColorObject color;
		int counter = 0;
		for (Enumeration enumColors = colors.elements(); enumColors.hasMoreElements();){
			color = (ColorObject)enumColors.nextElement();
			if (color.getName().equals(name)){
				colors.remove(counter);
				return true;
			}
			counter++;
		}
		return false;
	}
	
	/**
	 * method:         getColor
	 * description:    returns a color object if available
	 * @author         Tobias Reichling
	 * @param          name  name of the color object
	 * @return         color object or null
	 */
	public ColorObject getColor(String name){
		ColorObject color;
		for (Enumeration enumColors = colors.elements(); enumColors.hasMoreElements();){
			color = (ColorObject)enumColors.nextElement();
			if (color.getName().equals(name)){
				return color;
			}
		}
		return null;
	}
	
	/**
	 * method:         getAllColors
	 * description:    returns an enumeration of all colors
	 * @author         Tobias Reichling
	 * @return         enumeration
	 */
	public Enumeration getAllColors(){
		Enumeration enumColors = colors.elements();
		return enumColors;
	}
	
	/**
	 * method:         setElement
	 * description:    sets a new element
	 * @author         Tobias Reichling
	 * @param          name
	 * @param          width
	 * @param          height
	 * @param          matrix (matrix: true if element covers the matrix field, false otherwise)
	 * @param          stability
	 * @param          costs
	 * @return         true or false
	 */
	public boolean setElement(String name, int width, int height, boolean[][] matrix, int stability, int costs){
		ElementObject element;
		ElementObject elementNew = new ElementObject(name, width, height, matrix, stability, costs);
		for (Enumeration enumElements = elements.elements(); enumElements.hasMoreElements();){
			element = (ElementObject)enumElements.nextElement();
			if (element.getName().equals(name)){
				return false;
			}
			if ((element.getWidth() == width) && (element.getHeight() == height)){
				boolean[][] matrixNew = element.getMatrix();
				boolean difference = false;
				for (int row = 0; row < height; row++){
					for (int column = 0; column < width; column++){
						if (!(matrixNew[row][column] == matrix[row][column])){
							difference = true;
						}
					}
				}
				if (!difference){
					return false;
				}
			}
		}
		elements.addElement(elementNew);
		return true;
	}
	
	/**
	 * method:         setElement
	 * description:    sets a new element object
	 * @author         Tobias Reichling
	 * @param          name
	 * @param          width
	 * @param          height
	 * @param          matrix (matrix: true if element covers the matrix field, false otherwise)
	 * @return         true or false
	 */
	public boolean setElement(String name, int width, int height, boolean[][] matrix){
		ElementObject element;
		ElementObject elementNew = new ElementObject(name, width, height, matrix);
		for (Enumeration enumElements = elements.elements(); enumElements.hasMoreElements();){
			element = (ElementObject)enumElements.nextElement();
			if (element.getName().equals(name)){
				return false;
			}
			if ((element.getWidth()==width) && (element.getHeight()==height) && (element.getMatrix()==matrix)){
				return false;
			}
		}
		elements.addElement(elementNew);
		return true;
	}
	
	/**
	 * method:         setElement
	 * description:    sets a new Element
	 * @author         Tobias Reichling
	 * @param          element
	 */
	public void setElement(ElementObject element){
		elements.addElement(element);
	}
	
	/**
	 * method:         deleteElement
	 * description:    deletes an element if available
	 * @author         Tobias Reichling
	 * @param          name
	 * @return         true or false
	 */
	public boolean deleteElement(String name){
		ElementObject element;
		int counter = 0;
		for (Enumeration enumElements = elements.elements(); enumElements.hasMoreElements();){
			element = (ElementObject)enumElements.nextElement();
			if (element.getName().equals(name)){
				elements.remove(counter);
				return true;
			}
			counter++;
		}
		return false;
	}
	
	/**
	 * method:         getElement
	 * description:    returns an element object
	 * @author         Tobias Reichling
	 * @param          name
	 * @return         element object
	 */
	public ElementObject getElement(String name){
		ElementObject element;
		for (Enumeration enumElements = elements.elements(); enumElements.hasMoreElements();){
			element = (ElementObject)enumElements.nextElement();
			if (element.getName().equals(name)){
				return element;
			}
		}
		return null;
	}
	
	/**
	 * method:         getAllElements
	 * description:    returns an enumeration of all elements
	 * @author         Tobias Reichling
	 * @return         enumeration
	 */
	public Enumeration getAllElements(){
		Enumeration enumElements = elements.elements();
		return enumElements;
	}
	
}