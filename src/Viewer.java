import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.swing.*;
import GameObjects.Bullet;
import GameObjects.Enemy;
import GameObjects.Turret;
import map.MapManager;
import map.Node;

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
	
	private Model gameWorld =new Model();
	private MapManager mapManager = MapManager.getInstance();
	private Controller controller = Controller.getInstance();
	private GameManager gameManager = GameManager.getInstance();

	public Viewer(Model World) {
		this.gameWorld=World;
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

		gameWorld.getTurrets().forEach((temp) -> drawPlayer(temp, g));
		gameWorld.getBullets().forEach((temp) -> drawBullet(temp, g));
		gameWorld.getEnemies().forEach((temp) -> drawEnemies(temp,g));

		markSelected(g);
	}

	//https://gamesupply.itch.io/virus-free-mini-pack
	private void drawEnemies(Enemy enemy, Graphics g) {
		String texture = enemy.getTexture();
		int x = (int) enemy.getCentre().getX(), y = (int) enemy.getCentre().getY();
		int width = (int) enemy.getWidth(), height = (int) enemy.getHeight();

		float healthRemainingPerc = (float) enemy.getHealth()/enemy.getInitialHealth();
		mapManager.addToTileImages(texture);
		Image myImage = mapManager.getTileImages().get(texture);

		if(healthRemainingPerc > .66) g.setColor(Color.GREEN);
		else if(healthRemainingPerc >= .33 && healthRemainingPerc <= .66) g.setColor(Color.ORANGE);
		else g.setColor(Color.RED);

		g.drawRect(x,y, width, height/8);
		g.fillRect(x,y, (int)(width*healthRemainingPerc), height/8);
		g.drawImage(myImage, x,y, width, height, null);
	}

	private void drawBackground(Graphics g){

		Node[][] nodes = mapManager.getCurrentGameMap().getNodes();

		for(int i = 0; i < nodes.length; i++){
			for(int j = 0; j < nodes[0].length; j++){
				Node node = nodes[i][j];
				int x = (int) node.getCentre().getX(), y = (int) node.getCentre().getY();
				int width = (int) node.getWidth(), height = (int) node.getHeight();

				mapManager.addToTileImages(node.getTexture());
				Image image = mapManager.getTileImages().get(node.getTexture());

				if(isInside( x, x+width, y, y+height ) && node.isAvailable()){
					node.setTextureLocation("res/map/grass-selected.png");
				}else if (node.isAvailable()){
					node.setTextureLocation("res/map/grass.png");
				}
				g.drawImage(image, x, y, width, height, null);
			}
		}
	}

	private void drawBullet(Bullet bullet, Graphics g){
		int x = (int) bullet.getCentre().getX(), y = (int) bullet.getCentre().getY();
		int width = (int) bullet.getWidth(), height = (int) bullet.getHeight();

		g.setColor(Color.RED);
		g.fillArc(x, y, width, height, 0, 360);
	}

	private void drawPlayer(Turret player, Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;

		String texture = player.getTexture();
		int x = (int) player.getCentre().getX(), y = (int) player.getCentre().getY();
		int width = (int)player.getWidth(), height = (int) player.getHeight();
		int range = (int) player.getRange();
		String type = player.getType();

		mapManager.addToTileImages("res/turrets/Tower.png");
		Image base = mapManager.getTileImages().get("res/turrets/Tower.png");
		mapManager.addToTileImages(texture);
		Image myImage = mapManager.getTileImages().get(texture);

		AffineTransform old = g2d.getTransform();
		double angle = 0;
		g.drawImage(base, x, y, mapManager.getCurrentGameMap().getNodeWidth()-5, mapManager.getCurrentGameMap().getNodeHeight(), null);
		if(!type.equals("Controlled")){
			if(player.getTarget() != null) {
				//https://stackoverflow.com/questions/2676719/calculating-the-angle-between-the-line-defined-by-two-points
				angle = Math.atan2(player.getTarget().getCentre().getY() - player.getCentre().getY(), player.getTarget().getCentre().getX() - player.getCentre().getX()) * 180 / Math.PI;
				angle+=90;
			}
			//Reference : https://gist.github.com/sye8/edba2dfda1645b37bfcf5b9bd9ce3a75 (Some parts)
			g2d.rotate(Math.toRadians(angle), (x)+(mapManager.getCurrentGameMap().getNodeHeight()/2f) - 21+(double)(width/2),(y)+(mapManager.getCurrentGameMap().getNodeHeight()/2f) -35+(double)(height/2));
			g2d.drawImage(myImage, x+(mapManager.getCurrentGameMap().getNodeHeight()/2) - 21,y+(mapManager.getCurrentGameMap().getNodeHeight()/2) -50, width, height, null);
			g2d.setTransform(old);
		}else{
			angle = Math.atan2(controller.getMouseMovePosition().getY() - player.getCentre().getY(), controller.getMouseMovePosition().getX() - player.getCentre().getX()) * 180 / Math.PI;
			angle+= 90;
			g2d.rotate(Math.toRadians(angle), x+(mapManager.getCurrentGameMap().getNodeHeight()/2f) - 50+(double)(width/2), y+(mapManager.getCurrentGameMap().getNodeHeight()/2f) -50+(double)(height/2));
			g.drawImage(myImage, x-20, y-20, width, height, null);
			g2d.setTransform(old);
		}

		if(isInside(x,x+width, y, y+height)){
			g.setColor(Color.RED);
			g.drawArc(x-(range/2),y-(range/2), width+range, height+range, 0, 360);
		}
	}

	private boolean isInside(int xStart, int xEnd, int yStart, int yEnd){
		return controller.getMouseMovePosition().getX() >= xStart &&
				controller.getMouseMovePosition().getX() <= xEnd &&
				controller.getMouseMovePosition().getY() >= yStart  &&
				controller.getMouseMovePosition().getY() <= yEnd;
	}

    private void markSelected(Graphics g){
	    Node selected = gameManager.getSelected();
	    if(selected != null){
            g.setColor(Color.BLUE);
            g.drawArc((int) selected.getCentre().getX(), (int)selected.getCentre().getY(), (int) selected.getWidth(), (int) selected.getHeight(), 0, 360);
        }
    }
}