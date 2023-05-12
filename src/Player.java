import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Player {
	public int heart=3;
	public boolean alreadyExecuted = false;

	private static final int DX = 10;    // amount of X pixels to move in one keystroke

	private enum State{
		WALK,
		JUMP,
		FALL,
		LAND,
		DIE,
		IDLE,
		USE
	}

	protected Image heartImage;
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
   private final int pSize;
   private boolean goingUp;
   private boolean goingDown;

   private boolean jumping = false;
   private boolean inAir;
   private int initialVelocity;
   protected boolean gameOver=false;
   protected int points =0;
   protected SoundManager sm = SoundManager.getInstance();

	public Player (JPanel window, TileMap t, BackgroundManager b) {

	   this.window = window;
	   tileMap = t;            // tile map on which the player's sprite is displayed
	   bgManager = b;            // instance of BackgroundManager

	   goingUp = goingDown = false;
       inAir = false;
	   heartImage =  ImageManager.loadBufferedImage("Assets/heart.png");

	   playerAnimation = new PlayerAnimation();
	   this.pState = State.IDLE;
	   playerAnimation.start("idle","");
	   pSize = playerAnimation.getSize();
   }


   public Tile isCollision(int newX, int newY)  {
	   offsetX= tileMap.getOffsetX();
	   int xTile = TileMap.pixelsToTiles(newX+96);
	   int yTile = TileMap.pixelsToTiles(newY-offsetY);
	   Tile tile = tileMap.getTile(xTile, yTile);

	   if (tile!= null) {
		   switch (tile.getState()){
			   case "FOUNDATION", "USE", "STAR", "CHEST","HEART" -> {
				   return tile;
			   }
		   }
	   }
	   return null;
   }


   public Tile isTileBelow(int newX, int newY) {
	   int xTile = TileMap.pixelsToTiles(newX+96);
	   int yTileFrom = TileMap.pixelsToTiles(y - offsetY);
	   int yTileTo = TileMap.pixelsToTiles(newY - offsetY+96 );
	   System.out.println("\n !!!!!!!!!BELOW new Y: "+(newX));
	   System.out.println(" Y from:"+yTileFrom+ " \tyTileTo: " +yTileTo+"\t XTile: "+xTile);
	   for (int yTile=yTileFrom+1; yTile<=yTileTo; yTile++) {
		   Tile tile = tileMap.getTile(xTile, yTile);
		   if ( tile!= null ){
			   System.out.println("Collides with tile going down");
			   if(Objects.equals(tile.getState(), "FOUNDATION")){
				   return tile;
			   }
		   } else {
			   tile =tileMap.getTile(xTile+1, yTile);
			   if ( tile!= null&& Objects.equals(tile.getState(), "FOUNDATION")) {
				   int leftSide = (xTile + 1) * TILE_SIZE;
				   if (newX + pSize > leftSide) {
					   return tile;
				   }
			   }
		   }
	   }
	   return null;
   }



   public Tile isTileAbove(int newX, int newY) {
	   int xTile = TileMap.pixelsToTiles(newX+96);

	   int yTileFrom = TileMap.pixelsToTiles(y-offsetY);
	   int yTileTo = TileMap.pixelsToTiles(newY-offsetY);

	   for (int yTile=yTileFrom; yTile>=yTileTo; yTile--) {
		   Tile tile = tileMap.getTile(xTile, yTile);
		   if ( tile!= null ){
			   System.out.println("Collides with tile going down");
			   if(Objects.equals(tile.getState(), "FOUNDATION")) {
				   System.out.println("Collides with Tile:" + yTile);
				   return tile;
			   }
		   } else {
			   tile =tileMap.getTile(xTile+1, yTile);
			   if ( tile!= null&& Objects.equals(tile.getState(), "FOUNDATION")){
				   int leftSide = (xTile + 1) * TILE_SIZE;
				   if (newX + pSize > leftSide) {
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
	   Point tilePos;
	   if (!window.isVisible ()) return;

	   // move left
	   if (direction == 1) {
		   if(!inAir){
			   this.pState = State.WALK;
			   playerAnimation.loop = true;
			   playerAnimation.start("walk_right","footstep");
		   }
		   playerAnimation.left = true;
		   newX = x - DX;

		   if (newX < 0) {
			   x = 0;
			   return;
		   }
		   tile = isCollision(newX, y);
		   if(tile != null){
			   if(!Objects.equals(tile.getState(), "FOUNDATION"))
				   tile = null;
		   }
	   } else if (direction == 2 ) {        // move right
		   if(!inAir) {
			   this.pState = State.WALK;
			   playerAnimation.loop = true;
			   playerAnimation.start("walk_right", "footstep");
		   }
		   playerAnimation.left = false;
		   newX = x + DX;
		   int tileMapWidth = tileMap.getWidthPixels();

		   if (newX + pSize >= tileMapWidth) {
			   x = tileMapWidth - pSize;
			   return;
		   }
		   tile = isCollision(newX, y);
		   if(tile != null){
			   if(!Objects.equals(tile.getState(), "FOUNDATION"))
				   tile = null;
		   }

	   }else if (direction == 3 && !jumping) {
		   playerAnimation.loop = false;
		   playerAnimation.start("jumping","jump");
		   jump();
		   return;
	   }

      if (tile != null) {  //If player collides with wall (tile to the right or left)
		  tilePos = tile.position;
		  if (direction == 1) {
			  x = ((int) tilePos.getX() + 1) * TILE_SIZE;       // keep flush with right side of tile
		  } else {
			  x = ((int) tilePos.getX()) * TILE_SIZE - pSize; // keep flush with left side of tile
		  }
	  } else {

		  if (direction == 1) {
			  x = newX;
			  bgManager.moveLeft();
		  } else if (direction == 2) {
			  x = newX;
			  bgManager.moveRight();
		  }
		  if (isInAir()) {
			  if (direction == 1) {                // make adjustment for falling on left side of tile
				  x = x - DX;
			  }
			  fall();
		  }
	  }
   }


   public boolean isInAir() {
      Tile tile;
      if (!jumping && !inAir) {
		  tile = isTileBelow(x, y); // check below player to see if there is a tile
		  // there is no tile below player, so player is in the air
		  return tile == null;
      }
      return false;
   }




   public void update () {
      int distance;
      int newY;

      timeElapsed++;
	  Tile t = isCollision(x,y);
	  if(t != null) {
		  if(Objects.equals(t.getState(), "HEART")){
			  if(!t.used){
				  sm.playSound("heart",false);
				  heart++;
				  t.used=true;
			  }
		  }
	  }
	  inAir =isInAir();
      if (jumping || inAir) {
		  distance = (int) (initialVelocity * timeElapsed - 4.9 * timeElapsed * timeElapsed);
		  newY = startY - distance;

		  if (newY > y && goingUp) {
			  goingUp = false;
			  goingDown = true;
		  }

		  if (goingUp) {
			  newY-=pSize;
			  Tile tile = isTileAbove(x, newY);
			  if (tile != null) {                // hits a tile going up
				  Point tilePos = tile.position;
				  int topTileY = ((int) tilePos.getY()) *TILE_SIZE+offsetY;
				  y = topTileY+TILE_SIZE+64;
				  fall();
			  } else {
				  y = newY+pSize;
			  }
		  } else if (goingDown) {
			  Tile tile = isTileBelow(x, newY);
			  if(pState != State.DIE&&y>800){
				  System.out.println("died1");
				  die();
				  return;
			  }
			  if (tile != null) {                // hits a tile going down
				  Point tilePos = tile.position;
				  goingDown = false;
				  int topTileY = (((int) tilePos.getY()) * TILE_SIZE)+offsetY;
				  y = topTileY-TILE_SIZE;
				  inAir = false;
				  jumping = false;

				  this.pState = State.LAND;
				  playerAnimation.start("land","");

			  } else {
				  y = newY;
				  fall();
			  }
		  }
      }
   }

   public int getHeartNum() {
	   return heart;
   }

   public Image getHeartImage(){
	   return heartImage;
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
		System.out.println("newX: "+x+"    newY: "+y);
		Tile tile = isCollision(x,y);
		if(tile !=null && Objects.equals(tile.getState(),"STAR")){
			offsetX= tileMap.getOffsetX();
			System.out.println("\n\n```Bounding rect player x: " + (x+offsetX) + " y: " + (y)+"```");
			System.out.println("Offset X: "+offsetX+"  Offset Y: "+offsetY);
			return new Rectangle2D.Double (x+offsetX, y, 100, 100);
		}
		return null;
	}

	public void use(){
		Tile tile = isCollision(x,y);
		if(tile !=null && Objects.equals(tile.getState(),"USE")){
			if(!tile.used){
				System.out.println("USE");
				playerAnimation.loop = true;
				playerAnimation.start("use","collect");
				pState = State.USE;
				switch (tile.getUseType()){
					case "CORRECT"-> points +=5;
					case "INCORRECT"-> points -=2;
					case "NEVER"-> points -=5;
				}
				tile.used = true;
				System.out.println("POINTS: " + points);
			}
		}
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
		alreadyExecuted = false;
		playerAnimation.loop = false;
		playerAnimation.start("death","die");
		if(heart == 0){
			gameOver = true;
		}
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
		initialVelocity = 50;
	}



}