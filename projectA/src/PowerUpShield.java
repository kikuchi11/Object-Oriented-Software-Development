/**
 * PowerUpShield class, which extends PowerUp and Sprite objects
 */
public class PowerUpShield extends PowerUp {

	private static final String POWERUP_SHIELD_PATH = "res/shield-powerup.png";
	
	/** Construct a PowerUpShield object
     * @param x float value
     * @param y float value
     */
	public PowerUpShield(float x, float y) {
		super(POWERUP_SHIELD_PATH, x ,y);
	}
}