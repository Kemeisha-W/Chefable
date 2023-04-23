import java.awt.*;

public class Tile {
    Point position;
    private enum State{
        FOUNDATION,
        USE,
        STAR,
    }
    private enum Display {
        IMAGE,
        ANIMATION
    }

    private State tState;
    private final Display tDisplay;
    private Image tImage = null;

    /** Set Image Tile position*/
    public Tile(Image i, int x, int y,String key){
        tDisplay = Display.IMAGE;
        switch (key) {
            case "FOUNDATION" -> tState = State.FOUNDATION;
            case "USE" -> tState = State.USE;
            case "STAR" -> tState = State.STAR;
        }
        position = new Point(x,y);
        tImage = i;
    }

    /** Set Animation Tile position*/
    public Tile(int x, int y,String state){
        tDisplay = Display.ANIMATION;
        switch (state) {
            case "FOUNDATION" -> tState = State.FOUNDATION;
            case "USE" -> tState = State.USE;
            case "STAR" -> tState = State.STAR;
        }
        position = new Point(x,y);
    }

    public String getState(){
        return tState.toString();
    }

    public String getDisplay(){
        return tDisplay.toString();
    }

    public Image getImage(){
        return tImage;
    }
}
