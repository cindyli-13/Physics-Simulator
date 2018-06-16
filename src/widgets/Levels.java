package widgets;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;

/**
 * tests for if the ball intersects the target and what happens when it does
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Thursday, May 3rd, 2018
 */
 
public class Levels {

	//static variables
	private static final String CONGRATULATION_MESSAGE_TEXTURE_FILE = "./res/CongratulationsMessage.png";

	/**
	 * checks if the ball intersects target
	 * 
	 * @param level current level it passed
	 * @param Ball the circle entity
	 * @param Target the target entity
	 * 
	 * @return a boolean
	 */
	public boolean check(int level, Entity Ball, Entity Target)
	{
		if(Ball.intersects(Target))
		{
			return true;
		}
		return false;
	}

	/**
	 * displays the congratulation model
	 * 
	 * @param simulation takes in the current SimulationWindow
	 * @param loader the current loader used
	 * @ param z the z position of the screen
	 * 
	 * @ return a Label which is the congratulation message
	 */
	public Label displayMessage(SimulationWindow simulation,Loader loader, float z)
	{
		float labelWidth = 3f;
		float labelHeight = 3f;

		float[] vertices = Entity.getVertices(70, 70, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();

		float labelX = (simulation.getMax().x + simulation.getMin().x) / 2;
		float labelY = (simulation.getMax().y + simulation.getMin().y) / 2;

		Vector3f position = new Vector3f(labelX, labelY, z);
		Vector3f rotation = new Vector3f(0,0,0);

		int textureID = loader.loadTexture(CONGRATULATION_MESSAGE_TEXTURE_FILE);
		Model selectionASimModel = loader.loadToVAO(vertices, texCoords, indices, textureID);

		Label message = new Label(selectionASimModel, position, rotation, labelWidth, labelWidth, labelHeight);
		return message;
	}

	
}