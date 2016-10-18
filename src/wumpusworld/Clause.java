package wumpusworld;

/**
 * 
 * @author Greg Neznanski
 */

public class Clause {
	private int clauseNumber;
	MapCell facing;

	public Clause(int clauseNumber) {
		this.clauseNumber = clauseNumber;
	}

	public int[] condition(int[] currentState, Map worldMap) {
		boolean doAction = false;
		MapCell[] surrounding = worldMap.getSurrounding(currentState[0], currentState[1], currentState[2]);;

		// Determine the cell currently facing
		if (currentState[0] > 0 && currentState[0] < worldMap.size()) { // Check if at edge of x grid
			if (currentState[1] > 0 && currentState[1] < worldMap.size()) { // Check if at edge of y grid
				if (currentState[2] == 1) { // Facing East
					facing = worldMap.getCell(currentState[0] + 1,
							currentState[1]);
				} else if (currentState[2] == 2) { // Facing South
					facing = worldMap.getCell(currentState[0],
							currentState[1] + 1);
				} else if (currentState[2] == 3) { // Facing West
					facing = worldMap.getCell(currentState[0] - 1,
							currentState[1]);
				} else { // Facing North
					facing = worldMap.getCell(currentState[0],
							currentState[1] - 1);
				}
			} else facing = null; // At edge of x grid
		} else facing = null; // At edge of y grid

		if (facing != null) { // Clauses using facing cell
			if (this.clauseNumber == 0) { // Clause 1a
				if (facing.get('w') || facing.get('u'))
					doAction = true;
			} else if (this.clauseNumber == 1) { // Clause 1b
				if (facing.get('p') || facing.get('i'))
					doAction = true;
			} else if (this.clauseNumber == 2) { // Clause 1c
				if (facing.get('o'))
					doAction = true;
			} else if (this.clauseNumber == 15) { // Clause 3b
				if (facing.get('w'))
					doAction = true;
			} else if (this.clauseNumber == 16) { // Clause 3c
				// if scream and no smell in current
			}
		}
		// Clauses using current cell
		if (this.clauseNumber == 3) { // Clause 2a
			if (worldMap.getCell(currentState[0], currentState[1]).get('o'))
				doAction = true;
		} else if (this.clauseNumber == 4) { // Clause 2b
			if (worldMap.getCell(currentState[0], currentState[1]).get('m'))
				doAction = true; // m = smell
		} else if (this.clauseNumber == 5) { // Clause 2c
			if (worldMap.getCell(currentState[0], currentState[1]).get('r'))
				doAction = true; // r = breeze, need to set these in mapcell
		}

		// Clause head is true
		if (doAction) {
			return action(currentState); // return something?
		} else {
			return null; //Clause head is false
		}
	}

	//Clause body
	public int[] action(int[] currentState) {
		int[] cellResult = new int[4];
		if (this.clauseNumber == 0 || this.clauseNumber == 1 || this.clauseNumber == 2) { // Clause 1a, 1b, 1c
			cellResult[0] = currentState[2];
			cellResult[1] = 0;
			return cellResult;
		} else if (this.clauseNumber == 3) { // Clause 2a
			// do something about obstacle
			return cellResult;
		} else if (this.clauseNumber == 4) { // Clause 2b
			// flag wumpus for surrounding cells
			return cellResult;
		} else if (this.clauseNumber == 5) {
			// flag pit for surrounding cells
			return cellResult;
		} else if (this.clauseNumber == 6) { // placeholder
			return cellResult;
		} else if (this.clauseNumber == 15) {
			// shoot arrow
			return cellResult;
		} else if (this.clauseNumber == 16) {
			// wumpus killed, remove from board
			return cellResult;
		}
		return cellResult;
	}
}
