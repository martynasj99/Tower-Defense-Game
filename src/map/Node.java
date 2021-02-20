package map;

import GameObjects.Turret;
import util.Point3f;

import java.awt.*;

public class Node {

    private float width;
    private float height;
    private Color color;
    private Point3f position;
    private boolean isAvailable; //DEFINES WHETHER OR NOT YOU CAN PLACE TOWERS ON THIS NODE
    private Turret turret;


    public Node(float width, float height, Color color, Point3f position, boolean isAvailable) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.position = position;
        this.isAvailable = isAvailable;
        this.turret = null;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point3f getPosition() {
        return position;
    }

    public void setPosition(Point3f position) {
        this.position = position;
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
