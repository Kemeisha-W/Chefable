import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
    The FireAnimation class creates an animation of a fire.
*/
public class FireAnimation {

	Animation animation;

	private int x;		// x position of animation
	private int y;		// y position of animation

	private int width = 100;
	private int height = 80;


	private static FireAnimation single_instance = null;


	private FireAnimation() {
		// load images for fire animation
		Image animImage1 = ImageManager.loadBufferedImage("Assets/fire/Ground_Q.png");
		animImage1 = animImage1.getScaledInstance(width,height,Image.SCALE_DEFAULT);
		Image animImage2 = ImageManager.loadBufferedImage("Assets/fire/Ground_R.png");
		animImage2 = animImage2.getScaledInstance(width,height,Image.SCALE_DEFAULT);
		Image animImage3 = ImageManager.loadBufferedImage("Assets/fire/Ground_S.png");
		animImage3 = animImage3.getScaledInstance(width,height,Image.SCALE_DEFAULT);
		Image animImage4 = ImageManager.loadBufferedImage("Assets/fire/Ground_T.png");
		animImage4 = animImage4.getScaledInstance(width,height,Image.SCALE_DEFAULT);
		Image animImage5 = ImageManager.loadBufferedImage("Assets/fire/Ground_U.png");
		animImage5 = animImage5.getScaledInstance(width,height,Image.SCALE_DEFAULT);
		Image animImage6 = ImageManager.loadBufferedImage("Assets/fire/Ground_~.png");
		animImage6 = animImage6.getScaledInstance(width,height,Image.SCALE_DEFAULT);


		// create animation object and insert frames

		animation = new Animation(true);	// play once continuously

		animation.addFrame(animImage1, 200);
		animation.addFrame(animImage2, 200);
		animation.addFrame(animImage3, 200);
		animation.addFrame(animImage4, 200);
		animation.addFrame(animImage5, 200);
		animation.addFrame(animImage6, 200);

	}
	public static synchronized FireAnimation getInstance()
	{
		if (single_instance == null)
			single_instance = new FireAnimation();

		return single_instance;
	}



	public void start() {
		animation.start();
	}

	
	public void update() {
		if (!animation.isStillActive()) {
			return;
		}

		animation.update();
	}


	public void draw(Graphics2D g2) {

		if (!animation.isStillActive()) {
			return;
		}
		g2.drawImage(animation.getImage(), x, y, width, height, null);
	}

	public void setX(int x) {
		this.x = x;
	}


	public void setY(int y) {
		this.y = y;
	}


}
