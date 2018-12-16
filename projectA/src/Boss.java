import org.newdawn.slick.Input;
import java.util.Random;

/**
 * Boss class, extends Enemy and Sprite
 */
public class Boss extends Enemy {

	private static final float SPEED = 0.05f;
	private static final int HITPOINTS = 60;
	private static final int POINTS = 5000;
	private static final int maxX = 896;
	private static final int minX = 128;
	private static final int WAIT_1 = 5000;
	private static final int WAIT_2 = 2000;
	private static final String BOSS_PATH = "res/boss.png";
	private static final int initY = 72;
	private static final float HORIZONTAL_SPEED = 0.2f;
	private static final float HORIZONTAL_SPEED_2 = 0.1f;
	private static final int INTERVAL = 200;
	private int shootTime;
	private static final int INITIAL_SHOOT_TIME = 0;
	private static final int SHOT_XOFFSET_1 = -97;
	private static final int SHOT_XOFFSET_2 = -74;
	private static final int SHOT_XOFFSET_3 = 74;
	private static final int SHOT_XOFFSET_4 = 97;
	private int wait;
	private static final int INITIAL_TIME = 0;
	private static final int SHOOT_DURATION = 3000;
	private boolean hasReached;
	private static final int STAGE1 = 1;
	private static final int STAGE2 = 2;
	private static final int STAGE3 = 3;
	private static final int STAGE4 = 4;
	private static final int STAGE5 = 5;
	private static final int STAGE6 = 6;
	private static final int NEXTSTAGE = 1;
	private static final int INITIAL_STAGE = 1;
	private int stage;
	private int destinationX;
	private boolean positiveDirection = true;
	
	private static final int MIN_LIVES = 1;
	private static final int REDUCE_LIVES = -1;
	private static final int SHOTSPEED = 1;
	private static final int SHIELD = 0;
	
	/** Construct a Boss object
     * @param x float value
     * @param y float value
     */
	public Boss(float x, int delay) {
		super(BOSS_PATH, x);
		super.setHitpoints(HITPOINTS);
		super.setPoints(POINTS);
		super.setDelay(delay);
		
		// Initialise stage, wait time, hasReached and shootTime
		wait = INITIAL_TIME;
		stage = INITIAL_STAGE;
		hasReached = true;
		shootTime = INITIAL_SHOOT_TIME;
	}
	
	@Override
	public void contactSprite(Sprite other) {
		if (other instanceof Player && onScreen()) {
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
		else if (other instanceof PlayerShot && onScreen()) {
			super.reduceHitpoints();
			// Update the score
			int hitpoints = super.getHitpoints();
			if (MIN_LIVES > hitpoints) {
				int score = World.getScore();
				World.setScore(score + super.getPoints());
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

	@Override
	public void update(Input input, int delta) {
		// Start moving when the time since the game started becomes larger than the delay value.
		if (World.getTime() > super.getDelay()) {
			float y = super.getY();
			float x = super.getX();
			if (super.isActive(x, y)) {
				// If the current stage is stage 1
				if (stage == STAGE1) {
					// Move to the initial y coordinate.
					if (initY > super.getY()) {
						move(0, SPEED*delta);
					}
					else {
						stage += NEXTSTAGE;
						// reset wait time
						wait = INITIAL_TIME;
					}
				}
				// If the current stage is stage 2
				else if (stage == STAGE2) {
					// Wait WAIT_1 milliseconds
					if (WAIT_1 > wait) {
						wait += delta;
					}
					else {
						stage += NEXTSTAGE;
						// reset wait time
						wait = INITIAL_TIME;
					}
				}
				// If the current stage is stage 3
				else if (stage == STAGE3) {
					// Horizontal move
					if (hasReached == true) {
						// set the destination and reset hasReached
						Random rand = new Random();
						destinationX = rand.nextInt((maxX-minX)-1) + minX;
						hasReached = false;
						// decide which direction the boss moves toward
						if (destinationX >= x) {
							positiveDirection = true;
						}
						else {
							positiveDirection = false;
						}
					}
					else {
						if (positiveDirection) {
							move(HORIZONTAL_SPEED*delta, 0);
							if (super.getX() >= destinationX) {
								stage += NEXTSTAGE;
								// reset positiveDirection and hasReached
								positiveDirection = true;
								hasReached = true;
							}
						}
						else {
							move(-(HORIZONTAL_SPEED*delta), 0);
							if (destinationX > super.getX()) {
								stage += NEXTSTAGE;
								// reset positiveDirection and hasReached
								positiveDirection = true;
								hasReached = true;
							}
						}
					}
				}
				// If the current stage is stage 4
				else if (stage == STAGE4) {
					// Wait for WAIT_2 milliseconds
					if (WAIT_2 > wait) {
						wait += delta;
					}
					else  {
						stage += NEXTSTAGE;
						// reset wait time, shootTime
						wait = INITIAL_TIME;
						shootTime = INITIAL_SHOOT_TIME;
					}
				}
				// If the current stage is stage 5
				else if (stage == STAGE5) {
					// Start shooting
					if (hasReached == true) {
						// set the destination and reset hasReached
						Random rand = new Random();
						destinationX = rand.nextInt((maxX-minX)-1) + minX;
						hasReached = false;
						// decide which direction the boss moves toward
						if (destinationX >= x) {
							positiveDirection = true;
						}
						else {
							positiveDirection = false;
						}
					}
					else {
						wait += delta;
						// shoot
						if (wait >= INTERVAL) {
							World.getInstance().addSprite(new EnemyShot(getX()+SHOT_XOFFSET_1, getY()));
							World.getInstance().addSprite(new EnemyShot(getX()+SHOT_XOFFSET_2, getY()));
							World.getInstance().addSprite(new EnemyShot(getX()+SHOT_XOFFSET_3, getY()));
							World.getInstance().addSprite(new EnemyShot(getX()+SHOT_XOFFSET_4, getY()));
							wait = INITIAL_TIME;
						}
						if (positiveDirection) {
							move(HORIZONTAL_SPEED_2*delta, 0);
							if (super.getX() >= destinationX) {
								stage += NEXTSTAGE;
								// reset positiveDirection and hasReached
								positiveDirection = true;
								hasReached = true;
								wait = INITIAL_TIME;
							}
						}
						else {
							move(-(HORIZONTAL_SPEED_2*delta), 0);
							if (destinationX > super.getX()) {
								stage += NEXTSTAGE;
								// reset positiveDirection and hasReached
								positiveDirection = true;
								hasReached = true;
								wait = INITIAL_TIME;
							}
						}
					}
				}
				// If the current stage is stage 6
				else if (stage == STAGE6) {
					// Stop and keep shooting
					wait += delta;
					shootTime += delta;
					if (SHOOT_DURATION > shootTime) {
						if (wait >= INTERVAL) {
								World.getInstance().addSprite(new EnemyShot(getX()+SHOT_XOFFSET_1, getY()));
								World.getInstance().addSprite(new EnemyShot(getX()+SHOT_XOFFSET_2, getY()));
								World.getInstance().addSprite(new EnemyShot(getX()+SHOT_XOFFSET_3, getY()));
								World.getInstance().addSprite(new EnemyShot(getX()+SHOT_XOFFSET_4, getY()));
								wait = INITIAL_TIME;
						}
					}
					else {
						// reset shootTime, stage and wait
						stage = INITIAL_STAGE;
						shootTime = INITIAL_SHOOT_TIME;
						wait = INITIAL_TIME;
					}
				}
			}
			else {
				deactivate();
			}
		}
	}
}