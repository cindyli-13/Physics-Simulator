package widgets;

import org.joml.Vector3f;
import objects.Model;

/**
 * This class is the blueprint for labels to 
 * be rendered in OpenGL.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Thursday, April 19th, 2018
 */
public class Label extends GUIComponent {

	// instance variables	
	private float width;
	private float height;
	
	/**
	 * Creates a label object.
	 * 
	 * @param model
	 * @param position
	 * @param rotation
	 * @param scale
	 * @param width
	 * @param height
	 */
	public Label(Model model, Vector3f position, Vector3f rotation, float scale, float width, float height) {
		
		super(model, position, rotation, scale);
		
		this.width = width;
		this.height = height;
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
