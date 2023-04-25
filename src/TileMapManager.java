import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;

/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class TileMapManager {

    private ArrayList<Image> tiles;
    private HashMap<String,Image> ingredients;
    private ArrayList<String> foods = new ArrayList<String>();
    private GamePanel window;
    private GameWindow gWindow;


    private GraphicsConfiguration gc;

    // host sprites used for cloning
//    private Sprite playerSprite;
//    private Sprite musicSprite;
//    private Sprite coinSprite;
//    private Sprite goalSprite;
//    private Sprite grubSprite;
//    private Sprite flySprite;
//

    public TileMapManager(GamePanel window) {
        this.window = window;
        loadTileImages();
        loadCreatureSprites();
        loadIngredients();
//        loadPowerUpSprites();
    }


     public TileMap loadMap(String filename) throws IOException {

        ArrayList<String> lines = new ArrayList<>();
        int mapWidth = 0;
        int mapHeight;

        // read every line in the text file into the list

        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }
            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                mapWidth = Math.max(mapWidth, line.length());
            }
        }

        // parse the lines to create a TileMap
        mapHeight = lines.size();

        TileMap newMap = new TileMap(window, mapWidth, mapHeight);
        for (int y=0; y<mapHeight; y++) {
            String line = lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);
                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, tiles.get(tile),"FOUNDATION");
                }
                switch (ch) {
                    case '^' -> newMap.setPlayer(x,y);
                    case '~' -> newMap.setFire(x,y);
                    case '+' -> {//TODO add ingredient
                         }
                    case '#' -> {//TODO add heart
                    }
                    case '&' -> {//TODO add basket something to open
                    }
                    case '*' -> newMap.setStar(x,y);
                }
            }
        }
        return newMap;
    }



//    private void addSprite(TileMap map, Sprite hostSprite, int tileX, int tileY)
//    {
//        if (hostSprite != null) {
//            // clone the sprite from the "host"
//            Sprite sprite = (Sprite)hostSprite.clone();
//
//            // center the sprite
//            sprite.setX(
//                TileMapRenderer.tilesToPixels(tileX) +
//                (TileMapRenderer.tilesToPixels(1) -
//                sprite.getWidth()) / 2);
//
//            // bottom-justify the sprite
//            sprite.setY(
//                TileMapRenderer.tilesToPixels(tileY + 1) -
//                sprite.getHeight());
//
//            // add it to the map
//            map.addSprite(sprite);
//        }
//    }



    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------
    private static boolean isNumeric(Character str) {
        try {
            Double.parseDouble(String.valueOf(str));
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void loadTileImages() {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ folder
        File file;

        tiles = new ArrayList<>();
        char ch = 'A';
        String filename;
        while(true) {
            filename= "Assets/cartoon game tileset/PNG/Platformer/Ground_" + ch + ".png";
            file = new File(filename);
            if (!file.exists()) {
                System.out.println("Image file could not be opened: " + filename);
                break;
            }

            Image tileImage = new ImageIcon(filename).getImage();
            tileImage = tileImage.getScaledInstance(32, 32,Image.SCALE_DEFAULT);
            tiles.add(tileImage);
            ch++;
        }

    }


    public void loadIngredients(){
        try {
            Scanner s = new Scanner(new File("Assets/Food/food.txt"));
            while (s.hasNext()){
                foods.add(s.next());
            }
            s.close();
        }catch (IOException e){
            e.printStackTrace();
        }


        File file;
        String filename;
        ingredients = new HashMap<>();
        for (String food : foods) {
            filename = "Assets/Food/" + food;
            file = new File(filename);
            if (!file.exists()) {
                System.out.println("Image file could not be opened: " + filename);
                break;
            }

            Image image = new ImageIcon(filename).getImage();
            image = image.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
            String temp = food.replace(".png","");
            ingredients.put(temp, image);
        }
    }

    public void loadCreatureSprites() {
//        num =1;
//        while(true) {
//            filename="Assets/cartoon game tileset/PNG/Building/Door_0"+num+".png";
//            file = new File(filename);
//            if (!file.exists()) {
//                System.out.println("Image file could not be opened: " + filename);
//                break;
//            } else
//                System.out.println("Image file opened: " + filename);
//
//            Image tileImage = new ImageIcon(filename).getImage();
//            int width = Math.floorDiv(window.getWidth(), 20);
//            int height = Math.floorDiv(window.getHeight(), 15);
//            System.out.println("Tile Width: " + width +" \tTile Height: " + height);
//            tileImage = tileImage.getScaledInstance(width, height,Image.SCALE_DEFAULT);
//
//            tiles.add(tileImage);
//            num++;
//        }
        Image[][] images = new Image[4][];

        // load left-facing images
//        images[0] = new Image[] {
//            loadImage("player1.png"),
//            loadImage("player2.png"),
//            loadImage("player3.png"),
//            loadImage("fly1.png"),
//            loadImage("fly2.png"),
//            loadImage("fly3.png"),
//            loadImage("grub1.png"),
//            loadImage("grub2.png"),
//        };

//        images[1] = new Image[images[0].length];
//        images[2] = new Image[images[0].length];
//        images[3] = new Image[images[0].length];
//        for (int i=0; i<images[0].length; i++) {
//            // right-facing images
//            images[1][i] = getMirrorImage(images[0][i]);
//            // left-facing "dead" images
//            images[2][i] = getFlippedImage(images[0][i]);
//            // right-facing "dead" images
//            images[3][i] = getFlippedImage(images[1][i]);
//        }

//        // create creature animations
//        Animation[] playerAnim = new Animation[4];
//        Animation[] flyAnim = new Animation[4];
//        Animation[] grubAnim = new Animation[4];
//        for (int i=0; i<4; i++) {
//            playerAnim[i] = createPlayerAnim(
//                images[i][0], images[i][1], images[i][2]);
//            flyAnim[i] = createFlyAnim(
//                images[i][3], images[i][4], images[i][5]);
//            grubAnim[i] = createGrubAnim(
//                images[i][6], images[i][7]);
//        }

        // create creature sprites
//        playerSprite = new Player(playerAnim[0], playerAnim[1],
//            playerAnim[2], playerAnim[3]);
//        flySprite = new Fly(flyAnim[0], flyAnim[1],
//            flyAnim[2], flyAnim[3]);
//        grubSprite = new Grub(grubAnim[0], grubAnim[1],
//            grubAnim[2], grubAnim[3]);
//        System.out.println("loadCreatureSprites successfully executed.");

    }


}
