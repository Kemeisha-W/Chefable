import java.awt.*;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Objects;
import javax.swing.JFrame;

/**
    The TileMap class contains the data for a tile-based
    map, including Sprites. Each tile is a reference to an
    Image. Images are used multiple times in the tile map.
    map.
*/

public class TileMap {

    private static int TILE_SIZE = 32;

    private Tile[][] tiles;
    private final int screenWidth, screenHeight;
    private int mapWidth, mapHeight;
    private int offsetY;
    public int offsetX;

    private LinkedList sprites;
    private Player player;

    //Add animations
    private FireAnimation fire;
    private PlayerAnimation pAni;
    private AnimationManager aniManager;
    private Animation starAni;

    BackgroundManager bgManager;
    private GamePanel window;
    private Star star;

    /**
        Creates a new TileMap with the specified width and
        height (in number of tiles) of the map.
    */
    public TileMap(GamePanel window, int width, int height) {
        this.window = window;

        screenWidth = window.getWidth();
        screenHeight = window.getHeight();


        mapWidth = width;
        mapHeight = height;
        // get the y offset to draw all sprites and tiles
        offsetY = screenHeight - tilesToPixels(mapHeight);

        bgManager = new BackgroundManager (window, 12);
        tiles = new Tile[mapWidth][mapHeight];

        player = new Player (window, this, bgManager);
        sprites = new LinkedList<>();
        fire = FireAnimation.getInstance();
        if(window.getLevel()==1)
            fire.start();

        this.pAni = player.getPlayerAnimation();
        aniManager = new AnimationManager();
//        aniManager.start("star");
        star = Star.getInstance(window,player);
        starAni = star.getAnimation();
        starAni.start();
    }



    /**
        Gets the width of this TileMap (number of pixels across).
    */
    public int getWidthPixels() {
        return tilesToPixels(mapWidth);
    }


    /**
        Gets the width of this TileMap (number of tiles across).
    */
    public int getWidth() {
        return mapWidth;
    }


    /**
        Gets the height of this TileMap (number of tiles down).
    */
    public int getHeight() {
        return mapHeight;
    }


    /**
     * Gets the tile at the specified location. Returns null if
     * no tile is at the location or if the location is out of
     * bounds.
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= mapWidth ||
            y < 0 || y >= mapHeight)
        {
            return null;
        }
        else {
            return  tiles[x][y];
        }
    }

    public void setTileSize(int size){TILE_SIZE = size;}

    /**
        Sets the tile at the specified location.
    */
    public void setTile(int x, int y, Image tile, String key) {
        Tile t = new Tile(tile, x, y, key);
        tiles[x][y] = t;
    }

    /**
     * Sets the Player at the specified location
     */
    public void setPlayer(int x, int y) {
        int offsetY = screenHeight - tilesToPixels(mapHeight)-TILE_SIZE-100;
        player.setPlayOffsetY(offsetY);
        System.out.println("offsetY = " + offsetY);
        player.setX(tilesToPixels(x));
        player.setY(tilesToPixels(y)+offsetY);
    }

    /**
     * Sets the FireAnimation at the specified locations
     */
    public void setFire(int x, int y) {
        Tile fireT = new Tile(x, y,"FOUNDATION");
        tiles[x][y] = fireT;
    }

    /**
     * Sets the Star at the specified locations
     */
    public void setStar(int x, int y ) {
        Tile star = new Tile(x, y,"STAR");
        tiles[x][y] = star;
        System.out.println("Star set at " + x + " " + y);
    }

    /**
        Gets an Iterator of all the Sprites in this map,
        excluding the player Sprite.
    */

    public Iterator getSprites() {
        return sprites.iterator();
    }

