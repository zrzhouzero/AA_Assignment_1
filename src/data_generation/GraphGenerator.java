package data_generation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;

/**
 * This is a class to randomly generate a .csv file that contains three columns,
 * source node label (String) - target node label (String) - weight (int)
 *
 * @author Zéro
 * @version 0.9
 */
public class GraphGenerator {

    private HashSet<String> vertexSet;
    private HashSet<StructuredData> edgeSet;

    private GraphGenerator() {
        this.vertexSet = new HashSet<>();
        this.edgeSet = new HashSet<>();
    }


    /**
     * Generate a graph with a number of unique vertices and edges
     * The weight of the edges are randomly assigned
     *
     * @param vertexNumber is the number of vertices
     * @param edgeNumber is the number of edges
     * @param maxWeight is the max weight when doing random weight generation
     */
    private void generateGraph(int vertexNumber, int edgeNumber, int maxWeight) {
        for (int i = 1; i < vertexNumber + 1; i++) {
            this.vertexSet.add(String.valueOf(i));
        }
        Random r = new Random();
        int i1;
        int i2;
        while (this.edgeSet.size() < edgeNumber) {
            i1 = r.nextInt(this.vertexSet.size()) + 1;
            i2 = r.nextInt(this.vertexSet.size()) + 1;
            if (i1 == i2) {
                continue;
            }
            int weight = r.nextInt(maxWeight - 1) + 1;
            this.edgeSet.add(new StructuredData(String.valueOf(i1), String.valueOf(i2), weight));
        }
    }

    /**
     * Write the generated data to a designated file
     */
    private void outputToFile() {
        File file = new File("Graph/graph.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            for (StructuredData s: edgeSet) {
                pw.write(s.getSourceNode() + "," + s.getTargetNode() + "," + s.getWeight() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the generated data to the target file
     *
     * @param file is the target file
     */
    private void outputToFile(File file) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            for (StructuredData s: edgeSet) {
                pw.write(s.getSourceNode() + "," + s.getTargetNode() + "," + s.getWeight() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GraphGenerator graphLow = new GraphGenerator();
        graphLow.generateGraph(1000, 1000, 30);
        graphLow.outputToFile(new File("Graph/graph_l.csv"));

        GraphGenerator graphMedium = new GraphGenerator();
        graphMedium.generateGraph(1000, 10000, 30);
        graphMedium.outputToFile(new File("Graph/graph_m.csv"));

        GraphGenerator graphHigh = new GraphGenerator();
        graphHigh.generateGraph(1000, 100000, 30);
        graphHigh.outputToFile(new File("Graph/graph_h.csv"));
    }

}
