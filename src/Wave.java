import GameObjects.Enemy;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class Wave {

    private Queue<Enemy> enemiesToSpawn = new LinkedBlockingDeque<>();

    private Map<String, Integer> enemyTypeSpawn;

    public Wave(Map<String, Integer> enemyTypeSpawn) {
        this.enemyTypeSpawn = enemyTypeSpawn;
    }

    public Map<String, Integer> getEnemyTypeSpawn() {
        return enemyTypeSpawn;
    }

    public void setEnemyTypeSpawn(Map<String, Integer> enemyTypeSpawn) {
        this.enemyTypeSpawn = enemyTypeSpawn;
    }
}
