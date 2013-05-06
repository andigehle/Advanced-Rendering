package hilfsklassen;

/**
 * @author Andreas Elsner / Stephan Arens / Gitta Domik
 * 
 * CG2_CubeMappingTemplate
 * Department of Computer Science at the University of Paderborn, Germany
 * Research Group of Prof. Gitta Domik - Computer Graphics, Visualization and Digital Image Processing
 */
import hilfsklassen.JoglTemplate;

import java.awt.event.KeyEvent;
import java.io.File;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

@SuppressWarnings("serial")
public class AdR_CubeMappingTemplate extends JoglTemplate {

	private int textureFilterMode = GL.GL_NEAREST;

	private int textureGenMode;

	private enum Model {
		SPHERE, TEAPOT, BUNNY
	};

	private Model model = Model.TEAPOT;

	private int objectList;

	public static void main(String[] args) {
		AdR_CubeMappingTemplate assignment = new AdR_CubeMappingTemplate();
		assignment.setVisible(true);
	}

	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		GL gl = drawable.getGL();
		// smooth shading
		gl.glShadeModel(GL.GL_SMOOTH);
		// enable depth test
		gl.glEnable(GL.GL_DEPTH_TEST);
		// type of depth test
		gl.glDepthFunc(GL.GL_LEQUAL);
		// backface culling
		gl.glEnable(GL.GL_CULL_FACE);
		// calculate normals automatically
		gl.glEnable(GL.GL_AUTO_NORMAL);
		// normalize normals
		gl.glEnable(GL.GL_NORMALIZE);
		// best perspective calculations
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

		gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);

		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_S,
				GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_T,
				GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_R,
				GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_LINEAR);

		// TODO: complete the array to load the cube map
		String[] faceFile = { "quadrangle_right", "quadrangle_left", "quadrangle_top",
				"quadrangle_down", "quadrangle_front", "quadrangle_back" };

		int[] faceTarget = { GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
				GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
				GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
				GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
				GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
				GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };
		// load texture data
		TextureData[] faces = loadTextureData(faceFile,
				"assets/textures/quadrangle/");
		// assign textures to environment map
		for (int i = 0; i < 6; i++) {
			// Image format is GL_BGR for jpg (wrong internal format?), GL_RGB
			// for
			// png!
			gl.glTexImage2D(faceTarget[i], 0, faces[i].getInternalFormat(),
					faces[i].getWidth(), faces[i].getHeight(), 0, GL.GL_BGR,
					GL.GL_UNSIGNED_BYTE, faces[i].getBuffer());
		}

		// enable texture coordinates generation mode (mode must been set with
		// glTexGeni before, otherwise glEnable fails... we change the mode
		// afterwards in the display method)
		gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_REFLECTION_MAP);
		gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, GL.GL_REFLECTION_MAP);
		gl.glTexGeni(GL.GL_R, GL.GL_TEXTURE_GEN_MODE, GL.GL_REFLECTION_MAP);
		gl.glEnable(GL.GL_TEXTURE_GEN_S);
		gl.glEnable(GL.GL_TEXTURE_GEN_T);
		gl.glEnable(GL.GL_TEXTURE_GEN_R);
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
		// enable cube map
		gl.glEnable(GL.GL_TEXTURE_CUBE_MAP);

		// TODO: load the bunny model
		OBJLoader objLoader;
		objLoader = new OBJLoader("assets/objects/bunny", 1.0f,
				drawable.getGL());
		objectList = objLoader.getDisplayList();
	}

	private TextureData[] loadTextureData(String[] files, String path) {
		TextureData[] faces = new TextureData[files.length];
		String filename = "<unknown>";
		try {
			for (int i = 0; i < files.length; i++) {
				filename = path + files[i];
				faces[i] = TextureIO.newTextureData(new File(filename+".png"),
						GL.GL_RGBA, GL.GL_RGBA, false, TextureIO.PNG);
			}
			return faces;
		} catch (Exception e) {
			System.err.println("Load texture data [" + filename + "]: \n"
					+ e.getMessage());
		}
		System.exit(1);
		return null;
	}

	public void display(GLAutoDrawable drawable) {
		// get the gl object
		GL gl = drawable.getGL();
		// set the erasing color (black)
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		// clear screen with the defined erasing color and depth buffer
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();
		applyMouseTranslation(gl);
		applyMouseRotation(gl);

		// set texture filter mode
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_MIN_FILTER,
				textureFilterMode);
		// set texture coordinates generation mode
		gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, textureGenMode);
		gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, textureGenMode);
		gl.glTexGeni(GL.GL_R, GL.GL_TEXTURE_GEN_MODE, textureGenMode);

		// draw model
		if (model == Model.SPHERE) {
			getGlut().glutSolidSphere(1, 40, 40);
		} else if (model == Model.TEAPOT) {
			// the glutSolidTeapot seems to be buggy, polygons are facing with
			// their
			// back sides out
			gl.glCullFace(GL.GL_FRONT);
			getGlut().glutSolidTeapot(1);
			gl.glCullFace(GL.GL_BACK);
		} else if (model == Model.BUNNY) {
			// TODO: switch between sphere, teapot and bunny
			gl.glCallList(objectList);
		}

		gl.glPopMatrix();
	}

	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		if (e.getKeyCode() == KeyEvent.VK_F) {
			if (textureFilterMode == GL.GL_NEAREST)
				textureFilterMode = GL.GL_LINEAR;
			else
				textureFilterMode = GL.GL_NEAREST;
		} else if (e.getKeyCode() == KeyEvent.VK_M) {
			if (textureGenMode == GL.GL_REFLECTION_MAP)
				textureGenMode = GL.GL_NORMAL_MAP;
			else
				textureGenMode = GL.GL_REFLECTION_MAP;
		} else if (e.getKeyCode() == KeyEvent.VK_O) {
			if (model == Model.BUNNY)
				model = Model.TEAPOT;
			else if(model == Model.TEAPOT)
				model = Model.SPHERE;
			// TODO: switch between sphere, teapot and bunny
			else if (model == Model.SPHERE)
				model = Model.BUNNY;
		}
	}
}