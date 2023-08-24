package PicToBrick;

import java.awt.image.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.*;

import javax.swing.SwingUtilities;

/**
 * class:            NaiveQuantisierungLab
 * layer:            DataProcessing (three tier architecture)
 * description:      simple quantisation - cielab - euclidean distance
 * @author           Adrian Schuetz
 */
public class NaiveQuantisationLab
implements Quantisation {
	
	private DataProcessing dataProcessing;
	private Calculation calculation;
	private int percent = 0;
	private int rows = 0;
	private int mosaicRow;

	/**
	 * method:           NaiveQuantisierungLab
	 * description:      constructor
	 * @author           Adrian Schuetz
	 * @param            dataProcessing      dataProcessing
	 * @param            calculation         calculation
	 */
	public NaiveQuantisationLab(DataProcessing dataProcessing, Calculation calculation){
		this.dataProcessing = dataProcessing;
		this.calculation = calculation;
	}
	
	/**
	 * method:           quantisation
	 * description:      Farbmatching
	 * @author           Adrian Schuetz
	 * @param            image
	 * @param            mosaicWidth
	 * @param            mosaicHeight
	 * @param            configuration
	 * @param            mosaic (empty)
	 * @return           mosaic (with color information)
	 */
	public Mosaic quantisation(BufferedImage image,
			                   int mosaicWidth,
			                   int mosaicHeight,
			                   Configuration configuration,
			                   Mosaic mosaic){
		this.rows = mosaicHeight;
		//scale image to mosaic dimensions
		BufferedImage cutout = calculation.scale(image, mosaicWidth, mosaicHeight, dataProcessing.getInterpolation());
		String colorName = new String("");
		//compute pixelMatrix
	    int[][][] pixelMatrix = calculation.pixelMatrix(cutout);
	    //create a vector of all lab colors
		Vector labColors = new Vector();
	    for (Enumeration colorEnumeration = configuration.getAllColors(); colorEnumeration.hasMoreElements(); ) {
			ColorObject color = (ColorObject)(colorEnumeration.nextElement());
			labColors.add(calculation.rgbToLab(color.getRGB()));
			labColors.add(color.getName());
		}
	    //farb matching - cielab - euclidean distance
	    //compute every sRGB color to CieLAB color
	    int red, green, blue;
	    Lab lab, lab2;
	    String name;
	    double difference;
	    for (mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++){
			//submit progress bar refresh-function to the gui-thread
			try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
				percent = (int)((100.0/rows)*mosaicRow);
				dataProcessing.refreshProgressBarAlgorithm(percent,1);
			}});}catch(Exception e){System.out.println(e.toString());}
			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++){
				double minimumDifference = 500.0;
				red = pixelMatrix[mosaicRow][mosaicColumn][0];
				green = pixelMatrix[mosaicRow][mosaicColumn][1];
				blue = pixelMatrix[mosaicRow][mosaicColumn][2];
				lab = calculation.rgbToLab(new Color(red,green,blue));
				for (Enumeration colorEnumeration2 = labColors.elements(); colorEnumeration2.hasMoreElements(); ) {
					lab2 = (Lab)(colorEnumeration2.nextElement());
					name = (String)(colorEnumeration2.nextElement());
					difference =	java.lang.Math.sqrt(java.lang.Math.pow(lab.getL()-lab2.getL(),2.0) +
															java.lang.Math.pow(lab.getA()-lab2.getA(),2.0) +
															java.lang.Math.pow(lab.getB()-lab2.getB(),2.0));
					if (difference < minimumDifference){
						minimumDifference = difference;
						colorName = name;
					}
				}
				//sets found color to the mosaic
				mosaic.setElement(mosaicRow,mosaicColumn,colorName,false);
			}
		}	   
	    //submit progress bar refresh-function to the gui-thread
	    try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
			dataProcessing.refreshProgressBarAlgorithm(100,1);
		}});}catch(Exception e){System.out.println(e.toString());}
		return mosaic;
	}
}