package hilfsklassen;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import hilfsklassen.JoglTemplate;

@SuppressWarnings("serial")
public class AdvancedTemplate extends JoglTemplate {

	private int camera = 3;
	private boolean set_camera = false;
	private int light_flag = 0000;
	private boolean set_light = false;
	private int modi_flag = 00;
	private boolean set_modi = true;

	/**
	 * The main method
	 */
	public static void main(String[] args) {
		AdvancedTemplate template = new AdvancedTemplate();
		template.setVisible(true);
	}

	// ------------------------------------------------------------

	/**
	 * Constructor
	 */
	public AdvancedTemplate() {
		super();

		setTitle("Set app title");
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		GL gl = drawable.getGL();

		// Information about key binds
		System.out
				.println("Press following keys to enable/disable some optional functions.");
		System.out.println("1 - Orthogonal perspective");
		System.out.println("2 - Frustum perspective");
		System.out.println("3 - GLU perspective");
		System.out.println("4 - Toggle depth buffering");
		System.out.println("5 - Toggle culling");
		System.out.println("6 - Add directional light source");
		System.out.println("7 - Add spot light");
		System.out.println("8 - Add positional light source");
		System.out.println("9 - Toggle flat/smooth shading");

		// Define lights
		gl.glEnable(GL.GL_LIGHTING);

		float[] lt0_position = { 0, 1, 1, 1 };
		float[] lt0_ambient = { 0.3f, 0.3f, 0.3f, 1};
		float[] lt0_diffuse = { 1, 1, 1, 1};
		float[] lt0_specular = { 1, 1, 1, 1};

		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lt0_position, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, lt0_ambient, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, lt0_diffuse, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, lt0_specular, 0);

