import java.io.*;
import java.util.*;

/**
 * Incident matrix implementation for the AssociationGraph interface.
 * <p>
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 * updated by Zéro & Anna 31 March 2019
 */
public class AdjacentMatrix extends AbstractAssocGraph {

    /**
     * Parameters initialisation
     */
    private String[] vertexLabel;
    private Integer[][] weightMatrix;
    private final static int INITIAL_SIZE = 5;
    private int pointer;

    /**
     * Constructs empty graph
     * The vertex label array is to store the vertex label
     * The initial row and column of the matrix are both set 5
     * The indexes of vertex label and matrix are coincident
     * The pointer points to the last index
     */
    public AdjacentMatrix() {
        // Implement me!
        vertexLabel = new String[INITIAL_SIZE];
        weightMatrix = new Integer[INITIAL_SIZE][INITIAL_SIZE];
        initWeightMatrix(weightMatrix);
        pointer = 0;
    } // end of IncidentMatrix()

    /**
     * Add a vertex to the current pointer position
     * When success, the pointer moves to the next index
     * When the pointer is out of index boundary, the vertex label and matrix will be extended twice larger
     *
     * @param vertLabel Vertex to add.
     */
    public void addVertex(String vertLabel) {
        // Implement me!
        if (pointer > vertexLabel.length - 1) {
            extendMatrix();
        }
        if (ifAddable(vertLabel)) {
            vertexLabel[pointer] = vertLabel;
            pointer++;
            System.out.println("Vertex " + vertLabel + " is successfully added!");
        } else {
            System.err.println("Cannot add vertex " + vertLabel + ", the same vertex label exists!");
        }
    } // end of addVertex()

    /**
     * Add an edge between the two vertices identified by name, and give its weight
     *
     * @param srcLabel Source vertex of edge to add.
     * @param tarLabel Target vertex of edge to add.
     * @param weight   Integer weight to add between edges.
     */
    public void addEdge(String srcLabel, String tarLabel, int weight) {
        // Implement me!
        int[] i = findSrcAndTarIndexByLabels(srcLabel, tarLabel);
        if (i[0] == -1 || i[1] == -1) {
            System.err.println("Cannot add edge from " + srcLabel + " to " + tarLabel + ", both the source and target nodes must be existed!");
            return;
        }
        if (weight <= 0) {
            System.err.println("New weight must be over 0.");
            return;
        }
        if (this.weightMatrix[i[0]][i[1]] >= 0) {
            System.err.println("Cannot add edge from " + srcLabel + " to " + tarLabel + ", the weight is already existed, use \"update\" to modify.");
            return;
        }
        this.weightMatrix[i[0]][i[1]] = weight;
        System.out.println("Edge from " + srcLabel + " to " + tarLabel + " is successfully added, valued " + weight + "!");
    } // end of addEdge()

    /**
     * Get the edge weight between the two vertices identified by name
     *
     * @param srcLabel Source vertex of edge to check.
     * @param tarLabel Target vertex of edge to check.
     * @return the weight when both the vertices existed, -1 when the edge is not existed
     */
    public int getEdgeWeight(String srcLabel, String tarLabel) {
        // Implement me!
        int[] i = findSrcAndTarIndexByLabels(srcLabel, tarLabel);
        if (i[0] == -1 || i[1] == -1) {
            System.err.println("Cannot get edge weight, both " + srcLabel + " and " + tarLabel + " must be existed!");
            return EDGE_NOT_EXIST;
        }
        return weightMatrix[i[0]][i[1]];
    } // end of existEdge()

