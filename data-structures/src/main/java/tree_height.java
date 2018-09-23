import java.util.*;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

/**
 * Created by Sasha on 7/5/17.
 */
public class tree_height {

    public static class TreeNode {
        private int id;
        private List<TreeNode> children = new ArrayList<>();

        public TreeNode(int id) {
            this.id = id;
        }

        public void add(TreeNode node) {
            children.add(node);
        }

        public int getId() {
            return id;
        }

        public int getHeight() {
            int height = 1;
            List<TreeNode> nodes = children;
            while (true) {
                if (!nodes.isEmpty()) {
                    height = height + 1;
                    nodes = nodes.stream().flatMap(x -> x.getChildren().stream()).collect(Collectors.toList());
                } else return height;
            }
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<TreeNode> getChildren() {
            return children;
        }

        public void setChildren(List<TreeNode> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "TreeNode{" +
                    "id=" + id +
                    ", children=" + children +
                    '}';
        }
    }


    public static TreeNode buildTree(int count, String str) {

        Map<Integer, TreeNode> nodes = IntStream
                .range(0, count)
                .mapToObj(TreeNode::new)
                .collect(Collectors.toMap(TreeNode::getId, Function.identity()));


        int[] parents = Arrays
                .stream(str.split(" "))
                .mapToInt(Integer::valueOf).toArray();

        for (int i = 0; i < parents.length; i++) {
            if (parents[i] == -1) continue;
            nodes.get(parents[i]).add(nodes.get(i));
        }


        TreeNode parent = nodes
                .get(Arrays.stream(parents)
                .boxed()
                .collect(Collectors.toList())
                .indexOf(-1));
        return parent;

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int nodeCount = Integer.valueOf(scanner.nextLine());
        String nodes = scanner.nextLine();
        TreeNode tree = buildTree(nodeCount, nodes);
        System.out.println(tree.getHeight());

    }

}
