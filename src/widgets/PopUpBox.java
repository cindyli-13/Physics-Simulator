package widgets;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;

/**
 * This class is the blueprint for pop-up boxes 
 * which will pop up when the user selects an 
 * object from the simulation, allowing the user 
 * to edit the object's properties.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 */
public class PopUpBox extends GUIComponent {

	// instance variables
	private Button closeButton;
	private Button deleteEntityButton;
	
	private Button increaseSizeButton;
	private Button decreaseSizeButton;
	private Label sizeLabel;
	
	private Button increaseVelocityXButton;
	private Button decreaseVelocityXButton;
	private Label velocityXLabel;
	
	private Button increaseVelocityYButton;
	private Button decreaseVelocityYButton;
	private Label velocityYLabel;
	
	private Button increaseMassButton;
	private Button decreaseMassButton;
	private Label massLabel;
	
	private ArrayList<GUIComponent> guiComponents;
	
	private float width;
	private float height;
	
	private Model increaseButtonEnabledModel;
	private Model increaseButtonDisabledModel;
	private Model decreaseButtonEnabledModel;
	private Model decreaseButtonDisabledModel;
	
	// static variables
	public static final String CLOSE_BUTTON_TEXTURE_FILE = "./res/closeButton.png";
	public static final String DELETE_ENTITY_BUTTON_TEXTURE_FILE = "./res/deleteEntityButton.png";
	public static final String POP_UP_BOX_TEXTURE_FILE = "./res/popUpBox.png";
	public static final String INCREASE_BUTTON_ENABLED_TEXTURE_FILE = "./res/increaseSizeButtonEnabled.png";
	public static final String DECREASE_BUTTON_ENABLED_TEXTURE_FILE = "./res/decreaseSizeButtonEnabled.png";
	public static final String INCREASE_BUTTON_DISABLED_TEXTURE_FILE = "./res/increaseSizeButtonDisabled.png";
	public static final String DECREASE_BUTTON_DISABLED_TEXTURE_FILE = "./res/decreaseSizeButtonDisabled.png";
	public static final String SIZE_LABEL_TEXTURE_FILE = "./res/sizeLabel.png";
	public static final String VELOCITY_X_LABEL_TEXTURE_FILE = "./res/velocityXLabel.png";
	public static final String VELOCITY_Y_LABEL_TEXTURE_FILE = "./res/velocityYLabel.png";
	public static final String MASS_LABEL_TEXTURE_FILE = "./res/massLabel.png";
	
