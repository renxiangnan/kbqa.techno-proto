package rdf.relation;

import fragment.TypeShortNameIdPair;
import lucene.EntityFragmentFields;
import rdf.RDFLabel;

public class ImplicitRelation {
    private String subject;
    private String object;
    private int predicateId = -1;
    private int subjectId = -1;
    private int objectId = -1;
    private double score;

    public ImplicitRelation(int subjectId,
                            int objectId,
                            int predicateId,
                            double score) {
        this.predicateId = predicateId;
        this.subjectId = subjectId;
        this.objectId = objectId;
        this.score = score;
    }

    public ImplicitRelation(String subject, String object,
                            int predicateId, double score,
                            EntityFragmentFields entityFragmentFields,
                            TypeShortNameIdPair typeShortNameIdPair) {
        this.subject = subject;
        this.object = object;
        this.predicateId = predicateId;
        this.score = score;

        subjectId = entityFragmentFields.getEntityIdPair().getEntityNameToId().get(subject);
        if (predicateId != RDFLabel.RDF_TYPE_ID) {
            objectId = entityFragmentFields.getEntityIdPair().getEntityNameToId().get(object);
        } else {
            objectId = typeShortNameIdPair.getShortNameToId().get(object).get(0);
        }
    }

    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
    public void setObjectId(int objectId) { this.objectId = objectId; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setObject(String object) { this.object = object; }

    @Override
    public int hashCode() {

        return  new Integer(subjectId).hashCode() ^
                new Integer(predicateId).hashCode() ^
                new Integer(objectId).hashCode();
    }

    @Override
    public boolean equals(Object that) {
        ImplicitRelation tempIR = (ImplicitRelation) that;

        if (this.predicateId == tempIR.predicateId &&
                subjectId == tempIR.subjectId &&
                objectId == tempIR.objectId) {
            return true;
        } else {
            return false;
        }
    }

}
