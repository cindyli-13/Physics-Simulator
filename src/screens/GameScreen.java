package screens;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import main.Main;
import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;
import widgets.Button;
import widgets.GUIComponent;
import widgets.SimulationWindow;

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
	private Button menuButton;		// mButton
	
	private ArrayList<GUIComponent> guiComponents;
	private ArrayList<Button> buttons;
	
	private SimulationWindow simulation;
	
	// static variables
	private static String MENU_BUTTON_TEXTURE_FILE = "./res/MenuB.png";
	
	// constructor
	public GameScreen(long window, Loader loader, float screenWidth, float screenHeight, float z) {
		
		// ******** INITIAL STATES OF BUTTONS ********
		//
		// 	width		the button width
		// 	height		the button height
		// 	x			the x coordinate of the center of the button (in OpenGL world coordinates)
		// 	y			the y coordinate of the center of the button (in OpenGL world coordinates)
		
		float mbuttonWidth = 100f;
		float mbuttonHeight = 70f;	
		float mButtonX = -screenWidth/2 + mbuttonWidth/2;	// flush against left side of the screen
		float mButtonY = screenHeight/2 - mbuttonHeight/2;  // flush against top of the screen
		Vector3f mButtonPos = new Vector3f(mButtonX, mButtonY, z);
		
		float[] mButtonVertices = Entity.getVertices(mbuttonWidth, mbuttonHeight, z);
		
		// the following will be the same for each button
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
				
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
				
		// **************************************************
				
		// menu button
		int textureID = loader.loadTexture(MENU_BUTTON_TEXTURE_FILE);
		Model gButtonModel = loader.loadToVAO(mButtonVertices, texCoords, indices, textureID);
				
		menuButton = new Button(gButtonModel, mButtonPos, rotation, scale, mbuttonWidth, mbuttonHeight);
				
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(menuButton);
				
		// initialize button array list
		buttons = new ArrayList<Button>();
		buttons.add(menuButton);
		
		
		// simulation window
		simulation = new SimulationWindow(window, loader, screenWidth, screenHeight, z);
	}
	
	/**
	 * Renders the game screen.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		simulation.render(renderer);
		renderer.renderGUI(guiComponents);
	}
	
	/**
	 * Updates the game screen.
	 */
	public void update() {
		
		if (!simulation.isPause())
			simulation.update();
	}
	
	/**
	 * Contains the logic for input handling.
	 * 
	 * @param window
	 */
	public void input(Main main, long window, float screenWidth, float screenHeight) {
		
		// mouse input
		if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GL_TRUE) {
			
			// get cursor coordinate
			
			DoubleBuffer cursorPosX = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer cursorPosY = BufferUtils.createDoubleBuffer(1);
			
			glfwGetCursorPos(window, cursorPosX, cursorPosY);
			
			float x = (float) cursorPosX.get(0);
			float y = (float) cursorPosY.get(0);
			
			// convert cursor coordinate to OpenGL world coordinate
			x -= screenWidth/2;
			y *= -1f;
			y += screenHeight/2;
			
			mouseInput(main, x, y);
		}
	}
	
	/**
	 * Contains the logic for when a mouse is clicked.
	 * 
	 * @param x		the x coordinate of the cursor position
	 * @param y		the y coordinate of the cursor position
	 */
	public void mouseInput(Main main, float x, float y) {
		
		// loop through buttons array list
		for (Button button: buttons) {
					
			// check if this button was clicked
			if (button.getAabb().intersects(x, y)) {
						
				// check which button
				if (button.equals(menuButton)) {
					
					// pause simulation
					simulation.setPause(true);
					
					main.setCurrScreen(0);
				}
			}
			
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
