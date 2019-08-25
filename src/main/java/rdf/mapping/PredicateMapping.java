package rdf.mapping;

public class PredicateMapping implements Comparable<PredicateMapping> {
    private int predicateId;
    private String paraphrase;
    private double score;

    public PredicateMapping(int predicateId, double score, String paraphrase) {
        this.predicateId = predicateId;
        this.paraphrase = paraphrase;
        this.score = score;
    }

    public int getPredicateId() { return predicateId; }
    public String getParaphrase() { return paraphrase; }
    public double getScore() { return score; }

    public void setPredicateId(int predicateId) { this.predicateId = predicateId; }
    public void setParaphrase(String paraphrase) { this.paraphrase = paraphrase; }
    public void setScore(double score) { this.score = score; }

    @Override
    public int compareTo(PredicateMapping other) {

        return Double.compare(other.score, this.score);
    }

    @Override
    public String toString() {

        return "<" + predicateId + " : " + paraphrase + " : "+ score + ">";
    }




}
