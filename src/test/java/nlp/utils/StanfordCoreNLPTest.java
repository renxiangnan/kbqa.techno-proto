package nlp.utils;

import nlp.datastructure.Word;
import org.junit.Assert;
import org.junit.Test;

public class StanfordCoreNLPTest {

    private StanfordCoreNLP stanfordCoreNLP = new StanfordCoreNLP();

    private void lemmatizeByStandfordNLPCoreTest(String[] inputs, String[] expectedRes) {
        String[] res = new String[inputs.length];

        for (int i = 0; i < inputs.length; i++) {
            res[i] = stanfordCoreNLP.lemmatizeByStandfordNLPCore(inputs[i]);
        }

        Assert.assertArrayEquals(res, expectedRes);
    }

    private void lemmatizeByPorterTest(String[] inputs, String[] expectedRes) {
        String[] res = new String[inputs.length];

        for (int i = 0; i < inputs.length; i++) {
            res[i] = stanfordCoreNLP.lemmatizeByPorter(inputs[i]);
        }

        Assert.assertArrayEquals(res, expectedRes);
    }

    @Test
    public void lemmatizationTest() {
        String[] inputs = {"creating", "creation", "created", "creator", "creature"};
        String[] expectedStanfordNLPCoreRes = {"create", "creation", "create", "creator", "creature"};
        String[] expectedPorterRes = {"creat", "creation", "creat", "creator", "creatur"};

        lemmatizeByStandfordNLPCoreTest(inputs, expectedStanfordNLPCoreRes);
        lemmatizeByPorterTest(inputs, expectedPorterRes);
    }

    @Test
    public void getPOSTest() {
        String input = "I go to school at Stanford.";
        String expectedRes =
                "" +
                        "[Text=I go to school at Stanford. " +
                        "CharacterOffsetBegin=0 CharacterOffsetEnd=27 " +
                        "Tokens=[I-1, go-2, to-3, school-4, at-5, Stanford-6, .-7] " +
                        "SentenceIndex=0 TokenBegin=0 TokenEnd=7]" +
                        "";
        Assert.assertEquals(expectedRes, stanfordCoreNLP.getPOS(input).toShorterString());
    }

    @Test
    public void getTaggedWords() {
        String input = "I am going to school at Stanford.";
        Word[] expectedResult = {
                new Word("i", "I", "PRP", 1),
                new Word("am", "am", "VBP", 2),
                new Word("go", "going", "VBG", 3),
                new Word("to", "to", "TO", 4),
                new Word("school", "school", "NN", 5),
                new Word("at", "at", "IN", 6),
                new Word("stanford", "Stanford", "NNP", 7),
                new Word(".", ".", ".", 8),
        };

        Assert.assertArrayEquals(expectedResult, stanfordCoreNLP.getTaggedWords(input));
    }

}
