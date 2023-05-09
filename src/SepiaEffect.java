import java.awt.*;
import java.awt.image.BufferedImage;


public class SepiaEffect implements ImageEffect {

	private static final int XSIZE = 32;		// width of the image
	private static final int YSIZE = 32;		// height of the image

	private int x;
	private int y;

	private final BufferedImage spriteImage;		// image for sprite effect
	private boolean active;		// to activate or deactivate effect

	int time, timeChange;				// to control when the image is grayed
	boolean originalImage, effectImage;
	private BufferedImage copyImage;		// copy of image



	public SepiaEffect( BufferedImage image) {

		time = 0;				// range is 0 to 10
		timeChange = 1;				// set to 1
		spriteImage = image;
		originalImage = true;
		effectImage = false;
		active = false;		// behaviour is inactive by default

		copyImage = ImageManager.copyImage(spriteImage);
		copyToSepia();
	}


	public void setX(int x) { this.x = x; }
	public void setY(int y) {this.y = y; }


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

	private void copyToSepia() {
		int imWidth = copyImage.getWidth();
		int imHeight = copyImage.getHeight();

		int [] pixels = new int[imWidth * imHeight];
		copyImage.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i=0; i<pixels.length; i++) {
			pixels[i] = toEffect(pixels[i]);
		}

		copyImage.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void activate() {
		active = true;
	}

	@Override
	public void deActivate() {
		active = false;
	}

	public void draw (Graphics2D g2) {
		g2.drawImage(copyImage, x, y, XSIZE, YSIZE, null);
	}


}