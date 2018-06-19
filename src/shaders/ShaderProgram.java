package shaders;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

/**
 * This class manages the vertex and fragment shaders which 
 * OpenGL uses to render objects.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Monday, April 16th, 2018
 */
public class ShaderProgram {

	// static variables
	private static FloatBuffer matrixBuffer;
	
	// instance variables
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private int locationProjectionMatrix;
	private int locationWorldMatrix;
	
	/**
	 * Creates a shader program.
	 * 
	 * @param vertexFile		the location of the vertex shader file
	 * @param fragmentFile		the location of the fragment shader file
	 * @throws Exception
	 */
	public ShaderProgram(String vertexFile, String fragmentFile) throws Exception {
		
		// this line is important and is required for OpenGL to function
		GL.createCapabilities();
		
		// create shader program
		programID = glCreateProgram();
		
		// error message
		if (programID == 0)
			throw new Exception("Could not create Shader Program");
		
		// create vertex and fragment shaders
		vertexShaderID = createShader(vertexFile, GL_VERTEX_SHADER);
		fragmentShaderID = createShader(fragmentFile, GL_FRAGMENT_SHADER);
		
		bindAllAttributes();
		link();
		getAllUniformLocations();
	}
	
	/**
	 * Creates a shader given the shader file and shader type (eg. vertex 
	 * or fragment).
	 * 
	 * @param file  the file containing the shader code
	 * @param type  specifies the type of the shader (eg. vertex or fragment)
	 * @return shaderID  the shader's ID
	 * @throws Exception
	 */
	private int createShader(String file, int type) throws Exception {
		
		StringBuilder shaderSource = new StringBuilder();
		
		// put shader code into BufferedReader
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}
		catch (IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(1);
		}
		
		// get shader ID
		int shaderID = glCreateShader(type);
		
		// error message
		if (shaderID == 0)
			throw new Exception("Error creating Shader. Type: " + type);
		
		// link shader ID to shader code and compile the shader
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		
		// error message
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0)
			throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderID, 1024));
		
		// attach the shader to the program
		glAttachShader(programID, shaderID);
		
		return shaderID;
	}

	/**
	 * Links and validates the shader program.
	 * 
	 * @throws Exception
	 */
	public void link() throws Exception {
		
        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programID, 1024));
        }

        if (vertexShaderID != 0) {
            glDetachShader(programID, vertexShaderID);
        }
        if (fragmentShaderID != 0) {
            glDetachShader(programID, fragmentShaderID);
        }

        glValidateProgram(programID);
        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID, 1024));
        }
    }
	
	/**
	 * Loads the projection matrix to the shader's uniform variable.
	 * 
	 * @param projection  the projection matrix to load
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(locationProjectionMatrix, projection);
	}
	
	/**
	 * Loads the world matrix to the shader's uniform variable.
	 * 
	 * @param world  the world matrix to load
	 */
	public void loadWorldMatrix(Matrix4f world) {
		loadMatrix(locationWorldMatrix, world);
	}
	
	/**
	 * Binds the shader program.
	 */
	public void bind() {
		glUseProgram(programID);
	}
	
	/**
	 * Unbinds the shader program.
	 */
	public void unbind() {
		glUseProgram(0);
	}

	/**
	 * Cleanup method for clearing shader data when the program stops.
	 */
	public void cleanUp() {
		
		unbind();
		
		if (programID != 0)
			glDeleteProgram(programID);
	}
	
	/**
	 * Associates the attributes of a model to named attribute variables.
	 */
	private void bindAllAttributes() {
		glBindAttribLocation(programID, 0, "position");
		glBindAttribLocation(programID, 1, "textures");
	}
	
	/**
	 * Gets the locations of the uniform variables.
	 */
	private void getAllUniformLocations() {
		locationProjectionMatrix = glGetUniformLocation(programID, "projectionMatrix");
		locationWorldMatrix = glGetUniformLocation(programID, "worldMatrix");
	}
	
	/**
	 * Loads a matrix to the specified location.
	 * 
	 * @param location  the location to load the matrix to
	 * @param matrix  the matrix to load
	 */
	private void loadMatrix(int location, Matrix4f matrix) {
		matrixBuffer = MemoryUtil.memAllocFloat(16);
		matrix.get(matrixBuffer);
		glUniformMatrix4fv(location, false, matrixBuffer);
	}
}
