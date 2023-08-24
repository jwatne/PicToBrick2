package PicToBrick;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.JPanel;

/**
 * class:           PictureElement
 * layer:           Gui (three tier architecture)
 * description:     image component for gui
 * @author          Adrian Schuetz
 */
public class PictureElement
extends JPanel
implements MouseMotionListener, MouseListener
{
	private BufferedImage image;
	private Rectangle rectangle;
	private boolean isMoving;
	private boolean isRectangleExisting = false;
	private Image dbImage;
	private Graphics dbGraphics;
	private double ratioX = 0.0, ratioY = 0.0;
	//selection-rectangle
	private double rectangleStartX = 0.0;
	private double rectangleStartY = 0.0;
	private double rectangleEndX = 0.0;
	private double rectangleEndY = 0.0;
	private double rectangleStartBeforeAdjustmentX;
	private double rectangleStartBeforeAdjustmentY;
	//mouse-positions
	private double mousePositionRectangleRaisingX;
	private double mousePositionRectangleRaisingY;
	private double mousePositionCurrentX;
	private double mousePositionCurrentY;
	private double mousePositionBeforeAdjustmentX;
	private double mousePositionBeforeAdjustmentY;
	
	private MainWindow mainWindow;

	/**
	 * method:           PictureElement
	 * description:      constructor
	 * @author           Adrian Schuetz
	 */
	public PictureElement(MainWindow mainWindow){
		this.mainWindow = mainWindow;
		this.rectangle = new Rectangle(0, 0);
	}
	
	/**
	 * method:           PictureElement
	 * description:      constructor
	 * @author           Adrian Schuetz
	 * @param            image
	 */
	public PictureElement(BufferedImage image){
		this.image = image;
		this.rectangle = new Rectangle(image.getWidth(), image.getHeight());
	}

	/**
	 * method:         paintComponent
	 * description:    overwrites the paintCompoment method
	 * @author         Adrian Schuetz
	 * @param          graph
	 */
	protected void paintComponent(Graphics graph){
		super.paintComponent(graph);
		Graphics2D g2d = (Graphics2D)graph;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(image, 0, 0, this);
	}

	/**
	 * method:         paint
	 * description:    overwrites the paint method
	 * @author         Adrian Schuetz
	 * @param          graph Graphics
	 */
	public void paint(Graphics graph){
		super.paint(graph);
		//paints only if a rectangle is existing
		if((int)rectangleEndX > 0 && (int)rectangleEndY > 0){
			graph.setColor(new Color(255, 0, 0));
			graph.drawRect((int)rectangleStartX, (int)rectangleStartY, (int)(rectangleEndX), (int)(rectangleEndY));
			graph.setColor(new Color(0, 0, 255));
			graph.drawRect((int)rectangleStartX+1, (int)rectangleStartY+1, (int)(rectangleEndX)-2, (int)(rectangleEndY)-2);
		}
	}
	
	/**
	 * method:         update
	 * description:    double buffering the image
	 *                 writes the whole image information in an image object
	 *                 shows this image object AFTER all operations are closed
	 * 				   q.v. Krueger Handbuch der Programmierung Listing 34.14
	 * @author         Adrian Schuetz
	 * @param          graph Graphics
	 */
	public void update(Graphics graph){
		//initialisize double buffering
		if (dbImage == null) {
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbGraphics = dbImage.getGraphics();
		}
		//deletes background
		dbGraphics.setColor(getBackground());
		dbGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
		//draw foreground
		dbGraphics.setColor(getForeground());
		paint(dbGraphics);
		//show
		graph.drawImage(dbImage,0,0,this);
	}
	
	/**
	 * method:         getPreferredSize
	 * description:    overwrites the getPreferredSize method
	 * @author         Adrian Schuetz
	 * @return         dimension
	 */
	public Dimension getPreferredSize(){
		return new Dimension(rectangle.width, rectangle.height);
	}
	
	/**
	 * method:         getCutoutRectangle
	 * description:    returns the cutout
	 * @author         Adrian Schuetz
	 * @return         rectangle
	 */
	public Rectangle getCutoutRectangle(){
		Rectangle cutoutRectangle = new Rectangle((int)rectangleStartX, (int)rectangleStartY, (int)rectangleEndX, (int)rectangleEndY);
		//reset
		rectangleStartX = 0.0;
		rectangleStartY = 0.0;
		rectangleEndX = 0.0;
		rectangleEndY = 0.0;
		this.isRectangleExisting = false;
		return cutoutRectangle;
	}

	/**
	 * method:         setImage
	 * description:    sets the image
	 * @author         Adrian Schuetz
	 * @param          image
	 */
	public void setImage(BufferedImage image){
		this.image = image;
		if(this.image != null){
			rectangle = new Rectangle(image.getWidth(), image.getHeight());
		}else{
			rectangle = new Rectangle(0, 0);
		}
	}
	
	/**
	 * method:         setCutoutRatio
	 * description:    sets the ratio
	 * @author         Adrian Schuetz
	 * @param          dimension
	 */
	public void setCutoutRatio(Dimension dimension){
		this.ratioX = dimension.getWidth();
		this.ratioY = dimension.getHeight();
	}
	
	/**
	 * method:         isCutout
	 * description:    returns if a rectangle is cutted out or not
	 * @author         Adrian Schuetz
	 * @return         true or false
	 */
	public boolean isCutout(){		
		if((int)rectangleEndX == 0 && (int)rectangleEndY == 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * method:         mousePressed 
	 * description:    mouseListener
	 * @author         Adrian Schuetz
	 * @param          event
	 */
	public void mousePressed(MouseEvent e){
		//checks if the mouse cursor is within the rectangle or not
		if(e.getX() > rectangleStartX && e.getX() < (rectangleStartX+rectangleEndX) && e.getY() > rectangleStartY && e.getY() < (rectangleStartY+rectangleEndY)){
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.isMoving = true;
		}else{
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			this.isMoving = false;
		}
		//----------------------------------------------------------
		if(this.isMoving){
			//if the rectangle is moved by user
			mousePositionBeforeAdjustmentX = e.getX();
			mousePositionBeforeAdjustmentY = e.getY();
		}else{
			//if the rectangle is raised by user
			mousePositionRectangleRaisingX = e.getX();
			mousePositionRectangleRaisingY = e.getY();
			rectangleStartX = mousePositionRectangleRaisingX;
			rectangleStartY = mousePositionRectangleRaisingY;
			rectangleStartBeforeAdjustmentX = rectangleStartX;
			rectangleStartBeforeAdjustmentY = rectangleStartY;
			this.isRectangleExisting = true;			
		}
	}

	/**
	 * method:         mouseReleased
	 * description:    mouseListener
	 * @author         Adrian Schuetz
	 * @param          event
	 */
	public void mouseReleased(MouseEvent e){
		if(this.isMoving){
			rectangleStartBeforeAdjustmentX = rectangleStartX;
			rectangleStartBeforeAdjustmentY = rectangleStartY;
		}
	}

	/**
	 * method:         mouseDragged
	 * description:    mouseListener
	 * @author         Adrian Schuetz
	 * @param          event
	 */
	public void mouseDragged(MouseEvent e){
		//get current mouse cursor position
		mousePositionCurrentX = e.getX();
		mousePositionCurrentY = e.getY();
		if(this.isMoving){
			//rectangle is moving
			double rectangleStartAltX = rectangleStartX;
			double rectangleStartAltY= rectangleStartY;
			//computes new start position
			rectangleStartX = (mousePositionCurrentX) - ( (mousePositionBeforeAdjustmentX) - (rectangleStartBeforeAdjustmentX) );
			rectangleStartY = (mousePositionCurrentY) - ( (mousePositionBeforeAdjustmentY) - (rectangleStartBeforeAdjustmentY) );
			//checks if the rectangle is moved outside the image
			if ((int)((rectangleStartX+rectangleEndX)+1) > image.getWidth()  || (int)rectangleStartX < 0){
				rectangleStartX = rectangleStartAltX;
			}
			if ((int)((rectangleStartY+rectangleEndY)+1) > image.getHeight() || (int)rectangleStartY < 0){
				rectangleStartY = rectangleStartAltY;
			}
			repaint();
		}else{
			//rectangle is raised by user
			mainWindow.guiStatus(14);
			//caching
			double rectangleEndAltX = rectangleEndX;
			double rectangleEndAltY = rectangleEndY;
			//allows the user only to raise the rectangle to right and/or to bottom
			if (mousePositionCurrentX > mousePositionRectangleRaisingX && mousePositionCurrentY > mousePositionRectangleRaisingY)
			{
				//computes the rectangle coordinates
				if (((mousePositionCurrentX - mousePositionRectangleRaisingX) / (mousePositionCurrentY - mousePositionRectangleRaisingY)) >= (ratioX / ratioY))
				{
					rectangleEndY = mousePositionCurrentY - mousePositionRectangleRaisingY;
					rectangleEndX = rectangleEndY * (ratioX / ratioY);
				}
				else
				{
					rectangleEndX = mousePositionCurrentX - mousePositionRectangleRaisingX;
					rectangleEndY = rectangleEndX / (ratioX / ratioY);
				}
				//checks if the rectangle coordinates are within the image
				if ((rectangleEndX+mousePositionRectangleRaisingX) >= image.getWidth() || (rectangleEndY+mousePositionRectangleRaisingY) >= image.getHeight())
				{
					rectangleEndX = rectangleEndAltX;	
					rectangleEndY = rectangleEndAltY;
				}
				repaint();
			}
		}
	}
	
	/**
	 * method:         mouseClicked
	 * description:    mouseListener
	 * @author         Adrian Schuetz
	 * @param          event
	 */
	public void mouseClicked(MouseEvent event)
	{
		//biggest rectangle by doppleclick and pressed shift-key
		//in the top left corner
		if(event.isShiftDown() && event.getClickCount()>1){
			rectangleStartX = 0;
			rectangleStartY = 0;
			rectangleStartBeforeAdjustmentX = 0;
			rectangleStartBeforeAdjustmentY = 0;
			if (((double)image.getWidth()/(double)image.getHeight()) > (ratioX / ratioY)){
				//height deciding
				rectangleEndY = image.getHeight()-1;
				rectangleEndX = (rectangleEndY / ratioY) * ratioX;
			}else{
				//width deciding
				rectangleEndX = image.getWidth()-1;
				rectangleEndY = (rectangleEndX / ratioX) * ratioY;
			}
			mainWindow.guiStatus(14);
			this.isRectangleExisting = true;
			repaint();
		}else if(this.isMoving && event.getClickCount()>1){
			mainWindow.cutout();
		}else if(!this.isMoving && this.isRectangleExisting){
			mainWindow.guiStatus(13);
			//delete old rectangle
			rectangleStartX = 0.0;
			rectangleStartY = 0.0;
			rectangleEndX = 0.0;
			rectangleEndY = 0.0;
			this.isRectangleExisting = false;
			repaint();
		}
	}
	
	/**
	 * method:         mouseEntered
	 * description:    mouseListener
	 * @author         Adrian Schuetz
	 * @param          event
	 */
	public void mouseEntered(MouseEvent event){}
	
	/**
	 * method:         mouseExited
	 * description:    mouseListener
	 * @author         Adrian Schuetz
	 * @param          event
	 */
	public void mouseExited(MouseEvent event){}

	/**
	 * method:         mouseMoved
	 * description:    mouseListener
	 * @author         Adrian Schuetz
	 * @param          event
	 */
	public void mouseMoved(MouseEvent e)
	{
		//checks if the mouse cursor is within the rectangle or not
		if(e.getX() > rectangleStartX && e.getX() < (rectangleStartX+rectangleEndX) && e.getY() > rectangleStartY && e.getY() < (rectangleStartY+rectangleEndY)){
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.isMoving = true;
		}else{
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			this.isMoving = false;
		}
	}
}
