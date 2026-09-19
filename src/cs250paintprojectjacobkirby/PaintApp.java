package cs250paintprojectjacobkirby;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

import javafx.scene.paint.Color;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;

public class PaintApp {

    //the window handed in by Main JavaFX
    private Stage stage;

    //holds and draws the image
    private ImageCanvas canvas;

    //file the image came from. null means nothing opened or saved yet
    private File currentFile;

    public PaintApp(Stage stage) {
        this.stage = stage;
        stage.setTitle("Paint");

        canvas = new ImageCanvas();

        // scrollbars when the image is bigger than the window
        ScrollPane scrollPane = new ScrollPane(canvas);

        //menu items
        MenuItem openItem = new MenuItem("Open");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem resizeItem = new MenuItem("Resize Canvas");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");
        // these set the actions
         openItem.setOnAction(e -> openFile());
         saveItem.setOnAction(e -> save());
         saveAsItem.setOnAction(e -> saveAs());
         closeItem.setOnAction(e -> closeImage());
         resizeItem.setOnAction(e -> resizeCanvas());
         helpItem.setOnAction(e -> showInfo("Help","There is no help, good luck buddy"));
         aboutItem.setOnAction(e -> showInfo("About","Jacobs Paint Project"));
         
         //smart save upgrade
        exitItem.setOnAction(e -> {
        if (confirmDiscard()) {
          javafx.application.Platform.exit();
             }
        });
        
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, closeItem, resizeItem,
                new SeparatorMenuItem(), exitItem);
        //creates the menu bar and pulls from the menus to populat it
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(e -> canvas.setLineColor(colorPicker.getValue()));
 
        Slider widthSlider = new Slider (1, 20, 1);
        widthSlider.valueProperty().addListener(
        (obs, oldVal, newVal) -> canvas.setLineWidth(newVal.doubleValue()));
        
        ToolBar toolBar = new ToolBar(colorPicker, widthSlider);
        
        BorderPane root = new BorderPane();
        root.setTop(new VBox(menuBar, toolBar));
        root.setCenter(scrollPane);

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
        
        //menu and about additional code
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(helpItem, aboutItem);
        menuBar.getMenus().add(helpMenu);
        
       stage.setOnCloseRequest(e -> {
            if (!confirmDiscard()) {
                e.consume(); //stops
            }
        });
      
    }
       //does what it says, opens tje file and prevents other files
    
    private void resizeCanvas() {
        TextInputDialog widthDialog = new TextInputDialog("800");
        widthDialog.setHeaderText("New width:");
        Optional<String> widthResult = widthDialog.showAndWait();
    if (!widthResult.isPresent()) {
        return;
    }

        TextInputDialog heightDialog = new TextInputDialog("600");
         heightDialog.setHeaderText("New height:");
         Optional<String> heightResult = heightDialog.showAndWait();
    if (!heightResult.isPresent()) {
        return;
    }

    try {
        int w = Integer.parseInt(widthResult.get().trim());
        int h = Integer.parseInt(heightResult.get().trim());

        if (w <= 0 || h <= 0) {
            showError("Width and height must be a positive number");
            return;
        }
        canvas.resizeCanvas(w, h);
    } catch (NumberFormatException e) {
        showError("Please enter whole numbers only");
    }
}

    
    private void openFile() {
        //smartsave system
        if(!confirmDiscard()){
         return;   
        }
        
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Image");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images", "*.jpg",
                "*.jpeg", "*.gif", "*.png", "*.tif", "*.bmp"));
        //opens file dialog
        File file = chooser.showOpenDialog(stage);

        //FileChooser returns null if the user cancelled the dialog
        if (file != null) {
            //JavaFX loads images fromstring not a File
            Image img = new Image(file.toURI().toString());

            if (img.isError()) {
                showError("Not a supported image format.");
                return;
            }

            canvas.setImage(img);
            currentFile = file;
        }
    }

    private void save() {
        if (currentFile == null) {
            saveAs();
            return;
        }
        writeTo(currentFile);
    }
//all the file extensions supported
    private void saveAs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Image");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG", "*.png"));
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JPG", "*.jpg"));
          chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("BMP", "*.bmp"));
          chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JPEG", "*.jpeg"));
            
        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            writeTo(file);
            currentFile = file;
        }
    }

    // clears the image without exiting the program
    private void closeImage() {
        //smart save system
         if(!confirmDiscard()){
         return;   
        }
        canvas.setImage(null);
        currentFile = null;
        //sets to 0 to close the image
        canvas.setWidth(0);
        canvas.setHeight(0);
    }

    //hared by save and saveAs only difference between the two
    //is where the file came from
    private void writeTo(File file) {
        if (!canvas.hasImage()) {
            showError("Nothing to save.");
            return;
        }
        try {
            //BufferedImage that ImageIO can write.
            String name = file.getName();
             int dot = name.lastIndexOf(".");
             String format;
             if (dot != -1){
              format =name.substring(dot+1).toLowerCase();
             }
             else {
              format = "png";   
             }
             
             //no format to read file image error
             //https://bugs.openjdk.org/browse/JDK-8211748
             
                     BufferedImage bimg = SwingFXUtils.fromFXImage(canvas.getImage(), null);
//this is all to avoid the alpha channel PNG to JPG or other format bug, documented above because it was evil.
             if (format.equals("jpg") || format.equals("jpeg") || format.equals("bmp")){
             BufferedImage rgb = new BufferedImage(bimg.getWidth(), bimg.getHeight(),
             BufferedImage.TYPE_INT_RGB);
             
             Graphics2D g = rgb.createGraphics();
             g.drawImage(bimg, 0, 0, null);
             g.dispose();
             bimg = rgb;
        }
            boolean ok = ImageIO.write(bimg, format, file);

            if (!ok) {
                showError("No writer available for that file format.");
            }
            else {
                canvas.markSaved();
            }
        } catch (IOException e) {
            showError("Could not write to the file.");
        }
}
//checks if modified, if not, returns true
    private boolean confirmDiscard() {
     if (!canvas.isModified()){
      return true;   
     }
     ButtonType saveButton = new ButtonType("Save");
     ButtonType dontsaveButton = new ButtonType("Do Not Save");
     
     Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
             "You have unsaved changes, would you like to save them?", saveButton, dontsaveButton, ButtonType.CANCEL);
     
     Optional<ButtonType> result = alert.showAndWait();
     
         //dialog closed without picking anything
    if (!result.isPresent()) {
        return false;
    }

    if (result.get() == saveButton) {
        save();
        return true;
    }

    if (result.get() == dontsaveButton) {
        return true;
    }

    //cancel
    return false;
     
    }
   
    
    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
    //menu and about information popup
    private void showInfo(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.showAndWait();
}
}
