import java.awt.*;

public class GameAnimation extends AnimationManager{

    /**
     The Game Animation class creates an animation of a game animations
     */

    private int size=30;

    private int dx;		// increment to move along x-axis
    private int dy;		// increment to move along y-axis


    public GameAnimation() {
        loadChefWalking();
        loadFireworks();
        loadGameOverAni();
        loadStartGameAni();
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
            animImages.put("walk"+num,i);
        }

        animation = new Animation(false);	// play once continuously

        for(int num=1;num<9;num++){
            animation.addFrame(animImages.get("walk"+num), 150);
        }
        animations.put("chef_walk",animation);
    }

    private void loadGameOverAni(){
        for(int num=0;num<60;num++){
            Image i = ImageManager.loadBufferedImage("Assets/Free pack 6/5/1_"+num+".png");
            animImages.put("game_over"+num,i);
        }

        animation = new Animation(true);	// play continuously

        for(int num=0;num<60;num++){
            animation.addFrame(animImages.get("game_over"+num), 150);
        }
        animations.put("game_over",animation);
    }

    private void loadStartGameAni(){
        for(int num=0;num<60;num++){
            Image i = ImageManager.loadBufferedImage("Assets/Free pack 6/1/1_"+num+".png");
            animImages.put("start"+num,i);
        }

        animation = new Animation(true);	// play continuously

        for(int num=0;num<60;num++){
            animation.addFrame(animImages.get("start"+num), 150);
        }
        animations.put("start",animation);
    }

    private void loadHeart() {
        for (int num = 11; num < 21; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/Gold/Gold_" + num + ".png");
            animImages.put("heart"+num,i);
        }
        animation = new Animation(true);    // play continuously

        for (int num = 11; num < 21; num++) {
            animation.addFrame(animImages.get("heart"+num), 100);
        }
        animations.put("heart", animation);

    }

}
