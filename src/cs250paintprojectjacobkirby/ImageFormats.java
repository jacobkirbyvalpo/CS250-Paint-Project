package cs250paintprojectjacobkirby;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Rules for the image file formats.
 *
 * @author Jacob
 */

public class ImageFormats {

    /**
     * Gets the format of a file from its extension, defaults to png.
     */
    public static String getExtension(File file) {
            String name = file.getName();
             int dot = name.lastIndexOf(".");
             String format;
             if (dot != -1){
              format =name.substring(dot+1).toLowerCase();
             }
             else {
              format = "png";
             }
        return format;
    }

    /**
     * Checks if a format throws away image detail when saving.
     */
    public static boolean isLossy(String format) {
        return format.equals("jpg") || format.equals("jpeg");
    }

     /**
     * Checks if changing from one format to another can lose any image data.
     */
    public static boolean needsWarning(String oldFormat, String newFormat) {
        if (!isLossy(newFormat)) {
            return false;
        }
        if (oldFormat != null && isLossy(oldFormat)) {
            return false;
        }
        return true;
    }

    /**
     * Gets an image ready for ImageIO to write in the given provided format.
     */
    public static BufferedImage toSaveable(BufferedImage bimg, String format) {
             //no format to read file image error
             //https://bugs.openjdk.org/browse/JDK-8211748

//this is all to avoid the alpha channel PNG to JPG or other format bug, documented above because it was evil.
             if (format.equals("jpg") || format.equals("jpeg") || format.equals("bmp")){
             BufferedImage rgb = new BufferedImage(bimg.getWidth(), bimg.getHeight(),
             BufferedImage.TYPE_INT_RGB);

             Graphics2D g = rgb.createGraphics();
             g.drawImage(bimg, 0, 0, null);
             g.dispose();
             bimg = rgb;
        }
        return bimg;
    }
}
