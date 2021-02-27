package GameObjects;

import util.GameObject;
import util.Point3f;

import java.util.List;

public class Turret extends GameObject {

    private String type;
    private int level;
    private int cost;
    private int speed;
    private float range;
    private int sellCost;
    private Bullet bullet;
    private Enemy target;
    private List<String> textureLocations;

    public Turret(List<String> textureLocations, float width, float height, Point3f centre, String type, int cost, int speed, float range, Bullet bullet) {
        super(textureLocations.get(0), width, height, centre);
        this.textureLocations = textureLocations;
        this.type = type;
        this.level = 0;
        this.cost = cost;
        this.speed = speed;
        this.range = range;
        this.bullet = bullet;
        this.target = null;
        this.sellCost = 0;
    }

    public void upgradeTurret(){
        this.level++;
        this.range +=25;
        this.bullet.setDamage(getBullet().getDamage()+10);
        this.sellCost = cost/5;
        this.cost = this.level*this.level * 20;
        if(this.speed > 1)
            this.speed-= 1;
        if (this.level > 0 && this.level <= 6 && this.level%2==0){
            super.setTextureLocation(this.textureLocations.get(this.level/2));
        }
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

    public int getSellCost() {
        return sellCost;
    }

    public void setSellCost(int sellCost) {
        this.sellCost = sellCost;
    }

    public List<String> getTextureLocations() {
        return textureLocations;
    }

    public void setTextureLocations(List<String> textureLocations) {
        this.textureLocations = textureLocations;
    }
}