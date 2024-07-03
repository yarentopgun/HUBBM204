import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class DatasetProcessor {

    public static final int[][] sorted_subsets = new int[10][];
    public static final int[][] reversed_subsets = new int[10][];

    // Read flow durations from the given file
    public static List<Integer> readFlowDurations(String filePath) {
        List<Integer> flowDurations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                flowDurations.add(Integer.parseInt(values[6]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flowDurations;
    }

    // Create subsets of the given data
    public static int[][] createSubsets(List<Integer> data, int[] sizes) {
        int[][] subsets = new int[sizes.length][];

        for (int i = 0; i < sizes.length; i++) {
            int size = sizes[i];
            subsets[i] = new int[size];

            for (int j = 0; j < size; j++) {
                subsets[i][j] = data.get(j);
            }
        }

        return subsets;
    }

    // Performance measurement methods for InsertionSort, MergeSort and CountingSort with random data
    public static void InsertionSortWithRandomData(int[][] subsets) {

        System.out.println("INSERTION SORT WITH RANDOM DATA");

        int iterations = 0;

        for (int a=0; a<10; a++) {
            long totalTime = 0;
            int[] tempSubset = Arrays.copyOf(subsets[a], subsets[a].length);

            for (int i = 0; i < 10; i++) {

                long startTime = System.nanoTime();
                InsertionSort.insertionSort(tempSubset);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);

            }
            sorted_subsets[a] = new int[tempSubset.length];
            sorted_subsets[a] = Arrays.copyOf(tempSubset, tempSubset.length);

            double averageTime = (totalTime / 10.0) / 1_000_000.0;
            InsertionSort.averageTimesRandom[iterations] = averageTime;
            iterations++;

            System.out.printf("Average Time with %d input: %.0fms%n", subsets[a].length, averageTime);
        }
    }
    public static void MergeSortWithRandomData(int[][] subsets) {

        System.out.println("\nMERGE SORT WITH RANDOM DATA");

        int iterations = 0;

        for (int[] subset : subsets) {
            long totalTime = 0;
            int[] tempSubset = Arrays.copyOf(subset, subset.length);

            for (int i = 0; i < 10; i++) {

                long startTime = System.nanoTime();
                MergeSort.mergeSort(tempSubset);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = (totalTime / 10.0) / 1_000_000.0;
            MergeSort.averageTimesRandom[iterations] = averageTime;
            iterations++;

            System.out.printf("Average Time with %d input: %.0fms%n", subset.length, averageTime);
        }
    }

    public static void CountingSortWithRandomData(int[][] subsets) {


        System.out.println("\nCOUNTING SORT WITH RANDOM DATA");

        int iterations = 0;
        for (int[] subset : subsets) {
            long totalTime = 0;
            int[] tempSubset = Arrays.copyOf(subset, subset.length);
            for (int i = 0; i < 10; i++) {

                long startTime = System.nanoTime();
                CountingSort.countingSort(tempSubset);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = (totalTime / 10.0) / 1_000_000.0;
            CountingSort.averageTimesRandom[iterations] = averageTime;
            iterations++;

            System.out.printf("Average Time with %d input: %.0fms%n", subset.length, averageTime);
        }
    }

    // Performance measurement methods for InsertionSort, MergeSort and CountingSort with sorted data

    public static void InsertionSortWithSortedData() {

        System.out.println("\nINSERTION SORT WITH SORTED DATA");

        int iterations = 0;
        for (int[] subset : sorted_subsets) {
            long totalTime = 0;
            int[] tempSubset = Arrays.copyOf(subset, subset.length);

            for (int i = 0; i < 10; i++) {
                long startTime = System.nanoTime();
                InsertionSort.insertionSort(tempSubset);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = (totalTime / 10.0) / 1_000_000.0;
            InsertionSort.averageTimesSorted[iterations] = averageTime;
            iterations++;

            System.out.printf("Average Time with %d input: %.0fms%n", subset.length, averageTime);
        }
    }
    public static void MergeSortWithSortedData() {

        System.out.println("\nMERGE SORT WITH SORTED DATA");

        int iterations = 0;
        for (int[] subset : sorted_subsets) {
            long totalTime = 0;
            int[] tempSubset = Arrays.copyOf(subset, subset.length);

            for (int i = 0; i < 10; i++) {
                long startTime = System.nanoTime();
                MergeSort.mergeSort(tempSubset);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = (totalTime / 10.0) / 1_000_000.0;
            MergeSort.averageTimesSorted[iterations] = averageTime;
            iterations++;

            System.out.printf("Average Time with %d input: %.0fms%n", subset.length, averageTime);
        }
    }

    public static void CountingSortWithSortedData() {

        System.out.println("\nCOUNTING SORT WITH SORTED DATA");

        int iterations = 0;
        for (int[] subset : sorted_subsets) {
            long totalTime = 0;
            int[] tempSubset = Arrays.copyOf(subset, subset.length);

            for (int i = 0; i < 10; i++) {

                long startTime = System.nanoTime();
                CountingSort.countingSort(tempSubset);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = (totalTime / 10.0) / 1_000_000.0;
            CountingSort.averageTimesSorted[iterations] = averageTime;
            iterations++;

            System.out.printf("Average Time with %d input: %.0fms%n", subset.length, averageTime);
        }
    }


    // Performance measurement methods for InsertionSort, MergeSort and CountingSort with reversed data
    public static void InsertionSortWithReversedData() {

        System.out.println("\nINSERTION SORT WITH REVERSE SORTED DATA");

        int iterations = 0;

        for (int s=0; s<10; s++) {
            long totalTime = 0;
            int[] tempSubset = Arrays.copyOf(sorted_subsets[s], sorted_subsets[s].length);
            int temp;

            for(int a=0 ; a<tempSubset.length/2 ; a++){
                temp = tempSubset[a];
                tempSubset[a] = tempSubset[tempSubset.length-1-a];
                tempSubset[tempSubset.length-1-a] = temp;
            }
            reversed_subsets[s] = new int[tempSubset.length];
            reversed_subsets[s] = Arrays.copyOf(tempSubset, tempSubset.length);

            for (int i = 0; i < 10; i++) {

                long startTime = System.nanoTime();
                InsertionSort.insertionSort(tempSubset);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = (totalTime / 10.0) / 1_000_000.0;
            InsertionSort.averageTimesReversed[iterations] = averageTime;
            iterations++;

            System.out.printf("Average Time with %d input: %.0fms%n", sorted_subsets[s].length, averageTime);
        }
    }

    public static void MergeSortWithReversedData() {

        System.out.println("\nMERGE SORT WITH REVERSE SORTED DATA");

        int iterations = 0;
        for (int[] subset : reversed_subsets) {
            long totalTime = 0;
            int[] tempSubset = Arrays.copyOf(subset, subset.length);

            for (int i = 0; i < 10; i++) {

                long startTime = System.nanoTime();
                MergeSort.mergeSort(tempSubset);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = (totalTime / 10.0) / 1_000_000.0;
            MergeSort.averageTimesReversed[iterations] = averageTime;
            iterations++;
            System.out.printf("Average Time with %d input: %.0fms%n", subset.length, averageTime);
        }
    }

    public static void CountingSortWithReversedData() {

        System.out.println("\nCOUNTING SORT WITH REVERSE SORTED DATA");

        int iterations = 0;
        for (int[] subset : reversed_subsets) {

            long totalTime = 0;
            int[] tempSubset = Arrays.copyOf(subset, subset.length);

            for (int i = 0; i < 10; i++) {

                long startTime = System.nanoTime();
                CountingSort.countingSort(tempSubset);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = (totalTime / 10.0) / 1_000_000.0;
            CountingSort.averageTimesReversed[iterations] = averageTime;
            iterations++;
            System.out.printf("Average Time with %d input: %.0fms%n", subset.length, averageTime);
        }
    }

    // Performance measurement methods for LinearSearch and BinarySearch with random and sorted data
    public static void LinearSearchWithUnsorted(int[][] subsets) {

        System.out.println("\nLINEAR SEARCH WITH RANDOM DATA");

        Random random = new Random();

        int iterations = 0;
        for (int[] subset : subsets) {
            long totalTime = 0;

            for (int i = 0; i < 1000; i++) {
                int randomValue = subset[random.nextInt(subset.length)];
                long startTime = System.nanoTime();
                LinearSearch.linearSearch(subset, randomValue);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = totalTime / 100.0;
            LinearSearch.averageTimesRandom[iterations] = averageTime;
            iterations++;
            System.out.printf("Average Time with %d input for Linear Search: %.0fns%n", subset.length, averageTime);
        }
    }

    public static void LinearSearchWithSorted() {

        System.out.println("\nLINEAR SEARCH WITH SORTED DATA");

        Random random = new Random();

        int iterations = 0;
        for (int[] subset : sorted_subsets) {
            long totalTime = 0;

            for (int i = 0; i < 1000; i++) {
                int randomValue = subset[random.nextInt(subset.length)];
                long startTime = System.nanoTime();
                LinearSearch.linearSearch(subset, randomValue);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = totalTime / 100.0;
            LinearSearch.averageTimesSorted[iterations] = averageTime;
            iterations++;
            System.out.printf("Average Time with %d input for Linear Search: %.0fns%n", subset.length, averageTime);
        }
    }

    public static void BinarySearchWithSorted() {

        System.out.println("\nBINARY SEARCH WITH SORTED DATA");

        Random random = new Random();

        int iterations = 0;
        for (int[] subset : sorted_subsets) {

            long totalTime = 0;

            for (int i = 0; i < 1000; i++) {
                int randomValue = subset[random.nextInt(subset.length)];
                long startTime = System.nanoTime();
                BinarySearch.binarySearch(subset, randomValue);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double averageTime = (double)totalTime / 100.0;
            BinarySearch.averageTimesSorted[iterations] = averageTime;
            iterations++;
            System.out.printf("Average Time with %d input for Binary Search: %.0fns%n", subset.length, averageTime);
        }
    }

}
