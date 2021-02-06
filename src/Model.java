import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import GameObjects.Bullet;
import GameObjects.Enemy;
import GameObjects.Turret;
import map.Map;
import map.MapManager;
import map.Node;
import util.GameObject;
import util.Point3f;
import util.Vector3f; 
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
public class Model {

	private Controller controller = Controller.getInstance();
	private MapManager mapManager = MapManager.getInstance();
	private GameManager gameManager = GameManager.getInstance();

	private  CopyOnWriteArrayList<Enemy> EnemiesList  = new CopyOnWriteArrayList<>();
	private  CopyOnWriteArrayList<Bullet> BulletList  = new CopyOnWriteArrayList<>();
	private  CopyOnWriteArrayList<Turret> towers  = new CopyOnWriteArrayList<>();

	private int Score=0;
	private Map currentMap;

	public Model() {
		currentMap =  mapManager.getMaps().get(mapManager.getCurrentMap());

		EnemiesList.add(new Enemy("res/UFO.png",50,50,
				new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(),
						currentMap.getEnemyPath().get(0).getPosition().getY(),0), 1));

	}

	public void gamelogic() {
		playerLogic();
		enemyLogic();
		bulletLogic();
		gameLogic();
	}

	private void gameLogic() {
		for (Enemy temp : EnemiesList) {
			for (Bullet bullet : BulletList){
				if ( Math.abs(temp.getCentre().getX()- bullet.getCentre().getX())< temp.getWidth()/2
					&& Math.abs(temp.getCentre().getY()- bullet.getCentre().getY()) < temp.getHeight()/2){
					temp.takeDamage(bullet.getDamage());
					if(temp.getHealth() <= 0) {
						EnemiesList.remove(temp);
						gameManager.changeCoins(1);
					}
					BulletList.remove(bullet);


				}
			}
		}
	}

	private void enemyLogic() {
		for (Enemy temp : EnemiesList){
			Point3f curr = temp.getCentre();
			Point3f dest = currentMap.getEnemyPath().get(temp.getProgress()+1).getPosition();
			Point3f diff = new Point3f(curr.getX()-dest.getX(), curr.getY()-dest.getY(), 0);
			Vector3f direction;

			if(diff.getX() == 0 && diff.getY() == 0) {
				temp.setProgress(temp.getProgress() + 1);
				direction = new Vector3f(0,0,0);
			}
			else if(diff.getX() == 0.0)
				direction = new Vector3f(0, diff.getY() < 0 ? -1 : 1, 0);
			else
				direction = new Vector3f(diff.getX() < 0 ? 1 : -1, 0, 0 );

			temp.getCentre().ApplyVector(direction);
			if (temp.getProgress() == currentMap.getEnemyPath().size()-1){
				EnemiesList.remove(temp);
				Score--;
			} 
		}
	}

	private void bulletLogic() {
		for (Bullet temp : BulletList){
			temp.getCentre().ApplyVector(temp.getDirection());

			if (temp.getCentre().getY()<= 0 || temp.getCentre().getY() >= currentMap.getScreenHeight() ||
					temp.getCentre().getX() <= 0 || temp.getCentre().getX() >= currentMap.getScreenWidth()) {
			 	BulletList.remove(temp);
			} 
		}
	}

	private void playerLogic() {
		// smoother animation is possible if we make a target position  // done but may try to change things for students
		//check for movement and if you fired a bullet
		if(Controller.isMouseClicked()) {
			createTower();
			Controller.getInstance().setMouseClicked(false);
		}
	}

	private void createTower(){
		float x = controller.getMouseClickPosition().getX();
		float y = controller.getMouseClickPosition().getY();

		int nodeX = (int) (x/currentMap.getNodeWidth());
		int nodeY = (int) (y/currentMap.getNodeHeight());
		Node[][] nodes = currentMap.getNodes();

		if(nodeX >= nodes.length || nodeY >= nodes[0].length) return;

		Node node = currentMap.getNodes()[nodeY][nodeX];
		if(node.isAvailable() ){
			Point3f turretLocation = new Point3f(node.getPosition().getX()+(currentMap.getNodeWidth()/2)-25, node.getPosition().getY()+(currentMap.getNodeHeight()/2)-25, 0);
			Bullet turretBullet = new Bullet("res/Bullet.png",10,10, new Point3f(turretLocation.getX(), turretLocation.getY(),0), 20);
			Turret turret = new Turret("res/Lightning.png",50,50, turretLocation, "Standard", 1 ,1,200, turretBullet);
			if(gameManager.getCoins() >= turret.getCost()){
				gameManager.changeCoins(-turret.getCost());
				currentMap.useNode(nodeY,nodeX);
				node.setTurret(turret);
				towers.add(turret);
			}else{
				System.out.println("NOT ENOUGH COINS");
			}

		}else{
			System.out.println("CANNOT PLACE HERE!");
		}
	}

	public void spawn(){
		Enemy enemy = new Enemy("res/UFO.png",50,50,
				new Point3f(currentMap.getEnemyPath().get(0).getPosition().getX(),
						currentMap.getEnemyPath().get(0).getPosition().getY(),0), 100);
		EnemiesList.add(enemy);
	}

	public void fire(){
		for(Turret turret : towers){
			turret.setTarget(null);
			for(Enemy enemy : EnemiesList){
				if(enemy.getCentre().getX() >= turret.getCentre().getX()-turret.getRange()/2 &&
						enemy.getCentre().getX() <= turret.getCentre().getX()+turret.getRange()/2 &&
						enemy.getCentre().getY() >= turret.getCentre().getY()-turret.getRange()/2 &&
						enemy.getCentre().getY() <= turret.getCentre().getY()+turret.getRange()/2){
					turret.setTarget(enemy);
					break;
				}
			}
			if(turret.getTarget() == null) continue;

			Bullet bullet = turret.getBullet();
			Point3f tur = turret.getCentre();
			Point3f target;
			target = turret.getTarget().getCentre();

			float diff_x = target.getX() - tur.getX();
			float diff_y = (target.getY() - tur.getY())*-1;

			float max = Math.max(Math.abs(diff_x), Math.abs(diff_y));
			Vector3f dir = new Vector3f((diff_x/max)*8, (diff_y/max)*8, 0);

			bullet.setDirection(dir);
			bullet.setCentre(new Point3f(turret.getCentre().getX(), turret.getCentre().getY(), 0));
			BulletList.add(bullet);
		}
	}

	public void getTargets(){
	}

	public CopyOnWriteArrayList<Enemy> getEnemies() {
		return EnemiesList;
	}
	
	public CopyOnWriteArrayList<Bullet> getBullets() {
		return BulletList;
	}

	public CopyOnWriteArrayList<Turret> getTowers() {
		return towers;
	}

	public int getScore() {
		return Score;
	}
}