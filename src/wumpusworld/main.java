package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
public class main {
    public static void main(String[] args) {
        for(int i = 5; i <= 5; i += 5){
            WumpusWorld world = new WumpusWorld(i);
            int numWump = world.generateWorld(0.20, 0.20, 0.20);
            Explorer agent = new RExplorer(world, i, numWump);
            world.setExplorer(agent);
            // start the agent
            agent.start();
        }
    }

}
