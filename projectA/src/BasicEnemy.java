import org.newdawn.slick.Input;

/**
 * BasicEnemy class, which extends Enemy and Sprite
 */
public class BasicEnemy extends Enemy {

	private static final int POINTS = 50;
	private static final int HITPOINTS = 1;
	private static float SPEED = 0.2f;
	private static String BASICENEMY_PATH = "res/basic-enemy.png";
	
	/** Construct a BasicEnemy object
     * @param x float value
     * @param y float value
     */
	public BasicEnemy(float x, int delay) {
		super(BASICENEMY_PATH, x);
		super.setPoints(POINTS);
		super.setHitpoints(HITPOINTS);
		super.setDelay(delay);
	}
	
	@Override
	public void update(Input input, int delta) {
		// Start moving when the time since the game started becomes larger than the delay value.
		if (World.getTime() > super.getDelay()) {
			float y = super.getY();
			float x = super.getX();
			if (super.isActive(x, y)) {
				move(0, SPEED*delta);
			}
			else {
				deactivate();
			}
		}
	}

}