import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class GrayEffect implements ImageEffect {

	private static final int XSIZE = 32;		// width of the image
	private static final int YSIZE = 32;		// height of the image

	private int x;
	private int y;

	private final BufferedImage spriteImage;		// image for sprite effect
	private BufferedImage copyImage;		// copy of image
	private boolean active;		// to activate or deactivate effect

	int time, timeChange;				// to control when the image is grayed
	boolean originalImage, effectImage;


	public GrayEffect( BufferedImage image) {

		time = 0;				// range is 0 to 10
		timeChange = 1;				// set to 1
		spriteImage = image;
		originalImage = true;
		effectImage = false;
		active = false;		// behaviour is inactive by default
		copyImage = ImageManager.copyImage(spriteImage);
		copyToGray();

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

	private void copyToGray() {
		if(copyImage != null){
			int imWidth = copyImage.getWidth();
			int imHeight = copyImage.getHeight();

			int [] pixels = new int[imWidth * imHeight];
			copyImage.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

			for (int i=0; i<pixels.length; i++) {
				pixels[i] = toEffect(pixels[i]);
			}

			copyImage.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
		}

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