package widgets;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;

public class Levels {
private static final String CONGRATULATION_MESSAGE_TEXTURE_FILE = "./res/CongratulationsMessage.png";
private static final String DISABLED_TEXTURE_FILE = "./res/LevelDisabled.png";
	public boolean check(int level, Entity Ball, Entity Target)
	{
		if(Ball.intersects(Target)==true)
		{
			return true;
		}
//		if(level == 1)
//		{
//			
//		}
//		else if(level == 2)
//		{
//			if(Ball.intersects(Target)==true)
//			{
//				return true;
//			}
//		}
//		
		return false;
	}
	
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
	public Label ndisplayMessage(SimulationWindow simulation,Loader loader, float z)
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
				
		int textureID = loader.loadTexture(DISABLED_TEXTURE_FILE);
		Model selectionASimModel = loader.loadToVAO(vertices, texCoords, indices, textureID);
						
		Label message = new Label(selectionASimModel, position, rotation, labelWidth, labelWidth, labelHeight);
		return message;
	}
}
