package rdf.mapping;

public class EntityMapping implements Comparable<EntityMapping> {
    private int entityId;
    private String entityName;
    private double score;       // Confidence probability

    public EntityMapping(int entityId, String entityName, double score) {
        this.entityId = entityId;
        this.entityName = entityName;
        this.score = entityName.startsWith("?") ? score * 0.5 : score;
    }

    @Override
    public int compareTo(EntityMapping other) {
        double diff = this.score - other.score;

        /*
            Tier in descending order,
            do not return other.score - this.score, since
            the score is always between [0, 1].
         */
        if (diff > 0) return -1;
        else if (diff < 0) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return "[entityId:," + entityId + "]," +
                "[entityName:," + entityName + "]," +
                "[score:," + score + "]";
    }


    @Override
    public int hashCode() {
        return new Integer(this.entityId).hashCode();
    }

    @Override
    public boolean equals(Object otherObj) {
        if (otherObj instanceof EntityMapping) {
            return this.hashCode() == otherObj.hashCode();
        } else {
            return false;
        }
    }

    public int getEntityId() {

        return entityId;
    }

    public String getEntityName() {

        return entityName;
    }

    public double getScore() {

        return score;
    }


}
