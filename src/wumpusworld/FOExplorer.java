/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
public class FOExplorer extends Explorer {
    Map worldMap;
    int numWump;
    
    public FOExplorer(WumpusWorld w, int n, int num) {
        super(w, n, num);
        worldMap = new Map(n);
        numWump = num;
        worldMap.setCell(x, y, 'v');
        worldMap.setCell(x, y, 's', true);
        getPercepts();
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
        worldMap.setCell(x, y, 'v');
        worldMap.setCell(x, y, 'b', true);
        worldMap.setCell(x, y, 'f', false);
        world.moveExplorer(x, y);
        getPercepts();
    }
    
    public void feelBump(int x1, int y1){
        worldMap.setCell(x, y, 'o', true);
        x = x1;
        y = y1;
    }
    
    public void hearScream(){
        numWump--;
        cost += 10;
        System.out.print("Heard Scream");
        /*
         * TO-DO
         * send sense info to knowledge base
         */
    }
    
    // takes in last safe location and whetheror not a wumpus killed them
    public void die(int x1, int y1, boolean wumpus){
        if(wumpus) worldMap.setCell(x, y, 'w', true);
        if(! wumpus) worldMap.setCell(x, y, 'p', true);
        cost -= 1000;
        x = x1;
        y = y1;
        System.out.print("You died");
    }
    
    public void seeGlitter(){
        worldMap.setCell(x, y, 'g', true);
    }
    
    public void grabGold(){
        if(world.removeGold()){
            cost += 1000;
            // how to end game
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
