package exercises;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLException;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

@SuppressWarnings("serial")
public class DrawCube extends DrawSquare {
	
	private Texture crateTexture;

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
		//Enable culling & depth buffer
		setModiFlag(MODI_DEPTH_BUFFERING);
		setModiFlag(MODI_CULLING);
		setLightFlag(LIGHT_DIRECTIONAL);
	}

	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		GL gl = drawable.getGL();
		
		//Loading textures
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

	// ------------------------------------------------------------

	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		int size = 4;

		//gl.glEnable(GL.GL_COLOR_MATERIAL);

		// Draw coord-system
		//drawCoord(gl, 10);
		
		//center cube
		gl.glTranslatef(-size/2, -size/2, -size/2);

		// Test drawSquare method
		gl.glPushMatrix();
		{
			float[][] colors = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 },
					{ 1, 1, 0 }, { 1, 0, 1 }, { 0, 1, 1 } };
			float[][] vertices = { { 0, 0, 0 }, { 0, size, 0 },
					{ size, size, 0 }, { size, 0, 0 }, { 0, 0, size },
					{ 0, size, size }, { size, size, size }, { size, 0, size } };
			// Front - Left - Right - Top - Bottom - Back
			int[][] faces = { { 0, 1, 2, 3 }, { 0, 4, 5, 1 }, { 3, 2, 6, 7 },
					{ 1, 5, 6, 2 }, { 0, 3, 7, 4 }, { 4, 7, 6, 5 } };

			crateTexture.enable();
			crateTexture.bind();
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
