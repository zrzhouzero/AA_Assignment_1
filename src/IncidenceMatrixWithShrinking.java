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
public class IncidenceMatrixWithShrinking extends AbstractAssocGraph {

    /**
     * Parameters initialisation
     */
    private String[] vertexLabel;
    private Edge[] edgeLabel;
    private int[][] weightMatrix;
    private final static int INITIAL_SIZE = 5;
    private int vertexPointer;
    private int edgePointer;

    /**
     * Constructs empty graph
     * The vertex label array is to store the vertex label
     * The initial row and column of the matrix are both set 5
     * The indexes of vertex label and matrix are coincident
     * The vertexPointer points to the last vertex index
     * The edgePointer points to the last edge index
     */
    public IncidenceMatrixWithShrinking() {
        // Implement me!
        vertexLabel = new String[INITIAL_SIZE];
        edgeLabel = new Edge[INITIAL_SIZE];
        weightMatrix = new int[INITIAL_SIZE][INITIAL_SIZE];
        vertexPointer = 0;
        edgePointer = 0;
    } // end of IncidentMatrix()

    /**
     * Add a vertex to the current vertexPointer position
     * When success, the vertexPointer moves to the next index
     * When the vertexPointer is out of index boundary, the vertex label and matrix will be extended twice larger
     *
     * @param vertLabel Vertex to add.
     */
    public void addVertex(String vertLabel) {
        // Implement me!
        if (vertexPointer > vertexLabel.length - 1) {
            extendVertices();
        }
        if (ifVertexAddable(vertLabel)) {
            vertexLabel[vertexPointer] = vertLabel;
            vertexPointer++;
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
        if (edgePointer > edgeLabel.length - 1) {
            extendEdges();
        }
        int srcIndex = findLabelIndexByLabel(srcLabel);
        int tarIndex = findLabelIndexByLabel(tarLabel);
        if (srcIndex == -1 || tarIndex == -1) {
            System.err.println("Cannot add edge from " + srcLabel + " to " + tarLabel + ", both the source and target nodes must be existed!");
            return;
        }
        if (weight <= 0) {
            System.err.println("New weight must be over 0.");
            return;
        }
        if (findEdgeIndexByLabels(srcLabel, tarLabel) >= 0) {
            System.err.println("Cannot add edge from " + srcLabel + " to " + tarLabel + ", the edge is already existed, use \"update\" to modify.");
            return;
        }
        this.edgeLabel[edgePointer] = new Edge(srcLabel, tarLabel);
        this.weightMatrix[srcIndex][edgePointer] = weight;
        this.weightMatrix[tarIndex][edgePointer] = - weight;
        this.edgePointer++;
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
        int index = findEdgeIndexByLabels(srcLabel, tarLabel);
        if (index == -1) {
            System.err.println("Cannot get edge weight, both " + srcLabel + " and " + tarLabel + " must be existed!");
            return EDGE_NOT_EXIST;
        }
        int res = 0;
        for (int i = 0; i < vertexPointer; i++) {
            if (this.weightMatrix[i][index] > 0) {
                res = this.weightMatrix[i][index];
                break;
            }
        }
        return res;
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
        int edgeIndex = findEdgeIndexByLabels(srcLabel, tarLabel);
        int srcVertexIndex = findLabelIndexByLabel(srcLabel);
        int tarVertexIndex = findLabelIndexByLabel(tarLabel);
        if (srcVertexIndex == -1 || tarVertexIndex == -1) {
            System.err.println("Cannot update edge weight, both " + srcLabel + " and " + tarLabel + " must be existed!");
            return;
        }
        if (edgeIndex == -1) {
            System.err.println("Cannot update edge weight, there is no edge between " + srcLabel + " and " + tarLabel + "!");
            return;
        }
        if (weight <= 0) {
            this.weightMatrix[srcVertexIndex][edgeIndex] = 0;
            this.weightMatrix[tarVertexIndex][edgeIndex] = 0;
            if (edgePointer - 1 - edgeIndex >= 0)
                System.arraycopy(this.edgeLabel, edgeIndex + 1, this.edgeLabel, edgeIndex, edgePointer - 1 - edgeIndex);
            for (int i = edgeIndex; i < edgePointer - 1; i++) {
                for (int j = 0; j < vertexPointer; j++) {
                    this.weightMatrix[j][i] = this.weightMatrix[j][i + 1];
                }
            }
            this.edgePointer--;
            this.edgeLabel[edgePointer] = null;
            for (int i = 0; i < vertexPointer; i++) {
                this.weightMatrix[i][edgePointer] = 0;
            }
            System.out.println("Update complete! The edge from " + srcLabel + " to " + tarLabel + " has been removed!");
            return;
        }
        int previousWeight = this.weightMatrix[srcVertexIndex][edgeIndex];
        this.weightMatrix[srcVertexIndex][edgeIndex] = weight;
        this.weightMatrix[tarVertexIndex][edgeIndex] = - weight;
        System.out.println("Update complete! The weight from " + srcLabel + " to " + tarLabel + " is changed from " + previousWeight + " to " + weight);
    } // end of updateWeightEdge()

    /**
     * Remove the node identified by name and all its relations
     * and then shrink the matrix
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

        // remove the vertex
        for (int i = index; i < this.vertexPointer - 1; i++) {
            if (this.edgePointer >= 0) {
                System.arraycopy(this.weightMatrix[i + 1], 0, this.weightMatrix[i], 0, this.edgePointer);
            }
        }
        if (this.vertexPointer - 1 - index >= 0)
            System.arraycopy(this.vertexLabel, index + 1, this.vertexLabel, index, this.vertexPointer - 1 - index);
        this.vertexPointer--;
        this.vertexLabel[vertexPointer] = null;
        for (int i = 0; i < this.edgePointer; i++) {
            this.weightMatrix[vertexPointer][i] = 0;
        }

        // remove the related edges
        int edgeIndex = 0;
        while (edgeIndex < this.edgePointer) {
            if (this.edgeLabel[edgeIndex].getSrc().equals(vertLabel) || this.edgeLabel[edgeIndex].getTar().equals(vertLabel)) {
                for (int i = edgeIndex; i < this.edgePointer - 1; i++) {
                    for (int j = 0; j < vertexPointer; j++) {
                        this.weightMatrix[j][i] = this.weightMatrix[j][i + 1];
                    }
                }
                if (this.edgePointer - 1 - edgeIndex >= 0) {
                    System.arraycopy(this.edgeLabel, edgeIndex + 1, this.edgeLabel, edgeIndex, this.edgePointer - 1 - edgeIndex);
                }
                this.edgePointer--;
                this.edgeLabel[edgePointer] = null;
                for (int i = 0; i < this.vertexPointer; i++) {
                    this.weightMatrix[i][edgePointer] = 0;
                }
            } else {
                edgeIndex++;
            }
        }
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
        int index = findLabelIndexByLabel(vertLabel);
        if (index < 0) {
            System.err.println("Cannot find node " + vertLabel + "!");
            return new ArrayList<>();
        }
        for (int i = 0; i < edgePointer; i++) {
            if (weightMatrix[index][i] < 0) {
                MyPair myPair = new MyPair(edgeLabel[i].getSrc(), - weightMatrix[index][i]);
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
        int index = findLabelIndexByLabel(vertLabel);
        if (index < 0) {
            System.err.println("Cannot find node " + vertLabel + "!");
            return new ArrayList<>();
        }
        for (int i = 0; i < edgePointer; i++) {
            if (weightMatrix[index][i] > 0) {
                MyPair myPair = new MyPair(edgeLabel[i].getTar(), weightMatrix[index][i]);
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
        for (int i = 0; i < edgeLabel.length; i++) {
            if (this.edgeLabel[i] == null) break;
            int index = findLabelIndexByLabel(edgeLabel[i].getSrc());
            os.write(edgeLabel[i].getSrc() + " " + edgeLabel[i].getTar() + " " + this.weightMatrix[index][i] + "\n");
            edgeNum++;
        }
        os.write("Total edges: " + edgeNum + "\n");
        os.flush();
    } // end of printEdges()

    /**
     * Perform extension when the vertex label and matrix reach the limitation twice larger
     */
    private void extendVertices() {
        String[] newVertexLabel = new String[vertexLabel.length * 2];
        int[][] newWeightMatrix = new int[weightMatrix.length * 2][weightMatrix[0].length];
        System.arraycopy(this.vertexLabel, 0, newVertexLabel, 0, this.vertexLabel.length);
        for (int i = 0; i < vertexPointer; i++) {
            if (edgePointer >= 0) System.arraycopy(this.weightMatrix[i], 0, newWeightMatrix[i], 0, edgePointer);
        }
        this.vertexLabel = newVertexLabel;
        this.weightMatrix = newWeightMatrix;
    }

    /**
     * Perform extension when the edge label and matrix reach the limitation twice larger
     */
    private void extendEdges() {
        Edge[] newEdgeLabel = new Edge[edgeLabel.length * 2];
        int[][] newWeightMatrix = new int[weightMatrix.length][weightMatrix[0].length * 2];
        System.arraycopy(this.edgeLabel, 0, newEdgeLabel, 0, this.edgeLabel.length);
        for (int i = 0; i < vertexPointer; i++) {
            if (edgePointer >= 0) System.arraycopy(this.weightMatrix[i], 0, newWeightMatrix[i], 0, edgePointer);
        }
        this.edgeLabel = newEdgeLabel;
        this.weightMatrix = newWeightMatrix;
    }

    /**
     * check if the vertex can be added by restriction - only one parameter "name" for now
     *
     * @param str the name of the inserting vertex
     * @return if the vertex can be inserted
     */
    private boolean ifVertexAddable(String str) {
        if (this.vertexLabel[0] == null) {
            return true;
        }
        boolean res = true;
        for (String s : vertexLabel) {
            if (s == null) {
                break;
            }
            if (s.equals(str)) {
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
     * @return the index of the edge, when the vertices or the edge is not existed the value is set -1
     */
    private int findEdgeIndexByLabels(String srcLabel, String tarLabel) {
        // initiate array for result, res[0] for src label and res[1] for tar label
        int res = -1;
        for (int i = 0; i < edgeLabel.length; i++) {
            if (this.edgeLabel[i] == null) break;
            if (this.edgeLabel[i].getSrc().equals(srcLabel)) {
                if (this.edgeLabel[i].getTar().equals(tarLabel)) {
                    res = i;
                    break;
                }
            }
        }
        return res;
    }

    private class Edge {

        private String src;
        private String tar;

        private Edge(String src, String tar) {
            this.src = src;
            this.tar = tar;
        }

        private String getSrc() {
            return src;
        }

        private String getTar() {
            return tar;
        }

    }

} // end of class IncidenceMatrix
