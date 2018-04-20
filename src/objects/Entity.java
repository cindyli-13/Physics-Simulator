package objects;

import org.joml.Vector3f;

/**
 * This class is the blueprint for entities. An entity is an object to 
 * be placed in the OpenGL world.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, April 17th, 2018
 */
public class Entity {

	// instance variables
	private Model model;
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f acceleration;
	private Vector3f rotation;
	private float scale;
	private float mass;
	private float e;  // the entity's coefficient of restitution, or "bounciness"
	
	// constructor
	public Entity(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration, 
			Vector3f rotation, float scale, float mass, float e) {
		
		// model
		this.model = model;
		
		// position
		this.position = new Vector3f();
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
		
		// velocity
		this.velocity = new Vector3f();
		this.velocity.x = velocity.x;
		this.velocity.y = velocity.y;
		this.velocity.z = velocity.z;
		
		// acceleration
		this.acceleration = new Vector3f();
		this.acceleration.x = acceleration.x;
		this.acceleration.y = acceleration.y;
		this.acceleration.z = acceleration.z;
		
		//rotation
		this.rotation = new Vector3f();
		this.rotation.x = rotation.x;
		this.rotation.y = rotation.y;
		this.rotation.z = rotation.z;
		
		// scale
		this.scale = scale;
		
		// mass
		this.mass = mass;
		
		// coefficient of restitution
		this.e = e;
	}
	
	/**
	 * Updates the entity's properties.
	 * 
	 * @param dt  the change in time or time step
	 */
	public void update(float dt) {
		
		// update velocity
		velocity.x += acceleration.x * dt;
		velocity.y += acceleration.y * dt;
		velocity.z += acceleration.z * dt;
		
		// update position
		position.x += velocity.x * dt;
		position.y += velocity.y * dt;
		position.z += velocity.z * dt;
	}
	
	/**
	 * Checks whether or not this entity intersects another 
	 * entity.
	 * 
	 * @param e  the other entity
	 * @return true if they intersect, false otherwise
	 */
	public boolean intersects(Entity e) {
		
		/* need to check:
		 * rectangle-rectangle
		 * rectangle-circle
		 * circle-rectangle
		 * circle-circle
		 */
		
		// if A is rectangle
		if (this instanceof Rectangle) {
			
			// if B is rectangle
			if (e instanceof Rectangle) {
				
				return ((Rectangle) this).getAabb().intersects(((Rectangle) e).getAabb());
			}
			
			// if B is circle
			else if (e instanceof Circle) {
				
				return ((Rectangle) this).getAabb().intersects((Circle) e);
			}
		}
		
		// if A is circle
		else if (this instanceof Circle) {
			
			// if B is rectangle
			if (e instanceof Rectangle) {
							
				return ((Circle) this).intersects(((Rectangle) e).getAabb());
			}
						
			// if B is circle
			else if (e instanceof Circle) {
				
				return ((Circle) this).intersects((Circle) e);
			}
		}
		
		// program should not reach this point
		return false;
	}
	
	/**
	 * Increases the entity's rotation.
	 * 
	 * @param dx  angle change in the x direction
	 * @param dy  angle change in the y direction
	 * @param dz  angle change in the z direction
	 */
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
		
		this.rotation.x %= 360;
		this.rotation.y %= 360;
		this.rotation.z %= 360;
	}

	/**
	 * Returns the entity's model.
	 * 
	 * @return model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Returns the entity's position.
	 * 
	 * @return position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Sets the entity's position.
	 * 
	 * @param position
	 */
	public void setPosition(Vector3f position) {
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
	}

	/**
	 * Returns the entity's velocity.
	 * 
	 * @return velocity
	 */
	public Vector3f getVelocity() {
		return velocity;
	}

	/**
	 * Sets the entity's velocity.
	 * 
	 * @param velocity
	 */
	public void setVelocity(Vector3f velocity) {
		this.velocity.x = velocity.x;
		this.velocity.y = velocity.y;
		this.velocity.z = velocity.z;
	}
	
	/**
	 * Returns the entity's acceleration.
	 * 
	 * @return acceleration
	 */
	public Vector3f getAcceleration() {
		return acceleration;
	}

	/**
	 * Sets the entity's acceleration.
	 * 
	 * @param acceleration
	 */
	public void setAcceleration(Vector3f acceleration) {
		this.acceleration.x = acceleration.x;
		this.acceleration.y = acceleration.y;
		this.acceleration.z = acceleration.z;
	}

	/**
	 * Returns the entity's rotation.
	 * 
	 * @return rotation
	 */
	public Vector3f getRotation() {
		return rotation;
	}
	
	/**
	 * Sets the entity's rotation.
	 * 
	 * @param rotation
	 */
	public void setRotation(Vector3f rotation) {
		this.rotation.x = rotation.x;
		this.rotation.y = rotation.y;
		this.rotation.z = rotation.z;
	}

	/**
	 * Returns the entity's scale.
	 * 
	 * @return scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Setst the entity's scale.
	 * 
	 * @param scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * Returns the entity's mass.
	 * 
	 * @return mass
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * Sets the entity's mass.
	 * 
	 * @param mass
	 */
	public void setMass(float mass) {
		this.mass = mass;
	}

	/**
	 * Returns the entity's coefficient of restitution
	 * 
	 * @return e  the entity's coefficient of restitution
	 */
	public float getCoefficientOfRestitution() {
		return e;
	}

	/**
	 * Sets the entity's coefficient of restitution
	 * 
	 * @param e  the entity's coefficient of restitution
	 */
	public void setCoefficientOfRestitution(float e) {
		this.e = e;
	}
	
	
	// ********** PUBLIC HELPER METHODS **********
	
	/**
	 * Returns a float array containing the vertices of a rectangle's 
	 * model based on the given parameters.
	 * 
	 * @param width			the rectangle's width
	 * @param height		the rectangle's height
	 * @param z				the z coordinate of the rectangle
	 * @return vertices		a float array containing the rectangle's model's vertices
	 */
	public static float[] getVertices(float width, float height, float z) {
		
		float xMin = -width/2;
		float xMax = width/2;
		float yMin = -height/2;
		float yMax = height/2;
		
		float[] vertices = new float[] {
				
				xMin, yMax, z, 
				xMin, yMin, z, 
				xMax, yMin, z, 
				xMax, yMax, z
		};
		
		return vertices;
	}
	
	/**
	 * Returns a float array containing the texture coordinates 
	 * of a rectangular texture.
	 * 
	 * @return texCoords	a float array containing the rectangle's model's texture coordinates
	 */
	public static float[] getTexCoords() {
		
		float[] texCoords = new float[] {
			
			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		
		return texCoords;
	}
	
	/**
	 * Returns an integer array containing the indices of a 
	 * rectangle's model.
	 * 
	 * @return indices	  an integer array containing the rectangle's model's indices
	 */
	public static int[] getIndices() {
		
		int[] indices = new int[] {
				
			0,1,3,
			3,1,2
		};
		
		return indices;
	}
}
