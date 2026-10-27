package cs250paintprojectjacobkirby;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

 /**
 * Rotates and flips the selected area, or the whole canvas when nothing
 * is selected.
 *
 * @author Jacob
 */
public class TransformActions {

    private FileActions fileActions;

     /**
     * rotate and flip handler.
     */
    public TransformActions(FileActions fileActions) {
        this.fileActions = fileActions;
    }

    /**
     * Rotates or flips the selected area if there is one, otherwise the
     * whole canvas.
     *
     * @param how "Rotate 90", "Rotate 180", "Rotate 270", "Flip Horizontal" or "Flip Vertical"
     */
    public void apply(String how) {
        DocumentTab tab = fileActions.currentTab();
        if (tab == null) {
            return;
        }
        ImageCanvas canvas = tab.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Rectangle box = canvas.getSelectionBox();
        WritableImage before = canvas.snapshot(null, null);
           int x = (int) box.getX();
           int y = (int) box.getY();
           int w = (int) box.getWidth();
           int h = (int) box.getHeight();

        if (box.isVisible() && w > 0 && h > 0) {
            WritableImage turned = turn(new WritableImage(before.getPixelReader(), x, y, w, h), how);
            gc.setFill(Color.WHITE);
            gc.fillRect(x, y, w, h);
            gc.drawImage(turned, x, y);
            box.setWidth(Math.min(turned.getWidth(), canvas.getWidth() - x));
            box.setHeight(Math.min(turned.getHeight(), canvas.getHeight() - y));
            canvas.markChanged(before);
            ActivityLogger.log(how + " selection");
            return;
        }

        WritableImage turned = turn(before, how);
        canvas.setWidth(turned.getWidth());
        canvas.setHeight(turned.getHeight());
        gc.drawImage(turned, 0, 0);
        canvas.markChanged(before);
        ActivityLogger.log(how + " whole canvas");
    }

    /**
     * Makes a rotated or flipped copy of an image.
     *
     * @param img the image to copy
     * @param how which rotate or flip to do
     * @return the rotated or flipped copy
     */
    private WritableImage turn(Image img, String how) {
        double w = img.getWidth();
        double h = img.getHeight();
        Canvas temp = new Canvas(w, h);
            if (how.equals("Rotate 90") || how.equals("Rotate 270")) {
                temp = new Canvas(h, w);
            }
                GraphicsContext g = temp.getGraphicsContext2D();
                g.setImageSmoothing(false);
            if (how.equals("Rotate 90")) {
                g.translate(h, 0);
                g.rotate(90);
            }
            if (how.equals("Rotate 180")) {
                g.translate(w, h);
                g.rotate(180);
             }
             if (how.equals("Rotate 270")) {
                g.translate(0, w);
                g.rotate(270);
            }
            if (how.equals("Flip Horizontal")) {
                g.translate(w, 0);
                g.scale(-1, 1);
            }
            if (how.equals("Flip Vertical")) {
                g.translate(0, h);
                g.scale(1, -1);
            }
                g.drawImage(img, 0, 0);
                return temp.snapshot(null, null);
        }
}