package nlp.utils;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import nlp.datastructure.Word;
import org.apache.log4j.Logger;
import org.tartarus.snowball.ext.PorterStemmer;
import common.io.IOUtils;

import java.util.List;

public final class StanfordCoreNLP extends CoreNLPHelper {
    private PorterStemmer porterStemmer;

    public StanfordCoreNLP() {
        logger = Logger.getLogger(this.getClass());
        porterStemmer = new PorterStemmer();
    }

    public String lemmatizeByStandfordNLPCore(String input) {
        StringBuilder output = new StringBuilder();
        Annotation lemmatized = new Annotation(input);
        super.coreNLP.annotate(lemmatized);

        List<CoreMap> sentences = lemmatized.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String lemma = token.getString(CoreAnnotations.LemmaAnnotation.class);
                output.append(lemma).append(IOUtils.lineSeparator);
            }
        }

        return output.substring(0, output.length() - 1);
    }

    public String lemmatizeByPorter(String input) {
        porterStemmer.setCurrent(input);
        porterStemmer.stem();
        return porterStemmer.getCurrent();
    }

    public CoreMap getPOS(String input) {
        Annotation annotation = new Annotation(input);
        coreNLP.annotate(annotation);

        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        CoreMap posTag = null;

        try {
            posTag = sentences.get(0);
        } catch (Exception e) {
            logger.error("Get POS tag failed.");
            e.printStackTrace();
        }

        return posTag;
    }

    public Word[] getTaggedWords(String sentence) {
        CoreMap taggedSentence = getPOS(sentence);
        Word[] taggedWords = new Word[taggedSentence.get(CoreAnnotations.TokensAnnotation.class).size()];

        for (int i = 0; i < taggedSentence.get(CoreAnnotations.TokensAnnotation.class).size(); i ++) {
            CoreLabel token = taggedSentence.get(CoreAnnotations.TokensAnnotation.class).get(i);
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            taggedWords[i] = new Word(lemmatizeByStandfordNLPCore(word.toLowerCase()), word, pos, i + 1);
        }
        return taggedWords;
    }
}
