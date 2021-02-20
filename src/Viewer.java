import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.*;

import GameObjects.Enemy;
import GameObjects.Turret;
import map.MapManager;
import map.Node;
import util.GameObject;
import util.Point3f;


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
 
 * Credits: Kelly Charles (2020)
 */ 
public class Viewer extends JPanel {
	private long CurrentAnimationTime= 0; 
	
	private Model gameworld =new Model();
	private MapManager mapManager = MapManager.getInstance();
	private Controller controller = Controller.getInstance();
	private GameManager gameManager = GameManager.getInstance();

	 
	public Viewer(Model World) {
		this.gameworld=World;
	}

	public Viewer(LayoutManager layout) {
		super(layout);
	}

	public Viewer(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public Viewer(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public void updateview() {
		this.repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		CurrentAnimationTime++; // runs animation time step
		drawBackground(g);
		gameworld.getTurrets().forEach((temp) -> drawPlayer(temp, g));
		gameworld.getBullets().forEach((temp) -> drawBullet((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(), g));
		gameworld.getEnemies().forEach((temp) -> drawEnemies(temp,g));

		markSelected(g);
	}


	//https://gamesupply.itch.io/virus-free-mini-pack
	private void drawEnemies(Enemy enemy, Graphics g) {
		String texture = enemy.getTexture();
		int x = (int) enemy.getCentre().getX();
		int y = (int) enemy.getCentre().getY();
		int width = (int) enemy.getWidth();
		int height = (int) enemy.getHeight();
		float healthRemainingPerc = (float) enemy.getHealth()/enemy.getInitialHealth();
		File TextureToLoad = new File(texture);
		try {
			Image myImage = ImageIO.read(TextureToLoad);
			//The spirte is 32x32 pixel wide and 4 of them are placed together so we need to grab a different one each time 
			//remember your training :-) computer science everything starts at 0 so 32 pixels gets us to 31  
			//int currentPositionInAnimation= ((int) (CurrentAnimationTime%4 )*32); //slows down animation so every 10 frames we get another frame so every 100ms

			if(healthRemainingPerc > .66) g.setColor(Color.GREEN);
			else if(healthRemainingPerc >= .33 && healthRemainingPerc <= .66) g.setColor(Color.ORANGE);
			else g.setColor(Color.RED);

			g.drawRect(x,y, width, height/8);
			g.fillRect(x,y, (int)(width*healthRemainingPerc), height/8);
			//g.drawImage(myImage, x,y, x+width, y+height, currentPositionInAnimation  , 0, currentPositionInAnimation+31, 32, null);
			g.drawImage(myImage, x,y, width, height, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void drawBackground(Graphics g){
		Node[][] nodes = mapManager.getCurrentMap().getNodes();
		for(int i = 0; i < nodes.length; i++){
			for(int j = 0; j < nodes[0].length; j++){
				Node node = nodes[i][j];
				float mouseX = controller.getMouseMovePosition().getX();
				float mouseY = controller.getMouseMovePosition().getY();
				if(node.isAvailable() && mouseX >= node.getPosition().getX() &&
						mouseX <= node.getPosition().getX() +node.getWidth() &&
						mouseY >= node.getPosition().getY()  &&
						mouseY <= node.getPosition().getY()+node.getHeight() ){
					g.setColor(new Color(0,194,16));
				}else{
					g.setColor(node.getColor());
				}

				g.fillRect((int) node.getPosition().getX(), (int) node.getPosition().getY(), (int) node.getWidth(), (int) node.getHeight());
			}
		}
	}
	
	private void drawBullet(int x, int y, int width, int height, String texture,Graphics g){
		g.setColor(Color.RED);
		g.fillArc(x,y,width, height, 0, 360);
	}

	private void drawPlayer(Turret player, Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;

		String texture = player.getTexture();
		int x = (int) player.getCentre().getX();
		int y = (int) player.getCentre().getY();
		int width = (int)player.getWidth();
		int height = (int) player.getHeight();
		int range = (int) player.getRange();

		File TextureToLoad = new File(texture);
		try {
			BufferedImage base = ImageIO.read(new File("res/turrets/Tower.png"));
			BufferedImage myImage = ImageIO.read(TextureToLoad);

            double angle = 0;
            if(player.getTarget() != null)
                //https://stackoverflow.com/questions/2676719/calculating-the-angle-between-the-line-defined-by-two-points
                angle = Math.atan2(player.getTarget().getCentre().getY() - player.getCentre().getY(), player.getTarget().getCentre().getX() - player.getCentre().getX()) * 180 / Math.PI;

            g.drawImage(base, x, y, mapManager.getCurrentMap().getNodeWidth()-5, mapManager.getCurrentMap().getNodeHeight(), null); //TODO MAKE IT ADAPT TO TILE SIZE
            //Reference : https://gist.github.com/sye8/edba2dfda1645b37bfcf5b9bd9ce3a75 (Some parts)
            g2d.rotate(Math.toRadians(-1*angle), (x)+(mapManager.getCurrentMap().getNodeHeight()/2f) - 21+(double)(width/2),(y)+(mapManager.getCurrentMap().getNodeHeight()/2f) -50+(double)(height/2));
			g2d.drawImage(myImage, x+(mapManager.getCurrentMap().getNodeHeight()/2) - 21,y+(mapManager.getCurrentMap().getNodeHeight()/2) -50, width, height, null);
			g2d.rotate(Math.toRadians(angle),  (x)+(mapManager.getCurrentMap().getNodeHeight()/2f) - 21+(double)(width/2),(y)+(mapManager.getCurrentMap().getNodeHeight()/2f) -50+(double)(height/2));

			if(controller.getMouseMovePosition().getX() >= x &&
					controller.getMouseMovePosition().getX() <= x+width &&
					controller.getMouseMovePosition().getY() >= y  &&
					controller.getMouseMovePosition().getY() <= y+height ){
				g.setColor(Color.RED);
				g.drawArc(x-(range/2),y-(range/2), width+range, height+range, 0, 360);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		//g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer));
		//Lighnting Png from https://opengameart.org/content/animated-spaceships  its 32x32 thats why I know to increament by 32 each time 
		// Bullets from https://opengameart.org/forumtopic/tatermands-art 
		// background image from https://www.needpix.com/photo/download/677346/space-stars-nebula-background-galaxy-universe-free-pictures-free-photos-free-images
	}
    private void markSelected(Graphics g){
	    Node selected = gameManager.getSelected();
	    if(selected != null){
            g.setColor(Color.BLUE);
            g.drawArc((int) selected.getPosition().getX(), (int)selected.getPosition().getY(), (int) selected.getWidth(), (int) selected.getHeight(), 0, 360);
        }
    }
}