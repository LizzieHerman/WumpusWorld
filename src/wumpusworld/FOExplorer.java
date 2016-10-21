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
        if(x < worldMap.size() && y < worldMap.size() && x >= 0 && y >= 0){ // test to see if we hit wall or obstacle
            worldMap.setCell(x, y, 'o', true);
        }
        x = x1;
        y = y1;
        
        System.out.println("Walked into a wall in cell " + x1 + ", " + y1 + ". Returned to " + x + ", " + y + ".");
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
    	if(wumpus){
    		worldMap.getCell(super.deadCell[0], super.deadCell[1]).set('w', true);
    	}else{
    		worldMap.getCell(super.deadCell[0], super.deadCell[1]).set('p', true);
    	}
    	System.out.println("FO die");
    	super.die(x1, y1, wumpus);
    }
    
    //Update cells using clauses
    public void updateDB(){  ////////////////////////////////////Update loop numbers when all clauses added
    	int[] result;
    	
    	//update cells - clauses 2a-2f
    	result = rules[3].checkClause(agentState, worldMap); //2a
    	if(result[0] == 1){
    		super.grabGold();
    	}
    	
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
							}else{
								super.turnRight();
								super.turnRight();
							}
    						shootArrow();
    						boolean dead = super.wumpusKilled();
    						if(dead){
    							rules[10].setWumpusCell(super.deadWumpusCoords);
    							result = rules[10].checkClause(agentState, worldMap); //2h
    							relations.remove(i);
    						}
    						if(result[1] == 0){ //return to previous facing
								super.turnRight();
							}else if(result[1] == 2){ //return to previous facing
								super.turnLeft();
							}else{
								super.turnRight();
								super.turnRight();
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
    	int[] result;
    	
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
    		//If only move option is a flagged cell
        	result = rules[12].checkClause(agentState, worldMap); //3a
        	if(result[0] == 1){
        		super.turnLeft();
        		super.move();
        		moved = true;
        		System.out.println("No safe moves available.  Taking a risk moving left.");
        	}else if(result[1] == 1){
        		super.move();
        		moved = true;
        		System.out.println("No safe moves available.  Taking a risk moving forward.");
        	}else if(result[2] == 1){
        		super.turnRight();
        		super.move();
        		moved = true;
        		System.out.println("No safe moves available.  Taking a risk moving right.");
        	}else if(result[3] == 1){
        		super.turnRight();
        		super.turnRight();
        		super.move();
        		moved = true;
        		System.out.println("No safe moves available.  Taking a risk moving backward.");
        	}else{
        		System.out.println("No valid moves without dying or walking into a wall.");
        		super.gameWon = true;
        		return;
        	}
    	}
    	
