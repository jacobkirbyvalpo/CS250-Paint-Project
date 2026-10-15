package cs250paintprojectjacobkirby;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jacob
 */
public class ImageFormatsTest {
    
    public ImageFormatsTest() {
    }

    /**
     * Test of getExtension method, of class ImageFormats.
     */
    @Test
    public void testGetExtension() {
        assertEquals("png", ImageFormats.getExtension(new File("a.PNG")));
        assertEquals("jpg", ImageFormats.getExtension(new File("photo.jpg")));
        assertEquals("png", ImageFormats.getExtension(new File("noextension")));
    }

    /**
     * Test of isLossy method, of class ImageFormats.
     */
    @Test
    public void testIsLossy() {
        assertTrue(ImageFormats.isLossy("jpg"));
        assertTrue(ImageFormats.isLossy("jpeg"));
        assertFalse(ImageFormats.isLossy("png"));
        assertFalse(ImageFormats.isLossy("bmp"));
    }

    /**
     * Test of needsWarning method, of class ImageFormats.
     */
    @Test
    public void testNeedsWarning() {
        assertTrue(ImageFormats.needsWarning("png", "jpg"));
        assertTrue(ImageFormats.needsWarning(null, "jpg"));
        assertFalse(ImageFormats.needsWarning("jpg", "png"));
        assertFalse(ImageFormats.needsWarning("jpg", "jpeg"));
        assertFalse(ImageFormats.needsWarning("png", "bmp"));
    }

    /**
     * Test of toSaveable method, of class ImageFormats.
     */
    @Test
    public void testToSaveable() throws Exception {
        BufferedImage original = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 64; x++) {
                if ((x / 4 + y / 4) % 2 == 0) {
                    original.setRGB(x, y, 0xFFFF0000);
                }
                if ((x / 4 + y / 4) % 2 == 1) {
                    original.setRGB(x, y, 0xFF0000FF);
                }
            }
        }
        assertEquals(0, changedPixels(original, "png"));
        assertTrue(changedPixels(original, "jpg") > 0);
    }

    /**
     * Saves an image in a format in memory, reads it back, and counts how
     * many pixels came back different.
     */
    private int changedPixels(BufferedImage original, String format) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(ImageFormats.toSaveable(original, format), format, out);
        BufferedImage back = ImageIO.read(new ByteArrayInputStream(out.toByteArray()));
        int changed = 0;
        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 64; x++) {
                if ((back.getRGB(x, y) | 0xFF000000) != original.getRGB(x, y)) {
                    changed++;
                }
            }
        }
        return changed;
    }
    
}
