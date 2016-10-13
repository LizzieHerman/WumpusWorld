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
    GridLayout boardLayout;
    int[] stat = {0, 0, 0, 0, 0, 0, 0, 0};
    
    
    Container explorer1, explorer2, explorer3, explorer4, arrow1, arrow2, arrow3, arrow4, 
            wumpus, deadwumpus, obstacle, pit, gold, inGold, inWumpus, inPit, empty;
    
    public GameBoard(int size, int wump){
        // the board will include an indexing of the board spaces
        boardLayout = new GridLayout(size+1, size+1);
        stat[1] = wump;
        initPanels();
    }
    
    private void initPanels(){
        //make board
        JPanel board = new JPanel();
        board.setLayout(boardLayout);
        
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
