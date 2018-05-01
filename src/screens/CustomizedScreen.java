package screens;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import main.Main;
import objects.Circle;
import objects.Entity;
import objects.Loader;
import objects.Rectangle;
import renderEngine.Renderer;
import widgets.Button;
import widgets.SimulationWindow;
import widgets.Toolbar;

/**
 * This class implements the customized screen of the 
 * physics simulator.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Thursday, April 19th, 2018
 */
public class CustomizedScreen {

	// instance variables	
	private SimulationWindow simulation;
	private Toolbar toolbar;
	
	private float z;
	private boolean newEntityCreated;
	
	private long window;
	private float screenWidth;
	private float screenHeight;
		
	// constructor
	public CustomizedScreen(long window, Loader loader, float screenWidth, float screenHeight, float z) {
					
		// simulation window
		simulation = new SimulationWindow(window, loader, screenWidth, screenHeight, z);
		
		// toolbar
		toolbar = new Toolbar(loader, screenWidth, screenHeight, z);
		
		this.z = z;
		newEntityCreated = false;
		
		this.window = window;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
		
	/**
	 * Renders the customized screen.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		toolbar.render(renderer);
		simulation.render(renderer);
		
		if (newEntityCreated)
			moveNewObject();
	}
	
	/**
	 * Updates the game screen.
	 */
	public void update() {
		
		if (!simulation.isPause())
			simulation.update();
	}
	
	/**
	 * Contains the logic for input handling
	 * 
	 * @param main				where the main loop is
	 * @param key				the key that was pressed
	 * @param leftClick			whether the left mouse button was pressed
	 */
	public void input(Main main, int key, boolean leftClick) {
		
		// mouse input
		mouseInput(main, leftClick);
		
		// keyboard input
		keyboardInput(key);
	}
	
	/**
	 * Contains the logic for when a mouse is clicked.
	 * 
	 * @param main				where the main loop is
	 * @param leftClick			whether the left mouse button was pressed
	 */
	public void mouseInput(Main main, boolean leftClick) {
			
		// get cursor coordinate
		
		DoubleBuffer cursorPosX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer cursorPosY = BufferUtils.createDoubleBuffer(1);
		
		glfwGetCursorPos(window, cursorPosX, cursorPosY);
		
		float x = (float) cursorPosX.get(0);
		float y = (float) cursorPosY.get(0);
		
		
		// convert cursor coordinate to OpenGL world coordinate
		x -= screenWidth/2;
		y *= -1;
		y += screenHeight/2;
		
		// if left mouse button was pressed
		if (leftClick) {
			
			// clear new entity
			if (newEntityCreated) {
				
				placeNewEntity(simulation.getEntities().get(simulation.getEntities().size()-1));
				newEntityCreated = false;
			}
			
			else {
				
				// loop through buttons of toolbar
				for (Button button: toolbar.getButtons()) {
						
					// check if this button was clicked
					if (button.getAabb().intersects(x, y)) {
											
						// menu button
						if (button.equals(toolbar.getMenuButton())) {
								
							// pause simulation
							simulation.setPause(true);
											
							main.setCurrScreen(0);
						}
							
						// info button
						else if (button.equals(toolbar.getInfoButton())) {
								
								UserGuideScreen.showUserGuide();
						}
						
						// rectangle button
						else if (button.equals(toolbar.getRectangleButton())) {
								
							// generate random crate for now
							float sideLength = (float) Math.random() * 50 + 30;
							float posX = toolbar.getRectangleButton().getPosition().x;
							float posY = toolbar.getRectangleButton().getPosition().y;
							float mass = (float) Math.random() * 20 + 1;
							float e = -0.5f;
								
							simulation.createCrateEntity(sideLength, posX, posY, z, mass, e);
							
							newEntityCreated = true;
						}
						
						// circle button
						else if (button.equals(toolbar.getCircleButton())) {
								
							// generate random ball for now
							float radius = (float) Math.random() * 25 + 20;
							float posX = toolbar.getCircleButton().getPosition().x;
							float posY = toolbar.getCircleButton().getPosition().y;
							float mass = (float) Math.random() * 20 + 1;
							float e = -0.5f;
								
							simulation.createBallEntity(radius, posX, posY, z, mass, e);
							
							newEntityCreated = true;
						}
					}
						
				}
			}
			
		}
			
	}
	
	/**
	 * Contains the logic for when a key is pressed.
	 * 
	 * @param key  the key that was pressed
	 */
	public void keyboardInput(int key) {
		
		// space bar
		if(key == Main.KEY_SPACE) {
			simulation.setPause(!simulation.isPause());
		}
	}

	/**
	 * Returns the game screen's simulation window.
	 * 
	 * @return simulation
	 */
	public SimulationWindow getSimulationWindow() {
		return simulation;
	}
	
	/**
	 * Moves the newly created entity following the position of the cursor.
	 */
	public void moveNewObject() {
	
		// get cursor coordinate
		
		DoubleBuffer cursorPosX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer cursorPosY = BufferUtils.createDoubleBuffer(1);
		
		glfwGetCursorPos(window, cursorPosX, cursorPosY);
				
		float x = (float) cursorPosX.get(0);
		float y = (float) cursorPosY.get(0);
				
				
		// convert cursor coordinate to OpenGL world coordinate
		x -= screenWidth/2;
		y *= -1;
		y += screenHeight/2;
		
		// set position of entity
		Entity e = simulation.getEntities().get(simulation.getEntities().size()-1);
		e.setPosition(new Vector3f(x,y, e.getPosition().z));
				
		// update AABB if entity is a rectangle
		if (e instanceof Rectangle) {
			Rectangle r = (Rectangle) e;
					
			r.getAabb().setMin(new Vector2f(r.getPosition().x - r.getWidth()/2, r.getPosition().y - r.getHeight()/2)); 
			r.getAabb().setMax(new Vector2f(r.getPosition().x + r.getWidth()/2, r.getPosition().y + r.getHeight()/2));
		}
			
	}
	
	/**
	 * Checks whether or not a newly created entity can 
	 * be placed at the location of the cursor.
	 * 
	 * @param entity		the newly created entity
	 * @return true if yes, false otherwise
	 */
	public void placeNewEntity(Entity newEntity) {
		
		// if entity is a rectangle
		if (newEntity instanceof Rectangle) {
			
			Rectangle r = (Rectangle) newEntity;
			
			// horizontal check
			if (r.getAabb().getMin().x < simulation.getMin().x || 
					r.getAabb().getMax().x > simulation.getMax().x) {
				
				simulation.getEntities().remove(simulation.getEntities().size()-1);
				return;
			}
			
			// vertical check
			if (r.getAabb().getMin().y < simulation.getMin().y || 
					r.getAabb().getMax().y > simulation.getMax().y) {
				
				simulation.getEntities().remove(simulation.getEntities().size()-1);
				return;
			}
		}
		
		// if entity is circle
		else if (newEntity instanceof Circle) {
			
			Circle c = (Circle) newEntity;
			
			// horizontal check
			if (c.getPosition().x - c.getRadius() < simulation.getMin().x || 
					c.getPosition().x + c.getRadius() > simulation.getMax().x) {
				
				simulation.getEntities().remove(simulation.getEntities().size()-1);
				return;
			}
			
			// vertical check
			if (c.getPosition().y - c.getRadius() < simulation.getMin().y || 
					c.getPosition().y + c.getRadius() > simulation.getMax().y) {
				
				simulation.getEntities().remove(simulation.getEntities().size()-1);
				return;
			}
		}
		
		// loop through entities
		for(Entity entity : simulation.getEntities()) {
			
			// check that the entities to check collision for are not the same entity
			if (!newEntity.equals(entity)) {
				
				if (newEntity.intersects(entity)) {
					
					simulation.getEntities().remove(simulation.getEntities().size()-1);
					return;
				}
			}
		}
		
	}
}
