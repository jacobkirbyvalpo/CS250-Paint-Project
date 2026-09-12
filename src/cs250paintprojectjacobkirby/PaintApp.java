package cs250paintprojectjacobkirby;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

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
        // these set the actions
         openItem.setOnAction(e -> openFile());
         saveItem.setOnAction(e -> save());
         saveAsItem.setOnAction(e -> saveAs());
         closeItem.setOnAction(e -> closeImage());
         exitItem.setOnAction(e -> javafx.application.Platform.exit());

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, closeItem,
                new SeparatorMenuItem(), exitItem);
        //creates the menu bar and pulls from the menus to populat it
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(scrollPane);

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
       //does what it says, opens tje file and prevents other files
    private void openFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Image");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images",
                "*.jpg", "*.gif", "*.png", "*.tif"));
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

    private void saveAs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Image");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG", "*.png"));

        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            writeTo(file);
            currentFile = file;
        }
    }

    // clears the image without exiting the program
    private void closeImage() {
        canvas.setImage(null);
        currentFile = null;
        //sets to 0 to close the image
        canvas.setWidth(0);
        canvas.setHeight(0);
    }

    //hared by save and saveAs only difference between the two
    //is where the file came from
    private void writeTo(File file) {
        if (canvas.getImage() == null) {
            showError("Nothing to save.");
            return;
        }
        try {
            //BufferedImage that ImageIO can write.
            boolean ok = ImageIO.write(
                SwingFXUtils.fromFXImage(canvas.getImage(), null),
                "png", file);

            if (!ok) {
                showError("No writer available for that file format.");
            }
        } catch (IOException e) {
            showError("Could not write to the file.");
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}