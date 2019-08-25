package fragment.collection;

import fragment.TypeFragment;
import fragment.TypeShortNameIdPair;
import fragment.io.TypeFragmentIOUtils;

import java.util.Map;
import java.util.Set;

public class TypeFragmentCollection {
    private Map<Integer, TypeFragment> typeFragmentMap;
    private TypeShortNameIdPair typeShortNameIdPair;
    private Set<String> yagoTypes;

    public TypeFragmentCollection (String typeFragmentPath,
                                   String basicTypeIdPath,
                                   String yaoTypePath) {
        typeFragmentMap = TypeFragmentIOUtils.loadTypeFragmentMap(typeFragmentPath);
        typeShortNameIdPair = TypeFragmentIOUtils.loadTypeShortNameIdPair(basicTypeIdPath);
        yagoTypes = TypeFragmentIOUtils.RemoveStopTypesFromYago(yaoTypePath);
    }

    public Map<Integer, TypeFragment> getTypeFragmentMap() {

        return typeFragmentMap;
    }

    public TypeShortNameIdPair getTypeShortNameIdPair() {

        return typeShortNameIdPair;
    }

    public Set<String> getYagoTypes() {

        return yagoTypes;
    }

}
