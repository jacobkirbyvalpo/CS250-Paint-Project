package cs250paintprojectjacobkirby;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

//canvas only thing you can draw on, useful for later
public class ImageCanvas extends Canvas {

    //image load
    private Image img;

   //draw with Graphics context
    private GraphicsContext gc;

    public ImageCanvas() {
        gc = getGraphicsContext2D();
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
    }

    //wipes the canvas and draws the current image
    private void redraw() {
        gc.clearRect(0, 0, getWidth(), getHeight());
        if (img != null) {
            gc.drawImage(img, 0, 0);
        }
    }

    //hands the image to PaintApp for saving and save as and stuff
    public Image getImage() {
        return img;
    }
}