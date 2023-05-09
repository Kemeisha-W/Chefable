import java.awt.*;
import java.awt.image.BufferedImage;


public class DisintegrateEffect implements ImageEffect {

	private static final int xSize = 45;		// width of the image
	private static final int ySize = 45;		// height of the image
	protected int count=0;
	private int x;
	private int y;

	private final BufferedImage spriteImage;		// image for sprite effect
	private BufferedImage copy;			// copy of image
	private boolean active;		// to activate or deactivate effect

	int time, timeChange;				// to control when the image is grayed
	boolean originalImage, effectImage;


	public DisintegrateEffect( BufferedImage image) {

		time = 0;				// range is 0 to 10
		timeChange = 5;
		spriteImage = image;
		originalImage = true;
		effectImage = false;
		active = false;		// behaviour is inactive by default

		copy = copyImage(spriteImage);		//  copy original image

	}


	public void setX(int x) { this.x = x; }
	public void setY(int y) {this.y = y; }

	public void eraseImageParts(BufferedImage im, int interval) {

		int imWidth = im.getWidth();
		int imHeight = im.getHeight();

		int [] pixels = new int[imWidth * imHeight];
		im.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i = 0; i < pixels.length; i += interval) {
			pixels[i] = 0;    // make transparent (or black if no alpha)
		}

		im.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
	}

	@Override
	public int toEffect(int pixel) {
		return pixel;
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

		copy = copyImage(spriteImage);		//  copy original image

		if (time == 10)
			eraseImageParts(copy, 1);
		else
		if (time == 20)
			eraseImageParts(copy, 2);
		else
		if (time == 30)
			eraseImageParts(copy, 5);
		else
		if (time == 40)
			eraseImageParts(copy, 7);
		else
		if (time == 50)
			eraseImageParts(copy, 2);
		else
		if (time == 60)
			eraseImageParts(copy, 1);
		else
		if (time == 70)
			copy = ImageManager.copyImage(spriteImage);
		count++;
		g2.drawImage(copy, x-13, y-13, xSize, ySize, null);

	}

	public void update() {				// modify time and change the effect if required
		time = time + timeChange;

		if (time > 70)
			time = 0;
	}

}