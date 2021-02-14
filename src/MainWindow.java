import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import GameObjects.Turret;
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
	 private Controller controller = new Controller();
	 private MapManager mapManager = MapManager.getInstance();
	 private static GameManager gameManager = GameManager.getInstance();

	 private static int TargetFPS = 100;
	 private static boolean startGame= false;

	 private JLabel backgroundImageForStartMenu;
	 private static JLabel infoText;

	 private static JPanel selectedPanel;
	 private static JLabel selectedTurretText;
	 private static JButton upgradeButton;
	 private static JButton deleteButton;
	 private static JButton nextWave;
	 private static JList selectTurret;

	 private static int step;

	public MainWindow() {
		step = 0;
		frame.setSize(mapManager.getScreenWidth()+200, mapManager.getScreenHeight());  // you can customise this later and adapt it to change on size.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //If exit // you can modify with your way of quitting , just is a template.
		frame.setLayout(null);
		frame.add(canvas, BorderLayout.CENTER);
		canvas.setBounds(0, 0, mapManager.getScreenWidth(), mapManager.getScreenHeight());
		canvas.setBackground(new Color(255,255,255)); //white background  replaced by Space background but if you remove the background method this will draw a white screen
		canvas.setVisible(false); // this will become visible after you press the key.


		setUpInfoText();
		setUpSelectedPanel();
		setUpNextWaveButton();
		setUpStartMenu();
		setUpSelectTurrets();

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

	private static void gameloop() {
		//TODO Set up as thread.
		canvas.updateview();
		gameworld.gamelogic();
		infoText.setText("<html>Coins: " + gameManager.getCoins() +
				"<br/>Round: " + gameManager.getRound() +
				"<br/>Lives: " + gameManager.getLives() + "</html>");
		if(gameManager.getSelected() != null) {
			Turret selectedTurret = gameManager.getSelected().getTurret();
			selectedTurretText.setText("<html>Level: " + selectedTurret.getLevel() +
					"<br/>Range: " + selectedTurret.getRange() +
					"<br/>Damage: " + selectedTurret.getBullet().getDamage() +
					"<br/>Rate: " + selectedTurret.getSpeed() +
					"<br/>Cost: " + selectedTurret.getCost() + "</html>");
			deleteButton.setText("Sell ("+gameManager.getSelected().getTurret().getSellCost()+")");
			upgradeButton.setVisible(true);
			deleteButton.setVisible(true);
			selectedPanel.setVisible(true);
			if(gameManager.getLives() == 0){
				//TODO END GAME
			}
			//if(gameManager.getNextEnemy() == null) nextWave.setVisible(true);
		}
	}


	private void setUpInfoText(){
		infoText = new JLabel();
		frame.add(infoText);
		infoText.setBounds(mapManager.getScreenWidth(),0,200,50);
		infoText.setVisible(false);
	}

	private void setUpSelectedPanel(){
		selectedPanel = new JPanel();
		selectedTurretText = new JLabel();
		upgradeButton = new JButton("Upgrade");
		deleteButton = new JButton("Sell");
		selectedPanel.add(selectedTurretText);
		selectedPanel.add(upgradeButton);
		selectedPanel.add(deleteButton);
		frame.add(selectedPanel);
		selectedPanel.setBounds(mapManager.getScreenWidth(), mapManager.getScreenHeight()/2,200, mapManager.getScreenHeight()/2);
		upgradeButton.setVisible(false);
		deleteButton.setVisible(false);
		selectedPanel.setVisible(false);

		upgradeButton.addActionListener(e ->{
			if(gameManager.getCoins() >= gameManager.getSelected().getTurret().getCost()) {
				gameManager.changeCoins(-gameManager.getSelected().getTurret().getCost());
				gameManager.getSelected().getTurret().upgradeTurret();
			}
			else System.out.println("NOT ENOUGH COINS FOR UPGRADE!!!");
		});

		deleteButton.addActionListener(e ->{
			gameworld.deleteTurret();
			upgradeButton.setVisible(false);
			deleteButton.setVisible(false);
			selectedPanel.setVisible(false);
		});
	}

	private void setUpNextWaveButton(){
		nextWave = new JButton("Next Wave");
		frame.add(nextWave);
		nextWave.setBounds(mapManager.getScreenWidth(), mapManager.getScreenHeight()-50, 100, 50);
		nextWave.setVisible(true);
		nextWave.addActionListener(e -> gameworld.startWave());
	}

	private void setUpStartMenu(){
		JButton startMenuButton = new JButton("Start Game");
		startMenuButton.setBounds(400, 500, 200, 40);
		startMenuButton.setVisible(true);

		startMenuButton.addActionListener(e -> {
			startMenuButton.setVisible(false);
			canvas.setVisible(true);
			infoText.setVisible(true);
			selectTurret.setVisible(true);
			canvas.addMouseListener(controller);
			canvas.addMouseMotionListener(controller);
			canvas.requestFocusInWindow(); // making sure that the Canvas is in focus so keyboard input will be taking in .
			startGame=true;
			gameworld.startWave();
			gameworld.scheduleFire();
		});
		frame.add(startMenuButton);
	}

	private void setUpSelectTurrets(){
		String[] options = new String[gameManager.getTurretTypes().size()];
		for (int i = 0; i < options.length; i++){
			Turret turret = gameManager.getTurretTypes().get(i);
			options[i] =  turret.getType() + " (Cost: " + turret.getCost() + ")";
		}

		selectTurret = new JList(options);
		selectTurret.setBounds(mapManager.getScreenWidth(), 50, 200, 100);
		selectTurret.setSelectedIndex(0);
		gameManager.setSelectedTurret(0);
		selectTurret.addListSelectionListener(e -> gameManager.setSelectedTurret(selectTurret.getSelectedIndex()));
		selectTurret.setVisible(false);
		frame.add(selectTurret);

	}

	private void setUpBackground(){
		File BackgroundToLoad = new File("res/startscreen.png");
		try {
			BufferedImage myPicture = ImageIO.read(BackgroundToLoad);
			backgroundImageForStartMenu = new JLabel(new ImageIcon(myPicture));
			backgroundImageForStartMenu.setBounds(0, 0, mapManager.getScreenWidth()+200, mapManager.getScreenHeight());
			frame.add(backgroundImageForStartMenu);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}