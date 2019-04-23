import java.io.PrintWriter;

public class Test {

    public static void main(String[] args) {
        test3();
    }

    private static void test1() {
        AbstractAssocGraph t = new IncidenceMatrix();
        PrintWriter os = new PrintWriter(System.out);

        t.addVertex("A");
        t.addVertex("B");
        t.addVertex("C");
        t.addVertex("D");
        t.addVertex("E");
        t.addVertex("F");
        t.addVertex("G");
        t.addVertex("H");
        t.addEdge("A", "B", 1);
        t.addEdge("B", "C", 2);
        t.addEdge("D", "B", 3);
        t.addEdge("C", "E", 5);
        t.addEdge("E", "F", 6);
        t.addEdge("C", "G", 7);
        t.addEdge("G", "E", 7);
        t.addEdge("G", "H", 4);
        t.updateWeightEdge("G", "H", 5);
        t.updateWeightEdge("A", "B", 10);
        t.updateWeightEdge("D", "B", 9);
        t.outNearestNeighbours(-1, "B");
        t.inNearestNeighbours(-1, "F");
        t.outNearestNeighbours(-1, "C");
        t.outNearestNeighbours(1, "C");
        t.getEdgeWeight("A", "B");
        t.getEdgeWeight("D", "A");
        t.removeVertex("J");
        t.removeVertex("A");
        t.updateWeightEdge("B", "C", 0);
        t.updateWeightEdge("E", "F", 0);
        t.updateWeightEdge("F", "E", 0);
        t.addVertex("J");
        t.printVertices(os);
        t.printEdges(os);
        os.close();
    }

    private static void test2() {
        AbstractAssocGraph t = new IncidenceMatrix();
        PrintWriter os = new PrintWriter(System.out);

        t.addVertex("A");
        t.addVertex("B");
        t.addVertex("C");
        t.addVertex("D");
        t.addVertex("E");
        t.addVertex("F");
        t.addEdge("A", "B", 1);
        t.addEdge("C", "B", 1);
        t.addEdge("B", "D", 1);
        t.addEdge("A", "E", 3);
        t.addEdge("D", "C", 5);
        t.addEdge("F", "A", 2);
        t.outNearestNeighbours(-1, "A");
        t.inNearestNeighbours(-1, "F");
        t.getEdgeWeight("C", "B");
        t.getEdgeWeight("B", "C");
        t.getEdgeWeight("A", "e");
        t.updateWeightEdge("C", "B", 4);
        t.updateWeightEdge("A", "B", 0);
        t.removeVertex("D");
        t.addVertex("G");
        t.printVertices(os);
        t.printEdges(os);
        os.close();
    }

    private static void test3() {
        AbstractAssocGraph t = new IncidenceMatrix();
        PrintWriter os = new PrintWriter(System.out);

        t.addVertex("211");
        t.addVertex("124");
        t.addEdge("211", "124", 15);
        t.addVertex("204");
        t.addVertex("131");
        t.addEdge("204", "131", 2);
        t.addVertex("106");
        t.addVertex("231");
        t.addEdge("106", "231", 25);
        t.addVertex("234");
        t.addVertex("102");
        t.addEdge("234", "102", 23);
        t.addVertex("138");
        t.addVertex("203");
        t.addEdge("203", "138", 14);
        t.addVertex("E");
    }

}