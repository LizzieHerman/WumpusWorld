/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    Container explorer1, explorer2, explorer3, explorer4, arrow1, arrow2, arrow3, arrow4, 
            wumpus, deadwumpus, obstacle, pit, gold, inGold, inWumpus, inPit, empty;
    
    public GameBoard(String name, int size){
        super(name);
        // the board will include an indexing of the board spaces
        boardLayout = new GridLayout(size+1, size+1);
    }
    
    public void initComponents(){
        //make board
        final JPanel board = new JPanel();
        board.setLayout(boardLayout);
        
        // make stats
        final JPanel stats = new JPanel();
        stats.setLayout(new GridBagLayout());
        
        // combine board and stats
        this.add(board, BorderLayout.NORTH);
        this.add(new JSeparator(), BorderLayout.CENTER);
        this.add(stats, BorderLayout.SOUTH);
    }
    
    /* add to wumpus world
                 
    
    
        
    
    added percept knowledge functionality
    */
}
