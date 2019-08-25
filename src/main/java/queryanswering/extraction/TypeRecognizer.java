package queryanswering.extraction;

import fragment.collection.TypeFragmentCollection;
import lucene.TypeShortNameFragmentWithScore;
import lucene.search.TypeShortNameFragmentSearcher;
import nlp.datastructure.Word;
import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import queryanswering.ExtendSynsetForExtraction;
import queryanswering.QALabel;
import rdf.RDFLabel;
import rdf.mapping.PredicateMapping;
import rdf.mapping.TypeMapping;
import rdf.semantic.SemanticRelation;

import java.util.*;

public class TypeRecognizer {
    private Logger logger;
    private TypeFragmentCollection typeFragmentCollection;
    private Map<String, String> extendTypeMap;
    private TypeShortNameFragmentSearcher typeShortNameSearcher;

    public TypeRecognizer(TypeFragmentCollection typeFragmentCollection,
                          TypeShortNameFragmentSearcher typeShortNameSearcher) {
        logger = Logger.getLogger(this.getClass());
        extendTypeMap = new HashMap<>();
        extendTypeMap.putAll(ExtendSynsetForExtraction.EXTEND_TYPE_SYNSET);    // Additional type synset
        this.typeFragmentCollection = typeFragmentCollection;
        this.typeShortNameSearcher = typeShortNameSearcher;
    }

    public void addWHWordsType(Map<Integer, SemanticRelation> semanticRelationMap){
        List<TypeMapping> whWordType;

        for (int id: semanticRelationMap.keySet()) {
            SemanticRelation relation = semanticRelationMap.get(id);
            if (!relation.getArg1().getMayRdfType()) {
                whWordType = recognizeSpecialWord(relation.getArg1().getBaseForm());
                if (whWordType != null) {
                    relation.getArg1().setTypeMappings(whWordType);
                }
            }

            if (!relation.getArg2().getMayRdfType()) {
                whWordType = recognizeSpecialWord(relation.getArg2().getBaseForm());
                if (whWordType != null) {
                    relation.getArg2().setTypeMappings(whWordType);
                }
            }
        }
    }


    /**
     * Recognize WH words with type inferred:
     *      who -> person, org
     *      where -> place, org
     *
     */
    public List<TypeMapping> recognizeSpecialWord(String specialWord) {
        List<TypeMapping> typeMappings = new ArrayList<>();
        String normalizedWord = specialWord.toLowerCase();

        // Merge types
        if (normalizedWord.equals(QALabel.WHO)) {
            typeFragmentCollection.getTypeShortNameIdPair().getTypePersonIdList().
                    forEach(id -> typeMappings.add(new TypeMapping(id, QALabel.PERSON, 1)));
            typeFragmentCollection.getTypeShortNameIdPair().getTypeOrgIdList().
                    forEach(id -> typeMappings.add(new TypeMapping(id, QALabel.ORGANIZATION, 1)));
        } else if (normalizedWord.equals(QALabel.WHERE)) {
            typeFragmentCollection.getTypeShortNameIdPair().getTypeLocationIdList().
                    forEach(id -> typeMappings.add(new TypeMapping(id, QALabel.PLACE, 1)));
            typeFragmentCollection.getTypeShortNameIdPair().getTypeOrgIdList().
                    forEach(id -> typeMappings.add(new TypeMapping(id, QALabel.ORGANIZATION, 1)));
        }

        return typeMappings;
    }

