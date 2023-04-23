import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.Image;


public class Star {

    private static final int XSIZE = 50;        // width of the image
    private static final int YSIZE = 50;        // height of the image
    //private static final int DX = 2;		// amount of pixels to move in one update
    private static final int YPOS = 150;        // vertical position of the image

    private JPanel panel;                // JPanel on which image will be drawn
    private Dimension dimension;
    private int x;
    private int y;
    private int dx;

    private Player player;
    private Animation animation;
    private ArrayList<Image> starImages= new ArrayList<>();

    private static Star single_instance = null;

    //Graphics2D g2;

    int time, timeChange;                // to control when the image is grayed
    boolean originalImage, grayImage;


    private Star (JPanel panel, Player player) {
        this.panel = panel;
        //Graphics g = window.getGraphics ();
        //g2 = (Graphics2D) g;

        dimension = panel.getSize();

        dx = 2;

        this.player = player;

        time = 0;                // range is 0 to 10
        timeChange = 1;                // set to 1
        originalImage = true;
        grayImage = false;
        loadStar();
    }

    private void loadStar(){
        for(int num=0;num<4;num++){
            Image i = ImageManager.loadBufferedImage("Assets/star/star"+num+".png");
            i = i.getScaledInstance(XSIZE,YSIZE,Image.SCALE_DEFAULT);
            starImages.add(i);
        }

        animation = new Animation(false);    // play once continuously

        for(int num=0;num<4;num++){
            animation.addFrame(starImages.get(num), 150);
        }
    }


    public void draw (Graphics2D g2) {
        if (!animation.isStillActive()) {
            return;
        }
        g2.drawImage(animation.getImage(), x, y, XSIZE, YSIZE, null);

    }


    public boolean collidesWithPlayer () {
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double playerRect = player.getBoundingRectangle();

        if (myRect.intersects(playerRect)) {
            System.out.println ("Collision with player!");
            //TODO Add play sound to move to next map
            return true;
        }
        else
            return false;
    }


    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double (x, y, XSIZE, YSIZE);
    }


    public void update() {
        if (!animation.isStillActive()) {
            return;
        }
        animation.update();
    }

    public static synchronized Star getInstance(JPanel panel, Player player)
    {
        if (single_instance == null)
            single_instance = new Star(panel, player);

        return single_instance;
    }
    public int getX() {
        return x;
    }


    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }


    public void setY(int y) {
        this.y = y;
    }


    public Animation getAnimation() {
        return animation;
    }

}