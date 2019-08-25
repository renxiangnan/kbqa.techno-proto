package lucene;

import java.util.Objects;

public class EntityNameWithScore implements Comparable<EntityNameWithScore> {
    private int id;
    private String name;
    private double score;

    public EntityNameWithScore(int id, String name, double score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getScore() { return score; }

    public void setId() { this.id = id; }
    public void setName() { this.name= name; }
    public void setScore() { this.score = score; }

    @Override
    public int hashCode(){
        return Objects.hash(id, name, score);
    }

    @Override
    public boolean equals(Object otherObj) {
        if (otherObj instanceof EntityNameWithScore) {
            EntityNameWithScore otherEnt = (EntityNameWithScore) otherObj;

            return (this.id == otherEnt.id) &&
                    (this.name.equals(otherEnt.name)) &&
                    (this.score == otherEnt.score);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(EntityNameWithScore another) {

        return Double.compare(another.score, this.score);
    }

    @Override
    public String toString() {

        return id + ":<" + name + ">\t" + score;
    }
}
