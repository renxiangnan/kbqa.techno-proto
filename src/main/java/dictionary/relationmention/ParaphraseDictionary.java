package dictionary.relationmention;

import dictionary.io.DictionaryIOUtils;
import nlp.utils.StanfordCoreNLP;
import common.io.IOUtils;

import java.util.*;


public class ParaphraseDictionary {
    private PredicateIdPair predicateIdPair;
    private Set<Integer> predicateIdSet;
    private Map<String, List<PredicateIDSupport>> predicateNLPattern;
    private Map<String, List<String>> invertedIndex;


    private Map<String, List<PredicateIDSupport>>
        sortPredicateNLPattern(Map<String, List<PredicateIDSupport>> predicateNLPattern){
        for (Map.Entry<String, List<PredicateIDSupport>> entry: predicateNLPattern.entrySet()) {
            Collections.sort(entry.getValue());
        }
        return predicateNLPattern;
    }

    private PredicateIdPair createPredicateIdPair(String dbpediaPredicateIdPath) {
        return DictionaryIOUtils.loadPredicateIdPair(dbpediaPredicateIdPath);
    }

    private Set<Integer> createPredicateIdSet(String dbpediaDBPredicatePath,
                                              PredicateIdPair predicateIdPair) {
        return DictionaryIOUtils.
                loadPredicateId(dbpediaDBPredicatePath, predicateIdPair);
    }

    private Map<String, List<PredicateIDSupport>>
        createPredicateNLPattern(String baseRerankPath, String handwritePath) {
        Map<String, List<PredicateIDSupport>> predicateNLPattern = new HashMap<>();
        Map<String, List<PredicateIDSupport>> initialPredicateNLPattern = DictionaryIOUtils.
                initPredicateNLPattern(baseRerankPath, predicateIdPair);
        Map<String, List<PredicateIDSupport>> selfEnrichedPredicateNLPattern= DictionaryIOUtils.
                loadPredicateNLPattern(predicateIdPair, initialPredicateNLPattern, new StanfordCoreNLP());
        Map<String, List<PredicateIDSupport>> handwritePredicateNLPattern = DictionaryIOUtils.
                loadHandwritePredicateNLPattern(handwritePath, predicateIdPair);

        predicateNLPattern.putAll(initialPredicateNLPattern);
        predicateNLPattern.putAll(selfEnrichedPredicateNLPattern);
        predicateNLPattern.putAll(handwritePredicateNLPattern);

        return sortPredicateNLPattern(predicateNLPattern);
    }

    public PredicateIdPair getPredicateIdPair() {

        return predicateIdPair;
    }

    public Set<Integer> getPredicateIdSet() {

        return predicateIdSet;
    }

    public Map<String, List<String>> getInvertedIndex() {

        return invertedIndex;
    }

    public ParaphraseDictionary(String dbpediaPredIdPath,
                                String dbpediaDBPredPath,
                                String dbpediaRelParaBaseRerank,
                                String dbpediaRelParaHandwritePath) {
        predicateIdPair = createPredicateIdPair(dbpediaPredIdPath);
        predicateIdSet = createPredicateIdSet(dbpediaDBPredPath, predicateIdPair);
        predicateNLPattern = createPredicateNLPattern(dbpediaRelParaBaseRerank, dbpediaRelParaHandwritePath);
        invertedIndex = buildInvertedIndex();
    }

    public Map<String, List<PredicateIDSupport>> getPredicateNLPattern() {
        invertedIndex = new HashMap<>();

        for (String predicate: predicateNLPattern.keySet()) {
            String[] predicateTokens = predicate.split(IOUtils.lineSeparator);
            for (String predicateToken: predicateTokens) {
                if (predicateToken.length() < 1) continue;
                if (!invertedIndex.containsKey(predicateToken)) {
                    invertedIndex.put(predicateToken, new ArrayList<>());
                }
             invertedIndex.get(predicateToken).add(predicate);
            }
        }
        return predicateNLPattern;
    }

    public Map<String, List<String>> buildInvertedIndex () {
        Map<String, List<String>> invertedIndex = new HashMap<>();

        for (String predicate: predicateNLPattern.keySet()) {
            String[] predicateTokens = predicate.split(IOUtils.lineSeparator);
            for (String predicateToken: predicateTokens) {
                if (predicateToken.length() < 1) continue;
                if (!invertedIndex.containsKey(predicateToken)) {
                    invertedIndex.put(predicateToken, new ArrayList<>());
                }
                invertedIndex.get(predicateToken).add(predicate);
            }
        }
        return invertedIndex;
    }

}
