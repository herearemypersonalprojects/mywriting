import java.util.ArrayList;

class commonWord{
	String word;
	int originalPosition; // before removing stop-wrods
	int position; 		  // after removing stop-words
	double score;
	
}
/*******************************************************/
public class passage {
	double score;			
	int distance;
	int window_pos1;		
	int window_pos2;
	int matched_number;
	ArrayList<commonWord> commonWords;
	
	
	/*******************************************************/
	public passage(){
		matched_number = 0;
		distance = 0;
		score = 0.0;
		commonWords = new ArrayList<commonWord>();
	}
	/*******************************************************/
	public void addCommonWord(String word, int oriPos, int pos, double sco){
		commonWord cw = new commonWord();
		cw.word = word;
		cw.originalPosition = oriPos;
		cw.position = pos;
		cw.score = sco;
		
		commonWords.add(cw);
	}
	/*******************************************************/
	public boolean isCommonWord(String word){
		for(int i=0;i<this.commonWords.size();i++)
			if(this.commonWords.get(i).word.compareTo(word)==0)
				return true;
		return false;
	}
	
}
