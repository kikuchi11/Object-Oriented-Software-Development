import java.util.ArrayList;

import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2018
 * by Eleanor McMurtry and Takuhiro Kikuchi (900550), University of Melbourne
 */

/**
 * World class for the game
 * Creates, renders and controls objects.
 */
public class World {
	private static final String BACKGROUND_PATH = "res/space.png";
	private static final float BACKGROUND_SCROLL_SPEED = 0.2f;
	private static final char HASH = '#';
	private float backgroundOffset = 0;
	private Image background;
	private Image shield;
	private static final String SHIELD_PATH = "res/shield.png";
	private static final int lifeX = 20;
	private static final int lifeY = 696;
	private static final int nextLife = 40;
	private static final int scoreX = 20;
	private static final int scoreY = 738;
	private static int score;
	private static final int initScore = 0;
	private static final String scoreText = "Score: ";
	private static final String LIVES_PATH = "res/lives.png";
	private Image livesImage;
	private static World world;
	private static final String ENEMY_CREATION = "res/waves.txt";
	private static final int FIRST = 0;
	private static final int SECOND = 1;
	private static final int THIRD = 2;
	private static final int DROP = 0;
	private static int time = 0;
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private ArrayList<Sprite> powerups = new ArrayList<Sprite>();
	
	
	/** Get instance of world class
	 * @return the instance of world class
     */
	public static World getInstance() {
		if (world == null) {
			try {
				world = new World();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		return world;
	}
	
	/** Add a sprite object to sprites, which is an arraylist.
     * @param sprite sprite object.
     */
	public void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}
	
	/** Add a powerup object to powerups, which is an arraylist.
     * @param sprite sprite object.
     */
	public void addPowerup(Sprite sprite) {
		powerups.add(sprite);
	}
	
	/** Construct a World object.
     */
	public World() throws FileNotFoundException, IOException, SlickException {
		try {
			background = new Image(BACKGROUND_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		try {
			livesImage = new Image(LIVES_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		try {
			shield = new Image(SHIELD_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		// Initialise the score
		score = initScore;
		
		// Create enemies
		readFile();
		
		// Create the player
		sprites.add(new Player());
		//score = initScore;
		world = this;
	}
	
	/** Update the game state for a frame.
     * @param input user input object.
     * @param delta Time passed since last frame (milliseconds).
     */
	public void update(Input input, int delta) {
		// sprites.add(new PowerUpShield(100,0));
		if (input.isKeyDown(Input.KEY_S)) {
			delta *= 5;
		}
		// Update all sprites
		for (int i = 0; i < sprites.size(); ++i) {
			sprites.get(i).update(input, delta);
		}
		
		// Update all powerups
		for (int i = 0; i < powerups.size(); ++i) {
			powerups.get(i).update(input, delta);
		}
		
		// Handle collisions
		for (Sprite sprite : sprites) {
			for (Sprite other : sprites) {
				if (sprite != other && sprite.getBoundingBox().intersects(other.getBoundingBox())) {
					sprite.contactSprite(other);
				}
			}
		}
		
		// Handle collisions powerups
		for (Sprite powerup : powerups) {
			for (Sprite sprite : sprites) {
				if (sprite != powerup && sprite.getBoundingBox().intersects(powerup.getBoundingBox())) {
					sprite.contactSprite(powerup);
				}
			}
		}
		
		// Clean up inactive sprites
		for (int i = 0; i < sprites.size(); ++i) {
			if (sprites.get(i).getActive() == false) {
				sprites.remove(i);
				// decrement counter to make sure we don't miss any
				--i;
			}
		}
		
		// Clean up inactive powerups
		for (int i = 0; i < powerups.size(); ++i) {
			if (powerups.get(i).getActive() == false) {
				powerups.remove(i);
				// decrement counter to make sure we don't miss any
				--i;
			}
		}
		
		// Update time and background
		time += delta;
		backgroundOffset += BACKGROUND_SCROLL_SPEED * delta;
		backgroundOffset = backgroundOffset % background.getHeight();
	}
	
	/** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
	public void render(Graphics g) {
		// Tile the background image
		for (int i = 0; i < App.SCREEN_WIDTH; i += background.getWidth()) {
			for (int j = -background.getHeight() + (int)backgroundOffset; j < App.SCREEN_HEIGHT; j += background.getHeight()) {
				background.draw(i, j);
			}
		}
		// Draw all sprites
		for (Sprite sprite : sprites) {
			sprite.render();
			if (sprite instanceof Player) {
				// Draw lives
				int lives = ((Player) sprite).getLives();
				int offSet = 0;
				for (int i = 0; lives > i; i++) {
					livesImage.draw(lifeX+offSet, lifeY);
					offSet += nextLife;
				}
				// Draw shield
				if (((Player) sprite).getShield()) {
					shield.drawCentered(sprite.getX(), sprite.getY());
				}
				// Draw powerupshield
				if (((Player) sprite).getPowerupShield()) {
					shield.drawCentered(sprite.getX(), sprite.getY());
				}
			}
		}
		
		// Draw all powerups
		for (Sprite powerup : powerups) {
			powerup.render();
		}
		
		// Draw score
		String renderedText = scoreText + Integer.toString(score);
		g.drawString(renderedText, scoreX, scoreY);
		
	}
	
	/** Set its argument to the score variable in this class.
     * @param score integer value 
     */
	public static void setScore(int score) {
		World.score = score;
	}
	
	/** Get score
     * @return the score variable in this class
     */
	public static int getScore() {
		return score;
	}
	
	/* Create enemies
     * @param enemyType string value
     * @param x float value
     * @param delay integer value
     */
	private void createEnemies(String enemyType, float x, int delay) {
		if (enemyType.equals("BasicEnemy")) {
			sprites.add(new BasicEnemy(x, delay));
		}
		else if (enemyType.equals("SineEnemy")) {
			sprites.add(new SineEnemy(x, delay));
		}
		else if (enemyType.equals("BasicShooter")) {
			sprites.add(new BasicShooter(x, delay));
		}
		else if (enemyType.equals("Boss")) {
			sprites.add(new Boss(x, delay));
		}
	}
	
	/* Read a text file and create enemies based on the information in the text file.
     */
	private void readFile() throws FileNotFoundException, IOException, SlickException {
		BufferedReader br = new BufferedReader(new FileReader(new File(ENEMY_CREATION)));
		try {
			String txt;
			String enemyType;
			float x;
			int delay;
			while ((txt = br.readLine())!= null) {
				String information[] = txt.split(",");
				if (information[FIRST].charAt(FIRST) != HASH) {
					enemyType = information[FIRST];
					x = Float.parseFloat(information[SECOND]);
					delay = (int) Long.parseLong(information[THIRD]);
					createEnemies(enemyType, x, delay);
				}
			}
		} finally {
			br.close();
		}
	}

	/** Get time
	 * @return time variable in this class
     */
	public static int getTime() {
		return time;
	}
	
	/** Decide whether to drop a powerup object at 5% chance
     * @return a boolean value representing whether to drop
     */
	public boolean toDrop() {
		// Generate a random number from 0 to 19
		Random rand = new Random();
		int randomNumber = rand.nextInt(20);
		return randomNumber == DROP;
	}
	
	/** Decide the type of an powerup object.
	 * @return an integer value representing either shield or shotspeed.
     */
	public int dropType() {
		// returns either 0 or 1, which represent shield and shot speed respectively
		Random rand = new Random();
		int randomNumber = rand.nextInt(2);
		return randomNumber;
	}
}
