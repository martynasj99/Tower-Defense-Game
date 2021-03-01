import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.Clip;
import javax.swing.*;
import GameObjects.Turret;
import controller.Controller;
import controller.KeyController;
import manager.AudioManager;
import manager.GameManager;
import map.MapEditor;
import manager.MapManager;
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
/**
 * Student Name: Martynas Jagutis
 * Student Number: 17424866
 */
/**
 * Declaration of Authorship
 * I declare that all material in this assessment is my own work except where there is clear
 * acknowledgement and appropriate reference to the work of others.
 */
public class MainWindow {
	private final int INFO_PANEL_LENGTH = 200;
	private final int DEFAULT_INFO_HEIGHT = 25;

	private int nextInfoOffset = 0;

	 private static JFrame frame = new JFrame("Tower Defense Game");
	 private static Model gameWorld = new Model();
	 private static Viewer canvas = new Viewer(gameWorld);
	 private static Controller controller = new Controller();
	 private static KeyController keyController = new KeyController();
	 private static MapManager mapManager = MapManager.getInstance();
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
	 private static JButton exitGameButton;
	 private static JButton startMenuButton;
	 private static List<JButton> mapButtons;
	 private static List<JButton> difficultButtons;
	 private static List<JButton> gameSpeedButtons;
	 private static JButton mapEditorButton;

	 private static JButton saveNewMapButton;
	 private static JList selectTile;
	 private static JButton exitMapEditorButton;
	 private static JLabel helpText;
	 private static JLabel errorMessageText;

	 private static MapEditor mapEditor = new MapEditor();

	 private JButton muteButton;

	 private static Clip backgroundClip;

