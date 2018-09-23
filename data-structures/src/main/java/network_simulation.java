import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Sasha on 7/7/17.
 */
public class network_simulation {

    public static class Packet {
        int number;
        int time;
        int size;

        public Packet(int time, int size) {
            this.time = time;
            this.size = size;
        }
    }


    public static class PacketProcessor {
        class ProcessorState {
            private int credits = 0;

            public void tick() {
                credits = credits + 1;
            }

            public void reset() {
                credits = 0;
            }
        }

        private int[] result;
        private ProcessorState state = new ProcessorState();
        private final Queue<Packet> buffer = new ArrayDeque<>();
        private int currentTime = 0;
        private final int max;

        public PacketProcessor(int size) {
            max = size;
        }

        private void enqueue(Packet packet) {
            int size = buffer.size();
            if (size == max) {
                result[packet.number] = -1;
            } else {
                buffer.add(packet);
            }

        }


        private void checkQueue() {
            Packet packet = buffer.peek();
            while (packet != null) {
                int size = packet.size;
                int available = state.credits;
                if (available == size) {
                    buffer.remove();
                    result[packet.number] = currentTime - packet.size;
                    state.reset();
                } else return;
                packet = buffer.peek();
            }
        }


        public void tick() {
            currentTime = currentTime + 1;
            if(!buffer.isEmpty()) state.tick();
        }


        public int[] process(List<Packet> packets) {
            result = new int[packets.size()];

            for (int i = 0; i < packets.size();) {
                checkQueue();
                Packet packet = packets.get(i);
                if (currentTime >= packet.time) {
                    packet.number = i;
                    enqueue(packet);
                    i++;
                } else tick();
            }
            while (!buffer.isEmpty()) {
                checkQueue();
                tick();
            }

            return result;
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");
        int bufferSize = Integer.valueOf(input[0]);
        int count = Integer.valueOf(input[1]);

        List<Packet> packets = IntStream.range(0, count)
                .mapToObj(x -> scanner.nextLine().split(" "))
                .map(network_simulation::read).collect(Collectors.toList());

        PacketProcessor packetProcessor = new PacketProcessor(bufferSize);
        int[] res = packetProcessor.process(packets);
        Arrays.stream(res).forEach(System.out::println);
    }

    public static Packet read(String[] in) {
        return new Packet(Integer.valueOf(in[0]), Integer.valueOf(in[1]));
    }

}
