package screens;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
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

/**
 * This class implements the menu screen of the 
 * physics simulator.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Thursday, April 19th, 2018
 */
public class MenuScreen {

	// instance variables
	private Button gameButton;			// gButton
	private Button lessonButton;		// lButton
	private Button customizedButton;    // cButton
	private Button helpButton;			// hButton
	
	private GUIComponent background;
	
	private ArrayList<GUIComponent> guiComponents;
	private ArrayList<Button> buttons;
	
	// static variables
	public static String GAME_BUTTON_TEXTURE_FILE = "./res/GameB.png";
	public static String LESSON_BUTTON_TEXTURE_FILE = "./res/LessonB.png";
	public static String CUSTOMIZED_BUTTON_TEXTURE_FILE = "./res/CustomB.png";
	public static String HELP_BUTTON_TEXTURE_FILE = "./res/userGuide.png";
	public static String TITLE_SCREEN_TEXTURE_FILE = "./res/titleScreen.png";
	
	// constructor
	public MenuScreen(Loader loader, float screenWidth, float screenHeight, float z) {
		
		// ******** INITIAL STATES OF BUTTONS ********
		//
		// 	width		the button width
		// 	height		the button height
		// 	x			the x coordinate of the center of the button (in OpenGL world coordinates)
		// 	y			the y coordinate of the center of the button (in OpenGL world coordinates)
		
		float buttonWidth = 220f;
		float buttonHeight = 60f;
		
		float gButtonX = -screenWidth/2 + buttonWidth/2 + 50f;  // 50 pixels from the left side of the screen
		float gButtonY = screenHeight/2 - 120f;  // 120 pixels down from the top of the screen
		Vector3f gButtonPos = new Vector3f(gButtonX, gButtonY, z);
		
		float lButtonX = gButtonX;
		float lButtonY = gButtonY - buttonHeight - 20f;  // 20 pixels down from gButton
		Vector3f lButtonPos = new Vector3f(lButtonX, lButtonY, z);
		
		float cButtonX = gButtonX;
		float cButtonY = lButtonY - buttonHeight - 20f;  // 20 pixels down from lButton
		Vector3f cButtonPos = new Vector3f(cButtonX, cButtonY, z);
		
		float hButtonX = gButtonX;
		float hButtonY = cButtonY - buttonHeight - 70f;  // 70 pixels down from cButton
		Vector3f hButtonPos = new Vector3f(hButtonX, hButtonY, z);
		
		
		// the following will be the same for each button
		float[] vertices = Entity.getVertices(buttonWidth, buttonHeight, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		// **************************************************
		
		// game button
		int textureID = loader.loadTexture(GAME_BUTTON_TEXTURE_FILE);
		Model gButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		gameButton = new Button(gButtonModel, gButtonPos, rotation, scale, buttonWidth, buttonHeight);
		
		// lesson button
		textureID = loader.loadTexture(LESSON_BUTTON_TEXTURE_FILE);
		Model lButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		lessonButton = new Button(lButtonModel, lButtonPos, rotation, scale, buttonWidth, buttonHeight);
		
		// customized button
		textureID = loader.loadTexture(CUSTOMIZED_BUTTON_TEXTURE_FILE);
		Model cButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		customizedButton = new Button(cButtonModel, cButtonPos, rotation, scale, buttonWidth, buttonHeight);
				
		// help button
		textureID = loader.loadTexture(HELP_BUTTON_TEXTURE_FILE);
		Model hButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		helpButton = new Button(hButtonModel, hButtonPos, rotation, scale, buttonWidth, buttonHeight);
		
		// **************************************************
		
		// background
		
		float backgroundWidth = screenWidth;
		float backgroundHeight = screenHeight;
		
		vertices = Entity.getVertices(backgroundWidth, backgroundHeight, z - 100f);
		
		textureID = loader.loadTexture(TITLE_SCREEN_TEXTURE_FILE);
		Model backgroundModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		background = new GUIComponent(backgroundModel, new Vector3f(0,0,0), rotation,scale);
		
		// **************************************************
		
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(gameButton);
		guiComponents.add(lessonButton);
		guiComponents.add(customizedButton);
		guiComponents.add(helpButton);
		guiComponents.add(background);
		
		// initialize button array list
		buttons = new ArrayList<Button>();
		buttons.add(gameButton);
		buttons.add(lessonButton);
		buttons.add(customizedButton);
		buttons.add(helpButton);
	}
	
	/**
	 * Renders the menu screen.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		renderer.renderGUI(guiComponents);
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
				if (button.equals(gameButton))
					main.setCurrScreen(1);
				
				else if (button.equals(lessonButton))
					main.setCurrScreen(2);
				
				else if (button.equals(customizedButton))
					main.setCurrScreen(3);
				
				else if (button.equals(helpButton))
					UserGuideScreen.showUserGuide();
			}
			
		}
		
	}
	
}
