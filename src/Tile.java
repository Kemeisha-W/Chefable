import java.awt.*;
import java.util.Objects;

public class Tile {

    Point position;
    public boolean isUsed = false;
    private enum State{
        FOUNDATION,
        USE,
        STAR,
        CHEST,
        HEART,
        FIRE
    }
    private enum Display {
        IMAGE,
        ANIMATION
    }

    private enum Type{
        NEVER,
        CORRECT,
        INCORRECT
    }

    private State tState;
    private Type useType;
    private final Display tDisplay;
    private Image tImage = null;
    protected boolean used = false;

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
            case "CHEST" -> tState = State.CHEST;
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

    public void setUseTile(int value){
        switch (value){
            case 0-> useType = Type.CORRECT;
            case 1-> useType = Type.INCORRECT;
            case 2-> useType = Type.NEVER;
        }
    }

    public String getUseType(){
        return useType.toString();
    }

}
