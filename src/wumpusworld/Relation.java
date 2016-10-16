package wumpusworld;

import java.util.ArrayList;

/**
 * 
 * @author Greg Neznanski
 */

public class Relation extends Clause {
	ArrayList<MapCell> related;
	char hazard;
	
	public Relation(ArrayList<MapCell> related, char hazard) {
		super(0);
		this.related = related;
		this.hazard = hazard;
	}
	
	public boolean check(){
		boolean valid = true;
		
		if(this.hazard == 'w'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('u')) valid = false;
			}
		}else if(this.hazard == 'p'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('i')) valid = false;
			}
		}
		
		return valid;
	}
	
	public void update(){
		if(this.hazard == 'w'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('u'))related.remove(i);
			}
		}else if(this.hazard == 'p'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('i')) related.remove(i);
			}
		}
	}
}
