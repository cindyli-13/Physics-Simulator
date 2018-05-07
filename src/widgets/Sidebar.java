package widgets;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;

/**
 * This class contains the components of the sidebar.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Sunday, May 6th, 2018
 */
public class Sidebar {

	// instance variables
	private ArrayList<Button> buttons;
	private ArrayList<GUIComponent> guiComponents;
	private ArrayList<String> simulationsData;
	
	private Model simulationButtonModel;
	private Model createNewSimulationButtonModel;
	
	private Button createNewSimulationButton;
	
	private float buttonWidth;
	private float buttonHeight;

	// static variables
	private static String CREATE_NEW_SIMULATION_BUTTON_TEXTURE_FILE = "./res/New.png";
	private static String SIMULATION_BUTTON_TEXTURE_FILE = "./res/Cust.png";
		
	// constructor
	public Sidebar(Loader loader, float screenWidth, float screenHeight, float z, String[] files) {
		
		// ******** INITIAL STATES OF BUTTONS ********
		//
		// 	width		the button width
		// 	height		the button height
		// 	x			the x coordinate of the center of the button (in OpenGL world coordinates)
		// 	y			the y coordinate of the center of the button (in OpenGL world coordinates)
			
		// the following will be the same for each button
		this.buttonWidth = 70f;
		this.buttonHeight = 70f;
		
		float[] vertices = Entity.getVertices(buttonWidth, buttonHeight, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		
		// **************************************************
		
		
		// create new simulation button model
		int textureID = loader.loadTexture(CREATE_NEW_SIMULATION_BUTTON_TEXTURE_FILE);
		createNewSimulationButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		// simulation button model
		textureID = loader.loadTexture(SIMULATION_BUTTON_TEXTURE_FILE);
		simulationButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		// **************************************************
		
		
		// initialize buttons array list
		buttons = new ArrayList<Button>();
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		
		// initialize simulations data array list
		simulationsData = new ArrayList<String>();
		
		// **************************************************
		
		
		// create new simulation button
		
		float buttonX = -450f;
		float buttonY = 210f - simulationsData.size() * buttonHeight;
				
		Vector3f position = new Vector3f(buttonX, buttonY, z);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
				
		createNewSimulationButton = new Button(createNewSimulationButtonModel, position, rotation, scale, 
				buttonWidth, buttonHeight); 
				
		buttons.add(createNewSimulationButton);
		guiComponents.add(createNewSimulationButton);
		
		// **************************************************
		
		
		// create simulation buttons
		for (String filename: files) {
			
			simulationsData.add(filename);
			createSimulationButton(z);
		}
	}
	
	/**
	 * Creates a simulation button.
	 * 
	 * @param buttonWidth
	 * @param buttonHeight
	 * @param z		the z coordinate of the button
	 * @return the simulation button
	 */
	public void createSimulationButton(float z) {
		
		float buttonX = -450f;
		float buttonY = 210f - (simulationsData.size() - 1) * buttonHeight;
		
		Vector3f position = new Vector3f(buttonX, buttonY, z);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		Button button = new Button(simulationButtonModel, position, rotation, scale, buttonWidth, buttonHeight); 
		
		// add button to array list
		buttons.add(button);
		guiComponents.add(button);
	}
	
	/**
	 * Renders the objects of the sidebar.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		renderer.renderGUI(guiComponents);
	}

	/**
	 * Returns the array list of buttons of the sidebar.
	 * 
	 * @return buttons
	 */
	public ArrayList<Button> getButtons() {
		return buttons;
	}
	
	/**
	 * Returns the array list of GUI components of the sidebar.
	 * 
	 * @return guiComponents
	 */
	public ArrayList<GUIComponent> getGUIComponents() {
		return guiComponents;
	}
	
	/**
	 * Returns the array list of simulations data.
	 * 
	 * @return simulationsData
	 */
	public ArrayList<String> getSimulationsData() {
		return simulationsData;
	}

	/**
	 * Returns the create new simulation button.
	 * 
	 * @return createNewSimulationButton
	 */
	public Button getCreateNewSimulationButton() {
		return createNewSimulationButton;
	}
}
