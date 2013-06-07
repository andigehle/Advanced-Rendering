package exercises;

/**
 * @author Andreas Elsner / Stephan Arens / Gitta Domik
 * 
 * AdR Shading Template
 * Department of Computer Science at the University of Paderborn, Germany
 * Research Group of Prof. Gitta Domik - Computer Graphics, Visualization and Digital Image Processing
 */

import hilfsklassen.JoglTemplate;
import hilfsklassen.MeshLoader;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLException;

import com.sun.opengl.cg.CGcontext;
import com.sun.opengl.cg.CGparameter;
import com.sun.opengl.cg.CGprogram;
import com.sun.opengl.cg.CgGL;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

@SuppressWarnings("serial")
public class Bunny_Textures extends JoglTemplate
{
	// TODO: Assignment 3_3: create your own toon shader and load it here
	protected static final String FRAGMENT_SHADER = "src/hilfsklassen/shader/fp_pahl_phongPerPixel.cg";

	protected static final String VERTEX_SHADER = "src/hilfsklassen/shader/vp_phongPerPixel.cg";

	static final float[] BRASS_MATERIAL = { 0.33f, 0.22f, 0.03f, 1.0f, 0.78f,
			0.57f, 0.11f, 1.0f, 0.99f, 0.91f, 0.81f, 1.0f, 5.0f };

	static final float[] REDPLASTIC_MATERIAL = { 0.3f, 0.0f, 0.0f, 1.0f, 0.6f,
			0.0f, 0.0f, 1.0f, 0.8f, 0.4f, 0.4f, 1.0f, 10.0f };

	static final float[] WHITESHINEY_MATERIAL = { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 100.0f };

	static final float[][] MATERIALS = { BRASS_MATERIAL, REDPLASTIC_MATERIAL,
			WHITESHINEY_MATERIAL };

	static final float[] MOVING_LIGHT_ADS = { 0.2f, 0.2f, 0.2f, 1f, 0.95f, 0.95f,
			0.95f, 1f, 0.95f, 0.95f, 0.95f, 1f };

	private CGcontext cgContext;

	private CGprogram cgVertexProg = null, cgFragmentProg = null;

	private CGparameter cgBlackHolePosition, cgTime, cgStretchFactor, cgTexture;

	private int cgVertexProfile, cgFragProfile;

	protected int dList;

	private int frameCounter = 0;

	private int currentMaterial = 0;

	private boolean stretch = true, animation = false;
	
	private Texture crateTexture;

	public static void main(String[] args)
	{
		Bunny_Textures assignment = new Bunny_Textures();
		assignment.setVisible(true);
	}

