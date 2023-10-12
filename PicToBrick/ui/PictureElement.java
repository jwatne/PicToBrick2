package pictobrick.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pictobrick.ui.handlers.GuiStatusHandler;

/**
 * Image component for gui.
 *
 * @author Adrian Schuetz
 */
public class PictureElement extends JPanel
        implements MouseMotionListener, MouseListener {
    /** Image drawn. */
    private BufferedImage image;
    /** Rectangle. */
    private Rectangle rectangle;
    /** Indicates whether mouse is moving. */
    private boolean isMoving;
    /** Indicates whether a cutout rectangle exists. */
    private boolean isRectangleExisting = false;
    /** Double buffered image. */
    private Image dbImage;
    /** Double buffered Graphics. */
    private Graphics dbGraphics;
    /** Width of selected rectangle. */
    private double ratioX = 0.0;
    /** Height of selected rectangle. */
    private double ratioY = 0.0;
    // selection-rectangle
    /** Starting x coordinate for rectangle. */
    private double rectangleStartX = 0.0;
    /** Starting y coordinate for rectangle. */
    private double rectangleStartY = 0.0;
    /** Ending x coordinate for rectangle. */
    private double rectangleEndX = 0.0;
    /** Ending y coordinate for rectangle. */
    private double rectangleEndY = 0.0;
    /** Starting x coordinate of rectangle before an adjustment. */
    private double rectangleStartBeforeAdjustmentX;
    /** Starting y coordinate of rectangle before an adjustment. */
    private double rectangleStartBeforeAdjustmentY;
    // mouse-positions
    /** X coordinate if rectangle is raised by user. */
    private double mousePositionRectangleRaisingX;
    /** Y coordinate if rectangle is raised by user. */
    private double mousePositionRectangleRaisingY;
    /** Current X coordinate of mouse pointer. */
    private double mousePositionCurrentX;
    /** Current y coordinate of mouse pointer. */
    private double mousePositionCurrentY;
    /** X coordinate of mouse pointer before an adjustment. */
    private double mousePositionBeforeAdjustmentX;
    /** Y coordinate of mouse pointer before an adjustment. */
    private double mousePositionBeforeAdjustmentY;
    /** The main window of the application. */
    private MainWindow mainWindow;
    /** Handler for updating the GUI status for the MainWindow. */
    private GuiStatusHandler guiStatusHandler;

    /**
     * Constructor.
     *
     * @param parentWindow the parent window owning the PictureElement.
     *
     * @author Adrian Schuetz
     */
    public PictureElement(final MainWindow parentWindow) {
        this.mainWindow = parentWindow;
        this.guiStatusHandler = parentWindow.getGuiStatusHandler();
        this.rectangle = new Rectangle(0, 0);
    }

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param elementImage
     */
    public PictureElement(final BufferedImage elementImage) {
        this.image = elementImage;
        this.rectangle = new Rectangle(elementImage.getWidth(),
                elementImage.getHeight());
    }

    /**
     * Overrides the paintCompoment method.
     *
     * @author Adrian Schuetz
     * @param graph
     */
    @Override
    protected void paintComponent(final Graphics graph) {
        super.paintComponent(graph);
        final Graphics2D g2d = (Graphics2D) graph;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(image, 0, 0, this);
    }

    /**
     * Overrides the paint method.
     *
     * @author Adrian Schuetz
     * @param graph Graphics
     */
    @Override
    public void paint(final Graphics graph) {
        super.paint(graph);

        // paints only if a rectangle is existing
        if ((int) rectangleEndX > 0 && (int) rectangleEndY > 0) {
            graph.setColor(Color.RED);
            graph.drawRect((int) rectangleStartX, (int) rectangleStartY,
                    (int) (rectangleEndX), (int) (rectangleEndY));
            graph.setColor(Color.BLUE);
            graph.drawRect((int) rectangleStartX + 1, (int) rectangleStartY + 1,
                    (int) (rectangleEndX) - 2, (int) (rectangleEndY) - 2);
        }
    }

    /**
     * method: update description: double buffering the image writes the whole
     * image information in an image object shows this image object AFTER all
     * operations are closed q.v. Krueger Handbuch der Programmierung Listing
     * 34.14
     *
     * @author Adrian Schuetz
     * @param graph Graphics
     */
    public void update(final Graphics graph) {
        // initialisize double buffering
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbGraphics = dbImage.getGraphics();
        }

        // deletes background
        dbGraphics.setColor(getBackground());
        dbGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
        // draw foreground
        dbGraphics.setColor(getForeground());
        paint(dbGraphics);
        // show
        graph.drawImage(dbImage, 0, 0, this);
    }

    /**
     * Overrides the getPreferredSize method.
     *
     * @author Adrian Schuetz
     * @return dimension
     */
    public Dimension getPreferredSize() {
        return new Dimension(rectangle.width, rectangle.height);
    }

    /**
     * Returns the cutout.
     *
     * @author Adrian Schuetz
     * @return rectangle
     */
    public Rectangle getCutoutRectangle() {
        final Rectangle cutoutRectangle = new Rectangle((int) rectangleStartX,
                (int) rectangleStartY, (int) rectangleEndX,
                (int) rectangleEndY);
        // reset
        rectangleStartX = 0.0;
        rectangleStartY = 0.0;
        rectangleEndX = 0.0;
        rectangleEndY = 0.0;
        this.isRectangleExisting = false;
        return cutoutRectangle;
    }

    /**
     * Sets the image.
     *
     * @author Adrian Schuetz
     * @param elementImage
     */
    public void setImage(final BufferedImage elementImage) {
        this.image = elementImage;

        if (this.image != null) {
            rectangle = new Rectangle(elementImage.getWidth(),
                    elementImage.getHeight());
        } else {
            rectangle = new Rectangle(0, 0);
        }
    }

    /**
     * Sets the ratio.
     *
     * @author Adrian Schuetz
     * @param dimension
     */
    public void setCutoutRatio(final Dimension dimension) {
        this.ratioX = dimension.getWidth();
        this.ratioY = dimension.getHeight();
    }

    /**
     * Returns if a rectangle is cut out or not.
     *
     * @author Adrian Schuetz
     * @return true if the rectangle is cut out.
     */
    public boolean isCutout() {
        return !((int) rectangleEndX == 0 && (int) rectangleEndY == 0);
    }

    /**
     * MouseListener.
     *
     * @author Adrian Schuetz
     * @param e
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        // checks if the mouse cursor is within the rectangle or not
        if (e.getX() > rectangleStartX
                && e.getX() < (rectangleStartX + rectangleEndX)
                && e.getY() > rectangleStartY
                && e.getY() < (rectangleStartY + rectangleEndY)) {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.isMoving = true;
        } else {
            this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            this.isMoving = false;
        }

        // ----------------------------------------------------------
        if (this.isMoving) {
            // if the rectangle is moved by user
            mousePositionBeforeAdjustmentX = e.getX();
            mousePositionBeforeAdjustmentY = e.getY();
        } else {
            // if the rectangle is raised by user
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
     * MouseListener.
     *
     * @author Adrian Schuetz
     * @param e
     */
    @Override
    public void mouseReleased(final MouseEvent e) {
        if (this.isMoving) {
            rectangleStartBeforeAdjustmentX = rectangleStartX;
            rectangleStartBeforeAdjustmentY = rectangleStartY;
        }
    }

    /**
     * MouseListener.
     *
     * @author Adrian Schuetz
     * @param e
     */
    @Override
    public void mouseDragged(final MouseEvent e) {
        // get current mouse cursor position
        mousePositionCurrentX = e.getX();
        mousePositionCurrentY = e.getY();

        if (this.isMoving) {
            // rectangle is moving
            final double rectangleStartAltX = rectangleStartX;
            final double rectangleStartAltY = rectangleStartY;
            // computes new start position
            rectangleStartX = (mousePositionCurrentX)
                    - ((mousePositionBeforeAdjustmentX)
                            - (rectangleStartBeforeAdjustmentX));
            rectangleStartY = (mousePositionCurrentY)
                    - ((mousePositionBeforeAdjustmentY)
                            - (rectangleStartBeforeAdjustmentY));

            // checks if the rectangle is moved outside the image
            if ((int) ((rectangleStartX + rectangleEndX) + 1) > image.getWidth()
                    || (int) rectangleStartX < 0) {
                rectangleStartX = rectangleStartAltX;
            }

            if ((int) ((rectangleStartY + rectangleEndY) + 1) > image
                    .getHeight() || (int) rectangleStartY < 0) {
                rectangleStartY = rectangleStartAltY;
            }

            repaint();
        } else {
            // rectangle is raised by user
            guiStatusHandler.guiStatus(
                    GuiStatusHandler.CUTOUT_WITH_RECTANGLE_AVAILABLE);
            // caching
            final double rectangleEndAltX = rectangleEndX;
            final double rectangleEndAltY = rectangleEndY;

            // allows the user only to raise the rectangle to right and/or to
            // bottom
            if (mousePositionCurrentX > mousePositionRectangleRaisingX
                    && mousePositionCurrentY > mousePositionRectangleRaisingY) {
                // computes the rectangle coordinates
                if (((mousePositionCurrentX - mousePositionRectangleRaisingX)
                        / (mousePositionCurrentY
                                - mousePositionRectangleRaisingY)) >= (ratioX
                                        / ratioY)) {
                    rectangleEndY = mousePositionCurrentY
                            - mousePositionRectangleRaisingY;
                    rectangleEndX = rectangleEndY * (ratioX / ratioY);
                } else {
                    rectangleEndX = mousePositionCurrentX
                            - mousePositionRectangleRaisingX;
                    rectangleEndY = rectangleEndX / (ratioX / ratioY);
                }

                // checks if the rectangle coordinates are within the image
                if ((rectangleEndX + mousePositionRectangleRaisingX) >= image
                        .getWidth()
                        || (rectangleEndY
                                + mousePositionRectangleRaisingY) >= image
                                        .getHeight()) {
                    rectangleEndX = rectangleEndAltX;
                    rectangleEndY = rectangleEndAltY;
                }

                repaint();
            }
        }
    }

    /**
     * MouseListener.
     *
     * @author Adrian Schuetz
     * @param event
     */
    @Override
    public void mouseClicked(final MouseEvent event) {
        // biggest rectangle by doppleclick and pressed shift-key
        // in the top left corner
        if (event.isShiftDown() && event.getClickCount() > 1) {
            rectangleStartX = 0;
            rectangleStartY = 0;
            rectangleStartBeforeAdjustmentX = 0;
            rectangleStartBeforeAdjustmentY = 0;

            if (((double) image.getWidth()
                    / (double) image.getHeight()) > (ratioX / ratioY)) {
                // height deciding
                rectangleEndY = image.getHeight() - 1;
                rectangleEndX = (rectangleEndY / ratioY) * ratioX;
            } else {
                // width deciding
                rectangleEndX = image.getWidth() - 1;
                rectangleEndY = (rectangleEndX / ratioX) * ratioY;
            }

            guiStatusHandler.guiStatus(
                    GuiStatusHandler.CUTOUT_WITH_RECTANGLE_AVAILABLE);
            this.isRectangleExisting = true;
            repaint();
        } else if (this.isMoving && event.getClickCount() > 1) {
            mainWindow.cutout();
        } else if (!this.isMoving && this.isRectangleExisting) {
            guiStatusHandler
                    .guiStatus(GuiStatusHandler.CUTOUT_NO_RECTANGLE_AVAILABLE);
            // delete old rectangle
            rectangleStartX = 0.0;
            rectangleStartY = 0.0;
            rectangleEndX = 0.0;
            rectangleEndY = 0.0;
            this.isRectangleExisting = false;
            repaint();
        }
    }

    /**
     * MouseListener.
     *
     * @author Adrian Schuetz
     * @param event
     */
    @Override
    public void mouseEntered(final MouseEvent event) {
    }

    /**
     * MouseListener.
     *
     * @author Adrian Schuetz
     * @param event
     */
    @Override
    public void mouseExited(final MouseEvent event) {
    }

    /**
     * MouseListener.
     *
     * @author Adrian Schuetz
     * @param e
     */
    @Override
    public void mouseMoved(final MouseEvent e) {
        // checks if the mouse cursor is within the rectangle or not
        if (e.getX() > rectangleStartX
                && e.getX() < (rectangleStartX + rectangleEndX)
                && e.getY() > rectangleStartY
                && e.getY() < (rectangleStartY + rectangleEndY)) {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.isMoving = true;
        } else {
            this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            this.isMoving = false;
        }
    }
}
