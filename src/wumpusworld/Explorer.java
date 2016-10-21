package wumpusworld;

/**
 *
 * @author Lizzie Herman, Greg Neznanski
 *Some really small additions by Ryan Freivalds
 */
public class Explorer {
    int x;
    int y;
    int direction; // 1 = East, 2 = South, 3 = West, 4 = North
    int cost;
    int size;
    int arrows;
    int timesDied;
    WumpusWorld world;
    int[] agentState, deadWumpusCoords, deadCell;
    private boolean wumpusKilled;
    boolean gameWon;
    
    public Explorer(WumpusWorld w, int n, int num){ // the world it is exploring, world size (n by n), number wumpi num
        x = 0;
        y = 0;
        direction = 1;
        cost = 0;
        size = n;
        arrows = num;
        timesDied = 0;
        world = w;
        this.agentState = new int[]{0,0,1};
        this.wumpusKilled = false;
        this.gameWon = false;
        this.deadCell = new int[2];
    }
    
    public void start(){
        /*
         * TO-DO
         * decide how the program will start
         */
        turnRight();
    }
    
    public void move(){
        cost -= 10;
        switch(direction){
            case 1: //moving East
                x++;
                break;
            case 2: //Moving South
                y--;
                break;
            case 3: //Moving West
                x--;
                break;
            case 4: //Moving North
                y++;
                break;
        }
        world.moveExplorer(x, y);
        //getPercepts();
    }
    
    public void turnRight(){
        cost -= 10;
        if(direction == 4) direction = 1; //if facing North and turning right, we are now facing East
        else direction++; //otherwise our direction is properly set with ++
        world.turnExplorer(false); //indicates to the world class that we are turning right
    }
    
    public void turnLeft(){
        cost -= 10;
        if(direction == 1) direction = 4; //if facing East and turning Left, we are now facing North
        else direction--; //otherwise our direction is properly set with --
        world.turnExplorer(true); //indicates to the world class that we are turning left
    }
    
    // return whether able to shoot arrow
    public boolean shootArrow(){
        if(arrows <= 0){ 
            return false; //Can't fire what's not there.
        }
        cost -= 10;
        arrows--;
        world.shootArrow(x,y,direction); //sending our "starting" square of firing, and which direction the arrow is being fired.
        return true;
    }
    
    public void feelBump(int x1, int y1){
        x = x1;
        y = y1;
        System.out.print("Feel Bump");
    }
    
    public void hearScream(){
        cost += 10;
        System.out.print("Heard Scream");
        wumpusKilled = true;
    }
    
    // takes in last safe location and whetheror not a wumpus killed them
    public void die(int x1, int y1, boolean wumpus){
        timesDied++;
        cost -= 1000;
        if(wumpus){
        	System.out.print("You died to a wumpus in cell " + x + ", " + y + ". Returning to " + x1 + ", " + y1 + ".");
        }else{
        	System.out.print("You fell in a pit in cell " + x + ", " + y + ". Returning to " + x1 + ", " + y1 + ".");
        }
        x = x1;
        y = y1;
    }
    
    public void deadCoords(int x, int y){
    	this.deadCell[0] = x;
    	this.deadCell[1] = y;
    }
    
    public void seeGlitter(){
        System.out.print("See Glittering");
        // add gold to knowledge base
    }
    
    public void grabGold(){
        if(world.removeGold()){
            cost += 1000;
            System.out.println("You Won");
            this.gameWon = true;
        }
    }
    
    public void smellStench(){
        System.out.print("Smelled Stench");
        /*
         * TO-DO
         * send sense info to knowledge base
         */
    }
    
    public void feelBreeze(){
        System.out.print("Felt Breeze");
        /*
         * TO-DO
         * send sense info to knowledge base
         */
    }
    
//    public void getPercepts(){
//        boolean[] senses = world.senseCell(x, y);
//        // if don't smell stench or feel breeze then surrounding cells are safe
//        if(!(senses[0] || senses[1])) System.out.print("Surrounding Cells are Safe");
//        /*
//         * TO-DO
//         * other methods update knowledge
//         * infer where to go
//        */
//    }
    
    public void state(int[] agentState){
    	this.agentState = agentState;
    }
    
    public boolean wumpusKilled(){
    	boolean temp = this.wumpusKilled;
    	this.wumpusKilled = false;
    	return temp;
    }
    
    public boolean checkWin(){
    	return this.gameWon;
    }
    
    public int getCost(){
        return cost;
    }
}
