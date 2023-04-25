import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.Image;


public class Star implements Sprite {


    //private static final int DX = 2;		// amount of pixels to move in one update
    private static final int YPOS = 150;        // vertical position of the image

    private JPanel panel;                // JPanel on which image will be drawn
    private Dimension dimension;
    private int x;
    private int y;
    public int offsetX;
    public int offsetY;
    private int dx;
    private final SoundManager soundManager;		// reference to SoundManager to play clip

    private Player player;
    private Animation animation;
    private ArrayList<Image> starImages= new ArrayList<>();

    private static Star single_instance = null;

    int time, timeChange;                // to control when the image is grayed
    boolean originalImage, grayImage;


    private Star (JPanel panel, Player player) {
        this.panel = panel;

        dimension = panel.getSize();

        dx = 2;

        this.player = player;

        time = 0;                // range is 0 to 10
        timeChange = 1;                // set to 1
        originalImage = true;
        grayImage = false;
        loadStar();
        soundManager = SoundManager.getInstance();
    }
    public static Star getInstance(JPanel panel, Player player) {
        if (Star.single_instance == null)
            Star.single_instance = new Star(panel, player);

        return Star.single_instance;
    }
    private void loadStar() {
        for (int num = 0; num < 4; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/star/star" + num + ".png");
            i = i.getScaledInstance(xSize, ySize, Image.SCALE_DEFAULT);
            starImages.add(i);
        }

        animation = new Animation(true);    // play continuously

        for (int num = 0; num < 4; num++) {
            animation.addFrame(starImages.get(num), 150);
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
            System.out.println ("Collision with player!");
            soundManager.playSound("bubble", false);
            return true;
        }
        else
            return false;
    }


    @Override
    public Rectangle2D.Double getBoundingRectangle() {
//        System.out.println("Star bounding X:"+(x+offsetX)+" y:"+(y+offsetY));
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
    public int getX() {
        return x;
    }


    @Override
    public void setX(int x) {
        this.x = x;
    }


    @Override
    public int getY() {
        return y;
    }


    @Override
    public void setY(int y) {
        this.y = y;
    }


    @Override
    public Animation getAnimation() {
        return animation;
    }

}