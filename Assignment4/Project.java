import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        int projectDuration;
        int[] schedule = getEarliestSchedule();
        projectDuration = tasks.get(schedule.length-1).getDuration() + schedule[schedule.length-1];
        return projectDuration;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {
        int numTasks = tasks.size();
        int[] earliestStart = new int[numTasks];
        Arrays.fill(earliestStart, -1); // Use -1 to indicate unprocessed tasks
        earliestStart[0] = 0; // Task 0 starts immediately

        // Create adjacency list and in-degrees tracking for tasks
        List<List<Integer>> adjList = new ArrayList<>();
        int[] inDegree = new int[numTasks];
        for (int i = 0; i < numTasks; i++) {
            adjList.add(new ArrayList<>());
        }

        for (Task task : tasks) {
            for (Integer dep : task.getDependencies()) {
                adjList.get(dep).add(task.getTaskID());
                inDegree[task.getTaskID()]++;
            }
        }

        // Queue for tasks ready to be processed (i.e., no pending dependencies)
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numTasks; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        // Process the queue
        while (!queue.isEmpty()) {
            int taskId = queue.poll();
            Task currentTask = tasks.get(taskId);
            int currentTaskDuration = currentTask.getDuration();
            int taskFinishTime = earliestStart[taskId] + currentTaskDuration;

            for (int dependentId : adjList.get(taskId)) {
                // Update the earliest start time for dependencies
                if (earliestStart[dependentId] == -1 || earliestStart[dependentId] < taskFinishTime) {
                    earliestStart[dependentId] = taskFinishTime;
                }
                inDegree[dependentId]--;

                if (inDegree[dependentId] == 0) {
                    queue.add(dependentId);
                }
            }
        }

        // Check for any tasks that never got processed
        for (int i = 0; i < numTasks; i++) {
            if (inDegree[i] != 0) {
                throw new IllegalStateException("Circular dependency found in tasks. Task ID: " + i);
            }
            if (earliestStart[i] == -1) {
                throw new IllegalStateException("Some tasks were not scheduled properly, check dependencies. Task ID: " + i);
            }
        }

        return earliestStart;
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }


    /**
     * Print the project schedule in a tabular format.
     *
     * @param schedule An integer array consisting of the earliest start days for each task.
     */
    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.printf("Project name: %s%n", name);
        printlnDash(limit, symbol);

        // Print header
        System.out.printf("%-10s%-45s%-7s%-5s%n","Task ID","Description","Start","End");
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.printf("%-10d%-45s%-7d%-5d%n", i, t.getDescription(), schedule[i], schedule[i]+t.getDuration());
        }
        printlnDash(limit, symbol);
        System.out.printf("Project will be completed in %d days.%n", getProjectDuration());
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

}
