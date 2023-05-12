import java.awt.*;

import java.util.ArrayList;

public class Chest implements Sprite{
    private final int width=xSize+32;
    private final int height=ySize+32;
    private Animation closedAni;
    private Animation openAni;
    private Animation currentAni;
    private int x;
    private int y;
    public int offsetX;
    public int offsetY;
    private final ArrayList<Image> openImages= new ArrayList<>();
    private final ArrayList<Image> closedImages= new ArrayList<>();
    private boolean open = false;
    private static Chest single_instance = null;
    private int time;
    protected int timeChange;
    protected boolean alreadyExecuted = false;


    private Chest(){
        loadClosedChest();
        loadOpenChest();
        currentAni = closedAni;
        time = 0;
        timeChange = 5;
    }

    static Chest getInstance() {
        if (Chest.single_instance == null)
            Chest.single_instance = new Chest( );

        return Chest.single_instance;
    }

    private void loadClosedChest() {
        for (int num = 0; num < 5; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/Animated Chests/black_closed"+num+".png");
            i = i.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            closedImages.add(i);
        }

        closedAni = new Animation(true);    // play continuously

        for (int num = 0; num < 5; num++) {
            closedAni.addFrame(closedImages.get(num), 150);
        }
    }

    private void loadOpenChest() {
        for (int num = 0; num < 5; num++) {
            Image i = ImageManager.loadBufferedImage("Assets/Animated Chests/black_open"+num+".png");
            i = i.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            openImages.add(i);
        }

        openAni = new Animation(false);    // play once

        for (int num = 0; num < 5; num++) {
            openAni.addFrame(openImages.get(num), 150);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if(time>350&&!currentAni.isStillActive()){
            offsetY =offsetY-32;
            g2.drawImage(openImages.get(4), x+offsetX, y+offsetY, width,height, null);
            return;
        }
        if (!currentAni.isStillActive()) {
            return;
        }
        offsetY =offsetY-32;
        g2.drawImage(currentAni.getImage(), x+offsetX, y+offsetY, width,height, null);
    }


    @Override
    public void update() {
        if(time == 350){
            currentAni.stop();
            currentAni = openAni;
            currentAni.start();
            open = true;
        }
        if(currentAni.isStillActive())
            currentAni.update();
        time = time + timeChange;
    }

    @Override
    public int getX() {return x;}

    @Override
    public void setX(int x) {this.x = x;}

    @Override
    public int getY() {return y;}

    @Override
    public void setY(int y) {this.y = y;}

    @Override
    public Animation getAnimation() {
        return currentAni;
    }

    public boolean isOpen() {
        return open;
    }
}
