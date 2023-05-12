import java.awt.*;
import java.io.*;
import java.util.*;

/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class TileMapManager {

    private ArrayList<Image> tiles;
    private HashMap<String,Image> foodImgs;
    private LinkedList<String> foodStr = new LinkedList<>();
    private GamePanel window;
    private Theme theme;
    private int count =0;

    public TileMapManager(GamePanel window) {
        this.window = window;
        loadTileImages();
        loadFoods();
        theme = new Theme(foodImgs, foodStr);
    }


     public TileMap loadMap(String filename) throws IOException {
         System.out.println("Load map");

        ArrayList<String> lines = new ArrayList<>();
        int mapWidth = 0;
        int mapHeight;

        // read every line in the text file into the list

        BufferedReader reader = new BufferedReader(new FileReader(filename));

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

        TileMap newMap = new TileMap(window, mapWidth, mapHeight,theme);


        for (int y=0; y<mapHeight; y++) {
            String line = lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);
                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                System.out.print(ch+" ");
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, tiles.get(tile),"FOUNDATION");
                }
                switch (ch) {
                    case '^' -> newMap.setPlayer(x,y);
                    case '~' -> newMap.setFire(x,y);
                    case '+' -> newMap.setFood(x,y);
                    case '#' -> {
                        newMap.setHeart(x,y, count);
                        count++;
                    }
                    case '&' -> newMap.setChest(x,y);
                    case '*' -> newMap.setStar(x,y);
                    case '|'-> newMap.setChef(x,y);
                }
            }
        }
         System.out.println();
        return newMap;
    }


    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------

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

            Image tileImage = ImageManager.loadBufferedImage(filename);
            tileImage = tileImage.getScaledInstance(32, 32,Image.SCALE_DEFAULT);
            tiles.add(tileImage);
            ch++;
        }

    }


    public void loadFoods(){
        try {
            Scanner s = new Scanner(new File("Assets/Food/food.txt"));
            while (s.hasNextLine()){
                foodStr.add(s.nextLine());
            }
            s.close();
        }catch (IOException e){
            e.printStackTrace();
            return;
        }

        File file;
        String filename;
        foodImgs = new HashMap<>();
        for (String food : foodStr) {
            filename = "Assets/Food/" + food;
            file = new File(filename);
            if (!file.exists()) {
                System.out.println("Image file could not be opened: " + filename);
                break;
            }

            Image image = ImageManager.loadBufferedImage(filename);
            image = image.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
            String temp = food.replace(".png","");
            foodImgs.put(temp, image);
        }
    }


}
