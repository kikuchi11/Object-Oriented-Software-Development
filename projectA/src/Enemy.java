/**
 * Enemy class, which extends Sprite class.
 */
public abstract class Enemy extends Sprite {

	private int hitpoints;
	private int points;
	private int time;
	private int delay;
	private static final int initY = -64;
	private static final int MIN_LIVES = 1;
	private static final int REDUCE_LIVES = -1;
	private static final int OFFWINDOW = 10;
	private static final int SHOTSPEED = 1;
	private static final int SHIELD = 0;
	
	/** Construct an Enemy object
	 * @param imageSrc String object
     * @param x float value
     */
	public Enemy(String imageSrc, float x) {
		super(imageSrc, x, initY);
	}
	
	@Override
	public void contactSprite(Sprite other) {
		if (other instanceof Player) {
			int lives = ((Player) other).getLives();
			boolean shield = ((Player) other).getShield();
			boolean powerupShield = ((Player) other).getPowerupShield();
			// Reduce lives if shield or powerupshield are not on, and if the lives is 1, exit the game.
			if (shield == false && powerupShield == false) {
				if (lives > MIN_LIVES) {
					((Player) other).setLives(lives+REDUCE_LIVES);
					((Player) other).setShield(true);
				}
				else {
					System.exit(0);
				}
			}
		}
		else if (other instanceof PlayerShot) {
			reduceHitpoints();
			// Update the score
			if (MIN_LIVES > hitpoints) {
				int score = World.getScore();
				World.setScore(score + points);
				// drop an item at a 5% chance when an enemy is destroyed
				if (World.getInstance().toDrop()) {
					//decide the type of the powerup item.
					int dropType = World.getInstance().dropType();
					if (dropType == SHIELD) {    
						World.getInstance().addPowerup(new PowerUpShield(getX(), getY()));
					}
					else if (dropType == SHOTSPEED) {
						World.getInstance().addPowerup(new ShotSpeed(getX(), getY()));
					}
				}
				deactivate();
			}
			other.deactivate();
		}
	}
	
	/** Set its argument integer value to the points value in this object.
	 * @param points integer value
     */
	public void setPoints(int points) {
		this.points = points;
	}
	
	/** Get points value in this object
	 * @return points value representing the points the Enemy object is assigned to.
	 */
	public int getPoints() {
		return points;
	}
	
	/** Set its argument integer value to the points value in this object
	 * @param hitpoints integer value
     */
	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}
	
	/** Get hitpoints value in this object
	 * @return hitpoints value in this object.
     */
	public int getHitpoints() {
		return hitpoints;
	}

	/** Reduce the hitpoints value in this object
     */
	public void reduceHitpoints() {
		hitpoints += REDUCE_LIVES;
	}

	/** Get time value in this object
	 * @return time value in this object
     */
	public int getTime() {
		return time;
	}
	
	/** Set its argument integer value to the time value in this object.
	 * @param time integer value
     */
	public void setTime(int time) {
		this.time = time;
	}
	
	/** Construct an Enemy object
	 * @return delay value in this object
     */
	public int getDelay() {
		return delay;
	}
	
	/** Set its argument to the delay value in this object.
	 * @param delay integer value
     */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/** Checks whether an object is active
	 * @param x float value
     * @param y float value
     * @return true if the object is not beyond the screen height and false otherwise.
     */
	public boolean isActive(float x, float y) {
		if (App.SCREEN_HEIGHT + OFFWINDOW > y) {
			return true;
		}
		return false;
	}

}