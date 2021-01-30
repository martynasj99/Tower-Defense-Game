package map;

public class Map {

    private Node[][] nodes;

    public Map(Node[][] nodes) {
        this.nodes = nodes;
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }
}
