package rdf;

import nlp.datastructure.Word;
import rdf.semantic.SemanticRelation;

import static rdf.RDFLabel.TYPE_ROLE_ID;
import static rdf.RDFLabel.VAR_ROLE_ID;


public class TriplePattern implements Comparable<TriplePattern>{
    private String subject;
    private String predicate;
    private String object;
    private int subjectId = 1;
    private int objectId = -1;
    private int predicateId = -1;

    private Word subjectWord;
    private Word objectWord;

    private SemanticRelation semanticRelation;
    private double score;
    private boolean isSbjObjOrderSameWithSemRel = true;
    private boolean isSbjObjOrderPreferred = false;
    private Word typeSubjectWord;

    private TriplePattern(int subjectId, String subject,
                          int predicateId, String predicate,
                          int objectId, String object,
                          SemanticRelation semanticRelation, double score,
                          boolean isSbjObjOrderSameWithSemRel, boolean isSbjObjOrderPreferred) {
        this.subjectId = subjectId;
        this.subject = subject;
        this.predicateId = predicateId;
        this.predicate = predicate;
        this.objectId = objectId;
        this.object = object;
        this.semanticRelation = semanticRelation;
        this.score = score;
        this.isSbjObjOrderSameWithSemRel = isSbjObjOrderSameWithSemRel;
        this.isSbjObjOrderPreferred = isSbjObjOrderPreferred;
    }

    public TriplePattern(int subjectId, String subject,
                         int predicateId, String predicate,
                         int objectId, String object,
                         SemanticRelation semanticRelation, double score) {
        this.subjectId = subjectId;
        this.subject = subject;
        this.predicateId = predicateId;
        this.predicate = predicate;
        this.objectId = objectId;
        this.object = object;
        this.semanticRelation = semanticRelation;
        this.score = score;
    }

    public TriplePattern(int subjectId, String subject,
                         int predicateId, String predicate,
                         int objectId, String object,
                         SemanticRelation semanticRelation, double score,
                         boolean swapSO) {
        this.subjectId = subjectId;
        this.subject = subject;
        this.predicateId = predicateId;
        this.predicate = predicate;
        this.objectId = objectId;
        this.object = object;
        this.semanticRelation = semanticRelation;
        this.score = score;
        this.isSbjObjOrderSameWithSemRel = swapSO;
    }

    public TriplePattern(int subjectId, String subject,
                         int predicateId, String predicate,
                         int objectId, String object,
                         SemanticRelation semanticRelation, double score,
                         Word subjectWord, Word objectWord) {
        this.subjectId = subjectId;
        this.subject = subject;
        this.predicateId = predicateId;
        this.predicate = predicate;
        this.objectId = objectId;
        this.object = object;
        this.semanticRelation = semanticRelation;
        this.score = score;
        this.subjectWord = subjectWord;
        this.objectWord = objectWord;
    }

    public TriplePattern copy() {
        return new TriplePattern(
                this.subjectId, this.subject,
                this.predicateId, this.predicate,
                this.objectId, this.object,
                this.semanticRelation, this.score,
                this.isSbjObjOrderSameWithSemRel, this.isSbjObjOrderPreferred);
    }

    public TriplePattern copyReversed() {
        return new TriplePattern(
                this.objectId, this.object,
                this.predicateId, this.predicate,
                this.subjectId, this.subject,
                this.semanticRelation, this.score,
                !this.isSbjObjOrderSameWithSemRel, !this.isSbjObjOrderPreferred);
    }

    public String getSubject(){ return subject; }
    public String getPredicate() { return predicate; }
    public String getObject() { return object; }
    public int getSubjectId() { return subjectId; }
    public int getPredicateId() { return predicateId; }
    public int getObjectId() { return objectId; }
    public Word getSubjectWord() {
        if (predicateId == RDFLabel.RDF_TYPE_ID) {
            return typeSubjectWord;
        } else if (semanticRelation == null){
            return subjectWord;
        } else {
            return isSbjObjOrderSameWithSemRel ? semanticRelation.getArg1(): semanticRelation.getArg2();
        }
    }
    public Word getObjectWord() {
        if (predicateId == RDFLabel.RDF_TYPE_ID) {
            return typeSubjectWord;
        } else if (semanticRelation == null){
            return objectWord;
        } else {
            return isSbjObjOrderSameWithSemRel ? semanticRelation.getArg2(): semanticRelation.getArg1();
        }
    }
    public SemanticRelation getSemanticRelation() { return semanticRelation; }
    public double getScore() { return score; }
    public boolean isSbjObjOrderSameWithSemRel() {return isSbjObjOrderSameWithSemRel; }
    public boolean isSbjObjOrderPreferred() { return isSbjObjOrderPreferred; }
    public Word getTypeSubjectWord() { return typeSubjectWord; }

