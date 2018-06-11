package objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

/**
 * This is a helper class to load attributes 
 * into a Model.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Monday, April 16th, 2018
 */
public class Loader {

	// instance variables
	private static ArrayList<Integer> vaos;
	private static ArrayList<Integer> vbos;
	private static ArrayList<Integer> textures;
	
	/**
	 * Constructor.
	 */
	public Loader() {
		vaos = new ArrayList<Integer>();
		vbos = new ArrayList<Integer>();
		textures = new ArrayList<Integer>();
	}
	
	/**
	 * Takes in the attributes to create a Model and loads them into a new Model. 
	 * A Model is a blueprint for objects to be rendered in OpenGL. Each Model 
	 * has its Vertex Attribute Array, or VAO, which essentially stores the model's 
	 * attributes (eg. vertices, texCoords) in Vertex Buffer Objects, or VBOs. A 
	 * VBO stores a list of the elements of an attribute. A VAO is a list of VBOs.
	 * 
	 * @param vertices  the float array that contains the model's vertices
	 * @param texCoords  the float array that contains the model texture's vertices
	 * @param indices  the integer array that contains the indices to which the 
	 * 				   model's attributes' elements are referred
	 * @param textureID  the ID of the model texture
	 * @return Model m
	 */
	public Model loadToVAO(float[] vertices, float[] texCoords, int[] indices, int textureID) {
		
		// create a new VAO
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		vaos.add(vaoID);
		
		// bind the indices to the attribute elements
		bindIndicesBuffer(indices);
		
		// store data in attribute lists (VBOs)
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, texCoords);
		
		// unbind VAO
		glBindVertexArray(0);
		
		return new Model(vaoID, indices.length, textureID);
	}
	
	/**
	 * Takes in a texture a file and binds the texture to a texture ID. 
	 * Returns the texture ID.
	 * 
	 * @param filename  the name of the texture file
	 * @return id  the texture ID
	 */
	public int loadTexture(String filename) {
		
		BufferedImage bi;
		int width;
		int height;
		int id;
		
		// put the texture in a ByteBuffer and bind the texture to a 
		// texture ID
		try {
			bi = ImageIO.read(new File(filename));
			width = bi.getWidth();
			height = bi.getHeight();
			
			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);
			
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					
					int pixel = pixels_raw[i * height + j];
					pixels.put((byte) ((pixel >> 16) & 0xFF)); 	// RED
					pixels.put((byte) ((pixel >> 8) & 0xFF)); 	// GREEN
					pixels.put((byte) (pixel & 0xFF)); 			// BLUE
					pixels.put((byte) ((pixel >> 24) & 0xFF)); 	// ALPHA
				}
			}
			
			pixels.flip();
			
			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			
			textures.add(id);
			
			return id;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * A cleanup method to erase memory data when the program stops.
	 */
	public void cleanUp() {
		
		for (int vao:vaos) {
			glDeleteVertexArrays(vao);
		}
		for (int vbo:vbos) {
			glDeleteBuffers(vbo);
		}
		for (int texture:textures) {
			glDeleteTextures(texture);
		}
	}

	/**
	 * Takes in information about an attribute and stores them into a VBO, 
	 * then storing that VBO into the model's VAO.
	 * 
	 * @param attributeNumber  the index of the attribute list
	 * @param coordinateSize  the size of each element in the attribute list
	 * @param data  the float array that contains the attribute's elements
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		
		// create new VBO
		int vboID = glGenBuffers();
		vbos.add(vboID);
		
		// bind VBO and store data in VBO
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		MemoryUtil.memFree(buffer);
		glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		// clear buffer data
		if (buffer != null)
		    MemoryUtil.memFree(buffer);
	}
	
	/**
	 * Takes in the indices and binds them to the model.
	 * 
	 * @param indices  the integer array that contains the indices to which the 
	 * 				   model's attributes' elements are referred
	 */
	private void bindIndicesBuffer(int[] indices) {
		
		// create new VBO
		int vboID = glGenBuffers();
		vbos.add(vboID);
		
		// bind VBO and store indices in VBO
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}
	
	/**
	 * Helper method to store data in an integer array into an IntBuffer.
	 * 
	 * @param data  the integer array containing the data to store
	 * @return buffer  the IntBuffer
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		
		IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	/**
	 * Helper method to store data in an float array into an FloatBuffer.
	 * 
	 * @param data  the float array containing the data to store
	 * @return buffer  the FloatBuffer
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
}
