import java.io.File;
import java.io.IOException;

public class TextSummarizer {
    public static void main(String[] args) {
        try {
            // Create File objects for the Lucene index directories.
            File trainDir = new File(Index.TRAIN_INDEX_PATH);
            File testDir = new File(Index.TEST_INDEX_PATH);
            File valDir = new File(Index.VAL_INDEX_PATH);

            // If the indexes are already created, these will not run.
            // Build a Lucene index for the training data if it does not exist.
            if (trainDir.list().length == 0) {
                Index.buildIndex(Index.TRAIN_INDEX_PATH, Index.TRAIN_DATA_PATH);
            }

            // Build a Lucene index for the test data if it does not exist.
            if (testDir.list().length == 0) {
                Index.buildIndex(Index.TEST_INDEX_PATH, Index.TEST_DATA_PATH);
            }

            // Build a Lucene index for the validation data if it does not exist.
            if (valDir.list().length == 0) {
                Index.buildIndex(Index.VAL_INDEX_PATH, Index.VAL_DATA_PATH);
            }
        }

        // Catch IOExceptions and print stack trace.
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
