package dictionary.relationmention;

import java.util.Map;

public final class PredicateIdPair {
    private Map<String, Integer> predicateToId;
    private Map<Integer, String> idToPredicate;

    public PredicateIdPair(Map<String, Integer> predicateToId,
                           Map<Integer, String> idToPredicate) {
        this.predicateToId = predicateToId;
        this.idToPredicate = idToPredicate;
    }

    public Map<String, Integer> getPredicateToId() {
        return predicateToId;
    }

    public Map<Integer, String> getIdToPredicate() {
        return idToPredicate;
    }

    public String getPredicateById(int predicateId) {
        return  idToPredicate.get(predicateId);
    }

    public void showPredicateToId() {
        for (Map.Entry<String, Integer> predIdPair: predicateToId.entrySet()) {
            System.out.println(predIdPair.getKey() + "\t" +
                            predIdPair.getValue());
        }
    }

    public void showIdToPredicate() {
        for (Map.Entry<Integer, String> idPredPair: idToPredicate.entrySet()) {
            System.out.println(idPredPair.getKey() + "\t" +
                    idPredPair.getValue());
        }
    }

}
