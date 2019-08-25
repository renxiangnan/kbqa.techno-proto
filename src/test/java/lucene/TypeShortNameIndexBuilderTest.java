package lucene;

import common.io.LoadPath;
import fragment.collection.TypeFragmentCollection;
import lucene.index.TypeShortNameIndexBuilder;
import org.junit.Test;

public class TypeShortNameIndexBuilderTest {

    private TypeFragmentCollection typeFragmentCollection =
            new TypeFragmentCollection(
                    LoadPath.FragmentLoadPath.typeFragmentPath,
                    LoadPath.FragmentLoadPath.dbpediaBasicTypePath,
                    LoadPath.FragmentLoadPath.yagoTypePath);
    private TypeShortNameIndexBuilder typeShortNameIndexBuilder =
            new TypeShortNameIndexBuilder(
                    typeFragmentCollection,
                    LoadPath.FragmentLoadPath.typeShortNameIndexDirPath);

    @Test
    public void indexBuildTest() {

        typeShortNameIndexBuilder.buildIndex();
    }

}
