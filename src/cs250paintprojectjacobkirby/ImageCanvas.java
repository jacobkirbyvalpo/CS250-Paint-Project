package cs250paintprojectjacobkirby;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
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
    
    private UndoHistory history = new UndoHistory();
    private SelectionTool selection;

    
    /**
     *creates the canvas and sets up mouse handling
     * @param settings shared drawing settings read on every mouse press
     */

    public ImageCanvas(ToolSettings settings) {
        this.settings = settings;
        gc = getGraphicsContext2D();
        selection = new SelectionTool(this, settings);

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
            
                if (settings.getTool() == Tool.SELECT) {
                selection.getSelectionBox().setVisible(false);
            }

                if (settings.getTool() == Tool.MOVE) {
                selection.startMove(beforeDrag, e.getX(), e.getY());
            }
            
        });

            setOnMouseDragged(e -> {
            if (settings.getTool() == Tool.SELECT) {
                selection.updateSelection(startX, startY, e.getX(), e.getY());
                return;
             }
            if (settings.getTool() == Tool.TEXT) {
                return;
             }
            
            if (settings.getTool() == Tool.PASTE) {
                selection.drawPaste(beforeDrag, e.getX(), e.getY());
                return;
            }

            if (settings.getTool() == Tool.MOVE) {
                selection.dragMove(e.getX(), e.getY());
                return;
            }
            if (settings.getTool() == Tool.PENCIL) {
                gc.strokeLine(startX, startY, e.getX(), e.getY());
                startX = e.getX();
                startY = e.getY();
                modified = true;
             }
            //settings for each tool
            if (settings.getTool() != Tool.PENCIL && settings.getTool() != Tool.GRABBER) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.draw(gc, settings.getTool(), startX, startY, e.getX(), e.getY(), settings.getSides());
              }
            
            if (settings.getTool() == Tool.GRABBER) {
                settings.setColor(beforeDrag.getPixelReader().getColor((int) e.getX(), (int) e.getY()));
            }
        });
         
        setOnMouseReleased( e-> {
            
            if (settings.getTool() == Tool.TEXT) {
                addText(e.getX(), e.getY());
                return;
            }
            
             if (settings.getTool() == Tool.SELECT) {
                ActivityLogger.log("Selected an area");
                return;
            }
            if (settings.getTool() == Tool.PASTE) {
                if (settings.getClipboard() != null) {
                    saveForUndo(beforeDrag);
                    selection.drawPaste(beforeDrag, e.getX(), e.getY());
                    modified = true;
                    ActivityLogger.log("Pasted");
                }
                return;
            }
            
            if (settings.getTool() == Tool.MOVE) {
                if (selection.isMoving()) {
                    saveForUndo(beforeDrag);
                    selection.dragMove(e.getX(), e.getY());
                    modified = true;
                    selection.stopMove();
                    ActivityLogger.log("Moved selection");
                }
                return;
            }
             
            //has to do with undo addition
            if (settings.getTool() != Tool.GRABBER) {
                saveForUndo(beforeDrag);
            }
             
            //this controls behavior for each tool.
            if (settings.getTool() != Tool.PENCIL && settings.getTool() != Tool.GRABBER) {
                gc.drawImage(beforeDrag, 0, 0);
                ShapeDrawer.draw(gc, settings.getTool(), startX, startY, e.getX(), e.getY(), settings.getSides());
                //lets smart save know, image is modified
                modified = true;
                ActivityLogger.log("Drew " + settings.getTool());
            }
            if (settings.getTool() == Tool.PENCIL) {
                ActivityLogger.log("Drew with PENCIL");
            }
            if (settings.getTool() == Tool.GRABBER) {
                ActivityLogger.log("Picked color " + ColorNames.toHex(settings.getColor()));
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
        saveForUndo(current);
        setWidth(newWidth);
        setHeight(newHeight);

        gc.drawImage(current, 0, 0);
        modified = true;
    }
    
    /**
     * sets the whole canvas white, erasing everything on it
     */
    public void clear() {
        saveForUndo(snapshot(null, null));
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        modified = true;
    }
    /**
     * makes the canvas into a blank white image of the given size.
     * @param width width of the blank image in pixels
     * @param height height of the blank image in pixels
     */
    public void makeBlank(double width, double height) {
        setWidth(width);
        setHeight(height);
        clear();
        //for undo
        history.clear();
        //blank pages have nothing to save
        modified = false;
    }
    
        /**
     * sets the canvas back to the way it was before the previous change.
     */
    public void undo() {
        if (!history.canUndo()) {
            return;
        }
        restore(history.undo(snapshot(null, null)));
        modified = true;
    }

    /**
     * Brings back the last change that was undone.
     */
    public void redo() {
        if (!history.canRedo()) {
            return;
        }
        restore(history.redo(snapshot(null, null)));
        modified = true;
    }

    /**
     * everything ont he canvas is replaced with the image and is resized
     *
     * @param image the image to show
     */
    private void restore(WritableImage image) {
        setWidth(image.getWidth());
        setHeight(image.getHeight());
        gc.drawImage(image, 0, 0);
        selection.getSelectionBox().setVisible(false);
    }
    
    private void saveForUndo(WritableImage before) {
        history.save(before);
    }
    
     /**
     * Assks for text and placement .
     *
     * @param x x of where the text starts
     * @param y y of the line the text sits on
     */
    private void addText(double x, double y) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Text to add:");
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().isEmpty()) {
            return;
        }
        saveForUndo(beforeDrag);
        gc.setFill(settings.getColor());
        gc.setFont(new Font(10 + settings.getWidth() * 2));
        gc.fillText(result.get(), x, y);
        modified = true;
        ActivityLogger.log("Added text");
    }
    
    /**
     * gets the dashed showed area.
     *
     * @return the selection box
     */
    public Rectangle getSelectionBox() {
        return selection.getSelectionBox();
    }

     /**
     * selection box copy.
     */
    public void copySelection() {
        selection.copySelection();
    }
    
    /**
     * Pastes the copied piece in the top left corner and selects so the
     * move tool can then drag it.
     */
    public void pasteClipboard() {
        WritableImage before = snapshot(null, null);
        if (selection.pasteInCorner()) {
            saveForUndo(before);
            modified = true;
            ActivityLogger.log("Pasted");
        }
    }

    
     /**
     * Saves the picture from before a change for undo and marks
     * as change, used when another class then draws on the canvas.
     *
     */
    public void markChanged(WritableImage before) {
        saveForUndo(before);
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
     return getWidth() > 0 && getHeight() > 0;
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