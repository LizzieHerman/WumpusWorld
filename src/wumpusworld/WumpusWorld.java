package wumpusworld;

import java.util.Random;
/**
 *
 * @author Lizzie Herman
 * Really small additions by Ryan Freivalds
 */
public class WumpusWorld {
    private Cell world[][];
    private Explorer agent;
    private int[] agentState = { 0, 0, 1};// = { 0, 0, 1}; //represents the agent's X location, Y location, and current facing, respectively.
    //Direction is 1 for "East," 2 for "South," 3 for "West," and 4 for "North."
    
    public WumpusWorld(int n){ //initializes all of our cell objects, to be filled (or left empty) by the generateWorld() function.
        world = new Cell[n][n];
        for(int i = 0; i < world.length; i++){
            for(int j = 0; j < world[i].length; j++){
                world[i][j] = new Cell();
            }
        }
    }
    
    public int generateWorld(double pWumpus, double pPit, double pObstacle){ //these inputs -roughly- represent the chance that a given cell is turned into an obstacle of some sort.
        int numWumpus = 0; // the number of wumpi in the grid
        Random rand = new Random();
        int empty[] = new int[(world.length*world.length)];
        int numEmpty = 0;
        String grid[][] = new String[world.length][world.length];
        for(int i = 0; i < world.length; i++){
            for(int j = 0; j < world[i].length; j++){
                if(i == 0 && j == 0){
                    grid[i][j] = "explorer";
                    world[i][j].set('e', true); //placing the player
                }else if(rand.nextDouble() < pWumpus){ //otherwise seeing if we want to generate a Wumpus, Pit, or wall...
                    world[i][j].set('w', true);
                    grid[i][j] = "wumpus";
                    numWumpus++; //So we know how many arrows we take with us.
                }else if(rand.nextDouble() < pPit){
                    world[i][j].set('p', true);
                    grid[i][j] = "pit";
                }else if(rand.nextDouble() < pObstacle){
                    world[i][j].set('o', true);
                    grid[i][j] = "wall";
                }
                else{ //or just leave it empty.
                    empty[numEmpty] = ((i*100)+j); // add cell to list of empties
                    numEmpty++;
                    grid[i][j] = "empty";
                }
            }
        }
        int gold = empty[rand.nextInt(numEmpty)]; // get random empty cell 
        if(gold < 100) world[0][gold%100].set('g', true);
        else world[gold/100][gold%100].set('g', true);
        //GameBoard board = new GameBoard(numWumpus, grid);
        //board.pack();
        //board.setVisible(true);
        return numWumpus;
    }
    
    public void setExplorer(Explorer e){ //setting the Agent's behavior. 
        agent = e;
    }
    
    public Cell getCell(int x, int y){
        if(x < 0 || x >= world.length || y < 0 || y >= world.length){ //if out of bounds
            return null;
        }
        if(world[x][y].get('o')){ //if empty
            return null;
        }
        return world[x][y]; //otherwise return that cell.
    }
    
    public boolean[] senseCell(int x, int y){  //may change this section from a sequence of checks to a cell intrinsic. 
        boolean stench = false;
        boolean breeze = false;
        boolean gold = false;
        if(world[x][y].get('g')){
            gold = true;
            agent.seeGlitter();
        }
        // we want cells (x+1,y), (x,y+1), (x-1,y), (x,y-1)
        if(x+1 < world.length){
            if(! stench && world[x+1][y].get('w')){ //the cell to the right/east of a Wumpus smells
                stench = true;
                agent.smellStench();
            }
            if(! breeze && world[x+1][y].get('p')){ //the cell to the right/east of a pit has a breeze
                breeze = true;
                agent.feelBreeze();
            }
        }
        if(y+1 < world.length){
            if(! stench && world[x][y+1].get('w')){ //the cell above/North of a Wumpus smells...
                stench = true;
                agent.smellStench();
            }
            if(! breeze && world[x][y+1].get('p')){ //the cell above/North of a pit has a breeze...
                breeze = true;
                agent.feelBreeze();
            }
        }
        if(x-1 >= 0){
            if(! stench && world[x-1][y].get('w')){ //Stench left/West
                stench = true;
                agent.smellStench();
            }
            if(! breeze && world[x-1][y].get('p')){  //breeze left/west
                breeze = true;
                agent.feelBreeze();
            }
        }
        if(y-1 >= 0){
            if(! stench && world[x][y-1].get('w')){ //stench below/south
                stench = true;
                agent.smellStench();
            }
            if(! breeze && world[x][y-1].get('p')){ //breeze below south
                breeze = true;
                agent.feelBreeze();
            }
        }
        boolean[] senses = {stench, breeze, gold}; //setting what we preceive, but not any locations.
        return senses;
    }
    
