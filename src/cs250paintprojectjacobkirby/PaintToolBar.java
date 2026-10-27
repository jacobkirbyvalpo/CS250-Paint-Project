package cs250paintprojectjacobkirby;

import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
/**
 *holds the color picker and color readout, and the grabber, line width slide and dashed toggles
 *
 * @author Jacob
 */

public class PaintToolBar extends ToolBar {
    /**
     * builds all of the toolbar controls, and ecah control writes its value
     * into shared settings, and the canvas reads this.
     *
     * @param settings shared drawing settings
     */

    public PaintToolBar(ToolSettings settings) {
        //to toggle shape and tool
        ToggleGroup toolGroup = new ToggleGroup();

        ToggleButton pencilButton = new ToggleButton("Pencil");
        pencilButton.setToggleGroup(toolGroup);
        pencilButton.setOnAction(e -> settings.setTool(Tool.PENCIL));
        pencilButton.setTooltip(new Tooltip("Pencil: draw freehand"));
        Icons.set(pencilButton, "pencil");

        ToggleButton lineButton = new ToggleButton("Line");
        lineButton.setToggleGroup(toolGroup);
        lineButton.setSelected(true);
        lineButton.setOnAction(e -> settings.setTool(Tool.LINE));
        lineButton.setTooltip(new Tooltip("Line: drag to draw a straight line"));
        Icons.set(lineButton, "line");
        //rectangle tool select
        ToggleButton rectButton = new ToggleButton("Rectangle");
        rectButton.setToggleGroup(toolGroup);
        rectButton.setOnAction(e -> settings.setTool(Tool.RECTANGLE));
        rectButton.setTooltip(new Tooltip("Rectangle: drag to draw a rectangle"));
        Icons.set(rectButton, "rectangle");

        ToggleButton squareButton = new ToggleButton("Square");
        squareButton.setToggleGroup(toolGroup);
        squareButton.setOnAction(e -> settings.setTool(Tool.SQUARE));
        squareButton.setTooltip(new Tooltip("Square: drag to draw a square"));
        Icons.set(squareButton, "square");

        ToggleButton ellipseButton = new ToggleButton("Ellipse");
        ellipseButton.setToggleGroup(toolGroup);
        ellipseButton.setOnAction(e -> settings.setTool(Tool.ELLIPSE));
        ellipseButton.setTooltip(new Tooltip("Ellipse: drag to draw an ellipse"));
        Icons.set(ellipseButton, "ellipse");

        ToggleButton circleButton = new ToggleButton("Circle");
        circleButton.setToggleGroup(toolGroup);
        circleButton.setOnAction(e -> settings.setTool(Tool.CIRCLE));
        circleButton.setTooltip(new Tooltip("Circle: drag to draw a circle"));
        Icons.set(circleButton, "circle");

        ToggleButton triangleButton = new ToggleButton("Triangle");
        triangleButton.setToggleGroup(toolGroup);
        triangleButton.setOnAction(e -> settings.setTool(Tool.TRIANGLE));
        triangleButton.setTooltip(new Tooltip("Triangle: drag to draw a triangle"));
        Icons.set(triangleButton, "triangle");
        //adds button for color grabber
        ToggleButton grabberButton = new ToggleButton("Grabber");
        grabberButton.setToggleGroup(toolGroup);
        grabberButton.setOnAction(e -> settings.setTool(Tool.GRABBER));
        grabberButton.setTooltip(new Tooltip("Grabber: drag over the image to pick up a color"));
        Icons.set(grabberButton, "grabber");
        //adds diamond shape
        ToggleButton diamondButton = new ToggleButton("Diamond");
        diamondButton.setToggleGroup(toolGroup);
        diamondButton.setOnAction(e -> settings.setTool(Tool.DIAMOND));
        diamondButton.setTooltip(new Tooltip("Diamond: drag to draw a diamond"));
        Icons.set(diamondButton, "diamond");
        
        ToggleButton rightTriangleButton = new ToggleButton("Right Triangle");
        rightTriangleButton.setToggleGroup(toolGroup);
        rightTriangleButton.setOnAction(e -> settings.setTool(Tool.RIGHT_TRIANGLE));
        rightTriangleButton.setTooltip(new Tooltip("Right Triangle: drag to draw a right triangle"));
        Icons.set(rightTriangleButton, "right_triangle");

        ToggleButton polygonButton = new ToggleButton("Polygon");
        polygonButton.setToggleGroup(toolGroup);
        polygonButton.setOnAction(e -> settings.setTool(Tool.POLYGON));
        polygonButton.setTooltip(new Tooltip("Polygon: drag to draw a polygon, set sides with Sides"));
        Icons.set(polygonButton, "polygon");
        
        ToggleButton textButton = new ToggleButton("Text");
        textButton.setToggleGroup(toolGroup);
        textButton.setOnAction(e -> settings.setTool(Tool.TEXT));
        textButton.setTooltip(new Tooltip("Text: click where the text should go"));
        Icons.set(textButton, "text");
        
        ToggleButton selectButton = new ToggleButton("Select");
        selectButton.setToggleGroup(toolGroup);
        selectButton.setOnAction(e -> settings.setTool(Tool.SELECT));
        selectButton.setTooltip(new Tooltip("Select: drag a box to select an area"));
        Icons.set(selectButton, "select");
        
        ToggleButton pasteButton = new ToggleButton("Paste");
        pasteButton.setToggleGroup(toolGroup);
        pasteButton.setOnAction(e -> settings.setTool(Tool.PASTE));
        pasteButton.setTooltip(new Tooltip("Paste: click to place the copied area"));
        Icons.set(pasteButton, "paste");
        
        ToggleButton moveButton = new ToggleButton("Move");
        moveButton.setToggleGroup(toolGroup);
        moveButton.setOnAction(e -> settings.setTool(Tool.MOVE));
        moveButton.setTooltip(new Tooltip("Move: drag the selected area"));
        Icons.set(moveButton, "move");
        
        ToggleButton starButton = new ToggleButton("Star");
        starButton.setToggleGroup(toolGroup);
        starButton.setOnAction(e -> settings.setTool(Tool.STAR));
        starButton.setTooltip(new Tooltip("Star: drag to draw a star, set points with Sides"));
        Icons.set(starButton, "star");

        Label sidesLabel = new Label("Sides");
        Spinner<Integer> sidesSpinner = new Spinner<>(3, 100, 5);
        sidesSpinner.setPrefWidth(70);
        sidesSpinner.setEditable(true);
        sidesSpinner.setTooltip(new Tooltip("Sides for Polygon, points for Star"));
        sidesSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            settings.setSides(newVal);
        });

        //control color picker and displays RGB and color name
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        Label colorLabel = new Label(ColorNames.toName(Color.BLACK) + "  "
        + ColorNames.toHex(Color.BLACK) + "  " + ColorNames.toRgb(Color.BLACK));
        
        colorPicker.valueProperty().bindBidirectional(settings.colorProperty());
        colorPicker.setTooltip(new Tooltip("Drawing color"));
        settings.colorProperty().addListener((obs, oldVal, newVal) -> {
        colorLabel.setText(ColorNames.toName(newVal) + "  "
        + ColorNames.toHex(newVal) + "  " + ColorNames.toRgb(newVal));
        });
        //width and size slide
        Slider widthSlider = new Slider(1, 20, 1);
        widthSlider.setShowTickMarks(true);
        widthSlider.setMajorTickUnit(1);
        widthSlider.setMinorTickCount(0);
        widthSlider.setSnapToTicks(true);
        widthSlider.setTooltip(new Tooltip("Line width in pixels"));

        Label widthLabel = new Label("1 px");
        //sets the new values for line
        widthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            settings.setWidth(newVal.doubleValue());
            widthLabel.setText(newVal.intValue() + " px");
        });

        CheckBox dashedBox = new CheckBox("Dashed");
        dashedBox.setOnAction(e -> settings.setDashed(dashedBox.isSelected()));
        dashedBox.setTooltip(new Tooltip("Draw dashed lines"));

        getItems().addAll(colorPicker, colorLabel, grabberButton, widthSlider, widthLabel, textButton, selectButton, pasteButton, moveButton,
        pencilButton, lineButton, rectButton, squareButton, ellipseButton, circleButton, triangleButton,
        rightTriangleButton, polygonButton, starButton, sidesLabel, sidesSpinner, diamondButton, dashedBox);
    }
}
