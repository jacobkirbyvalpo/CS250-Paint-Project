package cs250paintprojectjacobkirby;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
//canvas only thing you can draw on, useful for later
/**
 * Canvas is the drawing surface, displayed the loaded image and has
 * input for every tool.
 *
 * @author Jacob
 */

public class ImageCanvas extends Canvas {

    //image load
    private Image img;

   //draw with Graphics context
    private GraphicsContext gc;

    private ToolSettings settings;

    //this is used to stop thousands of shapes from shape tools from being made
    //while dragging the shape
    private WritableImage beforeDrag;

    private double startX;
    private double startY;
    /**
     *creates the canvas and sets up mouse handling
     * @param settings shared drawing settings read on every mouse press
     */

    public ImageCanvas(ToolSettings settings) {
        this.settings = settings;
        gc = getGraphicsContext2D();

                setOnMousePressed(e -> {
            gc.setStroke(settings.getColor());
            gc.setLineWidth(settings.getWidth());
            //controls dashed lines, only used if enabled
            if (settings.isDashed()) {
                double w = settings.getWidth();
                gc.setLineDashes(w * 3, w * 3);
            } else {
                gc.setLineDashes(null);
            }
            //this records what the image looked before the drag
            //this is to help prevent infinite shapes
            beforeDrag = snapshot(null, null);
            startX = e.getX();
            startY = e.getY();
        });

         setOnMouseDragged(e -> {
            if (settings.getTool() == Tool.PENCIL) {
                gc.strokeLine(startX, startY, e.getX(), e.getY());
                startX = e.getX();
                startY = e.getY();
                modified = true;
            }
            //settings for each tool
            if (settings.getTool() == Tool.RECTANGLE) {
                   gc.drawImage(beforeDrag, 0, 0);
                   ShapeDrawer.drawRectangle(gc, startX, startY, e.getX(), e.getY());
               }
            if (settings.getTool() == Tool.SQUARE) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.drawSquare(gc, startX, startY, e.getX(), e.getY());
            }
            if (settings.getTool() == Tool.ELLIPSE) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.drawEllipse(gc, startX, startY, e.getX(), e.getY());
            }
            if (settings.getTool() == Tool.CIRCLE) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.drawCircle(gc, startX, startY, e.getX(), e.getY());
            }
            if (settings.getTool() == Tool.TRIANGLE) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.drawTriangle(gc, startX, startY, e.getX(), e.getY());
            }
            
            if (settings.getTool() == Tool.GRABBER) {
                settings.setColor(beforeDrag.getPixelReader().getColor((int) e.getX(), (int) e.getY()));
            }
        });

        setOnMouseReleased( e-> {
            if (settings.getTool() == Tool.LINE){
            gc.strokeLine(startX, startY, e.getX(), e.getY());
            //lets smart save know, image is modified
            modified = true;
            }
            //this controls behavior for each tool.
            if (settings.getTool() == Tool.RECTANGLE) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.drawRectangle(gc, startX, startY, e.getX(), e.getY());
                modified = true;
               }
            if (settings.getTool() == Tool.SQUARE) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.drawSquare(gc, startX, startY, e.getX(), e.getY());
                modified = true;
            }
            if (settings.getTool() == Tool.ELLIPSE) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.drawEllipse(gc, startX, startY, e.getX(), e.getY());
                modified = true;
            }
            if (settings.getTool() == Tool.CIRCLE) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.drawCircle(gc, startX, startY, e.getX(), e.getY());
                modified = true;
            }
            if (settings.getTool() == Tool.TRIANGLE) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.drawTriangle(gc, startX, startY, e.getX(), e.getY());
                modified = true;
            }

        });
    }

    /**
     * Changes the canvas size while keeping what is already drawn, dynamic.
     *
     * @param newWidth new width in pixels
     * @param newHeight new height in pixels
     */

    public void resizeCanvas(double newWidth, double newHeight){
        WritableImage current = snapshot(null, null);

        setWidth(newWidth);
        setHeight(newHeight);

        gc.drawImage(current, 0, 0);
        modified = true;
    }
    //called by PaintApp when a new image is loaded
    public void setImage(Image newImg) {
        this.img = newImg;

        if (newImg != null) {
           //gets width and height of image to display it
            setWidth(newImg.getWidth());
            setHeight(newImg.getHeight());
        }
//redraws when it gest that data from above
        redraw();
        //checks modified for smartsave
        modified = false;
    }

    //wipes the canvas and draws the current image
    private void redraw() {
        gc.clearRect(0, 0, getWidth(), getHeight());
        if (img != null) {
            gc.drawImage(img, 0, 0);
        }
    }

    public void setLineWidth(double w){
    gc.setLineWidth(w);
    }

    public void setLineColor(Color C){
        gc.setStroke(C);
    }

    //hands the image to PaintApp for saving and save as and stuff
    public Image getImage() {
        return snapshot(null,null);
    }
    public boolean hasImage() {
     return img != null;
    }
    //smart save
    private boolean modified;

    public void markSaved(){
     modified = false;
    }
    //checks if modified
    public boolean isModified(){
        return modified;
    }
}