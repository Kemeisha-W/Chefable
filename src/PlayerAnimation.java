import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerAnimation {

    /**
     The PlayerAnimation class creates an animation of a player moving, dying, jumping,etc.
     */

        public Animation animation;
        private static final int pWidth=300;
        private static final int pHeight=300;


        public HashMap<String,Animation> animations = new HashMap<>();
        private HashMap<String,Image> animImages= new HashMap<>();

        private int dx;		// increment to move along x-axis
        private int dy;		// increment to move along y-axis

        private SoundManager soundManager;		// reference to SoundManager to play clip

        public PlayerAnimation() {

            loadWalkingRight();
            loadWalkingLeft();
            loadDeath();
            loadIdle();
            loadJump();
            loadFall();
            loadLand();

        }


        public int getWidth(){return pWidth;}
        public int getHeight(){return pHeight;}
        public void start(String sound,String key) {
            animation = animations.get(key);
            animation.start();
            playSound(sound);
        }


        public void update(String sound,String key) {
            animation = animations.get(key);
            if (!animation.isStillActive()) {
                stopSound(sound);
                return;
            }
            animation.update();

        }


        public void draw(Graphics2D g2,String key,int x, int y) {
            animation = animations.get(key);
            if (!animation.isStillActive()) {
                System.out.println("active animation NOPE");
                return;
            }

            g2.drawImage(animation.getImage(), x, y, pWidth, pHeight, null);
        }

        public void playSound(String title) {
            soundManager.playSound(title, true);
        }

        public void stopSound(String title) {
            soundManager.stopSound(title);
        }

    private void loadWalkingRight(){
         for(int num=0;num<8;num++){
             Image i = ImageManager.loadBufferedImage("Assets/characters/player/Character3F_1_walk_"+num+".png");
             i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
             animImages.put("walk"+num,i);
         }

        animation = new Animation(true);	// play once continuously

        for(int num=0;num<8;num++){
            animation.addFrame(animImages.get("walk"+num), 150);
        }
        animations.put("walk_right",animation);
        soundManager = SoundManager.getInstance();
    }

    private void loadWalkingLeft(){
        for(int num=1;num<9;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/player/walk_left/walk_left ("+num+").png");
            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("walk"+num,i);
        }
        animation = new Animation(true);	// play continuously

        for(int num=1;num<9;num++){
            animation.addFrame(animImages.get("walk"+num), 150);
        }
        animations.put("walk_left",animation);
        soundManager = SoundManager.getInstance();
    }
    private void loadDeath(){
        for(int num=0;num<8;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/player/Character3F_1_death_"+num+".png");
            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("death"+num,i);
        }
        animation = new Animation(false);	// play once only

        for(int num=0;num<8;num++){
            animation.addFrame(animImages.get("death"+num), 50);
        }

        animations.put("death",animation);

        soundManager = SoundManager.getInstance();
    }

    private void loadIdle(){
        for(int num=0;num<8;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/player/Character3F_1_idle_"+num+".png");
            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("idle"+num,i);
        }
        animation = new Animation(true);	// play continuously

        for(int num=0;num<8;num++){
            animation.addFrame(animImages.get("idle"+num), 200);
        }
        animations.put("idle",animation);

        soundManager = SoundManager.getInstance();
    }

    private void loadJump(){
        for(int num=0;num<2;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/player/Character3F_1_jump_"+num+".png");
            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("jump"+num,i);
        }
        animation = new Animation(true);	// play continuously

        for(int num=0;num<2;num++){
            animation.addFrame(animImages.get("jump"+num), 200);
        }

        animations.put("jumping",animation);

        soundManager = SoundManager.getInstance();
    }

    private void loadFall(){
        for(int num=0;num<2;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/player/Character3F_1_fall_"+num+".png");
            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("fall"+num,i);
        }
        animation = new Animation(true);	// play continuously

        for(int num=0;num<2;num++){
            animation.addFrame(animImages.get("fall"+num), 200);
        }

        animations.put("falling",animation);

        soundManager = SoundManager.getInstance();
    }

    private void loadLand(){
        for(int num=0;num<2;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/player/Character3F_1_land_"+num+".png");
            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("land"+num,i);
        }
        animation = new Animation(false);	// play once only

        for(int num=0;num<2;num++){
            animation.addFrame(animImages.get("land"+num), 300);
        }
        animations.put("land",animation);

        soundManager = SoundManager.getInstance();
    }

}
