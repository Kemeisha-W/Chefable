import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AnimationManager {

    /**
     The PlayerAnimation class creates an animation of a player moving, dying, jumping,etc.
     */

    Animation animation;

    private int x;		// x position of animation
    private int y;		// y position of animation

    private int width;
    private int height;
    private HashMap<String,Animation> animations = new HashMap<>();
    private HashMap<String,Image> animImages= new HashMap<>();

    private int dx;		// increment to move along x-axis
    private int dy;		// increment to move along y-axis

    private SoundManager soundManager;		// reference to SoundManager to play clip

    public AnimationManager() {

        dx = 10;	// increment to move along x-axis
        dy = 0;	// increment to move along y-axis

        loadChefWalking();
        loadFire();
        loadFireworks();
        loadHeart();
        loadGameOverAni();
        loadStartGameAni();
    }


    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setDx(int dx) {
        this.dx = dx;
    }
    public void setDy(int dy) {
        this.dy = dy;
    }

    public void start(String sound) {

        animation.start();
        playSound(sound);
    }


    public void update(String sound) {

        if (!animation.isStillActive()) {
            stopSound(sound);
            return;
        }

        animation.update();

        x = x + dx;
        y = y + dy;

        if (x > 800)
            x = 100;
    }


    public void draw(Graphics2D g2) {

        if (!animation.isStillActive()) {
            return;
        }
        g2.drawImage(animation.getImage(), x, y, 150, 150, null);
    }

    public void playSound(String title) {
        soundManager.playSound(title, true);
    }

    public void stopSound(String title) {
        soundManager.stopSound(title);
    }

    private void loadFire(){
        animImages.put("fire1",ImageManager.loadImage("Assets/fire/1/Explosion_1.png"));
        animImages.put("fire2",ImageManager.loadImage("Assets/fire/1/Explosion_2.png"));
        animImages.put("fire3",ImageManager.loadImage("Assets/fire/1/Explosion_3.png"));
        animImages.put("fire4",ImageManager.loadImage("Assets/fire/1/Explosion_4.png"));
        animImages.put("fire5",ImageManager.loadImage("Assets/fire/1/Explosion_5.png"));
        animImages.put("fire6",ImageManager.loadImage("Assets/fire/1/Explosion_6.png"));

        animation = new Animation(false);	// play once only

        animation.addFrame(animImages.get("fire1"), 200);
        animation.addFrame(animImages.get("fire2"), 200);
        animation.addFrame(animImages.get("fire3"), 200);
        animation.addFrame(animImages.get("fire4"), 200);
        animation.addFrame(animImages.get("fire5"), 200);
        animation.addFrame(animImages.get("fire6"), 200);
        animations.put("fire",animation);

        soundManager = SoundManager.getInstance();
    }

    private void loadFireworks(){
        for(int num=1;num<11;num++){
            Image i = ImageManager.loadBufferedImage("Assets/blue fireworks/Explosion_"+num+".png");
//            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("fireworks"+num,i);
        }

        animation = new Animation(false);	// play once continuously

        for(int num=1;num<11;num++){
            animation.addFrame(animImages.get("fireworks"+num), 150);
        }
        animations.put("fireworks",animation);
        soundManager = SoundManager.getInstance();

    }

    private void loadChefWalking(){
        for(int num=1;num<9;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/chef/walk_left/walk_left("+num+").png");
//            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("walk"+num,i);
        }

        animation = new Animation(false);	// play once continuously

        for(int num=0;num<9;num++){
            animation.addFrame(animImages.get("walk"+num), 150);
        }
        animations.put("chef_walk",animation);
        soundManager = SoundManager.getInstance();
    }

    private void loadGameOverAni(){
        for(int num=0;num<60;num++){
            Image i = ImageManager.loadBufferedImage("Assets/Free pack 6/5/1_"+num+".png");
//            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("game_over"+num,i);
        }

        animation = new Animation(true);	// play continuously

        for(int num=0;num<60;num++){
            animation.addFrame(animImages.get("game_over"+num), 150);
        }
        animations.put("game_over",animation);
        soundManager = SoundManager.getInstance();
    }

    private void loadStartGameAni(){
        for(int num=0;num<60;num++){
            Image i = ImageManager.loadBufferedImage("Assets/Free pack 6/1/1_"+num+".png");
//            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("start"+num,i);
        }

        animation = new Animation(true);	// play continuously

        for(int num=0;num<60;num++){
            animation.addFrame(animImages.get("start"+num), 150);
        }
        animations.put("start",animation);
        soundManager = SoundManager.getInstance();
    }

    private void loadHeart(){
        for(int num=11;num<21;num++){
            Image i = ImageManager.loadBufferedImage("Assets/Free-Game-Coins-Sprite/PNG/Gold/Gold_"+num+".png");
//            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("heart"+num,i);
        }

        animation = new Animation(false);	// play once continuously

        for(int num=11;num<21;num++){
            animation.addFrame(animImages.get("heart"+num), 150);
        }
        animations.put("heart",animation);
        soundManager = SoundManager.getInstance();
    }


}
