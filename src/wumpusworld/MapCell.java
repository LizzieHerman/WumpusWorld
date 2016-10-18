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
    private boolean frontier, smell, breeze;
    private int visited, x, y;
    
    public MapCell(int x, int y) {
        super();
        wumpusFlag = false;
        pitFlag = false;
        beenHere = false;
        safe = false;
        frontier = true;
        visited = 0;
        smell = false;
        breeze = false;
        this.x = x;
        this.y = y;
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
        if(c == 'l') return smell;
        if(c == 'z') return breeze;
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
