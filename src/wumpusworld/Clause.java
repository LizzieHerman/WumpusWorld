package wumpusworld;

/**
 * 
 * @author Greg Neznanski
 */

public class Clause {
	private int clauseNumber, wumpusCellX, wumpusCellY;
	private boolean hazard;
        private int x, y;
	private MapCell facing;

	public Clause(int clauseNumber) {
		this.clauseNumber = clauseNumber;
		this.wumpusCellX = -1;
		this.wumpusCellY = -1;
		this.hazard = false;
                this.x = 0;
                this.y = 0;
	}

	public int[] checkClause(int[] currentState, Map worldMap) {
		if(this.clauseNumber == 3){
			return clause3(currentState, worldMap);
		}else if(this.clauseNumber == 5){
			return clause5(currentState, worldMap);
		}else if(this.clauseNumber == 6){
			return clause6(currentState, worldMap);
		}else if(this.clauseNumber == 7){
			return clause7(currentState, worldMap);
		}else if(this.clauseNumber == 8){
			return clause8(currentState, worldMap);
		}else if(this.clauseNumber == 9){
			return clause9(currentState, worldMap);
		}else if(this.clauseNumber == 10){
			return clause10(currentState, worldMap);
		}else if(this.clauseNumber == 11){
			return clause11(currentState, worldMap);
		}else if(this.clauseNumber == 12){
			return clause12(currentState, worldMap);
		}else if(this.clauseNumber == 13){
			//return clause13();
		}
		return null;
	}
	
	public void setWumpusCell(int[] deadWumpusCoords){
		this.wumpusCellX = deadWumpusCoords[0];
		this.wumpusCellY = deadWumpusCoords[1];
	}
	
	//Clause 2a. LOOKING FOR GLITTER glitter(p) => grab()
	public int[] clause3(int[] currentState, Map worldMap){
		int[] cellResult = new int[1];
		
		if (worldMap.getCell(currentState[0], currentState[1]).get('t')){ //if at current X and Y we see a glitter
			cellResult[0] = 1; //WE SEE A GLITTER
		}else{
			cellResult[0] = 0; //WE SEE NO GLITTER.
		}
		return cellResult; //1 means grab gold, 0 means no gold in cell
	}
	
