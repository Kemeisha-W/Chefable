import java.awt.*;
import java.awt.geom.Rectangle2D;

public interface Sprite {

    int xSize = 32;        // width of the image
    int ySize = 32;        // height of the image
    void draw(Graphics2D g2);

    boolean collidesWithPlayer();

    Rectangle2D.Double getBoundingRectangle();

    void update();

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    Animation getAnimation();
}
