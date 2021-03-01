package map;
import util.Point3f;
import java.util.ArrayList;
import java.util.List;
/**
 * Student Name: Martynas Jagutis
 * Student Number: 17424866
 */
/**
 * Tile Map idea inspired by https://www.youtube.com/user/Brackeys (where it was used in 3d unity game). All code however is mine.
 * Textures taken from https://opengameart.org/
 * */
public class GameMap {

    private final int[][] DIRECTIONS = {{0,1},{1,0},{0,-1},{-1,0}};

    private Node[][] nodes;
    private int[][] configuration;
    private int screenWidth;
    private int screenHeight;
    private int nodeWidth;
    private int nodeHeight;

    private List<Node> enemyPath;

    public GameMap(int screenWidth, int screenHeight, int[][] configuration) {
        this.enemyPath = new ArrayList<>();
        this.configuration = configuration;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        configure();
    }

    public void configure(){
        int nw = configuration.length;
        int nh = configuration[0].length;
        nodeWidth = screenWidth/nw;
        nodeHeight = screenHeight/nh;

        this.nodes = new Node[nw][nh];
        for(int i = 0; i < nodes.length; i++){
            for(int j = 0; j < nodes[0].length; j++){
                if(configuration[i][j] == 0)
                    nodes[i][j] = new Node("res/map/grass.png", nodeWidth-1, nodeHeight-1, new Point3f((j*nodeWidth), (i*nodeHeight),0), true);
                else if(configuration[i][j] == -1){
                    nodes[i][j] = new Node("res/blankSprite.png", nodeWidth-1, nodeHeight-1, new Point3f((j*nodeWidth), (i*nodeHeight),0), false);
                }
                else{
                    nodes[i][j] = new Node("res/map/dirt.png", nodeWidth-1, nodeHeight-1, new Point3f((j*nodeWidth), (i*nodeHeight),0), false);
                }
            }
        }
        generatePath();
    }

    private void generatePath(){
        this.enemyPath = new ArrayList<>();
        int curr = 0;
        int i = 0, j = 1;
        while( i != 0 || j != 0 ){
            if(this.configuration[i][j] == 2){
                this.enemyPath.add(this.nodes[i][j]);
                recursivePath(this.configuration, new boolean[this.configuration.length][this.configuration[0].length], i, j);
                break;
            }
            int newI = i + DIRECTIONS[curr][0];
            int newJ = j + DIRECTIONS[curr][1];
            if(newI >= 0 && newI < this.configuration.length && newJ >= 0 && newJ < this.configuration[0].length ){
                i = newI;
                j = newJ;
            }else{
                curr = (curr+1)%4;
            }
        }
    }

    private void recursivePath (int[][] config, boolean[][] visited, int row, int col){
        if(!isValid(visited, row, col)) return;

        visited[row][col] = true;
        if(config[row][col] == 1) enemyPath.add(nodes[row][col]);

        for(int i = 0; i < 4; i++ ){
            int newRow = row + DIRECTIONS[i][0];
            int newCol = col + DIRECTIONS[i][1];

            if(isValid(visited, newRow, newCol) && config[newRow][newCol] == 1){
                recursivePath(config, visited, newRow, newCol);
            }
        }
    }

    private boolean isValid(boolean[][] visited, int i, int j){
        return !(i >= this.configuration.length || i < 0 || j >= this.configuration[0].length || j < 0 || visited[i][j]);
    }

    public void useNode(int y, int x){
        this.nodes[y][x].setAvailable(false);
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }

    public int[][] getConfiguration() {
        return configuration;
    }

    public void setConfiguration(int[][] configuration) {
        this.configuration = configuration;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getNodeWidth() {
        return nodeWidth;
    }

    public void setNodeWidth(int nodeWidth) {
        this.nodeWidth = nodeWidth;
    }

    public int getNodeHeight() {
        return nodeHeight;
    }

    public void setNodeHeight(int nodeHeight) {
        this.nodeHeight = nodeHeight;
    }

    public List<Node> getEnemyPath() {
        return enemyPath;
    }

    public void setEnemyPath(List<Node> enemyPath) {
        this.enemyPath = enemyPath;
    }
}
