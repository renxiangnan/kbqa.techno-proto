package fragment;

import java.util.HashSet;
import java.util.Set;

public class RelationFragment extends Fragment {
    private static Set<Integer> innerTypes, outerTypes;

    public RelationFragment(int id, String inFgmtStr, String outFgmtStr) {
        this.id = id;
        innerTypes = new HashSet<>();
        outerTypes = new HashSet<>();

        /*
            Subject can not be literal, however, Object can.
            The literal is ignored in encoding.
         */
        for (String inFgmtId: inFgmtStr.split(",")) {
            if (inFgmtId.length() > 0) {
                innerTypes.add(Integer.parseInt(inFgmtId));
            }
        }

        if (outFgmtStr.equals(Fragment.nonEncodedLable.
                        substring(1, Fragment.nonEncodedLable.length() - 1))) {
            outerTypes.add(literalTypeId);
        } else {
            for (String outFgmtId: outFgmtStr.split(",")) {
                if (outFgmtId.length() > 0) {
                    outerTypes.add(Integer.parseInt(outFgmtId));
                }
            }
        }
    }

    public Set<Integer> getInnerTypes() {

        return innerTypes;
    }

    public Set<Integer> getOuterTypes() {

        return outerTypes;
    }



}
