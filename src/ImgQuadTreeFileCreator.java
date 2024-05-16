import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ImgQuadTreeFileCreator {
    public ImgQuadTreeFileCreator(String filename, int length) {
        try {
            Scanner reader = new Scanner(new File(filename));
            int[][] imageArray = getImageArray(reader, length);
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
        int[][] quarterArr = new int[arr.length/2][arr.length/2];
        if (quarter == 1)
            for (int row = 0; row < arr.length / 2; row++)
                System.arraycopy(arr[row], 0, quarterArr[row], 0, arr.length / 2);
        if (quarter == 2)
            for (int row = 0; row < arr.length/2; row++) {
                for (int column = arr.length/2; column < arr.length; column++) {

                }

            }
    }
}
