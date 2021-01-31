package map;

import util.Point3f;

import java.awt.*;

public class Map {

    private Node[][] nodes;
    private int[][] configuration;
    private int screenWidth;
    private int screenHeight;
    private float nodeWidth;
    private float nodeHeight;

    public Map(int screenWidth, int screenHeight, int[][] configuration) {
        this.configuration = configuration;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        configure();
    }

    public void configure(){
        int nw = configuration.length;
        int nh = configuration[0].length;
        nodeWidth = (screenWidth/nw);
        nodeHeight = (screenHeight/nh);

        this.nodes = new Node[nw][nh];
        for(int i = 0; i < nodes.length; i++){
            for(int j = 0; j < nodes[0].length; j++){
                if(configuration[i][j] == 0)
                    nodes[i][j] = new Node(nodeWidth-1, nodeHeight-1, Color.GREEN, new Point3f((i*nodeWidth), (j*nodeHeight),0), true);
                else{
                    nodes[i][j] = new Node(nodeWidth-1, nodeHeight-1, new Color(102,71,0), new Point3f((i*nodeWidth), (j*nodeHeight),0), false);
                }
            }
        }
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }

    public int[][] getConfiguration() {
        return configuration;
    }

    public void setConfiguration(int[][] configuration) {
        this.configuration = configuration;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getNodeWidth() {
        return nodeWidth;
    }

    public void setNodeWidth(float nodeWidth) {
        this.nodeWidth = nodeWidth;
    }

    public float getNodeHeight() {
        return nodeHeight;
    }

    public void setNodeHeight(float nodeHeight) {
        this.nodeHeight = nodeHeight;
    }
}
