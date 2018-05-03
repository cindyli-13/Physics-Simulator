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
	
	private ArrayList<GUIComponent> guiComponents;
	
	private float width;
	private float height;
	
	// static variables
	public static final String CLOSE_BUTTON_TEXTURE_FILE = "./res/closeButton.png";
	public static final String DELETE_ENTITY_BUTTON_TEXTURE_FILE = "./res/deleteEntityButton.png";
	public static final String POP_UP_BOX_TEXTURE_FILE = "./res/popUpBox.png";
	
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
		
		buttonWidth = 70f;
		buttonHeight = 40f;
		
		buttonX = position.x;
		buttonY = position.y - 20f;
		
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		pos = new Vector3f(buttonX, buttonY, z);
		
		textureID = loader.loadTexture(DELETE_ENTITY_BUTTON_TEXTURE_FILE);
		buttonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		deleteEntityButton = new Button(buttonModel, pos, rot, s, buttonWidth, buttonHeight);
		
		// *************************************
		
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(closeButton);
		guiComponents.add(deleteEntityButton);
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

}
