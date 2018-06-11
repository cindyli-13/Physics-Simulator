package widgets;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;
import textRender.Text;

/**
 * This class contains the components of the motion lesson
 * display panel, which allows the user to view and edit the 
 * properties of the object in the simulation.
 * 
 * This display panel allows the user to view the horizontal position, 
 * velocity, and acceleration of the object in the simulation. The user 
 * can change the velocity and acceleration magnitudes.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, May 22nd, 2018
 */
public class MotionLessonDisplayPanel {

	// instance variables
	private ArrayList<GUIComponent> guiComponents;
		
	private Model increaseButtonEnabledModel;
	private Model increaseButtonDisabledModel;
	private Model decreaseButtonEnabledModel;
	private Model decreaseButtonDisabledModel;
		
	private Button increaseVelocityButton;
	private Button decreaseVelocityButton;
	
	private Button increaseAccelerationButton;
	private Button decreaseAccelerationButton;
	
	private Label timeLabel;
	private Label positionLabel;
	private Label velocityLabel;
	private Label accelerationLabel;
	
	private Text timeText;
	private Text positionText;
	private Text velocityText;
	private Text accelerationText;
	
	private Label timeBlankLabel;
	private Label positionBlankLabel;
	private Label velocityBlankLabel;
	private Label accelerationBlankLabel;
		
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
	public static final String POSITION_LABEL_TEXTURE_FILE = "./res/positionLabel.png";
	public static final String VELOCITY_LABEL_TEXTURE_FILE = "./res/velocityLabel.png";
	public static final String ACCELERATION_LABEL_TEXTURE_FILE = "./res/accelerationLabel.png";
	
	public static final String BLANK_LABEL_TEXTURE_FILE = "./res/blankLabel.png";
	
