package screens;

import static org.lwjgl.glfw.GLFW.*;

import java.io.File;
import java.nio.DoubleBuffer;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import io.IO;
import main.Main;
import objects.Cannon;
import objects.Circle;
import objects.Entity;
import objects.Loader;
import objects.Model;
import objects.Rectangle;
import renderEngine.Renderer;
import textRender.Text;
import widgets.Button;
import widgets.GUIComponent;
import widgets.Label;
import widgets.PopUpBox;
import widgets.Sidebar;
import widgets.SimulationButton;
import widgets.SimulationWindow;
import widgets.Toolbar;

/**
 * This class implements the customized screen of the 
 * physics simulator.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Thursday, April 19th, 2018
 */
public class CustomizedScreen {

	// instance variables	
	private SimulationWindow simulation;
	private Toolbar toolbar;
	private Sidebar sidebar;
	private Button saveButton;		// sButton
	private Button deleteButton;	// dButton
	private PopUpBox popUpBox;
	private Entity selectedEntity;
	
	private Label selectASimLabel;
	private Label title;
	
	private Text currentSimName;
	
	private float selectedEntityOriginalX;
	private float selectedEntityOriginalY;
	
	private ArrayList<GUIComponent> guiComponents;
	
	private Loader loader;
	
	private float z;
	
	// specifies the program:
	// 0 = default
	// 1 = new entity created
	// 2 = pop-up box created
	// 3 = entity selected
	private int program;
	
	private int currentSim;
	
	private long window;
	private float screenWidth;
	private float screenHeight;
	
	// static variables
	public static final String SAVE_BUTTON_TEXTURE_FILE = "./res/Save.png";
	public static final String DELETE_BUTTON_TEXTURE_FILE = "./res/Delete.png";
	public static final String SELECT_A_SIM_LABEL_TEXTURE_FILE = "./res/selectASimulationLabel.png";
	public static final String TITLE_TEXTURE_FILE = "./res/customizedLabel.png";
		
