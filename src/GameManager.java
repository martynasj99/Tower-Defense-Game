import GameObjects.Bullet;
import GameObjects.Enemy;
import GameObjects.Turret;
import map.Map;
import map.MapManager;
import map.Node;
import util.Point3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class GameManager {
    private static final GameManager gameManager = new GameManager();

    private  MapManager mapManager = MapManager.getInstance();

    private List<Enemy> enemyTypes;
    private List<Turret> turretTypes;

    private int round;
    private int coins;
    private int lives;
    private Node selected;
    private Turret selectedTurret;

    private Queue<Enemy> enemiesToSpawn = new LinkedBlockingDeque<>();

    public GameManager(){
        this.round = 0;
        this.coins = 10;
        this.lives = 10;
        initializeTurrets();
        initializeEnemies();
        generateWave(this.round);
    }

    public static GameManager getInstance(){
        return gameManager;
    }

    public void setSelectedTurret(int ind){
        this.selectedTurret = turretTypes.get(ind);
    }

    public void initializeEnemies(){
        Map currentMap =  mapManager.getCurrentMap();
        enemyTypes = new ArrayList<>();
        enemyTypes.add(new Enemy("res/virus/virus_0.png",50,50, new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(), currentMap.getEnemyPath().get(0).getPosition().getY(),0), 1,100,this.round+1,1));
        enemyTypes.add(new Enemy("res/virus/virus_1.png",50,50, new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(), currentMap.getEnemyPath().get(0).getPosition().getY(),0), 1,400,2*this.round+1,1.5f));
        enemyTypes.add(new Enemy("res/virus/virus_2.png",50,50, new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(), currentMap.getEnemyPath().get(0).getPosition().getY(),0), 1,500,3*this.round+1,1));
        enemyTypes.add(new Enemy("res/virus/virus_3.png",50,50, new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(), currentMap.getEnemyPath().get(0).getPosition().getY(),0), 1,1000, 4*this.round+1, 1));
        enemyTypes.add(new Enemy("res/virus/virus_4.png",50,50, new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(), currentMap.getEnemyPath().get(0).getPosition().getY(),0), 1,2000,5*this.round+1, 2));
        enemyTypes.add(new Enemy("res/virus/virus_5.png",50,50, new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(), currentMap.getEnemyPath().get(0).getPosition().getY(),0), 1,2000,6*this.round+1, 1.5f));
        enemyTypes.add(new Enemy("res/virus/virus_6.png",50,50, new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(), currentMap.getEnemyPath().get(0).getPosition().getY(),0), 1,5000,7*this.round+1, 1));
    }

    public void initializeTurrets(){
        turretTypes = new ArrayList<>();
        turretTypes.add(new Turret("res/turrets/Cannon.png",40,70, new Point3f(), "Cannon", 1 ,10,200,
                new Bullet("res/Bullet.png",10,10, new Point3f(), 10)));
        turretTypes.add(new Turret("res/turrets/MG.png",40,70, new Point3f(), "MG", 5 ,5,200,
                new Bullet("res/Bullet.png",10,10, new Point3f(), 50)));
        turretTypes.add(new Turret("res/turrets/Missile_Launcher.png",40,70, new Point3f(), "Missile", 10 ,20,400,
                new Bullet("res/Bullet.png",10,10, new Point3f(), 100)));
    }

    public void generateWave(int wave){
        java.util.Map<Integer, Integer> spawnAmount = new HashMap<>();
        spawnAmount.put(0, (wave*2)+2);
        spawnAmount.put(1, wave-1 > 3 ? wave / 2 : 0);
        spawnAmount.put(2, wave > 5 ? wave / 3 : 0);
        spawnAmount.put(3, wave > 8 ? wave / 8 : 0);
        spawnAmount.put(4, wave > 10 ? wave / 5 : 0);
        spawnAmount.put(5, wave > 11 ? wave / 11 : 0);
        spawnAmount.put(6, wave > 12 ? wave / 6 : 0);

        for(java.util.Map.Entry<Integer, Integer> entry: spawnAmount.entrySet()){
            Enemy e = enemyTypes.get(entry.getKey());
            for(int i = 0; i < entry.getValue(); i++){
                enemiesToSpawn.add(new Enemy(e.getTexture(),e.getWidth(),e.getHeight(), new Point3f(e.getCentre().getX(), e.getCentre().getY(),e.getCentre().getZ()), e.getLevel(),e.getHealth(),e.getReward(), e.getSpeed()));
            }
        }
    }

    public Enemy getNextEnemy(){
        if(!enemiesToSpawn.isEmpty()) return enemiesToSpawn.poll();
        return null;
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

    public Node getSelected() {
        return selected;
    }

    public void setSelected(Node selected) {
        this.selected = selected;
    }

    public List<Enemy> getEnemyTypes() {
        return enemyTypes;
    }

    public void setEnemyTypes(List<Enemy> enemyTypes) {
        this.enemyTypes = enemyTypes;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Queue<Enemy> getEnemiesToSpawn() {
        return enemiesToSpawn;
    }

    public void setEnemiesToSpawn(Queue<Enemy> enemiesToSpawn) {
        this.enemiesToSpawn = enemiesToSpawn;
    }

    public List<Turret> getTurretTypes() {
        return turretTypes;
    }

    public void setTurretTypes(List<Turret> turretTypes) {
        this.turretTypes = turretTypes;
    }

    public Turret getSelectedTurret() {
        return selectedTurret;
    }

    public void setSelectedTurret(Turret selectedTurret) {
        this.selectedTurret = selectedTurret;
    }
}
