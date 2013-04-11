package sheet_1;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.GLUT;

import hilfsklassen.JoglTemplate;

public class DrawCube extends DrawSquare {

	/**
	 * The main method
	 */
	public static void main(String[] args) {
		DrawCube template = new DrawCube();
		template.setVisible(true);
	}

	// ------------------------------------------------------------

	/**
	 * Constructor
	 */
	public DrawCube() {
		super();
	}

	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		// setCamera(drawable.getGL(), 80);
	}

	// ------------------------------------------------------------

	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = getGlut();
		
		gl.glEnable(GL.GL_CULL_FACE);
		//gl.glCullFace(gl.GL_FRONT_FACE);
		
		//gl.glEnable(GL.GL_DEPTH_TEST);
		
		//Zoom out
		setView_transz(-40);

		// Draw coord-system
		drawCoord(gl,10);

		// Test drawSquare method
		gl.glPushMatrix();
		{
			int size = 4;

			float[][] colors = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 },
					{ 1, 1, 0 }, { 1, 0, 1 }, { 0, 1, 1 } };
			float[][] vertices = {
					{0,0,0}, {0,size,0}, {size,size,0}, {size,0,0},
					{0,0,size}, {0,size,size}, {size,size,size}, {size,0,size}
			};
			int[][] faces = null;

			drawCube(gl, colors, vertices, faces);
			/*
			 vertices = new float[][]{
						{0,0,0}, {0,size,0}, {size,size,0}, {size,0,0},
						{0,0,size}, {0,size,size}, {size,size,size}, {size,0,size}
				};
			drawCube(gl, colors, vertices, faces);
			*/
		}
		gl.glPopMatrix();
	}

	// ------------------------------------------------------------

	/**
	 * Draws a square based on given vertexes
	 * 
	 * Front face vertex array positions: 1 2 o-------o | | | | | | o-------o 0
	 * 3
	 * 
	 * Back face vertex array positions: 5 6 o-------o | | | | | | o-------o 4 7
	 * 
	 * Colors: Front, Left, Right, Top, Bottom, Back
	 */
	public void drawCube(GL gl, float[][] colors, float[][] vertices,
			int[][] faces) {

		gl.glPushMatrix();
		{
			// Front
			drawSquare3f(gl, colors[0], vertices[1], vertices[2], vertices[3],
					vertices[0]);
			// Left
			drawSquare3f(gl, colors[1], vertices[0], vertices[4], vertices[5],
					vertices[1]);
			// Right
			drawSquare3f(gl, colors[2], vertices[3], vertices[2], vertices[6],
					vertices[7]);
			// Top
			drawSquare3f(gl, colors[3], vertices[1], vertices[5], vertices[6],
					vertices[2]);
			// Bottom
			drawSquare3f(gl, colors[4], vertices[0], vertices[3], vertices[7],
					vertices[4]);
			// Back
			drawSquare3f(gl, colors[5], vertices[4], vertices[7], vertices[6],
					vertices[5]);
		}
		gl.glPopMatrix();
	}
}
