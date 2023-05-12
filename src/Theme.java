import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Theme {
    private final LinkedList<String> neverList = new LinkedList<>();
    private LinkedList<String> themeList = new LinkedList<>();

    public String currentTheme="";
    private HashMap<String, Image> foodImgs;
    private LinkedList<String> foodStr;
    public Theme(HashMap<String, Image> foodImgs, LinkedList<String> foodStr) {
        this.foodStr = foodStr;
        this.foodImgs = foodImgs;
        try {
            Scanner s = new Scanner(new File("Assets/Food/never.txt"));
            while (s.hasNextLine()) {
                neverList.add(s.nextLine());
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void loadTheme(String themeStr){
        try {
            Scanner s = new Scanner(new File("Assets/Food/"+themeStr.trim()+".txt"));
            while (s.hasNextLine()){
                themeList.add(s.nextLine());
            }
            s.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        currentTheme = themeStr;
    }

    public Image getThemeImage(){
        Random rand = new Random();
        String themeImgStr = themeList.get(rand.nextInt(themeList.size()));
        return foodImgs.get(themeImgStr);
    }

    public Image getNeverImage(){
        Random rand = new Random();
        String neverStr = neverList.get(rand.nextInt(neverList.size()));
        return foodImgs.get(neverStr);
    }

    public Image getOtherImage(){
        Random rand = new Random();
        String other = foodStr.get(rand.nextInt(foodStr.size()));
        while(neverList.contains(other)||themeList.contains(other)){
            other = foodStr.get(rand.nextInt(foodStr.size()));
        }
        return foodImgs.get(other);
    }

}
