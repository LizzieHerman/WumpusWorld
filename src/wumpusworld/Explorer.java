package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
public class Explorer {
    int x;
    int y;
    int direction; // 1=up, 2=right, 3=down, 4=left
    int cost;
    int size;
    int arrows;
    int timesDied;
    WumpusWorld world;
    
    public Explorer(WumpusWorld w, int n, int num){ // the world it is exploring, world size (n by n), number wumpi num
        x = 0;
        y = 0;
        direction = 1;
        cost = 0;
        size = n;
        arrows = num;
        timesDied = 0;
        world = w;
    }
    
    public void move(){
        cost -= 10;
        switch(direction){
            case 1:
                x++;
                break;
            case 2:
                y++;
                break;
            case 3:
                x--;
                break;
            case 4:
                y--;
                break;
        }
        world.moveExplorer(x, y);
        getPercepts();
    }
    
    public void turnRight(){
        cost -= 10;
        if(direction == 4) direction = 1;
        else direction++;
        world.turnExplorer(false);
    }
    
    public void turnLeft(){
        cost -= 10;
        if(direction == 1) direction = 4;
        else direction--;
        world.turnExplorer(true);
    }
    
    // return whether able to shoot arrow
    public boolean shootArrow(){
        if(arrows < 0){
            return false;
        }
        cost -= 10;
        arrows--;
        world.shootArrow(x,y,direction);
        return true;
    }
    
    public void feelBump(int x1, int y1){
        x = x1;
        y = y1;
        System.out.print("Feel Bump");
        /*
         * TO-DO
         * send sense info to knowledge base
         */
    }
    
    public void hearScream(){
        cost += 10;
        System.out.print("Heard Scream");
        /*
         * TO-DO
         * send sense info to knowledge base
         */
    }
    
    // takes in last safe location and whetheror not a wumpus killed them
    public void die(int x1, int y1, boolean wumpus){
        timesDied++;
        cost -= 1000;
        x = x1;
        y = y1;
        System.out.print("You died");
        // add wumpus/ pit to knowledge base
    }
    
    public void seeGlitter(){
        System.out.print("See Glittering");
        // add gold to knowledge base
    }
    
    public void grabGold(){
        if(world.removeGold()){
            cost += 1000;
            System.out.print("You Won");
            // how to end game
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
    
    public void getPercepts(){
        boolean[] senses = world.senseCell(x, y);
        // if don't smell stench or feel breeze then surrounding cells are safe
        if(!(senses[0] || senses[1])) System.out.print("Surrounding Cells are Safe");
        /*
         * TO-DO
         * other methods update knowledge
         * infer where to go
        */
    }
}
