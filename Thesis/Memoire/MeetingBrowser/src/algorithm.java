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
        
        // received passage size <= search window size
        p.window_pos1 = pos + window_size;
        p.window_pos2 = pos;
        
        //ArrayList<String> name = new ArrayList<String>();
        String name = "";
        
        if(pos<0 || pos+window_size>Transcript.listwords.size()) return p;
		
        boolean[] availTran = new boolean[window_size];
        for(int i=0; i<window_size; availTran[i++]=true);
        
        boolean[] availQues = new boolean[question.size()];
        for(int i=0; i<question.size(); availQues[i++]=true); 
        
        for(int i=0; i<question.size(); i++){
            if(availQues[i])
            	
            	
            	// speaker
                for(int j=pos;j<pos+window_size;j++)
                        if(availTran[j-pos])
                        {        
                        	if(question.get(i).stem.equals(Transcript.listwords.get(j).speaker))
                        	
                                {
                        				name = Transcript.listwords.get(j).speaker;
                                        p.score = p.score + Score.speaker;// + OkapiMB25(j,Transcript,pos,window_size,window_step);
                                        availQues[i] = false;
                    					if(Score.debug)
                                        p.addCommonWord(Transcript.listwords.get(j).speaker, 
                    							Transcript.listwords.get(j).pos, 
                    							j, 
                    							Score.speaker);
                                        if(p.window_pos1 > j) p.window_pos1 = j;
                                        if(p.window_pos2 < j) p.window_pos2 = j;                                       
                                        break;
                                }
                        }
            
            // stem
            if(availQues[i])
                for(int j=pos;j<pos+window_size;j++)
                        if(availTran[j-pos])
                        {
                            boolean similarity = true;
                            for(int ng = 0; ng<ngram && j+ng < Transcript.listwords.size() && i+ng < question.size(); ng++){                            	
                                 //if(Transcript.listwords.get(j+ng).stem.compareTo(question.get(i+ng).stem)!=0)                            	
                            	 if(!question.get(i+ng).lemmas.contains(Transcript.listwords.get(j+ng).stem))
                            	 //if(Transcript.listwords.get(j+ng).word.compareTo(question.get(i+ng).word)!=0)
                                            similarity = false;
                            }
                            if(similarity)                            
                            {       
                                    availTran[j-pos] = false;
                                    availQues[i] = false;
                                    
                					p.addCommonWord(Transcript.listwords.get(j).stem, 
        									Transcript.listwords.get(j).pos, 
        									j, 
        									Score.stem);
                                    
                                    if(p.window_pos1 > j) p.window_pos1 = j;
                                    if(p.window_pos2 < j) p.window_pos2 = j;
                                    
                                    
                                    p.score = p.score + Score.stem;
                                    
                                    if(Transcript.listwords.get(j).speaker.equals(name))
                                		p.score = p.score + 1.5;
                                   break;
                            }
                        }        	
            
            // synonyms
            if(availQues[i])
                for(int j=pos;j<pos+window_size;j++)
                	if(availTran[j-pos])
                        {
                            boolean similarity = true;
                            for(int ng = 0; ng<ngram && j+ng < Transcript.listwords.size() && i+ng < question.size(); ng++){                            	
                                    //if(Transcript.listwords.get(j+ng).stem.compareTo(question.get(i+ng).stem)!=0)                            	
                            	if(!Transcript.listwords.get(j+ng).synonyms.contains(question.get(i+ng).stem))                                            
                                            similarity = false;
                            }
                            if(similarity) 
                            //if(Transcript.listwords.get(j).word.compareTo(question.get(i))==0)
                            {       
                                    availTran[j-pos] = false;
                                    availQues[i] = false;
                                  
                					p.addCommonWord(Transcript.listwords.get(j).stem, 
        									Transcript.listwords.get(j).pos, 
        									j, 
        									Score.stem);
        							                                  
                                    p.score = p.score + Score.synonym;                                    
                                    if(p.window_pos1 > j) p.window_pos1 = j;
                                    if(p.window_pos2 < j) p.window_pos2 = j;                               
                                    break;
                            }
                        }        	
            
        }
 
                                 
        return p;
	}

	/*******************************************************/
	public passage PassageRetrieval(ArrayList<qfield> question1, transcript Transcript, int window_size, int window_step){
		passage bestPassage = new passage(); 
				
		for(int pos=0; pos<Transcript.listwords.size()-window_size; pos=pos+window_step)
		{
			//passage p = this.getPassage(pos, window_size,question1,Transcript);
			int ngram = 1;
			passage p = this.getPassageNgram(question1, Transcript, pos, window_size, window_step, ngram);
			
					
			if(p.score > bestPassage.score) bestPassage = p;
			
            else if(p.score == bestPassage.score ){

            	
            	passage p1 = p;
                passage p2 = bestPassage;
                int n_gram = 2;
                while(p1.score == p2.score && n_gram < question1.size()){// && p1.matched_number >= n_gram){
                        p1 = getPassageNgram(question1,Transcript,p1.window_pos1,window_size,window_step,n_gram);
                        p2 = getPassageNgram(question1,Transcript,p2.window_pos1,window_size,window_step,n_gram);
                        n_gram ++;
                }
                 
                if(p1.score>p2.score)
                	bestPassage = p;
                else if(p1.score == p2.score)
                {
                	
//            		for(int i=0;i<p.commonWords.size()-1;i++)
//            			for(int j=i;j<p.commonWords.size();j++)
//            			{
//            				int d = p.commonWords.get(i).originalPosition - p.commonWords.get(j).originalPosition;
//            				p.distance = p.distance + Math.abs(d);
//            			}
//            		if(p.distance < bestPassage.distance)
//            			bestPassage = p;
//            		else
//            		{
//            			//System.out.println("Still two equal passages");	
//            		}
                }
            } 
			

		}
		
		for(int i=0;i<bestPassage.commonWords.size()-1;i++)
			for(int j=i;j<bestPassage.commonWords.size();j++)
			{
				int d = bestPassage.commonWords.get(i).originalPosition - bestPassage.commonWords.get(j).originalPosition;
				bestPassage.distance = bestPassage.distance + Math.abs(d);
			}
		
		if(Score.debug)
		{
		System.out.println("Found postion="+bestPassage.window_pos1);
		for(int i=0;i<question1.size();i++)System.out.print(question1.get(i).word+" ");
		System.out.println("Size of window = "+window_size);
		System.out.println("Step of window = "+window_step);
		System.out.println("Score = " + bestPassage.score);
		System.out.print("Common words: ");
		for(int j=0;j<bestPassage.commonWords.size();j++){
			System.out.print("("+bestPassage.commonWords.get(j).word+" ");
			System.out.print(bestPassage.commonWords.get(j).score+" ");
			System.out.print(bestPassage.commonWords.get(j).originalPosition+" ");
			System.out.print(bestPassage.commonWords.get(j).position+") ");
		}
		System.out.println("\nFound:");
		for(int i=bestPassage.window_pos1;i<=bestPassage.window_pos2;i++)
			System.out.print(Transcript.listwords.get(i).word+" ");
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
				{
					if(Score.debug)
					System.out.println("\n"+pos1+":"+pos2+":"+t.listwords.get(p.window_pos1).pos+":"+t.listwords.get(p.window_pos2).pos+"==> CORRECT \n");
					return 1;
				}
				if(Score.debug)
				System.out.println("==> INCORRECT\n");
				return 0;
	}
	/*******************************************************/
	public int True_False_nAnswering(ArrayList<qfield> q1,ArrayList<qfield> q2,passage p1,passage p2, transcript Transcript, int size, int step){
		
		if(p1.score>p2.score) return 1;
		else if(p1.score == p2.score){
			passage pp1 = this.getPassageNgram(q1, Transcript, p1.window_pos1, size*q1.size(),step*q1.size(), 2);
			passage pp2 = this.getPassageNgram(q2, Transcript, p2.window_pos1, size*q2.size(),step*q2.size(), 2);
			if(pp1.score > pp2.score) return 1;
			else if(pp1.score == pp2.score){// && pp1.matched_number>=3){
				pp1 = this.getPassageNgram(q1, Transcript, p1.window_pos1, size*q1.size(),step*q1.size(), 3);
				pp2 = this.getPassageNgram(q2, Transcript, p2.window_pos1, size*q2.size(),step*q2.size(), 3);
				if(pp1.score > pp2.score) return 1;
				
				else if(pp1.score == pp2.score){
					if(p1.distance<p2.distance) return 1;
					else if(p1.distance == p2.distance){
						if(q1.size()<q2.size()) return 1;
//						else if(q1.size() == q2.size()){
//								System.out.print("\n\nq1 = ");
//								for(int i = 0; i< q1.size(); i++) System.out.print(q1.get(i).w ord+" ");
//								System.out.print("\npass1=");
//								for(int i=p1.window_pos1;i<p1.window_pos1+size*q1.size();i++)
//									System.out.print(Transcript.listwords.get(i).word+" ");
//								System.out.println("distance = "+p1.distance);
//								
//								System.out.print("\nq2 = ");
//								for(int i = 0; i< q2.size(); i++) System.out.print(q2.get(i).word+" ");
//								System.out.print("\npass2=");
//								for(int i=p2.window_pos1;i<p2.window_pos1+size*q2.size();i++)
//									System.out.print(Transcript.listwords.get(i).word+" ");
//								System.out.println("distance = "+p1.distance);
//						}
					}
				}
			}
		}
			
		return 0;
	
	}
}
