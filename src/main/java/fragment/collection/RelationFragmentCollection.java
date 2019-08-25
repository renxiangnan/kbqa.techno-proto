package fragment.collection;

import fragment.RelationShortNameIdPair;
import fragment.io.RelationFragmentIOUtils;
import common.Tuple2;

import java.util.Set;

public class RelationFragmentCollection {

    private RelationShortNameIdPair relationShortNameIdPair;
    private Set<Integer> literalRelationSet;

    public RelationFragmentCollection(String predicateFragmentPath,
                                      String predicateIdPath) {
        Tuple2<RelationShortNameIdPair, Set<Integer>> predFromFile =
                RelationFragmentIOUtils.
                        loadRelationShortNameIdPair(predicateFragmentPath, predicateIdPath);

        this.relationShortNameIdPair = predFromFile.s;
        this.literalRelationSet = predFromFile.t;
    }

    public RelationShortNameIdPair getRelationShortNameIdPair() {
        return relationShortNameIdPair;
    }

    public Set<Integer> getLiteralRelationSet() {

        return literalRelationSet;
    }

    public boolean isLiteral(int predicateId) {

        return literalRelationSet.contains(predicateId);
    }

}
