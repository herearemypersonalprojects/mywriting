import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;


public class lucene {
	Directory index;
	IndexWriter w;
	IndexSearcher s;
	Hits hits;
	Query q;
	
	
	public void initData(ArrayList<String> data){
	    // 1. create the index
		index = new RAMDirectory();
		try {
			w = new IndexWriter(index, new StandardAnalyzer(), true);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0; i < data.size(); i++){
			try {
				addDoc(w, data.get(i));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    try {
			w.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public ArrayList<String> run(String querystr) throws Exception{
	    // 2. query
	    Query q = new QueryParser("title", new StandardAnalyzer()).parse(querystr);

	    // 3. search
	    s = new IndexSearcher(index);
	    hits = s.search(q);

	    // 4. display results
	    //System.out.println("Found " + hits.length() + " hits.");
	    ArrayList<String> result = new ArrayList<String>();
	    for(int i=0;i<hits.length();++i) {
	      //System.out.println((i + 1) + ". " + hits.doc(i).get("title"));
	    	result.add(hits.doc(i).get("title"));
	    }
	    s.close();
		
	    return result;
	}
	private static void addDoc(IndexWriter w, String value) throws IOException {
		  Document doc = new Document();
		  doc.add(new Field("title", value, Field.Store.YES,   Field.Index.TOKENIZED));
		  w.addDocument(doc);
		}
}
