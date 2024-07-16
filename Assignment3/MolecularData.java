import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        ArrayList<MolecularStructure> structures = new ArrayList<>();

        for (Molecule molecule : molecules) {
            if (!molecule.isVisited()) {
                MolecularStructure structure = findAssignedStructure(molecule, structures);

                if (structure == null) {
                    structure = new MolecularStructure();
                    structures.add(structure);
                }

                dfs(molecule, structure, structures);

            }
        }
        return structures;
    }

    private void dfs(Molecule currentMolecule, MolecularStructure currentStructure, ArrayList<MolecularStructure> structures) {
        if (currentMolecule.isVisited()) return;
        currentMolecule.setVisited(true);
        currentStructure.addMolecule(currentMolecule);

        for (String bondId : currentMolecule.getBonds()) {
            Molecule bondedMolecule = findMoleculeById(bondId);
            if (bondedMolecule != null) {
                MolecularStructure structureForBonded = findAssignedStructure(bondedMolecule, structures);
                if (structureForBonded == null) {
                    dfs(bondedMolecule, currentStructure, structures);
                } else if (structureForBonded != currentStructure) {
                    mergeStructures(structureForBonded, currentStructure, structures);
                }
            }
        }
    }

    private MolecularStructure findAssignedStructure(Molecule molecule, ArrayList<MolecularStructure> structures) {
        for (MolecularStructure structure : structures) {
            if (structure.hasMolecule(molecule.getId())) {
                return structure;
            }
        }
        return null;
    }
    private void mergeStructures(MolecularStructure from, MolecularStructure to, List<MolecularStructure> structures) {
        if (from == to) return;
        to.merge(from);
        structures.remove(from);
    }

    private Molecule findMoleculeById(String id) {
        for (Molecule molecule : molecules) {
            if (molecule.getId().equals(id)) {
                return molecule;
            }
        }
        return null;
    }


    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        
        /* YOUR CODE HERE */

        int count = 1;
        System.out.println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");
        for (MolecularStructure structure : molecularStructures) {
            List<Molecule> molecules = structure.getMolecules();
            Collections.sort(molecules);

            System.out.print("Molecules in Molecular Structure " + count + ":"+" [");

            for (int i = 0; i < molecules.size(); i++) {
                System.out.print(molecules.get(i).getId());
                if (i < molecules.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
            count++;
        }

    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targeStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();
        
        /* YOUR CODE HERE */

        // Check each structure in the Vitales list

        for (MolecularStructure vitalesStructure : targeStructures) {
            boolean isUnique = true;

            // Compare against all structures in the human list
            for (MolecularStructure humanStructure : sourceStructures) {
                if (areStructuresEquivalent(vitalesStructure, humanStructure)) {
                    isUnique = false;
                    break;
                }
            }

            // If no equivalent structure is found in the human list, add to anomalies
            if (isUnique) {
                anomalyList.add(vitalesStructure);

            }
        }
        return anomalyList;
    }
    private static boolean areStructuresEquivalent(MolecularStructure a, MolecularStructure b) {
        // Compare based on molecule lists; need to ensure that both structures have the same molecules
        ArrayList<Molecule> moleculesA = (ArrayList<Molecule>) a.getMolecules();
        ArrayList<Molecule> moleculesB = (ArrayList<Molecule>) b.getMolecules();

        if (moleculesA.size() != moleculesB.size()) return false;

        // Use set to handle unordered comparison of molecules
        HashSet<String> idsA = new HashSet<>();
        HashSet<String> idsB = new HashSet<>();

        for (Molecule m : moleculesA) idsA.add(m.getId());
        for (Molecule m : moleculesB) idsB.add(m.getId());

        return idsA.equals(idsB);
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {

        /* YOUR CODE HERE */
        System.out.println("Molecular structures unique to Vitales individuals:\n");
        for (MolecularStructure structure : molecularStructures) {
            System.out.print("[");
            List<Molecule> molecules = structure.getMolecules();
            Collections.sort(molecules);
            for (int i = 0; i < molecules.size(); i++) {
                System.out.print(molecules.get(i).getId());
                if (i < molecules.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }

    }
}
