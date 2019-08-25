package queryanswering.extraction;

import common.EntityRecogTestTool;
import common.io.LoadPath;
import lucene.EntityFragmentFields;
import queryanswering.mapping.DBpediaLookup;
import rdf.mapping.EntityMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class EntityRecognizerTest {
    public static void main (String[] args)  {
        EntityRecognizer entRcog = new EntityRecognizer(LoadPath.StopEntityFilePath.stopEntityFilePath);
        DBpediaLookup dbpLookup = EntityRecogTestTool.dbpediaLookupForTest;
        EntityFragmentFields entFgmt = EntityRecogTestTool.entFgmtFieldsForTest;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("\nInput phrase: ");

                String phrase = br.readLine();
                List<EntityMapping> entityMappings = entRcog.
                        getEntityIdsAndNames(
                                phrase,
                                true,
                                EntityRecogTestTool.dbpediaLookupForTest,
                                EntityRecogTestTool.entFgmtFieldsForTest);

                for (EntityMapping em: entityMappings) {
                    System.out.println(em);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
