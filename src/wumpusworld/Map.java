/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
public class Map {
    private MapCell map[][];
    
    public Map(int n){
        map = new MapCell[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                map[i][j] = new MapCell();
            }
        }
    }
    
    public MapCell getCell(int x, int y){
        if(x < 0 || x >= map.length || y < 0 || y >= map.length) return null;
        return map[x][y];
    }
    
    public boolean setCell(int x, int y, char c, boolean b){
        if(x-1 < 0 || x+1 >= map.length || y-1 < 0 || y+1 >= map.length) return false;
        map[x][y].set(c, b);
        return true;
    }
    
    public boolean setCell(int x, int y, char c){
        if(x-1 < 0 || x+1 >= map.length || y-1 < 0 || y+1 >= map.length) return false;
        map[x][y].set(c);
        return true;
    }
    
    public int size(){
        return map.length;
    }
}
