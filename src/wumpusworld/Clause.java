package wumpusworld;

/**
 * 
 * @author Greg Neznanski
 */

public class Clause {
	private int clauseNumber, wumpusCell;
	private MapCell facing;

	public Clause(int clauseNumber) {
		this.clauseNumber = clauseNumber;
		this.wumpusCell = -1;
	}

	public int[] checkClause(int[] currentState, Map worldMap) {
		if(this.clauseNumber == 0){
			//return clause0(currentState, worldMap);
		}else if(this.clauseNumber == 1){
			//return clause1(currentState, worldMap);
		}else if(this.clauseNumber == 2){
			//return clause2(currentState, worldMap);
		}else if(this.clauseNumber == 3){
			return clause3(currentState, worldMap);
		}else if(this.clauseNumber == 4){
			//return clause4(currentState, worldMap);
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
			//return clause12();
		}else if(this.clauseNumber == 13){
			//return clause13();
		}
		return null;
	}
	
//	// Determine the cell currently facing
//	public void facing(int[] currentState, Map worldMap){
//		if (currentState[0] > 0 && currentState[0] < worldMap.size()) { // Check if at edge of x grid
//			if (currentState[1] > 0 && currentState[1] < worldMap.size()) { // Check if at edge of y grid
//				if (currentState[2] == 1) { // Facing East
//					facing = worldMap.getCell(currentState[0] + 1,
//							currentState[1]);
//				} else if (currentState[2] == 2) { // Facing South
//					facing = worldMap.getCell(currentState[0],
//							currentState[1] + 1);
//				} else if (currentState[2] == 3) { // Facing West
//					facing = worldMap.getCell(currentState[0] - 1,
//							currentState[1]);
//				} else { // Facing North
//					facing = worldMap.getCell(currentState[0],
//							currentState[1] - 1);
//				}
//			} else facing = null; // At edge of x grid
//		} else facing = null; // At edge of y grid
//	}
//	
//	//Clause 1a
//	public int[] clause0(int[] currentState, Map worldMap){
//		int[] cellResult = new int[4];
//		facing(currentState, worldMap);
//		
//		if(facing != null){
//			if (facing.get('w') || facing.get('u')){
//				cellResult[0] = currentState[2];
//				cellResult[1] = 0;
//			}else{
//				cellResult[0] = currentState[2];
//				cellResult[1] = 1;
//			}
//		}else{
//			cellResult[0] = -1;
//			cellResult[1] = -1;
//		}
//		return cellResult;
//	}
//	
//	//Clause 1b
//	public int[] clause1(int[] currentState, Map worldMap){
//		int[] cellResult = new int[2];
//		facing(currentState, worldMap);
//		
//		if(facing != null){
//			if (facing.get('p') || facing.get('i')){
//				cellResult[0] = currentState[2];
//				cellResult[1] = 0;
//			}else{
//				cellResult[0] = currentState[2];
//				cellResult[1] = 1;
//			}
//		}else{
//			cellResult[0] = -1;
//			cellResult[1] = -1;
//		}
//		return cellResult;
//	}
//	
//	//Clause 1c
//	public int[] clause2(int[] currentState, Map worldMap){
//		int[] cellResult = new int[2];
//		facing(currentState, worldMap);
//		
//		if(facing != null){
//			if (facing.get('o')){
//				cellResult[0] = currentState[2];
//				cellResult[1] = 0;
//			}else{
//				cellResult[0] = currentState[2];
//				cellResult[1] = 1;
//			}
//		}else{
//			cellResult[0] = -1;
//			cellResult[1] = -1;
//		}
//		return cellResult;
//	}
	
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
	
	//Clause 2b
//	public int[] clause4(int[] currentState, Map worldMap){
//		int[] cellResult = new int[1];
//		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);
//		surrounding[3] = null;
//		
//		for(int i = 0; i < surrounding.length; i++){
//			if(surrounding[i] != null){
//				if(surrounding[i].get('c')){
//					cellResult[0] = 0;
//				}else{
//					cellResult[0] = 1;
//				}
//			}
//		}
//		
//		return cellResult; 
//	}
	
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
		surrounding[3] = null;
		
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
		surrounding[3] = null;
		
		if(!worldMap.getCell(currentState[0], currentState[1]).get('z')){
			for(int i = 0; i < surrounding.length; i++){
				if(surrounding[i] != null){
					surrounding[i].set('i', false); //Unflag cell, no pit there
				}
			}
			cellResult[0] = 2; //2 means need to update relation, 0 means do nothing
			cellResult[0] = 2; //0 for wumpus, 1 for pit
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
					wumpusCell = i;
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
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);
		surrounding[3] = null;
		
		if(wumpusCell != -1){
			if(surrounding[wumpusCell] != null){
				surrounding[wumpusCell].set('w', false);
				surrounding[wumpusCell].set('u', false);
				surrounding[wumpusCell].set('s', true);
				wumpusCell = -1;
			}
		}
		
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
						if(!surrounding[i].get('o')){ //No obstacle flag
							surrounding[i].set('s', true);
							cellResult[i] = 1;
						}else cellResult[i] = 0;
					}else cellResult[i] = 0;
				}else cellResult[i] = 0;
			}else cellResult[i] = 0;
		}
		return cellResult; //This does nothing except return something
	}
}
