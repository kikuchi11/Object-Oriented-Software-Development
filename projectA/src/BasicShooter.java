import org.newdawn.slick.Input;
import java.util.Random;

/**
 * BasicShooter class, which extends Enemy
 */
public class BasicShooter extends Enemy {

	private static final int POINTS = 200;
	private static final float SPEED = 0.2f;
	private static final int maxY = 464;
	private static final int minY = 48;
	private static final int INTERVAL = 3500;
	private static final String BASICSHOOTER_PATH = "res/basic-shooter.png";
	private static final int HITPOINTS = 1;
	private boolean hasReached;
	private final int destinationY;
	private int time; 
	
	/** Construct a BasicShooter object
     * @param x float value
     * @param y float value
     */
	public BasicShooter(float x, int delay) {
		super(BASICSHOOTER_PATH, x);
		super.setHitpoints(HITPOINTS);
		super.setPoints(POINTS);
		super.setDelay(delay);
		// Set the y coordinate of the destination
		Random rand = new Random();
		destinationY = rand.nextInt((maxY-minY)-1) + minY;
		
		// Initialise time to zero and hasReached to false
		time = 0;
		hasReached = false;
	}

	@Override
	public void update(Input input, int delta) {
		// Start moving when the time since the game started becomes larger than the delay value.
		if (World.getTime() > super.getDelay()) {
			float y = super.getY();
			float x = super.getX();
			if (super.isActive(x, y)) {
				if (hasReached == false) {
					move(0, SPEED*delta);
					if (y >= destinationY) {
						hasReached = true;
						time += delta;
					}
				}
				if (hasReached == true)  {
					time += delta;
					if (time >= INTERVAL) {
						// Add an enemy shot and reset time.
						World.getInstance().addSprite(new EnemyShot(getX(), getY()));
						time = 0;
					}
				}
			}
			else {
				deactivate();
			}
		}
	}

}