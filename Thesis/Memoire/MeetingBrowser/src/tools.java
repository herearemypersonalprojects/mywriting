/*
 * Weâ€™re going to access Wordnet via RiTa Wordnet by Daniel Howe.
 * The RiTa Wordnet download comes with the Wordnet dictionary itself
 * so all you need is to add two JAR files to your Eclipse build path: 
 * ritaWN.jar, supportWN.jar. Both of these JARS are found in the download.  
 */
  
import java.util.ArrayList;

import qtag.AmbiguityClass;
import qtag.Tagger;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;


public class tools {
	
	/*******************************************************/
	// QTAG is a probabilistic parts-of-speech tagger
	public static String[] tags(ArrayList<String> token) throws Exception{
		
		String[] result = new String[token.size()];
		
		Tagger t = new Tagger("qtag-eng");
		AmbiguityClass at[] = t.tagComplete(token);		
		
		for(int i=0; i<at.length;i++){			
			int index = 0;
			int maxFreq = at[i].getFreq(index);
			for(int j=0; j<at[i].size();j++)
				//System.out.print(" " + at[i].getTag(j)+" " + at[i].getFreq(j)+ " " + at[i].getProbability(j));
				if(maxFreq < at[i].getFreq(j)){
					maxFreq = at[i].getFreq(j);
					index = j;
				}
			//System.out.print(to[i]+":" + at[i].getTag(index)+ " ");
			result[i] = at[i].getTag(index);
		}
				
		return result;
	}
	
	public static String[] tags(String[] token) throws Exception{
		
		String[] result = new String[token.length];
		
		Tagger t = new Tagger("qtag-eng");
		AmbiguityClass at[] = t.tagComplete(token);		
		
		for(int i=0; i<at.length;i++){			
			int index = 0;
			int maxFreq = at[i].getFreq(index);
			for(int j=0; j<at[i].size();j++)
				//System.out.print(" " + at[i].getTag(j)+" " + at[i].getFreq(j)+ " " + at[i].getProbability(j));
				if(maxFreq < at[i].getFreq(j)){
					maxFreq = at[i].getFreq(j);
					index = j;
				}
			//System.out.print(to[i]+":" + at[i].getTag(index)+ " ");
			result[i] = at[i].getTag(index);
		}
				
		return result;
	}	
	/*******************************************************/
	/*
	 * removing characters not related to words (punctuation signs such as
	 * comma, dot, quotation mark, semicolon or exclamation,...
	 * Special characters  	Display
	 * \' 	Single quotation mark
	 * \" 	Double quotation mark
	 * \\ 	Backslash
	 * \t 	Tab
	 * \b 	Backspace
	 * \r 	Carriage return
	 * \f 	Formfeed
	 * \n 	Newline
	 */
	public static String removing_punctuation(String line){
		line = line.replace(".", "").replace(",", "").replace("(", "").replace(")", "");		
		line = line.replace("_", "").replace("^", "").replace("%", "").replace("!", "");
		line = line.replace("'", "").replace(";","").replace(":","").replace("...","");
		line = line.replace("???","").replace("?", "").replace("\'", "").replace("\"", "");
		
		return line.replace("#","").toLowerCase();
	}
	/*******************************************************/
	// Stemming and adding synonym words
	public static String stem(String word) throws Exception{
		//if(word.trim().length()==0 || Character.isUpperCase(word.charAt(0))) return word;
		
		Class<?> stemClass = Class.forName("englishStemmer");
		SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();
		stemmer.setCurrent(word);
		stemmer.stem();
		word = stemmer.getCurrent();
		return word;
	}

	/*******************************************************/
	public static ArrayList<String> lemmas(String word){	
		ArrayList<String> setlems = new ArrayList<String>();
        String javaHome = System.getProperty("user.dir");
        javaHome = javaHome.concat("/dict");
        //System.out.println("javaHome = " + javaHome);
		System.setProperty ("wordnet.database.dir", javaHome);
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		
		String[] words = database.getBaseFormCandidates(word, SynsetType.ADJECTIVE);
		for(int i = 0; i< words.length; i++){
			//System.out.println(words[i]);
			setlems.add(words[i]);
		}		
		
		words = database.getBaseFormCandidates(word, SynsetType.ADVERB);
		for(int i = 0; i< words.length; i++){
			//System.out.println(words[i]);
			setlems.add(words[i]);
		}		

		words = database.getBaseFormCandidates(word, SynsetType.NOUN);
		for(int i = 0; i< words.length; i++){
			//System.out.println(words[i]);
			setlems.add(words[i]);
		}		

		words = database.getBaseFormCandidates(word, SynsetType.VERB);
		for(int i = 0; i< words.length; i++){
			//System.out.println(words[i]);
			setlems.add(words[i]);
		}		
		
		return setlems;
	}
	/*******************************************************/
	public static ArrayList<String> synonyms(String word, String type){		
		
		ArrayList<String> setsyns = new ArrayList<String>();
		//if(word.trim().length()==0 || Character.isUpperCase(word.charAt(0))) {
		//	return setsyns;
		//}
		// Get the synsets containing the word form
        String javaHome = System.getProperty("user.dir");
        javaHome = javaHome.concat("/dict");
        //System.out.println("javaHome = " + javaHome);
		System.setProperty ("wordnet.database.dir", javaHome);
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		
		 
		
		Synset[] synonyms = null;
		// JJ JJR JJS
		if(type.indexOf("JJ")>-1)
		synonyms = database.getSynsets(word, SynsetType.ADJECTIVE);
		else if(type.indexOf("NN")>-1)
		//	NN NNS
		synonyms = database.getSynsets(word, SynsetType.NOUN);
		else if(type.indexOf("VB")>-1)
		//VB VBD VBG VBN VBZ
		synonyms = database.getSynsets(word, SynsetType.VERB);
		else if(type.indexOf("RB")>-1)
		// RB RBR RBS
		synonyms = database.getSynsets(word, SynsetType.ADVERB);
		else
			synonyms = database.getSynsets(word);
		
		for(int i = 0; i<synonyms.length; i++){
			String[]  wordForms = synonyms[i].getWordForms();			
			
			for(int j = 0; j < wordForms.length; j++){
				
				//System.out.println(wordForms[j] + setsyns.size());
				//setsyns.add(wordForms[j]);
				if(wordForms[j].split(" ").length>1) continue; //remove combined-word whose length >= 2 words
				
				if(setsyns.size()>0)
				{
					int k;
					for(k =0; k<setsyns.size();k++)
						if(setsyns.get(k).compareTo(wordForms[j]) == 0){
							break;							
						}
					if(k == setsyns.size())
						setsyns.add(wordForms[j]);
				}
				else
					setsyns.add(wordForms[j]);
			}
		}		
		return setsyns;
	}
	
