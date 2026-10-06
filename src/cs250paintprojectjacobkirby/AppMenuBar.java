package cs250paintprojectjacobkirby;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;


/**
 * The File and Help menus, including keyboard shortcuts for the
 * main file actions.
 *
 * @author Jacob
 */

public class AppMenuBar extends MenuBar {

    private FileActions fileActions;
    private EditActions editActions;
    private Runnable onResize;
/**
 * Builds the menus and connects each of the items to its corresponding action.
 * @param fileActions this handles open, save, save as and close and ecit checks
 * @param onResize  if canvas resize is needed for image.
 * @param editActions this handles clearing the canvas
 */
    public AppMenuBar(FileActions fileActions, EditActions editActions, Runnable onResize) {
        this.fileActions = fileActions;
        this.onResize = onResize;
        this.editActions = editActions;

        //menu items
        MenuItem openItem = new MenuItem("Open");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem resizeItem = new MenuItem("Resize Canvas");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");
        MenuItem newItem = new MenuItem("New");
        MenuItem clearItem = new MenuItem("Clear Canvas");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        MenuItem copyItem = new MenuItem("Copy");
        // these set the actions
         openItem.setOnAction(e -> fileActions.openFile());
         saveItem.setOnAction(e -> fileActions.save());
         saveAsItem.setOnAction(e -> fileActions.saveAs());
         closeItem.setOnAction(e -> fileActions.closeImage());
         resizeItem.setOnAction(e -> onResize.run());
         helpItem.setOnAction(e -> Dialogs.showInfo("Help","There is no help, good luck buddy"));
         aboutItem.setOnAction(e -> Dialogs.showInfo("About","Jacobs Paint Project"));
         newItem.setOnAction(e -> fileActions.newFile());
         clearItem.setOnAction(e -> editActions.clearCanvas());
         undoItem.setOnAction(e -> editActions.undo());
         redoItem.setOnAction(e -> editActions.redo());
         copyItem.setOnAction(e -> editActions.copy());
         //shortcuts
         saveItem.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));
         openItem.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
         saveAsItem.setAccelerator(KeyCombination.keyCombination("Shortcut+Shift+S"));
         closeItem.setAccelerator(KeyCombination.keyCombination("Shortcut+W"));
         exitItem.setAccelerator(KeyCombination.keyCombination("Shortcut+Q"));
         newItem.setAccelerator(KeyCombination.keyCombination("Shortcut+N"));
         clearItem.setOnAction(e -> editActions.clearCanvas());
         undoItem.setAccelerator(KeyCombination.keyCombination("Shortcut+Z"));
         redoItem.setAccelerator(KeyCombination.keyCombination("Shortcut+Y"));
         copyItem.setAccelerator(KeyCombination.keyCombination("Shortcut+C"));
         //smart save upgrade
        exitItem.setOnAction(e -> {
        if (fileActions.confirmDiscardAll()) {
          javafx.application.Platform.exit();
             }
        });

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, closeItem, resizeItem,
                new SeparatorMenuItem(), exitItem);
        //creates the menu bar and pulls from the menus to populat it
        getMenus().add(fileMenu);
        //clear item menu
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(undoItem, redoItem, copyItem, new SeparatorMenuItem(), clearItem);
        getMenus().add(editMenu);
        
        //menu and about additional code
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(helpItem, aboutItem);
        getMenus().add(helpMenu);
    }
}