package wumpusworld;

/**
 *
 * @author Lizzie Herman
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PopUp extends JFrame{
    static int BUMP_WALL = 0;
    static int BUMP_OBS = 1;
    static int SCREAM = 2;
    static int DIED_WUMPUS = 3;
    static int DIED_PIT = 4;
    static int NO_MOVES = 5;
    static int WON = 6;
    JButton okButton = new JButton("OK");
    

    // to open pop up: construct, pack(), setVisible(true)
    public PopUp(int a, int cost){
        String mess = getMessage(a, cost);
        init(mess);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Pop-Up");
    }
    
    private void init(String mess){
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        JTextArea message = new JTextArea();
        message.setEditable(false);
        message.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        message.setText(mess);
        message.setFont(new java.awt.Font("Monospaced", 0, 18));
        okButton.setAlignmentX(CENTER_ALIGNMENT);
        okButton.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18));
        panel.setLayout(layout);
        panel.add(message);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(okButton);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                OKactionPerformed(evt);
            }
        });
        add(panel, BorderLayout.CENTER);
    }
    public void OKactionPerformed(ActionEvent e) {
        dispose();
    }
    
    
    private String getMessage(int a, int cost){
        String message;
        switch(a){
            case 0:
                message = "Player bumped into a wall.\nPlayer returned to previous position.";
                break;
            case 1:
                message = "Player bumped into a obstacle.\nPlayer returned to previous position.";
                break;
            case 2:
                message = "Player heard dying shriek of a Wumpus.\nOne less Wumpus on the board."
                        + "\nPlayer rewarded for ending Wumpus's life.";
                break;
            case 3:
                message = "Player walked into cell with Wumpus.\nPlayer lost their head."
                        + "\nPlayer has been penalized for dying.\nClone of player placed in last safe cell.";
                break;
            case 4:
                message = "Player walked into cell with a bottomless pit.\nPlayer is falling through the void."
                        + "\nPlayer has been penalized for dying.\nClone of player placed in last safe cell.";
                break;
            case 5:
                message = "Player has no more available moves.\nPlayer loses the game."
                        + "\nTotal cost of game: " + cost;
                break;
            case 6:
                message = "Player has grabbed the gold.\nPlayer wins the game."
                        + "\nTotal cost of game: " + cost;
                break;
            default:
                message = "This is the default Pop-Up";
                break;
            
        }
        return message;
    }
}
