package wumpusworld;

import java.util.ArrayList;

/**
 *
 * @author Lizzie Herman, Greg Neznanski
 *Some really small additions by Ryan Freivalds
 */
public class FOExplorer extends Explorer {
     Map worldMap;
     int numWump, facingBeforeMove;
     Clause[] rules;
     ArrayList<Relation> relations;
     ArrayList<Integer> moveHistory;
     int[] moveList; //holds 1 for safe, 0 for unsafe for the 4 surrounding cells
     boolean gameWon, noMoves;
     int[] bumpCoords;
    
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
                         if(w.world[i][j].get('g')){
                             worldMap.map[i][j].set('t', true); //set glitter in mapcell if cell has glitter
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
         this.facingBeforeMove = 1;
         this.moveHistory = new ArrayList<Integer>();
    }
    
    public void start(){
        updateDB();
        findMove();
    }
    
    // return whether able to shoot arrow
    public boolean shootArrow(){
        return super.shootArrow();
    }
    
    public void hearScream(){
    	super.hearScream();
    }
    
    public void feelBump(int x1, int y1){
    	worldMap.getCell(super.bumpCoords[0], super.bumpCoords[1]).set('a', true);
    	worldMap.getCell(super.bumpCoords[0], super.bumpCoords[1]).set('o', true);
    	worldMap.getCell(super.bumpCoords[0], super.bumpCoords[1]).set('f', false);
    	worldMap.getCell(super.bumpCoords[0], super.bumpCoords[1]).set('s', false);
    	worldMap.getCell(super.bumpCoords[0], super.bumpCoords[1]).set('a', false);
        x = x1;
        y = y1;
        agentState[2] = facingBeforeMove;
        super.direction = facingBeforeMove;

        System.out.println("Walked into a wall in cell " + super.bumpCoords[0] + ", " + super.bumpCoords[1] + ". Returned to " + x + ", " + y + ".");
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
    	boolean foundRelation = false;
    	rules[13].setDead(super.deadCell[0], super.deadCell[1], wumpus); //Record the location
        rules[13].checkClause(agentState, worldMap);  //Mark the cell as wumpus or pit
        for(int i = 0; i < relations.size(); i++){
        	if(relations.get(i).relatedTo().getCoords()[0] == agentState[0]){
        		if(relations.get(i).relatedTo().getCoords()[1] == agentState[1]){
        			//if(relations.get(i).getRelated().size() == 1){ //
        				if(relations.get(i).getRelated().get(0).getCoords()[0] == super.deadCell[0]){
        					if(relations.get(i).getRelated().get(0).getCoords()[1] == super.deadCell[1]){
        						if(relations.get(i).getHazard() == 'u'){ //If relation is found, update the relation with the new information
        	        				if(wumpus){
        	        					relations.get(i).updateKnown();
        	                			foundRelation = true;
        	        				}
        	        			}else{
        	        				if(!wumpus){
        	        					relations.get(i).updateKnown();
        	                			foundRelation = true;
        	        				}
        	        			}
        					}
        				}
        			//}
        		}
        	}
        }
        if(!foundRelation){ //If the threat was not in the relation list, usually means more than one threat around sense
        	Relation newRelation;
        	MapCell[] related = new MapCell[]{worldMap.getCell(super.deadCell[0], super.deadCell[1])};;
        	if(wumpus){
        		newRelation = new Relation(related, 'w', worldMap.getCell(agentState[0],agentState[1]));  //Create new relation for the surprise threat, confirm the threat, relation size is 1
        	}else{
        		newRelation = new Relation(related, 'p', worldMap.getCell(agentState[0],agentState[1]));
        	}
        	if(newRelation != null){
        		relations.add(newRelation);
        	}
        }
        super.die(x1, y1, wumpus);
        agentState[0] = super.x; //Return to previous location
    	agentState[1] = super.y;
    	agentState[2] = facingBeforeMove;
    	super.direction = facingBeforeMove;
        System.out.println(" Marked cell as dangerous.");
    }
    