	public MainWindow() {
		frame.setSize(mapManager.getSCREEN_WIDTH()+INFO_PANEL_LENGTH, mapManager.getSCREEN_HEIGHT());  // you can customise this later and adapt it to change on size.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //If exit // you can modify with your way of quitting , just is a template.
		frame.setLayout(null);
		frame.add(canvas, BorderLayout.CENTER);
		canvas.setBounds(0, 0, mapManager.getSCREEN_WIDTH(), mapManager.getSCREEN_HEIGHT());
		canvas.setBackground(new Color(0,0,0)); //white background  replaced by Space background but if you remove the background method this will draw a white screen
		canvas.setVisible(false); // this will become visible after you press the key.

		backgroundClip = audioManager.playSound("res/music/background-music.wav");
		backgroundClip.start();

		setUpStartMenu();
		setUpMaps();
		setUpInfoText();
		setUpSelectTurrets();
		setUpSelectedPanel();
		setUpNextWaveButton();
		setUpMute();
		setUpExit();
		setUpMapEditor();
		setUpDifficulty();
		setUpGameSpeedControl();
		setUpErrorMessage();
		setUpHelpText();

        frame.addKeyListener(keyController);
        canvas.addMouseListener(controller);
        canvas.addMouseMotionListener(controller);
		canvas.requestFocusInWindow(); // making sure that the Canvas is in focus so keyboard input will be taking in .

		mapManager.configureMap();
		changeGameState(true);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		MainWindow main = new MainWindow();

		Thread game = new Thread(() -> {
			while(true){
				int TimeBetweenFrames =  1000 / TargetFPS;
				long FrameCheck = System.currentTimeMillis() + (long) TimeBetweenFrames;

				while (FrameCheck > System.currentTimeMillis()){} //wait till next time step

				if(gameManager.isInEditor() && Controller.getInstance().isMouseDragged()){
					float x = controller.getMouseDraggedPosition().getX();
					float y = controller.getMouseDraggedPosition().getY();

					int nodeX = (int) (x/ mapManager.getCurrentGameMap().getNodeWidth());
					int nodeY = (int) (y/ mapManager.getCurrentGameMap().getNodeHeight());
					if(nodeX < mapEditor.getMapConfig().length && nodeX >= 0 && nodeY < mapEditor.getMapConfig()[0].length && nodeY >= 0 ){
						mapEditor.updateConfig(nodeX, nodeY);
						mapManager.getGameMaps().get(3).setConfiguration(mapEditor.getMapConfig());
						mapManager.getGameMaps().get(3).configure();
						canvas.updateview();
					}
				}
				if(startGame && gameManager.getGameSpeed() != GameManager.GameSpeed.PAUSED) {
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
				"<br/>Round: " + gameManager.getRound() + "/" + gameManager.getNO_ROUNDS() +
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

		if(gameManager.getLives() <= 0 ){
		    JOptionPane.showMessageDialog(frame, "You survived " + gameManager.getRound() + " rounds!");
			changeGameState(true);
		}
		else if(gameManager.getRound() == gameManager.getNO_ROUNDS()){
		    JOptionPane.showMessageDialog(frame, "You won the game! Congratulations!");
            changeGameState(true);
        }

        if(keyController.isKey1Pressed()){
            selectTurret.setSelectedIndex(0);
        }else if(keyController.isKey2Pressed()){
            selectTurret.setSelectedIndex(1);
        }else if(keyController.isKey3Pressed()){
            selectTurret.setSelectedIndex(2);
        }else if(keyController.isKey4Pressed()) {
            selectTurret.setSelectedIndex(3);
        }
        errorMessageText.setText(gameManager.getErrorMessage());

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
		upgradeButton.setFocusable(false);
		deleteButton.setFocusable(false);

		frame.add(selectedPanel);
		selectedPanel.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*NO_INFO);
		nextInfoOffset += NO_INFO;
		selectedPanel.setFocusable(false);

		upgradeButton.addActionListener(e ->{
			if(gameManager.getCoins() >= gameManager.getSelected().getTurret().getCost()) {
				gameManager.changeCoins(-gameManager.getSelected().getTurret().getCost());
				gameManager.getSelected().getTurret().upgradeTurret();
			}
			else gameManager.setErrorMessage("NOT ENOUGH COINS!");
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
		nextWave.setFocusable(false);
		frame.add(nextWave);
		nextWave.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT);
		nextInfoOffset++;
		nextWave.addActionListener(e -> gameWorld.startWave());
	}

	private void setUpStartMenu(){
		startMenuButton = new JButton("Start Game");
		startMenuButton.setBounds(400, 600, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*2);

		startMenuButton.setFocusable(false);
		startMenuButton.addActionListener(e -> {
			changeGameState(false);
		});
		frame.add(startMenuButton);
	}

	private void setUpSelectTurrets(){
		final int NO_TURRETS = gameManager.getTurretTypes().size();

		String[] options = new String[gameManager.getTurretTypes().size()];
		for (int i = 0; i < options.length; i++){
			Turret turret = gameManager.getTurretTypes().get(i);
			options[i] =  turret.getType() + " (Cost: " + turret.getCost() + ") ["+(i+1)+"]";
		}

		selectTurret = new JList(options);
		selectTurret.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*NO_TURRETS);
		nextInfoOffset += NO_TURRETS;
		selectTurret.setSelectedIndex(0);
		gameManager.setSelectedTurret(0);
		selectTurret.addListSelectionListener(e -> gameManager.setSelectedTurret(selectTurret.getSelectedIndex()));

		selectTurret.setFocusable(false);
		frame.add(selectTurret);
	}

	private void setUpMaps(){
		List<String> mapImages = Arrays.asList("res/map/map01.jpg", "res/map/map02.jpg", "res/map/map03.jpg", "res/map/map04.jpg");
		mapButtons = new ArrayList<>();
		for(int i = 0; i < mapManager.getGameMaps().size(); i++){
			int height = mapManager.getSCREEN_HEIGHT()/3;
			int width = (mapManager.getSCREEN_WIDTH()+INFO_PANEL_LENGTH)/mapManager.getGameMaps().size();

			//reference: https://stackoverflow.com/questions/13810213/java-stretch-icon-to-fit-button
			JButton map = new JButton(new ImageIcon(((new ImageIcon(mapImages.get(i)).getImage()
					.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH)))));
			map.setBounds(width*i, 100, width, height);
			map.setFocusable(false);
			mapButtons.add(map);
			map.addActionListener(e -> {
				mapManager.setCurrentGameMap(mapButtons.indexOf(map));
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
		muteButton.setFocusable(false);
		frame.add(muteButton);
	}

	private void setUpGameSpeedControl(){
		//<div>Icons made by <a href="https://www.flaticon.com/authors/pixel-perfect" title="Pixel perfect">Pixel perfect</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
		//<div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
		gameSpeedButtons = new ArrayList<>();
		String[] icons = {"res/icon/pause.png", "res/icon/play.png", "res/icon/fast-forward.png"};
		//reference: https://stackoverflow.com/questions/13810213/java-stretch-icon-to-fit-button
		for(int i = 0; i < icons.length; i++){
			JButton button = new JButton(new ImageIcon(((new ImageIcon(icons[i]).getImage()
					.getScaledInstance(DEFAULT_INFO_HEIGHT*2, DEFAULT_INFO_HEIGHT*2, java.awt.Image.SCALE_SMOOTH)))));
			button.setBackground(Color.WHITE);
			button.setBounds(mapManager.getSCREEN_WIDTH()+ (INFO_PANEL_LENGTH/3 *i), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH/3, DEFAULT_INFO_HEIGHT*2);

			button.setFocusable(false);
			gameSpeedButtons.add(button);
			button.addActionListener(e ->{
				switch (gameSpeedButtons.indexOf(button)){
					case 0:
						gameManager.setGameSpeed(GameManager.GameSpeed.PAUSED);
						break;
					case 1:
						gameManager.setGameSpeed(GameManager.GameSpeed.NORMAL);
						break;
					case 2:
						gameManager.setGameSpeed(GameManager.GameSpeed.FAST);
						break;
					default:
						break;
				}
				for(JButton btn : gameSpeedButtons) btn.setEnabled(true);
				button.setEnabled(false);
			});
			frame.add(button);
		}
        nextInfoOffset +=2;
		gameSpeedButtons.get(1).setEnabled(false);
	}

	private void setUpExit(){
		exitGameButton = new JButton("Menu");
		exitGameButton.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT);
		nextInfoOffset++;
		exitGameButton.addActionListener(e ->{
		    int n = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
		    if(n == 0)
			    changeGameState(true);
		});
		exitGameButton.setFocusable(false);
		frame.add(exitGameButton);
	}

	private void setUpDifficulty(){
		String[] difficulties = {"EASY", "MEDIUM", "HARD"};
		difficultButtons = new ArrayList<>();
		for (int i = 0; i < difficulties.length; i++) {
			JButton button = new JButton(difficulties[i]);
			button.setBounds((mapManager.getSCREEN_WIDTH()/2)-(((INFO_PANEL_LENGTH/2)*3)/2)+(INFO_PANEL_LENGTH/2)*(i+1), 500, INFO_PANEL_LENGTH/2, DEFAULT_INFO_HEIGHT*2);
			difficultButtons.add(button);
			button.addActionListener(e ->{
				gameManager.setDifficulty(difficultButtons.indexOf(button));
				for(JButton difButton : difficultButtons) difButton.setEnabled(true);
				button.setEnabled(false);
			});
            button.setFocusable(false);
			frame.add(button);
		}
		difficultButtons.get(1).setEnabled(false); //default
	}

	private void setUpMapEditor(){
		String[] tileTypes = {"grass", "dirt", "start"};

		mapEditorButton = new JButton("Map Editor");
		saveNewMapButton = new JButton("Save");
		exitMapEditorButton = new JButton("Exit");
		selectTile = new JList(tileTypes);

		mapEditorButton.setBounds(mapManager.getSCREEN_WIDTH()/2, 400, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*2);
		saveNewMapButton.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*2 + DEFAULT_INFO_HEIGHT*tileTypes.length, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT);
		exitMapEditorButton.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*3 + DEFAULT_INFO_HEIGHT*tileTypes.length, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT);
		selectTile.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*2, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*tileTypes.length);

		mapEditorButton.addActionListener(e ->{
			mapManager.setCurrentGameMap(3);
			changeToMapEditorState(true);
		});

		selectTile.addListSelectionListener(e -> mapEditor.setSelectedTexture(selectTile.getSelectedIndex()));

		saveNewMapButton.addActionListener(e ->{
			mapManager.getGameMaps().get(3).setConfiguration(mapEditor.getMapConfig());
			mapManager.getGameMaps().get(3).configure();
			changeToMapEditorState(false);
			changeGameState(true);
			gameManager.init();
		});
		mapManager.setCurrentGameMap(mapManager.getGameMaps().size()-1);
		canvas.setVisible(true);
        mapEditorButton.setFocusable(false);
        saveNewMapButton.setFocusable(false);
		frame.add(mapEditorButton);
		frame.add(saveNewMapButton);
		frame.add(selectTile);
	}

	private void setUpHelpText(){
		helpText = new JLabel();
		helpText.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*12);

		nextInfoOffset += 11;
		helpText.setText("<html><center>Help</center>" +
				"<hr><center>Complete "+gameManager.getNO_ROUNDS()+" rounds to win</center>" +
				"<hr><center>Select a turret and click on the tile to build it</center>" +
				"<hr><center>Select a turret in the map to upgrade or sell it</center>" +
				"<hr><center>Click the next wave button the spawn the next round/wave of enemies</center>" +
				"<hr><center>Actions that can be performed by the keyboard are indicated by [key]</center>" +
				"<hr><center>The controlled turret shoots where your mouse is located by pressing space or automatically after a certain level</center>" +
				"</html>");
		helpText.setVisible(true);
		frame.add(helpText);
	}

