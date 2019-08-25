package lucene;

import java.util.Map;

public class EntityIdPair {
    private Map<String, Integer> entityNameToId;
    private Map<Integer, String> entityIdToName;

    public EntityIdPair(Map<String, Integer> entityNameToId,
                        Map<Integer, String> entityIdToName){

        this.entityNameToId = entityNameToId;
        this.entityIdToName = entityIdToName;
    }

    public Map<String, Integer> getEntityNameToId() {

        return entityNameToId;
    }

    public Map<Integer, String> getEntityIdToName() {

        return entityIdToName;
    }
}