		float[] lt1_position = { 0, 0, -1, 1 };
		float[] lt1_ambient = { 0.4f, 0.4f, 0.2f, 1};
		float[] lt1_diffuse = { 1, 1, 0.4f, 1};
		float[] lt1_specular = { 1, 1, 0.6f, 1};

		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lt0_position, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lt0_ambient, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lt1_diffuse, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lt1_specular, 0);

		float[] lt2_position = { 0, 0, 3, 1 };
		float[] lt2_ambient = { 0.2f, 0, 0, 1};
		float[] lt2_diffuse = { 0, 1, 0, 1};
		float[] lt2_specular = { 0, 0, 1, 1};

		gl.glLightfv(GL.GL_LIGHT2, GL.GL_POSITION, lt0_position, 0);
		gl.glLightfv(GL.GL_LIGHT2, GL.GL_AMBIENT, lt0_ambient, 0);
		gl.glLightfv(GL.GL_LIGHT2, GL.GL_DIFFUSE, lt2_diffuse, 0);
		gl.glLightfv(GL.GL_LIGHT2, GL.GL_SPECULAR, lt2_specular, 0);
	}

	// ------------------------------------------------------------

	/**
	 * Key event handler
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// glOrtho, glFrustum, gluPerspective
		if (e.getKeyCode() == KeyEvent.VK_1) {
			setCameraFlag(1);
			System.out.println("Switched orthogonal perspective.");
		} else if (e.getKeyCode() == KeyEvent.VK_2) {
			setCameraFlag(2);
			System.out.println("Switched frustum perspective.");
		} else if (e.getKeyCode() == KeyEvent.VK_3) {
			setCameraFlag(3);
			System.out.println("Switched glu perspective.");

			// Buffer switcher
		} else if (e.getKeyCode() == KeyEvent.VK_4) {
			setModiFlag(modi_flag ^= 01);
			System.out.println("Depth buffering switched.");
		} else if (e.getKeyCode() == KeyEvent.VK_5) {
			setModiFlag(modi_flag ^= 10);
			System.out.println("Culling switched.");

			// Lights
		} else if (e.getKeyCode() == KeyEvent.VK_6) {
			setLightFlag(light_flag ^= 0001);
			System.out.println("Directional light switched.");
		} else if (e.getKeyCode() == KeyEvent.VK_7) {
			setLightFlag(light_flag ^= 0010);
			System.out.println("Spot light switched.");
		} else if (e.getKeyCode() == KeyEvent.VK_8) {
			setLightFlag(light_flag ^= 0100);
			System.out.println("Posistional light switched.");
		} else if (e.getKeyCode() == KeyEvent.VK_9) {
			setLightFlag(light_flag ^= 1000);
			System.out.println("Flat/Smooth shading switched.");
		}
	}

	protected void setLightFlag(int flag) {
		light_flag = flag;
		set_light = true;
		System.out.println("LIGHT_FLAG: " + light_flag);
	}

	/**
	 * Set modi flag
	 * 
	 * @param flag
	 */
	protected void setModiFlag(int flag) {
		modi_flag = flag;
		set_modi = true;
		System.out.println("MODI_FLAG: " + modi_flag);
	}

	/**
	 * Set cam flag
	 * 
	 * @param flag
	 */
	protected void setCameraFlag(int cam) {
		camera = (camera == cam) ? 0 : cam;
		set_camera = true;
		System.out.println("CAMERA: " + camera);
	}

	// ------------------------------------------------------------

	/**
	 * Added camera and modus control via keyboard
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		// get the gl object
		GL gl = drawable.getGL();
		// set the erasing color (black)
		gl.glClearColor(0f, 0f, 0f, 0f);
		// clear screen with the defined erasing color and depth buffer
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();
		applyMouseTranslation(gl);
		applyMouseRotation(gl);

		// set a default drawing color (red)
		gl.glColor3f(1f, 0f, 0f);

		// Buffer/Modi changes
		if (set_modi) {
			changeModi(gl);
			set_modi = false;
		}

		// Camera changes
		if (set_camera) {
			changeCamera(gl);
			set_camera = false;
		}

		// Light changes
		if (set_light) {
			changeLight(gl);
			set_light = false;
		}

		// Drawing
		this.displayDraw(drawable);

		gl.glPopMatrix();
	}

	private void changeLight(GL gl) {
		gl.glPushMatrix();
		{
			// TODO: else should be gl.glEnable(GL.GL_COLOR_MATERIAL);

			// Light sources
			if ((light_flag | 1110) == 1111) {
				gl.glEnable(GL.GL_LIGHT0);
			} else {
				gl.glDisable(GL.GL_LIGHT0);
			}

			if ((light_flag | 1101) == 1111) {
				gl.glEnable(GL.GL_LIGHT1);
			} else {
				gl.glDisable(GL.GL_LIGHT1);
			}

			if ((light_flag | 1011) == 1111) {
				gl.glEnable(GL.GL_LIGHT2);
			} else {
				gl.glDisable(GL.GL_LIGHT2);
			}

			// Shading
			if ((light_flag | 0111) == 1111) {
				gl.glShadeModel(GL.GL_SMOOTH);
			} else {
				gl.glShadeModel(GL.GL_FLAT);
			}
		}
		gl.glPopMatrix();

	}

	private void changeModi(GL gl) {
		// Z-buffer
		if ((modi_flag | 10) == 11) {
			gl.glEnable(GL.GL_CULL_FACE);
		} else {
			gl.glDisable(GL.GL_CULL_FACE);
		}
		// Culling
		if ((modi_flag | 01) == 11) {
			gl.glEnable(GL.GL_DEPTH_TEST);
		} else {
			gl.glDisable(GL.GL_DEPTH_TEST);
		}

	}

	private void changeCamera(GL gl) {
		int distance = 40;
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		GLU glu = getGlu();

		// Change to projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		// glOrtho, glFrustum, gluPerspective
		switch (camera) {
		case 1:
			gl.glOrtho(-widthHeightRatio, widthHeightRatio, -2, 2, 1, 100);
			break;
		case 2:
			gl.glFrustum(-widthHeightRatio, widthHeightRatio, -2, 2, 1, 100);
			break;
		case 3:
			glu.gluPerspective(30, widthHeightRatio, 1, 100);
			break;
		}

		glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);

		// Change back to model view matrix.
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// get a gl object
		GL gl = drawable.getGL();
		// set the OpenGL Viewport to the actual width and height of the window
		gl.glViewport(0, 0, width, height);
		// choose your type of projection(ortho or perspective)

		changeCamera(gl);
	}

	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		// Prepare light parameters.
		float SHINE_ALL_DIRECTIONS = 1;
		float[] lightPos = { -30, 0, 0, SHINE_ALL_DIRECTIONS };
		float[] lightColorAmbient = { 0.2f, 0.2f, 0.2f, 1f };
		float[] lightColorSpecular = { 0.8f, 0.8f, 0.8f, 1f };

		// Set light parameters.
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightColorAmbient, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lightColorSpecular, 0);

		// Enable lighting in GL.
		gl.glEnable(GL.GL_LIGHT1);
		gl.glEnable(GL.GL_LIGHTING);

		// Set material properties.
		float[] rgba = { 0.3f, 0.5f, 1f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, rgba, 0);
		gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 0.5f);

		getGlut().glutSolidTeapot(1);
	}

	// ------------------------------------------------------------

	public void drawCoord(GL gl, float range) {
		gl.glPushMatrix();
		{
			float[] plus = { 1, 0, 0 };
			float[] minus = { 0, 0, 1 };
			float size = 0.5f;
			gl.glBegin(GL.GL_LINES);
			{
				// x-Axis
				gl.glColor3f(plus[0], plus[1], plus[2]);
				gl.glVertex2d(range, 0);
				gl.glVertex2d(0, 0);
				// Draw X
				gl.glVertex2d(range + size, size);
				gl.glVertex2d(range + 2 * size, -size);
				gl.glVertex2d(range + size, -size);
				gl.glVertex2d(range + 2 * size, size);
				gl.glColor3f(minus[0], minus[1], minus[2]);
				gl.glVertex2d(-range, 0);
				gl.glVertex2d(0, 0);
				// y-Axis
				gl.glColor3f(plus[0], plus[1], plus[2]);
				gl.glVertex2d(0, range);
				gl.glVertex2d(0, 0);
				// Draw Y
				gl.glVertex2d(0, range + 2 * size);
				gl.glVertex2d(0, range + size);
				gl.glVertex2d(-size, range + 3 * size);
				gl.glVertex2d(0, range + 2 * size);
				gl.glVertex2d(size, range + 3 * size);
				gl.glVertex2d(0, range + 2 * size);
				gl.glColor3f(minus[0], minus[1], minus[2]);
				gl.glVertex2d(0, -range);
				gl.glVertex2d(0, 0);
				// z-Axis
				gl.glColor3f(plus[0], plus[1], plus[2]);
				gl.glVertex3d(0, 0, range);
				gl.glVertex2d(0, 0);
				gl.glColor3f(minus[0], minus[1], minus[2]);
				gl.glVertex3d(0, 0, -range);
				gl.glVertex2d(0, 0);
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
	}
}
