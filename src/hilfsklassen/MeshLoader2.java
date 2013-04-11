package hilfsklassen;
/**
* @author Andreas Elsner / Gitta Domik / Oleg Travkin
* CG2 MeshLoader (26.04.2010) 
* Department of Computer Science at the University of Paderborn, Germany
* Research Group of Prof. Gitta Domik - Computer Graphics, Visualization and Digital Image Processing
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;

public class MeshLoader2
{

	/**
	 * Loads a .obj mesh file.
	 * @param gl GL object
	 * @param fName file name of the .obj file
	 * @return int index of a display list
	 */
	public static int loadObj(GL gl, String fName)
	{
		return loadObj(gl, fName, 1);
	}
	
	/**
	 * Loads a .obj mesh file.
	 * @param gl GL object
	 * @param fName file name of the .obj file
	 * @param scale scaling factor (all vertices are multiplied with this factor)
	 * @return int index of a display list
	 */
public static int loadObj(GL gl, String fName, float scale)
	{
		ArrayList<int[]> faceList = new ArrayList<int[]>();
		ArrayList<int[]> normalList = new ArrayList<int[]>();
		ArrayList<float[]> normalVertexList = new ArrayList<float[]>();
		ArrayList<float[]> vertexList = new ArrayList<float[]>();
		ArrayList<int[]> texList = new ArrayList<int[]>();
		ArrayList<float[]> texVertexList = new ArrayList<float[]>();
		File file = new File(fName);
		String line;
		int lineCount = 0;

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null)
			{
				lineCount++;
				//trim, remove duplicate spaces, split to <type> <data> <data> ...
				String[] tokens = line.trim().replaceAll("[ ]+", " ").split(" ");
				String type = new String();
				if (tokens.length > 1)
					type = tokens[0];

				if (type.equals("v"))
				{
					float x = Float.parseFloat(tokens[1]);
					float y = Float.parseFloat(tokens[2]);
					float z = Float.parseFloat(tokens[3]);
					vertexList.add(new float[] { x * scale, y * scale, z * scale });
				}
				else if (type.equals("vt"))
				{
					texVertexList.add(new float[] { Float.parseFloat(tokens[1]),
							Float.parseFloat(tokens[2]) });
				}
				else if (type.equals("vn"))
				{
					normalVertexList.add(new float[] { Float.parseFloat(tokens[1]),
							Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]) });
				}
				else if (type.equals("f"))
				{
					String data = tokens[1];
					if (data.indexOf("/") != -1)
					{
						String[] comp0 = new String[3];
						String[] comp1 = new String[3];
						String[] comp2 = new String[3];
						String[] comp3 = new String[3];

						comp0 = tokens[1].split("/");
						comp1 = tokens[2].split("/");
						comp2 = tokens[3].split("/");

						if (tokens.length == 5)// && comp0.length == 3
						{
							comp3 = tokens[4].split("/");

							if (!comp0[0].equals(""))
							{
								faceList.add(new int[] { Integer.parseInt(comp0[0]) - 1,
										Integer.parseInt(comp1[0]) - 1,
										Integer.parseInt(comp2[0]) - 1,
										Integer.parseInt(comp3[0]) - 1 });
							}
							if (comp0.length == 3) //normal case
                    {
                       if (!comp0[1].equals(""))
                       {
                          texList.add(new int[] {
                                Integer.parseInt(comp0[1]) - 1,
                                Integer.parseInt(comp1[1]) - 1,
                                Integer.parseInt(comp2[1]) - 1,
                                Integer.parseInt(comp3[1]) - 1 });
                       }
                       if (!comp0[2].equals(""))
                       {
                          normalList.add(new int[] {
                                Integer.parseInt(comp0[2]) - 1,
                                Integer.parseInt(comp1[2]) - 1,
                                Integer.parseInt(comp2[2]) - 1,
                                Integer.parseInt(comp3[2]) - 1 });
                       }
                    }
							else //texture is missing
							{
							   if (!comp0[1].equals(""))
                       {
                          normalList.add(new int[] {
                                Integer.parseInt(comp0[1]) - 1,
                                Integer.parseInt(comp1[1]) - 1,
                                Integer.parseInt(comp2[1]) - 1,
                                Integer.parseInt(comp3[1]) - 1 });
                       }
							}
						}
						else //if(comp0.length == 3)
						{
							if (!comp0[0].equals(""))
							{
								faceList.add(new int[] { Integer.parseInt(comp0[0]) - 1,
										Integer.parseInt(comp1[0]) - 1,
										Integer.parseInt(comp2[0]) - 1 });
							}
							if (comp0.length == 3) //normal case
                    {
                       if (!comp0[1].equals(""))
                       {
                          texList.add(new int[] {
                                Integer.parseInt(comp0[1]) - 1,
                                Integer.parseInt(comp1[1]) - 1,
                                Integer.parseInt(comp2[1]) - 1 });
                       }
                       if (!comp0[2].equals(""))
                       {
                          normalList.add(new int[] {
                                Integer.parseInt(comp0[2]) - 1,
                                Integer.parseInt(comp1[2]) - 1,
                                Integer.parseInt(comp2[2]) - 1 });
                       }
                    }
							else
							{
							   if (!comp0[1].equals(""))
                       {
                          normalList.add(new int[] {
                                Integer.parseInt(comp0[1]) - 1,
                                Integer.parseInt(comp1[1]) - 1,
                                Integer.parseInt(comp2[1]) - 1 });
                       }
							}
						}
					}
					else
					{
						faceList.add(new int[] { Integer.parseInt(tokens[1]) - 1,
								Integer.parseInt(tokens[2]) - 1,
								Integer.parseInt(tokens[3]) - 1 });
					}
				}
			}
		}
		catch (FileNotFoundException fnfe)
		{
			System.out.println("The file " + file.getName() + " does not exist!");
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (NumberFormatException nfe)
		{
			System.out.println("NumberFormatException in line [" + lineCount + "]: "
					+ nfe.getMessage());
			nfe.printStackTrace();
		}

		return createDisplayList(gl, vertexList, faceList, normalVertexList,
				normalList, texVertexList, texList);
	}
	
	/**
	 * Loads a DirectX mesh file.
	 * TODO: Implement loading of texture coordinates.
	 * @param gl GL object
	 * @param fName file name of the .x file
	 * @return int index of a display list
	 */
	public static int loadDirectX(GL gl, String fName)
	{
		return loadDirectX(gl, fName, 1);		
	}

	/**
	 * Loads a DirectX mesh file.
	 * TODO: Implement loading of texture vertices.
	 * @param gl GL object
	 * @param fName file name of the .x file
	 * @param scale scaling factor (all vertices are multiplied with this factor)
	 * @return int index of a display list
	 */
	public static int loadDirectX(GL gl, String fName, float scale)
	{
		ArrayList<int[]> faceList = new ArrayList<int[]>();
		ArrayList<int[]> normalList = new ArrayList<int[]>();
		ArrayList<float[]> normalVertexList = new ArrayList<float[]>();
		ArrayList<float[]> vertexList = new ArrayList<float[]>();
		ArrayList<int[]> texList = new ArrayList<int[]>();
		ArrayList<float[]> texVertexList = new ArrayList<float[]>();
		File file = new File(fName);
		String line;
		int lineCount = 0;

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null)
			{
				lineCount++;
				String[] tok = line.trim().split(" ");

				if (tok[0].equals("Mesh") && tok[1].equals("{"))
				{
					line = br.readLine().trim();
					lineCount++;
					int numberOfVertices = Integer.valueOf((line.split(";"))[0].trim());

					// read vertices
					if (numberOfVertices > 0)
						while (!line.endsWith(";;"))
						{
							line = br.readLine().trim();
							lineCount++;
							String[] tokens = line.split(";");
							float x = Float.parseFloat(tokens[0]);
							float y = Float.parseFloat(tokens[1]);
							float z = Float.parseFloat(tokens[2]);
							vertexList.add(new float[] { x * scale, y * scale, z * scale });
						}

					// read faces
					line = br.readLine().trim();
					lineCount++;
					int numberOfFaces = Integer.valueOf((line.split(";"))[0].trim());
					if (numberOfFaces > 0)
						while (!line.endsWith(";;"))
						{
							line = br.readLine().trim();
							lineCount++;
							String[] tokens = line.split(";|,");

							int a = Integer.parseInt(tokens[1]);
							int b = Integer.parseInt(tokens[2]);
							int c = Integer.parseInt(tokens[3]);
							if (Integer.parseInt(tokens[0]) == 4)
							{
								int d = Integer.parseInt(tokens[4]);
								faceList.add(new int[] { a, b, c, d });
							}
							else
								faceList.add(new int[] { a, b, c });
						}
				}
				else if (tok[0].equals("MeshNormals") && tok[1].equals("{"))
				{
					line = br.readLine().trim();
					lineCount++;
					int numberOfNormals = Integer.valueOf((line.split(";"))[0].trim());

					// read normals
					if (numberOfNormals > 0)
						while (!line.endsWith(";;"))
						{
							line = br.readLine().trim();
							lineCount++;
							String[] tokens = line.split(";");
							float x = Float.parseFloat(tokens[0]);
							float y = Float.parseFloat(tokens[1]);
							float z = Float.parseFloat(tokens[2]);
							normalVertexList.add(new float[] { x, y, z });
						}

					// read indices
					line = br.readLine().trim();
					lineCount++;
					int numberOfFaces = Integer.valueOf((line.split(";"))[0].trim());
					if (numberOfFaces > 0)
						while (!line.endsWith(";;"))
						{
							line = br.readLine().trim();
							lineCount++;
							String[] tokens = line.split(";|,");

							int a = Integer.parseInt(tokens[1]);
							int b = Integer.parseInt(tokens[2]);
							int c = Integer.parseInt(tokens[3]);
							if (Integer.parseInt(tokens[0]) == 4)
							{
								int d = Integer.parseInt(tokens[4]);
								normalList.add(new int[] { a, b, c, d });
							}
							else
								normalList.add(new int[] { a, b, c });
						}
				}//if (tok[0].equals("MeshNormals") && tok[1].equals("{"))
				
			}//while
		}
		catch (FileNotFoundException fnfe)
		{
			System.out.println("The file " + file.getName() + " does not exist!");
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (NumberFormatException nfe)
		{
			System.out.println("NumberFormatException in line [" + lineCount + "]: "
					+ nfe.getMessage());
			nfe.printStackTrace();
		}

		return createDisplayList(gl, vertexList, faceList, normalVertexList,
				normalList, texVertexList, texList);
	}

	private static int createDisplayList(GL gl, ArrayList<float[]> vertexList,
			ArrayList<int[]> faceList, ArrayList<float[]> normalVertexList,
			ArrayList<int[]> normalList, ArrayList<float[]> texVertexList,
			ArrayList<int[]> texList)
	{
		if (faceList.size() == 0)
			return -1;

		int vertices = faceList.get(0).length;
		boolean tex = (texVertexList.size() > 0 && texList.size() > 0);
		boolean norm = (normalVertexList.size() > 0 && normalList.size() > 0);

		int tempList = gl.glGenLists(1);
		gl.glNewList(tempList, GL.GL_COMPILE);

		boolean quadsNow = false;
		if (vertices == 4)
		{
		   gl.glBegin(GL.GL_QUADS);
		   quadsNow = true;
		}
		else
		{
			gl.glBegin(GL.GL_TRIANGLES);
		}
	
		for (int i = 0; i < faceList.size(); i++)
		{
		   if(faceList.get(i).length == 4 && quadsNow == false)
		   {
		      gl.glEnd();
		      gl.glBegin(GL.GL_QUADS);
		      quadsNow = true;
		      vertices = 4;
		   }
		   else if(faceList.get(i).length == 3 && quadsNow == true)
		   {
		      gl.glEnd();
		      gl.glBegin(GL.GL_TRIANGLES);
           quadsNow = false;
           vertices = 3;
		   }
			for (int vIndex = 0; vIndex < vertices; vIndex++)
			{
				if (norm)
					gl.glNormal3fv(normalVertexList.get(normalList.get(i)[vIndex]), 0);
				if (tex)
					gl.glTexCoord2fv(texVertexList.get(texList.get(i)[vIndex]), 0);
				gl.glVertex3fv(vertexList.get(faceList.get(i)[vIndex]), 0);
			}
		}

		gl.glEnd();
		gl.glEndList();
		return tempList;
	}
}