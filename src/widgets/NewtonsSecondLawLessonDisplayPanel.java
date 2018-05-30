package widgets;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import renderEngine.Renderer;
import textRender.Text;

/**
 * This class contains the components of the Newton's 
 * Second Law lesson display panel, which allows the 
 * user to view and edit the properties of the object 
 * in the simulation.
 * 
 * This display panel allows the user to view the net force, 
 * mass, and acceleration of the object in the simulation. The 
 * user can change the mass and see its effect on the object's
 * acceleration.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Wednesday, May 23rd, 2018
 */
public class NewtonsSecondLawLessonDisplayPanel {

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
	private Label netForceLabel;
	private Label accelerationLabel;
	
	private Text timeText;
	private Text massText;
	private Text netForceText;
	private Text accelerationText;
	
	private Label timeBlankLabel;
	private Label massBlankLabel;
	private Label netForceBlankLabel;
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
	public static final String MASS_LABEL_TEXTURE_FILE = "./res/massLabel.png";
	public static final String NET_FORCE_LABEL_TEXTURE_FILE = "./res/netForceLabel.png";
	public static final String ACCELERATION_LABEL_TEXTURE_FILE = "./res/accelerationLabel.png";
	
	public static final String BLANK_LABEL_TEXTURE_FILE = "./res/blankLabel.png";
	
	// constructor
	public NewtonsSecondLawLessonDisplayPanel(Loader loader, float x, float y, float z) {
		
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
		
		
		// net force label		
		offsetY += labelHeight + 10f;
		
		textureID = loader.loadTexture(NET_FORCE_LABEL_TEXTURE_FILE);
		model = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
				
		netForceLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
				
		guiComponents.add(netForceLabel);
		
		
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
		
		
		offsetX += 83f + buttonWidth + 3f;
		
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.01f);
		
		increaseMassButton = new Button(increaseButtonEnabledModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents.add(increaseMassButton);
		
		// **************************************************
		
		
		float textWidth = 20f;
		float textHeight = 20f;
				
		// time text
				
		float textX = timeLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		float textY = timeLabel.getPosition().y - labelHeight/2;
				
		timeText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
				
		// mass text
				
		textX = massLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = massLabel.getPosition().y - labelHeight/2;
						
		massText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
				
		// net force text
				
		textX = netForceLabel.getPosition().x + labelWidth/2 + 3f + buttonWidth + 3f;
		textY = netForceLabel.getPosition().y - labelHeight/2;
								
		netForceText = new Text("0", textX, textY, z + 0.01f, textWidth, textHeight, loader);
				
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
						
		// mass blank label
		offsetX = 10f + labelWidth + 5f + buttonWidth + 3f + blanklabelWidth/2;
		offsetY = 10f + labelHeight/2;
				
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
						
		massBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
						
		guiComponents.add(massBlankLabel);
				
		// net force blank label
		offsetY += 10f + labelHeight;
								
		position = new Vector3f(leftOfDisplay + offsetX, topOfDisplay - offsetY, z + 0.005f);
										
		netForceBlankLabel = new Label(model, position, rotation, scale, labelWidth, labelHeight);
										
		guiComponents.add(netForceBlankLabel);
				
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
		renderer.renderGUI(massText.getGUIlist());
		renderer.renderGUI(netForceText.getGUIlist());
		renderer.renderGUI(accelerationText.getGUIlist());
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
	
	/**
	 * Updates the text for time.
	 * 
	 * @param time
	 */
	public void updateTimeText(float time) {
		
		if (time <= 999.9)
			timeText.changeStr(floatToText(time));
	}
	
	/**
	 * Updates the text for mass.
	 * 
	 * @param mass
	 */
	public void updateMassText(float mass) {
		
		if (mass <= 999.9 && mass >= -999.9)		
			massText.changeStr(floatToText(mass));
	}
	
	/**
	 * Updates the text for net force.
	 * 
	 * @param netForce
	 */
	public void updateNetForceText(float netForce) {
		
		if (netForce <= 999.9 && netForce >= -999.9) 			
			netForceText.changeStr(floatToText(netForce));
	}
	
	/**
	 * Updates the text for acceleration.
	 * 
	 * @param acceleration
	 */
	public void updateAccelerationText(float acceleration) {
		
		if (acceleration <= 999.9 && acceleration >= -999.9)
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
				
		for (int i = 0; i < 6 - temp.length(); i ++)
			s += " ";
				
		s += temp;
		
		return s;
	}
	
}