	/**
	 * Creates the customized screen.
	 * 
	 * @param window			the window ID
	 * @param loader			the loader object
	 * @param screenWidth		the width of the screen
	 * @param screenHeight		the height of the screen
	 * @param z					the z-value of the components of the screen
	 * @param files				the data files that contain data for each custom simulation
	 */
	public CustomizedScreen(long window, Loader loader, float screenWidth, float screenHeight, float z, 
			String[] files) {
					
		// simulation window
		simulation = new SimulationWindow(window, loader, screenWidth, screenHeight, z);
		
		// toolbar
		toolbar = new Toolbar(loader, z);
		
		// sidebar
		sidebar = new Sidebar(loader, z, files, true);
		
		// current simulation name text
		currentSimName = new Text("", 270, 180, z - 0.5f, 40f, 40f, loader);
		
		// save button
		float buttonX = 250;
		float buttonY = 260;
		
		float buttonWidth = 60f;
		float buttonHeight = 40f;
		
		float[] vertices = Entity.getVertices(buttonWidth, buttonHeight, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
				
		Vector3f position = new Vector3f(buttonX, buttonY, z - 100f);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		int textureID = loader.loadTexture(SAVE_BUTTON_TEXTURE_FILE);
		Model sButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		saveButton = new Button(sButtonModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		// delete button
		buttonX += buttonWidth + 4f;
		
		position = new Vector3f(buttonX, buttonY, z - 100f);
		
		textureID = loader.loadTexture(DELETE_BUTTON_TEXTURE_FILE);
		Model dButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		deleteButton = new Button(dButtonModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		// select a simulation label
		float labelWidth = 200f;
		float labelHeight = 200f;
		
		vertices = Entity.getVertices(labelWidth, labelHeight, z);
				
		float labelX = (simulation.getMax().x + simulation.getMin().x) / 2;
		float labelY = (simulation.getMax().y + simulation.getMin().y) / 2;
		
		position = new Vector3f(labelX, labelY, z);
		
		textureID = loader.loadTexture(SELECT_A_SIM_LABEL_TEXTURE_FILE);
		Model selectionASimModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		selectASimLabel = new Label(selectionASimModel, position, rotation, scale, labelWidth, labelHeight);
		
		// title label
		labelWidth = 200f;
		labelHeight = 50f;
		
		vertices = Entity.getVertices(labelWidth, labelHeight, z - 100f);
		
		labelX = -380f;
		labelY = 175 + labelHeight/2;
		
		position = new Vector3f(labelX, labelY, z);
		
		textureID = loader.loadTexture(TITLE_TEXTURE_FILE);
		Model titleModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		title = new Label(titleModel, position, rotation, 1, labelWidth, labelHeight);
		
		
		// change button texts
		for (SimulationButton button: sidebar.getButtons()) {
			
			String name = button.getFileName().substring(18, button.getFileName().length() - 4);
			
			button.getText().changeStr(name);
			
			button.getText().updatePosition(
					-(20f/2 +0.5f*20f*button.getText().getGUIlist().size() + 8f)/2,0);
		}
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(saveButton);
		guiComponents.add(deleteButton);
		guiComponents.add(title);
		
		this.loader = loader;
		
		this.z = z;
		program = 0;
		currentSim = -1;
		
		this.window = window;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
		
	/**
	 * Renders the customized screen.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		toolbar.render(renderer);
		sidebar.render(renderer);
		simulation.render(renderer);
		renderer.renderGUI(guiComponents);
		renderer.renderGUI(currentSimName.getGUIlist());
		
		// if no simulation selected
		if (currentSim == -1)
			renderer.render(selectASimLabel);
		
		// if entity selected
		if (program == 1 || program == 3)
			moveEntity();
		
		// if pop-up box is displayed
		else if (program == 2) {
			
			float x = 0f;
			
			if (selectedEntity instanceof Rectangle) {
				
				x = ((Rectangle) selectedEntity).getWidth()/2;
			}
			else if (selectedEntity instanceof Circle) {
				x = ((Circle) selectedEntity).getRadius();
			}
			
			float offsetX = selectedEntity.getPosition().x - 
					(popUpBox.getPosition().x + popUpBox.getWidth()/2 + x + 5f);
			
			float offsetY = selectedEntity.getPosition().y - 
					(popUpBox.getPosition().y - 20f);
			
			popUpBox.update(offsetX, offsetY);
			popUpBox.render(renderer);
		}
	}
	
	/**
	 * Updates the game screen.
	 */
	public void update() {
		
		if (!simulation.isPaused())
			simulation.update(true);
	}
	
	/**
	 * Contains the logic for input handling
	 * 
	 * @param main				where the main loop is
	 * @param key				the key that was pressed
	 * @param leftClick			whether the left mouse button was pressed
	 * @param rightClick		whether the right mouse button was pressed
	 */
	public void input(Main main, int key, boolean leftClick, boolean rightClick) {
		
		// mouse input
		mouseInput(main, leftClick, rightClick);
		
		// keyboard input
		keyboardInput(key);
	}
	
	/**
	 * Contains the logic for when a mouse is clicked.
	 * 
	 * @param main				where the main loop is
	 * @param leftClick			whether the left mouse button was pressed
	 * @param rightClick		whether the right mouse button was pressed
	 */
	public void mouseInput(Main main, boolean leftClick, boolean rightClick) {
		
		// get cursor coordinate
		
		DoubleBuffer cursorPosX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer cursorPosY = BufferUtils.createDoubleBuffer(1);
		
		glfwGetCursorPos(window, cursorPosX, cursorPosY);
		
		float x = (float) cursorPosX.get(0);
		float y = (float) cursorPosY.get(0);
		
		
		// convert cursor coordinate to OpenGL world coordinate
		x -= screenWidth/2;
		y *= -1;
		y += screenHeight/2;
		
		// if left mouse button was pressed
		if (leftClick) {
			
			// place entity
			if (program == 1 || program == 3) {
				
				placeEntity();
				program = 0;
				return;
			}
			
			// pop-up box
			else if (program == 2) {
				
				// close button
				if (popUpBox.getCloseButton().getAabb().intersects(x, y)) {
					
					popUpBox = null;
					selectedEntity = null;
					program = 0;
					return;
				}
				
				// delete entity button
				else if (popUpBox.getDeleteEntityButton().getAabb().intersects(x, y)) {
					
					// find and remove entity from array list
					for (int i = 0; i < simulation.getEntities().size(); i++) {
						
						if (selectedEntity.equals(simulation.getEntities().get(i))) {
							simulation.getEntities().remove(i);
							break;
						}
					}
					
					popUpBox = null;
					selectedEntity = null;
					program = 0;
					return;
				}
				
				// increase size button
				else if (popUpBox.getIncreaseSizeButton().getAabb().intersects(x, y)) {
					
					// increase entity's scale
					selectedEntity.setScale(selectedEntity.getScale() + 5f);
					
					if (selectedEntity.getScale() > 95f) {
						
						selectedEntity.setScale(100f);
						popUpBox.setIncreaseSizeButtonState(false);
					}
					else {
						popUpBox.setDecreaseSizeButtonState(true);
					}
					
					float size = 1f;
					
					// rectangle
					if (selectedEntity instanceof Rectangle) {
						
						Rectangle r = (Rectangle) selectedEntity;
						
						r.setWidth(r.getScale());
						r.setHeight(r.getScale());
						
						r.updateAABB();
						
						size = r.getWidth();
					}
					
					// circle
					else if (selectedEntity instanceof Circle) {
						
						Circle c = (Circle) selectedEntity;
						
						c.setRadius(c.getScale()/2);
						
						size = c.getRadius() * 2;
					}
					
					// update text
					popUpBox.updateSizeText(size);
				}
				
				// decrease size button
				else if (popUpBox.getDecreaseSizeButton().getAabb().intersects(x, y)) {
					
					// decrease entity's scale
					selectedEntity.setScale(selectedEntity.getScale() - 5f);
					
					if (selectedEntity.getScale() < 35f) {
						
						selectedEntity.setScale(30f);
						popUpBox.setDecreaseSizeButtonState(false);
					}
					else {
						popUpBox.setIncreaseSizeButtonState(true);
					}
					
					float size = 1f;
					
					// rectangle
					if (selectedEntity instanceof Rectangle) {
						
						Rectangle r = (Rectangle) selectedEntity;
						
						r.setWidth(r.getScale());
						r.setHeight(r.getScale());
						
						r.updateAABB();
						
						size = r.getWidth();
					}
					
					// circle
					else if (selectedEntity instanceof Circle) {
						
						Circle c = (Circle) selectedEntity;
						
						c.setRadius(c.getScale()/2);
						
						size = c.getRadius() * 2;
					}
					
					// update text
					popUpBox.updateSizeText(size);
				}
				
				// increase velocity x button
				else if (popUpBox.getIncreaseVelocityXButton().getAabb().intersects(x, y)) {
					
					// cannon
					if (selectedEntity instanceof Cannon) {
						
						// increase cannon's stored horizontal velocity
						selectedEntity.getStoredVelocity().x += 5f;
						
						if (selectedEntity.getStoredVelocity().x > 995f) {
							
							selectedEntity.getStoredVelocity().x = 999.9f;
							popUpBox.setIncreaseVelocityXButtonState(false);
						}
						else {
							popUpBox.setDecreaseVelocityXButtonState(true);
						}
						
						// update text
						popUpBox.updateVelocityXText(selectedEntity.getStoredVelocity().x);
					}
					
					// not cannon
					else {
						
						// increase entity's horizontal velocity
						selectedEntity.getVelocity().x += 5f;
						
						if (selectedEntity.getVelocity().x > 995f) {
							
							selectedEntity.getVelocity().x = 999.9f;
							popUpBox.setIncreaseVelocityXButtonState(false);
						}
						else {
							popUpBox.setDecreaseVelocityXButtonState(true);
						}
						
						// update text
						popUpBox.updateVelocityXText(selectedEntity.getVelocity().x);
					}
				}
				
				// decrease velocity x button
				else if (popUpBox.getDecreaseVelocityXButton().getAabb().intersects(x, y)) {
					
					// cannon
					if (selectedEntity instanceof Cannon) {
						
						// decrease cannon's stored horizontal velocity
						selectedEntity.getStoredVelocity().x -= 5f;
						
						if (selectedEntity.getStoredVelocity().x < -995) {
							
							selectedEntity.getStoredVelocity().x = -999.9f;
							popUpBox.setDecreaseVelocityXButtonState(false);
						}
						else {
							popUpBox.setIncreaseVelocityXButtonState(true);
						}
						
						// update text
						popUpBox.updateVelocityXText(selectedEntity.getStoredVelocity().x);
					}
					
					// not cannon
					else {
						
						// decrease entity's horizontal velocity
						selectedEntity.getVelocity().x -= 5f;
						
						if (selectedEntity.getVelocity().x < -995) {
							
							selectedEntity.getVelocity().x = -999.9f;
							popUpBox.setDecreaseVelocityXButtonState(false);
						}
						else {
							popUpBox.setIncreaseVelocityXButtonState(true);
						}
						
						// update text
						popUpBox.updateVelocityXText(selectedEntity.getVelocity().x);
					}
				}
				
				// increase velocity y button
				else if (popUpBox.getIncreaseVelocityYButton().getAabb().intersects(x, y)) {
					
					// cannon
					if (selectedEntity instanceof Cannon) {
						
						// increase cannon's stored vertical velocity
						selectedEntity.getStoredVelocity().y += 5f;
						
						if (selectedEntity.getStoredVelocity().y > 995f) {
							
							selectedEntity.getStoredVelocity().y = 999.9f;
							popUpBox.setIncreaseVelocityYButtonState(false);
						}
						else {
							popUpBox.setDecreaseVelocityYButtonState(true);
						}
						
						// update text
						popUpBox.updateVelocityYText(selectedEntity.getStoredVelocity().y);
					}
					
					// not cannon
					else {
						
						// increase entity's vertical velocity
						selectedEntity.getVelocity().y += 5f;
						
						if (selectedEntity.getVelocity().y > 995f) {
							
							selectedEntity.getVelocity().y = 999.9f;
							popUpBox.setIncreaseVelocityYButtonState(false);
						}
						else {
							popUpBox.setDecreaseVelocityYButtonState(true);
						}
						
						// update text
						popUpBox.updateVelocityYText(selectedEntity.getVelocity().y);
					}
				}
				
				// decrease velocity y button
				else if (popUpBox.getDecreaseVelocityYButton().getAabb().intersects(x, y)) {
					
					// cannon
					if (selectedEntity instanceof Cannon) {
						
						// decrease cannon's stored vertical velocity
						selectedEntity.getStoredVelocity().y -= 5f;
						
						if (selectedEntity.getStoredVelocity().y < -995) {
							
							selectedEntity.getStoredVelocity().y = -999.9f;
							popUpBox.setDecreaseVelocityYButtonState(false);
						}
						else {
							popUpBox.setIncreaseVelocityYButtonState(true);
						}
						
						// update text
						popUpBox.updateVelocityYText(selectedEntity.getStoredVelocity().y);
					}
					
					// not cannon
					else {
						
						// decrease entity's vertical velocity
						selectedEntity.getVelocity().y -= 5f;
						
						if (selectedEntity.getVelocity().y < -995) {
							
							selectedEntity.getVelocity().y = -999.9f;
							popUpBox.setDecreaseVelocityYButtonState(false);
						}
						else {
							popUpBox.setIncreaseVelocityYButtonState(true);
						}
						
						// update text
						popUpBox.updateVelocityYText(selectedEntity.getVelocity().y);
					}
				}
				
				// increase mass button
				else if (popUpBox.getIncreaseMassButton().getAabb().intersects(x, y)) {
					
					// increase entity's mass
					selectedEntity.setMass(selectedEntity.getMass() + 1f);
					
					if (selectedEntity.getMass() > 99f) {
						
						selectedEntity.setMass(100f);
						popUpBox.setIncreaseMassButtonState(false);
					}
					else {
						popUpBox.setDecreaseMassButtonState(true);
					}
					
					// update text
					popUpBox.updateMassText(selectedEntity.getMass());
				}
				
				// decrease mass button
				else if (popUpBox.getDecreaseMassButton().getAabb().intersects(x, y)) {
					
					// decrease entity's mass
					selectedEntity.setMass(selectedEntity.getMass() - 1f);
					
					if (selectedEntity.getMass() < 2f) {
						
						selectedEntity.setMass(1f);
						popUpBox.setDecreaseMassButtonState(false);
					}
					else {
						popUpBox.setIncreaseMassButtonState(true);
					}
					
					// update text
					popUpBox.updateMassText(selectedEntity.getMass());
				}
			}
			
			else {
				
				// loop through buttons of toolbar
				for (Button button: toolbar.getButtons()) {
						
					// check if this button was clicked
					if (button.getAabb().intersects(x, y)) {
											
						// menu button
						if (button.equals(toolbar.getMenuButton())) {
								
							// pause simulation
							simulation.setPause(true);
							
							// reset to no simulations
							currentSim = -1;
							currentSimName.changeStr("");
							
							main.setCurrScreen(0);
							return;
						}
							
						// info button
						else if (button.equals(toolbar.getInfoButton())) {
								
							main.setCurrScreen(4);
							return;
						}
						
						else if (currentSim != -1) {
							
							// rectangle button
							if (button.equals(toolbar.getRectangleButton()) && simulation.isPaused()) {
									
								// generate a crate
								float sideLength = 50;
								float posX = toolbar.getRectangleButton().getPosition().x;
								float posY = toolbar.getRectangleButton().getPosition().y;
								float mass = 20;
								float e = -0.3f;
									
								selectedEntity = simulation.createCrateEntity(sideLength, posX, posY, z, 
										0, 0, mass, e);
								
								program = 1;
								return;
							}
							
							// circle button
							else if (button.equals(toolbar.getCircleButton()) && simulation.isPaused()) {
									
								// generate a ball
								float radius = 25;
								float posX = toolbar.getCircleButton().getPosition().x;
								float posY = toolbar.getCircleButton().getPosition().y;
								float mass = 20;
								float e = -0.7f;
									
								selectedEntity = simulation.createBallEntity(radius, posX, posY, z, 
										0, 0, mass, e);
								
								program = 1;
								return;
							}
							
							// cannon button
							else if (button.equals(toolbar.getCannonButton()) && simulation.isPaused()) {
								
								float sideLength = 50;
								float posX = toolbar.getCannonButton().getPosition().x;
								float posY = toolbar.getCannonButton().getPosition().y;
								
								float mass = 20;
								float e = -0.7f;
											
								selectedEntity = simulation.createCannonEntity(sideLength, posX, posY, 0, 0, 
										z, mass, e);
							
								program = 1;
								return;
							}
						}
					}
						
				}
				
				// create new simulation button
				if (sidebar.getCreateNewSimulationButton().getAabb().intersects(x, y)
						&& sidebar.getCreateNewSimulationButton().isEnabled()) {
				
					// pause simulation
					simulation.setPause(false);
					simulation.pausePlaySimulation();
					
					// ***** uses Java Swing to get user input *****
					
					// get user input
					String input = JOptionPane.showInputDialog(null, 
							"Enter a name for your simulation (8 characters max):");
					
					if (input != null) {
						
						// remove leading and trailing spaces and clip to 8 characters
						input = input.trim();
						
						if (input.length() > 8)
							input = input.substring(0, 8);
						
						// create file name
						String fileName = "./data/customized_" + input + ".txt";
						
						int validName = 0;
						
						// these character: *, \, /, :, ;, <, >, ., ?, ", |
						// cause problems with file I/O so they cannot be used in the 
						// simulation name
						
						if (input.contains("*") || input.contains("\\") || input.contains("/")
								|| input.contains(":") || input.contains(";") || input.contains("<")
								|| input.contains(">") || input.contains(".") || input.contains("?")
								|| input.contains("\"") || input.contains("|"))
							validName = 1;
						
						// else check if name is all spaces
						else if  (input.replace(" ", "").equals(""))
							validName = 1;
						
						else {
							
							// check if simulation already exists
							for (String file: sidebar.getSimulationsData()) {
								
								if (file.compareToIgnoreCase(fileName) == 0) {
									
									validName =2;
									break;
								}
							}
						}
						
						// if name is valid
						if (validName == 0) {
							
							// create simulation button
							sidebar.createSimulationButton(fileName);
							
							// change button text
							SimulationButton newButton = sidebar.getButtons().get(sidebar.getButtons().size() - 1);
							String name = newButton.getFileName().substring(18, newButton.getFileName().length() - 4);
							
							newButton.getText().changeStr(name);
							
							newButton.getText().updatePosition(
									-(20f/2 +0.5f*20f*newButton.getText().getGUIlist().size() + 8f)/2, 0);
							
							// update file
							IO.createOutputFile(fileName);
							IO.println("0");
							IO.closeOutputFile();
							
							// sort simulations
							sidebar.sortSimulations(fileName);
							
							// add to customized data file
							IO.createOutputFile("./data/customized_data_files.txt");
							IO.println(Integer.toString(sidebar.getSimulationsData().size()));
							
							for (String file: sidebar.getSimulationsData()) {
								
								IO.println(file);
							}
							IO.closeOutputFile();
							
							// change to current simulation
							simulation.getEntities().clear();

							for (int i = 0; i < sidebar.getSimulationsData().size(); i++) {
								
								if (sidebar.getSimulationsData().get(i).equals(fileName)) {
									
									currentSim = i + 1;
									break;
								}
							}
							
							// update down button state
							if (sidebar.getButtons().size() < 4)
								sidebar.getDownButton().setModel(sidebar.getDownButtonDisabledModel());
							else
								sidebar.getDownButton().setModel(sidebar.getDownButtonEnabledModel());
							
							// set current simulation name text
							
							String toDisplay = "";
							
							for (int i = 0; i < (8 - name.length()); i++)
								toDisplay += " " ; // add space
							
							toDisplay += name;
							
							currentSimName.changeStr(toDisplay);
						}
						
						// if name is not valid
						else if (validName == 1){
							
							JOptionPane.showMessageDialog(null, "That name is invalid or contains invalid "
									+ "characters (*, \\, /, :, ;, <, >, ., ?, \", |).");
						}
						
						// if name is already taken
						else {
							JOptionPane.showMessageDialog(null, "That simulation already exists.");
						}
					}
					
					return;
				}
				
				// loop through buttons of sidebar
				for (int i = 0; i < sidebar.getButtons().size(); i++) {
					
					SimulationButton button = sidebar.getButtons().get(i);
					
					// check if this button was clicked
					if (button.getAabb().intersects(x, y) && button.isEnabled()) {
					
						currentSim = i + 1;
						resetSimulation();
						
						// set current simulation name text
						
						String name = button.getText().getString();
						String toDisplay = "";
						
						for (int j = 0; j < (8 - name.length()); j++)
							toDisplay += " " ; // add space
						
						toDisplay += name;
						
						currentSimName.changeStr(toDisplay);
						
						return;
					}
					
				}
				
				// up button of sidebar
				if (sidebar.getUpButton().getAabb().intersects(x, y)) {
					
					sidebar.updateTopSimulationIndex(true);
					return;
				}
				
				// down button of sidebar
				if (sidebar.getDownButton().getAabb().intersects(x, y)) {
					
					sidebar.updateTopSimulationIndex(false);
					return;
				}
				
				// save button
				if (saveButton.getAabb().intersects(x, y)) {
					
					if (currentSim != -1) {
					
						// save data into text file
						IO.createOutputFile(sidebar.getButtons().get(currentSim - 1).getFileName());
						
						// number of entities
						IO.println(Integer.toString(simulation.getEntities().size()));
						
						// loop through entities
						for (Entity entity: simulation.getEntities()) {
								
							// line buffer
							IO.println("");
							
							// rectangle
							if (entity instanceof Rectangle) {
									
								// cannon
								if (entity instanceof Cannon) {
									
									Cannon r = (Cannon) entity;
									
									IO.println("CANNON");
									IO.println(Float.toString(r.getWidth()));
									IO.println(Float.toString(r.getPosition().x));
									IO.println(Float.toString(r.getPosition().y));
									IO.println(Float.toString(r.getStoredVelocity().x));
									IO.println(Float.toString(r.getStoredVelocity().y));
									IO.println(Float.toString(r.getMass()));
									IO.println(Float.toString(r.getCoefficientOfRestitution()));
								}
								
								// regular rectangle
								else {
									
									Rectangle r = (Rectangle) entity;
									
									IO.println("RECTANGLE");
									IO.println(Float.toString(r.getWidth()));
									IO.println(Float.toString(r.getPosition().x));
									IO.println(Float.toString(r.getPosition().y));
									IO.println(Float.toString(r.getVelocity().x));
									IO.println(Float.toString(r.getVelocity().y));
									IO.println(Float.toString(r.getMass()));
									IO.println(Float.toString(r.getCoefficientOfRestitution()));
								}
							}
								
							// circle
							else if (entity instanceof Circle) {
									
								Circle c = (Circle) entity;
									
								IO.println("CIRCLE");
								IO.println(Float.toString(c.getRadius()));
								IO.println(Float.toString(c.getPosition().x));
								IO.println(Float.toString(c.getPosition().y));
								IO.println(Float.toString(c.getVelocity().x));
								IO.println(Float.toString(c.getVelocity().y));
								IO.println(Float.toString(c.getMass()));
								IO.println(Float.toString(c.getCoefficientOfRestitution()));
							}
						}
						IO.closeOutputFile();
						
						System.out.println("Simulation saved!");
					}
					return;
				}
				
				// delete button
				if (deleteButton.getAabb().intersects(x, y)) {
					
					if (currentSim != -1) {
						
						// ask "are you sure" message
						int choice = JOptionPane.showOptionDialog(null, "Are you sure you want to delete "
								+ "this simulation?", "Delete Simulation", JOptionPane.YES_NO_OPTION, 
								JOptionPane.PLAIN_MESSAGE, null, new Object[] {"Yes", "No"}, "");
						
						// if user picks "yes"
						if (choice == 0) {
							
							// delete file
							File file = new File(sidebar.getSimulationsData().get(currentSim - 1));
							file.delete();
							sidebar.getSimulationsData().remove(currentSim - 1);
							
							// edit customized_data_files.txt data
							IO.createOutputFile("./data/customized_data_files.txt");
							IO.println(Integer.toString(sidebar.getSimulationsData().size()));
							
							for (String fileName: sidebar.getSimulationsData())
								IO.println(fileName);
							
							IO.closeOutputFile();
							
							// delete simulation button
							Button button = sidebar.getButtons().get(currentSim - 1);
							sidebar.getButtons().remove(button);
							sidebar.getGUIComponents().remove(button);
							
							// clear simulation data
							simulation.getEntities().clear();
							
							// update top simulation index
							sidebar.updateTopSimulationIndex();
							
							// update current simulation name text
							currentSimName.changeStr("");
							
							currentSim = -1;
							
							System.out.println("Simulation deleted!");
						}
					}
					return;
				}
				
				// pause-play button
				if (simulation.getPausePlayButton().getAabb().intersects(x, y) && currentSim != -1) {
					
					simulation.pausePlaySimulation();
					return;
				}
				
				// reset button
				if (simulation.getResetButton().getAabb().intersects(x, y) && currentSim != -1) {
					
					resetSimulation();
					return;
				}
				
				// selecting an object
				if (simulation.isPaused() && currentSim != -1 && program == 0) {
					
					// loop through entities of simulation
					for (Entity entity: simulation.getEntities()) {
					
						if (entity.intersects(x, y)) {
						
							// select entity
							selectedEntity = entity;
							selectedEntityOriginalX = selectedEntity.getPosition().x;
							selectedEntityOriginalY = selectedEntity.getPosition().y;
						
							program = 3;						
							return;
						}
					}
				}
				
			}
			
		}
		
		// if right mouse button was pressed
		else if (rightClick && simulation.isPaused() && currentSim != -1 && program == 0) {
			
			// loop through entities of simulation
			for (Entity entity: simulation.getEntities()) {
				
				if (entity.intersects(x, y)) {
					
					// create pop-up box
					popUpBox = createPopUpBox(entity);
					
					selectedEntity = entity;
					program = 2;
					
					return;
				}
			}
		}
			
	}
	
	/**
	 * Contains the logic for when a key is pressed.
	 * 
	 * @param key  the key that was pressed
	 */
	public void keyboardInput(int key) {
		
		if (program == 0) {
		
			// space bar
			if (key == Main.KEY_SPACE && currentSim != -1)
				simulation.pausePlaySimulation();
			
			// up
			else if (key == Main.KEY_UP)
				sidebar.updateTopSimulationIndex(true);
			
			// down
			else if (key == Main.KEY_DOWN)
				sidebar.updateTopSimulationIndex(false);
			
			// r
			else if (key == Main.KEY_R)
				resetSimulation();
		}
	}

	/**
	 * Returns the game screen's simulation window.
	 * 
	 * @return simulation
	 */
	public SimulationWindow getSimulationWindow() {
		return simulation;
	}
	
	/**
	 * Moves the selected entity following the position of the cursor.
	 */
	public void moveEntity() {
	
		// get cursor coordinate
		
		DoubleBuffer cursorPosX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer cursorPosY = BufferUtils.createDoubleBuffer(1);
		
		glfwGetCursorPos(window, cursorPosX, cursorPosY);
				
		float x = (float) cursorPosX.get(0);
		float y = (float) cursorPosY.get(0);
				
				
		// convert cursor coordinate to OpenGL world coordinate
		x -= screenWidth/2;
		y *= -1;
		y += screenHeight/2;
		
		// set position of entity
		selectedEntity.setPosition(new Vector3f(x,y, selectedEntity.getPosition().z));
				
		// update AABB if entity is a rectangle
		if (selectedEntity instanceof Rectangle) {
			Rectangle r = (Rectangle) selectedEntity;
					
			r.updateAABB();
		}
			
	}
	
	/**
	 * Checks whether or not a selected entity can 
	 * be placed at the location of the cursor. If yes,
	 * places the entity at the location. If no, deletes 
	 * the entity.
	 */
	public void placeEntity() {
			
		// check if entity can be placed
		if (!simulation.isWithinBounds(selectedEntity)) {
				
			if (program == 1)
				simulation.getEntities().remove(simulation.getEntities().size()-1);
				
			else if (program == 3) {
					
				selectedEntity.setPosition(new Vector3f(selectedEntityOriginalX, selectedEntityOriginalY, 
						selectedEntity.getPosition().z));
					
				// update AABB if selected entity is a rectangle
				if (selectedEntity instanceof Rectangle) {
					
					Rectangle r = (Rectangle) selectedEntity;
					r.updateAABB();
				}
			}
			
			return;
		}
		
		// loop through entities
		for (Entity entity : simulation.getEntities()) {
			
			// check that the entities to check collision for are not the same entity
			if (!selectedEntity.equals(entity)) {
				
				if (selectedEntity.intersects(entity)) {
					
					if (program == 1)
						simulation.getEntities().remove(simulation.getEntities().size()-1);
					
					else if (program == 3) {
						
						selectedEntity.setPosition(new Vector3f(selectedEntityOriginalX, selectedEntityOriginalY, 
								selectedEntity.getPosition().z));
						
						// update AABB if selected entity is a rectangle
						if (selectedEntity instanceof Rectangle) {
							
							Rectangle r = (Rectangle) selectedEntity;
							
							r.updateAABB();
						}
						
					}
					
					return;
				}
			}
		}
		
	}
	
	/**
	 * Creates a pop-up box for the given entity.
	 * 
	 * @param entity
	 */
	public PopUpBox createPopUpBox(Entity entity) {
		
		float offsetX = 0f;
		float size = 1f;
		
		if (entity instanceof Rectangle) {
			
			offsetX = ((Rectangle) entity).getWidth()/2;
			size = ((Rectangle) entity).getWidth();
		}
		else if (entity instanceof Circle) {
			
			offsetX = ((Circle) entity).getRadius();
			size = ((Circle) entity).getRadius() * 2;
		}
		
		float width = 250f;
		float height = 180f;
		
		float x = entity.getPosition().x - width/2 - offsetX - 5f;
		float y = entity.getPosition().y + 20f;
				
		float[] vertices = Entity.getVertices(width, height, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
				
		Vector3f position = new Vector3f(x, y, z);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		int textureID = loader.loadTexture(PopUpBox.POP_UP_BOX_TEXTURE_FILE);
		Model model = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		
		// create pop-up box
		PopUpBox p = new PopUpBox(loader, model, position, rotation, scale, width, height, z + 0.4f);
		
		// set values
		p.updateSizeText(size);
		p.updateMassText(entity.getMass());
		p.updateVelocityXText(entity.getVelocity().x);
		p.updateVelocityYText(entity.getVelocity().y);
		
		
		// edit button states
		
		// cannon
		if (entity instanceof Cannon) {
					
			p.updateVelocityXText(entity.getStoredVelocity().x);
			p.updateVelocityYText(entity.getStoredVelocity().y);
			
			// velocity x
			if (entity.getStoredVelocity().x > 995f)
				p.setIncreaseVelocityXButtonState(false);
			else 
				p.setIncreaseVelocityXButtonState(true);
							
			if (entity.getStoredVelocity().x < -995f)
				p.setDecreaseVelocityXButtonState(false);
			else 
				p.setDecreaseVelocityXButtonState(true);
							
			// velocity y
			if (entity.getStoredVelocity().y > 995f)
				p.setIncreaseVelocityYButtonState(false);
			else 
				p.setIncreaseVelocityYButtonState(true);
					
			if (entity.getStoredVelocity().y < -995f)
				p.setDecreaseVelocityXButtonState(false);
			else 
				p.setDecreaseVelocityXButtonState(true);
		}
				
		// not cannon
		else {
			
			// velocity x
			if (entity.getVelocity().x > 995f)
				p.setIncreaseVelocityXButtonState(false);
			else 
				p.setIncreaseVelocityXButtonState(true);
			
			if (entity.getVelocity().x < -995f)
				p.setDecreaseVelocityXButtonState(false);
			else 
				p.setDecreaseVelocityXButtonState(true);
			
			// velocity y
			if (entity.getVelocity().y > 995f)
				p.setIncreaseVelocityYButtonState(false);
			else 
				p.setIncreaseVelocityYButtonState(true);
			
			if (entity.getVelocity().y < -995f)
				p.setDecreaseVelocityYButtonState(false);
			else 
				p.setDecreaseVelocityYButtonState(true);
		}
		
		// size
		if (entity.getScale() > 95f)
			p.setIncreaseSizeButtonState(false);
		else 
			p.setIncreaseSizeButtonState(true);
					
		if (entity.getScale() < 35f)
			p.setDecreaseSizeButtonState(false);
		else 
			p.setDecreaseSizeButtonState(true);
					
		// mass
		if (entity.getMass() > 99f)
			p.setIncreaseMassButtonState(false);
		else 
			p.setIncreaseMassButtonState(true);
					
		if (entity.getMass() < 2f)
			p.setDecreaseMassButtonState(false);
		else 
			p.setDecreaseMassButtonState(true);
		
		return p;
	}
	
	/**
	 * Sets the current simulation index.
	 * 
	 * @param currentSim
	 */
	public void setCurrentSim(int currentSim) {
		this.currentSim = currentSim;
	}
	
	/**
	 * Resets the current simulation.
	 */
	public void resetSimulation() {
		
		// pause simulation
		simulation.setPause(false);
		simulation.pausePlaySimulation();
		
		// load simulation
		simulation.loadSimulation(sidebar.getSimulationsData().get(currentSim - 1));
	}
}
