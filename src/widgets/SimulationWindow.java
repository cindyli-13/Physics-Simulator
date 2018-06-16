package widgets;

import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector3f;

import io.IO;
import objects.Cannon;
import objects.Circle;
import objects.Entity;
import objects.Loader;
import objects.Model;
import objects.Rectangle;
import physicsEngine.Physics;
import physicsEngine.PhysicsWithCannons;
import renderEngine.Renderer;

/**
 * This class contains all the initial components of the 
 * simulation window.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Friday, April 20th, 2018
 */
public class SimulationWindow {

	// instance variables
	private Rectangle ground;			// g
	private Rectangle leftBoundary;		// l
	private Rectangle rightBoundary;	// r
	private Rectangle topBoundary;		// t
	
	private GUIComponent sky;
	
	private ArrayList<Entity> entities;
	private ArrayList<Entity> boundaries;
	private ArrayList<Entity> other;
	private ArrayList<GUIComponent> guiComponents;
	
	private Loader loader;
	
	private boolean pause;
	private float z;
	
	private Vector3f min;	// the minimum point that is considered inside the bounds of the simulation
	private Vector3f max;	// the maximum point that is considered inside the bounds of the simulation
	
	// time step is, by default, set to 0.05f
	private float dt = 0.05f;
	
	// model side length is, by default, set as 1f
	// currently, all model's are square
	// need to change this if implementing rectangular models
	private float modelSideLength = 1f;
	
	private Model crateModel;
	private Model metalBoxModel;
	private Model ballModel;
	private Model cannonModel;
	private Model targetModel;
	private Model boundaryModel;
	
	// button for pause and play simulation
	private Button pausePlayButton;
	private Model pauseButtonModel;
	private Model playButtonModel;
	
	// button for reset simulation
	private Button resetButton;
	
	// static variables
	public static final String CRATE_TEXTURE_FILE = "./res/crate.png";
	public static final String METAL_BOX_TEXTURE_FILE = "./res/metal_box.png";
	public static final String BALL_TEXTURE_FILE = "./res/ball.png";
	public static final String GROUND_TEXTURE_FILE = "./res/ground.png";
	public static final String BOUNDARY_TEXTURE_FILE = "./res/boundary.png";
	public static final String SKY_TEXTURE_FILE = "./res/sky.png";
	public static final String CANNON_TEXTURE_FILE = "./res/Cannon.png";
	public static final String TARGET_TEXTURE_FILE = "./res/Target.png";
	
	public static final String PAUSE_BUTTON_TEXTURE_FILE = "./res/pauseButton.png";
	public static final String PLAY_BUTTON_TEXTURE_FILE = "./res/playButton.png";
	public static final String RESET_BUTTON_TEXTURE_FILE = "./res/resetButton.png";
	
	public static final float CRATE_STATIC_FRICTION = 0.2f;
	public static final float METAL_BOX_STATIC_FRICTION = 0.3f;
	public static final float BALL_STATIC_FRICTION = 0.05f;
	public static final float GROUND_STATIC_FRICTION = 0.2f;
	public static final float BOUNDARY_STATIC_FRICTION = 0.1f;
	
	public static final float CRATE_KINETIC_FRICTION = 0.1f;
	public static final float METAL_BOX_KINETIC_FRICTION = 0.2f;
	public static final float BALL_KINETIC_FRICTION = 0.02f;
	public static final float GROUND_KINETIC_FRICTION = 0.1f;
	public static final float BOUNDARY_KINETIC_FRICTION = 0.05f;
	
	private static final float maximumMass = Float.MAX_VALUE;
	
	private static float g = -9.81f;	// acceleration due to gravity
	
