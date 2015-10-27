package LostWoods;
/**
 * This is esentially the node class for the lost forest.
 * Each node can hold data, and a list of "paths" to other nodes.
 */
import java.util.ArrayList;

public class Tree<T>{

	private ArrayList<Tree> paths;
	private T data;

	public Tree(T data){

		this.data = data;
		paths = new ArrayList<Tree>();
	}

	void addPath(Tree path){

		paths.add(path);
	}

	void removePaths(){

		paths.clear();
	}

	ArrayList<Tree> getPaths() {

		return paths;
	}

	Tree getPath(int index){

		return paths.get(index);
	}

	T getData(){

		return data;
	}

	@Override
	public String toString() {

		return data.toString();
	}
}
