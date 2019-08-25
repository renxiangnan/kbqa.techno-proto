package rdf.semantic;

import nlp.datastructure.DependencyTreeNode;
import nlp.datastructure.Word;

import java.util.List;
import java.util.Map;

public class SemanticUnit {
    private Word centerWord;
    private List<DependencyTreeNode> describeNodes;
    private List<SemanticUnit> neighborUnits;
    private Map<Word, SemanticRelation> relationMap;
    private boolean isSubject = true;
    private Integer preferredType;


    public SemanticUnit(Word centerWord, boolean isSubject) {
        this.centerWord = centerWord;
        this.isSubject = isSubject;
    }

    @Override
    public int hashCode() {

        return centerWord.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof SemanticUnit) {
            return this.centerWord.
                    equals((((SemanticUnit) that).centerWord));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("<" + centerWord + ", {");
        for (SemanticUnit su: neighborUnits) {
            str.append(su.centerWord).append(",");
        }
        str.append("}>");

        return str.toString();
    }


}
