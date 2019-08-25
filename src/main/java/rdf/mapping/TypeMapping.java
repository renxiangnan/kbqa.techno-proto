package rdf.mapping;

import rdf.RDFLabel;

public class TypeMapping implements Comparable<TypeMapping> {
    private int typeId;
    private String typeName;
    private double score;

    public int preferredRelation = RDFLabel.RDF_TYPE_ID;

    public TypeMapping(int typeId, String typeName, double score) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.score = score;
    }

    public TypeMapping(int typeId, String typeName, double score, int preferredRelation) {
        this.typeId = typeId;
        this.typeName = typeName.replace("_", "");
        this.score = score;
        this.preferredRelation = preferredRelation;
    }

    public int getTypeId() { return typeId; }

    @Override
    public int compareTo(TypeMapping other) {
        double diff = this.score - other.score;
        if (diff > 0) return -1;
        else if (diff < 0) return 1;
        else return 0;
    }

    @Override
    public int hashCode() {

        return new Integer(typeId).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TypeMapping) {
            TypeMapping other = (TypeMapping) object;

            return this.typeName.equals(other.typeName) &&
                    this.typeId == other.typeId;
        } else {
            return false;
        }

    }

    @Override
    public String toString() {

        return typeName + "(" + score + ")";
    }


}
