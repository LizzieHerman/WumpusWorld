package wumpusworld;

import java.util.Random;
/**
 *
 * @author Lizzie Herman
 */
public class WumpusWorld {
    private Cell world[][];
    private Explorer agent;
    private int[] agentState = { 0, 0, 1};
    
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
                if(i == 0 && j == 0){
                    continue;
                }else if(rand.nextDouble() < pWumpus){
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
    
    public void setExplorer(Explorer e){
        agent = e;
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
        boolean stench = false;
        boolean breeze = false;
        boolean gold = false;
        if(world[x][y].get('g')){
            agent.seeGlitter();
            gold = true;
        }
        // we want cells (x+1,y), (x,y+1), (x-1,y), (x,y-1)
        if(x+1 < world.length){
            if(! stench && world[x+1][y].get('w')){
                stench = true;
                agent.smellStench();
            }
            if(! breeze && world[x+1][y].get('p')){
                breeze = true;
                agent.feelBreeze();
            }
        }
        if(y+1 < world.length){
            if(! stench && world[x][y+1].get('w')){
                stench = true;
                agent.smellStench();
            }
            if(! breeze && world[x][y+1].get('p')){
                breeze = true;
                agent.feelBreeze();
            }
        }
        if(x-1 >= 0){
            if(! stench && world[x-1][y].get('w')){
                stench = true;
                agent.smellStench();
            }
            if(! breeze && world[x-1][y].get('p')){
                breeze = true;
                agent.feelBreeze();
            }
        }
        if(y-1 >= 0){
            if(! stench && world[x][y-1].get('w')){
                stench = true;
                agent.smellStench();
            }
            if(! breeze && world[x][y-1].get('p')){
                breeze = true;
                agent.feelBreeze();
            }
        }
        boolean[] senses = {stench, breeze, gold};
        return senses;
    }
    
    public void turnExplorer( boolean left){
        if(left){
            if(agentState[2] == 1) agentState[2] = 4;
            else agentState[2]--;
        }else{
            if(agentState[2] == 4) agentState[2] = 1;
            else agentState[2]++;
        }
    }
    
    public void moveExplorer(int x, int y){
        if( x >= world.length || y >= world[x].length || x < 0 || y < 0){
            agent.feelBump( agentState[0], agentState[1]);
        }
        else if(world[x][y].get('o')){
            agent.feelBump( agentState[0], agentState[1]);
        }
        else if(world[x][y].get('w')){
            agent.die( agentState[0], agentState[1], true);
        }
        else if(world[x][y].get('p')){
            agent.die( agentState[0], agentState[1], false);
        }
        else{
            agentState[0] = x;
            agentState[1] = y;
        }
    }
    
    public void shootArrow(int x, int y, int direction){
        boolean shotWumpus = false;
        switch(direction){
            case 1:
                for(; x < world.length; x++){
                    if(world[x][y].get('w')){
                        world[x][y].set('w', false);
                        shotWumpus = true;
                        break;
                    }
                }
                break;
            case 2:
                for(; y < world.length; y++){
                    if(world[x][y].get('w')){
                        world[x][y].set('w', false);
                        shotWumpus = true;
                        break;
                    }
                }
                break;
            case 3:
                for(; x >= 0; x--){
                    if(world[x][y].get('w')){
                        world[x][y].set('w', false);
                        shotWumpus = true;
                        break;
                    }
                }
                break;
            case 4:
                for(; y >= 0; y--){
                    if(world[x][y].get('w')){
                        world[x][y].set('w', false);
                        shotWumpus = true;
                        break;
                    }
                }
                break;
        }
        if(shotWumpus){
            agent.hearScream();
        }
    }
    
    public boolean removeGold(){
        int x = agentState[0];
        int y = agentState[1];
        if(x < world.length && y < world[x].length && x >= 0 && y >= 0 && world[x][y].get('g')){
            world[x][y].set('g', false);
            return true;
        }
        return false;
    }
}
