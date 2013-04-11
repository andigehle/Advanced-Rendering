package sheet_1;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.GLUT;

import hilfsklassen.AdvancedTemplate;

public class FourGraphics extends AdvancedTemplate {
	
	/**
	 * The main method
	 */
	public static void main(String[] args)
	{
		FourGraphics template = new FourGraphics();
		template.setVisible(true);
	}
	
	//------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public FourGraphics(){
		super();
	}
	
	//------------------------------------------------------------
	
	public void displayDraw(GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		GLUT glut = getGlut();
		int w = 7, h = 7, zoom = -40, size = 5;
		int slices = 20, stacks = 20;

		//Draw cube
		gl.glPushMatrix();
		//gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glTranslatef( w, -h, zoom);
		glut.glutSolidCube(size);
		gl.glPopMatrix();
		
		//Draw sphere
		gl.glPushMatrix();
		//gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glTranslatef( -w, h, zoom);
		glut.glutSolidSphere(size/2, slices, stacks);
		gl.glPopMatrix();
		
		//Draw torus
		gl.glPushMatrix();
		//gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glTranslatef( -w, -h, zoom);
		glut.glutSolidTorus(size/2 - 1.25, size/2, slices, stacks);
		gl.glPopMatrix();
		
		//Draw cylinder
		gl.glPushMatrix();
		//gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glTranslatef( w, h- size/2, zoom);
		gl.glRotatef(-90, 1, 0, 0);
		glut.glutSolidCylinder(size/2, size, slices, stacks);
		gl.glPopMatrix();
	}
}
