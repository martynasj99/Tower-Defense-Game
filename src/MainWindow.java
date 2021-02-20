import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.Clip;
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
	private final int INFO_PANEL_LENGTH = 200;
	private final int DEFAULT_INFO_HEIGHT = 25;

	private int nextInfoOffset = 0;

	 private static JFrame frame = new JFrame("Tower Defense Game");
	 private static Model gameWorld = new Model();
	 private static Viewer canvas = new Viewer(gameWorld);
	 private Controller controller = new Controller();
	 private MapManager mapManager = MapManager.getInstance();
	 private AudioManager audioManager = AudioManager.getInstance();
	 private static GameManager gameManager = GameManager.getInstance();

	 private static int TargetFPS = 100;
	 private static boolean startGame= false;

	 private static JLabel infoText;
	 private static JPanel selectedPanel;
	 private static JLabel selectedTurretText;
	 private static JButton upgradeButton;
	 private static JButton deleteButton;
	 private static JButton nextWave;
	 private static JList selectTurret;
	 private static JButton pauseButton;
	 private static JButton exitGameButton;
	 private static JButton startMenuButton;
	 private static List<JButton> mapButtons;
	 private JButton muteButton;


	 private static Clip backgroundClip;

	public MainWindow() {
		frame.setSize(mapManager.getSCREEN_WIDTH()+INFO_PANEL_LENGTH, mapManager.getSCREEN_HEIGHT());  // you can customise this later and adapt it to change on size.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //If exit // you can modify with your way of quitting , just is a template.
		frame.setLayout(null);
		frame.add(canvas, BorderLayout.CENTER);
		canvas.setBounds(0, 0, mapManager.getSCREEN_WIDTH(), mapManager.getSCREEN_HEIGHT());
		canvas.setBackground(new Color(255,255,255)); //white background  replaced by Space background but if you remove the background method this will draw a white screen
		canvas.setVisible(false); // this will become visible after you press the key.

		backgroundClip = audioManager.playSound("res/music/background-music.wav");
		backgroundClip.start();

		mapButtons = new ArrayList<>();

		setUpStartMenu();
		setUpMaps();
		setUpInfoText();
		setUpSelectTurrets();
		setUpSelectedPanel();
		setUpNextWaveButton();
		setUpPause();
		setUpMute();
		setUpExit();

		canvas.addMouseListener(controller);
		canvas.addMouseMotionListener(controller);
		canvas.requestFocusInWindow(); // making sure that the Canvas is in focus so keyboard input will be taking in .

		changeGameState(true);

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
				if(startGame && !gameManager.isPaused()) {
					gameLoop();
				}
				UnitTests.CheckFrameRate(System.currentTimeMillis(),FrameCheck, TargetFPS);
			}
		});
		game.start();
	}

	private static void gameLoop() {
		canvas.updateview();
		gameWorld.gameLogic();

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
			showSelectedTurret(true);
		}
		if(gameManager.getLives() <= 0){
			changeGameState(true);
		}
	}

	private void setUpInfoText(){
		final int NO_OF_INFO = 3;
		infoText = new JLabel();
		frame.add(infoText);
		infoText.setBounds(mapManager.getSCREEN_WIDTH(),DEFAULT_INFO_HEIGHT*nextInfoOffset ,INFO_PANEL_LENGTH,DEFAULT_INFO_HEIGHT*NO_OF_INFO);
		nextInfoOffset+= NO_OF_INFO;
	}

	private void setUpSelectedPanel(){
		final int NO_INFO = 5;
		selectedPanel = new JPanel();
		selectedTurretText = new JLabel();
		upgradeButton = new JButton("Upgrade");
		deleteButton = new JButton("Sell");
		selectedPanel.add(selectedTurretText);
		selectedPanel.add(upgradeButton);
		selectedPanel.add(deleteButton);
		frame.add(selectedPanel);
		selectedPanel.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*NO_INFO);
		nextInfoOffset += NO_INFO;

		upgradeButton.addActionListener(e ->{
			if(gameManager.getCoins() >= gameManager.getSelected().getTurret().getCost()) {
				gameManager.changeCoins(-gameManager.getSelected().getTurret().getCost());
				gameManager.getSelected().getTurret().upgradeTurret();
			}
			else System.out.println("NOT ENOUGH COINS FOR UPGRADE!!!");
		});

		deleteButton.addActionListener(e ->{
			gameWorld.deleteTurret();
			showSelectedTurret(false);
		});
	}

	private static void showSelectedTurret(boolean isShow){
		upgradeButton.setVisible(isShow);
		deleteButton.setVisible(isShow);
		selectedPanel.setVisible(isShow);
	}

	private void setUpNextWaveButton(){
		nextWave = new JButton("Next Wave");
		frame.add(nextWave);
		nextWave.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT);
		nextInfoOffset++;
		nextWave.addActionListener(e -> gameWorld.startWave());
	}

	private void setUpStartMenu(){
		startMenuButton = new JButton("Start Game");
		startMenuButton.setBounds(400, 500, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*2);

		startMenuButton.addActionListener(e -> {
			changeGameState(false);
			gameWorld.startWave();
			gameWorld.scheduleFire();
		});
		frame.add(startMenuButton);
	}

	private void setUpSelectTurrets(){
		final int NO_TURRETS = gameManager.getTurretTypes().size();

		String[] options = new String[gameManager.getTurretTypes().size()];
		for (int i = 0; i < options.length; i++){
			Turret turret = gameManager.getTurretTypes().get(i);
			options[i] =  turret.getType() + " (Cost: " + turret.getCost() + ")";
		}

		selectTurret = new JList(options);
		selectTurret.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*NO_TURRETS);
		nextInfoOffset += NO_TURRETS;
		selectTurret.setSelectedIndex(0);
		gameManager.setSelectedTurret(0);
		selectTurret.addListSelectionListener(e -> gameManager.setSelectedTurret(selectTurret.getSelectedIndex()));
		frame.add(selectTurret);
	}

	private void setUpPause(){
		pauseButton = new JButton("Pause Game");
		pauseButton.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT);
		nextInfoOffset++;
		pauseButton.addActionListener(e -> {
			gameManager.setPaused(!gameManager.isPaused());
			if (gameManager.isPaused()) {
				pauseButton.setText("Resume Game");
			} else {
				pauseButton.setText("Pause Game");
			}
		});
		frame.add(pauseButton);
	}

	private void setUpMaps(){
		String[] mapImages = {"res/map/map01.jpg", "res/map/map02.jpg", "res/map/map03.jpg"};
		for(int i = 0; i < mapManager.getMaps().size(); i++){
			int height = mapManager.getSCREEN_HEIGHT()/3;
			int width = (mapManager.getSCREEN_WIDTH()+INFO_PANEL_LENGTH)/mapManager.getMaps().size();

			//reference: https://stackoverflow.com/questions/13810213/java-stretch-icon-to-fit-button
			JButton map = new JButton(new ImageIcon(((new ImageIcon(mapImages[i]).getImage()
					.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH)))));
			map.setBounds(width*i, 100, width, height);
			mapButtons.add(map);
			map.addActionListener(e -> {
				mapManager.setCurrentMap(mapButtons.indexOf(map));
				gameManager.initializeEnemies();
				for(JButton button : mapButtons) button.setEnabled(true);
				map.setEnabled(false);
			});
			frame.add(map);
		}
	}

	private void setUpMute(){
		String[] icons = {"res/icon/mute.png", "res/icon/sound.png"};
		//reference: https://stackoverflow.com/questions/13810213/java-stretch-icon-to-fit-button
		muteButton = new JButton(new ImageIcon(((new ImageIcon(icons[audioManager.isMute()?0:1]).getImage()
				.getScaledInstance(DEFAULT_INFO_HEIGHT*2, DEFAULT_INFO_HEIGHT*2, java.awt.Image.SCALE_SMOOTH)))));
		muteButton.setBackground(Color.WHITE);
		muteButton.setBounds(mapManager.getSCREEN_WIDTH()+INFO_PANEL_LENGTH-DEFAULT_INFO_HEIGHT*2, 0, DEFAULT_INFO_HEIGHT*2, DEFAULT_INFO_HEIGHT*2);
		muteButton.addActionListener(e ->{
			audioManager.setMute(!audioManager.isMute());
			muteButton.setIcon(new ImageIcon(((new ImageIcon(
					icons[audioManager.isMute() ? 0:1]).getImage()
					.getScaledInstance(DEFAULT_INFO_HEIGHT*2, DEFAULT_INFO_HEIGHT*2,
							java.awt.Image.SCALE_SMOOTH)))));
			if(audioManager.isMute()) backgroundClip.stop();
			else backgroundClip.start();
		});
		frame.add(muteButton);
	}

	private void setUpExit(){
		exitGameButton = new JButton("Menu");
		exitGameButton.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT);
		nextInfoOffset++;
		exitGameButton.addActionListener(e ->{
			changeGameState(true);
			gameWorld.clearAll();
			gameManager.reset();
			mapManager.configureMap();
		});
		frame.add(exitGameButton);
	}

	private static void changeGameState(boolean isEnd){
		startMenuButton.setVisible(isEnd);
		canvas.setVisible(!isEnd);
		infoText.setVisible(!isEnd);
		selectTurret.setVisible(!isEnd);
		pauseButton.setVisible(!isEnd);
		nextWave.setVisible(!isEnd);
		exitGameButton.setVisible(!isEnd);
		for (JButton mapButton : mapButtons) mapButton.setVisible(isEnd);
		startGame=!isEnd;
		showSelectedTurret(false);
	}
}