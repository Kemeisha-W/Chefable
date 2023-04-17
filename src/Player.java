import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.Image;
import java.awt.Point;
import java.util.Objects;

public class Player {			

   	private static final int DX = 8;	// amount of X pixels to move in one keystroke
   	private static final int DY = 32;	// amount of Y pixels to move in one keystroke
	private enum State{
		RIGHT,
		LEFT,
		JUMP,
		FALL,
		LAND,
		DIE,
		IDLE
	}
	private State pState;
   	private static final int TILE_SIZE = TileMap.getTileSize();
	private JFrame window;		// reference to the JFrame on which player is drawn
   	private TileMap tileMap;
   	private final BackgroundManager bgManager;

   	private int x;			// x-position of player's sprite
   	private int y;			// y-position of player's sprite

   	Graphics2D g2;
   private Dimension dimension;

   private Image playerImage;
   private int timeElapsed;
   private int startY;
   private PlayerAnimation playerAnimation;
   private Animation currentAnimation;
	private boolean goingUp;
   private boolean goingDown;

   private boolean jumping = false;
   private boolean inAir;
   private int initialVelocity;
   private int startAir;

   public Player (JFrame window, TileMap t, BackgroundManager b) {
	   this.window = window;
	   tileMap = t;			// tile map on which the player's sprite is displayed
	   bgManager = b;			// instance of BackgroundManager

	   goingUp = goingDown = false;
       inAir = false;

	   playerAnimation = new PlayerAnimation();
	   playerImage = playerAnimation.animations.get("idle").getImage();

	   this.pState = State.IDLE;
	   newAnimation("idle");
   }


   public Point isCollision(int newX, int newY) {
	   int offsetY = tileMap.getOffsetY();
	   int xTile = TileMap.pixelsToTiles(newX);
	   int yTile = TileMap.pixelsToTiles(newY - offsetY);
	   if (tileMap.getTile(xTile, yTile) != null) {
		   return new Point (xTile, yTile);
	   }
	   else {
//		   if(tileMap.isFireAt(newX,newY)){
//			   System.out.println("FIRE COLLIDES with Tile");
//			   pState = State.DIE;
//			   die();
//			   return new Point (xTile, yTile);
//		   }
		   return null;
	   }
   }


   public Point isTileBelow(int newX, int newY) {
	   int playerWidth = playerAnimation.getWidth();
	   int playerHeight =playerAnimation.getHeight();
	   int offsetY = tileMap.getOffsetY();
	   int xTile = TileMap.pixelsToTiles(newX);
	   int yTileFrom = TileMap.pixelsToTiles(y - offsetY);
	   int yTileTo = TileMap.pixelsToTiles(newY - offsetY + playerHeight);

	  for (int yTile=yTileFrom; yTile<=yTileTo; yTile++) {
//		if (tileMap.getTile(xTile, yTile) != null||tileMap.isFireAt(newX,newY)) {
			if (tileMap.getTile(xTile, yTile) != null) {
				System.out.println("Collides with tile going down");
			return new Point (xTile, yTile);
	  	}
		else {
			if (tileMap.getTile(xTile+1, yTile) != null) {
				int leftSide = (xTile + 1) * TILE_SIZE;
				if (newX + playerWidth > leftSide) {
					return new Point (xTile+1, yTile);
				}
			}
		}
	  }
	  return null;
   }

	//TODO
   public Point isTileAbove(int newX, int newY) {

	   int playerWidth = playerImage.getWidth(null);
	   int offsetY = tileMap.getOffsetY();
	   int xTile = TileMap.pixelsToTiles(newX);

	   int yTileFrom = TileMap.pixelsToTiles(y - offsetY);
	   int yTileTo = TileMap.pixelsToTiles(newY - offsetY);
	 
	   for (int yTile=yTileFrom; yTile>=yTileTo; yTile--) {
		 if (tileMap.getTile(xTile, yTile) != null) {
			return new Point (xTile, yTile);
		 }
		 else {
			 if (tileMap.getTile(xTile+1, yTile) != null) {
				 int leftSide = (xTile + 1) * TILE_SIZE;
				 if (newX + playerWidth > leftSide) {
					 return new Point (xTile+1, yTile);
				 }
			 }
		 }
				    
	   }

	  return null;
   }


