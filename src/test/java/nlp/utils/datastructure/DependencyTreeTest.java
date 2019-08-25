package nlp.utils.datastructure;

import nlp.datastructure.DependencyTree;
import nlp.datastructure.DependencyTreeNode;
import nlp.datastructure.Sentence;
import org.junit.Assert;
import org.junit.Test;
import org.maltparser.core.exception.MaltChainedException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class DependencyTreeTest extends DependencyTestUtils {

    @Test
    public void dependencyTreeInOrderTraversalTest() {
        Sentence sentence = new Sentence("What is the budget of the film directed by Paul Anderson", stanfordCoreNLP);

        try {
            DependencyTree dependencyTree = new DependencyTree(sentence, maltParser);
            dependencyTree.getAllNodes().sort(Comparator.comparingInt(word -> word.getWord().getPosition()));
            List<DependencyTreeNode> expecetedTreeNodes = Arrays.asList(
                    buildExpectedTreeNode("be", "is", "VBZ", 2, 0),
                    buildExpectedTreeNode("what", "What", "WP", 1, 1),
                    buildExpectedTreeNode("budget", "budget", "NN", 4, 1),
                    buildExpectedTreeNode("the", "the", "DT", 3, 2),
                    buildExpectedTreeNode("of", "of", "IN", 5, 2),
                    buildExpectedTreeNode("film", "film", "NN", 7, 3),
                    buildExpectedTreeNode("the", "the", "DT", 6, 4),
                    buildExpectedTreeNode("direct", "directed", "VBN", 8, 4),
                    buildExpectedTreeNode("by", "by", "IN", 9, 5),
                    buildExpectedTreeNode("anderson", "Anderson", "NNP", 11, 6),
                    buildExpectedTreeNode("paul", "Paul", "NNP", 10, 7));

            expecetedTreeNodes.sort(Comparator.comparingInt(word -> word.getWord().getPosition()));
            Assert.assertArrayEquals(dependencyTree.getAllNodes().toArray(), expecetedTreeNodes.toArray());
        } catch (MaltChainedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPathToRootTest() {
        Sentence sentence = new Sentence("What is the budget of the film directed by Paul Anderson", stanfordCoreNLP);

        try {
            DependencyTree dependencyTree = new DependencyTree(sentence, maltParser);
            dependencyTree.getAllNodes().sort(Comparator.comparingInt(word -> word.getWord().getPosition()));
            List<DependencyTreeNode> allNodes = dependencyTree.getAllNodes();

            DependencyTreeNode node_11 = allNodes.get(10);
            DependencyTreeNode[] path_node_11_to_root = dependencyTree.getPathToRoot(node_11).toArray(new DependencyTreeNode[0]);
            DependencyTreeNode[] expectedPath = {
                    buildExpectedTreeNode("anderson", "Anderson", "NNP", 11, 6),
                    buildExpectedTreeNode("by", "by", "IN", 9, 5),
                    buildExpectedTreeNode("direct", "directed", "VBN", 8, 4),
                    buildExpectedTreeNode("film", "film", "NN", 7, 3),
                    buildExpectedTreeNode("of", "of", "IN", 5, 2),
                    buildExpectedTreeNode("budget", "budget", "NN", 4, 1),
                    buildExpectedTreeNode("be", "is", "VBZ", 2, 0)};

            logger.debug("Path from " + node_11 + " to root.");
            for (DependencyTreeNode node : path_node_11_to_root) {
                logger.debug(node.toString());
            }

            Assert.assertArrayEquals(expectedPath, path_node_11_to_root);
        } catch (MaltChainedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getUndirectedShortestPathTest() {
        Sentence sentence = new Sentence("What is the budget of the film directed by Paul Anderson", stanfordCoreNLP);

        try {
            DependencyTree dependencyTree = new DependencyTree(sentence, maltParser);
            dependencyTree.getAllNodes().sort(Comparator.comparingInt(word -> word.getWord().getPosition()));
            List<DependencyTreeNode> allNodes = dependencyTree.getAllNodes();

            DependencyTreeNode node_6 = allNodes.get(5);
            DependencyTreeNode node_11 = allNodes.get(10);
            DependencyTreeNode[] path_6_to_11 = dependencyTree.
                    getUndirectedShortestPath(node_6, node_11).
                    toArray(new DependencyTreeNode[0]);
            DependencyTreeNode[] expectedPath = {
                    buildExpectedTreeNode("the", "the", "DT", 6, 4),
                    buildExpectedTreeNode("film", "film", "NN", 7, 3),
                    buildExpectedTreeNode("direct", "directed", "VBN", 8, 4),
                    buildExpectedTreeNode("by", "by", "IN", 9, 5),
                    buildExpectedTreeNode("anderson", "Anderson", "NNP", 11, 6)};

            logger.debug("Path from " + node_11 + " to root.");
            for (DependencyTreeNode node : path_6_to_11) {
                logger.debug(node.toString());
            }

            Assert.assertArrayEquals(expectedPath, path_6_to_11);
        } catch (MaltChainedException e) {
            e.printStackTrace();
        }
    }
}