    /**
     * Change the edge weight between the two vertices identified by name into a new value
     * If the value is set 0, delete the edge
     *
     * @param srcLabel Source vertex of edge to update weight of.
     * @param tarLabel Target vertex of edge to update weight of.
     * @param weight   Weight to update edge to.  If weight = 0, delete the edge.
     */
    public void updateWeightEdge(String srcLabel, String tarLabel, int weight) {
        // Implement me!
        int[] i = findSrcAndTarIndexByLabels(srcLabel, tarLabel);
        if (i[0] == -1 || i[1] == -1) {
            System.err.println("Cannot update edge weight, both " + srcLabel + " and " + tarLabel + " must be existed!");
            return;
        }
        if (this.weightMatrix[i[0]][i[1]] == -1) {
            System.err.println("Cannot update edge weight, there is no edge between " + srcLabel + " and " + tarLabel + "!");
            return;
        }
        if (weight <= 0) {
            this.weightMatrix[i[0]][i[1]] = EDGE_NOT_EXIST;
            System.out.println("Update complete! The edge from " + this.vertexLabel[i[0]] + " to " + this.vertexLabel[i[1]] + " has been removed!");
            return;
        }
        int previousWeight = this.weightMatrix[i[0]][i[1]];
        this.weightMatrix[i[0]][i[1]] = weight;
        System.out.println("Update complete! The weight from " + this.vertexLabel[i[0]] + " to " + this.vertexLabel[i[1]] + " is changed from " + previousWeight + " to " + weight);
    } // end of updateWeightEdge()

    /**
     * Remove the node identified by name and all its relations
     *
     * @param vertLabel Vertex to remove.
     */
    public void removeVertex(String vertLabel) {
        // Implement me!
        int index = findLabelIndexByLabel(vertLabel);
        if (index < 0) {
            System.err.println("Node " + vertLabel + " is not existed!");
            return;
        }
        if (this.pointer - index >= 0)
            System.arraycopy(vertexLabel, index + 1, vertexLabel, index, this.pointer - index);
        for (Integer[] weightMatrix1 : weightMatrix) {
            if (pointer - index >= 0)
                System.arraycopy(weightMatrix1, index + 1, weightMatrix1, index, pointer - index);
        }
        int columns = weightMatrix[0].length;
        for (int i = index; i < pointer; i++) {
            System.arraycopy(weightMatrix[i + 1], 0, weightMatrix[i], 0, columns);
        }
        this.pointer--;
        System.out.println("Node " + vertLabel + " has been removed!");
    } // end of removeVertex()

    /**
     * Get k in-nearest-neighbours of the vertex
     *
     * @param k is the max size of the result list
     * @param vertLabel Vertex to find the in-neighbourhood for.
     * @return the k in-nearest-neighbours, return all in-neighbours when k = -1
     */
    public List<MyPair> inNearestNeighbours(int k, String vertLabel) {
        if (k == 0) return new ArrayList<>();
        List<MyPair> neighbours = new ArrayList<MyPair>();
        // Implement me!
        int targetColumn = findLabelIndexByLabel(vertLabel);
        if (targetColumn < 0) {
            System.err.println("Cannot find node " + vertLabel + "!");
            return new ArrayList<>();
        }
        for (int i = 0; i < pointer; i++) {
            if (weightMatrix[i][targetColumn] > 0) {
                MyPair myPair = new MyPair(vertexLabel[i], weightMatrix[i][targetColumn]);
                neighbours.add(myPair);
            }
        }
        if (k < 0) return neighbours;
        return resizeList(k, (ArrayList<MyPair>) neighbours);
    } // end of inNearestNeighbours()

    /**
     * Get k out-nearest-neighbours of the vertex
     *
     * @param k is the max size of the result list
     * @param vertLabel Vertex to find the out-neighbourhood for.
     * @return the k out-nearest-neighbours, return all out-neighbours when k = -1
     */
    public List<MyPair> outNearestNeighbours(int k, String vertLabel) {
        if (k == 0) return new ArrayList<>();
        List<MyPair> neighbours = new ArrayList<MyPair>();
        // Implement me!
        int targetRow = findLabelIndexByLabel(vertLabel);
        if (targetRow < 0) {
            System.err.println("Cannot find node " + vertLabel + "!");
            return new ArrayList<>();
        }
        for (int i = 0; i < pointer; i++) {
            if (weightMatrix[targetRow][i] > 0) {
                MyPair myPair = new MyPair(vertexLabel[i], weightMatrix[targetRow][i]);
                neighbours.add(myPair);
            }
        }
        if (k < 0) return neighbours;
        return resizeList(k, (ArrayList<MyPair>) neighbours);
    } // end of outNearestNeighbours()