	/**
	 * Creates a simulation window.
	 * 
	 * @param window
	 * @param loader
	 * @param screenWidth
	 * @param screenHeight
	 * @param z
	 */
	public SimulationWindow(long window, Loader loader, float screenWidth, float screenHeight, float z) {
		
		// ******** INITIAL STATES OF BOUNDARIES ********
		// 
		// 	width		the boundary width
		// 	height		the boundary height
		// 	x			the x coordinate of the center of the boundary (in OpenGL world coordinates)
		// 	y			the y coordinate of the center of the boundary (in OpenGL world coordinates)
		
		// the following will be the same for each boundary
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
				
		Vector3f velocity = new Vector3f(0,0,0);
		Vector3f acceleration = new Vector3f(0,0,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		// ground
		float gWidth = 730f;
		float gHeight = 70f;
		
		float gX = -screenWidth/2 + 250f + gWidth/2; // 250 pixels from left side of the screen
		float gY = -screenHeight/2 + gHeight/2 + 20f;  // 20 pixels up from the bottom of the screen
		Vector3f gPos = new Vector3f(gX, gY, z - 100f);
		
		float[] vertices = Entity.getVertices(gWidth, gHeight, z - 100f);
		int textureID = loader.loadTexture(GROUND_TEXTURE_FILE);
		Model gModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		ground = new Rectangle(gModel, gPos, velocity, acceleration, rotation, scale, 
				maximumMass, -1f, gWidth, gHeight, GROUND_STATIC_FRICTION, GROUND_KINETIC_FRICTION);
		
		// left boundary
		float lWidth = 30f;
		float lHeight = screenHeight - gHeight - lWidth - 100f;
				
		float lX = gX - gWidth/2 + lWidth/2;	// left edge of ground
		float lY = gY + gHeight/2 + lHeight/2;  // above ground
		Vector3f lPos = new Vector3f(lX, lY, z - 100f);
		
		vertices = Entity.getVertices(lWidth, lHeight, z - 100f);
		textureID = loader.loadTexture(BOUNDARY_TEXTURE_FILE);
		boundaryModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		leftBoundary = new Rectangle(boundaryModel, lPos, velocity, acceleration, rotation, scale, 
				maximumMass, -1f, lWidth, lHeight, BOUNDARY_STATIC_FRICTION, BOUNDARY_KINETIC_FRICTION);
		
		// right boundary
		float rWidth = lWidth;	// same as left boundary
		float rHeight = lHeight;	// same as left boundary
			
		float rX = gX + gWidth/2 - rWidth/2;	// right edge of ground
		float rY = gY + gHeight/2 + lHeight/2;  // above ground
		Vector3f rPos = new Vector3f(rX, rY, z - 100f);
		
		vertices = Entity.getVertices(rWidth, rHeight, z - 100f);
		textureID = loader.loadTexture(BOUNDARY_TEXTURE_FILE);
		Model rModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		rightBoundary = new Rectangle(rModel, rPos, velocity, acceleration, rotation, scale, 
				maximumMass, -1f, rWidth, rHeight, BOUNDARY_STATIC_FRICTION, BOUNDARY_KINETIC_FRICTION);
		
		// top boundary
		float tWidth = gWidth;
		float tHeight = lWidth;
			
		float tX = gX;	// same as ground
		float tY = lY + lHeight/2 + tHeight/2;	// above left boundary
		Vector3f tPos = new Vector3f(tX, tY, z - 100f);
		
		vertices = Entity.getVertices(tWidth, tHeight, z - 100f);
		textureID = loader.loadTexture(BOUNDARY_TEXTURE_FILE);
		Model tModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		topBoundary = new Rectangle(tModel, tPos, velocity, acceleration, rotation, scale, 
				maximumMass, -1f, tWidth, tHeight, BOUNDARY_STATIC_FRICTION, BOUNDARY_KINETIC_FRICTION);
		
		
		// initialize boundaries array list
		boundaries = new ArrayList<Entity>();
		boundaries.add(ground);
		boundaries.add(leftBoundary);
		boundaries.add(rightBoundary);
		boundaries.add(topBoundary);
		
		// **********************************************
		
		
		// ****************** SKY ***********************
		
		float sWidth = gWidth;
		float sHeight = lHeight;
		
		float sX = gX;	// same as ground
		float sY = lY;
		Vector3f sPos = new Vector3f(sX, sY, z - 110f);
		
		vertices = Entity.getVertices(sWidth, sHeight, z - 120f);
		textureID = loader.loadTexture(SKY_TEXTURE_FILE);
		Model sModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		sky = new GUIComponent(sModel, sPos, rotation, scale);
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		guiComponents.add(sky);
		
		// **********************************************
		
		
		// ************* PAUSE-PLAY BUTTON **************
				
		float buttonWidth = 40f;
		float buttonHeight = 40f;
		
		float buttonX = rX + rWidth/2 - buttonWidth/2;
		float buttonY = tY + tHeight/2 + buttonHeight/2 + 20f;
				
		vertices = Entity.getVertices(buttonWidth, buttonHeight, z + 0.01f);
		Vector3f position = new Vector3f(buttonX, buttonY, z - 100f);
				
		textureID = loader.loadTexture(PAUSE_BUTTON_TEXTURE_FILE);
		pauseButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		textureID = loader.loadTexture(PLAY_BUTTON_TEXTURE_FILE);
		playButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		pausePlayButton = new Button(playButtonModel, position, rotation, scale, buttonWidth, buttonHeight);
		guiComponents.add(pausePlayButton);
		
		// **********************************************
		
		
		// *************** RESET BUTTON *****************
				
		buttonX -= buttonWidth + 5f;
						
		position = new Vector3f(buttonX, buttonY, z - 100f);
						
		textureID = loader.loadTexture(RESET_BUTTON_TEXTURE_FILE);
		Model model = loader.loadToVAO(vertices, texCoords, indices, textureID);
						
		resetButton = new Button(model, position, rotation, scale, buttonWidth, buttonHeight);
		guiComponents.add(resetButton);
		
		// **********************************************
		
		
		// ********* MODELS FOR PHYSICS OBJECTS *********
		// 
		
		// the following will be the same for each model
		vertices = Entity.getVertices(modelSideLength, modelSideLength, z);
		texCoords = Entity.getTexCoords();
		indices = Entity.getIndices();
		
		// crate model
		textureID = loader.loadTexture(CRATE_TEXTURE_FILE);
		crateModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		// metal box model
		textureID = loader.loadTexture(METAL_BOX_TEXTURE_FILE);
		metalBoxModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		// ball model
		textureID = loader.loadTexture(BALL_TEXTURE_FILE);
		ballModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		// cannon model
		textureID = loader.loadTexture(CANNON_TEXTURE_FILE);
		cannonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		// target model
		textureID = loader.loadTexture(TARGET_TEXTURE_FILE);
		targetModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		// **********************************************
		
		
		// initialize entities array list
		entities = new ArrayList<Entity>();
		other = new ArrayList<Entity>();
		
		// set pause to true
		pause = true;
		
		this.z = z;
		this.loader = loader;
		
		// set up min and max points
		min = new Vector3f(leftBoundary.getAabb().getMax().x, ground.getAabb().getMax().y, z);
		max = new Vector3f(rightBoundary.getAabb().getMin().x, topBoundary.getAabb().getMin().y, z);
	}
	
	/**
	 * Creates a crate entity in the simulation window.
	 * 
	 * @param sideLength	the side length of the crate
	 * @param x				the x coordinate of the crate's center
	 * @param y				the y coordinate of the crate's center
	 * @param vx			the horizontal component of the crate's velocity
	 * @param vy			the vertical component of the crate's velocity
	 * @param z				the z coordinate of the crate
	 * @param mass			the crate's mass
	 * @param e				the crate's coefficient of restitution
	 * @return crate		the crate entity
	 */
	public Entity createCrateEntity(float sideLength, float x, float y, float z, 
			float vx, float vy, float mass, float e) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(vx,vy,0);
		Vector3f acceleration = new Vector3f(0,g,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = sideLength / modelSideLength;
		
		Rectangle crate = new Rectangle(crateModel, position, velocity, acceleration, rotation, scale, 
				mass, e, sideLength, sideLength, CRATE_STATIC_FRICTION, CRATE_KINETIC_FRICTION);
		
		entities.add(crate);
		
		return crate;
	}
	
	/**
	 * Creates a crate boundary in the simulation window.
	 * 
	 * @param sideLength	the side length of the crate
	 * @param x				the x coordinate of the crate's center
	 * @param y				the y coordinate of the crate's center
	 * @param vx			the horizontal component of the crate's velocity
	 * @param vy			the vertical component of the crate's velocity
	 * @param z				the z coordinate of the crate
	 * @param mass			the crate's mass
	 * @param e				the crate's coefficient of restitution
	 * @return crate		the crate boundary
	 */
	public Entity ncreateCrateEntity(float sideLength, float x, float y, float z, float mass, float e) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(0,0,0);
		Vector3f acceleration = new Vector3f(0,0,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = sideLength/modelSideLength;
		
		Rectangle crate = new Rectangle(crateModel, position, velocity, acceleration, rotation, scale, 
				mass, e, sideLength, sideLength, CRATE_STATIC_FRICTION, CRATE_KINETIC_FRICTION);
		
		boundaries.add(crate);
		
		return crate;
	}
	
	/**
	 * Creates a metal box entity in the simulation window.
	 * 
	 * ***** Currently, this function is not used. *****
	 * 
	 * @param sideLength	the side length of the metal box 
	 * @param x				the x coordinate of the metal box's center
	 * @param y				the y coordinate of the metal box's center
	 * @param vx			the horizontal component of the metal box's velocity
	 * @param vy			the vertical component of the metal box's velocity
	 * @param z				the z coordinate of the metal box
	 * @param mass			the metal box's mass
	 * @param e				the metal box's coefficient of restitution
	 * @return metalBox		the metal box entity
	 */
	public Entity createMetalBoxEntity(float sideLength, float x, float y, float z, 
			float vx, float vy, float mass, float e) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(vx,vy,0);
		Vector3f acceleration = new Vector3f(0,g,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = sideLength / modelSideLength;
		
		Rectangle metalBox = new Rectangle(metalBoxModel, position, velocity, acceleration, rotation, scale, 
				mass, e, sideLength, sideLength, METAL_BOX_STATIC_FRICTION, METAL_BOX_KINETIC_FRICTION);
		
		entities.add(metalBox);
		
		return metalBox;
	}
	
	/**
	 * Creates a metal boundary in the simulation window.
	 * 
	 * @param sideLength	the side length of the metal 
	 * @param x				the x coordinate of the metal's center
	 * @param y				the y coordinate of the metal's center
	 * @param z				the z coordinate of the metal
	 * @param mass			the metal's mass
	 * @param e				the metal's coefficient of restitution
	 * @return metalBox		the metal entity
	 */
	public Entity ncreateMetalEntity(float width, float height, float x, float y, float z, float mass, float e) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(0,0,0);
		Vector3f acceleration = new Vector3f(0,0,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;
		
		float[] vertices = Entity.getVertices(width, height, z - 100f);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();
		
		// metal model
		int textureID = loader.loadTexture(BOUNDARY_TEXTURE_FILE);
		Model model = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		Rectangle metal = new Rectangle(model, position, velocity, acceleration, rotation, scale, 
				mass, e, width, height, METAL_BOX_STATIC_FRICTION, METAL_BOX_KINETIC_FRICTION);
		
		boundaries.add(metal);
		
		return metal;
	}
	
	/**
	 * Creates a ball entity in the simulation window.
	 * 
	 * @param radius		the radius of the ball
	 * @param x				the x coordinate of the ball's center
	 * @param y				the y coordinate of the ball's center
	 * @param vx			the horizontal component of the ball's velocity
	 * @param vy			the vertical component of the ball's velocity
	 * @param z				the z coordinate of the ball
	 * @param mass			the metal ball's mass
	 * @param e				the metal ball's coefficient of restitution
	 * @return ball			the ball entity
	 */
	public Entity createBallEntity(float radius, float x, float y, float z, 
			float vx, float vy, float mass, float e) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(vx,vy,0);
		Vector3f acceleration = new Vector3f(0,g,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 2 * radius / modelSideLength;
		
		Circle ball = new Circle(ballModel, position, velocity, acceleration, rotation, scale, 
				mass, e, radius, BALL_STATIC_FRICTION, BALL_KINETIC_FRICTION);
		
		entities.add(ball);
		
		return ball;
	}
	
	/**
	 * Only for Game Mode. Creates a target.
	 * 
	 * @param radius
	 * @param x
	 * @param y
	 * @param z
	 * @return target
	 */
	public Entity createTargetEntity(float radius, float x, float y, float z) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(0,0,0);
		Vector3f acceleration = new Vector3f(0,0,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 2 * radius / modelSideLength;
		
		Circle target = new Circle(targetModel, position, velocity, acceleration, rotation, scale, 
				Float.MAX_VALUE, -1, radius, BALL_STATIC_FRICTION, BALL_KINETIC_FRICTION);
		
		other.add(target);
		
		return target;
	}
	
	/**
	 * Creates a cannon entity in the simulation window.
	 * 
	 * @param sideLength	the side length of the cannon 
	 * @param x				the x coordinate of the cannon's center
	 * @param y				the y coordinate of the cannon's center
	 * @param vx			the horizontal component of the cannon's stored velocity
	 * @param vy			the vertical component of the cannon's stored velocity
	 * @param z				the z coordinate of the cannon
	 * @param mass			the cannon's mass
	 * @param e				the cannon's coefficient of restitution
	 * @return cannon		the cannon entity
	 */
	public Entity createCannonEntity( float sideLength, float x, float y, float vx, float vy, 
			float z, float mass, float e) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(0,0,0);
		Vector3f acceleration = new Vector3f(0,g,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = sideLength / modelSideLength;
		
		Cannon cannon = new Cannon(cannonModel, position, velocity, acceleration, rotation, scale, 
				mass, e,sideLength, sideLength, CRATE_STATIC_FRICTION, CRATE_KINETIC_FRICTION);
		
		cannon.getStoredVelocity().x = vx;
		cannon.getStoredVelocity().y = vy;
		
		entities.add(cannon);
		return cannon;
	}
	
	/**
	 * Renders the objects of the simulation window.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		renderer.render(entities);
		renderer.render(other);
		renderer.render(boundaries);
		renderer.renderGUI(guiComponents);
	}
	
	/**
	 * Updates each object in the simulation.
	 * 
	 * @param physicsWithCannons	whether or not the physics allows cannons
	 */
	public void update(boolean physicsWithCannons) {
		
		for (Entity entity:entities) {
			entity.update(dt);
		}
		
		for (Entity boundary:boundaries) {
			boundary.update(dt);
		}
		
		if (physicsWithCannons)
			PhysicsWithCannons.collision(entities, boundaries, z);
		else
			Physics.collision(entities, ground, leftBoundary, topBoundary, rightBoundary, z);
	}
	
	/**
	 * Checks whether an entity is within the bounds of the 
	 * simulation.
	 * 
	 * @param entity
	 * @return true if yes, false otherwise
	 */
	public boolean isWithinBounds(Entity entity) {
		
		// if entity is a rectangle
		if (entity instanceof Rectangle) {
					
			Rectangle r = (Rectangle) entity;
					
			// check if rectangle is within bounds
			if (r.getAabb().getMin().x < this.min.x || r.getAabb().getMax().x > this.max.x || 
				r.getAabb().getMin().y < this.min.y || r.getAabb().getMax().y > this.max.y)
				
				return false;
		}
		
		// if entity is a circle
		else if (entity instanceof Circle) {
			
			Circle c = (Circle) entity;
			
			// check if circle is within bounds
			if (c.getPosition().x - c.getRadius() < this.min.x || c.getPosition().x + c.getRadius() > this.max.x || 
				c.getPosition().y - c.getRadius() < this.min.y || c.getPosition().y + c.getRadius() > this.max.y)
				
				return false;
		}
		
		return true;
	}
	
	/**
	 * Loads the specified simulation into the simulation 
	 * window.
	 * 
	 * @param filename		where the simulation data is stored
	 */
	public void loadSimulation(String fileName) {
		
		// clear current simulation data
		entities.clear();
		other.clear();
		
		while (boundaries.size() > 4) {
			boundaries.remove(4);
		}
		
		try {
			IO.openInputFile(fileName);
				
			// get number of entities
			int numEntities = Integer.parseInt(IO.readLine());
					
			// line buffer
			IO.readLine();
					
			for (int i = 0; i < numEntities; i++) {
						
				String type = IO.readLine();
						
				// rectangle
				if (type.equals("RECTANGLE")) {
							
					float sideLength = Float.parseFloat(IO.readLine());
					float x = Float.parseFloat(IO.readLine());
					float y = Float.parseFloat(IO.readLine());
					float vx = Float.parseFloat(IO.readLine());
					float vy = Float.parseFloat(IO.readLine()); 
					float mass = Float.parseFloat(IO.readLine());
					float e = Float.parseFloat(IO.readLine());
					
					createCrateEntity(sideLength, x, y, z, vx, vy, mass, e);
				}
				
				// rectangle (boundary)
				else if (type.equals("bRECTANGLE")) {
					
					float sideLength = Float.parseFloat(IO.readLine());
					float x = Float.parseFloat(IO.readLine());
					float y = Float.parseFloat(IO.readLine());
					float mass = Float.parseFloat(IO.readLine());
					float e = Float.parseFloat(IO.readLine());
					
					ncreateCrateEntity(sideLength, x, y, z, mass, e);
				}
				
				// cannon
				else if (type.equals("CANNON")) {
							
					float sideLength = Float.parseFloat(IO.readLine());
					float x = Float.parseFloat(IO.readLine());
					float y = Float.parseFloat(IO.readLine());
					float vx = Float.parseFloat(IO.readLine());
					float vy = Float.parseFloat(IO.readLine()); 
					float mass = Float.parseFloat(IO.readLine());
					float e = Float.parseFloat(IO.readLine());
					
					createCannonEntity(sideLength, x, y, z, vx, vy, mass, e);
				}
						
				// circle
				else if (type.equals("CIRCLE")) {
							
					float radius = Float.parseFloat(IO.readLine());
					float x = Float.parseFloat(IO.readLine());
					float y = Float.parseFloat(IO.readLine());
					float vx = Float.parseFloat(IO.readLine());
					float vy = Float.parseFloat(IO.readLine()); 
					float mass = Float.parseFloat(IO.readLine());
					float e = Float.parseFloat(IO.readLine());
					
					createBallEntity(radius, x, y, z, vx, vy, mass, e);
				}
				
				// metal
				else if(type.equals("METAL")) {
					float width = Float.parseFloat(IO.readLine());
					float height = Float.parseFloat(IO.readLine());
					float x = Float.parseFloat(IO.readLine());
					float y = Float.parseFloat(IO.readLine());
					float mass = Float.parseFloat(IO.readLine());
					float e = Float.parseFloat(IO.readLine());
					
					ncreateMetalEntity(width, height, x, y, z, mass, e);
				}
				
				// target (only for game mode)
				else if (type.equals("TARGET")) {
					
					float radius = Float.parseFloat(IO.readLine());
					float x = Float.parseFloat(IO.readLine());
					float y = Float.parseFloat(IO.readLine());
					
					createTargetEntity(radius,x,y,z);
				}
						
				// line buffer
				IO.readLine();
			}
			IO.closeInputFile();
					
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Returns whether or not the simulation is paused.
	 * 
	 * @return pause
	 */
	public boolean isPaused() {
		return pause;
	}

	/**
	 * Sets whether or not the simulation is paused.
	 * 
	 * @param pause
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	/**
	 * Returns the pause-play button.
	 * 
	 * @return pausePlayButton
	 */
	public Button getPausePlayButton() {
		return pausePlayButton;
	}
	
	/**
	 * Returns the reset button.
	 * 
	 * @return resetButton
	 */
	public Button getResetButton() {
		return resetButton;
	}
	
	/**
	 * Sets the state of the pause-play button and 
	 * the simulation.
	 */
	public void pausePlaySimulation() {
		
		pause = !pause;
		
		if (pause)
			pausePlayButton.setModel(playButtonModel);
		
		else
			pausePlayButton.setModel(pauseButtonModel);
	}

	/**
	 * Returns the crate model.
	 * 
	 * @return crateModel
	 */
	public Model getCrateModel() {
		return crateModel;
	}

	/**
	 * Returns the metal box model.
	 * 
	 * @return metalBoxModel
	 */
	public Model getMetalBoxModel() {
		return metalBoxModel;
	}

	/**
	 * Returns the ball model.
	 * 
	 * @return ballModel
	 */
	public Model getBallModel() {
		return ballModel;
	}
	
	/**
	 * Returns the array list of entities in the simulation
	 * window.
	 * 
	 * @return entities
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Returns the array list of other objects in the simulation
	 * window.
	 * 
	 * @return other
	 */
	public ArrayList<Entity> getOther() {
		return other;
	}
	
	/**
	 * Returns the array list of boundaries in the simulation
	 * window.
	 * 
	 * @return boundaries
	 */
	public ArrayList<Entity> getBoundaries() {
		return boundaries;
	}
	
	/**
	 * Only for Game Mode. Returns the target.
	 * 
	 * @return the target
	 */
	public Entity getTarget() {
		return other.get(0);
	}
	
	/**
	 * Returns the minimum point that is considered inside the 
	 * bounds of the simulation.
	 * 
	 * @return min
	 */
	public Vector3f getMin() {
		return min;
	}
	
	/**
	 * Returns the maximum point that is considered inside the 
	 * bounds of the simulation.
	 * 
	 * @return max
	 */
	public Vector3f getMax() {
		return max;
	}
	
	/**
	 * Returns the time step.
	 * 
	 * @return dt
	 */
	public float getDt() {
		return dt;
	}
}
