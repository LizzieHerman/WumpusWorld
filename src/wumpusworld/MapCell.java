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
public class MapCell extends Cell{
    boolean wumpusFlag;
    boolean pitFlag;
    boolean beenHere;
    boolean safe;
    boolean frontier;
    int visited;
    
    public MapCell() {
        super(false, false, false, false);
        wumpusFlag = false;
        pitFlag = false;
        beenHere = false;
        safe = false;
        frontier = true;
        visited = 0;
    }
    
    public boolean get(char c){
        if(c == 'w') return wumpus;
        if(c == 'p') return pit;
        if(c == 'o') return obstacle;
        if(c == 'g') return gold;
        if(c == 'u') return wumpusFlag;
        if(c == 'i') return pitFlag;
        if(c == 'b') return beenHere;
        if(c == 'f') return frontier;
        if(c == 's') return safe;
        return false;
    }
    
    public void set(char c, boolean b){
        if(c == 'w') wumpus = b;
        if(c == 'p') pit = b;
        if(c == 'o') obstacle = b;
        if(c == 'g') gold = b;
        if(c == 'u') wumpusFlag = b;
        if(c == 'i') pitFlag = b;
        if(c == 'b') beenHere = b;
        if(c == 'f') frontier = b;
        if(c == 's') safe = b;
    }
    
    public void set(char c){
        if(c == 'v') visited++;
    }
    
    public int timesVisited(){
        return visited;
    }
    
    public void increaseVisited(){
        visited++;
    }
}
