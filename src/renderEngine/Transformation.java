package renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * This class helps to create matrices. Matrices are used to apply 
 * transformations to objects in the OpenGL world.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, April 17th, 2018
 */
public class Transformation {
	
	/**
	 * Creates a world matrix based on the given parameters.
	 * 
	 * @param offset  the offset from the origin of the OpenGL coordinate system
	 * @param rotation  the rotation to be applied to the model
	 * @param scale  the model's scale
	 * @return worldMatrix  the world matrix
	 */
	public static Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
		
		Matrix4f worldMatrix = new Matrix4f();
		
		worldMatrix.identity().translate(offset).
			rotateX((float) Math.toRadians(rotation.x)).
			rotateY((float) Math.toRadians(rotation.y)).
			rotateZ((float) Math.toRadians(rotation.z)).
			scale(scale);
		return worldMatrix;
	}
}
