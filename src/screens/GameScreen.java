package screens;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

import main.Main;
import objects.Loader;
import renderEngine.Renderer;
import widgets.Button;
import widgets.SimulationWindow;
import widgets.Toolbar;

/**
 * This class implements the game screen of the 
 * physics simulator.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Thursday, April 19th, 2018
 */
public class GameScreen {

	// instance variables	
	private SimulationWindow simulation;
	private Toolbar toolbar;
	
	private float z;
	
	// constructor
	public GameScreen(long window, Loader loader, float screenWidth, float screenHeight, float z) {
		
		// simulation window
		simulation = new SimulationWindow(window, loader, screenWidth, screenHeight, z);
				
		// toolbar
		toolbar = new Toolbar(loader, screenWidth, screenHeight, z);
				
		this.z = z;
	}
	
	/**
	 * Renders the game screen.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		toolbar.render(renderer);
		simulation.render(renderer);
	}
	
	/**
	 * Updates the game screen.
	 */
	public void update() {
		
		if (!simulation.isPaused())
			simulation.update();
	}
	
	/**
	 * Contains the logic for input handling
	 * 
	 * @param main				where the main loop is
	 * @param window			the window
	 * @param screenWidth		the screen width
	 * @param screenHeight		the screen height
	 * @param key				the key that was pressed
	 * @param leftClick			whether the left mouse button was pressed
	 */
	public void input(Main main, long window, float screenWidth, float screenHeight, int key, 
			boolean leftClick) {
		
		// mouse input
		mouseInput(main, window, screenWidth, screenHeight, leftClick);
		
		// keyboard input
		keyboardInput(key);
	}
	
	/**
	 * Contains the logic for when a mouse is clicked.
	 * 
	 * @param main				where the main loop is
	 * @param window			the window
	 * @param screenWidth		the screen width
	 * @param screenHeight		the screen height
	 * @param leftClick			whether the left mouse button was pressed
	 */
	public void mouseInput(Main main, long window, float screenWidth, float screenHeight, boolean leftClick) {
			
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
						float posX = (float) Math.random() * 650 - 230;
						float posY = (float) Math.random() * 150;
						float mass = (float) Math.random() * 20 + 1;
						float e = -0.5f;
							
						simulation.createCrateEntity(sideLength, posX, posY, z, mass, e);
					}
						
					// circle button
					else if (button.equals(toolbar.getCircleButton())) {
							
						// generate random ball for now
						float radius = (float) Math.random() * 25 + 20;
						float posX = (float) Math.random() * 650 - 230;
						float posY = (float) Math.random() * 150;
						float mass = (float) Math.random() * 20 + 1;
						float e = -0.5f;
							
						simulation.createBallEntity(radius, posX, posY, z, mass, e);
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
			simulation.setPause(!simulation.isPaused());
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
	
}
