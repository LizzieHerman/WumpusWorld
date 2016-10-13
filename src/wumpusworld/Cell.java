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
public class Cell {
    boolean wumpus;
    boolean pit;
    boolean obstacle;
    boolean gold;
    boolean explorer;
    
    public Cell(){
        wumpus = false;
        pit = false;
        obstacle = false;
        gold = false;
        explorer = false;
    }
    
    public boolean get(char c){
        if(c == 'w') return wumpus;
        if(c == 'p') return pit;
        if(c == 'o') return obstacle;
        if(c == 'g') return gold;
        if(c == 'e') return explorer;
        return false;
    }
    
    public void set(char c, boolean b){
        if(c == 'w') wumpus = b;
        if(c == 'p') pit = b;
        if(c == 'o') obstacle = b;
        if(c == 'g') gold = b;
        if(c == 'e') explorer = b;
    }
}
