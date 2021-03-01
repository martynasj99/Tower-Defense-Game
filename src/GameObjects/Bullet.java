package GameObjects;

import util.GameObject;
import util.Point3f;
import util.Vector3f;
/**
 * Student Name: Martynas Jagutis
 * Student Number: 17424866
 */
public class Bullet extends GameObject {

    private Vector3f direction;
    private int damage;

    public Bullet(String textureLocation, int width, int height, Point3f centre, int damage) {
        super(textureLocation, width, height, centre);
        this.direction = new Vector3f();
        this.damage = damage;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
