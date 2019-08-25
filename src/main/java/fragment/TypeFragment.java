package fragment;

import java.util.HashSet;
import java.util.Set;

public class TypeFragment extends Fragment {
    private Set<Integer> innerEdges, outerEdges, entitySet;

    private Set<Integer> initEdgesAndEntSet(String id,
                                            Set<Integer> set) {
        String[] ids = id.split(",");
        for (String fgmtId: ids) {
            if (fgmtId.length() > 0) {
                set.add(Integer.parseInt(fgmtId));
            }
        }
        return set;
    }

    public TypeFragment(int id, String typeFgmtStr) {

        this.id = id;
        innerEdges = new HashSet<>();
        outerEdges = new HashSet<>();
        entitySet = new HashSet<>();

        String[] splittedFgmt = typeFgmtStr.
                replace('|', '#').
                split("#");

        if (splittedFgmt.length > 0) {
            initEdgesAndEntSet(splittedFgmt[0], innerEdges);
        } else {
            innerEdges.add(NO_RELATION);
        }

        if (splittedFgmt.length > 1 && splittedFgmt[1].length() > 0) {
            initEdgesAndEntSet(splittedFgmt[1], outerEdges);
        } else {
            outerEdges.add(NO_RELATION);
        }

        if (splittedFgmt.length > 2 && splittedFgmt[2].length() > 0) {
            initEdgesAndEntSet(splittedFgmt[2], entitySet);
        }
    }

    public Set<Integer> getInnerEdges() {

        return innerEdges;
    }

    public Set<Integer> getOuterEdges() {

        return outerEdges;
    }

    public Set<Integer> getEntitySet() {

        return entitySet;
    }

}
