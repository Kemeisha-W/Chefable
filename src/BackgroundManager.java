/* BackgroundManager manages many backgrounds (wraparound images 
   used for the game's background). 

   Backgrounds 'further back' move slower than ones nearer the
   foreground of the game, creating a parallax distance effect.

   When a sprite is instructed to move left or right, the sprite
   doesn't actually move, instead the backgrounds move in the 
   opposite direction (right or left).

*/

import java.awt.Graphics2D;
import javax.swing.*;

public class BackgroundManager {

	private String bgImages[] = {
			"Assets/game-backgrounds/PNG/game_background_1/layers/sky.png",
			"Assets/game-backgrounds/PNG/game_background_1/layers/clouds_1.png",
			"Assets/game-backgrounds/PNG/game_background_1/layers/clouds_2.png",
			"Assets/game-backgrounds/PNG/game_background_1/layers/clouds_3.png",
			"Assets/game-backgrounds/PNG/game_background_1/layers/clouds_4.png",
			"Assets/game-backgrounds/PNG/game_background_1/layers/rocks_1.png",
			"Assets/game-backgrounds/PNG/game_background_1/layers/rocks_2.png",
			"Assets/art-streets/PNG/City4/Bright/road.png"
	};

  	private int moveAmount[] = {1,2,2,2,3,3,3,4};
						// pixel amounts to move each background left or right
     						// a move amount of 0 makes a background stationary

  	private Background[] backgrounds;
  	private int numBackgrounds;

  	private JPanel window;			// JFrame on which backgrounds are drawn

  	public BackgroundManager(JPanel window, int moveSize) {
    		this.window = window;

    		numBackgrounds = bgImages.length;
    		backgrounds = new Background[numBackgrounds];

    		for (int i = 0; i < numBackgrounds; i++) {
       			backgrounds[i] = new Background(window, bgImages[i], moveAmount[i]);
    		}
  	} 


  	public void moveRight() { 
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveRight();
  	}


  	public void moveLeft() {
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveLeft();
  	}


  	// The draw method draws the backgrounds on the screen. The
  	// backgrounds are drawn from the back to the front.

  	public void draw (Graphics2D g2) { 
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].draw(g2);
  	}

}

