package widgets;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Circle;
import objects.Entity;
import objects.Loader;
import objects.Model;
import objects.Rectangle;
import physicsEngine.Physics;
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
	
	// This is a special case: the sky will be a rectangle in the 
	// background of the simulation and will be rendered only for its colour.
	// It is a GUIComponent instead of a Rectangle because it does not have 
	// an AABB.
	private GUIComponent sky;			// s
	
	private ArrayList<Entity> entities;
	private ArrayList<Entity> boundaries;
	private ArrayList<GUIComponent> guiComponents;
	
	private boolean pause;
	private float z;
	
	// time step is, by default set to 0.05f
	private float dt = 0.05f;
	
	// model side length is, by default, set as 1f
	// currently, all model's are square
	// need to change this if implementing rectangular models
	private float modelSideLength = 1f;
	
	private Model crateModel;
	private Model metalBoxModel;
	private Model ballModel;
	
	// static variables
	private static final String CRATE_TEXTURE_FILE = "./res/crate.png";
	private static final String METAL_BOX_TEXTURE_FILE = "./res/metal_box.png";
	private static final String BALL_TEXTURE_FILE = "./res/ball.png";
	private static final String GROUND_TEXTURE_FILE = "./res/ground.png";
	private static final String BOUNDARY_TEXTURE_FILE = "./res/boundary.png";
	private static final String SKY_TEXTURE_FILE = "./res/sky.png";
	
	private static final float CRATE_STATIC_FRICTION = 0.4f;
	private static final float METAL_BOX_STATIC_FRICTION = 0.3f;
	private static final float BALL_STATIC_FRICTION = 0.1f;
	private static final float GROUND_STATIC_FRICTION = 0.4f;
	private static final float BOUNDARY_STATIC_FRICTION = 0.2f;
	
	private static final float CRATE_KINETIC_FRICTION = 0.3f;
	private static final float METAL_BOX_KINETIC_FRICTION = 0.2f;
	private static final float BALL_KINETIC_FRICTION = 0.05f;
	private static final float GROUND_KINETIC_FRICTION = 0.3f;
	private static final float BOUNDARY_KINETIC_FRICTION = 0.1f;
	
	private static final float maximumMass = Float.MAX_VALUE;
	
	private static float g = -9.81f;	// acceleration due to gravity
	
	// constructor
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
		Vector3f gPos = new Vector3f(gX, gY, z);
		
		float[] vertices = Entity.getVertices(gWidth, gHeight, z);
		int textureID = loader.loadTexture(GROUND_TEXTURE_FILE);
		Model gModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		ground = new Rectangle(gModel, gPos, velocity, acceleration, rotation, scale, 
				maximumMass, 0, gWidth, gHeight, GROUND_STATIC_FRICTION, GROUND_KINETIC_FRICTION);
		
		// left boundary
		float lWidth = 30f;
		float lHeight = screenHeight - gHeight - lWidth - 100f;
				
		float lX = gX - gWidth/2 + lWidth/2;	// left edge of ground
		float lY = gY + gHeight/2 + lHeight/2;  // above ground
		Vector3f lPos = new Vector3f(lX, lY, z);
		
		vertices = Entity.getVertices(lWidth, lHeight, z);
		textureID = loader.loadTexture(BOUNDARY_TEXTURE_FILE);
		Model lModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		leftBoundary = new Rectangle(lModel, lPos, velocity, acceleration, rotation, scale, 
				maximumMass, 0, lWidth, lHeight, BOUNDARY_STATIC_FRICTION, BOUNDARY_KINETIC_FRICTION);
		
		// right boundary
		float rWidth = lWidth;	// same as left boundary
		float rHeight = lHeight;	// same as left boundary
			
		float rX = gX + gWidth/2 - rWidth/2;	// right edge of ground
		float rY = gY + gHeight/2 + lHeight/2;  // above ground
		Vector3f rPos = new Vector3f(rX, rY, z);
		
		vertices = Entity.getVertices(rWidth, rHeight, z);
		textureID = loader.loadTexture(BOUNDARY_TEXTURE_FILE);
		Model rModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		rightBoundary = new Rectangle(rModel, rPos, velocity, acceleration, rotation, scale, 
				maximumMass, 0, rWidth, rHeight, BOUNDARY_STATIC_FRICTION, BOUNDARY_KINETIC_FRICTION);
		
		// top boundary
		float tWidth = gWidth;
		float tHeight = lWidth;
			
		float tX = gX;	// same as ground
		float tY = lY + lHeight/2 + tHeight/2;	// above left boundary
		Vector3f tPos = new Vector3f(tX, tY, z);
		
		vertices = Entity.getVertices(tWidth, tHeight, z);
		textureID = loader.loadTexture(BOUNDARY_TEXTURE_FILE);
		Model tModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
				
		topBoundary = new Rectangle(tModel, tPos, velocity, acceleration, rotation, scale, 
				maximumMass, 0, tWidth, tHeight, BOUNDARY_STATIC_FRICTION, BOUNDARY_KINETIC_FRICTION);
		
		
		// initialize boundaries array list
		boundaries = new ArrayList<Entity>();
		boundaries.add(ground);
		boundaries.add(leftBoundary);
		boundaries.add(rightBoundary);
		boundaries.add(topBoundary);
		
		// **********************************************
		
		
		// ******************** SKY *********************
		
		float sWidth = gWidth;	// same as ground
		float sHeight = gHeight + lHeight + tHeight;	// sum of these three boundaries
		
		vertices = Entity.getVertices(sWidth, sHeight, z);
		texCoords = Entity.getTexCoords();
		indices = Entity.getIndices();
		
		float sX = gX;	// same as ground
		float sY = (gY + gHeight/2 + lHeight + tHeight) - sHeight/2;	// the middle of the simulation window
		Vector3f sPos = new Vector3f(sX, sY, z - 100f);
		
		rotation = new Vector3f(0,0,0);
		scale = 1f;
		
		textureID = loader.loadTexture(SKY_TEXTURE_FILE);
		Model sModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
		
		sky = new GUIComponent(sModel, sPos, rotation, scale);
		
		
		// initialize GUI components array list
		guiComponents = new ArrayList<GUIComponent>();
		//guiComponents.add(sky);
		
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
		
		// **********************************************
		
		
		// initialize entities array list
		entities = new ArrayList<Entity>();
		
		// set pause to true
		pause = true;
		
		this.z = z;
	}
	
	/**
	 * Creates a crate entity in the simulation window.
	 * 
	 * @param sideLength	the side length of the crate
	 * @param x				the x coordinate of the crate's center
	 * @param y				the y coordinate of the crate's center
	 * @param z				the z coordinate of the crate
	 * @param mass			the crate's mass
	 * @param e				the crate's coefficient of restitution
	 */
	public void createCrateEntity(float sideLength, float x, float y, float z, float mass, float e) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(0,0,0);
		Vector3f acceleration = new Vector3f(0,g,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = sideLength / modelSideLength;
		
		Rectangle crate = new Rectangle(crateModel, position, velocity, acceleration, rotation, scale, 
				mass, e, sideLength, sideLength, CRATE_STATIC_FRICTION, CRATE_KINETIC_FRICTION);
		
		entities.add(crate);
	}
	
	/**
	 * Creates a metal box entity in the simulation window.
	 * 
	 * @param sideLength	the side length of the metal box 
	 * @param x				the x coordinate of the metal box's center
	 * @param y				the y coordinate of the metal box's center
	 * @param z				the z coordinate of the metal box
	 * @param mass			the metal box's mass
	 * @param e				the metal box's coefficient of restitution
	 */
	public void createMetalBoxEntity(float sideLength, float x, float y, float z, float mass, float e) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(0,0,0);
		Vector3f acceleration = new Vector3f(0,g,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = sideLength / modelSideLength;
		
		Rectangle metalBox = new Rectangle(metalBoxModel, position, velocity, acceleration, rotation, scale, 
				mass, e, sideLength, sideLength, METAL_BOX_STATIC_FRICTION, METAL_BOX_KINETIC_FRICTION);
		
		entities.add(metalBox);
	}
	
	/**
	 * Creates a ball entity in the simulation window.
	 * 
	 * @param radius		the radius of the ball
	 * @param x				the x coordinate of the ball's center
	 * @param y				the y coordinate of the ball's center
	 * @param z				the z coordinate of the ball
	 * @param mass			the metal ball's mass
	 * @param e				the metal ball's coefficient of restitution
	 */
	public void createBallEntity(float radius, float x, float y, float z, float mass, float e) {
		
		Vector3f position = new Vector3f(x,y,z);
		Vector3f velocity = new Vector3f(0,0,0);
		Vector3f acceleration = new Vector3f(0,g,0);
		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 2 * radius / modelSideLength;
		
		Circle ball = new Circle(ballModel, position, velocity, acceleration, rotation, scale, 
				mass, e, radius, BALL_STATIC_FRICTION, BALL_KINETIC_FRICTION);
		
		entities.add(ball);
	}
	
	/**
	 * Renders the objects of the simulation window.
	 * 
	 * @param renderer		the renderer
	 */
	public void render(Renderer renderer) {
		
		renderer.render(entities);
		renderer.render(boundaries);
		renderer.renderGUI(guiComponents);
	}
	
	/**
	 * Updates each object in the simulation.
	 * 
	 * @param dt	the change in time, or time step
	 */
	public void update() {
		
		for (Entity entity:entities) {
			entity.update(dt);
		}
		
		for (Entity boundary:boundaries) {
			boundary.update(dt);
		}
		
		Physics.collision(entities, ground, leftBoundary, topBoundary, rightBoundary, z);
	}

	/**
	 * Returns whether or not the simulation is paused.
	 * 
	 * @return pause
	 */
	public boolean isPause() {
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
	
}
