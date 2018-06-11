package widgets;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;
import widgets.Button;
import widgets.GUIComponent;

/**
 * This class contains the components of the toolbar.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Wednesday, April 25th, 2018
 */
public class Toolbar {

	// instance variables
	private ArrayList<GUIComponent> guiComponents;
	private ArrayList<Button> buttons;

	private Button menuButton;			// mButton
	private Button infoButton;			// iButton
	private Button rectangleButton;		// rButton
	private Button circleButton;		// cButton
	private Button cannonButton;		// caButton

	// static variables
	public static String MENU_BUTTON_TEXTURE_FILE = "./res/MenuB.png";
	public static String INFO_BUTTON_TEXTURE_FILE = "./res/InfoB.png";
	public static String RECT_BUTTON_TEXTURE_FILE = "./res/Rect.png";
	public static String CIRC_BUTTON_TEXTURE_FILE = "./res/Circ.png";
	public static String CANNON_BUTTON_TEXTURE_FILE="./res/CannonB.png";

	/**
	 * Creates a toolbar.
	 * 
	 * @param loader
	 * @param z
	 */
	public  Toolbar(Loader loader, float z) {
		
		// ******** INITIAL STATES OF BUTTONS ********
		//
		// 	width		the button width
		// 	height		the button height
		// 	x			the x coordinate of the center of the button (in OpenGL world coordinates)
		// 	y			the y coordinate of the center of the button (in OpenGL world coordinates)
			
		// the following will be the same for each button
		float buttonY = 265;
		
		float buttonWidth = 70f;
		float buttonHeight = 50f;
		
		float[] vertices = Entity.getVertices(buttonWidth, buttonHeight, z - 100f);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
				
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		
		float mButtonX = -445;
		Vector3f mButtonPos = new Vector3f(mButtonX, buttonY, z - 100f);
		
		float iButtonX = mButtonX + buttonWidth + 2f;
		Vector3f iButtonPos = new Vector3f(iButtonX, buttonY, z - 100f);
		
		float rButtonX = iButtonX + buttonWidth + 2f;
		Vector3f rButtonPos = new Vector3f(rButtonX, buttonY, z - 100f);
		
		float cButtonX = rButtonX + buttonWidth + 2f;
		Vector3f cButtonPos = new Vector3f(cButtonX, buttonY, z - 100f);
		
		float caButtonX = cButtonX + buttonWidth + 2f;
		Vector3f caButtonPos = new Vector3f(caButtonX, buttonY, z - 100f);
		
		// **************************************************
		
		// menu button
		int textureID = loader.loadTexture(MENU_BUTTON_TEXTURE_FILE);
		Model mButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		menuButton = new Button(mButtonModel, mButtonPos, rotation, scale, buttonWidth, buttonHeight);
		
		// info button
		textureID = loader.loadTexture(INFO_BUTTON_TEXTURE_FILE);
		Model iButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		infoButton = new Button(iButtonModel, iButtonPos, rotation, scale, buttonWidth, buttonHeight);
		
		// rectangle button
		textureID = loader.loadTexture(RECT_BUTTON_TEXTURE_FILE);
		Model rButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		rectangleButton = new Button(rButtonModel, rButtonPos, rotation, scale, buttonWidth, buttonHeight);
		
		// circle button
		textureID = loader.loadTexture(CIRC_BUTTON_TEXTURE_FILE);
		Model cButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		circleButton = new Button(cButtonModel, cButtonPos, rotation, scale, buttonWidth, buttonHeight);
		
		// cannon button
		textureID = loader.loadTexture(CANNON_BUTTON_TEXTURE_FILE);
		
		Model caButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		cannonButton = new Button(caButtonModel, caButtonPos, rotation, scale, buttonWidth, buttonHeight);
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(menuButton);
		guiComponents.add(infoButton);
		guiComponents.add(rectangleButton);
		guiComponents.add(circleButton);
		guiComponents.add(cannonButton);
		
		// initialize button array list
		buttons = new ArrayList<Button>();
		buttons.add(menuButton);
		buttons.add(infoButton);
		buttons.add(rectangleButton);
		buttons.add(circleButton);
		buttons.add(cannonButton);
	}
	
	/**
	 * Renders the objects of the simulation window.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		renderer.renderGUI(guiComponents);
	}

	/**
	 * Returns the array list of buttons of the toolbar.
	 * 
	 * @return buttons
	 */
	public ArrayList<Button> getButtons() {
		return buttons;
	}

	/**
	 * Returns the menu button.
	 * 
	 * @return menuButton
	 */
	public Button getMenuButton() {
		return menuButton;
	}

	/**
	 * Returns the info button.
	 * 
	 * @return infoButton
	 */
	public Button getInfoButton() {
		return infoButton;
	}

	/**
	 * Returns the rectangle button.
	 * 
	 * @return rectangleButton
	 */
	public Button getRectangleButton() {
		return rectangleButton;
	}

	/**
	 * Returns the circle button.
	 * 
	 * @return circleButton
	 */
	public Button getCircleButton() {
		return circleButton;
	}
	
	/**
	 * Returns the cannon button.
	 * 
	 * @return cannonButton
	 */
	public Button getCannonButton() {
		return cannonButton;
	}
	
}
