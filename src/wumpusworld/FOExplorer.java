package wumpusworld;

/**
 *
 * @author Lizzie Herman
 *Some really small additions by Ryan Freivalds
 */
public class FOExplorer extends Explorer {
     Map worldMap;
     int numWump;
     //Clause[] rules;
    
    public FOExplorer(WumpusWorld w, int n, int num) {
         super(w, n, num);
         worldMap = new Map(n);
         numWump = num;
         worldMap.setCell(x, y, 'v');
         worldMap.setCell(x, y, 's', true);
//         rules = new Clause[numRules];
//         for(int i = 0; i < numRules; i++){
//        	 rules[i] = new Clause(i);
//         }
    }
    
    public void start(){
        /*
         * TO-DO
         * decide how the program will start
         */
        move();
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
        if(x < worldMap.size() && y < worldMap.size() && x >= 0 && y >= 0){ // test to see if we hit wall or obstacle
            worldMap.setCell(x, y, 'o', true);
        }
        x = x1;
        y = y1;
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
