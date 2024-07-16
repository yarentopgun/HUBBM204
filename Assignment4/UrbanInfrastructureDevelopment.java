import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {

        for (Project project : projectList) {
            int[] schedule = project.getEarliestSchedule();  // Calculate the schedule for each project
            project.printSchedule(schedule);  // Print the project schedule using the provided format
        }
    }

    /**
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();

        try {
            File inputFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Project");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String projectName = eElement.getElementsByTagName("Name").item(0).getTextContent();
                    NodeList taskList = eElement.getElementsByTagName("Task");
                    List<Task> tasks = new ArrayList<>();
                    for (int count = 0; count < taskList.getLength(); count++) {
                        Node tNode = taskList.item(count);
                        if (tNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element tElement = (Element) tNode;
                            int taskId = Integer.parseInt(tElement.getElementsByTagName("TaskID").item(0).getTextContent());
                            String description = tElement.getElementsByTagName("Description").item(0).getTextContent();
                            int duration = Integer.parseInt(tElement.getElementsByTagName("Duration").item(0).getTextContent());
                            NodeList depList = tElement.getElementsByTagName("DependsOnTaskID");
                            List<Integer> dependencies = new ArrayList<>();
                            for (int d = 0; d < depList.getLength(); d++) {
                                dependencies.add(Integer.parseInt(depList.item(d).getTextContent()));
                            }
                            tasks.add(new Task(taskId, description, duration, dependencies));
                        }
                    }
                    projectList.add(new Project(projectName, tasks));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }
}