	//Clause 2c. LOOKING FOR SMELLS smell(p) => wumpusFlag(surrounding cells)
	public int[] clause5(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]); //Current state being our X value, Y value, and facing.
		surrounding[3] = null; //THE CELL WE MOVED FROM MUST BE SAFE.
		
		if(worldMap.getCell(currentState[0], currentState[1]).get('l')){ //if we smell something...
			for(int i = 0; i < surrounding.length; i++){ //all cells around us 
				if(surrounding[i] != null){
					surrounding[i].set('u', true); //Flag cell for possible wumpus 
					surrounding[i].set('s', false); //Set unsafe
				}
			}
			cellResult[0] = 1; //1 means need to create relation, 0 means do nothing
			cellResult[1] = 0; //0 for wumpus, 1 for pit
		}else{
			cellResult[0] = 0;
		}
		return cellResult;
	}
	 
	//Clause 2d WE FEEL BREEZES breeze(p) => pitFlag(surrounding cells)
	public int[] clause6(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]); //Current state being our X value, Y value, and facing.
		surrounding[3] = null; //The cell we moved from is safe.
		
		if(worldMap.getCell(currentState[0], currentState[1]).get('z')){
			for(int i = 0; i < surrounding.length-1; i++){ //marking all cells around us (except behind us) wuith a flag.
				if(surrounding[i] != null){
					surrounding[i].set('i', true); //Flag cell for possible pit
					surrounding[i].set('s', false); //Set unsafe
				}
			}
			cellResult[0] = 1; //1 means need to create relation, 0 means do nothing
			cellResult[1] = 1; //0 for wumpus, 1 for pit
		}else{
			cellResult[0] = 0;
		}
		return cellResult;
	}
	
	//Clause 2e NO SMELL MEANS NO WUMPUIS !smell(p) => !Wumpusflag(surrounding cells)
	public int[] clause7(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]); //Current state being our X value, Y value, and facing.
		
		if(!worldMap.getCell(currentState[0], currentState[1]).get('l')){ //unflagging surrounding cells
			for(int i = 0; i < surrounding.length; i++){
				if(surrounding[i] != null){
					surrounding[i].set('u', false); //Unflag cell, no wumpus there
				}
			}
			cellResult[0] = 2; //2 means need to update relation, 0 means do nothing
			cellResult[0] = 0; //0 for wumpus, 1 for pit
		}else{
			cellResult[0] = 0;
		}
		return cellResult;
	}
	
	//Clause 2f NO BREEZES MEANS NO PITS !breeze(p) => !Pitflag(surrounding cells)
	public int[] clause8(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]); //Current state being our X value, Y value, and facing.
		
		if(!worldMap.getCell(currentState[0], currentState[1]).get('z')){ //unflagging surrounding cells
			for(int i = 0; i < surrounding.length; i++){
				if(surrounding[i] != null){
					surrounding[i].set('i', false); //Unflag cell, no pit there
				}
			}
			cellResult[0] = 2; //2 means need to update relation, 0 means do nothing
			cellResult[0] = 1; //0 for wumpus, 1 for pit
		}else{
			cellResult[0] = 0;
		}
		return cellResult;
	}
	
	
	//Clause 2g IF WE KNOW WHERE A WUMPUS IS, SHOOT IT wumpus(surrounding) => facewumpus && shootarrow
	public int[] clause9(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		boolean shootWumpus = false;
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]); //Current state being our X value, Y value, and facing.
		surrounding[3] = null; //the wumpus isn't in the cell we moved from
		
		for(int i = 0; i < surrounding.length; i++){ 
			if(surrounding[i] != null){
				if(surrounding[i].get('w')){ //if we KNOW a wumpus is next to us
					cellResult[0] = 1;
					cellResult[1] = i;
					shootWumpus = true; //take action to kill it
				}
			}
		}
		if(!shootWumpus){ 
			cellResult[0] = 0; //otherwise don't waste an arrow
		}
		return cellResult;
	}

	//Clause 2h REDISCOVERING CELLS NOW WITHOUT STENCH wumpus(surrounding) => !wumpus(surrounding) && removeRelation
	public int[] clause10(int[] currentState, Map worldMap){
		int[] cellResult = new int[1];
		worldMap.getCell(wumpusCellX, wumpusCellY).set('w', false); //means there's no wumpus next to us.
		worldMap.getCell(wumpusCellX, wumpusCellY).set('u', false);
		
		return cellResult;
	}
	
	//Clause 2i checkSafe(surrounding) => setSafe(surrounding)
	public int[] clause11(int[] currentState, Map worldMap){
		int[] cellResult = new int[4];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]); //Current state being our X value, Y value, and facing.
		surrounding[3] = null; //The cell we moved from is always safe.
		
		for(int i = 0; i < surrounding.length; i++){ //unflagging all surrounding cells
			if(surrounding[i] != null){
				if(!surrounding[i].get('u')){ //No wumpusflag
					if(!surrounding[i].get('i')){ //No pitflag
						if(!surrounding[i].get('o')){ //No obstacle
							surrounding[i].set('s', true);
							cellResult[i] = 1;
						}
					}
				}
			}else cellResult[i] = 0;
		}
		return cellResult; //This does nothing except return something
	}
	
	//Clause 3a IF NO MOVE IS SAFE, TRY MOVING INTO A FLAGGED CELL noMoves(surrounding) => takeRisk
	public int[] clause12(int[] currentState, Map worldMap){
		int[] cellResult = new int[]{0,0,0,0};
		int leastFlags = 20;
		int cellNum = -1;
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]); //Current state being our X value, Y value, and facing.
		
		for(int i = 0; i < surrounding.length; i++){ //for the surrounding cells
			if(surrounding[i] != null){
				if(surrounding[i].get('o')){ //we still  can't move into a wall
					cellResult[i] = -1;
				}else{
					if(surrounding[i].get('u')){ //add a cell with a pit flag as a possible move
						cellResult[i] += 1;
					}
					if(surrounding[i].get('i')){ //add a cell with a wumpus flag as a possible move
						cellResult[i] += 1;
					}
				}
			}else{
				cellResult[i] = -1;
			}
		}
		
		for(int i = 0; i < cellResult.length; i++){ //try and move into the "least dangerous" cell.
			if(cellResult[i] != -1){
				if(cellResult[i] < leastFlags){
					leastFlags = cellResult[i];
					cellNum = i;
				}
			}
		}
		
		for(int i = 0; i < cellResult.length; i++){
			cellResult[i] = 0;
		}
		cellResult[cellNum] = 1;
		return cellResult;
	}
	
	//Clause 3b, if you die in a cell, mark the cell as wumpus or pit
        public int[] clause13(int[] currentState, Map worldMap){
            int[] cellResult = new int[]{1};
            if(this.hazard){
    		worldMap.getCell(this.x, this.y).set('w', true);
            }else{
    		worldMap.getCell(this.x, this.y).set('p', true);
            }
            return cellResult;
        }
        
        public void setDead(int x, int y, boolean hazard){
            this.x = x;
            this.y = y;
            this.hazard = hazard;
        }
}
