import java.io.*;
import java.util.*;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 * <p>
 * Your task is to complete the implementation of this class. You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 * updated by Zéro & Anna 4 April 2019
 */
public class AdjList extends AbstractAssocGraph {

    /**
     * Parameters initialisation
     */
    private Node[] nodes;
    private final static int INITIAL_SIZE = 5;
    private int pointer;

    /**
     * Constructs empty graph with no nodes
     */
    public AdjList() {
        // Implement me!
        nodes = new Node[INITIAL_SIZE];
        pointer = 0;
    } // end of AdjList()

    /**
     * If there is no vertices, add a vertex as the root node
     * or add a vertex next to the head node (the last index)
     *
     * @param vertLabel Vertex to add.
     */
    public void addVertex(String vertLabel) {
        // Implement me!
        if (pointer == 0) {
            Node node = new Node();
            node.setLabel(vertLabel);
            this.nodes[0] = node;
            pointer++;
            System.out.println("Vertex " + vertLabel + " is successfully added!");
            return;
        }
        boolean addable = true;
        for (Node n: nodes) {
            if (n == null) {
                break;
            }
            if (n.getVertexLabel().equals(vertLabel)) {
                addable = false;
                break;
            }
        }
        if (addable) {
            if (pointer > this.nodes.length - 1) {
                extendNodeArray();
            }
            Node node = new Node();
            node.setLabel(vertLabel);
            this.nodes[pointer] = node;
            pointer++;
            System.out.println("Vertex " + vertLabel + " is successfully added!");
        } else {
            System.err.println("Cannot add vertex " + vertLabel + ", the same vertex label exists!");
        }

    } // end of addVertex()

    /**
     * Add an edge between the two existed node
     * and give its weight
     *
     * @param srcLabel Source vertex of edge to add.
     * @param tarLabel Target vertex of edge to add.
     * @param weight   Integer weight to add between edges.
     */
    public void addEdge(String srcLabel, String tarLabel, int weight) {
        // Implement me!
        int srcIndex = getNodeIndexByName(srcLabel);
        int tarIndex = getNodeIndexByName(tarLabel);
        if (srcIndex == -1 || tarIndex == -1) {
            System.err.println("Cannot add edge from " + srcLabel + " to " + tarLabel + ", both the source and target nodes must be existed!");
            return;
        }
        if (this.nodes[srcIndex].addEdge(tarLabel, weight)) {
            System.out.println("Edge from " + srcLabel + " to " + tarLabel + " is successfully added, valued " + weight + "!");
        } else {
            System.err.println("Cannot add edge from " + srcLabel + " to " + tarLabel + ", the weight is already existed, use \"update\" to modify.");
        }
    } // end of addEdge()

    /**
     * Get the weight of the edge between the two selected nodes
     *
     * @param srcLabel Source vertex of edge to check.
     * @param tarLabel Target vertex of edge to check.
     * @return the edge weight between the two nodes
     */
    public int getEdgeWeight(String srcLabel, String tarLabel) {
        // Implement me!
        int srcIndex = getNodeIndexByName(srcLabel);
        int tarIndex = getNodeIndexByName(tarLabel);
        if (srcIndex == -1 || tarIndex == -1) {
            System.err.println("Cannot get edge weight, both " + srcLabel + " and " + tarLabel + " must be existed!");
            return EDGE_NOT_EXIST;
        }
        if (this.nodes[srcIndex].getTargetNode(tarLabel) != null) {
            return this.nodes[srcIndex].getTargetNode(tarLabel).getWeight();
        } else {
            return EDGE_NOT_EXIST;
        }
    } // end of existEdge()

