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
	private float xMin;
	private float yMin;
	private float xMax;
	private float yMax;
	
	private AABB aabb;
	
	// constructor
	public Rectangle(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration, Vector3f rotation,
			float scale, float mass, float e, float xMin, float yMin, float xMax, float yMax) {
		
		super(model, position, velocity, acceleration, rotation, scale, mass, e);
		
		// aabb
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
				
		this.aabb = new AABB(new Vector2f(xMin, yMin), new Vector2f(new Vector2f(xMax, yMax)));
	}

	/**
	 * Updates the rectangle's AABB.
	 * 
	 * @param dt  the change in time, or time step
	 */
	public void update(float dt) {
		super.update(dt);
		
		this.aabb.setMin(new Vector2f(getPosition().x + xMin, getPosition().y + yMin)); 
		this.aabb.setMax(new Vector2f(getPosition().x + xMax, getPosition().y + yMax));
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
	 * Returns the minimum x-value of the rectangle.
	 * 
	 * @return xMin
	 */
	public float getXMin() {
		return xMin;
	}

	/**
	 * Returns the minimum y-value of the rectangle.
	 * 
	 * @return yMin
	 */
	public float getYMin() {
		return yMin;
	}

	/**
	 * Returns the maximum x-value of the rectangle.
	 * 
	 * @return xMax
	 */
	public float getXMax() {
		return xMax;
	}

	/**
	 * Returns the maximum y-value of the rectangle.
	 * 
	 * @return yMax
	 */
	public float getYMax() {
		return yMax;
	}

}
