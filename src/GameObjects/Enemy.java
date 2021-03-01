package GameObjects;

import util.GameObject;
import util.Point3f;
/**
 * Student Name: Martynas Jagutis
 * Student Number: 17424866
 */
public class Enemy extends GameObject {

    private int level;
    private int progress;
    private int initialHealth;
    private int health;
    private int reward;
    private float speed;

    public Enemy(String textureLocation, float width, float height, Point3f centre, int level, int initialHealth, int reward, float speed) {
        super(textureLocation, width, height, centre);
        this.level = level;
        this.initialHealth = initialHealth;
        this.health = initialHealth;
        this.reward = reward;
        this.speed = speed;
        this.progress = 0;
    }

    public void takeDamage(int amt){
        this.health -= amt;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getInitialHealth() {
        return initialHealth;
    }

    public void setInitialHealth(int initialHealth) {
        this.initialHealth = initialHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
