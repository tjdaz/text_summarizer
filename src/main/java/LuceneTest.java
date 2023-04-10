import java.io.IOException;

import org.apache.lucene.util.Version;

import java.util.List;

public class LuceneTest {
	public static void printList(List<String> list) {
	    for (String element : list) {
	        System.out.println(element);
	    }
	}
	
	public static void main(String[] args) throws IOException {
	    // Check if Lucene is installed and print the version
	    String text = "This is the string that we are going to tokenize";
	    //String[] tokens = str.split(str);
	    List<String> processed = Preprocessor.preprocess(text);
	    printList(processed);
	}
}
