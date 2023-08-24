package PicToBrick;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.*;

/**
 * class:            Calculation
 * layer:            DataProcessing (three tier architecture)
 * description:      untility class with mathematical methods
 * @author           Tobias Reichling / Adrian Schuetz
 */
public class Calculation {
	
	/**
	 * method:           labToRgb
	 * description:      computes a s-rgb color object from a given cie-lab color object
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            lab
	 * @return           rgb
	 */
	public Color labToRgb(Lab lab){
		double x,y,z,r,g,b;		
		//----------------------------------------------------
		y = (lab.getL() + 16.0) / 116.0;
		x = lab.getA() / 500.0 + y;
		z = y - lab.getB() / 200.0;
		//----------------------------------------------------
		if(java.lang.Math.pow(y, 3.0) > 0.008856){
			y = java.lang.Math.pow(y, 3.0);
		}else{
			y = ( y - 16.0 / 116.0 ) / 7.787;
		}
		if(java.lang.Math.pow(x, 3.0) > 0.008856){
			x = java.lang.Math.pow(x, 3.0);
		}else{
			x = ( x - 16.0 / 116.0 ) / 7.787;
		}
		if(java.lang.Math.pow(z, 3.0) > 0.008856){
			z = java.lang.Math.pow(z, 3.0);
		}else{
			z = ( z - 16.0 / 116.0 ) / 7.787;
		}
		//----------------------------------------------------
		x = x *  95.047 / 100.0;  //  >
		y = y * 100.000 / 100.0;  //   > reference values: CIE Observer= 2°, Illuminant= D65
		z = z * 108.883 / 100.0;  //  >
		//----------------------------------------------------
		r = x *  3.2406 + y * -1.5372 + z * -0.4986;
		g = x * -0.9689 + y * 1.8758  + z *  0.0415;
		b = x *  0.0557 + y * -0.2040 + z *  1.0570;
		//----------------------------------------------------
		if (r > 0.0031308){
			r = 1.055 * java.lang.Math.pow(r, (1.0 / 2.4)) - 0.055;
		}else{
			r = 12.92 * r;
		}
		if (g > 0.0031308){
			g = 1.055 * java.lang.Math.pow(g, (1.0 / 2.4)) - 0.055;
		}else{
			g = 12.92 * g;
		}
		if (b > 0.0031308){
			b = 1.055 * java.lang.Math.pow(b, (1.0 / 2.4)) - 0.055;
		}else{
			b = 12.92 * b;
		}
		//----------------------------------------------------
		Color rgb = new Color(java.lang.Math.round((float)(r * 255.0)),java.lang.Math.round((float)(g * 255.0)),java.lang.Math.round((float)(b * 255.0)));
		return rgb;
	}
	
	/**
	 * method:           rgbToLab
	 * description:      computes a cie-lab color object from a given s-rgb color object
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            rgb
	 * @return           lab
	 */
	public Lab rgbToLab(Color rgb){
		double r,g,b,x,y,z;
		//----------------------------------------------------
		r = (double)rgb.getRed() / 255.0;
		g = (double)rgb.getGreen() / 255.0;
		b = (double)rgb.getBlue() / 255.0;
		//----------------------------------------------------
		if (r > 0.04045){
			r = java.lang.Math.pow(((r + 0.055 ) / 1.055 ),2.4);
		}else{
			r = r / 12.92;
		}
		if (g > 0.04045){
			g = java.lang.Math.pow(((g + 0.055 ) / 1.055 ),2.4);
		}else{
			g = g / 12.92;
		}
		if (b > 0.04045){
			b = java.lang.Math.pow(((b + 0.055 ) / 1.055 ), 2.4);
		}else{
			b = b / 12.92;
		}
		//----------------------------------------------------
		r = r * 100.0;
		g = g * 100.0;
		b = b * 100.0;
		//----------------------------------------------------
		x = r * 0.412424 + g * 0.357579 + b * 0.180464;
		y = r * 0.212656 + g * 0.715158 + b * 0.072186;
		z = r * 0.019332 + g * 0.119193 + b * 0.950444;
		//----------------------------------------------------
		x = x /  95.047;  //   >
		y = y / 100.000;  //    > reference values: CIE Observer= 2°, Illuminant= D65
		z = z / 108.883;  //   >
		//----------------------------------------------------
		if (x > 0.008856){
			x = java.lang.Math.pow(x, (1.0 / 3.0));
		}else{
			x = (7.787 * x) + (16.0 / 116.0);
		}
		if (y > 0.008856){
			y = java.lang.Math.pow(y, (1.0 / 3.0));
		}else{
			y = (7.787 * y) + (16.0 / 116.0);
		}
		if (z > 0.008856){
			z = java.lang.Math.pow(z, (1.0 / 3.0));
		}else{
			z = (7.787 * z) + (16.0 / 116.0);
		}
		//----------------------------------------------------
		Lab lab = new Lab((116.0 * y ) - 16.0,
				500.0 * ( x - y ),
				200.0 * ( y - z ));
		return lab;
	}
	
	/**
	 * method:           scale
	 * description:      scaling a picture
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            image                  source image
	 * @param            destinationWidth       destination image width
	 * @param            destinationHeight      destination image height
	 * @param            interpolation          1=bicubic, 2=bilinear, 3=nearestneighbor 
	 * @return           destination image
	 */
	public BufferedImage scale(BufferedImage image, int destinationWidth, int destinationHeight, int interpolation){
		double factorX = ((double)destinationWidth/(double)image.getWidth());
		double factorY = ((double)destinationHeight/(double)image.getHeight());
		BufferedImage imageScaled = new BufferedImage(destinationWidth, destinationHeight, image.getType());
		Graphics2D g2 = imageScaled.createGraphics();
		switch (interpolation){
		case 1:{
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			break;
		}
		case 2:{
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			break;
		}
		case 3:{
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			break;
		}
		default:{
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			break;
		}}
		AffineTransform at = AffineTransform.getScaleInstance(factorX, factorY);
		g2.drawRenderedImage(image, at);
		return imageScaled;
	}
	
