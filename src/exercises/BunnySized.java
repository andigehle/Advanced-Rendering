package exercises;

import OBJLoader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.cg.CgGL;

import hilfsklassen.AdR_ShadingTemplate;
import hilfsklassen.MeshLoader;

public class BunnySized extends AdR_ShadingTemplate{

	private long startAnimation = 0;

	public static void main(String[] args)
	{
		BunnySized assignment = new BunnySized();
		assignment.setVisible(true);
	}
	
	/**
	 * Initialization
	 * 
	 * Load object-files
	 */
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		
		//Set time for animations
		startAnimation = System.currentTimeMillis();

		//Load objects
		OBJLoader objLoader;
		objLoader = new OBJLoader("assets/objects/train", 1.0f,
				drawable.getGL());
		objectList[OBJECT_TRAIN] = objLoader.getDisplayList();
		
		objLoader = new OBJLoader("assets/objects/waggon1_michi", 1.0f,
				drawable.getGL());
		objectList[OBJECT_WAGGON1] = objLoader.getDisplayList();
		
		objLoader = new OBJLoader("assets/objects/waggon2_andi", 1.0f,
				drawable.getGL());
		objectList[OBJECT_WAGGON2] = objLoader.getDisplayList();
	}

	
	public void display(GLAutoDrawable drawable)
	{
		// get the gl object
		GL gl = drawable.getGL();
		// set the erasing color (black)
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		// clear screen with the defined erasing color and depth buffer
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();
		applyMouseTranslation(gl);
		applyMouseRotation(gl);

		// set material for cg
		float[] material = MATERIALS[currentMaterial];
		CgGL.cgSetParameter3fv(cgKa, material, 0);
		CgGL.cgSetParameter3fv(cgKd, material, 4);
		CgGL.cgSetParameter3fv(cgKs, material, 8);
		CgGL.cgSetParameter1fv(cgShininess, material, 12);

		// eyePosition for cg
		float[] eyePosition = new float[] { -getView_transx(), -getView_transy(),
				-getView_transz(), 1f };
		CgGL.cgGLSetParameter3fv(cgEyePosition, eyePosition, 0);

		// set light properties
		CgGL.cgGLSetParameter3fv(cgIa, MOVING_LIGHT_ADS, 0);
		CgGL.cgGLSetParameter3fv(cgId, MOVING_LIGHT_ADS, 4);
		CgGL.cgGLSetParameter3fv(cgIs, MOVING_LIGHT_ADS, 8);

		// calculate light position
		float dLightHeight = 5.0f;
		double dLightRadius = 5.0d;
		float[] lightPos = new float[] {
				(float) (dLightRadius * Math.cos(getDelay() * 3.14 / 200.0)),
				(float) (dLightRadius * Math.sin(getDelay() * 3.14 / 200.0)),
				dLightHeight, 1.0f };
		CgGL.cgGLSetParameter3fv(cgLightPosition, lightPos, 0);

		// draw light as sphere (without shader)
		gl.glPushMatrix();
		gl.glTranslatef(lightPos[0], lightPos[1], lightPos[2]);
		gl.glColor3f(1f, 1f, 1f);
		getGlu().gluSphere(getGlu().gluNewQuadric(), 0.3, 10, 10);
		gl.glPopMatrix();

		// move vertices along normals (see vertex program)
		float stretchFactor = 0;
		if (stretch)
		{
			// TODO: Assignment 3_1: use Math.sin and getFrameCounter here to
			// calculate stretchFactor
		}
		// TODO: Assignment 3_1: set cgStretch parameter

		// set modelview matrix for cg
		CgGL.cgGLSetStateMatrixParameter(cgModelViewProj,
				CgGL.CG_GL_MODELVIEW_PROJECTION_MATRIX, CgGL.CG_GL_MATRIX_IDENTITY);
		CgGL.cgGLSetStateMatrixParameter(cgModelViewInv,
				CgGL.CG_GL_MODELVIEW_MATRIX, CgGL.CG_GL_MATRIX_INVERSE);

		// enable profiles, bind shaders
		CgGL.cgGLEnableProfile(getCgVertexProfile());
		CgGL.cgGLBindProgram(cgVertexProg);
		CgGL.cgGLEnableProfile(getCgFragProfile());
		CgGL.cgGLBindProgram(cgFragmentProg);
		// draw mesh
		gl.glCallList(dList);
		// disable profiles, unload shaders
		CgGL.cgGLDisableProfile(getCgVertexProfile());
		CgGL.cgGLDisableProfile(getCgFragProfile());

		// TODO: Assignment 3_3: draw comic outlines here

		gl.glPopMatrix();
	}

}
