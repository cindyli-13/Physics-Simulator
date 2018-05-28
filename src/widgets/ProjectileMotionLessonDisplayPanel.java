package widgets;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;
import textRender.Text;

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
	
	private Text timeText;
	private Text positionXText;
	private Text velocityXText;
	private Text accelerationXText;
	private Text positionYText;
	private Text velocityYText;
	private Text accelerationYText;
	
	private Label timeBlankLabel;
	private Label positionXBlankLabel;
	private Label velocityXBlankLabel;
	private Label accelerationXBlankLabel;
	private Label positionYBlankLabel;
	private Label velocityYBlankLabel;
	private Label accelerationYBlankLabel;
		
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
	
	public static final String BLANK_LABEL_TEXTURE_FILE = "./res/blankLabel.png";
	
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
		
		
		offsetX += 40f + buttonWidth + 3f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		increaseVelocityXButton = new Button(increaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(increaseVelocityXButton);
		
		
		// for velocity y
		offsetX = 10f + labelWidth/2 + 110f + labelWidth + labelWidth/2 + buttonWidth/2 + 5f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		decreaseVelocityYButton = new Button(decreaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(decreaseVelocityYButton);
		
		
		
		offsetX += 40f + buttonWidth + 3f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		increaseVelocityYButton = new Button(increaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(increaseVelocityYButton);
		
		// **************************************************
		
		
		float textWidth = 20f;
		float textHeight = 20f;
				
		// time text
				
		float textX = timeLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		float textY = timeLabel.getPosition().y - labelHeight/2;
				
		timeText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
				
		// position x text
				
		textX = positionXLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = positionXLabel.getPosition().y - labelHeight/2;
						
		positionXText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
				
		// velocity x text
				
		textX = velocityXLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = velocityXLabel.getPosition().y - labelHeight/2;
								
		velocityXText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
				
		// acceleration x text
		
		textX = accelerationXLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = accelerationXLabel.getPosition().y - labelHeight/2;
										
		accelerationXText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
		
		// position y text
		
		textX = positionYLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = positionYLabel.getPosition().y - labelHeight/2;
								
		positionYText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
						
		// velocity y text
						
		textX = velocityYLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = velocityYLabel.getPosition().y - labelHeight/2;
										
		velocityYText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
						
		// acceleration y text
		
		textX = accelerationYLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = accelerationYLabel.getPosition().y - labelHeight/2;
												
		accelerationYText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
		
		// **************************************************
		
		// blank labels
			
		float blanklabelWidth = 37f;
		float blanklabelHeight = 20f;
				
		vertices = Entity.getVertices(blanklabelWidth, blanklabelHeight, z);
				
		textureID = loader.loadTexture(BLANK_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
						
		// position x blank label
		offsetX = 10f + labelWidth + 5f + buttonWidth + 3f + blanklabelWidth/2;
		offsetY = 10f + labelHeight/2;
				
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
						
		positionXBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
						
		guiComponents.add(positionXBlankLabel);
				
		// velocity x blank label
		offsetY += 10f + labelHeight;
								
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
										
		velocityXBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
								
		guiComponents.add(velocityXBlankLabel);
				
		// acceleration x blank label
		offsetY += 10f + labelHeight;
										
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
												
		accelerationXBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
												
		guiComponents.add(accelerationXBlankLabel);
						
		// time blank label
		offsetY += 15f + labelHeight;
										
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
												
		timeBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
												
		guiComponents.add(timeBlankLabel);
		
		// position y blank label
		offsetX = 10f + 2 * labelWidth + 110f + 5f + buttonWidth + 3f + blanklabelWidth/2;
		offsetY = 10f + labelHeight/2;
						
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
								
		positionYBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
								
		guiComponents.add(positionYBlankLabel);
						
		// velocity y blank label
		offsetY += 10f + labelHeight;
										
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
												
		velocityYBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
										
		guiComponents.add(velocityYBlankLabel);
						
		// acceleration y blank label
		offsetY += 10f + labelHeight;
												
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
														
		accelerationYBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
														
		guiComponents.add(accelerationYBlankLabel);
	}
	
	/**
	 * Renders the objects of the display panel.
	 * 
	 * @param renderer		the renderer
	 */
	public void render (Renderer renderer) {
		
		renderer.renderGUI(guiComponents);
		renderer.renderGUI(timeText.getGUIlist());
		renderer.renderGUI(positionXText.getGUIlist());
		renderer.renderGUI(velocityXText.getGUIlist());
		renderer.renderGUI(accelerationXText.getGUIlist());
		renderer.renderGUI(positionYText.getGUIlist());
		renderer.renderGUI(velocityYText.getGUIlist());
		renderer.renderGUI(accelerationYText.getGUIlist());
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
	
	/**
	 * Updates the text for time.
	 * 
	 * @param time
	 */
	public void updateTimeText(int time) {
		
		if (time <= 999)
			timeText.changeStr(intToText(time));
	}
	
	/**
	 * Updates the text for position x.
	 * 
	 * @param positionX
	 */
	public void updatePositionXText(int positionX) {
		
		if (positionX <= 999 && positionX >= -999)		
			positionXText.changeStr(intToText(positionX));
	}
	
	/**
	 * Updates the text for velocity x.
	 * 
	 * @param velocityX
	 */
	public void updateVelocityXText(int velocityX) {
		
		if (velocityX <= 999 && velocityX >= -999) 			
			velocityXText.changeStr(intToText(velocityX));
	}
	
	/**
	 * Updates the text for acceleration x.
	 * 
	 * @param accelerationX
	 */
	public void updateAccelerationXText(int accelerationX) {
		
		if (accelerationX <= 999 && accelerationX >= -999)
			accelerationXText.changeStr(intToText(accelerationX));
	}
	
	/**
	 * Updates the text for position y.
	 * 
	 * @param positionY
	 */
	public void updatePositionYText(int positionY) {
		
		if (positionY <= 999 && positionY >= -999)		
			positionYText.changeStr(intToText(positionY));
	}
	
	/**
	 * Updates the text for velocity y.
	 * 
	 * @param velocityY
	 */
	public void updateVelocityYText(int velocityY) {
		
		if (velocityY <= 999 && velocityY >= -999) 			
			velocityYText.changeStr(intToText(velocityY));
	}
	
	/**
	 * Updates the text for acceleration y.
	 * 
	 * @param accelerationY
	 */
	public void updateAccelerationYText(int accelerationY) {
		
		if (accelerationY <= 999 && accelerationY >= -999)
			accelerationYText.changeStr(intToText(accelerationY));
	}
	
	/**
	 * Converts an integer to text (a string).
	 * @param n
	 */
	private String intToText(int n) {
		
		String s = "";
		String temp = Integer.toString(n);
				
		if (n < 0) {
			s += "-";
			temp = temp.substring(1);
		}
		else
			s += " ";
				
		for (int i = 0; i < 3 - temp.length(); i ++)
			s += " ";
				
		s += temp;
		
		return s;
	}
}
