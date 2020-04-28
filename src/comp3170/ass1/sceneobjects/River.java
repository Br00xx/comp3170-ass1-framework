package comp3170.ass1.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class River extends SceneObject {
	
	// verts used to make bezier curve
	float[] One = {-0.5f, -1.0f};
	float[] Two = {0.8f, 0.1f};
	float[] Three = {0.3f, 1.0f};

	private float[] riverVerts = quadBezierCurve(6.0f);
	
	private float[] vertices = {0, 1.0f,
								0, -1.0f,	
								1.0f, -1.0f,
								
								0, 1.0f,
								1.0f, 1.0f,
								1.0f, -1.0f
								};
	
	private int vertexBuffer;
	
	private float[] riverColour = {0.0f, 0.0f, 1.0f};
	
	public River (Shader shader) {		
		this.vertexBuffer = shader.createBuffer(vertices);
		//this.vertexBuffer = shader.createBuffer(riverVerts);
	}
	
	
	/*
	 *	Creates the bezier curve points for the amount specified
	 * 
	 *  Returns float array with x and y points
	 */
	public float[] quadBezierCurve(float pointCount) {
		float[] points = new float[(int) (pointCount*2)];
		float interval = 1/pointCount;
		
		// loops through the amount of points
		for(int i = 0; i<pointCount*2; i+=2) {
			float[] curPoint = quadBezierCurvePoint(interval*(i/2.0f), Three, Two, One);
			points[i] = curPoint[0];
			points[i+1] = curPoint[1];
			
			//System.out.println(points[i] + " " + points[i+1]);
		}
		
		return points;
	}
	
	// find the final of one point of the bezier curve
	private float[] quadBezierCurvePoint(float t, float[] pZero, float[] pOne, float[] pTwo) {
		/*
		 * 
		 *  Q = (1-t) L(t, P0, P1) + t L(t, P1, P2)
		 * 
		 */
		
		float[] point = new float[2];
		
		point[0] = (float) Math.pow(1 - t, 2) * pZero[0] + (1 - t) * 2 * t * pOne[0] + t * t * pTwo[0];
		point[1] = (float) Math.pow(1 - t, 2) * pZero[1] + (1 - t) * 2 * t * pOne[1] + t * t * pTwo[1];
		
		System.out.println(point[0] + " " + point[1]);
		return point;
	}

	
	
	@Override 
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		shader.setAttribute("a_position", this.vertexBuffer);
		shader.setUniform("u_colour", this.riverColour);
		gl.glPointSize(10);
		//gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length/2);
		// uncomment for correct bezier curve draw
		gl.glDrawArrays(GL.GL_LINE_STRIP, 0, riverVerts.length);
	}
	
}
