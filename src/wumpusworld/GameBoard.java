/*
 * This will be part of the gui once it is complete
 */

package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */
import java.awt.*;
import javax.swing.*;

public class GameBoard extends JFrame {
    GridLayout boardLayout;
    JPanel board;
    Component[][] grid;
    
    public GameBoard(int wump, String[][] world, Cell[][] actualWorld){
        // the board will include an indexing of the board spaces
        boardLayout = new GridLayout(world.length+1, world.length+1);
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
        
        // combine board and stats
        this.add(board, BorderLayout.NORTH);
        //this.add(new JSeparator(), BorderLayout.CENTER);
    }
    
    public void updateUI(){
    	board.revalidate();
    	board.repaint();
    }
}
