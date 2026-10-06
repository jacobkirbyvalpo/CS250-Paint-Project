package cs250paintprojectjacobkirby;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
/**
 * Drawing settings shared by the toolbar and all canvas.
 * Each get and set does what it says, if you select a tool, it returns
 * that tool, color, etc.
 *
 * @author Jacob
 */

public class ToolSettings {
     private Tool currentTool = Tool.LINE;
     //needed for color picker
     private ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.BLACK);
     private double width = 1;
     
     private boolean dashed = false;
     private int sides = 5;
     
     private Image clipboard;

    public Tool getTool() {
        return currentTool;
    }

    public void setTool(Tool newTool){
        currentTool = newTool;
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(Color newColor){
        color.set(newColor);
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double newWidth){
        width = newWidth;
    }

    public boolean isDashed() {
        return dashed;
    }

    public void setDashed(boolean newDashed){
        dashed = newDashed;
    }
    
    public int getSides() {
        return sides;
    }

    /**
     * how many sides the polygon tool draws.
     *
     * @param newSides the number of sides, 3 or more for polygon.
     */
    public void setSides(int newSides){
        sides = newSides;
    }
    
    public Image getClipboard() {
        return clipboard;
    }

    public void setClipboard(Image newClipboard){
        clipboard = newClipboard;
    }

}