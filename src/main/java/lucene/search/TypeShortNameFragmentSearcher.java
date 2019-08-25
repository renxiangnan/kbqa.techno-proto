package lucene.search;

import fragment.TypeShortNameIdPair;
import lucene.TypeShortNameFragmentWithScore;
import lucene.index.FieldNameLabels;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import rdf.mapping.TypeMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeShortNameFragmentSearcher extends FragmentSearcher {
    private TypeShortNameFragmentWithScore
        buildTypeShortNameWithScore(IndexSearcher searcher,
                                    TopDocs topDocs,
                                    int rank) {

        return new TypeShortNameFragmentWithScore(
                findInDocument(
                        FieldNameLabels.TypeShortNameFieldLabel.typeShortName, searcher, topDocs, rank),
                topDocs.scoreDocs[rank].score);
    }

    public TypeShortNameFragmentSearcher(Directory indexDir, Analyzer analyzer) throws Exception {
        super(indexDir, analyzer);
    }

    public List<TypeMapping> searchTypeNameWithScore(TypeShortNameIdPair typeShortNameIdPair,
                                                     String queryStr) throws ParseException {
        return searchTypeNameWithScore(typeShortNameIdPair, queryStr, 0.4, 0.8, 10);
    }



    public List<TypeMapping> searchTypeNameWithScore(TypeShortNameIdPair typeShortNameIdPair,
                                                     String queryStr,
                                                     double threshold1,
                                                     double threshold2,
                                                     int K) throws ParseException {
        QueryParser queryParser = new QueryParser("SplittedTypeShortName", this.analyzer);
        Query query = queryParser.parse(queryStr);
        List<TypeMapping> result = new ArrayList<>();

        try {
            TopDocs topDocs = searcher.search(query, K);
            logger.info("topDocs.totalHits: " + topDocs.totalHits + ", " +
                    "topDocs.scoreDocs.length: " + topDocs.scoreDocs.length);

            if (topDocs.totalHits == 0) return result;

            for (int i = 0; i < Math.min(topDocs.scoreDocs.length, topDocs.totalHits); i ++) {
                if ((i < K && topDocs.scoreDocs[i].score >= threshold1) ||
                        (i >= K && topDocs.scoreDocs[i].score >= threshold2)) {
                    List<Integer> typeIds = typeShortNameIdPair.
                            getShortNameToId().
                            getOrDefault(findInDocument(
                                    FieldNameLabels.TypeShortNameFieldLabel.typeShortName, searcher, topDocs, i),
                                    new ArrayList<>());

                    if (typeIds.size() > 0) {
                        for (int typeId: typeIds) {
                            TypeShortNameFragmentWithScore shortNameWithScore =
                                    buildTypeShortNameWithScore(searcher, topDocs, i);
                            TypeMapping typeMapping =
                                    new TypeMapping(typeId, shortNameWithScore.getName(), shortNameWithScore.score);
                            result.add(typeMapping);
                        }
                    }
                } else break;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public List<TypeShortNameFragmentWithScore> searchTypeName(String queryStr) throws ParseException {
        return searchTypeName(
                queryStr,
                LCNPara.TypePara.TH1,
                LCNPara.TypePara.TH2,
                LCNPara.TypePara.K);
    }


    public List<TypeShortNameFragmentWithScore> searchTypeName(String queryStr,
                                                               double threshold1,
                                                               double threshold2,
                                                               int K) throws ParseException {
        QueryParser queryParser = new QueryParser("SplittedTypeShortName", this.analyzer);
        Query query = queryParser.parse(queryStr);
        List<TypeShortNameFragmentWithScore> result = new ArrayList<>();

        try {
            TopDocs topDocs = searcher.search(query, K);
            logger.info("topDocs.totalHits: " + topDocs.totalHits + ", " +
                    "topDocs.scoreDocs.length: " + topDocs.scoreDocs.length);

            if (topDocs.totalHits == 0) return result;

            for (int i = 0; i < Math.min(topDocs.scoreDocs.length, topDocs.totalHits); i ++) {
                if ((i < K && topDocs.scoreDocs[i].score >= threshold1) ||
                        (i >= K && topDocs.scoreDocs[i].score >= threshold2)) {
                    result.add(buildTypeShortNameWithScore(searcher, topDocs, i));
                } else break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
