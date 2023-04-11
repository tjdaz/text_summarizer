import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class IndexTest {
    public static void main(String[] args) {
        try {
            // Writes all the index to files, only need to do this once.
            Index.buildIndex(Index.TEST_TRAIN_INDEX_PATH, Index.TEST_TRAIN_DATA_PATH);
            Index.buildIndex(Index.TEST_TEST_INDEX_PATH, Index.TEST_TEST_DATA_PATH);
            Index.buildIndex(Index.TEST_VAL_INDEX_PATH, Index.TEST_VAL_DATA_PATH);

            // Open and search the test index for the test training data (small dataset).
            FSDirectory directory = FSDirectory.open(new File(Index.TEST_TRAIN_INDEX_PATH).toPath());
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(reader);

            // Return all the documents in the index to check output (not tokenized yet).
            MatchAllDocsQuery query = new MatchAllDocsQuery();
            TopDocs topDocs = indexSearcher.search(query, reader.maxDoc());

            // Print the fields of each document
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = reader.document(scoreDoc.doc);
                System.out.println("ID: " + doc.get("id"));
                System.out.println("Content: " + doc.get("article"));
                System.out.println("Summary: " + doc.get("summary"));
                System.out.println("------------------------------");
            }
            // Close the reader and directory.
            reader.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
