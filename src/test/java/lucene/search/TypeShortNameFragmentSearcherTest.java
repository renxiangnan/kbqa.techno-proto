package lucene.search;

import fragment.TypeShortNameIdPair;
import lucene.TypeShortNameFragmentWithScore;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;
import rdf.mapping.TypeMapping;
import common.TypeSearcherForTest;

import java.util.HashSet;
import java.util.List;

public class TypeShortNameFragmentSearcherTest extends TypeSearcherForTest {

    @Test
    public void typeShortNameWithScoreSearchByNameTest() {

        typeShortNameWithScoreTest(
                getTypeFragmentCollection().getTypeShortNameIdPair(),
                "Historic", 0.0, 0.0, 10);

        typeShortNameWithScoreTest(
                getTypeFragmentCollection().getTypeShortNameIdPair(),
                "Building", 0.0, 0.0, 10);

        typeShortNameWithScoreTest(
                getTypeFragmentCollection().getTypeShortNameIdPair(),
                "Organization", 0.0, 0.0, 10);
    }

    @Test
    public void typeShortNameSearchByNameTest() throws ParseException {

        typeShortNameSearchTest("Rocket", 0.0, 0.0, 10);
        typeShortNameSearchTest("Astronaut", 0.0, 0.0, 10);

    }

    private void typeShortNameWithScoreTest(TypeShortNameIdPair typeShortNameIdPair,
                                            String queryStr,
                                            double threshold1,
                                            double threshold2,
                                            int K) {
        try {
            List<TypeMapping> result = getSearcher().searchTypeNameWithScore(
                    typeShortNameIdPair, queryStr, threshold1, threshold2, K);

            new HashSet<>(result).forEach(System.out :: println);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void typeShortNameSearchTest(String queryStr,
                                         double threshold1,
                                         double threshold2,
                                         int K) {
        List<TypeShortNameFragmentWithScore> result;
        try {
            result = getSearcher().
                    searchTypeName(queryStr, threshold1, threshold2, K);
            new HashSet<>(result).forEach(x -> System.out.println("obtained type short name " + x));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
