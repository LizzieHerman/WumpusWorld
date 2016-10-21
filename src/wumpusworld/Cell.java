package wumpusworld;

/**
 *
 * @author Lizzie Herman, Greg Neznanski
 */
public class Cell {
    boolean wumpus;
    boolean pit;
    boolean obstacle;
    boolean gold, breeze, smell;
    String display;
    
    public Cell(){
        wumpus = false;
        pit = false;
        obstacle = false;
        gold = false;
        this.breeze = false;
        this.smell = false;
        this.display = " "; //the displayed value of what is in the cell
    }
    
    //returns whether or not this cell has the requested object in it.
    public boolean get(char c){
        if(c == 'w') return wumpus;
        if(c == 'p') return pit;
        if(c == 'o') return obstacle;
        if(c == 'g') return gold;
        if(c == 'l') return this.smell;
        if(c == 'z') return this.breeze;
        return false;
    }
    
    //sets the specified boolean in the cell to reflect what is now in it.  
    public void set(char c, boolean b){
        if(c == 'w') wumpus = b;
        if(c == 'p') pit = b;
        if(c == 'o') obstacle = b;
        if(c == 'g') gold = b;
        if(c == 'z') this.breeze = b;
        if(c == 'l') this.smell = b;
    }
    
    //updates the display to the newly given display
    public void setDisplay(String display){
    	this.display = display;
    }
    
    //returns this cells display
    public String getDisplay(){
    	return this.display;
    }
}
