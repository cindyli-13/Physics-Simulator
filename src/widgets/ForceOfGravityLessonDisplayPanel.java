package widgets;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;

/**
 * This class contains the components of the force of 
 * gravity lesson display panel, which allows the user 
 * to view and edit the properties of the object in the 
 * simulation.
 * 
 * This display panel allows the user to view the force of 
 * gravity exerted on, the mass, and the acceleration of the 
 * object in the simulation. The user can change the mass and 
 * see its effect on the force of gravity that is exerted on
 * the object.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Wednesday, May 23rd, 2018
 */
public class ForceOfGravityLessonDisplayPanel {

	// instance variables
	private ArrayList<GUIComponent> guiComponents;
		
	private Model increaseButtonEnabledModel;
	private Model increaseButtonDisabledModel;
	private Model decreaseButtonEnabledModel;
	private Model decreaseButtonDisabledModel;
		
	private Button increaseMassButton;
	private Button decreaseMassButton;
	
	private Label timeLabel;
	private Label massLabel;
	private Label forceOfGravityLabel;
	private Label accelerationLabel;
		
	private GUIComponent displayPanel;
		
	private float displayPanelWidth;
	private float displayPanelHeight;
	
	// static variables
	public static final String DISPLAY_PANEL_TEXTURE_FILE = "./res/displayPanel.png";
	
	public static final String INCREASE_BUTTON_ENABLED_TEXTURE_FILE = "./res/increaseSizeButtonEnabled.png";
	public static final String INCREASE_BUTTON_DISABLED_TEXTURE_FILE = "./res/increaseSizeButtonDisabled.png";
	public static final String DECREASE_BUTTON_ENABLED_TEXTURE_FILE = "./res/decreaseSizeButtonEnabled.png";
	public static final String DECREASE_BUTTON_DISABLED_TEXTURE_FILE = "./res/decreaseSizeButtonDisabled.png";
	
	public static final String TIME_LABEL_TEXTURE_FILE = "./res/timeLabel.png";
	public static final String MASS_LABEL_TEXTURE_FILE = "./res/massLabel.png";
	public static final String FORCE_OF_GRAVITY_LABEL_TEXTURE_FILE = "./res/forceOfGravityLabel-small.png";
	public static final String ACCELERATION_LABEL_TEXTURE_FILE = "./res/accelerationLabel.png";
	
	// constructor
	public ForceOfGravityLessonDisplayPanel(Loader loader, float x, float y, float z) {
		
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
				
		// **************************************************
		
				
		// display panel
		displayPanelWidth = 670f;
		displayPanelHeight = 140f;
		
		float[] vertices = Entity.getVertices(displayPanelWidth, displayPanelHeight, z);
		
		int textureID = loader.loadTexture(DISPLAY_PANEL_TEXTURE_FILE);
		Model model = loader.loadToVAO(vertices, texCoords, indices, textureID);
					
		Vector3f position = new Vector3f(x, y, z);
			
		displayPanel = new GUIComponent(model, position, rotation, scale);
			
		guiComponents.add(displayPanel);
				
		// **************************************************
		
		
		// labels
		float leftOfDisplay = x - displayPanelWidth/2;
		float topOfDisplay = y + displayPanelHeight/2;
		
		float labelWidth = 80f;
		float labelHeight = 20f;
		
		float offsetX = 10f + labelWidth/2;
		float offsetY = 10f + labelHeight/2;
		
		
		// mass label
		vertices = Entity.getVertices(labelWidth, labelHeight, z);
		
		textureID = loader.loadTexture(MASS_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		massLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
		
		guiComponents.add(massLabel);
		
		
		// force of gravity label		
		offsetY += labelHeight + 10f;
		
		textureID = loader.loadTexture(FORCE_OF_GRAVITY_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
				
		forceOfGravityLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
				
		guiComponents.add(forceOfGravityLabel);
		
		
		// acceleration label		
		offsetY += labelHeight + 10f;
				
		textureID = loader.loadTexture(ACCELERATION_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
						
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
						
		accelerationLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
						
		guiComponents.add(accelerationLabel);
		
		
		// time label
		offsetY += labelHeight + 15f;
				
		textureID = loader.loadTexture(TIME_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		timeLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
				
		guiComponents.add(timeLabel);
				
		// **************************************************
		
		
		// increase / decrease buttons
		
		float buttonWidth = 20f;
		float buttonHeight = 20f;
		
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z);
		
		textureID = loader.loadTexture(INCREASE_BUTTON_ENABLED_TEXTURE_FILE);
		increaseButtonEnabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(INCREASE_BUTTON_DISABLED_TEXTURE_FILE);
		increaseButtonDisabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(DECREASE_BUTTON_ENABLED_TEXTURE_FILE);
		decreaseButtonEnabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(DECREASE_BUTTON_DISABLED_TEXTURE_FILE);
		decreaseButtonDisabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		
		// for mass
		offsetX = 10f + labelWidth + 5f + buttonWidth/2;
		offsetY = 10f + labelHeight/2;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		decreaseMassButton = new Button(decreaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(decreaseMassButton);
		
		
		offsetX += buttonWidth + 3f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		increaseMassButton = new Button(increaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(increaseMassButton);
		
		
		// initial button states
		decreaseMassButton.setEnabled(false);
		decreaseMassButton.setModel(decreaseButtonDisabledModel);
	}
	
	/**
	 * Renders the objects of the display panel.
	 * 
	 * @param renderer		the renderer
	 */
	public void render (Renderer renderer) {
		
		renderer.renderGUI(guiComponents);
	}

	/**
	 * Returns the increase mass button.
	 * 
	 * @return increaseMassButton
	 */
	public Button getIncreaseMassButton() {
		return increaseMassButton;
	}

	/**
	 * Returns the decrease mass button.
	 * 
	 * @return decreaseMassButton
	 */
	public Button getDecreaseMassButton() {
		return decreaseMassButton;
	}

	/**
	 * Sets the state of the increase mass button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setIncreaseMassButtonState(boolean enabled) {
		
		if (enabled) {
			
			increaseMassButton.setEnabled(true);
			increaseMassButton.setModel(increaseButtonEnabledModel);
		}
		
		else {
			
			increaseMassButton.setEnabled(false);
			increaseMassButton.setModel(increaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the decrease mass button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setDecreaseMassButtonState(boolean enabled) {
		
		if (enabled) {
			
			decreaseMassButton.setEnabled(true);
			decreaseMassButton.setModel(decreaseButtonEnabledModel);
		}
		
		else {
			
			decreaseMassButton.setEnabled(false);
			decreaseMassButton.setModel(decreaseButtonDisabledModel);
		}
	}
	
}