//		moveCoords[0] = agentState[0];
//		moveCoords[1] = agentState[1];
//		moveCoords[2] = agentState[2];
    	
    	if(numMoves == 1 && moveList[3] != 0){ //Move backwards
    		super.turnRight();
    		super.turnRight();
    		super.move();
    		moved = true;
    		System.out.println("Moving backwards.");
    	}else{
    		numMoves++;
    	}
    	
    	if(numMoves > 1){ //Move to a frontier cell if there is one
    		int[] coordsLeft, coordsFront, coordsRight;
    		if(moveList[0] == 1 && surrounding[0] != null){ //Move left
    			coordsLeft = surrounding[0].getCoords();
    			if(worldMap.getCell(coordsLeft[0], coordsLeft[1]).get('f')){
	    			super.turnLeft();
	    			super.move();
	    			moved = true;
	    			System.out.println("Moving to cell " + coordsLeft[0] + ", " + coordsLeft[1] + ".");
    			}
    		}else if(moveList[1] == 1 && surrounding[1] != null){ //Move forward
    			coordsFront = surrounding[1].getCoords();
    			if(worldMap.getCell(coordsFront[0], coordsFront[1]).get('f')){
	    			super.move();
	    			moved = true;
	    			System.out.println("Moving to cell " + coordsFront[0] + ", " + coordsFront[1] + ".");
    			}
    		}else if(moveList[2] == 1 && surrounding[2] != null){ //Move right
    			coordsRight = surrounding[2].getCoords();
    			if(worldMap.getCell(coordsRight[0], coordsRight[1]).get('f')){
	    			super.turnRight();
	    			super.move();
	    			moved = true;
	    			System.out.println("Moving to cell " + coordsRight[0] + ", " + coordsRight[1] + ".");
    			}
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
    				
    				if(coords != null){
	    				if(coords[0] < agentState[0]){ //Move in -x direction
	    					makeMove('w', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in -x direction.");
	    				}else if(coords[0] > agentState[0]){ //Move in +x direction
	    					makeMove('e', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in +x direction.");
	    				}else if(coords[1] < agentState[1]){ //Move in -y direction
	    					makeMove('s', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in -y direction.");
	    				}else if(coords[1] > agentState[1]){ //Move in +y direction
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
	    					super.turnLeft();
	    					super.move();
		    				moved = true;
		    				System.out.println("Moving left.");
		    			}else if(random == 1){ //Move forward
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving forward.");
		    			}else if(random == 2){ //Move right
		    				super.turnRight();
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving right.");
		    			}else if(random == 3){ //Move backward
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
        if(x < worldMap.size() && y < worldMap.size() && x >= 0 && y >= 0){ // test to see if we hit wall or obstacle
            worldMap.setCell(x, y, 'o', true);
        }
        x = x1;
        y = y1;
        
        System.out.println("Walked into a wall in cell " + x1 + ", " + y1 + ". Returned to " + x + ", " + y + ".");
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
    	rules[13].setDead(super.deadCell[0], super.deadCell[1], wumpus);
        rules[13].checkClause(agentState, worldMap);
    	super.die(x1, y1, wumpus);
        System.out.println("Marked cell as dangerous.");
    }
    
    //Update cells using clauses
    public void updateDB(){  ////////////////////////////////////Update loop numbers when all clauses added
    	int[] result;
    	
    	//update cells - clauses 2a-2f
    	result = rules[3].checkClause(agentState, worldMap); //2a
    	if(result[0] == 1){
    		super.grabGold();
    	}
    	
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
    			Relation newRelation = new Relation(worldMap.getSurrounding(agentState[0], agentState[1], agentState[2]), hazard, worldMap.getCell(agentState[0], agentState[1]));
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
							}else{
								super.turnRight();
								super.turnRight();
							}
    						shootArrow();
    						boolean dead = super.wumpusKilled();
    						if(dead){
    							rules[10].setWumpusCell(super.deadWumpusCoords);
    							result = rules[10].checkClause(agentState, worldMap); //2h
    							relations.remove(i);
    						}
    						if(result[1] == 0){ //return to previous facing
								super.turnRight();
							}else if(result[1] == 2){ //return to previous facing
								super.turnLeft();
							}else{
								super.turnRight();
								super.turnRight();
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
    	int[] result;
    	
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
    		//If only move option is a flagged cell
        	result = rules[12].checkClause(agentState, worldMap); //3a
        	if(result[0] == 1){
                    System.out.println("No safe moves available.  Taking a risk moving left.");	
                    super.turnLeft();
        		super.move();
        		moved = true;
        	}else if(result[1] == 1){
        		System.out.println("No safe moves available.  Taking a risk moving forward.");
                        super.move();
        		moved = true;
        	}else if(result[2] == 1){
        		System.out.println("No safe moves available.  Taking a risk moving right.");
                    super.turnRight();
        		super.move();
        		moved = true;
        	}else if(result[3] == 1){
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
    	
//		moveCoords[0] = agentState[0];
//		moveCoords[1] = agentState[1];
//		moveCoords[2] = agentState[2];
    	
    	if(numMoves == 1 && moveList[3] != 0){ //Move backwards
    		super.turnRight();
    		super.turnRight();
    		super.move();
    		moved = true;
    		System.out.println("Moving backwards.");
    	}else{
    		numMoves++;
    	}
    	
    	if(numMoves > 1){ //Move to a frontier cell if there is one
    		int[] coordsLeft, coordsFront, coordsRight;
    		if(moveList[0] == 1 && surrounding[0] != null){ //Move left
    			coordsLeft = surrounding[0].getCoords();
    			if(worldMap.getCell(coordsLeft[0], coordsLeft[1]).get('f')){
	    			super.turnLeft();
	    			super.move();
	    			moved = true;
	    			System.out.println("Moving to cell " + coordsLeft[0] + ", " + coordsLeft[1] + ".");
    			}
    		}else if(moveList[1] == 1 && surrounding[1] != null){ //Move forward
    			coordsFront = surrounding[1].getCoords();
    			if(worldMap.getCell(coordsFront[0], coordsFront[1]).get('f')){
	    			super.move();
	    			moved = true;
	    			System.out.println("Moving to cell " + coordsFront[0] + ", " + coordsFront[1] + ".");
    			}
    		}else if(moveList[2] == 1 && surrounding[2] != null){ //Move right
    			coordsRight = surrounding[2].getCoords();
    			if(worldMap.getCell(coordsRight[0], coordsRight[1]).get('f')){
	    			super.turnRight();
	    			super.move();
	    			moved = true;
	    			System.out.println("Moving to cell " + coordsRight[0] + ", " + coordsRight[1] + ".");
    			}
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
    				
    				if(coords != null){
	    				if(coords[0] < agentState[0]){ //Move in -x direction
	    					makeMove('w', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in -x direction.");
	    				}else if(coords[0] > agentState[0]){ //Move in +x direction
	    					makeMove('e', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in +x direction.");
	    				}else if(coords[1] < agentState[1]){ //Move in -y direction
	    					makeMove('s', agentState[2]);
	    					moved = true;
	    					System.out.println("Moving in -y direction.");
	    				}else if(coords[1] > agentState[1]){ //Move in +y direction
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
	    					super.turnLeft();
	    					super.move();
		    				moved = true;
		    				System.out.println("Moving left.");
		    			}else if(random == 1){ //Move forward
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving forward.");
		    			}else if(random == 2){ //Move right
		    				super.turnRight();
		    				super.move();
		    				moved = true;
		    				System.out.println("Moving right.");
		    			}else if(random == 3){ //Move backward
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
