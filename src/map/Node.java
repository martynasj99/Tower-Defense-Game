package map;

import util.Point3f;

import java.awt.*;

public class Node {

    private int width;
    private int height;
    private Color color;
    private Point3f position;
    private boolean isAvailable; //DEFINES WHETHER OR NOT YOU CAN PLACE TOWERS ON THIS NODE

    public Node(int width, int height, Color color, Point3f position, boolean isAvailable) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.position = position;
        this.isAvailable = isAvailable;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
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
}
