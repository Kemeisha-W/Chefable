import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;

/**
 A component that displays all the game entities
 */

public class GamePanel extends JPanel
        implements Runnable {

    private SoundManager soundManager;

    private boolean isRunning;
    private boolean isPaused;

    private Thread gameThread;

    private BufferedImage image;
    private Image backgroundImage;


//    private ImageEffect imageEffect;		// sprite demonstrating an image effect

    private TileMapManager tileManager;
    private TileMap	tileMap;

    private boolean levelChange;
    private int level;

    private boolean gameOver;
    private int width;
    private int height;

    private Dimension dimension;

    public GamePanel () {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height =900;
        isRunning = false;
        isPaused = false;
        setSize(width,height);
        soundManager = SoundManager.getInstance();
        setBackground(Color.WHITE);

        image = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);

        level = 1;
        levelChange = false;
        gameStartMessage();
    }

    public int getWidth(){
            return width;
    }
    public int getHeight(){
        return height;
    }

    public void createGameEntities() {
//        imageEffect = new ImageEffect (this);
    }


    public void run () {
        try {
            isRunning = true;
            while (isRunning) {

                if (!isPaused && !gameOver)
                    gameUpdate();
                gameRender();
                Thread.sleep (50);
            }
        }
        catch(InterruptedException e) {}
    }


    public void gameUpdate() {

        tileMap.update();

        if (levelChange) {
            levelChange = false;
            tileManager = new TileMapManager (this);

            try {
                String filename = "maps/map" + level + ".txt";
                tileMap = tileManager.loadMap(filename) ;
                int w, h;
                w = tileMap.getWidth();
                h = tileMap.getHeight();
                System.out.println ("Changing level to Level " + level);
                System.out.println ("Width of tilemap " + w);
                System.out.println ("Height of tilemap " + h);
            }
            catch (Exception e) {		// no more maps: terminate game
                gameOver = true;
                System.out.println(e);
                System.out.println("Game Over");
                return;
            }

            createGameEntities();
            return;

        }
//        imageEffect.update();
    }


    public void gameRender() {
        int width = getWidth();
        int height = getHeight();

        // draw the game objects on the image

        Graphics2D imageContext = (Graphics2D) image.getGraphics();

        tileMap.draw (imageContext);

//        imageEffect.draw(imageContext);			// draw the image effect

        if (gameOver) {
            Color darken = new Color (0, 0, 0, 125);
            imageContext.setColor (darken);
            imageContext.fill (new Rectangle2D.Double (0, 0, width, height));
        }

        Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
        g2.drawImage(image, 0, 0, width, height, null);	// draw the image on the graphics context
        imageContext.dispose();
    }


//    public void startGame() {				// initialise and start the game thread
//
//        if (gameThread == null) {
//            soundManager.playSound ("background1", true);
//
//            gameOver = false;
//
//            tileManager = new TileMapManager (this);
//
//            try {
//                tileMap = tileManager.loadMap("maps/map1.txt");
//                int w, h;
//                w = tileMap.getWidth();
//                h = tileMap.getHeight();
//                System.out.println ("Width of tilemap " + w);
//                System.out.println ("Height of tilemap " + h);
//            }
//
//            catch (Exception e) {
//                System.out.println(e);
//                System.exit(0);
//            }
//
//            createGameEntities();
//
//            gameThread = new Thread(this);
//            gameThread.start();
//
//        }
//    }


    public void startNewGame() {				// initialise and start a new game thread
        if(isRunning){
           return;
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

            createGameEntities();

            gameThread = new Thread(this);
            gameThread.start();

        }
    }


	public int getLevel(){
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		levelChange = true;
	}

    public void pauseGame() {				// pause the game (don't update game entities)
        if (isRunning) {
            if (isPaused)
                isPaused = false;
            else
                isPaused = true;
        }
    }

    private void gameOverMessage(Graphics g) {

            Font font = new Font("SansSerif", Font.BOLD, 24);
            FontMetrics metrics = this.getFontMetrics(font);

            String msg = "Game Over. Thanks for playing!";

            int x = (width - metrics.stringWidth(msg)) / 2;
            int y = (height - metrics.getHeight()) / 2;

            g.setColor(Color.BLUE);
            g.setFont(font);
            g.drawString(msg, x, y);

	}

    public void gameStartMessage() {
        Graphics2D g = (Graphics2D) image.getGraphics();

        Font font = new Font("SansSerif", Font.BOLD, 40);
        FontMetrics metrics = this.getFontMetrics(font);

        String msg = "Chefable";

        int x = (width - metrics.stringWidth(msg)) / 2;
        int y = (height - metrics.getHeight()) / 2;

        g.setColor(Color.CYAN);
        g.setFont(font);
        g.drawString(msg, x, y);

    }

    public void endGame() {					// end the game thread
        isRunning = false;
        soundManager.stopSound ("background1");
        gameOverMessage(getGraphics());
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

    public void idle(){
        if (!gameOver)
            tileMap.idle();
    }


    public void endLevel() {
        level = level + 1;
        levelChange = true;
    }

}