	public void init(GLAutoDrawable drawable)
	{
		super.init(drawable);
		GL gl = drawable.getGL();
		// init Cg
		initCg();
		// load and compile shader
		cgVertexProg = loadShader(getCgVertexProfile(), VERTEX_SHADER);
		cgFragmentProg = loadShader(getCgFragProfile(), FRAGMENT_SHADER);
		// bind CGParameters to vertex and fragment program
		bindParameters();
		// z-buffer test
		gl.glEnable(GL.GL_DEPTH_TEST);
		// backface culling
		gl.glEnable(GL.GL_CULL_FACE);
		// load mesh
		dList = MeshLoader.loadObj(gl, "assets/objects/bunny.obj", 0.5f);
		
		//Loading textures
		gl.glEnable(GL.GL_TEXTURE_2D);
		try {
			File file_crate = new File("assets/textures/crate.png");
			crateTexture = TextureIO.newTexture(file_crate, true);
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initCg()
	{
		cgContext = CgGL.cgCreateContext();
		cgVertexProfile = CgGL.cgGLGetLatestProfile(CgGL.CG_GL_VERTEX);
		if (cgVertexProfile == CgGL.CG_PROFILE_UNKNOWN)
		{
			System.err.println("Invalid vertex profile");
			System.exit(1);
		}
		CgGL.cgGLSetOptimalOptions(cgVertexProfile);

		cgFragProfile = CgGL.cgGLGetLatestProfile(CgGL.CG_GL_FRAGMENT);
		if (cgFragProfile == CgGL.CG_PROFILE_UNKNOWN)
		{
			System.err.println("Invalid fragment profile");
			System.exit(1);
		}
		CgGL.cgGLSetOptimalOptions(cgFragProfile);
	}

	public CGprogram loadShader(int profile, String filename)
	{
		CGprogram shaderprog = CgGL.cgCreateProgramFromFile(getCgContext(),
				CgGL.CG_SOURCE, filename, profile, null, null);
		if (shaderprog == null)
		{
			int err = CgGL.cgGetError();
			System.err.println("Compile shader [" + filename + "] "
					+ CgGL.cgGetErrorString(err));
			if (CgGL.cgGetLastListing(getCgContext()) != null)
			{
				System.err.println(CgGL.cgGetLastListing(getCgContext()) + "\n");
			}
			System.exit(1);
		}

		CgGL.cgGLLoadProgram(shaderprog);

		int err = CgGL.cgGetError();
		if (err != CgGL.CG_NO_ERROR)
		{
			System.out.println("Load shader [" + filename + "]: "
					+ CgGL.cgGetErrorString(err));
			System.exit(1);
		}

		return shaderprog;
	}

	protected void bindParameters()
	{
		cgBlackHolePosition = CgGL.cgGetNamedParameter(cgFragmentProg, "blackHolePosition");
		cgTime = CgGL.cgGetNamedParameter(cgFragmentProg, "time");
		// TODO: Assignment 3_1: bind stretch factor
		cgStretchFactor = CgGL.cgGetNamedParameter(cgVertexProg, "stretchFactor");
		//cgTexture = CgGL.cgGetNamedParameter(cgFragmentProg, "texture0");
	}

	public void display(GLAutoDrawable drawable)
	{		
		// if animation is on, increase frame counter
		if (animation)
			incFrameCounter();
		// get the gl object
		GL gl = drawable.getGL();
		// set the erasing color (black)
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		// clear screen with the defined erasing color and depth buffer
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();
		applyMouseTranslation(gl);
		applyMouseRotation(gl);
		
		//Setup modis
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_COLOR_MATERIAL);

		// set material for cg
		float[] material = MATERIALS[currentMaterial];
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, material, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, material, 4);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, material, 8);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, material, 12);

		// blackHole for cg
		float[] blackHolePosition = new float[] { -5f, 5f, 0f, 1f };
		CgGL.cgGLSetParameter3fv(cgBlackHolePosition, blackHolePosition, 0);
		CgGL.cgGLSetParameter1f(cgTime, getFrameCounter());

		// set light properties
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, MOVING_LIGHT_ADS, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, MOVING_LIGHT_ADS, 4);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, MOVING_LIGHT_ADS, 8);
		
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, MOVING_LIGHT_ADS, 8);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, MOVING_LIGHT_ADS, 2);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, MOVING_LIGHT_ADS, 4);

		// calculate light position
		float dLightHeight = 2.0f;
		double dLightRadius = 5.0d;
		float[] lightPos = new float[] {
				(float) (dLightRadius * Math.cos(getFrameCounter() * 3.14 / 200.0)),
				(float) (dLightRadius * Math.sin(getFrameCounter() * 3.14 / 200.0)),
				dLightHeight, 1.0f };
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, new float[]{-lightPos[0],lightPos[1],lightPos[2],lightPos[3]}, 0);

		// draw light as sphere (without shader)
		gl.glPushMatrix();
		gl.glTranslatef(lightPos[0], lightPos[1], lightPos[2]);
		gl.glColor3f(1f, 1f, 1f);
		getGlu().gluSphere(getGlu().gluNewQuadric(), 0.3, 10, 10);
		gl.glPopMatrix();
		
		// draw light as sphere (without shader)
		gl.glPushMatrix();
		gl.glTranslatef(-lightPos[0], lightPos[1], lightPos[2]);
		gl.glColor3f(0.5f, 1f, 0.5f);
		getGlu().gluSphere(getGlu().gluNewQuadric(), 0.3, 10, 10);
		gl.glPopMatrix();

		// move vertices along normals (see vertex program)
		float stretchFactor = 0;
		if (stretch)
		{
			// TODO: Assignment 3_1: use Math.sin and getFrameCounter here to
			// calculate stretchFactor
			stretchFactor = (float) Math.abs(0.05f * Math.sin(0.01f * getFrameCounter()));
		}
		// TODO: Assignment 3_1: set cgStretch parameter
		CgGL.cgGLSetParameter1f(cgStretchFactor, stretchFactor);
		
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
		gl.glPolygonMode(GL.GL_BACK, GL.GL_LINE); //Draw As Wireframes
		gl.glCullFace(GL.GL_FRONT); // Don't Draw Any Front-Facing Polygons
		gl.glDepthFunc(GL.GL_LEQUAL); // Change The Depth Mode
		gl.glColor3f(0, 0, 0); // Set The Outline Color
		gl.glLineWidth(4); // Set The Line Width
		gl.glCallList(dList); //Call Your Display List
		gl.glDepthFunc(GL.GL_LESS); // Reset The Depth-Testing Mode
		gl.glCullFace(GL.GL_BACK); // Reset The Face To Be Culled
		gl.glPolygonMode(GL.GL_BACK, GL.GL_FILL); // Reset Polygon Drawing Mode
	

		gl.glPopMatrix();
	}

	/**
	 * This method increases the frame counter
	 * 
	 * @see getFrameCounter()
	 */
	private void incFrameCounter()
	{
		if (frameCounter < Integer.MAX_VALUE)
		{
			frameCounter++;
		}
		else
			frameCounter = 0;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		super.keyPressed(e);
		if (e.getKeyCode() == KeyEvent.VK_M)
		{
			currentMaterial = currentMaterial < MATERIALS.length - 1 ? currentMaterial + 1
					: 0;
			System.out.println("Material: " + currentMaterial);
		}
		else if (e.getKeyCode() == KeyEvent.VK_S)
		{
			stretch = !stretch;
			System.out.println("Stretch: " + stretch);
		}
		else if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			animation = !animation;
			System.out.println("Animation: " + animation);
		}
	}

	public int getCgVertexProfile()
	{
		return cgVertexProfile;
	}

	public int getCgFragProfile()
	{
		return cgFragProfile;
	}

	public CGprogram getCgVertexProg()
	{
		return cgVertexProg;
	}

	public void setCgVertexProg(CGprogram cgVertexProg)
	{
		this.cgVertexProg = cgVertexProg;
	}

	public CGprogram getCgFragmentProg()
	{
		return cgFragmentProg;
	}

	public void setCgFragmentProg(CGprogram cgFragmentProg)
	{
		this.cgFragmentProg = cgFragmentProg;
	}

	public CGcontext getCgContext()
	{
		return cgContext;
	}

	public int getFrameCounter()
	{
		return frameCounter;
	}
	
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

	// ------------------------------------------------------------

	/**
	 * Draws a square based on given vertexes
	 */
	public void drawSquare3f(GL gl, float[] color, float[] vertex1,
			float[] vertex2, float[] vertex3, float[] vertex4) {

		gl.glPushMatrix();
		{
			gl.glColor3f(color[0], color[1], color[2]);
			gl.glBegin(GL.GL_POLYGON);
			{
				//Set normals
				if(vertex1[0] == vertex2[0] && vertex1[0] == vertex3[0] && vertex1[0] == vertex4[0]){
					gl.glNormal3f(1, 0, 0);
				}else if(vertex1[1] == vertex2[1] && vertex1[1] == vertex3[1] && vertex1[1] == vertex4[1]){
					gl.glNormal3f(0, 1, 0);
				}else if(vertex1[2] == vertex2[2] && vertex1[2] == vertex3[2] && vertex1[2] == vertex4[2]){
					gl.glNormal3f(0, 0, 1);
				}

		        gl.glTexCoord2f(0, 0);
				gl.glVertex3f(vertex1[0], vertex1[1], vertex1[2]);
		        gl.glTexCoord2f(1, 0);
				gl.glVertex3f(vertex2[0], vertex2[1], vertex2[2]);
		        gl.glTexCoord2f(1, 1);
				gl.glVertex3f(vertex3[0], vertex3[1], vertex3[2]);
		        gl.glTexCoord2f(0, 1);
				gl.glVertex3f(vertex4[0], vertex4[1], vertex4[2]);
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
	}
}