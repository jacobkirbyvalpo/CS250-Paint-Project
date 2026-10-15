package cs250paintprojectjacobkirby;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * The selection box and everything that uses it.
 *
 * @author Jacob
 */

public class SelectionTool {

    private ImageCanvas canvas;

    private GraphicsContext gc;

    private ToolSettings settings;

    private Rectangle selectionBox = new Rectangle();

    private WritableImage movingPiece;
    private WritableImage moveBase;
    private double moveOffsetX;
    private double moveOffsetY;

     /**
     * Creates the selection box for a canvas.
     *
     */
    public SelectionTool(ImageCanvas canvas, ToolSettings settings) {
        this.canvas = canvas;
        this.settings = settings;
        gc = canvas.getGraphicsContext2D();

        selectionBox.setFill(null);
        selectionBox.setStroke(Color.BLUE);
        selectionBox.setStrokeType(StrokeType.INSIDE);
        selectionBox.getStrokeDashArray().addAll(6.0, 6.0);
        selectionBox.setMouseTransparent(true);
        selectionBox.setVisible(false);
    }

        /**
     * stretches the seclection box.
     *
     * @param startX x where the drag started
     * @param startY y where the drag started
     * @param mouseX x of the mouse
     * @param mouseY y of the mouse
     */
    public void updateSelection(double startX, double startY, double mouseX, double mouseY) {
        double x2 = Math.max(0, Math.min(mouseX, canvas.getWidth()));
        double y2 = Math.max(0, Math.min(mouseY, canvas.getHeight()));
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
        WritableImage whole = canvas.snapshot(null, null);
        settings.setClipboard(new WritableImage(whole.getPixelReader(), x, y, w, h));
    }

     /**
     * shows the copies piece on top of the picture.
     */
    public void drawPaste(WritableImage beforeDrag, double x, double y) {
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
    public void startMove(WritableImage beforeDrag, double mouseX, double mouseY) {
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
        moveBase = canvas.snapshot(null, null);
        gc.drawImage(movingPiece, x, y);
        moveOffsetX = mouseX - x;
        moveOffsetY = mouseY - y;
    }

     /**
     * shows the lifted image piece on top of the picture and moves the
     * selection box with it.
     */
    public void dragMove(double mouseX, double mouseY) {
        if (movingPiece == null) {
            return;
        }
        double x = Math.round(mouseX - moveOffsetX);
        double y = Math.round(mouseY - moveOffsetY);
        x = Math.max(0, Math.min(x, canvas.getWidth() - movingPiece.getWidth()));
        y = Math.max(0, Math.min(y, canvas.getHeight() - movingPiece.getHeight()));
        gc.drawImage(moveBase, 0, 0);
        gc.drawImage(movingPiece, x, y);
        selectionBox.setX(x);
        selectionBox.setY(y);
    }

    /**
     * Checks if a piece is currently lifted by the move tool.
     *
     * @return true if a piece is being moved
     */
    public boolean isMoving() {
        return movingPiece != null;
    }

    /**
     * Drops the lifted piece so the move is finished.
     */
    public void stopMove() {
        movingPiece = null;
    }
}
