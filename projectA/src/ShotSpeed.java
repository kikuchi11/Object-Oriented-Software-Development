
/**
 * ShotSpeed class which exnteds PowerUp and Sprite
 * Gives effects to a Player Object
 */
public class ShotSpeed extends PowerUp {
	
	private static final String SHOTSPEED_PATH = "res/shotspeed-powerup.png";
	
	/** Construct a ShotSpeed object
     * @param x float value
     * @param y float value
     */
	public ShotSpeed(float x, float y) {
		super(SHOTSPEED_PATH, x, y);
	}

}