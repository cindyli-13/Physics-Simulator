package main;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import io.IO;
import objects.Loader;
import renderEngine.Renderer;
import screens.CustomizedScreen;
import screens.GameScreen;
import screens.LessonScreen;
import screens.MenuScreen;
import shaders.ShaderProgram;

/**
 * This class contains the main method for the Physics Simulator 
 * project.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Monday, April 16th, 2018
 */
public class Main {
		
	// instance variables
	private long window;
	
	private ShaderProgram shader;
	private Renderer renderer;
	private Loader loader;
	
	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	private LessonScreen lessonScreen;
	private CustomizedScreen customizedScreen;
	
	private float z = -1f;
	private int currScreen;		// 0 = menu, 1 = game, 2 = lesson, 3 = customized
	private int key;
	private boolean leftClick;
	
	// static variables
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 600;
	
	public static final String VERTEX_FILE = "src/shaders/vertexShader.vs";
	public static final String FRAGMENT_FILE = "src/shaders/fragmentShader.fs";
	
	public static final int KEY_SPACE = 0;
	
	// main
	public static void main(String[] args) {
		new Main();
	}
		
	// constructor
	public Main() {
		initGLFW();
		initEngine();
		initScreens();
		
		loop();
		
		cleanUpGLFW();
		cleanUpEngine();
	}
	
	/**
	 * Contains the render loop and logic.
	 */
	public void loop() {
		
		// this line is essential for OpenGL to function
		GL.createCapabilities();
		
		// set the clear color
		glClearColor(0.6f, 0.9f, 0.9f, 1.0f); // light blue

		// run the loop until the user has attempted to close
		// the window or has pressed the ESCAPE key
		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			
			// key callback will be invoked here
			glfwPollEvents();
			
			glEnable(GL_DEPTH_TEST);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glEnable(GL_BLEND);
			
			// check the current screen the user is on, check for inputs, then render it
			switch (currScreen) {
				case 0:
					menuScreen.input(this, window, WIDTH, HEIGHT);
					menuScreen.render(renderer);
					break;
				case 1:
					gameScreen.input(this, window, WIDTH, HEIGHT, key, leftClick);
					gameScreen.update();
					gameScreen.render(renderer);
					break;
				case 2:
					lessonScreen.input(this, window, WIDTH, HEIGHT, key, leftClick);
					lessonScreen.update();
					lessonScreen.render(renderer);
					break;
				case 3:
					customizedScreen.input(this, key, leftClick);
					customizedScreen.update();
					customizedScreen.render(renderer);
					break;
			}
			
			// reset input holders
			key = -1;
			leftClick = false;
			
			glfwSwapBuffers(window); // swap the color buffers
		}
	}
		
	/**
	 * Initializes GLFW components.
	 */
	public void initGLFW() {
			
		// set up an error callback
		GLFWErrorCallback.createPrint(System.err).set();
					
		// initialize GLFW
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
					
		// configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
					
		// create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "OpenGL Test Project", NULL, NULL);
			if (window == NULL)
				throw new RuntimeException("Failed to create the GLFW window");
						
		// set up a key callback
		// these will be detected with "poll events" in the render loop
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true);
			
			if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE)
				this.key = KEY_SPACE;
		});
		
		// set up a mouse button callback
		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
		    
			if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE)
		        this.leftClick = true;
		});
		
		this.key = -1;
		this.leftClick = false;
			
		// get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// make the OpenGL context current
		glfwMakeContextCurrent(window);
		
		// enable v-sync
		glfwSwapInterval(1);
			
		// make the window visible
		glfwShowWindow(window);
	}

	/**
	 * Initializes components of the engine.
	 */
	public void initEngine() {
	
		try {
				
			shader = new ShaderProgram(VERTEX_FILE, FRAGMENT_FILE);
			renderer = new Renderer(shader, window);
			loader = new Loader();
				
		} catch (Exception e) {
			System.out.println("Exception occured");
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the screens.
	 */
	public void initScreens() {
		
		menuScreen = new MenuScreen(loader, WIDTH, HEIGHT, z);
		gameScreen = new GameScreen(window, loader, WIDTH, HEIGHT, z);
		lessonScreen = new LessonScreen(window, loader, WIDTH, HEIGHT, z);
		customizedScreen = new CustomizedScreen(window, loader, WIDTH, HEIGHT, z);
		
		currScreen = 0;
		
		// retrieve data
		retrieveDataFromTextFiles();
	}
	
	// test method (to be removed later)
	private void retrieveDataFromTextFiles() {
		
		// just a flag to remind me to change this later
		int dummy = 0;
		
		// game screen
		retrieveDataFromFile("./data/game_test.txt", 1);
		
		// customized screen
		retrieveDataFromFile("./data/customized_test.txt", 3);
	}
	
	/**
	 * Retrieves the simulation data from the specified 
	 * file.
	 * 
	 * @param fileName
	 */
	private void retrieveDataFromFile(String fileName, int screen) {
		
		try {
			IO.openInputFile(fileName);
				
			// get number of entities
			int numEntities = Integer.parseInt(IO.readLine());
					
			// line buffer
			IO.readLine();
					
			for (int i = 0; i < numEntities; i++) {
						
				String type = IO.readLine();
						
				// rectangle
				if (type.equals("RECTANGLE")) {
							
					float sideLength = Float.parseFloat(IO.readLine());
					float x = Float.parseFloat(IO.readLine());
					float y = Float.parseFloat(IO.readLine());
					float mass = Float.parseFloat(IO.readLine());
					float e = Float.parseFloat(IO.readLine());
						
					switch (screen) {
						case 1:
							gameScreen.getSimulationWindow().createCrateEntity(sideLength, x, y, z, mass, e);
							break;
						case 2:
							lessonScreen.getSimulationWindow().createCrateEntity(sideLength, x, y, z, mass, e);
							break;
						case 3:
							customizedScreen.getSimulationWindow().createCrateEntity(sideLength, x, y, z, mass, e);
							break;
					}
				}
						
				// circle
				else if (type.equals("CIRCLE")) {
							
					float radius = Float.parseFloat(IO.readLine());
					float x = Float.parseFloat(IO.readLine());
					float y = Float.parseFloat(IO.readLine());
					float mass = Float.parseFloat(IO.readLine());
					float e = Float.parseFloat(IO.readLine());
							
					switch (screen) {
						case 1:
							gameScreen.getSimulationWindow().createBallEntity(radius, x, y, z, mass, e);
							break;
						case 2:
							lessonScreen.getSimulationWindow().createBallEntity(radius, x, y, z, mass, e);
							break;
						case 3:
							customizedScreen.getSimulationWindow().createBallEntity(radius, x, y, z, mass, e);
							break;
					}
				}
						
				// line buffer
				IO.readLine();
			}
			IO.closeInputFile();
					
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
		
	/**
	 * Cleans up GLFW components.
	 */
	public void cleanUpGLFW() {
		
		// free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	/**
	 * Cleans up engine components.
	 */
	public void cleanUpEngine() {
			
		shader.cleanUp();
	}
	
	/**
	 * Sets the value of currScreen. 0 = menu, 1 = game, 
	 * 2 = lesson, 3 = customized.
	 * 
	 * @param currScreen
	 */
	public void setCurrScreen(int currScreen) {
		
		this.currScreen = currScreen;
	}
	
}
