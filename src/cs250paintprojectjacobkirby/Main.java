package cs250paintprojectjacobkirby;

import javafx.application.Application;
import javafx.stage.Stage;

//JavaFX must extend Application. launch() starts the JavaFX
//runtime, which then calls start() on its own thread
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        //hand the primary window to PaintApp to fill in
        new PaintApp(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}