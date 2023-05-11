import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Image;


public class Star implements Sprite {


    private int x;
    private int y;
    public int offsetX;
    public int offsetY;
    private final int width=xSize+32;
    private final int height=ySize+32;
    private final SoundManager soundManager;		// reference to SoundManager to play clip
    protected boolean collidedWith;
    private Player player;
    private Animation animation;
    private final ArrayList<Image> starImages= new ArrayList<>();

    int time, timeChange;                // to control when the image is grayed
    boolean originalImage, grayImage;


    protected Star (Player player) {
        collidedWith = false;
        this.player = player;
        time = 0;                // range is 0 to 10
        timeChange = 1;                // set to 1
        originalImage = true;
        grayImage = false;
        loadStar();
        soundManager = SoundManager.getInstance();
    }

    private void loadStar() {
        for (int num = 0; num < 4; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/star/star" + num + ".png");
            i = i.getScaledInstance(width, height, Image.SCALE_DEFAULT);
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
        offsetY -=32;
        g2.drawImage(animation.getImage(), x+offsetX, y+offsetY, width, height, null);
    }


    @Override
    public boolean collidesWithPlayer() {
        if(x==0 || y==0)
            return false;
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double playerRect = player.getBoundingRectangle();
        if(playerRect==null)
            return false;
        if (myRect.intersects(playerRect)) {
            System.out.println ("Star Collision with player!");
            soundManager.playSound("bubble", false);
            return true;
        }
        else
            return false;
    }


    @Override
    public Rectangle2D.Double getBoundingRectangle() {
        offsetX-=32;
        System.out.println("Star bounding X:"+(x+offsetX)+" y:"+(y+offsetY));
        System.out.println("Offset X: "+offsetX+"  Offset Y: "+offsetY);

        return new Rectangle2D.Double (x+offsetX, y+offsetY, 32, 32);
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