	/**
	 * method:           pixelMatrix
	 * description:      computes a matrix of rgb-values from the source image
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            image (including information: width and height)
	 * @return           int[][][]   pixelMatrix
	 */
	public int[][][] pixelMatrix(BufferedImage image){
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int pixels[] = new int[imageHeight * imageWidth];
		int pixelMatrix[][][] = new int[imageHeight][imageWidth][3];
		PixelGrabber grabber = new PixelGrabber(image, 0,0,imageWidth,imageHeight,pixels,0,imageWidth);
		try
	    {
			grabber.grabPixels();
			for(int row=0; row < imageHeight; row++)
			{
				for(int column=0; column < imageWidth; column++)
				{
					int pixel = pixels[row*imageWidth + column];
					pixelMatrix[row][column][0] = (pixel >> 16) & 0xff; 	//red
					pixelMatrix[row][column][1] = (pixel >> 8) & 0xff;	    //green
					pixelMatrix[row][column][2] = (pixel)  &  0xff;		    //blue
				}
			}
	    }
	    catch ( InterruptedException e ){}
	    return pixelMatrix;
	}
	
	/**
	 * method:           randomCoordinates
	 * description:      computes random coordinates
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            mosaic width
	 * @param            mosaic height
	 * @return           random coordinates as vector
	 */
	public Vector randomCoordinates(int width, int height){
		Vector randomCoordinates = new Vector();
		Random random = new Random();
		int[][] coordinates = new int[2][(height*width)];
		int position;
		int tempX, tempY;
		//initialize coordinates
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				coordinates[0][((i*width)+j)] = i;
				coordinates[1][((i*width)+j)] = j;
	    }}
		//randomize coordinates
		for (int i = 0; i < (width*height); i++) {
		    position = random.nextInt(width*height);
		    tempX = coordinates[0][i];
		    tempY = coordinates[1][i];
		    coordinates[0][i] = coordinates[0][position];
		    coordinates[1][i] = coordinates[1][position];
		    coordinates[0][position] = tempX;
		    coordinates[1][position] = tempY;
		}
		//add coordinates to the vector
		for (int x = 0; x < (height*width); x++){
			randomCoordinates.add(coordinates[0][x]);
			randomCoordinates.add(coordinates[1][x]);
		}
		return randomCoordinates;
	}
	
	/**
	 * method:           hilbertCoordinates
	 * description:      computes coordinates along a hilbert curve
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            mosaic width
	 * @param            mosaic height
	 * @return           hilbert coordinates as vector
	 */
	public Vector hilbertCoordinates(int mosaicWidth, int mosaicHeight){
		Vector hilbertCoordinates = new Vector();
		int length = 0;
		//length = largest mosaic dimension
		if(mosaicWidth > mosaicHeight){
			length = mosaicWidth;
		}else{
			length = mosaicHeight;
		}
		//computes the recursion depth from the length
		int n = 1;
		while(Math.pow(2, n) < length){
			n++;
		}
		//raise the length to the power of 2 because
		//we need a square (2^int x 2^int) to compute the hilbert coordinates
		length = (int)Math.pow(2, n);
		//
		//  type 0    type 1    type 2    type 3
		//  +------+  /\     +  +------+  <------+  
		//  |         |      |  |      |         |    
		//  |         |      |  |      |         |  
		//  +------>  +------+  +     \/  +------+  
		//
		//start recursion with type 0
		hilbert(0,0,0,length,n,hilbertCoordinates, mosaicWidth, mosaicHeight);
		return hilbertCoordinates;
	}

	/**
	 * method:           hilbert
	 * description:      computes coordinates along a hilbert curve (recursion)
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            mosaic width
	 * @param            mosaic height
	 * @return           hilbert coordinates as vector
	 */
	private void hilbert(int type,int x,int y,int length,int n, Vector coordinates, int mosaicWidth, int mosaicHeight){
		//l2 = half length
		//for computing the four subsquares
		int l2=length/2;
		//add the coordinates to the vector when the lowest recursions depth is reached
		if(n==0){
			//coordinates are interchanged because we need them
			//to index values in an array
			//add only coordinates which are positioned in the original mosaic dimensions
			if(y < mosaicHeight && x < mosaicWidth){
				coordinates.add(y);
				coordinates.add(x);
			}
		}
		//if not the lowest recursions depth is reached, call deeper recursions
		else{
			//decrements the recursions depth
			n=n-1;
			//
			//  type 0    type 1    type 2    type 3
			//  +------+  /\     +  +------+  <------+  
			//  |         |      |  |      |         |    
			//  |         |      |  |      |         |  
			//  +------>  +------+  +     \/  +------+  
			//
			if (type == 0) {
				hilbert(1, x+l2, y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(0, x,    y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(0, x,    y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(2, x+l2, y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
			}
			if (type == 1) {
				hilbert(0, x+l2, y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(1, x+l2, y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(1, x,    y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(3, x,    y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
			}
			if (type == 2) { 
				hilbert(3, x,    y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(2, x,    y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(2, x+l2, y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(0, x+l2, y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
			}
			if (type == 3) {   
				hilbert(2, x,    y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(3, x+l2, y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(3, x+l2, y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(1, x,    y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
			}
		}
	}
}
