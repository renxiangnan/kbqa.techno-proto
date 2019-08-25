package lucene.io;

import lucene.EntityIdPair;
import common.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class FragmentLoader {

    private FragmentLoader() {}

    public static EntityIdPair loadEntityIdPair(String entityNameToIdPath) {
        Map<String, Integer> entityNameToId = new HashMap<>();
        Map<Integer, String> entityIdToName = new HashMap<>();
        File entityNameToIdFile = new File(entityNameToIdPath);

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(entityNameToIdFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);

                // Remove "<" and ">"
                String entitName = splitted[0].substring(1, splitted[0].length() - 1);
                int entitId = Integer.parseInt(splitted[1]);

                entityNameToId.put(entitName, entitId);
                entityIdToName.put(entitId, entitName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new EntityIdPair(entityNameToId, entityIdToName);
    }

    public static Map<Integer, String> loadEntityFragment(String fragmentPath) {
        File fragmentFile = new File(fragmentPath);
        Map<Integer, String> entityFragment = new HashMap<>();

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(fragmentFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);

                int entityId = Integer.parseInt(splitted[0]);
                String fragmentName = splitted[1];
                entityFragment.put(entityId, fragmentName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entityFragment;
    }

}
