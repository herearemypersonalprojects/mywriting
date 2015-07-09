import java.util.ArrayList;
import java.util.ListIterator;

/*******************************************************/
/*
 *     position | word | stem | speaker | frequence | parts-of-speech | synonyms
 */
class field {
	String word;
	int frequence;
	ArrayList <Integer> frePos;
	String stem;
	int pos;
	String speaker;
	ArrayList<String> synonyms;	
	String  tag;
	
	public field(){
		this.synonyms = new ArrayList<String>();
		this.frePos = new ArrayList<Integer>();
	}
}
/*******************************************************/

public class transcript {
	String filename;
	String filenameOriginal;
	String stopwordfile;
	String filenameHandy;
	
	ArrayList<field> listwords;
	
	int WordNumber;
	int WordDiff;
	/*******************************************************/
	public transcript(String Filename, String FilenameOriginal, String FileHandy, String StopWordFile){
		this.filename = Filename;
		this.filenameOriginal = FilenameOriginal;
		this.stopwordfile = StopWordFile;
		this.filenameHandy = FileHandy;
		listwords = new ArrayList<field>();
		
	}
	
	/*******************************************************/
	public transcript(){
		this.filenameOriginal = "IB4010_transcript.txt";
		this.stopwordfile = "stop-words.txt";
		this.filename = "transcrip_done.txt";
		this.filenameHandy="transcrip_done_by_hand.txt";
		listwords = new ArrayList<field>();
	}

