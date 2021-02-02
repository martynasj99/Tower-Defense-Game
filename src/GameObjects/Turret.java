package GameObjects;

import util.GameObject;
import util.Point3f;

public class Turret extends GameObject {

    private String type;
    private int level;
    private int damage;
    private int speed;
    private float range;

    public Turret() {
        super();
    }

    public Turret(String textureLocation, int width, int height, Point3f centre, String type, int damage, int speed, float range) {
        super(textureLocation, width, height, centre);
        this.type = type;
        this.level = 0;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }
}