	/*******************************************************/
	public static String abbv(String sentence){
		/*
		 * word's = word is
		 * word'll = word will
		 * word've = word have
		 * wordn't = word not		 * 
		 * i'm = i am
		 * word're = word are
		 * word'd = word would
		 */ 
		if(sentence.indexOf("'s") > -1) sentence = sentence.replace("'s", "");
		if(sentence.indexOf("'ll") > -1) sentence = sentence.replace("'ll", " will");
		if(sentence.indexOf("'ve") > -1) sentence = sentence.replace("'ve", " have");
		if(sentence.indexOf("n't") > -1) sentence = sentence.replace("n't", " not");
		if(sentence.indexOf("'m") > -1) sentence = sentence.replace("'m", "");
		if(sentence.indexOf("'re") > -1) sentence = sentence.replace("'re", "");
		if(sentence.indexOf("'d") > -1) sentence = sentence.replace("'d", " would");
		
		if(sentence.indexOf("'") > -1) sentence = sentence.replace("'", "");
		return sentence.trim();
	}
	
	/*******************************************************/
	// Remove stop words
	public static String remove_StopWords(String word, String StopWords){
		//if(word.trim().length()==0 || Character.isUpperCase(word.charAt(0))) return word;
		
		if(StopWords.indexOf(" ".concat(word).trim().toLowerCase().concat(" "))==-1)
			return word;
		else
			return "";
	}
	
	/*******************************************************/
	public static String removeStopWords(String sentence, String filename){
		stop_words stopWords = new stop_words(filename);
		String words[] = sentence.split(" ");
		sentence = "";
		for(int j=0; j<words.length;j++){
			if(!stopWords.isStopWord(words[j])){
				sentence = sentence.concat(words[j]).concat(" ");
			}
		}
		return sentence.trim();
	}

	/*******************************************************/
	// Convert number into words in both question and transcript
	public static String convertNumeric(String word){
		if(word.compareTo("2nd")==0) return "second";
		if(word.compareTo("1st")==0) return "first";
		if(word.compareTo("3rd")==0) return "third";
		if(word.compareTo("5th")==0) return "fifth";
		if(word.compareTo("9th")==0) return "ninth";
		if(word.compareTo("12th")==0) return "twelfth";
				
	
		EnglishDecimalFormat f = new EnglishDecimalFormat();
		if(word.trim().length()>0)
		if(Character.isDigit(word.charAt(0))){
			int j=0;
			int k=0;
			String temp = "";
			while(j<word.length())
			{
				k=j;
				while(j <word.length() && Character.isDigit(word.charAt(j))){
					//System.out.println("\n"+j+words[i].charAt(j));
					j++;
				}			
				if(k<j)
				{
				//System.out.println("BAB = " + words[i].substring(k,j) + " = BAB");
				temp = temp.concat(f.convert(Integer.parseInt(word.substring(k,j)))).concat(" ");
				
				}
			j++;
			}
			/* already done by stem
			if(word.indexOf("s")>0){
				temp = temp.replace("y", "ies");
			}
			*/	
				
			if(word.indexOf("th")>0)
				word = temp.trim().concat("th");
			else
				word = temp.trim();
		}	
		
		return word;
	}
	/*******************************************************/
	public static String ConvertNumeric(String sentence){
		String words[] = sentence.split(" ");
		String temp = "";
		for(int j=0; j<words.length;j++){
			temp = temp.concat(tools.convertNumeric(words[j].trim())).concat(" ");
		}		
		return temp;
	}
	/*******************************************************/
	public static ArrayList<String> ConvertNumeric(ArrayList<String> input){
		for(int i=0; i<input.size();i++){
			String words[] = input.get(i).split(" ");
			String temp = "";
			for(int j=0; j<words.length;j++){
				temp = temp.concat(tools.convertNumeric(words[j].trim())).concat(" ");
			}
			
			input.set(i, temp);
		}
		return input;
	}
	
	/*******************************************************/
	public static String removeWords(String s){
		String element = s;
		element = element.toLowerCase();
		element = element.replace("hmm", "");
		element = element.replace("$", "");
		element = element.replace("mm", "");
		element = element.replace("hm", "");
		element = element.replace("um", "");			
		element = element.replace("@", "");
		element = element.replace("-", "");
		element = element.replace("*", "");
		element = element.replace("#", "");		
		//element = element.replace(":", "");
		element = element.replace("?", "");
		element = element.replace(".", "");
		element = element.replace(",", "");
		
		
		element = element.replace("okay", "ok");
		element = element.replace("yep", "yes");
		element = element.replace("yeah", "yes");
		
		while(element.indexOf("  ") > -1)
			element = element.replace("  ", " ");
	
		return element.trim();
	}
	
	/*******************************************************/
}
