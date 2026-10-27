package cs250paintprojectjacobkirby;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.scene.control.TabPane;

  /**
 * Logs what the user does to a text file, one event per line.
 *
 * @author Jacob
 */
public class ActivityLogger {

    private static final File LOG_FILE = new File(System.getProperty("user.home"), "JacobsPaintLog.txt");
    private static final String STOP = "STOP";
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private static TabPane tabPane;
    private static Thread writer;

     /**
     * Starts the background thread that writes lines to log file.
     *@param pane
     */
    public static void start(TabPane pane) {
        tabPane = pane;
        writer = new Thread(() -> writeLines());
        writer.setDaemon(true);
        writer.start();
    }

     /**
     * Adds one event to the log file.
     *
     * @param event what the user did during log
     */
    public static void log(String event) {
        if (writer == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        queue.add(now.format(DATE) + " [" + tabName() + "] " + now.format(TIME) + " " + event);
    }
     /**
     * Stops the background thread after it writes everything still queued.
     */
    public static void stop() {
        if (writer == null) {
              return;
        }
        queue.add(STOP);
        try {
            writer.join(1000);
        } catch (InterruptedException e) {
            System.err.println("Logger did not finish writing");
        }
    }
     /**
     * Gets the name to show in brackets for the current tab user is on.
     */
    private static String tabName() {
        DocumentTab tab = (DocumentTab) tabPane.getSelectionModel().getSelectedItem();
        if (tab == null) {
            return "no tab";
        }
        if (tab.getFile() == null) {
            return "new tab";
        }
        return tab.getFile().getName();
    }

    /**
     * Runs on the background thread. Waits for lines.
     */
    private static void writeLines() {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            while (true) {
                String line = queue.take();
                if (line.equals(STOP)) {
                    return;
                }
                  out.println(line);
                  out.flush();
            }
        } catch (IOException | InterruptedException e) {
             System.err.println("Logging stopped: " + e.getMessage());
        }
    }
}