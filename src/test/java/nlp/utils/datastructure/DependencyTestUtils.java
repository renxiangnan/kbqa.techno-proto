package nlp.utils.datastructure;

import nlp.datastructure.DependencyTreeNode;
import nlp.datastructure.Word;
import nlp.utils.MaltParser;
import nlp.utils.StanfordCoreNLP;
import org.apache.log4j.Logger;

abstract class DependencyTestUtils {
    Logger logger = Logger.getLogger(this.getClass());
    StanfordCoreNLP stanfordCoreNLP = new StanfordCoreNLP();
    MaltParser maltParser = new MaltParser();

    DependencyTreeNode
    buildExpectedTreeNode(String baseForm,
                          String originalForm,
                          String posTag,
                          int position,
                          int height) {
        Word word = new Word(baseForm, originalForm, posTag, position);
        DependencyTreeNode treeNode = new DependencyTreeNode(word);
        treeNode.setHeight(height);
        return treeNode;
    }

}
