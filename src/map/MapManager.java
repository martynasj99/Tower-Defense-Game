package map;

import java.util.ArrayList;
import java.util.List;

public class MapManager {

    private static final MapManager instance = new MapManager(800,800);

    private List<Map> maps;
    private int screenWidth;
    private int screenHeight;
    private int currentMap;

    public MapManager(int screenHeight, int screenWidth) {
        this.maps = new ArrayList<>();
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        configureMap();
    }

    public static MapManager getInstance(){
        return instance;
    }

    private void configureMap(){
        addMap(new Map(screenWidth, screenHeight, new int[][]{
                {0,0,0,1,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0},
                {0,0,0,1,1,1,1,1,0,0},
                {0,0,0,0,0,0,0,1,0,0},
                {0,0,1,1,1,1,1,1,0,0},
                {0,0,1,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0,0}}));
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

    public int getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(int currentMap) {
        this.currentMap = currentMap;
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
}
