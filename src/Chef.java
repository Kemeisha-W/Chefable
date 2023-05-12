
public class Chef {

	private int time;
	private static final int DX = 10;

	private enum State{
		WALK,
		TALK,
		WAIT,
		IDLE,
	}
	private State cState;

	private int x;            // x-position of player's sprite
	private int y;            // y-position of player's sprite

   private final ChefAnimation chefAnimation;
   int timeChange;

	public Chef() {
		chefAnimation = new ChefAnimation();
		this.cState = State.IDLE;
		timeChange =5;
		chefAnimation.start("idle","");
		time =0;
   }



   public synchronized void roaming () {
		if(time ==40){
			cState = State.WALK;
			chefAnimation.loop = true;
			chefAnimation.start("walk","");
			x -= DX;
		}else if(time == 200){
			cState = State.WAIT;
			chefAnimation.loop = true;
			chefAnimation.start("wait","");
		}

		if(cState == State.WALK){
			x -= DX;
		}

	   time = time + timeChange;
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

   public ChefAnimation getchefAnimation(){
	   return chefAnimation;
   }

	public String getState() {
		return this.cState.toString();
	}

}