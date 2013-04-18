package hilfsklassen;

import java.awt.event.KeyEvent;
import java.util.BitSet;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import hilfsklassen.JoglTemplate;

@SuppressWarnings("serial")
public class AdvancedTemplate extends JoglTemplate {

	private int camera = 3;
	private boolean set_camera = false;
	private BitSet light_flag = new BitSet();
	private boolean set_light = false;
	private BitSet modi_flag = new BitSet();
	private boolean set_modi = true;

	protected final static int MODI_DEPTH_BUFFERING = 0;
	protected final static int MODI_CULLING = 1;

	protected final static int LIGHT_DIRECTIONAL = 0;
	protected final static int LIGHT_SPOT = 1;
	protected final static int LIGHT_POSITIONAL = 2;
	protected final static int LIGHT_SHADING = 3;

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

		/*
		 * Directional light source (w = 0 in position) The light source is at
		 * an infinite distance, all the ray are parallel and have the direction
		 * (x, y, z).
		 */
		float[] lt0_position = { 0, 1, 1, 0 };
		float[] lt0_ambient = { 0.3f, 0.3f, 0.3f, 1 };
		float[] lt0_diffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] lt0_specular = { 1.0f, 1.0f, 1.0f, 1.0f };

		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lt0_position, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, lt0_ambient, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, lt0_diffuse, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, lt0_specular, 0);

		/**
		 * Spotlight This light is a sport. A spot emits lights on a particular
		 * direction. * To create a spot, we have to specify the spot direction
		 * (GL_SPOT_DIRECTION) * and the spot cut-off (GL_SPOT_CUTOFF). * The
		 * spot cut-off is the angle of the cone in which the spot emits light.
		 * The * axis of the cone is the spo direction.
		 */
		float[] lt1_position = { 1,1,1, 1.0f };
		float[] lt1_ambient = { 0.4f, 0.4f, 0.2f, 1.0f };
		float[] lt1_diffuse = { 1, 1, 0.4f, 1 };
		float[] lt1_specular = { 1, 1, 0.6f, 1 };
		float[] spot_direction = { 0, 0, 0f, 1.0f };
		int spot_cutoff = 180;

		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lt1_position, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lt1_ambient, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lt1_diffuse, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lt1_specular, 0);

		gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPOT_DIRECTION, spot_direction, 0);
		// angle of the cone light emitted by the spot : value between 0 to 180
		gl.glLightf(GL.GL_LIGHT1, GL.GL_SPOT_CUTOFF, spot_cutoff);
		gl.glLightf(GL.GL_LIGHT1, GL.GL_SPOT_EXPONENT, 15.0f);

		/*
		 * Positional light source (w = 1) The light source is positioned at (x,
		 * y, z). The ray come from this particular location (x, y, z) and goes
		 * towards all directions.
		 */
		float[] lt2_position = { -2.0f, 2.0f, -5.0f, 1.0f };
		float[] lt2_ambient = { 1.0f, 1.0f, 0.0f, 1.0f };
		float[] lt2_diffuse = { 1.0f, 1.0f, 0.0f, 1.0f };
		float[] lt2_specular = { 0, 0, 1, 1 };

		gl.glLightfv(GL.GL_LIGHT2, GL.GL_POSITION, lt2_position, 0);
		gl.glLightfv(GL.GL_LIGHT2, GL.GL_AMBIENT, lt2_ambient, 0);
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
			System.out.print("Depth buffering ");
			setModiFlag(MODI_DEPTH_BUFFERING);
		} else if (e.getKeyCode() == KeyEvent.VK_5) {
			System.out.print("Culling ");
			setModiFlag(MODI_CULLING);

			// Lights
		} else if (e.getKeyCode() == KeyEvent.VK_6) {
			System.out.print("Directional light ");
			setLightFlag(LIGHT_DIRECTIONAL);
		} else if (e.getKeyCode() == KeyEvent.VK_7) {
			System.out.print("Spot light ");
			setLightFlag(LIGHT_SPOT);
		} else if (e.getKeyCode() == KeyEvent.VK_8) {
			System.out.print("Posistional light ");
			setLightFlag(LIGHT_POSITIONAL);
		} else if (e.getKeyCode() == KeyEvent.VK_9) {
			System.out.print("Flat/Smooth shading ");
			setLightFlag(LIGHT_SHADING);
		}
	}

	protected void setLightFlag(int flag_index) {
		if (light_flag.get(flag_index)) {
			light_flag.clear(flag_index);
			System.out.print("disabled.");
		} else {
			light_flag.set(flag_index);
			System.out.print("enabled.");
		}
		set_light = true;
		System.out.println("");
	}

	/**
	 * Set modi flag
	 * 
	 * @param flag
	 */
	protected void setModiFlag(int flag_index) {
		if (modi_flag.get(flag_index)) {
			modi_flag.clear(flag_index);
			System.out.print("disabled.");
		} else {
			modi_flag.set(flag_index);
			System.out.print("enabled.");
		}
		set_modi = true;
		System.out.println("");
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
			if (light_flag.get(LIGHT_DIRECTIONAL)) {
				gl.glEnable(GL.GL_LIGHT0);
			} else {
				gl.glDisable(GL.GL_LIGHT0);
			}

			if (light_flag.get(LIGHT_SPOT)) {
				gl.glEnable(GL.GL_LIGHT1);
			} else {
				gl.glDisable(GL.GL_LIGHT1);
			}

			if (light_flag.get(LIGHT_POSITIONAL)) {
				gl.glEnable(GL.GL_LIGHT2);
			} else {
				gl.glDisable(GL.GL_LIGHT2);
			}

			// Shading
			if (light_flag.get(LIGHT_SHADING)) {
				gl.glShadeModel(GL.GL_SMOOTH);
			} else {
				gl.glShadeModel(GL.GL_FLAT);
			}
		}
		gl.glPopMatrix();

	}

	private void changeModi(GL gl) {
		// Depth-Buffer
		if (modi_flag.get(MODI_DEPTH_BUFFERING)) {
			gl.glEnable(GL.GL_DEPTH_TEST);
		} else {
			gl.glDisable(GL.GL_DEPTH_TEST);
		}

		// Culling
		if (modi_flag.get(MODI_CULLING)) {
			gl.glEnable(GL.GL_CULL_FACE);
		} else {
			gl.glDisable(GL.GL_CULL_FACE);
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

		// Set material properties.
		float[] rgba = { 0.3f, 0.5f, 1f, 1 };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, rgba, 0);
		gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 0.5f);

		getGlut().glutSolidTeapot(3);
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
