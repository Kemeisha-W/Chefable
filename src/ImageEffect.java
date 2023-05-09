import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public interface ImageEffect {
    int toEffect(int pixel);

    boolean isActive();

    void activate();

    void deActivate();

    void draw(Graphics2D g2);

    default BufferedImage copyImage(BufferedImage src) {
        if (src == null)
            return null;

        BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = copy.createGraphics();

        g2d.drawImage(src, 0, 0, null);        // source image is drawn on copy
        g2d.dispose();

        return copy;
    }
}