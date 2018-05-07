package renderEngine;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import objects.Entity;
import shaders.ShaderProgram;
import widgets.GUIComponent;

/**
 * This class allows objects to be rendered in OpenGL.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Tuesday, April 17th, 2018
 */
public class Renderer {

	// static variables
	private static final float Z_NEAR = 1f; 	// distance from camera to near plane
	private static final float Z_FAR = 1000; 	// distance from camera to far plane
	
	// instance variables
	private Matrix4f projectionMatrix;
	private ShaderProgram shader;
	private long window;
	
	//constructor
	public Renderer(ShaderProgram shader, long window) {
		
		this.shader = shader;
		this.window = window;
		
		createProjectionMatrix();
		
		shader.bind();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.unbind();
	}
	
	/**
	 * Takes in an entity and renders it in the OpenGL 
	 * world.
	 * 
	 * @param entity
	 */
	public void render(Entity entity) {
		
		shader.bind();
			
		// create transformation matrix
		Matrix4f worldMatrix = Transformation.getWorldMatrix(
				entity.getPosition(), 
				entity.getRotation(), 
				entity.getScale());
			
		shader.loadWorldMatrix(worldMatrix);
			
			
		// bind the entity's model
		glBindVertexArray(entity.getModel().getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
			
		// render the entity's model
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, entity.getModel().getTextureID());
		glDrawElements(GL_TRIANGLES, entity.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			
		// unbind the entity's model
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		
		shader.unbind();
	}
	
	/**
	 * Takes in an ArrayList of entities and renders each one in the OpenGL 
	 * world.
	 * 
	 * @param entities  the ArrayList of entities to render
	 */
	public void render(ArrayList<Entity> entities) {
		
		// loop through entities
		for (Entity entity:entities)
			render(entity);
	}
	
	/**
	 * Takes in a GUI component and renders it in the OpenGL 
	 * world.
	 * 
	 * @param guiComponent
	 */
	public void render(GUIComponent guiComponent) {
		
		shader.bind();
			
		// create transformation matrix
		Matrix4f worldMatrix = Transformation.getWorldMatrix(
				guiComponent.getPosition(), 
				guiComponent.getRotation(), 
				guiComponent.getScale());
			
		shader.loadWorldMatrix(worldMatrix);
			
		// bind the GUI component's model
		glBindVertexArray(guiComponent.getModel().getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
			
		// render the GUI component's model
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, guiComponent.getModel().getTextureID());
		glDrawElements(GL_TRIANGLES, guiComponent.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			
		// unbind the GUI component's model
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	
	/**
	 * Takes in an ArrayList of GUI components and renders each one in 
	 * the OpenGL world.
	 * 
	 * @param guiComponents  the ArrayList of GUI components
	 */
	public void renderGUI(ArrayList<GUIComponent> guiComponents) {
		
		// loop through GUI components
		for (GUIComponent guiComponent:guiComponents)
			render(guiComponent);
	}
	
	/**
	 * Creates a projetion matrix. A projection matrix controls the 
	 * view through which the OpenGL world is displayed to the screen.
	 */
	public void createProjectionMatrix() {
		
		// get width and height of window
		IntBuffer w = MemoryUtil.memAllocInt(1);
		IntBuffer h = MemoryUtil.memAllocInt(1);
		glfwGetWindowSize(window, w, h);
		int width = w.get(0);
		int height = h.get(0);
		
		// create projection matrix
		projectionMatrix = new Matrix4f().ortho(-width / 2, width / 2, -height / 2, height / 2, Z_NEAR, Z_FAR);
	}
}
