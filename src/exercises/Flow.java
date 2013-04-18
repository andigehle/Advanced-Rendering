package exercises;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import hilfsklassen.AdvancedTemplate;

@SuppressWarnings("serial")
public class Flow extends AdvancedTemplate {

	private float[][] flow_data;
	private int[] flow_spatial_dim = new int[3];
	private int[] flow_ext_dim = new int[3];

	private float flow_max = 0;

	/**
	 * The main method
	 */
	public static void main(String[] args) {
		Flow template = new Flow();
		template.setVisible(true);
	}

	// ------------------------------------------------------------

	/**
	 * Constructor
	 */
	public Flow() {
		super();
		setTitle("Visualization of a 2D flow slice");

		setModiFlag(MODI_DEPTH_BUFFERING);
		//setLightFlag(LIGHT_DIRECTIONAL);
		//setLightFlag(LIGHT_SHADING);

		// Parse data
		parseData("data/field2.irreg");
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		GL gl = drawable.getGL();
		gl.glDisable(GL.GL_LIGHTING);
	}

	private void parseData(String path) {
		System.out.println("Parsing data...");

		Scanner scan;
		File file = new File(path);

		try {
			scan = new Scanner(file);

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
			flow_data = new float[6724][7];
			for (int line = 0, value = 0; scan.hasNext(); value++) {
				if (value == 6) {
					//length
					flow_data[line][6] = getLength(flow_data[line]);
					flow_max = Math.max(flow_max, flow_data[line][6]);
					
					//reset
					line++;
					value = 0;
				}
				flow_data[line][value] = Float.parseFloat(scan.next());
				// System.out.println(line+" - "+value);
			}

		} catch (FileNotFoundException fe) {
			System.out.println("FileNotFoundException : " + fe);
		}
	}

	// ------------------------------------------------------------

	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glEnable(GL.GL_COLOR_MATERIAL);

		//drawCoord(gl, 20);
		
		gl.glPushMatrix();
		{
			//center image on 2d base
			gl.glTranslatef(-11f, -11f, 0);
			
			// Maybe use dim for something...border etc. ?
			gl.glScalef(20, 20, 1);
			for (int i = 0; i < flow_data.length; i++) {
				gl.glColor3fv(dataToColor(flow_data[i][6]), 0);
				drawFlow(gl, flow_data[i]);
			}
		}
		gl.glPopMatrix();
		
	}

	private float getLength(float[] coords){
		return (float) Math.sqrt(Math.pow((coords[3]), 2)
				+ Math.pow((coords[4]), 2)
				+ Math.pow((coords[5]), 2));
	}
	
	private void drawFlow(GL gl, float[] coords) {
		gl.glPushMatrix();
		{
			gl.glBegin(GL.GL_LINES);
			{
				gl.glVertex3f(coords[0], coords[1], coords[2]);
				//relative movement
				gl.glVertex3f(coords[0]+coords[3], coords[1]+coords[4], coords[2]+coords[5]);
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
	}

	private float[] dataToColor(float length) {
		float min = 0;
		float max = flow_max;
		float red, green, blue;
		float value = length/max;
		
	    if (0 <= value && value <= 1/8) {
	        red = 0;
	        green = 0;
	        blue = 4*value + .5f; // .5 - 1 // b = 1/2
	    } else if (1/8 < value && value <= 3/8) {
	        red = 0;
	        green = 4*value - .5f; // 0 - 1 // b = - 1/2
	        blue = 0;
	    } else if (3/8 < value && value <= 5/8) {
	        red = 4*value - 1.5f; // 0 - 1 // b = - 3/2
	        green = 1;
	        blue = -4*value + 2.5f; // 1 - 0 // b = 5/2
	    } else if (5/8 < value && value <= 7/8) {
	        red = 1;
	        green = -4*value + 3.5f; // 1 - 0 // b = 7/2
	        blue = 0;
	    } else if (7/8 < value && value <= 1) {
	        red = -4*value + 4.5f; // 1 - .5 // b = 9/2
	        green = 0;
	        blue = 0;
	    } else {    // should never happen - value > 1
	        red = .5f;
	        green = 0;
	        blue = 0;
	    }
		
		return new float[]{red,green,blue};
	}
}
