import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Basket implements Sprite{
    private final SoundManager soundManager;        // reference to SoundManager to play clip
    private JPanel panel;                // JPanel on which image will be drawn
    private Dimension dimension;
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

    private static Basket single_instance = null;

    private Basket(JPanel panel, Player player){
        this.panel = panel;
        dimension = panel.getSize();
        currentAni = closedAni;
        this.player = player;
        loadClosedBasket();
        loadOpenBasket();
        soundManager = SoundManager.getInstance();
    }

    static Basket getInstance(JPanel panel, Player player) {
        if (Basket.single_instance == null)
            Basket.single_instance = new Basket(panel, player);

        return Basket.single_instance;
    }

    private void loadClosedBasket() {
        for (int num = 0; num < 5; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/Animated Chests/black_closed"+num+".png");
            i = i.getScaledInstance(xSize, ySize, Image.SCALE_DEFAULT);
            closedImages.add(i);
        }

        closedAni = new Animation(true);    // play continuously

        for (int num = 0; num < 5; num++) {
            closedAni.addFrame(closedImages.get(num), 150);
        }
    }

    private void loadOpenBasket() {
        for (int num = 0; num < 5; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/Animated Chests/black_open"+num+".png");
            i = i.getScaledInstance(xSize, ySize, Image.SCALE_DEFAULT);
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
        g2.drawImage(currentAni.getImage(), x+offsetX, y+offsetY, xSize, ySize, null);
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
