package screens;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import io.IO;
import main.Main;
import objects.Circle;
import objects.Entity;
import objects.Loader;
import objects.Model;
import objects.Rectangle;
import renderEngine.Renderer;
import widgets.Button;
import widgets.GUIComponent;
import widgets.PopUpBox;
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
	private Button saveButton;		// sButton
	private PopUpBox popUpBox;
	private Entity selectedEntity;
	
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
	
	private long window;
	private float screenWidth;
	private float screenHeight;
	
	// static variables
	public static final String SAVE_BUTTON_TEXTURE_FILE = "./res/saveButton.png";
		
	// constructor
	public CustomizedScreen(long window, Loader loader, float screenWidth, float screenHeight, float z) {
					
		// simulation window
		simulation = new SimulationWindow(window, loader, screenWidth, screenHeight, z);
		
		// toolbar
		toolbar = new Toolbar(loader, screenWidth, screenHeight, z);
		
		// save button
		float buttonX = 100;
		float buttonY = 275;
		
		float buttonWidth = 70f;
		float buttonHeight = 70f;
		
		float[] vertices = Entity.getVertices(buttonWidth, buttonHeight, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
				
		Vector3f position = new Vector3f(buttonX, buttonY, z);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 0.7f;
		
		int textureID = loader.loadTexture(SAVE_BUTTON_TEXTURE_FILE);
		Model sButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		saveButton = new Button(sButtonModel, position, rotation, scale, buttonWidth, buttonHeight);
		
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(saveButton);
		
		this.loader = loader;
		
		this.z = z;
		program = 0;
		
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
		simulation.render(renderer);
		renderer.renderGUI(guiComponents);
		
		if (program == 1 || program == 3)
			moveEntity();
		
		else if (program == 2) {
			
			float x = 0f;
			float y = 0f;
			
			if (selectedEntity instanceof Rectangle) {
				
				x = ((Rectangle) selectedEntity).getWidth()/2;
				y = ((Rectangle) selectedEntity).getHeight()/2;
			}
			else if (selectedEntity instanceof Circle) {
				x = ((Circle) selectedEntity).getRadius();
				y = x;
			}
			
			float offsetX = selectedEntity.getPosition().x - 
					(popUpBox.getPosition().x + popUpBox.getWidth()/2 + x + 5f);
			
			float offsetY = selectedEntity.getPosition().y - 
					(popUpBox.getPosition().y - popUpBox.getHeight()/2 - y - 5f);
			
			popUpBox.update(offsetX, offsetY);
			popUpBox.render(renderer);
		}
	}
	
	/**
	 * Updates the game screen.
	 */
	public void update() {
		
		if (!simulation.isPaused())
			simulation.update();
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
					selectedEntity.setScale(selectedEntity.getScale() + 10f);
					
					if (selectedEntity.getScale() >= 100f) {
						
						selectedEntity.setScale(100f);
						popUpBox.setIncreaseSizeButtonState(false);
					}
					else {
						popUpBox.setDecreaseSizeButtonState(true);
					}
					
					// rectangle
					if (selectedEntity instanceof Rectangle) {
						
						Rectangle r = (Rectangle) selectedEntity;
						
						r.setWidth(r.getScale());
						r.setHeight(r.getScale());
						
						r.updateAABB();
					}
					
					// circle
					else if (selectedEntity instanceof Circle) {
						
						Circle c = (Circle) selectedEntity;
						
						c.setRadius(c.getScale()/2);
					}
				}
				
				// decrease size button
				else if (popUpBox.getDecreaseSizeButton().getAabb().intersects(x, y)) {
					
					// increase entity's scale
					selectedEntity.setScale(selectedEntity.getScale() - 10f);
					
					if (selectedEntity.getScale() <= 30f) {
						
						selectedEntity.setScale(30f);
						popUpBox.setDecreaseSizeButtonState(false);
					}
					else {
						popUpBox.setIncreaseSizeButtonState(true);
					}
					
					// rectangle
					if (selectedEntity instanceof Rectangle) {
						
						Rectangle r = (Rectangle) selectedEntity;
						
						r.setWidth(r.getScale());
						r.setHeight(r.getScale());
						
						r.updateAABB();
					}
					
					// circle
					else if (selectedEntity instanceof Circle) {
						
						Circle c = (Circle) selectedEntity;
						
						c.setRadius(c.getScale()/2);
					}
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
											
							main.setCurrScreen(0);
							return;
						}
							
						// info button
						else if (button.equals(toolbar.getInfoButton())) {
								
							UserGuideScreen.showUserGuide();
							return;
						}
						
						// rectangle button
						else if (button.equals(toolbar.getRectangleButton()) && simulation.isPaused()) {
								
							// generate a crate
							float sideLength = 50;
							float posX = toolbar.getRectangleButton().getPosition().x;
							float posY = toolbar.getRectangleButton().getPosition().y;
							float mass = 20;
							float e = -0.3f;
								
							selectedEntity = simulation.createCrateEntity(sideLength, posX, posY, z, mass, e);
							
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
								
							selectedEntity = simulation.createBallEntity(radius, posX, posY, z, mass, e);
							
							program = 1;
							return;
						}
					}
						
				}
				
				// save button
				if (saveButton.getAabb().intersects(x, y)) {
					
					// save data into text file
					IO.createOutputFile("./data/customized_test.txt");
					
					// number of entities
					IO.println(Integer.toString(simulation.getEntities().size()));
					
					// loop through entities
					for (Entity entity: simulation.getEntities()) {
							
						// line buffer
						IO.println("");
						
						// rectangle
						if (entity instanceof Rectangle) {
								
							Rectangle r = (Rectangle) entity;
								
							IO.println("RECTANGLE");
							IO.println(Float.toString(r.getWidth()));
							IO.println(Float.toString(r.getPosition().x));
							IO.println(Float.toString(r.getPosition().y));
							IO.println(Float.toString(r.getMass()));
							IO.println(Float.toString(r.getCoefficientOfRestitution()));
						}
							
						// circle
						else if (entity instanceof Circle) {
								
							Circle c = (Circle) entity;
								
							IO.println("CIRCLE");
							IO.println(Float.toString(c.getRadius()));
							IO.println(Float.toString(c.getPosition().x));
							IO.println(Float.toString(c.getPosition().y));
							IO.println(Float.toString(c.getMass()));
							IO.println(Float.toString(c.getCoefficientOfRestitution()));
						}
					}
					IO.closeOutputFile();
					
					System.out.println("Simulation saved!");
					return;
				}
				
				// pause-play button
				if (simulation.getPausePlayButton().getAabb().intersects(x, y)) {
					
					simulation.pausePlaySimulation();
					return;
				}
				
				// selecting an object
				if (simulation.isPaused()) {
					
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
		else if (rightClick && simulation.isPaused()) {
			
			// loop through entities of simulation
			for (Entity entity: simulation.getEntities()) {
				
				if (entity.intersects(x, y)) {
					
					// create pop-up box
					popUpBox = createPopUpBox(entity);
					selectedEntity = entity;
					program = 2;
					
					// set states of increase and decrease size button
					
					if (selectedEntity.getScale() == 100f)
						popUpBox.setIncreaseSizeButtonState(false);
					
					else if (selectedEntity.getScale() == 30f)
						popUpBox.setDecreaseSizeButtonState(false);
					
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
		
		// space bar
		if(key == Main.KEY_SPACE)
			simulation.pausePlaySimulation();
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
		float offsetY = 0f;
		
		if (entity instanceof Rectangle) {
			
			offsetX = ((Rectangle) entity).getWidth()/2;
			offsetY = ((Rectangle) entity).getHeight()/2;
		}
		else if (entity instanceof Circle) {
			offsetX = ((Circle) entity).getRadius();
			offsetY = offsetX;
		}
		
		float width = 200f;
		float height = 120f;
		
		float x = entity.getPosition().x - width/2 - offsetX - 5f;
		float y = entity.getPosition().y + height/2 + offsetY + 5f;
				
		float[] vertices = Entity.getVertices(width, height, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
				
		Vector3f position = new Vector3f(x, y, z);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		int textureID = loader.loadTexture(PopUpBox.POP_UP_BOX_TEXTURE_FILE);
		Model model = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		return new PopUpBox(loader, model, position, rotation, scale, width, height, z);
	}
	
}