   public synchronized void move (int direction) {
      int newX = x;
      Point tilePos = null;

      if (!window.isVisible ()) return;


      if (direction == 1) {		// move left
		  this.pState = State.LEFT;
		  if(!jumping||pState!=State.FALL)
		  	newAnimation("walk_left");
		  newX = x - DX;
		  if (newX < 0) {
			  System.out.println("NEW X: " + newX);
			x = 0;
			return;
		  }
		  tilePos = isCollision(newX, y);

      } else if (direction == 2 ) {		// move right
		  this.pState = State.RIGHT;
		  if(!jumping||pState!=State.FALL)
		  	newAnimation("walk_right");
		  int pWidth = playerAnimation.getWidth();
		  newX = x + DX;

		  int tileMapWidth = tileMap.getWidthPixels();

		  if (newX + pWidth >= tileMapWidth) {
			  System.out.println("New X: "+tileMapWidth);
			  x = tileMapWidth - pWidth;
			  return;
		  }
		  tilePos = isCollision(newX+pWidth, y);

      }else	if (direction == 3 && !jumping) {
		  newAnimation("jumping");
		  jump();
		  return;
	  }

      if (tilePos != null) {  //If player collides with wall (tile to the right or left)
//		  System.out.println("tile position is " + tilePos.x+"\t"+tilePos.y);
		  if(pState == State.DIE){
			  System.out.println("Dying???");
			  die();
			  return;
		  }
		  if (direction == 1) {
			  System.out.println (": Collision going left");
			  x = ((int) tilePos.getX() + 1) * TILE_SIZE;	   // keep flush with right side of tile
		  } else {
			  System.out.println (": Collision going right");
			  int playerWidth = playerAnimation.getWidth();
			  x = ((int) tilePos.getX()) * TILE_SIZE - playerWidth; // keep flush with left side of tile
		  }
	  } else {
		  System.out.println("**Move background?**");
		  if (direction == 1) {
			  x = newX;
			  bgManager.moveLeft();
		  }
		  else if (direction == 2) {
			  x = newX;
			  bgManager.moveRight();
		  }
		  if (isInAir()) {
			  System.out.println("In the air. Starting to fall.");
			  if (direction == 1) {				// make adjustment for falling on left side of tile
				  int playerWidth = playerAnimation.getWidth();
				  x = x - playerWidth - DX;
				  y = y-playerWidth-DY;
			  }
			  fall();
		  }
	  }

   }


   public boolean isInAir() {
      int playerHeight;
      Point tilePos;
      if (!jumping && !inAir) {
		  playerHeight = playerAnimation.getHeight();
		  tilePos = isCollision(x, y + playerHeight + 1); 	// check below player to see if there is a tile

		  if (tilePos == null)	{			   	// there is no tile below player, so player is in the air
			  System.out.println("inAIR");
			  return true;
		  }
		  else	{						// there is a tile below player, so the player is on a tile
			  return false;
		  }
      }

      return false;
   }


   private void fall() {
       inAir = true;
       timeElapsed = 0;

       goingUp = false;
       goingDown = true;

	   jumping = false;
	   this.pState = State.FALL;
		newAnimation("falling");

	   startY = y;
       initialVelocity = 0;
   }

	private void die(){
		System.out.println ("Collision FIRE");
		inAir = false;
		jumping = false;
		goingDown =false;
		pState = State.DIE;
		newAnimation("death");
	}

   private void jump () {
	   if (!window.isVisible ()) return;

	   jumping = true;
	   this.pState = State.JUMP;
	   timeElapsed = 0;

	   goingUp = true;
	   goingDown = false;

	   newAnimation("jumping");

	   startY = y;
	   initialVelocity = 60;
   }


   public void update () {
      int distance = 0;
      int newY = 0;

      timeElapsed++;

      if (jumping || inAir) {
		  distance = (int) (initialVelocity * timeElapsed - 4.9 * timeElapsed * timeElapsed);
		  newY = startY - distance;

		  if (newY > y && goingUp) {
			  goingUp = false;
			  goingDown = true;
		  }

		  if (goingUp) {
			  Point tilePos = isTileAbove(x, newY);
			  if (tilePos != null) {				// hits a tile going up
				   System.out.println ("Jumping: Collision Going Up!");
				   int offsetY = tileMap.getOffsetY();
				   int topTileY = ((int) tilePos.getY()) * TILE_SIZE + offsetY;
				   y = topTileY + TILE_SIZE;
				   fall();
			  }
			  else {
				  y = newY;
				  System.out.println ("Jumping: No collision. Going up");
			  }
		  } else if (goingDown) {
			  Point tilePos = isTileBelow(x, newY);
			  if(pState == State.DIE||y>800){
				  die();
				  return;
			  }
			  if (tilePos != null) {				// hits a tile going down
				   System.out.println ("Jumping: Collision Going Down!");
				   goingDown = false;

				   int offsetY = tileMap.getOffsetY();
				   int topTileY = ((int) tilePos.getY()) * TILE_SIZE - offsetY;

				  y = topTileY+ offsetY;
				  System.out.println("new Y: "+y+" Offset y: "+offsetY);
				  inAir = false;
				  jumping = false;

				  this.pState = State.LAND;
				  newAnimation("land");
			}
			else {
				y = newY;
				fall();
				System.out.println ("Jumping: No collision. Going Down ");
			}
		}
      }
   }


   public void moveUp () {
      if (!window.isVisible ()) return;

      y = y - DY;
   }

	private void newAnimation(String key){
		if(currentAnimation!=null){
			if(currentAnimation == playerAnimation.animations.get(key)){
				if(currentAnimation.isStillActive()) {
					return;
				}else{
					playerAnimation.animations.get(key).start();
				}
			}
			currentAnimation.stop();
			playerAnimation.animations.get(key).start();
			currentAnimation = playerAnimation.animations.get(key);
			return;
		}
		playerAnimation.animations.get(key).start();
		currentAnimation = playerAnimation.animations.get(key);
	}

   public int getX() {
      return x;
   }


   public void setX(int x) {
      this.x = x;
   }


   public int getY() {
      return y;
   }


   public void setY(int y) {
      this.y = y;
   }


   public void setState(String strState){
	   if(Objects.equals(strState, "IDLE")){
		   this.pState = State.IDLE;
		   newAnimation("idle");
	   }
   }

   public Image getImage() {
      return playerImage;
   }

   public PlayerAnimation getPlayerAnimation(){
	   return playerAnimation;
   }

	public String getState() {
	   return this.pState.toString();
	}
}