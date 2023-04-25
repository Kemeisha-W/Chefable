import java.awt.*;

public class Tile {
    Point position;
    private enum State{
        FOUNDATION,
        USE,
        STAR,
        BASKET,
        HEART,
        FIRE
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
            default -> {
                //TODO ADD ERROR message
                return;
            }
        }
        position = new Point(x,y);
        tImage = i;
    }

    /** Set Animation Tile position*/
    public Tile(int x, int y,String state){
        tDisplay = Display.ANIMATION;
        switch (state) {
            case "STAR" -> tState = State.STAR;
            case "HEART" -> tState = State.HEART;
            case "BASKET" -> tState = State.BASKET;
            case "FIRE" -> tState = State.FIRE;
            default -> {
                //TODO ADD ERROR message
                return;
            }
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
