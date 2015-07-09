import java.util.ArrayList;

import edu.smu.tspell.wordnet.*;



/**
 * 
 * @author qle
 *
 * Display word forms and definitions for synsets containing the word form
 * specified on the command line. To use this application, specify the word
 * form that you wish to view synsets for, as in the following example which
 * displays all synsets containing the word form "airplane":
 *  
 */

public class wordnet {
	/*******************************************************/
	public static void testJWNL(){ //Java WordNet Library
		
	}
	/*******************************************************/
	public static void test(){
		
		String word = "wishes";

		// Get the synsets containing the word form
		//System.setProperty ("wordnet.database.dir", "C:\\Workspace\\MeetingBrowser\\dict");


		// Get the synsets containing the word form		
        String javaHome = System.getProperty("user.dir");
        javaHome = javaHome.concat("/dict");
        System.out.println("javaHome = " + javaHome);
		System.setProperty ("wordnet.database.dir", javaHome);

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		//Synset[] nounFlies = database.getSynsets(wordForm, SynsetType.NOUN);
		
		
		Synset[] nounFlies = database.getSynsets(word);
		
				
		String[] words = database.getBaseFormCandidates(word, SynsetType.ADJECTIVE);
		for(int i = 0; i< words.length; i++){
			System.out.println(words[i]);			
		}		
		
		words = database.getBaseFormCandidates(word, SynsetType.ADVERB);
		for(int i = 0; i< words.length; i++){
			System.out.println(words[i]);			
		}		

		words = database.getBaseFormCandidates(word, SynsetType.NOUN);
		for(int i = 0; i< words.length; i++){
			System.out.println(words[i]);			
		}		

		words = database.getBaseFormCandidates(word, SynsetType.VERB);
		for(int i = 0; i< words.length; i++){
			System.out.println(words[i]);			
		}		
		
		
		// Display the word forms and definitions for synsets retrieved
		if(nounFlies.length > 0){
			System.out.println("The following synsets contain '" + 
								word + "' or a possible base form " +
								" of that text:");
			for(int i = 0; i < nounFlies.length; i ++){
				System.out.println("");
				String[]  wordForms = nounFlies[i].getWordForms();
				for(int j = 0; j < wordForms.length; j++){
					System.out.print((j > 0 ? ",": "") + wordForms[j]);
				}
				System.out.println(": " + nounFlies[i].getDefinition());
			}
		}
		else
		{
			System.err.println("No synsets exist that contain " +
								"the word form '" + word +"'");
		}
		
	}
	/*******************************************************/
	public static void main(String[] args) throws Exception{
		//test();
		//psychopaths
		//psychopathic
		System.out.println("SnowballStemmer");
		System.out.println(tools.stem("gave"));
		System.out.println(tools.stem("giving"));
		
		System.out.println("\nWordNet lemma");
		ArrayList<String> setlems = tools.lemmas(tools.stem("allow"));
		for(int i=0;i<setlems.size();i++)
			System.out.print(setlems.get(i)+ " ");
		
		System.out.println();
		ArrayList<String> setlems1 = tools.synonyms("did","LQA");
		for(int i=0;i<setlems1.size();i++)
			System.out.print(setlems1.get(i)+ " ");
		
		
		
	}
}