	/**
	 * @throws Exception *****************************************************/
	public void create_summary(String id_meeting,double threshold) throws Exception{
		
		
		//if(id_meeting.equals("IS1008c"))
		//{
			String[] id =       {"MIO086",  "FIE073",   "FIE038","MIE085"};
			String[] speaker  = {"sridhar", "christine","agnes", "ed"};
			
		//}
		//else
		if(id_meeting.equals("IB4010"))
		{			
			id[0] = "MIO046";
			id[1] = "MIO036";
			id[2] = "FIE038";
			id[3] = "MIO095";
			
			speaker[0]  = "denis";
			speaker[1] =  "andrei";
			speaker[2] = "agnes";
			speaker[3] = "mirek";
		}
		
		
		
		stop_words stopWords = new stop_words(this.stopwordfile);
		
		ArrayList<String> transcript = file.readLines(this.filenameOriginal);
		ListIterator i = transcript.listIterator();			
		
		//file.deletefile(this.filename);
		int count = 0;
		i.next(); // speaker	start	end	rank	text
		while(i.hasNext())
		{
			String line = (String)i.next(); //MIO086 	32.34	32.78	0.194980694980695	To do that
			line = line.trim();
			while(line.indexOf("  ")>-1) line = line.replaceAll("  ", " ");
			
			String name = line.substring(0,line.indexOf(" "));
			
			for(int iname = 0; iname<4; iname++)
				name = name.replaceAll(id[iname], speaker[iname]);
			
			line = line.substring(line.indexOf(".")+1); // start
			line = line.substring(line.indexOf(".")+1); // end
			line = line.substring(line.indexOf("0."));
			String rank = line.substring(0,line.indexOf(" "));
			System.out.println(rank);
			//if(Double.parseDouble(rank)<threshold) continue;
			
			line = line.substring(line.indexOf(".")+1); // rank
			line = line.substring(line.indexOf(" ")+1);
			
			
			//System.out.println(line);
			
			line = line.replace("_", "").replace("^", "").replace("%", "");
			
			while(line.indexOf("'")>-1) line = tools.abbv(line);

			String words[] = line.split(" ");
		
			String[] tags = tools.tags(words);
			
			if(Double.parseDouble(rank)>=threshold)
			//if(Double.parseDouble(rank)<threshold)
			for(int k=0; k<words.length;k++)
				if(!stopWords.isStopWord(words[k])){ //if it is not stop-word, add it
					field f = new field();
					// position
					f.pos = count + k;
					//line = String.valueOf(f.pos);
					System.out.print(String.valueOf(count+k));
					
					
					// word
					f.word = words[k]; 
					//line = line.concat(" " +f.word+" ");
					System.out.println(" " +words[k]+" ");
					
					// stem
					f.stem = tools.stem(words[k]);
					//line = line.concat(f.stem+" ");
					//System.out.print(tools.stem(words[k])+" ");
					
					// spkear
					f.speaker = tools.stem(name.trim());
					//line = line.concat(f.speaker+" ");
					//System.out.print(tools.stem(name.trim())+" ");
					
					// synonyms
					//ArrayList<String> setsyns = tools.synonyms(words[k],tags[k]);
					ArrayList<String> setsyns = tools.synonyms(words[k],"NoPoS");
					for(int j=0;j<setsyns.size();j++)
					{
						setsyns.set(j, tools.stem(setsyns.get(j)));
						if(setsyns.get(j).compareTo(tools.stem(words[k])) !=0){
							//System.out.print(setsyns.get(j)+" ");
							f.synonyms.add(setsyns.get(j));
							//line = line.concat(f.synonyms.get(f.synonyms.size()-1)+" ");
						}						
					}
					
					//tagging
					f.tag = tags[k];
					
					//System.out.println(line);						
					listwords.add(f);
				}
			count = count + words.length;
		}	
		
					
		//Compute frequence of words
		int sum = 0;
		for(int k=0; k<listwords.size(); k++)
		{			
			int freq = 0;			
			ArrayList <Integer> fpos = new ArrayList<Integer>();
			for(int q = 0; q < listwords.size(); q++)
				if(listwords.get(k).stem.compareTo(listwords.get(q).stem)==0)
				{
					freq = freq + 1;
					fpos.add(q);						
				}
			
			field element = new field();
			element.frequence = freq;
			element.pos = listwords.get(k).pos;
			element.speaker = listwords.get(k).speaker;
			element.stem = listwords.get(k).stem;
			element.synonyms = listwords.get(k).synonyms;
			element.word = listwords.get(k).word;
			element.tag = listwords.get(k).tag;
			element.frePos = fpos;
			listwords.set(k, element);
			sum = sum + freq;
			//System.out.println(k+ " - " + listwords.get(k).word + " : " + listwords.get(k).frequence);
		}
		
		// compute number of different words
		boolean check[] = new boolean[listwords.size()];
		for(int k=0; k<listwords.size();k++) check[k]=true;
		
		
		for(int k=0; k<listwords.size();k++)
			for(int kk=k+1; kk<listwords.size();kk++){
				if(check[kk] && listwords.get(kk).stem.compareTo(listwords.get(k).stem)==0)
					check[kk]=false;
			}
		
		ArrayList<String> aa = new ArrayList<String>();
		ArrayList<Integer> bb = new ArrayList<Integer>();
		WordDiff = 0;
		for(int k=0;k<listwords.size();k++)
			if(check[k])
			{
				WordDiff ++;
				int index = 0;
				while(bb.size() > index && bb.get(index)<listwords.get(k).frequence) index++;
				aa.add(index, listwords.get(k).word);
				bb.add(index, listwords.get(k).frequence);
			}
		
		//System.out.println("Before removing stop-words: Number of words in transcript = " + listwords.get(listwords.size()-1).pos);
		//System.out.println("After removing stop-words:");
		System.out.println("      + Number of words in transcript= " + listwords.size());
		System.out.println("      + Number of different words in transcript = " + WordDiff);
		
		
		// Save to file
		System.out.println("Write to file "+filename);
		file.deletefile(this.filename);
		
		file.append(String.valueOf(listwords.size()),filename);
		file.append(String.valueOf(WordDiff),filename);

		// position | word | stem | speaker | frequence | parts-of-speech | synonyms
		 
		 for(int k=0; k<listwords.size(); k++) {
			 String line =listwords.get(k).pos + " " +
					     listwords.get(k).word + " " +
					     listwords.get(k).stem + " " +
					     listwords.get(k).speaker + " "+
					     listwords.get(k).tag + " " +
					     listwords.get(k).frequence+" ";
			 
		     for(int f=0;f<listwords.get(k).frePos.size();f++)
		     {
		    	 line = line.concat(Integer.toString(listwords.get(k).frePos.get(f))+" ");			    	 
		     }
		     
			 for(int j=0; j<listwords.get(k).synonyms.size(); j++)
				 line = line.concat(listwords.get(k).synonyms.get(j)+" ");
			file.append(line, filename);

	
		 }
		
		
		//for(int k=0;k<wordcount;k++){
		//	System.out.println((k+1)+") " + aa.get(k) + " :" + bb.get(k));
		//}
}
	/*******************************************************/
	public void create() throws Exception{
		// Procedure: Analyse the transcript
			stop_words stopWords = new stop_words(this.stopwordfile);
			
			ArrayList<String> transcript = file.readLines(this.filenameOriginal);
			ListIterator i = transcript.listIterator();			
			
			int count = 0;
			
			while(i.hasNext()){
				String question = (String)i.next();
				question = question.substring(question.lastIndexOf("]")+2);
				
				String name = question.substring(0,question.indexOf(":"));
				question = question.substring(question.indexOf(":")+2);				
				
				question = question.replace("_", "").replace("^", "").replace("%", "");
				
				while(question.indexOf("'")>-1) question = tools.abbv(question);
								
				//Transcript -> | word | stem | position | speaker | synonyms |
				String words[] = question.split(" ");
				String[] tags = tools.tags(words);

				for(int k=0; k<words.length;k++)
					if(!stopWords.isStopWord(words[k])){ //if it is not stop-word, add it
						field f = new field();
						// position
						f.pos = count + k;
						//line = String.valueOf(f.pos);
						System.out.print(String.valueOf(count+k));
						
						
						// word
						f.word = words[k]; 
						//line = line.concat(" " +f.word+" ");
						System.out.println(" " +words[k]+" ");
						
						// stem
						f.stem = tools.stem(words[k]);
						//line = line.concat(f.stem+" ");
						//System.out.print(tools.stem(words[k])+" ");
						
						// spkear
						f.speaker = tools.stem(name.trim());
						//line = line.concat(f.speaker+" ");
						//System.out.print(tools.stem(name.trim())+" ");
						
						// synonyms
						//ArrayList<String> setsyns = tools.synonyms(words[k],tags[k]);
						ArrayList<String> setsyns = tools.synonyms(words[k],"NoPoS");
						for(int j=0;j<setsyns.size();j++)
						{
							setsyns.set(j, tools.stem(setsyns.get(j)));
							if(setsyns.get(j).compareTo(tools.stem(words[k])) !=0){
								//System.out.print(setsyns.get(j)+" ");
								f.synonyms.add(setsyns.get(j));
								//line = line.concat(f.synonyms.get(f.synonyms.size()-1)+" ");
							}						
						}
						
						//tagging
						f.tag = tags[k];
						
						//System.out.println(line);						
						listwords.add(f);
					}
				count = count + words.length;
			}	
			
						
			//Compute frequence of words
			int sum = 0;
			for(int k=0; k<listwords.size(); k++)
			{			
				int freq = 0;			
				ArrayList <Integer> fpos = new ArrayList<Integer>();
				for(int q = 0; q < listwords.size(); q++)
					if(listwords.get(k).stem.compareTo(listwords.get(q).stem)==0)
					{
						freq = freq + 1;
						fpos.add(q);						
					}
				
				field element = new field();
				element.frequence = freq;
				element.pos = listwords.get(k).pos;
				element.speaker = listwords.get(k).speaker;
				element.stem = listwords.get(k).stem;
				element.synonyms = listwords.get(k).synonyms;
				element.word = listwords.get(k).word;
				element.tag = listwords.get(k).tag;
				element.frePos = fpos;
				listwords.set(k, element);
				sum = sum + freq;
				//System.out.println(k+ " - " + listwords.get(k).word + " : " + listwords.get(k).frequence);
			}
			
			// compute number of different words
			boolean check[] = new boolean[listwords.size()];
			for(int k=0; k<listwords.size();k++) check[k]=true;
			
			
			for(int k=0; k<listwords.size();k++)
				for(int kk=k+1; kk<listwords.size();kk++){
					if(check[kk] && listwords.get(kk).stem.compareTo(listwords.get(k).stem)==0)
						check[kk]=false;
				}
			
			ArrayList<String> aa = new ArrayList<String>();
			ArrayList<Integer> bb = new ArrayList<Integer>();
			WordDiff = 0;
			for(int k=0;k<listwords.size();k++)
				if(check[k])
				{
					WordDiff ++;
					int index = 0;
					while(bb.size() > index && bb.get(index)<listwords.get(k).frequence) index++;
					aa.add(index, listwords.get(k).word);
					bb.add(index, listwords.get(k).frequence);
				}
			
			//System.out.println("Before removing stop-words: Number of words in transcript = " + listwords.get(listwords.size()-1).pos);
			//System.out.println("After removing stop-words:");
			System.out.println("      + Number of words in transcript= " + listwords.size());
			System.out.println("      + Number of different words in transcript = " + WordDiff);
			
			
			// Save to file
			file.deletefile(this.filename);
			
			file.append(String.valueOf(listwords.size()),filename);
			file.append(String.valueOf(WordDiff),filename);

			// position | word | stem | speaker | frequence | parts-of-speech | synonyms
			 
			 for(int k=0; k<listwords.size(); k++) {
				 String line =listwords.get(k).pos + " " +
						     listwords.get(k).word + " " +
						     listwords.get(k).stem + " " +
						     listwords.get(k).speaker + " "+
						     listwords.get(k).tag + " " +
						     listwords.get(k).frequence+" ";
				 
			     for(int f=0;f<listwords.get(k).frePos.size();f++)
			     {
			    	 line = line.concat(Integer.toString(listwords.get(k).frePos.get(f))+" ");			    	 
			     }
			     
				 for(int j=0; j<listwords.get(k).synonyms.size(); j++)
					 line = line.concat(listwords.get(k).synonyms.get(j)+" ");
				file.append(line, filename);
	
		
			 }
			
			
			//for(int k=0;k<wordcount;k++){
			//	System.out.println((k+1)+") " + aa.get(k) + " :" + bb.get(k));
			//}
	}

