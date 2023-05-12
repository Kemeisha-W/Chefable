import java.awt.*;

public class GameAnimation extends AnimationManager{

    /**
     The Game Animation class creates an animation of a game animations
     */

    protected int size=30;

    private int dx;		// increment to move along x-axis
    private int dy;		// increment to move along y-axis


    public GameAnimation() {
        loadChefWalking();
        loadFireworks();
        loadHeart();
        soundManager = SoundManager.getInstance();
    }

    public void setDx(int dx) {
        this.dx = dx;
    }
    public void setDy(int dy) {
        this.dy = dy;
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
//
//        x = x + dx;
//        y = y + dy;
//
//        if (x > 800)
//            x = 100;
    }


    public void draw(Graphics2D g2,String key,int x, int y) {
        currentAnimation = animations.get(key);
        if (!currentAnimation.isStillActive()) {
            return;
        }
        g2.drawImage(currentAnimation.getImage(), x, y, size, size, null);
    }


    private void loadFireworks(){
        for(int num=1;num<11;num++){
            Image i = ImageManager.loadBufferedImage("Assets/blue fireworks/Explosion_"+num+".png");
            i = i.getScaledInstance(32,32,Image.SCALE_DEFAULT);
            animImages.put("fireworks"+num,i);
        }

        animation = new Animation(false);	// play once continuously

        for(int num=1;num<11;num++){
            animation.addFrame(animImages.get("fireworks"+num), 150);
        }
        animations.put("fireworks",animation);
    }

    private void loadChefWalking(){
        for(int num=1;num<9;num++){
            Image i = ImageManager.loadBufferedImage("Assets/characters/chef/walk_left/walk_left("+num+").png");
            i = i.getScaledInstance(32,32,Image.SCALE_DEFAULT);
            animImages.put("walk"+num,i);
        }

        animation = new Animation(false);	// play once continuously

        for(int num=1;num<9;num++){
            animation.addFrame(animImages.get("walk"+num), 150);
        }
        animations.put("chef_walk",animation);
    }

    private void loadHeart() {
        for (int num = 11; num < 21; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/Gold/Gold_" + num + ".png");
            i = i.getScaledInstance(32,32,Image.SCALE_DEFAULT);
            animImages.put("heart"+num,i);
        }
        animation = new Animation(true);    // play continuously

        for (int num = 11; num < 21; num++) {
            animation.addFrame(animImages.get("heart"+num), 100);
        }
        animations.put("heart", animation);

    }

}
