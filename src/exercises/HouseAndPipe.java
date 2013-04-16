package exercises;

import hilfsklassen.OBJLoader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.GLUT;

@SuppressWarnings("serial")
public class HouseAndPipe extends DrawCube {

	private int displayList;
	private long startAnimation = 0;

	/**
	 * The main method
	 */
	public static void main(String[] args) {
		HouseAndPipe template = new HouseAndPipe();
		template.setVisible(true);
	}

	// ------------------------------------------------------------

	/**
	 * Constructor
	 */
	public HouseAndPipe() {
		super();
		setTitle("My little house with a pipe");
	}

	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		OBJLoader objLoader = new OBJLoader("assets/objects/bunny", 1.0f,
				drawable.getGL());
		displayList = objLoader.getDisplayList();
	}

	// ------------------------------------------------------------

	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = getGlut();
		
		//Animation
		if(startAnimation == 0){
			startAnimation = System.currentTimeMillis();
		}
		
		float delay = 0.01f * (System.currentTimeMillis() - startAnimation);
		
		

		//Debug
		// drawCoord(gl, 10);

		int size = 4;

		// Center the graphic a bit
		gl.glPushMatrix();
		gl.glTranslatef(-size / 2, -size / 2, -size / 2);

		// Load model: bunny
		gl.glPushMatrix();
		{
			gl.glColor3f(139 / 255f, 90 / 255f, 43 / 255f);
			gl.glTranslatef(size + 1, 0, size + 1);
			gl.glCallList(displayList);
		}
		gl.glPopMatrix();

		// Draw rocket
		gl.glPushMatrix();
		{
			gl.glColor3f(1, 0, 0);
			gl.glTranslatef(0, delay, size + 2);
			gl.glRotatef(-90, 1, 0, 0);
			glut.glutSolidCylinder(0.5f, 2, 20, 20);
		}
		gl.glPopMatrix();
		gl.glPushMatrix();
		{
			gl.glColor3f(1, 0.25f, 0.25f);
			gl.glTranslatef(0, delay+2, size + 2);
			gl.glRotatef(-90, 1, 0, 0);
			glut.glutSolidCone(0.5f, 1, 20, 20);
		}
		gl.glPopMatrix();

		// Draw a house-box
		gl.glPushMatrix();
		{
			float[][] colors = { { 1, 1, 1 }, { 0, 1, 0 }, { 0, 0, 1 },
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

		// Draw root
		gl.glPushMatrix();
		{
			gl.glBegin(GL.GL_TRIANGLES);
			{
				float[] left_top = { 0, size, size };
				float[] left_back = { 0, size, 0 };
				float[] right_top = { size, size, size };
				float[] right_back = { size, size, 0 };
				float[] middle = { size / 2, size + size / 2, size / 2 };

				// Front
				gl.glColor3f(1, 0.25f, 0.25f);
				gl.glVertex3fv(left_top, 0);
				gl.glVertex3fv(right_top, 0);
				gl.glVertex3fv(middle, 0);

				// Back
				gl.glColor3f(1, 0.25f, 0.25f);
				gl.glVertex3fv(right_back, 0);
				gl.glVertex3fv(left_back, 0);
				gl.glVertex3fv(middle, 0);

				// Left
				gl.glColor3f(1, 0, 0);
				gl.glVertex3fv(left_back, 0);
				gl.glVertex3fv(left_top, 0);
				gl.glVertex3fv(middle, 0);

				// Right
				gl.glColor3f(1, 0, 0);
				gl.glVertex3fv(right_top, 0);
				gl.glVertex3fv(right_back, 0);
				gl.glVertex3fv(middle, 0);
			}
			gl.glEnd();
		}
		gl.glPopMatrix();

		// Draw pipe
		gl.glPushMatrix();
		{
			gl.glColor3f(139 / 255f, 90 / 255f, 43 / 255f);
			gl.glTranslatef(size - 1f, size, 1f);
			gl.glRotatef(-90, 1, 0, 0);
			glut.glutSolidCylinder(0.25f, 2, 20, 20);
		}
		gl.glPopMatrix();

		// To center graphic
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
