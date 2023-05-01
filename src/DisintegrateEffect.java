import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class DisintegrateEffect implements ImageEffect {

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


	public DisintegrateEffect(JPanel window, BufferedImage image) {
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

	public void eraseImageParts(BufferedImage im, int interval) {

		int imWidth = im.getWidth();
		int imHeight = im.getHeight();

		int [] pixels = new int[imWidth * imHeight];
		im.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i = 0; i < pixels.length; i = i + interval) {
			pixels[i] = 0;    // make transparent (or black if no alpha)
		}

		im.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
	}

	@Override
	public int toEffect(int pixel) {
		int alpha, red, green, blue,newRed, newGreen, newBlue;
		int newPixel;

		alpha = (pixel >> 24) & 255;
		red = (pixel >> 16) & 255;
		green = (pixel >> 8) & 255;
		blue = pixel & 255;


		newRed = (int) (0.393*red+0.769*green+0.189+blue);
		newGreen = (int) (0.349*red+0.686*green+0.168+blue);
		newBlue = (int) (0.272*red+0.534*green+0.131*blue);

		newRed = Math.min(255,newRed);
		newGreen = Math.min(255,newGreen);
		newBlue = Math.min(255,newBlue);

		// Set red, green, and blue channels to sepia

		newPixel = newBlue | (newGreen << 8) | (newRed << 16) | (alpha << 24);
		return newPixel;
	}

	public void draw (Graphics2D g2) {

		copy = copyImage(spriteImage);		//  copy original image

//		if (originalImage) {			// draw copy (already in colour) and return
//			g2.drawImage(copy, x, y, XSIZE, YSIZE, null);
//			return;
//		}

		if (time == 10)
			eraseImageParts(copy, 11);
		else
		if (time == 20)
			eraseImageParts(copy, 7);
		else
		if (time == 30)
			eraseImageParts(copy, 5);
		else
		if (time == 40)
			eraseImageParts(copy, 3);
		else
		if (time == 50)
			eraseImageParts(copy, 2);
		else
		if (time == 60)
			eraseImageParts(copy, 1);
		else
		if (time == 70)
			copy = ImageManager.copyImage(spriteImage);

		g2.drawImage(copy, x, y, XSIZE, YSIZE, null);

	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, XSIZE, YSIZE);
	}


	public void update() {				// modify time and change the effect if required
		time = time + timeChange;

		if (time > 70)
			time = 0;
	}

}