	/**
	 * Creates the lesson display panel.
	 * 
	 * @param loader
	 * @param x
	 * @param y
	 * @param z
	 */
	public MotionLessonDisplayPanel(Loader loader, float x, float y, float z) {
		
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
		
		
		// position label
		vertices = Entity.getVertices(labelWidth, labelHeight, z);
		
		textureID = loader.loadTexture(POSITION_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		positionLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
		
		guiComponents.add(positionLabel);
		
		
		// velocity label		
		offsetY += labelHeight + 10f;
		
		textureID = loader.loadTexture(VELOCITY_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
				
		velocityLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
				
		guiComponents.add(velocityLabel);
		
		
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
		
		
		// for velocity
		offsetX = 10f + labelWidth + 5f + buttonWidth/2;
		offsetY = 10f + labelHeight/2 + labelHeight + 10f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		decreaseVelocityButton = new Button(decreaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(decreaseVelocityButton);
		
		
		offsetX += 83f + buttonWidth + 3f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		increaseVelocityButton = new Button(increaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(increaseVelocityButton);
		
		
		// for acceleration
		offsetX = 10f + labelWidth + 5f + buttonWidth/2;
		offsetY += 10f + labelHeight;
				
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
				
		decreaseAccelerationButton = new Button(decreaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
				
		guiComponents.add(decreaseAccelerationButton);
				
				
		offsetX += 83f + buttonWidth + 3f;
				
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
				
		increaseAccelerationButton = new Button(increaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
				
		guiComponents.add(increaseAccelerationButton);
		
		// **************************************************
		
		
		float textWidth = 20f;
		float textHeight = 20f;
		
		// time text
		
		float textX = timeLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		float textY = timeLabel.getPosition().y - labelHeight/2;
		
		timeText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
		
		// position text
		
		textX = positionLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = positionLabel.getPosition().y - labelHeight/2;
				
		positionText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
		
		// velocity text
		
		textX = velocityLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = velocityLabel.getPosition().y - labelHeight/2;
						
		velocityText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
		
		// acceleration text
		
		textX = accelerationLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = accelerationLabel.getPosition().y - labelHeight/2;
								
		accelerationText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
		
		// **************************************************
		
		// blank labels
				
		float blanklabelWidth = 80f;
		float blanklabelHeight = 20f;
		
		vertices = Entity.getVertices(blanklabelWidth, blanklabelHeight, z);
				
		textureID = loader.loadTexture(BLANK_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		// position blank label
		offsetX = 10f + labelWidth + 5f + buttonWidth + 3f + blanklabelWidth/2;
		offsetY = 10f + labelHeight/2;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
				
		positionBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
				
		guiComponents.add(positionBlankLabel);
		
		// velocity blank label
		offsetY += 10f + labelHeight;
						
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
								
		velocityBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
								
		guiComponents.add(velocityBlankLabel);
		
		// acceleration blank label
		offsetY += 10f + labelHeight;
								
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
										
		accelerationBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
										
		guiComponents.add(accelerationBlankLabel);
				
		// time blank label
		offsetY += 15f + labelHeight;
								
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
										
		timeBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
										
		guiComponents.add(timeBlankLabel);
	}
	
	/**
	 * Renders the objects of the display panel.
	 * 
	 * @param renderer		the renderer
	 */
	public void render (Renderer renderer) {
		
		renderer.renderGUI(guiComponents);
		renderer.renderGUI(timeText.getGUIlist());
		renderer.renderGUI(positionText.getGUIlist());
		renderer.renderGUI(velocityText.getGUIlist());
		renderer.renderGUI(accelerationText.getGUIlist());
	}

	/**
	 * Returns the increase velocity button.
	 * 
	 * @return increaseVelocityButton
	 */
	public Button getIncreaseVelocityButton() {
		return increaseVelocityButton;
	}

	/**
	 * Returns the decrease velocity button.
	 * 
	 * @return decreaseVelocityButton
	 */
	public Button getDecreaseVelocityButton() {
		return decreaseVelocityButton;
	}

	/**
	 * Returns the increase acceleration button.
	 * 
	 * @return increaseAccelerationButton
	 */
	public Button getIncreaseAccelerationButton() {
		return increaseAccelerationButton;
	}

	/**
	 * Returns the decrease acceleration button.
	 * 
	 * @return decreaseAccelerationButton
	 */
	public Button getDecreaseAccelerationButton() {
		return decreaseAccelerationButton;
	}
	
	/**
	 * Sets the state of the increase velocity button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setIncreaseVelocityButtonState(boolean enabled) {
		
		if (enabled) {
			
			increaseVelocityButton.setEnabled(true);
			increaseVelocityButton.setModel(increaseButtonEnabledModel);
		}
		
		else {
			
			increaseVelocityButton.setEnabled(false);
			increaseVelocityButton.setModel(increaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the decrease velocity button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setDecreaseVelocityButtonState(boolean enabled) {
		
		if (enabled) {
			
			decreaseVelocityButton.setEnabled(true);
			decreaseVelocityButton.setModel(decreaseButtonEnabledModel);
		}
		
		else {
			
			decreaseVelocityButton.setEnabled(false);
			decreaseVelocityButton.setModel(decreaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the increase acceleration button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setIncreaseAccelerationButtonState(boolean enabled) {
		
		if (enabled) {
			
			increaseAccelerationButton.setEnabled(true);
			increaseAccelerationButton.setModel(increaseButtonEnabledModel);
		}
		
		else {
			
			increaseAccelerationButton.setEnabled(false);
			increaseAccelerationButton.setModel(increaseButtonDisabledModel);
		}
	}
	
	/**
	 * Sets the state of the decrease acceleration button.
	 * 
	 * @param enabled  true if enabled, false if disabled
	 */
	public void setDecreaseAccelerationButtonState(boolean enabled) {
		
		if (enabled) {
			
			decreaseAccelerationButton.setEnabled(true);
			decreaseAccelerationButton.setModel(decreaseButtonEnabledModel);
		}
		
		else {
			
			decreaseAccelerationButton.setEnabled(false);
			decreaseAccelerationButton.setModel(decreaseButtonDisabledModel);
		}
	}
	
	/**
	 * Updates the text for time.
	 * 
	 * @param time
	 */
	public void updateTimeText(float time) {
		
		if (time <= 999.99)
			timeText.changeStr(floatToText(time));
	}
	
	/**
	 * Updates the text for position.
	 * 
	 * @param position
	 */
	public void updatePositionText(float position) {
		
		if (position <= 999.99 && position >= -999.99)		
			positionText.changeStr(floatToText(position));
	}
	
	/**
	 * Updates the text for velocity.
	 * 
	 * @param velocity
	 */
	public void updateVelocityText(float velocity) {
		
		if (velocity <= 999.99 && velocity >= -999.99) 			
			velocityText.changeStr(floatToText(velocity));
	}
	
	/**
	 * Updates the text for acceleration.
	 * 
	 * @param acceleration
	 */
	public void updateAccelerationText(float acceleration) {
		
		if (acceleration <= 999.99 && acceleration >= -999.99)
			accelerationText.changeStr(floatToText(acceleration));
	}
	
	/**
	 * Converts a float to text (a string).
	 * @param f
	 */
	private String floatToText(float f) {
		
		String s = "";
		
		// round float to 1 decimal places
		f = Math.round(f * 10.0f) / 10.0f;
		
		String temp = Float.toString(f);
				
		if (f < 0f) {
			s += "-";
			temp = temp.substring(1);
		}
		else
			s += " ";
				
		// add spaces before the string
		for (int i = 0; i < 6 - temp.length(); i ++)
			s += " ";
				
		s += temp;
		
		return s;
	}
	
}
