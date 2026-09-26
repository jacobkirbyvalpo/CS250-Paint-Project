package cs250paintprojectjacobkirby;

import java.io.File;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;


/**
 * One tab in the window. Holds its own canvas and knws which file it comes from.
 *
 * @author Jacob
 */

public class DocumentTab extends Tab {

    //holds and draws the image
    private ImageCanvas canvas;

    //file the image came from. null means nothing opened or saved yet
    private File currentFile;

    public DocumentTab(ToolSettings settings, Image img, File file) {
        canvas = new ImageCanvas(settings);
        canvas.setImage(img);
        currentFile = file;
        setText(file.getName());

        // scrollbars when the image is bigger than the window
        setContent(new ScrollPane(canvas));
    }

    public ImageCanvas getCanvas() {
        return canvas;
    }

    public File getFile() {
        return currentFile;
    }

    public void setFile(File newFile) {
        currentFile = newFile;
        setText(newFile.getName());
    }
}