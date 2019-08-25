package rdf.relation;

import dictionary.relationmention.ParaphraseDictionary;
import dictionary.relationmention.PredicateIDSupport;
import nlp.datastructure.DependencyTree;
import nlp.datastructure.DependencyTreeNode;
import nlp.datastructure.Word;

import java.util.List;
import java.util.Map;

public class SimpleRelation {
    private Word arg1;
    private Word arg2;
    private String relationParaphrase;
    private double matchingScore;
    private Word arg1BeforeCRR;
    private Word arg2BeforeCRR;
    private Map<Integer, Double> passList;
    private Word preferredSubject;
    private char extractingMethod;

    public SimpleRelation(Word arg1, Word arg2, String relationParaphrase,
                          double matchingScore, Word arg1BeforeCRR, Word arg2BeforeCRR,
                          Map<Integer, Double> passList, Word preferredSubject) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.relationParaphrase = relationParaphrase;
        this.matchingScore = matchingScore;
        this.arg1BeforeCRR = arg1BeforeCRR;
        this.arg2BeforeCRR = arg2BeforeCRR;
        this.passList = passList;
        this.preferredSubject = preferredSubject;
        this.extractingMethod = 'R';
    }

    public Word getArg1() { return arg1; }
    public Word getArg2() { return arg2; }
    public String getRelationParaphrase() { return relationParaphrase; }
    public double getMatchingScore() { return matchingScore; }
    public Word getArg1BeforeCRR() { return arg1BeforeCRR; }
    public Word getArg2BeforeCRR() { return arg2BeforeCRR; }
    public Map<Integer, Double> getPassList() { return passList; }
    public Word getPreferredSubject() { return preferredSubject; }
    public char getExtractingMethod() { return extractingMethod; }

    public void setPassList(String pattern,
                            double matchingScore,
                            boolean[] matchedFlg,
                            ParaphraseDictionary dictionary) {
        List<PredicateIDSupport> supportInstances = dictionary.getPredicateNLPattern().get(pattern);
        for (PredicateIDSupport predIdSup: supportInstances) {
            double accumSelectivity = 0;
            for (int i = 0; i < matchedFlg.length; i ++) {
                if (matchedFlg[i]) {
                    accumSelectivity += predIdSup.wordSelectivity[i];
                }
            }

            accumSelectivity *= matchingScore * predIdSup.support;
            int predId = predIdSup.predicateId;
            if (dictionary.getPredicateIdSet().contains(predId)) {
                accumSelectivity *= 1.5;
            }

            if (!passList.containsKey(predId)) {
                passList.put(predId, accumSelectivity);
            } else if (accumSelectivity > passList.get(predId)) {
                passList.put(predId, accumSelectivity);
            }
        }
    }

    public void setPreferredSubjectOrder(DependencyTree tree) {
        DependencyTreeNode node1 = tree.getNodeByIndex(this.arg1.getPosition()).getNounRootTopNode();
        DependencyTreeNode node2 = tree.getNodeByIndex(this.arg2.getPosition()).getNounRootTopNode();
        if (node1.getParent() != null &&
                node2.getParent().getWord().getBaseForm().equals("of") &&
                node1.getDependecyParentToChild().equals("pobj")) {
            this.preferredSubject = this.arg1;
        } else if (node2.getParent() != null &&
                node2.getParent().getWord().getBaseForm().equals("of") &&
                node2.getDependecyParentToChild().equals("pobj")) {
            this.preferredSubject = this.arg2;
        }
    }

    public SimpleRelation copy() {
        return new SimpleRelation(
                this.arg1, this.arg2, this.relationParaphrase, this.matchingScore,
                this.arg1BeforeCRR, this.arg2BeforeCRR, passList, preferredSubject);
    }

    @Override
    public String toString() {
        return arg1.getOriginalForm() + "," + arg2.getOriginalForm() + "," +
                relationParaphrase + "," + matchingScore +
                "[" + extractingMethod + "]";
    }

    public int getHashCodeForKey() { return arg1.hashCode() ^ arg2.hashCode(); }

}
