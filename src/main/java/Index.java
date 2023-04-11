import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Index {
    // Paths for actual data and indexes.
    public static final String DATA_PATH = "data/raw";
    public static final String INDEX_PATH = "data/index";

    // Data paths for testing index on small dataset.
    public static final String TEST_DATA_PATH = "data/test_raw";
    public static final String TEST_TRAIN_DATA_PATH = TEST_DATA_PATH + "/train";
    public static final String TEST_TEST_DATA_PATH = TEST_DATA_PATH + "/test";
    public static final String TEST_VAL_DATA_PATH = TEST_DATA_PATH + "/val";

    // Index paths for testing index on small dataset.
    public static final String TEST_INDEX_PATH = "data/test_index";
    public static final String TEST_TRAIN_INDEX_PATH = TEST_INDEX_PATH + "/train";
    public static final String TEST_TEST_INDEX_PATH = TEST_INDEX_PATH + "/test";
    public static final String TEST_VAL_INDEX_PATH = TEST_INDEX_PATH + "/val";

    // Builds an index from the files at the given path.
    public static void buildIndex(String indexPath, String articlePath) throws IOException {
        // Open the directory we will write the index to.
        Directory indexDir = FSDirectory.open(new File(indexPath).toPath());

        // Create and configure the IndexWriter instance.
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // Creates a new index file.
        IndexWriter writer = new IndexWriter(indexDir, config);

        // Store each file in the folder in an array.
        File folder = new File(articlePath);
        File[] files = folder.listFiles();

        // Go through each file in the training directory.
        for (File file : files) {
            // Get the "id" field. The id is the filename without the file extension.
            String fname = file.getName();
            String id = fname.substring(0, fname.lastIndexOf('.'));

            // Read the file into a String.
            String text = getFileString(file);

            // Split the article text and the summary into two strings.
            String[] textSplit = text.split("@highlight", 2);

            // Content of "article" field. The article is all text before @highlight.
            String article = textSplit[0].replaceAll("(?i)^.*?\\(CNN\\)[\\s-]*", "");
            // todo: write something to remove the contributers at bottom of some articles.

            // Content of "summary" field. The summary is all text under @highlight.
            // Removes @highlight annotation, NEW: tag, and extra blank lines.
            String summary = textSplit[1].replaceFirst("\\R+", "");
            summary = summary.replaceAll("@highlight", "");
            summary = summary.replaceAll("\\R+", ". ");
            summary = summary.replaceAll("NEW: ", "");

            // Adds the document to the index.
            addDoc(writer, id, article, summary);
        }
        writer.close();
    }

    // Adds a single document with id, article, and summary fields to the index.
    // Haven't implemented tokenization yet.
    private static void addDoc(IndexWriter w, String id, String article, String summary) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("id", id, Field.Store.YES));
        doc.add(new TextField("article", article, Field.Store.YES));
        doc.add(new TextField("summary", summary, Field.Store.YES));
        w.addDocument(doc);
    }

    // Reads the file into a char[] array, then builds and returns it as a String.
    private static String getFileString(File file) throws IOException {
        FileReader reader = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        reader.read(chars);
        reader.close();
        return new String(chars);
    }

}
