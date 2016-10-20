/*
 * This will be part of the gui once it is complete
 */

package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class GameBoard extends JFrame {
    GridLayout boardLayout;
    JPanel board;
    int[] stat = {0, 0, 0, 0, 0, 0, 0, 0};
    Component[][] grid;
        
    Container explorer1, explorer2, explorer3, explorer4, arrow1, arrow2, arrow3, arrow4, 
            wumpus, deadwumpus, obstacle, pit, gold, inGold, inWumpus, inPit, empty;
    
    public GameBoard(int wump, String[][] world, Cell[][] actualWorld){
        // the board will include an indexing of the board spaces
        boardLayout = new GridLayout(world.length+1, world.length+1);
        stat[1] = wump;
        grid = new Container[world.length+1][world.length+1];
        makeGrid(world, actualWorld);
        initPanels();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Game Board");
    }
    
    private void makeGrid(String[][] world, Cell[][] actualWorld){
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                int x = i-1; int y = j-1;
                if(i == 0 && j == 0){
                    grid[i][j] = Box.createRigidArea(new Dimension(50,50));
                }else if(i == 0 ){
                    grid[i][j] = new JLabel(Integer.toString(y));
                }else if(j == 0){
                    grid[i][j] = new JLabel(Integer.toString(x));
                }else{
                    String inCell = world[x][y];
                    if(inCell.isEmpty() || inCell.equalsIgnoreCase("empty")){
                        //grid[i][j] = new JLabel(new ImageIcon(getClass().getResource("/wumpusworld/empty.png")));
                    }else if(inCell.equalsIgnoreCase("explorer")){
                        //grid[i][j] = new JLabel(new ImageIcon(getClass().getResource("/wumpusworld/explorer1.png")));
                    }else if(inCell.equalsIgnoreCase("wumpus")){
                        //grid[i][j] = new JLabel(new ImageIcon(getClass().getResource("/wumpusworld/wumpus.png")));
                    }else if(inCell.equalsIgnoreCase("pit")){
                        //grid[i][j] = new JLabel(new ImageIcon(getClass().getResource("/wumpusworld/pit.png")));
                    }else if(inCell.equalsIgnoreCase("gold")){
                        //grid[i][j] = new JLabel(new ImageIcon(getClass().getResource("/wumpusworld/gold.png")));
                    }else if(inCell.equalsIgnoreCase("wall")){
                        //grid[i][j] = new JLabel(new ImageIcon(getClass().getResource("/wumpusworld/wall.png")));
                    }
                    
                    if(i <= actualWorld.length && j <= actualWorld.length){
                    	grid[i][j] = new JLabel(actualWorld[i-1][j-1].getDisplay());
                    }
                }
            }
        }  
    }
    
    private void initPanels(){
        //make board
        board = new JPanel();
        board.setLayout(boardLayout);
        for(int j = grid.length-1; j >= 0; j--){
            for(int i = 0; i < grid.length; i++){
                board.add(grid[i][j]);
            }
        }
        
        // make stats
        JPanel stats = new JPanel();
        stats.setLayout(new BoxLayout(stats, BoxLayout.PAGE_AXIS));
        // make first line of info
        JPanel line1 = new JPanel();
        line1.setLayout(new BoxLayout(line1, BoxLayout.LINE_AXIS));
        cost.setText("Cost: " + stat[0]);
        arrow.setText("Arrows Left: " + stat[1]);
        move.setText("Moves: " + stat[2]);
        death.setText("Death Count: " + stat[3]);
        line1.add(cost);
        line1.add(arrow);
        line1.add(move);
        line1.add(death);
        // make second line of info
        JPanel line2 = new JPanel();
        line2.setLayout(new BoxLayout(line2, BoxLayout.LINE_AXIS));
        stench.setText("Smell Stench: " + stat[4]);
        breeze.setText("Feel Breeze: " + stat[5]);
        glimmer.setText("See Glimmer: " + stat[6]);
        bump.setText("Bump Count: " + stat[7]);
        line2.add(stench);
        line2.add(breeze);
        line2.add(glimmer);
        line2.add(bump);
        // combine line one, two and the button
        next.setAlignmentX(CENTER_ALIGNMENT);
        stats.add(line1);
        stats.add(Box.createRigidArea(new Dimension(0,10)));
        stats.add(line2);
        stats.add(Box.createRigidArea(new Dimension(0,10)));
        stats.add(next);
        stats.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        // combine board and stats
        this.add(board, BorderLayout.NORTH);
        this.add(new JSeparator(), BorderLayout.CENTER);
        this.add(stats, BorderLayout.SOUTH);
    }
    
    public void updateUI(){
    	board.revalidate();
    	board.repaint();
    }
    
    private JButton next = new JButton("Next Move");
    private JLabel cost = new JLabel();
    private JLabel arrow = new JLabel();
    private JLabel move = new JLabel();
    private JLabel death = new JLabel();
    private JLabel stench = new JLabel();
    private JLabel breeze = new JLabel();
    private JLabel glimmer = new JLabel();
    private JLabel bump = new JLabel();
}
