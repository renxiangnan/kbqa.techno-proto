package nlp.utils;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import common.io.NLPUtilsLoadPath;

import java.io.StringReader;
import java.util.List;


/**
 *  Stanford parser is deactivated, since it performs badly in practice
 */
public final class StanfordParser {
    private LexicalizedParser lexicalizedParser;
    private TokenizerFactory<CoreLabel> tokenizerFactory;
    private TreebankLanguagePack treebankLanguagePack;
    private GrammaticalStructureFactory structureFactory;

    public StanfordParser() {
        lexicalizedParser = LexicalizedParser.loadModel(NLPUtilsLoadPath.stanfordPCFGModelPath);
        tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        treebankLanguagePack = new PennTreebankLanguagePack();
        structureFactory = treebankLanguagePack.grammaticalStructureFactory();
    }

    public GrammaticalStructure getGrammaticalStructure(String sentence) {
        List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(sentence)).tokenize();

        return structureFactory.
                newGrammaticalStructure(lexicalizedParser.apply(rawWords));
    }


}
