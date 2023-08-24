package PicToBrick;

import java.io.*;

/**
 * class:          ElementObject
 * layer:          DataManagement (three tier architecture)
 * description:    contains all information about an element
 * @author         Tobias Reichling
 */
public class ElementObject 
implements Serializable{

	private int width;
	private int height;
	private boolean[][] matrix;
	private String name;
	private int stability;
	private int costs;

	/**
	 * method:         ElementObject
	 * description:    constructor
	 * @author         Tobias Reichling
	 */
	public ElementObject(){}
	
	/**
	 * method:         ElementObject
	 * description:    constructor
	 * @author         Tobias Reichling
	 * @param          name 
	 * @param          width
	 * @param          height
	 * @param          matrix
	 */
	public ElementObject(String name, int width, int height, boolean[][] matrix){
		this.width = width;
		this.height = height;
		this.matrix = matrix;
		this.name = name;
		this.stability = 1;
		this.costs = 1;
	}
	
	/**
	 * method:         ElementObject
	 * description:    constructor
	 * @author         Tobias Reichling
	 * @param          name 
	 * @param          width
	 * @param          height
	 * @param          matrix
	 * @param          stability
	 * @param          costs
	 */
	public ElementObject(String name, int width, int height, boolean[][] matrix, int stability, int costs){
		this.width = width;
		this.height = height;
		this.matrix = matrix;
		this.name = name;
		this.stability = stability;
		this.costs = costs;
	}
	
	/**
	 * method:         getWidth
	 * description:    returns the width
	 * @author         Tobias Reichling
	 * @return         width
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * method:         getName
	 * description:    returns the name
	 * @author         Tobias Reichling
	 * @return         name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * method:         getHeight
	 * description:    returns the height
	 * @author         Tobias Reichling
	 * @return         height
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * method:         getMatrix
	 * description:    returns the matrix
	 * @author         Tobias Reichling
	 * @return         msatrix
	 */
	public boolean[][] getMatrix(){
		return matrix;
	}
	
	/**
	 * method:         getStability
	 * description:    returns the stability
	 * @author         Tobias Reichling
	 * @return         stability
	 */
	public int getStability(){
		return stability;
	}
	
	/**
	 * method:         getCosts
	 * description:    returns the costs
	 * @author         Tobias Reichling
	 * @return         costs
	 */
	public int getCosts(){
		return costs;
	}
	
	/**
	 * method:         setName
	 * description:    sets the name
	 * @author         Tobias Reichling
	 * @param          name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * method:         setWidth
	 * description:    sets the width
	 * @author         Tobias Reichling
	 * @param          width
	 */
	public void setWidth(int width){
		this.width = width;
	}
	
	/**
	 * method:         setHeight
	 * description:    sets the height
	 * @author         Tobias Reichling
	 * @param          height
	 */
	public void setHeight(int height){
		this.height = height;
	}
	
	/**
	 * method:         setMatrix
	 * description:    sets the matrix
	 * @author         Tobias Reichling
	 * @param          matrix
	 */
	public void setMatrix(boolean[][] matrix){
		this.matrix = matrix;
	}
	
	/**
	 * method:         setStability
	 * description:    sets the stability
	 * @author         Tobias Reichling
	 * @param          stability
	 */
	public void setStability(int stability){
		this.stability = stability;
	}
	
	/**
	 * method:         setCosts
	 * description:    sets the costs
	 * @author         Tobias Reichling
	 * @param          costs
	 */
	public void setCosts(int costs){
		this.costs = costs;
	}
}
