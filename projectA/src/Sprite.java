import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.BoundingBox;

/**
 * Sprite class for Sprite objects
 * Creates, renders and controls objects.
 */
public abstract class Sprite {

	private Image image = null;
	private float x;
	private float y;
	private BoundingBox bb;
	private boolean active = true;
	
	/** Construct a Sprite object
	 * @param imageSrc String object
     * @param x float value
     * @param y float value
     */
	public Sprite(String imageSrc, float x, float y) {
		try {
			image = new Image(imageSrc);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		this.x = x;
		this.y = y;
		
		bb = new BoundingBox(image, x, y);
	}
	
	/** Checks whether the object is on the screen.
	 * @return boolean value
     */
	public boolean onScreen() {
		return x >= 0 && x <= App.SCREEN_WIDTH - bb.getWidth()
			&& y >= 0 && y <= App.SCREEN_HEIGHT - bb.getHeight();
	}
	
	/** Clamp objects to the screen
     */
	public void clampToScreen() {
		x = Math.max(x, 0);
		x = Math.min(x, App.SCREEN_WIDTH);
		y = Math.max(y, 0);
		y = Math.min(y, App.SCREEN_HEIGHT);
	}
	
	/** Update the game state for a frame.
     * @param input user input object.
     * @param delta Time passed since last frame (milliseconds).
     */
	public abstract void update(Input input, int delta);
	
	/** Render the entire screen, so it reflects the current game state.
     */
	public void render() {
		image.drawCentered(x, y);
	}
	
	/** Handle the collisions of Sprite objects
     * @param other Sprite object
     */
	public void contactSprite(Sprite other) {
	}
	
	/** Get the x value in this object
     * @return x float value
     */
	public float getX() { return x; }
	
	/** Get the y value in this object
     * @return y float value
     */
	public float getY() { return y; }
	
	/** Set its argument float value to the x value in this object.
     * @param x float value
     */
	public void setX(float x) { this.x = x; }
	
	/** Set its argument float value to the y value in this object.
     * @param y float value
     */
	public void setY(float y) { this.y = y; }
	
	/** Get the active value in this object
     * @return active boolean value
     */
	public boolean getActive() { return active; }
	
	/** Deactivate this object
     */
	public void deactivate() { active = false; }
	
	/** Get the BoundingBox object in this object
     * @return bb BoundingBox object
     */
	public BoundingBox getBoundingBox() {
		return new BoundingBox(bb);
	}
	
	/** Move this object
     * @param dx float value
     * @param dy float value
     */
	public void move(float dx, float dy) {
		x += dx;
		y += dy;
		bb.setX(x);
		bb.setY(y);
	}

}