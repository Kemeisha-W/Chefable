import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerAnimation extends AnimationManager{

    /**
     The PlayerAnimation class creates an animation of a player moving, dying, jumping,etc.
     */

    private static final int pWidth=200;
    private static final int pHeight=200;
    protected boolean left = false;

    public PlayerAnimation() {
        loadWalkingRight();
//        loadWalkingLeft();
        loadDeath();
        loadIdle();
        loadJump();
        loadFall();
        loadLand();
        loadUse();
        soundManager = SoundManager.getInstance();
    }


    public int getWidth(){return pWidth;}
    public int getHeight(){return pHeight;}


    public void update(String sound,String key) {
//        currentAnimation = animations.get(key);
        if (!currentAnimation.isStillActive()) {
            stopSound(sound);
            return;
        }
        currentAnimation.update();

    }


    public void draw(Graphics2D g2,String key,int x, int y) {
        animation = animations.get(key);
        if (!animation.isStillActive()) {
            return;
        }
        if(left){

            BufferedImage flipped = ImageManager.toBufferedImage(animation.getImage(),200);
            flipped = ImageManager.hFlipImage(flipped);
            g2.drawImage(flipped, x, y, pWidth, pHeight, null);
            return;
        }
        g2.drawImage(animation.getImage(), x, y, pWidth, pHeight, null);
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
    }
    
    private void loadUse(){
        for(int num=0;num<13;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/player/Character3F_1_use_"+num+".png");
            i = i.getScaledInstance(pWidth,pHeight,Image.SCALE_DEFAULT);
            animImages.put("use"+num,i);
        }
        animation = new Animation(false);	// play continuously

        for(int num=0;num<13;num++){
            animation.addFrame(animImages.get("use"+num), 200);
        }
        animations.put("use",animation);
    }

}
