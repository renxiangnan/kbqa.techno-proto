package queryanswering.mapping;

import common.io.LoadPath;
import lucene.EntityFragmentFields;
import org.junit.Assert;
import org.junit.Test;
import rdf.mapping.EntityMapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBpediaLookupTest {
    private EntityFragmentFields entFgmtFields =
            new EntityFragmentFields(LoadPath.FragmentLoadPath.entityIdPairPath, LoadPath.FragmentLoadPath.entityFragmentPath);
    private DBpediaLookup dBpediaLookup = new DBpediaLookup(LoadPath.DBpediaLookupPath.DBpediaLookupURL);

    @Test
    public void lookupEntityNameTest() {
        Set<String> expectedRes_1 = new HashSet<>(Arrays.asList(
                "Sunil Shetty", "Sunil Gavaskar", "Sunil Dutt", "Sunil Gangopadhyay", "Samvrutha Sunil"));
        Set<String> obtainedRes_1 = new HashSet<>(dBpediaLookup.lookupEntityName("sunil"));

        Set<String> expectedRes_2 = new HashSet<>(Arrays.asList(
                "Kyle XY", "Xuzhou", "Guangxu Emperor", "Xuchang", "List of digraphs in Latin alphabets"));
        Set<String> obtainedRes_2 = new HashSet<>(dBpediaLookup.lookupEntityName("xu"));

        Set<String> expectedRes_3 = new HashSet<>(Arrays.asList(
                "Zhang Yimou", "Zhangzhou", "Baguazhang", "Michael Chang", "Zhang Zuolin"));
        Set<String> obtainedRes_3 = new HashSet<>(dBpediaLookup.lookupEntityName("zhang"));

        Assert.assertEquals(expectedRes_1, obtainedRes_1);
        Assert.assertEquals(expectedRes_2, obtainedRes_2);
        Assert.assertEquals(expectedRes_3, obtainedRes_3);
    }

    @Test
    public void getEntityMappingsTest() {
        List<EntityMapping> expectedMappings = Arrays.asList(
                new EntityMapping(1265555, "Sunil_Gavaskar", 40.0),
                new EntityMapping(1770482, "Sunil_Dutt", 38.0),
                new EntityMapping(761602, "Sunil_Gangopadhyay", 36.0),
                new EntityMapping(1817864, "Samvrutha_Sunil", 34.0)
        );
        List<EntityMapping> obtainedMappings = dBpediaLookup.
                getEntityMappings(entFgmtFields.getEntityIdPair(), "Sunil");

        Assert.assertEquals(new HashSet<>(expectedMappings), new HashSet<>(obtainedMappings));
    }

}
