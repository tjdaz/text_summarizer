import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 * Run TextSummarizer.java first, the Lucene indexes must be built first.
 * Gets the total document counts and prints random docs from each index.
 * Run this to make sure your Lucene indexes were built correctly.
 * There should be 92604 total docs, which took ~15 minutes to build in TextSummarizer.
 */

public class IndexTest {
    public static void main(String[] args) {
        try {
            // Open the /train, /test, and /val directories with Lucene.
            FSDirectory trainDir = FSDirectory.open(new File(Index.TRAIN_INDEX_PATH).toPath());
            FSDirectory testDir = FSDirectory.open(new File(Index.TEST_INDEX_PATH).toPath());
            FSDirectory valDir = FSDirectory.open(new File(Index.VAL_INDEX_PATH).toPath());

            // Open a reader for each directory.
            IndexReader trainReader = DirectoryReader.open(trainDir);
            IndexReader testReader = DirectoryReader.open(testDir);
            IndexReader valReader = DirectoryReader.open(valDir);

            // Open a searcher for each directory.
            IndexSearcher trainSearcher = new IndexSearcher(trainReader);
            IndexSearcher testSearcher = new IndexSearcher(testReader);
            IndexSearcher valSearcher = new IndexSearcher(valReader);

            // Return all the documents in the index to check output (not tokenized yet).
            MatchAllDocsQuery query = new MatchAllDocsQuery();

            // Get all train, test, and val docs.
            TopDocs trainTopDocs = trainSearcher.search(query, trainReader.maxDoc());
            TopDocs testTopDocs = testSearcher.search(query, testReader.maxDoc());
            TopDocs valTopDocs = valSearcher.search(query, valReader.maxDoc());

            // Get the number of training docs, add it to total docs.
            int trainDocsCount = trainTopDocs.scoreDocs.length;
            int testDocsCount = testTopDocs.scoreDocs.length;
            int valDocsCount = valTopDocs.scoreDocs.length;
            int totalDocsCount = trainDocsCount + testDocsCount + valDocsCount;

            // Print data about number of Documents in the index.
            System.out.println("*****************************************************");
            System.out.println("Number of total docs: " + totalDocsCount);
            System.out.println("Number of train docs: " + trainDocsCount);
            System.out.println("Number of test docs: " + testDocsCount);
            System.out.println("Number of val docs: " + valDocsCount);
            System.out.println("*****************************************************");

            // Array of TopDocs from all indexes for looping.
            TopDocs[] topDocs = { trainTopDocs, testTopDocs, valTopDocs };
            IndexSearcher[] searcher = { trainSearcher, testSearcher, valSearcher };

            // Number of random documents to display.
            int randDocCount = 3;

            // Test: Get three random articles from train index.
            for (int i = 0; i < topDocs.length; i++) {
                for (int j = 0; j < randDocCount; j++) {
                    Random rand = new Random();
                    int randomNum = rand.nextInt(topDocs[j].scoreDocs.length);
                    Document doc = searcher[j].doc(topDocs[j].scoreDocs[randomNum].doc);
                    System.out.println("ID: " + doc.get("id"));
                    System.out.println("Content: " + doc.get("article"));
                    System.out.println("Summary: " + doc.get("summary"));
                    System.out.println("------------------------------");
                }
            }

            // Close the readers and directories.
            trainReader.close();
            testReader.close();
            valReader.close();
            trainDir.close();
            testDir.close();
            valDir.close();

            // Catch all IOExceptoins and print stack trace.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
