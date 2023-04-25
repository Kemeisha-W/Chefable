import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Heart implements Sprite{
    private final SoundManager soundManager;		// reference to SoundManager to play clip
    private JPanel panel;                // JPanel on which image will be drawn
    private Dimension dimension;
    private Player player;
    private Animation animation;
    private int x;
    private int y;
    public int offsetX;
    public int offsetY;
    private final ArrayList<Image> heartImages= new ArrayList<>();

    private static Heart single_instance = null;

    private Heart(JPanel panel, Player player){
        this.panel = panel;

        dimension = panel.getSize();

        this.player = player;
        loadHeart();
        soundManager = SoundManager.getInstance();
    }

    static Heart getInstance(JPanel panel, Player player) {
        if (Heart.single_instance == null)
            Heart.single_instance = new Heart(panel, player);

        return Heart.single_instance;
    }

    private void loadHeart() {
        for (int num = 11; num < 21; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/Gold/Gold_" + num + ".png");
            i = i.getScaledInstance(xSize, ySize, Image.SCALE_DEFAULT);
            heartImages.add(i);
        }

        animation = new Animation(true);    // play continuously

        for (int num = 11; num < 21; num++) {
            animation.addFrame(heartImages.get(num), 150);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!animation.isStillActive()) {
            return;
        }
        g2.drawImage(animation.getImage(), x+offsetX, y+offsetY, xSize, ySize, null);

    }

    @Override
    public boolean collidesWithPlayer() {
        if(x==0 || y==0)
            return false;
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double playerRect = player.getBoundingRectangle();

        if (myRect.intersects(playerRect)) {
            System.out.println ("Heart Collision with player!");
            //TODO ADD HEART SOUND and increment player heart
            return true;
        }
        else
            return false;
    }

    @Override
    public Rectangle2D.Double getBoundingRectangle() {
        //        System.out.println("Heart bounding X:"+(x+offsetX)+" y:"+(y+offsetY));
        return new Rectangle2D.Double (x+offsetX, (y+offsetY), xSize, ySize);
    }

    @Override
    public void update() {
        if (!animation.isStillActive()) {
            return;
        }
        animation.update();
    }

    @Override
    public int getX() {return x;}

    @Override
    public void setX(int x) {this.x = x;}

    @Override
    public int getY() {return y;}

    @Override
    public void setY(int y) {this.y = y;}

    @Override
    public Animation getAnimation() {
        return animation;
    }
}
