package fragment.io;

import common.io.LoadPath;
import fragment.collection.RelationFragmentCollection;
import fragment.collection.TypeFragmentCollection;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;


public class FragmentIOUtilsTest {
    @Test
    public void loadTypeFragmentTest() {
        TypeFragmentCollection typeFragmentCollection =
                new TypeFragmentCollection(
                        LoadPath.FragmentLoadPath.typeFragmentPath,
                        LoadPath.FragmentLoadPath.dbpediaBasicTypePath,
                        LoadPath.FragmentLoadPath.yagoTypePath);

        int expectedSize = 334;
        Assert.assertEquals(expectedSize, typeFragmentCollection.getTypeFragmentMap().size());

        for (Map.Entry<Integer, String> entry : typeFragmentCollection.
                getTypeShortNameIdPair().getIdToShortName().entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }


    @Test
    public void loadYagoTypesTest() {
        TypeFragmentCollection typeFragmentCollection =
                new TypeFragmentCollection(
                        LoadPath.FragmentLoadPath.typeFragmentPath,
                        LoadPath.FragmentLoadPath.dbpediaBasicTypePath,
                        LoadPath.FragmentLoadPath.yagoTypePath);

        int expectedSize = 18444;
        Assert.assertEquals(expectedSize, typeFragmentCollection.getYagoTypes().size());

        for (String yagoType : typeFragmentCollection.getYagoTypes()) {
            System.out.println(yagoType);
        }
    }



    @Test
    public void loadRelationFragmentTest() {
        RelationFragmentCollection relationFragment =
                new RelationFragmentCollection(
                        LoadPath.FragmentLoadPath.predicateFragmentPath,
                        LoadPath.FragmentLoadPath.predicateIdPath);

        int expectedRelShortNameToIdSize = 7543;

        Assert.assertEquals(
                expectedRelShortNameToIdSize,
                relationFragment.getRelationShortNameIdPair().
                        getRelShortNameToId().size());

        Assert.assertEquals(
                expectedRelShortNameToIdSize,
                relationFragment.getRelationShortNameIdPair().
                        getRelIdToShortName().size());
    }

}
