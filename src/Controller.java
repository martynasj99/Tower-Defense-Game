import util.Point3f;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * Created by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   
   (MIT LICENSE ) e.g do what you want with this :-) 
 */ 

//Singeton pattern
public class Controller implements MouseListener, MouseMotionListener {

	private static boolean MouseClicked = false;
	private static final Controller instance = new Controller();
	private static Point3f mouseClickPosition = new Point3f();
	private static Point3f mouseMovePosition = new Point3f();
	   
	public Controller() {
	}
	 
	public static Controller getInstance(){
	        return instance;
	    }

	public void mouseClicked(MouseEvent e) {
		mouseClickPosition.setX(e.getX());
		mouseClickPosition.setY(e.getY());
		setMouseClicked(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setMouseClicked(false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("X: " + e.getX() + "Y: " + e.getY());
		setMouseMovePosition(new Point3f(e.getX(), e.getY(), 0));
	}


	public static boolean isMouseClicked() {
		return MouseClicked;
	}

	public void setMouseClicked(boolean mouseClicked) {
		MouseClicked = mouseClicked;
	}

	public Point3f getMouseClickPosition() {
		return mouseClickPosition;
	}

	public void setMouseClickPosition(Point3f mouseClickPosition) {
		this.mouseClickPosition = mouseClickPosition;
	}

	public Point3f getMouseMovePosition() {
		return mouseMovePosition;
	}

	public static void setMouseMovePosition(Point3f mouseMovePosition) {
		Controller.mouseMovePosition = mouseMovePosition;
	}
}