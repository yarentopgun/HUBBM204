import java.util.ArrayList;
import java.util.Collections;

/**
 * This class accomplishes Mission Eco-Maintenance
 */
public class OptimalESVDeploymentGP
{
    private final ArrayList<Integer> maintenanceTaskEnergyDemands;

    /*
     * Should include tasks assigned to ESVs.
     * For the sample input:
     * 8 100
     * 20 50 40 70 10 30 80 100 10
     *
     * The list should look like this:
     * [[100], [80, 20], [70, 30], [50, 40, 10], [10]]
     *
     * It is expected to be filled after getMinNumESVsToDeploy() is called.
     */
    private final ArrayList<ArrayList<Integer>> maintenanceTasksAssignedToESVs = new ArrayList<>();

    ArrayList<ArrayList<Integer>> getMaintenanceTasksAssignedToESVs() {

        return maintenanceTasksAssignedToESVs;
    }

    public OptimalESVDeploymentGP(ArrayList<Integer> maintenanceTaskEnergyDemands) {
        this.maintenanceTaskEnergyDemands = maintenanceTaskEnergyDemands;
    }

    public ArrayList<Integer> getMaintenanceTaskEnergyDemands() {

        return maintenanceTaskEnergyDemands;
    }


    /**
     *
     * @param maxNumberOfAvailableESVs the maximum number of available ESVs to be deployed
     * @param maxESVCapacity the maximum capacity of ESVs
     * @return the minimum number of ESVs required using first fit approach over reversely sorted items.
     * Must return -1 if all tasks can't be satisfied by the available ESVs
     */
    public int getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity)
    {
        maintenanceTaskEnergyDemands.sort(Collections.reverseOrder());

        ArrayList<Integer> esv = new ArrayList<>(Collections.nCopies(maxNumberOfAvailableESVs, maxESVCapacity));

        for (int task : maintenanceTaskEnergyDemands) {
            boolean bool = false;
            for (int i = 0; i < esv.size(); i++) {
                if (esv.get(i) >= task) {

                    esv.set(i, esv.get(i) - task);

                    while (maintenanceTasksAssignedToESVs.size() <= i) {
                        maintenanceTasksAssignedToESVs.add(new ArrayList<>());
                    }

                    maintenanceTasksAssignedToESVs.get(i).add(task);
                    bool = true;
                    break;
                }
            }
            // If no ESV can accommodate the task, return -1 indicating failure.
            if (!bool) return -1;
        }

        // Calculate the minimum number of ESVs used
        int minNumESVsUsed = 0;
        for (ArrayList<Integer> e : maintenanceTasksAssignedToESVs) {
            if (!e.isEmpty()) minNumESVsUsed++;
        }

        return minNumESVsUsed;
    }

}
