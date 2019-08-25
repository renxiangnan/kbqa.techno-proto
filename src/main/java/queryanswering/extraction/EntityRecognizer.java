package queryanswering.extraction;

import fragment.EntityFragment;
import lucene.EntityFragmentFields;
import lucene.EntityIdPair;
import nlp.datastructure.MergedWord;
import nlp.datastructure.Word;
import queryanswering.QALabel;
import queryanswering.io.ExtractionIOUtils;
import queryanswering.mapping.DBpediaLookup;
import rdf.mapping.EntityMapping;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// TODO, add additional fix
public class EntityRecognizer {
    @Deprecated
    private String preLog;
    private double entityAcceptScore = 26;
    private double typeAcceptScore = 0.5;

    private List<MergedWord> mergedWords;
    private List<String> stopEntities;
    private List<String> badTagsForEntitiesAndTypes;
    private List<List<Integer>> selectedList;


    public EntityRecognizer(String stopEntitiesPath) {
        badTagsForEntitiesAndTypes= new ArrayList<>();
        stopEntities = ExtractionIOUtils.loadStopEntities(stopEntitiesPath);
        badTagsForEntitiesAndTypes.addAll(
                Arrays.asList("RBS", "JJS", "W", ".", "VBD", "VBN", "VBZ", "VBP", "POS"));
    }

    // DFS search
    public List<List<Integer>> search(List<Integer> keys,
                       List<Integer> selected,
                       int depth,
                       int size) {
        List<List<Integer>> ret = new ArrayList<>();
        EntityRecognizerHelper.searchHelper(keys, selected, depth, size, ret);
        return ret;
    }

    public List<EntityMapping> getEntityIdsAndNames(String entity,
                                                    boolean dbpediaLookup,
                                                    DBpediaLookup dBpediaLookup,
                                                    EntityFragmentFields entityFragmentFields) {
        List<EntityMapping> entityMappings = new ArrayList<>();
        entityMappings.addAll(EntityFragment.getEntityMappings(entity, entityFragmentFields));

        if (dbpediaLookup) entityMappings.addAll(
                dBpediaLookup.getEntityMappings(entityFragmentFields.getEntityIdPair(), entity));
        Collections.sort(entityMappings);

        return entityMappings;
    }

    public List<String> process(String question) {

        return null;
    }

}
