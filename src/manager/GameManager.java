package manager;

import GameObjects.Bullet;
import GameObjects.Enemy;
import GameObjects.Turret;
import map.GameMap;
import map.Node;
import util.Point3f;
import util.WaveSpawn;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
/**
 * Student Name: Martynas Jagutis
 * Student Number: 17424866
 */
public class GameManager {
    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public enum GameSpeed{
        PAUSED,
        NORMAL,
        FAST
    }

    private final int NO_ROUNDS = 25;

    private  MapManager mapManager = MapManager.getInstance();
    private static final GameManager gameManager = new GameManager();

    private final int NODE_WIDTH = mapManager.getCurrentGameMap().getNodeWidth();
    private final int NODE_HEIGHT = mapManager.getCurrentGameMap().getNodeHeight();

    private Difficulty difficulty;
    private GameSpeed gameSpeed;

    private List<Enemy> enemyTypes;
    private List<Turret> turretTypes;

    private int round;
    private int coins;
    private int lives;
    private Node selected;
    private Turret selectedTurret;

    private boolean isPaused;
    private boolean isInEditor;

    private Queue<Enemy> enemiesToSpawn = new LinkedBlockingDeque<>();

    private String errorMessage;

    private GameManager(){

        this.difficulty = Difficulty.MEDIUM;
        this.isInEditor = false;
        init();
    }

    public static GameManager getInstance(){
        return gameManager;
    }

    public void setSelectedTurret(int ind){
        this.selectedTurret = turretTypes.get(ind);
    }

    public void init(){
        switch (difficulty){
            case EASY:
                this.coins = 100;
                this.lives = 20;
                break;
            case MEDIUM:
                this.coins = 25;
                this.lives = 15;
                break;
            case HARD:
                this.coins = 10;
                this.lives = 10;
                break;
            default:
                break;
        }
        this.enemiesToSpawn.clear();
        this.gameSpeed = GameSpeed.NORMAL;
        this.round = 0;
        this.isPaused = false;
        this.selected = null;
        this.errorMessage = "";
        initializeTurrets();
        initializeEnemies();

    }

    public void initializeEnemies(){
        GameMap currentGameMap =  mapManager.getCurrentGameMap();
        enemyTypes = new ArrayList<>();
        enemyTypes.add(new Enemy("res/virus/virus_0.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,30,this.round+1,1));
        enemyTypes.add(new Enemy("res/virus/virus_1.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,100,2*this.round+1,1));
        enemyTypes.add(new Enemy("res/virus/virus_2.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,200,3*this.round+1,1.5f));
        enemyTypes.add(new Enemy("res/virus/virus_3.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,400, 4*this.round+1, 2f));
        enemyTypes.add(new Enemy("res/virus/virus_4.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,800,5*this.round+1, 1));
        enemyTypes.add(new Enemy("res/virus/virus_5.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,1600,6*this.round+1, 1.5f));
        enemyTypes.add(new Enemy("res/virus/virus_6.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,3000,7*this.round+1, 1));
        enemyTypes.add(new Enemy("res/virus/virus_7.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,5000,7*this.round+1, 1));
        enemyTypes.add(new Enemy("res/virus/virus_8.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,8000,7*this.round+1, 1));
        enemyTypes.add(new Enemy("res/virus/virus_9.png",(NODE_WIDTH/2f)+10,(NODE_HEIGHT/2f)+10, new Point3f(currentGameMap.getEnemyPath().get(0).getCentre().getX(), currentGameMap.getEnemyPath().get(0).getCentre().getY(),0), 1,20000,7*this.round+1, 1));
    }

    public void initializeTurrets(){
        turretTypes = new ArrayList<>();
        List<String> cannonTextures = Arrays.asList("res/turrets/Cannon.png", "res/turrets/Cannon2.png", "res/turrets/Cannon3.png");
        List<String> mgTextures = Arrays.asList("res/turrets/MG.png", "res/turrets/MG2.png", "res/turrets/MG3.png");
        List<String> missileTextures = Arrays.asList("res/turrets/Missile_Launcher.png", "res/turrets/Missile_Launcher2.png", "res/turrets/Missile_Launcher3.png");

        turretTypes.add(new Turret(cannonTextures,40,70, new Point3f(), "Cannon", 1 ,15,200,
                new Bullet("res/turrets/Bullet_Cannon.png",10,20, new Point3f(), 20)));
        turretTypes.add(new Turret(mgTextures,40,70, new Point3f(), "MG", 5 ,5,200,
                new Bullet("res/turrets/Bullet_MG.png",10,20, new Point3f(), 12)));
        turretTypes.add(new Turret(missileTextures,40,70, new Point3f(), "Missile", 10 ,20,400,
                new Bullet("res/turrets/Missile.png",10,20, new Point3f(), 50)));
        turretTypes.add(new Turret(Collections.singletonList("res/turrets/Controlled.png"),100,100, new Point3f(), "Controlled", 50 ,5,0,
                new Bullet("res/Bullet.png",10,20, new Point3f(), 100)));
    }

    public void generateWave(int wave){
        WaveSpawn waveSpawn = new WaveSpawn();
        Map<Integer, Integer> spawnAmount = new HashMap<>();
        List<Integer> waveEnemies = waveSpawn.getWave(wave);

        for (int i = 0; i < waveEnemies.size(); i++) {
            spawnAmount.put(i, waveEnemies.get(i));
        }

        for(Map.Entry<Integer, Integer> entry: spawnAmount.entrySet()){
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

    public void setDifficulty(int num){
        switch (num){
            case 0:
                this.difficulty = Difficulty.EASY;
                break;
            case 1:
                this.difficulty = Difficulty.MEDIUM;
                break;
            case 2:
                this.difficulty = Difficulty.HARD;
                break;
            default:
                this.difficulty = Difficulty.MEDIUM;
                break;
        }
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

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public GameSpeed getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(GameSpeed gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public boolean isInEditor() {
        return isInEditor;
    }

    public void setInEditor(boolean inEditor) {
        isInEditor = inEditor;
    }

    public int getNO_ROUNDS() {
        return NO_ROUNDS;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
