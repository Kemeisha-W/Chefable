import java.awt.*;
import java.util.Objects;

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
    public Tile(Image i, int x, int y,String dKey, String state){
        if(Objects.equals(dKey, "Image")){
            tDisplay = Display.IMAGE;
            position = new Point(x,y);
            if(i != null){
                tImage = i;
            }
        }else {
            tDisplay = Display.ANIMATION;
            position = new Point(x,y);

        }
        switch (state) {
            case "FOUNDATION" -> tState = State.FOUNDATION;
            case "USE" -> tState = State.USE;
            case "STAR" -> tState = State.STAR;
            case "HEART" -> tState = State.HEART;
            case "BASKET" -> tState = State.BASKET;
            case "FIRE" -> tState = State.FIRE;
            default -> {
                //TODO ADD ERROR message
                return;
            }
        }
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
    public void setImage(Image img){
        tImage = img;
    }
}
