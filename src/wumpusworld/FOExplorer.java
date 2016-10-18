package wumpusworld;

import java.util.ArrayList;

/**
 *
 * @author Lizzie Herman
 *Some really small additions by Ryan Freivalds
 */
public class FOExplorer extends Explorer {
     Map worldMap;
     int numWump;
     Clause[] rules;
     ArrayList<Relation> relations;
     int[] moveList; //holds 1 for safe, 0 for unsafe for the 4 surrounding cells
    
    public FOExplorer(WumpusWorld w, int n, int num) {
         super(w, n, num);
         worldMap = new Map(n);
         numWump = num;
         worldMap.setCell(x, y, 'v');
         worldMap.setCell(x, y, 's', true);
         rules = new Clause[16];    ////change size to total number of clauses after all have been added to Clause
         for(int i = 0; i < 16; i++){
        	 rules[i] = new Clause(i);
         }
         relations = new ArrayList<Relation>();
         moveList = new int[]{1,1,1,1};
    }
    
    public void start(){
        boolean[] senses = world.senseCell(x,y);
        while (senses[2] != true){
        findMove();
        }
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
    
    //Update cells using clauses
    public void updateDB(){  //Update loop numbers when all clauses added
    	int[] result;
    	
    	//update cells - clauses 2a-2j
    	rules[3].condition(agentState, worldMap); //2a
    	for(int i = 4; i <= 7; i++){ //2b-2e
    		result = rules[i].condition(agentState, worldMap);
    		if(result[0] == 1){
    			char hazard;
    			if(result[1] == 0){
    				hazard = 'w';
    			}else{
    				hazard = 'p';
    			}
    			Relation newRelation = new Relation(worldMap.getSurrounding(agentState[0], agentState[1], agentState[2]), hazard);
    			relations.add(newRelation);
    		}
    	}
    	for(int i = 8; i <= 13; i++){ //2f-2j
    		rules[i].condition(agentState, worldMap);
    	}
    	
    	//check relations
    	boolean valid = true;
    	if(!relations.isEmpty()){
	    	for(int i = 0; i < relations.size(); i++){
	    		valid = relations.get(i).check();
	    		if(!valid){
	    			relations.get(i).update();
	    		}
	    	}
    	}
    	
    	//check clauses 1a-1c, 3a-3c
    	for(int i = 0; i <= 2; i++){
    		result = rules[i].condition(agentState, worldMap);
    		if(result != null){
    			moveList[result[0]] = result[1];
    		}
    	}
    	for(int i = 14; i <= 16; i++){
    		rules[i].condition(agentState, worldMap);
    	}
    }
    
    //Use info from checking clauses to decide on a move
    public void findMove(){
    	int numMoves = 0;
    	boolean moved = false;
    	System.out.println("Current position is (" + x + "," + y + ")"); //testline
        
    	for(int i = 0; i < moveList.length; i++){
    		if(moveList[i] == 1){
    			numMoves++;
    		}
    	}
    	
    	if(numMoves == 1){ //Move backwards
                System.out.println("Moving backwards."); //testline
    		super.turnRight();
    		super.turnRight();
    		super.move();
    		moved = true;
    	}
    	
    	if(numMoves > 1){ //Move to a frontier cell if there is one
    		if(moveList[0] == 1 && worldMap.getCell(agentState[0], agentState[1]).get('f')){ //Move left
                        System.out.println("Moving left seeking fontier cell.");
    			super.turnLeft();
    			super.move();
    			moved = true;
    		}else if(moveList[1] == 1 && worldMap.getCell(agentState[0], agentState[1]).get('f')){ //Move up
                        System.out.println("Moving forward seeking fontier cell.");
    			super.turnLeft(); //This need to be here to just move up?
    			super.move();
    			moved = true;
    		}else if(moveList[2] == 1 && worldMap.getCell(agentState[0], agentState[1]).get('f')){ //Move right
                      System.out.println("Moving right seeking fontier cell.");
            		super.turnRight();  //replaced this with turn right -Ryan
    			super.move();
    			moved = true;
    		}
    		
    		if(!moved){ //Move towards a relation cell
    			if(!relations.isEmpty()){
    				Relation tryToSolve = relations.get(0);
    				MapCell direction = tryToSolve.getCell();
    				int[] coords = direction.getCoords();
    				if(coords[0] < agentState[0]){ //Move in -x direction
                                        System.out.println("Moving West seeking relation cell.");
    					super.turnLeft();
    					super.move();
    					moved = true;
    				}else if(coords[0] > agentState[0]){ //Move in +x direction
                                        System.out.println("Moving East seeking relation cell.");
    					super.turnRight();
    					super.move();
    					moved = true;
    				}else if(coords[1] < agentState[1]){ //Move in -y direction
                                        System.out.println("Moving South seeking relation cell.");
    					super.move();
    					moved = true;
    				}else if(coords[1] > agentState[1]){ //Move in +y direction
                                        System.out.println("Moving North seeking relation cell.");
    					super.turnRight();
    					super.turnRight();
    					super.move();
    					moved = true;
    				}
    			}
    		}
    		
    		if(!moved){ //If still haven't moved, move in any safe direction
    			int random = (int) Math.random()*4 + 1;
    			while(!moved){
	    			if(moveList[random] == 1){
	    				if(random == 1){ //Move east
                                                System.out.println("Moving East to safe cell.");
		    				super.turnRight();
		    				super.move();
		    				moved = true;
		    			}else if(random == 2){ //Move south
                                                System.out.println("Moving South to safe cell.");
		    				super.turnRight();
		    				super.turnRight();
		    				super.move();
		    				moved = true;
		    			}else if(random == 3){ //Move west
                                                System.out.println("Moving West to safe cell.");
		    				super.turnLeft();
		    				super.move();
		    				moved = true;
		    			}else if(random == 4){ //Move north
                                                System.out.println("Moving North to safe cell.");
		    				super.move();
		    				moved = true;
		    			}
	    			}else{
                                        System.out.println("I'm freaking lost.");
	    				random = (int) Math.random()*4 + 1;
	    			}
    			}
    		}
    	}
    }
}
