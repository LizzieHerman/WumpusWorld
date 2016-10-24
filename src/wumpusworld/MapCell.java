package wumpusworld;

/**
 *
 * @author Lizzie Herman, Greg Neznanski
 */
public class MapCell extends Cell{
	private boolean wumpusFlag;
    private boolean pitFlag;
    private boolean beenHere, ignoring, madeRelation;
    private boolean safe, pit, obstacle, gold, flagable;
    private boolean frontier, smell, breeze, glitter, wumpus;
    private int visited, x, y;
    
    public MapCell(int x, int y) {
        super();
        wumpusFlag = false;
        pitFlag = false;
        beenHere = false;
        safe = false;
        frontier = true;
        visited = 0;
        this.smell = super.smell;
        this.breeze = super.breeze;
        this.x = x;
        this.y = y;
        this.wumpus = false;
        this.pit = false;
        this.obstacle = false;
        this.gold = false;
        this.flagable = true;
        this.ignoring = false;
        this.madeRelation = false;
    }
    
    public boolean get(char c){
        if(c == 'w') return this.wumpus;
        if(c == 'p') return this.pit;
        if(c == 'o') return this.obstacle;
        if(c == 'g') return this.gold;
        if(c == 'u') return this.wumpusFlag;
        if(c == 'i') return this.pitFlag;
        if(c == 'b') return this.beenHere;
        if(c == 'f') return this.frontier;
        if(c == 's') return this.safe;
        if(c == 'l') return this.smell;
        if(c == 'z') return this.breeze;
        if(c == 't') return this.glitter;
        if(c == 'a') return this.flagable;
        if(c == 'r') return this.ignoring;
        if(c == 'n') return this.madeRelation;
        return false;
    }
    
    public void set(char c, boolean b){
        super.set(c, b);
        if(c == 'u') this.wumpusFlag = b;
        if(c == 'i') this.pitFlag = b;
        if(c == 'b') this.beenHere = b;
        if(c == 'f') this.frontier = b;
        if(c == 's') this.safe = b;
        if(c == 'l') this.smell = b;
        if(c == 'z') this.breeze = b;
        if(c == 't') this.glitter = b;
        if(c == 'w') this.wumpus = b;
        if(c == 'p') this.pit = b;
        if(c == 'o') this.obstacle = b;
        if(c == 'g') this.gold = b;
        if(c == 'a') this.flagable = b;
        if(c == 'r') this.ignoring = b;
        if(c == 'n') this.madeRelation = b;
    }
    
    public void set(char c){
        if(c == 'v') this.visited++;
    }
    
    public int timesVisited(){
        return this.visited;
    }
    
    public int[] getCoords(){
    	int[] coords = new int[]{this.x, this.y};
    	return coords;
    }
}
