package screens;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import main.Main;
import objects.Circle;
import objects.Entity;
import objects.Loader;
import objects.Model;
import objects.Rectangle;
import renderEngine.Renderer;
import widgets.Button;
import widgets.Label;
import widgets.LessonPanel;
import widgets.PopUpBox;
import widgets.Sidebar;
import widgets.SimulationWindow;
import widgets.Toolbar;

/**
 * This class implements the lesson screen of the 
 * physics simulator.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Thursday, April 19th, 2018
 */
public class LessonScreen {

	// instance variables	
	private SimulationWindow simulation;
	private Toolbar toolbar;
	private Sidebar sidebar;
	private LessonPanel lessonPanel;
	private PopUpBox popUpBox;
	private Entity selectedEntity;
		
	private Label selectASimLabel;
		
	private float selectedEntityOriginalX;
	private float selectedEntityOriginalY;
		
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
	public static final String MOTION_LESSON_TEXTURE_FILE = "./res/motionLesson.png";
	public static final String PROJECTILE_MOTION_LESSON_TEXTURE_FILE = "./res/projectileMotionLesson.png";
	public static final String NEWTONS_SECOND_LAW_LESSON_TEXTURE_FILE = "./res/newtonsSecondLawLesson.png";
	public static final String FORCE_OF_GRAVITY_LESSON_TEXTURE_FILE = "./res/forceOfGravityLesson.png";
	public static final String FRICTION_LESSON_TEXTURE_FILE = "./res/frictionLesson.png";
	public static final String SELECT_A_SIM_LABEL_TEXTURE_FILE = "./res/selectASimulationLabel.png";
	
	// constructor
	public LessonScreen(long window, Loader loader, float screenWidth, float screenHeight, float z, 
			String[] files) {
		
		// simulation window
		simulation = new SimulationWindow(window, loader, screenWidth, screenHeight, z);
				
		// toolbar
		toolbar = new Toolbar(loader, screenWidth, screenHeight, z);
		
		// sidebar
		sidebar = new Sidebar(loader, screenWidth, screenHeight, z, files, false);
		
		// lesson panel
		lessonPanel = new LessonPanel(loader, screenWidth, screenHeight, z);
		
		// select a simulation label
		float labelWidth = 3f;
		float labelHeight = 3f;
				
		float[] vertices = Entity.getVertices(70, 70, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		
		float labelX = (simulation.getMax().x + simulation.getMin().x) / 2;
		float labelY = (simulation.getMax().y + simulation.getMin().y) / 2;
				
		Vector3f position = new Vector3f(labelX, labelY, z);
		Vector3f rotation = new Vector3f(0,0,0);
		
		int textureID = loader.loadTexture(SELECT_A_SIM_LABEL_TEXTURE_FILE);
		Model selectionASimModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		selectASimLabel = new Label(selectionASimModel, position, rotation, labelWidth, labelWidth, labelHeight);
				
		// reset simulation models
		
		vertices = Entity.getVertices(sidebar.getButtonWidth(), sidebar.getButtonHeight(), z);
		
		// motion lesson
		sidebar.getButtons().get(0).setModel(loader.loadToVAO(vertices, texCoords, indices, 
				loader.loadTexture(MOTION_LESSON_TEXTURE_FILE)));
		
		// projectile motion lesson
		sidebar.getButtons().get(1).setModel(loader.loadToVAO(vertices, texCoords, indices, 
				loader.loadTexture(PROJECTILE_MOTION_LESSON_TEXTURE_FILE)));
		
		// Newton's Second Law lesson
		sidebar.getButtons().get(2).setModel(loader.loadToVAO(vertices, texCoords, indices, 
				loader.loadTexture(NEWTONS_SECOND_LAW_LESSON_TEXTURE_FILE)));
		
		// force of gravity lesson
		sidebar.getButtons().get(3).setModel(loader.loadToVAO(vertices, texCoords, indices, 
				loader.loadTexture(FORCE_OF_GRAVITY_LESSON_TEXTURE_FILE)));
		
		// friction lesson
		sidebar.getButtons().get(4).setModel(loader.loadToVAO(vertices, texCoords, indices, 
				loader.loadTexture(FRICTION_LESSON_TEXTURE_FILE)));
		
		this.loader = loader;
				
		this.z = z;
		program = 0;
		currentSim = -1;
				
		this.window = window;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	/**
	 * Renders the lesson screen.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		toolbar.render(renderer);
		sidebar.render(renderer);
		simulation.render(renderer);
		
		if (currentSim == -1)
			renderer.render(selectASimLabel);
		else
			lessonPanel.render(renderer);
		
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
	 * Updates the lesson screen.
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
						
				// loop through buttons of sidebar
				for (int i = 0; i < sidebar.getButtons().size(); i++) {
							
					Button button = sidebar.getButtons().get(i);
							
					// check if this button was clicked
					if (button.getAabb().intersects(x, y) && button.isEnabled()) {
							
						simulation.setPause(false);
						simulation.pausePlaySimulation();
								
						// simulation button
						simulation.loadSimulation(sidebar.getSimulationsData().get(i));
						currentSim = i + 1;
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
						
				// pause-play button
				if (simulation.getPausePlayButton().getAabb().intersects(x, y)) {
							
					simulation.pausePlaySimulation();
					return;
				}
				
				// show hide lesson panel button
				if (lessonPanel.getShowHideButton().getAabb().intersects(x, y)) {
					
					lessonPanel.showHidePanel();
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
		
		if (program == 0) {
			
			// space bar
			if (key == Main.KEY_SPACE)
				simulation.pausePlaySimulation();
			
			// up
			else if (key == Main.KEY_UP)
				sidebar.updateTopSimulationIndex(true);
			
			// down
			else if (key == Main.KEY_DOWN)
				sidebar.updateTopSimulationIndex(false);
		}
	}
	
	/**
	 * Returns the lesson screen's simulation window.
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