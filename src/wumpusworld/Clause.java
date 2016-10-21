package wumpusworld;

/**
 * 
 * @author Greg Neznanski
 */

public class Clause {
	private int clauseNumber, wumpusCellX, wumpusCellY;
	private MapCell facing;

	public Clause(int clauseNumber) {
		this.clauseNumber = clauseNumber;
		this.wumpusCellX = -1;
		this.wumpusCellY = -1;
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
	
	//Clause 2a
	public int[] clause3(int[] currentState, Map worldMap){
		int[] cellResult = new int[1];
		
		if (worldMap.getCell(currentState[0], currentState[1]).get('t')){
			cellResult[0] = 1;
		}else{
			cellResult[0] = 0;
		}
		return cellResult; //1 means grab gold, 0 means no gold in cell
	}
	
	//Clause 2c
	public int[] clause5(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);
		surrounding[3] = null;
		
		if(worldMap.getCell(currentState[0], currentState[1]).get('l')){
			for(int i = 0; i < surrounding.length; i++){
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
	
	//Clause 2d
	public int[] clause6(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);
		surrounding[3] = null;
		
		if(worldMap.getCell(currentState[0], currentState[1]).get('z')){
			for(int i = 0; i < surrounding.length; i++){
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
	
	//Clause 2e
	public int[] clause7(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);
		
		if(!worldMap.getCell(currentState[0], currentState[1]).get('l')){
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
	
	//Clause 2f
	public int[] clause8(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);
		
		if(!worldMap.getCell(currentState[0], currentState[1]).get('z')){
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
	
	
	//Clause 2g
	public int[] clause9(int[] currentState, Map worldMap){
		int[] cellResult = new int[2];
		boolean shootWumpus = false;
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);
		surrounding[3] = null;
		
		for(int i = 0; i < surrounding.length; i++){
			if(surrounding[i] != null){
				if(surrounding[i].get('w')){
					cellResult[0] = 1;
					cellResult[1] = i;
					shootWumpus = true;
				}
			}
		}
		if(!shootWumpus){
			cellResult[0] = 0;
		}
		return cellResult;
	}

	//Clause 2h
	public int[] clause10(int[] currentState, Map worldMap){
		int[] cellResult = new int[1];
		worldMap.getCell(wumpusCellX, wumpusCellY).set('w', false);
		worldMap.getCell(wumpusCellX, wumpusCellY).set('u', false);
		
		return cellResult;
	}
	
	//Clause 2i
	public int[] clause11(int[] currentState, Map worldMap){
		int[] cellResult = new int[4];
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);
		surrounding[3] = null;
		
		for(int i = 0; i < surrounding.length; i++){
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
	
	//Clause 3a
	public int[] clause12(int[] currentState, Map worldMap){
		int[] cellResult = new int[]{0,0,0,0};
		int leastFlags = 20;
		int cellNum = -1;
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);
		
		for(int i = 0; i < surrounding.length; i++){
			if(surrounding[i] != null){
				if(surrounding[i].get('o')){
					cellResult[i] = -1;
				}else{
					if(surrounding[i].get('u')){
						cellResult[i] += 1;
					}
					if(surrounding[i].get('i')){
						cellResult[i] += 1;
					}
				}
			}else{
				cellResult[i] = -1;
			}
		}
		
		for(int i = 0; i < cellResult.length; i++){
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
}
