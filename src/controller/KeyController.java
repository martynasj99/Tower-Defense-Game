package controller;

import java.awt.event.*;
/**
 * Student Name: Martynas Jagutis
 * Student Number: 17424866
 */
public class KeyController implements KeyListener {

	private static boolean Key1Pressed = false;
	private static boolean Key2Pressed = false;
	private static boolean Key3Pressed = false;
	private static boolean Key4Pressed = false;
	private static boolean KeyAPressed = false;
	private static boolean KeyDPressed = false;
	private static boolean KeySpacePressed = false;

	private static final KeyController instance = new KeyController();

	public KeyController() {
	}

	public static KeyController getInstance(){
	        return instance;
	    }

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
			case '1':
				setKey1Pressed(true);
				break;
			case '2':
				setKey2Pressed(true);
				break;
			case '3':
				setKey3Pressed(true);
				break;
			case '4':
				setKey4Pressed(true);
				break;
			case 'A':
				setKeyAPressed(true);
				break;
			case 'D':
				setKeyDPressed(true);
				break;
			case ' ':
				setKeySpacePressed(true);
				break;
			default:
				System.out.println("Controller test:  Unknown key pressed");
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyChar()) {
			case '1':
				setKey1Pressed(false);
				break;
			case '2':
				setKey2Pressed(false);
				break;
			case '3':
				setKey3Pressed(false);
				break;
			case '4':
				setKey4Pressed(false);
				break;
			case 'A':
				setKeyAPressed(false);
				break;
			case 'D':
				setKeyDPressed(false);
				break;
			case ' ':
				setKeySpacePressed(false);
				break;
			default:
				System.out.println("Controller test:  Unknown key pressed");
				break;
		}
	}

	public boolean isKey1Pressed() {
		return Key1Pressed;
	}

	public void setKey1Pressed(boolean key1Pressed) {
		Key1Pressed = key1Pressed;
	}

	public boolean isKey2Pressed() {
		return Key2Pressed;
	}

	public void setKey2Pressed(boolean key2Pressed) {
		Key2Pressed = key2Pressed;
	}

	public boolean isKey3Pressed() {
		return Key3Pressed;
	}

	public void setKey3Pressed(boolean key3Pressed) {
		Key3Pressed = key3Pressed;
	}

	public boolean isKey4Pressed() {
		return Key4Pressed;
	}

	public void setKey4Pressed(boolean key4Pressed) {
		Key4Pressed = key4Pressed;
	}

	public boolean isKeyAPressed() {
		return KeyAPressed;
	}

	public void setKeyAPressed(boolean keyAPressed) {
		KeyAPressed = keyAPressed;
	}

	public boolean isKeyDPressed() {
		return KeyDPressed;
	}

	public void setKeyDPressed(boolean keyDPressed) {
		KeyDPressed = keyDPressed;
	}

	public boolean isKeySpacePressed() {
		return KeySpacePressed;
	}

	public void setKeySpacePressed(boolean keySpacePressed) {
		KeySpacePressed = keySpacePressed;
	}
}