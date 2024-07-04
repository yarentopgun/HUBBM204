import java.util.ArrayList;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */

public class PowerGridOptimization {
    private final ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour){
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {

        return amountOfEnergyDemandsArrivingPerHour;
    }

    /**
     *     Function to implement the given dynamic programming algorithm
     *     SOL(0) <- 0
     *     HOURS(0) <- [ ]
     *     For{j <- 1...N}
     *         SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     *         HOURS(j) <- [HOURS(i), j]
     *     EndFor
     *
     * @return OptimalPowerGridSolution
     */
    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP(){

        int n = amountOfEnergyDemandsArrivingPerHour.size();
        int[] solution = new int[n + 1];
        ArrayList<ArrayList<Integer>> hours = new ArrayList<>();

        for (int i = 0; i <= n; i++) {
            hours.add(new ArrayList<>());
        }

        // Initialize base case
        solution[0] = 0; // At the beginning, no energy demands have been met.

        // Dynamic Programming to fill SOL
        for (int j = 1; j <= n; j++) {
            int maxEnergy = 0;
            int index = 0;
            for (int i = 0; i < j; i++) {

                int min = Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), (int)Math.pow(j - i, 2));
                int currentEnergy = solution[i] + min;

                if (currentEnergy > maxEnergy) {
                    maxEnergy = currentEnergy;
                    index = i;
                }
            }
            solution[j] = maxEnergy;
            hours.set(j, new ArrayList<>(hours.get(index)));
            hours.get(j).add(j); // Add the current hour to the list of hours for discharging
        }

        // Extract the solution
        ArrayList<Integer> optimalHoursForDischarge = new ArrayList<>(hours.get(n));
        int maxNumberOfSatisfiedDemands = solution[n];

        return new OptimalPowerGridSolution(maxNumberOfSatisfiedDemands, optimalHoursForDischarge);
    }
}
