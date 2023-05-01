import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.awt.image.BufferedImage;


public class GrayEffect implements ImageEffect {

	private static final int XSIZE = 32;		// width of the image
	private static final int YSIZE = 32;		// height of the image


	private JPanel window;				// JFrame on which image will be drawn
	private Dimension dimension;
	private int x;
	private int y;

	private final BufferedImage spriteImage;		// image for sprite effect
	private BufferedImage copy;			// copy of image

	Graphics2D g2;

	int time, timeChange;				// to control when the image is grayed
	boolean originalImage, effectImage;


	public GrayEffect(JPanel window, BufferedImage image) {
		this.window = window;
		Graphics g = window.getGraphics ();
		g2 = (Graphics2D) g;

		dimension = window.getSize();

		time = 0;				// range is 0 to 10
		timeChange = 1;				// set to 1
		spriteImage = image;
		originalImage = true;
		effectImage = false;

		copy = copyImage(spriteImage);		//  copy original image

	}


	public void setX(int x) { this.x = x; }
	public void setY(int y) {this.y = y; }



	@Override
	public int toEffect(int pixel) {
		int alpha, red, green, blue, gray;
		int newPixel;

		alpha = (pixel >> 24) & 255;
		red = (pixel >> 16) & 255;
		green = (pixel >> 8) & 255;
		blue = pixel & 255;

		gray = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue); // Calculate the value for gray

		// Set red, green, and blue channels to gray

		red = green = blue = gray;

		newPixel = blue | (green << 8) | (red << 16) | (alpha << 24);
		return newPixel;
	}

	public void draw (Graphics2D g2) {

		copy = copyImage(spriteImage);		//  copy original image

		if (originalImage) {			// draw copy (already in colour) and return
			g2.drawImage(copy, x, y, XSIZE, YSIZE, null);
			return;
		}
							// change to gray and then draw
		int imWidth = copy.getWidth();
		int imHeight = copy.getHeight();

		int [] pixels = new int[imWidth * imHeight];
		copy.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

//		int alpha, red, green, blue, gray;

		for (int i=0; i<pixels.length; i++) {
//			if (effectImage){
				pixels[i] = toEffect(pixels[i]);
//			}
		}

		copy.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		g2.drawImage(copy, x, y, XSIZE, YSIZE, null);

	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, XSIZE, YSIZE);
	}


	public void update() {				// modify time and change the effect if required

		time = time + timeChange;

		if (time < 20) {
			originalImage = true;
			effectImage = false;
		}
		else
		if (time < 40) {
			originalImage = false;
			effectImage = true;
		}
		else {
			time = 0;
		}
	}


}