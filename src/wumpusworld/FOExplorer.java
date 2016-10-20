package wumpusworld;

import java.util.ArrayList;

/**
 *
 * @author Lizzie Herman, Greg Neznanski
 *Some really small additions by Ryan Freivalds
 */
public class FOExplorer extends Explorer {
     Map worldMap;
     int numWump;
     Clause[] rules;
     ArrayList<Relation> relations;
     int[] moveList; //holds 1 for safe, 0 for unsafe for the 4 surrounding cells
     boolean gameWon, noMoves;
     private int[] moveCoords;
    
    public FOExplorer(WumpusWorld w, int n, int num) {
         super(w, n, num);
         worldMap = new Map(n);
         for(int i = 0; i < worldMap.map.length; i++){
        	 for(int j = 0; j < worldMap.map.length; j++){
        		 worldMap.map[i][j].setDisplay(w.world[i][j].getDisplay());
        		 if(w.world[i][j].get('l')){
        			 worldMap.map[i][j].set('l', true); //set smell in mapcell if cell has smell
        		 }
        		 if(w.world[i][j].get('z')){
        			 worldMap.map[i][j].set('z', true); //set breeze in mapcell if cell has breeze
        		 }
        	 }
         }
         numWump = num;
         worldMap.setCell(x, y, 'v');
         worldMap.setCell(x, y, 's', true);
         rules = new Clause[16];    ////change size to total number of clauses after all have been added to Clause
         for(int i = 0; i < 16; i++){
        	 rules[i] = new Clause(i);
         }
         relations = new ArrayList<Relation>();
         moveList = new int[]{1,1,1,1};
         this.gameWon = false;
         this.noMoves = false;
         moveCoords = new int[3];
    }
    
    public void start(){
        updateDB();
        findMove();
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
        worldMap.getCell(moveCoords[0], moveCoords[1]).set('c', true);
        x = x1;
        y = y1;
        
        System.out.println("Walked into a wall.");
    }
    
    public void grabGold(){
        if(world.removeGold()){
            cost += 1000;
            // how to end game
            gameWon = true;
            System.out.print("You won");
        }
    }
    
    public void die(int x1, int y1, boolean wumpus){
    	super.die(x1, y1, wumpus);
    	if(wumpus){
    		worldMap.getCell(moveCoords[0], moveCoords[1]).set('w', true);
    	}else{
    		worldMap.getCell(moveCoords[0], moveCoords[1]).set('p', true);
    	}
    }
    
//    public void smellStench(){
//        // we want cells (x+1,y), (x,y+1), (x-1,y), (x,y-1)
//        if(x+1 < worldMap.size()){
//            worldMap.setCell(x+1, y, 'u', true);
//        }
//        if(y+1 < worldMap.size()){
//            worldMap.setCell(x, y+1, 'u', true);
//        }
//        if(x-1 >= 0){
//            worldMap.setCell(x-1, y, 'u', true);
//        }
//        if(y-1 >= 0){
//            worldMap.setCell(x, y-1, 'u', true);
//        }
//    }
//    
//    public void feelBreeze(){
//        // we want cells (x+1,y), (x,y+1), (x-1,y), (x,y-1)
//        if(x+1 < worldMap.size()){
//            worldMap.setCell(x+1, y, 'i', true);
//        }
//        if(y+1 < worldMap.size()){
//            worldMap.setCell(x, y+1, 'i', true);
//        }
//        if(x-1 >= 0){
//            worldMap.setCell(x-1, y, 'i', true);
//        }
//        if(y-1 >= 0){
//            worldMap.setCell(x, y-1, 'i', true);
//        }
//    }
//    
//    public void getPercepts(){
//        boolean senses[] = world.senseCell(x, y);
//        // if don't smell stench or feel breeze then surrounding cells are safe
//        if(!(senses[0] || senses[1])){
//            if(x+1 < worldMap.size()){
//                worldMap.setCell(x+1, y, 's', true);
//                worldMap.setCell(x+1, y, 'u', false);
//                worldMap.setCell(x+1, y, 'i', false);
//            }
//            if(y+1 < worldMap.size()){
//                worldMap.setCell(x, y+1, 's', true);
//                worldMap.setCell(x, y+1, 'u', false);
//                worldMap.setCell(x, y+1, 'i', false);
//            }
//            if(x-1 >= 0){
//                worldMap.setCell(x-1, y, 's', true);
//                worldMap.setCell(x-1, y, 'u', false);
//                worldMap.setCell(x-1, y, 'i', false);
//            }
//            if(y-1 >= 0){
//                worldMap.setCell(x, y-1, 's', true);
//                worldMap.setCell(x, y-1, 'u', false);
//                worldMap.setCell(x, y-1, 'i', false);
//            }
//        }
//        // if there is no gold in cell and you did not die in cell then cell is safe
//        if(senses[2]) grabGold();
//        else {
//            worldMap.setCell(x, y, 's', true);
//            worldMap.setCell(x, y, 'u', false);
//            worldMap.setCell(x, y, 'i', false);
//        }
//        /*
//         * TO-DO
//         * other methods update knowledge
//         * infer where to go
//        */
//    }
    
