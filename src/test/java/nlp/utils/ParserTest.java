package nlp.utils;

import edu.stanford.nlp.trees.GrammaticalStructure;
import nlp.datastructure.Sentence;
import org.junit.Assert;
import org.junit.Test;
import org.maltparser.core.exception.MaltChainedException;
import org.maltparser.core.syntaxgraph.DependencyStructure;
import org.maltparser.core.syntaxgraph.edge.Edge;
import org.maltparser.core.syntaxgraph.node.DependencyNode;


public class ParserTest {
    private StanfordCoreNLP stanfordCoreNLP = new StanfordCoreNLP();

    @Test
    public void stanfordParserTest() {
        StanfordParser stanfordParser = new StanfordParser();
        GrammaticalStructure structure = stanfordParser.
                getGrammaticalStructure("What is the budget of the film directed by Paul Anderson");

        System.out.println(structure.typedDependenciesEnhanced());
    }

    @Test
    public void maltParserDependencyTest() throws MaltChainedException {
        Sentence sentece = new Sentence(
                "What is the budget of the film directed by Paul Anderson",
                stanfordCoreNLP);
        MaltParser maltParser = new MaltParser();
        DependencyStructure ds = maltParser.getDependencyStructure(sentece);

        int[] expectedHeads = {0, 2, 0, 4, 2, 4, 7, 5, 7, 8, 11, 9};
        int[] computedHeads = new int[ds.getDependencyIndices().size()];
        for (int i = 1; i < computedHeads.length; i++) {
            computedHeads[i] = ds.getDependencyNode(i).getHead().getIndex();
        }

        Assert.assertArrayEquals(expectedHeads, computedHeads);

        for (Edge edge : maltParser.getDependencyStructure(sentece).getEdges()) {
            System.out.println();
            System.out.println("srcnode: " + edge.getSource() + " -> " + edge.getTarget() + ",  src right:    " + ((DependencyNode) (edge.getSource())).getRightDependents());
            System.out.println("srcnode: " + edge.getSource() + " -> " + edge.getTarget() + ",  src right sibling:    " + ((DependencyNode) (edge.getSource())).getRightSibling());
            System.out.println("srcnode: " + edge.getSource() + " -> " + edge.getTarget() + ",  src right most: " + ((DependencyNode) (edge.getSource())).getRightmostDependent());
            System.out.println();
        }
    }


}
