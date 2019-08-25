package rdf;

public class Match {
    private String[][] answers;
    private int answersNum;

    public Match(int m, int n, int answersNum) {
        this.answers = new String[m][n];
        this.answersNum = answersNum;
    }

}
