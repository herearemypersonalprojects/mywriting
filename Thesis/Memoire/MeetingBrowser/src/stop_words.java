
public class stop_words {
	String list_words;
	
	public stop_words(String filename){
		list_words = file.StopWords(filename);
	}
	
	boolean isStopWord(String word){
		if(list_words.indexOf(" ".concat(word).trim().toLowerCase().concat(" "))==-1 && word.trim().length()>1)
			return false;
		else
			return true;
	}
	
	void removeStopWords(String text){
		String words[] = text.split(" "); // text.split("\\b");
		String temp = "";
		for(int i=0; i< words.length; i++){
			if(!isStopWord(words[i])){
				temp = temp.concat(words[i]).concat(" ");
			}
		}
		
	}
}
