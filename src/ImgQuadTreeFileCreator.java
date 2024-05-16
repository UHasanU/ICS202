import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ImgQuadTreeFileCreator {
    public ImgQuadTreeFileCreator(String filename, int length) {
        try {
            int[][] imageArray;
            try (Scanner reader = new Scanner(new File(filename))) {
                imageArray = getImageArray(reader, length);
            }
            ArrayList<Integer> traversal = getPreOrderTraversal(imageArray);
            writeTraversal(traversal);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");;
        }
    }

    public int[][] getImageArray(Scanner reader, int length) {
        int[][] arr = new int[length][length];
        for (int row = 0; row < arr.length; row++)
            for (int column = 0; column < arr[row].length; column++)
                arr[row][column] = reader.nextInt();
        return arr;
    }

    public ArrayList<Integer> getPreOrderTraversal(int[][] imgArray) {
        if (ImgQuadTreeFileCreator.isOneValue(imgArray)) {
            ArrayList<Integer> arr = new ArrayList<>();
            arr.add(imgArray[0][0]);
            return arr;
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

    public void writeTraversal(ArrayList<Integer> preOrderTraversal) {
            try (PrintWriter writer = new PrintWriter("QuadTree.txt")) {
                for (int el : preOrderTraversal)
                    writer.println(el);
            } catch (FileNotFoundException e) {
                System.out.println("File not found");;
            }
    }

    public static boolean isOneValue(int[][] arr) {
        int value = arr[0][0];
        for (int row = 0; row < arr.length; row++)
            for (int column = 0; column < arr[row].length; column++)
                if (arr[row][column] != value)
                    return false;
        return true;
    }

    public static int[][] getQuarter(int[][] arr, int quarter) {
        int quarterLength = arr.length / 2;
        int[][] quarterArr = new int[quarterLength][quarterLength];

        int rowStart = (quarter == 3 || quarter == 4) ? quarterLength : 0;
        int colStart = (quarter == 2 || quarter == 4) ? quarterLength : 0;

        for (int row = 0; row < quarterLength; row++)
            for (int col = 0; col < quarterLength; col++)
                quarterArr[row][col] = arr[row + rowStart][col + colStart];

        return quarterArr;
    }
}
