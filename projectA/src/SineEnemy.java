import org.newdawn.slick.Input;

/**
 * SineEnemy class, which extends Enemy and Sprite class.
 */
public class SineEnemy extends Enemy {

	private static final int POINTS = 100;
	private static final String SINEENEMY_PATH = "res/sine-enemy.png";
	private static final int PERIOD = 1500;
	private static final int AMPLITUDE = 96;
	private static final float SPEED = 0.15f;
	private static final int HITPOINTS = 1;
	private final float initX;

	/** Construct a SineEnemy object
     * @param x float value
     * @param delay integer value
     */
	public SineEnemy(float x, int delay) {
		super(SINEENEMY_PATH, x);
		super.setPoints(POINTS);
		super.setHitpoints(HITPOINTS);
		super.setDelay(delay);
		initX = x;
	}

	@Override
	public void update(Input input, int delta) {
		if (World.getTime() > super.getDelay()) {
			float x = super.getX();
			float y = super.getY();
			if (super.isActive(x, y)) {
				setX(initX+calculateOffSet());
				move(0, SPEED*delta);
			}
			else {
				deactivate();
			}
		}
	}
	
	/* Calculate the offset for the next x coordinate of this object
	 * @return offset float value
     */
	private float calculateOffSet() {
		return (float) (AMPLITUDE * Math.sin(((2 * Math.PI) / PERIOD) * (World.getTime() - super.getDelay())));
	}
	
}
