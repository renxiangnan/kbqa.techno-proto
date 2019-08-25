package lucene;

import lucene.io.FragmentLoader;

import java.util.Map;


/**
 * Entity id <=> name dictionary
 */
public class EntityFragmentFields {
    private EntityIdPair entityIdPair;             // e.g., <Inspiration_Trust>	-> 2527
    private Map<Integer, String> entityFragment;   // e.g., 851	-> 1130041:5844;,||5844,||

    private EntityIdPair buildEntityNameToId(String entityNameToIdPath) {

        return FragmentLoader.loadEntityIdPair(entityNameToIdPath);
    }

    private Map<Integer, String> buildEntityFragment(String fragmentPath) {

        return FragmentLoader.loadEntityFragment(fragmentPath);
    }

    public EntityFragmentFields(String entityIdPairPath, String entityFragmentPath) {

        entityIdPair = buildEntityNameToId(entityIdPairPath);
        entityFragment = buildEntityFragment(entityFragmentPath);
    }

    public EntityIdPair getEntityIdPair() {

        return entityIdPair;
    }

    public Map<Integer, String> getEntityFragment() {

        return entityFragment;
    }

}
