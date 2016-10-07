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
public class Explorer {
    int x;
    int y;
    int direction; // 1=up, 2=right, 3=down, 4=left
    int cost;
    int size;
    int arrows;
    WumpusWorld world;
    
    public Explorer(WumpusWorld w, int n, int num){ // the world it is exploring, world size (n by n), number wumpi num
        x = 0;
        y = 0;
        direction = 1;
        cost = 0;
        size = n;
        arrows = num;
        world = w;
    }
    
    public boolean move(){
        cost -= 10;
        switch(direction){
            case 1:
                x++;
                break;
            case 2:
                y++;
                break;
            case 3:
                x--;
                break;
            case 4:
                y--;
        }
        if(world.getCell(x, y) == null){
            switch(direction){
                case 1:
                    x--;
                    break;
                case 2:
                    y--;
                    break;
                case 3:
                    x++;
                    break;
                case 4:
                    y++;
            }
            return false;
        }
        return true;    
    }
    
    public void turnRight(){
        cost -= 10;
        if(direction == 4) direction = 1;
        else direction++;
    }
    
    public void turnLeft(){
        cost -= 10;
        if(direction == 1) direction = 4;
        else direction--;
    }
    
    public void shootArrow(){
        cost -= 10;
        arrows--;
        /* to-do
         * how do we here a scream
         * regain cost is wumpus shot
         */
    }
}
