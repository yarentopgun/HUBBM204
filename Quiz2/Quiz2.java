import java.util.*;
import java.io.*;

public class Quiz2 {
    public static void main(String[] args) throws IOException {

        // Reading given input file
        String filename = args[0];
        FileInputStream file = new FileInputStream(filename);
        Scanner scanner = new Scanner(file);

        // Reading the first line, extracting M and n
        String[] firstLine = scanner.nextLine().split(" ");
        int M = Integer.parseInt(firstLine[0]);
        int n = Integer.parseInt(firstLine[1]);

        // Reading the second line, extracting the weights
        int[] weights = new int[n];

        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }


        scanner.close();
        L(M, weights);

    }


    /**
     * Determines whether a combination of the given weights can reach a specified maximum capacity.
     * It also prints the maximum capacity that can be reached with the given weights and outputs
     * a dynamic programming table showing which weights contribute to the total capacity.
     *
     * @param m The maximum capacity value.
     * @param weights An array of weights.
     * @return Returns true if the given set of weights can reach the maximum capacity, false otherwise.
     */
    public static boolean L(int m, int[] weights){

        int n = weights.length;
        boolean[][] dp = new boolean[m+1][n+1];

        // Initialize base cases
        for (int i = 0; i <= n; i++) {
            dp[0][i] = true;
        }
        for (int i = 1; i <= m; i++) {
            dp[i][0] = false;
        }

        // Fill the DP table using the given weights
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = dp[i][j-1];
                if (i >= weights[j-1]) {
                    dp[i][j] = dp[i][j] || dp[i-weights[j-1]][j-1];
                }
            }
        }


        // Calculate and print the maximum achievable capacity
        int maxCapacity = 0;
        for (int i = m; i >= 0; i--) {
            if (dp[i][n]) {
                maxCapacity = i;
                break;
            }
        }
        System.out.println(maxCapacity);

        // Print the DP table
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                System.out.print(dp[i][j] ? "1" : "0");
            }
            System.out.println();
        }

        return dp[m][n];
    }
}