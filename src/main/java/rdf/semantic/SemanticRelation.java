package rdf.semantic;

import nlp.datastructure.Word;
import rdf.mapping.PredicateMapping;
import rdf.relation.SimpleRelation;

import java.util.ArrayList;
import java.util.List;

/**
 *  <rel, arg1, arg2> represents a semantic relation.
 *  where rel is a relation mention, arg1, arg2 are node paraphrases
 */
public class SemanticRelation {
    private Word arg1;
    private Word arg2;
    private String longestRelPara;              // longest match
    private double longestMatchingScore;        // longest match score
    private int arg1SuffixId;
    private int arg2SuffixId;
    private Word arg1BeforeCRR;
    private Word arg2BeforeCRR;
    private List<PredicateMapping> predicateMappings;
    private char extractingMethod; // S: StanfordParser; M: MaltParser; N: N-gram; R: rules
    private SemanticRelation dependencyRelation;
    private Word preferredSubject;
    private boolean isSteadyEdge = true;

    public Word getArg1() { return arg1; }
    public Word getArg2() { return arg2; }
    public String getLongestRelPara() { return longestRelPara; }
    public double getLongestMatchingScore() { return longestMatchingScore; }
    public List<PredicateMapping> getPredicateMappings() { return predicateMappings; }
    public SemanticRelation getDependencyRelation() { return dependencyRelation; }
    public Word getPreferredSubject() { return preferredSubject; }
    public boolean isSteadyEdge() { return isSteadyEdge; }

    public void setArg1(Word arg1) { this.arg1 = arg1; }
    public void setArg2(Word arg2) { this.arg2 = arg2; }
    public void setLongestRelPara(String longestRelPara) { this.longestRelPara = longestRelPara; }
    public void setLongestMatchingScore(double longestMatchingScore) { this.longestMatchingScore = longestMatchingScore; }
    public void setPredicateMappings(List<PredicateMapping> predicateMappings) { this.predicateMappings = predicateMappings; }
    public void setDependencyRelation(SemanticRelation dependencyRelation) { this.dependencyRelation = dependencyRelation; }
    public void setPreferredSubject(Word preferredSubject) { this.preferredSubject = preferredSubject; }
    public void setIsSteadyEdge(boolean isSteadyEdge) { this.isSteadyEdge = isSteadyEdge; }

    // TODO, understand the meaning of this constructor
    public SemanticRelation(SimpleRelation simpleRelation) {
        predicateMappings = new ArrayList<>();



        if (simpleRelation.getPreferredSubject() == null) {
            if (simpleRelation.getArg1().compareTo(simpleRelation.getArg2()) < 0) {
                this.arg1 = simpleRelation.getArg1();
                this.arg2 = simpleRelation.getArg2();
                this.arg1BeforeCRR = simpleRelation.getArg1BeforeCRR();
                this.arg2BeforeCRR = simpleRelation.getArg2BeforeCRR();
            } else {
                this.arg1 = simpleRelation.getArg2();
                this.arg2 = simpleRelation.getArg1();
                this.arg1BeforeCRR = simpleRelation.getArg2BeforeCRR();
                this.arg2BeforeCRR = simpleRelation.getArg1BeforeCRR();
            }
        } else {
            if (simpleRelation.getArg1().equals(simpleRelation.getPreferredSubject())) {
                this.arg1 = simpleRelation.getArg1();
                this.arg2 = simpleRelation.getArg2();
                this.arg1BeforeCRR = simpleRelation.getArg1BeforeCRR();
                this.arg2BeforeCRR = simpleRelation.getArg2BeforeCRR();
                this.preferredSubject = simpleRelation.getPreferredSubject();
            } else {
                this.arg1 = simpleRelation.getArg2();
                this.arg2 = simpleRelation.getArg1();
                this.arg1BeforeCRR = simpleRelation.getArg2BeforeCRR();
                this.arg2BeforeCRR = simpleRelation.getArg1BeforeCRR();
                this.preferredSubject = simpleRelation.getPreferredSubject();
            }
        }
        this.extractingMethod = simpleRelation.getExtractingMethod();
    }

    public void swapArg1Arg2() {
        Word tempWord = arg1;
        Word tempWordBeforeCRR = arg1BeforeCRR;
        int tempSuffixId = arg1SuffixId;
        boolean tempIsConstant = arg1.isConstant();

        arg1 = arg2;
        arg2 = tempWord;
        arg1BeforeCRR = arg2BeforeCRR;
        arg2BeforeCRR = tempWordBeforeCRR;
        arg1SuffixId = arg2SuffixId;
        arg2SuffixId = tempSuffixId;
        arg1.setIsConstant(arg2.isConstant());
        arg2.setIsConstant(tempIsConstant);
    }

    @Override
    public int hashCode() {

        return arg1.hashCode() ^ arg2.hashCode() + arg1SuffixId + arg2SuffixId;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof SemanticRelation) {
            SemanticRelation otherRel = (SemanticRelation) that;
            return this.arg1.equals(otherRel.arg1) &&
                    this.arg2.equals(otherRel.arg2) &&
                    this.arg1SuffixId == otherRel.arg2SuffixId &&
                    this.arg2SuffixId == otherRel.arg2SuffixId &&
                    this.longestRelPara.equals(otherRel.getLongestRelPara()) &&
                    this.longestMatchingScore == otherRel.longestMatchingScore;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return arg1.getOriginalForm() + "," +
                arg2.getOriginalForm() + "," +
                longestRelPara + "," +
                longestMatchingScore +
                "[" + extractingMethod + "]";
    }



}
