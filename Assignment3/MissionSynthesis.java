import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    private List<Molecule> humanMolecules = new ArrayList<>();
    private List<Molecule> vitalesMolecules = new ArrayList<>();
    public void addHumanMolecule(Molecule molecule) {
        humanMolecules.add(molecule);
    }

    public void addVitalesMolecule(Molecule molecule) {
        vitalesMolecules.add(molecule);
    }


    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();

        for (MolecularStructure structure : humanStructures){
            Molecule weakestMolecule = structure.getMoleculeWithWeakestBondStrength();
            humanMolecules.add(weakestMolecule);
        }

        for (MolecularStructure structure : diffStructures){
            Molecule weakestMolecule = structure.getMoleculeWithWeakestBondStrength();
            vitalesMolecules.add(weakestMolecule);
        }

        /* YOUR CODE HERE */
        PriorityQueue<Bond> bondQueue = new PriorityQueue<>(Comparator.comparingDouble(Bond::getWeight));
        UnionFind uf = new UnionFind();

        // Add all molecules to the union-find structure
        humanMolecules.forEach(uf::add);
        vitalesMolecules.forEach(uf::add);

        // Only create bonds between human and Vitales molecules
        for (Molecule human : humanMolecules) {
            for (Molecule vitales : vitalesMolecules) {
                double strength = (human.getBondStrength() + vitales.getBondStrength()) / 2.0;
                bondQueue.add(new Bond(human, vitales, strength));
            }
        }

        // Kruskal's algorithm to find MST
        while (!bondQueue.isEmpty() && serum.size() < humanMolecules.size() + vitalesMolecules.size() - 1) {
            Bond bond = bondQueue.poll();
            String root1 = uf.find(bond.getFrom().getId());
            String root2 = uf.find(bond.getTo().getId());
            if (!root1.equals(root2)) {
                serum.add(bond);
                uf.union(root1, root2);
            }
        }
        return serum;
    }

    class UnionFind {
        private Map<String, String> parent = new HashMap<>();

        public void add(Molecule molecule) {
            parent.put(molecule.getId(), molecule.getId());
        }

        public String find(String id) {
            if (!parent.get(id).equals(id)) {
                parent.put(id, find(parent.get(id)));
            }
            return parent.get(id);
        }

        public void union(String id1, String id2) {
            String root1 = find(id1);
            String root2 = find(id2);
            if (!root1.equals(root2)) {
                parent.put(root1, root2);
            }
        }
    }


    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {

        /* YOUR CODE HERE */
        System.out.print("Typical human molecules selected for synthesis: [");
        for(int i=0; i<humanMolecules.size(); i++){
            System.out.print(humanMolecules.get(i).getId());
            if(i < humanMolecules.size() - 1){
                System.out.print(", ");
            }
        }
        System.out.println("]");

        System.out.print("Vitales molecules selected for synthesis: [");
        for(int i=0; i<vitalesMolecules.size(); i++){
            System.out.print(vitalesMolecules.get(i).getId());
            if(i < vitalesMolecules.size() - 1){
                System.out.print(", ");
            }
        }
        System.out.println("]");

        System.out.println("Synthesizing the serum...");

        double totalStrength = 0.0;

        for (Bond bond : serum) {
            System.out.println("Forming a bond between " +bond+ " with strength " + String.format("%.2f", bond.getWeight()));
            totalStrength += bond.getWeight();
        }

        System.out.println("The total serum bond strength is " + String.format("%.2f", totalStrength));

    }
}
