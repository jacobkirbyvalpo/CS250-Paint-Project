package cs250paintprojectjacobkirby;

import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;
import javafx.scene.control.TextInputDialog;
/**
 * Builds the main window: menu bar, toolbar, and the tab area that
 * holds each open image, it then wires the pieces together.
 *
 * @author Jacob
 */

public class PaintApp {

    //the window handed in by Main JavaFX
    private Stage stage;

    private TabPane tabPane;

    private FileActions fileActions;

    private ImageWebServer webServer;
    /**
     * Creates the interface and shows it.
     *
     * @param stage the main window
     */

    public PaintApp(Stage stage) {
        this.stage = stage;
        stage.setTitle("Paint");

        ToolSettings settings = new ToolSettings();
        tabPane = new TabPane();
        ActivityLogger.start(tabPane);
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> ActivityLogger.log("Switched tab"));
                fileActions = new FileActions(stage, tabPane, settings);
        fileActions.newFile();
        EditActions editActions = new EditActions(fileActions);
        StatusBar statusBar = new StatusBar();
        AutoSaveTimer autoSaveTimer = new AutoSaveTimer(tabPane, statusBar.getTimerLabel());
        fileActions.setAutoSaveTimer(autoSaveTimer);
        autoSaveTimer.start();
        webServer = new ImageWebServer(tabPane);
        webServer.start();
        AppMenuBar menuBar = new AppMenuBar(fileActions, editActions, () -> resizeCanvas(), statusBar);

        PaintToolBar toolBar = new PaintToolBar(settings);

        BorderPane root = new BorderPane();
        root.setTop(new VBox(menuBar, toolBar));
        root.setCenter(tabPane);
        root.setBottom(statusBar);

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
        ActivityLogger.log("Program started");

       stage.setOnCloseRequest(e -> {
            if (!fileActions.confirmDiscardAll()) {
                e.consume(); //stops
            }
        });

    }

    /**
     * Stops the web server, called by Main when the program closes.
     */
    public void shutdown() {
        ActivityLogger.log("Program closed");
        webServer.stop();
        ActivityLogger.stop();
    }
       //does what it says, opens tje file and prevents other files
    /**
     * Asks the user for a new width and height, then resizes the canvas
     * in the currently selected tab.
     */

    private void resizeCanvas() {
        DocumentTab tab = fileActions.currentTab();
        if (tab == null) {
            Dialogs.showError("No image open to resize");
            return;
        }
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
            Dialogs.showError("Width and height must be a positive number");
            return;
        }
        tab.getCanvas().resizeCanvas(w, h);
        ActivityLogger.log("Resized canvas to " + w + "x" + h);
    } catch (NumberFormatException e) {
        Dialogs.showError("Please enter whole numbers only");
    }
}
}