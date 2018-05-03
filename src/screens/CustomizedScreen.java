package screens;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.joml.Vector2f;
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
	private Entity entityForPopUpBox;
	
	private ArrayList<GUIComponent> guiComponents;
	
	private Loader loader;
	
	private float z;
	
	// specifies the program:
	// 0 = default
	// 1 = new entity created
	// 2 = pop-up box created
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
		
		if (program == 1)
			moveNewObject();
		
		else if (program == 2) {
			
			float x = 0f;
			float y = 0f;
			
			if (entityForPopUpBox instanceof Rectangle) {
				
				x = ((Rectangle) entityForPopUpBox).getWidth()/2;
				y = ((Rectangle) entityForPopUpBox).getHeight()/2;
			}
			else if (entityForPopUpBox instanceof Circle) {
				x = ((Circle) entityForPopUpBox).getRadius();
				y = x;
			}
			
			float offsetX = entityForPopUpBox.getPosition().x - 
					(popUpBox.getPosition().x + popUpBox.getWidth()/2 + x + 5f);
			
			float offsetY = entityForPopUpBox.getPosition().y - 
					(popUpBox.getPosition().y - popUpBox.getHeight()/2 - y - 5f);
			
			popUpBox.update(offsetX, offsetY);
			popUpBox.render(renderer);
		}
	}
	
	/**
	 * Updates the game screen.
	 */
	public void update() {
		
		if (!simulation.isPause())
			simulation.update();
	}
	
	/**
	 * Contains the logic for input handling
	 * 
	 * @param main				where the main loop is
	 * @param key				the key that was pressed
	 * @param leftClick			whether the left mouse button was pressed
	 */
	public void input(Main main, int key, boolean leftClick) {
		
		// mouse input
		mouseInput(main, leftClick);
		
		// keyboard input
		keyboardInput(key);
	}
	
	/**
	 * Contains the logic for when a mouse is clicked.
	 * 
	 * @param main				where the main loop is
	 * @param leftClick			whether the left mouse button was pressed
	 */
	public void mouseInput(Main main, boolean leftClick) {
			
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
			
			// clear new entity
			if (program == 1) {
				
				placeNewEntity(simulation.getEntities().get(simulation.getEntities().size()-1));
				program = 0;
				return;
			}
			
			// pop-up box
			else if (program == 2) {
				
				// close button
				if (popUpBox.getCloseButton().getAabb().intersects(x, y)) {
					
					popUpBox = null;
					entityForPopUpBox = null;
					program = 0;
					return;
				}
				
				// delete entity button
				else if (popUpBox.getDeleteEntityButton().getAabb().intersects(x, y)) {
					
					// find and remove entity from array list
					for (int i = 0; i < simulation.getEntities().size(); i++) {
						
						if (entityForPopUpBox.equals(simulation.getEntities().get(i))) {
							simulation.getEntities().remove(i);
							break;
						}
					}
					
					popUpBox = null;
					entityForPopUpBox = null;
					program = 0;
					return;
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
						else if (button.equals(toolbar.getRectangleButton())) {
								
							// generate a crate
							float sideLength = (float) Math.random() * 50 + 30;
							float posX = toolbar.getRectangleButton().getPosition().x;
							float posY = toolbar.getRectangleButton().getPosition().y;
							float mass = (float) Math.random() * 20 + 1;
							float e = -0.5f;
								
							simulation.createCrateEntity(sideLength, posX, posY, z, mass, e);
							
							program = 1;
							return;
						}
						
						// circle button
						else if (button.equals(toolbar.getCircleButton())) {
								
							// generate a ball
							float radius = (float) Math.random() * 25 + 20;
							float posX = toolbar.getCircleButton().getPosition().x;
							float posY = toolbar.getCircleButton().getPosition().y;
							float mass = (float) Math.random() * 20 + 1;
							float e = -0.5f;
								
							simulation.createBallEntity(radius, posX, posY, z, mass, e);
							
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
				
				// loop through entities of simulation
				for (Entity entity: simulation.getEntities()) {
					
					if (entity.intersects(x, y)) {
						
						// create pop-up box
						popUpBox = createPopUpBox(entity);
						entityForPopUpBox = entity;
						program = 2;
						return;
					}
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
		if(key == Main.KEY_SPACE) {
			simulation.setPause(!simulation.isPause());
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
	 * Moves the newly created entity following the position of the cursor.
	 */
	public void moveNewObject() {
	
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
		Entity e = simulation.getEntities().get(simulation.getEntities().size()-1);
		e.setPosition(new Vector3f(x,y, e.getPosition().z));
				
		// update AABB if entity is a rectangle
		if (e instanceof Rectangle) {
			Rectangle r = (Rectangle) e;
					
			r.getAabb().setMin(new Vector2f(r.getPosition().x - r.getWidth()/2, r.getPosition().y - r.getHeight()/2)); 
			r.getAabb().setMax(new Vector2f(r.getPosition().x + r.getWidth()/2, r.getPosition().y + r.getHeight()/2));
		}
			
	}
	
	/**
	 * Checks whether or not a newly created entity can 
	 * be placed at the location of the cursor. If yes,
	 * places the entity at the location. If no, deletes 
	 * the entity.
	 * 
	 * @param entity		the newly created entity
	 */
	public void placeNewEntity(Entity newEntity) {
		
		// if entity is a rectangle
		if (newEntity instanceof Rectangle) {
			
			Rectangle r = (Rectangle) newEntity;
			
			// horizontal check
			if (r.getAabb().getMin().x < simulation.getMin().x || 
					r.getAabb().getMax().x > simulation.getMax().x) {
				
				simulation.getEntities().remove(simulation.getEntities().size()-1);
				return;
			}
			
			// vertical check
			if (r.getAabb().getMin().y < simulation.getMin().y || 
					r.getAabb().getMax().y > simulation.getMax().y) {
				
				simulation.getEntities().remove(simulation.getEntities().size()-1);
				return;
			}
		}
		
		// if entity is circle
		else if (newEntity instanceof Circle) {
			
			Circle c = (Circle) newEntity;
			
			// horizontal check
			if (c.getPosition().x - c.getRadius() < simulation.getMin().x || 
					c.getPosition().x + c.getRadius() > simulation.getMax().x) {
				
				simulation.getEntities().remove(simulation.getEntities().size()-1);
				return;
			}
			
			// vertical check
			if (c.getPosition().y - c.getRadius() < simulation.getMin().y || 
					c.getPosition().y + c.getRadius() > simulation.getMax().y) {
				
				simulation.getEntities().remove(simulation.getEntities().size()-1);
				return;
			}
		}
		
		// loop through entities
		for(Entity entity : simulation.getEntities()) {
			
			// check that the entities to check collision for are not the same entity
			if (!newEntity.equals(entity)) {
				
				if (newEntity.intersects(entity)) {
					
					simulation.getEntities().remove(simulation.getEntities().size()-1);
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
