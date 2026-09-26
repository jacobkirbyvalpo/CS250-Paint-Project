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
        fileActions = new FileActions(stage, tabPane, settings);

        AppMenuBar menuBar = new AppMenuBar(fileActions, () -> resizeCanvas());

        PaintToolBar toolBar = new PaintToolBar(settings);

        BorderPane root = new BorderPane();
        root.setTop(new VBox(menuBar, toolBar));
        root.setCenter(tabPane);

        stage.setScene(new Scene(root, 800, 600));
        stage.show();

       stage.setOnCloseRequest(e -> {
            if (!fileActions.confirmDiscardAll()) {
                e.consume(); //stops
            }
        });

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
    } catch (NumberFormatException e) {
        Dialogs.showError("Please enter whole numbers only");
    }
}
}