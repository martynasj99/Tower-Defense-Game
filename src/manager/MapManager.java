package manager;

import map.GameMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
/**
 * Student Name: Martynas Jagutis
 * Student Number: 17424866
 */
/**
 * Tile Map idea inspired by https://www.youtube.com/user/Brackeys (where it was used in 3d unity game). All code however is mine.
 * */
public class MapManager {

    private static final MapManager instance = new MapManager();

    private final int SCREEN_HEIGHT = 800;
    private final int SCREEN_WIDTH = 800;

    private List<GameMap> gameMaps;
    private GameMap currentGameMap;

    private Map<String, Image> tileImages;

    public MapManager() {
        tileImages = new HashMap<>();
        configureMap();
    }

    public static MapManager getInstance(){
        return instance;
    }

    public void configureMap(){
        this.gameMaps = new ArrayList<>();
        addMap(new GameMap(SCREEN_WIDTH, SCREEN_HEIGHT, new int[][]{ //13x13
                {0,0,2,0,0,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,1,1,1,1,1,1,1,1,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,1,1,1,1,1,1,1,1,1,0,0},
                {0,0,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,1,1,1,1,1,1,1,1,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,1,1,1,1,1,1,1,1,1,0,0},
                {0,0,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,1,1,1,1,1,1,1,1,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0}}));
        addMap(new GameMap(SCREEN_WIDTH, SCREEN_HEIGHT, new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,1,1,1,1,1,1,0,0,0},
                {0,0,0,1,0,0,0,0,0,1,0,0,0},
                {2,1,1,1,0,0,0,0,0,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,0,0,0},
                {0,0,0,1,1,1,1,1,1,1,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0,0,0}}));
        addMap(new GameMap(SCREEN_WIDTH, SCREEN_HEIGHT, new int[][]{
                {0,0,0,0,0,0,2,0,0,0,0,0,0},
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
        addMap(new GameMap(SCREEN_WIDTH, SCREEN_HEIGHT, new int[][]{
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}}));

        setCurrentGameMap(0);
    }

    public void addToTileImages(String texture){
        try{
            if(!this.tileImages.containsKey(texture))
                this.tileImages.put(texture, ImageIO.read(new File(texture)));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addMap(GameMap gameMap){
        gameMaps.add(gameMap);
    }
    public List<GameMap> getGameMaps() {
        return gameMaps;
    }
    public void setGameMaps(List<GameMap> gameMaps) {
        this.gameMaps = gameMaps;
    }
    public GameMap getCurrentGameMap() {
        return currentGameMap;
    }
    public void setCurrentGameMap(int currentGameMap) {
        this.currentGameMap = gameMaps.get(currentGameMap);
    }
    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }
    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public Map<String, Image> getTileImages() {
        return tileImages;
    }

    public void setTileImages(Map<String, Image> tileImages) {
        this.tileImages = tileImages;
    }


}
