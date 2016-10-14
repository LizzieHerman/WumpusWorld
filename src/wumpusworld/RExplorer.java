/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wumpusworld;
import java.util.Random;
/**
 *
 * @author Lizzie Herman, Ryan Freivalds
 * 
 */
public class RExplorer extends Explorer {

    public RExplorer(WumpusWorld w, int n, int num) { //n is the size of the world, num is the number of wumpuses
        super(w, n, num);
        System.out.println("So it begins."); //testline
        agressiveExplorer();
        
        
    }

    public void cautiousExplorer(){  
        Random rand = new Random();
        
        double num;
        /* testlines
        move();
        move();
        turnLeft();
        move();
        move();
        */
        while (world.senseCell(x,y)[2] != true){ //while we do not see glittering 
            System.out.println("Current position is (" + x + "," + y + ")"); //testline
            if (world.senseCell(x,y)[0] && world.senseCell(x,y)[1] == false){ //if there is no present danger, we are supposed to move at random. CURRENTLY BROKEN. He always sees danger.
            System.out.println("No danger detected."); //testline
                num = rand.nextDouble();    //generate a random number
            /*
            * rather than a 1/3rd chance to turn left, 1/3rd turn right, 1/3rd move forward
            * we will do a 1/4 face-to-and-then-move North/South/East/West to cut down on sequences of turns
            * that do not advance our progress. 
            * Remember that the direction scheme is 1 = East, 2 = South, 3 = West, 4 = North.
            */
                if (num <= .25){  //move north
                System.out.println("Moving North."); //testline
                switch (direction) {
                    case 1:
                        //use the fewest moves to face North
                        turnLeft();
                        break;
                    case 3:
                        turnRight();
                        break;
                    case 2: //about face, if we are facing South
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    move(); //and then move
                }
                
                else if (num <= .50){ //move South
                    System.out.println("Moving South."); //testline
                    switch (direction) {
                    case 3:
                        //use the fewest moves to face South
                        turnLeft();
                        break;
                    case 1:
                        turnRight();
                        break;
                    case 4: //If we are facing North we about face
                        turnRight();
                        turnRight();
                        break;
                }
                    move();
                }
                
                else if (num <= .75){ //Move East
                    System.out.println("Moving East."); //testline
                    switch (direction) {
                    case 2:
                        //use the fewest moves to face East
                        turnLeft();
                        break;
                    case 4:
                        turnRight();
                        break;
                    case 3: //if we are facing West turn around
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    move();
                }
                
                else if (num <= 1){ //move West
                    System.out.println("Moving West."); //testline
                    switch (direction) {
                    case 4:
                        //use the fewest moves to face West
                        turnLeft();
                        break;
                    case 2:
                        turnRight();
                        break;
                    case 1: //about face, if we are facing East
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    move();
                }
            
            } //END of random move if current space is safe
            else {
                System.out.println("Danger is nearby.");
                turnRight();
                turnRight();
                move();
            }
            
        grabGold();
        }
    }
    
    public void agressiveExplorer(){
        Random rand = new Random();
        double num;
        while (world.senseCell(x,y)[2] != true){ 
        num = rand.nextDouble();
        if (num <= .25){  //move north
                System.out.println("Moving North."); //testline
                switch (direction) {
                    case 1:
                        //use the fewest moves to face North
                        turnLeft();
                        break;
                    case 3:
                        turnRight();
                        break;
                    case 2: //about face, if we are facing South
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    move(); //and then move
                }
                
                else if (num <= .50){ //move South
                    System.out.println("Moving South."); //testline
                    switch (direction) {
                    case 3:
                        //use the fewest moves to face South
                        turnLeft();
                        break;
                    case 1:
                        turnRight();
                        break;
                    case 4: //If we are facing North we about face
                        turnRight();
                        turnRight();
                        break;
                }
                    move();
                }
                
                else if (num <= .75){ //Move East
                    System.out.println("Moving East."); //testline
                    switch (direction) {
                    case 2:
                        //use the fewest moves to face East
                        turnLeft();
                        break;
                    case 4:
                        turnRight();
                        break;
                    case 3: //if we are facing West turn around
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    move();
                }
                
                else if (num <= 1){ //move West
                    System.out.println("Moving West."); //testline
                    switch (direction) {
                    case 4:
                        //use the fewest moves to face West
                        turnLeft();
                        break;
                    case 2:
                        turnRight();
                        break;
                    case 1: //about face, if we are facing East
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    move();
                }
        }
        grabGold();
    }
    
    public void feelBump(int x1, int y1){
        //x = x1;
        //y = y1;
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
}

    
