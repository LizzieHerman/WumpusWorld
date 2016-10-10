package wumpusworld;

import java.util.Random;
/**
 *
 * @author Lizzie Herman
 */
public class WumpusWorld {
    private Cell world[][];
    
    public WumpusWorld(int n){
        world = new Cell[n][n];
        for(int i = 0; i < world.length; i++){
            for(int j = 0; j < world[i].length; j++){
                world[i][j] = new Cell();
            }
        }
    }
    
    public int generateWorld(double pWumpus, double pPit, double pObstacle){
        int numWumpus = 0; // the number of wumpi in the grid
        Random rand = new Random();
        int empty[] = new int[(world.length*world.length)];
        int numEmpty = 0;
        for(int i = 0; i < world.length; i++){
            for(int j = 0; j < world[i].length; j++){
                if(rand.nextDouble() < pWumpus){
                    world[i][j].set('w', true);
                    numWumpus++;
                }else if(rand.nextDouble() < pPit){
                    world[i][j].set('p', true);
                }else if(rand.nextDouble() < pObstacle){
                    world[i][j].set('o', true);
                }
                else{
                    empty[numEmpty] = ((i*100)+j); // add cell to list of empties
                    numEmpty++;
                }
            }
        }
        int gold = empty[rand.nextInt(numEmpty)]; // get random empty cell 
        if(gold < 100) world[0][gold%100].set('g', true);
        else world[gold/100][gold%100].set('g', true);
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
        boolean[] senses = {false, false, false};  //stench, breeze, glimmer
        if(world[x][y].get('g')){
            senses[2] = true;
        }
        // we want cells (x+1,y), (x,y+1), (x-1,y), (x,y-1)
        if(x+1 < world.length){
            if(! senses[0]) senses[0] = world[x+1][y].get('w');
            if(! senses[1]) senses[1] = world[x+1][y].get('p');
        }
        if(y+1 < world.length){
            if(! senses[0]) senses[0] = world[x][y+1].get('w');
            if(! senses[1]) senses[1] = world[x][y+1].get('p');
        }
        if(x-1 >= 0){
            if(! senses[0]) senses[0] = world[x-1][y].get('w');
            if(! senses[1]) senses[1] = world[x-1][y].get('p');
        }
        if(y-1 >= 0){
            if(! senses[0]) senses[0] = world[x][y-1].get('w');
            if(! senses[1]) senses[1] = world[x][y-1].get('p');
        }
        return senses;
    }
}
