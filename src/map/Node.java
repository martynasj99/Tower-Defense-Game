package map;

import GameObjects.Turret;
import util.GameObject;
import util.Point3f;

import java.awt.*;

public class Node extends GameObject {

    private boolean isAvailable; //DEFINES WHETHER OR NOT YOU CAN PLACE TOWERS ON THIS NODE
    private Turret turret;

    public Node(String textureLocation,float width,float height, Point3f centre, boolean isAvailable) {
        super(textureLocation, width, height, centre);
        this.isAvailable = isAvailable;
        this.turret = null;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Turret getTurret() {
        return turret;
    }

    public void setTurret(Turret turret) {
        this.turret = turret;
    }
}
