import java.awt.*;

public class Tile {
    Point position;
    private enum State{
        FOUNDATION,
        USE,
    }
    private enum Display {
        IMAGE,
        ANIMATION
    }

    private State tState;
    private Display tDisplay;
    private Image tImage;
    public Tile(Image i, int x, int y,String key){
        /** Set Image Tile position*/
        tDisplay = Display.IMAGE;
        switch (key){
            case "foundation":
                tState = State.FOUNDATION;
                break;
            case "use":
                tState = State.USE;
                break;
        }
        position = new Point(x,y);
        tImage = i;
    }

    public Tile(int x, int y){
        /** Set Animation Tile position*/
        tDisplay = Display.ANIMATION;
        tState = State.FOUNDATION;
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
