package common;

import common.io.LoadPath;
import fragment.collection.TypeFragmentCollection;
import lucene.index.TypeShortNameIndexBuilder;
import lucene.search.TypeShortNameFragmentSearcher;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public abstract class TypeSearcherForTest {
    private Analyzer analyzer = TypeShortNameIndexBuilder.analyzer;
    private TypeShortNameFragmentSearcher typeShortNameFgmtSearcher;
    private TypeFragmentCollection typeFragmentCollection =
            new TypeFragmentCollection(
                    LoadPath.FragmentLoadPath.typeFragmentPath,
                    LoadPath.FragmentLoadPath.dbpediaBasicTypePath,
                    LoadPath.FragmentLoadPath.yagoTypePath);

    {
        try {
            Directory indexDir = FSDirectory.
                    open(Paths.get(LoadPath.FragmentLoadPath.typeShortNameIndexDirPath));
            typeShortNameFgmtSearcher = new TypeShortNameFragmentSearcher(indexDir, analyzer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TypeSearcherForTest() {
    }

    protected TypeShortNameFragmentSearcher getSearcher() {
        return typeShortNameFgmtSearcher;
    }

    protected TypeFragmentCollection getTypeFragmentCollection() {
        return typeFragmentCollection;
    }


}
