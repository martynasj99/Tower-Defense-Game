import GameObjects.Enemy;
import GameObjects.Turret;
import map.Map;
import map.MapManager;
import util.GameObject;
import util.Point3f;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static final GameManager gameManager = new GameManager();

    private  MapManager mapManager = MapManager.getInstance();

    private  List<Enemy> enemyTypes;

    private int wave;
    private int coins;
    private Turret selected;


    public GameManager(){
        this.wave = 0;
        this.coins = 10;
        initializeEnemies();
    }

    public static GameManager getInstance(){
        return gameManager;
    }

    public void initializeEnemies(){
        Map currentMap =  mapManager.getCurrentMap();
        enemyTypes = new ArrayList<>();
        enemyTypes.add(new Enemy("res/UFO.png",50,50, new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(), currentMap.getEnemyPath().get(0).getPosition().getY(),0), 1,100,1));

    }

    public Enemy getEnemeyClone(int ind){
        Enemy e = getEnemyTypes().get(ind);
        return new Enemy(e.getTexture(), e.getWidth(), e.getHeight(), e.getCentre(), e.getLevel(), e.getHealth(), e.getSpeed());
    }

    public void changeCoins(int amt){
        this.coins += amt;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public Turret getSelected() {
        return selected;
    }

    public void setSelected(Turret selected) {
        this.selected = selected;
    }

    public List<Enemy> getEnemyTypes() {
        return enemyTypes;
    }

    public void setEnemyTypes(List<Enemy> enemyTypes) {
        this.enemyTypes = enemyTypes;
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

}
