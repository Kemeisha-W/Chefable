import java.awt.*;
import javax.swing.*;

public class Background {
  	private Image bgImage;
  	private int bgImageWidth;      		// width of the background (>= panel Width)

	private int bgX;
	private int backgroundX;
	private int backgroundX2;
	private int bgDX;			// size of the background move (in pixels)


	public Background(JPanel window, String imageFile, int bgDX) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		this.bgImage = loadImage(imageFile);
		bgImageWidth = dimension.width;	// get width of the background

		dimension = window.getSize();
		this.bgImage = this.bgImage.getScaledInstance(bgImageWidth, dimension.height,Image.SCALE_DEFAULT);
		this.bgDX = bgDX;

  	}


  	public void moveRight() {

		if (bgX == 0) {
			backgroundX = 0;
			backgroundX2 = bgImageWidth;			
		}

		bgX = bgX - bgDX;

		backgroundX = backgroundX - bgDX;
		backgroundX2 = backgroundX2 - bgDX;

		if ((bgX + bgImageWidth) % bgImageWidth == 0) {
			backgroundX = 0;
			backgroundX2 = bgImageWidth;
		}

  	}


  	public void moveLeft() {
	
		if (bgX == 0) {
			backgroundX = bgImageWidth * -1;
			backgroundX2 = 0;			
		}

		bgX = bgX + bgDX;
				
		backgroundX = backgroundX + bgDX;	
		backgroundX2 = backgroundX2 + bgDX;

		if ((bgX + bgImageWidth) % bgImageWidth == 0) {
			backgroundX = bgImageWidth * -1;
			backgroundX2 = 0;
		}			
   	}
 

  	public void draw (Graphics2D g2) {
		g2.drawImage(bgImage, backgroundX, 0, null);
		g2.drawImage(bgImage, backgroundX2, 0, null);
  	}


  	public Image loadImage (String fileName) {
		return new ImageIcon(fileName).getImage();
  	}

}
