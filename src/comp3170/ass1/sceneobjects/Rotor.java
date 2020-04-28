package comp3170.ass1.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Rotor extends SceneObject{
	
	private float[] vertices = {-0.1f, 0.5f, 
								0.1f, 0.5f,
								0, 0,
								
								0.5f, 0.1f,
								0.5f, -0.1f,
								0, 0,
								
								-0.1f, -0.5f, 
								0.1f, -0.5f,
								0, 0,
								
								-0.5f, 0.1f,
								-0.5f, -0.1f,
								0, 0,
	};
	
	private int vertexBuffer;
	private float[] colour = {0, 0, 0};
	
	public Rotor (Shader shader) {
		this.vertexBuffer = shader.createBuffer(this.vertices);
	}
	
	
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setAttribute("a_position", this.vertexBuffer);	    
		shader.setUniform("u_colour", this.colour);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length / 2);           	
         	

	}
}
