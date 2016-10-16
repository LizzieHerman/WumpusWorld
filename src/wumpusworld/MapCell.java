package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
public class MapCell extends Cell{
	private boolean wumpusFlag;
    private boolean pitFlag;
    private boolean beenHere;
    private boolean safe;
    private boolean frontier;
    private int visited;
    private boolean smell, breeze;
    
    public MapCell() {
        super();
        wumpusFlag = false;
        pitFlag = false;
        beenHere = false;
        safe = false;
        frontier = true;
        visited = 0;
        smell = false;
        breeze = false;
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
        super.set(c, b);
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
    
    public void setSmell(){
    	this.smell = true;
    }
	public void setBreeze(){
		this.breeze = true;
	}
}
