package cs250paintprojectjacobkirby;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
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

}