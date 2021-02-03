package GameObjects;

import util.GameObject;
import util.Point3f;
import util.Vector3f;

public class Bullet extends GameObject {

    private Vector3f direction;

    public Bullet(String textureLocation, int width, int height, Point3f centre) {
        super(textureLocation, width, height, centre);
        this.direction = new Vector3f();
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}
