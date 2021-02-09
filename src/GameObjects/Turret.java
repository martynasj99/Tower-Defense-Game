package GameObjects;

import util.GameObject;
import util.Point3f;

import java.util.concurrent.TimeUnit;

public class Turret extends GameObject {

    private String type;
    private int level;
    private int cost;
    private int speed;
    private float range;
    private Bullet bullet;
    private Enemy target;

    public Turret() {
        super();
    }

    public Turret(String textureLocation, int width, int height, Point3f centre, String type, int cost, int speed, float range, Bullet bullet) {
        super(textureLocation, width, height, centre);
        this.type = type;
        this.level = 0;
        this.cost = cost;
        this.speed = speed;
        this.range = range;
        this.bullet = bullet;
        this.target = null;
    }

    public void upgradeTurret(){
        this.level++;
        this.range +=10;
        this.bullet.setDamage(getBullet().getDamage()+10);
        this.cost *= this.cost+5;
        this.speed-=2;
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

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
