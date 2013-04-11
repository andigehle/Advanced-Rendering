package hilfsklassen;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.GLUT;

import hilfsklassen.JoglTemplate;

public class AdvancedTemplate extends JoglTemplate {
	
	/**
	 * The main method
	 */
	public static void main(String[] args)
	{
		AdvancedTemplate template = new AdvancedTemplate();
		template.setVisible(true);
	}
	
	//------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public AdvancedTemplate(){
		super();
	}
	
	//------------------------------------------------------------
	
    protected void set_Camera(GL gl, float distance) {
    	GLU glu = getGlu();
        // Change to projection matrix.
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = (float) getWidth() / (float) getHeight();
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
	
	public void displayDraw(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = getGlut();
		
	      // Prepare light parameters.
        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = {-30, 0, 0, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};

        // Set light parameters.
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lightColorSpecular, 0);

        // Enable lighting in GL.
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHTING);

        // Set material properties.
        float[] rgba = {0.3f, 0.5f, 1f};
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 0.5f);
        
        getGlut().glutSolidTeapot(1);
	}
	
	//------------------------------------------------------------
	
	public void drawCoord(GL gl, float range){
		gl.glPushMatrix();
		{
			float[] plus = {1,0,0};
			float[] minus = {0,0,1};
			float size = 0.5f;
			gl.glBegin(gl.GL_LINES);
			{
				// x-Axis
				gl.glColor3f(plus[0],plus[1],plus[2]);
				gl.glVertex2d(range, 0);
				gl.glVertex2d(0, 0);
				//Draw X
				gl.glVertex2d(range+size, size);
				gl.glVertex2d(range+2*size, -size);
				gl.glVertex2d(range+size, -size);
				gl.glVertex2d(range+2*size, size);
				gl.glColor3f(minus[0],minus[1],minus[2]);
				gl.glVertex2d(-range, 0);
				gl.glVertex2d(0, 0);
				// y-Axis
				gl.glColor3f(plus[0],plus[1],plus[2]);
				gl.glVertex2d(0, range);
				gl.glVertex2d(0, 0);
				//Draw Y
				gl.glVertex2d(0, range+2*size);
				gl.glVertex2d(0, range+size);
				gl.glVertex2d(-size, range+3*size);
				gl.glVertex2d(0, range+2*size);
				gl.glVertex2d(size, range+3*size);
				gl.glVertex2d(0, range+2*size);
				gl.glColor3f(minus[0],minus[1],minus[2]);
				gl.glVertex2d(0, -range);
				gl.glVertex2d(0, 0);
				//z-Axis
				gl.glColor3f(plus[0],plus[1],plus[2]);
				gl.glVertex3d(0, 0, range);
				gl.glVertex2d(0, 0);
				gl.glColor3f(minus[0],minus[1],minus[2]);
				gl.glVertex3d(0, 0, -range);
				gl.glVertex2d(0, 0);
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
	}
}
