package widgets;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;

/**
 * This class contains the components of the projectile motion 
 * lesson display panel, which allows the user to view and edit 
 * the properties of the object in the simulation.
 * 
 * This display panel allows the user to view the horizontal and 
 * vertical position, velocity, and acceleration of the object in 
 * the simulation. The user can change the velocity components magnitudes.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Wednesday, May 23rd, 2018
 */
public class ProjectileMotionLessonDisplayPanel {

	// instance variables
	private ArrayList<GUIComponent> guiComponents;
		
	private Model increaseButtonEnabledModel;
	private Model increaseButtonDisabledModel;
	private Model decreaseButtonEnabledModel;
	private Model decreaseButtonDisabledModel;
		
	private Button increaseVelocityXButton;
	private Button decreaseVelocityXButton;
	private Button increaseVelocityYButton;
	private Button decreaseVelocityYButton;
	
	private Label timeLabel;
	private Label positionXLabel;
	private Label positionYLabel;
	private Label velocityXLabel;
	private Label velocityYLabel;
	private Label accelerationXLabel;
	private Label accelerationYLabel;
		
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
	public static final String POSITION_X_LABEL_TEXTURE_FILE = "./res/positionXLabel.png";
	public static final String POSITION_Y_LABEL_TEXTURE_FILE = "./res/positionYLabel.png";
	public static final String VELOCITY_X_LABEL_TEXTURE_FILE = "./res/velocityXLabel.png";
	public static final String VELOCITY_Y_LABEL_TEXTURE_FILE = "./res/velocityYLabel.png";
	public static final String ACCELERATION_X_LABEL_TEXTURE_FILE = "./res/accelerationXLabel.png";
	public static final String ACCELERATION_Y_LABEL_TEXTURE_FILE = "./res/accelerationYLabel.png";
	
	// constructor
	public ProjectileMotionLessonDisplayPanel(Loader loader, float x, float y, float z) {
		
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
		
		
		// position labels
		vertices = Entity.getVertices(labelWidth, labelHeight, z);
		
		textureID = loader.loadTexture(POSITION_X_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		positionXLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
		
		guiComponents.add(positionXLabel);
		
		
		textureID = loader.loadTexture(POSITION_Y_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		position = new Vector3f(leftOfDisplay + offsetX + labelWidth + 110f, topOfDisplay - offsetY, z + 0.01f);
		
		positionYLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
		
		guiComponents.add(positionYLabel);
		
		
		// velocity labels		
		offsetY += labelHeight + 10f;
		
		textureID = loader.loadTexture(VELOCITY_X_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
				
		velocityXLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
				
		guiComponents.add(velocityXLabel);
		
		
		textureID = loader.loadTexture(VELOCITY_Y_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		position = new Vector3f(leftOfDisplay + offsetX + labelWidth + 110f, topOfDisplay - offsetY, z + 0.01f);
		
		velocityYLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
		
		guiComponents.add(velocityYLabel);
		
		
		// acceleration labels		
		offsetY += labelHeight + 10f;
				
		textureID = loader.loadTexture(ACCELERATION_X_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
						
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
						
		accelerationXLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
						
		guiComponents.add(accelerationXLabel);
		
		
		textureID = loader.loadTexture(ACCELERATION_Y_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		position = new Vector3f(leftOfDisplay + offsetX + labelWidth + 110f, topOfDisplay - offsetY, z + 0.01f);
		
		accelerationYLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
		
		guiComponents.add(accelerationYLabel);
		
		
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
		
		
		// for velocity x
		offsetX = 10f + labelWidth + 5f + buttonWidth/2;
		offsetY = 10f + labelHeight/2 + labelHeight + 10f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		decreaseVelocityXButton = new Button(decreaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(decreaseVelocityXButton);
		
		
		offsetX += buttonWidth + 3f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		increaseVelocityXButton = new Button(increaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(increaseVelocityXButton);
		
		
		// for velocity y
		offsetX = 10f + labelWidth/2 + 110f + labelWidth + labelWidth/2 + buttonWidth/2 + 3f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		decreaseVelocityYButton = new Button(decreaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(decreaseVelocityYButton);
		
		
		
		offsetX += buttonWidth + 3f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		increaseVelocityYButton = new Button(increaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(increaseVelocityYButton);
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
	 * Returns the increase velocity x button.
	 * 
	 * @return increaseVelocityXButton
	 */
	public Button getIncreaseVelocityXButton() {
		return increaseVelocityXButton;
	}

	/**
	 * Returns the decrease velocity x button.
	 * 
	 * @return decreaseVelocityXButton
	 */
	public Button getDecreaseVelocityXButton() {
		return decreaseVelocityXButton;
	}
	
	/**
	 * Returns the increase velocity y button.
	 * 
	 * @return increaseVelocityYButton
	 */
	public Button getIncreaseVelocityYButton() {
		return increaseVelocityYButton;
	}

	/**
	 * Returns the decrease velocity y button.
	 * 
	 * @return decreaseVelocityYButton
	 */
	public Button getDecreaseVelocityYButton() {
		return decreaseVelocityYButton;
	}
	
	/**
	 * Sets the state of the increase velocity x button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setIncreaseVelocityXButtonState(boolean enabled) {
		
		if (enabled) {
			
			increaseVelocityXButton.setEnabled(true);
			increaseVelocityXButton.setModel(increaseButtonEnabledModel);
		}
		
		else {
			
			increaseVelocityXButton.setEnabled(false);
			increaseVelocityXButton.setModel(increaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the decrease velocity x button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setDecreaseVelocityXButtonState(boolean enabled) {
		
		if (enabled) {
			
			decreaseVelocityXButton.setEnabled(true);
			decreaseVelocityXButton.setModel(decreaseButtonEnabledModel);
		}
		
		else {
			
			decreaseVelocityXButton.setEnabled(false);
			decreaseVelocityXButton.setModel(decreaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the increase velocity y button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setIncreaseVelocityYButtonState(boolean enabled) {
		
		if (enabled) {
			
			increaseVelocityYButton.setEnabled(true);
			increaseVelocityYButton.setModel(increaseButtonEnabledModel);
		}
		
		else {
			
			increaseVelocityYButton.setEnabled(false);
			increaseVelocityYButton.setModel(increaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the decrease velocity y button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setDecreaseVelocityYButtonState(boolean enabled) {
		
		if (enabled) {
			
			decreaseVelocityYButton.setEnabled(true);
			decreaseVelocityYButton.setModel(decreaseButtonEnabledModel);
		}
		
		else {
			
			decreaseVelocityYButton.setEnabled(false);
			decreaseVelocityYButton.setModel(decreaseButtonDisabledModel);
		}
	}
	
}
