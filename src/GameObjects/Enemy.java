package GameObjects;

import util.GameObject;
import util.Point3f;

public class Enemy extends GameObject {

    private int level;
    private int progress;
    private int health;
    private double speed;

    public Enemy(String textureLocation, int width, int height, Point3f centre, int level, int health, double speed) {
        super(textureLocation, width, height, centre);
        this.level = level;
        this.health = health;
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
