package nlp.utils.datastructure;

import nlp.datastructure.DependencyTree;
import nlp.datastructure.Sentence;
import org.junit.Assert;
import org.junit.Test;


public class DependencyTreeNodeTest extends DependencyTestUtils {

    @Test
    public void getAllNodesTest() {
        Sentence sentence = new Sentence(
                "What is the budget of the film directed by Paul Anderson",
                stanfordCoreNLP);
        String[] expectedNodes = {
                "What-WP(attr)[1]", "is-VBZ(root)[2]", "the-DT(det)[3]",
                "budget-NN(nsubj)[4]", "of-IN(prep)[5]", "the-DT(det)[6]",
                "film-NN(pobj)[7]", "directed-VBN(partmod)[8]", "by-IN(prep)[9]",
                "Paul-NNP(nn)[10]", "Anderson-NNP(pobj)[11]"
        };

        try {
            DependencyTree dependencyTree = new DependencyTree(sentence, maltParser);
            String[] obtainedNodes = new String[dependencyTree.getAllNodes().size()];
            for (int i = 0; i < obtainedNodes.length; i++) {
                obtainedNodes[i] = dependencyTree.getAllNodes().get(i).toString();
            }

            Assert.assertArrayEquals(expectedNodes, obtainedNodes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getNNTopTreeNodeTest() {
        Sentence sentence = new Sentence(
                "What is the budget of the film directed by Paul Anderson",
                stanfordCoreNLP);
        String[] expectedNodes = {
                "What-WP(attr)[1]", "is-VBZ(root)[2]", "the-DT(det)[3]",
                "budget-NN(nsubj)[4]", "of-IN(prep)[5]", "the-DT(det)[6]",
                "film-NN(pobj)[7]", "directed-VBN(partmod)[8]", "by-IN(prep)[9]",
                "Anderson-NNP(pobj)[11]", "Anderson-NNP(pobj)[11]"};

        try {
            DependencyTree dependencyTree = new DependencyTree(sentence, maltParser);
            String[] obtainedNodes = new String[dependencyTree.getAllNodes().size()];
            for (int i = 0; i < obtainedNodes.length; i++) {
                obtainedNodes[i] = dependencyTree.getAllNodes().get(i).getNounRootTopNode().toString();
            }

            Assert.assertArrayEquals(expectedNodes, obtainedNodes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