    /**
     * Update the weight of the edge between the two selected nodes, if the updating weight is 0, delete the edge
     *
     * @param srcLabel Source vertex of edge to update weight of.
     * @param tarLabel Target vertex of edge to update weight of.
     * @param weight   Weight to update edge to.  If weight = 0, delete the edge.
     */
    public void updateWeightEdge(String srcLabel, String tarLabel, int weight) {
        // Implement me!
        int srcIndex = getNodeIndexByName(srcLabel);
        int tarIndex = getNodeIndexByName(tarLabel);
        if (srcIndex == -1 || tarIndex == -1) {
            System.err.println("Cannot update edge weight, both " + srcLabel + " and " + tarLabel + " must be existed!");
            return;
        }
        if (this.nodes[srcIndex].getTargetNode(tarLabel) != null) {
            if (weight <= 0) {
                this.nodes[srcIndex].removeEdge(tarLabel);
                System.out.println("Update complete! The edge from " + srcLabel + " to " + tarLabel + " has been removed!");
                return;
            }
            int previousWeight = this.nodes[srcIndex].getTargetNode(tarLabel).getWeight();
            this.nodes[srcIndex].getTargetNode(tarLabel).setWeight(weight);
            System.out.println("Update complete! The weight from " + srcLabel + " to " + tarLabel + " is changed from " + previousWeight + " to " + weight);
        } else {
            System.err.println("Cannot update edge weight, there is no edge between " + srcLabel + " and " + tarLabel + "!");
        }
    } // end of updateWeightEdge()

