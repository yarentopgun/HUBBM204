import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {

        /* YOUR CODE HERE */
        try {
            File xmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Parsing Human Molecular Data
            NodeList humanList = doc.getElementsByTagName("HumanMolecularData").item(0).getChildNodes();
            molecularDataHuman = new MolecularData(parseMolecularData(humanList));

            // Parsing Vitales Molecular Data
            NodeList vitalesList = doc.getElementsByTagName("VitalesMolecularData").item(0).getChildNodes();
            molecularDataVitales = new MolecularData(parseMolecularData(vitalesList));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Helper method to parse NodeList into a list of Molecule objects
    private List<Molecule> parseMolecularData(NodeList nodeList) {
        List<Molecule> molecules = new ArrayList<>();
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node nNode = nodeList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String id = eElement.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(eElement.getElementsByTagName("BondStrength").item(0).getTextContent());
                NodeList bondsList = eElement.getElementsByTagName("MoleculeID");
                List<String> bonds = new ArrayList<>();
                for (int j = 0; j < bondsList.getLength(); j++) {
                    bonds.add(bondsList.item(j).getTextContent());
                }
                molecules.add(new Molecule(id, bondStrength, bonds));
            }
        }
        return molecules;
    }
}
