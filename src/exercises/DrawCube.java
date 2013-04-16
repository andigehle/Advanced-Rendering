package exercises;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

@SuppressWarnings("serial")
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
		//Enable culling & depth buffer
		setModiFlag(11);		
	}

	// ------------------------------------------------------------

	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		// Draw coord-system
		drawCoord(gl, 10);

		// Test drawSquare method
		gl.glPushMatrix();
		{
			int size = 4;

			float[][] colors = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 },
					{ 1, 1, 0 }, { 1, 0, 1 }, { 0, 1, 1 } };
			float[][] vertices = { { 0, 0, 0 }, { 0, size, 0 },
					{ size, size, 0 }, { size, 0, 0 }, { 0, 0, size },
					{ 0, size, size }, { size, size, size }, { size, 0, size } };
			// Front - Left - Right - Top - Bottom - Back
			int[][] faces = { { 0, 1, 2, 3 }, { 0, 4, 5, 1 }, { 3, 2, 6, 7 },
					{ 1, 5, 6, 2 }, { 0, 3, 7, 4 }, { 4, 7, 6, 5 } };

			drawCube(gl, colors, vertices, faces);
		}
		gl.glPopMatrix();
	}

	// ------------------------------------------------------------

	/**
	 * Draws a square based on given vertexes
	 * 
	 * Front face vertex array positions: 
	 * 1       2 
	 * o-------o 
	 * |       | 
	 * |       |
	 * |       |
	 * o-------o 
	 * 0       3
	 * 
	 * Back face vertex array positions:
	 * 5       6 
	 * o-------o 
	 * |       | 
	 * |       |
	 * |       |
	 * o-------o 
	 * 4       7
	 * 
	 * Colors: Front, Left, Right, Top, Bottom, Back
	 */
	public void drawCube(GL gl, float[][] colors, float[][] vertices,
			int[][] faces) {

		gl.glPushMatrix();
		{
			for (int i = 0; i < 6; i++) {

				drawSquare3f(gl, colors[i], vertices[faces[i][0]],
						vertices[faces[i][1]], vertices[faces[i][2]],
						vertices[faces[i][3]]);
			}
		}
		gl.glPopMatrix();
	}
}
