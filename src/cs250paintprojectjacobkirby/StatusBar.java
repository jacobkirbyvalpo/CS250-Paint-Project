package cs250paintprojectjacobkirby;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * shows the autosave countdown at the bottom.
 *
 * @author Jacob
 */

public class StatusBar extends HBox {

    private Label timerLabel = new Label();
     /**
     * Builds the bar with the timer label in it.
     */
    public StatusBar() {
        setPadding(new Insets(2, 8, 2, 8));
        getChildren().add(timerLabel);
    }

    /**
     * Gets the label of the autosave timer
     */
    public Label getTimerLabel() {
        return timerLabel;
    }

     /**
     * Shows or hides the autosave countdown.
     */
    public void setTimerVisible(boolean visible) {
        timerLabel.setVisible(visible);
    }
}
