import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public static final double averageWalkingSpeed = 1000 / 6.0;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {

        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        throw new IllegalStateException("No match found for variable: " + varName);

    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {

        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"([^\"]+)\"");
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            return m.group(1);
        }
        throw new IllegalStateException("No match found for variable: " + varName);
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {

        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]*\\.?[0-9]+)");
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        }
        throw new IllegalStateException("No match found for variable: " + varName);
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {

        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\\([\\t ]*([0-9]+)[\\t ]*,[\\t ]*([0-9]+)[\\t ]*\\)");
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            return new Point(x, y);
        }
        throw new IllegalStateException("No match found for variable: " + varName);
    }



    /**
     * Function to extract the train lines from the fileContent by reading train line names and their 
     * respective stations.
     * @return List of TrainLine instances
     */
    public List<TrainLine> getTrainLines(String fileContent) {

        List<TrainLine> trainLines = new ArrayList<>();
        Pattern linePattern = Pattern.compile("train_line_name\\s*=\\s*\"([^\"]+)\"");
        Pattern stationPattern = Pattern.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");

        Matcher lineMatcher = linePattern.matcher(fileContent);

        while (lineMatcher.find()) {
            String lineName = lineMatcher.group(1);

            List<Station> stations = new ArrayList<>();

            // Find the segment of the file content relevant to the current line
            int start = lineMatcher.end();
            int end = fileContent.indexOf("train_line_name", start);
            if (end == -1) {
                end = fileContent.length();
            }

            String lineSegment = fileContent.substring(start, end);


            Matcher stationMatcher = stationPattern.matcher(lineSegment);
            while (stationMatcher.find()) {
                int x = Integer.parseInt(stationMatcher.group(1));
                int y = Integer.parseInt(stationMatcher.group(2));
                stations.add(new Station(new Point(x, y), lineName + " Line Station " + (stations.size() + 1)));

            }

            trainLines.add(new TrainLine(lineName, stations));

        }
        return trainLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {

        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filename)));

            this.numTrainLines = getIntVar("num_train_lines", fileContent);

            Point startPointCoordinates = getPointVar("starting_point", fileContent);
            this.startPoint = new Station(startPointCoordinates, "Starting Point");

            Point destinationPointCoordinates = getPointVar("destination_point", fileContent);
            this.destinationPoint = new Station(destinationPointCoordinates, "Final Destination");

            this.averageTrainSpeed = (getDoubleVar("average_train_speed", fileContent)) * 1000 / 60.0;

            this.lines = getTrainLines(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}