    /**
        Class method to convert a pixel position to a tile position.
    */

    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }


    /**
        Class method to convert a pixel position to a tile position.
    */

    public static int pixelsToTiles(int pixels) {
        return (int)Math.floor((float)pixels / TILE_SIZE);
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    /**
        Class method to convert a tile position to a pixel position.
    */

    public static int tilesToPixels(int numTiles) {
        return numTiles * TILE_SIZE;
    }



    /**
        Draws the specified TileMap.
    */
    public synchronized void draw(Graphics2D g2)  {

        int mapWidthPixels = tilesToPixels(mapWidth);

        // get the scrolling position of the map based on player's position
        int playerX = player.getX();
        offsetX = screenWidth/2-Math.round((float)playerX)-TILE_SIZE;
        System.out.println("\n_______________________________");
        System.out.println("\n Player x: "+playerX+"\noffsetX: "+offsetX);
        System.out.println("Player tile: "+pixelsToTiles(playerX));

        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX,  screenWidth-mapWidthPixels);


        // draw the background first
        bgManager.draw (g2);

        // draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth)+1;
        System.out.println("First: " + firstTileX+"\t Last:"  + lastTileX);
        for (int y=0; y<mapHeight; y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                if(x<mapWidth){
                    if(tiles[x][y] != null){
                        Tile tile = tiles[x][y];
                        switch (tile.getDisplay()) {
                            case "IMAGE" ->
                                    g2.drawImage(tile.getImage(),
                                            tilesToPixels(x) + offsetX,
                                            tilesToPixels(y) + offsetY,
                                            null);
                            case "ANIMATION" -> {
                                if(Objects.equals(tile.getState(), "STAR")){
                                    System.out.println("star?: x"+x+" y"+y);
                                    star.setX(tilesToPixels(x)+offsetX);
                                    star.setY(tilesToPixels(y)+offsetY);
                                    star.draw(g2);
                                    star.update();
                                }else {
                                    fire.setX(tilesToPixels(x) + offsetX);
                                    fire.setY(tilesToPixels(y) + offsetY);
                                    fire.draw(g2);
                                    fire.update();
                                }
                            }
                        }

                    }
                }
            }
        }
        //draw player animation
        int y = player.getY();
        playerX = Math.round((float)playerX)+offsetX;
        String key ="";
        switch (player.getState()) {
            case "LEFT" -> {
                key = "walk_left";
                this.pAni.draw(g2, key, playerX, y);
            }
            case "RIGHT" -> {
                key = "walk_right";
                this.pAni.draw(g2, key, playerX, y);
            }
            case "JUMP" -> {
                key = "jumping";
                this.pAni.draw(g2, key, playerX, y);
            }
            case "FALL" -> {
                key = "falling";
                this.pAni.draw(g2, key, playerX, y);
            }
            case "LAND" -> {
                key = "land";
                this.pAni.draw(g2, key, playerX, y);
                this.pAni.draw(g2, key, playerX, y);
                player.setState("IDLE");
            }
            case "DIE" -> {
                key = "death";
                System.out.println("DIE: ");
                this.pAni.draw(g2, key, playerX, y);
                this.pAni.draw(g2, key, playerX, y);
            }
            case "IDLE" -> {
                key = "idle";
                this.pAni.draw(g2, key, playerX, y);
            }
        }
        this.pAni.update("",key);



        // draw sprites
//        Iterator i = map.getSprites();
//        while (i.hasNext()) {
//            Sprite sprite = (Sprite)i.next();
//            int x = Math.round(sprite.getX()) + offsetX;
//            int y = Math.round(sprite.getY()) + offsetY;
//            g.drawImage(sprite.getImage(), x, y, null);
//
//            // wake up the creature when it's on screen
//            if (sprite instanceof Creature &&
//                x >= 0 && x < screenWidth)
//            {
//                ((Creature)sprite).wakeUp();
//            }
//        }
        if(Objects.equals(player.getState(), "DIE")){
            restartLevel();
        }
    }

    public void moveLeft() {
        player.move(1);
    }


    public void moveRight() {
        player.move(2);
    }


    public void jump() {
        player.move(3);
    }

    public void idle(){
        player.setState("IDLE");
    }

    public void update() {
        player.update();

        if (star.collidesWithPlayer()) {
            window.endLevel();
            return;
        }

        star.update();

        if (star.collidesWithPlayer()) {
            window.endLevel();
        }

    }

    public void restartLevel(){
        if(player.getState().equals("DIE")){
            int l = window.getLevel();
            window.setLevel(l);
        }
    }

}
