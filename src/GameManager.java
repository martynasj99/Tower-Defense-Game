import GameObjects.Turret;
import util.GameObject;

public class GameManager {
    private static final GameManager gameManager = new GameManager();

    private int coins;

    private Turret selected;

    public GameManager(){
        this.coins = 10;
    }

    public static GameManager getInstance(){
        return gameManager;
    }

    public void changeCoins(int amt){
        this.coins += amt;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public Turret getSelected() {
        return selected;
    }

    public void setSelected(Turret selected) {
        this.selected = selected;
    }
}
