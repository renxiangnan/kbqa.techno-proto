package nlp.datastructure;

import nlp.utils.StanfordCoreNLP;
import queryanswering.Query;

import java.util.HashMap;
import java.util.Map;

public class Sentence {
    public String plainText;
    public Word[] words;
    public Map<String, Word> wordMap;

    public Sentence(String plainText,
                    StanfordCoreNLP stanfordCoreNLP ) {
        this.plainText = plainText;
        words = stanfordCoreNLP.getTaggedWords(plainText);
        this.wordMap = new HashMap<>();

        for (Word word: words) {
            wordMap.put(word.getKey(), word);
        }
    }

    public Sentence(Query query, String plainText) {

    }

    public Word getWordByIndex(int ind) {

        return words[ind - 1];
    }

    public boolean hasModifier(Word input) {
        for (Word word: words) {
            if (!word.equals(input) && word.getModifiedWord().equals(input)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Word word: words) {
            stringBuilder.
                    append(word).
                    append(" ").
                    append("ner=").
                    append(word.getNer());
        }

        return stringBuilder.toString();
    }

}
