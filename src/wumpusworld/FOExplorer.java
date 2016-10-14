package wumpusworld;

/**
 *
 * @author Lizzie Herman
 *Some really small additions by Ryan Freivalds
 */
public class FOExplorer extends Explorer{
    int x;
    int y;
    int direction; // 1 = East, 2 = South, 3 = West, 4 = North
    int cost;
    int size;
    int arrows;
    int timesDied;
    WumpusWorld world;
    
    public FOExplorer(WumpusWorld w, int n, int num){ // the world it is exploring, world size (n by n), number wumpi num
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
        getPercepts();
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
    
    public void grabGold(){
        if(world.removeGold()){
            cost += 1000;
            // how to end game
            System.out.print("You won");
        }
    }
    
    public void smellStench(){
        // we want cells (x+1,y), (x,y+1), (x-1,y), (x,y-1)
        if(x+1 < worldMap.size()){
            worldMap.setCell(x+1, y, 'u', true);
        }
        if(y+1 < worldMap.size()){
            worldMap.setCell(x, y+1, 'u', true);
        }
        if(x-1 >= 0){
            worldMap.setCell(x-1, y, 'u', true);
        }
        if(y-1 >= 0){
            worldMap.setCell(x, y-1, 'u', true);
        }
    }
    
    public void feelBreeze(){
        // we want cells (x+1,y), (x,y+1), (x-1,y), (x,y-1)
        if(x+1 < worldMap.size()){
            worldMap.setCell(x+1, y, 'i', true);
        }
        if(y+1 < worldMap.size()){
            worldMap.setCell(x, y+1, 'i', true);
        }
        if(x-1 >= 0){
            worldMap.setCell(x-1, y, 'i', true);
        }
        if(y-1 >= 0){
            worldMap.setCell(x, y-1, 'i', true);
        }
    }
    
    public void getPercepts(){
        boolean senses[] = world.senseCell(x, y);
        // if don't smell stench or feel breeze then surrounding cells are safe
        if(!(senses[0] || senses[1])){
            if(x+1 < worldMap.size()){
                worldMap.setCell(x+1, y, 's', true);
                worldMap.setCell(x+1, y, 'u', false);
                worldMap.setCell(x+1, y, 'i', false);
            }
            if(y+1 < worldMap.size()){
                worldMap.setCell(x, y+1, 's', true);
                worldMap.setCell(x, y+1, 'u', false);
                worldMap.setCell(x, y+1, 'i', false);
            }
            if(x-1 >= 0){
                worldMap.setCell(x-1, y, 's', true);
                worldMap.setCell(x-1, y, 'u', false);
                worldMap.setCell(x-1, y, 'i', false);
            }
            if(y-1 >= 0){
                worldMap.setCell(x, y-1, 's', true);
                worldMap.setCell(x, y-1, 'u', false);
                worldMap.setCell(x, y-1, 'i', false);
            }
        }
        // if there is no gold in cell and you did not die in cell then cell is safe
        if(senses[2]) grabGold();
        else {
            worldMap.setCell(x, y, 's', true);
            worldMap.setCell(x, y, 'u', false);
            worldMap.setCell(x, y, 'i', false);
        }
        /*
         * TO-DO
         * other methods update knowledge
         * infer where to go
        */
    }
}
