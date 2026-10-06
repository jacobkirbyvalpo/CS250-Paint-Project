package cs250paintprojectjacobkirby;

import java.io.File;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.Group;


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
     /**
     * Creates a tab with its own canvas.
     *
     * @param settings  drawing settings, given to the canvas
     * @param img the image to show, or null for a tab with no image
     * @param file the file the image came from, or null for a new untitled tab
     */
    public DocumentTab(ToolSettings settings, Image img, File file) {
        canvas = new ImageCanvas(settings);
        canvas.setImage(img);
        currentFile = file;
        if (file == null) {
            setText("Untitled");
            } else {
            setText(file.getName());
            }
        // scrollbars when the image is bigger than the window
        setContent(new ScrollPane(new Group(canvas, canvas.getSelectionBox())));
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