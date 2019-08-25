package lucene;

import common.io.LoadPath;
import lucene.index.EntityFragmentIndexBuilder;
import org.junit.Test;

public class EntityFragmentIndexBuilderTest {

    private EntityFragmentIndexBuilder indexBuilder =
            new EntityFragmentIndexBuilder(
                    LoadPath.FragmentLoadPath.entityFragmentIndexDirPath,
                    LoadPath.FragmentLoadPath.sourceDirPath,
                    LoadPath.FragmentLoadPath.entityIdPairPath,
                    LoadPath.FragmentLoadPath.entityFragmentPath);

    @Test
    public void indexBuildTest() {

        indexBuilder.buildIndex();
    }

}
