package cs250paintprojectjacobkirby;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
//using Jpanel, we need its functionality of a normal panel
//we need to use extend for this functionality so we can hook an image
public class ImagePanel extends JPanel {
//image currently open, three different methods need the img
    //saves rect of pixels in mem
    private BufferedImage img;
//needs the image from Buffered,
    protected void paintComponent(Graphics g) {
        // runs jpanel, without it leftover pixels appear, calls everytime you
        //need it
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, null);
            //null when nothing is open, a state no error.
        }
    }

  // Called by PaintApp whenever a new image is loaded.only place img change
public void setImage(BufferedImage newImg) {
    // store the new image in the field so paintComponent can draw it later
    this.img = newImg;
    if (newImg != null) {
        // tell the layout how big this panel wants to be
        // use to see if scroll is needed
        setPreferredSize(new Dimension(newImg.getWidth(), newImg.getHeight()));
    }
    // size requirements changed -- ask the layout manager to run again
    revalidate();
    // appearance changed -- ask Swing to call paintComponent again
    repaint();
    
    //this lets the window change as needed for the user/.
}
//hands image to PaintApp.java
    public BufferedImage getImage() {
        return img;
    }
}