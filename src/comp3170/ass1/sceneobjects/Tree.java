package comp3170.ass1.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Tree extends SceneObject {
	
	private float[] branchVertices = new float[] {
			0.0f, 1.3f,
			0.5f, 0.3f,
			-0.5f, 0.3f
	};
	
	private int branchVertexBuffer;
	
	private float[] stumpVertices = new float[] {
			-0.1f, 0.3f,
			-0.1f, 0.0f,
			0.1f, 0.0f,
			
			0.1f, 0.0f,
			0.1f, 0.3f,
			-0.1f, 0.3f
	};
	
	private int stumpVertexBuffer;
	
	private float[] branchColour = {0.0f, 1.0f, 0.0f};
	private float[] stumpColour = {0.5f, 0.2f, 0.0f};
	
	public Tree(Shader shader) {
		this.branchVertexBuffer = shader.createBuffer(this.branchVertices);
		this.stumpVertexBuffer = shader.createBuffer(this.stumpVertices);
	}
	
	@Override 
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		shader.setAttribute("a_position", this.branchVertexBuffer);
		shader.setUniform("u_colour", this.branchColour);
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.branchVertices.length);
		
		shader.setAttribute("a_position", this.stumpVertexBuffer);
		shader.setUniform("u_colour", this.stumpColour);
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.stumpVertices.length);
	}
}
