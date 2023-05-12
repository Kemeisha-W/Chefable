import java.awt.*;
import java.util.HashMap;

public abstract class AnimationManager {

    public Animation animation;
    public boolean loop =false;
    protected String currentSound = "";
    protected Animation currentAnimation = null;
    protected HashMap<String,Animation> animations = new HashMap<>();
    protected HashMap<String,Image> animImages= new HashMap<>();
    protected SoundManager soundManager;		// reference to SoundManager to play clip

    protected boolean isActive(){
        return currentAnimation.isStillActive();
    }

    protected abstract int getSize();
    protected void playSound(String title) {
        soundManager.playSound(title, loop);
    }
    protected void stopSound(String title) {
        soundManager.stopSound(title);
    }

    protected void start(String key,String sound) {
        if(currentAnimation != null){
            if(currentAnimation == animations.get(key) )
                return;
            currentAnimation.stop();
            stopSound(currentSound);
        }

        playSound(sound);
        animation = animations.get(key);
        animation.start();
        currentSound = sound;
        currentAnimation =  animations.get(key);
    }

    protected abstract void update(String sound,String key);

    protected abstract void draw(Graphics2D g2,String key,int x, int y);

}
