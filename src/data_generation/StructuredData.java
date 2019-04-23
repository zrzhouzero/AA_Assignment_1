package data_generation;

/**
 * A data structure to store vertices and edges "source node label - target node label - weight"
 */
public class StructuredData {

    private String sourceNode;
    private String targetNode;
    private Integer weight;

    public StructuredData(String sourceNode, String targetNode, Integer weight) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.weight = weight;
    }

    public String getSourceNode() {
        return this.sourceNode;
    }

    public String getTargetNode() {
        return this.targetNode;
    }

    public Integer getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return this.sourceNode + " " + this.targetNode + " " + this.weight;
    }

    @Override
    public int hashCode() {
        return sourceNode.hashCode() + targetNode.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (!(object instanceof StructuredData)) return false;
        StructuredData s = (StructuredData) object;
        return (s.getSourceNode().equals(this.getSourceNode()) && s.getTargetNode().equals(this.getTargetNode()));
    }

}
