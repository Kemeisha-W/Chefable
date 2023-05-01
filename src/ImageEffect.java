import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface ImageEffect {
    int toEffect(int pixel);

    void draw(Graphics2D g2);
    Rectangle2D.Double getBoundingRectangle();
    void update();

    default BufferedImage copyImage(BufferedImage src) {
        if (src == null)
            return null;

        BufferedImage copy = new BufferedImage (src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = copy.createGraphics();

        g2d.drawImage(src, 0, 0, null);		// source image is drawn on copy
        g2d.dispose();

        return copy;
    }
    default BufferedImage loadImage(String filename) {
        BufferedImage bi = null;

        File file = new File (filename);
        try {
            bi = ImageIO.read(file);
        }
        catch (IOException ioe) {
            System.out.println ("Error opening file " + filename + ": " + ioe);
        }
        return bi;
    }
}