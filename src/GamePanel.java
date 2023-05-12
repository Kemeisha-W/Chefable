import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 A component that displays all the game entities
 */

public class GamePanel extends JPanel
        implements Runnable {

    private SoundManager soundManager;

    private boolean isRunning;
    protected boolean isPaused;

    private Thread gameThread;

    private BufferedImage image;
    private Image backgroundImage;


    private TileMapManager tileManager;
    private TileMap tileMap;

    private boolean levelChange;
    private int level;

    private boolean gameOver;
    private int width;
    private int height;

//    private Dimension dimension;

    public GamePanel (int bPanelHeight) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        System.out.println("bPanelHeight: " + bPanelHeight);
        height =(int) screenSize.getHeight()-bPanelHeight;
        isRunning = false;
        isPaused = false;
        setSize(width,height);
        soundManager = SoundManager.getInstance();
        setBackground(Color.WHITE);

        image = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);

        level = 1;
        levelChange = false;
    }

    public int getWidth(){
            return width;
    }
    public int getHeight(){
        return height;
    }

    public void run () {
        try {
            isRunning = true;
            while (isRunning) {
                if (!isPaused && !gameOver){
                    gameUpdate();
                    gameRender();
                }
                Thread.sleep (50);
            }
        }
        catch(InterruptedException | IOException ignored) {}
    }


    public void gameUpdate() throws IOException {

        tileMap.update();

        if (levelChange) {
            levelChange = false;
            try {
                String filename = "maps/map" + level + ".txt";
                tileMap = tileManager.loadMap(filename) ;
            }
            catch (Exception e) {        // no more maps: terminate game
                gameOver = true;
                System.out.println(e);
                System.out.println("Game Over");
            }

        }
    }


    public void gameRender() {
        int width = getWidth();
        int height = getHeight();

        // draw the game objects on the image

        Graphics2D imageContext = (Graphics2D) image.getGraphics();

        tileMap.draw (imageContext);

        if (gameOver) {
            Color darken = new Color (0, 0, 0, 125);
            imageContext.setColor (darken);
            imageContext.fill (new Rectangle2D.Double (0, 0, width, height));
        }

        Graphics2D g2 = (Graphics2D) getGraphics();    // get the graphics context for the panel
        g2.drawImage(image, 0, 0, width, height, null);    // draw the image on the graphics context
        imageContext.dispose();
    }



    public void startNewGame() {                // initialise and start a new game thread
        if(isRunning){
           restartGame();
        }
        if (gameThread == null) {
            soundManager.playSound ("background1", true);

            gameOver = false;
            level = 1;

            tileManager = new TileMapManager (this);

            try {
                tileMap = tileManager.loadMap("maps/map1.txt");

            }
            catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }

            gameThread = new Thread(this);
            gameThread.start();

        }
    }


    public int getLevel(){
        return level;
    }

    public void pauseGame() {                // pause the game (don't update game entities)
        if (isRunning) {
            isPaused = !isPaused;
        }
    }

    public void endGame() {                    // end the game thread
        isRunning = false;
        soundManager.stopSound ("background1");
        gameThread=null;

        Graphics2D g2=(Graphics2D) getGraphics();
        Font font = new Font("SansSerif", Font.BOLD, 40);
        FontMetrics metrics = this.getFontMetrics(font);

        String msg = "Game Over. Thanks for playing!";

        int x = (width - metrics.stringWidth(msg)) / 2;
        int y = (height - metrics.getHeight()) / 2;

        g2.setColor(Color.BLUE);
        g2.setFont(font);
        g2.drawString(msg, x, y);
    }

    public void restartGame(){
        isRunning = false;
        soundManager.stopSound ("background1");
        gameThread=null;
        levelChange = false;
    }


    public void moveLeft() {
        if (!gameOver)
            tileMap.moveLeft();
    }


    public void moveRight() {
        if (!gameOver)
            tileMap.moveRight();
    }


    public void jump() {
        if (!gameOver)
            tileMap.jump();
    }

    public void use(){
        if (!gameOver)
            tileMap.use();
    }

    public void idle(){
        if (!gameOver)
            tileMap.idle();
    }


    public void endLevel() {
        level = level + 1;
        levelChange = true;
    }

}