    public void setSubject(String subject){ this.subject = subject; }
    public void setPredicate(String predicate) { this.predicate = predicate; }
    public void setObject(String object) { this.object = object; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
    public void setObjectId(int objectId) { this.objectId = objectId; }
    public void setPredicateId(int predicateId) { this.predicateId = predicateId; }
    public void setSubjectWord(Word subjectWord) { this.subjectWord = subjectWord; }
    public void setObjectWord(Word objectWord) { this.objectWord = objectWord; }
    public void setSemanticRelation(SemanticRelation semanticRelation) { this.semanticRelation = semanticRelation; }
    public void setScore(double score) { this.score = score; }
    public void setIsSbjObjOrderSameWithSemRel(boolean isSbjObjOrderSameWithSemRel) {this.isSbjObjOrderSameWithSemRel = isSbjObjOrderSameWithSemRel; }
    public void setIsSbjObjOrderPreferred(boolean isSbjObjOrderPreferred) { this.isSbjObjOrderPreferred = isSbjObjOrderPreferred; }
    public void setTypeSubjectWord(Word typeSubjectWord) { this.typeSubjectWord = typeSubjectWord; }

    @Override
    public int compareTo(TriplePattern that) {
        if (this.predicateId == RDFLabel.RDF_TYPE_ID) {
            if (that.predicateId == RDFLabel.RDF_TYPE_ID) {
                return 0;
            } else {
                return -1;
            }
        }

        int cstCntThis = 0, cstCntThat = 0;
        if (!this.subject.startsWith("?")) { cstCntThis ++; }
        if (!this.object.startsWith("?")) { cstCntThis ++; }
        if (!that.object.startsWith("?")) { cstCntThat ++; }
        if (!that.object.startsWith("?")) { cstCntThat ++; }
        if (cstCntThis == cstCntThat) {
            return 0;
        } else {
            return cstCntThis > cstCntThat ? -1 : 1;
        }
    }

    @Override
    public int hashCode() {
        return new Integer(subjectId).hashCode() ^
                new Integer(objectId).hashCode() ^
                new Integer(predicateId).hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof TriplePattern) {
            return this.hashCode() == other.hashCode();
        } else return false;

    }

    public void addScore(int s) { this.score += s; }

    public String toNTFormatWithNodeId() {
        return subjectId + ":<" + subject + "> " +
                "<" + predicate+ "> " +
                objectId + ":<" + object + ">" + " : " +
                score;
    }

    public String toNTFormat() {
        return "<" + subject + "> " +
                "<" + predicate + "> " +
                "<" + object + ">";
    }

    public String toGStoreFormat() {
        StringBuilder strBuilder = new StringBuilder();
        String newSubject = this.subject;
        String newObject;

        if (newSubject.startsWith("?")) {
            strBuilder.append(newSubject).append("\t");
        } else {
            strBuilder.append("<").append(newSubject).append(">\t");
        }

        strBuilder.append("<").append(predicate).append(">\t");

        newObject = predicateId ==
                RDFLabel.RDF_TYPE_ID && object.contains("|") ?
                object.substring(0, object.indexOf('|')) : object;

        if (newObject.startsWith("?")) {
            strBuilder.append(object);
        } else {
            strBuilder.append("<").append(object).append(">");
        }

        return strBuilder.toString().replace(' ', '_');
    }

    public boolean isSubjectConstant() {
        if (predicateId == RDFLabel.RDF_TYPE_ID) {
            return !subject.startsWith("?");
        } else {
            if (semanticRelation != null) {                      // Triple from explicit relation
                if (isSbjObjOrderSameWithSemRel) {
                    return semanticRelation.getArg1().isConstant();
                } else {
                    return semanticRelation.getArg2().isConstant();
                }
            } else {                                             // Triple from implicit relation
                return subjectId != VAR_ROLE_ID && subjectId != TYPE_ROLE_ID;
            }
        }
    }

    public boolean isObjectConstant() {
        if (predicateId == RDFLabel.RDF_TYPE_ID) {
            return !object.startsWith("?");
        } else {
            if (semanticRelation != null) {
                if (isSbjObjOrderSameWithSemRel) {
                    return semanticRelation.getArg2().isConstant();
                } else {
                    return semanticRelation.getArg1().isConstant();
                }
            } else {
                return objectId != VAR_ROLE_ID && objectId != TYPE_ROLE_ID;
            }
        }
    }

    public void swapSbjObjOrder() {
        String tempNode = this.subject;
        int tempId = this.subjectId;

        this.subject = this.object;
        this.subjectId = this.objectId;
        this.object = tempNode;
        this.objectId = tempId;
        isSbjObjOrderSameWithSemRel = !this.isSbjObjOrderSameWithSemRel;
    }

}
