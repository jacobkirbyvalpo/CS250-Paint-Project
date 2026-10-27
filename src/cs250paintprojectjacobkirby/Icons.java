package cs250paintprojectjacobkirby;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

 /**
 * Puts the toolbar icons on the tool buttons.
 *
 * @author Jacob
 */
public class Icons {
     /**
     * Gives a button its icon and shows only the icon, the name is in the
     * button's tool tip.
     */
    public static void set(ToggleButton button, String name) {
        button.setGraphic(new ImageView(new Image(Icons.class.getResourceAsStream("icons/" + name + ".png"))));
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
}