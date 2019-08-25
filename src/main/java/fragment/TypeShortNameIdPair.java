package fragment;

import queryanswering.QALabel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeShortNameIdPair {

    private Map<String, List<Integer>> shortNameToId;
    private Map<Integer, String> idToShortName;

    public TypeShortNameIdPair(Map<String, List<Integer>> shortNameToId,
                               Map<Integer, String> idToShortName) {
        this.shortNameToId = shortNameToId;
        this.idToShortName = idToShortName;
    }

    public Map<String, List<Integer>> getShortNameToId() {

        return shortNameToId;
    }

    public Map<Integer, String> getIdToShortName() {

        return idToShortName;
    }


    public List<Integer> getTypeForWHWords(String[] keys) {
        List<Integer> resIds = new ArrayList<>();

        for (String key: keys) {
            if (this.getShortNameToId().containsKey(key)) {
                resIds.addAll(getShortNameToId().get(key));
            }
        }

        return resIds;
    }

    public List<Integer> getTypePersonIdList() {

        return getTypeForWHWords(new String[]{QALabel.PERSON, QALabel.NATURAL_PERSON});
    }

    public List<Integer> getTypeLocationIdList() {

        return getTypeForWHWords(new String[]{QALabel.LOCATION, QALabel.PLACE});
    }

    public List<Integer> getTypeOrgIdList() {

        return getTypeForWHWords(new String[]{QALabel.ORGANISATION, QALabel.ORGANIZATION});
    }
}

