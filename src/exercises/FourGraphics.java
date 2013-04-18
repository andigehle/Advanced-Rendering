package exercises;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import com.sun.opengl.util.GLUT;

import hilfsklassen.AdvancedTemplate;

@SuppressWarnings("serial")
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
		setTitle("Four graphics");

		setModiFlag(MODI_DEPTH_BUFFERING);
		setLightFlag(LIGHT_DIRECTIONAL);
		setLightFlag(LIGHT_SHADING);
	}

	// ------------------------------------------------------------

	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = getGlut();

		int w = 7, h = 7, zoom = 0, size = 5;
		int slices = 20, stacks = 20;

		boolean use_materials = false;

		// drawCoord(gl, 10);

		// Materials
		float[] BRASS_MATERIAL = { 0.33f, 0.22f, 0.03f, 1.0f, 0.78f, 0.57f,
				0.11f, 1.0f, 0.99f, 0.91f, 0.81f, 1.0f, 5.0f };
		float[] REDPLASTIC_MATERIAL = { 0.3f, 0.0f, 0.0f, 1.0f, 0.6f, 0.0f,
				0.0f, 1.0f, 0.8f, 0.4f, 0.4f, 1.0f, 10.0f };
		float[] WHITESHINEY_MATERIAL = { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 100.0f };
		float[] TEST = { 0.3f, 0.5f, 1f, 0.5f, 0.3f, 0.5f, 1f, 0.5f, 0.3f,
				0.5f, 1f, 0.5f, 5 };

		if (!use_materials) {
			gl.glEnable(GL.GL_COLOR_MATERIAL);
		}

		// Draw cube
		gl.glPushMatrix();
		{
			if (use_materials) {
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, BRASS_MATERIAL, 0);
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, BRASS_MATERIAL, 4);
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, BRASS_MATERIAL, 8);
				gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, BRASS_MATERIAL[12]);
			} else {
				gl.glColor3f(1.0f, 0f, 0f);
			}
			gl.glTranslatef(-w, h, zoom);
			glut.glutSolidCube(size);
		}
		gl.glPopMatrix();

		// Draw sphere
		gl.glPushMatrix();
		{
			if (use_materials) {
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT,
						REDPLASTIC_MATERIAL, 0);
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE,
						REDPLASTIC_MATERIAL, 4);
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR,
						REDPLASTIC_MATERIAL, 8);
				gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS,
						REDPLASTIC_MATERIAL[12]);
			} else {
				gl.glColor3f(0f, 1.0f, 0f);
			}
			gl.glTranslatef(w, h, zoom);
			glut.glutSolidSphere(size / 2, slices, stacks);
		}
		gl.glPopMatrix();

		// Draw torus
		gl.glPushMatrix();
		{
			if (use_materials) {
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT,
						WHITESHINEY_MATERIAL, 0);
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE,
						WHITESHINEY_MATERIAL, 4);
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR,
						WHITESHINEY_MATERIAL, 8);
				gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS,
						WHITESHINEY_MATERIAL[12]);
			} else {
				gl.glColor3f(1.0f, 1.0f, 0f);
			}
			gl.glTranslatef(w, -h, zoom);
			glut.glutSolidTorus(size / 2 - 1.25, size / 2, slices, stacks);
		}
		gl.glPopMatrix();

		// Draw cylinder
		gl.glPushMatrix();
		{
			if (use_materials) {
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, TEST, 0);
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, TEST, 4);
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, TEST, 8);
				gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, TEST[12]);
			} else {
				gl.glColor3f(0f, 0f, 1.0f);
			}
			gl.glTranslatef(-w, -h - size / 2, zoom);
			gl.glRotatef(-90, 1, 0, 0);
			glut.glutSolidCylinder(size / 2, size, slices, stacks);
		}
		gl.glPopMatrix();
	}
}
