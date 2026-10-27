package cs250paintprojectjacobkirby;

import java.io.IOException;

 /**
 * Shows desktop notifications in the corner of the screen using the
 * notify-send command. Each one runs on its own thread so the window
 * never waits for it to finish.
 *
 * @author Jacob
 */

//Notifications work only on Linux I believe, using a arch library from pacman
public class Notifier {

    private static boolean enabled = true;

     /**
     * Turns notifications on or off.
     *
     */
    public static void setEnabled(boolean on) {
        enabled = on;
    }

     /**
     * Shows a notification if they are enabled.
     */
    public static void show(String title, String message) {
        if (!enabled) {
            return;
        }
        Thread thread = new Thread(() -> {
            try {
                new ProcessBuilder("notify-send", "-a", "Jacob's Paint", title, message).start().waitFor();
            } catch (IOException | InterruptedException e) {
                System.err.println("Could not show notification: " + e.getMessage());
            }
        });
          thread.setDaemon(true);
         thread.start();
    }
}