import java.awt.*;

public class ChefAnimation extends AnimationManager{

    /**
     The Chef Animation class creates an animation of the chef
     */

    private final int size =200;


    public ChefAnimation() {
        loadChefWalking();
        loadIdle();
        loadWait();
        loadTalk();
        soundManager = SoundManager.getInstance();
    }


    @Override
    protected int getSize() {
        return size;
    }


    public void update(String sound, String key ) {
        currentAnimation = animations.get(key);

        if (!currentAnimation.isStillActive()) {
            stopSound(sound);
            return;
        }

        currentAnimation.update();
    }


    public void draw(Graphics2D g2,String key,int x, int y) {
        currentAnimation = animations.get(key);
        if (!currentAnimation.isStillActive()) {
            return;
        }
        g2.drawImage(currentAnimation.getImage(), x, y, size, size, null);
    }


    private void loadIdle(){
        for(int num=0;num<8;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/chef/Character3M_3_idle_"+num+".png");
            i =ImageManager.hFlipImage(ImageManager.toBufferedImage(i,200));
            animImages.put("idle"+num,i);
        }

        animation = new Animation(true);	// play continuously

        for(int num=0;num<8;num++){
            animation.addFrame(animImages.get("idle"+num), 100);
        }
        animations.put("idle",animation);
    }

    private void loadChefWalking(){
        for(int num=0;num<8;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/chef/Character3M_3_walk_"+num+".png");
            i =ImageManager.hFlipImage(ImageManager.toBufferedImage(i,200));
            animImages.put("walk"+num,i);
        }

        animation = new Animation(true);	// play continuously

        for(int num=0;num<8;num++){
            animation.addFrame(animImages.get("walk"+num), 150);
        }
        animations.put("walk",animation);
    }

    private void loadTalk(){
        for(int num=0;num<7;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/chef/Character3M_3_talk_"+num+".png");
            i =ImageManager.hFlipImage(ImageManager.toBufferedImage(i,200));
            animImages.put("talk"+num,i);
        }

        animation = new Animation(false);	// play once

        for(int num=0;num<7;num++){
            animation.addFrame(animImages.get("talk"+num), 150);
        }
        animations.put("talk",animation);
    }
    private void loadWait(){
        for(int num=0;num<6;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/chef/Character3M_3_wait_"+num+".png");
            i =ImageManager.hFlipImage(ImageManager.toBufferedImage(i,100));
            animImages.put("wait"+num,i);
        }

        animation = new Animation(true);	// play continuously

        for(int num=0;num<6;num++){
            animation.addFrame(animImages.get("wait"+num), 150);
        }
        animations.put("wait",animation);
    }

    public void update() {
        currentAnimation.update();
    }
}
