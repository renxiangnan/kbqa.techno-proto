package lucene.search;

import common.io.LoadPath;
import lucene.EntityNameWithScore;
import lucene.index.EntityFragmentIndexBuilder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

public class EntityFragmentSearcherTest {
    private Analyzer analyzer = EntityFragmentIndexBuilder.analyzer;

    private EntityFragmentSearcher entFgmtSearcher;

    {
        try {
            Directory indexDir = FSDirectory.open(
                    Paths.get(LoadPath.FragmentLoadPath.entityFragmentIndexDirPath));
            entFgmtSearcher = new EntityFragmentSearcher(indexDir, analyzer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void entitySearchByNameTest() {
        entityFragmentSearchTest("china -duplicate:false", 8.0, 9.0, 10000);
        entityFragmentSearchTest("U.S.A. -duplicate:false", 8.5, 9.5, 10000);

    }

    // TODO bug to fix, Tif-id scoring, repeated result, lower/upper case correction
    private void entityFragmentSearchTest(String queryStr,
                                          double threshold1,
                                          double threshold2,
                                          int K) {
        try {
            List<EntityNameWithScore> result = entFgmtSearcher.
                    search(queryStr, threshold1, threshold2, K);

            for (EntityNameWithScore ent : result)
                System.out.println(ent.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
