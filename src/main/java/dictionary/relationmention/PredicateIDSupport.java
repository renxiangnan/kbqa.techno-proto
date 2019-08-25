package dictionary.relationmention;

import java.util.Arrays;

public class PredicateIDSupport implements Comparable<PredicateIDSupport>{
    public int predicateId;
    public int support;
    public double[] wordSelectivity;

    public PredicateIDSupport(int predicateId, int support, double[] wordSelectivity) {
        this.predicateId = predicateId;
        this.support = support;
        this.wordSelectivity = wordSelectivity;
    }

    @Override
    public int compareTo(PredicateIDSupport another) {
        return another.support - this.support;
    }

    @Override
    public String toString() {
        return predicateId + ", " + support + ", " + Arrays.toString(wordSelectivity) ;
    }

    public static double[] initSelectivies(int size) {
        double[] selectivies = new double[size];
        Arrays.fill(selectivies, 1.0);
        return selectivies;
    }

}
