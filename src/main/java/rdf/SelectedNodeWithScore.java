package rdf;

import java.util.List;

public class SelectedNodeWithScore implements Comparable<SelectedNodeWithScore> {
    private List<Integer> selectedNodeIds;
    private double score;

    public SelectedNodeWithScore(List<Integer> selectedNodeIds,
                                 double score) {
        this.selectedNodeIds = selectedNodeIds;
        this.score = score;
    }

    @Override
    public int compareTo(SelectedNodeWithScore other) {
        double diff = this.score - other.score;
        if (diff > 0) return -1;
        else if (diff < 0) return 1;
        else return 0;
    }
}
