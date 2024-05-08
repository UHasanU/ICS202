import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ImgQuadTree q = new ImgQuadTree("data.txt");
        System.out.println(q.getNumLeaves());
    }
}