    //Update cells using clauses
    public void updateDB(){  ////////////////////////////////////Update loop numbers when all clauses added
    	int[] result;
    	boolean bump = false;
    	
    	//update cells - clauses 2a-2f
    	result = rules[3].checkClause(agentState, worldMap); //2a
    	if(result[0] == 1){
    		super.grabGold();
    	}
//    	result = rules[4].checkClause(agentState, worldMap); //2b
//    	if(result[0] == 1){
//    		bump = true;
//    	}
    	
    	
    	for(int i = 5; i <= 8; i++){ //2b-2f
    		result = rules[i].checkClause(agentState, worldMap);
    		if(result[0] == 1){
    			char hazard;
    			if(result[1] == 0){
    				hazard = 'u';
    			}else{
    				hazard = 'i';
    			}
    			//Add new relation
    			Relation newRelation = new Relation(worldMap.getSurrounding(agentState[0], agentState[1], agentState[2]), hazard);
    			relations.add(newRelation);
    		}
    	}
    	
    	//update relations
    	boolean valid = true;
    	if(!relations.isEmpty()){
	    	for(int i = 0; i < relations.size(); i++){
	    		valid = relations.get(i).check();
	    		if(!valid){
	    			relations.get(i).update();
	    		}
	    	}
    	}
    	
    	//Check for confirmed wumpus in a surrounding cell, if true shoot arrow, listen for scream, if scream remove relation from list
    	result = rules[9].checkClause(agentState, worldMap); //2g
    	if(result[0] == 1){
    		for(int i = 0; i < relations.size(); i++){
    			if(relations.get(i).getRelated().size() == 1){
    				if(relations.get(i).getHazard() == 'w'){
    					if(relations.get(i).getCell().get('w')){
							if(result[1] == 0){ //wumpus is in left cell
								super.turnLeft();
							}else if(result[1] == 2){ //wumpus is in right cell
								super.turnRight();
							}
    						super.shootArrow();
    						if(result[1] == 0){ //return to previous facing
								super.turnRight();
							}else if(result[1] == 2){ //return to previous facing
								super.turnLeft();
							}
    						boolean dead = super.wumpusKilled();
    						if(dead){
    							result = rules[10].checkClause(agentState, worldMap); //2h
    							relations.remove(i);
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	//Mark safe cells surrounding current cell, do after rules check
    	rules[11].checkClause(agentState, worldMap); //2i
    }
    
    //Use info from checking clauses to decide on a move
    public void findMove(){
    	int numMoves = 0;
    	boolean moved = false;
    	
    	System.out.println("current: " + agentState[0] + ", " + agentState[1] + " - facing: " + agentState[2]);
    	
    	MapCell[] surrounding = worldMap.getSurrounding(agentState[0], agentState[1], agentState[2]);
    	for(int i = 0; i < surrounding.length; i++){
    		if(surrounding[i] != null){
    			if(surrounding[i].get('s')){
    				moveList[i] = 1;
    			}else{
    				moveList[i] = 0;
    			}
    		}else{
    			moveList[i] = 0;
    		}
    	}
    	
    	for(int i = 0; i < moveList.length; i++){
    		if(moveList[i] == 1){
    			numMoves++;
    		}
    	}
    	
    	if(numMoves == 0){
    		//Need something here for taking a risk
    		System.out.println("No valid moves.");
    		//noMoves = true;
    		super.gameWon = true;
    		return;
    	}
    	
    	if(numMoves == 1){ //Move backwards
    		moveCoords[0] = agentState[0];
    		moveCoords[1] = agentState[1];
    		moveCoords[2] = agentState[2];
    		super.turnRight();
    		super.turnRight();
    		super.move();
    		moved = true;
    		System.out.println("Moving backwards.");
    	}
    	
    	if(numMoves > 1){ //Move to a frontier cell if there is one
    		int[] coordsLeft = surrounding[0].getCoords();
    		int[] coordsFront = surrounding[1].getCoords();
    		int[] coordsRight = surrounding[2].getCoords();
    		if(moveList[0] == 1 && worldMap.getCell(coordsLeft[0], coordsLeft[1]).get('f')){ //Move left
    			moveCoords[0] = coordsLeft[0];
    			moveCoords[1] = coordsLeft[1];
    			moveCoords[2] = agentState[2];
    			super.turnLeft();
    			super.move();
    			moved = true;
    			System.out.println("Moving left.");
    		}else if(moveList[1] == 1 && worldMap.getCell(coordsFront[0], coordsFront[1]).get('f')){ //Move up
    			moveCoords[0] = coordsFront[0];
    			moveCoords[1] = coordsFront[1];
    			moveCoords[2] = agentState[2];
    			super.turnLeft();
    			super.move();
    			moved = true;
    			System.out.println("Moving forward.");
    		}else if(moveList[2] == 1 && worldMap.getCell(coordsRight[0], coordsRight[1]).get('f')){ //Move right
    			moveCoords[0] = coordsRight[0];
    			moveCoords[1] = coordsRight[1];
    			moveCoords[2] = agentState[2];
    			super.turnLeft();
    			super.move();
    			moved = true;
    			System.out.println("Moving right.");
    		}
    		
    		if(!moved){ //Move towards a relation cell, or a confirmed wumpus
    			if(!relations.isEmpty()){
    				MapCell direction;
    				int[] coords = null;
    				for(int i = 0; i < relations.size(); i++){
    					if(relations.get(i).getRelated().size() != 1){
    						direction = relations.get(0).getCell();
    						coords = direction.getCoords();
    						break;
    					}else{
    						if(relations.get(i).getHazard() == 'w'){
    							direction = relations.get(0).getCell();
    							coords = direction.getCoords();
    						}
    					}
    				}
    				
    				if(coords != null){  ////////The move directions are still wrong here i think
	    				if(coords[0] < agentState[0]){ //Move in -x direction
	    					moveCoords[0] = coords[0];
	    	    			moveCoords[1] = coords[1];
	    					super.turnLeft();
	    					moveCoords[2] = agentState[2];
	    					super.move();
	    					moved = true;
	    					System.out.println("Moving -x.");
	    				}else if(coords[0] > agentState[0]){ //Move in +x direction
	    					moveCoords[0] = coords[0];
	    	    			moveCoords[1] = coords[1];
	    					super.turnRight();
	    					moveCoords[2] = agentState[2];
	    					super.move();
	    					moved = true;
	    					System.out.println("Moving +x.");
	    				}else if(coords[1] < agentState[1]){ //Move in -y direction
	    					moveCoords[0] = coords[0];
	    	    			moveCoords[1] = coords[1];
	    	    			moveCoords[2] = agentState[2];
	    					super.move();
	    					moved = true;
	    					System.out.println("Moving -y.");
	    				}else if(coords[1] > agentState[1]){ //Move in +y direction
	    					moveCoords[0] = coords[0];
	    	    			moveCoords[1] = coords[1];
	    					super.turnRight();
	    					super.turnRight();
	    					moveCoords[2] = agentState[2];
	    					super.move();
	    					moved = true;
	    					System.out.println("Moving +y.");
	    				}
    				}
    			}
    		}
    		
    		if(!moved){ //If still haven't moved, move in any safe direction
    			int random = (int) Math.random()*4 + 1;
    			while(!moved){
	    			if(moveList[random] == 1){   //////////need a check for safe direction, also move directions are still wrong i think
	    				if(random == 1){ //Move east
	    					super.turnRight();
		    				moveCoords[2] = agentState[2];
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving east.");
		    			}else if(random == 2){ //Move south
		    				super.turnRight();
		    				super.turnRight();
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving south.");
		    			}else if(random == 3){ //Move west
		    				super.turnLeft();
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving west.");
		    			}else if(random == 4){ //Move north
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving north.");
		    			}
	    			}else{
	    				random = (int) Math.random()*4 + 1;
	    			}
    			}
    		}
    	}
    }
}
