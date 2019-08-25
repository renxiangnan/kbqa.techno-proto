package lucene;

import common.io.LoadPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class EntityFragmentFieldsTest {

    private EntityFragmentFields entFgmtFields =
            new EntityFragmentFields(LoadPath.FragmentLoadPath.entityIdPairPath, LoadPath.FragmentLoadPath.entityFragmentPath);

    @Test
    public void buildEntityNameToIdTest() {

        // name -> id: University -> 946970
        Assert.assertEquals(
                entFgmtFields.getEntityIdPair().getEntityNameToId().size(),
                entFgmtFields.getEntityIdPair().getEntityIdToName().size());
    }

}