	/*******************************************************/
	public void create_for_handing() throws Exception{
		// Procedure: Analyse the transcript
		stop_words stopWords = new stop_words(this.stopwordfile);
		
		ArrayList<String> transcript = file.readLines(this.filenameOriginal);
		ListIterator i = transcript.listIterator();			
		
		file.deletefile(this.filenameHandy);
		int count = 0;
		String line = null;
		while(i.hasNext()){
			String question = (String)i.next();
			question = question.substring(question.lastIndexOf("]")+2);
			
			String name = question.substring(0,question.indexOf(":"));
			question = question.substring(question.indexOf(":")+2);				
			
			question = question.replace("_", "").replace("^", "").replace("%", "");
			
			while(question.indexOf("'")>-1) question = tools.abbv(question);
			
			//Transcript done by hand -> speaker pos1 word1 pos2 word2
			line = name.concat(" ");
			String words[] = question.split(" ");
			for(int k=0; k<words.length;k++)
				if(!stopWords.isStopWord(words[k])){ //if it is not stop-word, add it

					// position
					line = line.concat(String.valueOf(count+k));
					System.out.print(String.valueOf(count+k));
					// word
					line = line.concat(" " +words[k]+" ");
					System.out.print(" " +words[k]+" ");
				}
			System.out.println();
			file.append(line, this.filenameHandy);
			count = count + words.length;
		}		
		
	}
	/*******************************************************/
	public void load(){
		ArrayList<String> transcript = file.readLines(this.filename);
		ListIterator<String> i = transcript.listIterator();
		String line1 = (String)i.next();
		String line2 = (String)i.next();
		WordNumber = Integer.parseInt(line1);
		WordDiff = Integer.parseInt(line2);
		// 1 welcome welcom sridhar 1 VB receiv 
		while(i.hasNext()){
			String line = (String)i.next();
			String[] words = line.trim().split(" ");
			field f = new field();
			
			f.pos = Integer.parseInt(words[0]);
			f.word = words[1]; 
			f.stem = words[2];
			f.speaker = words[3];
			f.tag = words[4];
			f.frequence = Integer.parseInt(words[5]);
			for(int j=6;j<6+f.frequence;j++)
				f.frePos.add(Integer.parseInt(words[j]));
						
			for(int j=6+f.frequence;j<words.length;j++) f.synonyms.add(words[j]);
			listwords.add(f);
			
			//System.out.println(line);
		}
	}

