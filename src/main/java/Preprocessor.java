import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Preprocessor {

    public static List<String> preprocess(String text, Analyzer analyzer) throws IOException {
        List<String> tokens = new ArrayList<>();
        StringReader reader = new StringReader(text);
        try (TokenStream stream = analyzer.tokenStream("", reader)) {
            CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while (stream.incrementToken()) {
                String token = term.toString();
                tokens.add(token);
            }
            stream.end();
        }
        return tokens;
    }

    public static List<String> preprocess(String text) throws IOException {
        Analyzer analyzer = new EnglishAnalyzer();
        return preprocess(text, analyzer);
    }
}
