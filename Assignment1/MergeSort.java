
public class MergeSort {

    public static final double[] averageTimesRandom = new double[10];
    public static final double[] averageTimesSorted = new double[10];
    public static final double[] averageTimesReversed = new double[10];
    public static void mergeSort(int[] arr){
        if(arr.length <= 1){
            return;
        }
        int middle = arr.length / 2;
        int[] left = new int[middle];
        int[] right = new int[arr.length - middle];

        for(int i = 0; i < middle; i++){
            left[i] = arr[i];
        }
        for(int i = middle; i < arr.length; i++){
            right[i - middle] = arr[i];
        }

        mergeSort(left);
        mergeSort(right);
        merge(arr, left, right);
    }

    protected static void merge(int[] arr, int[] left, int[] right){
        int i = 0; int j = 0; int k = 0;

        while(i < left.length && j < right.length){
            if(left[i] <= right[j]){
                arr[k++] = left[i++];
            }
            else {
                arr[k++] = right[j++];
            }
        }
        while(i < left.length){
            arr[k++] = left[i++];
        }
        while(j < right.length){
            arr[k++] = right[j++];
        }
    }
}
