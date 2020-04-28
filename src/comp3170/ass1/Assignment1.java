/*
 * 	Joshua Brookes
 * 	43603467
 * 
 * 	Assignment 1, COMP 3170
 *  
 */

package comp3170.ass1;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import comp3170.GLException;
import comp3170.InputManager;
import comp3170.Shader;
import comp3170.ass1.sceneobjects.HeliPad;
import comp3170.ass1.sceneobjects.Helicopter;
import comp3170.ass1.sceneobjects.House;
import comp3170.ass1.sceneobjects.River;
import comp3170.ass1.sceneobjects.Rotor;
import comp3170.ass1.sceneobjects.SceneObject;
import comp3170.ass1.sceneobjects.Tree;

public class Assignment1 extends JFrame implements GLEventListener {
	
	final private float TAU = (float) (Math.PI * 2);
	
	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/ass1"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private InputManager input;
	
	private SceneObject root;	
	
	private int winWidth = 1000;
	private int winHeight = 1000;
	
	private Matrix4f worldMatrix;
	private Matrix4f viewMatrix;
	
	Animator anim;
	private long oldTime;
	
	private SceneObject camera;
	
	Helicopter heli;
	Rotor rotorBack;
	Rotor rotorFront;
	
	public Assignment1() {
		super("COMP3170 Assignment 1");
		
		// create an OpenGL 4 canvas and add this as a listener
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// create an input manager to listen for keypresses and mouse events
		
		this.input = new InputManager();
		input.addListener(this);
		input.addListener(this.canvas);

		// set up the JFrame		
		
		this.setSize(winWidth, winHeight);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});	
		
		anim = new Animator(canvas);
		anim.start();
		this.oldTime = System.currentTimeMillis();
	}

	@Override
	/**
	 * Initialise the GLCanvas
	 */
	public void init(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			this.shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// allocate matrices
		
		this.worldMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		
		// construct objects and attach to the scene-graph
		this.root = new SceneObject();
		
		River river = new River(this.shader);
		river.setParent(this.root);
		// comment out next two lines when looking at bezier curve river
		river.localMatrix.rotate(TAU/8, 0, 0, 1);
		river.localMatrix.scale(0.2f, 2.5f, 1.0f);

		
		House town = new House(this.shader);
		town.localMatrix.translate(-0.1f, -0.2f, 0);
		town.localMatrix.scale(0.04f, 0.04f, 1);
		town.setParent(this.root);
		
		House house2 = new House(this.shader);
		house2.localMatrix.translate(-3.3f, 4.5f, 0);
		house2.setParent(town);
		
		House house3 = new House(this.shader);
		house3.localMatrix.translate(8f, 10.0f, 0);
		house3.setParent(town);
		
		
		Tree forest = new Tree(this.shader);
		forest.localMatrix.translate(0.4f, 0.2f, 0);
		forest.localMatrix.scale(0.1f);
		forest.setParent(this.root);
		
		Tree tree = new Tree(this.shader);
		tree.setParent(forest);
		tree.localMatrix.translate(0.5f, -2.0f, 0);
		
		Tree treeTwo = new Tree(this.shader);
		treeTwo.setParent(forest);
		treeTwo.localMatrix.translate(-1.5f, 1.5f, 0);
		
		HeliPad heliPad = new HeliPad(this.shader);
		heliPad.setParent(this.root);
		heliPad.localMatrix.translate(-0.5f, -0.5f, 0);
		heliPad.localMatrix.scale(0.1f);
		
		heli = new Helicopter(shader);
		heli.setParent(this.root);
		heli.localMatrix.scale(0.1f);
		
		rotorBack = new Rotor(shader);
		rotorBack.setParent(heli);
		rotorBack.localMatrix.translate(-0.5f, 0, 0);
		rotorBack.localMatrix.scale(0.8f);
		
		rotorFront = new Rotor(shader);
		rotorFront.setParent(heli);
		rotorFront.localMatrix.translate(0.5f, 0, 0);
		rotorFront.localMatrix.scale(0.8f);
		
		this.camera = new SceneObject();
		this.camera.setParent(this.root);
	}
	
	
	/*
	 * 
	 * Movement of the helicopter done using UP DOWN LEFT RIGHT
	 * 
	 * Rotors rotation is updated here
	 * 
	 */
	private final float heliTurn = TAU/2;
	public void update(float dt) {
		
		if (this.input.isKeyDown(KeyEvent.VK_UP)) {
			this.heli.localMatrix.translate(0.02f, 0, 0);
			
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_DOWN)) {
			this.heli.localMatrix.translate(-0.02f, 0, 0);
		}

		
		if (this.input.isKeyDown(KeyEvent.VK_LEFT)) {
			this.heli.localMatrix.rotateZ(heliTurn * dt);
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_RIGHT)) {
			this.heli.localMatrix.rotateZ(-heliTurn* dt);
		}
		
		rotorFront.localMatrix.rotate(TAU * dt, 0, 0, 1);
		rotorBack.localMatrix.rotate(-TAU * dt, 0, 0, 1);
		
	}
	
	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// compute time since last frame
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000.0f;
		oldTime = time;
		update(dt);
		
		// set the background colour to dark green
		gl.glClearColor(0.1f, 0.6f, 0.1f, 1.0f);
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		
		this.shader.enable();

		this.camera.getWorldMatrix(this.viewMatrix);
		this.viewMatrix.invert();
		shader.setUniform("u_viewMatrix", this.viewMatrix);
		
		this.worldMatrix.identity();
		this.root.draw(shader, worldMatrix);

	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		float xChange = (float)width/this.winWidth;
		float yChange = (float)height/this.winHeight;
		
		this.winWidth = width;
		this.winHeight = height;
		
		this.camera.localMatrix.scale(xChange, yChange, 1);
		
	}

	@Override
	/**
	 * Called when we dispose of the canvas 
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	public static void main(String[] args) throws IOException, GLException {
		new Assignment1();
	}


}
