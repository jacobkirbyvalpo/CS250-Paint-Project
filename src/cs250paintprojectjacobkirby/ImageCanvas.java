package cs250paintprojectjacobkirby;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
//canvas only thing you can draw on, useful for later
public class ImageCanvas extends Canvas {

    //image load
    private Image img;

   //draw with Graphics context
    private GraphicsContext gc;
    
    private double startX;
    private double startY;

    public ImageCanvas() {
        gc = getGraphicsContext2D();
        
        setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();
        });
        
        setOnMouseReleased( e-> {
            gc.strokeLine(startX, startY, e.getX(), e.getY());
            //lets smart save know, image is modified
            modified = true;
        });
    }

    
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