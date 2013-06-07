import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import rungenPlot.rungexy;

/**
 * Vis. assignment 1 19.03.2013
 * 
 * @author Andreas Gehle, 6603927
 * 
 *         Visualization of a flow data set
 */
@SuppressWarnings("serial")
public class FlowVisualization extends JoglTemplate {

	private LinkedList<Flow> flow_data;
	private int[] flow_spatial_dim = new int[3];
	private int[] flow_ext_dim = new int[3];

	private float flow_min = -1;
	private float flow_max = 0;

	private rungexy slv = new rungexy();
	private rungexy slv1 = new rungexy();

	/**
	 * The main method
	 */
	public static void main(String[] args) {
		FlowVisualization template = new FlowVisualization();
		template.setVisible(true);
	}

	// ------------------------------------------------------------

	/**
	 * Constructor
	 */
	public FlowVisualization() {
		super();
		setTitle("Flow visualization with Runge Kutta");

		flow_data = new LinkedList<Flow>();

		// Parse data
		parseData("data/field2.irreg");

		// Zoom out to full view
		setView_transz(-45);

		new RungeKutta();
	}

	private void parseData(String path) {
		System.out.println("Parsing data...");

		Scanner scan;
		File file = new File(path);

		try {
			scan = new Scanner(file);

			// TODO: Question about dimension

			// First dim
			flow_spatial_dim[0] = scan.nextInt();
			// Second dim
			flow_spatial_dim[1] = scan.nextInt();
			// Extension of each dim
			flow_ext_dim[0] = scan.nextInt();
			flow_ext_dim[1] = scan.nextInt();
			flow_ext_dim[2] = scan.nextInt();
			// Third dim
			flow_spatial_dim[2] = scan.nextInt();

			// Data array
			while (scan.hasNext()) {
				float x = Float.parseFloat(scan.next());
				float y = Float.parseFloat(scan.next());
				float z = Float.parseFloat(scan.next());
				float direction_x = Float.parseFloat(scan.next());
				float direction_y = Float.parseFloat(scan.next());
				float direction_z = Float.parseFloat(scan.next());

				Flow flow = new Flow(new Point(x, y, z), new Vector(new Point(
						direction_x, direction_y, direction_z)));

				// Set max
				flow_max = Math.max(flow_max, flow.to.length);

				// Set min
				if (flow_min < 0 || flow_min > flow.to.length) {
					flow_min = flow.to.length;
				}

				flow_data.add(flow);

				// System.out.println(flow);
			}

		} catch (FileNotFoundException fe) {
			System.out.println("FileNotFoundException : " + fe);
		}
	}

	// ------------------------------------------------------------

	public void display(GLAutoDrawable drawable) {
		// get the gl object
		GL gl = drawable.getGL();
		// set the erasing color (black)
		gl.glClearColor(0f, 0f, 0f, 0f);
		// clear screen with the defined erasing color and depth buffer
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		{
			applyMouseTranslation(gl);
			applyMouseRotation(gl);

			// now do your drawing here!
			gl.glPushMatrix();
			{
				// center image on 2d base
				gl.glTranslatef(-11f, -11f, 0);

				// Maybe use dim for something...border etc. ?
				gl.glScalef(20, 20, 1);
				for (int i = 0; i < flow_data.size(); i++) {
					Flow current = flow_data.get(i);
					// Flow next = flow_data.get(i+1);

					gl.glColor3fv(dataToColor(current.to.length), 0);
					rungeKuttaLines(gl, sedes);
					//eulerIntegrationLines(gl, current);
				}

			}
			gl.glPopMatrix();
		}
		gl.glPopMatrix();
	}

