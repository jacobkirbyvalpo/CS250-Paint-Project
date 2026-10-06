package cs250paintprojectjacobkirby;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Handles the edit operations that work on the current tab.
 *
 * @author Jacob
 */
public class EditActions {

    private FileActions fileActions;

    /**
     * edit handler.
     *
     * @param fileActions used to find the tab the user has selected
     */
    public EditActions(FileActions fileActions) {
        this.fileActions = fileActions;
    }
    
     /**
     * copies selected area with selection tool.
     */
    public void copy() {
        DocumentTab tab = fileActions.currentTab();
        if (tab == null) {
            return;
        }
        tab.getCanvas().copySelection();
    }

    /**
     * Asks the user to confirm, then paints the current canvas white.
     */
    public void clearCanvas() {
        DocumentTab tab = fileActions.currentTab();
        if (tab == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Do you wish to clear the whole canvas?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            tab.getCanvas().clear();
        }
    }
    
      /**
     * Undo the last change on the current canvas.
     */
    public void undo() {
        DocumentTab tab = fileActions.currentTab();
        if (tab == null) {
            return;
        }
        tab.getCanvas().undo();
    }

    /**
     * re does the last undone change on the current canvas.
     */
    public void redo() {
        DocumentTab tab = fileActions.currentTab();
        if (tab == null) {
            return;
        }
        tab.getCanvas().redo();
    }
    
}