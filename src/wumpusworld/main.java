package wumpusworld;

/**
 *
 * @author Lizzie Herman, Greg Neznanski
 */
public class main {
    public static void main(String[] args) {
    	
		for (int i = 5; i <= 25; i += 5) {
			int wins = 0;
			int totalcost=0;
			int wincost=0;
			for (int j = 0; j < 100; j++) {
				int count = 0;
				WumpusWorld world = new WumpusWorld(i);
				int numWump = world.generateWorld(0.075, 0.1, 0.1);
				Explorer agent = new FOExplorer(world, i, numWump);
				world.setExplorer(agent);

				System.out.println("Starting game size: " + i);
				while (!agent.checkWin() && count < 500) {
					agent.start();
					count++;
					// world.board.updateUI();
					// System.out.println("looping");
				}
				if (agent.checkWin()) {
					wins++;
					wincost+=agent.cost;
				}
				System.out.println("Cost: " + agent.cost);
				totalcost+=agent.cost;
			}
			System.out.println("wins: "+wins);
			System.out.println("avg cost: "+totalcost/100);
			System.out.println("avg win cost: "+wincost/wins);
		}
	}

}