	private void drawFlowArrow(GL gl, Flow flow) {
		float stroke_length = 0.02f;
		float arrow_length = 0.002f;

		float[] normal = new float[] { -flow.to.direction.y,
				flow.to.direction.x, flow.to.direction.z };

		gl.glPushMatrix();
		{
			gl.glBegin(GL.GL_TRIANGLE_FAN);
			{
				gl.glVertex3f(flow.from.x + arrow_length * normal[0],
						flow.from.y + arrow_length * normal[1], flow.from.z
								+ arrow_length * normal[2]);
				gl.glVertex3f(flow.from.x - arrow_length * normal[0],
						flow.from.y - arrow_length * normal[1], flow.from.z
								- arrow_length * normal[2]);
				// relative movement
				gl.glVertex3f(
						flow.from.x + stroke_length * flow.to.direction.x,
						flow.from.y + stroke_length * flow.to.direction.y,
						flow.from.z + stroke_length * flow.to.direction.z);
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
	}

	private void drawFlowArrow3D(GL gl, float[] coords) {
		float stroke_length = 0.02f;
		float arrow_length = 0.002f;
		float arrow_depth = 1f;

		float[] normal = new float[] { -coords[4], coords[3], 0 };

		gl.glPushMatrix();
		{
			gl.glBegin(GL.GL_TRIANGLES);
			{
				// Back1
				gl.glVertex3f(coords[0] + arrow_length * normal[0], coords[1]
						+ arrow_length * normal[1], coords[2] + arrow_length
						* normal[2]);
				gl.glVertex3f(coords[0], coords[1], arrow_depth * coords[6]);
				gl.glVertex3f(coords[0] - arrow_length * normal[0], coords[1]
						- arrow_length * normal[1], coords[2] - arrow_length
						* normal[2]);

				// Back2
				gl.glVertex3f(coords[0] + arrow_length * normal[0], coords[1]
						+ arrow_length * normal[1], coords[2] + arrow_length
						* normal[2]);
				gl.glVertex3f(coords[0], coords[1], -arrow_depth * coords[6]);
				gl.glVertex3f(coords[0] - arrow_length * normal[0], coords[1]
						- arrow_length * normal[1], coords[2] - arrow_length
						* normal[2]);

				// Front11
				gl.glVertex3f(coords[0] + arrow_length * normal[0], coords[1]
						+ arrow_length * normal[1], coords[2] + arrow_length
						* normal[2]);
				gl.glVertex3f(coords[0] + stroke_length * coords[3], coords[1]
						+ stroke_length * coords[4], coords[2] + stroke_length
						* coords[5]);
				gl.glVertex3f(coords[0], coords[1], arrow_depth * coords[6]);

				// Front12
				gl.glVertex3f(coords[0] - arrow_length * normal[0], coords[1]
						- arrow_length * normal[1], coords[2] - arrow_length
						* normal[2]);
				gl.glVertex3f(coords[0] + stroke_length * coords[3], coords[1]
						+ stroke_length * coords[4], coords[2] + stroke_length
						* coords[5]);
				gl.glVertex3f(coords[0], coords[1], arrow_depth * coords[6]);

				// Front21
				gl.glVertex3f(coords[0] + arrow_length * normal[0], coords[1]
						+ arrow_length * normal[1], coords[2] + arrow_length
						* normal[2]);
				gl.glVertex3f(coords[0] + stroke_length * coords[3], coords[1]
						+ stroke_length * coords[4], coords[2] + stroke_length
						* coords[5]);
				gl.glVertex3f(coords[0], coords[1], -arrow_depth * coords[6]);

				// Front22
				gl.glVertex3f(coords[0] - arrow_length * normal[0], coords[1]
						- arrow_length * normal[1], coords[2] - arrow_length
						* normal[2]);
				gl.glVertex3f(coords[0] + stroke_length * coords[3], coords[1]
						+ stroke_length * coords[4], coords[2] + stroke_length
						* coords[5]);
				gl.glVertex3f(coords[0], coords[1], -arrow_depth * coords[6]);
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
	}

	private float[] dataToColor(float length) {
		float range = flow_max - flow_min;
		float value = length / range;

		float h = (1 - value);
		float s = 1;
		float b = 0.5f;

		Color hsb = Color.getHSBColor(h, s, b);

		return new float[] { hsb.getRed(), hsb.getGreen(), hsb.getBlue() };
	}

	private void eulerIntegrationLines(GL gl, Flow current) {
		int n = 30;
		float dt = .001f;

		float c_x = current.from.x;
		float c_y = current.from.y;
		float f_x = current.to.direction.x;
		float f_y = current.to.direction.y;

		// Draw lines
		gl.glPushMatrix();
		{
			gl.glBegin(GL.GL_LINES);
			{
				gl.glVertex2f(c_x, c_y);
				for (int i = 0; i < n; i++) {
					// Next step
					c_x = c_x + f_x * dt;
					c_y = c_y + f_y * dt;
					f_x = -c_y;
					f_y = dt * c_x;

					gl.glVertex2f(c_x, c_y);
					gl.glVertex2f(c_x, c_y);
				}
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
	}

	private void eulerIntegrationArrows(GL gl, Flow current) {
		int n = 10;
		float dt = .001f;

		float c_x = current.from.x;
		float c_y = current.from.y;
		float f_x = current.to.direction.x;
		float f_y = current.to.direction.y;

		drawFlowArrow(gl, current);
		for (int i = 0; i < n; i++) {
			// Next step
			c_x = c_x + f_x * dt;
			c_y = c_y + f_y * dt;
			f_x = -c_y;
			f_y = dt * c_x;

			drawFlowArrow(gl, new Flow(new Point(c_x, c_y, 0), new Vector(
					new Point(f_x, f_y, 0))));
		}
	}

	public void rungeKuttaLines(GL gl, Flow[] seeds) {
		slv.rkn(x0, y0, h, N, boundmin, boundmax);
		
		slv
	}

	/*
	 * RungeKutta.java by Richard J. Davies from `Introductory Java for
	 * Scientists and Engineers' chapter: `Numerical Computation' section:
	 * `Runge-Kutta Methods'
	 * 
	 * This problem uses Euclid's method and the fourth order Runge-Kutta method
	 * to compute y at x=1 for the D.E. dy/dx = x * sqrt(1 + y*y) with initial
	 * value y=0 at x=0.
	 */
	public void rungeKutta() {
		// The number of steps to use in the interval
		int STEPS = 100;

		// `h' is the size of each step.
		double h = 1.0 / STEPS;
		double k1, k2, k3, k4;
		double x, y;
		int i;

		// Computation by Euclid's method
		// Initialize y
		y = 0;

		for (i = 0; i < STEPS; i++) {
			// Step through, updating x and incrementing y
			x = i * h;

			y += h * deriv(x, y);
		}

		// Print out the result that we get.
		System.out.println("Using the Euler method " + "The value at x=1 is:");
		System.out.println(y);

		// Computation by 4th order Runge-Kutta
		// Initialize y
		y = 0;

		for (i = 0; i < STEPS; i++) {
			// Step through, updating x
			x = i * h;

			// Computing all of the trial values
			k1 = h * deriv(x, y);
			k2 = h * deriv(x + h / 2, y + k1 / 2);
			k3 = h * deriv(x + h / 2, y + k2 / 2);
			k4 = h * deriv(x + h, y + k3);

			// Incrementing y
			y += k1 / 6 + k2 / 3 + k3 / 3 + k4 / 6;
		}

		// Print out the result that we get.
		System.out.println();
		System.out.println("Using 4th order Runge-Kutta "
				+ "The value at x=1 is:");
		System.out.println(y);

		// Computation by closed form solution
		// Print out the result that we get.
		System.out.println();
		System.out.println("The value really is:");
		y = (Math.exp(0.5) - Math.exp(-0.5)) / 2;
		System.out.println(y);
	}

	// The derivative dy/dx at a given value of x and y.
	public static double deriv(double x, double y) {
		return x * Math.sqrt(1 + y * y);
	}
}

class rungexy extends runge {
	String Title = "Lösung der DGL y'= x^2 + y^2";

	public double f(double x, double y) {
		return x * x + y * y;
	}
}

class Flow {

	public Point from;
	public Vector to;

	public Flow(Point from, Vector to) {
		this.from = from;
		this.to = to;
	}

	public String toString() {
		return "Flow from " + from + " to " + to;
	}
}

class Point {

	public float x, y, z;

	public Point(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String toString() {
		return "Point(" + x + ", " + y + ", " + z + ")";
	}
}

class Vector {

	public Point direction;
	public float length;

	public Vector(Point direction) {
		length = (float) Math.sqrt(Math.pow((direction.x), 2)
				+ Math.pow((direction.y), 2) + Math.pow((direction.z), 2));
		this.direction = normalize(direction);
	}

	private Point normalize(Point direction) {
		return new Point(direction.x / length, direction.y / length,
				direction.z / length);
	}

	public String toString() {
		return "Vector(" + direction.x + ", " + direction.y + ", "
				+ direction.z + ")";
	}

}
