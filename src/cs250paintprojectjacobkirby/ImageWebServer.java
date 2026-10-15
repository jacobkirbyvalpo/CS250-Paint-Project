package cs250paintprojectjacobkirby;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javax.imageio.ImageIO;

 /**
 * web server at http://localhost:8080
 *
 * @author Jacob
 */
//using port 8080 to avoid my python -m http.server from code OSS and 8080 is 
//default traditional.
public class ImageWebServer {

    private static final int PORT = 8080;
    private HttpServer server;
    private TabPane tabPane;

     /**
     * Creates the web server, it does not run until start is called.
     *
     */
    public ImageWebServer(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    /**
     * Starts the server on its own thread.
     */
    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext("/", exchange -> handle(exchange));
            server.start();
        } catch (IOException e) {
            System.err.println("Web server could not start on port " + PORT);
        }
    }

     /**
     * Stops the server so the program can close.
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    /**
     * Answers a browser request
     */
    private void handle(HttpExchange exchange) throws IOException {
        CompletableFuture<String> page = new CompletableFuture<>();
        Platform.runLater(() -> page.complete(buildPage()));
        byte[] body;
        try {
            body = page.get().getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            body = "Could not build the page.".getBytes(StandardCharsets.UTF_8);
        }
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }

     /**
     * Builds the html page with every shared tab.
     */
    private String buildPage() {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Jacob's Paint</title>");
        html.append("<meta http-equiv=\"refresh\" content=\"5\"></head><body>");
        int shared = 0;
        for (Tab t : tabPane.getTabs()) {
            DocumentTab tab = (DocumentTab) t;
            if (!tab.isShared()) {
                continue;
            }
            shared++;
            html.append("<h2>").append(tab.getText()).append("</h2>");
            html.append("<img src=\"data:image/png;base64,").append(toBase64Png(tab)).append("\">");
        }
        if (shared == 0) {
            html.append("<p>No images selected. Right-click a tab in Jacob's Paint and check Share on Web.</p>");
        }
        html.append("</body></html>");
        return html.toString();
    }

     /**
     * Turns a tab's image into png text that can go straight into an img tag.
     */
    private String toBase64Png(DocumentTab tab) {
        BufferedImage bimg = SwingFXUtils.fromFXImage(tab.getCanvas().getImage(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bimg, "png", out);
        } catch (IOException e) {
            return "";
        }
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }
}
