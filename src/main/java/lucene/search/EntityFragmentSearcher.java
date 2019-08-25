package lucene.search;

import lucene.EntityNameWithScore;
import lucene.index.FieldNameLabels;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityFragmentSearcher extends FragmentSearcher{
    private EntityNameWithScore buildEntityNameWithScore(IndexSearcher searcher,
                                                         TopDocs topDocs,
                                                         int rank) {
        String entity = findInDocument(
                FieldNameLabels.EntityFieldLabel.EntityName, searcher, topDocs, rank);
        int id = Integer.parseInt(
                findInDocument(
                        FieldNameLabels.EntityFieldLabel.EntityId, searcher, topDocs, rank));

        return new EntityNameWithScore(id, entity, topDocs.scoreDocs[rank].score);
    }

    public EntityFragmentSearcher(Directory indexDir, Analyzer analyzer) throws Exception {
        super(indexDir, analyzer);
    }


    public List<EntityNameWithScore> search(String queryStr) throws ParseException {

        return search(queryStr,
                LCNPara.EntityPara.TH1,
                LCNPara.EntityPara.TH2,
                LCNPara.EntityPara.K);
    }


    public List<EntityNameWithScore> search(String queryStr,
                                            double threshold1,
                                            double threshold2,
                                            int K) throws ParseException {
        QueryParser queryParser = new QueryParser("EntityName", this.analyzer);
        Query query = queryParser.parse(queryStr);
        List<EntityNameWithScore> result = new ArrayList<>();

        try {
            TopDocs topDocs = searcher.search(query, K);
            if (topDocs.totalHits == 0) return result;

            logger.info("topDocs.totalHits: " + topDocs.totalHits + ", " +
                    "topDocs.scoreDocs.length: " + topDocs.scoreDocs.length);

            for (int i = 0; i < Math.min(topDocs.scoreDocs.length, topDocs.totalHits); i ++) {
                if ((i < K && topDocs.scoreDocs[i].score >= threshold1) ||
                        (i >= K && topDocs.scoreDocs[i].score >= threshold2)) {
                    result.add(buildEntityNameWithScore(searcher, topDocs, i));
                } else break;
            }

            Set<EntityNameWithScore> set = new HashSet<>(result);
            System.out.println("Set size: " + set.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}

