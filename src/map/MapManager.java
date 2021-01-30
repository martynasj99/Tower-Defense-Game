package map;

import util.Point3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapManager {

    private static final MapManager instance = new MapManager(80,80, new int[][]{
            {0,0,0,1,0,0,0,0,0,0},
            {0,0,0,1,0,0,0,0,0,0},
            {0,0,0,1,0,0,0,0,0,0},
            {0,0,0,1,1,1,1,1,0,0},
            {0,0,0,0,0,0,0,1,0,0},
            {0,0,1,1,1,1,1,1,0,0},
            {0,0,1,0,0,0,0,0,0,0},
            {0,0,1,0,0,0,0,0,0,0},
            {0,0,1,0,0,0,0,0,0,0},
            {0,0,1,0,0,0,0,0,0,0}});

    private List<Map> maps;
    private int nodeWidth;
    private int nodeHeight;
    private int[][] configuration;
    private int currentMap;

    public MapManager(int nodeWidth, int nodeHeight, int[][] configuration) {
        this.maps = new ArrayList<>();
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.configuration = configuration;
        configureMap();
    }

    public static MapManager getInstance(){
        return instance;
    }

    private void configureMap(){
        int nw = configuration.length;
        int nh = configuration[0].length;

        Node[][] nodes = new Node[nw][nh];
        for(int i = 0; i < nodes.length; i++){
            for(int j = 0; j < nodes[0].length; j++){
                if(configuration[i][j] == 0)
                    nodes[i][j] = new Node(nodeWidth, nodeHeight, Color.GREEN, new Point3f(i*nodeWidth, j*nodeHeight,0), true);
                else{
                    nodes[i][j] = new Node(nodeWidth, nodeHeight, new Color(102,71,0), new Point3f(i*nodeWidth, j*nodeHeight,0), false);
                }
            }
        }
        addMap(new Map(nodes));
    }

    public void addMap(Map map){
        maps.add(map);
    }

    public List<Map> getMaps() {
        return maps;
    }

    public void setMaps(List<Map> maps) {
        this.maps = maps;
    }

    public int getNodeWidth() {
        return nodeWidth;
    }

    public void setNodeWidth(int nodeWidth) {
        this.nodeWidth = nodeWidth;
    }

    public int getNodeHeight() {
        return nodeHeight;
    }

    public void setNodeHeight(int nodeHeight) {
        this.nodeHeight = nodeHeight;
    }

    public int[][] getConfiguration() {
        return configuration;
    }

    public void setConfiguration(int[][] configuration) {
        this.configuration = configuration;
    }

    public int getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(int currentMap) {
        this.currentMap = currentMap;
    }
}
