/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cs250paintprojectjacobkirby;

import java.util.Stack;
import javafx.scene.image.WritableImage;

/**
 *
 * @author Jacob
 */
public class UndoHistory {

    private Stack<WritableImage> undoStack = new Stack<>();
    private Stack<WritableImage> redoStack = new Stack<>();

    /**
     * Stores the image from before a change so that it can be undone.
     */
    public void save(WritableImage before) {
        undoStack.push(before);
        redoStack.clear();
    }

    /**
     * Checks if there is anything to undo.
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Checks if there is anything that can be or needs to be redo.
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Stores the current image for redo and gives back the previous one.
     */
    public WritableImage undo(WritableImage current) {
        redoStack.push(current);
        return undoStack.pop();
    }

     /**
     * Stores the current image for undo and gives back the last undone image one.
     */
    public WritableImage redo(WritableImage current) {
        undoStack.push(current);
        return redoStack.pop();
    }

      /**
     * Empties both undo and redo.
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
