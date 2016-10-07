package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
public class WumpusWorld {
    private Cell world[][];
    
    public WumpusWorld(int n){
        world = new Cell[n][n];
    }
    
    public int generateWorld(double pWumpus, double pPit, double pObstacle){
        int numWumpus = 0; // the number of wumpi in the grid
        /* TO-DO
         * Generate cells using provided probabilites
         * place gold in random empty cell
         */
        return numWumpus;
    }
    
    public Cell getCell(int x, int y){
        if(x < 0 || x >= world.length || y < 0 || y >= world.length){
            System.out.print("bump");
            return null;
        }
        if(world[x][y].get('o')){
            System.out.print("bump");
            return null;
        }
        return world[x][y];
    }
    
    public boolean[] senseCell(int x, int y){
        if(x-1 < 0 || x+1 >= world.length || y-1 < 0 || y+1 >= world.length){
            boolean[] bump = {true};
            return bump;
        }
        boolean[] senses = {false, false, false};  //stench, breeze, glimmer
        /* TO-DO
         * get surrounding cells and see whether they have Wumpus or Pit
         */
        return senses;
    }
}
