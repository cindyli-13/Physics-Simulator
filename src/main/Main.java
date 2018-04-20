package main;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

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
	
	// static variables
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 600;
	
	private static final String VERTEX_FILE = "src/shaders/vertexShader.vs";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.fs";
	
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
		glClearColor(0f, 0f, 0f, 0f); // black

		// run the loop until the user has attempted to close
		// the window or has pressed the ESCAPE key
		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			// key callback will be invoked here
			glfwPollEvents();
			
			// mouse input
			if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GL_TRUE) {
				
				// get cursor coordinate
				
				DoubleBuffer cursorPosX = BufferUtils.createDoubleBuffer(1);
				DoubleBuffer cursorPosY = BufferUtils.createDoubleBuffer(1);
				
				glfwGetCursorPos(window, cursorPosX, cursorPosY);
				
				float x = (float) cursorPosX.get(0);
				float y = (float) cursorPosY.get(0);
				
				// convert cursor coordinate to world coordinate
				x -= WIDTH/2;
				y *= -1;
				y += HEIGHT/2;
				
				// check the current screen the user is on
				switch (currScreen) {
					case 0:
						menuScreen.mouseInput(this, x, y);
						break;
					case 1:
						gameScreen.mouseInput(this, x, y);
						break;
					case 2:
						lessonScreen.mouseInput(this, x, y);
						break;
					case 3:
						customizedScreen.mouseInput(this, x, y);
						break;
				}
			}
			
			glEnable(GL_DEPTH_TEST);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			// check the current screen the user is on and render it
			switch (currScreen) {
				case 0:
					menuScreen.render(renderer);
					break;
				case 1:
					gameScreen.render(renderer);
					break;
				case 2:
					lessonScreen.render(renderer);
					break;
				case 3:
					customizedScreen.render(renderer);
					break;
			}
			
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
		});
			
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
		gameScreen = new GameScreen(loader, WIDTH, HEIGHT, z);
		lessonScreen = new LessonScreen(loader, WIDTH, HEIGHT, z);
		customizedScreen = new CustomizedScreen(loader, WIDTH, HEIGHT, z);
		
		currScreen = 0;
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
