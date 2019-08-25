package nlp.utils;

import nlp.datastructure.Sentence;
import nlp.datastructure.Word;
import org.junit.Assert;
import org.junit.Test;
import common.io.NLPUtilsLoadPath;

import java.util.Arrays;

public class NERecognizerTest {
    private StanfordCoreNLP stanfordCoreNLP = new StanfordCoreNLP();
    private NERecognizer neRecognizer = new NERecognizer(NLPUtilsLoadPath.abstractSequenceClassifierPath);

    @Test
    public void recognizeTest() {
        String[] expectedNERRes1 = {
                null, null, null, null, null, "ORGANIZATION", "ORGANIZATION",
                null, null, null, null, null, "LOCATION", null};

        String[] expectedNERRes2 = {
                null, null, null, null, null, null, null, null,
                null, null, null, "LOCATION", "LOCATION", null};


        Sentence sentence1 = new Sentence(
                "I go to school at Stanford University, which is located in California.",
                stanfordCoreNLP);
        neRecognizer.recognize(sentence1);

        Sentence sentence2 = new Sentence(
                "I go to work at IIAI, which is located in Abu Dhabi.",
                stanfordCoreNLP);
        neRecognizer.recognize(sentence2);

        Assert.assertArrayEquals(expectedNERRes1, Arrays.stream(sentence1.words).map(Word::getNer).toArray(String[]::new));
        Assert.assertArrayEquals(expectedNERRes2, Arrays.stream(sentence2.words).map(Word::getNer).toArray(String[]::new));
    }


}
