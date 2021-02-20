package map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapManager {

    private static final MapManager instance = new MapManager();

    private final int SCREEN_HEIGHT = 800;
    private final int SCREEN_WIDTH = 800;

    private List<Map> maps;
    private Map currentMap;

    public MapManager() {
        configureMap();
    }

    public static MapManager getInstance(){
        return instance;
    }

    public void configureMap(){
        this.maps = new ArrayList<>();
        addMap(new Map(SCREEN_WIDTH, SCREEN_HEIGHT, new int[][]{ //13x13
                {0,0,2,0,0,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,2,1,1,1,1,1,1,1,2,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,2,1,1,1,1,1,1,1,2,0,0},
                {0,0,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,2,1,1,1,1,1,1,1,2,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,2,1,1,1,1,1,1,1,2,0,0},
                {0,0,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,2,1,1,1,1,1,1,1,2,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,2,0,0}}));
        addMap(new Map(SCREEN_WIDTH, SCREEN_HEIGHT, new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,1,1,1,1,1,1,0,0,0},
                {0,0,0,1,0,0,0,0,0,1,0,0,0},
                {1,1,1,1,0,0,0,0,0,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,0,0,0},
                {0,0,0,1,1,1,1,1,1,1,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0}}));
        addMap(new Map(SCREEN_WIDTH, SCREEN_HEIGHT, new int[][]{
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0}}));
        Node[][] nodes = maps.get(0).getNodes();
        maps.get(0).setEnemyPath(Arrays.asList(nodes[0][2], nodes[2][2], nodes[2][10],nodes[4][10], nodes[4][2], nodes[6][2], nodes[6][10],
                nodes[8][10], nodes[8][2], nodes[10][2], nodes[10][10], nodes[12][10]));

        nodes = maps.get(1).getNodes();
        maps.get(1).setEnemyPath(Arrays.asList(nodes[3][0], nodes[3][3], nodes[1][3], nodes[3][9], nodes[7][9], nodes[12][3]));

        nodes = maps.get(2).getNodes();
        maps.get(2).setEnemyPath(Arrays.asList(nodes[0][6], nodes[12][6]));
        setCurrentMap(0);
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
    public Map getCurrentMap() {
        return currentMap;
    }
    public void setCurrentMap(int currentMap) {
        this.currentMap = maps.get(currentMap);
    }
    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }
    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }
}
