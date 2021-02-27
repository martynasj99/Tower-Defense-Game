package map;

public class MapEditor {

    private int selectedTexture;
    private int[][] mapConfig;

    public MapEditor() {
        this.selectedTexture = -1;
        this.mapConfig = new int[][]{
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}};
    }


    public void updateConfig(int row, int col){
        this.mapConfig[col][row] = selectedTexture;
    }

    public boolean isValidConfiguration(){
        boolean isFound = false;
        for (int i = 0; i < this.mapConfig.length ; i++) {
            for (int j = 0; j < this.mapConfig[0].length; j++) {
                if(this.mapConfig[i][j] == 2) {
                    if(isFound) return false;
                    isFound = true;
                }
                if(this.mapConfig[i][j] == -1 ) return false;
            }
        }
        return isFound;
    }

    public int getSelectedTexture() {
        return selectedTexture;
    }

    public void setSelectedTexture(int selectedTexture) {
        this.selectedTexture = selectedTexture;
    }

    public int[][] getMapConfig() {
        return mapConfig;
    }

    public void setMapConfig(int[][] mapConfig) {
        this.mapConfig = mapConfig;
    }
}
