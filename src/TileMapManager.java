import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.ImageIcon;

/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class TileMapManager {

    private ArrayList<Image> tiles;
    private HashMap<String,Image> foodImgs;
    private LinkedList<String> foods = new LinkedList<>();
    private GamePanel window;


    public TileMapManager(GamePanel window) {
        this.window = window;
        loadTileImages();
        loadIngredients();
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

        TileMap newMap = new TileMap(window, mapWidth, mapHeight);
         System.out.println("New Tile map");
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
                    case '+' -> newMap.setFood(x,y);
                    case '#' -> newMap.setHeart(x,y);
                    case '&' -> newMap.setBasket(x,y);
                    case '*' -> newMap.setStar(x,y);
                }
            }
        }
        return newMap;
    }




    public HashMap<String,Image> getFoods(){
        return foodImgs;
    }

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
            while (s.hasNextLine()){
                foods.add(s.nextLine());
            }
            s.close();
        }catch (IOException e){
            e.printStackTrace();
            return;
        }

        File file;
        String filename;
        foodImgs = new HashMap<>();
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
            foodImgs.put(temp, image);
        }
    }

    private LinkedList<String> loadThemeBased(String themeStr){

        LinkedList<String> themeList = new LinkedList<String>();
        try {
            Scanner s = new Scanner(new File("Assets/Food/"+themeStr.trim()+".txt"));
            while (s.hasNextLine()){
                themeList.add(s.nextLine());
            }
            s.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        return themeList;
    }

}