    public void turnExplorer( boolean left){ //Directions are 1 for "East," 2 for "South," 3 for "West," and 4 for "North."
        if(left){
            if(agentState[2] == 1) agentState[2] = 4; //if we are facing East and turn left we are now facing North.
            else agentState[2]--; //otherwise we just count down to get the proper facing.
        }else{
            if(agentState[2] == 4) agentState[2] = 1; //if we are facing North and turn right we are now facing East.
            else agentState[2]++; //otherwise we count up.
        }
    }
    
    public void moveExplorer(int x, int y){ //inputs are the cell we are attempting to move to
        if( x >= world.length || y >= world.length || x < 0 || y < 0){ //if attempting to move "out of bounds (and into a wall)."
            agent.feelBump( agentState[0], agentState[1]); //we don't actually move, but we feel a bump...
        }
        else if(world[x][y].get('o')){ //Or we just walk into a wall
            agent.feelBump( agentState[0], agentState[1]);
        }
        else if(world[x][y].get('w')){ //Or we just walk into a Wumpus
            agent.die( agentState[0], agentState[1], true); //return to last safe location, and die by a Wumpus
        }
        else if(world[x][y].get('p')){ //Or we just walk into a pit
            agent.die( agentState[0], agentState[1], false); //return to a safe location, and die by a pit.
        }
        else{ //if it's empty...
            world[agentState[0]][agentState[1]].set('e', false); //remove the agent from where we were.
            agentState[0] = x; //update our agent's X coordinate
            agentState[1] = y; //update our agent's y coordinate
            world[agentState[0]][agentState[1]].set('e', true); //place the agent in that cell
            agent.state(agentState);
        }
        
    }
    
    public void shootArrow(int x, int y, int direction){ //the location fired from and direction fired
        boolean shotWumpus = false;
        switch(direction){ //the arrow travels along our facing
            case 1:
                for(; x < world.length; x++){ //traveling along the X axis, increasing (shot traveling East)
                    if(world[x][y].get('w')){ //if we hit a Wumpus...
                        world[x][y].set('w', false); //No more Wumpus
                        shotWumpus = true; //And toggle a scream.
                        break;
                    }
                    else if (world[x][y].get('o')){ //if we hit a wall
                         break; //what the arrow does, get it?
                    }
                }
                break;
            case 2:
                for(; y < world.length; y--){ //traveling down the Y axis, decreasing (going South)
                    if(world[x][y].get('w')){
                        world[x][y].set('w', false);
                        shotWumpus = true;
                        break;
                    }
                    else if (world[x][y].get('o')){ //if we hit a wall
                         break; 
                    }
                }
                break;
            case 3:
                for(; x >= 0; x--){ //traveing down the X axis, decreasing (going West)
                    if(world[x][y].get('w')){
                        world[x][y].set('w', false);
                        shotWumpus = true;
                        break;
                    }
                    else if (world[x][y].get('o')){ //if we hit a wall
                         break; 
                    }
                }
                break;
            case 4:
                for(; y >= 0; y++){ //traveling up the Y axis, increasing. (Going North)
                    if(world[x][y].get('w')){
                        world[x][y].set('w', false);
                        shotWumpus = true;
                        break;
                    }
                    else if (world[x][y].get('o')){ //if we hit a wall
                         break; 
                    }
                }
                break;
        }
        if(shotWumpus){
            agent.hearScream(); //kinda self explanatory
        }
    }
    
    public boolean removeGold(){
        int x = agentState[0];
        int y = agentState[1];
        if(x < world.length && y < world[x].length && x >= 0 && y >= 0 && world[x][y].get('g')){
            world[x][y].set('g', false); //removing the gold from the world.
            return true;
        }
        return false; //you can't pick it up if it's not there.
    }
}

