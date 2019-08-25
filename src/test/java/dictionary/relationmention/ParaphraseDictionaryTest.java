package dictionary.relationmention;

import common.io.LoadPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParaphraseDictionaryTest {

    private ParaphraseDictionary paraDict = new ParaphraseDictionary(
            LoadPath.DictionaryLoadPath.dbpediaPredicateIdPath,
            LoadPath.DictionaryLoadPath.dbpediaDBPredicatePath,
            LoadPath.DictionaryLoadPath.dbpediaRelParaBaseRerankPath,
            LoadPath.DictionaryLoadPath.dbpediaHandwritePredicatePath
    );

    @Test
    public void predicateIdPairCreationTest() {
        PredicateIdPair predIdPair = paraDict.getPredicateIdPair();
        predIdPair.showPredicateToId();
        predIdPair.showIdToPredicate();
    }

    @Test
    public void predicateIdSetCreationTest() {
        Set<Integer> predicateIdSet = paraDict.getPredicateIdSet();
        System.out.println(Arrays.toString(predicateIdSet.toArray()));
    }

    @Test
    public void addHandWriteNLPatternTest() {
        Map<String, List<PredicateIDSupport>> handWriteNLPattern = paraDict.getPredicateNLPattern();
        for (Map.Entry<String, List<PredicateIDSupport>> entry : handWriteNLPattern.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Test
    public void predicateNLPatternCreationTest() {
        Map<String, List<PredicateIDSupport>> predicateNLPattern = paraDict.getPredicateNLPattern();
        int expecetedSize = 38517;
        Assert.assertEquals(expecetedSize, predicateNLPattern.size());
    }

    @Test
    public void buildInvertedIndexTest() {
        Map<String, List<String>> invertedIndex = paraDict.getInvertedIndex();
        int expectedSize = 5064;
        Assert.assertEquals(expectedSize, invertedIndex.size());
    }

}
