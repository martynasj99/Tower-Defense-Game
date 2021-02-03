import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;

import map.MapManager;
import util.UnitTests;

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

public class MainWindow {
	 private static JFrame frame = new JFrame("Tower Defense Game");   // Change to the name of your game
	 private static Model gameworld = new Model();
	 private static Viewer canvas = new Viewer(gameworld);
	 private MouseListener Controller = new Controller();
	 private MapManager mapManager = MapManager.getInstance();

	 private static int TargetFPS = 100;
	 private static boolean startGame= false;

	 private JLabel backgroundImageForStartMenu;
	  
	public MainWindow() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(mapManager.getScreenWidth()+200, mapManager.getScreenHeight());  // you can customise this later and adapt it to change on size.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //If exit // you can modify with your way of quitting , just is a template.
		frame.setLayout(null);
		frame.add(canvas);
		canvas.setBounds(0, 0, mapManager.getScreenWidth()+200, mapManager.getScreenHeight());
		canvas.setBackground(new Color(255,255,255)); //white background  replaced by Space background but if you remove the background method this will draw a white screen
		canvas.setVisible(false); // this will become visible after you press the key.

		JButton startMenuButton = new JButton("Start Game");
        startMenuButton.setBounds(400, 500, 200, 40);
		startMenuButton.setVisible(true);



		startMenuButton.addActionListener(e -> {
			startMenuButton.setVisible(false);
			backgroundImageForStartMenu.setVisible(false);
			canvas.setVisible(true);
			canvas.addMouseListener(Controller);
			canvas.requestFocusInWindow(); // making sure that the Canvas is in focus so keyboard input will be taking in .
			startGame=true;
			scheduleSpawn();
			scheduleFire();
		});

		File BackgroundToLoad = new File("res/startscreen.png");
		try {
			BufferedImage myPicture = ImageIO.read(BackgroundToLoad);
			backgroundImageForStartMenu = new JLabel(new ImageIcon(myPicture));
			backgroundImageForStartMenu.setBounds(0, 0, mapManager.getScreenWidth()+200, mapManager.getScreenHeight());
			frame.add(backgroundImageForStartMenu);
		} catch (IOException e) {
			e.printStackTrace();
		}

		frame.add(startMenuButton);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		MainWindow main = new MainWindow();

		Thread game = new Thread(() -> {
			while(true){
				//swing has timer class to help us time this but I'm writing my own, you can of course use the timer, but I want to set FPS and display it
				int TimeBetweenFrames =  1000 / TargetFPS;
				long FrameCheck = System.currentTimeMillis() + (long) TimeBetweenFrames;

				while (FrameCheck > System.currentTimeMillis()){} //wait till next time step
				if(startGame) {
					gameloop();
				}
				UnitTests.CheckFrameRate(System.currentTimeMillis(),FrameCheck, TargetFPS);
			}
		});

		game.run();
	}
	private void scheduleSpawn(){
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				gameworld.spawn();
			}
		};
		Timer timer = new Timer("spawner");
		timer.scheduleAtFixedRate(task, 2000L, 500L);
	}

	private void scheduleFire(){
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				gameworld.fire();
			}
		};
		Timer timer = new Timer("spawner");
		timer.scheduleAtFixedRate(task, 1000L, 2000L);
	}

	private static void gameloop() {
		//TODO Set up as thread.
		canvas.updateview();
		gameworld.gamelogic();
		frame.setTitle("Score =  "+ gameworld.getScore());

	}
}