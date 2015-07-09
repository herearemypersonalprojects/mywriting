import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;


public class file {
	
	String filename = null;
	
	public file(String filename){
		this.filename = filename;
	}
	
	public void setFilename(String filename){
		this.filename = filename;
	}
	
	public static void deletefile(String filename) {
	    File file = new File(filename);
	    if(file.exists())
	    	file.delete();		
	}
	
	
	public static void append(String s, String filename){		

		BufferedWriter bw = null;
        try {        	        	
        	bw = new BufferedWriter(new FileWriter(filename, true));
        	bw.write(s);
        	bw.newLine();
        	bw.flush(); 
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        } finally {                       // always close the file
  	 if (bw != null) try {
  		 bw.close();
  	 } catch (IOException ioe2) {
  	    // just ignore it
  	 }
        } // end try/catch/finally
	}
	
	// read stop-words list
	public static String StopWords(String filename){
		String list = "";
		String s = null;
		try{
			BufferedReader f = new BufferedReader(new FileReader(filename));
			while((s = f.readLine()) != null){
				if(s.trim().length()>0){					
					list = list.concat(" ").concat(s);					
				}
			}
			f.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return list.concat(" ");	   
	}
	

	public static ArrayList<String> readTranscript(String filename){
		ArrayList<String> lines = new ArrayList<String>();
		String s;
		if(filename == null) return null;
		try{
			BufferedReader f = new BufferedReader(new FileReader(filename));
			while((s = f.readLine()) != null){
				if(s.trim().length()>0 && s.indexOf("Episode #")==-1){
					s = s.substring(7).trim();
					s = s.replace("$", "").replace(".", "").replace("-", "");
					s = s.replace(",", "").replace("#", "").replace("?", "");
					while(s.indexOf("  ") > -1) s = s.replace("  ", " ");
					lines.add(s);					
				}
			}
			f.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return lines;
	}
	
	//each line is contained into an element of array
	public static ArrayList<String> readLines(String filename){
		ArrayList<String> lines = new ArrayList<String>();
		String s;
		if(filename == null) return null;
		try{
			BufferedReader f = new BufferedReader(new FileReader(filename));
			while((s = f.readLine()) != null){
				if(s.trim().length()>0){					
					lines.add(s);					
				}
			}
			f.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return lines;
	}	
	// create transcript text from two file transcript_time and transcript_name
	public void mixTranscripts(String file_time, String file_name){
		ArrayList<String> transcript_time = this.readLines(file_time);
		ArrayList<String> transcript_name = this.readLines(file_name);
		
//		 standarisation transcript_time
		ListIterator<String> iterator = transcript_time.listIterator();		
		String element = null;
		 while (iterator.hasNext()) {			 
			element = iterator.next();
			element = tools.removeWords(element);
			iterator.set(element);			
		}
		
//		 standarisation transcript_name
		iterator = transcript_name.listIterator();
		while (iterator.hasNext()) {			 
			element = iterator.next();			
			element = tools.removeWords(element);			
			iterator.set(element);		
			//System.out.println(element);
		}		 
		
		this.deletefile(filename);
		
		String temp = null;
		String firstSentence_time = null;
		String firstSentence_name = null;
		String s = null;
		String name = null;
		String time1 = null;
		String time2 = null;
		String timesave = null;
		int pos = 0;
		
		for(int j = 0; j < transcript_name.size(); j++){
			if(transcript_name.get(j).indexOf("keywords")>-1) continue;
			
			int k = transcript_name.get(j).indexOf(" ",8);			
			if(k < 0) k = transcript_name.get(j).length();
			name = transcript_name.get(j).substring(8,k).replace(":", "");
				
			s = transcript_name.get(j).substring(8+name.length());

			firstSentence_name = s.trim();
			
			if(firstSentence_name.length() < 1 || name.length() < 1) continue;
					
			pos = 0;
				
			for(int i = 0; i < transcript_time.size(); i++){
				s = transcript_time.get(i);
				temp = s.substring(s.indexOf(']')+1,s.lastIndexOf('[')).trim();
				time1 = s.substring(s.indexOf('['), s.indexOf(']')+1);
				time2 = s.substring(s.lastIndexOf('['), s.lastIndexOf(']')+1);
				
				firstSentence_time = temp.trim(); 
				//System.out.println(firstSentence);
				
				if(firstSentence_time.length() < 1) continue;
								
				temp = name.concat(" : ").concat(firstSentence_name);
				
				if(firstSentence_time.indexOf(firstSentence_name) > -1){				
					temp = time1.concat(time2).concat(" ").concat(temp);
					s = s.replace(firstSentence_name, "");
					transcript_time.set(i, s);
					timesave = time1;
					//System.out.println("no =" + i + transcript_time.get(i)+ " : " + firstSentence_name);
					pos = 1;
					break;
				}
			}
				
				if(pos == 0)
					for(int i = 0; i < transcript_time.size(); i++){
						s = transcript_time.get(i);
						temp = s.substring(s.indexOf(']')+1,s.lastIndexOf('[')).trim();
						time1 = s.substring(s.indexOf('['), s.indexOf(']')+1);
						time2 = s.substring(s.lastIndexOf('['), s.lastIndexOf(']')+1);
						
						firstSentence_time = temp.trim(); 
						//System.out.println(firstSentence);
						
						if(firstSentence_time.length() < 1) continue;
										
						temp = name.concat(" : ").concat(firstSentence_name);
						
					if(transcript_name.get(j).indexOf(firstSentence_time)>-1){					
					temp = time1.concat(time2).concat(" ").concat(temp);
					
					s = s.replace(firstSentence_name, "");
					transcript_time.set(i, s);
					//transcript_time.remove(i);
					timesave = time1;
					//System.out.println("no =" + i + transcript_time.get(i)+ " : " + firstSentence_name);
					pos = 1; 
					break;					
				}		
			}
			
			s = temp;
			System.out.println(s);
			this.append(s,filename);
		}
		/*
		for(int i = 0; i < transcript_time.size(); i++){
			s = transcript_time.get(i);
			temp = s.substring(s.indexOf(']')+1,s.lastIndexOf('[')).trim();
			time1 = s.substring(s.indexOf('['), s.indexOf(']')+1);
			time2 = s.substring(s.lastIndexOf('['), s.lastIndexOf(']')+1);
			
			firstSentence_time = tools.firstSentence(temp).trim(); 
			//System.out.println(firstSentence);
			
			if(firstSentence_time.length() < 1) continue;
			
			for(int j = 0; j < transcript_name.size(); j++){
				int k = transcript_name.get(j).indexOf(" ",8);
				name = transcript_name.get(j).substring(8,k).replace(":", "");
					
				s = transcript_name.get(j).substring(8+name.length()+1);

				if(name.length() < 1) continue;
				
				firstSentence_name = s.trim(); 
				//this.append(firstSentence_name);
				if(firstSentence_time.indexOf(firstSentence_name) > -1){
					temp = name.concat(" : ").concat(temp);					
					transcript_name.remove(j);
					break;
				}else if(transcript_name.get(j).indexOf(firstSentence_time)>-1){
					transcript_name.set(j, transcript_name.get(j).replace(firstSentence_time, ""));
					temp = name.concat(" : ").concat(temp);				
					break;
					
				}
				
			}
			 
			s = time1.concat(time2).concat(" ").concat(temp);
			System.out.println(s);
			this.append(s);
			
		}
		*/
	}
	
	/*******************************************************/	
	// read questions
	public static ArrayList<String> readQuestions(String filename){
		ArrayList<String> result = new ArrayList<String>();
		String s;
		if(filename == null) return null;
		/*
1
Here
Denis was interested in whether participants have received an agenda for the meeting
Agnes was interested in whether participants have received an agenda for the meeting
8
21
denis  8 do 9 not 10 know 11 if 12 you 13 all 14 received 18 agenda 21 meeting 
---------------------------		 
		 */
		
		try{
			BufferedReader f = new BufferedReader(new FileReader(filename));
			String temp = null;
			while((s = f.readLine()) != null){ // 1				
				s = f.readLine(); // Around/Here..
				s = f.readLine(); // question 1
				result.add(s);
				s = f.readLine(); // question 2
				result.add(s);
				s = f.readLine(); // start of answering window
				result.add(s);
				s = f.readLine(); // end of answering window
				result.add(s);
				temp = "";
				while((s = f.readLine()) != null && s.compareTo("---------------------------")!=0){
					temp = temp.concat(s).concat("\n");
				}
				
				result.add(temp);
			}
			f.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return result;
	}
	/*******************************************************/
	//read all file to a string
	public String readTranscript(){		
		String s = null;
		String temp = null;
		String result = "";
		if(filename == null) return null;
		try{
			BufferedReader f = new BufferedReader(new FileReader(filename));
			while((s = f.readLine()) != null && s.trim().length()>0){				
				temp = s.substring(s.indexOf(']')+1,s.lastIndexOf('[')).trim();
				//System.out.println(s.indexOf(']')+1 + ":"+s.lastIndexOf('[')+":"+temp);
		        temp = temp.replace("$", "").replace("#", "").replace("@", "").replace("*","");
		        temp = temp.replace("%", "").replace("^","").replace("&","");
		        temp = temp.replace("?","").replace(",", "").replace(".", "").trim();
				if(temp.trim().length()>0){
					result = result.concat(" " + temp); //"\n" for break line 
				}
			}
			f.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}		
		
		return result;
	}
}
