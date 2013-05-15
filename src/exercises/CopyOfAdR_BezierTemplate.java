package exercises;

/**
 * @author Andreas Elsner / Stephan Arens / Gitta Domik
 * 
 * AdR Bezier Template
 * Department of Computer Science at the University of Paderborn, Germany
 * Research Group of Prof. Gitta Domik - Computer Graphics, Visualization and Digital Image Processing
 */

import hilfsklassen.JoglTemplate;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLException;

import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

@SuppressWarnings("serial")
public class CopyOfAdR_BezierTemplate extends JoglTemplate {
	// bezier curve control points (assignment 1)
	protected final static float CTRL_POINTS[] = { -2.0f, -2.0f, 0.0f, -1.0f,
			2.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, 2.0f, 0.0f };

	// bezier surface control points (assignment 2)
	protected final static float CTRL_POINTS_3D[] = { -1.5f, -1.5f, 4.0f,
			-0.5f, -1.5f, 2.0f, 0.5f, -1.5f, -1.0f, 1.5f, -1.5f, 2.0f, -1.5f,
			-0.5f, 1.0f, -0.5f, -0.5f, 3.0f, 0.5f, -0.5f, 0.0f, 1.5f, -0.5f,
			-1.0f, -1.5f, 0.5f, 4.0f, -0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 3.0f,
			1.5f, 0.5f, 4.0f, -1.5f, 1.5f, -2.0f, -0.5f, 1.5f, -2.0f, 0.5f,
			1.5f, 0.0f, 1.5f, 1.5f, -1.0f };

	// texture coordinates (assignment 2)
	protected final static float TEX_POINTS[] = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f };

	// for loop or glEvalMesh approach
	protected boolean eval_mesh_approach = false;

	private Texture crateTexture;

	/**
	 * The main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CopyOfAdR_BezierTemplate assignment = new CopyOfAdR_BezierTemplate();
		assignment.setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		GL gl = drawable.getGL();
		// z-buffer test
		gl.glEnable(GL.GL_DEPTH_TEST);
		// calculate normals automatically for evaluators
		gl.glEnable(GL.GL_AUTO_NORMAL);
		// use color as material
		//gl.glEnable(GL.GL_COLOR_MATERIAL);
		// use smooth shading
		gl.glShadeModel(GL.GL_SMOOTH);
		// turn light on
		gl.glEnable(GL.GL_LIGHTING);
		// use light source 0
		gl.glEnable(GL.GL_LIGHT0);

		// Loading textures
		gl.glEnable(GL.GL_TEXTURE_2D);
		try {
			File file_crate = new File("assets/textures/crate.png");
			crateTexture = TextureIO.newTexture(file_crate, false);
		} catch (GLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		// set the erasing color (black)
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		// clear screen with the defined erasing color and depth buffer
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		// set a default drawing color
		gl.glColor3f(1f, 1f, 1f);

		// light position, last parameter = 0 directed light, 1 = positional
		// light
		float[] pos = { 0.0f, 10.0f, 3.0f, 1.0f };
		// define light source
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, pos, 0);

		// store modelview matrix
		gl.glPushMatrix();
		applyMouseTranslation(gl);
		applyMouseRotation(gl);

		// copy control points to FloatBuffer
		FloatBuffer tmpCtrlpointsBuf = BufferUtil
				.newFloatBuffer(CTRL_POINTS_3D.length);
		for (int i = 0; i < CTRL_POINTS_3D.length; i++)
			tmpCtrlpointsBuf.put(CTRL_POINTS_3D[i]);
		tmpCtrlpointsBuf.rewind();

		FloatBuffer tmpTexpointsBuf = BufferUtil
				.newFloatBuffer(TEX_POINTS.length);
		for (int i = 0; i < TEX_POINTS.length; i++)
			tmpTexpointsBuf.put(TEX_POINTS[i]);
		tmpTexpointsBuf.rewind();

		// TODO assign data to evaluator.
		// Parameters: GL.GL_MAP1_VERTEX_3 specifies the evaluator type, u1, u2,
		// number of values in each block (should be 3 for vertex3f values),
		// number of control points, FloatBuffer object
		gl.glMap2f(GL.GL_MAP2_VERTEX_3, 0, 1, 3, 4, 0, 1, 12, 4,
				tmpCtrlpointsBuf);
		gl.glMap2f(GL.GL_MAP2_TEXTURE_COORD_2, 0, 1, 2, 2, 0, 1, 4, 2,
				tmpTexpointsBuf);

		// TODO enable evaluator of type GL_MAP1_VERTEX_3
		gl.glEnable(GL.GL_MAP2_VERTEX_3);
		gl.glEnable(GL.GL_MAP2_TEXTURE_COORD_2);

		if (!eval_mesh_approach) {
			// TODO use a for loop and glEvalCoord1f to draw a bezier curve
			gl.glPushMatrix();
			{
				gl.glColor3f(1f, 0f, 0f);
				for (int j = 0; j <= 8; j++) {
					gl.glBegin(GL.GL_LINE_STRIP);
					{
						for (int i = 0; i <= 30; i++)
							gl.glEvalCoord2f((float) i / 30.0f,
									(float) j / 8.0f);
					}
					gl.glEnd();
					gl.glBegin(GL.GL_LINE_STRIP);
					{
						for (int i = 0; i <= 30; i++)
							gl.glEvalCoord2f((float) j / 8.0f,
									(float) i / 30.0f);
					}
					gl.glEnd();
				}
			}
			gl.glPopMatrix();
		} else {
			// TODO use glMapGrid1f and glEvalMesh1 to draw a bezier curve
			gl.glColor3f(0f, 0f, 1f);
			gl.glMapGrid2f(30, 0, 1, 8, 0, 1);
			gl.glEvalMesh2(GL.GL_LINE, 0, 30, 0, 8);
			gl.glColor3f(1f, 0f, 0f);
			gl.glMapGrid2f(30, 0, 1, 8, 0, 1);
			gl.glEvalMesh2(GL.GL_FILL, 0, 30, 0, 8);
		}

		// TODO disable evaluator of type GL_MAP1_VERTEX_3
		gl.glDisable(GL.GL_MAP2_TEXTURE_COORD_2);
		gl.glDisable(GL.GL_MAP2_VERTEX_3);

		// draw control points
		// TODO implement method drawControlPoints(GL gl, float[] ctrlPoints)
		drawControlPoints(gl, CTRL_POINTS_3D);

		// restore modelview matrix
		gl.glPopMatrix();
	}

	protected void drawControlPoints(GL gl, float[] ctrlPoints) {
		// TODO implement method to draw control points
		gl.glPushMatrix();
		{
			gl.glColor3f(1f, 1f, 0f);
			gl.glBegin(GL.GL_POINTS);
			{
				for (int i = 0; i < ctrlPoints.length / 3; i++) {
					gl.glVertex3fv(ctrlPoints, i * 3);
				}
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
	}

	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);

		if (e.getKeyCode() == KeyEvent.VK_E) {
			eval_mesh_approach = !eval_mesh_approach;
		}
	}
}