	/*******************************************************/
	public void println(){
		for(int i=0; i<this.listwords.size(); i++)
		{
			System.out.print(this.listwords.get(i).pos + " ");
			System.out.print(this.listwords.get(i).word + " ");
			System.out.print(this.listwords.get(i).stem + " ");
			System.out.print(this.listwords.get(i).speaker + " ");
			for(int j=0; j<this.listwords.get(i).synonyms.size(); j++){
				System.out.print(this.listwords.get(i).synonyms.get(j)+" ");
			}
			System.out.println();		
		}
	
	}
	/*******************************************************/
	public void println(int window_size, int pos){
		for(int i=pos; i<window_size+pos; i++){
			System.out.print(this.listwords.get(i).word + " ");
		}
		System.out.println();	
	}	
	/*******************************************************/
	public passage getPassage(int pos, int window_size,ArrayList<String> question){
		passage p = new passage();
		
		p.window_pos1 = pos;
		p.window_pos2 = pos + window_size;
						
		for(int i=0; i<question.size(); i++){  // each word in the question
			for(int j=pos; j<pos+window_size; j++){// each word in the window				
				
				if(this.listwords.get(j).stem.compareTo(question.get(i))==0 && 
						!p.isCommonWord(this.listwords.get(j).stem)){
					p.score = p.score + 1.0;
					p.addCommonWord(this.listwords.get(j).stem, 
									this.listwords.get(j).pos, 
									j, 
									1.0);				
				}
				else if(this.listwords.get(j).synonyms.contains(question.get(i)) && 
						!p.isCommonWord(question.get(i))){
					p.score = p.score + 0.5;
					p.addCommonWord(this.listwords.get(j).synonyms.get(
							this.listwords.get(j).synonyms.indexOf(question.get(i))), 
									this.listwords.get(j).pos, 
									j, 
									0.5);				
				}
				else if(this.listwords.get(j).speaker.compareTo(question.get(i))==0 && 
						!p.isCommonWord(this.listwords.get(j).speaker)){
					p.score = p.score + 2.0;
					p.addCommonWord(this.listwords.get(j).speaker, 
									this.listwords.get(j).pos, 
									j, 
									2.0);				
				}
				
			}
		}
		
		return p;
	}