    public List<Integer> recognize(String baseForm) {
        List<Integer> resIds = new ArrayList<>();
        int numSuffixLength = 0;

        for (int i = baseForm.length() - 1; i >= 0; i --) {
            if (!Character.isDigit(baseForm.charAt(i))) { break; }
            numSuffixLength ++;
        }

        try {
            String newBaseForm = baseForm.substring(0, baseForm.length() - numSuffixLength);
            List<TypeShortNameFragmentWithScore> res = this.typeShortNameSearcher.searchTypeName(newBaseForm);

            for (TypeShortNameFragmentWithScore typeShortName: res) {
                if (this.typeFragmentCollection.getTypeShortNameIdPair().
                        getShortNameToId().containsKey(typeShortName.getName())) {
                    resIds.addAll(this.typeFragmentCollection.getTypeShortNameIdPair().
                            getShortNameToId().get(typeShortName.getName()));
                } else {
                    logger.warn("The id of type with short name <" + typeShortName.getName() +
                            "> can not be found in knowledge base");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resIds;
    }

    public List<TypeMapping> getTypeIdsAndNamesByString(String baseForm) {
        List<TypeMapping> typeMappings;

        try {
            typeMappings = typeShortNameSearcher.
                    searchTypeNameWithScore(typeFragmentCollection.getTypeShortNameIdPair(), baseForm);
            if (typeMappings.size() > 0) { return typeMappings; }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     *  Search in Yago, etc.
     *
     */
    public List<TypeMapping> getExtendTypeByString(String wordStr) {
        List<TypeMapping> typeMappings = new ArrayList<>();

        // Ignore single word type. e.g., Battle, War, Daughter
        if (wordStr.length() > 1 && wordStr.substring(1).equals(wordStr.substring(1).toLowerCase())) {
            return typeMappings;
        }

        if (typeFragmentCollection.getYagoTypes().contains(wordStr)) {
            String newTypeName = RDFLabel.YAGO_PREFIX + wordStr;
            TypeMapping typeMapping = new TypeMapping(-1, newTypeName, RDFLabel.RDF_TYPE_ID);
            typeMappings.add(typeMapping);
        } else if (extendTypeMap.containsKey(wordStr)) {
            String newTypeName = extendTypeMap.get(wordStr);
            TypeMapping typeMapping = new TypeMapping(-1, newTypeName, RDFLabel.RDF_TYPE_ID);
            typeMappings.add(typeMapping);
        }

        return typeMappings;
    }

    /**
     *  1. Priority: mayEnt(Uppercase) > mayType > mayEnt
     *  2. mayEnt=1: Constant
     *  3. mayType=1:
     *      (1)Variable, a triple will be added when evaluation. | eg, Which [books] by Kerouac were published by Viking Press?
     *      (2)Constant, it modify other words. | eg, Are tree frogs a type of [amphibian]?
     *  4、extend variable (a variable embedded triples)
     *
     */
    public void recognizeConstantVariable(HashMap<Integer, SemanticRelation> relationMap,
                                          Word[] words) {
        for (Map.Entry<Integer, SemanticRelation> entry: relationMap.entrySet()) {
            SemanticRelation relation = entry.getValue();

            processArg(words, relation, true);
            processArg(words, relation, false);

            if (!relation.getArg1().equals(relation.getPreferredSubject())) {
                relation.swapArg1Arg2();
            }
        }
    }

    void processArg(Word[] words, SemanticRelation relation, boolean forArg1) {
        Word arg = forArg1 ? relation.getArg1() : relation.getArg2();

        boolean cndForArgRDFType = arg.getPosition() >= 2 &&
                !words[arg.getPosition() - 2].getPosTag().startsWith("V") &&
                (words[arg.getPosition() - 1].getBaseForm().equals("in") || words[arg.getPosition() - 1].getBaseForm().equals("of"));
        boolean cndForArg2RDFType = !forArg1 && words[0].getBaseForm().equals("be") && arg.getPosition() >= 3 &&
                words[arg.getPosition() - 1].getBaseForm().equals("a");

        if (relation.getArg1().getMayExtendVariable()) {
            if (arg.getMayRdfType()) { arg.setMayRdfType(false); }
            if (arg.getMayEntity()) {
                if (arg.getPosition() + 1 < words.length && words[arg.getPosition() + 1].getPosTag().startsWith("N")) {
                    arg.setMayExtendVariable(false);
                    arg.setIsConstant(true);
                } else { arg.setMayEntity(false); }
            }
        } else if (arg.getMayRdfType()) {
            if (cndForArgRDFType) { processType(relation, arg); }
            if (cndForArg2RDFType) { processType(relation, arg); }

        } else if (arg.getMayEntity()) { arg.setIsConstant(true); }
    }


    private void processType(SemanticRelation relation, Word arg) {
        arg.setIsConstant(true);
        double largerScore = 1000;

        if (relation.getPredicateMappings() != null && relation.getPredicateMappings().size() > 0) {
            largerScore = relation.getPredicateMappings().get(0).getScore() * 2;
        }

        PredicateMapping newPredicate = new PredicateMapping(RDFLabel.RDF_TYPE_ID, largerScore, "[type]");
        Objects.requireNonNull(relation.getPredicateMappings()).add(0, newPredicate);
        relation.setPreferredSubject(relation.getArg2());
    }

}
