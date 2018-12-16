import org.newdawn.slick.Input;

/**
 * EnemyShot class, which extends Laser and Sprite classes
 */
public class EnemyShot extends Laser {

	private static final String ENEMYSHOT_PATH = "res/enemy-shot.png";
	private static final float SHOT_SPEED = 0.7f;

	/** Construct an EnemyShot object
	 * @param x float value
     * @param y float value
     */
	public EnemyShot(float x, float y) {
		super(ENEMYSHOT_PATH, x, y);
	}
	
	@Override
	public void update(Input input, int delta) {
		move(0, SHOT_SPEED*delta);
		if (!onScreen()) {
			deactivate();
		}
	}

}