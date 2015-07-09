import java.io.IOException;

import qtag.Tagger;
import qtag.AmbiguityClass;

public class QTag {

	/**
	 * @param args
	 */
	public static void main(String[] args)  throws IOException {
		// TODO Auto-generated method stub
		Tagger t = new Tagger("qtag-eng");
		String phrase = "actually tried propose just one movie some alternates";

		//String phrase = "I love you more                 than I can    say";
		String[] parts = phrase.split("[\b\t\n\r ]+");

		String[] tparts = t.tag(parts);
		
		String[] to =  t.getTokenArray();
		
		for(int i=0; i<tparts.length;i++)
		{
			System.out.print(to[i	]+":"+tparts[i]+" ");
		}
		
		AmbiguityClass at[] = t.tagComplete(parts);
		System.out.println("\n");
		for(int i=0; i<at.length;i++){			
			int index = 0;
			int maxFreq = at[i].getFreq(index);
			for(int j=0; j<at[i].size();j++)
				//System.out.print(" " + at[i].getTag(j)+" " + at[i].getFreq(j)+ " " + at[i].getProbability(j));
				if(maxFreq < at[i].getFreq(j)){
					maxFreq = at[i].getFreq(j);
					index = j;
				}
			System.out.print(to[i]+":" + at[i].getTag(index)+ " ");
		}
		
		
	}

}
