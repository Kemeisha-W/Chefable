import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Chest implements Sprite{
    private final SoundManager soundManager;        // reference to SoundManager to play clip
    private JPanel panel;                // JPanel on which image will be drawn
    private Dimension dimension;
    private final int width=xSize+32;
    private final int height=ySize+32;
    private Player player;
    private Animation closedAni;
    private Animation openAni;
    private Animation currentAni;
    private int x;
    private int y;
    public int offsetX;
    public int offsetY;
    private final ArrayList<Image> openImages= new ArrayList<>();
    private final ArrayList<Image> closedImages= new ArrayList<>();

    private static Chest single_instance = null;

    private Chest(JPanel panel, Player player){
        this.panel = panel;
        dimension = panel.getSize();
        currentAni = closedAni;
        this.player = player;
        loadClosedChest();
        loadOpenChest();
        soundManager = SoundManager.getInstance();
    }

    static Chest getInstance(JPanel panel, Player player) {
        if (Chest.single_instance == null)
            Chest.single_instance = new Chest(panel, player);

        return Chest.single_instance;
    }

    private void loadClosedChest() {
        for (int num = 0; num < 5; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/Animated Chests/black_closed"+num+".png");
            i = i.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            closedImages.add(i);
        }

        closedAni = new Animation(true);    // play continuously

        for (int num = 0; num < 5; num++) {
            closedAni.addFrame(closedImages.get(num), 150);
        }
    }

    private void loadOpenChest() {
        for (int num = 0; num < 5; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/Animated Chests/black_open"+num+".png");
            i = i.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            openImages.add(i);
        }

        openAni = new Animation(true);    // play continuously

        for (int num = 0; num < 5; num++) {
            openAni.addFrame(openImages.get(num), 150);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!currentAni.isStillActive()) {
            return;
        }
        offsetY =offsetY-32;
        g2.drawImage(currentAni.getImage(), x+offsetX, y+offsetY, width,height, null);
    }

    @Override
    public boolean collidesWithPlayer() {
        if(x==0 || y==0)
            return false;
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double playerRect = player.getBoundingRectangle();

        if (myRect.intersects(playerRect)) {
            System.out.println ("Heart Collision with player!");
            //TODO ADD CHEST SOUND OPEN AND CLOSE
            return true;
        }
        else
            return false;
    }

    @Override
    public Rectangle2D.Double getBoundingRectangle() {
        //        System.out.println("Heart bounding X:"+(x+offsetX)+" y:"+(y+offsetY));
        return new Rectangle2D.Double (x+offsetX, (y+offsetY), width, height);
    }

    @Override
    public void update() {
        if (!currentAni.isStillActive()) {
            return;
        }
        currentAni.update();
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
        return currentAni;
    }

    public void setAnimation(String ani) {
        switch (ani) {
            case "open"-> currentAni = openAni;
            case "close"-> currentAni = closedAni;
        }
    }
}
