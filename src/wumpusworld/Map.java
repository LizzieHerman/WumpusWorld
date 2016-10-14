package wumpusworld;

/**
 *
 * @author Lizzie Herman
 *small additions by Ryan Freivalds
 */
public class Map {
    private MapCell map[][];
    
    public Map(int n){ //generating our X by X grid of MapCells
        map = new MapCell[n][n]; 
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                map[i][j] = new MapCell();
            }
        }
    }
    
    public MapCell getCell(int x, int y){
        if(x < 0 || x >= map.length || y < 0 || y >= map.length) return null; //if the cell is out of bounds return nothing
        return map[x][y]; //otherwise return that cell
    }
    
    public boolean setCell(int x, int y, char c, boolean b){
        if(x-1 < 0 || x+1 >= map.length || y-1 < 0 || y+1 >= map.length) return false; //if this non-existent "cell" is out of bounds we cannot change it
        map[x][y].set(c, b); //otherwise populate the cell
        return true; //and return successful
    }
    
    public boolean setCell(int x, int y, char c){
        if(x-1 < 0 || x+1 >= map.length || y-1 < 0 || y+1 >= map.length) return false; //if this non-existent "cell" is out of bounds we cannot change it
        map[x][y].set(c); //otherwise populate the cell
        return true; //and return successful
    }
    
    public int size(){
        return map.length;
    }
}
