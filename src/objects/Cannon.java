package objects;
import org.joml.Vector3f;

/**
 * This class specifies a cannon entity. It extends 
 * the rectangle class. Cannons are special in the sense 
 * that they contain a stored velocity. If another object 
 * falls on the cannon it will be shot with the stored 
 * velocity of the cannon.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Sunday, June 3rd, 2018
 */
public class Cannon extends Rectangle {
	
	/**
	 * Creates a cannon object.
	 * 
	 * @param model
	 * @param position
	 * @param velocity
	 * @param acceleration
	 * @param rotation
	 * @param scale
	 * @param mass
	 * @param e
	 * @param width
	 * @param height
	 * @param staticFriction
	 * @param kineticFriction
	 */
	public Cannon(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration, Vector3f rotation,
			float scale, float mass, float e, float width, float height, float staticFriction, 
			float kineticFriction) {
		
		super(model, position, velocity, acceleration, rotation, scale, mass, e,width,height, 
				staticFriction, kineticFriction);
		
	}

}
