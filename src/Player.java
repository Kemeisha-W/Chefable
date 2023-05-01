import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Player {
	private int heart = 0;
	private static final int DX = 16;    // amount of X pixels to move in one keystroke
	private static final int DY = 32;    // amount of Y pixels to move in one keystroke

	private enum State{
		RIGHT,
		LEFT,
		JUMP,
		FALL,
		LAND,
		DIE,
		IDLE,
		USE
	}

	protected Image heartImage=null;
	private State pState;
	private int offsetY;
	private int offsetX;
	private static final int TILE_SIZE = TileMap.getTileSize();
	private JPanel window;        // reference to the JFrame on which player is drawn
	private TileMap tileMap;
	private final BackgroundManager bgManager;

	private int x;            // x-position of player's sprite
	private int y;            // y-position of player's sprite


	private int timeElapsed;
   private int startY;
   private final PlayerAnimation playerAnimation;
   private boolean goingUp;
   private boolean goingDown;

   private boolean jumping = false;
   private boolean inAir;
   private int initialVelocity;
   private int startAir;

   public Player (JPanel window, TileMap t, BackgroundManager b) {
	   this.window = window;
	   tileMap = t;            // tile map on which the player's sprite is displayed
	   bgManager = b;            // instance of BackgroundManager

	   goingUp = goingDown = false;
       inAir = false;
	   heartImage =  ImageManager.loadBufferedImage("Assets/heart.png");
	   heart = 3;

	   playerAnimation = new PlayerAnimation();
	   this.pState = State.IDLE;
	   playerAnimation.start("idle","");
   }


   public Tile isCollision(int newX, int newY) {
	   offsetX= tileMap.getOffsetX();
	   int xTile = TileMap.pixelsToTiles(newX+96);
	   int yTile = TileMap.pixelsToTiles(newY-offsetY);
	   System.out.println("Player collision X: "+(newX+96)+" Y: "+(newY-offsetY));
	   Tile tile = tileMap.getTile(xTile, yTile);
	   System.out.println("XTile: "+xTile+" YTile: "+yTile);

	   if (tile!= null && Objects.equals(tile.getState(), "FOUNDATION")) {
		   System.out.println("is collision TILE" );
		   if(isFire(tile)){
			   return null;
		   }
		   return tile;
	   } else if(tile != null && Objects.equals(tile.getState(), "USE")){
		   System.out.println("TILE collision  USE" );
		   return tile;
	   }else {
		   return null;
	   }
   }


   public Tile isTileBelow(int newX, int newY) {
	   int xTile = TileMap.pixelsToTiles(newX+96);
	   int yTileFrom = TileMap.pixelsToTiles(y - offsetY);
	   int yTileTo = TileMap.pixelsToTiles(newY - offsetY +100);
	   System.out.println("\n !!!!!!!!!BELOW new Y: "+(newX));
	   System.out.println(" Y from:"+yTileFrom+ " \tyTileTo: " +yTileTo+"\t XTile: "+xTile);

	   for (int yTile=yTileFrom+1; yTile<=yTileTo; yTile++) {
		   System.out.println(" Y from:"+yTileFrom+ " \tyTile: " +yTile+"\t XTile: "+xTile);
		   Tile tile = tileMap.getTile(xTile, yTile);
		   if ( tile!= null && Objects.equals(tile.getState(), "FOUNDATION")){
			   System.out.println("Collides with tile going down");
			   if(isFire(tile)){
				   return null;
			   }
			   return tile;
		   } else {
			   tile =tileMap.getTile(xTile+1, yTile);
			   if ( tile!= null&& Objects.equals(tile.getState(), "FOUNDATION")) {
				   if(isFire(tile)){
					   return null;
				   }
				   int leftSide = (xTile + 1) * TILE_SIZE;
				   if (newX + playerAnimation.getWidth() > leftSide) {
					   return tile;
				   }
			   }
		   }
	   }
	   return null;
   }



   public Tile isTileAbove(int newX, int newY) {
	   int playerWidth = playerAnimation.getWidth();
	   int xTile = TileMap.pixelsToTiles(newX);

	   int yTileFrom = TileMap.pixelsToTiles(y - offsetY);
	   int yTileTo = TileMap.pixelsToTiles(newY - offsetY+32);
	   System.out.println("\n ________ABOVE new Y: "+newY);
	   System.out.println(" Y from:"+yTileFrom+ " \tyTileTo: " +yTileTo+"\t XTile: "+xTile);

	   for (int yTile=yTileFrom; yTile>=yTileTo; yTile--) {
		   System.out.println(" Y from:"+yTileFrom+ " \tyTile: " +yTile+"\t XTile: "+xTile);
		   Tile tile = tileMap.getTile(xTile, yTile);
		   if ( tile!= null && Objects.equals(tile.getState(), "FOUNDATION")) {
			   System.out.println("Collides with Tile:"+yTile);

			   return tile;
		   } else {
			   tile =tileMap.getTile(xTile+1, yTile);
			   if ( tile!= null&& Objects.equals(tile.getState(), "FOUNDATION")){
				   int leftSide = (xTile + 1) * TILE_SIZE;
				   if (newX + playerWidth > leftSide) {
					   return tile;
				   }
			   }
		   }
	   }
	   return null;
   }


   public synchronized void move (int direction) {
	   int newX = x;
	   Tile tile = null;
	   Point tilePos = null;
	   if (!window.isVisible ()) return;
	   // move left

	   if (direction == 1) {
		   this.pState = State.LEFT;
		   if(!inAir){
			   playerAnimation.loop = true;
			   playerAnimation.start("walk_left","footstep");
		   }

		   newX = x - DX;
		   System.out.println("NEW X left: " + newX);

		   if (newX < 0) {
			   System.out.println("left NEW X: " + newX);
			   x = 0;
			   return;
		   }
		   System.out.println("X left: " + x);
		   tile = isCollision(newX, y);
		   if(tile != null && Objects.equals(tile.getState(), "USE"))
			   tile = null;
	   } else if (direction == 2 ) {        // move right
		   this.pState = State.RIGHT;
		   if(!inAir) {
			   playerAnimation.loop = true;
			   playerAnimation.start("walk_right", "footstep");
		   }

		   System.out.println("moving right");
		   int pWidth = playerAnimation.getWidth();
		   newX = x + DX;
		   int tileMapWidth = tileMap.getWidthPixels();
		   if (newX + pWidth >= tileMapWidth) {
			   x = tileMapWidth - pWidth;
//			   System.out.println(" right X: "+x);
			   return;
		   }
		   tile = isCollision(newX, y);
		   if(tile != null && Objects.equals(tile.getState(), "USE"))
			   tile = null;
	   }else if (direction == 3 && !jumping) {
		   playerAnimation.loop = false;
		   playerAnimation.start("jumping","jump");
		   jump();
		   return;
	   }

      if (tile != null) {  //If player collides with wall (tile to the right or left)
		  tilePos = tile.position;
		  System.out.println("tile position is " + tilePos.x+"\t"+tilePos.y);
		  if(pState == State.DIE){
			  die();
			  return;
		  }
		  if (direction == 1) {
			  System.out.println (": Collision going left");
			  x = ((int) tilePos.getX() + 1) * TILE_SIZE;       // keep flush with right side of tile
		  } else {
			  System.out.println (": Collision going right");
			  int playerWidth = playerAnimation.getWidth();
			  x = ((int) tilePos.getX()) * TILE_SIZE - playerWidth; // keep flush with left side of tile
		  }
	  } else {
		  System.out.println("**NO Collision left or right**");
		  if (direction == 1) {
			  x = newX;
			  System.out.println(" left X: "+x);
			  bgManager.moveLeft();
		  } else if (direction == 2) {
			  x = newX;
			  System.out.println(" right X: "+x);
			  bgManager.moveRight();
		  }
		  if (isInAir()) {
			  System.out.println("In the air. Starting to fall.");
			  if (direction == 1) {                // make adjustment for falling on left side of tile
				  int playerWidth = playerAnimation.getWidth();
				  x = x -playerWidth+ DX;
			  }
			  fall();
		  }
	  }

   }

   public void use(){
	   Tile tile = isCollision(x,y);
	   System.out.println("Use test no offset x: " + x + " y: " + y+"\nTILE: " + tile);
	   if(tile !=null && Objects.equals(tile.getState(),"USE")){
		   System.out.println("USE");
		   playerAnimation.loop = true;
		   playerAnimation.start("use","");
		   pState = State.USE;
	   }
   }

   public boolean isInAir() {
      Tile tile;
      if (!jumping && !inAir) {
		  tile = isTileBelow(x, y); // check below player to see if there is a tile
		  if (tile == null) {                   // there is no tile below player, so player is in the air
			  System.out.println("inAIR");
			  return true;
		  } else {                        // there is a tile below player, so the player is on a tile
			  return false;
		  }
      }

      return false;
   }




   public void update () {
      int distance = 0;
      int newY = 0;

      timeElapsed++;

      if (jumping || inAir) {
		  System.out.println("Jumping: "+jumping+" \n inAir: "+inAir);
		  distance = (int) (initialVelocity * timeElapsed - 4.9 * timeElapsed * timeElapsed);
		  newY = startY - distance;
		  System.out.println("Start y: "+startY+" Distance: "+distance);

		  if (newY > y && goingUp) {
			  goingUp = false;
			  goingDown = true;
		  }

		  if (goingUp) {
			  Tile tile = isTileAbove(x, newY);
			  if (tile != null) {                // hits a tile going up
				  Point tilePos = tile.position;
				  System.out.println ("Collision Going Up!");
				  int topTileY = ((int) tilePos.getY()) * TILE_SIZE + offsetY;
				  y = topTileY + TILE_SIZE;
				  fall();
			  } else {
				  y = newY;
				  System.out.println ("No collision. Going up");
			  }
		  } else if (goingDown) {
			  Tile tile = isTileBelow(x, newY);
			  if(pState == State.DIE||y>800){
				  die();
				  return;
			  }
			  if (tile != null) {                // hits a tile going down
				  Point tilePos = tile.position;
				  System.out.println ("Jumping: Collision Going Down!");
				  goingDown = false;
				  int topTileY = (((int) tilePos.getY()) * TILE_SIZE)+offsetY;
				  y = topTileY-TILE_SIZE  ;
				  inAir = false;
				  jumping = false;

				  this.pState = State.LAND;
				  playerAnimation.start("land","");

			  } else {
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

   public int getHeartNum() {
	   return heart;
   }

   public Image getHeartImage(){
	   return heartImage;
   }

	public void increaseHeart() {
		this.heart++;
	}
	public void reduceHeart() {
		this.heart--;
	}
   public int getX() {
      return x;
   }


   public int getY() {
      return y;
   }

	public void setX(int x) {
		this.x = x;
	}
   public void setY(int y) {
      this.y = y;
   }

   public void setPlayOffsetY(int offsetY){
	   this.offsetY = offsetY;
   }

   public void setState(String strState){
	   if(Objects.equals(strState, "IDLE")){
		   this.pState = State.IDLE;
		   playerAnimation.start("idle","");

	   }
   }

   public PlayerAnimation getPlayerAnimation(){
	   return playerAnimation;
   }

	public String getState() {
		return this.pState.toString();
	}

	public Rectangle2D.Double getBoundingRectangle() {
		offsetX= tileMap.getOffsetX();
		return new Rectangle2D.Double (x+offsetX, y, 50, 50);
	}

	private boolean isFire(Tile tile){
		System.out.println("Is Fire below check");
		System.out.println("Tile: " + tile.getDisplay());
		if(Objects.equals(tile.getDisplay(), "ANIMATION")){
			die();
			return true;
		}
		return false;
	}

	private void fall() {
		this.pState = State.FALL;
		playerAnimation.start("falling","");


		inAir = true;
		timeElapsed = 1;

		goingUp = false;
		goingDown = true;

		jumping = false;

		startY = y;
		initialVelocity = 0;
	}

	private void die(){
		heart --;
		inAir = false;
		jumping = false;
		goingDown =false;
		pState = State.DIE;
		playerAnimation.start("death","");
	}

	private void jump () {
		if (!window.isVisible ()) return;
		playerAnimation.loop = false;
		playerAnimation.start("jumping","jump");

		jumping = true;
		this.pState = State.JUMP;
		timeElapsed = 0;

		goingUp = true;
		goingDown = false;


		startY = y;
		initialVelocity = 60;
	}

}