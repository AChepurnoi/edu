import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Sasha on 7/8/17.
 */
public class make_heap {


    static class Swap{
        int l;
        int r;

        public static Swap of(int l, int r){
            return new Swap(l, r);
        }
        public Swap(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }

    static class HeapBuilder{


        public List<Swap> construct(int[] nodes){
            int size = nodes.length / 2 - 1;



        }


    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int[] nodes = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::valueOf).toArray();



    }

}
