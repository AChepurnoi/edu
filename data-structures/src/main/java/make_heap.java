import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Sasha on 7/8/17.
 */
public class make_heap {


    static class Swap {
        int l;
        int r;

        public static Swap of(int l, int r) {
            return new Swap(l, r);
        }

        public Swap(int l, int r) {
            this.l = l;
            this.r = r;
        }

        @Override
        public String toString() {
            return l + " " + r;
        }
    }

    static class Node {
        final int val;
        final int idx;

        public Node(int val, int idx) {
            this.val = val;
            this.idx = idx;
        }
    }

    static class HeapBuilder {


        private final int[] nodes;
        private final int len;
        private final List<Swap> swaps = new ArrayList<>();

        public HeapBuilder(int[] nodes) {
            this.nodes = nodes;
            len = nodes.length;
        }

        public List<Swap> construct() {
            int size = len / 2 + 1;
            for (int i = size - 1; i >= 0; i--)
                balance(i);

            return swaps;
        }

        private void balance(int rootIdx) {
            Optional<Integer> root = getNode(rootIdx);
            if (!root.isPresent()) return;
            int rootVal = root.get();
            int leftIdx = 2 * rootIdx + 1;
            int rightIdx = 2 * rootIdx + 2;
            Node left = getNode(leftIdx).map(i -> new Node(i, leftIdx)).orElse(new Node(rootVal, rootIdx));
            Node right = getNode(rightIdx).map(i -> new Node(i, rightIdx)).orElse(new Node(rootVal, rootIdx));

            Optional<Node> min = Stream.of(left, right)
                    .filter(it -> it.val < rootVal)
                    .min(Comparator.comparingInt(l -> l.val));
            if (!min.isPresent()) return;
            Node minimal = min.get();

            apply(new Swap(rootIdx, minimal.idx));
            balance(leftIdx);
            balance(rightIdx);


        }

        private Optional<Integer> getNode(int idx) {
            if (idx < len) return Optional.of(nodes[idx]);
            else return Optional.empty();
        }


        private void apply(Swap swap) {
            int tmp = nodes[swap.l];
            nodes[swap.l] = nodes[swap.r];
            nodes[swap.r] = tmp;
            swaps.add(swap);
        }

    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int[] nodes = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::valueOf).toArray();
        HeapBuilder builder = new HeapBuilder(nodes);
        List<Swap> swaps = builder.construct();
        System.out.println(swaps.size());
        swaps.forEach(System.out::println);

    }

}
