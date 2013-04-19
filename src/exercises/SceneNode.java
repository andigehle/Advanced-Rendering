package exercises;

import java.util.LinkedList;

public abstract class SceneNode {
	public float rotX, rotY, rotZ;
	public LinkedList<SceneNode> children;
	
	public SceneNode(){
		
	}
}