	private void setUpErrorMessage(){
	    errorMessageText = new JLabel();
	    errorMessageText.setBounds(mapManager.getSCREEN_WIDTH(), DEFAULT_INFO_HEIGHT*nextInfoOffset, INFO_PANEL_LENGTH, DEFAULT_INFO_HEIGHT*2);
	    nextInfoOffset += 2;
	    errorMessageText.setForeground(Color.RED);
	    errorMessageText.setVisible(true);
	    errorMessageText.setHorizontalAlignment(SwingConstants.CENTER);
	    frame.add(errorMessageText);
    }

	private static void changeToMapEditorState(boolean toEditor){
		startMenuButton.setVisible(!toEditor);
		mapEditorButton.setVisible(!toEditor);
		saveNewMapButton.setVisible(toEditor);
		exitMapEditorButton.setVisible(toEditor);
		selectTile.setVisible(toEditor);
		canvas.setVisible(toEditor);
		for (JButton mapButton : mapButtons){
			mapButton.setVisible(!toEditor);
		}
		for(JButton difficultyButton : difficultButtons) difficultyButton.setVisible(!toEditor);
		gameManager.setInEditor(toEditor);
	}

	private static void changeGameState(boolean isEnd){
		if(isEnd){
			gameWorld.clearAll();
			mapManager.configureMap();
			if(mapEditor!=null){
				mapManager.getGameMaps().get(3).setConfiguration(mapEditor.getMapConfig());
				mapManager.getGameMaps().get(3).configure();
			}
		}else{
			gameWorld.startWave();
			gameWorld.scheduleFire();
		}
		gameManager.init();
		startMenuButton.setVisible(isEnd);
		canvas.setVisible(!isEnd);
		infoText.setVisible(!isEnd);
		selectTurret.setVisible(!isEnd);
		nextWave.setVisible(!isEnd);
		exitGameButton.setVisible(!isEnd);
		mapEditorButton.setVisible(isEnd);
		errorMessageText.setVisible(!isEnd);
		saveNewMapButton.setVisible(false);
		selectTile.setVisible(false);
		helpText.setVisible(!isEnd);
		for (JButton mapButton : mapButtons){
			mapButton.setVisible(isEnd);
			mapButton.setEnabled(true);
		}
		mapButtons.get(3).setVisible(isEnd && mapEditor.isValidConfiguration());

		for(JButton difficultyButton : difficultButtons) difficultyButton.setVisible(isEnd);
		for(JButton gameSpeedButton : gameSpeedButtons) {
			gameSpeedButton.setEnabled(true);
			gameSpeedButton.setVisible(!isEnd);
		}
		if(!isEnd) {
			gameSpeedButtons.get(1).setEnabled(false);
		}
		startGame=!isEnd;
		showSelectedTurret(false);
	}
}