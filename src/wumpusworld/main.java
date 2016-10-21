package wumpusworld;

/**
 *
 * @author Lizzie Herman, Greg Neznanski
 */
public class main {
    public static void main(String[] args) {
        for(int i = 5; i <= 5; i += 5){
            WumpusWorld world = new WumpusWorld(i);
            int numWump = world.generateWorld(0.2, 0.2, 0.2);
            Explorer agent = new FOExplorer(world, i, numWump);
            world.setExplorer(agent);
            
            System.out.println("Starting game size: " + i);
            while(!agent.checkWin()){
            	agent.start();
            	//world.board.updateUI();
            	//System.out.println("looping");
            }
        }
    }

}
