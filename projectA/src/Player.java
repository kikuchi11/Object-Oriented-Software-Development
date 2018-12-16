
import org.newdawn.slick.Input;

/**
 * Player class, which extends Sprite class.
 */
public class Player extends Sprite {

	private static final int PLAYER_INITIAL_X = 480;
	private static final int PLAYER_INITIAL_Y = 688;
	private static final float SPEED = 0.5f;
	private int points = 0;
	private int lives = 3;
	private boolean shield;
	private int shieldTime;
	private static final int INITIAL_TIME = 0;
	private static final int MAX_SHIELD_TIME = 3000;
	private static final String PLAYER_PATH = "res/spaceship.png";
	private static final int REDUCE_LIVES = -1;
	private static final int MIN_LIVES = 1;
	private int shootTime;
	private static final int SHOOT_INTERVAL = 350;
	private static final int POWERUP_SHOOT_INTERVAL = 150;
	private static final int MAX_POWERUP_TIME = 5000;
	private int powerupShieldTime;
	private int shotspeedTime;
	private boolean shotspeed;
	private boolean powerupShield;
	
	/** Construct a Player object
     */
	public Player() {
		super(PLAYER_PATH, PLAYER_INITIAL_X, PLAYER_INITIAL_Y);
		// Initialise shootTime and shieldTime to zero and shield and shotspeed to false
		shootTime = INITIAL_TIME;
		shieldTime = INITIAL_TIME;
		shield = false;
		powerupShield = false;
		shotspeed = false;
		powerupShield = false;
	}
	
	@Override
	public void update(Input input, int delta) {
		// update time
		shootTime += delta;
		
		doMovement(input, delta);
		// do shooting
		if (input.isKeyDown(Input.KEY_SPACE)) {
			// Use the shorter shoot interval if shotspeed is on.
			if (shotspeed == true) {
				if (shootTime >= POWERUP_SHOOT_INTERVAL) {
					World.getInstance().addSprite(new PlayerShot(getX(), getY()));
					shootTime = INITIAL_TIME;
				}
			}
			else {
				if (shootTime >= SHOOT_INTERVAL) {
					World.getInstance().addSprite(new PlayerShot(getX(), getY()));
					shootTime = INITIAL_TIME;
				}
			}
		}
		// Update the time left until the effect is over.
		if (shield == true) {
			shieldTime += delta;
			if (shieldTime >= MAX_SHIELD_TIME) {
				shield = false;
				shieldTime = INITIAL_TIME;
			}
		}
		if (powerupShield == true) {
			powerupShieldTime += delta;
			if (powerupShieldTime >= MAX_POWERUP_TIME) {
				powerupShield = false;
				powerupShieldTime = INITIAL_TIME;
			}
		}
		if (shotspeed == true) {
			shotspeedTime += delta;
			if (shotspeedTime >= MAX_POWERUP_TIME) {
				shotspeed = false;
				shotspeedTime = INITIAL_TIME;
			}
		}
	}
	
	/** Get the lives value in this object.
	 * @return lives value in this object.
     */
	public int getLives() {
		return this.lives;
	}
	
	/** Set its argument integer value to the lives value in this object
	 * @param lives integer value
     */
	public void setLives(int lives) {
		this.lives = lives;
	}
	
	/** Get points value in this object
     * @return points value in this object
     */
	public int getPoints() {
		return this.points;
	}
	
	/** Set its argument integer value to the points value in this object
	 * @param newPoints integer value
     */
	public void setPoints(int newPoints) {
		this.points = newPoints;
	}
	
	/* Move the player object
	 * @param input Input object
     * @param delta integer value
     */
	private void doMovement(Input input, int delta) {
		// handle horizontal movement
		float dx = 0;
		if (input.isKeyDown(Input.KEY_LEFT)) {
			dx -= SPEED;
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			dx += SPEED;
		}

		// handle vertical movement
		float dy = 0;
		if (input.isKeyDown(Input.KEY_UP)) {
			dy -= SPEED;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			dy += SPEED;
		}
		
		move(dx * delta, dy * delta);
		clampToScreen();
	}
	
	@Override
	public void contactSprite(Sprite other) {
		if (other instanceof EnemyShot) {
			// Reduce lives if shield and powerupshield are not on, and exit the game if lives is 1.
			if (shield == false && powerupShield == false) {
				if (lives > MIN_LIVES) {
					lives += REDUCE_LIVES;
					shield = true;
					other.deactivate();
				}  
				else {
					System.exit(0);
				}
			}
		}
		// Activate powerup items when the player and powerup items collide.
		else if (other instanceof PowerUpShield) {
			powerupShield = true;
			powerupShieldTime = INITIAL_TIME;
			other.deactivate();
		}
		else if (other instanceof ShotSpeed) {
			shotspeedTime = INITIAL_TIME;
			shotspeed = true;
			other.deactivate();
		}
	}
	
	/** Get the shield value in this object
     * @return the shield value in this object representing whether the shield is active.
     */
	public boolean getShield() {
		return this.shield;
	}
	
	/** Set its argument boolean value to the shield value in this object.
	 * @param isShield boolean value
     */
	public void setShield(boolean isShield) {
		this.shield = isShield;
	}
	
	/** Get the powerupShield value in this object
     * @return powerupShield value in this object
     */
	public boolean getPowerupShield() {
		return powerupShield;
	}
	
	/** Get the shotspeed value in this object
	 * @return shotspeed boolean value
     */
	public boolean getShotspeed() {
		return shotspeed;
	}
}