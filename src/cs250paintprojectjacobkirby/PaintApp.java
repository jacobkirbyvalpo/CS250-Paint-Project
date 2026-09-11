/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cs250paintprojectjacobkirby;

/**
 *
 * @author Norboc
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class PaintApp implements ActionListener {
    //window everything sits in
    private JFrame frame;
    // components holds and draws image
    private ImagePanel panel;
    //file image comes from
    private File currentFile;
    //menu items for actione performed
    private JMenuItem openItem; 
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
    private JMenuItem exitItem;
// constructor, holds the GUI
    public PaintApp() {
               frame = new JFrame("Paint");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new ImagePanel();
        frame.add(new JScrollPane(panel));

        // menu items
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save As");
        exitItem = new JMenuItem("Exit");
        //this used since class implements action listener, they all call the same
        //action performed method
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        exitItem.addActionListener(this);

        // File menu holds the items (drop down)
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // menu bar holds the menus
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setSize(800, 600);
        frame.setVisible(true);
       }  
        public void actionPerformed(ActionEvent event) {
            //returns the object the user clicked
        Object src = event.getSource();

        if (src == openItem) {
            openFile();
        } else if (src == saveItem) {
            save();
        } else if (src == saveAsItem) {
            saveAs();
        } else if (src == exitItem) {
            System.exit(0);
        }
    }
// shows file dialog and reads chosen image
    private void openFile() {
        //limits files
         JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Images", "jpg", "gif", "png", "tif");
    chooser.setFileFilter(filter);
//centers dialog on window, blocks until user picks
// a file or cancels dialog
    int returnVal = chooser.showOpenDialog(frame);
    // chose a file
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = chooser.getSelectedFile();
        try {
            //related to buffered image in LoadImage, turns into pixels in mem
            BufferedImage img = ImageIO.read(file);
            //incase user selects a file anyway not supported
            if (img == null) {
                JOptionPane.showMessageDialog(frame, "Not a supported image format.");
                return;
            }
            //hands to panel and sizes to image, and repaints it
            panel.setImage(img);
            currentFile = file;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Could not read the file.");
        }
    }
    }

    private void save() {
    }
// not implemented yet will before due
    private void saveAs() {
        // not implemented yet will before due
    }
}

