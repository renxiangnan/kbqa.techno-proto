package nlp.datastructure;

public class MergedWord extends AbstractWord implements Comparable<MergedWord> {
    private int start, end;
    private String name;

    public MergedWord(int start, int end, String name) {
        this.start = start;
        this.end = end;
        this.name = name;
    }

    @Override
    public int compareTo(MergedWord o) {
        int lenDiff = (this.end-this.start) - (o.end-o.start);

        if (lenDiff > 0) return -1;
        else if (lenDiff < 0) return 1;
        return 0;
    }


}
