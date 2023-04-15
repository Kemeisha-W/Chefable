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
        dy = -3;	// increment to move along y-axis

        loadChefWalking();
        loadFire();
        loadFireworks();

    }



    public void start(String sound) {
        x = 100;
        y = 300;
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
        g2.drawImage(animation.getImage(), x, y, 150, 125, null);
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
        animImages.put("fireworks1",ImageManager.loadImage("Assets/blue fireworks/Explosion_1.png"));
        animImages.put("fireworks2",ImageManager.loadImage("Assets/blue fireworks/Explosion_2.png"));
        animImages.put("fireworks3",ImageManager.loadImage("Assets/blue fireworks/Explosion_3.png"));
        animImages.put("fireworks4",ImageManager.loadImage("Assets/blue fireworks/Explosion_4.png"));
        animImages.put("fireworks5",ImageManager.loadImage("Assets/blue fireworks/Explosion_5.png"));
        animImages.put("fireworks6",ImageManager.loadImage("Assets/blue fireworks/Explosion_6.png"));
        animImages.put("fireworks7",ImageManager.loadImage("Assets/blue fireworks/Explosion_7.png"));
        animImages.put("fireworks8",ImageManager.loadImage("Assets/blue fireworks/Explosion_8.png"));
        animImages.put("fireworks9",ImageManager.loadImage("Assets/blue fireworks/Explosion_9.png"));
        animImages.put("fireworks10",ImageManager.loadImage("Assets/blue fireworks/Explosion_10.png"));

        animation = new Animation(false);	// play once only

        animation.addFrame(animImages.get("fireworks1"), 200);
        animation.addFrame(animImages.get("fireworks2"), 200);
        animation.addFrame(animImages.get("fireworks3"), 200);
        animation.addFrame(animImages.get("fireworks4"), 200);
        animation.addFrame(animImages.get("fireworks5"), 200);
        animation.addFrame(animImages.get("fireworks6"), 200);
        animation.addFrame(animImages.get("fireworks7"), 200);
        animation.addFrame(animImages.get("fireworks8"), 200);
        animation.addFrame(animImages.get("fireworks9"), 200);
        animation.addFrame(animImages.get("fireworks10"), 200);

        animations.put("fireworks",animation);

        soundManager = SoundManager.getInstance();
    }

    private void loadChefWalking(){
        animImages.put("walk1",ImageManager.loadImage("Assets/characters/chef/walk_left/walk_left(1).png"));
        animImages.put("walk2",ImageManager.loadImage("Assets/characters/chef/walk_left/walk_left(2).png"));
        animImages.put("walk3",ImageManager.loadImage("Assets/characters/chef/walk_left/walk_left(3).png"));
        animImages.put("walk4",ImageManager.loadImage("Assets/characters/chef/walk_left/walk_left(4).png"));
        animImages.put("walk5",ImageManager.loadImage("Assets/characters/chef/walk_left/walk_left(5).png"));
        animImages.put("walk6",ImageManager.loadImage("Assets/characters/chef/walk_left/walk_left(6).png"));
        animImages.put("walk7",ImageManager.loadImage("Assets/characters/chef/walk_left/walk_left(7).png"));
        animImages.put("walk8",ImageManager.loadImage("Assets/characters/chef/walk_left/walk_left(8).png"));

        animation = new Animation(false);	// play once only

        animation.addFrame(animImages.get("walk1"), 200);
        animation.addFrame(animImages.get("walk2"), 200);
        animation.addFrame(animImages.get("walk3"), 200);
        animation.addFrame(animImages.get("walk4"), 200);
        animation.addFrame(animImages.get("walk5"), 200);
        animation.addFrame(animImages.get("walk6"), 200);
        animation.addFrame(animImages.get("walk7"), 200);
        animation.addFrame(animImages.get("walk8"), 200);
        animations.put("chef_walk",animation);

        soundManager = SoundManager.getInstance();
    }

}
