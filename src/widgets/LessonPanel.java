package widgets;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;

/**
 * This class contains the components of the lesson panel.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Saturday, May 19th, 2018
 */
public class LessonPanel {

	// instance variables
	private ArrayList<GUIComponent> guiComponents;
		
	private Model leftButtonEnabledModel;
	private Model leftButtonDisabledModel;
	private Model rightButtonEnabledModel;
	private Model rightButtonDisabledModel;
	private Model showButtonModel;
	private Model hideButtonModel;
		
	private Button leftButton;
	private Button rightButton;
	private Button showHideButton;
		
	private GUIComponent lessonPanel;
		
	private float lessonPanelWidth;
	private float lessonPanelHeight;
		
	private float x;
	private float y;
	private float z;
	
	private boolean showPanel;
	
	// static variables
	private static String LESSON_PANEL_TEXTURE_FILE = "./res/lessonPanel.png";
	private static String LEFT_BUTTON_ENABLED_TEXTURE_FILE = "./res/leftButtonEnabled.png";
	private static String LEFT_BUTTON_DISABLED_TEXTURE_FILE = "./res/leftButtonDisabled.png";
	private static String RIGHT_BUTTON_ENABLED_TEXTURE_FILE = "./res/rightButtonEnabled.png";
	private static String RIGHT_BUTTON_DISABLED_TEXTURE_FILE = "./res/rightButtonDisabled.png";
	private static String SHOW_BUTTON_TEXTURE_FILE = "./res/showButton.png";
	private static String HIDE_BUTTON_TEXTURE_FILE = "./res/hideButton.png";
	
	// constructor
	public LessonPanel(Loader loader, float screenWidth, float screenHeight, float z) {
		
		this.showPanel = true;
		this.x = 330f;
		this.y = -55f;
		this.z = z - 100f;
		
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		
		// **************************************************
					
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
					
		// **************************************************
					
			
		// lesson panel
		lessonPanelWidth = 300f;
		lessonPanelHeight = 450f;
			
		float[] verticesPanel = Entity.getVertices(lessonPanelWidth, lessonPanelHeight, z);
			
		int textureID = loader.loadTexture(LESSON_PANEL_TEXTURE_FILE);
		Model lessonPanelModel = loader.loadToVAO(verticesPanel, texCoords, indices, textureID);
					
		Vector3f position = new Vector3f(x, y, this.z);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
			
		lessonPanel = new GUIComponent(lessonPanelModel, position, rotation, scale);
			
		guiComponents.add(lessonPanel);
			
		// **************************************************
		
		
		// show hide lesson panel button
		float showHideButtonWidth = 160f;
		float showHideButtonHeight = 40f;
			
		float[] vertices = Entity.getVertices(showHideButtonWidth, showHideButtonHeight, z);
			
		float buttonX = x + lessonPanelWidth/2 - showHideButtonWidth/2;
		float buttonY = y + lessonPanelHeight/2 + 10f + showHideButtonHeight/2;
							
		position = new Vector3f(buttonX, buttonY, this.z + 0.01f);
		rotation = new Vector3f(0,0,0);
		scale = 1f;
							
		textureID = loader.loadTexture(SHOW_BUTTON_TEXTURE_FILE);
		showButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
			
		textureID = loader.loadTexture(HIDE_BUTTON_TEXTURE_FILE);
		hideButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
					
		showHideButton = new Button(hideButtonModel, position, rotation, scale, 
				showHideButtonWidth, showHideButtonHeight); 
							
		guiComponents.add(showHideButton);
		
		// **************************************************
		
			
		// left button
			
		float leftRightButtonWidth = 40f;
		float leftRightButtonHeight = 40f;
			
		float[] verticesLeftRightButtons = Entity.getVertices(leftRightButtonWidth, leftRightButtonHeight, z);
			
		buttonX = x - lessonPanelWidth/2 + 10f + leftRightButtonWidth/2;
		buttonY = y - lessonPanelHeight/2 + 10f + leftRightButtonHeight/2;
							
		position = new Vector3f(buttonX, buttonY, this.z + 0.01f);
		rotation = new Vector3f(0,0,0);
		scale = 1f;
							
		textureID = loader.loadTexture(LEFT_BUTTON_ENABLED_TEXTURE_FILE);
		leftButtonEnabledModel = loader.loadToVAO(verticesLeftRightButtons, texCoords, indices, textureID);
			
		textureID = loader.loadTexture(LEFT_BUTTON_DISABLED_TEXTURE_FILE);
		leftButtonDisabledModel = loader.loadToVAO(verticesLeftRightButtons, texCoords, indices, textureID);
					
		leftButton = new Button(leftButtonEnabledModel, position, rotation, scale, 
				leftRightButtonWidth, leftRightButtonHeight); 
							
		guiComponents.add(leftButton);
					
		// **************************************************
			
			
		// right button
					
		buttonX = x + lessonPanelWidth/2 - 10f - leftRightButtonWidth/2;
		buttonY = y - lessonPanelHeight/2 + 10f + leftRightButtonHeight/2;
									
		position = new Vector3f(buttonX, buttonY, this.z + 0.01f);
									
		textureID = loader.loadTexture(RIGHT_BUTTON_ENABLED_TEXTURE_FILE);
		rightButtonEnabledModel = loader.loadToVAO(verticesLeftRightButtons, texCoords, indices, textureID);
					
		textureID = loader.loadTexture(RIGHT_BUTTON_DISABLED_TEXTURE_FILE);
		rightButtonDisabledModel = loader.loadToVAO(verticesLeftRightButtons, texCoords, indices, textureID);
							
		rightButton = new Button(rightButtonEnabledModel, position, rotation, scale, 
			leftRightButtonWidth, leftRightButtonHeight); 
									
		guiComponents.add(rightButton);
							
		// **************************************************
		
	}
	
	/**
	 * Renders the objects of the lesson panel.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		if (showPanel)
			renderer.renderGUI(guiComponents);
		else
			renderer.render(showHideButton);
	}
	
	/**
	 * Shows or hides the lesson panel.
	 */
	public void showHidePanel() {
		
		showPanel = !showPanel;
		
		// show
		if (showPanel) {
			
			showHideButton.setModel(hideButtonModel);
			leftButton.setEnabled(true);
			leftButton.setEnabled(true);
		}
		
		// hide
		else {
			
			showHideButton.setModel(showButtonModel);
			leftButton.setEnabled(false);
			leftButton.setEnabled(false);
		}
	}
	
	/**
	 * Returns the left button.
	 * 
	 * @return leftButton
	 */
	public Button getLeftButton() {
		return leftButton;
	}

	/**
	 * Returns the right button.
	 * 
	 * @return rightButton
	 */
	public Button getRightButton() {
		return rightButton;
	}
	
	/**
	 * Returns the show hide button.
	 * 
	 * @return showHideButton
	 */
	public Button getShowHideButton() {
		return showHideButton;
	}
}
