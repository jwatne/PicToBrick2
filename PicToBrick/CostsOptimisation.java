package PicToBrick;

import java.util.Enumeration;
import javax.swing.*;
import java.util.*;

/**
 * class:            CostsOptimisation
 * layer:            Data processing (three tier architecture)
 * description:      tiling with cost optimisation
 * @author           Tobias Reichling
 */
public class CostsOptimisation
implements Tiling {
	
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private DataProcessing dataProcessing;
	private Calculation calculation;
	private int procent = 0;
	private int referenceValue;
	private int elements = 0;
	private int elementCounter = 0;
	private int totalCosts = 0;

	/**
	 * method:           CostsOptimisation
	 * description:      contructor
	 * @author           Tobias Reichling
	 * @param            dataProcessing  dataProcessing
	 * @param            calculation         calculation
	 */
	public CostsOptimisation(DataProcessing dataProcessing, Calculation calculation){
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
	}
	
	/**
	 * method:           tiling
	 * description:      tiling with cost optimisation
	 * @author           Tobias Reichling
	 * @param            mosaicWidth
	 * @param            mosaicHeight
	 * @param            configuration
	 * @param            statistic
	 * @return           mosaic
	 */
	public Mosaic tiling(int mosaicWidth,
			                   int mosaicHeight,
			                   Configuration configuration,
			                   Mosaic mosaic,
			                   boolean statistic){
		referenceValue = (mosaicWidth*mosaicHeight)/100;
		if (referenceValue == 0){referenceValue = 1;}
		//Vector with all elements (sorted by costs)
		Vector elementsSorted = sortElementsByCosts(configuration.getAllElements());
		//run through the image by random - determine element
		String currentColor = "";
		Enumeration sorted;
		Vector pixel;
		Vector pixel2;
		ElementObject currentElement;
		boolean elementSet = false;
		Vector elementCoords;
		//coordinates
		int left = 0;
		int top = 0;
		//Flag for the leftmost and top rung coordinate of the element
		int elementLeft = -1;
		int elementTop = -1;
		//position in the image (random coordinates from the vector)
		int colorRow, colorColumn;
		//compute random coordinates
		Vector coords = calculation.randomCoordinates(mosaicWidth, mosaicHeight);
		Enumeration coordsEnum = coords.elements();
		//run through the image (random coordinates)
		while (coordsEnum.hasMoreElements()){
			colorRow = (Integer)coordsEnum.nextElement();
			colorColumn = (Integer)coordsEnum.nextElement();
			//pixel of the current position
			pixel = mosaic.getMosaic()[colorRow][colorColumn];
			//if the pixel is not allready covered by an element
			if (pixel.size()==1){
				//compute pixel color
				currentColor = (String)(pixel.get(0));	
				//the current pixel is a 1x1 element if the 4 surrounding pixels are
				//not of the same color or covered by another element
				//No border handling
				if ((colorRow > 0)
						&& (colorRow < mosaicHeight-1)
						&& (colorColumn > 0)
						&& (colorColumn < mosaicWidth-1)
						//top: vector contains a object which is not the current color
						&& (((mosaic.getMosaic()[colorRow-1][colorColumn].size()==1)
								&& !(((String)(mosaic.getMosaic()[colorRow-1][colorColumn].get(0))).equals(currentColor)))
								//or vector is empty or 2 objects (pixel is covered)
								|| (mosaic.getMosaic()[colorRow-1][colorColumn].size()!=1))
						//bottom:
						&& (((mosaic.getMosaic()[colorRow+1][colorColumn].size()==1)
								&& !(((String)(mosaic.getMosaic()[colorRow+1][colorColumn].get(0))).equals(currentColor)))
								|| (mosaic.getMosaic()[colorRow+1][colorColumn].size()!=1))
						//right:
						&& (((mosaic.getMosaic()[colorRow][colorColumn+1].size()==1)
								&& !(((String)(mosaic.getMosaic()[colorRow][colorColumn+1].get(0))).equals(currentColor)))
								|| (mosaic.getMosaic()[colorRow][colorColumn+1].size()!=1))
						//left:
						&& (((mosaic.getMosaic()[colorRow][colorColumn-1].size()==1)
								&& !(((String)(mosaic.getMosaic()[colorRow][colorColumn-1].get(0))).equals(currentColor)))
								|| (mosaic.getMosaic()[colorRow][colorColumn-1].size()!=1))){
					//set current element to basis element
					currentElement = configuration.getElement(configuration.getBasisName());
					//set mosaic element (color and element)
					mosaic.setElement(colorRow, colorColumn, currentElement.getName(), true);
					totalCosts = totalCosts + currentElement.getCosts();
					elements++;
					elementCounter++;
				}else{
					//Enumeration (sorted elements)
					sorted = elementsSorted.elements();
					//run through sorted elements
					elementSet = false;
					while (sorted.hasMoreElements() && !elementSet)
					{
						currentElement = (ElementObject)sorted.nextElement();
						//compute random coordinates for the element
						elementCoords = calculation.randomCoordinates(currentElement.getWidth(), currentElement.getHeight());
						Enumeration elementCoordsEnum = elementCoords.elements();
						while (elementCoordsEnum.hasMoreElements() && !elementSet){
							//the coordinates of the element (to place the element) are also random
							//testing if the element fits!
							top = (Integer)elementCoordsEnum.nextElement();
							left = (Integer)elementCoordsEnum.nextElement();
							//only place the element if it covers the current pixel!
							if (currentElement.getMatrix()[top][left]){
								//test mosaic borders
					            //bottom
								if (((colorRow + currentElement.getHeight()-(top+1)) < mosaicHeight)
											//top
										    && (colorRow - top >= 0)
										    //left
											&& ((colorColumn - left) >= 0)
											//right
											&& ((colorColumn + (currentElement.getWidth() - (left+1))) < mosaicWidth)){
									//colormatching with true values of the element
									boolean elementFits = true;
									elementLeft = -1; elementTop = -1;
									for (int elementRow = 0; elementRow < currentElement.getHeight(); elementRow++){
										for (int elementColumn = 0; elementColumn < currentElement.getWidth(); elementColumn++){
											//only test if "true" in the element
											if (currentElement.getMatrix()[elementRow][elementColumn]){
												//remember leftmost element in top row
												if (elementLeft == -1){elementLeft = elementColumn;}
												if (elementTop == -1){elementTop = elementRow;}
												pixel2 = mosaic.getMosaic()[(colorRow+elementRow)-top][(colorColumn+elementColumn)-left];
												if (!pixel2.isEmpty()){
													if (!((String)(pixel2.get(0))).equals(currentColor)){
														elementFits = false;
													}
												}else{
													elementFits = false;
												}
											}
										}
									}
									if (elementFits){
										//Set element
										elementSet = true;
										for (int elementRow = 0; elementRow < currentElement.getHeight(); elementRow++){
											for (int elementColumn = 0; elementColumn < currentElement.getWidth(); elementColumn++){
												//only set if "true" in the element
												if (currentElement.getMatrix()[elementRow][elementColumn]){
													mosaic.initVector(((colorRow+elementRow)-top), ((colorColumn+elementColumn)-left));
												}
											}
										}
										mosaic.setElement(colorRow-top+elementTop, colorColumn-left+elementLeft, currentColor, false);
										mosaic.setElement(colorRow-top+elementTop, colorColumn-left+elementLeft, currentElement.getName(), true);
										totalCosts = totalCosts + currentElement.getCosts();
										elements = elements + computeElementSize(currentElement);
										elementCounter++;
									}
								}
							}
						}
					}
				}
			}
			//Update progressbar
			if (elements%referenceValue == 0){
				try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
					procent++;
					dataProcessing.refreshProgressBarAlgorithm(procent,2);
				}});}catch(Exception e){System.out.println(e.toString());}
			}
		}
		dataProcessing.setInfo(textbundle.getString("output_costsOptimisation_1"),3);
		dataProcessing.setInfo(textbundle.getString("output_costsOptimisation_2") + ": " + totalCosts/100 + textbundle.getString("output_decimalPoint") + totalCosts%100 + " " + textbundle.getString("output_costsOptimisation_3"),3);
		//Update progressbar
		try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
			dataProcessing.refreshProgressBarAlgorithm(100,2);
		}});}catch(Exception e){System.out.println(e.toString());}
		return mosaic;
	}
	
	/**
	 * method:           sortElementsByCosts
	 * description:      sort the element vector
	 * @author           Tobias Reichling
	 * @param            elementsUnsorted
	 * @return           elementsSorted
	 */
	private Vector sortElementsByCosts(Enumeration elementsUnsorted){
		double costs = 0.0;
		boolean included = false;
		int position;
		ElementObject supportElement;
		Vector elementsSorted = new Vector();
		//Sort elements by cost
		while (elementsUnsorted.hasMoreElements()) {
			supportElement = ((ElementObject)elementsUnsorted.nextElement());
			if (elementsSorted.size()==0){
				elementsSorted.add(supportElement);
			}else{
				costs = (double)supportElement.getCosts() / (double)computeElementSize(supportElement);
				position = 0;
				included = false;
				Enumeration supportEnum = elementsSorted.elements();
				while (supportEnum.hasMoreElements() && !included) {
					ElementObject anotherElement = (ElementObject)supportEnum.nextElement();
					double costs2 = (double)anotherElement.getCosts() / (double)computeElementSize(anotherElement);
					if (costs < costs2){
						elementsSorted.add(position, supportElement);
						included = true;
					}else{
						position++;
					}
				}
				if (!included){
					elementsSorted.add(supportElement);
				}
			}
		}
		return elementsSorted;
	}
	
	/**
	 * method:           computeElementSize
	 * description:      computes the size of an element
	 * @author           Tobias Reichling
	 * @param            element
	 * @return           size
	 */
	private int computeElementSize(ElementObject element){
		int result = 0;
		for (int row = 0; row < element.getHeight(); row++){
			for (int column = 0; column < element.getWidth(); column++){
				if (element.getMatrix()[row][column]){
					result++;
				}
			}
		}
		return result;
	}
}