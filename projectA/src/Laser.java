import org.newdawn.slick.Input;

/**
 * Laser class, which extends Sprite
 */
public abstract class Laser extends Sprite {
	
	/** Construct an Laser object
	 * @param imageSrc String object
     * @param x float value
     * @param y float value
     */
	public Laser(String imageSrc, float x, float y) {
		super(imageSrc, x, y);
	}
	
	@Override
	public abstract void update(Input input, int delta);

}