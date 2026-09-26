package cs250paintprojectjacobkirby;

import javafx.scene.paint.Color;
/**
 *converst a color into readable text, hex code, RGB and the most nearest color name
 *
 * @author Jacob
 */

public class ColorNames {

    private static final String[] NAMES = {
        "Black", "White", "Red", "Green", "Blue", "Yellow",
        "Cyan", "Magenta", "Gray", "Orange", "Purple", "Brown", "Pink"
    };

    private static final Color[] COLORS = {
        Color.BLACK, Color.WHITE, Color.RED, Color.LIME, Color.BLUE, Color.YELLOW,
        Color.CYAN, Color.MAGENTA, Color.GRAY, Color.ORANGE, Color.PURPLE, Color.BROWN, Color.PINK
    };
    
    
    public static String toHex(Color c) {
        int r = (int) Math.round(c.getRed() * 255);
        int g = (int) Math.round(c.getGreen() * 255);
        int b = (int) Math.round(c.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }
    
        public static String toRgb(Color c) {
        int r = (int) Math.round(c.getRed() * 255);
        int g = (int) Math.round(c.getGreen() * 255);
        int b = (int) Math.round(c.getBlue() * 255);
        return "RGB(" + r + ", " + g + ", " + b + ")";
    }
        
            public static String toName(Color c) {
        int best = 0;
        double bestDist = Double.MAX_VALUE;

        for (int i = 0; i < COLORS.length; i++) {
            double dr = c.getRed() - COLORS[i].getRed();
            double dg = c.getGreen() - COLORS[i].getGreen();
            double db = c.getBlue() - COLORS[i].getBlue();
            double dist = dr *dr + dg * dg+db * db;

            if (dist < bestDist) {
                bestDist = dist;
                best = i;
            }
        }
        return NAMES[best];
    }
}