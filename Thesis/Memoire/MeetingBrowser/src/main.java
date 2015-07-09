import java.util.ArrayList;


public class main {

	/*******************************************************/
	static void Run(transcript T,questions Q, algorithm A,int size, int step) throws Exception{
	
		int score_passage = 0;
		int score_truefalse = 0;
		
		for(int i=0; i<Q.listpairs.size();i++)
			{				
				ArrayList<qfield> q1 = Q.listpairs.get(i).question1;
				ArrayList<qfield> q2 = Q.listpairs.get(i).question2;	
				int pos1 = Q.listpairs.get(i).start_answering;
				int pos2 = Q.listpairs.get(i).end_answering;

				// Passage retrieval	
				passage p1 = A.PassageRetrieval(q1, T,size*q1.size(),step*q1.size());
				passage p2 = A.PassageRetrieval(q2, T,size*q2.size(),step*q2.size());
		
		
				score_passage = score_passage + A.PassageEvaluation(p1,pos1,pos2, T,0);
		
				score_truefalse = score_truefalse + A.True_False_nAnswering(q1,q2,p1,p2,T,size,step);
			}
	
		System.out.println("Correct answers for passage:"+score_passage);
		System.out.println("Correct answers for true-false questions:"+score_truefalse);	
	}
	/*******************************************************
	*  (bcde,a), (acde,b), (abde,c), (abce,d), (abcd, e)
	*/
	static void Cross_Validation(transcript T,questions Q, algorithm A,int size, int step) throws Exception{
		
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.###");
		
		int[] passages = new int[6];
		int[] answers = new int[6];
		
		for(int dd = 0; dd<5;dd++)
		{
			
			int max_ScoPass=0;
			int max_ScoTF=0;			
			int max_sizePass=0;
			int max_SizeTF=0;
			int max_stepPass=0;
			int max_StepTF=0;		
			for(step=1;step<=13;step++)
			{
				System.out.print("\nstep = " + step+":\n");
				for(size=1;size<=13;size++)	if(step<=size)
				{
					int score_passage = 0;
					int score_truefalse = 0;
					
					for(int i=0; i<Q.listpairs.size();i++)
						if(i<Q.listpairs.size()*dd/5 || i>=Q.listpairs.size()*(dd+1)/5)
						{				
							ArrayList<qfield> q1 = Q.listpairs.get(i).question1;
							ArrayList<qfield> q2 = Q.listpairs.get(i).question2;	
							int pos1 = Q.listpairs.get(i).start_answering;
							int pos2 = Q.listpairs.get(i).end_answering;
			
							// Passage retrieval			
							passage p1 = A.PassageRetrieval(q1, T,size*q1.size(),step*q1.size());
							passage p2 = A.PassageRetrieval(q2, T,size*q2.size(),step*q2.size());
			
							score_passage = score_passage + A.PassageEvaluation(p1, pos1, pos2, T, 0);
			
							// True-false statement
							score_truefalse = score_truefalse + A.True_False_nAnswering(q1,q2,p1,p2,T,size,step);
						}
					System.out.println(score_passage);
					
					if(max_ScoPass<score_passage) {
						max_ScoPass = score_passage;			
						max_sizePass=size;
						max_stepPass=step;
					}
					
					if(max_ScoTF < score_truefalse){
						max_ScoTF = score_truefalse;
						max_SizeTF = size;
						max_StepTF = step;
					}					
			
				}
			}
					
			String section = "(bcde,a)";
			if(dd==1) section = "(acde,b)";
			else if(dd==2) section = "(abde,c)";
			else if(dd==3) section = "(abce,d)";
			else if(dd==4) section = "(abcd,e)";
					
			
			// FOR PASSAGE
			System.out.print("Passage "+section+": "+max_ScoPass+" at ("+max_sizePass+","+max_stepPass+")");
		
			size = max_sizePass;
			step = max_stepPass;
			
			int score_passage = 0;
			int score_truefalse = 0;
			
			for(int i=0; i<Q.listpairs.size();i++)
				if(Q.listpairs.size()*dd/5<=i && i<Q.listpairs.size()*(dd+1)/5)
				{				
					ArrayList<qfield> q1 = Q.listpairs.get(i).question1;
					ArrayList<qfield> q2 = Q.listpairs.get(i).question2;	
					int pos1 = Q.listpairs.get(i).start_answering;
					int pos2 = Q.listpairs.get(i).end_answering;

					// Passage retrieval	
					passage p1 = A.PassageRetrieval(q1, T,size*q1.size(),step*q1.size());
					passage p2 = A.PassageRetrieval(q2, T,size*q2.size(),step*q2.size());
			
			
					score_passage = score_passage + A.PassageEvaluation(p1,pos1,pos2, T,0);
			
					score_truefalse = score_truefalse + A.True_False_nAnswering(q1,q2,p1,p2,T,size,step);
				}
		
			System.out.println("Correct answers for test data:"+score_passage);
			passages[dd] = score_passage;
			
			
			// FOR TRUE-FALSE ANSWERS
			System.out.print("True false answers "+section+": "+max_ScoTF+" at ("+max_SizeTF+","+max_StepTF+")");
			
			size = max_SizeTF;
			step = max_StepTF;
			
			score_passage = 0;
			score_truefalse = 0;
			
			for(int i=0; i<Q.listpairs.size();i++)
				if(Q.listpairs.size()*dd/5<=i && i<Q.listpairs.size()*(dd+1)/5)
				{				
					ArrayList<qfield> q1 = Q.listpairs.get(i).question1;
					ArrayList<qfield> q2 = Q.listpairs.get(i).question2;	
					int pos1 = Q.listpairs.get(i).start_answering;
					int pos2 = Q.listpairs.get(i).end_answering;

					// Passage retrieval	
					passage p1 = A.PassageRetrieval(q1, T,size*q1.size(),step*q1.size());
					passage p2 = A.PassageRetrieval(q2, T,size*q2.size(),step*q2.size());
			
			
					score_passage = score_passage + A.PassageEvaluation(p1,pos1,pos2, T,0);
			
					score_truefalse = score_truefalse + A.True_False_nAnswering(q1,q2,p1,p2,T,size,step);
				}
		
			System.out.println("Correct answers for test data:"+score_truefalse);
			answers[dd] = score_truefalse;
		}
		
	int ss = Q.listpairs.size()/5;	
	
	double sum = 0;
	for(int i=0;i<5;i++) sum = sum + passages[i];
	double average = sum / 5;
	sum = 0;
	for(int i=0;i<5;i++) sum = sum +  (average - passages[i])*(average - passages[i]);
	sum = Math.sqrt(sum / 4);
	
	System.out.println("For passage: " + average + " +/-" + sum);
	System.out.println("For passage: " + average/ss + " +/-" + sum/ss);
	
	sum = 0;
	for(int i=0;i<5;i++) sum = sum + answers[i];
	average = sum / 5;
	sum = 0;
	for(int i=0;i<5;i++) sum = sum +  (average - answers[i])*(average - answers[i]);
	sum = Math.sqrt(sum / 4);
	System.out.println("For final answer: " + average + " +/-" + sum);
	System.out.println("For final answer: " + average/ss + " +/-" + sum/ss);
	
	
	}
	/*******************************************************/
	static void BET_Questions(transcript T,questions Q, algorithm A, int size, int step) throws Exception{
		int score_passage = 0;
		int score_truefalse = 0;
		
		System.out.println("Number of pairs:"+Q.listpairs.size());
		for(int i=0; i<Q.listpairs.size();i++)
			{				
			
			timer Timer = new timer();
			Timer.start();
			
				ArrayList<qfield> q1 = Q.listpairs.get(i).question1;
				ArrayList<qfield> q2 = Q.listpairs.get(i).question2;	
				int pos1 = Q.listpairs.get(i).start_answering;
				int pos2 = Q.listpairs.get(i).end_answering;

				// Passage retrieval	
				passage p1 = A.PassageRetrieval(q1, T,size*q1.size(),step*q1.size());
				passage p2 = A.PassageRetrieval(q2, T,size*q2.size(),step*q2.size());
		
		
				
				score_passage = A.PassageEvaluation(p1,pos1,pos2, T,0);
				
				System.out.println(score_passage);
		
				score_truefalse = A.True_False_nAnswering(q1,q2,p1,p2,T,size,step);
				
			    //Timer.end();
			    //System.out.println(Timer.duration());				
				//System.out.println(score_truefalse);
			}
	
	}	
	/*******************************************************/
	public static void main(String[] args) throws Exception {
		String filename = "IS1008c";
		//String filename = "IB4010";
		
		//System.out.println("Run < 0.50 on " + filename);
		System.out.println("Run stem +le+syn on " + filename);

		//transcript T = new transcript("transcrip_"+filename+"_summary_done.txt",filename+".extream.txt","transcrip_+"+filename+"_summary_done_by_hand.txt","stop-words.txt");
		transcript T = new transcript("transcrip_"+filename+"_done.txt",filename+"_transcript.txt","transcrip_+"+filename+"_done_by_hand.txt","stop-words.txt");
		T.load();
		// Questions Analyse
		questions Q = new questions("questions_"+filename+".txt","stop-words.txt");
		//questions Q = new questions("questions_"+filename+"_summary.txt","stop-words.txt");
		Q.load();

		// ..........

		algorithm A = new algorithm();
		
		int size = 4;
		int step = 1;
		
		timer Timer = new timer();
		Timer.start();
		Cross_Validation(T,Q, A, size,step); // run (bcde,a) (acde,b), (abde,c), (abce,d), (abcd,e)		
	    
		//Run(T,Q, A, size,step);              // run (abcde)

		//BET_Questions(T,Q, A, size,step);    // run 8 BET questions;
		
	    Timer.end();
	    System.out.println("\n" + Timer.duration() + " ms");
	
		
	}
}

