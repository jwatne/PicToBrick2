package PicToBrick.service;

import javax.swing.*;

import PicToBrick.model.Configuration;
import PicToBrick.model.Mosaic;

/**
 * class:            BasicElementsOnly
 * layer:            Data processing (three tier architecture)
 * description:      tiling with basis elements
 * @author           Adrian Schuetz
 */
public class BasicElementsOnly
implements Tiling {

	private DataProcessing dataProcessing;
	private int rows = 0;
	private int procent = 0;
	private boolean statisticOutput;
	private int colorRow;

	/**
	 * method:           BasicElementsOnly
	 * description:      construkcor
	 * @author           Adrian Schuetz
	 * @param            DataProcessing  dataProcessing
	 * @param            Calculation     calculation
	 */
	public BasicElementsOnly(DataProcessing dataProcessing, Calculation calculation){
		this.dataProcessing = dataProcessing;
	}

	/**
	 * method:           tiling
	 * description:      tile every pixel with a basiselement
	 * @author           Adrian Schuetz
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
		this.statisticOutput = statistic;
		rows = mosaicHeight;
		procent = 0;
		for (colorRow = 0; colorRow < mosaicHeight; colorRow++){
			//assign progress bar controls to the GUI thread
			try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
				procent = (int)((100.0/rows)*colorRow);
				if (statisticOutput){
					dataProcessing.refreshProgressBarAlgorithm(procent,4);
				}else{
					dataProcessing.refreshProgressBarAlgorithm(procent,2);
				}
			}});}catch(Exception e){System.out.println(e.toString());}
			for (int colorColumn = 0; colorColumn < mosaicWidth; colorColumn++){
				mosaic.setElement(colorRow,colorColumn,configuration.getBasisName(),true);
    		}
		}
		//assign progress bar controls to the GUI thread
		try{SwingUtilities.invokeAndWait(new Runnable(){public void run(){
			if (statisticOutput){
				dataProcessing.refreshProgressBarAlgorithm(100,4);
			}else{
				dataProcessing.refreshProgressBarAlgorithm(100,2);
			}
		}});}catch(Exception e){System.out.println(e.toString());}
		return mosaic;
	}
}
