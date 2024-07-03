
public class InsertionSort {

    public static final double[] averageTimesRandom = new double[10];
    public static final double[] averageTimesSorted = new double[10];
    public static final double[] averageTimesReversed = new double[10];

    public static void insertionSort(int[] arr) {
        for (int  j= 1; j < arr.length; j++) {
            int key = arr[j];
            int i = j - 1;

            while (i >= 0 && arr[i] > key) {
                arr[i + 1] = arr[i];
                i = i - 1;
            }
            arr[i + 1] = key;
        }
    }

}
