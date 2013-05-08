package exercises;

import hilfsklassen.JoglTemplate;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class VolumeTemplate extends JoglTemplate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String VOLUME_FILE = "assets/heart.raw";

	private final int XDIM = 64;

	private final int YDIM = 64;

	private final int ZDIM = 64;

	private int[] textureNames = new int[1];

	public static void main(String[] args) {
		VolumeTemplate assignment = new VolumeTemplate();
		assignment.setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		GL gl = drawable.getGL();

		// enable depth testing
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LESS);

		// Texture-loading occurs only once in a program, that's why it's in the
		// init-method.
		gl.glEnable(GL.GL_TEXTURE_3D);
		loadSingleRaw3DTexture(gl, VOLUME_FILE);
		gl.glDisable(GL.GL_TEXTURE_3D);

		// this blending-function is just a suggestion. Others are possible,
		// too.
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	}

	private float x_position = 0.5f;
	private float y_position = 0.5f;

	@Override
	public void display(GLAutoDrawable drawable) {
		// get the gl object
		GL gl = drawable.getGL();
		// set the erasing color (black)
		gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		// clear screen with the defined erasing color and depth buffer
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();

		gl.glTranslatef(-0.5f, -0.5f, 7);

		// apply mouse translation
		applyMouseTranslation(gl);

		// rotate around center of cube
		gl.glTranslatef(0.5f, 0.5f, 0.5f);
		applyMouseRotation(gl);
		gl.glTranslatef(-0.5f, -0.5f, -0.5f);

		gl.glColor3f(0f, 1f, 0f);
		drawCubeGrid(gl);

		gl.glColor3f(1, 1, 1);

		// DRAW HERE
		// Exercise a)
		/*
		 * gl.glBegin(GL.GL_TRIANGLE_FAN); { gl.glNormal3f(1, 1, 0);
		 * 
		 * gl.glTexCoord3f(0, 0, 0); gl.glVertex3f(0,0,0); gl.glTexCoord3f(0, 1,
		 * 1); gl.glVertex3f(0, 1, 1); gl.glTexCoord3f(1,0.2f,1);
		 * gl.glVertex3f(1,0.2f,1); } gl.glEnd();
		 */

		gl.glEnable(GL.GL_TEXTURE_3D);
		gl.glBegin(GL.GL_QUADS);
		{
			// Y-Plane
			gl.glTexCoord3f(0, 0, y_position);
			gl.glVertex3f(0, 0,y_position);
			gl.glTexCoord3f(1, 0, y_position);
			gl.glVertex3f(1, 0, y_position);
			gl.glTexCoord3f(1, 1, y_position);
			gl.glVertex3f(1, 1, y_position);
			gl.glTexCoord3f(0, 1, y_position);
			gl.glVertex3f(0, 1,y_position);

			// X-Plane
			gl.glTexCoord3f(x_position, 0, 0);
			gl.glVertex3f(x_position, 0, 0);
			gl.glTexCoord3f(x_position, 0, 1);
			gl.glVertex3f(x_position, 0, 1);
			gl.glTexCoord3f(x_position, 1, 1);
			gl.glVertex3f(x_position, 1, 1);
			gl.glTexCoord3f(x_position, 1, 0);
			gl.glVertex3f(x_position, 1, 0);
		}
		gl.glEnd();
		gl.glDisable(GL.GL_TEXTURE_3D);

		// 2)
		gl.glPushMatrix();
		{
			gl.glEnable(GL.GL_BLEND);
			gl.glTranslatef(1.5f, 0, 0);

			gl.glColor3f(0f, 1f, 0f);
			drawCubeGrid(gl);
			
			gl.glColor3f(1, 1, 1);

			// 32 quads
			gl.glEnable(GL.GL_TEXTURE_3D);
			gl.glBegin(GL.GL_QUADS);
			{
				for (float i = 0; i < 32; i++) {
					float z = i/32;
					// Parallel to XY-Plane
					gl.glTexCoord3f(0, 0, z);
					gl.glVertex3f(0, 0, z);
					gl.glTexCoord3f(1, 0, z);
					gl.glVertex3f(1, 0, z);
					gl.glTexCoord3f(1, 1, z);
					gl.glVertex3f(1, 1, z);
					gl.glTexCoord3f(0, 1, z);
					gl.glVertex3f(0, 1, z);
				}
			}
			gl.glEnd();
			gl.glDisable(GL.GL_TEXTURE_3D);
			gl.glDisable(GL.GL_BLEND);			
		}
		gl.glPopMatrix();

		gl.glPopMatrix();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);

		float steps = 0.1f;

		if (e.getKeyCode() == KeyEvent.VK_W) {
			y_position += steps;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			y_position -= steps;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			x_position -= steps;
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			x_position += steps;
		}
	}

	private void drawCubeGrid(GL gl) {
		// front "face"
		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(0, 0, 0);
			gl.glVertex3f(1, 0, 0);
		}
		gl.glEnd();

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(1, 0, 0);
			gl.glVertex3f(1, 1, 0);
		}
		gl.glEnd();

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(1, 1, 0);
			gl.glVertex3f(0, 1, 0);
		}
		gl.glEnd();

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(0, 1, 0);
			gl.glVertex3f(0, 0, 0);
		}
		gl.glEnd();

		// back "face"

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(0, 0, 1);
			gl.glVertex3f(1, 0, 1);
		}
		gl.glEnd();

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(1, 0, 1);
			gl.glVertex3f(1, 1, 1);
		}
		gl.glEnd();

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(1, 1, 1);
			gl.glVertex3f(0, 1, 1);
		}
		gl.glEnd();

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(0, 1, 1);
			gl.glVertex3f(0, 0, 1);
		}
		gl.glEnd();

		// "sides"

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(0, 0, 0);
			gl.glVertex3f(0, 0, 1);
		}
		gl.glEnd();

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(1, 0, 0);
			gl.glVertex3f(1, 0, 1);
		}
		gl.glEnd();

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(1, 1, 0);
			gl.glVertex3f(1, 1, 1);
		}
		gl.glEnd();

		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex3f(0, 1, 0);
			gl.glVertex3f(0, 1, 1);
		}
		gl.glEnd();
	}

	/**
	 * Loads data and creates 3D Texture
	 * 
	 * @param gl
	 * @param fname
	 */
	private void loadSingleRaw3DTexture(GL gl, String fname) {
		File f = new File(fname);
		try {
			FileInputStream fis = new FileInputStream(f);
			ByteBuffer scalarData = ByteBuffer.allocate(XDIM * YDIM * ZDIM);
			if (fis.available() != XDIM * YDIM * ZDIM) {
				System.out.println("file size doesn't match dimensions");
			} else {
				fis.read(scalarData.array());
				fis.close();

				// generate texture name
				gl.glGenTextures(1, textureNames, 0);

				ByteBuffer currentBuffer = ByteBuffer.allocate(XDIM * YDIM
						* ZDIM * 4);
				for (int z = 0; z < ZDIM; z++) {
					for (int y = 0; y < YDIM; y++) {
						for (int x = 0; x < XDIM; x++) {
							byte b = scalarData.get(x + (XDIM * y)
									+ (z * XDIM * YDIM));
							currentBuffer.put(b); // R
							currentBuffer.put(b); // G
							currentBuffer.put(b); // B
							currentBuffer.put(b); // A
						}
					}
				}
				currentBuffer.rewind();
				gl.glBindTexture(GL.GL_TEXTURE_3D, textureNames[0]);
				gl.glTexParameteri(GL.GL_TEXTURE_3D, GL.GL_TEXTURE_WRAP_S,
						GL.GL_CLAMP_TO_BORDER);
				gl.glTexParameteri(GL.GL_TEXTURE_3D, GL.GL_TEXTURE_WRAP_T,
						GL.GL_CLAMP_TO_BORDER);
				gl.glTexParameteri(GL.GL_TEXTURE_3D, GL.GL_TEXTURE_WRAP_R,
						GL.GL_CLAMP_TO_BORDER);
				gl.glTexParameteri(GL.GL_TEXTURE_3D, GL.GL_TEXTURE_MAG_FILTER,
						GL.GL_LINEAR);
				gl.glTexParameteri(GL.GL_TEXTURE_3D, GL.GL_TEXTURE_MIN_FILTER,
						GL.GL_LINEAR);
				gl.glTexImage3D(GL.GL_TEXTURE_3D, 0, GL.GL_RGBA, XDIM, YDIM,
						ZDIM, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, currentBuffer);

			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("The file " + fname + " does not exist!");
		} catch (IOException ioe) {
			System.out.println("IOException! Reason: " + ioe.getCause());
		}
	}
}
