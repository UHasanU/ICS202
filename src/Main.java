import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ImgQuadTree q = new ImgQuadTree("data.txt");
        int[][] arr = q.getImageArray();
        System.out.println();
        for (int row = 0; row < arr.length; row++) {
            System.out.println(Arrays.toString(arr[row]));
        }
    }
}