	/*******************************************************/
	public void create_for_handing_summary(String filename) throws Exception{
//		if(filename.equals("IS1008c"))
//		{
			String[] id =       {"MIO086",  "FIE073",   "FIE038","MIE085"};
			String[] speaker  = {"sridhar", "christine","agnes", "ed"};
			
//		}
//		else
//		{
//			String[] id =           {"MIO046","MIO036","FIE038","MIO095"};
//			String[] speaker  =     {"denis", "andrei","agnes", "mirek"};
//		}
		stop_words stopWords = new stop_words(this.stopwordfile);
		
		ArrayList<String> transcript = file.readLines(this.filenameOriginal);
		ListIterator i = transcript.listIterator();			
		
		// Save to file
		System.out.println(this.filenameHandy);
		file.deletefile(this.filenameHandy);
		
		int count = 0;
		i.next(); // speaker	start	end	rank	text
		while(i.hasNext())
		{
			String line = (String)i.next(); //MIO086 	32.34	32.78	0.194980694980695	To do that
			line = line.trim();
			while(line.indexOf("  ")>-1) line = line.replaceAll("  ", " ");
			
			String name = line.substring(0,line.indexOf(" "));
			for(int iname=0;iname<4;iname++)
				name = name.replaceAll(id[iname], speaker[iname]);

			name = name.trim() + " ";
			
			line = line.substring(line.indexOf(".")+1);
			line = line.substring(line.indexOf(".")+1);
			line = line.substring(line.indexOf(".")+1);
			line = line.substring(line.indexOf(" ")+1);
			
			
			//System.out.println(line);
			
			line = line.replace("_", "").replace("^", "").replace("%", "");
			
			while(line.indexOf("'")>-1) line = tools.abbv(line);

			String words[] = line.split(" ");
		
			String[] tags = tools.tags(words);
			
			for(int k=0; k<words.length;k++)
				if(!stopWords.isStopWord(words[k])){ //if it is not stop-word, add it
					//field f = new field();
					// position
					//f.pos = count + k;
					//line = String.valueOf(f.pos);
					name = name.concat(String.valueOf(count+k));
					//System.out.print(String.valueOf(count+k));
					
					
					// word
					//f.word = words[k]; 
					//line = line.concat(" " +f.word+" ");
					name = name.concat(" " +words[k]+" ");
					//System.out.println(" " +words[k]+" ");
				}
				else
				{
					name = name.concat(words[k]+" ");
				}
			file.append(name, this.filenameHandy);
			count = count + words.length;
		}

		
	}
	/*******************************************************/
	public static void main(String[] args) throws Exception{
		String filename = "IB4010";
		//String filename = "IS1008c";
		System.out.println("Run on transcript.java at " + filename);
		
		//transcript Transcript = new transcript("transcrip_IS1008c_done.txt","IS1008c_transcript.txt","transcrip_IS1008c_done_by_hand.txt","stop-words.txt");
		
		//transcript Transcript = new transcript("transcrip_IB4010_done.txt","IB4010_transcript.txt","transcrip_IB4010_done_by_hand.txt","stop-words.txt");		
		
		transcript Transcript = new transcript("transcrip_"+filename+"_summary_done_050_inverse.txt",filename+".extream.txt","transcrip_"+filename+"_summary_done_by_hand_005.txt","stop-words.txt");
		//transcript Transcript = new transcript("transcrip_IB4010_summary_done.txt","IB4010.extream.txt","transcrip_IB4010_summary_done_by_hand.txt","stop-words.txt");

	    timer Timer = new timer();
	    Timer.start();
	    
		//Transcript.create();
		
		
		Transcript.create_summary(filename,0.50);
		
		
		//Transcript.create_for_handing_summary(filename);
	    Timer.end();
	    System.out.println("\n" + Timer.duration() + " ms");		


	}
}
