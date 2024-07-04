import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        // MISSION POWER GRID OPTIMIZATION BELOW

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");

        String file = args[0];
        List<String> lines = Files.readAllLines(Paths.get(file));

        String[] demands = lines.get(0).split("\\s+");
        ArrayList<Integer> energyDemands = new ArrayList<>();
        for (String demand : demands) {
            try {
                energyDemands.add(Integer.parseInt(demand));
            } catch (NumberFormatException e) {
                System.out.println("Error parsing the energy demand data: " + e.getMessage());
                return;
            }
        }

        int totalEnergyDemands = 0;
        for (String i : demands) {
            totalEnergyDemands += Integer.parseInt(i);
        }


        PowerGridOptimization powerGridOptimization = new PowerGridOptimization(energyDemands);

        OptimalPowerGridSolution optimalSolution = powerGridOptimization.getOptimalPowerGridSolutionDP();


        System.out.println("The total number of demanded gigawatts: " + totalEnergyDemands);
        System.out.println("Maximum number of satisfied gigawatts: " + optimalSolution.getmaxNumberOfSatisfiedDemands());
        System.out.print("Hours at which the battery bank should be discharged: ");
        for (int i = 0; i < optimalSolution.getHoursToDischargeBatteriesForMaxEfficiency().size(); i++) {
            System.out.print(optimalSolution.getHoursToDischargeBatteriesForMaxEfficiency().get(i));
            if (i < optimalSolution.getHoursToDischargeBatteriesForMaxEfficiency().size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("\nThe number of unsatisfied gigawatts: " + (totalEnergyDemands - optimalSolution.getmaxNumberOfSatisfiedDemands()));

        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");


        // MISSION ECO-MAINTENANCE BELOW

        System.out.println("##MISSION ECO-MAINTENANCE##");

        String file2 = args[1];

        try {
            Scanner scanner = new Scanner(Paths.get(file2));

            // Read the first line: the number of available ESVs and the capacity of each ESV

            int maxNumberOfAvailableESVs = scanner.nextInt();
            int ESV_CAPACITY = scanner.nextInt();
            scanner.nextLine();

            // Read the second line: energy demands of maintenance tasks

            ArrayList<Integer> maintenanceTaskEnergyDemands = new ArrayList<>();
            String[] energyDemands2 = scanner.nextLine().trim().split(" ");
            for (String demand : energyDemands2) {
                maintenanceTaskEnergyDemands.add(Integer.parseInt(demand));
            }

            scanner.close();


            OptimalESVDeploymentGP deployment = new OptimalESVDeploymentGP(maintenanceTaskEnergyDemands);
            int minESVsNeeded = deployment.getMinNumESVsToDeploy(maxNumberOfAvailableESVs, ESV_CAPACITY);


            if (minESVsNeeded != -1) {
                System.out.println("The minimum number of ESVs to deploy: " + minESVsNeeded);
                for (int i = 0; i < deployment.getMaintenanceTasksAssignedToESVs().size(); i++) {
                    if (!deployment.getMaintenanceTasksAssignedToESVs().get(i).isEmpty()) {
                        System.out.println("ESV " + (i + 1) + " tasks: " +
                                deployment.getMaintenanceTasksAssignedToESVs().get(i));
                    }
                }
            } else {
                System.out.println("Warning: Mission Eco-Maintenance Failed.");
            }
            System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
        } catch (Exception e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }
}