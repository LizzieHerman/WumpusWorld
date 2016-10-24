package wumpusworld;

/**
 *
 * @author Lizzie Herman, Greg Neznanski
 *small additions by Ryan Freivalds
 */
public class Map {
    MapCell map[][];
    
    public Map(int n){ //generating our X by X grid of MapCells
        map = new MapCell[n][n]; 
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                map[i][j] = new MapCell(i, j);
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
    
    //Return cells surrounding current cell
    public MapCell[] getSurrounding(int x, int y, int facing){
    	MapCell[] surrounding = new MapCell[4];
    	
    	//Add all surrounding cells to array, depending on facing
    	if(facing == 1){ //Facing east
    		if(y+1 < map.length){ //Check left cell
	    		surrounding[0] = map[x][y+1];
	    	}else{
	    		surrounding[0] = null;
	    	}
	    	if(y-1 >= 0){ //Check right cell
	    		surrounding[2] = map[x][y-1];
	    	}else{
	    		surrounding[2] = null;
	    	}
	    	if(x+1 < map.length){ //Check forward cell
	    		surrounding[1] = map[x+1][y];
	    	}else{
	    		surrounding[1] = null;
	    	}
	    	if(x-1 >= 0){ //Check backward cell
	    		surrounding[3] = map[x-1][y];
	    	}else{
	    		surrounding[3] = null;
	    	}
    	}else if(facing == 2){ //Facing south
    		if(x+1 < map.length){ //Check left cell
	    		surrounding[0] = map[x+1][y];
	    	}else{
	    		surrounding[0] = null;
	    	}
	    	if(x-1 >= 0){ //Check right cell
	    		surrounding[2] = map[x-1][y];
	    	}else{
	    		surrounding[2] = null;
	    	}
	    	if(y-1 >= 0){ //Check forward cell
	    		surrounding[1] = map[x][y-1];
	    	}else{
	    		surrounding[1] = null;
	    	}
	    	if(y+1 < map.length){ //Check backward cell
	    		surrounding[3] = map[x][y+1];
	    	}else{
	    		surrounding[3] = null;
	    	}
    	}else if(facing == 3){ //Facing west
    		if(y-1 >= 0){ //Check left cell
	    		surrounding[0] = map[x][y-1];
	    	}else{
	    		surrounding[0] = null;
	    	}
	    	if(y+1 < map.length){ //Check right cell
	    		surrounding[2] = map[x][y+1];
	    	}else{
	    		surrounding[2] = null;
	    	}
	    	if(x-1 >= 0){ //Check forward cell
	    		surrounding[1] = map[x-1][y];
	    	}else{
	    		surrounding[1] = null;
	    	}
	    	if(x+1 < map.length){ //Check backward cell
	    		surrounding[3] = map[x+1][y];
	    	}else{
	    		surrounding[3] = null;
	    	}
    	}else{ //Facing north
	    	if(x-1 >= 0){ //Check left cell
	    		surrounding[0] = map[x-1][y];
	    	}else{
	    		surrounding[0] = null;
	    	}
	    	if(x+1 < map.length){ //Check right cell
	    		surrounding[2] = map[x+1][y];
	    	}else{
	    		surrounding[2] = null;
	    	}
	    	if(y+1 < map.length){ //Check forward cell
	    		surrounding[1] = map[x][y+1];
	    	}else{
	    		surrounding[1] = null;
	    	}
	    	if(y-1 >= 0){ //Check backward cell
	    		surrounding[3] = map[x][y-1];
	    	}else{
	    		surrounding[3] = null;
	    	}
    	}
    	
    	return surrounding;
    }
}
