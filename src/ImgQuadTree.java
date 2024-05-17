import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ImgQuadTree {
	/*
	 * This function takes
	 * src: a source 2D array (a quarter of a bigger array)
	 * dest: a destination array (The bigger array in which the destination is a quarter of it)
	 * startRow: a starting row i.e. 0 for Q1, length/2 for Q3, etc...
	 * startCol: a starting column i.e. 0 for Q1, length/2 for Q2, etc...
	 * Note this is the order of the quarters:
	 * Q1 Q2
	 * Q3 Q4
	 */
	public static void copy2DArray(int[][] src, int[][] dest, int startRow, int startCol) {
		for (int i = 0; i < src.length; i++) {
			for (int j = 0; j < src[i].length; j++) {
				int destRow = startRow + i;
				int destCol = startCol + j;
				dest[destRow][destCol] = src[i][j];
			}
		}
	}

	QTNode root = new QTNode(); // Define a global root to be used in different functions

	/*
	 * This function takes a file name as a string, and hand a Scanner of it to the buildTree function
	 */
	public ImgQuadTree(String filename){
		try {
			Scanner reader = new Scanner(new File(filename));
            root = buildTree(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File is not found");
		}
	}

	/*
	 * This functions takes
	 * reader: a Scanner to read the next entries in the txt file
	 * current: The current node we are working on
	 * The mechanism of this function is that it read an integer with two cases:
	 * * if entry is -1: The function calls itself recursively, forming the subtrees
	 and assigning them to the correct references
	 * * else the function creates a node with the intensity level and return it,
	 creating a node that'll be a part of a subtree
	 * When the function finishes building the tree, it will finish the if statement and goes to the return statement,
	 making that the base case
	 */
	public QTNode buildTree(Scanner reader) {
		int entry = reader.nextInt();
		QTNode current;
		if (entry == -1) {
			current = new QTNode(-1);
			current.leftNode = buildTree(reader);
			current.midLeftNode = buildTree(reader);
			current.midRightNode = buildTree(reader);
			current.rightNode = buildTree(reader);
		}
		else {
			current = new QTNode(entry);
			return current;
		}

		return current;
	}

	public int getNumNodes() {
		return getNumNodesHelper(root);
	}
	/*
	 * This function will traverse the tree recursively adding 1 with each call, unless the current is null,
	 in which the function will return 0, leading to the end of the function call
	 */
	public int getNumNodesHelper(QTNode current) {
		if (current == null)
			return 0;
		return 1 + getNumNodesHelper(current.leftNode) + getNumNodesHelper(current.midLeftNode)
				+ getNumNodesHelper(current.midRightNode) + getNumNodesHelper(current.rightNode);
	}


	
	public int getNumLeaves() {
		return getNumLeavesHelper(root);
	}
	/*
	 * This function will traverse the tree recursively similarly to the previous function,
	 but will only add 1 when the intensity level is not 1 i.e. there's no children, meaning it's a leaf node
	 */
	public int getNumLeavesHelper(QTNode current) {
		if (current == null)
			return 0;
		if (current.intensityLevel == -1)
			return getNumLeavesHelper(current.leftNode) + getNumLeavesHelper(current.midLeftNode)
					+ getNumLeavesHelper(current.midRightNode) + getNumLeavesHelper(current.rightNode);
		else
			return 1;
	}
	
	public int[][] getImageArray() {
		final int LENGTH = 256;  // The number of pixels in one side of the square picture
		return getImageArrayHelper(new int[LENGTH][LENGTH], root, LENGTH);
	}

	/*
	 * This function will take the tree, will recursively create arrays that represent the leaf nodes,
	 and then combine them into the correct position in the bigger array, until filling in original 256*256 array
	 * This is done through the following:
	 * * Base case: if the length is less than 1, then we already finished creating the array
	 and should proceed into combining
	 * * Recursive case 1: if the node is not leaf, then we call the function again with an array with quarter the size
	 to return a quarter of the array in the previous call. After gathering the four quarters of the array, we proceed with combining
	 the quarters into their correct positions and returning the array to be used in the previous function calls.
	 * * Recursive case 2: if the node is leaf, take the size of the given array and fill it with the intensity value
	 */
	public int[][] getImageArrayHelper(int[][] arr, QTNode current, int length) {
		if (length < 1)
			return arr;
		if (current.intensityLevel == -1) {
			int[][] q1 = getImageArrayHelper(new int[length/2][length/2], current.leftNode, length/2);
			int[][] q2 = getImageArrayHelper(new int[length/2][length/2], current.midLeftNode, length/2);
			int[][] q3 = getImageArrayHelper(new int[length/2][length/2], current.midRightNode, length/2);
			int[][] q4 = getImageArrayHelper(new int[length/2][length/2], current.rightNode, length/2);
			copy2DArray(q1, arr, 0, 0);
			copy2DArray(q2, arr,0, length/2);
			copy2DArray(q3, arr, length/2, 0);
			copy2DArray(q4, arr, length/2, length/2);
		}
		else {
			int[][] q = new int[length][length];
			for (int rows = 0; rows < length; rows++)
				for (int columns = 0; columns < length; columns++)
					q[rows][columns] = current.intensityLevel;
			return q;
		}

		return arr;
			
	}

	
	private class QTNode {
		protected int intensityLevel;
		protected QTNode leftNode;
		protected QTNode midLeftNode;
		protected QTNode midRightNode;
		protected QTNode rightNode;
		public QTNode() {
			intensityLevel = -1;
			leftNode = midLeftNode = midRightNode = rightNode = null;
		}

		public QTNode(int intensityLevel) {
			this.intensityLevel = intensityLevel;
			leftNode = midLeftNode = midRightNode = rightNode = null;
		}

		public QTNode(int intensityLevel, QTNode left, QTNode midLeft, QTNode midRight, QTNode right) {
			this(intensityLevel);
			leftNode = left;
			midLeftNode = midLeft;
			midRightNode = midRight;
			rightNode = right;
		}
		
		
		
		
	}
}

