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
public class RExplorer extends Explorer {

    public RExplorer(WumpusWorld w, int n, int num) {
        super(w, n, num);
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
         * where do we go
        */
    }
}
