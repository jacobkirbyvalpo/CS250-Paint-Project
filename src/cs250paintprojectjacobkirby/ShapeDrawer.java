package cs250paintprojectjacobkirby;

import javafx.scene.canvas.GraphicsContext;
//these control the math for every shape
/**
 * Math for the frawing of each shape, every method uses two corners.
 *
 * @author Jacob
 */

public class ShapeDrawer {

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
}