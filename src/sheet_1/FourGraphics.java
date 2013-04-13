package sheet_1;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.GLUT;

import hilfsklassen.AdvancedTemplate;

public class FourGraphics extends AdvancedTemplate {

	/**
	 * The main method
	 */
	public static void main(String[] args) {
		FourGraphics template = new FourGraphics();
		template.setVisible(true);
	}

	// ------------------------------------------------------------

	/**
	 * Constructor
	 */
	public FourGraphics() {
		super();
	}

	// ------------------------------------------------------------

	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = getGlut();
		GLU glu = getGlu();
		int w = 7, h = 7, zoom = -40, size = 5;
		int slices = 20, stacks = 20;
		
//		gl.glEnable(GL.GL_DEPTH_TEST);
//		gl.glDepthRange(0.0,1.0);
//		gl.glMatrixMode(GL.GL_MODELVIEW);
//		gl.glLoadIdentity();
//		gl.glEnable(GL.GL_LIGHTING);
//		gl.glEnable(GL.GL_LIGHT0);
//		gl.glShadeModel(GL.GL_SMOOTH);
//		float light_position[] = {
//		2.0f, 0.0f, 0.0f, 1.0f };
//		gl.glLightfv(GL.GL_LIGHT0,
//		GL.GL_POSITION,
//		light_position,0);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		//Light
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[]{2,0,0,1}, 0);
		//gl.glLightf(0, GL.GL_AMBIENT, 0);

        // Set material properties.
        float[] rgba = {0.3f, 0.5f, 1f};
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 0.5f);

		// Draw cube
		gl.glPushMatrix();
		{
			gl.glColor3f(1.0f, 0f, 0f);
			gl.glTranslatef(-w, h, zoom);
			glut.glutSolidCube(size);
		}
		gl.glPopMatrix();

		// Draw sphere
		gl.glPushMatrix();
		{
			gl.glColor3f(0f, 1.0f, 0f);
			gl.glTranslatef(w, h, zoom);
			glut.glutSolidSphere(size / 2, slices, stacks);
		}
		gl.glPopMatrix();

		// Draw torus
		gl.glPushMatrix();
		{
			gl.glColor3f(1.0f, 1.0f, 0f);
			gl.glTranslatef(w, -h, zoom);
			glut.glutSolidTorus(size / 2 - 1.25, size / 2, slices, stacks);
		}
		gl.glPopMatrix();

		// Draw cylinder
		gl.glPushMatrix();
		{
			gl.glColor3f(0f, 0f, 1.0f);
			gl.glTranslatef(-w, -h - size / 2, zoom);
			gl.glRotatef(-90, 1, 0, 0);
			glut.glutSolidCylinder(size / 2, size, slices, stacks);
		}
		gl.glPopMatrix();
	}
}
