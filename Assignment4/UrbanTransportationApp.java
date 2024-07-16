import java.io.Serializable;
import java.util.*;


class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;
    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {

        Station start = network.startPoint;
        Station destination = network.destinationPoint;

        if (start == null || destination == null || network.lines == null) {
            throw new IllegalStateException("Start, destination, or train lines are not properly initialized");
        }

        PriorityQueue<Node> toExplore = new PriorityQueue<>(Comparator.comparingDouble(node -> node.time));
        toExplore.add(new Node(start, 0, null));

        Map<Station, Node> shortestPaths = new HashMap<>();
        shortestPaths.put(start, new Node(start, 0, null));

        while (!toExplore.isEmpty()) {
            Node current = toExplore.poll();
            if (current.station.equals(destination)) {
                return constructPath(network, current);
            }

            // Consider train rides first
            for (TrainLine line : network.lines) {
                if (line.trainLineStations.contains(current.station)) {
                    int currentIndex = line.trainLineStations.indexOf(current.station);
                    // Move forward in the line
                    if (currentIndex + 1 < line.trainLineStations.size()) {
                        Station nextStation = line.trainLineStations.get(currentIndex + 1);
                        calculateTravel(network, (PriorityQueue<Node>) toExplore, (Map<Station, Node>) shortestPaths, current, nextStation);
                    }
                    // Move backward in the line
                    if (currentIndex - 1 >= 0) {
                        Station nextStation = line.trainLineStations.get(currentIndex - 1);
                        calculateTravel(network, (PriorityQueue<Node>) toExplore, (Map<Station, Node>) shortestPaths, current, nextStation);
                    }
                }
            }

            // Then consider walking to all other stations
            for (TrainLine line : network.lines) {
                for (Station nextStation : line.trainLineStations) {
                    if (!current.station.equals(nextStation)) {
                        double walkTime = calculateWalkingTime(current.station.coordinates, nextStation.coordinates);
                        addShortestPath((PriorityQueue<Node>) toExplore, (Map<Station, Node>) shortestPaths, current, nextStation, walkTime);
                    }
                }
            }

            // Finally consider walking directly to the destination
            double walkTime = calculateWalkingTime(current.station.coordinates, destination.coordinates);
            if (!shortestPaths.containsKey(destination) || current.time + walkTime < shortestPaths.get(destination).time) {
                toExplore.add(new Node(destination, current.time + walkTime, current));
                shortestPaths.put(destination, new Node(destination, current.time + walkTime, current));
            }
        }

        return Collections.emptyList();
    }

    private void calculateTravel(HyperloopTrainNetwork network, PriorityQueue<Node> toExplore, Map<Station, Node> shortestPaths, Node current, Station nextStation) {
        double travelTime = calculateTrainTime(current.station.coordinates, nextStation.coordinates, network.averageTrainSpeed);
        addShortestPath((PriorityQueue<Node>) toExplore, (Map<Station, Node>) shortestPaths, current, nextStation, travelTime);
    }

    private void addShortestPath(PriorityQueue<Node> toExplore, Map<Station, Node> shortestPaths, Node current, Station nextStation, double travelTime) {
        double newTime = current.time + travelTime;
        if (!shortestPaths.containsKey(nextStation) || newTime < shortestPaths.get(nextStation).time) {
            Node newNode = new Node(nextStation, newTime, current);
            toExplore.add(newNode);
            shortestPaths.put(nextStation, newNode);
        }
    }


    private double calculateTrainTime(Point a, Point b, double trainSpeed) {
        double distance = Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
        return (distance ) / (trainSpeed );
    }

    private double calculateWalkingTime(Point a, Point b) {
        double distance = Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
        return (distance ) / (HyperloopTrainNetwork.averageWalkingSpeed);
    }

    private List<RouteDirection> constructPath(HyperloopTrainNetwork network, Node endNode) {
        List<RouteDirection> path = new ArrayList<>();
        Node current = endNode;

        while (current.previous != null) {
            boolean trainRide = false;
            for (TrainLine line : network.lines) {
                if (line.trainLineStations.contains(current.station) && line.trainLineStations.contains(current.previous.station)) {
                    trainRide = true;
                    break;
                }
            }
            double duration = trainRide ?
                    calculateTrainTime(current.previous.station.coordinates, current.station.coordinates, network.averageTrainSpeed) :
                    calculateWalkingTime(current.previous.station.coordinates, current.station.coordinates);
            path.add(0, new RouteDirection(current.previous.station.description, current.station.description, duration, trainRide));
            current = current.previous;
        }

        return path;
    }


    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {

        double totalTime = directions.stream().mapToDouble(d -> d.duration).sum();
        System.out.println("The fastest route takes " + Math.round(totalTime) + " minute(s).");
        System.out.println("Directions");
        System.out.println("----------");
        for (int i = 0; i < directions.size(); i++) {
            RouteDirection direction = directions.get(i);
            String startStation = direction.startStationName;
            String endStation = direction.endStationName;
            String mode = direction.trainRide ? "Get on the train" : "Walk";
            System.out.printf("%d. %s from \"%s\" to \"%s\" for %.2f minutes.%n", i + 1, mode, startStation, endStation, direction.duration);
        }
    }

    private static class Node {
        Station station;
        double time;
        Node previous;

        Node(Station station, double time, Node previous) {
            this.station = station;
            this.time = time;
            this.previous = previous;
        }
    }
}