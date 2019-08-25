package fragment.collection;

import fragment.EntityFragment;
import lucene.EntityFragmentFields;

import java.util.HashMap;
import java.util.Map;

public class EntityFragmentCollection {
    private Map<Integer, EntityFragment> entityFragmentMap;

    public EntityFragmentCollection() {

        entityFragmentMap = new HashMap<>();
    }

    public Map<Integer, EntityFragment> getEntityFragmentMap() { return entityFragmentMap; }

    public EntityFragment getEntityFragmentById(int id, EntityFragmentFields entFgmtFields) {
        if (!entityFragmentMap.containsKey(id)) {
            entityFragmentMap.put(id, getEntityFragmentByEntityId(id, entFgmtFields));
        }

        return entityFragmentMap.get(id);
    }

    private EntityFragment getEntityFragmentByEntityId(Integer entityId,
                                                       EntityFragmentFields entFgmtFields) {
        String entFgmt = "";

        try {
            entFgmt = entFgmtFields.getEntityFragment().get(entityId);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return new EntityFragment(entityId, entFgmt);
    }

}