	// constructor
	public PopUpBox(Loader loader, Model model, Vector3f position, Vector3f rotation, float scale, 
			float width, float height, float z) {
		
		super(model, position, rotation, scale);
		
		
		// *************************************
		
		// the following will be the same for each button
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		Vector3f rot = new Vector3f(0,0,0);
		float s = 1f;
		
		// close button
		
		float buttonWidth = 30f;
		float buttonHeight = 20f;
		
		float buttonX = position.x + width/2 - buttonWidth/2;
		float buttonY = position.y + height/2 - buttonHeight/2;
		
		float[] vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		Vector3f pos = new Vector3f(buttonX, buttonY, z);
		
		int textureID = loader.loadTexture(CLOSE_BUTTON_TEXTURE_FILE);
		Model buttonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		closeButton = new Button(buttonModel, pos, rot, s, buttonWidth, buttonHeight);
		
		// delete entity button
		
		buttonWidth = 60f;
		buttonHeight = 30f;
		
		buttonX = position.x;
		buttonY = position.y - height/2 + buttonHeight/2 + 10f;
		
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
		
		textureID = loader.loadTexture(DELETE_ENTITY_BUTTON_TEXTURE_FILE);
		buttonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		deleteEntityButton = new Button(buttonModel, pos, rot, s, buttonWidth, buttonHeight);
		
		// decrease size button
		
		buttonWidth = 25f;
		buttonHeight = 25f;
				
		buttonX = position.x + 20f;
		buttonY = position.y + 60f;
				
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
				
		textureID = loader.loadTexture(DECREASE_BUTTON_ENABLED_TEXTURE_FILE);
		decreaseButtonEnabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(DECREASE_BUTTON_DISABLED_TEXTURE_FILE);
		decreaseButtonDisabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		decreaseSizeButton = new Button(decreaseButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
		
		// increase size button
		
		buttonX = position.x + 50f;
						
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
						
		textureID = loader.loadTexture(INCREASE_BUTTON_ENABLED_TEXTURE_FILE);
		increaseButtonEnabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(INCREASE_BUTTON_DISABLED_TEXTURE_FILE);
		increaseButtonDisabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
						
		increaseSizeButton = new Button(increaseButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
		
		// size label
		
		float labelWidth = 70f;
		float labelHeight = 25f;
								
		float labelX = position.x - 40f;
		float labelY = position.y + 60f;
								
		vertices = Entity.getVertices(labelWidth, labelHeight, z + 0.01f);
		pos = new Vector3f(labelX, labelY, z);
								
		textureID = loader.loadTexture(SIZE_LABEL_TEXTURE_FILE);
		Model labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
								
		sizeLabel = new Label(labelModel, pos, rot, s, labelWidth, labelHeight);
		
		// *************************************
		
		// decrease velocity x button
						
		buttonX = position.x + 20f;
		buttonY -= buttonHeight + 5f;
						
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
						
		decreaseVelocityXButton = new Button(decreaseButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
				
		
		// increase velocity x button
		buttonX = position.x + 50f;
								
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
								
		increaseVelocityXButton = new Button(increaseButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
		
		
		// velocity x label
									
		labelY = buttonY;
										
		vertices = Entity.getVertices(labelWidth, labelHeight, z + 0.01f);
		pos = new Vector3f(labelX, labelY, z);
										
		textureID = loader.loadTexture(VELOCITY_X_LABEL_TEXTURE_FILE);
		labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
										
		velocityXLabel = new Label(labelModel, pos, rot, s, labelWidth, labelHeight);
				
		// *************************************
		
		// decrease velocity y button
		
		buttonX = position.x + 20f;
		buttonY -= buttonHeight + 5f;
								
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
								
		decreaseVelocityYButton = new Button(decreaseButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
						
				
		// increase velocity y button
		buttonX = position.x + 50f;
										
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
										
		increaseVelocityYButton = new Button(increaseButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
				
				
		// velocity y label
											
		labelY = buttonY;
												
		vertices = Entity.getVertices(labelWidth, labelHeight, z + 0.01f);
		pos = new Vector3f(labelX, labelY, z);
												
		textureID = loader.loadTexture(VELOCITY_Y_LABEL_TEXTURE_FILE);
		labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
												
		velocityYLabel = new Label(labelModel, pos, rot, s, labelWidth, labelHeight);
						
		// *************************************
		
		// decrease mass button
		
		buttonX = position.x + 20f;
		buttonY -= buttonHeight + 5f;
										
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
										
		decreaseMassButton = new Button(decreaseButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
								
						
		// increase mass button
		buttonX = position.x + 50f;
												
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
												
		increaseMassButton = new Button(increaseButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
						
						
		// mass label
													
		labelY = buttonY;
														
		vertices = Entity.getVertices(labelWidth, labelHeight, z + 0.01f);
		pos = new Vector3f(labelX, labelY, z);
														
		textureID = loader.loadTexture(MASS_LABEL_TEXTURE_FILE);
		labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
														
		massLabel = new Label(labelModel, pos, rot, s, labelWidth, labelHeight);
								
		// *************************************
		
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(closeButton);
		guiComponents.add(deleteEntityButton);
		guiComponents.add(increaseSizeButton);
		guiComponents.add(decreaseSizeButton);
		guiComponents.add(sizeLabel);
		guiComponents.add(increaseVelocityXButton);
		guiComponents.add(decreaseVelocityXButton);
		guiComponents.add(velocityXLabel);
		guiComponents.add(increaseVelocityYButton);
		guiComponents.add(decreaseVelocityYButton);
		guiComponents.add(velocityYLabel);
		guiComponents.add(increaseMassButton);
		guiComponents.add(decreaseMassButton);
		guiComponents.add(massLabel);
		guiComponents.add(this);
		
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Returns the close button.
	 * 
	 * @return closeButton
	 */
	public Button getCloseButton() {
		return closeButton;
	}
	
	/**
	 * Returns the delete entity button.
	 * 
	 * @return deleteEntityButton
	 */
	public Button getDeleteEntityButton() {
		return deleteEntityButton;
	}
	
	/**
	 * Returns the increase size button.
	 * 
	 * @return increaseSizeButton
	 */
	public Button getIncreaseSizeButton() {
		return increaseSizeButton;
	}
	
	/**
	 * Returns the decrease size button.
	 * 
	 * @return decreaseSizeButton
	 */
	public Button getDecreaseSizeButton() {
		return decreaseSizeButton;
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
	 * Renders the objects of the simulation window.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		renderer.renderGUI(guiComponents);
	}
	
	/**
	 * Updates the pop-up box's position by the 
	 * given offsets.
	 * 
	 * @param offsetX
	 * @param offsetY
	 */
	public void update(float offsetX, float offsetY) {
		
		// update pop-up box
		this.getPosition().x += offsetX;
		this.getPosition().y += offsetY;
		
		// update buttons
		updateButton(closeButton, offsetX, offsetY);
		updateButton(deleteEntityButton, offsetX, offsetY);
		updateButton(increaseSizeButton, offsetX, offsetY);
		updateButton(decreaseSizeButton, offsetX, offsetY);
		updateButton(increaseVelocityXButton, offsetX, offsetY);
		updateButton(decreaseVelocityXButton, offsetX, offsetY);
		updateButton(increaseVelocityYButton, offsetX, offsetY);
		updateButton(decreaseVelocityYButton, offsetX, offsetY);
		updateButton(increaseMassButton, offsetX, offsetY);
		updateButton(decreaseMassButton, offsetX, offsetY);
		
		// update labels
		updateLabel(sizeLabel, offsetX, offsetY);
		updateLabel(velocityXLabel, offsetX, offsetY);
		updateLabel(velocityYLabel, offsetX, offsetY);
		updateLabel(massLabel, offsetX, offsetY);
	}
	
	/**
	 * Updates the button's position by the 
	 * given offsets.
	 * 
	 * @param button
	 * @param offsetX
	 * @param offsetY
	 */
	private void updateButton(Button button, float offsetX, float offsetY) {
		
		button.getPosition().x += offsetX;
		button.getPosition().y += offsetY;
						
		button.getAabb().setMin(new Vector2f(button.getPosition().x 
				- button.getWidth()/2, 
				button.getPosition().y - button.getHeight()/2)); 
						
		button.getAabb().setMax(new Vector2f(button.getPosition().x 
				+ button.getWidth()/2, 
				button.getPosition().y + button.getHeight()/2));
	}
	
	/**
	 * Updates the label's position by the 
	 * given offsets.
	 * 
	 * @param label
	 * @param offsetX
	 * @param offsetY
	 */
	private void updateLabel(Label label, float offsetX, float offsetY) {
		
		label.getPosition().x += offsetX;
		label.getPosition().y += offsetY;
	}
	
	/**
	 * Returns the pop-up box's width.
	 * 
	 * @return width
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Returns the pop-up box's height.
	 * 
	 * @return height
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Sets the state of the increase size button to 
	 * either enabled or disabled.
	 * 
	 * @param enabled 	specifies whether to set it 
	 * to enabled or disabled
	 */
	public void setIncreaseSizeButtonState(boolean enabled) {
		
		if (enabled) {
			
			increaseSizeButton.setModel(increaseButtonEnabledModel);
		}
		else {
			increaseSizeButton.setModel(increaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the decrease size button to 
	 * either enabled or disabled.
	 * 
	 * @param enabled 	specifies whether to set it 
	 * to enabled or disabled
	 */
	public void setDecreaseSizeButtonState(boolean enabled) {
		
		if (enabled) {
			
			decreaseSizeButton.setModel(decreaseButtonEnabledModel);
		}
		else {
			decreaseSizeButton.setModel(decreaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the increase velocity x
	 * button to either enabled or disabled.
	 * 
	 * @param enabled 	specifies whether to set it 
	 * to enabled or disabled
	 */
	public void setIncreaseVelocityXButtonState(boolean enabled) {
		
		if (enabled) {
			
			increaseVelocityXButton.setModel(increaseButtonEnabledModel);
		}
		else {
			increaseVelocityXButton.setModel(increaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the decrease velocity x 
	 * button to either enabled or disabled.
	 * 
	 * @param enabled 	specifies whether to set it 
	 * to enabled or disabled
	 */
	public void setDecreaseVelocityXButtonState(boolean enabled) {
		
		if (enabled) {
			
			decreaseVelocityXButton.setModel(decreaseButtonEnabledModel);
		}
		else {
			decreaseVelocityXButton.setModel(decreaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the increase velocity y
	 * button to either enabled or disabled.
	 * 
	 * @param enabled 	specifies whether to set it 
	 * to enabled or disabled
	 */
	public void setIncreaseVelocityYButtonState(boolean enabled) {
		
		if (enabled) {
			
			increaseVelocityYButton.setModel(increaseButtonEnabledModel);
		}
		else {
			increaseVelocityYButton.setModel(increaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the decrease velocity y 
	 * button to either enabled or disabled.
	 * 
	 * @param enabled 	specifies whether to set it 
	 * to enabled or disabled
	 */
	public void setDecreaseVelocityYButtonState(boolean enabled) {
		
		if (enabled) {
			
			decreaseVelocityYButton.setModel(decreaseButtonEnabledModel);
		}
		else {
			decreaseVelocityYButton.setModel(decreaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the increase mass button to 
	 * either enabled or disabled.
	 * 
	 * @param enabled 	specifies whether to set it 
	 * to enabled or disabled
	 */
	public void setIncreaseMassButtonState(boolean enabled) {
		
		if (enabled) {
			
			increaseMassButton.setModel(increaseButtonEnabledModel);
		}
		else {
			increaseMassButton.setModel(increaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the decrease mass button to 
	 * either enabled or disabled.
	 * 
	 * @param enabled 	specifies whether to set it 
	 * to enabled or disabled
	 */
	public void setDecreaseMassButtonState(boolean enabled) {
		
		if (enabled) {
			
			decreaseMassButton.setModel(decreaseButtonEnabledModel);
		}
		else {
			decreaseMassButton.setModel(decreaseButtonDisabledModel);
		}
	}

}
