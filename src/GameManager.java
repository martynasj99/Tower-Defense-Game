public class GameManager {
    private static final GameManager gameManager = new GameManager();

    private int coins;

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
}