    /**
     * Remove the node identified by name and all its relations
     *
     * @param vertLabel Vertex to remove.
     */
    public void removeVertex(String vertLabel) {
        // Implement me!
        int nodeRemoving = getNodeIndexByName(vertLabel);
        if (nodeRemoving == -1) {
            System.err.println("Node " + vertLabel + " is not existed!");
            return;
        }
        int i;
        for (i = nodeRemoving; i < this.nodes.length - 2; i++) {
            this.nodes[i] = this.nodes[i + 1];
        }
        i++;
        this.nodes[i] = null;
        for (Node n: this.nodes) {
            if (n == null) {
                break;
            }
            n.removeEdge(vertLabel);
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
        ArrayList<MyPair> neighbours = new ArrayList<>();
        // Implement me!
        int targetIndex = getNodeIndexByName(vertLabel);
        if (targetIndex == -1) {
            System.err.println("Cannot find node " + vertLabel + "!");
            return new ArrayList<>();
        }
        for (Node n: nodes) {
            if (n == null) {
                break;
            }
            Node nextNode = n.getNextNode();
            while (nextNode != null) {
                if (nextNode.getVertexLabel().equals(vertLabel)) {
                    MyPair myPair = new MyPair(n.getVertexLabel(), nextNode.getWeight());
                    neighbours.add(myPair);
                    break;
                }
                nextNode = nextNode.getNextNode();
            }
        }
        if (k < 0) return neighbours;
        return resizeList(k, neighbours);
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
        ArrayList<MyPair> neighbours = new ArrayList<>();
        // Implement me!
        int targetIndex = getNodeIndexByName(vertLabel);
        if (targetIndex == -1) {
            System.err.println("Cannot find node " + vertLabel + "!");
            return new ArrayList<>();
        }
        Node nextNode = this.nodes[targetIndex].getNextNode();
        while (nextNode != null) {
            MyPair myPair = new MyPair(nextNode.getVertexLabel(), nextNode.getWeight());
            neighbours.add(myPair);
            nextNode = nextNode.getNextNode();
        }
        if (k < 0) return neighbours;
        return resizeList(k, neighbours);
    } // end of outNearestNeighbours()

    /**
     * Print all the vertices
     *
     * @param os PrinterWriter to print to.
     */
    public void printVertices(PrintWriter os) {
        // Implement me!
        if (this.nodes.length == 0) {
            return;
        }
        int vertexNum = 0;
        for (Node n: this.nodes) {
            if (n == null) {
                break;
            }
            vertexNum++;
            os.write(n.getVertexLabel() + " ");
        }
        os.write("\n");
        os.write("Total vertices: " + vertexNum + "\n");
        os.flush();
    } // end of printVertices()

    /**
     * Print all the edges
     *
     * @param os PrinterWriter to print to.
     */
    public void printEdges(PrintWriter os) {
        // Implement me!
        if (this.nodes.length == 0) {
            return;
        }
        int edgeNum = 0;
        for (Node n: this.nodes) {
            if (n == null) {
                break;
            }
            Node targetNode = n.getNextNode();
            while (targetNode != null) {
                os.write(n.getVertexLabel() + " " + targetNode.getVertexLabel() + " " + targetNode.getWeight() + "\n");
                targetNode = targetNode.getNextNode();
                edgeNum++;
            }
        }
        os.write("Total edges: " + edgeNum + "\n");
        os.flush();
    } // end of printEdges()

    /**
     * Extend the array twice as larger when the size is not enough
     */
    private void extendNodeArray() {
        Node[] nodes = new Node[this.nodes.length * 2];
        System.arraycopy(this.nodes, 0, nodes, 0, this.nodes.length);
        this.nodes = nodes;
    }

    /**
     * Get the index of the node from the array by label
     *
     * @param label is the label to search for
     * @return the index of the target node
     */
    private int getNodeIndexByName(String label) {
        int res = -1;
        for (int i = 0; i < this.nodes.length; i++) {
            if (this.nodes[i] == null) {
                break;
            }
            if (this.nodes[i].getVertexLabel().equals(label)) {
                res = i;
                break;
            }
        }
        return res;
    }

    /**
     * A node class to handle the linked list node
     */
    protected class Node {

        private Node nextNode;
        private String vertexLabel;
        private int weight;

        public Node() {
            this.nextNode = null;
            this.vertexLabel = "";
            this.weight = 0;
        }

        public void setNextNode(Node node) {
            this.nextNode = node;
        }

        public void setLabel(String str) {
            this.vertexLabel = str;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public Node getNextNode() {
            return this.nextNode;
        }

        public String getVertexLabel() {
            return this.vertexLabel;
        }

        public int getWeight() {
            return this.weight;
        }

        /**
         * Add a node to store the weight between this node and the target node
         *
         * @param targetLabel is the label of the target node
         * @param weight is the weight of the edge
         * @return if the edge is successfully added
         */
        public boolean addEdge(String targetLabel, int weight) {
            Node headNode = this;
            while (headNode.getNextNode() != null) {
                headNode = headNode.getNextNode();
                if (headNode.getVertexLabel().equals(targetLabel)) {
                    return false;
                }
            }
            Node targetNode = new Node();
            targetNode.setLabel(targetLabel);
            targetNode.setWeight(weight);
            headNode.setNextNode(targetNode);
            return true;
        }

        /**
         * Get the target node identified by name
         *
         * @param targetLabel is the label of the target node
         * @return the target Node
         */
        public Node getTargetNode(String targetLabel) {
            Node targetNode = this;
            boolean ifFound = false;
            while (targetNode.getNextNode() != null) {
                targetNode = targetNode.getNextNode();
                if (targetNode.getVertexLabel().equals(targetLabel)) {
                    ifFound = true;
                    break;
                }
            }
            if (ifFound) {
                return targetNode;
            } else {
                return null;
            }
        }

        /**
         * Remove the node by the target label
         * then link the previous node and the next node
         *
         * @param targetLabel
         */
        public void removeEdge(String targetLabel) {
            Node targetNode = this;
            Node previousNode = this;
            while (targetNode.getVertexLabel() != null) {
                previousNode = targetNode;
                targetNode = targetNode.getNextNode();
                if (targetNode == null) {
                    break;
                }
                if (targetNode.getVertexLabel().equals(targetLabel)) {
                    break;
                }
            }
            if (targetNode == null) {
                previousNode.setNextNode(null);
            } else {
                previousNode.setNextNode(targetNode.getNextNode());
            }
        }

    }

} // end of class AdjList
