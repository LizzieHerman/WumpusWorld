package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
public class Cell {
    boolean wumpus;
    boolean pit;
    boolean obstacle;
    boolean gold;
    
    public Cell(){
        wumpus = false;
        pit = false;
        obstacle = false;
        gold = false;
    }
    
    public boolean get(char c){
        if(c == 'w') return wumpus;
        if(c == 'p') return pit;
        if(c == 'o') return obstacle;
        if(c == 'g') return gold;
        return false;
    }
    
    public void set(char c, boolean b){
        if(c == 'w') wumpus = b;
        if(c == 'p') pit = b;
        if(c == 'o') obstacle = b;
        if(c == 'g') gold = b;
    }
}
