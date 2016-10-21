package wumpusworld;

/**
 *
 * @author Lizzie Herman, Greg Neznanski
 */
public class MapCell extends Cell{
	private boolean wumpusFlag;
    private boolean pitFlag;
    private boolean beenHere;
    private boolean safe;
    private boolean frontier, smell, breeze, glitter, wumpus, pit, obstacle, gold;
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
        if(c == 'l') return this.smell;
        if(c == 'z') return this.breeze;
        if(c == 't') return glitter;
        return false;
    }
    
    public void set(char c, boolean b){
        super.set(c, b);
        if(c == 'u') wumpusFlag = b;
        if(c == 'i') pitFlag = b;
        if(c == 'b') beenHere = b;
        if(c == 'f') frontier = b;
        if(c == 's') safe = b;
        if(c == 'l') smell = b;
        if(c == 'z') breeze = b;
        if(c == 't') glitter = b;
        if(c == 'w') wumpus = b;
        if(c == 'p') pit = b;
        if(c == 'o') obstacle = b;
        if(c == 'g') gold = b;
    }
    
    public void set(char c){
        if(c == 'v') visited++;
    }
    
    public int timesVisited(){
        return visited;
    }
    
    public int[] getCoords(){
    	int[] coords = new int[]{this.x, this.y};
    	return coords;
    }
}
