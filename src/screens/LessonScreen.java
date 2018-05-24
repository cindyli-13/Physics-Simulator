package screens;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

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
import widgets.ForceOfGravityLessonDisplayPanel;
import widgets.FrictionLessonDisplayPanel;
import widgets.GUIComponent;
import widgets.Label;
import widgets.LessonPanel;
import widgets.MotionLessonDisplayPanel;
import widgets.NewtonsSecondLawLessonDisplayPanel;
import widgets.ProjectileMotionLessonDisplayPanel;
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
	private ArrayList<GUIComponent> guiComponents;
	private SimulationWindow simulation;
	private Toolbar toolbar;
	private Sidebar sidebar;
	private LessonPanel lessonPanel;
		
	private Label selectASimLabel;
	private Label title;
	
	private MotionLessonDisplayPanel motionLessonDisplayPanel;
	private ProjectileMotionLessonDisplayPanel projectileMotionLessonDisplayPanel;
	private NewtonsSecondLawLessonDisplayPanel newtonsSecondLawLessonDisplayPanel;
	private ForceOfGravityLessonDisplayPanel forceOfGravityLessonDisplayPanel;
	private FrictionLessonDisplayPanel frictionLessonDisplayPanel;
		
	private int currentSim;
	
	// only for Newton's Second Law lesson
	private float netForce;
	private GUIComponent netForceArrow; 
	
	// only for force of gravity lesson
	private float forceOfGravity;
	private GUIComponent forceOfGravityArrow;
	
	// only for friction lesson
	private GUIComponent frictionArrow;
		
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
	public static final String TITLE_TEXTURE_FILE = "./res/lessonLabel.png";
	public static final String NET_FORCE_ARROW_TEXTURE_FILE = "./res/netForceArrow.png";
	public static final String ARROW_TEXTURE_FILE = "./res/arrow.png";
	
	// constructor
	public LessonScreen(long window, Loader loader, float screenWidth, float screenHeight, float z, 
			String[] files) {
		
		// simulation window
		simulation = new SimulationWindow(window, loader, screenWidth, screenHeight, z);
				
		// toolbar
		toolbar = new Toolbar(loader, z);
		
		// sidebar
		sidebar = new Sidebar(loader, z, files, false);
		
		// lesson panel
		lessonPanel = new LessonPanel(loader, z);
		
		// display panels
		motionLessonDisplayPanel = new MotionLessonDisplayPanel(loader, 115f, 120f, z);
		projectileMotionLessonDisplayPanel = new ProjectileMotionLessonDisplayPanel(loader, 115f, 120f, z);
		newtonsSecondLawLessonDisplayPanel = new NewtonsSecondLawLessonDisplayPanel(loader, 115f, 120f, z);
		forceOfGravityLessonDisplayPanel = new ForceOfGravityLessonDisplayPanel(loader, 115f, 120f, z);
		frictionLessonDisplayPanel = new FrictionLessonDisplayPanel(loader, 115f, 120f, z);
		
		// select a simulation label
		float labelWidth = 200f;
		float labelHeight = 200f;
		
		float[] vertices = Entity.getVertices(labelWidth, labelHeight, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		
		float labelX = (simulation.getMax().x + simulation.getMin().x) / 2;
		float labelY = (simulation.getMax().y + simulation.getMin().y) / 2;
				
		Vector3f position = new Vector3f(labelX, labelY, z);
		Vector3f rotation = new Vector3f(0,0,0);
		
		int textureID = loader.loadTexture(SELECT_A_SIM_LABEL_TEXTURE_FILE);
		Model selectionASimModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		selectASimLabel = new Label(selectionASimModel, position, rotation, 1, labelWidth, labelHeight);
				
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
				
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(title);
		
		
		// net force arrow
		float arrowWidth = 40f;
		float arrowHeight = 40f;
		
		vertices = Entity.getVertices(arrowWidth, arrowHeight, z - 100f);
						
		textureID = loader.loadTexture(ARROW_TEXTURE_FILE);
		Model arrowModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
						
		netForceArrow = new GUIComponent(arrowModel, new Vector3f(0,0,z), rotation, 1);
		
		
		// force of gravity arrow								
		forceOfGravityArrow = new GUIComponent(arrowModel, new Vector3f(0,0,z), 
				new Vector3f(0,0,270), 1);
		
		
		// friction arrow								
		frictionArrow = new GUIComponent(arrowModel, new Vector3f(0,0,z), rotation, 1);
		
				
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
		renderer.renderGUI(guiComponents);
		
		if (currentSim == -1)
			renderer.render(selectASimLabel);
		else
			lessonPanel.render(renderer);
		
		// display panel
		switch (currentSim) {
			
			// motion lesson
			case 1:
				motionLessonDisplayPanel.render(renderer);
				break;
				
			// projectile motion lesson
			case 2:
				projectileMotionLessonDisplayPanel.render(renderer);
				break;
				
			// Newton's Second Law lesson
			case 3:
				newtonsSecondLawLessonDisplayPanel.render(renderer);
				renderer.render(netForceArrow);
				break;
				
			// force of gravity lesson
			case 4:
				forceOfGravityLessonDisplayPanel.render(renderer);
				renderer.render(forceOfGravityArrow);
				break;
				
			// friction lesson
			case 5:
				frictionLessonDisplayPanel.render(renderer);
				
				if (!simulation.isPaused() && simulation.getEntities().get(0).getVelocity().x != 0)
					renderer.render(frictionArrow);
				break;
		}
	}
	
	/**
	 * Updates the lesson screen.
	 */
	public void update() {
		
		if (!simulation.isPaused()) {
			
			simulation.update();
			
			if (currentSim != -1) {
			
				Entity entity = simulation.getEntities().get(0);
				
				// update based on current lesson
				switch (currentSim) {
					
					// motion lesson
					case 1:
						
						// update velocity button states
						if (entity.getVelocity().x < 95)
							motionLessonDisplayPanel.setIncreaseVelocityButtonState(true);
						else
							motionLessonDisplayPanel.setIncreaseVelocityButtonState(false);
						
						if (entity.getVelocity().x > -95)
							motionLessonDisplayPanel.setDecreaseVelocityButtonState(true);
						else
							motionLessonDisplayPanel.setDecreaseVelocityButtonState(false);
						
						break;
						
					// projectile motion
					case 2:
						
						// update velocity x button states
						if (entity.getVelocity().x < 95)
							projectileMotionLessonDisplayPanel.setIncreaseVelocityXButtonState(true);
						else
							projectileMotionLessonDisplayPanel.setIncreaseVelocityXButtonState(false);
						
						if (entity.getVelocity().x > -95)
							projectileMotionLessonDisplayPanel.setDecreaseVelocityXButtonState(true);
						else
							projectileMotionLessonDisplayPanel.setDecreaseVelocityXButtonState(false);
						
						// update velocity y button states
						if (entity.getVelocity().y < 95)
							projectileMotionLessonDisplayPanel.setIncreaseVelocityYButtonState(true);
						else
							projectileMotionLessonDisplayPanel.setIncreaseVelocityYButtonState(false);
						
						if (entity.getVelocity().y > -95)
							projectileMotionLessonDisplayPanel.setDecreaseVelocityYButtonState(true);
						else
							projectileMotionLessonDisplayPanel.setDecreaseVelocityYButtonState(false);
						
						break;
						
					// Newton's Second Law lesson
					case 3:
						
						// update net force arrow
						Rectangle r1 = (Rectangle) entity;
						
						float arrowX = r1.getPosition().x + r1.getWidth()/2 + 20f;
						float arrowY = r1.getPosition().y;
						
						netForceArrow.getPosition().x = arrowX;
						netForceArrow.getPosition().y = arrowY;
						
						break;
					
					// force of gravity lesson
					case 4:
						
						// update force of gravity arrow
						Circle c = (Circle) entity;
						
						arrowX = c.getPosition().x;
						arrowY = c.getPosition().y - c.getRadius() - 20f * forceOfGravityArrow.getScale();
						
						forceOfGravityArrow.getPosition().x = arrowX;
						forceOfGravityArrow.getPosition().y = arrowY;
						
						break;
						
					// friction lesson
					case 5:
						
						// update velocity button states
						if (entity.getVelocity().x < 190)
							frictionLessonDisplayPanel.setIncreaseVelocityButtonState(true);
						else
							frictionLessonDisplayPanel.setIncreaseVelocityButtonState(false);
						
						if (entity.getVelocity().x > -190)
							frictionLessonDisplayPanel.setDecreaseVelocityButtonState(true);
						else
							frictionLessonDisplayPanel.setDecreaseVelocityButtonState(false);
						
						// update friction arrow
						Rectangle r2 = (Rectangle) simulation.getEntities().get(0);
						
						arrowX = 0;
						
						// object traveling right
						if (r2.getVelocity().x > 0) {
							
							arrowX = r2.getPosition().x - r2.getWidth()/2 - 20f;
							frictionArrow.setRotation(new Vector3f(0,0,180));
						}
						
						// object traveling left
						else if (r2.getVelocity().x < 0) {
							
							arrowX = r2.getPosition().x + r2.getWidth()/2 + 20f;
							frictionArrow.setRotation(new Vector3f(0,0,0));
						}
						
						frictionArrow.getPosition().x = arrowX;
						
						break;
				}
			}
		}
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
	 * @param rightClick		whether the right mouse button was pressed
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
					lessonPanel.setLesson(i);
						
					// edit if needed
					switch (i) {
						
						// motion lesson
						case 0:
							simulation.getEntities().get(0).getAcceleration().y = 0f;
							simulation.getEntities().get(0).getVelocity().x = 10f;
							break;
							
						// projectile motion lesson
						case 1:
							simulation.getEntities().get(0).getVelocity().x = 20f;
							simulation.getEntities().get(0).getVelocity().y = 50f;
							break;
							
						// Newton's Second Law lesson
						case 2:
							simulation.getEntities().get(0).getAcceleration().y = 0f;
							simulation.getEntities().get(0).getAcceleration().x = 5f;
							netForce = 50f;
							
							Rectangle r1 = (Rectangle) simulation.getEntities().get(0);
									
							float arrowX = r1.getPosition().x + r1.getWidth()/2 + 20f;
							float arrowY = r1.getPosition().y;
								
							netForceArrow.getPosition().x = arrowX;
							netForceArrow.getPosition().y = arrowY;
							
							break;
						
						// force of gravity lesson
						case 3:
							
							// force of gravity arrow
							forceOfGravity = simulation.getEntities().get(0).getMass() *
								simulation.getEntities().get(0).getAcceleration().y;
							
							forceOfGravityArrow.setScale(Math.abs(forceOfGravity) / 60f);
							
							Circle c = (Circle) simulation.getEntities().get(0);
							
							arrowX = c.getPosition().x;
							arrowY = c.getPosition().y - c.getRadius() - 20f * forceOfGravityArrow.getScale();
							
							forceOfGravityArrow.getPosition().x = arrowX;
							forceOfGravityArrow.getPosition().y = arrowY;
							
							break;
						
						// friction lesson
						case 4:
							
							// friction arrow
							
							Rectangle r2 = (Rectangle) simulation.getEntities().get(0);
							
							arrowX = 0;
							arrowY = r2.getPosition().y - 15f;
							
							// object traveling right
							if (r2.getVelocity().x > 0) {
								
								arrowX = r2.getPosition().x - r2.getWidth()/2 - 20f;
								frictionArrow.setRotation(new Vector3f(0,0,180));
							}
							
							// object traveling left
							else if (r2.getVelocity().x < 0) {
								
								arrowX = r2.getPosition().x + r2.getWidth()/2 + 20f;
								frictionArrow.setRotation(new Vector3f(0,0,0));
							}
							
							frictionArrow.getPosition().x = arrowX;
							frictionArrow.getPosition().y = arrowY;
					}
						
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
			if (simulation.getPausePlayButton().getAabb().intersects(x, y)  && currentSim != -1) {
							
				simulation.pausePlaySimulation();
				return;
			}
				
			// show hide lesson panel button
			if (lessonPanel.getShowHideButton().getAabb().intersects(x, y)) {
					
				lessonPanel.showHidePanel();
				return;
			}
				
			// left button
			if (lessonPanel.getLeftButton().getAabb().intersects(x, y)) {
					
				lessonPanel.updatePageIndex(true);
				return;
			}
				
			// right button
			if (lessonPanel.getRightButton().getAabb().intersects(x, y)) {
					
				lessonPanel.updatePageIndex(false);
				return;
			}
						
			// editing an object
			if (simulation.isPaused()) {
					
				// buttons in display panel
				
				// motion lesson
				if (currentSim == 1) {
						
					// increase velocity button
					if (motionLessonDisplayPanel.getIncreaseVelocityButton().getAabb().intersects(x, y)) {
							
						// increase object velocity
						simulation.getEntities().get(0).getVelocity().x += 5f;
						motionLessonDisplayPanel.setDecreaseVelocityButtonState(true);
							
						if (simulation.getEntities().get(0).getVelocity().x > 95f) {
								
							simulation.getEntities().get(0).getVelocity().x = 100f;
							motionLessonDisplayPanel.setIncreaseVelocityButtonState(false);
						}
					}
						
					// decrease velocity button
					else if (motionLessonDisplayPanel.getDecreaseVelocityButton().getAabb().intersects(x, y)) {
							
						// decrease object velocity
						simulation.getEntities().get(0).getVelocity().x -= 5f;
						motionLessonDisplayPanel.setIncreaseVelocityButtonState(true);
							
						if (simulation.getEntities().get(0).getVelocity().x < -95f) {
								
							simulation.getEntities().get(0).getVelocity().x = -100f;
							motionLessonDisplayPanel.setDecreaseVelocityButtonState(false);
						}
					}
						
					// increase acceleration button
					else if (motionLessonDisplayPanel.getIncreaseAccelerationButton().getAabb().intersects(x, y)) {
							
						// increase object acceleration
						simulation.getEntities().get(0).getAcceleration().x += 5f;
						motionLessonDisplayPanel.setDecreaseAccelerationButtonState(true);
							
						if (simulation.getEntities().get(0).getAcceleration().x > 95f) {
								
							simulation.getEntities().get(0).getAcceleration().x = 100f;
							motionLessonDisplayPanel.setIncreaseAccelerationButtonState(false);
						}
					}
						
					// decrease acceleration button
					else if (motionLessonDisplayPanel.getDecreaseAccelerationButton().getAabb().intersects(x, y)) {
							
						// decrease object acceleration
						simulation.getEntities().get(0).getAcceleration().x -= 5f;
						motionLessonDisplayPanel.setIncreaseAccelerationButtonState(true);
							
						if (simulation.getEntities().get(0).getAcceleration().x < -95f) {
								
							simulation.getEntities().get(0).getAcceleration().x = -100f;
							motionLessonDisplayPanel.setDecreaseAccelerationButtonState(false);
						}
					}
				}
				
				// projectile motion lesson
				else if (currentSim == 2) {
						
					// increase velocity x button
					if (projectileMotionLessonDisplayPanel.getIncreaseVelocityXButton().getAabb().intersects(x, y)) {
							
						// increase object velocity x
						simulation.getEntities().get(0).getVelocity().x += 5f;
						projectileMotionLessonDisplayPanel.setDecreaseVelocityXButtonState(true);
							
						if (simulation.getEntities().get(0).getVelocity().x > 95f) {
								
							simulation.getEntities().get(0).getVelocity().x = 100f;
							projectileMotionLessonDisplayPanel.setIncreaseVelocityXButtonState(false);
						}
					}
						
					// decrease velocity x button
					else if (projectileMotionLessonDisplayPanel.getDecreaseVelocityXButton().getAabb().intersects(x, y)) {
							
						// decrease object velocity x
						simulation.getEntities().get(0).getVelocity().x -= 5f;
						projectileMotionLessonDisplayPanel.setIncreaseVelocityXButtonState(true);
							
						if (simulation.getEntities().get(0).getVelocity().x < -95f) {
								
							simulation.getEntities().get(0).getVelocity().x = -100f;
							projectileMotionLessonDisplayPanel.setDecreaseVelocityXButtonState(false);
						}
					}
						
					// increase velocity y button
					else if (projectileMotionLessonDisplayPanel.getIncreaseVelocityYButton().getAabb().intersects(x, y)) {
							
						// increase object velocity y
						simulation.getEntities().get(0).getVelocity().y += 5f;
						projectileMotionLessonDisplayPanel.setDecreaseVelocityYButtonState(true);
							
						if (simulation.getEntities().get(0).getVelocity().y > 95f) {
								
							simulation.getEntities().get(0).getVelocity().y = 100f;
							projectileMotionLessonDisplayPanel.setIncreaseVelocityYButtonState(false);
						}
					}
						
					// decrease velocity y button
					else if (projectileMotionLessonDisplayPanel.getDecreaseVelocityYButton().getAabb().intersects(x, y)) {
							
						// decrease object velocity y
						simulation.getEntities().get(0).getVelocity().y -= 5f;
						projectileMotionLessonDisplayPanel.setIncreaseVelocityYButtonState(true);
							
						if (simulation.getEntities().get(0).getVelocity().y < 5f) {
								
							simulation.getEntities().get(0).getVelocity().y = 0f;
							projectileMotionLessonDisplayPanel.setDecreaseVelocityYButtonState(false);
						}
					}
				}
				
				// Newton's Second Law lesson
				else if (currentSim == 3) {
					
					// increase mass button
					if (newtonsSecondLawLessonDisplayPanel.getIncreaseMassButton().getAabb().intersects(x, y)) {
							
						// increase object mass
						simulation.getEntities().get(0).setMass(simulation.getEntities().get(0).getMass() + 5f);
						newtonsSecondLawLessonDisplayPanel.setDecreaseMassButtonState(true);
							
						if (simulation.getEntities().get(0).getMass() > 95f) {
								
							simulation.getEntities().get(0).setMass(100f);
							newtonsSecondLawLessonDisplayPanel.setIncreaseMassButtonState(false);
						}
						
						// update acceleration
						float acceleration = netForce / simulation.getEntities().get(0).getMass();
						simulation.getEntities().get(0).getAcceleration().x = acceleration;
					}
						
					// decrease mass button
					else if (newtonsSecondLawLessonDisplayPanel.getDecreaseMassButton().getAabb().intersects(x, y)) {
							
						// decrease object mass
						simulation.getEntities().get(0).setMass(simulation.getEntities().get(0).getMass() - 5f);
						newtonsSecondLawLessonDisplayPanel.setIncreaseMassButtonState(true);
							
						if (simulation.getEntities().get(0).getMass() < 10f) {
								
							simulation.getEntities().get(0).setMass(5f);
							newtonsSecondLawLessonDisplayPanel.setDecreaseMassButtonState(false);
						}
						
						// update acceleration
						float acceleration = netForce / simulation.getEntities().get(0).getMass();
						simulation.getEntities().get(0).getAcceleration().x = acceleration;
					}
				}
					
				// force of gravity lesson
				else if (currentSim == 4) {
						
					// increase mass button
					if (forceOfGravityLessonDisplayPanel.getIncreaseMassButton().getAabb().intersects(x, y)) {
							
						// increase object mass
						simulation.getEntities().get(0).setMass(simulation.getEntities().get(0).getMass() + 1f);
						forceOfGravityLessonDisplayPanel.setDecreaseMassButtonState(true);
						forceOfGravityArrow.setScale(forceOfGravityArrow.getScale() + 0.05f);
								
						if (simulation.getEntities().get(0).getMass() > 19f) {
									
							simulation.getEntities().get(0).setMass(20f);
							forceOfGravityLessonDisplayPanel.setIncreaseMassButtonState(false);
							forceOfGravityArrow.setScale(forceOfGravityArrow.getScale() - 0.05f);
						}
					}
							
					// decrease mass button
					else if (forceOfGravityLessonDisplayPanel.getDecreaseMassButton().getAabb().intersects(x, y)) {
								
						// decrease object mass
						simulation.getEntities().get(0).setMass(simulation.getEntities().get(0).getMass() - 1f);
						forceOfGravityLessonDisplayPanel.setIncreaseMassButtonState(true);
						forceOfGravityArrow.setScale(forceOfGravityArrow.getScale() - 0.05f);

						if (simulation.getEntities().get(0).getMass() < 2f) {
									
							simulation.getEntities().get(0).setMass(1f);
							forceOfGravityLessonDisplayPanel.setDecreaseMassButtonState(false);
							forceOfGravityArrow.setScale(forceOfGravityArrow.getScale() + 0.05f);
						}	
					}
					
					// update force of gravity
					forceOfGravity = simulation.getEntities().get(0).getMass() * 
							simulation.getEntities().get(0).getAcceleration().y;	
					
					// update force of gravity arrow
					
					Circle c = (Circle) simulation.getEntities().get(0);
					
					float arrowX = c.getPosition().x;
					float arrowY = c.getPosition().y - c.getRadius() - 20f * forceOfGravityArrow.getScale();
					
					forceOfGravityArrow.getPosition().x = arrowX;
					forceOfGravityArrow.getPosition().y = arrowY;
						
				}
				
				// friction lesson
				else if (currentSim == 5) {
						
					// increase velocity button
					if (frictionLessonDisplayPanel.getIncreaseVelocityButton().getAabb().intersects(x, y)) {
							
						// increase object velocity
						simulation.getEntities().get(0).getVelocity().x += 10f;
						frictionLessonDisplayPanel.setDecreaseVelocityButtonState(true);
							
						if (simulation.getEntities().get(0).getVelocity().x > 190f) {
								
							simulation.getEntities().get(0).getVelocity().x = 200f;
							frictionLessonDisplayPanel.setIncreaseVelocityButtonState(false);
						}
					}
						
					// decrease velocity button
					else if (frictionLessonDisplayPanel.getDecreaseVelocityButton().getAabb().intersects(x, y)) {
							
						// decrease object velocity
						simulation.getEntities().get(0).getVelocity().x -= 10f;
						frictionLessonDisplayPanel.setIncreaseVelocityButtonState(true);
							
						if (simulation.getEntities().get(0).getVelocity().x < -190f) {
								
							simulation.getEntities().get(0).getVelocity().x = -200f;
							frictionLessonDisplayPanel.setDecreaseVelocityButtonState(false);
						}
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
		if (key == Main.KEY_SPACE && currentSim != -1)
			simulation.pausePlaySimulation();
			
		// up
		else if (key == Main.KEY_UP)
			sidebar.updateTopSimulationIndex(true);
			
		// down
		else if (key == Main.KEY_DOWN)
			sidebar.updateTopSimulationIndex(false);
			
		// left
		else if (key == Main.KEY_LEFT && lessonPanel.isShowPanel())
			lessonPanel.updatePageIndex(true);
			
		// right
		else if (key == Main.KEY_RIGHT && lessonPanel.isShowPanel())
			lessonPanel.updatePageIndex(false);
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
	 * Sets the current simulation index.
	 * 
	 * @param currentSim
	 */
	public void setCurrentSim(int currentSim) {
		this.currentSim = currentSim;
	}
	
}