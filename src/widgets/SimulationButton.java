package widgets;

import org.joml.Vector3f;

import objects.Loader;
import objects.Model;
import textRender.Text;

/**
 * This class is the blueprint for simulation 
 * buttons of the sidebar.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, May 29th, 2018
 */
public class SimulationButton extends Button {

	// instance variables
	String fileName;
	Text text;
	
	/**
	 * Creates a simulation button object.
	 * 
	 * @param model
	 * @param position
	 * @param rotation
	 * @param scale
	 * @param width
	 * @param height
	 * @param fileName
	 * @param loader
	 */
	public SimulationButton(Model model, Vector3f position, Vector3f rotation, float scale, float width, float height,
			String fileName, Loader loader) {
		
		super(model, position, rotation, scale, width, height);
		
		this.fileName = fileName;
		
		text = new Text("", position.x, position.y - 15f, position.z + 99f, 20f, 20f, loader);
	}
	
	/**
	 * Returns the file name.
	 * 
	 * @return fileName
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Returns the text.
	 * 
	 * @return text
	 */
	public Text getText() {
		return text;
	}
}
