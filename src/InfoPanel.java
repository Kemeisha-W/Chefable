import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class InfoPanel extends JPanel {
    private int width;
    private int height;
    private BufferedImage image;

    public InfoPanel(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height =400;
        setSize(width,height);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }



}
