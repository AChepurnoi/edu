
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Sasha on 7/4/17.
 */
public class check_brackets {


    private static class Pair<K, V>{
        public K first;
        public V second;

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
    }

    private static class Bracket {
        public int symbol;
        public boolean opening;

        public Bracket(int symbol, boolean opening) {
            this.symbol = symbol;
            this.opening = opening;
        }
    }


    private static Bracket R_OPEN = new Bracket('(', true);
    private static Bracket R_CLOSING = new Bracket(')', false);
    private static Bracket SQ_OPEN = new Bracket('[', true);
    private static Bracket SQ_CLOSING = new Bracket(']', false);
    private static Bracket F_OPEN = new Bracket('{', true);
    private static Bracket F_CLOSING = new Bracket('}', false);


    private static List<Bracket> opening = Arrays.asList(R_OPEN, SQ_OPEN, F_OPEN);
    private static List<Bracket> closing = Arrays.asList(R_CLOSING, SQ_CLOSING, F_CLOSING);
    private static List<Bracket> brackets = Stream
            .concat(closing.stream(), opening.stream())
            .collect(Collectors.toList());

    private static List<Integer> symbols = brackets.stream().map(x -> x.symbol).collect(Collectors.toList());


    private static List<Pair<Bracket, Bracket>> matching = Arrays.asList(
            new Pair<>(R_OPEN, R_CLOSING),
            new Pair<>(SQ_OPEN, SQ_CLOSING),
            new Pair<>(F_OPEN, F_CLOSING));

    public static Bracket buildFrom(char ch) {

        for (Bracket bracket : brackets) {
            if(bracket.symbol == ch)
                return bracket;
        }
        return null;
    }

    private static class BracketsMatcher {
        private Stack<Pair<Bracket, Integer>> stack = new Stack<>();
        private int currentPos;
        private int errorPos = -1;


        @Override
        public String toString() {
            return "BracketsMatcher{" +
                    "stack=" + stack +
                    ", currentPos=" + currentPos +
                    ", errorPos=" + errorPos +
                    '}';
        }

        public int getErrorPos(){
            if(errorPos > -1 || stack.isEmpty()) return errorPos;
            else return stack.peek().second;
        }

        public void process(char ch) {
            if (errorPos > -1) return;
            if (!symbols.contains((int) ch)) {
                currentPos = currentPos + 1;
                return;
            }

            Bracket br = buildFrom(ch);

            if (br.opening) {
                stack.push(new Pair<>(br, currentPos));
                currentPos = currentPos + 1;
            } else {
                if (stack.isEmpty()) {
                    errorPos = currentPos;
                    return;
                }
                Pair<Bracket, Integer> last = stack.pop();
                Pair<Bracket, Bracket> lastPair = matching.stream().filter(x -> x.first.symbol == last.first.symbol).findAny().get();
                if (lastPair.second.symbol != br.symbol) {
                    errorPos = currentPos;
                    return;

                }
                currentPos = currentPos + 1;
            }
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        BracketsMatcher matcher = new BracketsMatcher();
        for (char c : line.toCharArray()) {
            matcher.process(c);
        }

        int errorPos = matcher.getErrorPos();
        if(errorPos > -1) System.out.println(errorPos + 1);
        else System.out.println("Success");



    }


}
