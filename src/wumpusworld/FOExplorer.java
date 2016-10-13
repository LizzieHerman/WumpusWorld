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
    }
    public void feelBump(int x1, int y1){
        worldMap.setCell(x, y, 'o', true);
        x = x1;
        y = y1;
        System.out.print("Feel Bump");
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
}
