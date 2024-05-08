import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ImgQuadTree {

	public static void copy2DArray(int[][] src, int[][] dest, int startRow, int startCol) {
		for (int i = 0; i < src.length; i++) {
			for (int j = 0; j < src[i].length; j++) {
				int destRow = startRow + i;
				int destCol = startCol + j;
				dest[destRow][destCol] = src[i][j];
			}
		}
	}

	QTNode root = new QTNode();
	public ImgQuadTree(String filename){
		try {
			Scanner reader = new Scanner(new File(filename));
            root = buildTree(reader, root);
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File is not found");
		}



	}

	public QTNode buildTree(Scanner reader, QTNode current) {
		if (! reader.hasNextInt())
			return root;
		int entry = reader.nextInt();
		if (entry == -1) {
			current = new QTNode(-1);
			current.leftNode = buildTree(reader, current.leftNode);
			current.midLeftNode = buildTree(reader, current.midLeftNode);
			current.midRightNode = buildTree(reader, current.midRightNode);
			current.rightNode = buildTree(reader, current.rightNode);
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

	public int getNumNodesHelper(QTNode current) {
		if (current == null)
			return 0;
		return 1 + getNumNodesHelper(current.leftNode) + getNumNodesHelper(current.midLeftNode)
				+ getNumNodesHelper(current.midRightNode) + getNumNodesHelper(current.rightNode);
	}


	
	public int getNumLeaves() {
		return getNumLeavesHelper(root);
	}

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
		final int LENGTH = 256;
		final int QUARTER = 1;
		return getImageArrayHelper(new int[LENGTH][LENGTH], root, LENGTH, QUARTER);
	}

	public int[][] getImageArrayHelper(int[][] arr, QTNode current, int length, int quarter) {
		if (length < 1)
			return arr;
		if (current.intensityLevel == -1) {
			int[][] q1 = getImageArrayHelper(new int[length/2][length/2], current.leftNode, length/2, 1);
			int[][] q2 = getImageArrayHelper(new int[length/2][length/2], current.midLeftNode, length/2, 2);
			int[][] q3 = getImageArrayHelper(new int[length/2][length/2], current.midRightNode, length/2, 3);
			int[][] q4 = getImageArrayHelper(new int[length/2][length/2], current.rightNode, length/2, 4);
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

