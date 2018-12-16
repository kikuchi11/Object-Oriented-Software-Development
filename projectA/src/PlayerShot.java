import org.newdawn.slick.Input;

/**
 * PlayerShot class, which extends Laser and Sprite class.
 */
public class PlayerShot extends Laser {

	private static final String PLAYERSHOT_PATH = "res/shot.png";
	private static final float SHOT_SPEED = -3f;
	
	/** Construct a PlayerShot object
     * @param x float value
     * @param y float value
     */
	public PlayerShot(float x, float y) {
		super(PLAYERSHOT_PATH, x, y);
	}

	@Override
	public void update(Input input, int delta) {
		move(0, SHOT_SPEED);
		if (!onScreen()) {
			deactivate();
		}
	}
	
}