package PicToBrick;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

/**
 * class:           Picture
 * layer:           DataManagement (three tier architecture)
 * description:     contains all information and methods for an image
 * @author          Adrian Schuetz
 */

public class Picture
extends Canvas
{
	private BufferedImage image;
	private BufferedImage scaledImage;
	private double factor = 1.0;
	private String imageName = "";
	private double scalingFactor;
	
	/**
	 * method:           Picture
	 * description:      constructor
	 * @author           Adrian Schuetz
	 * @param            file (image file)
	 * @exception        exception
	 */
	public Picture(File file) throws IOException
	{
		loadImage(file);
		this.imageName = file.getName();
	}
	
	/**
	 * method:           Picture
	 * description:      constructor
	 * @author           Adrian Schuetz
	 * @param            image
	 */
	public Picture(BufferedImage image)
	{
		this.image = image;
	}
	
	/**
	 * method:           loadImage
	 * description:      loads an image file
	 * @author           Adrian Schuetz
	 * @param            file (image file)
	 * @exception        exception
	 */
	private void loadImage(File file) throws IOException
	{
		this.image = ImageIO.read(file);
		if(image.getType() == 0){
			//if type is custom do not load image
			this.image = null;
		}
	}

	
	/**
	 * method:           cutout
	 * description:      replace the image with a cutout of the original image
	 * @author           Adrian Schuetz
	 * @param            cutout rectangle
	 */
	public void cutout(Rectangle rectangle){
		//map rectangle coordinates from scaled image to original image
		int startX = java.lang.Math.round((float)(rectangle.x/scalingFactor));
		int startY = java.lang.Math.round((float)(rectangle.y/scalingFactor));
		int endX = java.lang.Math.round((float)((rectangle.x + rectangle.width)/scalingFactor));
		int endY = java.lang.Math.round((float)((rectangle.y + rectangle.height)/scalingFactor));
		//the cutout is processed from the scaled image
		this.image = this.image.getSubimage(startX, startY, (endX - startX), (endY - startY));
	}
	
	/**
	 * method:           scaleImage
	 * description:      scales the image
	 * @author           Adrian Schuetz
	 * @param            scaleFactor
	 */
	private void scaleImage (double scaleFactor)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int scaleWidth = (int)(width*scaleFactor);
		int scaleHeight = (int)(height*scaleFactor);
		this.scaledImage = new BufferedImage(scaleWidth, scaleHeight, image.getType());
		//creates an graphics2d object, which is used to draw the scaled image
		Graphics2D g2 = scaledImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		AffineTransform at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
		g2.drawRenderedImage(image, at);
	}
	
	/**
	 * method:           getImage
	 * description:      returns the image
	 * @author           Adrian Schuetz
	 * @return           image
	 */
	public BufferedImage getImage()
	{
		return image;
	}
	
	/**
	 * method:           getImageName
	 * description:      returns the image name
	 * @author           Adrian Schuetz
	 * @return           image name
	 */
	public String getImageName()
	{
		return this.imageName;
	}
	
	/**
	 * method:           computeScaleFactor
	 * description:      computes the scale factor from the user-defined rectangle
	 * @author           Adrian Schuetz
	 * @param            width 
	 * @param            height
	 */
	public void computeScaleFactor(double width, double height)
	{
		double imageWidth = (double)image.getWidth();
		double imageHeight = (double)image.getHeight();
		double rectangleWidth; 
		double rectangleHeight;
		if(width <= 0){
			rectangleWidth = 10;
		}else{
			rectangleWidth = width;
		}
		if(height <= 0){
			rectangleHeight = 10;
		}else{
			rectangleHeight = height;
		}
		//compute factor
		if( (rectangleHeight / rectangleWidth) >= (imageHeight / imageWidth) )
		{
			this.factor = rectangleWidth / imageWidth;
		}
		else
		{
			this.factor = rectangleHeight / imageHeight;
		}
	}
	
	/**
	 * method:           getScaledImage
	 * description:      returns a scaled image to a given slider value
	 * @author           Adrian Schuetz
	 * @return           scaled image
	 */
	public BufferedImage getScaledImage(int sliderValue)
	{	
		this.scalingFactor = factor * Math.pow((Math.sqrt(2.0)), (sliderValue-3));
		scaleImage (scalingFactor);
		return this.scaledImage;
	}
	
	/**
	 * method:           paint
	 * description:      overwrites the paint method (Canvas)
	 * @author           Adrian Schuetz
	 * @param            g2d
	 */
	public void paint(Graphics g2d)
	{
		g2d.drawImage(image,1,1,this);
	}
	
	/**
	 * method:           getHeight
	 * description:      returns the image height
	 * @author           Adrian Schuetz
	 * @return           image height
	 */
	public int getHeight()
	{
		return image.getHeight(this);
	}
	
	/**
	 * method:           getWidth
	 * description:      returns the image width
	 * @author           Adrian Schuetz
	 * @return           image width
	 */
	public int getWidth()
	{
		return image.getWidth(this);
	}
	
	/**
	 * method:           getPixelMatrix
	 * description:      returns a 3D pixel matrix (width, height, rgb)
	 * @author           Adrian Schuetz
	 * @return           pixel matrix
	 */
	public int[][][] getPixelMatrix()
	{
		final int width = image.getWidth(this), height = image.getHeight(this);
		final int pixels[] = new int[height * width];
		int pixelMatrix[][][] = new int[height][width][3];
		PixelGrabber grabber = new PixelGrabber(image, 0,0,width,height,pixels,0,width);
		try
	    {
			grabber.grabPixels();
			for(int row=0; row < height; row++)
			{
				for(int column=0; column < width; column++)
				{
					int pixel = pixels[row*width + column];
					pixelMatrix[row][column][0] = (pixel >> 16) & 0xff; 	//red
					pixelMatrix[row][column][1] = (pixel >> 8) & 0xff;	    //green
					pixelMatrix[row][column][2] = (pixel)  &  0xff;		    //blue
				}
			}
	    }
	    catch ( InterruptedException e ){}
	    return pixelMatrix;
	}
}
