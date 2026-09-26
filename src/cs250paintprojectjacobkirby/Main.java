package cs250paintprojectjacobkirby;

import javafx.application.Application;
import javafx.stage.Stage;

//JavaFX must extend Application. launch() starts the JavaFX
//runtime, which then calls start() on its own thread
/**
 * Starts the paint program and hands the main window to PaintApp.
 *
 * @author Jacob
 */
public class Main extends Application {

 /**
 * Called by JavaFX once the runtime is ready. Builds the application
 * inside the window it is given.
 *
 * @param stage the main window created by JavaFX
 */
    @Override
    public void start(Stage stage) {
        //hand the primary window tso the differo PaintApp to fill in
        new PaintApp(stage);
    }
    
    /**
     * Launches the JavaFX application.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        launch(args);
    }
}