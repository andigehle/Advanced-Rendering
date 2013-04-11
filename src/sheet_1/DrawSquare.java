package sheet_1;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.GLUT;

import hilfsklassen.AdvancedTemplate;

public class DrawSquare extends AdvancedTemplate {

	/**
	 * The main method
	 */
	public static void main(String[] args) {
		DrawSquare template = new DrawSquare();
		template.setVisible(true);
	}

	// ------------------------------------------------------------

	/**
	 * Constructor
	 */
	public DrawSquare() {
		super();
	}

	// ------------------------------------------------------------

	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = getGlut();
		int zoom = -30;

		// Draw coord-system
		gl.glPushMatrix();
		{
			// gl.glColor3f(1.0f, 1.0f, 1.0f);
			gl.glTranslatef(0, 0, zoom);
			gl.glBegin(gl.GL_LINES);
			{
				// x-Axis
				gl.glVertex2d(-10, 0);
				gl.glVertex2d(10, 0);
				// y-Axis
				gl.glVertex2d(0, 10);
				gl.glVertex2d(0, -10);
			}
			gl.glEnd();
		}
		gl.glPopMatrix();

		// Test drawSquare method
		gl.glPushMatrix();
		{
			int size = 2;
			// Translated away from origin to avoid collisions of following
			// exercises
			gl.glTranslatef(5, 5, zoom);
			drawSquare3f(gl, new float[] { 0, 1, 0 }, new float[] { 0, 0, 0 },
					new float[] { 0, size, 0 }, new float[] { size, size, 0 },
					new float[] { size, 0, 0 });
		}
		gl.glPopMatrix();

		// Perform OpenGl operations
		gl.glPushMatrix();
		{
			int size = 2;
			gl.glTranslatef(0, 0, zoom);

			// Translate to (2,0,0)*
			gl.glTranslatef(2, 0, 0);

			// Rotating and scaling around (4,5)*
			gl.glTranslatef(2, 5, 0);
			gl.glRotatef(-45, 0, 0, 1);
			gl.glScalef(2, 1, 1);
			gl.glTranslated(Math.sqrt(1.5), -4.5, 0); // Cause of scaling factor

			drawSquare3f(gl, new float[] { 0, 0, 1 }, new float[] { 0, 0, 0 },
					new float[] { 0, size, 0 }, new float[] { size, size, 0 },
					new float[] { size, 0, 0 });
		}
		gl.glPopMatrix();
	}

	// ------------------------------------------------------------

	/**
	 * Draws a square based on given vertexes
	 */
	public void drawSquare3f(GL gl, float[] color, float[] vertex1,
			float[] vertex2, float[] vertex3, float[] vertex4) {

		gl.glPushMatrix();
		{
			gl.glColor3f(color[0], color[1], color[2]);
			gl.glBegin(gl.GL_POLYGON);
			{
				//gl.glNormal3f(0, 0, 1);
				gl.glVertex3f(vertex1[0], vertex1[1], vertex1[2]);
				gl.glVertex3f(vertex2[0], vertex2[1], vertex2[2]);
				gl.glVertex3f(vertex3[0], vertex3[1], vertex3[2]);
				gl.glVertex3f(vertex4[0], vertex4[1], vertex4[2]);
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
	}
}
