package cs250paintprojectjacobkirby;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.Stack;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
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
    
    private Stack<WritableImage> undoStack = new Stack<>();
    private Stack<WritableImage> redoStack = new Stack<>();
    private Rectangle selectionBox = new Rectangle();
    
    private WritableImage movingPiece;
    private WritableImage moveBase;
    private double moveOffsetX;
    private double moveOffsetY;
    
    
    /**
     *creates the canvas and sets up mouse handling
     * @param settings shared drawing settings read on every mouse press
     */

    public ImageCanvas(ToolSettings settings) {
        this.settings = settings;
        gc = getGraphicsContext2D();
        
        selectionBox.setFill(null);
        selectionBox.setStroke(Color.BLUE);
        selectionBox.setStrokeType(StrokeType.INSIDE);
        selectionBox.getStrokeDashArray().addAll(6.0, 6.0);
        selectionBox.setMouseTransparent(true);
        selectionBox.setVisible(false);

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
                selectionBox.setVisible(false);
            }
                
                if (settings.getTool() == Tool.MOVE) {
                startMove(e.getX(), e.getY());
            }
            
        });

            setOnMouseDragged(e -> {
            if (settings.getTool() == Tool.SELECT) {
                updateSelection(e.getX(), e.getY());
                return;
             }
            if (settings.getTool() == Tool.TEXT) {
                return;
             }
            
            if (settings.getTool() == Tool.PASTE) {
                drawPaste(e.getX(), e.getY());
                return;
            }
            
            if (settings.getTool() == Tool.MOVE) {
                dragMove(e.getX(), e.getY());
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
                return;
            }
             
            if (settings.getTool() == Tool.PASTE) {
                if (settings.getClipboard() != null) {
                    saveForUndo(beforeDrag);
                    drawPaste(e.getX(), e.getY());
                    modified = true;
                }
                return;
            }
            
            if (settings.getTool() == Tool.MOVE) {
                if (movingPiece != null) {
                    saveForUndo(beforeDrag);
                    dragMove(e.getX(), e.getY());
                    modified = true;
                    movingPiece = null;
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
        undoStack.clear();
        redoStack.clear();
        //blank pages have nothing to save
        modified = false;
    }
    
        /**
     * sets the canvas back to the way it was before the previous change.
     */
    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        redoStack.push(snapshot(null, null));
        restore(undoStack.pop());
        modified = true;
    }

    /**
     * Brings back the last change that was undone.
     */
    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        undoStack.push(snapshot(null, null));
        restore(redoStack.pop());
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
        selectionBox.setVisible(false);
    }
    
    private void saveForUndo(WritableImage before) {
        undoStack.push(before);
        redoStack.clear();
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
    }
    
        /**
     * stretches the seclection box.
     *
     * @param mouseX x of the mouse
     * @param mouseY y of the mouse
     */
    private void updateSelection(double mouseX, double mouseY) {
        double x2 = Math.max(0, Math.min(mouseX, getWidth()));
        double y2 = Math.max(0, Math.min(mouseY, getHeight()));
        selectionBox.setX(Math.min(startX, x2));
        selectionBox.setY(Math.min(startY, y2));
        selectionBox.setWidth(Math.abs(x2 - startX));
        selectionBox.setHeight(Math.abs(y2 - startY));
        selectionBox.setVisible(true);
    }

    /**
     * gets the dashed showed area.
     *
     * @return the selection box
     */
    public Rectangle getSelectionBox() {
        return selectionBox;
    }
    
     /**
     * selection box copy.
     */
    public void copySelection() {
        int x = (int) selectionBox.getX();
        int y = (int) selectionBox.getY();
        int w = (int) selectionBox.getWidth();
        int h = (int) selectionBox.getHeight();
        if (!selectionBox.isVisible() || w < 1 || h < 1) {
            return;
        }
        WritableImage whole = snapshot(null, null);
        settings.setClipboard(new WritableImage(whole.getPixelReader(), x, y, w, h));
    }
    
     /**
     * shows the copies piece on top of the picture.
     */
    private void drawPaste(double x, double y) {
        Image piece = settings.getClipboard();
        if (piece == null) {
            return;
        }
        gc.drawImage(beforeDrag, 0, 0);
        gc.drawImage(piece, x - piece.getWidth() / 2, y - piece.getHeight() / 2);
    }
    
     /**
     * lifts the selected piece off of the canvas.
     */
    private void startMove(double mouseX, double mouseY) {
        int x = (int) selectionBox.getX();
        int y = (int) selectionBox.getY();
        int w = (int) selectionBox.getWidth();
        int h = (int) selectionBox.getHeight();
        movingPiece = null;
        if (!selectionBox.isVisible() || w < 1 || h < 1) {
            return;
        }
        movingPiece = new WritableImage(beforeDrag.getPixelReader(), x, y, w, h);
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y, w, h);
        moveBase = snapshot(null, null);
        gc.drawImage(movingPiece, x, y);
        moveOffsetX = mouseX - x;
        moveOffsetY = mouseY - y;
    }

     /**
     * shows the lifted image piece on top of the picture and moves the
     * selection box with it.
     */
    private void dragMove(double mouseX, double mouseY) {
        if (movingPiece == null) {
            return;
        }
        double x = Math.round(mouseX - moveOffsetX);
        double y = Math.round(mouseY - moveOffsetY);
        x = Math.max(0, Math.min(x, getWidth() - movingPiece.getWidth()));
        y = Math.max(0, Math.min(y, getHeight() - movingPiece.getHeight()));
        gc.drawImage(moveBase, 0, 0);
        gc.drawImage(movingPiece, x, y);
        selectionBox.setX(x);
        selectionBox.setY(y);
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