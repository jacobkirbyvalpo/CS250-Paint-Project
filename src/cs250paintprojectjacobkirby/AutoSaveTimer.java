package cs250paintprojectjacobkirby;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import javax.imageio.ImageIO;

 /**
 * Counts down and saves a backup copy of every tab with unsaved changes
 * when it reaches zero on the timer.
 *
 * @author Jacob
 */

public class AutoSaveTimer {

    private static final int INTERVAL_SECONDS = 20;
    private int secondsLeft = INTERVAL_SECONDS;
    private Timeline timeline;
    private TabPane tabPane;
    private Label label;

    /**
     * Creates the timer, it does not start to count until called.
     */
    public AutoSaveTimer(TabPane tabPane, Label label) {
        this.tabPane = tabPane;
        this.label = label;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        updateLabel();
    }

     /**
     * Starts the countdown.
     */
    public void start() {
        timeline.play();
    }

     /**
     * Starts the countdown over.
     */
    public void reset() {
        secondsLeft = INTERVAL_SECONDS;
        updateLabel();
    }

     /**
     * Runs once a second, saves the backups when the countdown timer hits zero.
     */
    private void tick() {
        secondsLeft--;
        if (secondsLeft <= 0) {
            saveBackups();
            secondsLeft = INTERVAL_SECONDS;
        }
        updateLabel();
    }

     /**
     * Writes a png backup of every tab that has unsaved changes once timer reaches zero
     */
    
    //this uses a linux file directory
    private void saveBackups() {
        File folder = new File(System.getProperty("user.home"), "JacobsPaintAutosave");
        folder.mkdirs();
        int number = 0;
        int saved = 0;
        for (Tab t : tabPane.getTabs()) {
            DocumentTab tab = (DocumentTab) t;
            number++;
            if (!tab.getCanvas().isModified()) {
                continue;
            }
            File out = new File(folder, "tab" + number + "-" + tab.getText() + ".png");
            BufferedImage bimg = SwingFXUtils.fromFXImage(tab.getCanvas().getImage(), null);
            try {
                ImageIO.write(bimg, "png", out);
                ActivityLogger.log("Autosaved " + out.getName());
                saved++;
            } catch (IOException e) {
                System.err.println("Autosave failed for " + out);
            }
        }
        if (saved > 0) {
            Notifier.show("Jacob's Paint", "Autosaved " + saved + " tab(s)");
        }
    }

     /**
     * Writes the time left into the label.
     */
    private void updateLabel() {
        label.setText("Autosave in " + secondsLeft + "s");
    }
}
