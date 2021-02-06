package GameObjects;

import util.GameObject;
import util.Point3f;

public class Enemy extends GameObject {

    private int progress;
    private int health;

    public Enemy(String textureLocation, int width, int height, Point3f centre, int health) {
        super(textureLocation, width, height, centre);
        this.health = health;
        this.progress = 0;
    }

    public void takeDamage(int amt){
        this.health -= amt;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
