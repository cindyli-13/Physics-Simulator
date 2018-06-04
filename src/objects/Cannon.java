package objects;
import org.joml.Vector3f;

/**
 * This class specifies a cannon entity. It extends 
 * the rectangle class.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Sunday, June 3rd, 2018
 */
public class Cannon extends Rectangle {
	
	// constructor
	public Cannon(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration, Vector3f rotation,
			float scale, float mass, float e, float width, float height, float staticFriction, 
			float kineticFriction) {
		
		super(model, position, velocity, acceleration, rotation, scale, mass, e,width,height, 
				staticFriction, kineticFriction);
		
	}

}
