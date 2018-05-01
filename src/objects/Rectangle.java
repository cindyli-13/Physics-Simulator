package objects;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * This class specifies a rectangular entity. It manages and updates 
 * the rectangle's AABB bounding-box.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, April 17th, 2018
 */
public class Rectangle extends Entity {

	// instance variables
	private float width;
	private float height;
	
	private AABB aabb;
	
	// constructor
	public Rectangle(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration, Vector3f rotation,
			float scale, float mass, float e, float width, float height, float staticFriction, 
			float kineticFriction) {
		
		super(model, position, velocity, acceleration, rotation, scale, mass, e, 
				staticFriction, kineticFriction);
		
		// dimensions
		this.width = width;
		this.height = height;
		
		// aabb
		float xMin = position.x - width/2;
		float xMax = position.x + width/2;
		float yMin = position.y - height/2;
		float yMax = position.y + height/2;
				
		this.aabb = new AABB(new Vector2f(xMin, yMin), new Vector2f(new Vector2f(xMax, yMax)));
	}

	/**
	 * Updates the rectangle's ABB.
	 * 
	 * @param dt  the change in time, or time step
	 */
	public void update(float dt) {
		super.update(dt);
		
		this.aabb.setMin(new Vector2f(getPosition().x - width/2, getPosition().y - height/2)); 
		this.aabb.setMax(new Vector2f(getPosition().x + width/2, getPosition().y + height/2));
	}
	
	/**
	 * Returns the rectangle's AABB.
	 * 
	 * @return aabb
	 */
	public AABB getAabb() {
		return aabb;
	}

	/**
	 * Returns the rectangle's width.
	 * 
	 * @return width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Sets the rectangle's width.
	 * 
	 * @param width
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Returns the rectangle's height.
	 * 
	 * @return height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Sets the rectangle's height.
	 * 
	 * @param height
	 */
	public void setHeight(float height) {
		this.height = height;
	}

}
