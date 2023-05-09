import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Heart{
    private final SoundManager soundManager;        // reference to SoundManager to play clip

    private Player player;
    private int x;
    private int y;
    public int offsetX;

    public Heart(Player player){
        this.player = player;
        soundManager = SoundManager.getInstance();
    }

    public boolean collidesWithPlayer() {
        if(x==0 || y==0)
            return false;
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double playerRect = player.getBoundingRectangle();

        if (myRect.intersects(playerRect)) {
            System.out.println ("Heart Collision with player!");
            soundManager.playSound("heart", false);
            return true;
        }
        else
            return false;
    }

    public Rectangle2D.Double getBoundingRectangle() {
        System.out.println("Heart bounding X:"+(x+offsetX)+" y:"+(y));
        // width of the image
        int width = 30;
        // height of the image
        int height = 30;
        return new Rectangle2D.Double (x+offsetX, (y), width, height);
    }



    public int getX() {return x;}

    public void setX(int x) {this.x = x;}


    public int getY() {return y;}


    public void setY(int y) {this.y = y;}

}
