package cs250paintprojectjacobkirby;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.util.Optional;

/**
 * Handles every file operation and save check
 *
 * @author Jacob
 */
public class FileActions {

    //the window handed in by Main JavaFX
    private Stage stage;

    private TabPane tabPane;

    private ToolSettings settings;

    private AutoSaveTimer autoSaveTimer;

    /**
     * Creates the file handler.
     *
     * @param stage the main window, used to position file dialogs
     * @param tabPane the tab area that opened images are added to
     * @param settings shared drawing settings, given to each new canvas
     */
    public FileActions(Stage stage, TabPane tabPane, ToolSettings settings) {
        this.stage = stage;
        this.tabPane = tabPane;
        this.settings = settings;
    }

    /**
     * Gives the file handler the autosave timer so a save can reset it.
     *
     * @param autoSaveTimer the timer to reset after each save
     */
    public void setAutoSaveTimer(AutoSaveTimer autoSaveTimer) {
        this.autoSaveTimer = autoSaveTimer;
    }

    /**
     * Returns the tab the user currently has selected.
     *
     * @return the selected tab, or null if no tabs are open
     */
    public DocumentTab currentTab() {
        return (DocumentTab) tabPane.getSelectionModel().getSelectedItem();
    }

    /**
     * Shows an open dialog and loads the chosen image into a new tab.
     */
    public void openFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Image");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images", "*.jpg",
                "*.jpeg", "*.gif", "*.png", "*.tif", "*.bmp"));
        //opens file dialog
        File file = chooser.showOpenDialog(stage);

        //FileChooser returns null if the user cancelled the dialog
        if (file != null) {
            //JavaFX loads images fromstring not a File
            Image img = new Image(file.toURI().toString());

            if (img.isError()) {
                Dialogs.showError("Not a supported image format.");
                return;
            }

            DocumentTab tab = new DocumentTab(settings, img, file);
            tab.setOnCloseRequest(e -> {
                tabPane.getSelectionModel().select(tab);
                if (!confirmDiscard()) {
                    e.consume();
                }
            });
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            ActivityLogger.log("Opened " + file.getName() + " in tab " + tabPane.getTabs().size());
        }
    }
    
    /**
     * Opens a new tab with a blank white image canvas.
     */
    public void newFile() {
        DocumentTab tab = new DocumentTab(settings, null, null);
        tab.getCanvas().makeBlank(800, 600);
        addTab(tab);
        ActivityLogger.log("New blank image in tab " + tabPane.getTabs().size());
    }

    /**
     *adds a tab to the window and selects it, unsaved check if tab closed
     *
     * @param tab the tab to add
     */
    private void addTab(DocumentTab tab) {
        tab.setOnCloseRequest(e -> {
            tabPane.getSelectionModel().select(tab);
            if (!confirmDiscard()) {
                e.consume();
            }
        });
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }
    /**
     * Saves the current tab to its file. A fall back to save as if needed.
     */
    public void save() {
        DocumentTab tab = currentTab();
        if (tab == null) {
            Dialogs.showError("Nothing to save.");
            return;
        }
        if (tab.getFile() == null) {
            saveAs();
            return;
        }
        writeTo(tab.getFile());
    }
    /**
     * Asks for a file name and format, saves the current tab there, and
     * remembers that file for later saves.
     */
    public void saveAs() {
        DocumentTab tab = currentTab();
        if (tab == null) {
            Dialogs.showError("Nothing to save.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Image");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG", "*.png"));
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JPG", "*.jpg"));
          chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("BMP", "*.bmp"));
          chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JPEG", "*.jpeg"));

        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            String oldFormat = null;
            if (tab.getFile() != null) {
                oldFormat = ImageFormats.getExtension(tab.getFile());
            }
            String newFormat = ImageFormats.getExtension(file);
            if (ImageFormats.needsWarning(oldFormat, newFormat)
                    && !Dialogs.confirm("Saving as " + newFormat.toUpperCase()
                    + " uses lossy compression, so some image detail will be lost. Continue?")) {
                return;
            }
            writeTo(file);
            tab.setFile(file);
        }
    }

    // clears the image without exiting the program
    /**
     * Closes the current tab, asks about unsaved changes first, this is the 
     * save check system in place.
     */
    public void closeImage() {
        DocumentTab tab = currentTab();
        if (tab == null) {
            return;
        }
        //smart save system
         if(!confirmDiscard()){
         return;
        }
        ActivityLogger.log("Closed tab");
        tabPane.getTabs().remove(tab);
    }

    //hared by save and saveAs only difference between the two
    //is where the file came from
    /**
     * writes the data to disk. The format comes from the
     * file extension, defaults to png.
     *
     * @param file where to write the image
     */
    private void writeTo(File file) {
        ImageCanvas canvas = currentTab().getCanvas();
        if (!canvas.hasImage()) {
            Dialogs.showError("Nothing to save.");
            return;
        }
        try {
            //BufferedImage that ImageIO can write.
            String format = ImageFormats.getExtension(file);

                     BufferedImage bimg = SwingFXUtils.fromFXImage(canvas.getImage(), null);
            bimg = ImageFormats.toSaveable(bimg, format);
            boolean ok = ImageIO.write(bimg, format, file);

            if (!ok) {
                Dialogs.showError("No writer available for that file format.");
            }
            else {
                canvas.markSaved();
                ActivityLogger.log("Saved " + file.getName());
                if (autoSaveTimer != null) {
                    autoSaveTimer.reset();
                }
            }
        } catch (IOException e) {
            Dialogs.showError("Could not write to the file.");
        }
}
//checks if modified, if not, returns true
    /**
     * if unsaved changes, askes about it for current tab.
     *
     * @return true if it is safe to continue, false if the user cancelled
     */
    public boolean confirmDiscard() {
     DocumentTab tab = currentTab();
     if (tab == null || !tab.getCanvas().isModified()){
      return true;
     }
     ButtonType saveButton = new ButtonType("Save");
     ButtonType dontsaveButton = new ButtonType("Do Not Save");

     Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
             "You have unsaved changes, would you like to save them?", saveButton, dontsaveButton, ButtonType.CANCEL);

     Optional<ButtonType> result = alert.showAndWait();

         //dialog closed without picking anything
    if (!result.isPresent()) {
        return false;
    }

    if (result.get() == saveButton) {
        save();
        return true;
    }

    if (result.get() == dontsaveButton) {
        return true;
    }

    //cancel
    return false;

    }

    /**
     * Checks every open tab for unsaved changes.
     *
     * @return true if every tab is safe to close, false if the user cancelled
     */
    public boolean confirmDiscardAll() {
        for (Tab t : tabPane.getTabs()) {
            tabPane.getSelectionModel().select(t);
            if (!confirmDiscard()) {
                return false;
            }
        }
        return true;
    }
}