    /**
     * Print all the vertices
     *
     * @param os PrinterWriter to print to.
     */
    public void printVertices(PrintWriter os) {
        // Implement me!
        int vertexNum = 0;
        for (String s : this.vertexLabel) {
            if (s == null) break;
            os.write(s + " ");
            vertexNum++;
        }
        os.write("\n");
        os.write("Total vertices: " + vertexNum + "\n");
        os.flush();
    } // end of printVertices()

    /**
     * Print all the edges and their weights
     *
     * @param os PrinterWriter to print to.
     */
    public void printEdges(PrintWriter os) {
        // Implement me!
        int edgeNum = 0;
        for (int i = 0; i < this.pointer; i++) {
            for (int j = 0; j < this.pointer; j++) {
                if (this.weightMatrix[i][j] > 0) {
                    os.write(this.vertexLabel[i] + " " + this.vertexLabel[j] + " " + this.weightMatrix[i][j] + "\n");
                    edgeNum++;
                }
            }
        }
        os.write("Total edges: " + edgeNum + "\n");
        os.flush();
    } // end of printEdges()

    /**
     * Perform extension when the vertex label and matrix reach the limitation twice larger
     */
    private void extendMatrix() {
        String[] newVertexLabel = new String[vertexLabel.length * 2];
        Integer[][] newWeightMatrix = new Integer[weightMatrix.length * 2][weightMatrix[0].length * 2];
        initWeightMatrix(newWeightMatrix);
        System.arraycopy(this.vertexLabel, 0, newVertexLabel, 0, this.vertexLabel.length);
        for (int i = 0; i < weightMatrix[0].length; i++) {
            System.arraycopy(this.weightMatrix[i], 0, newWeightMatrix[i], 0, weightMatrix[i].length);
        }
        this.vertexLabel = newVertexLabel;
        this.weightMatrix = newWeightMatrix;
    }

    /**
     * check if the vertex can be added by restriction - only one parameter "name" for now
     *
     * @param str the name of the inserting vertex
     * @return if the vertex can be inserted
     */
    private boolean ifAddable(String str) {
        if (this.vertexLabel[0] == null) {
            return true;
        }
        boolean res = true;
        for (int i = 0; i < pointer; i++) {
            if (this.vertexLabel[i].equals(str)) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * Find the index of the target vertex by name
     *
     * @param str the name of the target vertex
     * @return the target index found by vertex name, -1 when the vertex is not existed
     */
    private int findLabelIndexByLabel(String str) {
        int res = -1;
        for (int i = 0; i < this.vertexLabel.length; i++) {
            if (this.vertexLabel[i] == null) break;
            if (this.vertexLabel[i].equals(str)) {
                res = i;
                break;
            }
        }
        return res;
    }

    /**
     * Find both the source and the target indexes of the vertices by name
     *
     * @param srcLabel the name of the source vertex
     * @param tarLabel the name of the target vertex
     * @return the source index in res[0] and target index in res[1], when the vertex is not existed the value is set -1
     */
    private int[] findSrcAndTarIndexByLabels(String srcLabel, String tarLabel) {
        // initiate array for result, res[0] for src label and res[1] for tar label
        int[] res = {-1, -1};
        boolean srcFound = false, tarFound = false;
        for (int i = 0; i < this.vertexLabel.length; i++) {
            if (this.vertexLabel[i] == null) break;
            if (!srcFound) {
                if (srcLabel.equals(this.vertexLabel[i])) {
                    res[0] = i;
                    srcFound = true;
                }
            }
            if (!tarFound) {
                if (tarLabel.equals(this.vertexLabel[i])) {
                    res[1] = i;
                    tarFound = true;
                }
            }
            if (tarFound && srcFound) {
                break;
            }
        }
        return res;
    }

    /**
     * Set all the initial matrix value with EDGE_NOT_EXIST
     *
     * @param weightMatrix the weight matrix declared in the class
     */
    private void initWeightMatrix(Integer[][] weightMatrix) {
        for (int i = 0; i < weightMatrix[0].length; i++) {
            for (int j = 0; j < weightMatrix.length; j++) {
                weightMatrix[i][j] = EDGE_NOT_EXIST;
            }
        }
    }

} // end of class IncidenceMatrix
