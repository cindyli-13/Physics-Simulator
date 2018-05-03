package objects;

import org.joml.Vector2f;

import physicsEngine.Physics;

/**
 * This class is an implementation of the Axis-Aligned Bounding Box, a bounding box 
 * which has sides that are parallel to the x- and y-axis.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, April 17th, 2018
 */
public class AABB {

	// instance variables
	private Vector2f min;
	private Vector2f max;
	
	// constructor
	public AABB(Vector2f min, Vector2f max) {
		
		this.min = new Vector2f();
		this.min.x = min.x;
		this.min.y = min.y;
		
		this.max = new Vector2f();
		this.max.x = max.x;
		this.max.y = max.y;
	}
	
	/**
	 * Checks whether or not this AABB intersects another AABB.
	 * 
	 * @param a the other AABB
	 * @return true if they intersect, false otherwise
	 */
	public boolean intersects(AABB a) {
		
		if (this.max.x <= a.min.x || this.min.x >= a.max.x)
			return false;
		if (this.max.y <= a.min.y || this.min.y >= a.max.y)
			return false;
		
		return true;
	}
	
	/**
	 * Checks whether or not this AABB intersects with a circle.
	 * 
	 * @param c  the circle
	 * @return true if they intersect, false otherwise
	 */
	public boolean intersects(Circle c) {
		
		if (this.max.x <= c.getPosition().x - c.getRadius() || 
				this.min.x >= c.getPosition().x + c.getRadius())
			return false;
		
		if (this.max.y <= c.getPosition().y - c.getRadius() || 
				this.min.y >= c.getPosition().y + c.getRadius())
			return false;
		
		// clamp closest point to the AABB's extents
		Vector2f closest = new Vector2f(c.getPosition().x, c.getPosition().y);
		closest.x = Physics.clamp(closest.x, this.min.x, this.max.x);
		closest.y = Physics.clamp(closest.y, this.min.y, this.max.y);
		
		Vector2f circleCenter = new Vector2f(c.getPosition().x, c.getPosition().y);
		float distance = circleCenter.distance(closest);
		
		if (distance >= c.getRadius())
			return false;
		
		return true;
	}
	
	/**
	 * Checks whether or not this AABB intersects with a point.
	 * 
	 * @param x		the x coordinate of the point
	 * @param y		the y coordinate of the point
	 * @return true if they intersect, false otherwise
	 */
	public boolean intersects(float x, float y) {
		
		return (x >= this.min.x && x <= this.max.x && y >= this.min.y && y <= this.max.y);
	}

	/**
	 * Returns the coordinate of the minimum point of the AABB (xMin, yMin).
	 * 
	 * @return min
	 */
	public Vector2f getMin() {
		return min;
	}

	/**
	 * Sets the coordinate of the minimum point of the AABB (xMin, yMin).
	 * 
	 * @param min
	 */
	public void setMin(Vector2f min) {
		this.min.x = min.x;
		this.min.y = min.y;
	}

	/**
	 * Returns the coordinate of the maximum point of the AABB (xMax, yMax).
	 * 
	 * @return max
	 */
	public Vector2f getMax() {
		return max;
	}

	/**
	 * Sets the coordinate of the maximum point of the AABB (xMax, yMax).
	 * 
	 * @param max
	 */
	public void setMax(Vector2f max) {
		this.max.x = max.x;
		this.max.y = max.y;
	}
}
