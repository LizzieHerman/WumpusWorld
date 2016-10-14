package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
public class main {
    public static void main(String[] args) {
        for(int i = 5; i <= 25; i += 5){
            WumpusWorld world = new WumpusWorld(i);
            int numWump = world.generateWorld(0.20, 0.20, 0.20);
            Explorer agent = new FOExplorer(world, i, numWump);
            world.setExplorer(agent);
            // sense the cell you are currently in
            agent.getPercepts();
        }
    }

}
