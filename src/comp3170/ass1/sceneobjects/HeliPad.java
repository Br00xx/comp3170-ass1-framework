package comp3170.ass1.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class HeliPad extends SceneObject{
	
	private float[] heliPad = {
			-1.0f, 1.0f,
			-1.0f, -1.0f, 
			1.0f, 1.0f, 
			
			-1.0f, -1.0f, 
			1.0f, 1.0f, 
			1.0f, -1.0f
	};
	
	private float[] heliPadH = {
			-0.5f, 0.5f, 
			-0.5f, -0.5f, 
			-0.25f, -0.5f, 
			
			-0.5f, 0.5f, 
			-0.25f, -0.5f, 
			-0.25f, 0.5f, 
			
			-0.25f, 0.2f, 
			-0.25f, -0.2f, 
			0.25f, 0.2f, 
			
			-0.25f, -0.2f, 
			0.25f, 0.2f,
			0.25f, -0.2f,
			
			0.5f, 0.5f, 
			0.5f, -0.5f, 
			0.25f, -0.5f, 
			
			0.5f, 0.5f, 
			0.25f, -0.5f, 
			0.25f, 0.5f, 
	};
	
	private int vertexBufferPad;
	private int vertexBufferH;
	
	private float[] padColour = {0.8f, 0.8f, 0.8f};
	private float[] hColour = {1.0f, 0, 0};
	
	
	public HeliPad (Shader shader) {
		this.vertexBufferPad = shader.createBuffer(heliPad);
		this.vertexBufferH = shader.createBuffer(heliPadH);
	}
	
	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		shader.setAttribute("a_position", this.vertexBufferPad);
		shader.setUniform("u_colour", this.padColour);
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.heliPad.length/2);
		
		shader.setAttribute("a_position", this.vertexBufferH);
		shader.setUniform("u_colour", this.hColour);
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.heliPadH.length/2);
	}
	
}
