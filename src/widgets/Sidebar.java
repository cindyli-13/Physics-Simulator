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
	private Model upButtonEnabledModel;
	private Model upButtonDisabledModel;
	private Model downButtonEnabledModel;
	private Model downButtonDisabledModel;
	
	private Button createNewSimulationButton;
	private Button downButton;
	private Button upButton;
	
	private GUIComponent sidebarPanel;
	
	private float buttonWidth;
	private float buttonHeight;
	
	private float sidebarWidth;
	private float sidebarHeight;
	
	private float x;
	private float y;
	private float z;
	
	private int topSimulationIndex;
	
	private boolean canCreateNewSimulation;

	// static variables
	private static String CREATE_NEW_SIMULATION_BUTTON_TEXTURE_FILE = "./res/createNewSimulationButton.png";
	private static String SIMULATION_BUTTON_TEXTURE_FILE = "./res/simulationButton.png";
	private static String SIDEBAR_PANEL_TEXTURE_FILE = "./res/sidebarPanel.png";
	private static String UP_BUTTON_ENABLED_TEXTURE_FILE = "./res/upButtonEnabled.png";
	private static String UP_BUTTON_DISABLED_TEXTURE_FILE = "./res/upButtonDisabled.png";
	private static String DOWN_BUTTON_ENABLED_TEXTURE_FILE = "./res/downButtonEnabled.png";
	private static String DOWN_BUTTON_DISABLED_TEXTURE_FILE = "./res/downButtonDisabled.png";
	
	// constructor
	public Sidebar(Loader loader, float screenWidth, float screenHeight, float z, String[] files, 
			boolean canCreateNewSimulation) {
		
		// ******** INITIAL STATES OF BUTTONS ********
		//
		// 	width		the button width
		// 	height		the button height
		// 	x			the x coordinate of the center of the button (in OpenGL world coordinates)
		// 	y			the y coordinate of the center of the button (in OpenGL world coordinates)
			
		// the following will be the same for each button
		this.buttonWidth = 100f;
		this.buttonHeight = 100f;
		this.x = -380f;
		this.y = -50f;
		this.z = z - 100f;
		
		float[] vertices = Entity.getVertices(buttonWidth, buttonHeight, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		
		// **************************************************
		
		// initialize buttons array list
		buttons = new ArrayList<Button>();
				
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
				
		// initialize simulations data array list
		simulationsData = new ArrayList<String>();
				
		// **************************************************
				
		
		// sidebar panel
		sidebarWidth = 200f;
		sidebarHeight = 450f;
		
		float[] verticesPanel = Entity.getVertices(sidebarWidth, sidebarHeight, z);
		
		int textureID = loader.loadTexture(SIDEBAR_PANEL_TEXTURE_FILE);
		Model sidebarPanelModel = loader.loadToVAO(verticesPanel, texCoords, indices, textureID);
				
		Vector3f position = new Vector3f(x, y, this.z);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		sidebarPanel = new GUIComponent(sidebarPanelModel, position, rotation, scale);
		
		guiComponents.add(sidebarPanel);
		
		// **************************************************
		
		
		// simulation button model
		textureID = loader.loadTexture(SIMULATION_BUTTON_TEXTURE_FILE);
		simulationButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		// **************************************************
		
		
		// create new simulation button
		
		float buttonY = 0f; // will be changed
				
		position = new Vector3f(x - 30f, buttonY, this.z + 0.01f);
		rotation = new Vector3f(0,0,0);
		scale = 1f;
				
		textureID = loader.loadTexture(CREATE_NEW_SIMULATION_BUTTON_TEXTURE_FILE);
		Model createNewSimulationButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		createNewSimulationButton = new Button(createNewSimulationButtonModel, position, rotation, scale, 
				buttonWidth, buttonHeight);
		
		// **************************************************
		
		
		// up button
		
		float upDownButtonWidth = 60f;
		float upDownButtonHeight = 60f;
		
		float[] verticesUpDownButtons = Entity.getVertices(upDownButtonWidth, upDownButtonHeight, z);
		
		float buttonX = x + sidebarWidth/2 - 10f - upDownButtonWidth/2;
		buttonY = y + sidebarHeight/2 - 10f - upDownButtonHeight/2;
						
		position = new Vector3f(buttonX, buttonY, this.z + 0.01f);
		rotation = new Vector3f(0,0,0);
		scale = 1f;
						
		textureID = loader.loadTexture(UP_BUTTON_ENABLED_TEXTURE_FILE);
		upButtonEnabledModel = loader.loadToVAO(verticesUpDownButtons, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(UP_BUTTON_DISABLED_TEXTURE_FILE);
		upButtonDisabledModel = loader.loadToVAO(verticesUpDownButtons, texCoords, indices, textureID);
				
		upButton = new Button(upButtonEnabledModel, position, rotation, scale, 
				upDownButtonWidth, upDownButtonHeight); 
						
		guiComponents.add(upButton);
				
		// **************************************************
		
		
		// down button
				
		buttonX = x + sidebarWidth/2 - 10f - upDownButtonWidth/2;
		buttonY = y - sidebarHeight/2 + 10f + upDownButtonHeight/2;
								
		position = new Vector3f(buttonX, buttonY, this.z + 0.01f);
		rotation = new Vector3f(0,0,0);
		scale = 1f;
								
		textureID = loader.loadTexture(DOWN_BUTTON_ENABLED_TEXTURE_FILE);
		downButtonEnabledModel = loader.loadToVAO(verticesUpDownButtons, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(DOWN_BUTTON_DISABLED_TEXTURE_FILE);
		downButtonDisabledModel = loader.loadToVAO(verticesUpDownButtons, texCoords, indices, textureID);
						
		downButton = new Button(downButtonEnabledModel, position, rotation, scale, 
				upDownButtonWidth, upDownButtonHeight); 
								
		guiComponents.add(downButton);
						
		// **************************************************
		
		// create simulation buttons
		for (String filename: files) {
			
			simulationsData.add(filename);
			createSimulationButton();
		}
		
		topSimulationIndex = 0;
		
		this.canCreateNewSimulation = canCreateNewSimulation;
		
		// update up down button states
		updateTopSimulationIndex(true);
	}
	
	/**
	 * Creates a simulation button.
	 * 
	 * @return the simulation button
	 */
	public void createSimulationButton() {
		
		float buttonY = 0f; // will be changed
		
		Vector3f position = new Vector3f(x - 30f, buttonY, z  + 0.01f);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		Button button = new Button(simulationButtonModel, position, rotation, scale, buttonWidth, buttonHeight); 
		
		// add button to array list
		buttons.add(button);
	}
	
	/**
	 * Renders the objects of the sidebar.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		renderer.renderGUI(guiComponents);
		
		// render the proper buttons
		float buttonY = y + sidebarHeight/2 - 15f - buttonHeight/2; // initial y value
				
		createNewSimulationButton.setEnabled(false);
		
		for (int i = topSimulationIndex; i < (topSimulationIndex + 4); i++) {
			
			// end of array
			if (i == buttons.size()) {
				
				// create a new simulation button
				if (canCreateNewSimulation) {
					
					createNewSimulationButton.getPosition().y = buttonY;
					createNewSimulationButton.updateAABB();
					createNewSimulationButton.setEnabled(true);
					renderer.render(createNewSimulationButton);
				}
				break;
			}
			
			// simulation button
			else {
				
				Button button = buttons.get(i);
				button.getPosition().y = buttonY;
				button.updateAABB();
				button.setEnabled(true);
				renderer.render(button);
			}
			
			buttonY -= buttonHeight + 7f;
		}
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

	/**
	 * Returns the down button.
	 * 
	 * @return downButton
	 */
	public Button getDownButton() {
		return downButton;
	}

	/**
	 * Returns the up button.
	 * 
	 * @return upButton
	 */
	public Button getUpButton() {
		return upButton;
	}
	
	/**
	 * Updates the top simulation index.
	 * 
	 * @param up	true if up button is pressed, false 
	 * if down button is pressed
	 */
	public void updateTopSimulationIndex(boolean up) {
		
		int n = 1;
		if (canCreateNewSimulation)
			n--;
		
		// up button
		if (up) {
			
			topSimulationIndex--;
			
			if (topSimulationIndex <= 0) {
				
				topSimulationIndex = 0;
				upButton.setModel(upButtonDisabledModel);
			}
			
			// update down button
			if (buttons.size() < 4 + n)
				downButton.setModel(downButtonDisabledModel);
			else
				downButton.setModel(downButtonEnabledModel);
		}
		
		// down button
		else {
			
			if (buttons.size() >= 3 + n) {
				
				topSimulationIndex++;
				
				if (topSimulationIndex >= buttons.size() - 3 - n) {
					
					topSimulationIndex = buttons.size() - 3 - n;
					downButton.setModel(downButtonDisabledModel);
				}
				
				// update up button
				if (buttons.size() < 4 + n)
					upButton.setModel(upButtonDisabledModel);
				else
					upButton.setModel(upButtonEnabledModel);
			}
		
		}
		
		// update AABBs
		for (int i = 0; i < buttons.size(); i++) {
			
			if (i < topSimulationIndex || i > topSimulationIndex + 3) {
				
				buttons.get(i).setEnabled(false);
			}
		}
	}
	
	/**
	 * Updates the top simulation index after a simulation 
	 * has been deleted.
	 */
	public void updateTopSimulationIndex() {
		
		int n = 1;
		if (canCreateNewSimulation)
			n--;
		
		topSimulationIndex--;
		
		if (topSimulationIndex <= 0) {
			
			topSimulationIndex = 0;
			upButton.setModel(upButtonDisabledModel);
		}
		
		// update down button
		if (buttons.size() < 4 + n || topSimulationIndex >= buttons.size() - 3 - n)
			downButton.setModel(downButtonDisabledModel);
		else
			downButton.setModel(downButtonEnabledModel);
	}

	/**
	 * Returns the up button enabled model.
	 * 
	 * @return upButtonEnabledModel
	 */
	public Model getUpButtonEnabledModel() {
		return upButtonEnabledModel;
	}

	/**
	 * Returns the up button disabled model.
	 * 
	 * @return upButtonDisabledModel
	 */
	public Model getUpButtonDisabledModel() {
		return upButtonDisabledModel;
	}

	/**
	 * Returns the down button enabled model.
	 * 
	 * @return downButtonEnabledModel
	 */
	public Model getDownButtonEnabledModel() {
		return downButtonEnabledModel;
	}

	/**
	 * Returns the down button disabled model.
	 * 
	 * @return downButtonDisabledModel
	 */
	public Model getDownButtonDisabledModel() {
		return downButtonDisabledModel;
	}
	
	/**
	 * Returns the button width.
	 * 
	 * @return buttonWidth
	 */
	public float getButtonWidth() {
		return buttonWidth;
	}
	
	/**
	 * Returns the button height.
	 * 
	 * @return buttonHeight
	 */
	public float getButtonHeight() {
		return buttonHeight;
	}
}
