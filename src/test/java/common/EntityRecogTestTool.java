package common;

import common.io.LoadPath;
import lucene.EntityFragmentFields;
import queryanswering.mapping.DBpediaLookup;

public class EntityRecogTestTool {

    public static EntityFragmentFields entFgmtFieldsForTest =
            new EntityFragmentFields(LoadPath.FragmentLoadPath.entityIdPairPath, LoadPath.FragmentLoadPath.entityFragmentPath);

    public static  DBpediaLookup dbpediaLookupForTest =
            new DBpediaLookup(LoadPath.DBpediaLookupPath.DBpediaLookupURL);




}
