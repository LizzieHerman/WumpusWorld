package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
public class main {
    public static void main(String[] args) {
        for(int i = 5; i <= 25; i += 5){
            WumpusWorld world = new WumpusWorld(i);
            int numWump = world.generateWorld(0.01, 0.01, 0.01);
            Explorer agent = new Explorer(world, i, numWump);
            world.setExplorer(agent);
        }
    }

}
