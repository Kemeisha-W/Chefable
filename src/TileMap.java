import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
    The TileMap class contains the data for a tile-based
    map, including Sprites. Each tile is a reference to an
    Image. Images are used multiple times in the tile map.
    map.
*/

public class TileMap {
    private int average;
    private Path gamePoints;
    private ArrayList<String> result;
    private File fs;

    private int size = 32;
    private static final int TILE_SIZE = 32;
    private String currentTheme = "";
    private int origX;
    private int origY;
    private boolean toogle=true;
    private boolean win;
    private Random random = new Random();
    private Tile[][] tiles;
    private final int screenWidth, screenHeight;
    private int mapWidth, mapHeight;
    private int offsetY;
    public int offsetX;

    private Player player;
    private Chef chef;

    //Add animations
    private final FireAnimation fire;
    private final PlayerAnimation pAni;
    private ChefAnimation cAni;
    private final GameAnimation gameAni;

    BackgroundManager bgManager;
    private GamePanel window;
    private final Star star;
    private final Chest chest;
    private final Theme theme;
    private int level;
    protected int correct, incorrect, never;


    /**
        Creates a new TileMap with the specified width and
        height (in number of tiles) of the map.
    */
    public TileMap(GamePanel window, int width, int height, Theme theme) {
        correct=0;
        incorrect=0;
        never = 0;

        gamePoints = Paths.get("gamePoints.txt");
        fs = gamePoints.toFile();
        try {
            if(!Files.exists(gamePoints)) {
                Files.createFile(gamePoints);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }


        this.window = window;

        screenWidth = window.getWidth();
        screenHeight = window.getHeight();


        mapWidth = width;
        mapHeight = height;
        // get the y offset to draw all sprites and tiles
        offsetY = screenHeight - tilesToPixels(mapHeight);

        bgManager = new BackgroundManager (window, 12);
        tiles = new Tile[mapWidth][mapHeight];

        level = window.getLevel();
        //Set Theme
        this.theme = theme;
        setTheme();

        //Add instance of fires
        fire = FireAnimation.getInstance();
        fire.start();

        //Add Player
        player=new Player(window, this, bgManager);
        this.pAni = player.getPlayerAnimation();
        gameAni = new GameAnimation();
        gameAni.start("heart","");



        //Add the star
        star = new Star(player);
        Animation starAni = star.getAnimation();
        starAni.start();

        //Add the chest
        chest = Chest.getInstance();
        Animation chestAni = chest.getAnimation();
        chestAni.start();
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

//    public void setTileSize(int size){TILE_SIZE = size;}

    /**
        Sets the tile at the specified location.
    */
    public void setTile(int x, int y, Image tile, String key) {
        Tile t = new Tile(tile, x, y,"Image", key);
        tiles[x][y] = t;
    }

    /**
     * Sets the Player at the specified location
     */
    public void setPlayer(int x, int y) {
        int offsetY = screenHeight - tilesToPixels(mapHeight)-TILE_SIZE-85;
        player.setPlayOffsetY(offsetY);
        player.setX(tilesToPixels(x));
        player.setY(tilesToPixels(y)+offsetY);
        origX = x;
        origY = y;
    }

    /**
     * Sets the Chef at the specified location
     */
    public void setChef(int x, int y) {
        //Add Chef
        int offsetY = screenHeight - tilesToPixels(mapHeight)-TILE_SIZE-85;
        chef = new Chef();
        cAni = chef.getchefAnimation();
        chef.setX(tilesToPixels(x));
        chef.setY(tilesToPixels(y)+offsetY);
    }

    /**
     * Sets the FireAnimation at the specified locations
     */
    public void setFire(int x, int y) {
        Tile fireT = new Tile(null,x, y,"Animation","FIRE");
        tiles[x][y] = fireT;
    }

    /**
     * Sets the Star at the specified locations
     */
    public void setStar(int x, int y ) {
        Tile star = new Tile(null,x, y,"Animation","STAR");
        tiles[x][y] = star;
//        this.star.setX(tilesToPixels(x));
//        this.star.setY(tilesToPixels(y));
    }

    /**
     * Sets the Food at the specified locations
     */
    public void setFood(int x, int y) {
        Tile food = new Tile(null,x, y,"Image","USE");
        tiles[x][y] = food ;
    }

    /**
     * Sets the Heart at the specified locations
     */
    public void setHeart(int x, int y, int count) {
        Tile heart = new Tile(null,x, y,"Animation","HEART");
        heart.count = count;
        tiles[x][y] = heart;
    }

    /**
      Sets the Chest at the specified locations
     */
    public void setChest(int x, int y) {
        Tile chest = new Tile(null,x, y,"Animation","CHEST");
        tiles[x][y] = chest;
    }


    public void setTheme(){
        Random rand = new Random();
        ArrayList<String> themes = new ArrayList<>(Arrays.asList("Never","Vegetable","Fruit","Lunch","Bakery","Snacks","Meat"));
        int tValue;
        if(level ==1){
            tValue = rand.nextInt(2) + 1;
        }else{
            tValue = rand.nextInt(7 -3) + 3;
        }
        currentTheme = themes.get(tValue);
        theme.loadTheme(currentTheme);
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

    public int getOffsetX(){
        return offsetX;
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
    public synchronized void draw(Graphics2D g2){
        System.out.println("\n____________Draw___________________");
        int mapWidthPixels = tilesToPixels(mapWidth);

        // draw the background first
        bgManager.draw (g2);

        // get the scrolling position of the map based on player's position
        int playerX = player.getX();
        offsetX = screenWidth/2-Math.round((float)playerX)-TILE_SIZE;

        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX,  screenWidth-mapWidthPixels);


        Font font = new Font("Serif", Font.PLAIN, 60);
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(5,5,screenWidth-25,70,20,20);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(5,5,screenWidth-25,70,20,20);
        g2.setColor(Color.BLACK);
        if(window.getLevel() != 3){
            //Draw Current Theme
            g2.drawString(currentTheme,(screenWidth/2)-50,tilesToPixels(2));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Draw Hearts
            Image heartImg = player.getHeartImage();
            int numHearts = player.getHeartNum();
            for (int i = 0;i<numHearts;i++){
                g2.drawImage(heartImg, tilesToPixels(i)+10, tilesToPixels(1),32,32, null);
            }
        }else{

            if(!chest.isOpen()){
                g2.drawString("Did you get the Job? ...",20,tilesToPixels(2));
            }
            if(chest.isOpen()&&win){
                g2.drawString("Congratulations! You got the Job. Work starts next Monday.",20,tilesToPixels(2));
                gameAni.size = 400;
                gameAni.draw(g2,"fireworks",screenWidth/2,screenHeight/3);
                gameAni.update("fireworks","fireworks");
                if(!gameAni.isActive()){
                    Shape shape = new RoundRectangle2D.Float( 5,80,400,80,20,20);
                    g2.setColor(Color.WHITE);
                    g2.fill(shape);
                    g2.setColor(Color.BLACK);
                    g2.setFont(font);
                    g2.drawString("Points "+average,5,150);
                    player.gameOver = true;
                }

            }else if(chest.isOpen()&&!win){
                Shape shape = new RoundRectangle2D.Float( 5,80,400,80,20,20);
                g2.drawString("You did NOT get the JOB",20,tilesToPixels(2));
                font = new Font("Serif", Font.PLAIN, 60);
                g2.setColor(Color.DARK_GRAY);
                g2.fill(shape);
                g2.setColor(Color.RED);
                g2.setFont(font);
                g2.drawString("Points "+average,5,150);
                player.gameOver = true;
            }
        }


        if(random.nextInt()%2==0){toogle = !toogle;}

        // draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth)+1;
        for (int y=0; y<mapHeight; y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                if(x<mapWidth){
                    if(tiles[x][y] != null){
                        Tile tile = tiles[x][y];
                        switch (tile.getDisplay()) {
                            case "IMAGE" -> {
                                if(Objects.equals(tile.getState(),"USE")){
                                    if(tile.getImage() ==null){
                                        Random rand = new Random();
                                        int temp = rand.nextInt(3);
                                        tile.setUseTile(temp);
                                        if(Objects.equals(tile.getUseType(), "NEVER")){
                                            tile.setImage(theme.getNeverImage());
                                            never++;
                                        }
                                        else if(Objects.equals(tile.getUseType(), "CORRECT")){
                                            tile.setImage(theme.getThemeImage());
                                            correct++;
                                        }
                                        else{
                                            tile.setImage(theme.getOtherImage());
                                            incorrect++;
                                        }
                                    }
                                    if(!tile.used){
                                        size =tile.update(size,toogle);
                                        g2.drawImage(tile.getImage(),
                                                tilesToPixels(x) + offsetX,
                                                tilesToPixels(y) + offsetY, size, size,
                                                null);

                                    }else if(!tile.alreadyExecuted){
                                        tile.disintegrateTile();
                                        tile.disEffect.setX(tilesToPixels(x) + offsetX);
                                        tile.disEffect.setY(tilesToPixels(y) + offsetY);
                                        tile.disEffect.draw(g2);
                                        tile.disEffect.update();
                                        tile.alreadyExecuted = true;
                                    }else {

                                        if(tile.disEffect.count == 70){
                                            tile.disEffect.deActivate();
                                            if (tile.getUseType().equals("NEVER")) {
                                                tile.sepiaTile();
                                                tile.sepiaEffect.setX(tilesToPixels(x) + offsetX);
                                                tile.sepiaEffect.setY(tilesToPixels(y) + offsetY);
                                                tile.sepiaEffect.draw(g2);
                                            } else {
                                                tile.grayTile();
                                                tile.grayEffect.setX(tilesToPixels(x) + offsetX);
                                                tile.grayEffect.setY(tilesToPixels(y) + offsetY);
                                                tile.grayEffect.draw(g2);
                                            }
                                        }else{
                                            tile.disEffect.activate();
                                            tile.disEffect.setX(tilesToPixels(x) + offsetX);
                                            tile.disEffect.setY(tilesToPixels(y) + offsetY);
                                            tile.disEffect.draw(g2);
                                            tile.disEffect.update();
                                        }

                                    }
                                }else{
                                    g2.drawImage(tile.getImage(),
                                            tilesToPixels(x) + offsetX,
                                            tilesToPixels(y) + offsetY,32,32,
                                            null);
                                }
                            }

                            case "ANIMATION" -> {
                                if (Objects.equals(tile.getState(), "FIRE")) {
                                    fire.setX(tilesToPixels(x) + offsetX);
                                    fire.setY(tilesToPixels(y) + offsetY);
                                    fire.draw(g2);
                                    fire.update();
                                } else if (Objects.equals(tile.getState(), "STAR")) {
                                    star.offsetY = offsetY;
                                    star.offsetX = offsetX;
                                    star.setX(tilesToPixels(x));
                                    star.setY(tilesToPixels(y));
                                    star.draw(g2);
                                    star.update();
                                } else if (Objects.equals(tile.getState(), "HEART")) {
                                    if(!tile.used){
                                        gameAni.draw(g2,"heart",tilesToPixels(x)+offsetX,tilesToPixels(y)+offsetY);
                                        gameAni.update("","heart");
                                    }
                                } else if (Objects.equals(tile.getState(), "CHEST")) {
                                    chest.offsetY = offsetY;
                                    chest.offsetX = offsetX;
                                    chest.setX(tilesToPixels(x));
                                    chest.setY(tilesToPixels(y));
                                    chest.draw(g2);
                                    chest.update();
                                }
                            }
                        }
                    }
                }
            }
        }

        //draw chef
        if(chef!= null) {
            int y = chef.getY();
            int x = chef.getX();
            String key;
            switch (chef.getState()) {
                case "WALK" -> {
                    key = "walk";
                    cAni.draw(g2, key, x, y);
                }
                case "IDLE" -> {
                    key = "idle";
                    cAni.draw(g2, key, x, y);
                }
                case "WAIT" -> {
                    key = "wait";
                    cAni.draw(g2, key, x, y);
                }
                case "TALK" -> {
                    key = "talk";
                    cAni.draw(g2, key, x, y);
//                    cAni.draw(g2, key, x, y);
                }
            }
            cAni.update();
        }
        //draw player animation
        int y = player.getY();
        playerX = Math.round((float)playerX)+offsetX;
        String key ="";
        switch (player.getState()) {
            case "WALK" -> {
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

                if(!this.pAni.isActive()&&!player.alreadyExecuted){
                    offsetX = screenWidth/2-Math.round((float)playerX)-TILE_SIZE;
                    offsetX = Math.min(offsetX, 0);
                    offsetX = Math.max(offsetX,  screenWidth-mapWidthPixels);

                    offsetY = screenHeight - tilesToPixels(mapHeight);
                    int playerOffY = screenHeight - tilesToPixels(mapHeight)-TILE_SIZE-85;
                    player.setPlayOffsetY(playerOffY);
                    player.setX(tilesToPixels(origX));
                    player.setY(tilesToPixels(origY)+playerOffY);

                    player.alreadyExecuted = true;
                    player.setState("IDLE");
                    playerX = Math.round((float)tilesToPixels(origX));
                }
                this.pAni.draw(g2, key, playerX, y);
            }
            case "IDLE" -> {
                key = "idle";
                this.pAni.draw(g2, key, playerX, y);
            }
            case "USE" -> {
                key = "use";
                this.pAni.draw(g2, key, playerX, y);
                if(!this.pAni.isActive()) {
                    player.setState("IDLE");

                }
            }
        }
        System.out.println("\n___X="+playerX+"___y="+y);
        this.pAni.update("",key);

    }

    public void moveLeft() {player.move(1);}

    public void moveRight() {player.move(2);}

    public void jump() {player.move(3);}

    public void use(){
        player.use();
    }

    public void idle(){
        player.setState("IDLE");
    }

    public void update() throws IOException {
        player.update();
        if(chef != null) {
            chef.roaming();
        }

        if (star.collidesWithPlayer()) {
            BufferedWriter w;
            int percentage=0;
            if(correct !=0){
                percentage = (player.points/(correct*7))*100;
            }
            else if( player.points == 0)
                percentage = 100;
            if(fs.length() !=0&& window.getLevel() == 1){
                w = Files.newBufferedWriter(gamePoints);
                w.write("");
                w.flush();
            }
            if(fs.length() ==0&&window.getLevel() == 1) {
                String line ="Level1 "+percentage;
                Files.write(gamePoints,line.getBytes(),StandardOpenOption.WRITE);
            }else if(window.getLevel()==2){
                String line ="\nLevel2 "+percentage;
                Files.write(gamePoints,line.getBytes(), StandardOpenOption.APPEND);
            }

            window.endLevel();
            setTheme();
            return;
        }
        star.update();

        if(player.gameOver){
//            Files.deleteIfExists(gamePoints);
            window.gameOver = true;
        }

        if(chest.isOpen()&&!chest.alreadyExecuted){
            result = (ArrayList<String>) Files.readAllLines(gamePoints);
            int num =0;
            int[] level = new int[2];
            for(String line : result){
                line = line.replace("Level"+(num+1),"");
                level[num]= Integer.parseInt(line.trim());
                num++;
            }
             average = ((level[0]+level[1])/2);
            if (average>49){
                gameAni.start("fireworks","fireworks");
                win = true;
            }else{
                win = false;
            }
            chest.alreadyExecuted = true;
        }
    }

}
