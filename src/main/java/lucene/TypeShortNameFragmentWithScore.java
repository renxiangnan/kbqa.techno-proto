package lucene;

import java.util.Objects;

public class TypeShortNameFragmentWithScore
        implements Comparable<TypeShortNameFragmentWithScore> {
    public int id;
    private String name;
    public double score;

    public void setId(int id) {
        this.id = id;
    }

    public TypeShortNameFragmentWithScore(int id, String name, double score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public TypeShortNameFragmentWithScore(String name, double score) {
        this.id = -1;
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }


    @Override
    public int hashCode() {

        return Objects.hash(id, name, score);
    }


    @Override
    public boolean equals(Object otherObj) {
        if (otherObj instanceof TypeShortNameFragmentWithScore) {
            TypeShortNameFragmentWithScore otherEnt = (TypeShortNameFragmentWithScore) otherObj;

            return (this.id == otherEnt.id) &&
                    (this.name.equals(otherEnt.name)) &&
                    (this.score == otherEnt.score);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(TypeShortNameFragmentWithScore other) {

        return Double.compare(other.score, this.score);
    }

    @Override
    public String toString() {

        return id + ":<" + name + ">\t" + score;
    }

}
