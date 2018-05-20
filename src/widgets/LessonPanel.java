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
	private ArrayList<ArrayList<GUIComponent>> lessons;
		
	private Model leftButtonEnabledModel;
	private Model leftButtonDisabledModel;
	private Model rightButtonEnabledModel;
	private Model rightButtonDisabledModel;
	
	private Model showButtonModel;
	private Model hideButtonModel;
	
	private Model motionLabelModel;
	private Model projectileMotionLabelModel;
	private Model newtonsSecondLawLabelModel;
	private Model forceOfGravityLabelModel;
	private Model frictionLabelModel;
		
	private Button leftButton;
	private Button rightButton;
	
	private Button showHideButton;
	
	private Label title;
		
	private GUIComponent lessonPanel;
		
	private float lessonPanelWidth;
	private float lessonPanelHeight;
		
	private float x;
	private float y;
	private float z;
	
	private boolean showPanel;
	
	// 0 = motion
	// 1 = projectile motion
	// 2 = Newton's Second Law
	// 3 = force of gravity
	// 4 = friction
	private int lessonIndex;
	private int pageIndex;
	
	// static variables
	private static String LESSON_PANEL_TEXTURE_FILE = "./res/lessonPanel.png";
	private static String LEFT_BUTTON_ENABLED_TEXTURE_FILE = "./res/leftButtonEnabled.png";
	private static String LEFT_BUTTON_DISABLED_TEXTURE_FILE = "./res/leftButtonDisabled.png";
	private static String RIGHT_BUTTON_ENABLED_TEXTURE_FILE = "./res/rightButtonEnabled.png";
	private static String RIGHT_BUTTON_DISABLED_TEXTURE_FILE = "./res/rightButtonDisabled.png";
	private static String SHOW_BUTTON_TEXTURE_FILE = "./res/showButton.png";
	private static String HIDE_BUTTON_TEXTURE_FILE = "./res/hideButton.png";
	
	public static String MOTION_LABEL_TEXTURE_FILE = "./res/motionLabel.png";
	public static String PROJECTILE_MOTION_LABEL_TEXTURE_FILE = "./res/projectileMotionLabel.png";
	public static String NEWTONS_SECOND_LAW_LABEL_TEXTURE_FILE = "./res/newtonsSecondLawLabel.png";
	public static String FORCE_OF_GRAVITY_LABEL_TEXTURE_FILE = "./res/forceOfGravityLabel.png";
	public static String FRICTION_LABEL_TEXTURE_FILE = "./res/frictionLabel.png";
	
	public static String MOTION_LESSON_1_TEXTURE_FILE = "./res/motionLesson1.png";
	public static String MOTION_LESSON_2_TEXTURE_FILE = "./res/motionLesson2.png";
	public static String MOTION_LESSON_3_TEXTURE_FILE = "./res/motionLesson3.png";
	
	public static String PROJECTILE_MOTION_LESSON_1_TEXTURE_FILE = "./res/projectileMotionLesson1.png";
	public static String PROJECTILE_MOTION_LESSON_2_TEXTURE_FILE = "./res/projectileMotionLesson2.png";
	
	// constructor
	public LessonPanel(Loader loader, float screenWidth, float screenHeight, float z) {
		
		this.lessonIndex = -1;
		this.pageIndex = 0;
		this.showPanel = true;
		this.x = 330f;
		this.y = -55f;
		this.z = z;
		
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		
		// **************************************************
					
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		
		// initialize lessons array list of array lists
		lessons = new ArrayList<ArrayList<GUIComponent>>();
					
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
		
		
		// title label
		float titleWidth = 160;
		float titleHeight = 40;
		
		vertices = Entity.getVertices(titleWidth, titleHeight, z);
		
		float labelX = x;
		float labelY = y + lessonPanelHeight/2 - 6f - titleHeight/2;
									
		position = new Vector3f(labelX, labelY, this.z + 0.01f);
									
		textureID = loader.loadTexture(MOTION_LABEL_TEXTURE_FILE);
		motionLabelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
					
		textureID = loader.loadTexture(PROJECTILE_MOTION_LABEL_TEXTURE_FILE);
		projectileMotionLabelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
							
		textureID = loader.loadTexture(NEWTONS_SECOND_LAW_LABEL_TEXTURE_FILE);
		newtonsSecondLawLabelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
					
		textureID = loader.loadTexture(FORCE_OF_GRAVITY_LABEL_TEXTURE_FILE);
		forceOfGravityLabelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(FRICTION_LABEL_TEXTURE_FILE);
		frictionLabelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		title = new Label(motionLabelModel, position, rotation, scale, 
				titleWidth, titleHeight); 
									
		guiComponents.add(title);
		
		// **************************************************
		
		
		// lesson labels
		
		float labelWidth = 285f;
		float labelHeight = 350f;
		
		vertices = Entity.getVertices(labelWidth, labelHeight, z);
		
		labelX = x;
		labelY = y + 5f;
									
		position = new Vector3f(labelX, labelY, this.z + 0.01f);
		
		// motion lesson
		lessons.add(new ArrayList<GUIComponent>());
		
		textureID = loader.loadTexture(MOTION_LESSON_1_TEXTURE_FILE);
		Model labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		lessons.get(0).add(new Label(labelModel, position, rotation, scale, 
				labelWidth, labelHeight));
		
		textureID = loader.loadTexture(MOTION_LESSON_2_TEXTURE_FILE);
		labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		lessons.get(0).add(new Label(labelModel, position, rotation, scale, 
				labelWidth, labelHeight));
		
		textureID = loader.loadTexture(MOTION_LESSON_3_TEXTURE_FILE);
		labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		lessons.get(0).add(new Label(labelModel, position, rotation, scale, 
				labelWidth, labelHeight));
		
		// projectile motion lesson
		lessons.add(new ArrayList<GUIComponent>());
		
		textureID = loader.loadTexture(PROJECTILE_MOTION_LESSON_1_TEXTURE_FILE);
		labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		lessons.get(1).add(new Label(labelModel, position, rotation, scale, 
				labelWidth, labelHeight));
		
		textureID = loader.loadTexture(PROJECTILE_MOTION_LESSON_2_TEXTURE_FILE);
		labelModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		lessons.get(1).add(new Label(labelModel, position, rotation, scale, 
				labelWidth, labelHeight));
	}
	
	/**
	 * Renders the objects of the lesson panel.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		if (showPanel) {
			renderer.renderGUI(guiComponents);
			
			renderer.render(lessons.get(lessonIndex).get(pageIndex));
		}
		
		else {
			renderer.render(showHideButton);
		}
	}
	
	/**
	 * Shows or hides the lesson panel.
	 */
	public void showHidePanel() {
		
		showPanel = !showPanel;
		
		// show
		if (showPanel) {
			
			showHideButton.setModel(hideButtonModel);
			
			if (leftButton.getModel().equals(leftButtonEnabledModel))
				leftButton.setEnabled(true);
			
			if (rightButton.getModel().equals(rightButtonEnabledModel))
				rightButton.setEnabled(true);
		}
		
		// hide
		else {
			
			showHideButton.setModel(showButtonModel);
			leftButton.setEnabled(false);
			rightButton.setEnabled(false);
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
	
	/**
	 * Sets up the current lesson.
	 * 
	 * @param currentLesson
	 */
	public void setLesson(int lessonIndex) {
		
		this.lessonIndex = lessonIndex;
		pageIndex = 0;
		showPanel = true;
		
		switch (lessonIndex) {
			case 0:
				title.setModel(motionLabelModel);
				break;
			case 1:
				title.setModel(projectileMotionLabelModel);
				break;
			case 2:
				title.setModel(newtonsSecondLawLabelModel);
				break;
			case 3:
				title.setModel(forceOfGravityLabelModel);
				break;
			case 4:
				title.setModel(frictionLabelModel);
				break;
		}
		
		leftButton.setEnabled(false);
		leftButton.setModel(leftButtonDisabledModel);
		
		rightButton.setEnabled(true);
		rightButton.setModel(rightButtonEnabledModel);
	}
	
	/**
	 * Updates the page index.
	 * 
	 * @param left	true if left button is clicked, 
	 * false if right button is clicked.
	 */
	public void updatePageIndex(boolean left) {
		
		if (left) {
			
			pageIndex--;
			
			if (pageIndex <= 0) {
				
				pageIndex = 0;
				leftButton.setEnabled(false);
				leftButton.setModel(leftButtonDisabledModel);
			}
			
			rightButton.setEnabled(true);
			rightButton.setModel(rightButtonEnabledModel);
		}
		
		else {
			
			pageIndex++;
			
			if (pageIndex >= lessons.get(lessonIndex).size() - 1) {
				
				pageIndex = lessons.get(lessonIndex).size() - 1;
				rightButton.setEnabled(false);
				rightButton.setModel(rightButtonDisabledModel);
			}
			
			leftButton.setEnabled(true);
			leftButton.setModel(leftButtonEnabledModel);
		}
	}
	
}
