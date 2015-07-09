import java.util.ArrayList;

interface Score
{
	public static final boolean debug = false;
	
    public static final double stem = 1.0;
    public static final double speaker = 4;
    public static final double NP = 2.5;
    public static final double JJ = 1.5;
    public static final double NN = 2.0;    
    public static final double VB = 1.5;
    
    public static final double synonym = 0.5;
}

public class algorithm {

	
	/******************************************************/
	boolean compare(String word, String lemma) {
		
		if(word.equals(lemma)) return true;
		
		ArrayList<String> setlems = tools.lemmas(lemma);
		for(int i=0; i<setlems.size(); i++)
		{
			try {
				String stem;
				stem = tools.stem(setlems.get(i));
				if(word.equals(stem))
					return true;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/*******************************************************/
	passage getPassageBasedFrequence(ArrayList<qfield> question,transcript Transcript, int pos,int window_size){
        passage p = new passage();
		p.window_pos1 = pos + window_size;
        p.window_pos2 = pos;
        return p;
		
	}
	
	/*******************************************************/
	double OkapiMB25(int j, transcript t,int pos, int window_size, int window_step){
		
		int PassNum = ((t.WordNumber - window_size)/window_step)+1;
		int PassNumContainWord = t.listwords.get(j).frequence*(window_size/window_step);		
		
		int WordNumInPassage = 0;		
		for(int i=0; i< t.listwords.get(j).frePos.size();i++){
			if(pos<=t.listwords.get(j).frePos.get(i) && t.listwords.get(j).frePos.get(i)<=pos+window_size)
				WordNumInPassage = WordNumInPassage + 1;
		}
		
		double fre = (double)WordNumInPassage/(double)window_size ;
		double idf = Math.log10((PassNum-PassNumContainWord+0.5)/(PassNumContainWord+0.5));
		double varK1 = 2.0;
		//double varb = 0.75;
		
		double score = idf*((fre*(varK1+1))/(fre+varK1));
		//System.out.println(PassNum+":"+PassNumContainWord+":"+WordNumInPassage+":"+fre+":"+idf+":"+score);
		return score;
	}
	/*******************************************************/
passage getPassageNgram(ArrayList<qfield> question,transcript Transcript, int pos,int window_size, int window_step, int ngram){
passage p = new passage();
ArrayList<String> name = new ArrayList<String>();

p.window_pos1 = pos + window_size;
p.window_pos2 = pos;
        
if(pos<0 || pos+window_size>Transcript.listwords.size()) return p;
		
boolean[] availTran = new boolean[window_size];
for(int i=0; i<window_size; availTran[i++]=true);
        
boolean[] availQues = new boolean[question.size()];
for(int i=0; i<question.size(); availQues[i++]=true); 
        
// speaker
for(int i=0; i<question.size(); i++){
	for(int j=pos; j<pos+window_size && availTran[j-pos]; j++)
   		if(question.get(i).stem.equals(Transcript.listwords.get(j).speaker)){
   			name.add(Transcript.listwords.get(j).speaker);
            availQues[i] = false;
            p.addCommonWord(Transcript.listwords.get(j).speaker, 
               				Transcript.listwords.get(j).pos, 
                   			j, 
                   			Score.speaker);
            if(p.window_pos1 > j) p.window_pos1 = j;
            if(p.window_pos2 < j) p.window_pos2 = j;
            
            p.score = p.score + Score.speaker;
            break;
    	}
}
        
// stem       
for(int i=0; i<question.size() && availQues[i]; i++){
	for(int j=pos; j<pos+window_size && availTran[j-pos]; j++){
		boolean similarity = true;
        for(int ng = 0; ng<ngram && j+ng < Transcript.listwords.size() && i+ng < question.size(); ng++){                            	
        	//if(Transcript.listwords.get(j+ng).stem.compareTo(question.get(i+ng).stem)!=0)                            	
            if(!question.get(i+ng).lemmas.contains(Transcript.listwords.get(j+ng).stem))
            //if(Transcript.listwords.get(j+ng).word.compareTo(question.get(i+ng).word)!=0)
            	similarity = false;
            }
    
            if(similarity){       
            	availTran[j-pos] = false;
                availQues[i] = false;
                p.addCommonWord(Transcript.listwords.get(j).stem, 
                				Transcript.listwords.get(j).pos, 
        						j, 
        						Score.stem);
               if(p.window_pos1 > j) p.window_pos1 = j;
               if(p.window_pos2 < j) p.window_pos2 = j;
                                    
               p.score = p.score + Score.stem;
                                    
               if(name.contains(Transcript.listwords.get(j).speaker))
            	   p.score = p.score + 1.5;
               
               break;
           }
    }      
}
            
// synonyms
for(int i=0; i<question.size() && availQues[i]; i++){        
	for(int j=pos; j<pos+window_size && availTran[j-pos];j++){
			boolean similarity = true;
            for(int ng = 0; ng<ngram && j+ng < Transcript.listwords.size() && i+ng < question.size(); ng++){
                if(!Transcript.listwords.get(j+ng).synonyms.contains(question.get(i+ng).stem))                                            
                similarity = false;
            }                
            
            if(similarity){       
            	availTran[j-pos] = false;					
            	p.addCommonWord(Transcript.listwords.get(j).stem, 
        						Transcript.listwords.get(j).pos, 
        						j, 
        						Score.synonym);
                if(p.window_pos1 > j) p.window_pos1 = j;
                if(p.window_pos2 < j) p.window_pos2 = j;
                                    
                p.score = p.score + Score.synonym;
                                    
                break;
            }
		}
	}

return p;
}

	/*******************************************************/
public passage PassageRetrieval(ArrayList<qfield> question1, transcript Transcript, int window_size, int window_step){
	passage bestPassage = new passage(); 
				
	for(int pos=0; pos<Transcript.listwords.size()-window_size; pos=pos+window_step){
		int ngram = 1;
		passage p = this.getPassageNgram(question1, Transcript, pos, window_size, window_step, ngram);
		if(p.score > bestPassage.score) bestPassage = p;
        	else if(p.score == bestPassage.score ){
        		passage p1 = p;
        		passage p2 = bestPassage;
        		ngram ++;
        		while(p1.score == p2.score && ngram < question1.size()){// && p1.matched_number >= n_gram){
        			p1 = getPassageNgram(question1,Transcript,p1.window_pos1,window_size,window_step,ngram);
        			p2 = getPassageNgram(question1,Transcript,p2.window_pos1,window_size,window_step,ngram);
        			ngram ++;
        			}
                 
            if(p1.score>p2.score)
            	bestPassage = p;
       }
	}
		
	for(int i=0;i<bestPassage.commonWords.size()-1;i++)
		for(int j=i;j<bestPassage.commonWords.size();j++){
			int d = bestPassage.commonWords.get(i).originalPosition - bestPassage.commonWords.get(j).originalPosition;
			bestPassage.distance = bestPassage.distance + Math.abs(d);
		}
		
return bestPassage;
}

	/*******************************************************/
public int PassageEvaluation(passage p,int pos1,int pos2, transcript t, int threshold){	
//		 at least 2 common words between passage candidate and passage reference
	if((pos1 <= t.listwords.get(p.window_pos1).pos && t.listwords.get(p.window_pos1).pos + threshold <= pos2) || 
	   (pos1 <= t.listwords.get(p.window_pos2).pos - threshold && t.listwords.get(p.window_pos2).pos <= pos2) || 
	   (t.listwords.get(p.window_pos1).pos <= pos1 && pos1 + threshold <= t.listwords.get(p.window_pos2).pos) || 
	   (t.listwords.get(p.window_pos1).pos <= pos2 - threshold && pos2 <= t.listwords.get(p.window_pos2).pos))
			return 1;
return 0;
}

	/*******************************************************/
public int True_False_nAnswering(ArrayList<qfield> q1,ArrayList<qfield> q2,passage p1,passage p2, transcript Transcript, int size, int step){
if(p1.score>p2.score) return 1;
	else if(p1.score == p2.score){
		int ngram = 2;
		passage pp1 = p1;
		passage pp2 = p2;
		while(pp1.score == pp2.score && ngram < q1.size() && ngram < q2.size()){
			pp1 = getPassageNgram(q1,Transcript,pp1.window_pos1,size,step,ngram);
			pp2 = getPassageNgram(q2,Transcript,pp2.window_pos1,size,step,ngram);
			ngram ++;
			}
         
    if(pp1.score>pp2.score)
    	return 1;
    else if (pp1.score == pp2.score)
    	if(q1.size() < q2.size())
    		return 1;
	}	
return 0;
}


}
