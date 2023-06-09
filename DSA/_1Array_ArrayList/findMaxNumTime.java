package DSA._1Array_ArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
// Get max number - for threads count = user input 
// line no 20 : numThreads = 10

public class findMaxNumTime {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Integer> list = new ArrayList<>();
        Random rand = new Random();
        while (list.size() < 1000) {
            int num = rand.nextInt(9001) + 1000; // generates random number between 1000 and 10000
            if (!list.contains(num)) {
                list.add(num); // adds unique number to the list
            }
        }

        int numThreads = 10; // number of threads to use
        System.out.println("==================================================================");

        int sublistSize = list.size() / numThreads; // size of each sublist
        ArrayList<SublistMaxFinder> threads = new ArrayList<>();

        // create and start threads
        for (int i = 0; i < numThreads; i++) {
            SublistMaxFinder thread = new SublistMaxFinder(list.subList(i * sublistSize, (i + 1) * sublistSize));
            threads.add(thread);
            thread.start();
        }

        int max = Integer.MIN_VALUE;
        long[] threadTimes = new long[numThreads];
        // join threads and find maximum number among returned values
        for (int i = 0; i < numThreads; i++) {
            threads.get(i).join();
            int threadMax = threads.get(i).getMax();
            if (threadMax > max) {
                max = threadMax;
            }
            threadTimes[i] = threads.get(i).getElapsedTime();
        }

        System.out.println("Maximum number: " + max);
        for (int i = 0; i < numThreads; i++) {
            System.out.println("Thread " + i + " time: " + threadTimes[i] + " ns");
        }
        System.out.println("==================================================================");

    }

    public static class SublistMaxFinder extends Thread {
        private List<Integer> list;
        private int max = Integer.MIN_VALUE;
        private long elapsedTime;

        public SublistMaxFinder(List<Integer> list) {
            this.list = list;
        }

        public int getMax() {
            return max;
        }

        public long getElapsedTime() {
            return elapsedTime;
        }

        public void run() {
            long startTime = System.nanoTime();
            for (int num : list) {
                if (num > max) {
                    max = num;
                }
            }
            long endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
        }
    }
}
