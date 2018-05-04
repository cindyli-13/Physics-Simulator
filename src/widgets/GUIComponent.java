package widgets;

import org.joml.Vector3f;

import objects.Model;

/**
 * This class is the blueprint for GUI components to 
 * be rendered in OpenGL.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, April 17th, 2018
 */
public class GUIComponent {

	// instance variables
	private Model model;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;

	// constructor
	public GUIComponent(Model model, Vector3f position, Vector3f rotation, float scale) {
		
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	/**
	 * Returns the GUI component's model.
	 * 
	 * @return model
	 */
	public Model getModel() {
		return model;
	}
	
	/**
	 * Sets the GUI component's model.
	 * 
	 * @param model
	 */
	public void setModel(Model model) {
		this.model = model;
	}

	/**
	 * Returns the GUI component's position.
	 * 
	 * @return postion
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Sets the GUI component's position.
	 * 
	 * @param position
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * Returns the GUI component's rotation.
	 * 
	 * @return rotation
	 */
	public Vector3f getRotation() {
		return rotation;
	}

	/**
	 * Sets the GUI component's rotation.
	 * 
	 * @param rotation
	 */
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	/**
	 * Returns the GUI component's scale.
	 * 
	 * @return scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Sets the GUI component's scale.
	 * 
	 * @param scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
}
