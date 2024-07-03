

public class CountingSort {

    public static final double[] averageTimesRandom = new double[10];
    public static final double[] averageTimesSorted = new double[10];
    public static final double[] averageTimesReversed = new double[10];
    public static  int[]  countingSort(int[] A){

        int k = 0;
        for (int value : A) {
            if (value > k) {
                k = value;
            }
        }

        int[] count = new int[k + 1];
        int[] output = new int[A.length];
        int size = A.length;

        for(int i = 0; i < k; i++){
            count[i] = 0;
        }

        for (int j : A) {
            count[j] = count[j] + 1;
        }

        for(int i=1; i<k+1; i++){
            count[i] = count[i] + count[i-1];
        }

        for(int i=size-1; i>=0; i--){
            int j = A[i];
            count[j] = count[j] - 1;
            output[count[j]] = A[i];

        }
        return output;
    }
}
