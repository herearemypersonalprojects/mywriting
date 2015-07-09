import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

/*******************************************************/
class qfield {
	String word;
	String stem;
	ArrayList<String> lemmas;	
	
	public qfield(){
		this.lemmas = new ArrayList<String>();
	}
}
/*******************************************************/

class pair{
	ArrayList<qfield> question1;
	ArrayList<qfield> question2;
	int start_answering;
	int end_answering;
	String answering;
	
	public pair(String q1, String q2, int start, int end, String ans) throws Exception{
		String[] words1 = q1.split(" "); // q1.split("\\b");
		String[] words2 = q2.split(" "); // q2.split("\\b"); 
		
		question1 = new ArrayList<qfield>();
		question2 = new ArrayList<qfield>();
		
		for(int i= 0; i<words1.length; i++){
			if(words1[i].trim().length()>0)
			{
				qfield f = new qfield();
				f.stem = tools.stem(words1[i]);
				f.word = words1[i];
				
				ArrayList<String> setlems = tools.lemmas(f.stem);
				f.lemmas.add(f.stem);
				for(int j=0; j<setlems.size(); j++)
				{
					try {
						String stem;
						stem = tools.stem(setlems.get(j));
						f.lemmas.add(stem);					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				question1.add(f);
			}
		}
		
		for(int i= 0; i<words2.length; i++){
			if(words2[i].trim().length()>0)
			{
				qfield f = new qfield();
				f.stem = tools.stem(words2[i]);
				f.word = words2[i];
				
				ArrayList<String> setlems = tools.lemmas(f.stem);
				f.lemmas.add(f.stem);
				for(int j=0; j<setlems.size(); j++)
				{
					try {
						String stem;
						stem = tools.stem(setlems.get(j));
						f.lemmas.add(stem);					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				question2.add(f);
			}
		}		
		
		this.start_answering = start;
		this.end_answering = end;
		this.answering = ans;
	}
	
	public void println(){
		for(int i=0; i<this.question1.size();i++) 
			System.out.print(this.question1.get(i)+" ");
		System.out.println();
		for(int i=0; i<this.question2.size();i++) 
			System.out.print(this.question2.get(i)+" ");
		System.out.println();
	}
}

/*******************************************************/

public class questions {
	ArrayList<pair> listpairs;
	
	String filename;
	String stopwordfile;
	
	/*******************************************************/
	public questions(){
		this.filename = "questions_IB4010.txt";
		this.stopwordfile = "stop-words.txt";
		listpairs = new ArrayList<pair>();		
	}
	/*******************************************************/
	public questions(String Filename, String Stopwordfile){
		this.filename = Filename;
		this.stopwordfile = Stopwordfile;
		listpairs = new ArrayList<pair>();
	}
	/**
	 * @throws Exception *****************************************************/
	public void load() throws Exception{
		
		ArrayList<String> ques = file.readQuestions(filename);
	
		ListIterator i = ques.listIterator();	
		while(i.hasNext()){
			String question1 = (String)i.next();
			String question2 = (String)i.next();
			//System.out.println(question2);
			int start = Integer.parseInt((String)i.next().toString().trim());
			int end = Integer.parseInt((String)i.next().toString().trim());
			
			String ans = (String)i.next();
			
			question1 = tools.abbv(question1);
			question2 = tools.abbv(question2);
			
			question1 = tools.ConvertNumeric(question1);
			question2 = tools.ConvertNumeric(question2);
			
			question1 = tools.removeStopWords(question1, this.stopwordfile);
			question2 = tools.removeStopWords(question2, this.stopwordfile);
				
			question1 = question1.replace(".", "").replace(",", "").toLowerCase();
			question2 = question2.replace(".", "").replace(",", "").toLowerCase();

			pair p = new pair(question1, question2, start, end, ans);
			this.listpairs.add(p);			
			
			//System.out.println(question1);
			//System.out.println(question2);
			//System.out.println(ans);
		}
	}
	
	/******************************************************/
	public static void main(String[] args) throws Exception{
		String filename = "IS1008c";
		questions Questions = new questions("questions_"+filename+".txt","stop-words.txt");
		Questions.load();
		int length = 0;
		for(int i=0;i<Questions.listpairs.size();i++){
			length = length + Questions.listpairs.get(i).question1.size();
			System.out.println("Stt = " + i + " : " + Questions.listpairs.get(i).question1.size());
		}
		length = length / Questions.listpairs.size();
		System.out.println("Length average = " + length);
	}
	
}
