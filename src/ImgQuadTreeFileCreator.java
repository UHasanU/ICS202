import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ImgQuadTreeFileCreator {
    /*
     * Main function for creating the reader, generate the 2D image array, get the traversal, and write it
     */
    public ImgQuadTreeFileCreator() {
        System.out.println("Please input the name of the uncompressed file to compress:");
        Scanner fileNameReader = new Scanner(System.in);
        String filename = fileNameReader.nextLine();
        fileNameReader.close();
        try (Scanner reader = new Scanner(new File(filename))) {
            final int LENGTH = 256; // The length of the side of the uncompressed file
            int[][] imageArray = getImageArray(reader, LENGTH);
            ArrayList<Integer> traversal = getPreOrderTraversal(imageArray);
            writeTraversal(traversal, filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /*
     * Create an image array by reading each "length" integers and make them a one line of the image array
     i.e. fill each row with "length" integers then go to the next until filling the length×length array
     */
    public int[][] getImageArray(Scanner reader, int length) {
        int[][] arr = new int[length][length];
        for (int row = 0; row < arr.length; row++)
            for (int column = 0; column < arr[row].length; column++)
                arr[row][column] = reader.nextInt();
        return arr;
    }

    /*
     * A function to generate a 1D pre-order traversal list from a 2D image array
     * if the image array has one value only, then it is one node in the Quad Tree with intensity equal to that value
     * else then it's a -1 node with 4 sub nodes, each node is determined by calling the function
     with quarter the image array. This will be done recursively until reaching a point when
     all the calls return from the first if statement, that's when the caller will merge them into
     one arraylist, representing the pre-order traversal.
     * By adding the -1 first, then the q1, then q2, then q3, then q4, we make sure that's pre-order traversal
     in the way that's Root-Left-LeftMid-RightMid-Right
     */
    public ArrayList<Integer> getPreOrderTraversal(int[][] imgArray) {
        if (ImgQuadTreeFileCreator.isOneValue(imgArray)) {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(imgArray[0][0]);
            return list;
        }
        else {
            ArrayList<Integer> main = new ArrayList<>();
            main.add(-1);
            ArrayList<Integer> q1 = getPreOrderTraversal(getQuarter(imgArray, 1));
            ArrayList<Integer> q2 = getPreOrderTraversal(getQuarter(imgArray, 2));
            ArrayList<Integer> q3 = getPreOrderTraversal(getQuarter(imgArray, 3));
            ArrayList<Integer> q4 = getPreOrderTraversal(getQuarter(imgArray, 4));
            main.addAll(q1);
            main.addAll(q2);
            main.addAll(q3);
            main.addAll(q4);
            return main;
        }
    }

    /*
     * Create a print write and write the traversal line by line into a file
     */
    public void writeTraversal(ArrayList<Integer> preOrderTraversal, String filename) {
            try (PrintWriter writer = new PrintWriter(filename.substring(0, filename.length() - 4) + "QT.txt")) {
                for (int el : preOrderTraversal)
                    writer.println(el);
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
    }

    /*
     * Loops on the 2D array and check if it contains 1 value i.e. can it be represented by 1 node
     */
    public static boolean isOneValue(int[][] arr) {
        int value = arr[0][0];
        for (int row = 0; row < arr.length; row++)
            for (int column = 0; column < arr[row].length; column++)
                if (arr[row][column] != value)
                    return false;
        return true;
    }

    /*
     * Receives a 2D array and return a new array from a specific quarter
     * Note the order of the quarters is:
     * Q1 Q2
     * Q3 Q4
     */
    public static int[][] getQuarter(int[][] arr, int quarter) {
        int quarterLength = arr.length / 2;
        // Half the width with half the height creates a quarter array
        int[][] quarterArr = new int[quarterLength][quarterLength];
        // Should start the rows at length/2 with the third or forth quarter
        int rowStart = (quarter == 3 || quarter == 4) ? quarterLength : 0;
        // Should start the columns at length/2 with the second or forth quarter
        int colStart = (quarter == 2 || quarter == 4) ? quarterLength : 0;

        // copy the elements from the array to their corresponding location
        for (int row = 0; row < quarterLength; row++)
            for (int col = 0; col < quarterLength; col++)
                quarterArr[row][col] = arr[row + rowStart][col + colStart];

        return quarterArr;
    }
}