    //Update cells using clauses
    public void updateDB(){  ////////////////////////////////////Update loop numbers when all clauses added
    	int[] result;
    	
    	//Clause 3c - if not dead, mark current cell as safe
    	result = rules[2].checkClause(agentState, worldMap);
    	if(result[0] == 1){
    		for(int i = 0; i < relations.size(); i++){
    			for(int j = 0; j < relations.get(i).getRelated().size(); j++){
    				if(relations.get(i).getRelated().get(j).getCoords()[0] == agentState[0]){
    					if(relations.get(i).getRelated().get(j).getCoords()[1] == agentState[1]){
    						boolean valid = relations.get(i).checkFlags();
    						if(!valid){
    							relations.get(i).updateFlags();
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	//Clause 3e - If cell is marked safe, set unflaggable
    	result = rules[1].checkClause(agentState, worldMap);
    	if(result[0] == 1){
    		worldMap.getCell(agentState[0], agentState[1]).set('a', false);
    	}
    	
    	//update cells - clauses 2a-2f
    	result = rules[3].checkClause(agentState, worldMap); //2a
    	if(result[0] == 1){
    		super.grabGold();
    	}
    	
    	for(int i = 5; i <= 8; i++){ //2b-2f
    		boolean valid = false;
    		result = rules[i].checkClause(agentState, worldMap);
    		if(result[0] == 1){
    			char hazard;
    			if(result[1] == 0){
    				hazard = 'u';
    			}else{
    				hazard = 'i';
    			}
    			if(!worldMap.getCell(agentState[0], agentState[1]).get('n')){
    				//Add new relation
    				Relation newRelation = new Relation(worldMap.getSurrounding(agentState[0], agentState[1], agentState[2]), hazard, worldMap.getCell(agentState[0], agentState[1]));
    				relations.add(newRelation);
    			}
    		}
    	}
    	
    	//Check for confirmed wumpus in a surrounding cell, if true shoot arrow, listen for scream, if scream remove relation from list
    	result = rules[9].checkClause(agentState, worldMap); //2g
    	if(result[0] == 1){
    		for(int i = 0; i < relations.size(); i++){
    			if(relations.get(i).getRelated().size() == 1){
    				if(relations.get(i).getHazard() == 'u'){
    					if(relations.get(i).getCell().get('w')){
							if(result[1] == 0){ //wumpus is in left cell
								super.turnLeft();
							}else if(result[1] == 2){ //wumpus is in right cell
								super.turnRight();
							}else{
								super.turnRight();
								super.turnRight();
							}
    						shootArrow();
    						boolean dead = super.wumpusKilled();
    						if(dead){
    							rules[10].setWumpusCell(super.deadWumpusCoords);
    							rules[10].checkClause(agentState, worldMap); //2h
    							for(int j = 0; j < relations.size(); j++){
    								for(int k = 0; k < relations.get(j).getRelated().size(); k++){
    									if(relations.get(j).getRelated().get(k).getCoords() == super.deadWumpusCoords){
    										relations.get(j).clearRelation('u');
    									}
    								}
    							}
    							relations.remove(i);
    						}
    						agentState[2] = facingBeforeMove;
    					}
    				}
    			}
    		}
    	}
    	
    	//update relations
    	boolean valid = true;
    	if(!relations.isEmpty()){
	    	for(int i = 0; i < relations.size(); i++){
	    		valid = relations.get(i).checkFlags(); //If a flag in a relation has been changed
	    		if(!valid){
	    			relations.get(i).updateFlags(); //remove that cell from relation
	    		}
	    		valid = relations.get(i).checkKnown(); //If a wumpus or pit is known to be in the relation
	    		if(valid){
	    			relations.get(i).updateKnown(); //Remove other cells from relation, leaving only the wumpus or pit cell in the relation
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
    	int[] result;
    	facingBeforeMove = agentState[2];
    	int[] avoidRepeat = new int[]{0,0,0,0};
    	
    	System.out.println("current: " + agentState[0] + ", " + agentState[1] + " - facing: " + agentState[2]);
    	
    	//Implement solution to repeated movements
    	//check last 5 moves for repeat pattern
    	//if pattern, dont let move to repeat location, take risk or force to frontier
    	//This is not working correctly, it finds a pattern and forces a different move, but it ends up in the pattern again
    	if(moveHistory.size() == 5){
	    	if(moveHistory.get(0) == moveHistory.get(2) && moveHistory.get(1) == moveHistory.get(3)){
	    		if(moveHistory.get(4) == moveHistory.get(1)){
	    			if(moveHistory.get(4) == 1 || moveHistory.get(4) == 5 || moveHistory.get(4) == 13){
	    				avoidRepeat[0] = 1;
	    			}else if(moveHistory.get(4) == 2 || moveHistory.get(4) == 6 || moveHistory.get(4) == 14){
	    				avoidRepeat[1] = 1;
	    			}else if(moveHistory.get(4) == 3 || moveHistory.get(4) == 7 || moveHistory.get(4) == 15){
	    				avoidRepeat[2] = 1;
	    			}else {
	    				avoidRepeat[3] = 1;
	    			}
	    		}
	    	}
	    	moveHistory.remove(0); //Keep track of last 5 moves to prevent forever repeating same move
    	}
    	
    	MapCell[] surrounding = worldMap.getSurrounding(agentState[0], agentState[1], agentState[2]);
    	for(int i = 0; i < surrounding.length; i++){
    		if(surrounding[i] != null){
    			if(surrounding[i].get('s')){
    				if(i == 0 && avoidRepeat[0] == 1){
    					moveList[i] = 0;
    				}else if(i == 1 && avoidRepeat[1] == 1){
    					moveList[i] = 0;
    				}else if(i == 2 && avoidRepeat[2] == 1){
    					moveList[i] = 0;
    				}else if(i == 3 && avoidRepeat[3] == 1){
    					moveList[i] = 0;
    				}else{
    					moveList[i] = 1;
    				}
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
    		//If only move option is a flagged cell
        	result = rules[12].checkClause(agentState, worldMap); //3a
        	if(result[0] == 1){
                moveHistory.add(1);
        		System.out.println("No safe moves available.  Taking a risk moving left.");	
                super.turnLeft();
        		super.move();
        		moved = true;
        	}else if(result[1] == 1){
        		moveHistory.add(2);
        		System.out.println("No safe moves available.  Taking a risk moving forward.");
                super.move();
        		moved = true;
        	}else if(result[2] == 1){
        		moveHistory.add(3);
        		System.out.println("No safe moves available.  Taking a risk moving right.");
                super.turnRight();
        		super.move();
        		moved = true;
        	}else if(result[3] == 1){
        		moveHistory.add(4);
        		System.out.println("No safe moves available.  Taking a risk moving backward.");
                super.turnRight();
        		super.turnRight();
        		super.move();
        		moved = true;
        	}else{
        		System.out.println("No valid moves without dying or walking into a wall. Game over.");
        		super.gameWon = true;
        		return;
        	}
    	}
    	
    	if(numMoves >= 1){ //Move to a frontier cell if there is one
    		int[] coordsLeft, coordsFront, coordsRight, coordsBack;
    		if(moveList[0] == 1 && surrounding[0] != null){ //Move left
    			coordsLeft = surrounding[0].getCoords();
    			if(worldMap.getCell(coordsLeft[0], coordsLeft[1]).get('f')){
    				moveHistory.add(5);
    				System.out.println("1Moving to cell " + coordsLeft[0] + ", " + coordsLeft[1] + ".");
    				super.turnLeft();
	    			super.move();
	    			moved = true;
    			}
    		}else if(moveList[1] == 1 && surrounding[1] != null){ //Move forward
    			coordsFront = surrounding[1].getCoords();
    			if(worldMap.getCell(coordsFront[0], coordsFront[1]).get('f')){
    				moveHistory.add(6);
    				System.out.println("2Moving to cell " + coordsFront[0] + ", " + coordsFront[1] + ".");
    				super.move();
	    			moved = true;
    			}
    		}else if(moveList[2] == 1 && surrounding[2] != null){ //Move right
    			coordsRight = surrounding[2].getCoords();
    			if(worldMap.getCell(coordsRight[0], coordsRight[1]).get('f')){
    				moveHistory.add(7);
    				System.out.println("3Moving to cell " + coordsRight[0] + ", " + coordsRight[1] + ".");
    				super.turnRight();
	    			super.move();
	    			moved = true;
    			}
    		}else if(moveList[3] == 1 && surrounding[3] != null){
    			coordsBack = surrounding[3].getCoords();
    			if(worldMap.getCell(coordsBack[0], coordsBack[1]).get('f')){
    				moveHistory.add(8);
    				System.out.println("4Moving to cell " + coordsBack[0] + ", " + coordsBack[1] + ".");
    				super.turnRight();
    				super.turnRight();
    				super.move();
    				moved = true;
    			}
    		}
    		
    		if(!moved){ //Move towards a relation cell, or a confirmed wumpus
    			if(!relations.isEmpty()){
    				MapCell direction;
    				int[] coords = null;
    				for(int i = 0; i < relations.size(); i++){
    					if(relations.get(i).getRelated().size() != 1){ //Pick an incomplete relation from the list
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
    				
    				if(coords != null){ //Move in the direction of the relation
	    				if(coords[0] < agentState[0]){ //Move in -x direction
	    					//moveHistory.add(9);
	    					makeMove('w', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in -x direction.");
	    				}else if(coords[0] > agentState[0]){ //Move in +x direction
	    					//moveHistory.add(10);
	    					makeMove('e', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in +x direction.");
	    				}else if(coords[1] < agentState[1]){ //Move in -y direction
	    					//moveHistory.add(11);
	    					makeMove('s', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in -y direction.");
	    				}else if(coords[1] > agentState[1]){ //Move in +y direction
	    					//moveHistory.add(12);
	    					makeMove('n', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in +y direction.");
	    				}
    				}
    			}
    		}
    		
    		if(!moved){ //If still haven't moved, move in any safe direction
    			int random = (int) Math.random()*4;
    			while(!moved){
	    			if(moveList[random] == 1){
	    				if(random == 0){ //Move left
	    					moveHistory.add(13);
	    					super.turnLeft();
	    					super.move();
		    				moved = true;
		    				System.out.println("Moving left.");
		    			}else if(random == 1){ //Move forward
		    				moveHistory.add(14);
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving forward.");
		    			}else if(random == 2){ //Move right
		    				moveHistory.add(15);
		    				super.turnRight();
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving right.");
		    			}else if(random == 3){ //Move backward
		    				moveHistory.add(16);
		    				super.turnRight();
		    				super.turnRight();
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving backward.");
		    			}
	    			}else{
	    				random = (int) Math.random()*4 + 1;
	    			}
    			}
    		}
    	}
    }
    
    //Determine how which direction to face and move, depending on facing
    public void makeMove(char direction, int facing){
    	if(facing == 1){
	    	if(direction == 'w'){
	    		super.turnRight();
	    		super.turnRight();
	    		super.move();
	    	}else if(direction == 's'){
	    		super.turnRight();
	    		super.move();
	    	}else if(direction == 'e'){
	    		super.move();
	    	}else if(direction == 'n'){
	    		super.turnLeft();
	    		super.move();
	    	}
    	}else if(facing == 2){
    		if(direction == 'w'){
	    		super.turnRight();
	    		super.move();
	    	}else if(direction == 's'){
	    		super.move();
	    	}else if(direction == 'e'){
	    		super.turnLeft();
	    	}else if(direction == 'n'){
	    		super.turnRight();
	    		super.turnRight();
	    		super.move();
	    	}
    	}
    	else if(facing == 3){
    		if(direction == 'w'){
	    		super.move();
	    	}else if(direction == 's'){
	    		super.turnLeft();
	    		super.move();
	    	}else if(direction == 'e'){
	    		super.turnRight();
	    		super.turnRight();
	    		super.move();
	    	}else if(direction == 'n'){
	    		super.turnRight();
	    		super.move();
	    	}
    	}else{
    		if(direction == 'w'){
	    		super.turnLeft();
	    		super.move();
	    	}else if(direction == 's'){
	    		super.turnRight();
	    		super.turnRight();
	    		super.move();
	    	}else if(direction == 'e'){
	    		super.turnRight();
	    		super.move();
	    	}else if(direction == 'n'){
	    		super.move();
	    	}
    	}
    }
}
