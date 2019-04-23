package data_generation;

import java.io.*;
import java.util.*;

/**
 * Generate data for structure (Adjacent List and Incidence Matrix) test
 * The source data is assocGraph.csv
 *
 * @author Zéro
 * @version 1.0
 */
public class CommandGeneration {

    /**
     * list is to handle the loaded file
     * vertexSet is to store the current existed vertex
     * edgeSet is to store the current existed edge
     */
    private final static File file = new File("/Users/zhouzhirou/IdeaProjects/AA Assignment 1/Graph/graph_h.csv");
    private HashSet<String> vertexSet;
    private HashSet<StructuredData> edgeSet;

    /**
     * Load the source file and create two empty result set
     */
    private CommandGeneration() {
        listInitialisation();
    }

    private void listInitialisation() {
        this.vertexSet = new HashSet<>();
        this.edgeSet = new HashSet<>();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                String[] temp = sc.nextLine().split(",");
                this.vertexSet.add(temp[0]);
                this.edgeSet.add(new StructuredData(temp[0], temp[1], Integer.valueOf(temp[2])));
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate data to add vertex
     *
     * @param k          is the expected number of the result set
     * @param fileName   is the name of the written file
     * @param startLabel is the start label of the adding vertex
     */
    private void generateAddVertexOperation(int k, String fileName, int startLabel) {
        HashSet<String> resultSet = new HashSet<>();
        int n = 0;
        while (n < k) {
            if (this.vertexSet.contains(String.valueOf(startLabel))) {
                startLabel++;
                continue;
            }
            this.vertexSet.add(String.valueOf(startLabel));
            resultSet.add(String.valueOf(startLabel));
            startLabel++;
            n++;
        }
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(fileName, true));
            for (String s : resultSet) {
                pw.write("AV " + s + "\n");
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate data to add edge
     *
     * @param k         is the expected number of the result set
     * @param filename  is the name of the written file
     * @param maxWeight is the max weight of the edge being added
     */
    private void generateAddEdgeOperation(int k, String filename, int maxWeight) {
        HashSet<StructuredData> resultSet = new HashSet<>();
        ArrayList<String> tempList = new ArrayList<>(this.vertexSet);
        Random r = new Random();
        int srcIndex;
        int tarIndex;
        int n = 0;
        while (n < k) {
            srcIndex = r.nextInt(tempList.size());
            tarIndex = r.nextInt(tempList.size());
            if (srcIndex == tarIndex) {
                continue;
            }
            StructuredData s = new StructuredData(tempList.get(srcIndex), tempList.get(tarIndex), r.nextInt(maxWeight));
            if (resultSet.contains(s)) {
                continue;
            }
            resultSet.add(s);
            n++;
        }
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename, true));
            for (StructuredData s : resultSet) {
                this.edgeSet.add(s);
                pw.write("AE " + s.getSourceNode() + " " + s.getTargetNode() + " " + s.getWeight() + "\n");
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate data to get edge weight
     *
     * @param k        is the expected number of the result set
     * @param filename is the name of the written file
     */
    private void generateGetEdgeWeightOperation(int k, String filename) {
        ArrayList<StructuredData> tempList = new ArrayList<>(this.edgeSet);
        HashSet<StructuredData> resultSet = new HashSet<>();
        Random r = new Random();
        int randomIndex = 0;
        if (tempList.size() > k) {
            while (resultSet.size() < k) {
                randomIndex += r.nextInt(tempList.size());
                if (randomIndex >= tempList.size()) {
                    randomIndex -= tempList.size();
                }
                resultSet.add(tempList.get(randomIndex));
            }
        } else {
            resultSet = new HashSet<>(tempList);
        }
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename, true));
            for (StructuredData s : resultSet) {
                pw.write("W " + s.getSourceNode() + " " + s.getTargetNode() + "\n");
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate data to update edge weight
     *
     * @param k        is the expected number of the result set
     * @param maxValue is the max number of the random weight
     * @param filename is the name of the written file
     */
    private void generatedEdgeWeightUpdate(int k, int maxValue, String filename) {
        ArrayList<StructuredData> tempList = new ArrayList<>(this.edgeSet);
        HashSet<StructuredData> resultSet = resizeResultSet(k, tempList);
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename, true));
            Random r = new Random();
            for (StructuredData s : resultSet) {
                int weight = r.nextInt(maxValue);
                pw.write("U " + s.getSourceNode() + " " + s.getTargetNode() + " " + weight + "\n");
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate data to remove vertex
     *
     * @param k        is the expected number of the result set
     * @param filename is the name of the written file
     */
    private void generateVertexRemoval(int k, String filename) {
        ArrayList<String> tempList = new ArrayList<>(vertexSet);
        HashSet<String> resultSet = resizeResultSet(k, tempList);
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename, true));
            for (String s : resultSet) {
                pw.write("RV " + s + "\n");
                this.vertexSet.remove(s);
                this.edgeSet.removeIf(e -> e.getSourceNode().equals(s) || e.getTargetNode().equals(s));
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate data to get n in nearest neighbours
     *
     * @param k        is the expected number of the result set
     * @param maxValue is the max value of n
     * @param filename is the name of the written file
     */
    private void generateIn(int k, int maxValue, String filename) {
        ArrayList<String> tempList = new ArrayList<>(vertexSet);
        HashSet<String> resultSet = resizeResultSet(k, tempList);
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename, true));
            Random ran = new Random();
            int[] value = getIndexSets(maxValue);
            for (String s : resultSet) {
                pw.write("IN " + value[(ran.nextInt(maxValue + 1))] + " " + s + "\n");
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate data to get n out nearest neighbours
     *
     * @param k        is the expected number of the result set
     * @param maxValue is the max value of n
     * @param filename is the name of the written file
     */
    private void generateOut(int k, int maxValue, String filename) {
        ArrayList<String> tempList = new ArrayList<>(vertexSet);
        HashSet<String> resultSet = resizeResultSet(k, tempList);
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename, true));
            Random ran = new Random();
            int[] value = getIndexSets(maxValue);
            for (String s : resultSet) {
                pw.write("ON " + value[(ran.nextInt(maxValue + 1))] + " " + s + "\n");
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generate data to print all vertices
     *
     * @param filename is the name of the written file
     */
    private void generateVertexPrinting(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            pw.write("PV\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generate data to print all edges
     *
     * @param filename is the name of the written file
     */
    private void generateEdgePrinting(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            pw.write("PE\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate data to quit
     *
     * @param filename is the name of the written file
     */
    private void generateQuit(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            pw.write("Q\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * resize the result set by the restriction k
     *
     * @param k        the max size of the result size
     * @param tempList is the list being cut
     * @return the pruned data
     */
    private <T> HashSet<T> resizeResultSet(int k, ArrayList<T> tempList) {
        HashSet<T> resultSet = new HashSet<>();
        Random r = new Random();
        int randomIndex = 0;
        if (tempList.size() > k) {
            while (resultSet.size() < k) {
                randomIndex += r.nextInt(tempList.size());
                if (randomIndex >= tempList.size()) {
                    randomIndex -= tempList.size();
                }
                resultSet.add(tempList.get(randomIndex));
            }
        } else {
            resultSet.addAll(tempList);
        }
        return resultSet;
    }

    /**
     * Modify the index set to fit the number of in/out nearest neighbours {-1, 1, ..., maxValue}
     *
     * @param maxValue the max value of the n nearest neighbours
     * @return the modified value
     */
    private int[] getIndexSets(int maxValue) {
        int[] value = new int[maxValue + 1];
        value[0] = -1;
        for (int i = 1; i < maxValue + 1; i++) {
            value[i] = i;
        }
        return value;
    }

    public static void defaultGenerator() {
        CommandGeneration cg;

        cg = new CommandGeneration();
        cg.generateVertexRemoval(200, "ExperimentData/remove_vertex");

        cg = new CommandGeneration();
        cg.generatedEdgeWeightUpdate(200, 1, "ExperimentData/remove_edge");

        cg = new CommandGeneration();
        cg.generateIn(200, 5, "ExperimentData/get_in_nearest_neighbours");

        cg = new CommandGeneration();
        cg.generateOut(200, 5, "ExperimentData/get_out_nearest_neighbours");

        cg = new CommandGeneration();
        cg.generatedEdgeWeightUpdate(200, 30, "ExperimentData/update_edge");
    }

    public static void main(String[] args) {
        defaultGenerator();
    }

}
