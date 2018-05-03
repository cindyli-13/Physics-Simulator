package widgets;

import org.joml.Vector2f;
import org.joml.Vector3f;

import objects.AABB;
import objects.Model;

/**
 * This class is the blueprint for buttons to 
 * be rendered in OpenGL.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Thursday, April 19th, 2018
 */
public class Button extends GUIComponent {

	// instance variables
	private AABB aabb;
	
	private float width;
	private float height;
	
	// constructor
	public Button(Model model, Vector3f position, Vector3f rotation, float scale, float width, float height) {
		
		super(model, position, rotation, scale);
		
		// create AABB
		
		float xMin = position.x - width/2;
		float xMax = position.x + width/2;
		float yMin = position.y - height/2;
		float yMax = position.y + height/2;
		
		this.aabb = new AABB(new Vector2f(xMin, yMin), new Vector2f(xMax, yMax));
		
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Returns the button's AABB.
	 * 
	 * @return aabb
	 */
	public AABB getAabb() {
		return aabb;
	}

	/**
	 * Returns the button's width.
	 * 
	 * @return width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Returns the button's height.
	 * 
	 * @return height
	 */
	public float getHeight() {
		return height;
	}

}
