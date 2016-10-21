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

public class GameBoard extends JFrame {
    private GridLayout boardLayout;
    private int[] stat = {0, 0, 0, 0, 0, 0, 0, 0}; // cost, arrow, move, death, stench, breeze, glimmer, bump
    private Component[][] grid;
        
    public GameBoard(int wump, String[][] world){
        // the board will include an indexing of the board spaces
        setMaximumSize(new Dimension(1050,1050));
        boardLayout = new GridLayout(world.length+1, world.length+1);
        stat[1] = wump;
        grid = new Container[world.length+1][world.length+1];
        makeGrid(world);
        initPanels();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Game Board");
    }
    
    private void makeGrid(String[][] world){
        int size = 100;
        if(grid.length <= 9) size = 100;
        else if(grid.length <= 10) size = 90;
        else if(grid.length <= 11) size = 80;
        else if(grid.length <= 12) size = 75;
        else if(grid.length <= 15) size = 60;
        else if(grid.length <= 18) size = 50;
        else if(grid.length <= 22) size = 40;
        else size = 36;
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                int x = i-1; int y = j-1;
                if(i == 0 && j == 0){
                    grid[i][j] = Box.createRigidArea(new Dimension(size,size));
                }else if(i == 0 ){
                    grid[i][j] = new JLabel(Integer.toString(y));
                    grid[i][j].setFont(new java.awt.Font("Monospaced", 0, size));
                }else if(j == 0){
                    grid[i][j] = new JLabel(Integer.toString(x));
                    grid[i][j].setFont(new java.awt.Font("Monospaced", 0, size));
                }else{
                    String inCell = world[x][y];
                    ImageIcon imgicon;
                    if(inCell.isEmpty() || inCell.equalsIgnoreCase("empty")){
                        imgicon = new ImageIcon(getClass().getResource("/wumpusworld/empty.png"));
                    }else if(inCell.equalsIgnoreCase("explorer")){
                        imgicon = new ImageIcon(getClass().getResource("/wumpusworld/explorer1.png"));
                    }else if(inCell.equalsIgnoreCase("wumpus")){
                        imgicon = new ImageIcon(getClass().getResource("/wumpusworld/wumpus.png"));
                    }else if(inCell.equalsIgnoreCase("pit")){
                        imgicon = new ImageIcon(getClass().getResource("/wumpusworld/pit.png"));
                    }else if(inCell.equalsIgnoreCase("gold")){
                        imgicon = new ImageIcon(getClass().getResource("/wumpusworld/gold.png"));
                    }else if(inCell.equalsIgnoreCase("wall")){
                        imgicon = new ImageIcon(getClass().getResource("/wumpusworld/wall.png"));
                    }else{
                        imgicon = new ImageIcon(getClass().getResource("/wumpusworld/empty.png"));
                    }
                    Image img = imgicon.getImage(); 
                    Image newimg = img.getScaledInstance(size, size,  java.awt.Image.SCALE_SMOOTH);  
                    imgicon = new ImageIcon(newimg);
                    grid[i][j] = new JLabel(imgicon);
                }
            }
        }  
    }
    
    public void changeGrid(int x, int y, String inCell, int n){
        int size = 100;
        if(grid.length <= 9) size = 100;
        else if(grid.length <= 10) size = 90;
        else if(grid.length <= 11) size = 80;
        else if(grid.length <= 12) size = 75;
        else if(grid.length <= 15) size = 60;
        else if(grid.length <= 18) size = 50;
        else if(grid.length <= 22) size = 40;
        else size = 36;
        x++; y++;
        ImageIcon imgicon;
        if(inCell.equalsIgnoreCase("empty")){
            imgicon = new ImageIcon(getClass().getResource("/wumpusworld/empty.png"));
        }else if(inCell.equalsIgnoreCase("explorer") && n == 1){
            imgicon = new ImageIcon(getClass().getResource("/wumpusworld/explorer1.png"));
        }else if(inCell.equalsIgnoreCase("explorer") && n == 2){
            imgicon = new ImageIcon(getClass().getResource("/wumpusworld/explorer2.png"));
        }else if(inCell.equalsIgnoreCase("explorer") && n == 3){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/explorer3.png")));
        }else if(inCell.equalsIgnoreCase("explorer") && n == 4){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/explorer4.png")));
        }else if(inCell.equalsIgnoreCase("wumpus")){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/wumpus.png")));
        }else if(inCell.equalsIgnoreCase("deadwumpus")){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/deadwumpus.png")));
        }else if(inCell.equalsIgnoreCase("inwumpus")){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/inwumpus.png")));
        }else if(inCell.equalsIgnoreCase("pit")){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/pit.png")));
        }else if(inCell.equalsIgnoreCase("inpit")){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/inpit.png")));
        }else if(inCell.equalsIgnoreCase("gold")){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/gold.png")));
        }else if(inCell.equalsIgnoreCase("ingold")){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/ingold.png")));
        }else if(inCell.equalsIgnoreCase("wall")){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/wall.png")));
        }else if(inCell.equalsIgnoreCase("path") && n == 1){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/right.png")));
        }else if(inCell.equalsIgnoreCase("path") && n == 2){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/down.png")));
        }else if(inCell.equalsIgnoreCase("path") && n == 3){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/left.png")));
        }else if(inCell.equalsIgnoreCase("path") && n == 4){
            imgicon = (new ImageIcon(getClass().getResource("/wumpusworld/up.png")));
        }else{
            imgicon = new ImageIcon(getClass().getResource("/wumpusworld/empty.png"));
        }
        Image img = imgicon.getImage(); 
        Image newimg = img.getScaledInstance(size, size,  java.awt.Image.SCALE_SMOOTH);  
        imgicon = new ImageIcon(newimg);
        grid[x][y] = new JLabel(imgicon);
    }
    
    public void changeCost(int cost){
        stat[0] = cost;
    }
    
    public void changeBoolStates(int state, boolean data){
        stat[state] = data ? 1 : 0;
    }
    
    public void incremStates(int state){
        stat[state] += 1;
    }
    
    public void decremStates(int state){
        stat[state] -= 1;
    }
    
    private void initPanels(){
        //make board
        JPanel board = new JPanel();
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
        stats.add(line1);
        stats.add(Box.createRigidArea(new Dimension(0,10)));
        stats.add(line2);
        stats.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        // combine board and stats
        this.add(board, BorderLayout.NORTH);
        this.add(new JSeparator(), BorderLayout.CENTER);
        this.add(stats, BorderLayout.SOUTH);
    }
    
    public void updatePanels(){
        JPanel nboard = new JPanel();
        nboard.setLayout(boardLayout);
        for(int j = grid.length-1; j >= 0; j--){
            for(int i = 0; i < grid.length; i++){
                nboard.add(grid[i][j]);
            }
        }
        cost.setText("Cost: " + stat[0]);
        arrow.setText("Arrows Left: " + stat[1]);
        move.setText("Moves: " + stat[2]);
        death.setText("Death Count: " + stat[3]);
        stench.setText("Smell Stench: " + stat[4]);
        breeze.setText("Feel Breeze: " + stat[5]);
        glimmer.setText("See Glimmer: " + stat[6]);
        bump.setText("Bump Count: " + stat[7]);
        this.add(nboard, BorderLayout.NORTH);
    }
    
    private JLabel cost = new JLabel();
    private JLabel arrow = new JLabel();
    private JLabel move = new JLabel();
    private JLabel death = new JLabel();
    private JLabel stench = new JLabel();
    private JLabel breeze = new JLabel();
    private JLabel glimmer = new JLabel();
    private JLabel bump = new JLabel();
}