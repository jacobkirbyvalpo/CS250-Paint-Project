/**
 * Popup dialogs used across the entire app
 * @author Norboc
 */
package cs250paintprojectjacobkirby;

import javafx.scene.control.Alert;
 
/**
 * Popup dialogs used across the entire app.
 *
 * @author Jacob
 */

public class Dialogs {
      public static void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
    //menu and about information popup
      
    /**
     * Shows an information popup and waits for the user to interact with it.
     *
     * @param title text for the window title bar
     * @param message the text to display
     */
    public static void showInfo(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.showAndWait();
}
}
