package wumpusworld;

import java.util.ArrayList;

/**
 * 
 * @author Greg Neznanski
 */

public class Relation{
	ArrayList<MapCell> related;
        MapCell relatedTo;
	char hazard;
	
	public Relation(MapCell[] related, char hazard, MapCell relatedTo) {
		this.hazard = hazard;
                this.relatedTo = relatedTo;
		this.related = new ArrayList<MapCell>();
		for(int i = 0; i < related.length ; i++){
			if(related[i] != null){
				this.related.add(related[i]);
			}
		}
	}
	
	public boolean check(){
		boolean valid = true;
		
		if(this.hazard == 'u'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('u')) valid = false;
			}
		}else if(this.hazard == 'i'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('i')) valid = false;
			}
		}
		
		return valid;
	}
	
	public void update(){
		if(this.hazard == 'u'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('u'))related.remove(i);
			}
		}else if(this.hazard == 'i'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('i')) related.remove(i);
			}
		}
	}
	
	public MapCell getCell(){
		return related.get(0);
	}
	
	public ArrayList<MapCell> getRelated(){
		return this.related;
	}
	
	public char getHazard(){
		return this.hazard;
	}
        
        public MapCell relatedTo(){
            return relatedTo();
        }
}
