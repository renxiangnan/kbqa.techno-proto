package nlp.utils;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import nlp.datastructure.Sentence;
import nlp.datastructure.Word;

import java.util.List;


/**
 * Basic Stanford Named Entity Recognizer
 */
public class NERecognizer {
    private AbstractSequenceClassifier<CoreLabel> classifier;

    public NERecognizer(String classifierModelPath) {
        this.classifier = CRFClassifier.
                getClassifierNoExceptions(classifierModelPath);
    }

    public void recognize(Sentence sentence) {
        List<CoreLabel> coreLabels = classifier.classify(sentence.plainText).get(0);

        for (CoreLabel core : coreLabels) {
            int position = Integer.parseInt(
                    core.get(CoreAnnotations.PositionAnnotation.class)) + 1;

            Word word = sentence.getWordByIndex(position);
            String ner = core.get(CoreAnnotations.AnswerAnnotation.class);
            if (ner.equals("O")) {
                word.setNer(null);
            } else {
                word.setNer(ner);
            }
        }
    }


}
