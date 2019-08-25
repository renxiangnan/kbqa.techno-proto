package fragment;

import java.util.List;
import java.util.Map;

public class RelationShortNameIdPair {

    private Map<Integer, List<RelationFragment>> relIdToShortName;
    private Map<String, List<Integer>> relShortNameToId;

    public RelationShortNameIdPair(Map<Integer, List<RelationFragment>> relIdToShortName,
                                   Map<String, List<Integer>> relShortNameToId) {

        this.relIdToShortName = relIdToShortName;
        this.relShortNameToId = relShortNameToId;
    }

    public Map<Integer, List<RelationFragment>> getRelIdToShortName() {

        return relIdToShortName;
    }

    public Map<String, List<Integer>> getRelShortNameToId() {

        return relShortNameToId;
    }


}
