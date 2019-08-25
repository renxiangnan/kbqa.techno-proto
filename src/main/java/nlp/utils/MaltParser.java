package nlp.utils;

import nlp.datastructure.Sentence;
import nlp.datastructure.Word;
import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;
import org.maltparser.core.syntaxgraph.DependencyStructure;

public class MaltParser {
    public MaltParserService service;

    public MaltParser() {
        try {
            service = new MaltParserService();
            service.initializeParserModel("-c engmalt.linear-1.7.mco -m parse -w " +
                    "/Users/xiangnanren/IDEAWorkspace/kbqa.techno-proto/src/main/resources -lfi parser.log");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DependencyStructure getDependencyStructure(Sentence sentence) {
        try {
            return service.parse(getTaggedTokens(sentence));
        } catch (MaltChainedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] getTaggedTokens (Sentence sentence) {
        String[] taggedTokens = new String[sentence.words.length];

        for (int i = 0; i < sentence.words.length; i ++) {
            Word currWord = sentence.words[i];
            taggedTokens[i] = currWord.getPosition() + "\t" +
                    currWord.getOriginalForm() + "\t_\t" +
                    currWord.getPosTag() + "\t" +
                    currWord.getPosTag() + "\t_";
        }
        return taggedTokens;
    }

}
