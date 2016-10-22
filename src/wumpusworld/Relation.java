package wumpusworld;

import java.util.ArrayList;

/**
 * 
 * @author Greg Neznanski
 */

public class Relation{
	private ArrayList<MapCell> related;
	private MapCell relatedTo;
	private char hazard;
	
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
	
	//Check to see if a hazard flag is no longer set for each cell in the relation, means it was updated based on new information since relation was created
	public boolean checkFlags(){
		boolean valid = true;
		
		if(this.hazard == 'u'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('u')){
					valid = false;
					break;
				}
			}
		}else if(this.hazard == 'i'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('i')){
					valid = false;
					break;
				}
			}
		}
		
		return valid;
	}
	
	//If hazard flag has been changed from updated knowledge, remove the unflagged cell from the relation
	public void updateFlags(){
		if(this.hazard == 'u'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('u')) related.remove(i);
			}
		}else if(this.hazard == 'i'){
			for(int i = 0; i < related.size(); i++){
				if(!this.related.get(i).get('i')) related.remove(i);
			}
		}
	}
	
	//Clear flags in each cell of the relation
	public void clearRelation(char hazard){
		if(this.hazard == hazard){
			for(int i = 0; i < related.size(); i++){
				related.get(i).set(hazard, false);
			}
		}
		this.relatedTo.set('r', true); //Ignore breeze/smell for the relatedTo cell since it is no longer valid for this relation
	}
	
	//Check cells in the relation for a confirmed wumpus or pit
	public boolean checkKnown(){
		boolean valid = false;
		for(int i = 0; i < related.size(); i++){
			if(this.hazard == 'u'){
				if(related.get(i).get('w')){
					valid = true;
					break;
				}
			}else{
				if(related.get(i).get('p')){
					valid = true;
					break;
				}
			}
		}
		return valid;
	}
	
	//If a confirmed wumpus or pit is found, clear other cells from the relation
	public void updateKnown(){
		for(int i = 0; i < related.size(); i++){
			if(this.hazard == 'u'){
				if(related.get(i).get('u') && !related.get(i).get('w')){
					related.get(i).set('u', false);
					related.remove(i);
				}
			}else{
				if(related.get(i).get('i') && !related.get(i).get('p')){
					related.get(i).set('i', false);
					related.remove(i);
				}
			}
		}
		this.relatedTo.set('r', true); //Ignore breeze/smell from relatedTo cell since we know where the hazard is for this relation
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
    	return this.relatedTo;
    }
}
