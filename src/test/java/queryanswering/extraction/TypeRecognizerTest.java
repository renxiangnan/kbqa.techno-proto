package queryanswering.extraction;

import lucene.TypeShortNameFragmentWithScore;
import nlp.datastructure.Word;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Assert;
import org.junit.Test;
import rdf.mapping.TypeMapping;
import common.TypeSearcherForTest;

import java.util.*;
import java.util.stream.Collectors;


// TODO
public class TypeRecognizerTest extends TypeSearcherForTest {
    private TypeRecognizer typeRecognizer = new TypeRecognizer(
            super.getTypeFragmentCollection(),
            getSearcher());

    // TODO
    @Test
    public void addWHWordsTypeTest() {


    }

    @Test
    public void recognizeSpecialWordTest() {
        Set<TypeMapping> expectedWhere = new HashSet<>(Arrays.asList(
                new TypeMapping(282, "Place", 1.0),
                new TypeMapping(291, "Place", 1.0),
                new TypeMapping(92, "Organization", 1.0),
                new TypeMapping(108, "Organization", 1.0)
        ));
        Set<TypeMapping> expectedWho = new HashSet<>(Arrays.asList(
                new TypeMapping(99, "Person", 1.0),
                new TypeMapping(278, "Person", 1.0),
                new TypeMapping(92, "Organization", 1.0),
                new TypeMapping(108, "Organization", 1.0)
        ));


        Assert.assertEquals(expectedWhere, new HashSet<>(typeRecognizer.recognizeSpecialWord("where")));
        Assert.assertEquals(expectedWho, new HashSet<>(typeRecognizer.recognizeSpecialWord("who")));
    }

    @Test
    public void recognizeTest() {
        recognizeTestBase("Person",
                new HashSet<>(Arrays.asList("Person", "MilitaryPerson", "PersonFunction", "SocialPerson", "NaturalPerson")),
                new HashSet<>(Arrays.asList(99, 211, 35, 254, 278)));

        recognizeTestBase("Place",
                new HashSet<>(Arrays.asList("Place", "HistoricPlace", "NaturalPlace", "PopulatedPlace")),
                new HashSet<>(Arrays.asList(291, 134, 180, 117)));

        recognizeTestBase("Location",
                new HashSet<>(Collections.singletonList("Location")),
                new HashSet<>(Collections.singletonList(282)));

        recognizeTestBase("Organization",
                new HashSet<>(Arrays.asList("Organization", "EducationalOrganization")),
                new HashSet<>(Arrays.asList(108, 258)));

        recognizeTestBase("Organisation",
                new HashSet<>(Arrays.asList("Organisation", "OrganisationMember", "Non-ProfitOrganisation")),
                new HashSet<>(Arrays.asList(92, 328, 84)));
    }

    private void recognizeTestBase (String baseStrToSearch,
                                    Set<String> expecetedRes, Set<Integer> expectedIds) {
        try {
            List<String> obtainedTypeShortNames = getSearcher().
                    searchTypeName(baseStrToSearch).
                    stream().
                    map(TypeShortNameFragmentWithScore::getName).
                    collect(Collectors.toList());
            List<Integer> obtainedIds = typeRecognizer.recognize(baseStrToSearch);

            Assert.assertEquals(expecetedRes, new HashSet<>(obtainedTypeShortNames));
            Assert.assertEquals(expectedIds, new HashSet<>(obtainedIds));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTypeIdsAndNamesByStringTest() {
        HashSet<TypeMapping> expectedRes =
                new HashSet<>(Collections.singletonList(new TypeMapping(310, "RacingDriver", 1.0)));
        List<TypeMapping> obtained = typeRecognizer.getTypeIdsAndNamesByString("racing");

        Assert.assertEquals(expectedRes, new HashSet<>(obtained));
    }

    @Test
    public void getExtendTypeByStringTest() {
        List<TypeMapping> obtainedRes = typeRecognizer.getExtendTypeByString("<yago:WikicatPeopleFromAktobe>");

        System.out.println(obtainedRes.size());
    }

    @Test
    public void recognizeConstantVariableTest() {


    }

    @Test
    public void processArgTest() {
        Word[] words = {

        };

    }





}
