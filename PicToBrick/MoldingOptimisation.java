package PicToBrick;

import java.util.Enumeration;
import java.util.Random;
import javax.swing.*;

import java.util.*;

/**
 * class:            MoldingOptimisation
 * layer:            Data processing (three tier architecture)
 * description:      tiling with molding optimisation
 * @author           Tobias Reichling
 */
public class MoldingOptimisation
implements Tiling {
	
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private DataProcessing dataProcessing;
	private int percent = 0;
	private int rows = 0;
	private int colorRow;
	private boolean statisticOutput;
	private String[] colorArray;
	private int[][] elementArray, elementArrayOptimisation;
	private String currentColorName;
	private int currentColorNumber;
	private int colorCount;
	private int pixelCount;
	private int pixelCounter;
	private Configuration configuration;
	private Vector dialogSelection;
	private int optimisationMethod;
	private int pixelColorChanges;

	/**
	 * method:           MoldingOptimisation
	 * description:      contructor
	 * @author           Tobias Reichling
	 * @param            DataProcessing  dataProcessing
	 * @param            Calculation     calculation
	 * @param            Vector          dialogSelection
	 */
	public MoldingOptimisation(DataProcessing dataProcessing, Calculation calculation, Vector dialogSelection){
		this.dataProcessing = dataProcessing;
		this.dialogSelection = dialogSelection;
	}
	
	/**
	 * method:           tiling
	 * description:      tiling with molding optimisation
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
		//vector dialogSelection contains information about the algorithm mode:
		//vector is empty: no additional optimisation
		//vector contains action command "no": no additional optimisation
		//vector contains action command "yes": additional optimisation mode
		if (dialogSelection.isEmpty()){
			this.optimisationMethod = 0;
		}else if (((String)dialogSelection.elementAt(0)).equals("no")){
			this.optimisationMethod = 0;
		}else if (((String)dialogSelection.elementAt(0)).equals("yes")){
			this.optimisationMethod = 1;
		}
		this.rows = mosaicHeight;
		this.pixelCount = mosaicWidth*mosaicHeight;
		this.statisticOutput = statistic;
		this.configuration = configuration;
		//consistency check: all elements in the configuration must be
		//the same elements as in the system ministeck configuration!
		//if no, we must switch to the algorithm "basic elements only"
		//if yes, we can process the original algorithm for molding optimisation
		Configuration ministeckSystem = dataProcessing.getSystemConfiguration(2);
		if (!consistencyCheck(ministeckSystem, configuration)){
			//------------"basic elements only"------------
			try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
				dataProcessing.errorDialog(textbundle.getString("output_moldingOptimisation_1") + "\n\r" + textbundle.getString("output_moldingOptimisation_2"));
			}});}catch(Exception e){System.out.println(e.toString());}
			for (colorRow = 0; colorRow < mosaicHeight; colorRow++){
				//refresh progress bar
				//2 different bars: normal mode and statistic mode
				try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
					percent = (int)((100.0/rows)*colorRow);
					if (statisticOutput){
						dataProcessing.refreshProgressBarAlgorithm(percent,4);
					}else{
						dataProcessing.refreshProgressBarAlgorithm(percent,2);
					}
				}});}catch(Exception e){System.out.println(e.toString());}
				for (int colorColumn = 0; colorColumn < mosaicWidth; colorColumn++){
					mosaic.setElement(colorRow,colorColumn,configuration.getBasisName(),true);
				}
			}  	
			return mosaic;
		}else{
			//------------ molding optimisation ------------
			//flags
			pixelColorChanges=0;
			currentColorName = new String("");
			currentColorNumber = -1;
			pixelCounter=0;
			Vector pixel;
			Vector pixel2;
			Enumeration sorted;
			int left = 0;
			boolean elementSet = false;
			ElementObject currentElement;
			//init arrays
			colorCount = configuration.getQuantityColors();
			Enumeration colorEnum = configuration.getAllColors();
			colorArray = new String[colorCount];
			for (int i = 0; i < colorCount; i++){
				colorArray[i] = ((ColorObject)colorEnum.nextElement()).getName();
			}
			elementArray = new int[colorCount][7];
			for (int j = 0; j < colorCount; j++){
				elementArray[j][0] =  0;     // 0 => 1x1     covering: 1
				elementArray[j][1] =  0;     // 1 => 1x2     covering: 2
				elementArray[j][2] =  0;     // 2 => 1x3     covering: 3 and matrix 1x3 
				elementArray[j][3] =  0;     // 3 => corner  covering: 3 and matrix 2x2
				elementArray[j][4] =  0;     // 4 => 2x2     covering: 4
				elementArray[j][5] =  0;     // 5 => normal molding
				elementArray[j][6] =  0;     // 6 => 1er molding
			}
			//if: additional optimisation mode ...
			if (optimisationMethod == 1){
				//init array
				elementArrayOptimisation = new int[colorCount][5];
				for (int j = 0; j < colorCount; j++){
					elementArrayOptimisation[j][0] =  0;     // 0 => 1x1     covering: 1
					elementArrayOptimisation[j][1] =  0;     // 1 => 1x2     covering: 2
					elementArrayOptimisation[j][2] =  0;     // 2 => 1x3     covering: 3 and matrix 1x3 
					elementArrayOptimisation[j][3] =  0;     // 3 => corner  covering: 3 and matrix 2x2
					elementArrayOptimisation[j][4] =  0;     // 4 => 2x2     covering: 4
				}
			}
			//scan mosaic linear
			//for each pixel the elementVector is computed
			//(greatest quantity to the vector head)
			//if an element is not available any more, a new molding
			//is added to all elment counters in the current color
			for (colorRow = 0; colorRow < mosaicHeight; colorRow++){
				//refresh progress bar
				try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
					percent = (int)((100.0/rows)*colorRow);
					if (statisticOutput){
						dataProcessing.refreshProgressBarAlgorithm(percent,4);
					}else{
						dataProcessing.refreshProgressBarAlgorithm(percent,2);
					}
				}});}catch(Exception e){System.out.println(e.toString());}
				for (int colorColumn = 0; colorColumn < mosaicWidth; colorColumn++){
					pixelCounter++;
					pixel = mosaic.getMosaic()[colorRow][colorColumn];
					//if the pixel is not reserved by an other element ...
					if (!pixel.isEmpty()){
						//compute color
						currentColorName = (String)(pixel.get(0));
						currentColorNumber = computeColorNumber();
						//compute element vector
						Vector elementsSorted = sortElementsByAvailability(configuration.getAllElements());
						sorted = elementsSorted.elements();
						//check each element in this sorted vector
						elementSet = false;
						/////////////////////////////////////////////////////////////////////////////////////////
						/////////////////////////////////////////////////////////////////////////////////////////
						//if: additional optimisation mode:
						//a vector is computed with critical elements
						//an element is critical if the counter is more than 2x the quantity of this
						//element on a normal molding
						//-----------------------------------------------
						//(1x1 elements cause no problems)
						if (optimisationMethod == 1){
							Vector criticalElements = new Vector();
							//2x2 element
							if (elementArrayOptimisation[currentColorNumber][4]==1){
								Enumeration criticalElements4 = computeElements(4,2,2);
								while (criticalElements4.hasMoreElements()){
									criticalElements.add((ElementObject)criticalElements4.nextElement());
								}
							}
							//corner element
							if (elementArrayOptimisation[currentColorNumber][3]==1){
								Enumeration criticalElements3 = computeElements(3,2,2);
								while (criticalElements3.hasMoreElements()){
									criticalElements.add((ElementObject)criticalElements3.nextElement());
								}
							}
							//1x3 element
							if (elementArrayOptimisation[currentColorNumber][2]==1){
								Enumeration criticalElements2 = computeElements(3,1,3);
								while (criticalElements2.hasMoreElements()){
									criticalElements.add((ElementObject)criticalElements2.nextElement());
								}
							}
							//1x2 element
							if (elementArrayOptimisation[currentColorNumber][1]==1){
								Enumeration criticalElements1 = computeElements(2,1,2);
								while (criticalElements1.hasMoreElements()){
									criticalElements.add((ElementObject)criticalElements1.nextElement());
								}
							}
							//if critical elements are found, the critical elment vector is scanned
							//for each element we check, if we can used it in the
							//mosaic with a maximal re-coloring of 1 pixel
							if (criticalElements.size()>0){
								Enumeration criticalElementsEnum = criticalElements.elements();
								while (criticalElementsEnum.hasMoreElements() && !elementSet){
									currentElement = (ElementObject)criticalElementsEnum.nextElement();
									//find the left element in the top row
									left = -1;
									for (int i = 0; i < currentElement.getWidth(); i++){
										if (currentElement.getMatrix()[0][i] && left == -1){
											left = i;
										}
									}
									//check mosaic borders
									//bottom
									if (((colorRow + currentElement.getHeight()-1) < mosaicHeight)
												//left
												&& ((colorColumn - left) >= 0)
												//right
												&& ((colorColumn + (currentElement.getWidth() - (left+1))) < mosaicWidth)){
										//color matching
										//-------------------------
										//count all pixel who fits in color
										int fits = 0;
										boolean occupied = false;
										for (int elementRow = 0; elementRow < currentElement.getHeight(); elementRow++){
											for (int elementColumn = 0; elementColumn < currentElement.getWidth(); elementColumn++){
												//test only "true" positions in elment matrix
												if (currentElement.getMatrix()[elementRow][elementColumn]){
													pixel2 = mosaic.getMosaic()[colorRow+elementRow][(colorColumn+elementColumn)-left];
													if (!pixel2.isEmpty()){
														if (((String)(pixel2.get(0))).equals(currentColorName)){
															fits++;
														}
													}else{
														occupied = true;
													}
												}
											}
										}
										//if the number of fitting colors is equal or only 1 less than the element size
										//the element is set to the mosaic
										//(increment the pixelColorChanges-counter)
										if (fits >= (computeElementSize(currentElement)-1) && !occupied){
											//set element
											pixelColorChanges++;
											elementSet = true;
											for (int elementRow = 0; elementRow < currentElement.getHeight(); elementRow++){
												for (int elementColumn = 0; elementColumn < currentElement.getWidth(); elementColumn++){
													if (currentElement.getMatrix()[elementRow][elementColumn]){
														mosaic.initVector(colorRow+elementRow, (colorColumn+elementColumn)-left);
													}
												}
											}
											mosaic.setElement(colorRow, colorColumn, currentColorName, false);
											mosaic.setElement(colorRow, colorColumn, currentElement.getName(), true);
											//count element and if necessary use new molding
											updateElementAppearance(currentElement, pixelCounter);
										}
									}
								}//end while
							}//end if (criticalElements.size()>0)
						}//end optimisation
						/////////////////////////////////////////////////////////////////////////////////////////
						/////////////////////////////////////////////////////////////////////////////////////////
						//if: normal optimisation mode
						//... or ...
						//if: additional optimisation mode, but no critical element is set to the mosaic:
						//...
						//the normal algorithm is used here:
						//scan sorted element vector and check for each element
						while (sorted.hasMoreElements() && !elementSet)
						{
							currentElement = (ElementObject)sorted.nextElement();
							//find the left position in the top row of the element matrix
							left = -1;
							for (int i = 0; i < currentElement.getWidth(); i++){
								if (currentElement.getMatrix()[0][i] && left == -1){
									left = i;
								}
							}
							//check mosaic border: bottom
							if (((colorRow + currentElement.getHeight()-1) < mosaicHeight)
										//left
										&& ((colorColumn - left) >= 0)
										//right
										&& ((colorColumn + (currentElement.getWidth() - (left+1))) < mosaicWidth)){
								//farb matching
								boolean elementFits = true;
								for (int elementRow = 0; elementRow < currentElement.getHeight(); elementRow++){
									for (int elementColumn = 0; elementColumn < currentElement.getWidth(); elementColumn++){
										//check only "true" positions in the element matrix
										if (currentElement.getMatrix()[elementRow][elementColumn]){
											pixel2 = mosaic.getMosaic()[colorRow+elementRow][(colorColumn+elementColumn)-left];
											if (!pixel2.isEmpty()){
												if (!((String)(pixel2.get(0))).equals(currentColorName)){
													elementFits = false;
												}
											}else{
												elementFits = false;
											}
										}
									}
								}
								if (elementFits){
									//set element
									elementSet = true;
									for (int elementRow = 0; elementRow < currentElement.getHeight(); elementRow++){
										for (int elementColumn = 0; elementColumn < currentElement.getWidth(); elementColumn++){
											if (currentElement.getMatrix()[elementRow][elementColumn]){
												mosaic.initVector(colorRow+elementRow, (colorColumn+elementColumn)-left);
											}
										}
									}
									mosaic.setElement(colorRow, colorColumn, currentColorName, false);
									mosaic.setElement(colorRow, colorColumn, currentElement.getName(), true);
									//count element and if necessary use new molding
									updateElementAppearance(currentElement, pixelCounter);
								}
							}//end border check
						}//end while
					}//end if (!pixel.isEmpty()){
			}}//end for(for())
			//--------------------------------------------------------------------------------
			//output
			output();
			//refresh progress bar
			try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
				if (statisticOutput){
					dataProcessing.refreshProgressBarAlgorithm(100,4);
				}else{
					dataProcessing.refreshProgressBarAlgorithm(100,2);
				}
			}});}catch(Exception e){System.out.println(e.toString());}
			//--------------------------------------------------------------------------------
			return mosaic;
		}
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
	
	/**
	 * method:           sortElementsByAvailability
	 * description:      sorts the element vector
	 * @author           Tobias Reichling
	 * @param            elementsUnsorted
	 * @return           elementsSorted
	 */
	private Vector sortElementsByAvailability(Enumeration elementsUnsorted){
		//vector init
		Vector elementsSorted = new Vector();
		//array init
		double[] referenceValues = new double[5];
		//-------------------
		//1er molding: quantity of 1x1 elements = 20
		//normal molding: quantity of 1x1 elements = 2
		//-------------------
		//during algorithm:
		//1x1 element: computes average values for quantity and costs
		//-------------------
		double singleAppearanceAverage = ((elementArray[currentColorNumber][5]*2.0
                + elementArray[currentColorNumber][6]*20.0)
                / (elementArray[currentColorNumber][5]
                + elementArray[currentColorNumber][6]));
		double singlePriceAverage = ((elementArray[currentColorNumber][5]*(27.0/114.0)
                + elementArray[currentColorNumber][6]*(10.0/20.0))
                / (elementArray[currentColorNumber][5]
                + elementArray[currentColorNumber][6]));
		//
		//                  (    current availaible quantity                 costs per molding       )
		// referenceValue = ( ---------------------------------   /   -----------------------------  )
		//                  (    quantity in a normal molding           covering per molding         )
		//
		//first fraction
		//		bigger, the more quantity of the element is available in ratio to the quantity of this
		//		element in a normal molding
		//
		//second fraction
		//		compute the covering costs per pixel of an element
		//		1x1 elements have other costs per coverd pixel ... so ...
		//		the more 1er moldings we use the dearer are the covering costs of an 1x1 element
		//
		//complete fraction
		//		uses current available quantity and costs per covering one pixel to sort
		//      the element vector
		//
		referenceValues[0] =  (elementArray[currentColorNumber][0]/singleAppearanceAverage)/singlePriceAverage;
		referenceValues[1] =  (elementArray[currentColorNumber][1]/16.0)/(27.0/114.0);
		referenceValues[2] =  (elementArray[currentColorNumber][2]/12.0)/(27.0/114.0);
		referenceValues[3] =  (elementArray[currentColorNumber][3]/4.0)/(27.0/114.0);
		referenceValues[4] =  (elementArray[currentColorNumber][4]/8.0)/(27.0/114.0);
		//flags
		boolean included = false;
		int position;
		ElementObject supportElement;
		while (elementsUnsorted.hasMoreElements()) {
			supportElement = ((ElementObject)elementsUnsorted.nextElement());
			//if the sorted vector is empty, add the next element ...
			if (elementsSorted.size()==0){
				elementsSorted.add(supportElement);
			}else{
				//... then check the reference value of the next element and sort
				//it accordingly into the vector
				position = 0;
				included = false;
				Enumeration supportEnum = elementsSorted.elements();
				while (supportEnum.hasMoreElements() && !included) {
					ElementObject anotherElement = (ElementObject)supportEnum.nextElement();
					if (referenceValues[computeElementNumber(supportElement)] >= referenceValues[computeElementNumber(anotherElement)]){
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
	 * method:           computeElements
	 * description:      computes elements
	 * @param            covering
	 * @param            width
	 * @param            height
	 * @author           Tobias Reichling
	 */
	private Enumeration computeElements(int covering, int width, int height){
		ElementObject element;
		int cover = 0;
		Vector elements = new Vector();
		Enumeration allElements = configuration.getAllElements();
		while (allElements.hasMoreElements()){
			element = (ElementObject)allElements.nextElement();
			//check width and height ...
			if (element.getWidth()==width && element.getHeight()==height){
				cover = 0;
				for (int i = 0; i < element.getHeight(); i++){
					for (int j = 0; j < element.getWidth(); j++){
						if (element.getMatrix()[i][j]){
							cover++;
						}
					}
				}
				//... and the covering of the element
				if (cover == covering){
					elements.add(element);
				}
			}
			//also check covering if width = height and height = width because there
			//are different directions of elements use (2x3 = 3x2)
			if (element.getWidth()==height && element.getHeight()==width){
				cover = 0;
				for (int i = 0; i < element.getHeight(); i++){
					for (int j = 0; j < element.getWidth(); j++){
						if (element.getMatrix()[i][j]){
							cover++;
						}
					}
				}
				if (cover == covering){
					elements.add(element);
				}
			}
		}
		Enumeration elementsEnum = elements.elements();
		return elementsEnum;
	}
	
	/**
	 * method:           updateElementAppearance
	 * description:      updates the array with the element quantity
	 *                   and broach a new molding if necessary
	 * @param            element
	 * @param            counter
	 * @author           Tobias Reichling
	 */
	private void updateElementAppearance(ElementObject element, int counter){
		Random random = new Random();
		int quantity = elementArray[currentColorNumber][computeElementNumber(element)];
		//decrement
		elementArray[currentColorNumber][computeElementNumber(element)] = (quantity-1);
		if (quantity==0){
			if (!(computeElementNumber(element)==0)){
				//if quantity is 0, use a new normal molding ...
				elementArray[currentColorNumber][0] = elementArray[currentColorNumber][0]+2;
				elementArray[currentColorNumber][1] = elementArray[currentColorNumber][1]+16;
				elementArray[currentColorNumber][2] = elementArray[currentColorNumber][2]+12;
				elementArray[currentColorNumber][3] = elementArray[currentColorNumber][3]+4;
				elementArray[currentColorNumber][4] = elementArray[currentColorNumber][4]+8;
				elementArray[currentColorNumber][5] = elementArray[currentColorNumber][5]+1;
			}else{
				//if quantity of 1x1 element is 0, use a new 1er molding ...
				elementArray[currentColorNumber][0] = elementArray[currentColorNumber][0]+20;
				elementArray[currentColorNumber][6] = elementArray[currentColorNumber][6]+1;
			}
		}
		//if: additional optimisation mode is on:
		//now we must check if an element has critical quantity or not
		//critical: quantity is bigger than the quantity of this element in a normal molding
		//- solution: random usage of the critical elements
		//(random: to avoid visual artefacts !!!)
		//very critical: quantity is 2x bigger than the quantity of this element in a normal molding
		//- solution: strict usage of the very critical elements
		if (optimisationMethod == 1){
			//1x2 element
			//very critical
			if (elementArray[currentColorNumber][1] > 32){
				elementArrayOptimisation[currentColorNumber][1] = 1;
			//critical: random
			}else if (elementArray[currentColorNumber][1] > 16){
				if (random.nextInt(2)%2==0){
					elementArrayOptimisation[currentColorNumber][1] = 1;
				}else{
					elementArrayOptimisation[currentColorNumber][1] = 0;
				}
			}else{		
				//not critical
				elementArrayOptimisation[currentColorNumber][1] = 0;
			}
			//1x3 element
			//very critical
			if (elementArray[currentColorNumber][2] > 24){
				elementArrayOptimisation[currentColorNumber][2] = 1;
				//critical: random
			}else if (elementArray[currentColorNumber][2] > 12){
				if (random.nextInt(2)%2==0){
					elementArrayOptimisation[currentColorNumber][2] = 1;
				}else{
					elementArrayOptimisation[currentColorNumber][2] = 0;
				}
			}else{		
				//not critical
				elementArrayOptimisation[currentColorNumber][2] = 0;
			}
			//corner element
			//very critical
			if (elementArray[currentColorNumber][3] > 8){
				elementArrayOptimisation[currentColorNumber][3] = 1;
				//critical: random
			}else if (elementArray[currentColorNumber][3] > 4){
				if (random.nextInt(2)%2==0){
					elementArrayOptimisation[currentColorNumber][3] = 1;
				}else{
					elementArrayOptimisation[currentColorNumber][3] = 0;
				}
			}else{	
				//not critical
				elementArrayOptimisation[currentColorNumber][3] = 0;
			}
			//2x2 element
			//very critical
			if (elementArray[currentColorNumber][4] > 16){
				elementArrayOptimisation[currentColorNumber][4] = 1;
				//critical: random
			}else if (elementArray[currentColorNumber][4] > 8){
				if (random.nextInt(2)%2==0){
					elementArrayOptimisation[currentColorNumber][4] = 1;
				}else{
					elementArrayOptimisation[currentColorNumber][4] = 0;
				}
			}else{
				//not critical
				elementArrayOptimisation[currentColorNumber][4] = 0;
			}
		}
	}
	
	/**
	 * method:           output
	 * description:      computes output
	 * @author           Tobias Reichling
	 */
	private void output(){
		int normalUsageAllColors  = 0;
		int singleUsageAllColors   = 0;
		int normalFracturedAllColors = 0;
		int singleFracturedAllColors  = 0;
		int normalFracturedOneColor  = 0;
		int singleFracturedOneColor   = 0;
		dataProcessing.setInfo(textbundle.getString("output_moldingOptimisation_3"),3);
		if (optimisationMethod==0){
			dataProcessing.setInfo("(" + textbundle.getString("output_moldingOptimisation_4") + ")",3);
		}else{
			int recolored = (int)((((double)pixelColorChanges)/((double)(pixelCount)))*1000);
			dataProcessing.setInfo("(" + textbundle.getString("output_moldingOptimisation_5") + ". " + recolored/10 + textbundle.getString("output_decimalPoint") + recolored%10 + " % " + textbundle.getString("output_moldingOptimisation_6") + ".)",3);
		}
		dataProcessing.setInfo(textbundle.getString("output_moldingOptimisation_7") + ",",2);
		dataProcessing.setInfo(textbundle.getString("output_moldingOptimisation_8") + ":",2);
		dataProcessing.setInfo("[ " + textbundle.getString("output_moldingOptimisation_9") + " / " + textbundle.getString("output_moldingOptimisation_10") + " ]",2);
		//for each color ...
		for (int j = 0; j < colorCount; j++){
			//... and only if we have used the color within the mosaic
			if (elementArray[j][5]>0 || elementArray[j][6]>0){
				normalUsageAllColors = normalUsageAllColors + elementArray[j][5];
				singleUsageAllColors = singleUsageAllColors + elementArray[j][6];
				normalFracturedOneColor=0;
				singleFracturedOneColor=0;
				//decrement the remaining elements so often (with the quantity of a normal molding)
				//till all element counters are <= 0 (normal moldings and 1er moldings)
				while (elementArray[j][1] > 0
						|| elementArray[j][2] > 0
						|| elementArray[j][3] > 0
						|| elementArray[j][4] > 0){
					//counter for particular used normal moldings
					normalFracturedOneColor++;
					elementArray[j][0] = elementArray[j][0] - 2; 
					elementArray[j][1] = elementArray[j][1] - 16; 
					elementArray[j][2] = elementArray[j][2] - 12;
					elementArray[j][3] = elementArray[j][3] - 4;
					elementArray[j][4] = elementArray[j][4] - 8;
				}
				//for each molding variation one will set to "not complete usable"
				if(normalFracturedOneColor > 0){
					normalFracturedOneColor--;
				}
				normalFracturedAllColors = normalFracturedAllColors + normalFracturedOneColor;
				while (elementArray[j][0] > 0){
					//counter for particular used 1er moldings
					singleFracturedOneColor++;
					elementArray[j][0] = elementArray[j][0] - 20;
				}
				//for each molding variation one will set to "not complete usable"
				if(singleFracturedOneColor > 0){
					singleFracturedOneColor--;
				}
				singleFracturedAllColors = singleFracturedAllColors + singleFracturedOneColor;
				//output per color
				dataProcessing.setInfo("[" + elementArray[j][5] + "/" + normalFracturedOneColor
						+ "]  [" +  + elementArray[j][6] + "/" + singleFracturedOneColor
						+ "]  -  " + colorArray[j],2);
			}
		}
		//calculation costs, etc.
		int costs = normalUsageAllColors * 27 + singleUsageAllColors * 10;
		int euro = costs/100;
		int cent = costs%100;
		String centText;
		if (cent < 10){
			centText = "0"+cent;
		}else{
			centText = ""+cent;
		}
		int percentUsage = (int)(((double)(normalFracturedAllColors+singleFracturedAllColors)) / ((double)(normalUsageAllColors+singleUsageAllColors)) * 10000.0);
		//output costs, etc.
		dataProcessing.setInfo(textbundle.getString("output_moldingOptimisation_11") + ": "
				+ (normalUsageAllColors+singleUsageAllColors) + " " + textbundle.getString("output_moldingOptimisation_12") + ". "
				+ (normalFracturedAllColors+singleFracturedAllColors)
				+ " (" + textbundle.getString("output_moldingOptimisation_13") + ". " + (percentUsage/100) + textbundle.getString("output_decimalPoint") + (percentUsage%100) + " %) " + textbundle.getString("output_moldingOptimisation_14") + ".",3);
		dataProcessing.setInfo(textbundle.getString("output_moldingOptimisation_15") + " " + normalUsageAllColors + " " + textbundle.getString("output_moldingOptimisation_16") + " " + singleUsageAllColors
				+ " " + textbundle.getString("output_moldingOptimisation_17") + ": " + euro + textbundle.getString("output_decimalPoint") + centText + " " + textbundle.getString("output_moldingOptimisation_18"),3);
	}
	
	/**
	 * method:           computeColorNumber
	 * description:      returns the color number
	 * @author           Tobias Reichling
	 * @return           Nummer
	 */
	private int computeColorNumber(){
		for (int x = 0; x < colorArray.length; x++){
			if (colorArray[x].equals(currentColorName)){
				return x;
			}
		}
		return -1;
	}
	
	/**
	 * method:           computeElementNumber
	 * description:      returns the element number
	 * @author           Tobias Reichling
	 * @return           Nummer
	 */
	private int computeElementNumber(ElementObject supportElement){
		int counter = 0;
		for (int row = 0; row < supportElement.getHeight(); row++){
			for (int column = 0; column < supportElement.getWidth(); column++){
				if (supportElement.getMatrix()[row][column]){
					counter++;
				}
			}
		}
		switch (counter){
		case 1:{
			return 0;
		}
		case 2:{
			return 1;
		}
		case 3:{
			if (supportElement.getWidth()==2){
				return 3;
			}else{
				return 2;
			}
		}
		case 4:{
			return 4;
		}
		default:{
			return -1;
		}
		}
	}
	
	/**
	 * method:           consistencyCheck
	 * description:      checks if configuration is valid
	 * @author           Tobias Reichling
	 * @param            ministeckSystem
	 * @param            configuration
	 * @return           true, false
	 */
	private boolean consistencyCheck(Configuration ministeckSystem, Configuration configuration){
		//elementsvektoren erstellen
		Vector systemConf = new Vector();
		Vector currentConf = new Vector();
		for (Enumeration systemEnum = ministeckSystem.getAllElements(); systemEnum.hasMoreElements(); ) {
			systemConf.add((ElementObject)systemEnum.nextElement());
		}
		for (Enumeration currentEnum = configuration.getAllElements(); currentEnum.hasMoreElements(); ) {
			currentConf.add((ElementObject)currentEnum.nextElement());
		}
		//not valid if the two vectors have different size!
		if (!(systemConf.size()==currentConf.size())){
			return false;
		}else{
			int counter = -1;
			boolean [][] systemMatrix;
			int systemWidth, systemHeight;
			ElementObject supportElement;
			for (Enumeration systemMatrixEnum = systemConf.elements(); systemMatrixEnum.hasMoreElements(); ) {
				supportElement = ((ElementObject)systemMatrixEnum.nextElement());
				systemMatrix = supportElement.getMatrix();
				systemWidth = supportElement.getWidth();
				systemHeight = supportElement.getHeight();
				counter = 0;
				//check each element from vector 1 to each element from vector 2
				boolean found = false;
				while ((counter < currentConf.size()) && (!found)){
					if ((systemWidth == ((ElementObject)currentConf.get(counter)).getWidth()) &&
							(systemHeight == ((ElementObject)currentConf.get(counter)).getHeight())){
						if (elementEqual(((ElementObject)currentConf.get(counter)).getMatrix(), systemMatrix, systemWidth, systemHeight)){
							found = true;
							currentConf.remove(counter);
						}
					}
					counter++;
				}
				if (!found){return false;}
			}
			return true;
		}
	}
	
	/**
	 * method:           elementEqual
	 * description:      checks 2 boolean arrays and returns true
	 *                   if the arrays are equal
	 * @author           Tobias Reichling
	 * @param            matrix1
	 * @param            matrix2
	 * @param            width
	 * @param            height
	 * @return           result: true bzw. false
	 */
	private boolean elementEqual(boolean[][] matrix1, boolean[][] matrix2, int width, int height){
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				if (!(matrix1[i][j] == matrix2[i][j])){
					return false;
				}
			}
		}
		return true;
	}
}