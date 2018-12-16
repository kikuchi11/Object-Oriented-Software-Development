
import org.newdawn.slick.Input;

/**
 * PowerUp class, which extends Sprite class
 */
public abstract class PowerUp extends Sprite {

	private static final float SPEED = 0.1f;

	/** Construct a PowerUp object
	 * @param imageSrc String object
     * @param x float value
     * @param y float value
     */
	public PowerUp(String imageSrc, float x, float y) {
		super(imageSrc, x, y);
	}
	
	@Override
	public void update(Input input, int delta) {
		move(0, SPEED*delta);
		if (!onScreen()) {
			deactivate();
		}
	}
}