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
	private GUIComponent sizeLabel;
	
	private ArrayList<GUIComponent> guiComponents;
	
	private float width;
	private float height;
	
	private Model increaseSizeButtonEnabledModel;
	private Model increaseSizeButtonDisabledModel;
	private Model decreaseSizeButtonEnabledModel;
	private Model decreaseSizeButtonDisabledModel;
	
	// static variables
	public static final String CLOSE_BUTTON_TEXTURE_FILE = "./res/closeButton.png";
	public static final String DELETE_ENTITY_BUTTON_TEXTURE_FILE = "./res/deleteEntityButton.png";
	public static final String POP_UP_BOX_TEXTURE_FILE = "./res/popUpBox.png";
	public static final String INCREASE_SIZE_BUTTON_ENABLED_TEXTURE_FILE = "./res/increaseSizeButtonEnabled.png";
	public static final String DECREASE_SIZE_BUTTON_ENABLED_TEXTURE_FILE = "./res/decreaseSizeButtonEnabled.png";
	public static final String INCREASE_SIZE_BUTTON_DISABLED_TEXTURE_FILE = "./res/increaseSizeButtonDisabled.png";
	public static final String DECREASE_SIZE_BUTTON_DISABLED_TEXTURE_FILE = "./res/decreaseSizeButtonDisabled.png";
	public static final String SIZE_LABEL_TEXTURE_FILE = "./res/sizeLabel.png";
	
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
		buttonY = position.y - 20f;
		
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
		
		textureID = loader.loadTexture(DELETE_ENTITY_BUTTON_TEXTURE_FILE);
		buttonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		deleteEntityButton = new Button(buttonModel, pos, rot, s, buttonWidth, buttonHeight);
		
		// increase size button
		
		buttonWidth = 25f;
		buttonHeight = 25f;
				
		buttonX = position.x + 10f;
		buttonY = position.y + 25f;
				
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
				
		textureID = loader.loadTexture(INCREASE_SIZE_BUTTON_ENABLED_TEXTURE_FILE);
		increaseSizeButtonEnabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(INCREASE_SIZE_BUTTON_DISABLED_TEXTURE_FILE);
		increaseSizeButtonDisabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		increaseSizeButton = new Button(increaseSizeButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
		
		// decrease size button
		
		buttonWidth = 25f;
		buttonHeight = 25f;
						
		buttonX = position.x + 40f;
		buttonY = position.y + 25f;
						
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
						
		textureID = loader.loadTexture(DECREASE_SIZE_BUTTON_ENABLED_TEXTURE_FILE);
		decreaseSizeButtonEnabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(DECREASE_SIZE_BUTTON_DISABLED_TEXTURE_FILE);
		decreaseSizeButtonDisabledModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
						
		decreaseSizeButton = new Button(decreaseSizeButtonEnabledModel, pos, rot, s, buttonWidth, buttonHeight);
		
		// size label
		
		float labelWidth = 50f;
		float labelHeight = 25f;
								
		float labelX = position.x - 40f;
		float labelY = position.y + 25f;
								
		vertices = Entity.getVertices(labelWidth, labelHeight, z + 0.01f);
		pos = new Vector3f(labelX, labelY, z);
								
		textureID = loader.loadTexture(SIZE_LABEL_TEXTURE_FILE);
		Model labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
								
		sizeLabel = new Label(labelModel, pos, rot, s, labelWidth, labelHeight);
		
		// *************************************
		
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(closeButton);
		guiComponents.add(deleteEntityButton);
		guiComponents.add(increaseSizeButton);
		guiComponents.add(decreaseSizeButton);
		guiComponents.add(sizeLabel);
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
		
		// pop-up box
		
		this.getPosition().x += offsetX;
		this.getPosition().y += offsetY;
		
		// close button
		
		closeButton.getPosition().x += offsetX;
		closeButton.getPosition().y += offsetY;
		
		closeButton.getAabb().setMin(new Vector2f(closeButton.getPosition().x - closeButton.getWidth()/2, 
				closeButton.getPosition().y - closeButton.getHeight()/2)); 
		
		closeButton.getAabb().setMax(new Vector2f(closeButton.getPosition().x + closeButton.getWidth()/2, 
				closeButton.getPosition().y + closeButton.getHeight()/2));
		
		// delete entity button
		
		deleteEntityButton.getPosition().x += offsetX;
		deleteEntityButton.getPosition().y += offsetY;
				
		deleteEntityButton.getAabb().setMin(new Vector2f(deleteEntityButton.getPosition().x 
				- deleteEntityButton.getWidth()/2, 
				deleteEntityButton.getPosition().y - deleteEntityButton.getHeight()/2)); 
				
		deleteEntityButton.getAabb().setMax(new Vector2f(deleteEntityButton.getPosition().x 
				+ deleteEntityButton.getWidth()/2, 
				deleteEntityButton.getPosition().y + deleteEntityButton.getHeight()/2));
		
		// increase size button
		
		increaseSizeButton.getPosition().x += offsetX;
		increaseSizeButton.getPosition().y += offsetY;
						
		increaseSizeButton.getAabb().setMin(new Vector2f(increaseSizeButton.getPosition().x 
				- increaseSizeButton.getWidth()/2, 
				increaseSizeButton.getPosition().y - increaseSizeButton.getHeight()/2)); 
						
		increaseSizeButton.getAabb().setMax(new Vector2f(increaseSizeButton.getPosition().x 
				+ increaseSizeButton.getWidth()/2, 
				increaseSizeButton.getPosition().y + increaseSizeButton.getHeight()/2));
		
		// decrease size button
		
		decreaseSizeButton.getPosition().x += offsetX;
		decreaseSizeButton.getPosition().y += offsetY;
								
		decreaseSizeButton.getAabb().setMin(new Vector2f(decreaseSizeButton.getPosition().x 
				- decreaseSizeButton.getWidth()/2, 
				decreaseSizeButton.getPosition().y - decreaseSizeButton.getHeight()/2)); 
								
		decreaseSizeButton.getAabb().setMax(new Vector2f(decreaseSizeButton.getPosition().x 
				+ decreaseSizeButton.getWidth()/2, 
				decreaseSizeButton.getPosition().y + decreaseSizeButton.getHeight()/2));
		
		// size label
		
		sizeLabel.getPosition().x += offsetX;
		sizeLabel.getPosition().y += offsetY;
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
			
			increaseSizeButton.setModel(increaseSizeButtonEnabledModel);
		}
		else {
			increaseSizeButton.setModel(increaseSizeButtonDisabledModel);
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
			
			decreaseSizeButton.setModel(decreaseSizeButtonEnabledModel);
		}
		else {
			decreaseSizeButton.setModel(decreaseSizeButtonDisabledModel);
		}
	}

}
