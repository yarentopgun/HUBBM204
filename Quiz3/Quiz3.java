import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Quiz3 {

    public int vertices;
    public List<Edge> edges;

    public int src, dest;
    public double weight;

    static class Edge extends Quiz3 implements Comparable<Edge> {

        Edge(int src, int dest, double weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        public int compareTo(Edge compareEdge) {
            return Double.compare(this.weight, compareEdge.weight);
        }
        public String toString() {
            return String.valueOf(weight);
        }
    }

    public static class Graph extends Quiz3 {

        Graph(int vertices) {
            this.vertices = vertices;
            edges = new ArrayList<>();
        }
    }

    void addEdge(int src, int dest, double weight) {
        Edge edge = new Edge(src, dest, weight);
        edges.add(edge);
    }

    double calculateDistance(int[] p1, int[] p2) {
        return Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2));
    }

    ArrayList<Edge> KruskalMST() {
        // Sort edges by weight
        Collections.sort(edges);

        // Create subsets for union-find
        int[] parent = new int[vertices];
        int[] rank = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            parent[i] = i;
            rank[i] = 0;
        }

        int e = 0;  // Initially 0 edges in MST

        ArrayList<Edge> result = new ArrayList<>(); // This will store the resultant MST

        int i = 0; // Index variable, used for sorted edges
        while (e < vertices - 1) {
            Edge next_edge = edges.get(i++);

            int x = find(parent, next_edge.src);
            int y = find(parent, next_edge.dest);

            if (x != y) {
                result.add(next_edge);
                e++;
                union(parent, rank, x, y);
            }
        }

        // Reverse the result to get edges sorted in descending order
        Collections.reverse(result);
        return result;
        /*
        // Printing the MST
        for (Edge edge : result) {
            System.out.println(edge.src + " - " + edge.dest + ": " + edge.weight);
        }

         */
    }

    int find(int[] parent, int i) {
        if (parent[i] == i)
            return i;
        return find(parent, parent[i]);
    }

    void union(int[] parent, int[] rank, int x, int y) {
        int xroot = find(parent, x);
        int yroot = find(parent, y);

        if (rank[xroot] < rank[yroot])
            parent[xroot] = yroot;
        else if (rank[xroot] > rank[yroot])
            parent[yroot] = xroot;
        else {
            parent[yroot] = xroot;
            rank[xroot]++;
        }
    }

    public static void main(String[] args) throws IOException {

        Locale.setDefault(Locale.US);

        List<String> lines = new ArrayList<>();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        while ((line = reader.readLine()) != null) { // Dosya sonuna kadar oku
            lines.add(line); // Her satırı ArrayList'e ekle
        }

        int numTests = Integer.parseInt(lines.get(0).trim());
        lines.remove(0);

        for(int i = 0; i < numTests; i++){
            String[] params = lines.get(0).trim().split(" ");
            int S = Integer.parseInt(params[0]);
            int P = Integer.parseInt(params[1]);

            lines.remove(0);
            int[][] coordinates = new int[P][2];

            for (int j = 0; j < P; j++) {

                String[] c = lines.get(0).trim().split(" ");
                int x = Integer.parseInt(c[0]);
                int y = Integer.parseInt(c[1]);
                coordinates[j][0] = x;
                coordinates[j][1] = y;

                lines.remove(0);

            }
            Graph g = new Graph(P);

            for (int a = 0; a < coordinates.length; a++) {
                for (int b = a + 1; b < coordinates.length; b++) {
                    double dist = g.calculateDistance(coordinates[a], coordinates[b]);
                    g.addEdge(a, b, dist);
                }
            }

            ArrayList<Edge> result = new ArrayList<>();
            result  = g.KruskalMST();

            // Printing the MST
            //System.out.println(result.get(S-1));
            double sonuc = result.get(S-1).weight;

            System.out.printf("%.2f\n",sonuc);
        }

        /*
        String[] params = reader.readLine().trim().split(" ");

        int S = Integer.parseInt(params[0]);
        int P = Integer.parseInt(params[1]);

        int[][] coordinates = new int[P][2];

        for(int j=0 ; j<P; j++){
            String[] c = reader.readLine().trim().split(" ");
            coordinates[j][0] = Integer.parseInt(c[0]);
            coordinates[j][1] = Integer.parseInt(c[1]);
        }

        for (int t = 0; t < numTests; t++) {

            Graph g = new Graph(P);

            for (int i = 0; i < coordinates.length; i++) {
                for (int j = i + 1; j < coordinates.length; j++) {
                    double dist = g.calculateDistance(coordinates[i], coordinates[j]);
                    g.addEdge(i, j, dist);
                }
            }

            ArrayList<Edge> result = g.KruskalMST();

            // Printing the MST
            //System.out.println(result.get(S-1));
            double sonuc = result.get(S-1).weight;

            System.out.printf("%.2f\n",sonuc);
        }
    reader.close();

         */
    }
}
