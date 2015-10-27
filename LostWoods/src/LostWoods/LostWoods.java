package LostWoods;
/**
 * Lost Woods is an academic pursuit to find the very worst data structure in existance.
 * It is a data structure with nodes (currently called trees) that point to at least 1 other node
 * in the forest. At most, they point to every other node in the forest.
 *
 * Every time you move through the forest, the pointers get scrambled --each "path" from one tree
 * to another gets changed around.
 *
 * This should not be used for any practical application.
 */
import java.util.ArrayList;
import java.util.Random;

public class LostWoods<T> {

	private ArrayList<Tree> magicForest;
	private Tree entrance;
	private int cursor;

	public LostWoods(){

		magicForest = new ArrayList<Tree>();
		cursor = 0;
	}

	/**
	 * Returns the "entrance" to the forest.
	 * This will also set the cursor.
	 * This will also scramble the forest.
	 * @return the object at the entrance.
	 */
	public T getEntrance() {

		cursor = 0;
		entrance = magicForest.get(0);
		scramble();
		return (T)entrance.getData();
	}

	/**
	 * This gives you a list of path options for this tree at the cursor.
	 * You can pass in the index of this arraylist, or the value of the object
	 * to the followPaths method.
	 * @return the list of possible paths at the current cursor.
	 */
	public ArrayList<T> getPaths(){

		Tree<T> tree = magicForest.get(cursor);
		int numPaths = tree.getPaths().size();

		ArrayList<T> paths = new ArrayList<T>();

		for (int i = 0; i < numPaths; i++){
			paths.add((T)tree.getPath(i).getData());
		}

		return paths;
	}

	/**
	 * This method follows the path by moving the cursor, and returns the object where the cursor is at.
	 * It also scrambles the forest.
	 * @param path the index of the path you wish to follow in the list of paths
	 *             belonging to the tree the cursor is at.
	 * @return the new tree that was followed to (the cursor is now pointing at this)
	 */
	public T followPath(int path){

		Tree<T> tree = magicForest.get(cursor).getPath(path);
		cursor = magicForest.indexOf(tree);
		T value = (T)tree.getPath(path).getData();
		scramble();
		return value;
	}

	/**
	 * This method follows the path by moving the cursor, and returns the object where the cursor is at.
	 * It also scrambles the forest.
	 * @param path the object that will become the new cursor
	 * @return the new tree that was followed to (the cursor is now pointing at this)
	 */
	public T followPath(T path){

		Tree<T> currTree = magicForest.get(cursor);

		for (int i = 0; i < currTree.getPaths().size(); i++){
			if (currTree.getPath(i).equals(path)){

				cursor = magicForest.indexOf(currTree.getPath(i));
				T value = (T)currTree.getPath(i).getData();
				scramble();
				return value;
			}
		}
		return null;
	}

	/**
	 * Removes the item from the forest. Also scrambles it.
	 * @param data
	 */
	public void remove(T data){

		magicForest.remove(data);
		scramble();
	}

	/**
	 * Adds an item to the forest.
	 * This will scramble the forest.
	 * @param data The item to add.
	 */
	public void add(T data){

		Tree tree = new Tree(data);
		magicForest.add(tree);
		scramble();

		if (entrance == null){
			entrance = tree;
		}
	}

	/**
	 * Scrambles the list. The list must be at least size 3, or it will return false
	 * and do nothing.
	 * @return  whether the scramble was performed.
	 */
	public boolean scramble(){

		Random rng  = new Random();

		if (magicForest.size() < 3){
			return false;
		}

		for (int i = 0; i < magicForest.size(); i++){

			magicForest.get(i).removePaths();
		}

		// Loops through every tree in the ordered forest.
		for (int i = 0; i < magicForest.size(); i++){

			int numPaths = findNumPaths(magicForest.get(i));

			// Assign the according number of paths.
			for (int j = 0; j < numPaths; j++){

				int pathToAdd = findNewPath(magicForest.get(i));

				// Add the new path.
				magicForest.get(i).addPath(magicForest.get(pathToAdd));
				// Add the reciprocal path.
				magicForest.get(pathToAdd).addPath(magicForest.get(i));
			}
		}
		return true;
	}

	/**
	 * For internal use. Finds a suitable path for the specified tree.
	 * The constraints for this are:
	 *  A) The path may not lead back to itself
	 *  B) The path may not already be defined for the tree.
	 * @param tree the tree to add the path to
	 * @return the index of the found path (in the magic forest array)
	 */
	private int findNewPath(Tree<T> tree) {

		int index = magicForest.indexOf(tree);
		Random rng  = new Random();
		int pathToAdd = -1;
		// Keep trying until you get a path that isn't yourself.
		// Or the initialize value
		// Exits when a path is found.
		while (pathToAdd == index || pathToAdd == -1) {
			pathToAdd = rng.nextInt(magicForest.size());

			if (pathToAdd != -1) {

				boolean pathExists = false;
				// Checks to see if the path is already one of the tree's paths.
				for (int k = 0; k < tree.getPaths().size(); k++) {

					// Checks if the path k of the current path i is equal to the path to add
					// i.e. If this tree already has the new path.
					if (tree.getPaths().get(k) == magicForest.get(pathToAdd)) {
						pathExists = true;
					}
				}

				if (pathExists) {
					pathToAdd = -1;
				}
			}
		}

		return pathToAdd;
	}

	/**
	 * For internal use. Figures out how many paths should be generated for the current tree.
	 * @param tree
	 * @return
	 */
	private int findNumPaths(Tree<T> tree) {

		Random rng = new Random();

		if (magicForest.size() < 3) {
			return 0;
		}

		int totalTrees = magicForest.size();
		int totalPossiblePaths = totalTrees - totalTrees / 2;
		int currentTreePaths = tree.getPaths().size();

		if (currentTreePaths >= totalPossiblePaths) {
			return 0;
		}

		int numPaths = 0;
		if (currentTreePaths < totalPossiblePaths) {
			numPaths = rng.nextInt(totalPossiblePaths - currentTreePaths) + 1;
		}

		return numPaths;
	}

	/**
	 * It's dangerous to go alone! Take this.
	 * This method is for use as a control, and returns whether the item is in the forest or not.
	 * @param data the item to look for
	 * @return whether the item is in the forest.
	 */
	public boolean realCheck(T data){

		return (magicForest.indexOf(data) != -1);
	}
}
