import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Sasha on 7/9/17.
 */
public class parallel_processing {


    static class Result {
        int id;
        long time;

        public Result(int id, long time) {
            this.id = id;
            this.time = time;
        }

        @Override
        public String toString() {
            return id + " " + time;
        }
    }

    static class Thread implements Comparable<Thread> {
        int id;

        @Override
        public int compareTo(Thread o) {
            return id - o.id;
        }

        public Thread(int id) {
            this.id = id;
        }
    }

    static class WorkingThread implements Comparable<WorkingThread> {
        Thread thread;
        long required;
        long startTime;

        @Override
        public int compareTo(WorkingThread o) {
            return (int) (getEndTime() - o.getEndTime());
        }

        public long getEndTime() {
            return startTime + required;
        }

        public boolean isCompleted(long current) {
            return (startTime + required) == current;
        }

        public WorkingThread(Thread thread, long startTime, long required) {
            this.thread = thread;
            this.required = required;
            this.startTime = startTime;
        }
    }

    static class PacketProcessor {

        private final PriorityQueue<Thread> threads = new PriorityQueue<>();
        private final PriorityQueue<WorkingThread> workingThreads = new PriorityQueue<>();
        private long time = 0;
        private final Result[] results;
        private final long[] packets;

        public PacketProcessor(int num, long[] packets) {
            results = new Result[packets.length];
            this.packets = packets;

            IntStream.range(0, num)
                    .mapToObj(Thread::new)
                    .forEach(threads::add);

        }


        private Optional<Thread> getThread() {
            return Optional.ofNullable(threads.poll());
        }

        private Optional<WorkingThread> getFinished() {
            boolean completed = Optional.ofNullable(workingThreads.peek())
                    .map(x -> x.isCompleted(time))
                    .orElse(false);
            if (completed) return Optional.of(workingThreads.poll());
            else return Optional.empty();
        }


        private void clearWorkingThreads() {
            Optional<WorkingThread> finished = getFinished();
            while (finished.isPresent()) {
                threads.add(finished.get().thread);
                finished = getFinished();
            }
        }

        private void clock() {
            Optional<WorkingThread> closest = Optional.ofNullable(workingThreads.peek());
            time = closest.map(WorkingThread::getEndTime).orElse(time + 1);
            clearWorkingThreads();
        }

        public Result[] process() {
            for (int i = 0; i < packets.length; ) {
                Optional<Thread> free = getThread();
                if (free.isPresent()) {
                    Thread thread = free.get();
                    WorkingThread workingThread = new WorkingThread(thread, time, packets[i]);
                    workingThreads.add(workingThread);
                    results[i] = new Result(thread.id, time);
                    i = i + 1;
                } else clock();
            }
            return results;
        }

        public Result[] getResults() {
            return results;
        }
    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int count = Arrays.stream(scanner.nextLine().split(" "))
                .mapToInt(Integer::valueOf).toArray()[0];
        long[] times = Arrays.stream(scanner.nextLine().split(" "))
                .mapToLong(Long::valueOf).toArray();

        PacketProcessor processor = new PacketProcessor(count, times);
        Result[] res = processor.process();
        Arrays.stream(res).forEach(System.out::println);

    }


}
