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

        ToggleButton lineButton = new ToggleButton("Line");
        lineButton.setToggleGroup(toolGroup);
        lineButton.setSelected(true);
        lineButton.setOnAction(e -> settings.setTool(Tool.LINE));
        //rectangle tool select
        ToggleButton rectButton = new ToggleButton("Rectangle");
        rectButton.setToggleGroup(toolGroup);
        rectButton.setOnAction(e -> settings.setTool(Tool.RECTANGLE));

        ToggleButton squareButton = new ToggleButton("Square");
        squareButton.setToggleGroup(toolGroup);
        squareButton.setOnAction(e -> settings.setTool(Tool.SQUARE));

        ToggleButton ellipseButton = new ToggleButton("Ellipse");
        ellipseButton.setToggleGroup(toolGroup);
        ellipseButton.setOnAction(e -> settings.setTool(Tool.ELLIPSE));

        ToggleButton circleButton = new ToggleButton("Circle");
        circleButton.setToggleGroup(toolGroup);
        circleButton.setOnAction(e -> settings.setTool(Tool.CIRCLE));

        ToggleButton triangleButton = new ToggleButton("Triangle");
        triangleButton.setToggleGroup(toolGroup);
        triangleButton.setOnAction(e -> settings.setTool(Tool.TRIANGLE));
        //adds button for color grabber
        ToggleButton grabberButton = new ToggleButton("Grabber");
        grabberButton.setToggleGroup(toolGroup);
        grabberButton.setOnAction(e -> settings.setTool(Tool.GRABBER));
        //adds diamond shape
        ToggleButton diamondButton = new ToggleButton("Diamond");
        diamondButton.setToggleGroup(toolGroup);
        diamondButton.setOnAction(e -> settings.setTool(Tool.DIAMOND));
        
        ToggleButton rightTriangleButton = new ToggleButton("Right Triangle");
        rightTriangleButton.setToggleGroup(toolGroup);
        rightTriangleButton.setOnAction(e -> settings.setTool(Tool.RIGHT_TRIANGLE));

        ToggleButton polygonButton = new ToggleButton("Polygon");
        polygonButton.setToggleGroup(toolGroup);
        polygonButton.setOnAction(e -> settings.setTool(Tool.POLYGON));
        
        ToggleButton textButton = new ToggleButton("Text");
        textButton.setToggleGroup(toolGroup);
        textButton.setOnAction(e -> settings.setTool(Tool.TEXT));
        
        ToggleButton selectButton = new ToggleButton("Select");
        selectButton.setToggleGroup(toolGroup);
        selectButton.setOnAction(e -> settings.setTool(Tool.SELECT));
        
        ToggleButton pasteButton = new ToggleButton("Paste");
        pasteButton.setToggleGroup(toolGroup);
        pasteButton.setOnAction(e -> settings.setTool(Tool.PASTE));
        
        ToggleButton moveButton = new ToggleButton("Move");
        moveButton.setToggleGroup(toolGroup);
        moveButton.setOnAction(e -> settings.setTool(Tool.MOVE));

        Label sidesLabel = new Label("Sides");
        Spinner<Integer> sidesSpinner = new Spinner<>(3, 100, 5);
        sidesSpinner.setPrefWidth(70);
        sidesSpinner.setEditable(true);
        sidesSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            settings.setSides(newVal);
        });

        //control color picker and displays RGB and color name
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        Label colorLabel = new Label(ColorNames.toName(Color.BLACK) + "  "
        + ColorNames.toHex(Color.BLACK) + "  " + ColorNames.toRgb(Color.BLACK));
        
        colorPicker.valueProperty().bindBidirectional(settings.colorProperty());
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

        Label widthLabel = new Label("1 px");
        //sets the new values for line
        widthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            settings.setWidth(newVal.doubleValue());
            widthLabel.setText(newVal.intValue() + " px");
        });

        CheckBox dashedBox = new CheckBox("Dashed");
        dashedBox.setOnAction(e -> settings.setDashed(dashedBox.isSelected()));

        getItems().addAll(colorPicker, colorLabel, grabberButton, widthSlider, widthLabel, textButton, selectButton, pasteButton, moveButton,
        pencilButton, lineButton, rectButton, squareButton, ellipseButton, circleButton, triangleButton,
        rightTriangleButton, polygonButton, sidesLabel, sidesSpinner, diamondButton, dashedBox);
    }
}
