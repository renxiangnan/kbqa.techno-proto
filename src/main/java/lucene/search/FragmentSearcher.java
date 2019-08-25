package lucene.search;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;

abstract class FragmentSearcher {
    IndexSearcher searcher;
    Analyzer analyzer;
    final Logger logger = Logger.getLogger(FragmentSearcher.class);

    String findInDocument(String fieldName,
                          IndexSearcher searcher,
                          TopDocs topDocs,
                          int rank) {
        String result = "";
        try {
            return searcher.
                    doc(topDocs.scoreDocs[rank].doc).
                    get(fieldName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    FragmentSearcher(Directory indexDir, Analyzer analyzer) throws Exception {
        try {
            searcher = new IndexSearcher(DirectoryReader.open(indexDir));
            this.analyzer = analyzer;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("EntityFragmentSearcher initialization failed. ");
        }
    }

}
