package cs250paintprojectjacobkirby;

import javafx.scene.canvas.GraphicsContext;
//these control the math for every shape
/**
 * Math for the frawing of each shape, every method uses two corners.
 *
 * @author Jacob
 */

public class ShapeDrawer {
    
     /**
     * Draws the shape.
     * non shapes like pencil and grabber, draw nothing.
     *
     * @param gc the graphics context to draw on
     * @param tool the tool that decides which shape is drawn
     * @param x1 x of the first corner, where the drag started
     * @param y1 y of the first corner, where the drag started
     * @param x2 x of the second corner, where the mouse is now
     * @param y2 y of the second corner, where the mouse is now
     */
          public static void draw(GraphicsContext gc, Tool tool, double x1, double y1, double x2, double y2, int sides) {
        switch (tool) {
            case LINE -> gc.strokeLine(x1, y1, x2, y2);
            case RECTANGLE -> drawRectangle(gc, x1, y1, x2, y2);
            case SQUARE -> drawSquare(gc, x1, y1, x2, y2);
            case ELLIPSE -> drawEllipse(gc, x1, y1, x2, y2);
            case CIRCLE -> drawCircle(gc, x1, y1, x2, y2);
            case TRIANGLE -> drawTriangle(gc, x1, y1, x2, y2);
            case RIGHT_TRIANGLE -> drawRightTriangle(gc, x1, y1, x2, y2);
            case POLYGON -> drawPolygon(gc, x1, y1, x2, y2, sides);
            case DIAMOND -> drawDiamond(gc, x1, y1, x2, y2);
            default -> { }
        }
    }
    
    
    public static void drawRectangle(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        gc.strokeRect(x, y, w, h);
    }
    
        public static void drawEllipse(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        gc.strokeOval(x, y, w, h);
    }

    public static void drawSquare(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double side = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        double x = (x2 < x1) ? x1 - side : x1;
        double y = (y2 < y1) ? y1 - side : y1;
        gc.strokeRect(x, y, side, side);
    }

    public static void drawCircle(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double side = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        double x = (x2 < x1) ? x1 - side : x1;
        double y = (y2 < y1) ? y1 - side : y1;
        gc.strokeOval(x, y, side, side);
    }

    public static void drawTriangle(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        double[] xs = { x + w / 2, x, x + w };
        double[] ys = { y, y + h, y + h };
        gc.strokePolygon(xs, ys, 3);
    }
    
    public static void drawDiamond(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double midX = (x1 + x2) / 2;
        double midY = (y1 + y2) / 2;
        double[] xs = { midX, x2, midX, x1 };
        double[] ys = { y1, midY, y2, midY };
        gc.strokePolygon(xs, ys, 4);
    }
    
     /**
     * Draws a right triangle
     *
     * @param gc the graphics context to draw on
     * @param x1 x where the drag started
     * @param y1 y where the drag started
     * @param x2 x where the mouse is now
     * @param y2 y where the mouse is now
     */
    public static void drawRightTriangle(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double[] xs = { x1, x1, x2 };
        double[] ys = { y1, y2, y2 };
        gc.strokePolygon(xs, ys, 3);
    }

    /**
     * draws a regular polygon
     *
     * @param gc the graphics context to draw on
     * @param x1 x of the center
     * @param y1 y of the center
     * @param x2 x of the first corner
     * @param y2 y of the first corner
     * @param sides how many sides the polygon has
     */
    public static void drawPolygon(GraphicsContext gc, double x1, double y1, double x2, double y2, int sides) {
        double radius = Math.hypot(x2 - x1, y2 - y1);
        double startAngle = Math.atan2(y2 - y1, x2 - x1);
        double[] xs = new double[sides];
        double[] ys = new double[sides];
        for (int i = 0; i < sides; i++) {
            double angle = startAngle + i * 2 * Math.PI / sides;
            xs[i] = x1 + radius * Math.cos(angle);
            ys[i] = y1 + radius * Math.sin(angle);
        }
        gc.strokePolygon(xs, ys, sides);
    }
}