
public class LinearSearch {

    public static final double[] averageTimesRandom = new double[10];
    public static final double[] averageTimesSorted = new double[10];
    public static int linearSearch(int[] A, int x){
        int size = A.length;
        for(int i=0; i< size; i++){
            if(A[i] == x){
                return i;
            }
        }
        return -1;
    }
}
