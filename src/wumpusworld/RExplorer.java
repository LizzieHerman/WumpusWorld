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
    }
    
    public void start(){
        System.out.println("So it begins."); //testline
        //agressiveExplorer();
        cautiousExplorer();
    }

    public void cautiousExplorer(){  
        Random rand = new Random();
        int moves = 0;
        double num;
        /* testlines
        move();
        move();
        turnLeft();
        move();
        move();
        */
        boolean[] senses = world.senseCell(x,y);
        while (senses[2] != true){ //while we do not see glittering 
            System.out.println();
            System.out.println("Current position is (" + x + "," + y + ")"); //testline
           // System.out.println(world.senseCell(x,y)[0]); testlines
          //  System.out.println(world.senseCell(x,y)[1]);
          
          //RANDOM MOVE
            if (senses[0] == false && senses[1] == false){ //if there is no present danger, we are supposed to move at random. 
            //System.out.println("No danger detected."); //testline
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
                        moves++;
                        break;
                    case 3:
                        turnRight();
                        moves++;
                        break;
                    case 2: //about face, if we are facing South
                        turnRight();
                        turnRight();
                        moves += 2;
                        break;
                    default:
                        break;
                }
                    moves++;
                    move(); //and then move
                    senses = world.senseCell(x,y);
                }
                
                else if (num <= .50){ //move South
                    System.out.println("Moving South."); //testline
                    switch (direction) {
                    case 3:
                        //use the fewest moves to face South
                        moves++;
                        turnLeft();
                        break;
                    case 1:
                        moves++;
                        turnRight();
                        break;
                    case 4: //If we are facing North we about face
                        moves+=2;
                        turnRight();
                        turnRight();
                        break;
                }
                    moves++;
                    move();
                    senses = world.senseCell(x,y);
                }
                
                else if (num <= .75){ //Move East
                    System.out.println("Moving East."); //testline
                    switch (direction) {
                    case 2:
                        //use the fewest moves to face East
                        turnLeft();
                        moves++;
                        break;
                    case 4:
                        moves++;
                        turnRight();
                        break;
                    case 3: //if we are facing West turn around
                        moves+=2;
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    moves++;
                    move();
                    senses = world.senseCell(x,y);
                }
                
                else if (num <= 1){ //move West
                    System.out.println("Moving West."); //testline
                    switch (direction) {
                    case 4:
                        //use the fewest moves to face West
                        moves++;
                        turnLeft();
                        break;
                    case 2:
                        moves++;
                        turnRight();
                        break;
                    case 1: //about face, if we are facing East
                        moves+=2;
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    moves++;
                    move();
                    senses = world.senseCell(x,y);
                }
            
            } //END OF RANDOM MOVE if current space is safe
            
            else if (senses[0] == true && arrows != 0){ // if we smell a Wumpus and are currently armed
                num = rand.nextDouble();
                //We can be sure that for the first arrow we fire the Wumpus is not behind us, so we either need to fire to the front, left, or right.
                //but how can we refine further shots if we miss the first one with no internal state?
                if (num <= .33){
                    System.out.println("Turning right and letting fly.");
                    turnRight();
                    shootArrow();
                }
                else if (num <= .66){
                   System.out.println("Turning left and letting fly.");
                   turnLeft();
                   shootArrow();
                }
                else if (num <= 1){
                   System.out.println("Letting fly.");
                   shootArrow();
                }
                
                senses = world.senseCell(x,y);//update our senses
            }
            
            else {
                //System.out.println("Danger is nearby.");
                //if (world.senseCell(x,y)[0] == true)
                //System.out.println("We detect a Wumpus.");
                //if (world.senseCell(x,y)[1] == true)
                //System.out.println("We detect a Pit.");
                moves+=3;
                turnRight();
                turnRight();
                move();
                senses = world.senseCell(x,y);
            }
            
        }
        moves++;
        grabGold();
        System.out.println();
        System.out.println("WORLD COMPLETED WITH A SCORE OF " + cost + ".");
        System.out.println();
        
    }
    
    public void agressiveExplorer(){
        Random rand = new Random();
        int moves = 0;
        double num;
        boolean[] senses = world.senseCell(x,y);
        while (senses[2] != true){ 
            System.out.println("Current position is (" + x + "," + y + ")"); //testline
        num = rand.nextDouble();
        if (senses[0] == true && arrows != 0){ // if we smell a Wumpus and are currently armed
                //We can be sure that for the first arrow we fire the Wumpus is not behind us, so we either need to fire to the front, left, or right.
                //but how can we refine further shots if we miss the first one with no internal state?
                if (num <= .33){
                    System.out.println("Turning right and letting fly.");
                    turnRight();
                    shootArrow();
                }
                else if (num <= .66){
                   System.out.println("Turning left and letting fly.");
                   turnLeft();
                   shootArrow();
                }
                else if (num <= 1){
                   System.out.println("Letting fly.");
                   shootArrow();
                }
                
                senses = world.senseCell(x,y);//update our senses
            }
        else if (num <= .25){  //move north
                //System.out.println("Moving North."); //testline
                switch (direction) {
                    case 1:
                        //use the fewest moves to face North
                        moves++;
                        turnLeft();
                        break;
                    case 3:
                        moves++;
                        turnRight();
                        break;
                    case 2: //about face, if we are facing South
                        moves+=2;
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                moves++;
                    move(); //and then move
                    senses = world.senseCell(x,y);
                }
                
                else if (num <= .50){ //move South
                    //System.out.println("Moving South."); //testline
                    switch (direction) {
                    case 3:
                        //use the fewest moves to face South
                        moves++;
                        turnLeft();
                        break;
                    case 1:
                        moves++;
                        turnRight();
                        break;
                    case 4: //If we are facing North we about face
                        moves+=2;
                        turnRight();
                        turnRight();
                        break;
                }
                    moves++;
                    move();
                    senses = world.senseCell(x,y);
                }
                
                else if (num <= .75){ //Move East
                    //System.out.println("Moving East."); //testline
                    switch (direction) {
                    case 2:
                        //use the fewest moves to face East
                        moves++;
                        turnLeft();
                        break;
                    case 4:
                        moves++;
                        turnRight();
                        break;
                    case 3: //if we are facing West turn around
                        moves+=2;
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    moves++;
                    move();
                    senses = world.senseCell(x,y);
                }
                
                else if (num <= 1){ //move West
                    //System.out.println("Moving West."); //testline
                    switch (direction) {
                    case 4:
                        //use the fewest moves to face West
                        moves++;
                        turnLeft();
                        break;
                    case 2:
                        moves++;
                        turnRight();
                        break;
                    case 1: //about face, if we are facing East
                        moves+=2;
                        turnRight();
                        turnRight();
                        break;
                    default:
                        break;
                }
                    moves++;
                    move();
                    senses = world.senseCell(x,y);
                }
        }
        moves++;
        grabGold();
        System.out.println();
        System.out.println("WORLD COMPLETED WITH A SCORE OF " + cost + ".");
        System.out.println();
    }
    
    public void feelBump(int x1, int y1){
        x = x1;
        y = y1;
        System.out.println("Feel Bump");
        /*
         * TO-DO
         * send sense info to knowledge base
         */
    }
    
    public void hearScream(){
        cost += 10;
        System.out.print("WUMPUS SLAIN!");
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
            System.out.println();
            System.out.println("Gold grabbed!");
            System.out.println("You won!");
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
