package objects;

import org.joml.Vector3f;

/**
 * This class specifies a circular entity. It manages and updates the circle's 
 * bounding-box.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, April 17th, 2018
 */
public class Circle extends Entity {

	// instance variables
	private float radius;
	
	// constructor
	public Circle(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration, Vector3f rotation,
			float scale, float mass, float e, float radius) {
		
		super(model, position, velocity, acceleration, rotation, scale, mass, e);
		
		this.radius = radius;
	}
	
	/**
	 * Checks whether or not this circle intersects another circle. 
	 * 
	 * @param c  the other circle
	 * @return true if they intersect, false otherwise
	 */
	public boolean intersects(Circle c) {
		
		float r = this.radius + c.getRadius();
		r *= r;
		
		return r > Math.pow(this.getPosition().x - c.getPosition().x, 2) 
				+ Math.pow(this.getPosition().y - c.getPosition().y, 2);
	}
	
	/**
	 * Checks whether or not this circle intersects with an AABB.
	 * 
	 * @param a  the AABB
	 * @return true if they intersect, false otherwise
	 */
	public boolean intersects(AABB a) {
		
		return a.intersects(this);
	}

	/**
	 * Returns the circle's radius.
	 * 
	 * @return radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Sets the circle's radius.
	 * 
	 * @param radius
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}
}
